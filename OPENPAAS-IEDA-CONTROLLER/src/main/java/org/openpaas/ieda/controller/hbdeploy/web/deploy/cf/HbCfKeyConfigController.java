package org.openpaas.ieda.controller.hbdeploy.web.deploy.cf;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfDefaultConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfKeyConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfKeyConfigDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.service.HbCfDefaultConfigService;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.service.HbCfKeyConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HbCfKeyConfigController {
    private final static Logger LOGGER = LoggerFactory.getLogger(HbCfKeyConfigController.class);
    @Autowired private HbCfKeyConfigService service;
    @Autowired private HbCfDefaultConfigService defaultservice;
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Key 정보 관리 화면 이동
     * @title : goKeyConfig
     * @return : String
    ***************************************************/
    @RequestMapping(value = "/deploy/hbCf/keyConfig", method = RequestMethod.GET)
    public String goKeyConfig() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/keyConfig"); }
        return "/hbdeploy/deploy/cf/hbCfKeyConfig";
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Key 정보 목록 전체 조회
     * @title : getKeyConfigInfoList
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/key/list", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getKeyConfigInfoList() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/defaultConfig/list"); }
        List<HbCfKeyConfigVO> keyConfigList = service.getKeyConfigInfoList();
        HashMap<String, Object> list = new HashMap<String, Object>();
        int size =0;
        if( keyConfigList.size() > 0  ) {
            size = keyConfigList.size();
        }
        list.put("total", size);
        list.put("records", keyConfigList);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Default 정보 목록 전체 조회
     * @title : getDefaultConfigInfoList
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/key/defaultConfig/list", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getDefaultConfigInfoList() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/key/defaultConfig/list"); }
        List<HbCfDefaultConfigVO> defaultConfigList = defaultservice.getDefaultConfigInfoList();
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
     * @description : CF Default 정보 상세 조회
     * @title : getDefaultConfigInfo
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/key/defaultConfig/info/{id}", method = RequestMethod.GET)
    public ResponseEntity<HbCfDefaultConfigVO> getDefaultConfigInfo(@PathVariable int id) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/key/defaultConfig/info"); }
        HbCfDefaultConfigVO defaultConfigList = defaultservice.getDefaultConfigInfo(id);
        return new ResponseEntity<>(defaultConfigList, HttpStatus.OK);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Key 정보 저장
     * @title : saveKeyConfigInfo
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/key/save", method = RequestMethod.PUT)
    public ResponseEntity<?> saveKeyConfigInfo(@RequestBody HbCfKeyConfigDTO dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/default/save"); }
        service.saveKeyConfigInfo(dto, principal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Key 정보 삭제
     * @title : deleteKeyConfigInfo
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/key/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteKeyConfigInfo(@RequestBody HbCfKeyConfigDTO dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/default/save"); }
        service.deleteKeyConfigInfo(dto, principal);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    
    
}
