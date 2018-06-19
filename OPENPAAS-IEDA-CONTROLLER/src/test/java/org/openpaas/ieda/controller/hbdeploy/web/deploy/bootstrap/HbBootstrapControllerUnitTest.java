package org.openpaas.ieda.controller.hbdeploy.web.deploy.bootstrap;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dao.HbBootstrapVO;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dto.HbBootStrapDeployDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dto.HbBootstrapListDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.service.HbBootstrapDeleteDeployAsyncService;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.service.HbBootstrapDeployAsyncService;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.service.HbBootstrapSaveService;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.service.HbBootstrapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HbBootstrapControllerUnitTest extends BaseControllerUnitTest {
    
    private MockMvc mockMvc;
    private static final ObjectMapper mapper = new ObjectMapper();
    private Principal principal = null;
    
    @InjectMocks
    HbBootstrapController mockHbBootstrapControllerUnitTest;
    
    @Mock 
    HbBootstrapSaveService mockHbBootstrapSaveService;
    
    @Mock
    HbBootstrapService mockHbBootstrapService;
    
    @Mock
    HbBootstrapDeployAsyncService mockHbBootstrapDeployAsyncService;
    
    @Mock
    HbBootstrapDeleteDeployAsyncService mockHbBootstrapDeleteDeployAsyncService;
    
    private final static Logger LOGGER = LoggerFactory.getLogger(HbBootstrapController.class);
    
    final static String HYBRID_BOOTSTRAP_VIEW_URL="/deploy/hbBootstrap";
    final static String HYBRID_BOOTSTRAP_POP_VIEW_URL="/deploy/hbBootstrap/install/bootstrapPopup";
    final static String HYBRID_BOOTSTRAP_LIST_URL="/deploy/hbBootstrap/list";
    final static String HYBIRD_BOOTSTRAP_INFO_URL="/deploy/hbBootstrap/install/detail/{id}/{iaas}";
    final static String HYBIRD_BOOTSTRAP_INSTALL_INFO_URL="/deploy/hbBootstrap/install/hbDetail/{privateBootstrapId}/{publicBootStrapId}";
    final static String HYBRID_SAVE_IAASCONFIG_INFO_URL="/deploy/hbBootstrap/install/setIaasConfigInfo";
    final static String HYBRID_SAVE_DEFAULT_INFO_URL="/deploy/hbBootstrap/install/setDefaultInfo";
    final static String HYBRID_SAVE_NETWORK_INFO_URL="/deploy/hbBootstrap/install/setNetworkInfo";
    final static String HYBRID_SAVE_RESOURCE_INFO_URL="/deploy/hbBootstrap/install/setResourceInfo";
    final static String HYBRID_CREATE_SETTING_FILE_URL="/deploy/hbBootstrap/install/createSettingFile/{id}/{iaas}";
    final static String HYBRID_BOOTSTRAP_INSTALL_URL="deploy/hbBootstrap/install/bootstrapInstall";
    final static String HYBRID_BOOTSTRAP_DELETE_URL="/deploy/hbBootstrap/delete/instance";
    final static String HYBRID_BOOTSTRAP_DELETE_INFO_URL="/deploy/hbBootstrap/delete/data";
    final static String HYBRID_BOOTSTRAP_LOG_INFO_URL="/deploy/hbBootstrap/list/{id}/{iaas}";
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : 하나의 메소드가 실행되기전 호출
     * @title : setUp
     * @return : void
    *****************************************************************/
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mockHbBootstrapControllerUnitTest).build();
        principal = getLoggined();
    }
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : Hybrid_Bootstrap 설치 화면 이동 Unit Test
     * @title : testGoBootstrap
     * @return : void
    *****************************************************************/
    //@Test
    public void testGoBootstrap() throws Exception{
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> Hybrid_Bootstrap 설치 화면 이동 Unit Test"); }
        mockMvc.perform(get(HYBRID_BOOTSTRAP_VIEW_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(view().name("/hbdeploy/deploy/bootstrap/hbBootstrap"));
    }
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : Hybrid_Bootstrap 설치 팝업 화면 이동 Unit Test
     * @title : testGoHbBootstrapPopup
     * @return : void
    *****************************************************************/
    @Test
    public void testGoHbBootstrapPopup() throws Exception{
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> Hybrid_Bootstrap 설치 팝업 화면 이동 Unit Test"); }
        mockMvc.perform(get(HYBRID_BOOTSTRAP_POP_VIEW_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(view().name("/hbdeploy/deploy/bootstrap/hbBootstrapPopup"));
    }
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : Hybrid_Bootstrap 정보 목록 조회 Unit Test
     * @title : testGetHbBootstrapList
     * @return : void
    *****************************************************************/
    @Test
    public void testGetHbBootstrapList() throws Exception{
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> Hybrid_Bootstrap 정보 목록 조회 Unit Test"); }
        List<HbBootstrapListDTO> expectBootstrapList = setBootstrapList();
        when(mockHbBootstrapService.getHbBootstrapList()).thenReturn(expectBootstrapList);
        mockMvc.perform(get(HYBRID_BOOTSTRAP_LIST_URL).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.records[0].recid").value(expectBootstrapList.get(0).getRecid()))
        .andExpect(jsonPath("$.records[0].id").value(expectBootstrapList.get(0).getId()))
        .andExpect(jsonPath("$.records[0].iaasConfigAlias").value(expectBootstrapList.get(0).getIaasConfigAlias()))
        .andExpect(jsonPath("$.records[0].deployStatus").value(expectBootstrapList.get(0).getDeployStatus()))
        .andExpect(jsonPath("$.records[0].deploymentName").value(expectBootstrapList.get(0).getDeploymentName()))
        .andExpect(jsonPath("$.records[0].directorName").value(expectBootstrapList.get(0).getDirectorName()))
        .andExpect(jsonPath("$.records[0].iaas").value(expectBootstrapList.get(0).getIaas()))
        .andExpect(jsonPath("$.records[0].boshRelease").value(expectBootstrapList.get(0).getBoshRelease()))
        .andExpect(jsonPath("$.records[0].boshCpiRelease").value(expectBootstrapList.get(0).getBoshCpiRelease()))
        .andExpect(jsonPath("$.records[0].subnetId").value(expectBootstrapList.get(0).getSubnetId()))
        .andExpect(jsonPath("$.records[0].subnetRange").value(expectBootstrapList.get(0).getSubnetRange()))
        .andExpect(jsonPath("$.records[0].publicStaticIp").value(expectBootstrapList.get(0).getPublicStaticIp()))
        .andExpect(jsonPath("$.records[0].privateStaticIp").value(expectBootstrapList.get(0).getPrivateStaticIp()))
        .andExpect(jsonPath("$.records[0].subnetGateway").value(expectBootstrapList.get(0).getSubnetGateway()))
        .andExpect(jsonPath("$.records[0].subnetDns").value(expectBootstrapList.get(0).getSubnetDns()))
        .andExpect(jsonPath("$.records[0].ntp").value(expectBootstrapList.get(0).getNtp()))
        .andExpect(jsonPath("$.records[0].stemcell").value(expectBootstrapList.get(0).getStemcell()))
        .andExpect(jsonPath("$.records[0].instanceType").value(expectBootstrapList.get(0).getInstanceType()))
        .andExpect(jsonPath("$.records[0].boshPassword").value(expectBootstrapList.get(0).getBoshPassword()))
        .andExpect(jsonPath("$.records[0].deploymentFile").value(expectBootstrapList.get(0).getDeploymentFile()))
        .andExpect(jsonPath("$.records[0].deployLog").value(expectBootstrapList.get(0).getDeployLog()))
        .andExpect(jsonPath("$.records[0].hypriId").value(expectBootstrapList.get(0).getHyPriId()))
        .andExpect(jsonPath("$.records[0].hyPriDeployStatus").value(expectBootstrapList.get(0).getHyPriDeployStatus()))
        .andExpect(jsonPath("$.records[0].hyPriDeploymentName").value(expectBootstrapList.get(0).getHyPriDeploymentName()))
        .andExpect(jsonPath("$.records[0].hyPriDirectorName").value(expectBootstrapList.get(0).getHyPriDirectorName()))
        .andExpect(jsonPath("$.records[0].hyPriIaas").value(expectBootstrapList.get(0).getHyPriIaas()))
        .andExpect(jsonPath("$.records[0].hyPriBoshRelease").value(expectBootstrapList.get(0).getHyPriBoshRelease()))
        .andExpect(jsonPath("$.records[0].hyPriBoshCpiRelease").value(expectBootstrapList.get(0).getHyPriBoshCpiRelease()))
        .andExpect(jsonPath("$.records[0].hyPriSubnetId").value(expectBootstrapList.get(0).getHyPriSubnetId()))
        .andExpect(jsonPath("$.records[0].hyPriSubnetRange").value(expectBootstrapList.get(0).getHyPriSubnetRange()))
        .andExpect(jsonPath("$.records[0].hyPriPublicStaticIp").value(expectBootstrapList.get(0).getHyPriPublicStaticIp()))
        .andExpect(jsonPath("$.records[0].hyPriPrivateStaticIp").value(expectBootstrapList.get(0).getHyPriPrivateStaticIp()))
        .andExpect(jsonPath("$.records[0].hyPriSubnetGateway").value(expectBootstrapList.get(0).getHyPriSubnetGateway()))
        .andExpect(jsonPath("$.records[0].hyPriSubnetDns").value(expectBootstrapList.get(0).getHyPriSubnetDns()))
        .andExpect(jsonPath("$.records[0].hyPriNtp").value(expectBootstrapList.get(0).getHyPriNtp()))
        .andExpect(jsonPath("$.records[0].hyPriStemcell").value(expectBootstrapList.get(0).getHyPriStemcell()))
        .andExpect(jsonPath("$.records[0].hyPriInstanceType").value(expectBootstrapList.get(0).getHyPriInstanceType()))
        .andExpect(jsonPath("$.records[0].hyPriBoshPassword").value(expectBootstrapList.get(0).getHyPriBoshPassword()))
        .andExpect(jsonPath("$.records[0].hyPriDeploymentFile").value(expectBootstrapList.get(0).getHyPriDeploymentFile()))
        .andExpect(jsonPath("$.records[0].hyPriDeployLog").value(expectBootstrapList.get(0).getHyPriDeployLog()))
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : Hybrid_Bootstrap 상세 정보 조회 Unit Test
     * @title : testGetHbBootstrapInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testGetHbBootstrapInfo() throws Exception{
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> Hybrid_Bootstrap 정보 목록 조회 Unit Test"); }
        HbBootstrapVO expectVo = setBootstrapInfo();
        when(mockHbBootstrapService.getHbBootstrapInfo(anyInt(), anyString())).thenReturn(expectVo);
        mockMvc.perform(get(HYBIRD_BOOTSTRAP_INFO_URL,"1","openstack").contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(expectVo.getId()))
        .andExpect(jsonPath("$.iaasType").value(expectVo.getIaasType()))
        .andExpect(jsonPath("$.iaasConfigId").value(expectVo.getIaasConfigId()))
        .andExpect(jsonPath("$.iaasConfig.accountId").value(expectVo.getIaasConfig().getAccountId()))
        .andExpect(jsonPath("$.iaasConfig.accountName").value(expectVo.getIaasConfig().getAccountName()))
        .andExpect(jsonPath("$.iaasConfig.commonKeypairName").value(expectVo.getIaasConfig().getCommonKeypairName()))
        .andExpect(jsonPath("$.iaasConfig.commonKeypairPath").value(expectVo.getIaasConfig().getCommonKeypairPath()))
        .andExpect(jsonPath("$.iaasConfig.commonSecurityGroup").value(expectVo.getIaasConfig().getCommonSecurityGroup()))
        .andExpect(jsonPath("$.iaasConfig.iaasConfigAlias").value(expectVo.getIaasConfig().getIaasConfigAlias()))
        
        .andExpect(jsonPath("$.deploymentName").value(expectVo.getDeploymentName()))
        .andExpect(jsonPath("$.directorName").value(expectVo.getDirectorName()))
        .andExpect(jsonPath("$.boshRelease").value(expectVo.getBoshRelease()))
        .andExpect(jsonPath("$.boshCpiRelease").value(expectVo.getBoshCpiRelease()))
        .andExpect(jsonPath("$.enableSnapshots").value(expectVo.getEnableSnapshots()))
        .andExpect(jsonPath("$.snapshotSchedule").value(expectVo.getSnapshotSchedule()))
        
        .andExpect(jsonPath("$.subnetId").value(expectVo.getSubnetId()))
        .andExpect(jsonPath("$.privateStaticIp").value(expectVo.getPrivateStaticIp()))
        .andExpect(jsonPath("$.publicStaticIp").value(expectVo.getPublicStaticIp()))
        .andExpect(jsonPath("$.subnetRange").value(expectVo.getSubnetRange()))
        .andExpect(jsonPath("$.subnetGateway").value(expectVo.getSubnetGateway()))
        .andExpect(jsonPath("$.subnetDns").value(expectVo.getSubnetDns()))
        .andExpect(jsonPath("$.ntp").value(expectVo.getNtp()))
        
        .andExpect(jsonPath("$.stemcell").value(expectVo.getStemcell()))
        .andExpect(jsonPath("$.cloudInstanceType").value(expectVo.getCloudInstanceType()))
        .andExpect(jsonPath("$.boshPassword").value(expectVo.getBoshPassword()))
        .andExpect(jsonPath("$.deploymentFile").value(expectVo.getDeploymentFile()))
        
        
        .andExpect(jsonPath("$.hyPriId").value(expectVo.getHyPriId()))
        .andExpect(jsonPath("$.hyPriIaasType").value(expectVo.getHyPriIaasType()))
        .andExpect(jsonPath("$.hyPriIaasConfigId").value(expectVo.getHyPriIaasConfigId()))
        
        .andExpect(jsonPath("$.hyPriDeploymentName").value(expectVo.getHyPriDeploymentName()))
        .andExpect(jsonPath("$.hyPriDirectorName").value(expectVo.getHyPriDirectorName()))
        .andExpect(jsonPath("$.hyPriBoshRelease").value(expectVo.getHyPriBoshRelease()))
        .andExpect(jsonPath("$.hyPriBoshCpiRelease").value(expectVo.getHyPriBoshCpiRelease()))
        .andExpect(jsonPath("$.hyPriEnableSnapshots").value(expectVo.getHyPriEnableSnapshots()))
        .andExpect(jsonPath("$.hyPriSnapshotSchedule").value(expectVo.getHyPriSnapshotSchedule()))
        
        .andExpect(jsonPath("$.hyPriSubnetId").value(expectVo.getHyPriSubnetId()))
        .andExpect(jsonPath("$.hyPriPrivateStaticIp").value(expectVo.getHyPriPrivateStaticIp()))
        .andExpect(jsonPath("$.hyPriPublicStaticIp").value(expectVo.getHyPriPublicStaticIp()))
        .andExpect(jsonPath("$.hyPriSubnetRange").value(expectVo.getHyPriSubnetRange()))
        .andExpect(jsonPath("$.hyPriSubnetGateway").value(expectVo.getHyPriSubnetGateway()))
        .andExpect(jsonPath("$.hyPriSubnetDns").value(expectVo.getHyPriSubnetDns()))
        .andExpect(jsonPath("$.hyPriNtp").value(expectVo.getHyPriNtp()))
        
        .andExpect(jsonPath("$.hyPriStemcell").value(expectVo.getStemcell()))
        .andExpect(jsonPath("$.hyPriCloudInstanceType").value(expectVo.getCloudInstanceType()))
        .andExpect(jsonPath("$.hyPriBoshPassword").value(expectVo.getBoshPassword()))
        .andExpect(jsonPath("$.hyPriDeploymentFile").value(expectVo.getDeploymentFile()));
    }
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : Hybrid_Bootstrap 상세 정보 조회 Unit Test
     * @title : testGetHbBootstrapInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testGetHbBootstrapInstallInfo() throws Exception{
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> Hybrid_Bootstrap 설치 상세 정보 조회 Unit Test"); }
        HbBootstrapVO expectVo = setBootstrapInfo();
        when(mockHbBootstrapService.getHbBootstrapInstallInfo(anyString(), anyString())).thenReturn(expectVo);
        mockMvc.perform(get(HYBIRD_BOOTSTRAP_INSTALL_INFO_URL,"1","1").contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(expectVo.getId()))
        .andExpect(jsonPath("$.iaasType").value(expectVo.getIaasType()))
        .andExpect(jsonPath("$.iaasConfigId").value(expectVo.getIaasConfigId()))
        .andExpect(jsonPath("$.iaasConfig.accountId").value(expectVo.getIaasConfig().getAccountId()))
        .andExpect(jsonPath("$.iaasConfig.accountName").value(expectVo.getIaasConfig().getAccountName()))
        .andExpect(jsonPath("$.iaasConfig.commonKeypairName").value(expectVo.getIaasConfig().getCommonKeypairName()))
        .andExpect(jsonPath("$.iaasConfig.commonKeypairPath").value(expectVo.getIaasConfig().getCommonKeypairPath()))
        .andExpect(jsonPath("$.iaasConfig.commonSecurityGroup").value(expectVo.getIaasConfig().getCommonSecurityGroup()))
        .andExpect(jsonPath("$.iaasConfig.iaasConfigAlias").value(expectVo.getIaasConfig().getIaasConfigAlias()))
        
        .andExpect(jsonPath("$.deploymentName").value(expectVo.getDeploymentName()))
        .andExpect(jsonPath("$.directorName").value(expectVo.getDirectorName()))
        .andExpect(jsonPath("$.boshRelease").value(expectVo.getBoshRelease()))
        .andExpect(jsonPath("$.boshCpiRelease").value(expectVo.getBoshCpiRelease()))
        .andExpect(jsonPath("$.enableSnapshots").value(expectVo.getEnableSnapshots()))
        .andExpect(jsonPath("$.snapshotSchedule").value(expectVo.getSnapshotSchedule()))
        
        .andExpect(jsonPath("$.subnetId").value(expectVo.getSubnetId()))
        .andExpect(jsonPath("$.privateStaticIp").value(expectVo.getPrivateStaticIp()))
        .andExpect(jsonPath("$.publicStaticIp").value(expectVo.getPublicStaticIp()))
        .andExpect(jsonPath("$.subnetRange").value(expectVo.getSubnetRange()))
        .andExpect(jsonPath("$.subnetGateway").value(expectVo.getSubnetGateway()))
        .andExpect(jsonPath("$.subnetDns").value(expectVo.getSubnetDns()))
        .andExpect(jsonPath("$.ntp").value(expectVo.getNtp()))
        
        .andExpect(jsonPath("$.stemcell").value(expectVo.getStemcell()))
        .andExpect(jsonPath("$.cloudInstanceType").value(expectVo.getCloudInstanceType()))
        .andExpect(jsonPath("$.boshPassword").value(expectVo.getBoshPassword()))
        .andExpect(jsonPath("$.deploymentFile").value(expectVo.getDeploymentFile()))
        
        
        .andExpect(jsonPath("$.hyPriId").value(expectVo.getHyPriId()))
        .andExpect(jsonPath("$.hyPriIaasType").value(expectVo.getHyPriIaasType()))
        .andExpect(jsonPath("$.hyPriIaasConfigId").value(expectVo.getHyPriIaasConfigId()))
        
        .andExpect(jsonPath("$.hyPriDeploymentName").value(expectVo.getHyPriDeploymentName()))
        .andExpect(jsonPath("$.hyPriDirectorName").value(expectVo.getHyPriDirectorName()))
        .andExpect(jsonPath("$.hyPriBoshRelease").value(expectVo.getHyPriBoshRelease()))
        .andExpect(jsonPath("$.hyPriBoshCpiRelease").value(expectVo.getHyPriBoshCpiRelease()))
        .andExpect(jsonPath("$.hyPriEnableSnapshots").value(expectVo.getHyPriEnableSnapshots()))
        .andExpect(jsonPath("$.hyPriSnapshotSchedule").value(expectVo.getHyPriSnapshotSchedule()))
        
        .andExpect(jsonPath("$.hyPriSubnetId").value(expectVo.getHyPriSubnetId()))
        .andExpect(jsonPath("$.hyPriPrivateStaticIp").value(expectVo.getHyPriPrivateStaticIp()))
        .andExpect(jsonPath("$.hyPriPublicStaticIp").value(expectVo.getHyPriPublicStaticIp()))
        .andExpect(jsonPath("$.hyPriSubnetRange").value(expectVo.getHyPriSubnetRange()))
        .andExpect(jsonPath("$.hyPriSubnetGateway").value(expectVo.getHyPriSubnetGateway()))
        .andExpect(jsonPath("$.hyPriSubnetDns").value(expectVo.getHyPriSubnetDns()))
        .andExpect(jsonPath("$.hyPriNtp").value(expectVo.getHyPriNtp()))
        
        .andExpect(jsonPath("$.hyPriStemcell").value(expectVo.getStemcell()))
        .andExpect(jsonPath("$.hyPriCloudInstanceType").value(expectVo.getCloudInstanceType()))
        .andExpect(jsonPath("$.hyPriBoshPassword").value(expectVo.getBoshPassword()))
        .andExpect(jsonPath("$.hyPriDeploymentFile").value(expectVo.getDeploymentFile()));
    }
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : 기본 정보 저장 Unit Test
     * @title : testSaveIaasConfigInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testSaveDefaultInfo() throws Exception{
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> 기본 정보 저장 Unit Test"); }
        String requestJson = mapper.writeValueAsString(setDefaultInfo());
        when(mockHbBootstrapSaveService.saveDefaultInfo(any(), any())).thenReturn(setBootstrapInfo());
        mockMvc.perform(put(HYBRID_SAVE_DEFAULT_INFO_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.deploymentName").value("bosh"))
            .andExpect(jsonPath("$.directorName").value("test-bosh"))
            .andExpect(jsonPath("$.ntp").value("1.kr.pool.ntp.org, 0.asia.pool.ntp.org"))
            .andExpect(jsonPath("$.boshRelease").value("bosh-257.tgz"))
            .andExpect(jsonPath("$.boshCpiRelease").value("bosh-openstack-cpi-release-14.tgz"))
            .andExpect(jsonPath("$.enableSnapshots").value("true"))
            .andExpect(jsonPath("$.snapshotSchedule").value("0 0 7 * * * schedule"));
    }
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : 네트워크 정보 저장 Unit Test
     * @title : testSaveNetworkInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testSaveNetworkInfo() throws Exception{
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> 네트워크 정보 저장 Unit Test"); }
        String requestJson = mapper.writeValueAsString(setNetworkInfo());
        when(mockHbBootstrapSaveService.saveNetworkInfo(any(), any())).thenReturn(setBootstrapInfo());
        mockMvc.perform(put(HYBRID_SAVE_NETWORK_INFO_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.subnetId").value("subnet-12345"))
            .andExpect(jsonPath("$.privateStaticIp").value("10.0.100.11"))
            .andExpect(jsonPath("$.publicStaticIp").value("10.0.20.6"))
            .andExpect(jsonPath("$.subnetRange").value("10.0.20.0/24"))
            .andExpect(jsonPath("$.subnetGateway").value("10.0.20.1"))
            .andExpect(jsonPath("$.subnetDns").value("8.8.8.8"));
    }
    
    
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : 리소스 정보 저장 Unit Test
     * @title : testSaveResourcesInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testSaveResourcesInfo() throws Exception{
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> 리소스 정보 저장 Unit Test"); }
        String requestJson = mapper.writeValueAsString(setResourceInfo());
        when(mockHbBootstrapSaveService.saveResourceInfo(any(), any())).thenReturn(setBootstrapInfo());
        mockMvc.perform(put(HYBRID_SAVE_RESOURCE_INFO_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.stemcell").value("bosh-stemcell-3421-openstack-kvm-ubuntu-trusty-go_agent.tgz"))
            .andExpect(jsonPath("$.boshPassword").value("1234"))
            .andExpect(jsonPath("$.cloudInstanceType").value("m1.large"));
    }
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : Bootstrap 설치 Unit Test
     * @title : testInstallBootstrap
     * @return : void
    *****************************************************************/
    @Test
    public void testInstallBootstrap() throws Exception{
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> Bootstrap 설치 Unit Test"); }
        HbBootStrapDeployDTO.Install dto = setInstallInfo();
        doNothing().when(mockHbBootstrapDeployAsyncService).deployAsync(dto, principal);
        mockHbBootstrapControllerUnitTest.installBootstrap(dto, principal);
        verify(mockHbBootstrapDeployAsyncService, times(1)).deployAsync(dto, principal);
        verifyNoMoreInteractions(mockHbBootstrapDeployAsyncService);
    }
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : Bootstrap 배포 삭제 Unit Test
     * @title : testDeleteBootstrap
     * @return : void
    *****************************************************************/
    @Test
    public void testDeleteBootstrap() throws Exception{
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> Bootstrap 배포 삭제 Unit Test"); }
        HbBootStrapDeployDTO.Delete dto = setDeleteInfo();
        doNothing().when(mockHbBootstrapDeleteDeployAsyncService).deleteDeployAsync(dto, principal);
        mockHbBootstrapControllerUnitTest.deleteBootstrap(dto, principal);
        verify(mockHbBootstrapDeleteDeployAsyncService, times(1)).deleteDeployAsync(dto, principal);
        verifyNoMoreInteractions(mockHbBootstrapDeleteDeployAsyncService);
    }
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : Bootstrap 정보 삭제 Unit Test
     * @title : testDeleteBootstrapInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testDeleteBootstrapInfo() throws Exception{
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> Bootstrap 배포 삭제 Unit Test"); }
        HbBootStrapDeployDTO.Delete dto = setDeleteInfo();
        doNothing().when(mockHbBootstrapService).deleteBootstrapInfo(any());
        mockMvc.perform(MockMvcRequestBuilders.delete(HYBRID_BOOTSTRAP_DELETE_INFO_URL).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    }
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : 배포 파일 생성 Unit Test
     * @title : testMakeDeploymentFile
     * @return : void
    *****************************************************************/
    @Test
    public void testMakeDeploymentFile() throws Exception{
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> 배포 파일 생성 Unit Test"); }
        doNothing().when(mockHbBootstrapService).createSettingFile(1,"openstack");
        mockMvc.perform(post(HYBRID_CREATE_SETTING_FILE_URL, "1", "openstack").contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : 배포 파일 생성 Unit Test
     * @title : testMakeDeploymentFile
     * @return : void
    *****************************************************************/
    @Test
    public void testGetDeployLogMsg() throws Exception{
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> 배포 파일 생성 Unit Test"); }
        HbBootstrapVO expectVo = setBootstrapInfo();
        when(mockHbBootstrapService.getHbBootstrapInfo(anyInt(), anyString())).thenReturn(expectVo);
        mockMvc.perform(get(HYBRID_BOOTSTRAP_LOG_INFO_URL,1,"openstack").contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk());
    }
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : Bootstrap 삭제 정보 설정
     * @title : setDeleteInfo
     * @return : BootStrapDeployDTO.Delete
    *****************************************************************/
    public HbBootStrapDeployDTO.Delete setDeleteInfo(){
        HbBootStrapDeployDTO.Delete delete = new HbBootStrapDeployDTO.Delete();
        delete.setId("1");
        delete.setIaasType("Openstack");
        return delete;
    }
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : Bootstrap 설치 정보 설정
     * @title : setInstallInfo
     * @return : BootStrapDeployDTO.Install
    *****************************************************************/
    public HbBootStrapDeployDTO.Install setInstallInfo(){
        HbBootStrapDeployDTO.Install install = new HbBootStrapDeployDTO.Install();
        install.setId("1");
        install.setIaasType("Openstack");
        return install;
    }
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : 리소스 정보 설정
     * @title : setResourceInfo
     * @return : BootStrapDeployDTO.Resource
    *****************************************************************/
    public HbBootStrapDeployDTO.Resource setResourceInfo(){
        HbBootStrapDeployDTO.Resource resource = new HbBootStrapDeployDTO.Resource();
        resource.setId("1");
        resource.setStemcell("bosh-stemcell-3421-openstack-kvm-ubuntu-trusty-go_agent.tgz");
        resource.setCloudInstanceType("m1.large");
        resource.setBoshPassword("1234");
        return resource;
    }
    
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : 네트워크 정보 설정
     * @title : setNetworkInfo
     * @return : BootStrapDeployDTO.Network
    *****************************************************************/
    public HbBootStrapDeployDTO.Network setNetworkInfo(){
        HbBootStrapDeployDTO.Network network = new HbBootStrapDeployDTO.Network();
        network.setId("1");
        network.setSubnetId("text-subnetId-12345");
        network.setPrivateStaticIp("10.0.100.11");
        network.setPublicStaticIp("10.0.20.6");
        network.setSubnetRange("10.0.20.0/24");
        network.setSubnetGateway("10.0.20.1");
        network.setSubnetDns("8.8.8.8");
        
        return network;
    }
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : 기본 정보 설정
     * @title : setDefaultInfo
     * @return : BootStrapDeployDTO.Default
    *****************************************************************/
    public HbBootStrapDeployDTO.Default setDefaultInfo(){
        HbBootStrapDeployDTO.Default defaultInfo = new HbBootStrapDeployDTO.Default();
        defaultInfo.setId("1");
        defaultInfo.setDeploymentName("bosh");
        defaultInfo.setDirectorName("test-bosh");
        defaultInfo.setBoshRelease("bosh-257.tgz");
        defaultInfo.setBoshCpiRelease("bosh-openstack-cpi-release-14.tgz");
        defaultInfo.setEnableSnapshots("true");
        defaultInfo.setSnapshotSchedule("0 0 7 * * * schedule");
        defaultInfo.setNtp("1.kr.pool.ntp.org, 0.asia.pool.ntp.org");
        
        return defaultInfo;
    }
    
    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : Hybrid_Bootstrap 상세 정보 조회 정보 설정
     * @title : setBootstrapInfo
     * @return : HbBootstrapVO
    *****************************************************************/
    private HbBootstrapVO setBootstrapInfo() {
        HbBootstrapVO vo = new HbBootstrapVO();
        vo.setId(1);
        vo.setIaasType("Openstack");
        vo.setIaasConfigId(1);
        vo.getIaasConfig().setAccountId(1);
        vo.getIaasConfig().setAccountName("openstack_v2");
        vo.getIaasConfig().setCommonKeypairName("bosh-key");
        vo.getIaasConfig().setCommonKeypairPath("bosh-key.pem");
        vo.getIaasConfig().setCommonSecurityGroup("bosh-security");
        vo.getIaasConfig().setIaasConfigAlias("openstack-config1");
        vo.setDeploymentName("bosh");
        vo.setDirectorName("test-bosh");
        vo.setBoshRelease("bosh-257.tgz");
        vo.setBoshCpiRelease("bosh-openstack-cpi-release-14.tgz");
        vo.setEnableSnapshots("true");
        vo.setSnapshotSchedule("0 0 7 * * * schedule");
        vo.setNtp("1.kr.pool.ntp.org, 0.asia.pool.ntp.org");
        vo.setSubnetId("subnet-12345");
        vo.setPrivateStaticIp("10.0.100.11");
        vo.setPublicStaticIp("10.0.20.6");
        vo.setSubnetRange("10.0.20.0/24");
        vo.setSubnetGateway("10.0.20.1");
        vo.setSubnetDns("8.8.8.8");
        vo.setStemcell("bosh-stemcell-3421-openstack-kvm-ubuntu-trusty-go_agent.tgz");
        vo.setCloudInstanceType("m1.large");
        vo.setBoshPassword("1234");
        vo.setDeploymentFile("openstack-microbosh-test-1.yml");
        vo.setDeployLog("log...");
        
        vo.setHyPriId(1);
        vo.setHyPriIaasType("Openstack");
        vo.setHyPriIaasConfigId(1);
        vo.setHyPriDeploymentName("bosh");
        vo.setHyPriDirectorName("test-bosh");
        vo.setHyPriBoshRelease("bosh-257.tgz");
        vo.setHyPriBoshCpiRelease("bosh-openstack-cpi-release-14.tgz");
        vo.setHyPriEnableSnapshots("true");
        vo.setHyPriSnapshotSchedule("0 0 7 * * * schedule");
        vo.setHyPriNtp("1.kr.pool.ntp.org, 0.asia.pool.ntp.org");
        vo.setHyPriSubnetId("subnet-12345");
        vo.setHyPriPrivateStaticIp("10.0.100.11");
        vo.setHyPriPublicStaticIp("10.0.20.6");
        vo.setHyPriSubnetRange("10.0.20.0/24");
        vo.setHyPriSubnetGateway("10.0.20.1");
        vo.setHyPriSubnetDns("8.8.8.8");
        vo.setHyPriStemcell("bosh-stemcell-3421-openstack-kvm-ubuntu-trusty-go_agent.tgz");
        vo.setHyPriCloudInstanceType("m1.large");
        vo.setHyPriBoshPassword("1234");
        vo.setHyPriDeploymentFile("openstack-microbosh-test-1.yml");
        vo.setHyPriDeployLog("log...");
        
        
        vo.getId();
        vo.getIaasType();
        vo.getIaasConfigId();
        vo.getIaasConfig().getAccountId();
        vo.getIaasConfig().getAccountName();
        vo.getIaasConfig().getCommonKeypairName();
        vo.getIaasConfig().getCommonKeypairPath();
        vo.getIaasConfig().getCommonSecurityGroup();
        vo.getIaasConfig().getIaasConfigAlias();
        vo.getDeploymentName();
        vo.getDirectorName();
        vo.getBoshRelease();
        vo.getBoshCpiRelease();
        vo.getEnableSnapshots();
        vo.getSnapshotSchedule();
        vo.getNtp();
        vo.getSubnetId();
        vo.getPrivateStaticIp();
        vo.getPublicStaticIp();
        vo.getSubnetRange();
        vo.getSubnetGateway();
        vo.getSubnetDns();
        vo.getStemcell();
        vo.getCloudInstanceType();
        vo.getBoshPassword();
        vo.getDeploymentFile();
        vo.getDeployLog();
        
        vo.getHyPriId();
        vo.getHyPriIaasType();
        vo.getHyPriIaasConfigId();
        vo.getHyPriDeploymentName();
        vo.getHyPriDirectorName();
        vo.getHyPriBoshRelease();
        vo.getHyPriBoshCpiRelease();
        vo.getHyPriEnableSnapshots();
        vo.getHyPriSnapshotSchedule();
        vo.getHyPriNtp();
        vo.getHyPriSubnetId();
        vo.getHyPriPrivateStaticIp();
        vo.getHyPriPublicStaticIp();
        vo.getHyPriSubnetRange();
        vo.getHyPriSubnetGateway();
        vo.getHyPriSubnetDns();
        vo.getHyPriStemcell();
        vo.getHyPriCloudInstanceType();
        vo.getHyPriBoshPassword();
        vo.getHyPriDeploymentFile();
        vo.getHyPriDeployLog();
        return vo;
    }

    /****************************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : Hybrid_Bootstrap 정보 목록 조회 값 설정
     * @title : setBootstrapList
     * @return : List<HbBootstrapListDTO>
    *****************************************************************/
    private List<HbBootstrapListDTO> setBootstrapList() {
        List<HbBootstrapListDTO> list = new ArrayList<HbBootstrapListDTO>();
        HbBootstrapListDTO dto = new HbBootstrapListDTO();
        dto.setRecid(1);
        dto.setId(1);
        dto.setIaasConfigAlias("aws-config1");
        dto.setDeployStatus("사용안함");
        dto.setDeploymentName("bosh");
        dto.setDirectorName("bosh");
        dto.setIaas("AWS");
        dto.setBoshRelease("bosh-256.tgz");
        dto.setBoshCpiRelease("bosh-aws-cpi-release-14.tgz");
        dto.setSubnetId("subnet-test");
        dto.setSubnetRange("10.0.0.0/24");
        dto.setPublicStaticIp("52.16.23.10");
        dto.setPrivateStaticIp("10.0.20.10");
        dto.setSubnetGateway("10.0.0.1");
        dto.setSubnetDns("8.8.8.8");
        dto.setNtp("1.kr.pool.ntp.org, 0.asia.pool.ntp.org");
        dto.setStemcell("light-bosh-stemcell-3147-aws-xen-ubuntu-trusty-go_agent.tgz");
        dto.setInstanceType("m1.large");
        dto.setBoshPassword("testCloud");
        dto.setDeploymentFile("aws-microbosh-1.yml");
        dto.setDeployLog("test..");
        dto.setHybridBootStrapId("1");
        dto.setHyPriDeployStatus("사용안함");
        dto.setHyPriDeploymentName("bosh");
        dto.setHyPriDirectorName("bosh");
        dto.setHyPriIaas("AWS");
        dto.setHyPriBoshRelease("bosh-256.tgz");
        dto.setHyPriBoshCpiRelease("bosh-aws-cpi-release-14.tgz");
        dto.setHyPriSubnetId("subnet-test");
        dto.setHyPriSubnetRange("10.0.0.0/24");
        dto.setHyPriPublicStaticIp("52.16.23.10");
        dto.setHyPriPrivateStaticIp("10.0.20.10");
        dto.setHyPriSubnetGateway("10.0.0.1");
        dto.setHyPriSubnetDns("8.8.8.8");
        dto.setHyPriNtp("1.kr.pool.ntp.org, 0.asia.pool.ntp.org");
        dto.setHyPriStemcell("light-bosh-stemcell-3147-aws-xen-ubuntu-trusty-go_agent.tgz");
        dto.setHyPriInstanceType("m1.large");
        dto.setHyPriBoshPassword("testCloud");
        dto.setHyPriDeploymentFile("aws-microbosh-1.yml");
        dto.setHyPriDeployLog("test..");
        
        dto.getRecid();
        dto.getId();
        dto.getIaasConfigAlias();
        dto.getDeployStatus();
        dto.getDeploymentName();
        dto.getDirectorName();
        dto.getIaas();
        dto.getBoshRelease();
        dto.getBoshCpiRelease();
        dto.getSubnetId();
        dto.getSubnetRange();
        dto.getPublicStaticIp();
        dto.getPrivateStaticIp();
        dto.getSubnetGateway();
        dto.getSubnetDns();
        dto.getNtp();
        dto.getStemcell();
        dto.getInstanceType();
        dto.getBoshPassword();
        dto.getDeploymentFile();
        dto.getDeployLog();
        dto.getHybridBootStrapId();
        dto.getHyPriDeployStatus();
        dto.getHyPriDeploymentName();
        dto.getHyPriDirectorName();
        dto.getHyPriIaas();
        dto.getHyPriBoshRelease();
        dto.getHyPriBoshCpiRelease();
        dto.getHyPriSubnetId();
        dto.getHyPriSubnetRange();
        dto.getHyPriPublicStaticIp();
        dto.getHyPriPrivateStaticIp();
        dto.getHyPriSubnetGateway();
        dto.getHyPriSubnetDns();
        dto.getHyPriNtp();
        dto.getHyPriStemcell();
        dto.getHyPriInstanceType();
        dto.getHyPriBoshPassword();
        dto.getHyPriDeploymentFile();
        dto.getHyPriDeployLog();
        
        list.add(dto);
        return list;
    }
}
