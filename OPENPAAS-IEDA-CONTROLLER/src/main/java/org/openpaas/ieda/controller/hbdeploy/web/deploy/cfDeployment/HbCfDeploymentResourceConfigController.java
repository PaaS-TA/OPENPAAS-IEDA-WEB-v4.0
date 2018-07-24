package org.openpaas.ieda.controller.hbdeploy.web.deploy.cfDeployment;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import org.openpaas.ieda.controller.common.BaseController;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao.HbCfDeploymentResourceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dto.HbCfDeploymentResourceConfigDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.service.HbCfDeploymentResourceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
@Controller
public class HbCfDeploymentResourceConfigController extends BaseController{
    @Autowired private HbCfDeploymentResourceConfigService service;
	private final static Logger LOGGER = LoggerFactory.getLogger(HbCfDeploymentResourceConfigController.class);
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Hybrid CfDeployment 리소스 정보 화면 이동
     * @title : goCfDeployment
     * @return : String
    ***************************************************/
    @RequestMapping(value = "/deploy/hbCfDeployment/resourceConfig", method = RequestMethod.GET)
    public String goNetworkConfig() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCfDeployment/resourceConfig"); }
        return "/hbdeploy/deploy/cfDeployment/hbCfDeploymentResourceConfig";
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 리소스 목록 정보 조회
     * @title : selectResourceConfigInfoList
     * @return : ResponseEntity<CfDeploymentVO>
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCfDeployment/resourceConfig/list", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getRecourceConfigInfoList() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCfDeployment/resourceConfig/list"); }
        List<HbCfDeploymentResourceConfigVO> ResourceConfigList = service.getResourceConfigInfoList();
        HashMap<String, Object> list = new HashMap<String, Object>();
        int size =0;
        if( ResourceConfigList.size() > 0  ) {
            size = ResourceConfigList.size();
        }
        list.put("total", size);
        list.put("records", ResourceConfigList);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 리소스 정보 등록/수정
     * @title : saveResourceConfigInfo
     * @return : ResponseEntity<>
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCfDeployment/resourceConfig/save", method = RequestMethod.PUT)
    public ResponseEntity<?> saveResourceConfigInfo(@RequestBody HbCfDeploymentResourceConfigDTO dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCfDeployment/resourceConfig/save"); }
        service.saveResourceConfigInfo(dto, principal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 리소스 정보 삭제
     * @title : deleteResourceConfigInfo
     * @return : ResponseEntity<>
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCfDeployment/resourceConfig/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteResourceConfigInfo(@RequestBody HbCfDeploymentResourceConfigDTO dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCfDeployment/resourceConfig/delete"); }
        service.deleteResourceConfigInfo(dto, principal);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
