package org.openpaas.ieda.hbdeploy.web.deploy.diego;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.Application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.ieda.common.BaseControllerUnitTest;
import org.openpaas.ieda.controller.hbdeploy.web.deploy.diego.HbDiegoInstanceConfigController;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoDefaultConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoInstanceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoNetworkConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoInstanceConfigDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.service.HbDiegoDefaultConfigService;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.service.HbDiegoInstanceConfigService;
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
public class HbDiegoInstanceConfigControllerUnitTest extends BaseControllerUnitTest{

	private MockMvc mockMvc;
	private Principal principal = null;
	
	@InjectMocks HbDiegoInstanceConfigController mockHbDiegoInstanceConfigController;
	@Mock HbDiegoInstanceConfigService mockHbDiegoInstanceConfigService;
	@Mock HbDiegoDefaultConfigService mockHbDiegoDefaultConfigService;
	@Mock HbDiegoNetworkConfigService mockHbDiegoNetworkConfigService;
	
	final static String VIEW_URL = "/deploy/hbDiego/HbDiegoInstanceConfig";
	final static String INSTANCEINFO_LIST_URL = "/deploy/hbDiego/instance/list";
	final static String INSTANCEINFO_DETAIL_LIST_URL = "/deploy/hbDiego/instance/list/detail/1000";
	final static String DEFAULTINFO_LIST_URL = "/deploy/hbDiego/instance/list/defaultConfig";
	final static String NETWORKINFO_LIST_URL = "/deploy/hbDiego/instance/list/networkConfig";
	final static String JOBINFO_LIST_URL = "/deploy/hbDiego/install/instance/list/job/0.149.0/DEPLOY_TYPE_DIEGO";
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 하나의 메소드가 실행되기 전에 호출
     * @title : setUp
     * @return : void
    *****************************************************************/
    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mockHbDiegoInstanceConfigController).build();
        principal = getLoggined();
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 DIEGO 인스턴스 정보 설정 관리 화면 이동 TEST
     * @title : goHbDiegoInstanceConfigListTest
     * @return : void설
    *****************************************************************/
    @Test
    public void goHbDiegoInstanceConfigInfoTest() throws Exception{
        mockMvc.perform(get(VIEW_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(view().name("/hbdeploy/deploy/diego/hbDiegoInstanceConfig"));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 DIEGO 인스턴스 정보 설정 목록 조회 TEST
     * @title : getHbDiegoInstanceConfigInfoListTest
     * @return : void
    *****************************************************************/
    @Test
    public void getHbDiegoInstanceConfigInfoListTest() throws Exception{
        List<HbDiegoInstanceConfigVO> resultList = setDiegoInstanceConfigList();
        when(mockHbDiegoInstanceConfigService.getInstanceConfigList()).thenReturn(resultList);
        mockMvc.perform(get(INSTANCEINFO_LIST_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.records[0].id").value(1000))
        .andExpect(jsonPath("$.records[0].iaasType").value("OPENSTACK"))
        .andExpect(jsonPath("$.records[0].instanceConfigName").value("openstack_instance"))
        .andExpect(jsonPath("$.records[0].defaultConfigInfo").value("openstack_default"))
        .andExpect(jsonPath("$.records[0].networkConfigInfo").value("openstack_network"))
        .andExpect(jsonPath("$.records[0].database_z1").value("1"))
        .andExpect(jsonPath("$.records[0].access_z1").value("1"))
        .andExpect(jsonPath("$.records[0].cc_bridge_z1").value("1"))
        .andExpect(jsonPath("$.records[0].cell_z1").value("1"))
        .andExpect(jsonPath("$.records[0].brain_z1").value("1"))
        .andExpect(jsonPath("$.records[0].database_z2").value("1"))
        .andExpect(jsonPath("$.records[0].access_z2").value("1"))
        .andExpect(jsonPath("$.records[0].cc_bridge_z2").value("1"))
        .andExpect(jsonPath("$.records[0].cell_z2").value("1"))
        .andExpect(jsonPath("$.records[0].brain_z2").value("1"))
        .andExpect(jsonPath("$.records[0].database_z3").value("1"))
        .andExpect(jsonPath("$.records[0].access_z3").value("1"))
        .andExpect(jsonPath("$.records[0].cc_bridge_z3").value("1"))
        .andExpect(jsonPath("$.records[0].cell_z3").value("1"))
        .andExpect(jsonPath("$.records[0].brain_z3").value("1"))
        .andExpect(jsonPath("$.records[0].createUserId").value("admin"));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 DIEGO 인스턴스 정보 상세조회 TEST
     * @title : getHbDiegoInstanceConfigDetailInfoTest
     * @return : void
    *****************************************************************/
    @Test
    public void getHbDiegoInstanceConfigDetailInfoTest() throws Exception{
        HbDiegoInstanceConfigVO resultVo = setDiegoInstanceConfigInfoVO();
        when(mockHbDiegoInstanceConfigService.getInstanceConfigInfo(anyInt())).thenReturn(resultVo);
        mockMvc.perform(get(INSTANCEINFO_DETAIL_LIST_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.id").value(1000))
        .andExpect(jsonPath("$.iaasType").value("OPENSTACK"))
        .andExpect(jsonPath("$.instanceConfigName").value("openstack_instance"))
        .andExpect(jsonPath("$.defaultConfigInfo").value("openstack_default"))
        .andExpect(jsonPath("$.networkConfigInfo").value("openstack_network"))
        .andExpect(jsonPath("$.database_z1").value("1"))
        .andExpect(jsonPath("$.access_z1").value("1"))
        .andExpect(jsonPath("$.cc_bridge_z1").value("1"))
        .andExpect(jsonPath("$.cell_z1").value("1"))
        .andExpect(jsonPath("$.brain_z1").value("1"))
        .andExpect(jsonPath("$.database_z2").value("1"))
        .andExpect(jsonPath("$.access_z2").value("1"))
        .andExpect(jsonPath("$.cc_bridge_z2").value("1"))
        .andExpect(jsonPath("$.cell_z2").value("1"))
        .andExpect(jsonPath("$.brain_z2").value("1"))
        .andExpect(jsonPath("$.database_z3").value("1"))
        .andExpect(jsonPath("$.access_z3").value("1"))
        .andExpect(jsonPath("$.cc_bridge_z3").value("1"))
        .andExpect(jsonPath("$.cell_z3").value("1"))
        .andExpect(jsonPath("$.brain_z3").value("1"))
        .andExpect(jsonPath("$.createUserId").value("admin"));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description :  DIEGO 인스턴스 정보 설정 저장 테스트
     * @title : saveHbDiegoInstanceConfigInfoTest
     * @return : void
    *****************************************************************/
    @Test
    public void saveHbDiegoInstanceConfigInfoTest(){
        HbDiegoInstanceConfigDTO resultDto = setDiegoInstanceConfigInfoDTO();
        mockHbDiegoInstanceConfigController.saveResourceConfigInfo(resultDto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 인스턴스 정보 설정 삭제 테스트
     * @title : deleteHbDiegoInstanceConfigInfoTest
     * @return : void
    *****************************************************************/
    @Test
    public void deleteHbDiegoInstanceConfigInfoTest(){
        HbDiegoInstanceConfigDTO resultDto = setDiegoInstanceConfigInfoDTO();
        mockHbDiegoInstanceConfigController.deleteResourceConfigInfo(resultDto, principal);
    }
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 이종 DIEGO 기본 정보 설정 목록 조회 TEST
     * @title : hbDiegoDefaultConfigInfoListByInstanceConfigInfoTest
     * @return : void
    *****************************************************************/
    @Test
    public void hbDiegoDefaultConfigInfoListByInstanceConfigInfoTest() throws Exception{
        List<HbDiegoDefaultConfigVO> resultList = setDiegoDefaultConfigList();
        when(mockHbDiegoDefaultConfigService.getDefaultConfigInfoList()).thenReturn(resultList);
        mockMvc.perform(get(DEFAULTINFO_LIST_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.records[0].id").value(1000))
        .andExpect(jsonPath("$.records[0].iaasType").value("OPENSTACK"))
        .andExpect(jsonPath("$.records[0].defaultConfigName").value("openstack_default"))
        .andExpect(jsonPath("$.records[0].deploymentName").value("openstack_diego"))
        .andExpect(jsonPath("$.records[0].directorId").value("1"))
        .andExpect(jsonPath("$.records[0].cfId").value(1))
        .andExpect(jsonPath("$.records[0].cfConfigName").value("cf_config"))
        .andExpect(jsonPath("$.records[0].cfReleaseName").value("cf-release"))
        .andExpect(jsonPath("$.records[0].cfReleaseVersion").value("272"))
        .andExpect(jsonPath("$.records[0].diegoReleaseName").value("diego-release"))
        .andExpect(jsonPath("$.records[0].diegoReleaseVersion").value("1.25.3"))
        .andExpect(jsonPath("$.records[0].cflinuxfs2rootfsreleaseName").value("cflinuxfs2-rootfs"))
        .andExpect(jsonPath("$.records[0].cflinuxfs2rootfsreleaseVersion").value("3.0"))
        .andExpect(jsonPath("$.records[0].gardenReleaseName").value("garden-runc"))
        .andExpect(jsonPath("$.records[0].gardenReleaseVersion").value("1.9.3"))
        .andExpect(jsonPath("$.records[0].paastaMonitoringUse").value("false"))
        .andExpect(jsonPath("$.records[0].createUserId").value("admin"));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 DIEGO 네트워크 정보 설정 목록 조회 TEST
     * @title : getHbDiegoNetworkConfigInfoListByInstanceConfigInfoTest
     * @return : void
    *****************************************************************/
    @Test
    public void getHbDiegoNetworkConfigInfoListByInstanceConfigInfoTest() throws Exception{
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
     * @description : DIEGO 인스턴스 정보 설정 DIEGO 릴리즈 버전 및 인프라 별 JOB 목록 조회
     * @title : getDiegoJobListTest
     * @return : void
    *****************************************************************/
    @Test
    public void getDiegoJobListTest() throws Exception{
        List<HashMap<String, String>> resultList = setJobInfoList();
        when(mockHbDiegoInstanceConfigService.getJobTemplateList(anyString(), anyString())).thenReturn(resultList);
        mockMvc.perform(get(JOBINFO_LIST_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.[0].releaseVersion").value("0.149.0"))
        .andExpect(jsonPath("$.[0].deployType").value("DEPLOY_TYPE_DIEGO"));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 인스턴스 정보 목록 값 설정
     * @title : setDiegoInstanceConfigList
     * @return : List<HbDiegoInstanceConfigVO>
    *****************************************************************/
    public List<HbDiegoInstanceConfigVO> setDiegoInstanceConfigList(){
        List<HbDiegoInstanceConfigVO> list = new ArrayList<HbDiegoInstanceConfigVO>();
        HbDiegoInstanceConfigVO vo = new HbDiegoInstanceConfigVO();
        vo.setId(1000);
        vo.setIaasType("OPENSTACK");
        vo.setInstanceConfigName("openstack_instance");
        vo.setDefaultConfigInfo("openstack_default");
        vo.setNetworkConfigInfo("openstack_network");
        vo.setDatabase_z1("1");
        vo.setAccess_z1("1");
        vo.setCc_bridge_z1("1");
        vo.setCell_z1("1");
        vo.setBrain_z1("1");
        vo.setDatabase_z2("1");
        vo.setAccess_z2("1");
        vo.setCc_bridge_z2("1");
        vo.setCell_z2("1");
        vo.setBrain_z2("1");
        vo.setDatabase_z3("1");
        vo.setAccess_z3("1");
        vo.setCc_bridge_z3("1");
        vo.setCell_z3("1");
        vo.setBrain_z3("1");
        vo.setCreateUserId("admin");
        list.add(vo);
        return list;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 인스턴스 정보 값 설정(vo)
     * @title : setDiegoInstanceConfigInfo
     * @return : HbDiegoInstanceConfigVO
    *****************************************************************/
    public HbDiegoInstanceConfigVO setDiegoInstanceConfigInfoVO(){
        HbDiegoInstanceConfigVO vo = new HbDiegoInstanceConfigVO();
        vo.setId(1000);
        vo.setIaasType("OPENSTACK");
        vo.setInstanceConfigName("openstack_instance");
        vo.setDefaultConfigInfo("openstack_default");
        vo.setNetworkConfigInfo("openstack_network");
        vo.setDatabase_z1("1");
        vo.setAccess_z1("1");
        vo.setCc_bridge_z1("1");
        vo.setCell_z1("1");
        vo.setBrain_z1("1");
        vo.setDatabase_z2("1");
        vo.setAccess_z2("1");
        vo.setCc_bridge_z2("1");
        vo.setCell_z2("1");
        vo.setBrain_z2("1");
        vo.setDatabase_z3("1");
        vo.setAccess_z3("1");
        vo.setCc_bridge_z3("1");
        vo.setCell_z3("1");
        vo.setBrain_z3("1");
        vo.setCreateUserId("admin");
        return vo;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 인스턴스 정보 값 설정(dto)
     * @title : setDiegoInstanceConfigInfo
     * @return : HbDiegoInstanceConfigVO
    *****************************************************************/
    public HbDiegoInstanceConfigDTO setDiegoInstanceConfigInfoDTO(){
        HbDiegoInstanceConfigDTO dto = new HbDiegoInstanceConfigDTO();
        dto.setId("1002");
        dto.setIaasType("OPENSTACK");
        dto.setInstanceConfigName("openstack_instance_re");
        dto.setDefaultConfigInfo("openstack_default_re");
        dto.setNetworkConfigInfo("openstack_network_re");
        dto.setDatabase_z1("1");
        dto.setAccess_z1("1");
        dto.setCc_bridge_z1("1");
        dto.setCell_z1("1");
        dto.setBrain_z1("1");
        dto.setDatabase_z2("2");
        dto.setAccess_z2("2");
        dto.setCc_bridge_z2("2");
        dto.setCell_z2("2");
        dto.setBrain_z2("2");
        dto.setDatabase_z3("3");
        dto.setAccess_z3("3");
        dto.setCc_bridge_z3("3");
        dto.setCell_z3("3");
        dto.setBrain_z3("3");
        return dto;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 기본 정보 설정 목록 조회 값 설정
     * @title : setDiegoDefaultConfigList
     * @return : List<HbDiegoDefaultConfigVO>
    *****************************************************************/
    public List<HbDiegoDefaultConfigVO> setDiegoDefaultConfigList(){
        List<HbDiegoDefaultConfigVO> list = new ArrayList<HbDiegoDefaultConfigVO>();
        HbDiegoDefaultConfigVO vo = new HbDiegoDefaultConfigVO();
        vo.setId(1000);
        vo.setIaasType("OPENSTACK");
        vo.setDefaultConfigName("openstack_default");
        vo.setDeploymentName("openstack_diego");
        vo.setDirectorId("1");
        vo.setCfId(1);
        vo.setCfConfigName("cf_config");
        vo.setCfReleaseName("cf-release");
        vo.setCfReleaseVersion("272");
        vo.setDiegoReleaseName("diego-release");
        vo.setDiegoReleaseVersion("1.25.3");
        vo.setCflinuxfs2rootfsreleaseName("cflinuxfs2-rootfs");
        vo.setCflinuxfs2rootfsreleaseVersion("3.0");
        vo.setGardenReleaseName("garden-runc");
        vo.setGardenReleaseVersion("1.9.3");
        vo.setPaastaMonitoringUse("false");
        vo.setCreateUserId("admin");
        list.add(vo);
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
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 인스턴스 정보 JOB Template 값 설정 
     * @title : setJobInfoList
     * @return : List<HashMap<String,String>>
    *****************************************************************/
    public List<HashMap<String, String>> setJobInfoList(){
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("releaseVersion", "0.149.0");
        map.put("deployType", "DEPLOY_TYPE_DIEGO");
        list.add(map);
        return list;
    }
    
}
