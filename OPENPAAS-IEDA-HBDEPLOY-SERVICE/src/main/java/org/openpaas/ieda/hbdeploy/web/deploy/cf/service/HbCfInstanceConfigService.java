package org.openpaas.ieda.hbdeploy.web.deploy.cf.service;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfInstanceConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfInstanceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfInstanceConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HbCfInstanceConfigService {
    
    @Autowired private HbCfInstanceConfigDAO dao;
    @Autowired private MessageSource message;
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 인스턴스 정보 목록 전체 조회
     * @title : getInstanceConfigList
     * @return : List<HbCfInstanceConfigVO>
    *****************************************************************/
    public List<HbCfInstanceConfigVO> getInstanceConfigList() {
        List<HbCfInstanceConfigVO> list = dao.selectInstanceConfigList();
        return list;
    }

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 인스턴스 Job Template 조회
     * @title : getJobTemplateList
     * @return : List<HashMap<String, String>>
    *****************************************************************/
    public List<HashMap<String, String>> getJobTemplateList(String deployType, String releaseVersion) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("releaseVersion", releaseVersion);
        map.put("deployType", deployType);
        List<HashMap<String, String>> list = dao.selectCfJobTemplatesByReleaseVersion(map);
        return list;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Instance 정보 저장
     * @title : saveInstanceConfig
     * @return : void
    *****************************************************************/
    public void saveInstanceConfig(HbCfInstanceConfigDTO dto, Principal principal) {
        HbCfInstanceConfigVO vo = null;
        int count = dao.selectInstanceConfigByName(dto.getInstanceConfigName());
        if(StringUtils.isEmpty(dto.getId())) {
            vo = new HbCfInstanceConfigVO();
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
            vo.setUpdateUserId(principal.getName());
            vo.setInstanceConfigName(dto.getInstanceConfigName());
            vo.setNetworkConfigInfo(dto.getNetworkConfigInfo());
            vo.setIaasType(dto.getIaasType());
            vo.setDefaultConfigInfo(dto.getDefaultConfigInfo());
            
            vo.setApi_worker_z1(dto.getApi_worker_z1());
            vo.setApi_z1(dto.getApi_z1());
            vo.setBlobstore_z1(dto.getBlobstore_z1());
            vo.setClock_z1(dto.getClock_z1());
            vo.setConsul_z1(dto.getConsul_z1());
            vo.setDoppler_z1(dto.getDroppler_z1());
            vo.setEtcd_z1(dto.getEtcd_z1());
            vo.setLoggregator_z1(dto.getLoggregator_trafficcontroller_z1());
            vo.setNats_z1(dto.getNats_z1());
            vo.setPostgres_z1(dto.getPostgres_z1());
            vo.setRouter_z1(dto.getRouter_z1());
            vo.setUaa_z1(dto.getUaa_z1());
            
            vo.setApi_worker_z2(dto.getApi_worker_z2());
            vo.setApi_z2(dto.getApi_z2());
            vo.setBlobstore_z2(dto.getBlobstore_z2());
            vo.setClock_z2(dto.getClock_z2());
            vo.setConsul_z2(dto.getConsul_z2());
            vo.setDoppler_z2(dto.getDroppler_z2());
            vo.setEtcd_z2(dto.getEtcd_z2());
            vo.setLoggregator_z2(dto.getLoggregator_trafficcontroller_z2());
            vo.setNats_z2(dto.getNats_z2());
            vo.setPostgres_z2(dto.getPostgres_z2());
            vo.setRouter_z2(dto.getRouter_z2());
            vo.setUaa_z2(dto.getUaa_z2());
        }
        
        if(StringUtils.isEmpty(dto.getId())) {
            dao.insertInstanceConfg(vo);
        } else {
            dao.updateInstanceConfig(vo);
        }
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 인스턴스 정보 삭제
     * @title : deleteInstanceConfig
     * @return : void
    *****************************************************************/
    public void deleteInstanceConfig(HbCfInstanceConfigDTO dto, Principal principal) {
        if(StringUtils.isEmpty(dto.getId())){
            throw new CommonException(message.getMessage("common.badRequest.exception.code", null, Locale.KOREA),
                    message.getMessage("common.badRequest.message", null, Locale.KOREA), HttpStatus.BAD_REQUEST);
        }
        dao.deleteInstanceConfig(Integer.parseInt(dto.getId()));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 인스턴스 정보 상세 조회
     * @title : getInstanceConfigInfo
     * @return : HbCfInstanceConfigVO
    *****************************************************************/
    public HbCfInstanceConfigVO getInstanceConfigInfo(int id) {
        HbCfInstanceConfigVO vo = dao.selectInstanceConfigById(id);
        if(vo == null){
            throw new CommonException(message.getMessage("common.badRequest.exception.code", null, Locale.KOREA),
                    "인스턴스 상세 조회 실패", HttpStatus.BAD_REQUEST);
        }
        return vo;
    }
}
