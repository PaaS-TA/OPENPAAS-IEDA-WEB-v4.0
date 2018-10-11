package org.openpaas.ieda.hbdeploy.web.deploy.cf.service;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.hbdeploy.web.config.setting.dao.HbDirectorConfigDAO;
import org.openpaas.ieda.hbdeploy.web.config.setting.dao.HbDirectorConfigVO;
import org.openpaas.ieda.hbdeploy.web.config.setting.service.HbDirectorConfigService;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfDefaultConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfDefaultConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfDefaultConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HbCfDefaultConfigService {
    
    @Autowired private HbCfDefaultConfigDAO dao;
    @Autowired private MessageSource message;
    @Autowired private HbDirectorConfigService directorService;
    @Autowired private HbDirectorConfigDAO directorDao;
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 기본 정보 목록 전체 조회
     * @title : getDefaultConfigInfoList
     * @return : List<HbCfDefaultConfigVO>
    *****************************************************************/
    public List<HbCfDefaultConfigVO> getDefaultConfigInfoList() {
        List<HbCfDefaultConfigVO> list = dao.selectCfDefaultConfigInfoList();
        return list;
    }
    
    /****************************************************************
     * @param principal 
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 기본 정보 저장
     * @title : saveDefaultConfigInfo
     * @return : void
    *****************************************************************/
    public void saveDefaultConfigInfo(HbCfDefaultConfigDTO dto, Principal principal) {
        HbCfDefaultConfigVO vo = null;
        int count = dao.selectCfDefaultConfigInfoByName(dto.getDefaultConfigName());
        HbDirectorConfigVO directorVo =  directorDao.selectHbDirectorConfigBySeq(dto.getDirectorInfo());
        directorService.isExistBoshEnvLogin(directorVo.getDirectorUrl(), directorVo.getDirectorPort(), directorVo.getUserId(), directorVo.getUserPassword());
        
        if(StringUtils.isEmpty(dto.getId())) {
            vo = new HbCfDefaultConfigVO();
            vo.setCreateUserId(principal.getName());
            if(count > 0){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        } else {
            vo = dao.selectCfDefaultInfoById(Integer.parseInt(dto.getId()));
            if(!dto.getDefaultConfigName().equals(vo.getDefaultConfigName()) && count > 0){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        }
        
        if(vo != null){
            vo.setUpdateUserId(principal.getName());
            vo.setDefaultConfigName(dto.getDefaultConfigName());
            vo.setDeploymentName(dto.getDeploymentName());
            vo.setDirectorId(dto.getDirectorInfo());
            vo.setDomain(dto.getDomain());
            vo.setLoginSecret(dto.getLoginSecret());
            vo.setIaasType(dto.getIaasType());
            vo.setDomainOrganization(dto.getDomainOrganization());
            vo.setIngestorIp(dto.getIngestorIp());
            vo.setReleaseName(dto.getReleases().split("/")[0]);
            vo.setReleaseVersion(dto.getReleases().split("/")[1]);
            if(!StringUtils.isEmpty(dto.getLoggregatorReleases())){
                vo.setLoggregatorReleaseName(dto.getLoggregatorReleases().split("/")[0]);
                vo.setLoggregatorReleaseVersion(dto.getLoggregatorReleases().split("/")[1]);
            } else {
            	vo.setLoggregatorReleaseName("");
            	vo.setLoggregatorReleaseVersion("");
            }
            vo.setPaastaMonitoringUse(dto.getPaastaMonitoring());
        }
        
        if(StringUtils.isEmpty(dto.getId())){
        	dao.insertCfDefaultInfo(vo);
        }else {
        	dao.updateCfDefaultInfo(vo);
        }
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 기본 정보 삭제
     * @title : deleteDefaultConfigInfo
     * @return : void
    *****************************************************************/
	public void deleteDefaultConfigInfo(HbCfDefaultConfigDTO dto, Principal principal) {
		if(StringUtils.isEmpty(dto.getId())){
            throw new CommonException(message.getMessage("common.badRequest.exception.code", null, Locale.KOREA),
                    message.getMessage("common.badRequest.message", null, Locale.KOREA), HttpStatus.BAD_REQUEST);
		}
		dao.deleteCfDefaultConfigInfo(dto);
	}
    
}
