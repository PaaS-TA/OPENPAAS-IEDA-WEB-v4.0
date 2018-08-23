package org.openpaas.ieda.controller.hbdeploy.web.deploy.cfDeployment;

import java.util.HashMap;
import java.util.List;

import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao.HbCfDeploymentDefaultConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao.HbCfDeploymentVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.service.HbCfDeploymentDefaultConfigService;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.service.HbCfDeploymentDeleteAsyncService;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.service.HbCfDeploymentDeployAsyncService;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.service.HbCfDeploymentSaveService;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.service.HbCfDeploymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HbCfDeploymentController {
    
    @Autowired private HbCfDeploymentService hbCfDeploymentService;
    @Autowired private HbCfDeploymentSaveService hbCfDeploymentSaveService;
    @Autowired private HbCfDeploymentDeployAsyncService hbCfDeploymentDeployAsyncService;
    @Autowired private HbCfDeploymentDeleteAsyncService hbCfDeploymentDeleteAsyncService;
    @Autowired private HbCfDeploymentDefaultConfigService service;
    
    final private static Logger LOGGER = LoggerFactory.getLogger(HbCfDeploymentController.class);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 설치 화면 이동
     * @title : goCfDeployment
     * @return : String
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCfDeployment/hbCfDeployment", method=RequestMethod.GET)
    public String goCfDeployment() {
        if(LOGGER.isInfoEnabled()){ LOGGER.info("==================================> /deploy/hbCfDeployment/hbCfDeployment"); }
        return "/hbdeploy/deploy/cfDeployment/hbCfDeployment";
    }
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Deployment 정보 조회
     * @title : getHbCfDeploymenList
     * @return : ResponseEntity<HashMap<String,Object>>
    ***************************************************/
    @RequestMapping(value = "/deploy/hbCfDeployment/list/{installStatus}", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getHbCfDeploymenList(@PathVariable String installStatus) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbBootstrap/list/{installStatus}"); }
        List<HbCfDeploymentVO> content = hbCfDeploymentService.getHbBCfDeploymentList(installStatus);
        HashMap<String, Object> result = new HashMap<String, Object>();
        int total = content != null ? content.size() : 0;
        result.put("records", content);
        result.put("total", total);
        return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
    }
}
