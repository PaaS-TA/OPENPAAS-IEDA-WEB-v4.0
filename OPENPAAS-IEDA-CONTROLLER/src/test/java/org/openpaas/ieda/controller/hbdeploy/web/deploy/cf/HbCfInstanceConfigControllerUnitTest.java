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
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.Application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.ieda.controller.hbdeploy.web.deploy.cf.HbCfInstanceConfigController;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfInstanceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfInstanceConfigDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.service.HbCfInstanceConfigService;
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
public class HbCfInstanceConfigControllerUnitTest {
    
    private MockMvc mockMvc;
    private static final ObjectMapper mapper = new ObjectMapper();
    
    @InjectMocks private HbCfInstanceConfigController mockHbCfInstanceConfigController;
    @Mock private HbCfInstanceConfigService mockHbCfInstanceConfigService;
    
    private static final String CF_INSTANCE_VIEW_URL = "/deploy/hbCf/instanceConfig";
    private static final String CF_INSTANCE_LIST_URL = "/deploy/hbCf/instance/list";
    private static final String CF_INSTANCE_JOB_LIST_URL = "/deploy/hbCf/instance/jobSetting/list/{version}/{deployType}";
    private static final String CF_INSTANCE_SAVE_URL = "/deploy/hbCf/instance/save";
    private static final String CF_INSTANCE_DELETE_URL = "/deploy/hbCf/instance/delete";
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : 하나의 메소드가 실행되기전 호출
     * @title : setUp
     * @return : void
    *****************************************************************/
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mockHbCfInstanceConfigController).build();
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  인스턴스 정보 관리 화면 이동 Unit Test
     * @title : testgoDefaulConfig
     * @return : void
    *****************************************************************/
    @Test
    public void testGoInstanceConfig() throws Exception{
        mockMvc.perform(get(CF_INSTANCE_VIEW_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(view().name("/hbdeploy/deploy/cf/hbCfInstanceConfig"));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  인스턴스 목록 정보 조회 Unit Test
     * @title : testgoDefaulConfig
     * @return : void
    *****************************************************************/
    @Test
    public void testGetHbCfInstanceConfigInfoList() throws Exception{
        List<HbCfInstanceConfigVO> expectList = setCfInstanceConfigList();
        when(mockHbCfInstanceConfigService.getInstanceConfigList()).thenReturn(expectList);
        mockMvc.perform(get(CF_INSTANCE_LIST_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.records[0].recid").value(expectList.get(0).getRecid()))
        .andExpect(jsonPath("$.records[0].instanceConfigName").value(expectList.get(0).getInstanceConfigName()))
        .andExpect(jsonPath("$.records[0].iaasType").value(expectList.get(0).getIaasType()));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  Job 정보 조회 Unit Test
     * @title : testGetHbCfJobList
     * @return : void
    *****************************************************************/
    @Test
    public void testGetHbCfJobList() throws Exception{
        List<HashMap<String, String>> expectList = setCfJobList();
        when(mockHbCfInstanceConfigService.getJobTemplateList(anyString(), anyString())).thenReturn(expectList);
        mockMvc.perform(get(CF_INSTANCE_JOB_LIST_URL,"2.7.0", "CF-").contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].zone").value(expectList.get(0).get("zone")))
        .andExpect(jsonPath("$[0].job").value(expectList.get(0).get("job")))
        .andExpect(jsonPath("$[0].instances").value(expectList.get(0).get("instances")));
    }

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  Job 정보 저장 Unit Test
     * @title : testSaveInstanceConfigInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testSaveInstanceConfigInfo() throws Exception{
        String requestJson = mapper.writeValueAsString(setCfInstanceConfigInfo());
        mockMvc.perform(put(CF_INSTANCE_SAVE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isCreated());
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  Job 정보 삭제 Unit Test
     * @title : testDeleteInstanceConfigInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testDeleteInstanceConfigInfo() throws Exception{
        String requestJson = mapper.writeValueAsString(setCfInstanceConfigInfo());
        mockMvc.perform(delete(CF_INSTANCE_DELETE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isNoContent());
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  Job 정보 저장/삭제 값 설정
     * @title : setCfInstanceConfigInfo
     * @return : HbCfInstanceConfigDTO
    *****************************************************************/
    private HbCfInstanceConfigDTO setCfInstanceConfigInfo() {
        HbCfInstanceConfigDTO dto = new HbCfInstanceConfigDTO();
        dto.setId("1");
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
        return dto;
    }

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  JOB 목록 정보 조회 결과 값 설정
     * @title : setCfInstanceConfigList
     * @return : List<HashMap<String, String>>
    *****************************************************************/
    private List<HashMap<String, String>> setCfJobList() {
        List<HashMap<String, String>> maps = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("zone", "z1");
        map.put("job", "diego-cell");
        map.put("instances", "1");
        maps.add(map);
        return maps;
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
