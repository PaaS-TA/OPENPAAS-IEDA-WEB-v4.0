package org.openpaas.ieda.hbdeploy.web.deploy.diego;

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
import org.openpaas.ieda.controller.hbdeploy.web.deploy.diego.HbDiegoResourceConfigController;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoResourceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoResourceConfigDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.service.HbDiegoResourceConfigService;
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
public class HbDiegoResourceControllerUnitTest extends BaseControllerUnitTest{

	private MockMvc mockMvc;
	private Principal principal = null;
	
	@InjectMocks HbDiegoResourceConfigController mockHbDiegoResourceConfigController;
	@Mock HbDiegoResourceConfigService mockHbDiegoResourceConfigService;
	
	final static String VIEW_URL = "/deploy/hbDiego/HbDiegoResourceConfig";
	final static String RESOURCEINFO_LIST_URL = "/deploy/hbDiego/resource/list";
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 하나의 메소드가 실행되기 전에 호출
     * @title : setUp
     * @return : void
    *****************************************************************/
    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mockHbDiegoResourceConfigController).build();
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
        .andExpect(view().name("/hbdeploy/deploy/diego/hbDiegoResourceConfig"));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 DIEGO 리소스 정보 설정 목록 조회 TEST
     * @title : getHbDiegoResourceConfigInfoListTest
     * @return : void
    *****************************************************************/
    @Test
    public void getHbDiegoResourceConfigInfoListTest() throws Exception{
        List<HbDiegoResourceConfigVO> resultList = setDiegoResourceConfigList();
        when(mockHbDiegoResourceConfigService.getDefaultConfigInfoList()).thenReturn(resultList);
        mockMvc.perform(get(RESOURCEINFO_LIST_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.records[0].id").value(1000))
        .andExpect(jsonPath("$.records[0].iaasType").value("OPENSTACK"))
        .andExpect(jsonPath("$.records[0].resourceConfigName").value("openstack_resource"))
        .andExpect(jsonPath("$.records[0].boshPassword").value("admin"))
        .andExpect(jsonPath("$.records[0].stemcellName").value("bosh-stmecell-openstack-kvm-ubuntu-trusty"))
        .andExpect(jsonPath("$.records[0].stemcellVersion").value("3468.21"))
        .andExpect(jsonPath("$.records[0].smallFlavor").value("m1.small"))
        .andExpect(jsonPath("$.records[0].mediumFlavor").value("m1.medium"))
        .andExpect(jsonPath("$.records[0].largeFlavor").value("m1.large"))
        .andExpect(jsonPath("$.records[0].directorId").value("1"))
        .andExpect(jsonPath("$.records[0].createUserId").value("admin"));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description :  DIEGO 리소스 정보 설정 저장 테스트
     * @title : saveHbDiegoResourceConfigInfoTest
     * @return : void
    *****************************************************************/
    @Test
    public void saveHbDiegoResourceConfigInfoTest(){
        HbDiegoResourceConfigDTO resultDto = setDiegoResourceConfigInfo();
        mockHbDiegoResourceConfigController.saveResourceConfigInfo(resultDto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 리소스 정보 설정 삭제 테스트
     * @title : deleteHbDiegoResourceConfigInfoTest
     * @return : void
    *****************************************************************/
    @Test
    public void deleteHbDiegoResourceConfigInfoTest(){
        HbDiegoResourceConfigDTO resultDto = setDiegoResourceConfigInfo();
        mockHbDiegoResourceConfigController.deleteResourceConfigInfo(resultDto, principal);
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
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 리소스 정보 값 설정
     * @title : setDiegoResourceConfigInfo
     * @return : HbDiegoResourceConfigDTO
    *****************************************************************/
    public HbDiegoResourceConfigDTO setDiegoResourceConfigInfo(){
        HbDiegoResourceConfigDTO dto = new HbDiegoResourceConfigDTO();
        dto.setId("1000");
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
}
