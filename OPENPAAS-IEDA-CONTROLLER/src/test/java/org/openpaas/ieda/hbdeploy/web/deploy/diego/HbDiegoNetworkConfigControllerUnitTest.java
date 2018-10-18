package org.openpaas.ieda.hbdeploy.web.deploy.diego;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import org.openpaas.ieda.common.BaseControllerUnitTest;
import org.openpaas.ieda.controller.hbdeploy.web.deploy.diego.HbDiegoNetworkConfigController;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoNetworkConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoNetworkConfigDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.service.HbDiegoNetworkConfigService;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HbDiegoNetworkConfigControllerUnitTest extends BaseControllerUnitTest{

	private MockMvc mockMvc;
	private Principal principal = null;

	@InjectMocks HbDiegoNetworkConfigController mockHbDiegoNetworkConfigController;
	@Mock HbDiegoNetworkConfigService mockHbDiegoNetworkConfigService;
	
	final static String VIEW_URL = "/deploy/hbDiego/HbDiegoNetworkConfig";
	final static String NETWORKINFO_LIST_URL = "/deploy/hbDiego/network/list";
	final static String NETWORKINFO_DETAIL_LIST_URL = "/deploy/hbDiego/network/list/detail/openstack_network";
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 하나의 메소드가 실행되기 전에 호출
     * @title : setUp
     * @return : void
    *****************************************************************/
    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mockHbDiegoNetworkConfigController).build();
        principal = getLoggined();
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 DIEGO 네트워크 정보 설정 관리 화면 이동 TEST
     * @title : goHbDiegoNetworkConfigListTest
     * @return : void
    *****************************************************************/
    @Test
    public void goHbDiegoNetworkConfigInfoTest() throws Exception{
        mockMvc.perform(get(VIEW_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(view().name("/hbdeploy/deploy/diego/hbDiegoNetworkConfig"));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 DIEGO 네트워크 정보 설정 목록 조회 TEST
     * @title : getHbDiegoNetworkConfigInfoListTest
     * @return : void
    *****************************************************************/
    @Test
    public void getHbDiegoNetworkConfigInfoListTest() throws Exception{
    	List<HbDiegoNetworkConfigVO> resultList = setDiegoNetworkConfigList();
        when(mockHbDiegoNetworkConfigService.getNetworkConfigInfoList()).thenReturn(resultList);
        mockMvc.perform(get(NETWORKINFO_LIST_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.records[0].id").value(1000))
        .andExpect(jsonPath("$.records[0].iaasType").value("OPENSTACK"))
        .andExpect(jsonPath("$.records[0].networkConfigName").value("openstack_network"))
        .andExpect(jsonPath("$.records[0].net").value("Internal"))
        .andExpect(jsonPath("$.records[0].seq").value(0))
        .andExpect(jsonPath("$.records[0].subnetId").value("openstack_subnet1"))
        .andExpect(jsonPath("$.records[0].availabilityZone").value(""))
        .andExpect(jsonPath("$.records[0].cloudSecurityGroups").value("diego_security"))
        .andExpect(jsonPath("$.records[0].subnetRange").value("192.168.10.0/24"))
        .andExpect(jsonPath("$.records[0].subnetGateway").value("192.168.10.1"))
        .andExpect(jsonPath("$.records[0].subnetDns").value("8.8.8.8"))
        .andExpect(jsonPath("$.records[0].subnetReservedFrom").value("192.168.10.1"))
        .andExpect(jsonPath("$.records[0].subnetReservedTo").value("192.168.10.10"))
        .andExpect(jsonPath("$.records[0].subnetStaticFrom").value("192.168.10.11"))
        .andExpect(jsonPath("$.records[0].subnetStaticTo").value("192.168.10.30"))
        .andExpect(jsonPath("$.records[0].createUserId").value("admin"));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 DIEGO 네트워크 정보 설정 상세조회 TEST
     * @title : getHbDiegoNetworkConfigInfoTest
     * @return : void
    *****************************************************************/
    @Test
    public void getHbDiegoNetworkConfigInfoTest() throws Exception{
    	List<HbDiegoNetworkConfigVO> resultList = setDiegoNetworkConfigList();
        when(mockHbDiegoNetworkConfigService.getNetworkConfigDetailInfoList(anyString())).thenReturn(resultList);
        mockMvc.perform(get(NETWORKINFO_DETAIL_LIST_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.records[0].id").value(1000))
        .andExpect(jsonPath("$.records[0].iaasType").value("OPENSTACK"))
        .andExpect(jsonPath("$.records[0].networkConfigName").value("openstack_network"))
        .andExpect(jsonPath("$.records[0].net").value("Internal"))
        .andExpect(jsonPath("$.records[0].seq").value(0))
        .andExpect(jsonPath("$.records[0].subnetId").value("openstack_subnet1"))
        .andExpect(jsonPath("$.records[0].availabilityZone").value(""))
        .andExpect(jsonPath("$.records[0].cloudSecurityGroups").value("diego_security"))
        .andExpect(jsonPath("$.records[0].subnetRange").value("192.168.10.0/24"))
        .andExpect(jsonPath("$.records[0].subnetGateway").value("192.168.10.1"))
        .andExpect(jsonPath("$.records[0].subnetDns").value("8.8.8.8"))
        .andExpect(jsonPath("$.records[0].subnetReservedFrom").value("192.168.10.1"))
        .andExpect(jsonPath("$.records[0].subnetReservedTo").value("192.168.10.10"))
        .andExpect(jsonPath("$.records[0].subnetStaticFrom").value("192.168.10.11"))
        .andExpect(jsonPath("$.records[0].subnetStaticTo").value("192.168.10.30"))
        .andExpect(jsonPath("$.records[0].createUserId").value("admin"));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description :  DIEGO 네트워크 정보 설정 저장 테스트
     * @title : saveHbDiegoNetworkConfigInfoTest
     * @return : void
    *****************************************************************/
    @Test
    public void saveHbDiegoNetworkConfigInfoTest(){
        List<HbDiegoNetworkConfigDTO> resultList = setDiegoNetworkConfigListInfo();
        mockHbDiegoNetworkConfigController.saveDiegoNetworkConfigInfo(resultList, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 네트워크 정보 설정 삭제 테스트
     * @title : deleteHbDiegoNetworkConfigInfoTest
     * @return : void
    *****************************************************************/
    @Test
    public void deleteHbDiegoNetworkConfigInfoTest(){
        HbDiegoNetworkConfigDTO resultDto = setDiegoNetworkConfigInfo();
        mockHbDiegoNetworkConfigController.deleteDiegoNetworkConfigInfo(resultDto, principal);
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
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 네트워크 정보 값 설정
     * @title : setDiegoNetworkConfigInfo
     * @return : HbDiegoNetworkConfigDTO
    *****************************************************************/
    public HbDiegoNetworkConfigDTO setDiegoNetworkConfigInfo(){
        HbDiegoNetworkConfigDTO dto = new HbDiegoNetworkConfigDTO();
        dto.setId("1001");
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
        return dto;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 네트워크 정보 목록 값 설정
     * @title : setDiegoNetworkConfigListInfo
     * @return : HbDiegoNetworkConfigDTO
    *****************************************************************/
    public List<HbDiegoNetworkConfigDTO> setDiegoNetworkConfigListInfo(){
        List<HbDiegoNetworkConfigDTO> list = new ArrayList<HbDiegoNetworkConfigDTO>();
        HbDiegoNetworkConfigDTO dto = new HbDiegoNetworkConfigDTO();
        dto.setId("1001");
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
}
