package org.openpaas.ieda.hbdeploy.web.deploy.cf.service;

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
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfNetworkConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfNetworkConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfNetworkConfigDTO;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HbCfNetworkConfigServiceUnitTest extends BaseHbDeployControllerUnitTest{
    
    @InjectMocks HbCfNetworkConfigService mockHbCfNetworkConfigService;
    @Mock MessageSource mockMessageSource;
    @Mock HbCfNetworkConfigDAO mockHbCfNetworkConfigDAO;
    
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
     * @description : 이종 CF  네트워크  정보 목록 조회 Unit Test
     * @title : testGetNetworkConfigInfoList
     * @return : void
    *****************************************************************/
    @Test
    public void testGetNetworkConfigInfoList(){
        List<HbCfNetworkConfigVO> expectList = setCfNetworkConfigList();
        when(mockHbCfNetworkConfigDAO.selectCfNetworkConfigList()).thenReturn(expectList);
        mockHbCfNetworkConfigService.getNetworkConfigInfoList();
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  네트워크 정보 저장 Unit Test
     * @title : testsaveNetworkConfigInfo
     * @return : void
    *****************************************************************/
    public void testsaveNetworkConfigInfo(){
    	List<HbCfNetworkConfigDTO> dto = setCfNetworkConfigInfoList("insert");
        when(mockHbCfNetworkConfigDAO.selectCfDefaultConfigInfoByName(anyString())).thenReturn(0);
        mockHbCfNetworkConfigService.saveNetworkConfigInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  네트워크 정보 저장 Excepiton Unit Test
     * @title : testUpdateNetworkConfigInfo
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testSaveNetworkConfigInfoConflict(){
        List<HbCfNetworkConfigDTO> dto = setCfNetworkConfigInfoList("insert");
        when(mockHbCfNetworkConfigDAO.selectCfDefaultConfigInfoByName(anyString())).thenReturn(1);
        when(mockMessageSource.getMessage(anyString(), anyObject(), anyObject())).thenReturn("conflic_exception");
        mockHbCfNetworkConfigService.saveNetworkConfigInfo(dto, principal);
        
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  네트워크 정보 삭제 Unit Test
     * @title : testDeleteNetworkConfigInfo
     * @return : void
    *****************************************************************/
    @Test 
    public void testDeleteNetworkConfigInfo(){
    	HbCfNetworkConfigDTO dto = setCfNetworkConfigInfo("update");
    	mockHbCfNetworkConfigService.deleteNetworkConfigInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  네트워크 정보 삭제 Excepiton Unit Test
     * @title : testDeleteNetworkConfigInfoNull
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testDeleteNetworkConfigInfoNull(){
    	HbCfNetworkConfigDTO dto = setCfNetworkConfigInfo("insert");
    	when(mockMessageSource.getMessage(anyString(), anyObject(), anyObject())).thenReturn("null");
    	mockHbCfNetworkConfigService.deleteNetworkConfigInfo(dto, principal);
    }
    
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  네트워크 목록 정보 조회 값 설정
     * @title : setCfNetworkConfigList
     * @return : List<HbCfNetworkConfigVO>
    *****************************************************************/
    private HbCfNetworkConfigVO setCfNetworkConfig() {
        HbCfNetworkConfigVO vo = new HbCfNetworkConfigVO();
        vo.setAvailabilityZone("us-west-1a");
        vo.setId(1);
        vo.setIaasType("Openstack");
        vo.setSeq(1);
        vo.setSubnetGateway("172.16.100.1");
        vo.setSubnetDns("8.8.8.8");
        vo.setSubnetRange("10.0.0.0/24");
        return vo;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  저장/삭제 요청 값 설정
     * @title : setCfNetworkConfigInfo
     * @return : HbCfNetworkConfigDTO
    *****************************************************************/
    private HbCfNetworkConfigDTO setCfNetworkConfigInfo(String type) {
        HbCfNetworkConfigDTO dto = new HbCfNetworkConfigDTO();
        if("update".equalsIgnoreCase(type)){
            dto.setId("1");
            dto.setNetworkConfigName("network-config");
        }
        dto.setIaasType("Openstack");
        dto.setNet("EXTERNAL");
        dto.setPublicStaticIP("172.16.100.111");
        dto.setIaasType("Openstack");
        dto.setSubnetGateway("172.16.100.1");
        dto.setSubnetDns("8.8.8.8");
        dto.setSubnetRange("10.0.0.0/24");
        return dto;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  저장/삭제 요청 값 설정
     * @title : setCfNetworkConfigInfo
     * @return : HbCfNetworkConfigDTO
    *****************************************************************/
    private List<HbCfNetworkConfigDTO> setCfNetworkConfigInfoList(String type) {
    	List<HbCfNetworkConfigDTO> list = new ArrayList<HbCfNetworkConfigDTO>();
        HbCfNetworkConfigDTO dto = new HbCfNetworkConfigDTO();
        if("update".equalsIgnoreCase(type)){
            dto.setId("1");
        }
        dto.setIaasType("Openstack");
        dto.setNet("EXTERNAL");
        dto.setPublicStaticIP("172.16.100.111");
        dto.setIaasType("Openstack");
        dto.setNetworkConfigName("network-config");
        dto.setSubnetGateway("172.16.100.1");
        dto.setSubnetDns("8.8.8.8");
        dto.setSubnetRange("10.0.0.0/24");
        list.add(dto);
        return list;
    }

    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  네트워크 목록 정보 조회 값 설정
     * @title : setCfNetworkConfigList
     * @return : List<HbCfNetworkConfigVO>
    *****************************************************************/
    private List<HbCfNetworkConfigVO> setCfNetworkConfigList() {
        List<HbCfNetworkConfigVO> list = new ArrayList<HbCfNetworkConfigVO>();
        HbCfNetworkConfigVO vo = new HbCfNetworkConfigVO();
        vo.setAvailabilityZone("us-west-1a");
        vo.setId(1);
        vo.setNet("EXTERNAL");
        vo.setSeq(0);
        vo.setNetworkConfigName("network-config");
        vo.setPublicStaticIP("172.16.100.111");
        vo.setIaasType("Openstack");
        vo.setSeq(1);
        vo.setSubnetGateway("172.16.100.1");
        vo.setSubnetDns("8.8.8.8");
        vo.setSubnetRange("10.0.0.0/24");
        list.add(vo);
        return list;
    }
    
}
