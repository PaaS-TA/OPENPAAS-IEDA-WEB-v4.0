package org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.Locale;

import javax.transaction.Transactional;

import org.hsqldb.lib.StringUtil;
import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dao.HbBootstrapDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dao.HbBootstrapVO;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dto.HbBootStrapDeployDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HbBootstrapSaveService {

    @Autowired private MessageSource message;
    @Autowired private HbBootstrapDAO bootstrapDao;
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 인프라 환경 설정 정보 등록/수정
     * @title : saveIaasConfigInfo
     * @return : BootstrapVO
     * @throws NoSuchAlgorithmException 
     * @throws InterruptedException 
     * @throws IOException 
    *****************************************************************/
    @Transactional
    public HbBootstrapVO saveIaasConfigInfo(HbBootStrapDeployDTO.IaasConfig dto, Principal principal) {
        HbBootstrapVO vo = null;
        HbBootstrapVO hybridvo = null;
        if( StringUtils.isEmpty(dto.getId())){
            vo = new HbBootstrapVO();
            vo.setIaasType(dto.getIaasType());
            vo.setCreateUserId(principal.getName());
            vo.setTestFlag(dto.getTestFlag());
            vo.setBootstrapType(dto.getBootstrapType());
        }else{
            vo = bootstrapDao.selectBootstrapInfo(Integer.parseInt(dto.getId()), dto.getIaasType().toLowerCase());
        }
        
        if( vo != null ){
            vo.setIaasConfigId(Integer.parseInt(dto.getIaasConfigId()));
            vo.setUpdateUserId(principal.getName());
        }else{
            throw new CommonException(message.getMessage("common.badRequest.exception.code", null, Locale.KOREA),
                    message.getMessage("common.badRequest.message", null, Locale.KOREA), HttpStatus.BAD_REQUEST);
        }
        String HybridBootstrapDbTableName = "ieda_"+dto.getBootstrapType()+"_bootstrap";
        vo.setSetHybridDbTableName(HybridBootstrapDbTableName);
        if( StringUtils.isEmpty(dto.getId()) ){
            bootstrapDao.insertBootStrapInfo(vo); // bootstrap insert 시 select primary key value가 자동적으로 vo에 저장
            int priPubBootstrapId = vo.getId();
            if("Openstack".equals(vo.getIaasType()) || "vSphere".equals(vo.getIaasType())) {
                vo.setPrivateBootStrapId(String.valueOf(priPubBootstrapId));
                if(dto.getPrivateDeploymentFileName() == null || dto.getPrivateDeploymentFileName().isEmpty()){
                    bootstrapDao.insertHybridBootstrapMgntInfo(vo); // 초기 삽입 시 private만 들어 가도록
                }
            }
            else {
                // bootstrap Openstack 정보 저장 후 AWS 정보 저장 할 경우 공통적으로 사용 하는 Key를 만들기 어려워 private cloud BOOTSTRAP에 고유 배포 파일 명으로 Select
                hybridvo = bootstrapDao.selectHbBootstrapMgntFromPrvateDeploymentFileName(dto.getPrivateDeploymentFileName()); 
                if( hybridvo == null ){
                    throw new CommonException(message.getMessage("common.badRequest.exception.code", null, Locale.KOREA),
                            message.getMessage("common.badRequest.message", null, Locale.KOREA), HttpStatus.BAD_REQUEST);
                }
                hybridvo.setPublicBootStrapId(String.valueOf(priPubBootstrapId));
                vo.setPublicBootStrapId(String.valueOf(priPubBootstrapId));
                vo.setPrivateBootStrapId(hybridvo.getPrivateBootStrapId());
                bootstrapDao.updateHybridBootstrapMgntInfo(hybridvo);
                vo.setHybridBootStrapId(hybridvo.getHybridBootStrapId());
                
            }
        }else{
            vo.setSetHybridDbTableName("ieda_"+dto.getBootstrapType()+"_bootstrap");
            bootstrapDao.updateBootStrapInfo(vo);
        }
        return vo;
    }
    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 기본 정보 저장
     * @title : saveDefaultInfo
     * @return : BootstrapVO
    ***************************************************/
    @Transactional
    public HbBootstrapVO saveDefaultInfo(HbBootStrapDeployDTO.Default dto, Principal principal) {
        if( StringUtils.isEmpty(dto.getId()) ){
            throw new CommonException(message.getMessage("common.badRequest.exception.code", null, Locale.KOREA),
                    message.getMessage("common.badRequest.message", null, Locale.KOREA), HttpStatus.BAD_REQUEST);
        }
        HbBootstrapVO vo = bootstrapDao.selectBootstrapInfo(Integer.parseInt(dto.getId()), dto.getIaasType().toLowerCase());
        if( vo != null ){
            vo.setSetHybridDbTableName("ieda_"+dto.getBootstrapType()+"_bootstrap");
            vo.setDeploymentName(dto.getDeploymentName().trim());
            vo.setDirectorName(dto.getDirectorName().trim());
            vo.setBoshRelease(dto.getBoshRelease().trim());
            vo.setCredentialKeyName(dto.getCredentialKeyName());
            vo.setNtp(dto.getNtp());
            vo.setBoshCpiRelease(dto.getBoshCpiRelease().trim());
            vo.setEnableSnapshots(dto.getEnableSnapshots().trim());
            vo.setSnapshotSchedule(dto.getSnapshotSchedule().trim());
            vo.setUpdateUserId(principal.getName());
            vo.setPaastaMonitoringUse(dto.getPaastaMonitoringUse());
            vo.setPaastaMonitoringIp(dto.getPaastaMonitoringIp());
            vo.setInfluxdbIp(dto.getInfluxdbIp());
            vo.setPaastaMonitoringRelease(dto.getPaastaMonitoringRelease());
            vo.setOsConfRelease(dto.getOsConfRelease());
        }else{
            throw new CommonException(message.getMessage("common.badRequest.exception.code", null, Locale.KOREA),
                    message.getMessage("common.badRequest.message", null, Locale.KOREA), HttpStatus.BAD_REQUEST);
        }
        bootstrapDao.updateBootStrapInfo(vo);
        return vo;
    }

    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 네트워크 정보 저장
     * @title : saveNetworkInfo
     * @return : BootstrapVO
    ***************************************************/
    @Transactional
    public HbBootstrapVO saveNetworkInfo(HbBootStrapDeployDTO.Network dto, Principal principal) {
        if( StringUtils.isEmpty(dto.getId()) ){
            throw new CommonException(message.getMessage("common.badRequest.exception.code", null, Locale.KOREA),
                    message.getMessage("common.badRequest.message", null, Locale.KOREA), HttpStatus.BAD_REQUEST);
        }
        
        HbBootstrapVO vo = bootstrapDao.selectBootstrapInfo(Integer.parseInt(dto.getId()), dto.getIaasType().toLowerCase());
        
        if( vo != null ){
            vo.setSetHybridDbTableName("ieda_"+dto.getBootstrapType()+"_bootstrap");
            vo.setSubnetId(dto.getSubnetId());
            vo.setPrivateStaticIp(dto.getPrivateStaticIp());
            vo.setPublicStaticIp(dto.getPublicStaticIp());
            vo.setSubnetRange(dto.getSubnetRange());
            vo.setSubnetGateway(dto.getSubnetGateway());
            vo.setSubnetDns(dto.getSubnetDns());
            vo.setUpdateUserId(principal.getName());
            vo.setPublicSubnetId(dto.getPublicSubnetId()); //vSphere
            vo.setPublicSubnetRange(dto.getPublicSubnetRange()); //vSphere
            vo.setPublicSubnetGateway(dto.getPublicSubnetGateway()); //vSphere
            vo.setPublicSubnetDns(dto.getPublicSubnetDns()); //vSphere
            vo.setNetworkName(dto.getNetworkName());
        }else{
            throw new CommonException(message.getMessage("common.badRequest.exception.code", null, Locale.KOREA),
                    message.getMessage("common.badRequest.message", null, Locale.KOREA), HttpStatus.BAD_REQUEST);
        }
        
        bootstrapDao.updateBootStrapInfo(vo);
        
        return vo;
    }

    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 리소스 정보 저장
     * @title : saveResourcesInfo
     * @return : BootstrapVO
    *****************************************************************/
    @Transactional
    public HbBootstrapVO saveResourceInfo(HbBootStrapDeployDTO.Resource dto, Principal principal) {
        if( StringUtils.isEmpty(dto.getId()) ){
            throw new CommonException(message.getMessage("common.badRequest.exception.code", null, Locale.KOREA),
                    message.getMessage("common.badRequest.message", null, Locale.KOREA), HttpStatus.BAD_REQUEST);
        }
        
        HbBootstrapVO bootstrapVo = bootstrapDao.selectBootstrapInfo(Integer.parseInt(dto.getId()), dto.getIaasType().toLowerCase());
        //Result Check
        if(bootstrapVo != null){
            if( StringUtil.isEmpty(bootstrapVo.getDeploymentFile()) ){
                bootstrapVo.setDeploymentFile(makeDeploymentName(bootstrapVo));
            }
            bootstrapVo.setSetHybridDbTableName("ieda_"+dto.getBootstrapType()+"_bootstrap");
            bootstrapVo.setStemcell(dto.getStemcell());
            bootstrapVo.setCloudInstanceType(dto.getCloudInstanceType());
            bootstrapVo.setBoshPassword(dto.getBoshPassword());
            bootstrapVo.setResourcePoolCpu(dto.getResourcePoolCpu());
            bootstrapVo.setResourcePoolRam(dto.getResourcePoolRam());
            bootstrapVo.setResourcePoolDisk(dto.getResourcePoolDisk());
            bootstrapVo.setUpdateUserId(principal.getName());
        }else{
            throw new CommonException(message.getMessage("common.badRequest.exception.code", null, Locale.KOREA),
                    message.getMessage("common.badRequest.message", null, Locale.KOREA), HttpStatus.BAD_REQUEST);
        }
        bootstrapDao.updateBootStrapInfo(bootstrapVo);
        return bootstrapVo;
    }    
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 배포 파일명 생성
     * @title : makeDeploymentName
     * @return : String
    *****************************************************************/
    public String makeDeploymentName(HbBootstrapVO bootstrapVo ){
        String settingFileName = "";
        if( !StringUtils.isEmpty(bootstrapVo.getIaasType()) || !StringUtils.isEmpty(bootstrapVo.getId()) ){
            settingFileName = bootstrapVo.getIaasType().toLowerCase() + "-microbosh-"+ bootstrapVo.getId() +".yml";
        }else{
            throw new CommonException(message.getMessage("common.badRequest.exception.code", null, Locale.KOREA),
                    message.getMessage("common.badRequest.message", null, Locale.KOREA), HttpStatus.BAD_REQUEST);
        }
        return settingFileName;
    }
}