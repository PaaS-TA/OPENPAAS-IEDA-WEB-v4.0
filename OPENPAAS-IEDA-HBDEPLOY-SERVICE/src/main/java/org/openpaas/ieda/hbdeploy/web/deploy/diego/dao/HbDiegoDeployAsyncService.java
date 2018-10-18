package org.openpaas.ieda.hbdeploy.web.deploy.diego.dao;

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
import org.openpaas.ieda.common.web.security.SessionInfoDTO;
import org.openpaas.ieda.deploy.api.director.utility.DirectorRestHelper;
import org.openpaas.ieda.deploy.web.deploy.diego.service.DiegoDeployAsyncService;
import org.openpaas.ieda.hbdeploy.api.director.utility.HbDirectorRestHelper;
import org.openpaas.ieda.hbdeploy.web.config.setting.dao.HbDirectorConfigDAO;
import org.openpaas.ieda.hbdeploy.web.config.setting.dao.HbDirectorConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HbDiegoDeployAsyncService {
    @Autowired private HbDiegoDAO diegoDao;
    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private HbDirectorConfigDAO directorConfigDao;
    @Autowired private MessageSource message;
    
    private final static Logger LOGGER = LoggerFactory.getLogger(DiegoDeployAsyncService.class);
    private final static String SEPARATOR = System.getProperty("file.separator");
    private final static String DEPLOYMENT_DIR = LocalDirectoryConfiguration.getDeploymentDir() + SEPARATOR;
    private final static String MESSAGE_ENDPOINT = "/deploy/hbDiego/install/logs"; 
    private final static String LOCK_DIR = LocalDirectoryConfiguration.getLockDir()+SEPARATOR;
    
    /***************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : Diego 플랫폼 설치를 요청
     * @title : deploy
     * @return : void
    ***************************************************/
    public void deploy(HbDiegoDTO dto, Principal principal, String deploy) {
        
        HbDiegoVO vo = null;
        String deploymentFileName = null;
        SessionInfoDTO sessionInfo = new SessionInfoDTO(principal);
        
        vo = diegoDao.selectHbDiegoInfoById(Integer.parseInt(dto.getId()));
        if ( vo != null ) {
            deploymentFileName = vo.getDeploymentFile();
        }
            
        if (  StringUtils.isEmpty(deploymentFileName) ) {
            throw new CommonException("notfound.diegodelete.exception",
                    "배포파일 정보가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        
        if ( vo != null ) {
            vo.setDeployStatus(message.getMessage("common.deploy.status.processing", null, Locale.KOREA));
            vo.setUpdateUserId(sessionInfo.getUserId());
            diegoDao.updateHbDiegoInfo(vo);;
        }
        
        String status = "";
        StringBuffer content = new StringBuffer("");
        String temp = "";
        String taskId = "";
        HbDirectorConfigVO defaultDirector = directorConfigDao.selectHbDirectorConfigBySeq(Integer.parseInt(vo.getDefaultConfigVO().getDirectorId()));
        
        InputStreamReader isr = null;
        FileInputStream fis = null;
        BufferedReader br = null;
        try {
            
            //Instantiate an HttpClient
            HttpClient httpClient = DirectorRestHelper.getHttpClient(defaultDirector.getDirectorPort());
            PostMethod postMethod = new PostMethod(DirectorRestHelper.getDeployURI(defaultDirector.getDirectorUrl(), defaultDirector.getDirectorPort()));
            postMethod = (PostMethod)DirectorRestHelper.setAuthorization(defaultDirector.getUserId(), defaultDirector.getUserPassword(), (HttpMethodBase)postMethod);
            postMethod.setRequestHeader("Content-Type", "text/yaml"); //header 정의
            
            String deployFile = DEPLOYMENT_DIR + deploymentFileName;
            
            fis = new FileInputStream(deployFile);
            isr = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(isr);
            
            while ( (temp=br.readLine()) != null) {
                content.append(temp).append("\n");
            }
            
            postMethod.setRequestEntity(new StringRequestEntity(content.toString(), "text/yaml", "UTF-8"));
            
            //HTTP 요청 및 요청 결과
            int statusCode = httpClient.executeMethod(postMethod);
            if ( statusCode == HttpStatus.MOVED_PERMANENTLY.value() || statusCode == HttpStatus.MOVED_TEMPORARILY.value() ) {
                Header location = postMethod.getResponseHeader("Location");
                taskId = DirectorRestHelper.getTaskId(location.getValue());
                status = HbDirectorRestHelper.trackToTask(defaultDirector, messagingTemplate, MESSAGE_ENDPOINT, httpClient, taskId, "event", principal.getName());
            } else {
                DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, MESSAGE_ENDPOINT, "error", Arrays.asList("배포 중 오류가 발생하였습니다.<br> DIEGO 정보를 확인 하세요."));
            }
            
        } catch ( IOException e) {
            status = "error";
            DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, MESSAGE_ENDPOINT, "error", Arrays.asList("배포 중 Exception이 발생하였습니다."));
        } catch (RuntimeException e) {
            status = "error";
            DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, MESSAGE_ENDPOINT, "error", Arrays.asList("배포 중 Exception이 발생하였습니다."));
        } catch ( Exception e) {
            status = "error";
            DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, MESSAGE_ENDPOINT, "error", Arrays.asList("배포 중 Exception이 발생하였습니다."));
        } finally {
            File lockFile = new File(LOCK_DIR + "hybird_diego.lock");
            if(lockFile.exists()){
                lockFile.delete();
            }
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
            } catch ( IOException e ) {
                if( LOGGER.isErrorEnabled() ){
                    LOGGER.error( e.getMessage() );
                }
            }
        }
        if ( vo != null ) {
            String deployStatus = "";
            if( status.equalsIgnoreCase("done") ){
                deployStatus =  message.getMessage("common.deploy.status.done", null, Locale.KOREA);
            } else if( status.equalsIgnoreCase("error") ){
                deployStatus = message.getMessage("common.deploy.status.error", null, Locale.KOREA);
            } else if( status.equalsIgnoreCase("cancelled") ){
                deployStatus = message.getMessage("common.deploy.status.cancelled", null, Locale.KOREA);
            }
            vo.setDeployStatus(deployStatus);
            if( !StringUtils.isEmpty(taskId) ) {
                vo.setTaskId(Integer.parseInt(taskId));
            }
            vo.setUpdateUserId(sessionInfo.getUserId());
            diegoDao.updateHbDiegoInfo(vo);
        }
    }

    /***************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : deploy 메소드를 호출
     * @title : deployAsync
     * @return : void
    ***************************************************/
    @Async
    public void deployAsync(HbDiegoDTO dto, Principal principal, String install) {
        deploy(dto, principal, install);
    }
    
}
