package org.openpaas.ieda.controller.hbdeploy.web.deploy.cf;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.ieda.controller.hbdeploy.web.deploy.cf.HbCfDefaultConfigController;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfDefaultConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfDefaultConfigDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.service.HbCfDefaultConfigService;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HbCfDefaultConfigControllerUnitTest {
    
    private MockMvc mockMvc;
    private static final ObjectMapper mapper = new ObjectMapper();
    
    @InjectMocks private HbCfDefaultConfigController mockHbCfDefaultConfigController;
    @Mock HbCfDefaultConfigService mockHbCfDefaultConfigService;
    
    private static final String CF_DEFAULT_CONFIG_VIEW_URL = "/deploy/hbCf/defaultConfig";
    private static final String CF_DEFAULT_CONFIG_LIST_URL = "/deploy/hbCf/default/list";
    private static final String CF_DEFAULT_CONFIG_SAVE_URL = "/deploy/hbCf/default/save";
    private static final String CF_DEFAULT_CONFIG_DELETE_URL = "/deploy/hbCf/default/delete";
    
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : 하나의 메소드가 실행되기전 호출
     * @title : setUp
     * @return : void
    *****************************************************************/
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mockHbCfDefaultConfigController).build();
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  기본 정보 관리 화면 이동 Unit Test
     * @title : testgoDefaulConfig
     * @return : void
    *****************************************************************/
    @Test
    public void testgoDefaulConfig() throws Exception{
        mockMvc.perform(get(CF_DEFAULT_CONFIG_VIEW_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(view().name("/hbdeploy/deploy/cf/hbCfDefaultConfig"));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  기본 정보 조회 Unit Test
     * @title : testGetDefaultConfigInfoList
     * @return : void
    *****************************************************************/
    @Test
    public void testGetDefaultConfigInfoList() throws Exception{
        List<HbCfDefaultConfigVO> expectList = setCfDefaultConfigList();
        when(mockHbCfDefaultConfigService.getDefaultConfigInfoList()).thenReturn(expectList);
        mockMvc.perform(get(CF_DEFAULT_CONFIG_LIST_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.records[0].recid").value(expectList.get(0).getRecid()))
        .andExpect(jsonPath("$.records[0].defaultConfigName").value(expectList.get(0).getDefaultConfigName()))
        .andExpect(jsonPath("$.records[0].iaasType").value(expectList.get(0).getIaasType()))
        .andExpect(jsonPath("$.records[0].deploymentName").value(expectList.get(0).getDeploymentName()))
        .andExpect(jsonPath("$.records[0].domain").value(expectList.get(0).getDomain()))
        .andExpect(jsonPath("$.records[0].domainOrganization").value(expectList.get(0).getDomainOrganization()));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  기본 정보 저장 Unit Test
     * @title : testSveDefaultConfigInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testSaveDefaultConfigInfo() throws Exception{
        String requestJson = mapper.writeValueAsString(setDefaultConfigInfo());
        mockMvc.perform(put(CF_DEFAULT_CONFIG_SAVE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isCreated());
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  기본 정보 삭제 Unit Test
     * @title : testSveDefaultConfigInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testDeleteDefaultConfigInfo() throws Exception{
        String requestJson = mapper.writeValueAsString(setDefaultConfigInfo());
        mockMvc.perform(delete(CF_DEFAULT_CONFIG_DELETE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isNoContent());
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  기본 정보 저장 값 설정
     * @title : testSveDefaultConfigInfo
     * @return : void
    *****************************************************************/
    private HbCfDefaultConfigDTO setDefaultConfigInfo() {
        HbCfDefaultConfigDTO dto = new HbCfDefaultConfigDTO();
        dto.setDefaultConfigName("default-config");
        dto.setDomain("cf.com");
        dto.setDomainOrganization("paas");
        dto.setIaasType("Openstack");
        dto.setId("1");
        return dto;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  기본 정보 목록 조회 값 설정
     * @title : testSveDefaultConfigInfo
     * @return : void
    *****************************************************************/
    private List<HbCfDefaultConfigVO> setCfDefaultConfigList() {
        List<HbCfDefaultConfigVO> list = new ArrayList<HbCfDefaultConfigVO>();
        HbCfDefaultConfigVO vo = new HbCfDefaultConfigVO();
        vo.setDefaultConfigName("defalut-config");
        vo.setDeploymentName("cf-deployment");
        vo.setDomain("cf.com");
        vo.setDomainOrganization("paas");
        vo.setIaasType("Openstack");
        vo.setId(1);
        list.add(vo);
        return list;
    }
}
