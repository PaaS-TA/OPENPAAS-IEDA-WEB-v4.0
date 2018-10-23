package org.openpaas.ieda.hbdeploy.web.deploy.cf.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.hbdeploy.web.common.base.BaseHbDeployControllerUnitTest;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfKeyConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfKeyConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfKeyConfigDTO;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HbCfKeyConfigServiceUnitTest extends BaseHbDeployControllerUnitTest{
    
    @InjectMocks HbCfKeyConfigService mockHbCfKeyConfigService;
    @Mock MessageSource mockMessageSource;
    @Mock HbCfKeyConfigDAO mockHbCfKeyConfigDAO;
    
    private Principal principal = null;
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 유닛 테스트가 실행되기 전 호출
     * @title : setUp
     * @return : void
    ***************************************************/
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        principal = getLoggined();
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  인증서 정보 목록 조회 Unit Test
     * @title : testGetDefaultConfigInfoList
     * @return : void
    *****************************************************************/
    @Test
    public void testGetDefaultConfigInfoList(){
        List<HbCfKeyConfigVO> expectList = setCfCrednetialConfigList();
        when(mockHbCfKeyConfigDAO.selectKeyConfigInfoList()).thenReturn(expectList);
        List<HbCfKeyConfigVO> resultList = mockHbCfKeyConfigService.getKeyConfigInfoList();
        assertEquals(expectList.size(), resultList.size());
        assertEquals(expectList.get(0).getId(), resultList.get(0).getId());
        assertEquals(expectList.get(0).getKeyConfigName(), resultList.get(0).getKeyConfigName());
        assertEquals(expectList.get(0).getKeyFileName(), resultList.get(0).getKeyFileName());
        assertEquals(expectList.get(0).getDomain(), resultList.get(0).getDomain());
        assertEquals(expectList.get(0).getIaasType(), resultList.get(0).getIaasType());
        assertEquals(expectList.get(0).getReleaseName(), resultList.get(0).getReleaseName());
        assertEquals(expectList.get(0).getReleaseVersion(), resultList.get(0).getReleaseVersion());
        assertEquals(expectList.get(0).getCity(), resultList.get(0).getCity());
        assertEquals(expectList.get(0).getCountryCode(), resultList.get(0).getCountryCode());
        assertEquals(expectList.get(0).getCompany(), resultList.get(0).getCompany());
        assertEquals(expectList.get(0).getEmailAddress(), resultList.get(0).getEmailAddress());
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  인증서 정보 저장 Exception Unit Test
     * @title : testUpdateKeyConfigInfoConflict
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testUpdateKeyConfigInfoConflict(){
        HbCfKeyConfigDTO dto = setCfKeyConfigInfo("insert");
        when(mockMessageSource.getMessage(anyString(), anyObject(), anyObject())).thenReturn("conflic_exception");
        when(mockHbCfKeyConfigDAO.selectCfKeyConfigInfoByName(anyString())).thenReturn(1);
        mockHbCfKeyConfigService.saveKeyConfigInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  인증서 정보 삭제 Unit Test
     * @title : testDeleteKeyConfigInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testDeleteKeyConfigInfo(){
        HbCfKeyConfigDTO dto = new HbCfKeyConfigDTO();
        dto.setId("1");
        dto.setIaasType("Openstack");
        dto.setKeyFileName("1_test.yml");
        mockHbCfKeyConfigService.deleteKeyConfigInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  인증서 정보 삭제 Exception Unit Test
     * @title : testDeleteKeyConfigInfoNull
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testDeleteKeyConfigInfoNull(){
        HbCfKeyConfigDTO dto = new HbCfKeyConfigDTO();
        when(mockMessageSource.getMessage(anyString(), anyObject(), anyObject())).thenReturn("null");
        mockHbCfKeyConfigService.deleteKeyConfigInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  인증서 정보 값 설정
     * @title : setCfCrednetialConfig
     * @return : void
    *****************************************************************/
    private HbCfKeyConfigVO setCfCrednetialConfig() {
        HbCfKeyConfigVO vo = new HbCfKeyConfigVO();
        vo.setCity("seoul");
        vo.setCompany("paas-ta");
        vo.setKeyFileName("creds.yml");
        vo.setCountryCode("seoul");
        vo.setDomain("cf.com");
        vo.setEmailAddress("leedh@cloud4u.co.kr");
        vo.setReleaseVersion("2.7.0");
        vo.setReleaseName("cf");
        vo.setKeyConfigName("Key-config");
        vo.setIaasType("Openstack");
        vo.setId(1);
        return vo;
    }
    
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  정보 저장 정보 값 설정
     * @title : setCfCrednetialConfigList
     * @return : void
    *****************************************************************/
    private HbCfKeyConfigDTO setCfKeyConfigInfo(String type) {
        HbCfKeyConfigDTO dto = new HbCfKeyConfigDTO();
        dto.setCity("seoul");
        dto.setCompany("paasta");
        dto.setCountryCode("korea");
        dto.setKeyFileName("creds.yml");
        dto.setKeyConfigName("Key-config");
        dto.setDomain("cf.com");
        if("update".equalsIgnoreCase(type)){
            dto.setId("1");
        }
        dto.setIaasType("Openstack");
        dto.setReleaseName("cf-");
        dto.setReleaseVersion("2.7.0");
        return dto;
    }

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  인증서 목록 정보 값 설정
     * @title : setCfCrednetialConfigList
     * @return : void
    *****************************************************************/
    private List<HbCfKeyConfigVO> setCfCrednetialConfigList() {
        List<HbCfKeyConfigVO> list = new ArrayList<HbCfKeyConfigVO>();
        HbCfKeyConfigVO vo = new HbCfKeyConfigVO();
        vo.setCity("seoul");
        vo.setCompany("paas-ta");
        vo.setKeyFileName("creds.yml");
        vo.setCountryCode("seoul");
        vo.setDomain("cf.com");
        vo.setEmailAddress("leedh@cloud4u.co.kr");
        vo.setReleaseVersion("2.7.0");
        vo.setReleaseName("cf");
        vo.setKeyConfigName("Key-config");
        vo.setIaasType("Openstack");
        vo.setId(1);
        list.add(vo);
        return list;
    }
}
