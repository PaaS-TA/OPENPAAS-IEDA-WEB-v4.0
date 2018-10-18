package org.openpaas.ieda.controller.hbdeploy.web.deploy.diego;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.service.HbCfService;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoDefaultConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoDefaultConfigDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.service.HbDiegoDefaultConfigService;
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
public class HbDiegoDefaultConfigController {

    @Autowired
    private HbDiegoDefaultConfigService service;
    @Autowired
    private HbCfService cfService;
    private final static Logger LOGGER = LoggerFactory.getLogger(HbDiegoDefaultConfigController.class);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 DIEGO 기본 정보 관리 화면 이동
     * @title : goHbDiego
     * @return : String
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/HbDiegoDefaultConfig", method = RequestMethod.GET)
    public String goHbDiegoDefaultConfig (){
        if (LOGGER.isInfoEnabled()) LOGGER.info("====================================> /deploy/hbDiego/HbDiegoDefaultConfig");
        return "/hbdeploy/deploy/diego/hbDiegoDefaultConfig";
        
    }
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 기본 정보 목록 조회
     * @title : getHbDiegoList
     * @return : RequestEntity<HashMap<String,Object>>
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/default/list", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> defaultConfigInfoList(){
        if (LOGGER.isInfoEnabled()) LOGGER.info("====================================> /deploy/hbdiego/default/list");
        List<HbDiegoDefaultConfigVO> defaultConfigList = service.getDefaultConfigInfoList();
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
     * @description : DIEGO 기본 정보 저장
     * @title : saveDefaultConfigInfo
     * @return : ResponseEntity<?>
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/default/save", method = RequestMethod.PUT)
    public ResponseEntity<?> saveDefaultConfigInfo(@RequestBody HbDiegoDefaultConfigDTO dto, Principal principal){
        if (LOGGER.isInfoEnabled()) LOGGER.info("====================================> /deploy/hbDiego/default/save");
        service.saveDefaultConfigInfo(dto, principal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 목록 조회
     * @title : getCfConfigInfoList
     * @return : RequestEntity<HashMap<String,Object>>
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/list/cf", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getCfConfigInfoList(){
        if (LOGGER.isInfoEnabled()) LOGGER.info("====================================> /deploy/hbDiego/list/cf");
        List<HbCfVO> cfList = cfService.getCfInfoList("installed");
        HashMap<String, Object> map = new HashMap<String, Object>();
        int size = 0;
        if( cfList!=null && cfList.size() > 0){
            size = cfList.size();
        }
        map.put("total", size);
        map.put("records", cfList);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
    
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : DIEGO 기본 정보 삭제
     * @title : deleteDefaultConfigInfo
     * @return : ResponseEntity<?>
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbdiego/default/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteDefaultConfigInfo(@RequestBody HbDiegoDefaultConfigDTO dto, Principal principal){
        if (LOGGER.isInfoEnabled()) LOGGER.info("====================================> /deploy/hbDiego/default/save");
        service.DeleteDiegoDefaultConfigInfo(dto, principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
