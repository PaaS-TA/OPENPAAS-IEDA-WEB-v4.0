package org.openpaas.ieda.hbdeploy.web.deploy.diego.service;

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
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoResourceConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoResourceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoResourceConfigDTO;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HbDiegoResourceConfigServiceUnitTest extends BaseHbDeployControllerUnitTest{
    
    @InjectMocks private HbDiegoResourceConfigService mockHbDiegoResourceConfigService;
    @Mock private MessageSource mockMessageSource;
    @Mock private HbDiegoResourceConfigDAO mockHbDiegoResourceConfigDAO;
    
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
     * @description : 이종 DIEGO 기본 정보 목록 조회 Unit Test
     * @title : testGetResourceConfigInfoList
     * @return : void
    *****************************************************************/
    @Test
    public void testGetResourceConfigInfoList(){
        List<HbDiegoResourceConfigVO> expectList = setDiegoResourceConfigList();
        when(mockHbDiegoResourceConfigDAO.selectResourceConfigInfoList()).thenReturn(expectList);
        List<HbDiegoResourceConfigVO> resultList = mockHbDiegoResourceConfigService.getDefaultConfigInfoList();
        assertEquals(expectList.size(), resultList.size());
        assertEquals(expectList.get(0).getResourceConfigName(), resultList.get(0).getResourceConfigName());
        assertEquals(expectList.get(0).getIaasType(), resultList.get(0).getIaasType());
        assertEquals(expectList.get(0).getId(), resultList.get(0).getId());
        assertEquals(expectList.get(0).getResourceConfigName(), resultList.get(0).getResourceConfigName());
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Resource 목록 정보 저장 Unit Test
     * @title : testInsertResourceConfigInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testInsertResourceConfigInfo(){
        HbDiegoResourceConfigDTO dto = setDiegoResourceConfigInfo("insert");
        when(mockHbDiegoResourceConfigDAO.selectResourceConfigInfoByName(anyString())).thenReturn(0);
        mockHbDiegoResourceConfigService.saveResourceConfigInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Resource 목록 정보 저장 Unit Test
     * @title : testUpdateResourceConfigInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testUpdateResourceConfigInfo(){
    	HbDiegoResourceConfigDTO dto = setDiegoResourceConfigInfo("update");
    	HbDiegoResourceConfigVO vo = setDiegoResourceConfig();
        when(mockHbDiegoResourceConfigDAO.selectResourceConfigInfoById(anyInt())).thenReturn(vo);
        when(mockHbDiegoResourceConfigDAO.selectResourceConfigInfoByName(anyString())).thenReturn(0);
        mockHbDiegoResourceConfigService.saveResourceConfigInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Resource 목록 정보 저장 Exception Unit Test
     * @title : testSaveResourceConfigInfoConflict
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testSaveResourceConfigInfoConflict(){
    	HbDiegoResourceConfigDTO dto = setDiegoResourceConfigInfo("insert");
        when(mockMessageSource.getMessage(anyString(), anyObject(), anyObject())).thenReturn("conflic_exception");
        when(mockHbDiegoResourceConfigDAO.selectResourceConfigInfoByName(anyString())).thenReturn(1);
        mockHbDiegoResourceConfigService.saveResourceConfigInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Resource 목록 정보 삭제 Exception Unit Test
     * @title : testDeleteResourceConfigInfo
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testDeleteResourceConfigInfoNull(){
    	HbDiegoResourceConfigDTO dto = setDiegoResourceConfigInfo("null");
        when(mockMessageSource.getMessage(anyString(), anyObject(), anyObject())).thenReturn("null");
        mockHbDiegoResourceConfigService.deleteResourceConfigInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Resource 목록 정보 삭제 Unit Test
     * @title : testDeleteResourceConfigInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testDeleteResourceConfigInfo(){
    	HbDiegoResourceConfigDTO dto = setDiegoResourceConfigInfo("update");
        mockHbDiegoResourceConfigService.deleteResourceConfigInfo(dto, principal);
    }
    
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 리소스 정보 값 설정
     * @title : setDiegoResourceConfigList
     * @return : List<HbDiegoNetworkConfigVO>
    *****************************************************************/
    public HbDiegoResourceConfigVO setDiegoResourceConfig(){
        HbDiegoResourceConfigVO vo = new HbDiegoResourceConfigVO();
        vo.setId(1000);
        vo.setIaasType("OPENSTACK");
        vo.setResourceConfigName("openstack_resource");
        vo.setBoshPassword("admin");
        vo.setStemcellName("bosh-stmecell-openstack-kvm-ubuntu-trusty");
        vo.setStemcellVersion("3468.21");
        vo.setSmallFlavor("m1.small");
        vo.setMediumFlavor("m1.medium");
        vo.setLargeFlavor("m1.large");
        vo.setDirectorId("1");
        vo.setCreateUserId("admin");
        return vo;
    }
    
    /****************************************************************
     * @param string 
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 리소스 정보 값 설정
     * @title : setDiegoResourceConfigInfo
     * @return : HbDiegoResourceConfigDTO
    *****************************************************************/
    public HbDiegoResourceConfigDTO setDiegoResourceConfigInfo(String type){
        HbDiegoResourceConfigDTO dto = new HbDiegoResourceConfigDTO();
        if("update".equals(type)){
            dto.setId("1");
        }
        dto.setIaasType("OPENSTACK");
        dto.setResourceConfigName("openstack_resource");
        dto.setBoshPassword("admin");
        dto.setStemcellName("bosh-stmecell-openstack-kvm-ubuntu-trusty");
        dto.setStemcellVersion("3468.21");
        dto.setSmallFlavor("m1.small");
        dto.setMediumFlavor("m1.medium");
        dto.setLargeFlavor("m1.large");
        return dto;
    }
    
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 리소스 정보 목록 값 설정
     * @title : setDiegoResourceConfigList
     * @return : List<HbDiegoNetworkConfigVO>
    *****************************************************************/
    public List<HbDiegoResourceConfigVO> setDiegoResourceConfigList(){
        List<HbDiegoResourceConfigVO> list = new ArrayList<HbDiegoResourceConfigVO>();
        HbDiegoResourceConfigVO vo = new HbDiegoResourceConfigVO();
        vo.setId(1000);
        vo.setIaasType("OPENSTACK");
        vo.setResourceConfigName("openstack_resource");
        vo.setBoshPassword("admin");
        vo.setStemcellName("bosh-stmecell-openstack-kvm-ubuntu-trusty");
        vo.setStemcellVersion("3468.21");
        vo.setSmallFlavor("m1.small");
        vo.setMediumFlavor("m1.medium");
        vo.setLargeFlavor("m1.large");
        vo.setDirectorId("1");
        vo.setCreateUserId("admin");
        list.add(vo);
        return list;
    }
}
