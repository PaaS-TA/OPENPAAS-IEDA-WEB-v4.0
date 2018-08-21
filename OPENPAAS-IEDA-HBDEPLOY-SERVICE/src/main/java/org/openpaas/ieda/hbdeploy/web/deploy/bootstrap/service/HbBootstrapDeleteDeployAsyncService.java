package org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.Arrays;
import java.util.Locale;

import org.openpaas.ieda.common.api.LocalDirectoryConfiguration;
import org.openpaas.ieda.deploy.web.common.service.CommonDeployUtils;
import org.openpaas.ieda.hbdeploy.api.director.utility.DirectorRestHelper;
import org.openpaas.ieda.hbdeploy.web.config.setting.dao.HbDirectorConfigDAO;
import org.openpaas.ieda.hbdeploy.web.config.setting.dao.HbDirectorConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dao.HbBootstrapDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dao.HbBootstrapVO;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dto.HbBootStrapDeployDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class HbBootstrapDeleteDeployAsyncService{

    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private HbDirectorConfigDAO directorDao;
    @Autowired private HbBootstrapDAO bootstrapDao;
    @Autowired MessageSource message;
    
    final private static String SEPARATOR = System.getProperty("file.separator");
    final private static String DEPLOYMENT_DIR = LocalDirectoryConfiguration.getDeploymentDir() + SEPARATOR;
    final private static String HYBRID_CREDENTIAL_FILE = LocalDirectoryConfiguration.getGenerateHybridCredentialDir() + SEPARATOR;
    final private static String LOCK_DIR=LocalDirectoryConfiguration.getLockDir();
    final private static String MESSAGE_ENDPOINT = "/deploy/hbBootstrap/delete/logs"; 
    private final static Logger LOGGER = LoggerFactory.getLogger(HbBootstrapDeleteDeployAsyncService.class);
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : bosh를 실행하여 해당 플랫폼 삭제 요청
     * @title : deleteBootstrapDeploy
     * @return : void
    *****************************************************************/
    public void deleteBootstrapDeploy(HbBootStrapDeployDTO dto, Principal principal) {
        
        String accumulatedLog = "";
        HbBootstrapVO vo = bootstrapDao.selectBootstrapConfigInfo(Integer.parseInt(dto.getId()), dto.getIaasType().toLowerCase());
        if( vo == null ){
            bootstrapDao.deleteBootstrapInfo(dto);
            vo = new HbBootstrapVO();
        }
        String status = "";
        String resultMessage = "";
        BufferedReader bufferedReader = null;

        try {
            String deployStateFile = DEPLOYMENT_DIR +vo.getDeploymentFile().split(".yml")[0] + "-state.json";
            File stateFile = new File(deployStateFile);
            if ( !stateFile.exists() ) {
                status = "done";
                resultMessage = "BOOTSTRAP 삭제가 완료되었습니다.";
                bootstrapDao.deleteBootstrapInfo(dto);
                DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, MESSAGE_ENDPOINT, status, Arrays.asList("BOOTSTRAP를 삭제했습니다."));
                
            }else{
                File credentialFile = new File(HYBRID_CREDENTIAL_FILE+vo.getDefaultConfigVo().getCredentialKeyName());
                
                if(!credentialFile.exists()){
                    status = "error";
                    DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, MESSAGE_ENDPOINT, status, Arrays.asList("BOOTSTRAP 디렉터 인증서가 존재 하지 않습니다."));
                }
                String deployFile = DEPLOYMENT_DIR + vo.getDeploymentFile();
                File file = new File(deployFile);
                if( file.exists() ){
                    ProcessBuilder builder = new ProcessBuilder("bosh", "delete-env", deployFile, 
                                                                "--state="+deployStateFile, 
                                                                "--vars-store="+HYBRID_CREDENTIAL_FILE+vo.getDefaultConfigVo().getCredentialKeyName(), "--tty");
                    builder.redirectErrorStream(true);
                    Process process = builder.start();
                    
                       //배포 상태
                    vo.setDeployStatus( message.getMessage("common.deploy.status.deleting", null, Locale.KOREA) );
                    saveDeployStatus(vo, principal);
                    
                    //Delete log...
                    InputStream inputStream = process.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                    StringBuffer accumulatedBuffer = new StringBuffer("");
                    String info = null;
                    while ((info = bufferedReader.readLine()) != null){
                        accumulatedBuffer.append(info).append("\n");
                        DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, MESSAGE_ENDPOINT, "started", Arrays.asList(info));
                    }
                    accumulatedLog = accumulatedBuffer.toString();
                } else {
                    status = "error";
                    resultMessage = "배포 파일(" + deployFile + ")이 존재하지 않습니다.";
                    vo.setDeployStatus( message.getMessage("common.deploy.status.failed", null, Locale.KOREA) );
                    saveDeployStatus(vo, principal);
                }
                
                if ( "error".equalsIgnoreCase(status) || accumulatedLog.contains("fail") || accumulatedLog.contains("error") || accumulatedLog.contains("No deployment") || accumulatedLog.contains("Error")) {
                    status = "error";
                    vo.setDeployStatus(message.getMessage("common.deploy.status.failed", null, Locale.KOREA));
                    saveDeployStatus(vo, principal);
                    if ( resultMessage.isEmpty() ) {
                        resultMessage = "BOOTSTRAP 삭제 중 오류가 발생하였습니다.<br> 로그를 확인하세요.";
                    }
                } else {
                    status = "done";
                    resultMessage = "BOOTSTRAP 삭제가 완료되었습니다.";
                    bootstrapDao.deleteBootstrapInfo(dto);
                       //설치 관리자 삭제
                    deleteDirectorConfigInfo(vo.getIaasType(), vo.getDefaultConfigVo().getDirectorName());
                }
                DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, MESSAGE_ENDPOINT, status, Arrays.asList(resultMessage));
            }
        }catch(RuntimeException e){
            status = "error";
            e.printStackTrace();
            CommonDeployUtils.deleteFile(LOCK_DIR, "hybird_bootstrap.lock");
            DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, MESSAGE_ENDPOINT, status, Arrays.asList("BOOTSTRAP 삭제 중 Exception이 발생하였습니다."));
        } catch ( Exception e) {
            status = "error";
            CommonDeployUtils.deleteFile(LOCK_DIR, "hybird_bootstrap.lock");
            DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, MESSAGE_ENDPOINT, status, Arrays.asList("BOOTSTRAP 삭제 중 Exception이 발생하였습니다."));
        }finally {
            if(status.toLowerCase().equalsIgnoreCase("error")){
                vo.setDeployStatus(message.getMessage("common.deploy.status.failed", null, Locale.KOREA));
                saveDeployStatus(vo, principal);
            }
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    if( LOGGER.isErrorEnabled() ){
                        LOGGER.error( e.getMessage() );
                    }
                }
            }
            CommonDeployUtils.deleteFile(LOCK_DIR, "hybird_bootstrap.lock");
        }
    }

    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 설치 상태 저장
     * @title : saveDeployStatus
     * @return : BootstrapVO
    *****************************************************************/
    public HbBootstrapVO saveDeployStatus(HbBootstrapVO bootstrapVo, Principal principal) {
        if ( bootstrapVo == null ) {
            return null;
        }
        bootstrapVo.setUpdateUserId(principal.getName());
        bootstrapDao.updateBootStrapConfigInfo(bootstrapVo);
        return bootstrapVo;
    }
    
    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 설치관리자 정보가 존재 할 경우 삭제
     * @title : deleteDirectorConfigInfo
     * @return : void
    ***************************************************/
    public void deleteDirectorConfigInfo(String iaasType, String directorName) {
        String cpi = iaasType.toLowerCase()+"_cpi";
        HbDirectorConfigVO vo = directorDao.selectHbDirectorConfigInfoByDirectorNameAndCPI(cpi, directorName);
        if( vo != null ) {
          directorDao.deleteHbDirector(vo.getIedaDirectorConfigSeq());
        }
    }

    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 비동기로  deleteDeploy 호출
     * @title : deleteDeployAsync
     * @return : void
    *****************************************************************/
    @Async
    public void deleteDeployAsync(HbBootStrapDeployDTO dto, Principal principal) {
            deleteBootstrapDeploy(dto, principal);
    }    

}
