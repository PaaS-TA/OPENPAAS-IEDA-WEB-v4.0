package org.openpaas.ieda.controller.hbdeploy.web.deploy.cfDeployment;
import org.openpaas.ieda.controller.common.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HbCfDeploymentInstanceConfigController extends BaseController{
	private final static Logger LOGGER = LoggerFactory.getLogger(HbCfDeploymentInstanceConfigController.class);
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Hybrid CfDeployment 인스턴스 정보 화면 이동
     * @title : goInstanceConfig
     * @return : String 
    ***************************************************/
    @RequestMapping(value = "/deploy/hbCfDeployment/instanceConfig", method = RequestMethod.GET)
    public String goInstanceConfig() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCfDeployment/resourceConfig"); }
        return "/hbdeploy/deploy/cfDeployment/hbCfDeploymentInstanceConfig";
    }
    
}
