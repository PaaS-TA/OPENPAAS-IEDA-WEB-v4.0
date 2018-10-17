package org.openpaas.ieda.controller.hbdeploy.web.deploy.diego;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoNetworkConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoNetworkConfigDTO;
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
public class HbDiegoNetworkConfigController {

    @Autowired HbDiegoNetworkConfigService service;

    private final static Logger LOGGER = LoggerFactory.getLogger(HbDiegoNetworkConfigController.class);
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description :  DIEGO 네트워크 정보 화면 이동
     * @title : goHbNetworkConfig
     * @return : String
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/HbDiegoNetworkConfig", method = RequestMethod.GET)
    public String goHbNetworkConfig(){
        if (LOGGER.isInfoEnabled()) LOGGER.info("====================================> /deploy/hbDiego/HbDiegoNetworkConfig");
        return "/hbdeploy/deploy/diego/HbDiegoNetworkConfig";
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 네트워크 정보 목록 전체 조회
     * @title : getDiegoNetworkInfoList
     * @return : ResponseEntity<HashMap<String,Object>>
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/network/list", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getDiegoNetworkInfoList() {
        if (LOGGER.isInfoEnabled()) LOGGER.info("====================================> /deploy/hbDiego/network/list");
        List<HbDiegoNetworkConfigVO> networkConfigList = service.getNetworkConfigInfoList();
        HashMap<String, Object> list = new HashMap<String, Object>();
        int size = 0;
        if( networkConfigList.size() > 0 ){
            size = networkConfigList.size();
        }
        list.put("total", size);
        list.put("records", networkConfigList);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 네트워크 정보 상세 조회
     * @title : getDiegoNetworkInfoListDetail
     * @return : ResponseEntity<HashMap<String,Object>>
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/network/list/detail/{networkConfigName}", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getDiegoNetworkInfoListDetail(@PathVariable String networkConfigName){
    	if (LOGGER.isInfoEnabled()) LOGGER.info("====================================> /deploy/hbDiego/network/list/detail/{networkConfigName}");
    	List<HbDiegoNetworkConfigVO> networkConfigList = service.getNetworkConfigDetailInfoList(networkConfigName);
    	HashMap<String, Object> list = new HashMap<String, Object>();
    	int size = 0;
    	if(networkConfigList.size() > 0){
    		size = networkConfigList.size();
    	}
    	list.put("total", size);
    	list.put("records", networkConfigList);
    	return new ResponseEntity<>(list, HttpStatus.OK);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 네트워크 정보 저장
     * @title : saveDiegoNetworkConfigInfo
     * @return : ResponseEntity<?>
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/network/save", method = RequestMethod.PUT)
    public ResponseEntity<?> saveDiegoNetworkConfigInfo(@RequestBody @Valid List<HbDiegoNetworkConfigDTO> dto, Principal principal){
    	if (LOGGER.isInfoEnabled()) LOGGER.info("====================================> /deploy/hbDiego/network/save");
    	service.saveNetworkConfigInfo(dto, principal);
    	return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
