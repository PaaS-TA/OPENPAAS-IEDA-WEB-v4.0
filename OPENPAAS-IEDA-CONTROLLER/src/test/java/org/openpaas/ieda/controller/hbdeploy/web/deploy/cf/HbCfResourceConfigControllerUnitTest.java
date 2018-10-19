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
import org.openpaas.ieda.hbdeploy.web.config.setting.dao.HbDirectorConfigVO;
import org.openpaas.ieda.hbdeploy.web.config.setting.service.HbDirectorConfigService;
import org.openpaas.ieda.hbdeploy.web.config.stemcell.dao.HbStemcellManagementVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfResourceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfResourceConfigDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.service.HbCfResourceConfigService;
import org.openpaas.ieda.hbdeploy.web.information.stemcell.service.HbStemcellService;
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
public class HbCfResourceConfigControllerUnitTest {
    
    private MockMvc mockMvc;
    private static final ObjectMapper mapper = new ObjectMapper();
    
    @InjectMocks private HbCfResourceConfigController mockHbCfResourceConfigController;
    @Mock private HbCfResourceConfigService mockHbCfResourceConfigService;
    @Mock private HbDirectorConfigService mockHbDirectorConfigService;
    @Mock private HbStemcellService mockHbStemcellService;
    
    private final static String CF_RESOURCE_VIEW_URL = "/deploy/hbCf/resourceConfig";
    private final static String CF_RESOURCE_LIST_URL = "/deploy/hbCf/resource/list";
    private final static String CF_RESOURCE_SAVE_URL = "/deploy/hbCf/resource/save";
    private final static String CF_RESOURCE_DELETE_URL = "/deploy/hbCf/resource/delete";
    
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : 하나의 메소드가 실행되기전 호출
     * @title : setUp
     * @return : void
    *****************************************************************/
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mockHbCfResourceConfigController).build();
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  리소스 정보 관리 화면 이동 Unit Test
     * @title : testGoResourceConfig
     * @return : void
    *****************************************************************/
    @Test
    public void testGoResourceConfig() throws Exception{
        mockMvc.perform(get(CF_RESOURCE_VIEW_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(view().name("/hbdeploy/deploy/cf/hbCfResourceConfig"));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  리소스 정보 목록 조회  Unit Test
     * @title : testGetRecourceConfigInfoList
     * @return : void
    *****************************************************************/
    @Test
    public void testGetRecourceConfigInfoList() throws Exception{
        List<HbCfResourceConfigVO> expectList = setCfCrednetialConfigList();
        when(mockHbCfResourceConfigService.getResourceConfigInfoList()).thenReturn(expectList);
        mockMvc.perform(get(CF_RESOURCE_LIST_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.records[0].recid").value(expectList.get(0).getRecid()))
        .andExpect(jsonPath("$.records[0].id").value(expectList.get(0).getId()))
        .andExpect(jsonPath("$.records[0].resourceConfigName").value(expectList.get(0).getResourceConfigName()))
        .andExpect(jsonPath("$.records[0].iaasType").value(expectList.get(0).getIaasType()))
        .andExpect(jsonPath("$.records[0].stemcellName").value(expectList.get(0).getStemcellName()))
        .andExpect(jsonPath("$.records[0].stemcellVersion").value(expectList.get(0).getStemcellVersion()))
        .andExpect(jsonPath("$.records[0].smallFlavor").value(expectList.get(0).getSmallFlavor()))
        .andExpect(jsonPath("$.records[0].mediumFlavor").value(expectList.get(0).getMediumFlavor()))
        .andExpect(jsonPath("$.records[0].largeFlavor").value(expectList.get(0).getLargeFlavor()));
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  리소스 정보 저장 Unit Test
     * @title : testGetRecourceConfigInfoList
     * @return : void
    *****************************************************************/
    @Test
    public void testSaveResourceConfigInfo() throws Exception{
        String requestJson = mapper.writeValueAsString(setCfResourceConfigInfo());
        mockMvc.perform(put(CF_RESOURCE_SAVE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isCreated());
    }
    
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  리소스 정보 삭제 Unit Test
     * @title : testDeleteResourceConfigInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testDeleteResourceConfigInfo() throws Exception{
        String requestJson = mapper.writeValueAsString(setCfResourceConfigInfo());
        mockMvc.perform(delete(CF_RESOURCE_DELETE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isNoContent());
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  스템셀 목록 조회 결과 값 설정
     * @title : setCfStecmellInfoList
     * @return : List<HbStemcellManagementVO>
    *****************************************************************/
    private List<HbStemcellManagementVO> setCfStecmellInfoList() {
        List<HbStemcellManagementVO> list = new ArrayList<HbStemcellManagementVO> ();
        HbStemcellManagementVO vo = new HbStemcellManagementVO();
        vo.setDownloadStatus("done");
        vo.setIaas("Openstack");
        vo.setId(1);
        vo.setOs("ubuntu");
        vo.setOsVersion("14.04");
        vo.setIsExisted("exist");
        vo.setStemcellName("ubuntu-trust");
        vo.setStemcellUrl("https://aaa.com");
        vo.setStemcellVersion("3666.21");
        list.add(vo);
        return list;
    }

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  디렉터 정보 조회 결과 값 설정
     * @title : setCfDriectorInfoList
     * @return : List<HbDirectorConfigVO>
    *****************************************************************/
    private List<HbDirectorConfigVO> setCfDriectorInfoList() {
        List<HbDirectorConfigVO> list= new ArrayList<HbDirectorConfigVO>();
        HbDirectorConfigVO vo = new HbDirectorConfigVO();
        vo.setDirectorCpi("openstack_cpi");
        vo.setDirectorName("obosh");
        vo.setDirectorPort(25555);
        vo.setDirectorType("private");
        vo.setDirectorUrl("172.16.100.1");
        vo.setIedaDirectorConfigSeq(1);
        vo.setDirectorUuid("3123-ddfAS123sdfs313-dfv");
        vo.setIaasType("Openstack");
        list.add(vo);
        return list;
    }

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  리소스 정보 저장/삭제 요청 값 설정
     * @title : setCfResourceConfigInfo
     * @return : HbCfResourceConfigDTO
    *****************************************************************/
    private HbCfResourceConfigDTO setCfResourceConfigInfo() {
        HbCfResourceConfigDTO dto = new HbCfResourceConfigDTO();
        dto.setDirectorInfo("1");
        dto.setIaasType("Openstack");
        dto.setId("1");
        dto.setSmallFlavor("m1.small");
        dto.setMediumFlavor("m1.medium");
        dto.setLargeFlavor("m1.large");
        dto.setResourceConfigName("resource-config");
        dto.setStemcellName("ubuntu-trust");
        dto.setStemcellVersion("3568.21");
        return dto;
    }

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  리소스 정보 목록 조회 결과 값 설정
     * @title : setCfCrednetialConfigList
     * @return : List<HbCfResourceConfigVO>
    *****************************************************************/
    private List<HbCfResourceConfigVO> setCfCrednetialConfigList() {
        List<HbCfResourceConfigVO> list = new ArrayList<HbCfResourceConfigVO>();
        HbCfResourceConfigVO vo = new HbCfResourceConfigVO();
        vo.setIaasType("Openstack");
        vo.setId(1);
        vo.setSmallFlavor("m1.small");
        vo.setMediumFlavor("m1.medium");
        vo.setLargeFlavor("m1.large");
        vo.setResourceConfigName("resource-config");
        vo.setStemcellName("openstakc-ubuntu");
        vo.setStemcellVersion("3621.48");
        list.add(vo);
        return list;
    }
}
