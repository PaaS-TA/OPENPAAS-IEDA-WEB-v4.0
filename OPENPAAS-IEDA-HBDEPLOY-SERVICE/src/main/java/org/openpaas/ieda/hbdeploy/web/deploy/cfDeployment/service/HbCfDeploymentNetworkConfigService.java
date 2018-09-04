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
    public void saveNetworkConfigInfo(List<HbCfDeploymentNetworkConfigDTO> dtoList, Principal principal) {

        HbCfDeploymentNetworkConfigDTO dto = new HbCfDeploymentNetworkConfigDTO();

        String iaasType ="";
        String networkName="";
        String publicStaticIp = "";
       // String direction= "";
        String subnetId1= "";
        String securityGroup1="";
        String subnetRange1 = "";
        String subnetGateway1 = "";
        String subnetDns1 = "";
        String subnetReservedFrom1 = "";
        String subnetReservedTo1 = "";
        String subnetStaticFrom1 = "";
        String subnetStaticTo1 = "";
        String availabilityZone1 = "";
        String subnetId2= "";
        String securityGroup2="";
        String subnetRange2 = "";
        String subnetGateway2 = "";
        String subnetDns2 = "";
        String subnetReservedFrom2 = "";
        String subnetReservedTo2 = "";
        String subnetStaticFrom2 = "";
        String subnetStaticTo2 = "";
        String availabilityZone2 = "";
        
        
        for(int i = 0; i< dtoList.size(); i++){
            if(dtoList.get(i).getIaasType()!=null){
            	iaasType += dtoList.get(i).getIaasType();
            }
            if(dtoList.get(i).getNetworkName()!=null){
            	networkName += dtoList.get(i).getNetworkName(); 
            }
            if(dtoList.get(i).getPublicStaticIp()!=null){
            	publicStaticIp += dtoList.get(i).getPublicStaticIp();  
            }
/*            if(dtoList.get(i).getDirection()!=null){
            	direction += dtoList.get(i).getDirection();
            }*/
            if(dtoList.get(i).getSubnetId1()!=null){
            	subnetId1 += dtoList.get(i).getSubnetId1();
            }
            if(dtoList.get(i).getSecurityGroup1()!=null){
            	securityGroup1 += dtoList.get(i).getSecurityGroup1();
            }
            if(dtoList.get(i).getSubnetRange1()!=null){
            	subnetRange1 += dtoList.get(i).getSubnetRange1();
            }
            if(dtoList.get(i).getSubnetGateway1()!=null){
            	subnetGateway1 += dtoList.get(i).getSubnetGateway1();
            }
            if(dtoList.get(i).getSubnetDns1()!=null){
            	subnetDns1 += dtoList.get(i).getSubnetDns1();
            }
            if(dtoList.get(i).getSubnetReservedFrom1()!=null){
            	subnetReservedFrom1 += dtoList.get(i).getSubnetReservedFrom1();
            }
            if(dtoList.get(i).getSubnetReservedTo1()!=null){
            	subnetReservedTo1 += dtoList.get(i).getSubnetReservedTo1();
            }
            if(dtoList.get(i).getSubnetStaticFrom1()!=null){
            	subnetStaticFrom1 += dtoList.get(i).getSubnetStaticFrom1();
            }
            if(dtoList.get(i).getSubnetStaticTo1()!=null){
            	subnetStaticTo1 += dtoList.get(i).getSubnetStaticTo1();
            }
            if(dtoList.get(i).getAvailabilityZone1()!=null){
            	availabilityZone1 += dtoList.get(i).getAvailabilityZone1();
            	
            }
            
            if(dtoList.get(i).getSubnetId2()!=null){
                subnetId2 += dtoList.get(i).getSubnetId2();
                securityGroup2 += dtoList.get(i).getSecurityGroup2();
                subnetRange2 += dtoList.get(i).getSubnetRange2();
                subnetGateway2 += dtoList.get(i).getSubnetGateway2();
                subnetDns2 += dtoList.get(i).getSubnetDns2();
                subnetReservedFrom2 += dtoList.get(i).getSubnetReservedFrom2();
                subnetReservedTo2 += dtoList.get(i).getSubnetReservedTo2();
                subnetStaticFrom2 += dtoList.get(i).getSubnetStaticFrom2();
                subnetStaticTo2 += dtoList.get(i).getSubnetStaticTo2();
                if(dtoList.get(i).getAvailabilityZone2()!=null)
                    availabilityZone2 += dtoList.get(i).getAvailabilityZone2(); 
            }
        }
        
        dto.setIaasType(iaasType);
        dto.setNetworkName(networkName);
        dto.setPublicStaticIp(publicStaticIp);
       // dto.setDirection(direction);
        dto.setSubnetId1(subnetId1);
        dto.setSecurityGroup1(securityGroup1);
        dto.setSubnetRange1(subnetRange1);
        dto.setSubnetGateway1(subnetGateway1);
        dto.setSubnetDns1(subnetDns1);
        dto.setSubnetReservedFrom1(subnetReservedFrom1);
        dto.setSubnetReservedTo1(subnetReservedTo1);
        dto.setSubnetStaticFrom1(subnetStaticFrom1);
        dto.setSubnetStaticTo1(subnetStaticTo1);
        dto.setAvailabilityZone1(availabilityZone1);
        dto.setSubnetId2(subnetId2);
        dto.setSecurityGroup2(securityGroup2);
        dto.setSubnetRange2(subnetRange2);
        dto.setSubnetGateway2(subnetGateway2);
        dto.setSubnetDns2(subnetDns2);
        dto.setSubnetReservedFrom2(subnetReservedFrom2);
        dto.setSubnetReservedTo2(subnetReservedTo2);
        dto.setSubnetStaticFrom2(subnetStaticFrom2);
        dto.setSubnetStaticTo2(subnetStaticTo2);
        dto.setAvailabilityZone2(availabilityZone2);
        
        int count = cfDeploymentNetworkDao.selectHbCfDeploymentNetworkConfigByName(dto.getNetworkName());
        HbCfDeploymentNetworkConfigVO vo = new HbCfDeploymentNetworkConfigVO();
        if(dto.getId() == null){
        //if( StringUtils.isEmpty(dto.getId().toString())){
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
            vo.setSubnetGateway1(dto.getSubnetGateway1());
            vo.setSubnetDns1(dto.getSubnetDns1());
            vo.setSubnetReservedFrom1(dto.getSubnetReservedFrom1());
            vo.setSubnetReservedTo1(dto.getSubnetReservedTo1());
            vo.setSubnetStaticFrom1(dto.getSubnetStaticFrom1());
            vo.setSubnetStaticTo1(dto.getSubnetStaticTo1());
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
        }        
       
    }
    
    public HbCfDeploymentNetworkConfigDTO setNetworkInfoList(HbCfDeploymentNetworkConfigDTO cfListInfo, HbCfDeploymentNetworkConfigVO vo){
       HbCfDeploymentNetworkConfigVO networks = null;
        String br = "";
        //int cnt = 0;
        String subnetRange , subnetGateway , subnetDns , subnetReservedIp;
        subnetRange  = subnetGateway = subnetDns = subnetReservedIp  = "";
        String subnetStaticIp ,subnetId , securityGroup, availabilityZone;
        subnetStaticIp  = subnetId = securityGroup = availabilityZone = "";
        
        if(networks  != null){
         //   for(HbCfDeploymentNetworkConfigVO networkVO: networks){
        	HbCfDeploymentNetworkConfigVO networkVO = networks;
                if( "internal".equalsIgnoreCase(networkVO.getDirection() )){
/*                  cnt ++;
                    if( cnt > 1  && cnt < networks.size() ){
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
                cfListInfo.setPublicStaticIp(networkVO.getPublicStaticIp());
                cfListInfo.setNetworkName(networkVO.getNetworkName());
                cfListInfo.setIaasType(networkVO.getIaasType());
            //}
            cfListInfo.setSubnetId1(subnetId);
            cfListInfo.setSecurityGroup1(securityGroup);
            cfListInfo.setSubnetRange1(subnetRange);
            cfListInfo.setSubnetGateway1(subnetGateway);
            cfListInfo.setSubnetDns1(subnetDns);
            cfListInfo.setSubnetReservedIp(subnetReservedIp);
            cfListInfo.setSubnetStaticIp(subnetStaticIp);
            cfListInfo.setAvailabilityZone1(availabilityZone);
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
