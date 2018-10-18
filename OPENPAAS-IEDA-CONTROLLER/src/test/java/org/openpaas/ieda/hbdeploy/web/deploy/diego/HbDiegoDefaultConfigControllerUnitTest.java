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
import org.openpaas.ieda.controller.hbdeploy.web.deploy.diego.HbDiegoDefaultConfigController;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.service.HbCfService;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoDefaultConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoDefaultConfigDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.service.HbDiegoDefaultConfigService;
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
public class HbDiegoDefaultConfigControllerUnitTest extends BaseControllerUnitTest{

    private MockMvc mockMvc;
    private Principal principal = null;
    
    @InjectMocks HbDiegoDefaultConfigController mockHbDiegoDefaultConfigController;
    @Mock HbDiegoDefaultConfigService mockHbDiegoDefaultconfigService;
    @Mock HbCfService mockHbcfService;
    
    final static String VIEW_URL = "/deploy/hbDiego/HbDiegoDefaultConfig";
    final static String DEFAULTINFO_LIST_URL = "/deploy/hbDiego/default/list";
    final static String CF_LIST_URL = "/deploy/hbDiego/list/cf";
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 하나의 메소드가 실행되기전 호출
     * @title : setUp
     * @return : void
    *****************************************************************/
    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mockHbDiegoDefaultConfigController).build();
        principal = getLoggined();
    }
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 DIEGO 기본 정보 설정 관리 화면 이동 TEST
     * @title : goHbDiegoDefaultConfigTest
     * @return : void
    *****************************************************************/
    @Test
    public void goHbDiegoDefaultConfigTest() throws Exception{
        mockMvc.perform(get(VIEW_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(view().name("/hbdeploy/deploy/diego/hbDiegoDefaultConfig"));
    }
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description : 이종 DIEGO 기본 정보 설정 목록 조회 TEST
     * @title : hbDiegoDefaultConfigInfoList
     * @return : void
    *****************************************************************/
    @Test
    public void hbDiegoDefaultConfigInfoListTest() throws Exception{
        List<HbDiegoDefaultConfigVO> resultList = setDiegoDefaultConfigList();
        when(mockHbDiegoDefaultconfigService.getDefaultConfigInfoList()).thenReturn(resultList);
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
     * @description : 이종 DIEGO 기본 정보 설정 CF 정보 목록 조회 TEST
     * @title : getCfConfigInfoListTest
     * @return : void
    *****************************************************************/
    @Test
    public void getHbDiegoCfConfigInfoListTest() throws Exception{
        List<HbCfVO> resultList = setCfInfoList();
        when(mockHbcfService.getCfInfoList(anyString())).thenReturn(resultList);
        mockMvc.perform(get(CF_LIST_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.records[0].id").value(1))
        .andExpect(jsonPath("$.records[0].cfConfigName").value("cf_config"))
        .andExpect(jsonPath("$.records[0].iaasType").value("OPENSTACK"))
        .andExpect(jsonPath("$.records[0].networkConfigInfo").value("cf_network_config"))
        .andExpect(jsonPath("$.records[0].keyConfigInfo").value("cf_key_config"))
        .andExpect(jsonPath("$.records[0].defaultConfigInfo").value("cf_default_config"))
        .andExpect(jsonPath("$.records[0].resourceConfigInfo").value("cf_resource_config"))
        .andExpect(jsonPath("$.records[0].instanceConfigInfo").value("cf_instance_config"))
        .andExpect(jsonPath("$.records[0].deployStatus").value("installed"))
        .andExpect(jsonPath("$.records[0].deploymentFile").value("cf-deployment.yml"))
        .andExpect(jsonPath("$.records[0].createUserId").value("admin"));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 기본 정보 설정 삭제 테스트
     * @title : deleteHbDiegoDefaultConfigInfo
     * @return : void
    *****************************************************************/
    @Test
    public void deleteHbDiegoDefaultConfigInfo(){
        HbDiegoDefaultConfigDTO resultDto = setDiegoInfo();
        mockHbDiegoDefaultConfigController.deleteDefaultConfigInfo(resultDto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 기본 정보 설정 저장 테스트
     * @title : saveHbDiegoDefaultConfigInfo
     * @return : void
    *****************************************************************/
    @Test
    public void saveHbDiegoDefaultConfigInfo(){
        HbDiegoDefaultConfigDTO resultDto = setDiegoInfo();
        mockHbDiegoDefaultConfigController.saveDefaultConfigInfo(resultDto, principal);
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
     * @description : DIEGO 기본 정보 설정 CF 정보 목록 조회 값 설정
     * @title : setCfInfoList
     * @return : List<HbCfVO>
    *****************************************************************/
    public List<HbCfVO> setCfInfoList(){
        List<HbCfVO> list = new ArrayList<HbCfVO>();
        HbCfVO vo = new HbCfVO();
        vo.setId(1);
        vo.setCfConfigName("cf_config");
        vo.setIaasType("OPENSTACK");
        vo.setNetworkConfigInfo("cf_network_config");
        vo.setKeyConfigInfo("cf_key_config");
        vo.setDefaultConfigInfo("cf_default_config");
        vo.setResourceConfigInfo("cf_resource_config");
        vo.setInstanceConfigInfo("cf_instance_config");
        vo.setDeployStatus("installed");
        vo.setDeploymentFile("cf-deployment.yml");
        vo.setCreateUserId("admin");
        list.add(vo);
        return list;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 기본 정보 값 설정
     * @title : setDiegoInfo
     * @return : HbDiegoDefaultConfigDTO
    *****************************************************************/
    public HbDiegoDefaultConfigDTO setDiegoInfo(){
        HbDiegoDefaultConfigDTO dto = new HbDiegoDefaultConfigDTO();
        dto.setId("1000");
        dto.setIaasType("OPENSTACK");
        dto.setDefaultConfigName("openstack_default_info");
        dto.setDeploymentName("openstack_diego");
        dto.setDirectorId(2);
        dto.setCfId(2);
        dto.setCfConfigName("cf_config_info");
        dto.setDiegoReleaseName("diego-release");
        dto.setDiegoReleaseVersion("1.25.0");
        dto.setCflinuxfs2rootfsreleaseName("cflinuxfs2-rootfs");
        dto.setCflinuxfs2rootfsreleaseVersion("2.0");
        dto.setGardenReleaseName("garden-runc");
        dto.setGardenReleaseVersion("1.9.1");
        dto.setPaastaMonitoringUse("false");
        return dto;
    }
}
