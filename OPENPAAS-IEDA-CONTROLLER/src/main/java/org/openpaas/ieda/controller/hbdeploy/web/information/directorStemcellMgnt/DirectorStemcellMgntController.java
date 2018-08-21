package org.openpaas.ieda.controller.hbdeploy.web.information.directorStemcellMgnt;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.openpaas.ieda.common.api.LocalDirectoryConfiguration;
import org.openpaas.ieda.controller.common.BaseController;
import org.openpaas.ieda.hbdeploy.web.config.setting.dto.HbDirectorConfigDTO;
import org.openpaas.ieda.hbdeploy.web.config.stemcell.dao.HbStemcellManagementVO;
import org.openpaas.ieda.hbdeploy.web.config.stemcell.service.HbStemcellManagementService;
import org.openpaas.ieda.hbdeploy.web.information.directorStemcellMgnt.dto.DirectorStemcellDTO;
import org.openpaas.ieda.hbdeploy.web.information.directorStemcellMgnt.service.HbStemcellDeleteAsyncService;
import org.openpaas.ieda.hbdeploy.web.information.directorStemcellMgnt.service.HbStemcellService;
import org.openpaas.ieda.hbdeploy.web.information.directorStemcellMgnt.service.HbStemcellUploadAsyncService;
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
public class DirectorStemcellMgntController extends BaseController {

    @Autowired private HbStemcellService service;
    @Autowired private HbStemcellManagementService stemcellManagementService;
    @Autowired private HbStemcellDeleteAsyncService stemcellDeleteService;
    @Autowired private HbStemcellUploadAsyncService stemcellUploadService;
    
    final private static String STEMCELL_DIR = LocalDirectoryConfiguration.getStemcellDir();
    final private static Logger LOGGER = LoggerFactory.getLogger(DirectorStemcellMgntController.class);

    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 스템셀 업로드 화면을 호출하여 이동
     * @title : goListStemcell
     * @return : String
    *****************************************************************/
    @RequestMapping(value = "/info/hbstemcell", method = RequestMethod.GET)
    public String goListStemcell() {
        return "/hbdeploy/information/listHbStemcell";
    }
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 업로드된 스템셀 목록 조회 
     * @title : getUploadStemcellLIst
     * @return : ResponseEntity<HashMap<String,Object>>
    *****************************************************************/
    @RequestMapping(value = "/info/hbstemcell/list/upload", method = RequestMethod.POST)
    public ResponseEntity<HashMap<String, Object>> getUploadStemcellList(@RequestBody HbDirectorConfigDTO dto) {
        if(LOGGER.isInfoEnabled()){ LOGGER.info("=======================> /info/stemcell/list/upload"); }
        List<HbStemcellManagementVO> contents = service.getStemcellList(dto);
        HashMap<String, Object> result = new HashMap<String, Object>();
        if ( contents != null ) {
            result.put("total", contents.size());
            result.put("records", contents);
        }
        return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
    }

    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 로컬에 다운로드된 스템셀 목록 조회
     * @title : getLocalStemcellList
     * @return : ResponseEntity<HashMap<String,Object>>
    *****************************************************************/
    @RequestMapping(value = "/info/hbstemcell/list/local/{iaas}", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getLocalStemcellList(@PathVariable String iaas) {
        if(LOGGER.isInfoEnabled()){ LOGGER.info("=======================> /info/stemcell/list/local/"+iaas); }
        List<HbStemcellManagementVO> contents = stemcellManagementService.getLocalStemcellList(iaas);
        
        HashMap<String, Object> result = new HashMap<String, Object>();
        int size = 0;
        if( contents != null ) {
            size = contents.size();
        }
        result.put("total", size);
        result.put("records", contents);
        return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
    }

    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 스템셀 업로드
     * @title : doUploadStemcell
     * @return : ResponseEntity<Object>
    *****************************************************************/
    @MessageMapping("/info/hbstemcell/upload/stemcellUploading")
    @SendTo("/info/hbstemcell/upload/logs")
    public ResponseEntity<Object> uploadStemcell(@RequestBody @Valid DirectorStemcellDTO.Upload dto, Principal principal) {
        if(LOGGER.isInfoEnabled()){ LOGGER.info("=======================> /info/stemcell/upload/stemcellUploading"); }
        stemcellUploadService.uploadStemcellAsync(STEMCELL_DIR, dto.getFileName(), principal.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 업로드 된 스템셀 삭제
     * @title : doDeleteStemcell
     * @return : ResponseEntity<Object>
    *****************************************************************/
    @MessageMapping("/info/hbstemcell/delete/stemcellDelete")
    @SendTo("/info/hbstemcell/delete/logs")
    public ResponseEntity<Object> deleteStemcell(@RequestBody @Valid DirectorStemcellDTO.Delete dto, Principal principal) {
        if(LOGGER.isInfoEnabled()){ LOGGER.info("=======================> /info/stemcell/delete/stemcellDelete"); }
        stemcellDeleteService.deleteStemcellAsync(dto.getStemcellName(), dto.getVersion(), principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
