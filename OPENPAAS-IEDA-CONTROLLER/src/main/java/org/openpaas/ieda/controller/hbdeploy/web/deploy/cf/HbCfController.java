package org.openpaas.ieda.controller.hbdeploy.web.deploy.cf;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import org.openpaas.ieda.controller.common.BaseController;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.service.HbCfService;
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
public class HbCfController extends BaseController {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(HbCfController.class);
	
	@Autowired private HbCfService service;

    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 설치 화면 이동
     * @title : goInstall
     * @return : String
    ***************************************************/
    @RequestMapping(value = "/deploy/hbCf", method = RequestMethod.GET)
    public String goInstall() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/defaultConfig"); }
        return "/hbdeploy/deploy/cf/hbCf";
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 설치 목록 조회
     * @title : getInstallInfoList
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/list/{installStatus}", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getInstallInfoList(@PathVariable String installStatus) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/defaultConfig/list"); }
        List<HbCfVO> cfList = service.getCfInfoList(installStatus);
        HashMap<String, Object> list = new HashMap<String, Object>();
        int size =0;
        if( cfList.size() > 0  ) {
            size = cfList.size();
        }
        list.put("total", size);
        list.put("records", cfList);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 기본 정보 저장
     * @title : saveDefaultConfigInfo
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbCf/install/save", method = RequestMethod.PUT)
    public ResponseEntity<?> saveDefaultConfigInfo(@RequestBody HbCfDTO dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbCf/default/save"); }
        service.saveCfInfo(dto, principal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
