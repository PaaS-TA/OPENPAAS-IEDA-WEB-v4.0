package org.openpaas.ieda.hbdeploy.web.deploy.diego.service;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoInstanceConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoInstanceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoInstanceConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HbDiegoInstanceConfigService {
    
    @Autowired private HbDiegoInstanceConfigDAO dao;
    @Autowired private MessageSource message;
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 인스턴스 정보 목록 전체 조회
     * @title : getInstanceConfigList
     * @return : List<HbDiegoInstanceConfigVO
    *****************************************************************/
    public List<HbDiegoInstanceConfigVO> getInstanceConfigList() {
        return dao.selectDiegoInstanceInfoList();
    }
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : 인프라 및 릴리즈 버전 별 job 목록 조회
     * @title : getJobTemplateList
     * @return : List<HashMap<String,String>>
    *****************************************************************/
    public List<HashMap<String, String>> getJobTemplateList(String deployType, String releaseVersion) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("releaseVersion", releaseVersion);
        map.put("deployType",deployType);
        List<HashMap<String, String>> list = dao.selectDiegoJobTemplatesByReleaseVersion(map);
        return list;
    }

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 인스턴스 정보 저장
     * @title : saveInstanceConfig
     * @return : void
    *****************************************************************/
    public void saveInstanceConfig(HbDiegoInstanceConfigDTO dto, Principal principal) {
        HbDiegoInstanceConfigVO vo = null;
        int count = dao.selectInstanceConfigByName(dto.getInstanceConfigName());
        if(StringUtils.isEmpty(dto.getId())) {
            vo = new HbDiegoInstanceConfigVO();
            vo.setCreateUserId(principal.getName());
            if(count > 0){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        } else {
            vo = dao.selectInstanceConfigById(Integer.parseInt(dto.getId()));
            if(!dto.getInstanceConfigName().equals(vo.getInstanceConfigName()) && count > 0){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        }
        
        if(vo != null){
            
            vo.setIaasType(dto.getIaasType());
            vo.setInstanceConfigName(dto.getInstanceConfigName());
            vo.setDefaultConfigInfo(dto.getDefaultConfigInfo());
            vo.setNetworkConfigInfo(dto.getNetworkConfigInfo());
            
            vo.setAccess_z1(dto.getAccess_z1());
            vo.setBrain_z1(dto.getBrain_z1());
            vo.setCc_bridge_z1(dto.getCc_bridge_z1());
            vo.setCell_z1(dto.getCell_z1());
            vo.setDatabase_z1(dto.getDatabase_z1());
            vo.setAccess_z2(dto.getAccess_z2());
            vo.setBrain_z2(dto.getBrain_z2());
            vo.setCc_bridge_z2(dto.getCc_bridge_z2());
            vo.setCell_z2(dto.getCell_z2());
            vo.setDatabase_z2(dto.getDatabase_z2());
            vo.setAccess_z3(dto.getAccess_z3());
            vo.setBrain_z3(dto.getBrain_z3());
            vo.setCc_bridge_z3(dto.getCc_bridge_z3());
            vo.setCell_z3(dto.getCell_z3());
            vo.setDatabase_z3(dto.getDatabase_z3());
        }
        if(StringUtils.isEmpty(dto.getId())) {
            dao.insertInstanceConfg(vo);
        }else {
            dao.updateInstanceConfig(vo);
        }
        
    }

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 인스턴스 정보 삭제
     * @title : deleteInstanceConfig
     * @return : void
    *****************************************************************/
    public void deleteInstanceConfig(HbDiegoInstanceConfigDTO dto, Principal principal) {
        if(StringUtils.isEmpty(dto.getId())){
            throw new CommonException(message.getMessage("common.badrequest.exception.code", null, Locale.KOREA),
                    "인스턴스 정보 삭제 실패", HttpStatus.CONFLICT);
        }
        dao.deleteInstanceConfig(Integer.parseInt(dto.getId()));
    }

    public HbDiegoInstanceConfigVO getInstanceConfigInfo(int id) {
        HbDiegoInstanceConfigVO vo = dao.selectInstanceConfigById(id);
        if(vo == null){
            throw new CommonException(message.getMessage("common.badRequest.exception.code", null, Locale.KOREA),
                    "인스턴스 상세 조회 실패", HttpStatus.BAD_REQUEST);
        }
        return vo;
    }
}
