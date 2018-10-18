package org.openpaas.ieda.controller.hbdeploy.web.deploy.diego;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoDeployAsyncService;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.service.HbDiegoDeleteAsyncService;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.service.HbDiegoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HbDiegoController {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(HbDiegoController.class);
	
	@Autowired private HbDiegoService service; 
	@Autowired private HbDiegoDeployAsyncService hbDiegoDeployAsyncService;
	@Autowired private HbDiegoDeleteAsyncService hbDiegoDeleteAsyncService;
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 DIEGO 설치 화면 이동
     * @title : goHbDiego
     * @return : String
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/HbDiego", method = RequestMethod.GET)
    public String goHbDiegoDefaultConfig (){
        if (LOGGER.isInfoEnabled()) LOGGER.info("====================================> /deploy/hbDiego/HbDiego");
        return "/hbdeploy/deploy/diego/hbDiego";
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Diego 설치 목록 조회
     * @title : getDiegoInfoList
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/list/{installStatus}", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getDiegoInfoList(@PathVariable String installStatus) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbDiego/list/{installStatus}"); }
        List<HbDiegoVO> diegoList = service.getDiegoInfoList(installStatus);
        HashMap<String, Object> list = new HashMap<String, Object>();
        int size =0;
        if( diegoList.size() > 0  ) {
            size = diegoList.size();
        }
        list.put("total", size);
        list.put("records", diegoList);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Diego 기본 정보 저장
     * @title : saveDiegoInfo
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/install/save", method = RequestMethod.PUT)
    public ResponseEntity<?> saveDiegoInfo(@RequestBody HbDiegoDTO dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbDiego/install/save"); }
        service.saveDiegoInfo(dto, principal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 단순 데이터 삭제
     * @title : deleteDiegoInfo
     * @return : ResponseEntity
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbDiego/delete/data", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCfInfo(@RequestBody HbDiegoDTO dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbhbDiego/delete/data"); }
        service.deleteDiegoInfo(dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : DIEGO 플랫폼 설치
     * @title : installDiego
     * @return : ResponseEntity<?>
    *****************************************************************/
    @MessageMapping("/deploy/hbDiego/install/diegoInstall")
    @SendTo("/deploy/hbDiego/install/logs")
    public ResponseEntity<?> installDiego(@RequestBody @Valid HbDiegoDTO dto, Principal principal){
        if(LOGGER.isInfoEnabled()){ LOGGER.info("==================================> /deploy/hbDiego/install/cfInstall"); }
        hbDiegoDeployAsyncService.deployAsync(dto, principal, "diego");
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : Diego 플랫폼 삭제 요청
     * @title : deleteDiego
     * @return : ResponseEntity<?>
    *****************************************************************/
    @MessageMapping("/deploy/hbDiego/delete/instance")
    @SendTo("/deploy/hbDiego/delete/logs")
    public ResponseEntity<?> deleteDiego(@RequestBody @Valid HbDiegoDTO dto, Principal principal){
        if(LOGGER.isInfoEnabled()){ LOGGER.info("==================================> /deploy/hbDiego/delete/instance"); }
        hbDiegoDeleteAsyncService.deleteDeployAsync(dto, "cf", principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
}
