package org.openpaas.ieda.hbdeploy.web.deploy.cf.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
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
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfResourceConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfResourceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfResourceConfigDTO;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HbCfResourceConfigServiceUnitTest extends BaseHbDeployControllerUnitTest{
    
    @InjectMocks private HbCfResourceConfigService mockHbCfResourceConfigService;
    @Mock private MessageSource mockMessageSource;
    @Mock private HbCfResourceConfigDAO mockHbCfResourceConfigDAO;
    
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
     * @description : Resource 목록 정보 조회 Unit Test
     * @title : testGetResourceConfigInfoList
     * @return : void
    *****************************************************************/
    @Test
    public void testGetResourceConfigInfoList(){
        List<HbCfResourceConfigVO> expectList = setCfCrednetialConfigList();
        when(mockHbCfResourceConfigDAO.selectCfResourceConfigList()).thenReturn(expectList);
        List< HbCfResourceConfigVO> resultList = mockHbCfResourceConfigService.getResourceConfigInfoList();
        assertEquals(expectList.size(), resultList.size());
        assertEquals(expectList.get(0).getDirectorId(), resultList.get(0).getDirectorId());
        assertEquals(expectList.get(0).getIaasType(), resultList.get(0).getIaasType());
        assertEquals(expectList.get(0).getSmallFlavor(), resultList.get(0).getSmallFlavor());
        assertEquals(expectList.get(0).getId(), resultList.get(0).getId());
        assertEquals(expectList.get(0).getMediumFlavor(), resultList.get(0).getMediumFlavor());
        assertEquals(expectList.get(0).getLargeFlavor(), resultList.get(0).getLargeFlavor());
        assertEquals(expectList.get(0).getStemcellName(), resultList.get(0).getStemcellName());
        assertEquals(expectList.get(0).getStemcellVersion(), resultList.get(0).getStemcellVersion());
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Resource 목록 정보 저장 Unit Test
     * @title : testInsertResourceConfigInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testInsertResourceConfigInfo(){
        HbCfResourceConfigDTO dto = setCfResourceConfigInfo("insert");
        when(mockHbCfResourceConfigDAO.selectCfResourceConfigInfoByName(anyString())).thenReturn(0);
        mockHbCfResourceConfigService.saveResourceConfigInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Resource 목록 정보 저장 Unit Test
     * @title : testUpdateResourceConfigInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testUpdateResourceConfigInfo(){
        HbCfResourceConfigDTO dto = setCfResourceConfigInfo("update");
        HbCfResourceConfigVO vo = setCfCrednetialConfig();
        when(mockHbCfResourceConfigDAO.selectCfResourceConfigInfoById(anyInt())).thenReturn(vo);
        when(mockHbCfResourceConfigDAO.selectCfResourceConfigInfoByName(anyString())).thenReturn(0);
        mockHbCfResourceConfigService.saveResourceConfigInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Resource 목록 정보 저장 Exception Unit Test
     * @title : testSaveResourceConfigInfoConflict
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testSaveResourceConfigInfoConflict(){
        HbCfResourceConfigDTO dto = setCfResourceConfigInfo("insert");
        when(mockMessageSource.getMessage(anyString(), anyObject(), anyObject())).thenReturn("conflic_exception");
        when(mockHbCfResourceConfigDAO.selectCfResourceConfigInfoByName(anyString())).thenReturn(1);
        mockHbCfResourceConfigService.saveResourceConfigInfo(dto, principal);
    }
    
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Resource 목록 정보 삭제 Unit Test
     * @title : testDeleteResourceConfigInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testDeleteResourceConfigInfo(){
        HbCfResourceConfigDTO dto = setCfResourceConfigInfo("update");
        mockHbCfResourceConfigService.deleteResourceConfigInfo(dto, principal);
    }
    
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Resource 목록 정보 삭제 Exception Unit Test
     * @title : testDeleteResourceConfigInfo
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testDeleteResourceConfigInfoNull(){
        HbCfResourceConfigDTO dto = setCfResourceConfigInfo("insert");
        when(mockMessageSource.getMessage(anyString(), anyObject(), anyObject())).thenReturn("null");
        mockHbCfResourceConfigService.deleteResourceConfigInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  리소스 정보 목록 조회 결과 값 설정
     * @title : setCfCrednetialConfigList
     * @return : List<HbCfResourceConfigVO>
    *****************************************************************/
    private HbCfResourceConfigVO setCfCrednetialConfig() {
        HbCfResourceConfigVO vo = new HbCfResourceConfigVO();
        vo.setDirectorId("1");
        vo.setIaasType("Openstack");
        vo.setId(1);
        vo.setSmallFlavor("m1.small");
        vo.setMediumFlavor("m1.medium");
        vo.setLargeFlavor("m1.large");
        vo.setResourceConfigName("resource-config");
        vo.setStemcellName("openstakc-ubuntu");
        vo.setStemcellVersion("3621.48");
        return vo;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  리소스 정보 저장/삭제 요청 값 설정
     * @title : setCfResourceConfigInfo
     * @return : HbCfResourceConfigDTO
    *****************************************************************/
    private HbCfResourceConfigDTO setCfResourceConfigInfo(String type) {
        HbCfResourceConfigDTO dto = new HbCfResourceConfigDTO();
        dto.setDirectorInfo("1");
        dto.setIaasType("Openstack");
        if("update".equalsIgnoreCase(type)){
            dto.setId("1");
        }
        dto.setSmallFlavor("m1.small");
        dto.setMediumFlavor("m1.medium");
        dto.setLargeFlavor("m1.large");
        dto.setResourceConfigName("resource-config");
        dto.setStemcellName("ubuntu-trust");
        dto.setStemcellVersion("3568.21");
        return dto;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  리소스 정보 목록 조회 결과 값 설정
     * @title : setCfCrednetialConfigList
     * @return : List<HbCfResourceConfigVO>
    *****************************************************************/
    private List<HbCfResourceConfigVO> setCfCrednetialConfigList() {
        List<HbCfResourceConfigVO> list = new ArrayList<HbCfResourceConfigVO>();
        HbCfResourceConfigVO vo = new HbCfResourceConfigVO();
        vo.setDirectorId("1");
        vo.setIaasType("Openstack");
        vo.setId(1);
        vo.setSmallFlavor("m1.small");
        vo.setMediumFlavor("m1.medium");
        vo.setLargeFlavor("m1.large");
        vo.setResourceConfigName("resource-config");
        vo.setStemcellName("openstakc-ubuntu");
        vo.setStemcellVersion("3621.48");
        list.add(vo);
        return list;
    }
}
