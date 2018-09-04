package org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.openpaas.ieda.common.api.LocalDirectoryConfiguration;
import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.deploy.api.director.utility.DirectorRestHelper;
import org.openpaas.ieda.deploy.web.common.dao.CommonDeployDAO;
import org.openpaas.ieda.deploy.web.common.dao.ManifestTemplateVO;
import org.openpaas.ieda.hbdeploy.web.config.setting.dao.HbDirectorConfigDAO;
import org.openpaas.ieda.hbdeploy.web.config.setting.dao.HbDirectorConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao.HbCfDeploymentDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao.HbCfDeploymentVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dto.HbCfDeploymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HbCfDeploymentDeployAsyncService {
	
    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private HbDirectorConfigDAO directorConfigDao;
    @Autowired private HbCfDeploymentDAO cfDeploymentDao;
    @Autowired private HbCfDeploymentService cfService;
    @Autowired private MessageSource message;
    @Autowired private CommonDeployDAO commonDao;
    
    private final static String SEPARATOR = System.getProperty("file.separator");
    private final static String MANIFEST_TEMPLATE_DIR = LocalDirectoryConfiguration.getManifastTemplateDir();
    final private static String HYBRID_CF_CREDENTIAL_DIR = LocalDirectoryConfiguration.getGenerateHybridCfCredentialDir();
    private final static String DEPLOYMENT_DIR = LocalDirectoryConfiguration.getDeploymentDir();
    
	public void deploy(HbCfDeploymentDTO dto, Principal principal, String platform) {
        String deploymentFileName = "";
        String messageEndpoint =  "/deploy/cf/install/logs"; 
        HbCfDeploymentVO vo = new HbCfDeploymentVO();
        //HbCfDeploymentVO vo = cfService.getCfInfo(dto.getId());
        ManifestTemplateVO result = commonDao.selectManifetTemplate(vo.getIaasType(), vo.getHbCfDeploymentDefaultConfigVO().getCfDeploymentVersion(), "HBCFDEPLOYMENT", vo.getHbCfDeploymentDefaultConfigVO());
        deploymentFileName = vo != null ? vo.getHbCfDeploymentDefaultConfigVO().getdeployment : "";
        
        if ( StringUtils.isEmpty(deploymentFileName) ) {
            throw new CommonException(message.getMessage("common.badRequest.exception.code", null, Locale.KOREA), 
                    message.getMessage("common.badRequest.message", null, Locale.KOREA), HttpStatus.BAD_REQUEST);
        }
        String cloudConfigFile = DEPLOYMENT_DIR + SEPARATOR + deploymentFileName; 
        String errorMessage = message.getMessage("common.internalServerError.message", null, Locale.KOREA);
        String status = "";
        
        try {
            BufferedReader bufferedReader = null;
            String accumulatedLog= null;
            HbDirectorConfigVO directorInfo = directorConfigDao.selectHbDirectorConfigBySeq(1);
            List<String> cmd = new ArrayList<String>(); //bosh cloud config 명령어 실행 줄 Cloud Config 관련 Rest API를 아직 지원 안하는 것 같음 2018.08.01
            cmd.add("bosh");
            cmd.add("-e");
            cmd.add(directorInfo.getDirectorName());
            cmd.add("update-cloud-config");
            cmd.add(cloudConfigFile);
            cmd.add("-n");
            ProcessBuilder builder = new ProcessBuilder(cmd);
            builder.redirectErrorStream(true);
            builder.start();
            if ( vo != null ) {
                String deployStatus = message.getMessage("common.deploy.status.processing", null, Locale.KOREA);
                vo.setDeployStatus(deployStatus);
                vo.setUpdateUserId(principal.getName());
                saveDeployStatus(vo);
            }
            //if 문을 통해 public IP 사용 유무/mysql/postgres 사용 유무에 따라 해당 커맨드 라인 변경
            cmd = new ArrayList<String>(); // bosh deploy 명령어 실행 줄 Rest API를 통해 deploy 시 -v/-o 옵션을 사용하지 못하고 통 Manifest 파일을 올려야 하는 것 같음 2018.08.01
            cmd.add("bosh");
            cmd.add("-e");
            cmd.add(directorInfo.getDirectorName());
            cmd.add("-d");
            cmd.add(vo.getHbCfDeploymentDefaultConfigVO().getDefaultConfigName());
            cmd.add("deploy");
            cmd.add(MANIFEST_TEMPLATE_DIR+"/cf-deployment/"+result.getMinReleaseVersion()+"/common/"+result.getCommonBaseTemplate()+"");
            setDefualtInfo(cmd, vo, result);
            setPublicNetworkIpUse(cmd, vo, result);
            if("postgres".equals(vo.getHbCfDeploymentDefaultConfigVO().getCfDbType())){
                postgresDbUse(cmd, result);
            }
            setJobSetting(cmd, vo, result);
            cmd.add("--tty");
            cmd.add("-n");
            //cmd.add("--no-redact");
            builder = new ProcessBuilder(cmd);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            InputStream inputStream = process.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            String info = null;
            StringBuffer accumulatedBuffer = new StringBuffer();
            while ((info = bufferedReader.readLine()) != null){
                accumulatedBuffer.append(info).append("\n");
                DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, messageEndpoint, "started", Arrays.asList(info));
            }
            if( accumulatedBuffer != null ) {
                accumulatedLog = accumulatedBuffer.toString();
            }
            
            if( accumulatedLog.contains("Failed deploying") || accumulatedLog.contains("Failed") || accumulatedLog.contains("error") 
                || accumulatedLog.contains("invalid") || accumulatedLog.contains("not support") || accumulatedLog.contains("Expected") || accumulatedLog.contains("expected") || accumulatedLog.contains("refused") ){
                status = "error";
                DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, messageEndpoint, "error", Arrays.asList("CF-Deployment 설치 중 에러가 발생 했습니다.<br> 설정을 확인 해주세요."));
            } else {
                status = "done";
                vo.setDeployStatus( message.getMessage("common.deploy.status.done", null,  Locale.KOREA ) );
                DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, messageEndpoint, "done", Arrays.asList("", "CF-Deployment 설치가 완료되었습니다."));
            }
            
        }catch (RuntimeException e) {
            status = "error";
            DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, messageEndpoint, "error", Arrays.asList("CF-Deployment 설치 중 에러가 발생 했습니다.<br> 설정을 확인 해주세요."));
        }catch ( Exception e) {
            status = "error";
            DirectorRestHelper.sendTaskOutput(principal.getName(), messagingTemplate, messageEndpoint, "error", Arrays.asList(errorMessage));
        } 
        String deployStatus = message.getMessage("common.deploy.status."+status.toLowerCase(), null, Locale.KOREA);
        if ( vo != null ) {
            vo.setDeployStatus(deployStatus);
            vo.setUpdateUserId(principal.getName());
            saveDeployStatus(vo);
        }
	}
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Job 인스턴스 수 설정
     * @title : setDefualtInfo
     * @return : void
    *****************************************************************/
    private void setDefualtInfo(List<String> cmd, HbCfDeploymentVO vo, ManifestTemplateVO result) {
        cmd.add("--vars-store="+HYBRID_CF_CREDENTIAL_DIR+ SEPARATOR +vo.getHbCfDeploymentCredentialConfigVO().getCredentialConfigKeyFileName()+"");
        cmd.add("-v");
        cmd.add("deployment_name="+vo.getHbCfDeploymentDefaultConfigVO().getDefaultConfigName()+"");
        cmd.add("-v");
        cmd.add("system_domain="+vo.getHbCfDeploymentDefaultConfigVO().getDomain()+"");
        cmd.add("-v");
        cmd.add("system_domain_org="+vo.getHbCfDeploymentDefaultConfigVO().getDomainOrganization()+"");
        cmd.add("-v");
        cmd.add("stemcell_version="+vo.getHbCfDeploymentResourceConfigVO().getStemcellVersion()+"");
        cmd.add("-o");
        cmd.add(MANIFEST_TEMPLATE_DIR+"/cf-deployment/"+result.getMinReleaseVersion()+"/common/"+result.getCommonJobTemplate());
        cmd.add("-o");
        cmd.add(MANIFEST_TEMPLATE_DIR+"/cf-deployment/"+result.getMinReleaseVersion()+"/common/"+result.getMetaTemplate());
    }
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Deployment Job 인스턴스 수 설정
     * @title : setPublicNetworkIpUse
     * @return : void
    *****************************************************************/
    private void setJobSetting(List<String> cmd, HbCfDeploymentVO vo, ManifestTemplateVO result) {
        if (vo.getJobs()!=null && vo.getJobs().size()!=0 ){
            for(int i=0; i<vo.getJobs().size(); i++){
                cmd.add("-v");
                cmd.add(vo.getJobs().get(i).get("job_name")+"_instance="+String.valueOf(vo.getHbCfDeploymentInstanceConfigVO().get(i).get("instances"))+"");
            }
            cmd.add("-o");
            cmd.add(MANIFEST_TEMPLATE_DIR+"/cf-deployment/"+result.getMinReleaseVersion()+"/common/"+result.getOptionResourceTemplate());
        }
    }
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Public Network IP 사용 시 값
     * @title : setPublicNetworkIpUse
     * @return : void
    *****************************************************************/
    private void setPublicNetworkIpUse(List<String> cmd, HbCfDeploymentVO vo, ManifestTemplateVO result) {
        if(vo.getNetworks() != null && vo.getNetworks().size() != 0){
            for( int i=0; i<vo.getNetworks().size(); i++ ){
                if("external".equals(vo.getNetworks().get(i).getNet().toLowerCase()) 
                    && ( vo.getNetworks().get(i).getPublicStaticIp() != null && !vo.getNetworks().get(i).getPublicStaticIp().isEmpty())){
                    cmd.add("-v");
                    cmd.add("haproxy_public_ip="+vo.getNetworks().get(i).getPublicStaticIp()+"");
                    cmd.add("-o");
                    cmd.add(MANIFEST_TEMPLATE_DIR+"/cf-deployment/"+result.getMinReleaseVersion()+"/common/"+result.getCommonOptionTemplate());
                }
            }
            if(vo.getNetworks().size() == 3){
                cmd.add("-o");
                cmd.add(MANIFEST_TEMPLATE_DIR+"/cf-deployment/"+result.getMinReleaseVersion()+"/common/"+result.getOptionNetworkTemplate());
            }
        }
    }
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : postgres DB 사용 시 옵션 값 추가
     * @title : postgresDbUse
     * @return : void
    *****************************************************************/
    private void postgresDbUse(List<String> cmd, ManifestTemplateVO result) {
        cmd.add("-o");
        cmd.add(MANIFEST_TEMPLATE_DIR+"/cf-deployment/"+result.getMinReleaseVersion()+"/common/"+result.getOptionEtc());
    }
    
    
    @Async
    public void deployAsync(HbCfDeploymentDTO dto, Principal principal, String platform) {
    	deploy(dto, principal, platform);
    }
    
}
