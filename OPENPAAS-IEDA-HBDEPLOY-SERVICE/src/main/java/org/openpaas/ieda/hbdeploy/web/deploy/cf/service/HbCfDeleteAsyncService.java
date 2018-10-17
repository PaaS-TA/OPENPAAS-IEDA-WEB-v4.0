package org.openpaas.ieda.hbdeploy.web.deploy.cf.service;

import java.io.File;
import java.security.Principal;
import java.util.Arrays;
import java.util.Locale;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.openpaas.ieda.common.api.LocalDirectoryConfiguration;
import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.deploy.api.director.utility.DirectorRestHelper;
import org.openpaas.ieda.hbdeploy.api.director.utility.HbDirectorRestHelper;
import org.openpaas.ieda.hbdeploy.web.config.setting.dao.HbDirectorConfigDAO;
import org.openpaas.ieda.hbdeploy.web.config.setting.dao.HbDirectorConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
@Service
public class HbCfDeleteAsyncService {
    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private HbDirectorConfigDAO directorConfigDao;
    @Autowired private HbCfDAO cfDao;
    @Autowired private MessageSource message;
    private final static String SEPARATOR = System.getProperty("file.separator");
    private final static String LOCK_DIR = LocalDirectoryConfiguration.getLockDir()+SEPARATOR;
    private final static  String CF_MESSAGE_ENDPOINT =  "/deploy/hbCf/delete/logs";
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : CF 플랫폼 삭제 요청
     * @title : deleteDeploy
     * @return : void
    *****************************************************************/
    public void deleteDeploy(HbCfDTO dto, String platform, Principal principal) {
        String errorMsg = message.getMessage("common.internalServerError.message", null, Locale.KOREA);
        String deploymentName = "";
        
        HbCfVO vo  = cfDao.selectHbCfInfoById(Integer.parseInt(dto.getId()));
        deploymentName = vo != null ?vo.getDefaultConfigVO().getDeploymentName() : "";
            
        if ( StringUtils.isEmpty(deploymentName) ) {
            throw new CommonException(message.getMessage("common.badRequest.exception.code", null, Locale.KOREA), 
                    message.getMessage("common.badRequest.message", null, Locale.KOREA), HttpStatus.BAD_REQUEST);
        }
        String deleteStatusName = message.getMessage("common.deploy.status.deleting", null, Locale.KOREA);
        if ( vo != null ) {
            vo.setDeployStatus(deleteStatusName);
            vo.setUpdateUserId(principal.getName());
            saveDeployStatus(vo);
        }
        
        try {
            HbDirectorConfigVO defaultDirector = directorConfigDao.selectHbDirectorConfigBySeq(vo.getDefaultConfigVO().getDirectorId());
            HttpClient httpClient = DirectorRestHelper.getHttpClient(defaultDirector.getDirectorPort());
            
            DeleteMethod deleteMethod = new DeleteMethod(DirectorRestHelper.getDeleteDeploymentURI(defaultDirector.getDirectorUrl(), defaultDirector.getDirectorPort(), deploymentName));
            deleteMethod = (DeleteMethod)DirectorRestHelper.setAuthorization(defaultDirector.getUserId(), defaultDirector.getUserPassword(), (HttpMethodBase)deleteMethod);
        
            int statusCode = httpClient.executeMethod(deleteMethod);
            if( statusCode == HttpStatus.MOVED_PERMANENTLY.value() || statusCode == HttpStatus.MOVED_TEMPORARILY.value() ) {
                Header location = deleteMethod.getResponseHeader("Location");
                String taskId = DirectorRestHelper.getTaskId(location.getValue());
                HbDirectorRestHelper.trackToTask(defaultDirector, messagingTemplate, CF_MESSAGE_ENDPOINT, httpClient, taskId, "event", principal.getName());
            }else {
                DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, CF_MESSAGE_ENDPOINT, "done", Arrays.asList("CF 삭제가 완료되었습니다."));
            }
            deleteCfInfo(vo);
        } catch(RuntimeException e){
            DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, CF_MESSAGE_ENDPOINT, "error", Arrays.asList(errorMsg));
        } catch ( Exception e) {
            DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, CF_MESSAGE_ENDPOINT, "error", Arrays.asList(errorMsg));
        } finally {
            File lockFile = new File(LOCK_DIR + "hybird_cf.lock");
            if(lockFile.exists()){
                lockFile.delete();
            }
		}
    }
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : CF 정보 삭제
     * @title : deleteCfInfo
     * @return : void
    *****************************************************************/
    @Transactional
    public void deleteCfInfo( HbCfVO vo ){
        if ( vo != null ) {
            cfDao.deleteHbCfInfo(vo.getId());
        }
    }
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : CF 배포 삭제 상태 저장
     * @title : saveDeployStatus
     * @return : CfVO
    *****************************************************************/
    public HbCfVO saveDeployStatus(HbCfVO vo) {
        cfDao.updateHbCfInfo(vo);
        return vo;
    }

    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : 비동기식으로 deleteDeploy 호출
     * @title : deleteDeployAsync
     * @return : void
    *****************************************************************/
    @Async
    public void deleteDeployAsync(HbCfDTO dto, String platform, Principal principal) {
        deleteDeploy(dto, platform, principal);
    }    
}
