package org.openpaas.ieda.hbdeploy.web.deploy.diego.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.openpaas.ieda.common.api.LocalDirectoryConfiguration;
import org.openpaas.ieda.common.api.Sha512Crypt;
import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.deploy.web.common.dao.CommonDeployDAO;
import org.openpaas.ieda.deploy.web.common.dao.ManifestTemplateVO;
import org.openpaas.ieda.deploy.web.common.dto.ReplaceItemDTO;
import org.openpaas.ieda.deploy.web.deploy.cf.service.CfService;
import org.openpaas.ieda.hbdeploy.web.common.service.CommonHbDeployUtils;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoNetworkConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
@Service
public class HbDiegoService {
    
    @Autowired private MessageSource message;
    @Autowired private HbDiegoDAO dao;
    @Autowired private HbCfDAO cfDao;
    @Autowired private HbDiegoNetworkConfigDAO networkDao;
    @Autowired private CommonDeployDAO commonDao;
    
    final private static String SEPARATOR = System.getProperty("file.separator");
    final private static String CF_FILE  = LocalDirectoryConfiguration.getDeploymentDir()+ SEPARATOR;
    final private static String TEMP_FILE = LocalDirectoryConfiguration.getTempDir() + SEPARATOR;
    final private static String SHELLSCRIPT_FILE = LocalDirectoryConfiguration.getManifastTemplateDir() + SEPARATOR  + "diego" + SEPARATOR;
    final private static Logger LOGGER = LoggerFactory.getLogger(HbDiegoService.class);
    
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Diego 설치 목록 조회
     * @title : getDiegoInfoList
     * @return : ResponseEntity
    *****************************************************************/
    public List<HbDiegoVO> getDiegoInfoList(String installStatus) {
        return dao.selectHbDiegoInfoList(installStatus);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Diego 정보 저장
     * @title : saveDiegoInfo
     * @return : ResponseEntity
    *****************************************************************/
    public void saveDiegoInfo(HbDiegoDTO dto, Principal principal) {
        HbDiegoVO vo = null;
        
        int count = dao.selectHbDiegoInfoByName(dto.getDiegoConfigName());
        
        if(StringUtils.isEmpty(dto.getId())) {
            vo = new HbDiegoVO();
            vo.setCreateUserId(principal.getName());
            if(count > 0){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        } else {
            vo = dao.selectHbDiegoInfoById(Integer.parseInt(dto.getId()));
            if(!dto.getDiegoConfigName().equals(vo.getDiegoConfigName()) && count > 0){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        }
        String deploymentFile = null;
        if(vo != null){
            vo.setDiegoConfigName(dto.getDiegoConfigName());
            vo.setDefaultConfigInfo(dto.getDefaultConfigInfo());
            vo.setNetworkConfigInfo(dto.getNetworkConfigInfo());
            vo.setIaasType(dto.getIaasType());
            vo.setResourceConfigInfo(dto.getResourceConfigInfo());
            vo.setInstanceConfigInfo(dto.getInstanceConfigInfo());
            deploymentFile = makeDeploymentName(vo);
            vo.setDeploymentFile(deploymentFile);
        }
        
        if(StringUtils.isEmpty(dto.getId())) {
            dao.insertHbDiegoInfo(vo);
            try{
                HbDiegoVO hbDiegoVo = dao.selectHbDiegoInfoById(vo.getId());
                createSettingFile(hbDiegoVo);
            }catch (Exception e) {
                dao.deleteHbDiegoInfo(vo.getId());
                throw new CommonException(setMessageSourceValue("common.badRequest.exception.code"), 
                        "배포 파일 생성 실패,<br>CF 정보를 확인해주세요.", HttpStatus.NOT_FOUND);
            }
        }else {
            dao.updateHbDiegoInfo(vo);
            try{
                createSettingFile(vo);
            }catch (Exception e) {
                throw new CommonException(setMessageSourceValue("common.badRequest.exception.code"), 
                        "배포 파일 생성 실패,<br>CF 정보를 확인해주세요.", HttpStatus.NOT_FOUND);
            }
        }
    }
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : 입력 정보를 바탕으로 manifest 파일 생성 및 배포 파일명 응답  
     * @title : createSettingFile
     * @return : void
    *****************************************************************/
    public void createSettingFile(HbDiegoVO vo) {
        String content = "";
        ManifestTemplateVO result = null;
        InputStream inputs  = null;
        
        try {
            result = commonDao.selectManifetTemplate(vo.getIaasType().toLowerCase(), vo.getDefaultConfigVO().getDiegoReleaseVersion(), "DIEGO",vo.getDefaultConfigVO().getDiegoReleaseName());
            vo.setNetworks(networkDao.selectDiegoNetworkConfigInfoByNameResultVo(vo.getNetworkConfigInfo()));
            ManifestTemplateVO manifestTemplate = null;
            if(result != null){
                inputs =  this.getClass().getClassLoader().getResourceAsStream("static/deploy_template/diego/"+result.getTemplateVersion()+ SEPARATOR + vo.getIaasType().toLowerCase() + SEPARATOR +result.getInputTemplate());
                content = IOUtils.toString(inputs, "UTF-8");
                manifestTemplate = new ManifestTemplateVO();
                manifestTemplate = setOptionManifestTemplateInfo(result, manifestTemplate, vo);
                manifestTemplate.setMinReleaseVersion(result.getTemplateVersion());
                manifestTemplate.setDeployType("DIEGO");
                manifestTemplate.setIaasType(vo.getIaasType());
            }else {
                throw new CommonException(setMessageSourceValue("common.badRequest.exception.code"),
                        setMessageSourceValue("common.notFound.template.message"), HttpStatus.BAD_REQUEST);
            }
            List<ReplaceItemDTO> replaceItems = setReplaceItems(vo);
            for (ReplaceItemDTO item : replaceItems) {
                System.out.println(item.getTargetItem() + "==="+ item.getSourceItem());
                content = content.replace(item.getTargetItem(), item.getSourceItem());
            }
            if( LOGGER.isDebugEnabled() ) {
                LOGGER.debug("content: " + content);
            }
            IOUtils.write(content, new FileOutputStream(TEMP_FILE+ vo.getDeploymentFile()), "UTF-8");
            CommonHbDeployUtils.setHbShellScript(vo.getDeploymentFile(),  manifestTemplate, vo);
        } catch (IOException e) {
            throw new CommonException(setMessageSourceValue("common.badRequest.exception.code"),
                    setMessageSourceValue("common.badRequest.message"), HttpStatus.BAD_REQUEST);
        } catch(NullPointerException e){
            throw new CommonException(setMessageSourceValue("common.badRequest.exception.code"),
                    setMessageSourceValue("common.badRequest.message"), HttpStatus.BAD_REQUEST);
        }
    }
    
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : 입력한 정보를 ReplaceItemDTO 목록에 넣고 setReplaceItems 메소드에 응답
     * @title : setReplaceItems
     * @return : List<ReplaceItemDTO>
    *****************************************************************/
    public List<ReplaceItemDTO> setReplaceItems(HbDiegoVO vo) {
        //1.1 기본정보
        List<ReplaceItemDTO> items = new ArrayList<ReplaceItemDTO>();
        items.add(new ReplaceItemDTO("[diegoReleaseName]", vo.getDefaultConfigVO().getDiegoReleaseName()));
        items.add(new ReplaceItemDTO("[diegoReleaseVersion]", "\"" + vo.getDefaultConfigVO().getDiegoReleaseVersion() + "\""));
        items.add(new ReplaceItemDTO("[gardenLinuxReleaseName]", vo.getDefaultConfigVO().getGardenReleaseName()));
        items.add(new ReplaceItemDTO("[gardenLinuxReleaseVersion]", "\"" + vo.getDefaultConfigVO().getGardenReleaseVersion() + "\""));
        if(vo.getDefaultConfigVO().getCflinuxfs2rootfsreleaseName()!=null && !vo.getDefaultConfigVO().getCflinuxfs2rootfsreleaseName().equalsIgnoreCase("") 
                && vo.getDefaultConfigVO().getCflinuxfs2rootfsreleaseVersion() != null && !vo.getDefaultConfigVO().getCflinuxfs2rootfsreleaseVersion().equalsIgnoreCase("")){
            items.add(new ReplaceItemDTO("[cflinuxfs2RootfsReleaseName]", vo.getDefaultConfigVO().getCflinuxfs2rootfsreleaseName()));
            items.add(new ReplaceItemDTO("[cflinuxfs2RootfsReleaseVersion]", ("\"" + vo.getDefaultConfigVO().getCflinuxfs2rootfsreleaseVersion()+"\"").trim()));
        }else{
            items.add(new ReplaceItemDTO("[cflinuxfs2RootfsReleaseName]", "\"" + "" + "\""));
            items.add(new ReplaceItemDTO("[cflinuxfs2RootfsReleaseVersion]", "\"" + "" + "\""));
        }
        items.add(new ReplaceItemDTO("[cadvisorDriverIp]", vo.getDefaultConfigVO().getIngestorIp()));
        for( int i=0; i<vo.getNetworks().size(); i++ ){
            if( "internal".equalsIgnoreCase(vo.getNetworks().get(i).getNet())){
                if(i  == 0 ){
                    items.add(new ReplaceItemDTO("[subnetRange]", vo.getNetworks().get(i).getSubnetRange()));
                    items.add(new ReplaceItemDTO("[subnetGateway]", vo.getNetworks().get(i).getSubnetGateway()));
                    items.add(new ReplaceItemDTO("[subnetDns]", vo.getNetworks().get(i).getSubnetDns()));
                    items.add(new ReplaceItemDTO("[subnetReserved]", vo.getNetworks().get(i).getSubnetReservedFrom() + " - " + vo.getNetworks().get(i).getSubnetReservedTo()));
                    items.add(new ReplaceItemDTO("[subnetStatic]", vo.getNetworks().get(i).getSubnetStaticFrom() + " - " + vo.getNetworks().get(i).getSubnetStaticTo()));
                    items.add(new ReplaceItemDTO("[subnetId]", vo.getNetworks().get(i).getSubnetId()));            
                    items.add(new ReplaceItemDTO("[cloudSecurityGroups]", vo.getNetworks().get(i).getCloudSecurityGroups()));
                    if("aws".equalsIgnoreCase(vo.getIaasType())) {
                        items.add(new ReplaceItemDTO("[availabilityZone]", vo.getNetworks().get(i).getAvailabilityZone()));
                    }
                    
                }else if(i > 0){
                    items.add(new ReplaceItemDTO("[subnetRange"+i+"]", vo.getNetworks().get(i).getSubnetRange()));
                    items.add(new ReplaceItemDTO("[subnetGateway"+i+"]", vo.getNetworks().get(i).getSubnetGateway()));
                    items.add(new ReplaceItemDTO("[subnetDns"+i+"]", vo.getNetworks().get(i).getSubnetDns()));
                    items.add(new ReplaceItemDTO("[subnetReserved"+i+"]", vo.getNetworks().get(i).getSubnetReservedFrom() + " - " + vo.getNetworks().get(i).getSubnetReservedTo()));
                    items.add(new ReplaceItemDTO("[subnetStatic"+i+"]", vo.getNetworks().get(i).getSubnetStaticFrom() + " - " + vo.getNetworks().get(i).getSubnetStaticTo()));
                    items.add(new ReplaceItemDTO("[subnetId"+i+"]", vo.getNetworks().get(i).getSubnetId()));            
                    items.add(new ReplaceItemDTO("[cloudSecurityGroups"+i+"]", vo.getNetworks().get(i).getCloudSecurityGroups()));
                    if("aws".equalsIgnoreCase(vo.getIaasType())) {
                        items.add(new ReplaceItemDTO("[availabilityZone"+i+"]", vo.getNetworks().get(i).getAvailabilityZone()));
                    }
                    
                }
            }
        } 
        if( vo.getNetworks().size() == 1  ){
            //network1
            items.add(new ReplaceItemDTO("[subnetRange1]", ""));
            items.add(new ReplaceItemDTO("[subnetGateway1]", ""));
            items.add(new ReplaceItemDTO("[subnetDns1]", ""));
            items.add(new ReplaceItemDTO("[subnetReserved1]", ""));
            items.add(new ReplaceItemDTO("[subnetStatic1]", ""));
            items.add(new ReplaceItemDTO("[subnetId1]", ""));
            //network2
            items.add(new ReplaceItemDTO("[subnetRange2]", ""));
            items.add(new ReplaceItemDTO("[subnetGateway2]", ""));
            items.add(new ReplaceItemDTO("[subnetDns2]", ""));
            items.add(new ReplaceItemDTO("[subnetReserved2]", ""));
            items.add(new ReplaceItemDTO("[subnetStatic2]", ""));
            items.add(new ReplaceItemDTO("[subnetId2]", ""));
            
            items.add(new ReplaceItemDTO("[cloudSecurityGroups1]", ""));
            items.add(new ReplaceItemDTO("[cloudSecurityGroups2]", ""));
            
            items.add(new ReplaceItemDTO("[availabilityZone1]", ""));
            items.add(new ReplaceItemDTO("[availabilityZone2]", ""));
        }else if( vo.getNetworks().size() > 1 ){
            items.add(new ReplaceItemDTO("[subnetRange2]", ""));
            items.add(new ReplaceItemDTO("[subnetGateway2]", ""));
            items.add(new ReplaceItemDTO("[subnetDns2]", ""));
            items.add(new ReplaceItemDTO("[subnetReserved2]", ""));
            items.add(new ReplaceItemDTO("[subnetStatic2]", ""));
            items.add(new ReplaceItemDTO("[subnetId2]", ""));
            items.add(new ReplaceItemDTO("[cloudSecurityGroups2]", ""));
            items.add(new ReplaceItemDTO("[availabilityZone2]", ""));
        }
                
        //3.리소스 정보
        items.add(new ReplaceItemDTO("[stemcellName]", vo.getResourceConfigVO().getStemcellName() ));
        items.add(new ReplaceItemDTO("[stemcellVersion]", "\"" + vo.getResourceConfigVO().getStemcellVersion() + "\"" ));    
        items.add(new ReplaceItemDTO("[boshPassword]", Sha512Crypt.Sha512_crypt(vo.getResourceConfigVO().getBoshPassword(), RandomStringUtils.randomAlphabetic(10), 0)));
        items.add(new ReplaceItemDTO("[smallInstanceType]", vo.getResourceConfigVO().getSmallFlavor()));
        items.add(new ReplaceItemDTO("[mediumInstanceType]", vo.getResourceConfigVO().getMediumFlavor()));
        items.add(new ReplaceItemDTO("[largeInstanceType]", vo.getResourceConfigVO().getLargeFlavor()));
        items.add(new ReplaceItemDTO("[cellInstanceType]", vo.getResourceConfigVO().getLargeFlavor()));
        
        items.add(new ReplaceItemDTO("[databaseZ1]", vo.getInstanceConfigVO().getDatabase_z1()));
        items.add(new ReplaceItemDTO("[accessZ1]", vo.getInstanceConfigVO().getAccess_z1()));
        items.add(new ReplaceItemDTO("[cc_bridgeZ1]", vo.getInstanceConfigVO().getCc_bridge_z1()));
        items.add(new ReplaceItemDTO("[cellZ1]", vo.getInstanceConfigVO().getCell_z1()));
        items.add(new ReplaceItemDTO("[brainZ1]", vo.getInstanceConfigVO().getBrain_z1()));
        if(vo.getNetworks().size() == 2){
            items.add(new ReplaceItemDTO("[databaseZ2]", vo.getInstanceConfigVO().getDatabase_z2()));
            items.add(new ReplaceItemDTO("[accessZ2]", vo.getInstanceConfigVO().getAccess_z2()));
            items.add(new ReplaceItemDTO("[cc_bridgeZ2]", vo.getInstanceConfigVO().getCc_bridge_z2()));
            items.add(new ReplaceItemDTO("[cellZ2]", vo.getInstanceConfigVO().getCell_z2()));
            items.add(new ReplaceItemDTO("[brainZ2]", vo.getInstanceConfigVO().getBrain_z2()));
        }else if(vo.getNetworks().size() == 3){
            items.add(new ReplaceItemDTO("[databaseZ3]", vo.getInstanceConfigVO().getDatabase_z3()));
            items.add(new ReplaceItemDTO("[accessZ3]", vo.getInstanceConfigVO().getAccess_z3()));
            items.add(new ReplaceItemDTO("[cc_bridgeZ3]", vo.getInstanceConfigVO().getCc_bridge_z3()));
            items.add(new ReplaceItemDTO("[cellZ3]", vo.getInstanceConfigVO().getCell_z3()));
            items.add(new ReplaceItemDTO("[brainZ3]", vo.getInstanceConfigVO().getBrain_z3()));
        } else {
            items.add(new ReplaceItemDTO("[databaseZ2]", "0"));
            items.add(new ReplaceItemDTO("[accessZ2]", "0"));
            items.add(new ReplaceItemDTO("[cc_bridgeZ2]", "0"));
            items.add(new ReplaceItemDTO("[cellZ2]", "0"));
            items.add(new ReplaceItemDTO("[brainZ2]", "0"));
            items.add(new ReplaceItemDTO("[databaseZ3]", "0"));
            items.add(new ReplaceItemDTO("[accessZ3]", "0"));
            items.add(new ReplaceItemDTO("[cc_bridgeZ3]", "0"));
            items.add(new ReplaceItemDTO("[cellZ3]", "0"));
            items.add(new ReplaceItemDTO("[brainZ3]", "0"));
        }
        
        return items;
    }
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : option Manifest 템플릿 정보 설정
     * @title : setOptionManifestTemplateInfo
     * @return : ManifestTemplateVO
    *****************************************************************/
    public ManifestTemplateVO setOptionManifestTemplateInfo(ManifestTemplateVO result, ManifestTemplateVO  manifestTemplate, HbDiegoVO vo){
        HbCfVO cfVo = cfDao.selectHbCfInfoById(vo.getDefaultConfigVO().getCfId());
        //Base Template File
        if( result.getCommonBaseTemplate() !=null && !(StringUtils.isEmpty( result.getCommonBaseTemplate())) ){
            manifestTemplate.setCommonBaseTemplate( result.getCommonBaseTemplate() );
        } else{
            manifestTemplate.setCommonBaseTemplate("");
        }
        //Job Template File
        if( result.getCommonJobTemplate()!=null && !(StringUtils.isEmpty( result.getCommonJobTemplate())) ){
            manifestTemplate.setCommonJobTemplate( result.getCommonJobTemplate() );
        } else{
            manifestTemplate.setCommonJobTemplate("");
        }
        //common option Template File 
        if( "true".equals(vo.getDefaultConfigVO().getPaastaMonitoringUse().toLowerCase()) && result.getCommonOptionTemplate() != null  && !(StringUtils.isEmpty( result.getCommonOptionTemplate())) ){
            manifestTemplate.setCommonOptionTemplate( result.getCommonOptionTemplate() );
        } else{
            manifestTemplate.setCommonOptionTemplate("");
        }
        //iaas Property Template File
        if( result.getIaasPropertyTemplate() != null && !(StringUtils.isEmpty( result.getIaasPropertyTemplate())) ){
            manifestTemplate.setIaasPropertyTemplate(vo.getIaasType().toLowerCase() +SEPARATOR+ result.getIaasPropertyTemplate() );
        } else{
            manifestTemplate.setIaasPropertyTemplate("");
        }
        //네트워크를 추가할 경우(2개 이상)
        if( vo.getNetworks().size() > 1 && result.getOptionNetworkTemplate() != null && !StringUtils.isEmpty( result.getOptionNetworkTemplate()) ){
            manifestTemplate.setOptionNetworkTemplate(vo.getIaasType().toLowerCase() +SEPARATOR+ result.getOptionNetworkTemplate() );
        } else{
            manifestTemplate.setOptionNetworkTemplate("");
        }
        //option resource Template File 
        if( result.getCommonOptionTemplate() != null  && !(StringUtils.isEmpty( result.getCommonOptionTemplate())) ){
            manifestTemplate.setOptionResourceTemplate( result.getOptionResourceTemplate() );
        } else{
            manifestTemplate.setOptionResourceTemplate("");    
        }
        //option etc Template File(Network 3개 일 경우)
        if( result.getOptionEtc() != null && vo.getNetworks().size() == 3 && !(StringUtils.isEmpty( result.getOptionEtc())) ){
            manifestTemplate.setOptionEtc( vo.getIaasType().toLowerCase() +SEPARATOR+ result.getOptionEtc() );
        } else{
            manifestTemplate.setOptionEtc("");    
        }
        //meta Template File
        if( result.getMetaTemplate() != null && !(StringUtils.isEmpty( result.getMetaTemplate())) ) {
            manifestTemplate.setMetaTemplate(vo.getIaasType().toLowerCase() +SEPARATOR+ result.getMetaTemplate());
        } else{
            manifestTemplate.setMetaTemplate("");
        }
         //임시 CF 파일 경로 + 명
        manifestTemplate.setCfTempleate( CF_FILE +cfVo.getDeploymentFile());  
        //shell script File
        manifestTemplate.setShellScript(SHELLSCRIPT_FILE + "generate_diego_manifest");
        
        return manifestTemplate;
    }
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : 배포 파일명 설정
     * @title : makeDeploymentName
     * @return : String
    *****************************************************************/
    public String makeDeploymentName(HbDiegoVO vo ){
        String settingFileName = "";
        if(vo.getIaasType() != null){
            settingFileName = vo.getIaasType().toLowerCase() + "-hybrid-diego-"+ vo.getDiegoConfigName() +".yml";
        }else{
            throw new CommonException(message.getMessage("common.badrequest.exception.code", null, Locale.KOREA),
                    "배포 파일 명 생성 중 예외가 발생했습니다.", HttpStatus.BAD_REQUEST);
        }
        return settingFileName;
    }
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : message 값 가져오기
     * @title : setMessageSourceValue
     * @return : String
    *****************************************************************/
    public String setMessageSourceValue(String name){
        return message.getMessage(name, null, Locale.KOREA);
    }
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : Diego 정보 삭제
     * @title : deleteDiegoInfo
     * @return : void
    *****************************************************************/
    public void deleteDiegoInfo(HbDiegoDTO dto) {
        if(StringUtils.isEmpty(dto.getId())){
            throw new CommonException(message.getMessage("common.badrequest.exception.code", null, Locale.KOREA),
                    "배포 파일 명 생성 중 예외가 발생했습니다.", HttpStatus.BAD_REQUEST);
        }
        dao.deleteHbDiegoInfo(Integer.parseInt(dto.getId()));
    }

}
