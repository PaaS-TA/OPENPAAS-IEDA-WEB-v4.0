package org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.service;

import java.security.Principal;
import java.util.Locale;

import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao.HbCfDeploymentDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao.HbCfDeploymentVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dto.HbCfDeploymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HbCfDeploymentSaveService {

    @Autowired private MessageSource message;
    @Autowired private HbCfDeploymentDAO hbCfDeploymentDao;
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : CF Deployment 정보 등록/수정
     * @title : saveCfdeploymentConfigInfo
     * @return : void
    *****************************************************************/
    public void saveCfdeploymentConfigInfo(HbCfDeploymentDTO dto, Principal principal){
        HbCfDeploymentVO vo = new HbCfDeploymentVO();
        int count = hbCfDeploymentDao.selectCfDeploymentConfigByName(dto.getCfDeploymentConfigName());
        if(StringUtils.isEmpty(dto.getId())){
            vo.setIaasType(dto.getIaasType());
            vo.setCreateUserId(principal.getName());
            if(count > 0){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        }else{
            vo = hbCfDeploymentDao.selectCfDeploymentConfigInfo(dto.getId());
            if(!dto.getCfDeploymentConfigName().equals(vo.getCfDeploymentConfigName()) && count > 0){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        }
    }
}
