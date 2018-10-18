package org.openpaas.ieda.controller.hbdeploy.web.deploy.cf;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfDefaultConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfInstanceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfNetworkConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfInstanceConfigDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.service.HbCfDefaultConfigService;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.service.HbCfInstanceConfigService;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.service.HbCfNetworkConfigService;
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
public class HbCfInstanceConfigController {
    private final static Logger LOGGER = LoggerFactory.getLogger(HbCfInstanceConfigController.class);
    
    @Autowired private HbCfInstanceConfigService service;
    @Autowired private HbCfDefaultConfigService defaultService;
    @Autowired private HbCfNetworkConfigService networkService;
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 인스턴스 정보 관리 화면 이동
     * @title : goKeyConfig
     * @return : String
    ***************************************************/
    @RequestMapping(value = "/deploy/hbCf/instanceConfig", method = RequestMethod.GET)
    public String goKeyConfig() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/InstanceConfig"); }
        return "/hbdeploy/deploy/cf/hbCfInstanceConfig";
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 인스턴스 정보 목록 전체 조회
     * @title : getResourceConfigInfoList
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/instance/list", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getResourceConfigInfoList() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/instance/list"); }
        List<HbCfInstanceConfigVO> instanceConfigList = service.getInstanceConfigList();
        HashMap<String, Object> list = new HashMap<String, Object>();
        int size =0;
        if( instanceConfigList.size() > 0  ) {
            size = instanceConfigList.size();
        }
        list.put("total", size);
        list.put("records", instanceConfigList);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Default 정보 상세 조회
     * @title : getInstanceConfigInfo
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/instance/list/detail/{id}", method = RequestMethod.GET)
    public ResponseEntity<HbCfInstanceConfigVO> getInstanceConfigInfo(@PathVariable int id) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/instance/list/detail/{id}"); }
        HbCfInstanceConfigVO instanceConfigInfo = service.getInstanceConfigInfo(id);
        return new ResponseEntity<>(instanceConfigInfo, HttpStatus.OK);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Default 정보 목록 전체 조회
     * @title : getDefaultConfigInfoList
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/instance/defaultConfig/list", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getDefaultConfigInfoList() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/instance/defaultConfig/list"); }
        List<HbCfDefaultConfigVO> defaultConfigList = defaultService.getDefaultConfigInfoList();
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
     * @description : CF Network 정보 목록 전체 조회
     * @title : getNetworkConfigInfoList
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/instance/networkConfig/list", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getNetworkConfigInfoList() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/instance/defaultConfig/list"); }
        List<HbCfNetworkConfigVO> networkConfigList = networkService.getNetworkConfigInfoList();
        HashMap<String, Object> list = new HashMap<String, Object>();
        int size =0;
        if( networkConfigList.size() > 0  ) {
            size = networkConfigList.size();
        }
        list.put("total", size);
        list.put("records", networkConfigList);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Network 정보 목록 전체 조회
     * @title : getNetworkConfigInfoList
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/instance/jobSetting/list/{releaseVersion}/{deployType}", method = RequestMethod.GET)
    public ResponseEntity<List<HashMap<String, String>>> getJobSettingnfoList(@PathVariable String releaseVersion, @PathVariable String deployType) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/key/defaultConfig/list"); }
        List<HashMap<String, String>> list = service.getJobTemplateList(deployType, releaseVersion);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 인스턴스 정보 저장
     * @title : saveResourceConfigInfo
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/instance/save", method = RequestMethod.PUT)
    public ResponseEntity<?> saveResourceConfigInfo(@RequestBody HbCfInstanceConfigDTO dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/instance/save"); }
        service.saveInstanceConfig(dto, principal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 인스턴스 정보 삭제
     * @title : deleteKeyConfigInfo
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/instance/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteResourceConfigInfo(@RequestBody HbCfInstanceConfigDTO dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/instance/save"); }
        service.deleteInstanceConfig(dto, principal);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
