package org.openpaas.ieda.hbdeploy.web.deploy.cf.service;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfResourceConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfResourceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfResourceConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HbCfResourceConfigService {
    
    @Autowired private HbCfResourceConfigDAO dao;
    @Autowired private MessageSource message;
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Key 정보 목록 전체 조회
     * @title : getResourceConfigInfoList
     * @return : List<HbCfDefaultConfigVO>
    *****************************************************************/
    public List<HbCfResourceConfigVO> getResourceConfigInfoList() {
        List<HbCfResourceConfigVO> list = dao.selectCfResourceConfigList();
        return list;
    }
  
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Key 정보 저장
     * @title : saveKeyConfigInfo
     * @return : void
    *****************************************************************/
    public void saveResourceConfigInfo(HbCfResourceConfigDTO dto, Principal principal) {
        HbCfResourceConfigVO vo = null;
        int count = dao.selectCfResourceConfigInfoByName(dto.getResourceConfigName());
        if(StringUtils.isEmpty(dto.getId())) {
            vo = new HbCfResourceConfigVO();
            vo.setCreateUserId(principal.getName());
            if(count > 0){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        } else {
            vo = dao.selectCfResourceConfigInfoById(Integer.parseInt(dto.getId()));
            if(!dto.getResourceConfigName().equals(vo.getResourceConfigName()) && count > 0){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        }
        
        if(vo != null){
            vo.setResourceConfigName(dto.getResourceConfigName());
            vo.setBoshPassword(dto.getBoshPassword());
            vo.setIaasType(dto.getIaasType());
            vo.setLargeFlavor(dto.getLargeFlavor());
            vo.setMediumFlavor(dto.getMediumFlavor());
            vo.setSmallFlavor(dto.getSmallFlavor());
            vo.setUpdateUserId(principal.getName());
            vo.setStemcellName(dto.getStemcellName());
            vo.setStemcellVersion(dto.getStemcellVersion());
            vo.setDirectorId(dto.getDirectorInfo());
        }
        
        if(StringUtils.isEmpty(dto.getId())) {
            dao.insertCfResourceConfigInfo(vo);
        }else {
            dao.updateCfResourceConfigInfo(vo);
        }
    }

    /***************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : Message 값 가져오기
     * @title : deleteKeyConfigInfo
     * @return : void
    ***************************************************/
    public void deleteResourceConfigInfo(HbCfResourceConfigDTO dto, Principal principal) {
        if(StringUtils.isEmpty(dto.getId())){
            throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                    "리소스 정보 삭제 실패", HttpStatus.BAD_REQUEST);
        }
        dao.deleteCfResourceConfigInfo(Integer.parseInt(dto.getId()));
        
    }
}
