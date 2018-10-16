package org.openpaas.ieda.controller.hbdeploy.web.deploy.cf;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfResourceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfResourceConfigDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.service.HbCfResourceConfigService;
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
public class HbCfResourceConfigController {
    private final static Logger LOGGER = LoggerFactory.getLogger(HbCfResourceConfigController.class);
    
    @Autowired private HbCfResourceConfigService service;
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Resource 정보 관리 화면 이동
     * @title : goKeyConfig
     * @return : String
    ***************************************************/
    @RequestMapping(value = "/deploy/hbCf/resourceConfig", method = RequestMethod.GET)
    public String goKeyConfig() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/resourceConfig"); }
        return "/hbdeploy/deploy/cf/hbCfResourceConfig";
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Resource 정보 목록 전체 조회
     * @title : getResourceConfigInfoList
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/resource/list", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getResourceConfigInfoList() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/resource/list"); }
        List<HbCfResourceConfigVO> resourceConfigList = service.getResourceConfigInfoList();
        HashMap<String, Object> list = new HashMap<String, Object>();
        int size =0;
        if( resourceConfigList.size() > 0  ) {
            size = resourceConfigList.size();
        }
        list.put("total", size);
        list.put("records", resourceConfigList);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    
 
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Resource 정보 저장
     * @title : saveResourceConfigInfo
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/resource/save", method = RequestMethod.PUT)
    public ResponseEntity<?> saveResourceConfigInfo(@RequestBody HbCfResourceConfigDTO dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/resource/save"); }
        service.saveResourceConfigInfo(dto, principal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Resource 정보 삭제
     * @title : deleteKeyConfigInfo
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/resource/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteResourceConfigInfo(@RequestBody HbCfResourceConfigDTO dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/default/save"); }
        service.deleteResourceConfigInfo(dto, principal);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    
    
}
