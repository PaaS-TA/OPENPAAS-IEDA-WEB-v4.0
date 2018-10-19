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
import org.openpaas.ieda.hbdeploy.web.config.setting.dao.HbDirectorConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfDefaultConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfDefaultConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfDefaultConfigDTO;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HbCfDefaultConfigServiceUnitTest extends BaseHbDeployControllerUnitTest{
    
    @InjectMocks private HbCfDefaultConfigService mockHbCfDefaultConfigService;
    @Mock private MessageSource mockMessageSource;
    @Mock private HbCfDefaultConfigDAO mockHbCfDefaultConfigDAO;
    @Mock private HbDirectorConfigDAO mockHbDirectorConfigDAO;
    
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
     * @description : 이종 CF  기본 정보 목록 조회 Unit Test
     * @title : testGetDefaultConfigInfoList
     * @return : void
    *****************************************************************/
    @Test
    public void testGetDefaultConfigInfoList(){
        List<HbCfDefaultConfigVO> expectList = setCfDefaultConfigList();
        when(mockHbCfDefaultConfigDAO.selectCfDefaultConfigInfoList()).thenReturn(expectList);
        List<HbCfDefaultConfigVO> resultList = mockHbCfDefaultConfigService.getDefaultConfigInfoList();
        assertEquals(expectList.size(), resultList.size());
        assertEquals(expectList.get(0).getDefaultConfigName(), resultList.get(0).getDefaultConfigName());
        assertEquals(expectList.get(0).getIaasType(), resultList.get(0).getIaasType());
        assertEquals(expectList.get(0).getDomain(), resultList.get(0).getDomain());
        assertEquals(expectList.get(0).getDomainOrganization(), resultList.get(0).getDomainOrganization());
        assertEquals(expectList.get(0).getId(), resultList.get(0).getId());
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  기본 정보 삭제 Unit Test
     * @title : testDeleteDefaultConfigInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testDeleteDefaultConfigInfo(){
        HbCfDefaultConfigDTO dto = new HbCfDefaultConfigDTO();
        dto.setId("1");
        dto.setIaasType("Openstack");
        mockHbCfDefaultConfigService.deleteDefaultConfigInfo(dto, principal);
    }
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  기본 정보 삭제 Exception Unit Test
     * @title : testDeleteDefaultConfigInfoEmpty
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testDeleteDefaultConfigInfoEmpty(){
        HbCfDefaultConfigDTO dto = new HbCfDefaultConfigDTO();
        dto.setIaasType("Openstack");
        when(mockMessageSource.getMessage(anyString(), anyObject(), anyObject())).thenReturn("empty");
        mockHbCfDefaultConfigService.deleteDefaultConfigInfo(dto, principal);
    }
    
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  기본 정보 저장 값 설정
     * @title : testSveDefaultConfigInfo
     * @return : void
    *****************************************************************/
    private HbCfDefaultConfigDTO setDefaultConfigInfo(String type) {
        HbCfDefaultConfigDTO dto = new HbCfDefaultConfigDTO();
        dto.setDefaultConfigName("defalut-config");
        dto.setDomain("cf.com");
        dto.setDirectorInfo(1);
        dto.setDomainOrganization("paas");
        dto.setIngestorIp("10.0.0.1");
        dto.setLoginSecret("admin");
        dto.setIaasType("Openstack");
        dto.setReleases("cf/287");
        dto.setLoggregatorReleases("loggregator/3.1");
        if("update".equalsIgnoreCase(type)){
            dto.setId("1");
        }
        
        return dto;
    }
    
    private HbCfDefaultConfigVO setCfDefaultConfig(){
        HbCfDefaultConfigVO vo = new HbCfDefaultConfigVO();
        vo.setDefaultConfigName("defalut-config");
        vo.setDomain("cf.com");
        vo.setDomainOrganization("paas");
        vo.setReleaseName("cf");
        vo.setReleaseVersion("287");
        vo.setDirectorId(1);
        vo.setLoggregatorReleaseName("loggregator");
        vo.setLoggregatorReleaseVersion("3.1");
        vo.setIngestorIp("10.0.0.1");
        vo.setLoginSecret("admin");
        vo.setIaasType("Openstack");
        return vo;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  기본 정보 목록 조회 값 설정
     * @title : setCfDefaultConfigList
     * @return : void
    *****************************************************************/
    private List<HbCfDefaultConfigVO> setCfDefaultConfigList() {
        List<HbCfDefaultConfigVO> list = new ArrayList<HbCfDefaultConfigVO>();
        HbCfDefaultConfigVO vo = new HbCfDefaultConfigVO();
        vo.setDefaultConfigName("defalut-config");
        vo.setDomain("cf.com");
        vo.setDomainOrganization("paas");
        vo.setReleaseName("cf");
        vo.setReleaseVersion("287");
        vo.setDirectorId(1);
        vo.setLoggregatorReleaseName("loggregator");
        vo.setLoggregatorReleaseVersion("3.1");
        vo.setIngestorIp("10.0.0.1");
        vo.setLoginSecret("admin");
        vo.setIaasType("Openstack");
        vo.setId(1);
        list.add(vo);
        return list;
    }
    
}
