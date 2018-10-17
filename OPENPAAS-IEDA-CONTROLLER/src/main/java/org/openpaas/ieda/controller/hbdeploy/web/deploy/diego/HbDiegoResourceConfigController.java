package org.openpaas.ieda.controller.hbdeploy.web.deploy.diego;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoResourceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoResourceConfigDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.service.HbDiegoResourceConfigService;
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
public class HbDiegoResourceConfigController {
	
	@Autowired HbDiegoResourceConfigService service;
	
	private final static Logger LOGGER = LoggerFactory.getLogger(HbDiegoResourceConfigController.class);
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 DIEGO 기본 정보 관리 화면 이동
     * @title : goHbDiegoInstanceConfig
     * @return : String
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/HbDiegoResourceConfig", method = RequestMethod.GET)
    public String goHbDiegoInstanceConfig (){
        if (LOGGER.isInfoEnabled()) LOGGER.info("====================================> /deploy/hbDiego/HbDiegoResourceConfig");
        return "/hbdeploy/deploy/diego/HbDiegoResourceConfig";
        
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 리소스 정보 목록 조회
     * @title : resourceConfigInfoList
     * @return : RequestEntity<HashMap<String,Object>>
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/resource/list", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> resourceConfigInfoList(){
        if (LOGGER.isInfoEnabled()) LOGGER.info("====================================> /deploy/hbdiego/resource/list");
        List<HbDiegoResourceConfigVO> resourceConfigList = service.getDefaultConfigInfoList();
        HashMap<String, Object> map = new HashMap<String, Object>();
        int size = 0;
        if( resourceConfigList.size() > 0){
            size = resourceConfigList.size();
        }
        map.put("total", size);
        map.put("records", resourceConfigList);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 리소스 정보 저장
     * @title : saveResourceConfigInfo
     * @return : ResponseEntity<?>
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/resource/save", method = RequestMethod.PUT)
    public ResponseEntity<?> saveResourceConfigInfo(@RequestBody HbDiegoResourceConfigDTO dto, Principal principal){
        if (LOGGER.isInfoEnabled()) LOGGER.info("====================================> /deploy/hbDiego/resource/save");
        service.saveResourceConfigInfo(dto, principal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 리소스 정보 삭제
     * @title : deleteResourceConfigInfo
     * @return : ResponseEntity<?>
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/resource/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteResourceConfigInfo(@RequestBody HbDiegoResourceConfigDTO dto, Principal principal){
        if (LOGGER.isInfoEnabled()) LOGGER.info("====================================> /deploy/hbDiego/resource/save");
        service.deleteResourceConfigInfo(dto, principal);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
	
}
