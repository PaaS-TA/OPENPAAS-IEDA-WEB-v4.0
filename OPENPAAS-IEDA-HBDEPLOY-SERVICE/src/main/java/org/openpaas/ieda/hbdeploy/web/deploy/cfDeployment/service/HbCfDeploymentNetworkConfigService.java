package org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao.HbCfDeploymentNetworkConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao.HbCfDeploymentNetworkConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dto.HbCfDeploymentNetworkConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class HbCfDeploymentNetworkConfigService {
    
    @Autowired private MessageSource message;
    @Autowired private  HbCfDeploymentNetworkConfigDAO cfDeploymentNetworkDao;

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Network 정보 조회 
     * @title : getNetworkConfigInfoList selectCfDeploymentNetworkConfigInfoList
     * @return : List< HbCfDeploymentNetworkConfigVO>
    *****************************************************************/
    public List< HbCfDeploymentNetworkConfigDTO> getNetworkConfigInfoList() {
        List< HbCfDeploymentNetworkConfigDTO> networkList = new ArrayList<HbCfDeploymentNetworkConfigDTO>();
        List< HbCfDeploymentNetworkConfigVO> list = cfDeploymentNetworkDao.selectHbCfDeploymentNetworkConfigInfoList();
        if(! list.isEmpty()){
            int recid = 0;
            for( HbCfDeploymentNetworkConfigVO vo :list){
                HbCfDeploymentNetworkConfigDTO networkInfo = new HbCfDeploymentNetworkConfigDTO();
                networkInfo.setRecid(recid++);
                networkInfo.setId(vo.getId());
                networkInfo.setIaasType(vo.getIaasType());
                //external network
                networkInfo.setPublicStaticIp(vo.getPublicStaticIp());
                //internal network
                networkInfo  = setNetworkInfoList(networkInfo, vo);
                networkList.add(networkInfo);
            }
        }
        return networkList;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Network 정보 등록/수정
     * @title : saveNetworkConfigInfo
     * @return : void
    *****************************************************************/
    @Transactional
    public void saveNetworkConfigInfo(List<HbCfDeploymentNetworkConfigDTO> dto, Principal principal) {
    	List<HbCfDeploymentNetworkConfigDTO>  vo = null;
/*        int count = cfDeploymentNetworkDao.selectHbCfDeploymentNetworkConfigByName(dto.getNetworkName());
        if(dto.getId() == null){
        //if( StringUtils.isEmpty(dto.getId().toString())){
            vo = new HbCfDeploymentNetworkConfigVO();
            vo.setCreateUserId(principal.getName());
            if(count > 0){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        }else{
            vo = cfDeploymentNetworkDao.selectHbCfDeploymentNetworkConfigInfo(dto.getId(), dto.getIaasType().toLowerCase());
            if(!dto.getNetworkName().equals(vo.getNetworkName()) && count > 0){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        }
        
        if( vo != null ){
            vo.setIaasType(dto.getIaasType());
            vo.setNetworkName(dto.getNetworkName());
            vo.setPublicStaticIp(dto.getPublicStaticIp());
            
            vo.setSubnetId1(dto.getSubnetId1());
            vo.setSecurityGroup1(dto.getSecurityGroup1());
            vo.setSubnetRange1(dto.getSubnetRange1());
            vo.setSubnetGateway1(dto.getSubnetGateway());
            vo.setSubnetDns1(dto.getSubnetDns());
            vo.setSubnetReservedFrom1(dto.getSubnetReservedFrom());
            vo.setSubnetReservedTo1(dto.getSubnetReservedTo());
            vo.setSubnetStaticFrom1(dto.getSubnetStaticFrom());
            vo.setSubnetStaticTo1(dto.getSubnetStaticTo());
            if(dto.getAvailabilityZone1() != null){
                vo.setAvailabilityZone1(dto.getAvailabilityZone1());
            }
            
            if(dto.getSubnetId2() != null){
                
                vo.setSubnetId2(dto.getSubnetId2());
                vo.setSecurityGroup2(dto.getSecurityGroup2());
                vo.setSubnetRange2(dto.getSubnetRange2());
                vo.setSubnetGateway2(dto.getSubnetGateway2());
                vo.setSubnetDns2(dto.getSubnetDns2());
                vo.setSubnetReservedFrom2(dto.getSubnetReservedFrom2());
                vo.setSubnetReservedTo2(dto.getSubnetReservedTo2());
                vo.setSubnetStaticFrom2(dto.getSubnetStaticFrom2());
                vo.setSubnetStaticTo2(dto.getSubnetStaticTo2());
                
                if( dto.getAvailabilityZone2() != null){
                    vo.setAvailabilityZone2(dto.getAvailabilityZone2());
                }
            }
            
            vo.setCreateUserId(principal.getName());
            vo.setCreateDate(vo.getCreateDate());
            vo.setUpdateDate(vo.getUpdateDate());
            vo.setUpdateUserId(principal.getName());

        }
        
        if( dto.getId() == null ){
        //if( StringUtils.isEmpty(dto.getId().toString())){
            cfDeploymentNetworkDao.insertHbCfDeploymentNetworkConfigInfo(vo);
            
        }else{
            cfDeploymentNetworkDao.updateHbCfDeploymentNetworkConfigInfo(vo);
        }*/
    }
    
    
    public HbCfDeploymentNetworkConfigDTO setNetworkInfoList(HbCfDeploymentNetworkConfigDTO cfListInfo, HbCfDeploymentNetworkConfigVO vo){
        List<HbCfDeploymentNetworkConfigVO> networks = cfDeploymentNetworkDao.insertHbCfDeploymentNetworkConfigInfo(vo);
        String br = "";
        //int cnt = 0;
        String subnetRange , subnetGateway , subnetDns , subnetReservedIp;
        subnetRange  = subnetGateway = subnetDns = subnetReservedIp  = "";
        String subnetStaticIp ,subnetId , securityGroup, availabilityZone;
        subnetStaticIp  = subnetId = securityGroup = availabilityZone = "";
        
        if(networks  != null){
            for(HbCfDeploymentNetworkConfigVO networkVO: networks){
                if( "internal".equalsIgnoreCase(networkVO.getDirection() )){
                 /* cnt ++;
                    if( cnt > 1  && cnt < networks.size() ){
                    }else {
                    }*/
                    subnetId = networkVO.getSubnetId1() ;
                    securityGroup = networkVO.getSecurityGroup1();
                    subnetRange = networkVO.getSubnetRange1();
                    subnetGateway = networkVO.getSubnetGateway1();
                    subnetDns = networkVO.getSubnetDns1();
                    subnetReservedIp = (networkVO.getSubnetReservedFrom1() + " - " +  networkVO.getSubnetReservedTo1());
                    subnetStaticIp = networkVO.getSubnetStaticFrom1() +" - " + networkVO.getSubnetStaticTo1();
                    if( networkVO.getAvailabilityZone1() != null){
                        availabilityZone = networkVO.getAvailabilityZone1();
                    }
                    if(networkVO.getSubnetId2() !=null){
                        br = "<br>";
                        
                        subnetId = subnetId + br + networkVO.getSubnetId2();
                        securityGroup = securityGroup + br + networkVO.getSecurityGroup2();
                        subnetRange = subnetRange + br + networkVO.getSubnetRange2();
                        subnetGateway = subnetGateway + br + networkVO.getSubnetGateway2();
                        subnetDns = subnetDns + br + networkVO.getSubnetDns1();
                        subnetReservedIp = subnetReservedIp + br +  (networkVO.getSubnetReservedFrom2() + " - " +  networkVO.getSubnetReservedTo2());
                        subnetStaticIp = subnetStaticIp + br + networkVO.getSubnetStaticFrom2() +" - " + networkVO.getSubnetStaticTo2();
                        if( networkVO.getAvailabilityZone2() != null){
                            availabilityZone = networkVO.getAvailabilityZone2();
                        }
                    }
                }
            }
            
            cfListInfo.setSubnetId(subnetId);
            cfListInfo.setSecurityGroup(securityGroup);
            cfListInfo.setSubnetRange(subnetRange);
            cfListInfo.setSubnetGateway(subnetGateway);
            cfListInfo.setSubnetDns(subnetDns);
            cfListInfo.setSubnetReservedIp(subnetReservedIp);
            cfListInfo.setSubnetStaticIp(subnetStaticIp);
            cfListInfo.setAvailabilityZone(availabilityZone);
        }
        return cfListInfo;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Network 정보 삭제
     * @title : deleteNetworkConfigInfo
     * @return : void
    *****************************************************************/
    public void deleteNetworkConfigInfo(HbCfDeploymentNetworkConfigDTO dto, Principal principal) {
        if(dto.getId()  == null || dto.getId().toString().isEmpty()){
            throw new CommonException(message.getMessage("common.badRequest.exception.code", null, Locale.KOREA),
                    message.getMessage("common.badRequest.message", null, Locale.KOREA), HttpStatus.BAD_REQUEST);
        }
        cfDeploymentNetworkDao.deleteHbCfDeploymentNetworkConfigInfo(dto);
    }

}
