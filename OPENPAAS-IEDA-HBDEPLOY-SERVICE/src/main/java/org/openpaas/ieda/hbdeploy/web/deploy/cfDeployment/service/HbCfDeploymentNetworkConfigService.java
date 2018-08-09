package org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.service;

import java.security.Principal;
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
    public List< HbCfDeploymentNetworkConfigVO> getNetworkConfigInfoList() {
        List< HbCfDeploymentNetworkConfigVO> list = cfDeploymentNetworkDao.selectHbCfDeploymentNetworkConfigInfoList();
        return list;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Network 정보 등록/수정
     * @title : saveNetworkConfigInfo
     * @return : void
    *****************************************************************/
    @Transactional
    public void saveNetworkConfigInfo(HbCfDeploymentNetworkConfigDTO dto, Principal principal) {
        HbCfDeploymentNetworkConfigVO vo = null;
        int count = cfDeploymentNetworkDao.selectHbCfDeploymentNetworkConfigByName(dto.getNetworkName());
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
           // vo.setSubnetId(dto.getSubnetId());
          //  vo.setSecurityGroup(dto.getSecurityGroup());
           // vo.setSubnetRange(dto.getSubnetRange());
           // vo.setSubnetGateway(dto.getSubnetGateway());
           // vo.setSubnetDns(dto.getSubnetDns());
           // vo.setSubnetReservedFrom(dto.getSubnetReservedFrom());
           // vo.setSubnetReservedTo(dto.getSubnetReservedTo());
            vo.setCreateUserId(principal.getName());
            vo.setCreateDate(vo.getCreateDate());
            vo.setUpdateDate(vo.getUpdateDate());
            vo.setUpdateUserId(principal.getName());
            if(dto.getSubnetReservedFrom2() !=null && dto.getSubnetReservedTo2() != null){
         //       vo.setSubnetReservedFrom2(dto.getSubnetReservedFrom2());
         //       vo.setSubnetReservedTo2(dto.getSubnetReservedTo2());
            }
        }
        
        if( dto.getId() == null ){
        //if( StringUtils.isEmpty(dto.getId().toString())){
        	cfDeploymentNetworkDao.insertHbCfDeploymentNetworkConfigInfo(vo);
        }else{
        	cfDeploymentNetworkDao.updateHbCfDeploymentNetworkConfigInfo(vo);
        }
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
