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
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfInstanceConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfInstanceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfInstanceConfigDTO;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HbCfInstanceConfigServiceUnitTest extends BaseHbDeployControllerUnitTest{
    
    @InjectMocks private HbCfInstanceConfigService mockHbCfInstanceConfigService;
    @Mock private MessageSource mockMessageSource;
    @Mock private HbCfInstanceConfigDAO mockHbCfInstanceConfigDAO;
    
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
     * @description : CF  Instance 목록 정보 조회 Unit Test
     * @title : testGetHbCfInstanceConfigInfoList
     * @return : void
    ***************************************************/
    @Test
    public void testGetHbCfInstanceConfigInfoList(){
        List<HbCfInstanceConfigVO> expectList = setCfInstanceConfigList();
        when(mockHbCfInstanceConfigDAO.selectInstanceConfigList()).thenReturn(expectList);
        List<HbCfInstanceConfigVO>  resultList = mockHbCfInstanceConfigService.getInstanceConfigList();
        assertEquals(expectList.size(), resultList.size());
        assertEquals(expectList.get(0).getApi_worker_z1(), resultList.get(0).getApi_worker_z1());
        assertEquals(expectList.get(0).getApi_z1(), resultList.get(0).getApi_z1());
        assertEquals(expectList.get(0).getBlobstore_z1(), resultList.get(0).getBlobstore_z1());
        assertEquals(expectList.get(0).getClock_z1(), resultList.get(0).getClock_z1());
        assertEquals(expectList.get(0).getConsul_z1(), resultList.get(0).getConsul_z1());
        assertEquals(expectList.get(0).getDoppler_z1(), resultList.get(0).getDoppler_z1());
        assertEquals(expectList.get(0).getEtcd_z1(), resultList.get(0).getEtcd_z1());
        assertEquals(expectList.get(0).getPostgres_z1(), resultList.get(0).getPostgres_z1());
        assertEquals(expectList.get(0).getIaasType(), resultList.get(0).getIaasType());
        assertEquals(expectList.get(0).getUaa_z1(), resultList.get(0).getUaa_z1());
        assertEquals(expectList.get(0).getNats_z1(), resultList.get(0).getNats_z1());
        assertEquals(expectList.get(0).getLoggregator_z1(), resultList.get(0).getLoggregator_z1());
        assertEquals(expectList.get(0).getId(), resultList.get(0).getId());
        assertEquals(expectList.get(0).getInstanceConfigName(), resultList.get(0).getInstanceConfigName());
    }
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF  Instance 정보 저장 Unit Test
     * @title : testInsertHbCfInstanceConfigInfo
     * @return : void
    ***************************************************/
    @Test
    public void testInsertHbCfInstanceConfigInfo(){
        HbCfInstanceConfigDTO dto = setCfInstanceConfigInfo("insert");
        when(mockHbCfInstanceConfigDAO.selectInstanceConfigByName(anyString())).thenReturn(0);
        mockHbCfInstanceConfigService.saveInstanceConfig(dto, principal);
    }
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF  Instance 정보 저장 Unit Test
     * @title : testUpdateHbCfInstanceConfigInfo
     * @return : void
    ***************************************************/
    @Test
    public void testUpdateHbCfInstanceConfigInfo(){
        HbCfInstanceConfigDTO dto = setCfInstanceConfigInfo("update");
        HbCfInstanceConfigVO vo = setCfInstanceConfig();
        when(mockHbCfInstanceConfigDAO.selectInstanceConfigById(anyInt())).thenReturn(vo);
        when(mockHbCfInstanceConfigDAO.selectInstanceConfigByName(anyString())).thenReturn(0);
        mockHbCfInstanceConfigService.saveInstanceConfig(dto, principal);
    }
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF  Instance 정보 저장 Exception Unit Test
     * @title : testUpdateHbCfInstanceConfigInfo
     * @return : void
    ***************************************************/
    @Test(expected=CommonException.class)
    public void testSaveHbCfInstanceConfigInfoConflict(){
        HbCfInstanceConfigDTO dto = setCfInstanceConfigInfo("insert");
        when(mockHbCfInstanceConfigDAO.selectInstanceConfigByName(anyString())).thenReturn(1);
        when(mockMessageSource.getMessage(anyString(), anyObject(), anyObject())).thenReturn("conflict");
        mockHbCfInstanceConfigService.saveInstanceConfig(dto, principal);
    }
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF  Instance 정보 삭제 Unit Test
     * @title : testUpdateHbCfInstanceConfigInfo
     * @return : void
    ***************************************************/
    @Test
    public void testDeleteHbCfInstanceConfigInfo(){
        HbCfInstanceConfigDTO dto = new HbCfInstanceConfigDTO();
        dto.setId("1");
        dto.setIaasType("Openstack");
        mockHbCfInstanceConfigService.deleteInstanceConfig(dto, principal);
    }
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF  Instance 정보 삭제 Exception Unit Test
     * @title : testUpdateHbCfInstanceConfigInfo
     * @return : void
    ***************************************************/
    @Test(expected=CommonException.class)
    public void testDeleteHbCfInstanceConfigInfoNull(){
        HbCfInstanceConfigDTO dto = new HbCfInstanceConfigDTO();
        when(mockMessageSource.getMessage(anyString(), anyObject(), anyObject())).thenReturn("null");
        mockHbCfInstanceConfigService.deleteInstanceConfig(dto, principal);
    }
    
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  인스턴스 목록 정보 조회 결과 값 설정
     * @title : setCfInstanceConfigList
     * @return : List<HbCfCredentialConfigVO>
    *****************************************************************/
    private HbCfInstanceConfigVO setCfInstanceConfig() {
        HbCfInstanceConfigVO vo = new HbCfInstanceConfigVO();
        vo.setId(1);
        vo.setIaasType("Openstack");
        vo.setApi_worker_z1("1");
        vo.setApi_z1("1");
        vo.setBlobstore_z1("1");
        vo.setClock_z1("1");
        vo.setConsul_z1("1");
        vo.setDefaultConfigInfo("default-config");
        vo.setEtcd_z1("1");
        vo.setDoppler_z1("1");
        vo.setEtcd_z1("1");
        vo.setInstanceConfigName("instance");
        vo.setUaa_z1("1");
        vo.setRouter_z1("1");
        vo.setPostgres_z1("1");
        vo.setNetworkConfigInfo("network-config");
        
        vo.setApi_worker_z2("1");
        vo.setApi_z2("1");
        vo.setBlobstore_z2("1");
        vo.setClock_z2("1");
        vo.setConsul_z2("1");
        vo.setEtcd_z2("1");
        vo.setDoppler_z2("1");
        vo.setEtcd_z2("1");
        vo.setUaa_z2("1");
        vo.setRouter_z2("1");
        vo.setPostgres_z2("1");
        return vo;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  Job 정보 저장/삭제 값 설정
     * @title : setCfInstanceConfigInfo
     * @return : HbCfInstanceConfigDTO
    *****************************************************************/
    private HbCfInstanceConfigDTO setCfInstanceConfigInfo(String type) {
        HbCfInstanceConfigDTO dto = new HbCfInstanceConfigDTO();
        dto.setIaasType("Openstack");
        dto.setApi_worker_z1("1");
        dto.setApi_z1("1");
        dto.setBlobstore_z1("1");
        dto.setClock_z1("1");
        dto.setConsul_z1("1");
        dto.setDefaultConfigInfo("default-config");
        dto.setEtcd_z1("1");
        dto.setDroppler_z1("1");
        dto.setEtcd_z1("1");
        dto.setInstanceConfigName("instance");
        dto.setUaa_z1("1");
        dto.setRouter_z1("1");
        dto.setPostgres_z1("1");
        dto.setNetworkConfigInfo("network-config");
        
        dto.setApi_worker_z2("1");
        dto.setApi_z2("1");
        dto.setBlobstore_z2("1");
        dto.setClock_z2("1");
        dto.setConsul_z2("1");
        dto.setEtcd_z2("1");
        dto.setDroppler_z2("1");
        dto.setEtcd_z2("1");
        dto.setUaa_z2("1");
        dto.setRouter_z2("1");
        dto.setPostgres_z2("1");
        if("update".equalsIgnoreCase(type)){
            dto.setId("1");
        }
        
        return dto;
    }
    
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  인스턴스 목록 정보 조회 결과 값 설정
     * @title : setCfInstanceConfigList
     * @return : List<HbCfCredentialConfigVO>
    *****************************************************************/
    private List<HbCfInstanceConfigVO> setCfInstanceConfigList() {
        List<HbCfInstanceConfigVO> list = new ArrayList<HbCfInstanceConfigVO>();
        HbCfInstanceConfigVO vo = new HbCfInstanceConfigVO();
        vo.setId(1);
        vo.setIaasType("Openstack");
        vo.setApi_worker_z1("1");
        vo.setApi_z1("1");
        vo.setBlobstore_z1("1");
        vo.setClock_z1("1");
        vo.setConsul_z1("1");
        vo.setDefaultConfigInfo("default-config");
        vo.setEtcd_z1("1");
        vo.setDoppler_z1("1");
        vo.setEtcd_z1("1");
        vo.setInstanceConfigName("instance");
        vo.setUaa_z1("1");
        vo.setRouter_z1("1");
        vo.setPostgres_z1("1");
        vo.setNetworkConfigInfo("network-config");
        vo.setApi_worker_z2("1");
        vo.setApi_z2("1");
        vo.setBlobstore_z2("1");
        vo.setClock_z2("1");
        vo.setConsul_z2("1");
        vo.setEtcd_z2("1");
        vo.setDoppler_z2("1");
        vo.setEtcd_z2("1");
        vo.setUaa_z2("1");
        vo.setRouter_z2("1");
        vo.setPostgres_z2("1");
        list.add(vo);
        return list;
    }
    
}
