package org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.deploy.web.common.dao.CommonDeployDAO;
import org.openpaas.ieda.deploy.web.common.dao.ManifestTemplateVO;
import org.openpaas.ieda.deploy.web.common.service.CommonDeployService;
import org.openpaas.ieda.hbdeploy.web.common.base.BaseHbDeployControllerUnitTest;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dao.HbBootstrapDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dao.HbBootstrapVO;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dto.HbBootStrapDeployDTO;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dto.HbBootstrapListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HbBootstrapServiceUnitTest extends BaseHbDeployControllerUnitTest{
    
    @InjectMocks private HbBootstrapService mockHbBootstrapService;
    @Mock private HbBootstrapDAO mockHbBootstrapDAO;
    @Mock private CommonDeployDAO mockCommonDeployDAO;
    @Mock private CommonDeployService mockCommonDeployService;
    @Mock private MessageSource mockMessageSource;
    final static Logger LOGGER = LoggerFactory.getLogger(HbBootstrapServiceUnitTest.class);
    
    /***************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : 유닛 테스트가 실행되기 전 호출
     * @title : setUp
     * @return : void
    ***************************************************/
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        getLoggined();
    }
    
    
    /***************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : Hybrid Bootstrap 목록 조회 Unit Test
     * @title : testGetBootstrapList
     * @return : void
    ***************************************************/
    @Test
    public void testGetBootstrapList(){
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> Hybrid Bootstrap 목록 조회 Unit Test"); }
        List<HbBootstrapVO> list = setBootstrapList("openstack");
        when(mockHbBootstrapDAO.selectBootstrapList("")).thenReturn(list);
        List<HbBootstrapListDTO> result = mockHbBootstrapService.getHbBootstrapList("");
        for( int i=0; i<result.size(); i++ ){
            assertEquals(list.size(), result.size());
            assertEquals(list.get(i).getId(), result.get(i).getId());
            assertEquals(list.get(i).getHyPriId(), result.get(i).getHyPriId());
            assertEquals(list.get(i).getDeploymentFile(), result.get(i).getDeploymentFile());
            assertEquals(list.get(i).getHyPriDeploymentFile(), result.get(i).getHyPriDeploymentFile());
            assertEquals(list.get(i).getHyPriDeployStatus(), result.get(i).getHyPriDeployStatus());
            assertEquals(list.get(i).getDeployStatus(), result.get(i).getDeployStatus());
            assertEquals(list.get(i).getDeployLog(), result.get(i).getDeployLog());
            assertEquals(list.get(i).getHyPriDeployLog(), result.get(i).getHyPriDeployLog());
        }
        verify(mockHbBootstrapDAO, times(1)).selectBootstrapList("");
        verifyNoMoreInteractions(mockHbBootstrapDAO);
    }
    
    /***************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : Hybrid Bootstrap 정보 상세 조회 Unit Test
     * @title : testGetBootstrapList
     * @return : void
    ***************************************************/
    @Test
    public void testGetBootstrapInfo(){
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> Hybrid Bootstrap 정보 상세 조회 Unit Test"); }
        HbBootstrapVO vo = setBootstrapInfo("openstack");
        when(mockHbBootstrapDAO.selectBootstrapInfo(anyInt(), anyString())).thenReturn(vo);
        HbBootstrapVO result = mockHbBootstrapService.getHbBootstrapInfo(1, "openstack");
        assertEquals(vo.getId(), result.getId());
        assertEquals(vo.getIaasType(), result.getIaasType());
        assertEquals(vo.getDeploymentName(), result.getDeploymentName());
        assertEquals(vo.getDirectorName(), result.getDirectorName());
        assertEquals(vo.getBoshRelease(), result.getBoshRelease());
        assertEquals(vo.getBoshCpiRelease(), result.getBoshCpiRelease());
        assertEquals(vo.getNtp(), result.getNtp());
        assertEquals(vo.getDeployLog(), result.getDeployLog());
        assertEquals(vo.getDeployStatus(), result.getDeployStatus());
        assertEquals(vo.getDeploymentFile(), result.getDeploymentFile());
        assertEquals(vo.getIaasConfig().getIaasConfigAlias(), result.getIaasConfig().getIaasConfigAlias());
        assertEquals(vo.getSubnetId(), result.getSubnetId());
        assertEquals(vo.getPublicStaticIp(), result.getPublicStaticIp());
        assertEquals(vo.getPrivateStaticIp(), result.getPrivateStaticIp());
        assertEquals(vo.getSubnetRange(), result.getSubnetRange());
        assertEquals(vo.getSubnetGateway(), result.getSubnetGateway());
        assertEquals(vo.getSubnetDns(), result.getSubnetDns());
        assertEquals(vo.getStemcell(), result.getStemcell());
        assertEquals(vo.getBoshPassword(), result.getBoshPassword());
        assertEquals(vo.getCloudInstanceType(), result.getCloudInstanceType());
        assertEquals(vo.getHyPriBoshCpiRelease(), result.getHyPriBoshCpiRelease());
        assertEquals(vo.getHyPriDeployLog(), result.getHyPriDeployLog());
        assertEquals(vo.getHyPriCredentialKeyName(), result.getHyPriCredentialKeyName());
        assertEquals(vo.getHyPriNetworkName(), result.getHyPriNetworkName());
        assertEquals(vo.getHyPriInfluxdbIp(), result.getHyPriInfluxdbIp());
        assertEquals(vo.getHyPriDeploymentFile(), result.getHyPriDeploymentFile());
        verify(mockHbBootstrapDAO, times(1)).selectBootstrapInfo(1, "openstack");
        verifyNoMoreInteractions(mockHbBootstrapDAO);
    }
    
    /***************************************************
     * @project : Paas 이종 클라우드 플랫폼 설치 자동화
     * @description : Hybrid Bootstrap 정보 상세 조회 Null Point Unit Test
     * @title : testGetBootstrapInfoNullPoint
     * @return : void
    ***************************************************/
    @Test(expected=CommonException.class)
    public void testGetBootstrapInfoNullPoint(){
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> Hybrid Bootstrap 정보 상세 조회 Unit Test"); }
        when(mockHbBootstrapDAO.selectBootstrapInfo(anyInt(), anyString())).thenReturn(null);
        when(mockMessageSource.getMessage(any(), any(), any())).thenReturn("");
        mockHbBootstrapService.getHbBootstrapInfo(1, "openstack");
        verify(mockHbBootstrapDAO, times(1)).selectBootstrapInfo(1, "openstack");
        verifyNoMoreInteractions(mockHbBootstrapDAO);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 배포 파일 생성 (Bootstrap 상세 정보 null 케이스 ) Unit Test
     * @title : testCreateSettingFileBootstrapNullCase
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testCreateSettingFileBootstrapNullCase(){
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> 배포 파일 생성 (Bootstrap 상세 정보 null 케이스 ) Unit Test"); }
        when(mockHbBootstrapDAO.selectBootstrapInfo(anyInt(), anyString())).thenReturn(null);
        when(mockMessageSource.getMessage(any(), any(), any())).thenReturn("");
        mockHbBootstrapService.createSettingFile(1, "openstack");
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 배포 파일 생성 (Bootstrap Manifest Template null 케이스) Unit Test
     * @title : testCreateSettingFileManifestTemplateNullCase
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testCreateSettingFileManifestTemplateNullCase(){
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> 배포 파일 생성 (Bootstrap Manifest Template null 케이스) Unit Test"); }
        HbBootstrapVO vo = setBootstrapInfo("Openstack");//data 상세 조회
        when(mockHbBootstrapDAO.selectBootstrapInfo(anyInt(), anyString())).thenReturn(vo);
        when(mockMessageSource.getMessage(any(), any(), any())).thenReturn("");
        when(mockCommonDeployDAO.selectManifetTemplate(anyString(), anyString(), anyString(), anyString())).thenReturn(null);
        mockHbBootstrapService.createSettingFile(1, "openstack");
    }
    

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Manifest Template 디렉토리 정보 설정(BOSH RELEASE 256) Unit Test
     * @title : testSetOptionManifestTemplateInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testSetOptionManifestTemplateInfoFrom256Release(){
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> Manifest Template 디렉토리 정보 설정(BOSH RELEASE 256) Unit Test"); }
        ManifestTemplateVO  manifestTemplate = setManifestTemplate("256", "Openstack");
        mockHbBootstrapService.setOptionManifestTemplateInfo(manifestTemplate, "openstack", "v2");
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Manifest Template 디렉토리 정보 설정(BOSH RELEASE 233)
     * @title : testSetOptionManifestTemplateInfoFrom233Release
     * @return : void
    *****************************************************************/
    @Test
    public void testSetOptionManifestTemplateInfoFrom233Release(){
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> Manifest Template 디렉토리 정보 설정(BOSH RELEASE 233)"); }
        ManifestTemplateVO  manifestTemplate = setManifestTemplate("233", "Openstack");
        mockHbBootstrapService.setOptionManifestTemplateInfo(manifestTemplate, "openstack", "v3");
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 단순 레코드 삭제 Unit Test
     * @title : testDeleteBootstrapInfo
     * @return : void
    *****************************************************************/
   @Test
    public void testDeleteBootstrapInfo(){
       if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> 단순 레코드 삭제 Unit Test )"); }
       HbBootStrapDeployDTO.Delete dto = setDeleteInfo();
        mockHbBootstrapService.deleteBootstrapInfo(dto);
        doNothing().when(mockHbBootstrapDAO).deleteBootstrapInfo(dto);
        verify(mockHbBootstrapDAO, times(1)).deleteBootstrapInfo(dto);
        verifyNoMoreInteractions(mockHbBootstrapDAO);
    }
   
   
   /****************************************************************
    * @project : Paas 이종 플랫폼 설치 자동화
    * @description : 단순 레코드 삭제 데이터가 존재 하지 않을 경우 Unit Test
    * @title : testDeleteBootstrapInfoIsEmpty
    * @return : void
   *****************************************************************/
    @Test(expected=CommonException.class)
    public void testDeleteBootstrapInfoIsEmpty(){
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> 단순 레코드 삭제 데이터가 존재 하지 않을 경우 Unit Test )"); }
        HbBootStrapDeployDTO.Delete dto = new HbBootStrapDeployDTO.Delete();
        dto.setBootstrapType("openstack");
        doNothing().when(mockHbBootstrapDAO).deleteBootstrapInfo(dto);
        when(mockMessageSource.getMessage(any(), any(), any())).thenReturn("");
        mockHbBootstrapService.deleteBootstrapInfo(dto);
        
        verify(mockHbBootstrapDAO, times(1)).deleteBootstrapInfo(dto);
        verifyNoMoreInteractions(mockHbBootstrapDAO);
    }
   
   /****************************************************************
    * @project : Paas 이종 플랫폼 설치 자동화
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
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Manifest Template 디렉토리 정보 Unit Test
     * @title : testSetOptionManifestTemplateInfoFromEmptyValue
     * @return : void
    *****************************************************************/
    @Test
    public void testSetOptionManifestTemplateInfoFromEmptyValue(){
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> Manifest Template 디렉토리 정보 Unit Test )"); }
        ManifestTemplateVO  manifestTemplate = new ManifestTemplateVO();
        ManifestTemplateVO result = mockHbBootstrapService.setOptionManifestTemplateInfo(manifestTemplate, "openstack", "v2");
        assertEquals(result.getCommonBaseTemplate(), "");
        assertEquals(result.getCommonJobTemplate(), "");
        assertEquals(result.getIaasPropertyTemplate(), "");
        assertEquals(result.getMetaTemplate(), "");
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Hybrid Bootstrap 설치 정보 조회 Unit Test
     * @title : testGetBootstrapInstallInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testGetBootstrapInstallInfo(){
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> Hybrid Bootstrap 설치 정보 조회 Unit Test)"); }
        HbBootstrapVO expectVo = setBootstrapInfo("openstack");
        when(mockHbBootstrapDAO.selectInstallBootstrapInfo(anyInt(), anyInt())).thenReturn(expectVo);
        HbBootstrapVO resultVo = mockHbBootstrapService.getHbBootstrapInstallInfo("1", "1");
        assertEquals(expectVo.getBootstrapType(), resultVo.getBootstrapType());
        assertEquals(expectVo.getBoshPassword(), resultVo.getBoshPassword());
        assertEquals(expectVo.getBoshCpiRelease(), resultVo.getBoshCpiRelease());
        assertEquals(expectVo.getBoshRelease(), resultVo.getBoshRelease());
        assertEquals(expectVo.getCloudInstanceType(), resultVo.getCloudInstanceType());
        assertEquals(expectVo.getCreateUserId(), resultVo.getCreateUserId());
        assertEquals(expectVo.getCredentialKeyName(), resultVo.getCredentialKeyName());
        assertEquals(expectVo.getDeployLog(), resultVo.getDeployLog());
        assertEquals(expectVo.getDeploymentFile(), resultVo.getDeploymentFile());
        assertEquals(expectVo.getDeployStatus(), resultVo.getDeployStatus());
        assertEquals(expectVo.getSubnetRange(), resultVo.getSubnetRange());
        assertEquals(expectVo.getSubnetId(), resultVo.getSubnetId());
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Hybrid Bootstrap 설치 정보 조회 expect Null point Unit Test
     * @title : testGetBootstrapInstallInfoExpectNull
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testGetBootstrapInstallInfoExpectNull(){
        if (LOGGER.isInfoEnabled()) { LOGGER.info("====================================> Hybrid Bootstrap 설치 정보 조회 expect Null point Unit Test)"); }
        when(mockMessageSource.getMessage(any(), any(), any())).thenReturn("");
        when(mockHbBootstrapDAO.selectInstallBootstrapInfo(anyInt(), anyInt())).thenReturn(null);
        mockHbBootstrapService.getHbBootstrapInstallInfo("1", "1");
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Bootstrap 배포 파일 정보 설정
     * @title : setManifestTemplate
     * @return : ManifestTemplateVO
    *****************************************************************/
    public ManifestTemplateVO setManifestTemplate(String version, String iaas){
        ManifestTemplateVO vo = new ManifestTemplateVO();
        
        vo.setId(1);
        vo.setDeployType("BOOTSTRAP");
        vo.setReleaseType("bosh");
        vo.setTemplateVersion(version);
        vo.setMinReleaseVersion(version);
        vo.setCommonBaseTemplate("generic_manifest_mask.yml");
        
        if( iaas.equalsIgnoreCase("openstack") ){
            vo.setIaasType("openstack");
            if( version.equals("256") ){
                vo.setCommonJobTemplate("bootstrap.yml");
                vo.setMetaTemplate("bootstrap_openstack_stub_256.yml");
                vo.setInputTemplate("bootstrap_openstack_inputs.yml");
            }else{
                vo.setIaasPropertyTemplate("openstack-microbosh-stub.yml");
                vo.setInputTemplate("openstack-microbosh-param.yml");
                vo.setCommonOptionTemplate("bootstrap.yml");
            }
        }else{
            vo.setIaasType("Google");
            vo.setCommonJobTemplate("bootstrap.yml");
            
            vo.setMetaTemplate("bootstrap_google_stub_256.yml");
            vo.setInputTemplate("bootstrap_google_inputs.yml");
        }
        return vo;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Bootstrap 상세 정보 설정
     * @title : setBootstrapInfo
     * @return : BootstrapVO
    *****************************************************************/
    private HbBootstrapVO setBootstrapInfo(String iaasType){
        HbBootstrapVO vo = new HbBootstrapVO();
        vo.setId(1);
        vo.setIaasType(iaasType);
        vo.setIaasConfigId(1);
        vo.setBootstrapType("private");
        vo.getIaasAccount().put("commonAccessUser", "bosh-test");
        vo.getIaasAccount().put("commonAccessSecret", "secret-test");
        vo.getIaasAccount().put("commonTenant", "tenent");
        if( iaasType.equals("Openstack") ){
            vo.getIaasAccount().put("commonAccessEndpoint", "http://10.10.10.1:5000/v2.0");
            vo.getIaasConfig().setCommonKeypairPath("bosh-key.pem");
            vo.getIaasConfig().setIaasConfigAlias("openstack-config1");
            vo.getIaasConfig().setAccountName("openstack_v2");
        }else{
            vo.getIaasAccount().put("commonAccessEndpoint", "");
            vo.getIaasConfig().setCommonKeypairPath("bosh-key");
            vo.getIaasConfig().setIaasConfigAlias("google-config1");
            vo.getIaasConfig().setAccountName("google_c1");
            vo.getIaasAccount().put("googleJsonKey", "googleJsonKey");
        }
        vo.getIaasConfig().setAccountId(1);
        vo.getIaasConfig().setCommonKeypairName("bosh-key");
        vo.getIaasConfig().setCommonSecurityGroup("bosh-security");
        vo.setDeploymentName("bosh");
        vo.setDirectorName("test-bosh");
        vo.setBoshRelease("bosh-257.tgz");
        vo.setBoshCpiRelease("bosh-"+iaasType+"-cpi-release-14.tgz");
        vo.setEnableSnapshots("true");
        vo.setSnapshotSchedule("0 0 7 * * * schedule");
        vo.setNtp("1.kr.pool.ntp.org, 0.asia.pool.ntp.org");
        vo.setPaastaMonitoringUse("false");
        vo.setSubnetId("subnet-12345");
        vo.setPrivateStaticIp("10.0.100.11");
        vo.setPublicStaticIp("10.0.20.6");
        vo.setSubnetRange("10.0.20.0/24");
        vo.setSubnetGateway("10.0.20.1");
        vo.setSubnetDns("8.8.8.8");
        vo.setStemcell("bosh-stemcell-3421-"+iaasType+"-kvm-ubuntu-trusty-go_agent.tgz");
        vo.setCloudInstanceType("m1.large");
        vo.setBoshPassword("1234");
        vo.setDeploymentFile(iaasType+"-microbosh-test-1.yml");
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
        return vo;
    }

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Bootstrap 목록 조회 결과 값 설정
     * @title : setBootstrapList
     * @return : List<BootstrapVO>
    *****************************************************************/
    public List<HbBootstrapVO> setBootstrapList(String iaasType){
        List<HbBootstrapVO> list = new ArrayList<HbBootstrapVO>();
        HbBootstrapVO vo = new HbBootstrapVO();
        vo.setId(1);
        vo.setIaasType(iaasType);
        vo.setIaasConfigId(1);
        vo.getIaasConfig().setAccountId(1);
        vo.getIaasConfig().setAccountName("bosh");
        vo.getIaasConfig().setCommonKeypairName("bosh-key");
        vo.getIaasConfig().setCommonKeypairPath("bosh-key.pem");
        vo.getIaasConfig().setCommonSecurityGroup("bosh-security");
        vo.getIaasConfig().setIaasConfigAlias(iaasType+"-config1");
        vo.setDeploymentName("bosh");
        vo.setDirectorName("test-bosh");
        vo.setBoshRelease("bosh-257.tgz");
        vo.setBoshCpiRelease("bosh-"+iaasType+"-cpi-release-14.tgz");
        vo.setEnableSnapshots("true");
        vo.setSnapshotSchedule("0 0 7 * * * schedule");
        vo.setNtp("1.kr.pool.ntp.org, 0.asia.pool.ntp.org");
        if(  iaasType.equals("Google") ){
            vo.setNetworkName("bosh");
            vo.setSubnetId("subnet");
        }
        vo.setSubnetId("subnet-12345");
        vo.setPrivateStaticIp("10.0.100.11");
        vo.setPublicStaticIp("10.0.20.6");
        vo.setSubnetRange("10.0.20.0/24");
        vo.setSubnetGateway("10.0.20.1");
        vo.setSubnetDns("8.8.8.8");
        vo.setStemcell("bosh-stemcell-3421-"+iaasType+"-kvm-ubuntu-trusty-go_agent.tgz");
        vo.setCloudInstanceType("m1.large");
        vo.setBoshPassword("1234");
        vo.setDeploymentFile(iaasType+"-microbosh-test-1.yml");
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
        list.add(vo);
        return list;
    }
}
