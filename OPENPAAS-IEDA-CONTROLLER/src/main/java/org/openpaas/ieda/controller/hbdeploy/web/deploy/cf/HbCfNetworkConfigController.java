package org.openpaas.ieda.controller.hbdeploy.web.deploy.cf;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfNetworkConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfNetworkConfigDTO;
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
public class HbCfNetworkConfigController {
    private final static Logger LOGGER = LoggerFactory.getLogger(HbCfNetworkConfigController.class);
    @Autowired private HbCfNetworkConfigService service;
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 네트워크 정보 화면 이동
     * @title : goNetworkConfig
     * @return : String
    ***************************************************/
    @RequestMapping(value = "/deploy/hbCf/networkConfig", method = RequestMethod.GET)
    public String goNetworkConfig() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/NetworkConfig"); }
        return "/hbdeploy/deploy/cf/hbCfNetworkConfig";
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 네트워크 정보 목록 전체 조회
     * @title : getDefaultConfigInfoList
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/network/list", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getNetworkConfigInfoList() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/network/list"); }
        List<HbCfNetworkConfigVO> networkConfigList = service.getNetworkConfigInfoList();
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
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : CF 네트워크 정보 저장 
     * @title : saveNetworkConfigInfot
     * @return : ResponseEntity<?>
    *****************************************************************/
    @RequestMapping(value="/deploy/hbCf/network/list/detail/{networkConfigName:.+}", method=RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getNetworkConfigInfoListDetail(@PathVariable String networkConfigName){
        if(LOGGER.isInfoEnabled()){ LOGGER.info("==================================> /deploy/hbCf/network/list/detail"); }
        List<HbCfNetworkConfigVO> networkConfigList = service.getNetworkConfigInfoListDetail(networkConfigName);
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
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : CF 네트워크 정보 저장 
     * @title : saveNetworkConfigInfot
     * @return : ResponseEntity<?>
    *****************************************************************/
    @RequestMapping(value="/deploy/hbCf/network/save", method=RequestMethod.PUT)
    public ResponseEntity<?> saveNetworkConfigInfot(@RequestBody @Valid List<HbCfNetworkConfigDTO> dto, Principal principal){
        if(LOGGER.isInfoEnabled()){ LOGGER.info("==================================> /deploy/cf/install/saveNetworkInfo"); }
        service.saveNetworkConfigInfo(dto, principal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : CF 네트워크 정보 삭제
     * @title : deleteNetworkConfigInfot
     * @return : ResponseEntity<?>
    *****************************************************************/
    @RequestMapping(value="/deploy/hbCf/network/delete", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteNetworkConfigInfot(@RequestBody @Valid HbCfNetworkConfigDTO dto, Principal principal){
        if(LOGGER.isInfoEnabled()){ LOGGER.info("==================================> /deploy/hbCf/network/delete"); }
        service.deleteNetworkConfigInfo(dto, principal);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
