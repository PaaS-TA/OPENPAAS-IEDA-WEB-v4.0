package org.openpaas.ieda.controller.hbdeploy.web.deploy.bootstrap;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.openpaas.ieda.controller.common.BaseController;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dao.HbBootstrapVO;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dto.HbBootStrapDeployDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dto.HbBootstrapListDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.service.HbBootstrapDeleteDeployAsyncService;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.service.HbBootstrapDeployAsyncService;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.service.HbBootstrapSaveService;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.service.HbBootstrapService;
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
public class HbBootstrapController extends BaseController {

    @Autowired private HbBootstrapSaveService saveService;
    @Autowired private HbBootstrapService bootstrapService;
    @Autowired private HbBootstrapDeployAsyncService deployAsyncService;
    @Autowired private HbBootstrapDeleteDeployAsyncService deleteDeployService;

    private final static Logger LOGGER = LoggerFactory.getLogger(HbBootstrapController.class);

    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : Bootstrap 설치 화면 이동
     * @title : goBootstrap
     * @return : String
    ***************************************************/
    @RequestMapping(value = "/deploy/hbBootstrap", method = RequestMethod.GET)
    public String goHbBootstrap() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/Hbbootstrap"); }
        return "/hbdeploy/deploy/bootstrap/hbBootstrap";
    }

    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : Bootstrap 설치 화면 이동
     * @title : goBootstrapPopup
     * @return : String
    ***************************************************/
    @RequestMapping(value = "/deploy/hbBootstrap/install/bootstrapPopup", method = RequestMethod.GET)
    public String goHbBootstrapPopup() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbbootstrap/install/bootstrapPopup"); }
        return "/hbdeploy/deploy/bootstrap/hbBootstrapPopup";
    }

    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : Bootstrap 정보 목록 조회
     * @title : getBootstrapList
     * @return : ResponseEntity<HashMap<String,Object>>
    ***************************************************/
    @RequestMapping(value = "/deploy/hbBootstrap/list", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getHbBootstrapList() {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbbootstrap/list"); }
        List<HbBootstrapListDTO> content = bootstrapService.getHbBootstrapList();
        HashMap<String, Object> result = new HashMap<String, Object>();
        int total = content != null ? content.size() : 0;
        result.put("records", content);
        result.put("total", total);
        return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
    }

    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : Bootstrap 상세 조회
     * @title : getBootstrapInfo
     * @return : ResponseEntity<BootstrapVO>
    ***************************************************/
    @RequestMapping(value = "/deploy/hbBootstrap/install/detail/{id}/{iaas}", method = RequestMethod.GET)
    public ResponseEntity<HbBootstrapVO> getHbBootstrapInfo(@PathVariable int id, @PathVariable String iaas) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbbootstrap/install/detail/"+id); }
        HbBootstrapVO vo = bootstrapService.getHbBootstrapInfo(id, iaas);
        return new ResponseEntity<HbBootstrapVO>(vo, HttpStatus.OK);
    }
    
    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : Bootstrap 설치 정보  조회
     * @title : getBootstrapInfo
     * @return : ResponseEntity<BootstrapVO>
    ***************************************************/
    @RequestMapping(value = "/deploy/hbBootstrap/install/hbDetail/{privateBootstrapId}/{publicBootStrapId}", method = RequestMethod.GET)
    public ResponseEntity<HbBootstrapVO> getHbBootstrapInstallInfo(@PathVariable String privateBootstrapId, @PathVariable String publicBootStrapId) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbbootstrap/install/detail/"); }
        HbBootstrapVO vo = bootstrapService.getHbBootstrapInstallInfo(privateBootstrapId, publicBootStrapId);
        return new ResponseEntity<HbBootstrapVO>(vo, HttpStatus.OK);
    }

    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 인프라 환경 설정 정보 등록/수정
     * @title : saveIaasConfigInfo
     * @return : ResponseEntity<BootstrapVO>
    *****************************************************************/
    @RequestMapping(value = "/deploy/hbBootstrap/install/setIaasConfigInfo", method = RequestMethod.PUT)
    public ResponseEntity<HbBootstrapVO> saveIaasConfigInfo(@RequestBody HbBootStrapDeployDTO.IaasConfig dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbbootstrap/install/setIaasConfigInfo"); }
        HbBootstrapVO config = saveService.saveIaasConfigInfo(dto, principal);
        return new ResponseEntity<>(config, HttpStatus.OK);
    }
    
    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 기본 정보 저장
     * @title : saveDefaultInfo
     * @return : ResponseEntity<BootstrapVO>
    ***************************************************/
    @RequestMapping(value = "/deploy/hbBootstrap/install/setDefaultInfo", method = RequestMethod.PUT)
    public ResponseEntity<HbBootstrapVO> saveDefaultInfo(@RequestBody HbBootStrapDeployDTO.Default dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbbootstrap/install/setDefaultInfo"); }
        HbBootstrapVO vo = saveService.saveDefaultInfo(dto, principal);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 네트워크 정보 저장
     * @title : saveNetworkInfo
     * @return : ResponseEntity<BootstrapVO>
    ***************************************************/
    @RequestMapping(value = "/deploy/hbBootstrap/install/setNetworkInfo", method = RequestMethod.PUT)
    public ResponseEntity<HbBootstrapVO> saveNetworkInfo(@RequestBody @Valid HbBootStrapDeployDTO.Network dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbbootstrap/install/setNetworkInfo"); }
        HbBootstrapVO vo = saveService.saveNetworkInfo(dto, principal);
        return new ResponseEntity<HbBootstrapVO>(vo, HttpStatus.OK);
    }

    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 리소스 정보 저장
     * @title : saveResourcesInfo
     * @return : ResponseEntity<BootstrapVO>
    ***************************************************/
    @RequestMapping(value = "/deploy/hbBootstrap/install/setResourceInfo", method = RequestMethod.PUT)
    public ResponseEntity<HbBootstrapVO> saveResourcesInfo(@RequestBody @Valid HbBootStrapDeployDTO.Resource dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbbootstrap/install/setResourceInfo"); }
        HbBootstrapVO vo = saveService.saveResourceInfo(dto, principal);
        return new ResponseEntity<HbBootstrapVO>(vo, HttpStatus.OK);
    }

    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 배포 파일 생성
     * @title : makeDeploymentFile
     * @return : ResponseEntity<?>
    ***************************************************/
    @RequestMapping(value = "/deploy/hbBootstrap/install/createSettingFile/{id}/{iaas}", method = RequestMethod.POST)
    public ResponseEntity<?> makeDeploymentFile(@PathVariable int id, @PathVariable String iaas) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbbootstrap/install/createSettingFile/"+id); }
        bootstrapService.createSettingFile(id, iaas);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 설치
     * @title : installBootstrap
     * @return : ResponseEntity<BootstrapVO>
    ***************************************************/
    @MessageMapping("/deploy/hbBootstrap/install/bootstrapInstall")
    @SendTo("/deploy/hbBootstrap/install/logs")
    public ResponseEntity<HbBootstrapVO> installBootstrap(@RequestBody @Valid HbBootStrapDeployDTO.Install dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbbootstrap/install/bootstrapInstall"); }
        deployAsyncService.deployAsync(dto, principal);
        return new ResponseEntity<HbBootstrapVO>(HttpStatus.OK);
    }

    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : Bootstrap 삭제 요청
     * @title : deleteBootstrap
     * @return : ResponseEntity<?>
    ***************************************************/
    @MessageMapping("/deploy/hbBootstrap/delete/instance")
    @SendTo("/deploy/hbBootstrap/delete/logs")
    public ResponseEntity<?> deleteBootstrap(@RequestBody @Valid HbBootStrapDeployDTO.Delete dto, Principal principal) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> /deploy/hbbootstrap/delete/instance"); }
        deleteDeployService.deleteDeployAsync(dto, principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 단순 Bootstrap 레코드 삭제
     * @title : deleteJustOnlyBootstrapRecord
     * @return : ResponseEntity<?>
    ***************************************************/
    @RequestMapping(value = "/deploy/hbBootstrap/delete/data", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteBootstrapInfo(@RequestBody @Valid HbBootStrapDeployDTO.Delete dto) {
        if (LOGGER.isInfoEnabled()) { LOGGER.info("===================== /deploy/hbbootstrap/delete/data"); }
            bootstrapService.deleteBootstrapInfo(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /***************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 배포 로그 정보 조회
     * @title : getDeployLogMsg
     * @return : ResponseEntity<String>
    ***************************************************/
    @RequestMapping(value = "/deploy/hbBootstrap/list/{id}/{iaas}", method = RequestMethod.GET)
    public ResponseEntity<String> getDeployLogMsg(@PathVariable int id, @PathVariable String iaas) {
        if (LOGGER.isDebugEnabled()) { 
        LOGGER.debug("====================================> /deploy/hbbootstrap/list/"); 
    }
        HbBootstrapVO vo = bootstrapService.getHbBootstrapInfo(id, iaas);
        return new ResponseEntity<String>(vo.getDeployLog(), HttpStatus.OK);
    }

}