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
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoInstanceConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoInstanceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoInstanceConfigDTO;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HbDiegoInstanceConfigServiceUnitTest extends BaseHbDeployControllerUnitTest{
    
    @InjectMocks private HbDiegoInstanceConfigService mockHbDiegoInstanceConfigService;
    @Mock private MessageSource mockMessageSource;
    @Mock private HbDiegoInstanceConfigDAO mockHbDiegoInstanceConfigDAO;
    
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
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Diego  Instance 목록 정보 조회 Unit Test
     * @title : testGetHbDiegoInstanceConfigInfoList
     * @return : void
    ***************************************************/
    @Test
    public void testGetHbDiegoInstanceConfigInfoList(){
        List<HbDiegoInstanceConfigVO> expectList = setDiegoInstanceConfigList();
        when(mockHbDiegoInstanceConfigDAO.selectDiegoInstanceInfoList()).thenReturn(expectList);
        List<HbDiegoInstanceConfigVO>  resultList = mockHbDiegoInstanceConfigService.getInstanceConfigList();
        assertEquals(expectList.size(), resultList.size());
        assertEquals(expectList.get(0).getIaasType(), resultList.get(0).getIaasType());
        assertEquals(expectList.get(0).getId(), resultList.get(0).getId());
        
        assertEquals(expectList.get(0).getAccess_z1(), resultList.get(0).getAccess_z1());
        assertEquals(expectList.get(0).getBrain_z1(), resultList.get(0).getBrain_z1());
        assertEquals(expectList.get(0).getCc_bridge_z1(), resultList.get(0).getCc_bridge_z1());
        assertEquals(expectList.get(0).getCell_z1(), resultList.get(0).getCell_z1());
        assertEquals(expectList.get(0).getDatabase_z1(), resultList.get(0).getDatabase_z1());
        
        assertEquals(expectList.get(0).getAccess_z2(), resultList.get(0).getAccess_z2());
        assertEquals(expectList.get(0).getBrain_z2(), resultList.get(0).getBrain_z2());
        assertEquals(expectList.get(0).getCc_bridge_z2(), resultList.get(0).getCc_bridge_z2());
        assertEquals(expectList.get(0).getCell_z2(), resultList.get(0).getCell_z2());
        assertEquals(expectList.get(0).getDatabase_z2(), resultList.get(0).getDatabase_z2());
        
        assertEquals(expectList.get(0).getAccess_z3(), resultList.get(0).getAccess_z3());
        assertEquals(expectList.get(0).getBrain_z3(), resultList.get(0).getBrain_z3());
        assertEquals(expectList.get(0).getCc_bridge_z3(), resultList.get(0).getCc_bridge_z3());
        assertEquals(expectList.get(0).getCell_z3(), resultList.get(0).getCell_z3());
        assertEquals(expectList.get(0).getDatabase_z3(), resultList.get(0).getDatabase_z3());
        
        assertEquals(expectList.get(0).getInstanceConfigName(), resultList.get(0).getInstanceConfigName());
        assertEquals(expectList.get(0).getNetworkConfigInfo(), resultList.get(0).getNetworkConfigInfo());
        
        assertEquals(expectList.get(0).getInstanceConfigName(), resultList.get(0).getInstanceConfigName());
    }
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Diego  Instance 정보 저장 Unit Test
     * @title : testInsertHbDiegoInstanceConfigInfo
     * @return : void
    ***************************************************/
    @Test
    public void testInsertHbDiegoInstanceConfigInfo(){
        HbDiegoInstanceConfigDTO dto = setDiegoInstanceConfigInfo("insert");
        when(mockHbDiegoInstanceConfigDAO.selectInstanceConfigByName(anyString())).thenReturn(0);
        mockHbDiegoInstanceConfigService.saveInstanceConfig(dto, principal);
    }
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Diego  Instance 정보 저장 Unit Test
     * @title : testUpdateHbDiegoInstanceConfigInfo
     * @return : void
    ***************************************************/
    @Test
    public void testUpdateHbDiegoInstanceConfigInfo(){
        HbDiegoInstanceConfigDTO dto = setDiegoInstanceConfigInfo("update");
        HbDiegoInstanceConfigVO vo = setDiegoInstanceConfig();
        when(mockHbDiegoInstanceConfigDAO.selectInstanceConfigById(anyInt())).thenReturn(vo);
        when(mockHbDiegoInstanceConfigDAO.selectInstanceConfigByName(anyString())).thenReturn(0);
        mockHbDiegoInstanceConfigService.saveInstanceConfig(dto, principal);
    }
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Diego  Instance 정보 저장 Exception Unit Test
     * @title : testUpdateHbDiegoInstanceConfigInfo
     * @return : void
    ***************************************************/
    @Test(expected=CommonException.class)
    public void testSaveHbDiegoInstanceConfigInfoConflict(){
        HbDiegoInstanceConfigDTO dto = setDiegoInstanceConfigInfo("insert");
        when(mockHbDiegoInstanceConfigDAO.selectInstanceConfigByName(anyString())).thenReturn(1);
        when(mockMessageSource.getMessage(anyString(), anyObject(), anyObject())).thenReturn("conflict");
        mockHbDiegoInstanceConfigService.saveInstanceConfig(dto, principal);
    }
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Diego  Instance 정보 삭제 Unit Test
     * @title : testUpdateHbDiegoInstanceConfigInfo
     * @return : void
    ***************************************************/
    @Test
    public void testDeleteHbDiegoInstanceConfigInfo(){
        HbDiegoInstanceConfigDTO dto = new HbDiegoInstanceConfigDTO();
        dto.setId("1");
        dto.setIaasType("Openstack");
        mockHbDiegoInstanceConfigService.deleteInstanceConfig(dto, principal);
    }
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Diego  Instance 정보 삭제 Exception Unit Test
     * @title : testUpdateHbDiegoInstanceConfigInfo
     * @return : void
    ***************************************************/
    @Test(expected=CommonException.class)
    public void testDeleteHbDiegoInstanceConfigInfoNull(){
        HbDiegoInstanceConfigDTO dto = new HbDiegoInstanceConfigDTO();
        when(mockMessageSource.getMessage(anyString(), anyObject(), anyObject())).thenReturn("null");
        mockHbDiegoInstanceConfigService.deleteInstanceConfig(dto, principal);
    }
    
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 Diego  인스턴스 목록 정보 조회 결과 값 설정
     * @title : setDiegoInstanceConfigList
     * @return : List<HbDiegoCredentialConfigVO>
    *****************************************************************/
    private HbDiegoInstanceConfigVO setDiegoInstanceConfig() {
        HbDiegoInstanceConfigVO vo = new HbDiegoInstanceConfigVO();
        vo.setId(1);
        vo.setIaasType("Openstack");
        vo.setDefaultConfigInfo("default-config");
        vo.setInstanceConfigName("instance");
        vo.setNetworkConfigInfo("network-config");
        vo.setAccess_z1("1");
        vo.setBrain_z1("1");
        vo.setCc_bridge_z1("1");
        vo.setCell_z1("1");
        vo.setDatabase_z1("1");
        
        vo.setAccess_z2("1");
        vo.setBrain_z2("1");
        vo.setCc_bridge_z2("1");
        vo.setCell_z2("1");
        vo.setDatabase_z2("1");
        
        vo.setAccess_z3("1");
        vo.setBrain_z3("1");
        vo.setCc_bridge_z3("1");
        vo.setCell_z3("1");
        vo.setDatabase_z3("1");
        return vo;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 Diego  Job 정보 저장/삭제 값 설정
     * @title : setDiegoInstanceConfigInfo
     * @return : HbDiegoInstanceConfigDTO
    *****************************************************************/
    private HbDiegoInstanceConfigDTO setDiegoInstanceConfigInfo(String type) {
        HbDiegoInstanceConfigDTO dto = new HbDiegoInstanceConfigDTO();
        dto.setIaasType("Openstack");
        dto.setDefaultConfigInfo("default-config");
        dto.setInstanceConfigName("instance");
        dto.setNetworkConfigInfo("network-config");
        if("update".equalsIgnoreCase(type)){
            dto.setId("1");
        }
        dto.setAccess_z1("1");
        dto.setBrain_z1("1");
        dto.setCc_bridge_z1("1");
        dto.setCell_z1("1");
        dto.setDatabase_z1("1");
        
        dto.setAccess_z2("1");
        dto.setBrain_z2("1");
        dto.setCc_bridge_z2("1");
        dto.setCell_z2("1");
        dto.setDatabase_z2("1");
        
        dto.setAccess_z3("1");
        dto.setBrain_z3("1");
        dto.setCc_bridge_z3("1");
        dto.setCell_z3("1");
        dto.setDatabase_z3("1");
        return dto;
    }
    
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 Diego  인스턴스 목록 정보 조회 결과 값 설정
     * @title : setDiegoInstanceConfigList
     * @return : List<HbDiegoCredentialConfigVO>
    *****************************************************************/
    private List<HbDiegoInstanceConfigVO> setDiegoInstanceConfigList() {
        List<HbDiegoInstanceConfigVO> list = new ArrayList<HbDiegoInstanceConfigVO>();
        HbDiegoInstanceConfigVO vo = new HbDiegoInstanceConfigVO();
        vo.setId(1);
        vo.setIaasType("Openstack");
        vo.setDefaultConfigInfo("default-config");
        vo.setInstanceConfigName("instance");
        vo.setNetworkConfigInfo("network-config");
        vo.setAccess_z1("1");
        vo.setBrain_z1("1");
        vo.setCc_bridge_z1("1");
        vo.setCell_z1("1");
        vo.setDatabase_z1("1");
        
        vo.setAccess_z2("1");
        vo.setBrain_z2("1");
        vo.setCc_bridge_z2("1");
        vo.setCell_z2("1");
        vo.setDatabase_z2("1");
        
        vo.setAccess_z3("1");
        vo.setBrain_z3("1");
        vo.setCc_bridge_z3("1");
        vo.setCell_z3("1");
        vo.setDatabase_z3("1");
        list.add(vo);
        return list;
    }
    
}
