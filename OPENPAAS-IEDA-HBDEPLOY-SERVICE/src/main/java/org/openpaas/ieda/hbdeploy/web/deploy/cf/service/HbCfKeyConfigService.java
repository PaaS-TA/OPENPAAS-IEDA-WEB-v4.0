package org.openpaas.ieda.hbdeploy.web.deploy.cf.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.openpaas.ieda.common.api.LocalDirectoryConfiguration;
import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfKeyConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfKeyConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfKeyConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HbCfKeyConfigService {
    
    @Autowired private HbCfKeyConfigDAO dao;
    @Autowired private MessageSource message;
    
    final private static String SEPARATOR = System.getProperty("file.separator");
    final private static String GENERATE_CERTS_DIR = LocalDirectoryConfiguration.getGenerateCertsDir();
    final private static String HYBRID_KEY_DIR = LocalDirectoryConfiguration.getKeyDir();
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Key 정보 목록 전체 조회
     * @title : getKeyConfigInfoList
     * @return : List<HbCfDefaultConfigVO>
    *****************************************************************/
    public List<HbCfKeyConfigVO> getKeyConfigInfoList() {
        List<HbCfKeyConfigVO> list = dao.selectKeyConfigInfoList();
        return list;
    }

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Key 정보 저장
     * @title : saveKeyConfigInfo
     * @return : void
    *****************************************************************/
    public void saveKeyConfigInfo(HbCfKeyConfigDTO dto, Principal principal) {
        HbCfKeyConfigVO vo = null;
        int count = dao.selectCfKeyConfigInfoByName(dto.getKeyConfigName());
        if(StringUtils.isEmpty(dto.getId())) {
            vo = new HbCfKeyConfigVO();
            vo.setCreateUserId(principal.getName());
            if(count > 0){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        } else {
            vo = dao.selectCfKeyConfigInfoById(Integer.parseInt(dto.getId()));
            if(!dto.getKeyConfigName().equals(vo.getKeyConfigName()) && count > 0){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        }
        
        if(vo != null){
            vo.setCity(dto.getCity());
            vo.setCompany(dto.getCompany());
            vo.setCountryCode(dto.getCountryCode());
            vo.setUpdateUserId(principal.getName());
            vo.setDefaultConfigInfo(dto.getDefaultConfig());
            vo.setKeyConfigName(dto.getKeyConfigName());
            vo.setDomain(dto.getDomain());
            vo.setEmailAddress(dto.getEmailAddress());
            vo.setIaasType(dto.getIaasType());
            vo.setJobTitle(dto.getJobTitle());
            vo.setReleaseName(dto.getReleaseName());
            vo.setReleaseVersion(dto.getReleaseVersion());
            vo.setKeyFileName(hbCfCreateKeyCmdProcess(vo, principal));
        }
        
        if(StringUtils.isEmpty(dto.getId())) {
            dao.insertCfKeyConfigInfo(vo);
        }else {
            dao.updateCfKeyConfigInfo(vo);
        }
    }
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : Hybrid Key 생성
     * @title : hbCfCreateKeyCmdProcess
     * @return : String
    *****************************************************************/
    public String hbCfCreateKeyCmdProcess( HbCfKeyConfigVO vo, Principal principal){
        File generateCertsFile = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        String keyFileName = "";
        if(vo.getReleaseVersion().equals("287") || vo.getReleaseVersion().equals("272") 
                || vo.getReleaseVersion().equals("3.0") || vo.getReleaseVersion().equals("3.1")){
            String generateCerts = GENERATE_CERTS_DIR + SEPARATOR + "generate-certs_v2" + SEPARATOR + "generate-certs";
            try {
                generateCertsFile = new File( generateCerts );
                if( generateCertsFile.exists() ){
                    keyFileName = vo.getIaasType().toLowerCase() + "-" + vo.getKeyConfigName()+"-key.yml";
                    ProcessBuilder builder = new ProcessBuilder();
                    List<String> cmd = new ArrayList<String>();
                    cmd.add(generateCerts);
                    cmd.add(GENERATE_CERTS_DIR + SEPARATOR + "generate-certs_v2");
                    cmd.add("3"); //1:cf, 2: diego, 3: cf-diego
                    cmd.add( keyFileName.split(".yml")[0] ); // make key name(<iaas>-cf-key-<id>);
                    cmd.add(vo.getDomain());//domain
                    cmd.add(vo.getCountryCode());//국가 코드
                    cmd.add(vo.getCity());//시//도
                    cmd.add(vo.getCity());//시/구/군
                    cmd.add(vo.getCompany());//회사명
                    cmd.add(vo.getJobTitle());//부서명
                    cmd.add(vo.getEmailAddress());//email
                    builder.command(cmd);
                    builder.redirectErrorStream(true);
                    Process process = builder.start();//start script
                    
                    inputStream = process.getInputStream();//get script log
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                    String info = null;
                    while ((info = bufferedReader.readLine()) != null) {
                        if( info.indexOf("ERROR") > -1 ){
                            System.out.println(info);
                            throw new CommonException(getMessageValue("common.internalServerError.exception.code"),
                                    keyFileName + getMessageValue("common.file.create.internalServerError.message"), HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    }
                    File keyFile = new File( HYBRID_KEY_DIR + SEPARATOR + keyFileName );
                    if( !keyFile.exists() ){
                        throw new CommonException(message.getMessage("common.internalServerError.exception.code", null, Locale.KOREA),
                                keyFileName +" 파일을 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }else{
                    throw new CommonException(getMessageValue("common.notFound.exception.code"),
                            getMessageValue("common.notFound.template.message"), HttpStatus.NOT_FOUND);
                }
            } catch (IOException e) {
                throw new CommonException(getMessageValue("common.internalServerError.exception.code"),
                        getMessageValue("common.internalServerError.message"), HttpStatus.INTERNAL_SERVER_ERROR);
            }finally{
                try {
                    if( bufferedReader != null ){
                        bufferedReader.close();
                    }
                } catch (IOException e) {
                    throw new CommonException(getMessageValue("common.internalServerError.exception.code"),
                            getMessageValue("common.internalServerError.message"), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                    "지원 CF Release 버전을 확인하세요.", HttpStatus.BAD_REQUEST);
        }
        return keyFileName;
    }
    
    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : Message 값 가져오기
     * @title : getMessageValue
     * @return : void
    ***************************************************/
    public String getMessageValue(String messageCode) {
        String messageValue = message.getMessage(messageCode, null, Locale.KOREA);
        return messageValue;
    }
    
    /***************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : Message 값 가져오기
     * @title : deleteKeyConfigInfo
     * @return : void
    ***************************************************/
    public void deleteKeyConfigInfo(HbCfKeyConfigDTO dto, Principal principal) {
        if(StringUtils.isEmpty(dto.getId())){
            throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                    "Key 삭제 실패", HttpStatus.BAD_REQUEST);
        }
        File file = new File(dto.getKeyFileName());
        if(file.exists()){
        	file.delete();
        }
        dao.deleteCfKeyConfigInfo(Integer.parseInt(dto.getId()));
        
    }
}
