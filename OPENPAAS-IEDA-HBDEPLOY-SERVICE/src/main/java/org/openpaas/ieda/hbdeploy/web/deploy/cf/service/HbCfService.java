package org.openpaas.ieda.hbdeploy.web.deploy.cf.service;

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
import org.openpaas.ieda.deploy.web.common.service.CommonDeployUtils;
import org.openpaas.ieda.deploy.web.deploy.cf.service.CfService;
import org.openpaas.ieda.hbdeploy.web.config.setting.dao.HbDirectorConfigDAO;
import org.openpaas.ieda.hbdeploy.web.config.setting.dao.HbDirectorConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfNetworkConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HbCfService {
	
    @Autowired private HbCfDAO dao;
    @Autowired private MessageSource message;
    @Autowired private HbCfNetworkConfigDAO networkDao;
    @Autowired private CommonDeployDAO commonDao;
    @Autowired private HbDirectorConfigDAO directorDao;
    
    final private static String SEPARATOR = System.getProperty("file.separator");
    final private static String MANIFEST_TEMPLATE_LOCATION = LocalDirectoryConfiguration.getManifastTemplateDir() + SEPARATOR +"cf" + SEPARATOR;
    final private static String TEMP_DIR = LocalDirectoryConfiguration.getTempDir();
    final private static Logger LOGGER = LoggerFactory.getLogger(CfService.class);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 정보 목록 전체 조회
     * @title : getCfInfoList
     * @return : List<HbCfVO>
    *****************************************************************/
    public List<HbCfVO> getCfInfoList(String installStatus) {
        List<HbCfVO> list = dao.selectHbCfInfoList(installStatus);
        return list;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 정보 저장
     * @title : saveCfInfo
     * @return : void
    *****************************************************************/
    public void saveCfInfo(HbCfDTO dto, Principal principal) {
        HbCfVO vo = null;
        
        int count = dao.selectHbCfInfoByName(dto.getCfConfigName());
        
        if(StringUtils.isEmpty(dto.getId())) {
            vo = new HbCfVO();
            vo.setCreateUserId(principal.getName());
            if(count > 0){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        } else {
            vo = dao.selectHbCfInfoById(Integer.parseInt(dto.getId()));
            if(!dto.getCfConfigName().equals(vo.getCfConfigName()) && count > 0){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        }
        String deploymentFile = null;
        if(vo != null){
            vo.setCfConfigName(dto.getCfConfigName());
            vo.setDefaultConfigInfo(dto.getDefaultConfigInfo());
            vo.setNetworkConfigInfo(dto.getNetworkConfigInfo());
            vo.setKeyConfigInfo(dto.getKeyConfigInfo());
            vo.setIaasType(dto.getIaasType());
            vo.setResourceConfigInfo(dto.getResourceConfigInfo());
            vo.setInstanceConfigInfo(dto.getInstanceConfigInfo());
            deploymentFile = makeDeploymentName(vo);
            vo.setDeploymentFile(deploymentFile);
        }
        if(StringUtils.isEmpty(dto.getId())) {
            dao.insertHbCfInfo(vo);
            try{
                HbCfVO hbDiegoVo = dao.selectHbCfInfoById(vo.getId());
                createSettingFile(hbDiegoVo);
            }catch (Exception e) {
                dao.deleteHbCfInfo(vo.getId());
                throw new CommonException(setMessageSourceValue("common.badRequest.exception.code"), 
                        "배포 파일 생성 실패,<br>CF 정보를 확인해주세요.", HttpStatus.NOT_FOUND);
            }
        }else {
            dao.updateHbCfInfo(vo);
            try{
                createSettingFile(vo);
            }catch (Exception e) {
                throw new CommonException(setMessageSourceValue("common.badRequest.exception.code"), 
                        "배포 파일 생성 실패,<br>CF 정보를 확인해주세요.", HttpStatus.NOT_FOUND);
            }
        }

    }
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 배포 파일명 설정
     * @title : makeDeploymentName
     * @return : String
    *****************************************************************/
    public String makeDeploymentName(HbCfVO vo ){
        String settingFileName = "";
        if(vo.getIaasType() != null){
            settingFileName = vo.getIaasType().toLowerCase() + "-hybrid-cf-"+ vo.getCfConfigName() +".yml";
        }else{
            throw new CommonException(message.getMessage("common.badrequest.exception.code", null, Locale.KOREA),
                    "배포 파일 명 생성 중 예외가 발생했습니다.", HttpStatus.BAD_REQUEST);
        }
        return settingFileName;
    }
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : CF 상세 조회
     * @title : getCfInfo
     * @return : HbCfVO
    *****************************************************************/
    public HbCfVO getCfInfo(int id) {
        HbCfVO vo = dao.selectHbCfInfoById(id);
        if(vo == null){
            throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                    "배포 파일 생성 중 예외가 발생했습니다.", HttpStatus.BAD_REQUEST);
        }
        return vo;
    }

    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : CF 배포 파일 생성
     * @title : createSettingFile
     * @return : void
    *****************************************************************/
    public void createSettingFile(HbCfVO vo) {
        String content = "";
        ManifestTemplateVO result = null;
        InputStream inputs  = null;
        try {
            //1. get Manifest Template info
            result = commonDao.selectManifetTemplate(vo.getIaasType(), vo.getDefaultConfigVO().getReleaseVersion(), "CF", vo.getDefaultConfigVO().getReleaseName());
            
            vo.setNetworks(networkDao.selectCfDefaultConfigInfoByNameResultVo(vo.getNetworkConfigInfo()));
            
            ManifestTemplateVO manifestTemplate = null;
            if(result != null){
                inputs =  this.getClass().getClassLoader().getResourceAsStream("static/deploy_template/cf/"+ result.getTemplateVersion()  + "/" + vo.getIaasType().toLowerCase() + "/" +result.getInputTemplate());
                content = IOUtils.toString(inputs, "UTF-8");
                
                manifestTemplate = new ManifestTemplateVO();
                manifestTemplate = setOptionManifestTemplateInfo(result, manifestTemplate, vo);
                manifestTemplate.setDeployType("CF");
                manifestTemplate.setIaasType(vo.getIaasType());
            }else {
                throw new CommonException(setMessageSourceValue("common.badRequest.exception.code"), 
                        setMessageSourceValue("common.badRequest.manifest_template.message"), HttpStatus.BAD_REQUEST);
            }
            List<ReplaceItemDTO> replaceItems = setReplaceItems(vo, vo.getIaasType());
            for (ReplaceItemDTO item : replaceItems) {
                content = content.replace(item.getTargetItem(), item.getSourceItem());
                
            }
            if( LOGGER.isDebugEnabled() ) {
                LOGGER.debug("content: " + content);
            }
            
            IOUtils.write(content, new FileOutputStream(TEMP_DIR + SEPARATOR + vo.getDeploymentFile()), "UTF-8");
            CommonDeployUtils.setSpiffMerge(vo.getKeyConfigVO().getKeyFileName(),  vo.getDeploymentFile(),  manifestTemplate, vo.getDefaultConfigVO().getPaastaMonitoringUse());
        } catch (IOException e) {
            throw new CommonException(setMessageSourceValue("common.internalServerError.exception.code"), 
                    setMessageSourceValue("common.file.internalServerError.message"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NullPointerException e){
            throw new CommonException(setMessageSourceValue("common.badRequest.exception.code"), 
                    "배포 파일 생성 실패,<br>CF 정보를 확인해주세요.", HttpStatus.NOT_FOUND);
        } catch(Exception e){
            throw new CommonException(setMessageSourceValue("common.badRequest.exception.code"), 
                    setMessageSourceValue("common.badRequest.message"), HttpStatus.BAD_REQUEST);
        } 
    }
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : Option Manifest 템플릿 정보 설정
     * @title : setOptionManifestTemplateInfo
     * @return : ManifestTemplateVO
    *****************************************************************/
    public ManifestTemplateVO setOptionManifestTemplateInfo(ManifestTemplateVO result, ManifestTemplateVO  manifestTemplate, HbCfVO vo){
        //Base Template File
        if(result.getCommonBaseTemplate() != null && !(StringUtils.isEmpty( result.getCommonBaseTemplate()) )){
            manifestTemplate.setCommonBaseTemplate( MANIFEST_TEMPLATE_LOCATION + result.getTemplateVersion() + SEPARATOR + "common" + SEPARATOR  +  result.getCommonBaseTemplate());
        }else{
            manifestTemplate.setCommonBaseTemplate("");
        }
        //Job Template File
        if(result.getCommonJobTemplate() != null && !(StringUtils.isEmpty( result.getCommonJobTemplate()) )){
            if(vo.getIaasType().equalsIgnoreCase("google")){
                manifestTemplate.setCommonJobTemplate(  MANIFEST_TEMPLATE_LOCATION + result.getTemplateVersion() + SEPARATOR + "common" + SEPARATOR  +  "google_cf.yml"  );
            }else{
                manifestTemplate.setCommonJobTemplate(  MANIFEST_TEMPLATE_LOCATION + result.getTemplateVersion() + SEPARATOR + "common" + SEPARATOR  +  result.getCommonJobTemplate() );
            }
        }else{
            manifestTemplate.setCommonJobTemplate("");
        }
        //meta Template File
        if(result.getMetaTemplate() != null  && !(StringUtils.isEmpty( result.getMetaTemplate()) )){
            manifestTemplate.setMetaTemplate(MANIFEST_TEMPLATE_LOCATION + result.getTemplateVersion() + SEPARATOR + vo.getIaasType().toLowerCase() + SEPARATOR  +  result.getMetaTemplate());
        }else{
            manifestTemplate.setMetaTemplate("");
        }
        //iaas Property Template File
        if(result.getIaasPropertyTemplate() != null  && !(StringUtils.isEmpty( result.getIaasPropertyTemplate()) )){
            manifestTemplate.setIaasPropertyTemplate(MANIFEST_TEMPLATE_LOCATION + result.getTemplateVersion() + SEPARATOR + vo.getIaasType().toLowerCase() + SEPARATOR + result.getIaasPropertyTemplate() );
        }else{
            manifestTemplate.setIaasPropertyTemplate("");
        }
        //네트워크를 추가할 경우(2개 이상)
        if( vo.getNetworks().size() >2 && result.getOptionNetworkTemplate() != null  && !(StringUtils.isEmpty( result.getOptionNetworkTemplate()) )){
            manifestTemplate.setOptionNetworkTemplate(MANIFEST_TEMPLATE_LOCATION + result.getTemplateVersion() + SEPARATOR + vo.getIaasType().toLowerCase() + SEPARATOR + result.getOptionNetworkTemplate() );
        }else{
            manifestTemplate.setOptionNetworkTemplate("");
        }
        //resource Template File 
        if( vo.getInstanceConfigVO() !=null && result.getOptionResourceTemplate() != null && !(StringUtils.isEmpty( result.getOptionResourceTemplate())) ){
            manifestTemplate.setOptionResourceTemplate(MANIFEST_TEMPLATE_LOCATION + result.getTemplateVersion() + SEPARATOR + vo.getIaasType().toLowerCase() + SEPARATOR + result.getOptionResourceTemplate() );
        }else{
            manifestTemplate.setOptionResourceTemplate("");
        }
        //option etc Template File
        if( ( vo.getDefaultConfigVO().getPaastaMonitoringUse() != null && "true".equals(vo.getDefaultConfigVO().getPaastaMonitoringUse())) && result.getCommonOptionTemplate() != null  && !(StringUtils.isEmpty( result.getCommonOptionTemplate()) )){
            manifestTemplate.setCommonOptionTemplate(MANIFEST_TEMPLATE_LOCATION + result.getTemplateVersion() + SEPARATOR + "common" + SEPARATOR + result.getCommonOptionTemplate() );
        }else{
            manifestTemplate.setCommonOptionTemplate("");
        }
        //diego use Template File
        if( result.getOptionEtc() != null  && !(StringUtils.isEmpty( result.getOptionEtc()) )){
            manifestTemplate.setOptionEtc(MANIFEST_TEMPLATE_LOCATION + result.getTemplateVersion() + SEPARATOR + "common" + SEPARATOR + result.getOptionEtc() );
        }else{
            manifestTemplate.setOptionEtc("");
        }
        
        return manifestTemplate;
    }

    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : 입력한 정보를 ReplaceItemDTO에 설정
     * @title : setReplaceItems
     * @return : List<ReplaceItemDTO>
    *****************************************************************/
    public List<ReplaceItemDTO> setReplaceItems(HbCfVO vo, String iaas) {
        List<ReplaceItemDTO> items = new ArrayList<ReplaceItemDTO>();
        // 1 Deployment 정보
        items.add(new ReplaceItemDTO("[deploymentName]", vo.getDefaultConfigVO().getDeploymentName()));
        HbDirectorConfigVO directorVO = directorDao.selectHbDirectorConfigBySeq(vo.getDefaultConfigVO().getDirectorId());
        items.add(new ReplaceItemDTO("[directorUuid]", directorVO.getDirectorUuid()));
        items.add(new ReplaceItemDTO("[releaseName]", vo.getDefaultConfigVO().getReleaseName()));
        items.add(new ReplaceItemDTO("[releaseVersion]",  "\"" +vo.getDefaultConfigVO().getReleaseVersion() + "\""));
        
        items.add(new ReplaceItemDTO("[loggregatorReleaseName]", vo.getDefaultConfigVO().getLoggregatorReleaseName()));
        items.add(new ReplaceItemDTO("[loggregatorReleaseVersion]",  "\"" +vo.getDefaultConfigVO().getLoggregatorReleaseVersion() + "\""));
        
        // 2 기본정보
        setReplaceItemsDefaultInfo(vo, items);
        
        // 3. 네트워크 정보
        setReplaceItemsFromNetworkInfo(vo, items);
        
        // 4. 리소스 정보
        setReplaceItemsFromResourceInfo(vo, items);
        
        //5. 고급 설정 정보
        setReplaceItmesFromJobsInfo(vo, items);
        return items;
    }
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : 인스턴스 정보 ReplaceItemDTO에 설정
     * @title : setReplaceItemsDefaultInfo
     * @return : List<ReplaceItemDTO>
    *****************************************************************/
    private void setReplaceItmesFromJobsInfo(HbCfVO vo, List<ReplaceItemDTO> items) {
        items.add(new ReplaceItemDTO("[natsZ1]", vo.getInstanceConfigVO().getNats_z1()));
        items.add(new ReplaceItemDTO("[blobstoreZ1]", vo.getInstanceConfigVO().getBlobstore_z1()));
        items.add(new ReplaceItemDTO("[routerZ1]", vo.getInstanceConfigVO().getRouter_z1()));
        items.add(new ReplaceItemDTO("[dopplerZ1]", vo.getInstanceConfigVO().getDoppler_z1()));
        items.add(new ReplaceItemDTO("[loggregator_trafficcontrollerZ1]", vo.getInstanceConfigVO().getLoggregator_z1()));
        items.add(new ReplaceItemDTO("[etcdZ1]", vo.getInstanceConfigVO().getEtcd_z1()));
        items.add(new ReplaceItemDTO("[consulZ1]", vo.getInstanceConfigVO().getConsul_z1()));
        items.add(new ReplaceItemDTO("[clockZ1]", vo.getInstanceConfigVO().getClock_z1()));
        if(vo.getNetworks().size() > 2) {
            items.add(new ReplaceItemDTO("[natsZ2]", vo.getInstanceConfigVO().getNats_z2()));
            items.add(new ReplaceItemDTO("[blobstoreZ2]", vo.getInstanceConfigVO().getBlobstore_z2()));
            items.add(new ReplaceItemDTO("[routerZ2]", vo.getInstanceConfigVO().getRouter_z2()));
            items.add(new ReplaceItemDTO("[dopplerZ2]", vo.getInstanceConfigVO().getDoppler_z2()));
            items.add(new ReplaceItemDTO("[loggregator_trafficcontrollerZ2]", vo.getInstanceConfigVO().getLoggregator_z2()));
            items.add(new ReplaceItemDTO("[etcdZ2]", vo.getInstanceConfigVO().getEtcd_z2()));
            items.add(new ReplaceItemDTO("[consulZ2]", vo.getInstanceConfigVO().getConsul_z2()));
            items.add(new ReplaceItemDTO("[clockZ2]", vo.getInstanceConfigVO().getClock_z2()));
        } else {
            items.add(new ReplaceItemDTO("[natsZ2]", "0"));
            items.add(new ReplaceItemDTO("[blobstoreZ2]", "0"));
            items.add(new ReplaceItemDTO("[routerZ2]", "0"));
            items.add(new ReplaceItemDTO("[dopplerZ2]", "0"));
            items.add(new ReplaceItemDTO("[loggregator_trafficcontrollerZ2]", "0"));
            items.add(new ReplaceItemDTO("[etcdZ2]", "0"));
            items.add(new ReplaceItemDTO("[consulZ2]", "0"));
            items.add(new ReplaceItemDTO("[clockZ2]", "0"));
        }
        
    }

    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : 기본 정보 ReplaceItemDTO에 설정
     * @title : setReplaceItemsDefaultInfo
     * @return : List<ReplaceItemDTO>
    *****************************************************************/
    public List<ReplaceItemDTO> setReplaceItemsDefaultInfo(HbCfVO vo,List<ReplaceItemDTO> items){
        items.add(new ReplaceItemDTO("[domain]", vo.getDefaultConfigVO().getDomain()));
        items.add(new ReplaceItemDTO("[domainOrganization]", vo.getDefaultConfigVO().getDomainOrganization()));
        items.add(new ReplaceItemDTO("[loginSecret]", vo.getDefaultConfigVO().getLoginSecret()));
        //핑커프린트(diego 연동 유무)
        items.add(new ReplaceItemDTO("[appSshFingerprint]", ""));
        items.add(new ReplaceItemDTO("[description]", ""));
        items.add(new ReplaceItemDTO("[ingestorIp]", vo.getDefaultConfigVO().getIngestorIp()));
        return items;
    }
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 네트워크 정보 ReplaceItemDTO에 Replace
     * @title : setReplaceItemsFromNetworkInfo
     * @return : List<ReplaceItemDTO>
    *****************************************************************/
    public List<ReplaceItemDTO> setReplaceItemsFromNetworkInfo(HbCfVO vo, List<ReplaceItemDTO> items){
        int internalCnt = 0;
        for( int i=0; i<vo.getNetworks().size(); i++ ){
            if( "INTERNAL".equalsIgnoreCase(vo.getNetworks().get(i).getNet()) ){
                internalCnt ++;
                if(internalCnt  == 1 ){
                    items.add(new ReplaceItemDTO("[subnetRange]", vo.getNetworks().get(i).getSubnetRange()));
                    items.add(new ReplaceItemDTO("[subnetGateway]", vo.getNetworks().get(i).getSubnetGateway()));
                    items.add(new ReplaceItemDTO("[subnetDns]", vo.getNetworks().get(i).getSubnetDns()));
                    items.add(new ReplaceItemDTO("[subnetReserved]", vo.getNetworks().get(i).getSubnetReservedFrom() + " - " + vo.getNetworks().get(i).getSubnetReservedTo()));
                    items.add(new ReplaceItemDTO("[subnetStatic]", vo.getNetworks().get(i).getSubnetStaticFrom() + " - " + vo.getNetworks().get(i).getSubnetStaticTo()));
                    items.add(new ReplaceItemDTO("[cloudNetId]", vo.getNetworks().get(i).getSubnetId()));            
                    if( !("VSPHERE".equalsIgnoreCase(vo.getIaasType())) ){
                        items.add(new ReplaceItemDTO("[cloudSecurityGroups]", vo.getNetworks().get(i).getCloudSecurityGroups()));
                        if("AWS".equalsIgnoreCase(vo.getIaasType())){
                            items.add(new ReplaceItemDTO("[availabilityZone]", vo.getNetworks().get(i).getAvailabilityZone()));
                        }
                    }
                }else if( internalCnt > 1){
                    items.add(new ReplaceItemDTO("[subnetRange1]", vo.getNetworks().get(i).getSubnetRange()));
                    items.add(new ReplaceItemDTO("[subnetGateway1]", vo.getNetworks().get(i).getSubnetGateway()));
                    items.add(new ReplaceItemDTO("[subnetDns1]", vo.getNetworks().get(i).getSubnetDns()));
                    items.add(new ReplaceItemDTO("[subnetReserved1]", vo.getNetworks().get(i).getSubnetReservedFrom() + " - " + vo.getNetworks().get(i).getSubnetReservedTo()));
                    items.add(new ReplaceItemDTO("[subnetStatic1]", vo.getNetworks().get(i).getSubnetStaticFrom() + " - " + vo.getNetworks().get(i).getSubnetStaticTo()));
                    items.add(new ReplaceItemDTO("[cloudNetId1]", vo.getNetworks().get(i).getSubnetId()));
                    if( !("VSPHERE".equalsIgnoreCase(vo.getIaasType())) ){
                        items.add(new ReplaceItemDTO("[cloudSecurityGroups1]", vo.getNetworks().get(i).getCloudSecurityGroups()));
                        if("AWS".equalsIgnoreCase(vo.getIaasType())){
                            items.add(new ReplaceItemDTO("[availabilityZone1]", vo.getNetworks().get(i).getAvailabilityZone()));
                        }
                    }
                }
            } else {
                items.add(new ReplaceItemDTO("[proxyStaticIps]", vo.getNetworks().get(i).getPublicStaticIP()) );
            }
        }
        if( internalCnt < 2 ){
            items.add(new ReplaceItemDTO("[subnetRange1]", ""));
            items.add(new ReplaceItemDTO("[subnetGateway1]", ""));
            items.add(new ReplaceItemDTO("[subnetDns1]", ""));
            items.add(new ReplaceItemDTO("[subnetReserved1]", ""));
            items.add(new ReplaceItemDTO("[subnetStatic1]", ""));
            items.add(new ReplaceItemDTO("[networkName1]", ""));
            items.add(new ReplaceItemDTO("[cloudNetId1]", ""));
            items.add(new ReplaceItemDTO("[cloudSecurityGroups1]", ""));
            items.add(new ReplaceItemDTO("[availabilityZone1]", ""));
        }
        
        return items;
    }
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : 리소스 정보 ReplaceItemDTO에 Replace
     * @title : setReplaceItemsFromResourceInfo
     * @return : List<ReplaceItemDTO>
    *****************************************************************/
    public List<ReplaceItemDTO> setReplaceItemsFromResourceInfo(HbCfVO vo, List<ReplaceItemDTO> items){
        items.add(new ReplaceItemDTO("[stemcellName]", vo.getResourceConfigVO().getStemcellName() ));
        items.add(new ReplaceItemDTO("[stemcellVersion]", "\"" + vo.getResourceConfigVO().getStemcellVersion() + "\"" ));
        items.add(new ReplaceItemDTO("[boshPassword]", Sha512Crypt.Sha512_crypt(vo.getResourceConfigVO().getBoshPassword(), RandomStringUtils.randomAlphabetic(10), 0)));
        items.add(new ReplaceItemDTO("[smallInstanceType]", vo.getResourceConfigVO().getSmallFlavor()));
        items.add(new ReplaceItemDTO("[mediumInstanceType]", vo.getResourceConfigVO().getMediumFlavor()));
        items.add(new ReplaceItemDTO("[largeInstanceType]", vo.getResourceConfigVO().getLargeFlavor()));
        return items;
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
     * @description : CF 정보 삭제
     * @title : deleteCfInfo
     * @return : void
    *****************************************************************/
    public void deleteCfInfo(HbCfDTO dto) {
        if(StringUtils.isEmpty(dto.getId())){
            throw new CommonException(setMessageSourceValue("common.badRequest.exception.code"), 
                    setMessageSourceValue("common.badRequest.message"), HttpStatus.BAD_REQUEST);
        }
        dao.deleteHbCfInfo(Integer.parseInt(dto.getId()));
    }
}
