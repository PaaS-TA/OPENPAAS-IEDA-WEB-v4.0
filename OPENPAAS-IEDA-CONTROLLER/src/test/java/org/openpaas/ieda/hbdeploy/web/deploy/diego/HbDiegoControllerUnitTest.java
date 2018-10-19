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
import org.openpaas.ieda.controller.hbdeploy.web.deploy.diego.HbDiegoController;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoDeployAsyncService;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.service.HbDiegoDeleteAsyncService;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.service.HbDiegoService;
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
public class HbDiegoControllerUnitTest extends BaseControllerUnitTest{

	private MockMvc mockMvc;
	private Principal principal = null;
	
	@InjectMocks HbDiegoController mockHbDiegoController;
	@Mock HbDiegoService mockHbDiegoService;
	@Mock HbDiegoDeployAsyncService mockHbDiegoDeployAsyncService;
	@Mock HbDiegoDeleteAsyncService mockHbDiegoDeleteAsyncService;
	
	final static String VIEW_URL = "/deploy/hbDiego/HbDiego";
	final static String DIEGOINFO_LIST_URL = "/deploy/hbDiego/list/installed";
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 하나의 메소드가 실행되기 전에 호출
     * @title : setUp
     * @return : void
    *****************************************************************/
    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mockHbDiegoController).build();
        principal = getLoggined();
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 DIEGO 설치 화면 이동 TEST
     * @title : goHbDiegoDeploymentTest
     * @return : void
    *****************************************************************/
    @Test
    public void goHbDiegoDeploymentTest() throws Exception{
        mockMvc.perform(get(VIEW_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(view().name("/hbdeploy/deploy/diego/hbDiego"));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 설치 목록 조회 TEST
     * @title : getHbDiegoInfoListTest
     * @return : void
    *****************************************************************/
    @Test
    public void getHbDiegoInfoListTest() throws Exception{
        List<HbDiegoVO> resultList = setDiegoInfoList();
        when(mockHbDiegoService.getDiegoInfoList(anyString())).thenReturn(resultList);
        mockMvc.perform(get(DIEGOINFO_LIST_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.records[0].id").value(1))
        .andExpect(jsonPath("$.records[0].iaasType").value("OPENSTACK"))
        .andExpect(jsonPath("$.records[0].diegoConfigName").value("openstack_diego"))
        .andExpect(jsonPath("$.records[0].defaultConfigInfo").value("diego_default"))
        .andExpect(jsonPath("$.records[0].networkConfigInfo").value("diego_network"))
        .andExpect(jsonPath("$.records[0].resourceConfigInfo").value("diego_resource"))
        .andExpect(jsonPath("$.records[0].instanceConfigInfo").value("diego_instance"))
        .andExpect(jsonPath("$.records[0].deployStatus").value("installed"))
        .andExpect(jsonPath("$.records[0].deploymentFile").value("openstack_diego.yml"))
        .andExpect(jsonPath("$.records[0].createUserId").value("admin"));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description :  DIEGO 설정 정보 저장 TEST
     * @title : saveDiegoInfoTest
     * @return : void
    *****************************************************************/
    @Test
    public void saveDiegoInfoTest() throws Exception{
        HbDiegoDTO resultDto = setDiegoInfo();
        mockHbDiegoController.saveDiegoInfo(resultDto, principal);
    }
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화삭제
     * @description :  DIEGO 설정 정보 삭제 TEST
     * @title : DiegoInfoTest
     * @return : void
    *****************************************************************/
    @Test
    public void deleteDiegoInfoTest() throws Exception{
        HbDiegoDTO resultDto = setDiegoInfo();
        mockHbDiegoController.deleteDiegoDataInfo(resultDto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description :  DIEGO 설치 테스트
     * @title : installDiegoTest
     * @return : void
    *****************************************************************/
    @Test
    public void installDiegoTest() throws Exception{
        HbDiegoDTO resultDto = setDiegoInfo();
        mockHbDiegoController.installDiego(resultDto, principal);
        
    }
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description :  DIEGO 삭제 테스트
     * @title : deleteDiegoTest
     * @return : void
    *****************************************************************/
    @Test
    public void deleteDiegoTest() throws Exception{
        HbDiegoDTO resultDto = setDiegoInfo();
        mockHbDiegoController.deleteDiego(resultDto, principal);
        
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 정보 리스트 값 설정
     * @title : setDiegoInfoList
     * @return : List<HbDiegoVO>
    *****************************************************************/
    public List<HbDiegoVO> setDiegoInfoList(){
        List<HbDiegoVO> list = new ArrayList<HbDiegoVO>();
        HbDiegoVO vo = new HbDiegoVO();
        vo.setId(1);
        vo.setIaasType("OPENSTACK");
        vo.setDiegoConfigName("openstack_diego");
        vo.setDefaultConfigInfo("diego_default");
        vo.setNetworkConfigInfo("diego_network");
        vo.setResourceConfigInfo("diego_resource");
        vo.setInstanceConfigInfo("diego_instance");
        vo.setDeployStatus("installed");
        vo.setDeploymentFile("openstack_diego.yml");
        vo.setCreateUserId("admin");
        list.add(vo);
        return list;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 
     * @title : setDiegoInfo
     * @return : HbDiegoDTO
    *****************************************************************/
    public HbDiegoDTO setDiegoInfo(){
        HbDiegoDTO dto = new HbDiegoDTO();
        dto.setId("2");
        dto.setIaasType("OPENSTACK");
        dto.setDiegoConfigName("openstack_diego_new");
        dto.setDefaultConfigInfo("diego_default_re");
        dto.setNetworkConfigInfo("diego_network_re");
        dto.setResourceConfigInfo("diego_resource_re");
        dto.setInstanceConfigInfo("diego_instance_re");
        dto.setDeployStatus("installed");
        dto.setDeploymentFile("openstack_diego_2.yml");
        return dto;
    }
}
