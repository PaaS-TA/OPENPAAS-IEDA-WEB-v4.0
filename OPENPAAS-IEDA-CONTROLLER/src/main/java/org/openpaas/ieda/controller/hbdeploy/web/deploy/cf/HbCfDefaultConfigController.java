package org.openpaas.ieda.controller.hbdeploy.web.deploy.cf;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import org.openpaas.ieda.controller.common.BaseController;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfDefaultConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfDefaultConfigDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.service.HbCfDefaultConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HbCfDefaultConfigController extends BaseController {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(HbCfDefaultConfigController.class);
	
	@Autowired private HbCfDefaultConfigService service;

    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Hybrid CF 기본 정보 화면 이동
     * @title : goCfDefaultConfig
     * @return : String
    ***************************************************/
    @RequestMapping(value = "/deploy/hbCf/defaultConfig", method = RequestMethod.GET)
    public String goCfDefaultConfig() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/defaultConfig"); }
        return "/hbdeploy/deploy/cf/hbCfDefaultConfig";
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 기본 정보 목록 전체 조회
     * @title : getDefaultConfigInfoList
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/default/list", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getDefaultConfigInfoList() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/defaultConfig/list"); }
        List<HbCfDefaultConfigVO> defaultConfigList = service.getDefaultConfigInfoList();
        HashMap<String, Object> list = new HashMap<String, Object>();
        int size =0;
        if( defaultConfigList.size() > 0  ) {
            size = defaultConfigList.size();
        }
        list.put("total", size);
        list.put("records", defaultConfigList);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 기본 정보 저장
     * @title : saveDefaultConfigInfo
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/default/save", method = RequestMethod.PUT)
    public ResponseEntity<?> saveDefaultConfigInfo(@RequestBody HbCfDefaultConfigDTO dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/default/save"); }
        service.saveDefaultConfigInfo(dto, principal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 기본 정보 삭제
     * @title : saveDefaultConfigInfo
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/default/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteDefaultConfigInfo(@RequestBody HbCfDefaultConfigDTO dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/default/delete"); }
        service.deleteDefaultConfigInfo(dto, principal);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    

}
