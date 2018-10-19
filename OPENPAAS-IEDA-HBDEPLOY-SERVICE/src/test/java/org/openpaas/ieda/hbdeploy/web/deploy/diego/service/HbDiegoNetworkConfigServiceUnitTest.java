package org.openpaas.ieda.hbdeploy.web.deploy.diego.service;

import static org.junit.Assert.assertEquals;
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
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoNetworkConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoNetworkConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoNetworkConfigDTO;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HbDiegoNetworkConfigServiceUnitTest extends BaseHbDeployControllerUnitTest{
    
    @InjectMocks private HbDiegoNetworkConfigService mockHbDiegoNetworkConfigService;
    @Mock private MessageSource mockMessageSource;
    @Mock private HbDiegoNetworkConfigDAO mockHbDiegoNetworkConfigDAO;
    
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
     * @description : 이종 DIEGO 네트워크 정보 목록 조회 Unit Test
     * @title : testGetNetworkConfigInfoList
     * @return : void
    *****************************************************************/
    @Test
    public void testGetNetworkConfigInfoList(){
        List<HbDiegoNetworkConfigVO> expectList = setDiegoNetworkConfigList();
        when(mockHbDiegoNetworkConfigDAO.selectNetworkConfigList()).thenReturn(expectList);
        when(mockHbDiegoNetworkConfigDAO.selectDiegoNetworkConfigInfoByNameResultVo(anyString())).thenReturn(expectList);
        List<HbDiegoNetworkConfigVO> resultList = mockHbDiegoNetworkConfigService.getNetworkConfigInfoList();
        assertEquals(expectList.size(), resultList.size());
        assertEquals(expectList.get(0).getIaasType(), resultList.get(0).getIaasType());
        assertEquals(expectList.get(0).getId(), resultList.get(0).getId());
        assertEquals(expectList.get(0).getCloudSecurityGroups(), resultList.get(0).getCloudSecurityGroups());
        assertEquals(expectList.get(0).getNetworkConfigName(), resultList.get(0).getNetworkConfigName());
        assertEquals(expectList.get(0).getSubnetId(), resultList.get(0).getSubnetId());
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 DIEGO 네트워크 정보 상세 목록 조회 Unit Test
     * @title : testGetNetworkConfigDetailInfoList
     * @return : void
    *****************************************************************/
    @Test
    public void testGetNetworkConfigDetailInfoList(){
        List<HbDiegoNetworkConfigVO> expectList = setDiegoNetworkConfigList();
        when(mockHbDiegoNetworkConfigDAO.selectDiegoNetworkConfigInfoByNameResultVo(anyString())).thenReturn(expectList);
        List<HbDiegoNetworkConfigVO> resultList = mockHbDiegoNetworkConfigService.getNetworkConfigDetailInfoList("network-cofnig");
        assertEquals(expectList.size(), resultList.size());
        assertEquals(expectList.get(0).getIaasType(), resultList.get(0).getIaasType());
        assertEquals(expectList.get(0).getId(), resultList.get(0).getId());
        assertEquals(expectList.get(0).getCloudSecurityGroups(), resultList.get(0).getCloudSecurityGroups());
        assertEquals(expectList.get(0).getNet(), resultList.get(0).getNet());
        assertEquals(expectList.get(0).getNetworkConfigName(), resultList.get(0).getNetworkConfigName());
        assertEquals(expectList.get(0).getSubnetId(), resultList.get(0).getSubnetId());
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 DIEGO 네트워크 정보 저장 TEST
     * @title : testSaveNetworkConfigInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testSaveNetworkConfigInfo(){
        List<HbDiegoNetworkConfigDTO> dto = setDiegoNetworkConfigListInfo("insert");
        when(mockHbDiegoNetworkConfigDAO.selectDiegoNetworkConfigInfoByName(anyString())).thenReturn(0);
        mockHbDiegoNetworkConfigService.saveNetworkConfigInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 DIEGO 네트워크 정보 저장 값 중복 TEST
     * @title : testSaveNetworkConfigInfoConflict
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testSaveNetworkConfigInfoConflict(){
        List<HbDiegoNetworkConfigDTO> dto = setDiegoNetworkConfigListInfo("error");
        when(mockHbDiegoNetworkConfigDAO.selectDiegoNetworkConfigInfoByName(anyString())).thenReturn(1);
        mockHbDiegoNetworkConfigService.saveNetworkConfigInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 DIEGO 네트워크 정보 삭제 TEST
     * @title : testSaveNetworkConfigInfoConflict
     * @return : void
    *****************************************************************/
    @Test
    public void testDeleteNetworkConfigInfo(){
    	HbDiegoNetworkConfigDTO dto = new HbDiegoNetworkConfigDTO();
    	dto.setId("1");
    	mockHbDiegoNetworkConfigService.deleteNetworkConfigInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 DIEGO 네트워크 정보 삭제 NULL TEST
     * @title : testSaveNetworkConfigInfoConflict
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testDeleteNetworkConfigInfoNull(){
    	HbDiegoNetworkConfigDTO dto = new HbDiegoNetworkConfigDTO();
    	mockHbDiegoNetworkConfigService.deleteNetworkConfigInfo(dto, principal);
    }
    
    /****************************************************************
     * @param string 
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 네트워크 정보 목록 값 설정
     * @title : setDiegoNetworkConfigListInfo
     * @return : HbDiegoNetworkConfigDTO
    *****************************************************************/
    public List<HbDiegoNetworkConfigDTO> setDiegoNetworkConfigListInfo(String type){
        List<HbDiegoNetworkConfigDTO> list = new ArrayList<HbDiegoNetworkConfigDTO>();
        HbDiegoNetworkConfigDTO dto = new HbDiegoNetworkConfigDTO();
        if(!"error".equals(type)){
            dto.setId("1001");
        }
        dto.setIaasType("OPENSTACK");
        dto.setNetworkConfigName("openstack_network1");
        dto.setNet("Internal");
        dto.setSeq("1");
        dto.setSubnetId("openstack_subnet2");
        dto.setAvailabilityZone("");
        dto.setCloudSecurityGroups("diego_security");
        dto.setSubnetRange("192.168.20.0/24");
        dto.setSubnetGateway("192.168.20.1");
        dto.setSubnetDns("8.8.8.8");
        dto.setSubnetReservedFrom("192.168.20.1");
        dto.setSubnetReservedTo("192.168.20.10");
        dto.setSubnetStaticFrom("192.168.20.11");
        dto.setSubnetStaticTo("192.168.20.35");
        list.add(dto);
        return list;
    }
    
    
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 네트워크 정보 목록 값 설정
     * @title : setDiegoNetworkConfigList
     * @return : List<HbDiegoNetworkConfigVO>
    *****************************************************************/
    public List<HbDiegoNetworkConfigVO> setDiegoNetworkConfigList(){
        List<HbDiegoNetworkConfigVO> list = new ArrayList<HbDiegoNetworkConfigVO>();
        HbDiegoNetworkConfigVO vo = new HbDiegoNetworkConfigVO();
        vo.setId(1000);
        vo.setIaasType("OPENSTACK");
        vo.setNetworkConfigName("openstack_network");
        vo.setNet("Internal");
        vo.setSeq(0);
        vo.setSubnetId("openstack_subnet1");
        vo.setAvailabilityZone("");
        vo.setCloudSecurityGroups("diego_security");
        vo.setSubnetRange("192.168.10.0/24");
        vo.setSubnetGateway("192.168.10.1");
        vo.setSubnetDns("8.8.8.8");
        vo.setSubnetReservedFrom("192.168.10.1");
        vo.setSubnetReservedTo("192.168.10.10");
        vo.setSubnetStaticFrom("192.168.10.11");
        vo.setSubnetStaticTo("192.168.10.30");
        vo.setCreateUserId("admin");
        list.add(vo);
        return list;
    }
}
