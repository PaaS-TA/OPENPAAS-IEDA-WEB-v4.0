package org.openpaas.ieda.hbdeploy.web.deploy.diego.service;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.hbdeploy.web.config.setting.dao.HbDirectorConfigDAO;
import org.openpaas.ieda.hbdeploy.web.config.setting.dao.HbDirectorConfigVO;
import org.openpaas.ieda.hbdeploy.web.config.setting.service.HbDirectorConfigService;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoDefaultConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoDefaultConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoDefaultConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.cloud.datastore.StringValue;

@Service
public class HbDiegoDefaultConfigService {

    @Autowired
    private HbDiegoDefaultConfigDAO dao;
    @Autowired
    private MessageSource message;
    @Autowired
    private HbDirectorConfigService directorService;
    @Autowired
    private HbDirectorConfigDAO directorDao;
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 기본 정보 목록 조회 서비스
     * @title : getDefaultConfigInfoList
     * @return : List<HbDiegoDefaultConfigVO>
    *****************************************************************/
    public List<HbDiegoDefaultConfigVO> getDefaultConfigInfoList(){
        List<HbDiegoDefaultConfigVO> list = dao.selectDiegoDefaultInfoList();
        return list;
    }
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : DIEGO 기본 정보 저장
     * @title : saveDefaultConfigInfo
     * @return : void
    *****************************************************************/
    public void saveDefaultConfigInfo(HbDiegoDefaultConfigDTO dto, Principal principal){
        HbDiegoDefaultConfigVO vo = new HbDiegoDefaultConfigVO();
        int count = dao.selectDiegoDefaultConfigInfoByName(dto.getDefaultConfigName());
        HbDirectorConfigVO directorVo = directorDao.selectHbDirectorConfigBySeq(dto.getDirectorId());
        directorService.isExistBoshEnvLogin(directorVo.getDirectorUrl(), directorVo.getDirectorPort(), directorVo.getUserId(), directorVo.getUserPassword());
        
        if(StringUtils.isEmpty(dto.getId())){
            vo = new HbDiegoDefaultConfigVO();
            vo.setCreateUserId(principal.getName());
            if(count > 0){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        }else {
            vo = dao.selectDiegoDefaultConfigInfoById(Integer.parseInt(dto.getId()));
            if(!dto.getDefaultConfigName().equals(vo.getDefaultConfigName()) && count > 0 ){
                throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                        message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
            }
        }
        
        if(vo != null){
            vo.setUpdateUserId(principal.getName());
            vo.setDefaultConfigName(dto.getDefaultConfigName());
            vo.setIaasType(dto.getIaasType());
            vo.setDeploymentName(dto.getDeploymentName());
            vo.setDirectorId(String.valueOf(dto.getDirectorId()));
            vo.setDiegoReleaseName(dto.getDiegoReleaseName());
            vo.setDiegoReleaseVersion(dto.getDiegoReleaseVersion());
            vo.setCfDeployment(dto.getCfDeployment());
            vo.setGardenReleaseName(dto.getGardenReleaseName());
            vo.setGardenReleaseVersion(dto.getGardenReleaseName());
            vo.setGardenReleaseVersion(dto.getGardenReleaseVersion());
            vo.setCflinuxfs2rootfsreleaseName(dto.getCflinuxfs2rootfsreleaseName());
            vo.setCflinuxfs2rootfsreleaseVersion(dto.getCflinuxfs2rootfsreleaseVersion());
            vo.setPaastaMonitoringUse(dto.getPaastaMonitoringUse());
            vo.setIngestorIp(dto.getIngestorIp());
        }
        if(StringUtils.isEmpty(dto.getId())){
            dao.insertDiegoDefaultConfigInfo(vo);
        }else{
            dao.updateDiegoDefaultConfigInfo(vo);
        }
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 기본 정보 삭제
     * @title : DeleteDiegoDefaultConfigInfo
     * @return : void
    *****************************************************************/
    public void DeleteDiegoDefaultConfigInfo(HbDiegoDefaultConfigDTO dto, Principal principal){
        if(StringUtils.isEmpty(dto.getId())){
            throw new CommonException(message.getMessage("common.badRequest.exception.code", null, Locale.KOREA),
                    message.getMessage("common.badRequest.message", null, Locale.KOREA), HttpStatus.BAD_REQUEST);
        }
        dao.deleteDiegoDefaultConfigInfo(dto.getId());
    }
}
