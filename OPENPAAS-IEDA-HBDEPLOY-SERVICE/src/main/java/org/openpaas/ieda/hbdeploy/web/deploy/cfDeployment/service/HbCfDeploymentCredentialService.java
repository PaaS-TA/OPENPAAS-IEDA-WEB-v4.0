package org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.service;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao.HbCfDeploymentCredentialConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao.HbCfDeploymentCredentialConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dto.HbCfDeploymentCredentialConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class HbCfDeploymentCredentialService {
	@Autowired private MessageSource message;
    @Autowired private  HbCfDeploymentCredentialConfigDAO hbCfDeploymentCredentialConfigDao;

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Default 정보 조회 
     * @title : getCredentialConfigInfoList
     * @return : List< HbCfDeploymentCredentialConfigVO>
    *****************************************************************/
    public List< HbCfDeploymentCredentialConfigVO> getCredentialConfigInfoList() {
        List< HbCfDeploymentCredentialConfigVO> list = hbCfDeploymentCredentialConfigDao.selectHbCfDeploymentCredentialConfigInfoList();
        return list;
    }

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Default 정보 등록/수정
     * @title : saveCredentialConfigInfo
     * @return : void
    *****************************************************************/
    @Transactional
    public void saveCredentialConfigInfo(HbCfDeploymentCredentialConfigDTO dto, Principal principal) {
        HbCfDeploymentCredentialConfigVO vo = null;
        int count = hbCfDeploymentCredentialConfigDao.selectHbCfDeploymentCredentialConfigByName(dto.getCredentialConfigName());
        if(dto.getId() == null){
        //if( StringUtils.isEmpty(dto.getId().toString())){
            vo = new HbCfDeploymentCredentialConfigVO();
            vo.setCreateUserId(principal.getName());
            if(count > 0){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        }else{
            vo = hbCfDeploymentCredentialConfigDao.selectHbCfDeploymentCredentialConfigInfo(dto.getId(), dto.getIaasType().toLowerCase());
            if(!dto.getCredentialConfigName().equals(vo.getCredentialConfigName()) && count > 0){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        }
        if( vo != null ){
        	vo.setCredentialConfigName(dto.getCredentialConfigName());
        	vo.setIaasType(dto.getIaasType());
        	vo.setCompany(dto.getCompany());
        	vo.setCountryCode(dto.getCountryCode());
        	vo.setDomain(dto.getDomain());
        	vo.setCity(dto.getCity());
        	vo.setEmailAddress(dto.getEmailAddress());
        	vo.setJobTitle(dto.getJobTitle());
            vo.setCreateDate(vo.getCreateDate());
            vo.setUpdateUserId(principal.getName());
        }
        if( dto.getId() == null ){
        //if( StringUtils.isEmpty(dto.getId().toString())){
        	hbCfDeploymentCredentialConfigDao.insertHbCfDeploymentCredentialConfigInfo(vo);
        }else{
        	hbCfDeploymentCredentialConfigDao.updateHbCfDeploymentCredentialConfigInfo(vo);
        }
    }

}
