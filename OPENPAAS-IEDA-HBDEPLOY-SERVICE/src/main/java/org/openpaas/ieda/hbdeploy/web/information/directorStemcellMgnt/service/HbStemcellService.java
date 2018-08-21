package org.openpaas.ieda.hbdeploy.web.information.directorStemcellMgnt.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.GetMethod;
import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.hbdeploy.api.stemcell.StemcellListDTO;
import org.openpaas.ieda.hbdeploy.web.config.setting.dao.HbDirectorConfigVO;
import org.openpaas.ieda.hbdeploy.web.config.setting.dto.HbDirectorConfigDTO;
import org.openpaas.ieda.hbdeploy.web.config.setting.service.HbDirectorConfigService;
import org.openpaas.ieda.hbdeploy.web.config.stemcell.dao.HbStemcellManagementVO;
import org.openpaas.ieda.hbdeploy.api.director.utility.DirectorRestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class HbStemcellService {
    
    @Autowired 
    private HbDirectorConfigService hbDirectorConfigService;
    @Autowired 
    private MessageSource message;
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 업로드된 스템셀 조회 요청
     * @title : getStemcellList
     * @return : List<StemcellManagementVO>
    *****************************************************************/
    public List<HbStemcellManagementVO> getStemcellList(HbDirectorConfigDTO dto) {
        HbDirectorConfigVO selectedDirector = hbDirectorConfigService.getSelectedDirectorByUrl(dto.getDirectorUrl());
        List<HbStemcellManagementVO> stemcellInfoList= new ArrayList<HbStemcellManagementVO>();
        try {
            HttpClient client = DirectorRestHelper.getHttpClient(selectedDirector.getDirectorPort().intValue());
            GetMethod get = new GetMethod(DirectorRestHelper.getStemcellsURI(selectedDirector.getDirectorUrl(), selectedDirector.getDirectorPort().intValue()));
            get = (GetMethod)DirectorRestHelper.setAuthorization(selectedDirector.getUserId(), selectedDirector.getUserPassword(), (HttpMethodBase)get);
            client.executeMethod(get);
            stemcellInfoList= setUploadedStemcellList(get.getResponseBodyAsString());
        } catch (HttpException e) {
            throw new CommonException(message.getMessage("common.internalServerError.exception.code", null, Locale.KOREA),
                    message.getMessage("common.internalServerError.message", null, Locale.KOREA), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            throw new CommonException(message.getMessage("common.internalServerError.exception.code", null, Locale.KOREA),
                    message.getMessage("common.internalServerError.message", null, Locale.KOREA), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return stemcellInfoList;
    }
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 업로드된 스템셀 목록 정보 설정
     * @title : setUploadedStemcellList
     * @return : List<StemcellManagementVO>
    *****************************************************************/
    public List<HbStemcellManagementVO> setUploadedStemcellList( String responseBody ){
        List<HbStemcellManagementVO> stemcellInfoList= new ArrayList<HbStemcellManagementVO>();
        if ( !StringUtils.isEmpty(responseBody) ) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                StemcellListDTO[] stemcells = mapper.readValue(responseBody, StemcellListDTO[].class);
                int idx = 0;
                for ( StemcellListDTO stemcell : stemcells ) {
                HbStemcellManagementVO stemcellInfo = new HbStemcellManagementVO();
                    stemcellInfo.setRecid(idx++);
                    stemcellInfo.setStemcellFileName(stemcell.getName());
                    stemcellInfo.setOs(stemcell.getOperatingSystem());
                    stemcellInfo.setStemcellVersion(stemcell.getVersion());
                    stemcellInfoList.add(stemcellInfo);
                }
            } catch (JsonParseException e) {
                throw new CommonException(message.getMessage("common.internalServerError.exception.code", null, Locale.KOREA),
                        message.getMessage("common.internalServerError.message", null, Locale.KOREA), HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (JsonMappingException e) {
                throw new CommonException(message.getMessage("common.internalServerError.exception.code", null, Locale.KOREA),
                        message.getMessage("common.internalServerError.message", null, Locale.KOREA), HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (IOException e) {
                throw new CommonException(message.getMessage("common.internalServerError.exception.code", null, Locale.KOREA),
                        message.getMessage("common.internalServerError.message", null, Locale.KOREA), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return stemcellInfoList;
    }

}
