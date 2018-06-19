package org.openpaas.ieda.controller.deploy.web.config.hbstemcell;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.openpaas.ieda.controller.deploy.web.config.stemcell.StemcellManagementController;
import org.openpaas.ieda.deploy.web.config.stemcell.dao.StemcellManagementVO;
import org.openpaas.ieda.deploy.web.config.stemcell.dto.StemcellManagementDTO;
import org.openpaas.ieda.deploy.web.config.stemcell.service.StemcellManagementUploadService;
import org.openpaas.ieda.hbdeploy.web.config.stemcell.dao.HbStemcellManagementVO;
import org.openpaas.ieda.hbdeploy.web.config.stemcell.dto.HbStemcellManagementDTO;
import org.openpaas.ieda.hbdeploy.web.config.stemcell.service.HbStemcellManagementDownloadAsyncService;
import org.openpaas.ieda.hbdeploy.web.config.stemcell.service.HbStemcellManagementService;
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
public class HybridStemcellManagementController {

    @Autowired private HbStemcellManagementService service;
    @Autowired private StemcellManagementUploadService uploadService;
    @Autowired private HbStemcellManagementDownloadAsyncService donwonloadService;

    private final static Logger LOGGER = LoggerFactory.getLogger(StemcellManagementController.class);
    
	/****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 이종 스템셀 관리 화면 이동
     * @title : goHbStemcellManagement
     * @return : String
    *****************************************************************/
    @RequestMapping(value="/config/hbstemcell", method=RequestMethod.GET)
    public String goHbStemcellManagement() {
        if(LOGGER.isInfoEnabled()){ LOGGER.info("================================> /config/hbstemcell"); }
        return "/hbdeploy/config/hbStemcellManagement";
    }
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : IaaS 별 스템셀 목록 조회
     * @title : getStemcellListDetail
     * @return : ResponseEntity<HashMap<String,Object>>
    *****************************************************************/
    @RequestMapping(value="/config/hbstemcell/list/{iaas}", method=RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getStemcellListDetail(@PathVariable String iaas) {
        if(LOGGER.isInfoEnabled()){ LOGGER.info("================================> /config/stemcell/list/"+iaas); }
        HashMap<String, Object> list = new HashMap<String, Object>();
        List<HbStemcellManagementVO> stemcellList = service.getHybridStemcellList(iaas);
        list.put("records", stemcellList);
        return new ResponseEntity<HashMap<String,Object>>(list, HttpStatus.OK);
    }
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 이종 스템셀 입력 정보
     * @title : savePublicStemcellInfo
     * @return : ResponseEntity<StemcellManagementVO>
    *****************************************************************/
    @RequestMapping(value="/config/hbstemcell/regist/info/{testFlag}",  method=RequestMethod.POST)
    public ResponseEntity<StemcellManagementVO> savePublicStemce저장llInfo(@RequestBody StemcellManagementDTO.Regist dto, @PathVariable String testFlag, Principal principal ){
        
        if(LOGGER.isInfoEnabled()){ LOGGER.info("================================> /config/stemcell/regist/info/"+testFlag); }
        StemcellManagementVO result = null;
        if("file".equalsIgnoreCase(dto.getFileType())){
            result = service.saveStemcellInfoByFilePath(dto, testFlag, principal);
        }else{
            result = service.saveStemcellInfoByURL(dto, testFlag, principal);
        }
        
        return new ResponseEntity<StemcellManagementVO>(result, HttpStatus.CREATED);
    }
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 멀티 딜리트
     * @title : publicStemcellMultiDelete
     * @return : ResponseEntity<?>
    *****************************************************************/
    @RequestMapping(value="/config/hbstemcell/Muldelete",  method=RequestMethod.DELETE)
    public ResponseEntity<?> publicStemcellMultiDelete(@RequestBody ArrayList<StemcellManagementDTO.Delete> list){
        if(LOGGER.isInfoEnabled()){ LOGGER.info("================================> /config/stemcell/deletePublicStemcell"); }
        for(int i=0;i<list.size();i++){
            service.deleteHybridStemcell(list.get(i));
        }
        
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 원격지에 있는 스템셀 다운로드
     * @title : doHybridStemcellDonwload
     * @return : ResponseEntity<?>
    *****************************************************************/
    @MessageMapping("/config/hbstemcell/regist/stemcellDownloading")
    @SendTo("/config/hbstemcell/regist/socket/logs")
    public ResponseEntity<?> doHybridStemcellDonwload(@RequestBody @Valid HbStemcellManagementDTO.Regist dto, Principal principal){
        
        if(LOGGER.isInfoEnabled()){ LOGGER.info("================================> /config/hbstemcell/regist/stemcellDownloading"); }
        donwonloadService.stemcellDownloadAsync(dto, principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 스템셀 파일 및 정보 삭제
     * @title : publicStemcellDelete
     * @return : ResponseEntity<?>
    *****************************************************************/
    @RequestMapping(value="/config/hbstemcell/delete",  method=RequestMethod.DELETE)
    public ResponseEntity<?> publicStemcellDelete(@RequestBody StemcellManagementDTO.Delete dto ){
        if(LOGGER.isInfoEnabled()){ LOGGER.info("================================> /config/stemcell/deletePublicStemcell"); }
        service.deleteHybridStemcell(dto);
        
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
