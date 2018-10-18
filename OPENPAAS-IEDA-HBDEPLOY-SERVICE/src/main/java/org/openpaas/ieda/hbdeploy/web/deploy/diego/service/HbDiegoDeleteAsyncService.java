package org.openpaas.ieda.hbdeploy.web.deploy.diego.service;

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
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class HbDiegoDeleteAsyncService {
    
    @Autowired private HbDiegoDAO diegoDao;
    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private HbDirectorConfigDAO directorConfigDao;
    @Autowired private MessageSource message;
    private static final String MESSAGE_ENDPOINT = "/deploy/hbDiego/delete/logs";
    private final static String SEPARATOR = System.getProperty("file.separator");
    private final static String LOCK_DIR = LocalDirectoryConfiguration.getLockDir()+SEPARATOR;
    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 이종 Diego 플랫폼 삭제 요청
     * @title : deleteDeploy
     * @return : void
    ***************************************************/
    public void deleteDeploy(HbDiegoDTO dto, String platform, Principal principal) {
        String deploymentName = null;
        HbDiegoVO vo = diegoDao.selectHbDiegoInfoById(Integer.parseInt(dto.getId()));
        
        if ( vo != null ) {
            deploymentName = vo.getDefaultConfigVO().getDeploymentName();
        }
            
        if ( StringUtils.isEmpty(deploymentName) ) {
            throw new CommonException("notfound.diegodelete.exception", "배포정보가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        
        HbDirectorConfigVO defaultDirector = directorConfigDao.selectHbDirectorConfigBySeq(Integer.parseInt(vo.getDefaultConfigVO().getDirectorId()));
        if ( vo != null ) {
            vo.setDeployStatus(message.getMessage("common.deploy.status.deleting", null, Locale.KOREA));
            vo.setUpdateUserId(principal.getName());
            saveDeployStatus(vo);
        }
        
        try {
            HttpClient httpClient = DirectorRestHelper.getHttpClient(defaultDirector.getDirectorPort());
            
            DeleteMethod deleteMethod = new DeleteMethod(DirectorRestHelper.getDeleteDeploymentURI(defaultDirector.getDirectorUrl(), defaultDirector.getDirectorPort(), deploymentName));
            deleteMethod = (DeleteMethod)DirectorRestHelper.setAuthorization(defaultDirector.getUserId(), defaultDirector.getUserPassword(), (HttpMethodBase)deleteMethod);
            int statusCode = httpClient.executeMethod(deleteMethod);
            if ( statusCode == HttpStatus.MOVED_PERMANENTLY.value() || statusCode == HttpStatus.MOVED_TEMPORARILY.value() ) {
                Header location = deleteMethod.getResponseHeader("Location");
                String taskId = DirectorRestHelper.getTaskId(location.getValue());
                HbDirectorRestHelper.trackToTask(defaultDirector, messagingTemplate, MESSAGE_ENDPOINT, httpClient, taskId, "event", principal.getName());
                deleteDiegoInfo(vo);
                
            } else {
                deleteDiegoInfo(vo);
                DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, MESSAGE_ENDPOINT, "done", Arrays.asList("Diego 삭제가 완료되었습니다."));
            }
        }catch(RuntimeException e){
            vo.setDeployStatus(message.getMessage("common.deploy.status.error", null, Locale.KOREA));
            vo.setUpdateUserId(principal.getName());
            saveDeployStatus(vo);
            DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, MESSAGE_ENDPOINT, "error", Arrays.asList("배포삭제 중 Exception이 발생하였습니다."));
        }catch ( Exception e) {
            vo.setDeployStatus(message.getMessage("common.deploy.status.error", null, Locale.KOREA));
            vo.setUpdateUserId(principal.getName());
            saveDeployStatus(vo);
            DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, MESSAGE_ENDPOINT, "error", Arrays.asList("배포삭제 중 Exception이 발생하였습니다."));
        } finally {
            File lockFile = new File(LOCK_DIR + "hybird_diego.lock");
            if(lockFile.exists()){
                lockFile.delete();
            }
        }
    }
    
    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : Diego 정보 삭제
     * @title : deleteDiegoInfo
     * @return : void
    ***************************************************/
    @Transactional
    public void deleteDiegoInfo( HbDiegoVO vo ){
        if ( vo != null ) {
            diegoDao.deleteHbDiegoInfo(vo.getId());
        }
    }
    
    
    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : Diego 배포 상태 저장
     * @title : saveDeployStatus
     * @return : DiegoVO
    ***************************************************/
    public HbDiegoVO saveDeployStatus(HbDiegoVO diegoVo) {
        if ( diegoVo == null ) {
            return null;
        }
        diegoDao.updateHbDiegoInfo(diegoVo);
        return diegoVo;
    }
    
    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : deleteDeploy 메소드 호출
     * @title : deleteDeployAsync
     * @return : void
    ***************************************************/
    @Async
    public void deleteDeployAsync(HbDiegoDTO dto, String platform, Principal principal) {
        deleteDeploy(dto, platform, principal);
    }
    
}
