package org.openpaas.ieda.hbdeploy.web.deploy.diego.service;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoResourceConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoResourceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoResourceConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HbDiegoResourceConfigService {
    
    @Autowired private HbDiegoResourceConfigDAO dao;
    @Autowired private MessageSource message;
    
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 리소스 정보 목록 조회
     * @title : getDefaultConfigInfoList
     * @return : List<HbDiegoResourceConfigVO>
    *****************************************************************/
    public List<HbDiegoResourceConfigVO> getDefaultConfigInfoList() {
        List<HbDiegoResourceConfigVO> list = dao.selectResourceConfigInfoList();
        return list;
    }

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 리소스 정보 목록 조회
     * @title : getDefaultConfigInfoList
     * @return : List<HbDiegoResourceConfigVO>
    *****************************************************************/
    public void saveResourceConfigInfo(HbDiegoResourceConfigDTO dto, Principal principal) {
        HbDiegoResourceConfigVO vo = null;
        int count = dao.selectResourceConfigInfoByName(dto.getResourceConfigName());
        if(StringUtils.isEmpty(dto.getId())) {
            vo = new HbDiegoResourceConfigVO();
            vo.setCreateUserId(principal.getName());
            if(count > 0){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        } else {
            vo = dao.selectResourceConfigInfoById(Integer.parseInt(dto.getId()));
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
            dao.insertResourceConfigInfo(vo);
        }else {
            dao.updateResourceConfigInfo(vo);
        }
        
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 리소스 정보 삭제
     * @title : deleteResourceConfigInfo
     * @return : void
    *****************************************************************/
	public void deleteResourceConfigInfo(HbDiegoResourceConfigDTO dto, Principal principal) {
        if(StringUtils.isEmpty(dto.getId())){
            throw new CommonException(message.getMessage("common.badrequest.exception.code", null, Locale.KOREA),
                    "리소스 정보 삭제 실패", HttpStatus.BAD_REQUEST);
        }
        dao.deleteResourceConfigInfo(Integer.parseInt(dto.getId()));
	}
}
