package org.openpaas.ieda.controller.hbdeploy.web.deploy.cf;
import static org.mockito.Matchers.anyString;
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
import org.openpaas.ieda.controller.hbdeploy.web.deploy.cf.HbCfController;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.service.HbCfDeleteAsyncService;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.service.HbCfDeployAsynService;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.service.HbCfService;
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
public class HbCfControllerUnitTest {
    
    private MockMvc mockMvc;
    private static final ObjectMapper mapper = new ObjectMapper();
    
    @InjectMocks private HbCfController mockHbCfController;
    @Mock private HbCfService mockHbCfService;
    @Mock private HbCfDeployAsynService mockHbCfDeployAsyncService;
    @Mock private HbCfDeleteAsyncService mockHbCfDeleteAsyncService;
    
    private static final String CF_VIEW_URL = "/deploy/hbCf";
    private static final String CF_LIST_URL = "/deploy/hbCf/list/{installStatus}";
    private static final String CF_SAVE_URL = "/deploy/hbCf/install/save";
    private static final String CF_DELETE_URL = "/deploy/hbCf/delete/data";
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : 하나의 메소드가 실행되기전 호출
     * @title : setUp
     * @return : void
    *****************************************************************/
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mockHbCfController).build();
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF 설치 화면 이동 Unit Test
     * @title : testGoCfDeployment
     * @return : void
    *****************************************************************/
    @Test
    public void testGoCfDeployment() throws Exception{
        mockMvc.perform(get(CF_VIEW_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(view().name("/hbdeploy/deploy/cf/hbCf"));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF 설치 목록 조회 Unit Test
     * @title : testGoCfDeployment
     * @return : void
    *****************************************************************/
    @Test
    public void testGetHbCfDeploymenList() throws Exception{
        List<HbCfVO> expectList = setCfConfigList();
        when(mockHbCfService.getCfInfoList(anyString())).thenReturn(expectList);
        mockMvc.perform(get(CF_LIST_URL,"installAble").contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.records[0].recid").value(expectList.get(0).getRecid()))
        .andExpect(jsonPath("$.records[0].cfConfigName").value(expectList.get(0).getCfConfigName()))
        .andExpect(jsonPath("$.records[0].id").value(expectList.get(0).getId()))
        .andExpect(jsonPath("$.records[0].iaasType").value(expectList.get(0).getIaasType()))
        .andExpect(jsonPath("$.records[0].networkConfigInfo").value(expectList.get(0).getNetworkConfigInfo()))
        .andExpect(jsonPath("$.records[0].defaultConfigInfo").value(expectList.get(0).getDefaultConfigInfo()))
        .andExpect(jsonPath("$.records[0].taskId").value(expectList.get(0).getTaskId()))
        .andExpect(jsonPath("$.records[0].instanceConfigInfo").value(expectList.get(0).getInstanceConfigInfo()))
        .andExpect(jsonPath("$.records[0].resourceConfigInfo").value(expectList.get(0).getResourceConfigInfo()));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  설치 정보 저장 Unit Test
     * @title : testGoCf
     * @return : void
    *****************************************************************/
    @Test
    public void testSaveCfInfo() throws Exception{
        String requestJson = mapper.writeValueAsString(setCfConfigInfo());
        mockMvc.perform(put(CF_SAVE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isCreated());
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  설치 정보 삭제 Unit Test
     * @title : testGoCf
     * @return : void
    *****************************************************************/
    @Test
    public void testDeleteCfInfo() throws Exception{
        String requestJson = mapper.writeValueAsString(setCfConfigInfo());
        mockMvc.perform(delete(CF_DELETE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isNoContent());
    }

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  설치 목록 조회 결과 값 설정
     * @title : setCfConfigInfo
     * @return : List<HbCfVO>
    *****************************************************************/
    private HbCfDTO setCfConfigInfo() {
        HbCfDTO dto = new HbCfDTO();
        dto.setId("1");
        dto.setCfConfigName("cf-config");
        dto.setDefaultConfigInfo("default-config");
        dto.setIaasType("Openstack");
        dto.setInstanceConfigInfo("instance-config");
        dto.setIaasType("Openstack");
        dto.setNetworkConfigInfo("network-config");
        dto.setResourceConfigInfo("resource-config");
        return dto;
    }

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  설치 목록 조회 결과 값 설정
     * @title : setCfConfigList
     * @return : List<HbCfVO>
    *****************************************************************/
    private List<HbCfVO> setCfConfigList() {
        List<HbCfVO> list = new ArrayList<HbCfVO>();
        HbCfVO vo = new HbCfVO();
        vo.setCfConfigName("cf-config");
        vo.setDefaultConfigInfo("default-config");
        vo.setIaasType("Openstack");
        vo.setResourceConfigInfo("resource-config");
        vo.setDeployStatus("done");
        vo.setNetworkConfigInfo("network-config");
        vo.setId(1);
        vo.setInstanceConfigInfo("instance-config");
        list.add(vo);
        return list;
    }
}
