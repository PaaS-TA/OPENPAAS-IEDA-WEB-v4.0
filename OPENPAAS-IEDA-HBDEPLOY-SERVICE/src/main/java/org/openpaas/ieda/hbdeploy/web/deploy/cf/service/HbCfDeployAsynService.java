package org.openpaas.ieda.hbdeploy.web.deploy.cf.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.Arrays;
import java.util.Locale;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
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
import org.springframework.util.StringUtils;

@Service
public class HbCfDeployAsynService {
    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private HbDirectorConfigDAO directorConfigDao;
    @Autowired private HbCfDAO cfDao;
    @Autowired MessageSource message;
    
    private final static String SEPARATOR = System.getProperty("file.separator");
    private final static String DEPLOYMENT_DIR = LocalDirectoryConfiguration.getDeploymentDir();
    private final static String LOCK_DIR = LocalDirectoryConfiguration.getLockDir()+SEPARATOR;
    private final static String MESSAGE_END_POINT =  "/deploy/hbCf/install/logs";
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : CF 플랫폼 설치 요청
     * @title : deploy
     * @return : void
    *****************************************************************/
    public void deploy(HbCfDTO dto, Principal principal, String platform) {
        HbCfVO vo = cfDao.selectHbCfInfoById(Integer.parseInt(dto.getId()));
        String deploymentFileName = vo != null ? vo.getDeploymentFile() : "";
        
        if ( StringUtils.isEmpty(deploymentFileName) ) {
            throw new CommonException(message.getMessage("common.badRequest.exception.code", null, Locale.KOREA), 
                    message.getMessage("common.badRequest.message", null, Locale.KOREA), HttpStatus.BAD_REQUEST);
        }
        String deployFile = DEPLOYMENT_DIR + SEPARATOR + deploymentFileName; 
        String errorMessage = message.getMessage("common.internalServerError.message", null, Locale.KOREA);
        
        if ( vo != null ) {
            String deployStatus = message.getMessage("common.deploy.status.processing", null, Locale.KOREA);
            vo.setDeployStatus(deployStatus);
            vo.setUpdateUserId(principal.getName());
            saveDeployStatus(vo);
        }
        
        String status = "";
        String temp = "";
        String taskId = "";
        
        BufferedReader br = null;
        InputStreamReader isr = null;
        FileInputStream fis = null;
        StringBuffer content = new StringBuffer();
        try {
            HbDirectorConfigVO defaultDirector = directorConfigDao.selectHbDirectorConfigBySeq(vo.getDefaultConfigVO().getDirectorId());
            HttpClient httpClient = DirectorRestHelper.getHttpClient(defaultDirector.getDirectorPort());
            PostMethod postMethod = new PostMethod(DirectorRestHelper.getDeployURI(defaultDirector.getDirectorUrl(), defaultDirector.getDirectorPort()));
            postMethod = (PostMethod)DirectorRestHelper.setAuthorization(defaultDirector.getUserId(), defaultDirector.getUserPassword(), (HttpMethodBase)postMethod);
            postMethod.setRequestHeader("Content-Type", "text/yaml");
            
            fis = new FileInputStream(deployFile);
            isr = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(isr);
            
            while ( (temp=br.readLine()) != null) {
                content.append(temp).append("\n");
            }
            postMethod.setRequestEntity(new StringRequestEntity(content.toString(), "text/yaml", "UTF-8"));
            int statusCode = httpClient.executeMethod(postMethod);
            
            if ( statusCode == HttpStatus.MOVED_PERMANENTLY.value() || statusCode == HttpStatus.MOVED_TEMPORARILY.value() ) {
                Header location = postMethod.getResponseHeader("Location");
                taskId = DirectorRestHelper.getTaskId(location.getValue());
                
                status = HbDirectorRestHelper.trackToTask(defaultDirector, messagingTemplate, MESSAGE_END_POINT, httpClient, taskId, "event", principal.getName());
                
            } else {
            	status = "error";
                DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, MESSAGE_END_POINT, "error", Arrays.asList("CF 설치 중 에러가 발생 했습니다.<br> 설정 및 배포 파일을 확인 해주세요."));
            }
        }catch(IOException e){
            status = "error";
            DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, MESSAGE_END_POINT, "error", Arrays.asList(errorMessage));
        }catch (RuntimeException e) {
            status = "error";
            DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, MESSAGE_END_POINT, "error", Arrays.asList("CF 설치 중 에러가 발생 했습니다.<br> 설정을 확인 해주세요."));
        }catch ( Exception e) {
            status = "error";
            DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, MESSAGE_END_POINT, "error", Arrays.asList(errorMessage));
        } finally {
            try {
                if ( br != null ) {
                    br.close();
                }
                if ( fis != null ) {
                    fis.close();
                }
                if ( isr != null ) {
                    isr.close();
                }
            } catch ( Exception e ) {
                DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, MESSAGE_END_POINT, "error", Arrays.asList(errorMessage));
            }
            File lockFile = new File(LOCK_DIR + "hybird_cf.lock");
            if(lockFile.exists()){
                lockFile.delete();
            }
        }
        String deployStatus = message.getMessage("common.deploy.status."+status.toLowerCase(), null, Locale.KOREA);
        if ( vo != null ) {
            vo.setDeployStatus(deployStatus);
            if( !StringUtils.isEmpty(taskId) ) {
                vo.setTaskId(Integer.parseInt(taskId));
            }
            vo.setUpdateUserId(principal.getName());
            saveDeployStatus(vo);
        }
    }

    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : CF 설치 상태 저장
     * @title : saveDeployStatus
     * @return : CfVO
    *****************************************************************/
    public HbCfVO saveDeployStatus(HbCfVO vo) {
        cfDao.updateHbCfInfo(vo);
        return vo;
    }
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : 비동기로 deploy 메소드 호출
     * @title : deployAsync
     * @return : void
    *****************************************************************/
    @Async
    public void deployAsync(HbCfDTO dto, Principal principal, String platform) {
        deploy(dto, principal, platform);
    }
}
