package org.openpaas.ieda.hbdeploy.web.common.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.openpaas.ieda.common.api.LocalDirectoryConfiguration;
import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.deploy.web.common.dao.ManifestTemplateVO;
import org.openpaas.ieda.deploy.web.common.service.CommonDeployUtils;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

public class CommonHbDeployUtils {
	
    final private static Logger LOGGER = LoggerFactory.getLogger(CommonDeployUtils.class);
    final private static String SEPARATOR = System.getProperty("file.separator");
    final private static String TEMP_FILE =  LocalDirectoryConfiguration.getTempDir() + SEPARATOR;
    final private static String DEPLOYMENT_FILE = LocalDirectoryConfiguration.getDeploymentDir() + SEPARATOR;
	
	
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : Diego Manifest 템플릿 파일과 병합하여 읽어와서 deployment 경로에 최종 Manifest 파일 생성
     * @title : setShellScript
     * @return : void
    *****************************************************************/
    public static void setHbShellScript(String inputFile, ManifestTemplateVO manifestTemplate, HbDiegoVO vo) {
        File settingFile = null;
        File shellScriptFile = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;

        try {
            settingFile = new File( TEMP_FILE + inputFile);
            shellScriptFile = new File( manifestTemplate.getShellScript() );
            ProcessBuilder builder = new ProcessBuilder();

            if ( settingFile.exists() && shellScriptFile.exists() && vo.getDefaultConfigVO().getDiegoReleaseVersion() != null ) {
                List<String> cmd = new ArrayList<String>();
                //1. shellScript
                cmd.add(manifestTemplate.getShellScript());
                //1.1 home
                cmd.add(System.getProperty("user.home"));
                //1.2 Deployment Version
                cmd.add(manifestTemplate.getMinReleaseVersion());

                //1.3 Path to BOSH manifest stub file.
                if (!StringUtils.isEmpty(manifestTemplate.getCommonBaseTemplate())) {
                    cmd.add(manifestTemplate.getCommonBaseTemplate());
                } else{
                    cmd.add("");
                }
                //1.4 Path to DIEGO stub file.
                if (!StringUtils.isEmpty(manifestTemplate.getCommonJobTemplate())) {
                    cmd.add(manifestTemplate.getCommonJobTemplate());
                } else{
                    cmd.add("");
                }
                //1.5 Path to DIEGO-version-overrides stub file.
                if (!StringUtils.isEmpty(manifestTemplate.getMetaTemplate())) {
                    cmd.add(manifestTemplate.getMetaTemplate());
                } else{
                    cmd.add("");
                }
                //1.6 Path to IaaS-Settings-overrides stub file.
                if (!StringUtils.isEmpty(manifestTemplate.getIaasPropertyTemplate())) {
                    cmd.add(manifestTemplate.getIaasPropertyTemplate());
                } else{
                    cmd.add("");
                }
                //1.7 Path to CF manifest file.
                if (!StringUtils.isEmpty(manifestTemplate.getCfTempleate())) {
                    cmd.add(manifestTemplate.getCfTempleate());
                } else{
                    cmd.add("");
                }
                //1.8 Path to DIEGO input file.
                cmd.add(inputFile);
                //1.9 Path to DIEGO manifest file.
                cmd.add(inputFile);
                if (!StringUtils.isEmpty( vo.getDefaultConfigVO().getKeyFile() )) {
                    cmd.add( vo.getDefaultConfigVO().getKeyFile() );
                }
                
                //2. OPTIONAL TEMPLATES
                //2.1 Path to DIEGO Network 1 stub file.
                if (!StringUtils.isEmpty(manifestTemplate.getOptionNetworkTemplate())) {
                    cmd.add(manifestTemplate.getOptionNetworkTemplate());
                } else {
                    cmd.add("");
                }
                //2.2 Path to DIEGO Network 2 stub file.
                if (!StringUtils.isEmpty(manifestTemplate.getOptionEtc())) {
                    cmd.add(manifestTemplate.getOptionEtc());
                } else {
                    cmd.add("");
                }
                //2.3 Path to Resource-overrides stub file.
                if (!StringUtils.isEmpty(manifestTemplate.getOptionResourceTemplate())) {
                    cmd.add(manifestTemplate.getOptionResourceTemplate());
                } else {
                    cmd.add("");
                }
                //2.4 Path to Path to PaaSTA-overrides stub file.
                if( !StringUtils.isEmpty(manifestTemplate.getCommonOptionTemplate())){
                    cmd.add(manifestTemplate.getCommonOptionTemplate());
                }else{
                    cmd.add("");
                }
                
                builder.command(cmd);
                builder.redirectErrorStream(true);
                Process process = builder.start();
                
                inputStream = process.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                
                String info = null;
                StringBuffer deployBuffer = new StringBuffer("");
                while ((info = bufferedReader.readLine()) != null) {
                    deployBuffer.append(info).append("\n");
                }
                
                String deloymentContent = deployBuffer.toString();
                if (!deloymentContent.equals("")) {
                    IOUtils.write(deloymentContent, new FileOutputStream( DEPLOYMENT_FILE + vo.getDeploymentFile()), "UTF-8");
                }
            } else {
                throw new CommonException("notfound.diegoManifest.exception",  "Merge할 File이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
            }
        } catch( UnsupportedEncodingException e){
            throw new CommonException("unsupportedEncoding.diegoManifest.exception", 
                    "DIEGO Manifest 파일 정보를 읽어올 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch ( FileNotFoundException e ){
            throw new CommonException("notfound.diegoManifest.exception", 
                    "DIEGO Manifest 파일을 생성할 수 없습니다. ", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            throw new CommonException("ioFileRead.diegoManifest.exception", 
                    "DIEGO Manifest 파일 정보를 읽어올 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
             
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                if( LOGGER.isErrorEnabled() ){
                    LOGGER.error( e.getMessage() );
                }
            }
        }
    }
}
