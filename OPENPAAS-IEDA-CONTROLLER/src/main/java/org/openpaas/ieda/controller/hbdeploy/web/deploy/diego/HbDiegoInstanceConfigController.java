package org.openpaas.ieda.controller.hbdeploy.web.deploy.diego;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoDefaultConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoInstanceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoNetworkConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoInstanceConfigDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.service.HbDiegoDefaultConfigService;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.service.HbDiegoInstanceConfigService;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.service.HbDiegoNetworkConfigService;
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
public class HbDiegoInstanceConfigController {
    
    @Autowired
    private HbDiegoInstanceConfigService service;
    @Autowired
    private HbDiegoDefaultConfigService defaultService;
    @Autowired
    private HbDiegoNetworkConfigService networkService;
    
    private final static Logger LOGGER = LoggerFactory.getLogger(HbDiegoInstanceConfigController.class);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 DIEGO 기본 정보 관리 화면 이동
     * @title : goHbDiego
     * @return : String
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/HbDiegoInstanceConfig", method = RequestMethod.GET)
    public String goHbDiegoDefaultConfig (){
        if (LOGGER.isInfoEnabled()) LOGGER.info("====================================> /deploy/hbDiego/HbDiegoDefaultConfig");
        return "/hbdeploy/deploy/diego/hbDiegoInstanceConfig";
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 인스턴스 정보 목록 전체 조회
     * @title : getResourceConfigInfoList
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/instance/list", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getResourceConfigInfoList() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbDiego/instance/list"); }
        List<HbDiegoInstanceConfigVO> instanceConfigList = service.getInstanceConfigList();
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
     * @description : 인스턴스 정보 상세 조회
     * @title : getInstanceConfigInfo
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/instance/list/detail/{id}", method = RequestMethod.GET)
    public ResponseEntity<HbDiegoInstanceConfigVO> getInstanceConfigInfo(@PathVariable int id) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/instance/list/detail/{id}"); }
        HbDiegoInstanceConfigVO instanceConfigInfo = service.getInstanceConfigInfo(id);
        return new ResponseEntity<>(instanceConfigInfo, HttpStatus.OK);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 인스턴스 정보 저장
     * @title : saveResourceConfigInfo
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/instance/save", method = RequestMethod.PUT)
    public ResponseEntity<?> saveResourceConfigInfo(@RequestBody HbDiegoInstanceConfigDTO dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbDiego/instance/save"); }
        service.saveInstanceConfig(dto, principal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 인스턴스 정보 삭제
     * @title : deleteResourceConfigInfo
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/instance/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteResourceConfigInfo(@RequestBody HbDiegoInstanceConfigDTO dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbDiego/instance/delete"); }
        service.deleteInstanceConfig(dto, principal);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 기본 정보 목록 조회
     * @title : getHbDiegoList
     * @return : RequestEntity<HashMap<String,Object>>
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/instance/list/defaultConfig", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> defaultConfigInfoList(){
        if (LOGGER.isInfoEnabled()) LOGGER.info("====================================> /deploy/hbdiego/default/list");
        List<HbDiegoDefaultConfigVO> defaultConfigList = defaultService.getDefaultConfigInfoList();
        HashMap<String, Object> map = new HashMap<String, Object>();
        int size = 0;
        if( defaultConfigList.size() > 0){
            size = defaultConfigList.size();
        }
        map.put("total", size);
        map.put("records", defaultConfigList);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Network 정보 목록 전체 조회
     * @title : getNetworkConfigInfoList
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/instance/list/networkConfig", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getNetworkConfigInfoList() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/instance/defaultConfig/list"); }
        List<HbDiegoNetworkConfigVO> networkConfigList = networkService.getNetworkConfigInfoList();
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
     * @project : Paas 플랫폼 설치 자동화
     * @description : DIEGO 릴리즈 버전 및 인프라 별 JOb 목록 조회
     * @title : getCfJobList
     * @return : ResponseEntity<List<HashMap<String, String>>>
    *****************************************************************/
    @RequestMapping(value="/deploy/hbDiego/install/instance/list/job/{version}/{deployType}", method=RequestMethod.GET)
    public ResponseEntity<List<HashMap<String, String>>> getDiegoJobList(@PathVariable String version, @PathVariable String deployType){
        if(LOGGER.isInfoEnabled()){ LOGGER.info("====================================> /deploy/cf/install/save/job/list/"+version); }
        List<HashMap<String, String>> list = service.getJobTemplateList(deployType, version);
        return new ResponseEntity<List<HashMap<String, String>>>(list, HttpStatus.OK);
    }
    
    
}
