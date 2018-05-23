package org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Date;

import javax.ws.rs.core.Application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.hbdeploy.web.common.base.BaseHbDeployControllerUnitTest;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dao.HbBootstrapDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dao.HbBootstrapVO;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dto.HbBootStrapDeployDTO;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HbBootstrapSaveServiceUnitTest extends BaseHbDeployControllerUnitTest{
    
    @InjectMocks private HbBootstrapSaveService mockHbBootstrapSaveService;
    @Mock private MessageSource mockMessageSource;
    @Mock private HbBootstrapDAO mockHbBootstrapDAO;
    private Principal principal;
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 유닛 테스트가 실행되기 전 호출
     * @title : setUp
     * @return : void
    ***************************************************/
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        principal=getLoggined();
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Google 환경 설정 정보 저장
     * @title : testSaveIaasConfigInfoFromInsertCase
     * @return : void
    *****************************************************************/
    @Test
    public void testSaveIaasConfigInfoFromInsertCase(){
        HbBootStrapDeployDTO.IaasConfig dto = setIaasConfigInfo("update");
        HbBootstrapVO vo = setBootstrapVOInfoFromGoogle("insert");
        when(mockHbBootstrapDAO.selectBootstrapInfo(anyInt(), anyString())).thenReturn(vo);
        
        when(mockHbBootstrapDAO.insertBootStrapInfo(vo)).thenReturn(1);
        
        HbBootstrapVO result = mockHbBootstrapSaveService.saveIaasConfigInfo(dto, principal);
        assertEquals(result.getIaasType(), vo.getIaasType());
        assertEquals(result.getCreateUserId(), result.getCreateUserId());
        assertEquals(result.getUpdateUserId(), result.getUpdateUserId());
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 기본 정보 저장
     * @title : testSaveDefaultInfoFromUpdate
     * @return : void
    *****************************************************************/
    @Test
    public void testSaveDefaultInfoFromUpdate(){
        HbBootStrapDeployDTO.Default dto = setDefaultInfo();
        HbBootstrapVO vo = setBootstrapVOInfoFromGoogle("update");
        when(mockHbBootstrapDAO.selectBootstrapInfo(anyInt(), anyString())).thenReturn(vo);
        when(mockHbBootstrapDAO.updateBootStrapInfo(vo)).thenReturn(1);
        HbBootstrapVO result = mockHbBootstrapSaveService.saveDefaultInfo(dto, principal);
        assertEquals(result.getDeploymentName(), vo.getDeploymentName());
        assertEquals(result.getDirectorName(), vo.getDirectorName());
        assertEquals(result.getNtp(), vo.getNtp());
        assertEquals(result.getBoshRelease(), vo.getBoshRelease());
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 기본 정보 저장 id null 오류
     * @title : testSaveDefaultInfoFromIdEmptyException
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testSaveDefaultInfoFromIdEmptyException(){
        HbBootStrapDeployDTO.Default dto = setDefaultInfo();
        HbBootstrapVO vo = setBootstrapVOInfoFromGoogle("update");
        dto.setId(null);
        when(mockHbBootstrapDAO.selectBootstrapInfo(anyInt(), anyString())).thenReturn(vo);
        mockHbBootstrapSaveService.saveDefaultInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 기본 정보 저장 (Null 오류)
     * @title : testSaveDefaultInfoFromNullException
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testSaveDefaultInfoFromNullException(){
        HbBootStrapDeployDTO.Default dto = setDefaultInfo();
        when(mockHbBootstrapDAO.selectBootstrapInfo(anyInt(), anyString())).thenReturn(null);
        mockHbBootstrapSaveService.saveDefaultInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 네트워크 정보 저장
     * @title : testSaveNetworkInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testSaveNetworkInfo(){
    	HbBootStrapDeployDTO.Network dto = setNetworkInfo();
        HbBootstrapVO vo = setBootstrapVOInfoFromGoogle("update");
        when(mockHbBootstrapDAO.selectBootstrapInfo(anyInt(), anyString())).thenReturn(vo);
        when(mockHbBootstrapDAO.updateBootStrapInfo(vo)).thenReturn(1);
        mockHbBootstrapSaveService.saveNetworkInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 네트워크 정보 저장( 조회 null 오류) 
     * @title : testSaveNetworkInfoFromNullException
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testSaveNetworkInfoFromNullException(){
        HbBootStrapDeployDTO.Network dto = setNetworkInfo();
        when(mockHbBootstrapDAO.selectBootstrapInfo(anyInt(), anyString())).thenReturn(null);
        when(mockMessageSource.getMessage(any(), any(), any())).thenReturn("");
        mockHbBootstrapSaveService.saveNetworkInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 네트워크 정보 저장( 조회 empty 오류) 
     * @title : testSaveNetworkInfoFromEmptyId
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testSaveNetworkInfoFromEmptyId(){
        HbBootStrapDeployDTO.Network dto = setNetworkInfo();
        when(mockHbBootstrapDAO.selectBootstrapInfo(anyInt(), anyString())).thenReturn(null);
        when(mockMessageSource.getMessage(any(), any(), any())).thenReturn("");
        dto.setId(null);
        mockHbBootstrapSaveService.saveNetworkInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 리소스 정보 저장
     * @title : testSaveResourcesInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testSaveResourcesInfo(){
        HbBootStrapDeployDTO.Resource dto = setResourceInfo();
        HbBootstrapVO vo = setBootstrapVOInfoFromGoogle("update");
        vo.setDeploymentFile(null);
        when(mockHbBootstrapDAO.selectBootstrapInfo(anyInt(), anyString())).thenReturn(vo);
        when(mockHbBootstrapDAO.updateBootStrapInfo(vo)).thenReturn(1);
        HbBootstrapVO result =  mockHbBootstrapSaveService.saveResourceInfo(dto, principal);
        assertEquals(result.getStemcell(), vo.getStemcell());
        assertEquals(result.getCloudInstanceType(), vo.getCloudInstanceType());
        assertEquals(result.getBoshPassword(), vo.getBoshPassword());
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 리소스 정보 저장(Id null 오류) 
     * @title : testSaveResourcesInfoFromIdNull
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testSaveResourcesInfoFromIdNull(){
        HbBootStrapDeployDTO.Resource dto = setResourceInfo();
        dto.setId(null);
        mockHbBootstrapSaveService.saveResourceInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 리소스 정보 저장 (조회 null 오류)
     * @title : testSaveResourcesInfoFromNullException
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testSaveResourcesInfoFromNullException(){
        HbBootStrapDeployDTO.Resource dto = setResourceInfo();
        when(mockMessageSource.getMessage(any(), any(), any())).thenReturn("");
        when(mockHbBootstrapDAO.selectBootstrapInfo(anyInt(), anyString())).thenReturn(null);
        mockHbBootstrapSaveService.saveResourceInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 배포 파일명 생성 오류
     * @title : testMakeDeploymentNameNullException
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testMakeDeploymentNameNullException(){
        HbBootstrapVO vo = setBootstrapVOInfoFromGoogle("update");
        vo.setIaasType(null);
        vo.setId(null);
        when(mockMessageSource.getMessage(any(), any(), any())).thenReturn("");
        mockHbBootstrapSaveService.makeDeploymentName(vo);
    }
    
    
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 리소스 정보 저장
     * @title : setResourceInfo
     * @return : BootStrapDeployDTO.Resource
    *****************************************************************/
    public HbBootStrapDeployDTO.Resource setResourceInfo(){
    	HbBootStrapDeployDTO.Resource dto = new HbBootStrapDeployDTO.Resource();
        dto.setId("1");
        dto.setStemcell("light-bosh-stemcell-3363-google-kvm-ubuntu-trusty-go_agent.tgz");
        dto.setCloudInstanceType("n1-standard-2");
        dto.setBoshPassword("cloudc0w");
        dto.setResourcePoolCpu(null);
        dto.setResourcePoolRam(null);
        dto.setResourcePoolDisk(null);
        dto.setIaasType("openstack");
        dto.getId();
        dto.getStemcell();
        dto.getCloudInstanceType();
        dto.getBoshPassword();
        dto.getResourcePoolCpu();
        dto.getResourcePoolRam();
        dto.getResourcePoolDisk();
        
        return dto;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 네트워크 정보 설정
     * @title : setNetworkInfo
     * @return : BootStrapDeployDTO.Network
    *****************************************************************/
    public HbBootStrapDeployDTO.Network setNetworkInfo(){
    	HbBootStrapDeployDTO.Network dto = new HbBootStrapDeployDTO.Network ();
        dto.setId("1");
        dto.setSubnetId("sbosh");
        dto.setNetworkName("bosh");
        dto.setPrivateStaticIp("192.168.40.10");
        dto.setSubnetRange("192.168.40.0/24");
        dto.setSubnetGateway("192.168.40.1");
        dto.setSubnetDns("8.8.8.8");
        dto.setPublicStaticIp("35.200.83.25");
        dto.setPublicSubnetId(null);
        dto.setPublicSubnetRange(null);
        dto.setPublicSubnetGateway(null);
        dto.setPublicSubnetDns(null);
        dto.setIaasType("openstack");
        dto.getId();
        dto.getSubnetId();
        dto.getNetworkName();
        dto.getPrivateStaticIp();
        dto.getSubnetRange();
        dto.getSubnetGateway();
        dto.getPublicSubnetDns();
        dto.getPublicStaticIp();
        dto.getPublicSubnetDns();
        dto.getPublicSubnetId();
        dto.getPublicSubnetGateway();
        
        
        return dto;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 기본 정보 설정 
     * @title : setDefaultInfo
     * @return : BootStrapDeployDTO.Default
    *****************************************************************/
    public HbBootStrapDeployDTO.Default setDefaultInfo(){
    	HbBootStrapDeployDTO.Default dto = new HbBootStrapDeployDTO.Default();
        dto.setId("1");
        dto.setIaasConfigId("1");
        dto.setDeploymentName("bosh");
        dto.setDirectorName("bosh");
        dto.setBoshRelease("bosh-release");
        dto.setNtp("1.kr.pool.ntp.org, 0.asia.pool.ntp.org");
        dto.setBoshCpiRelease("bosh-cpi-release");
        dto.setEnableSnapshots("false");
        dto.setSnapshotSchedule("");
        dto.setIaasType("openstack");
        dto.getId();
        dto.getIaasConfigId();
        dto.getDeploymentName();
        dto.getDirectorName();
        dto.getBoshRelease();
        dto.getBoshCpiRelease();
        dto.getNtp();
        dto.getEnableSnapshots();
        dto.getSnapshotSchedule();
        
        return dto;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 환경 설정 정보 설정
     * @title : setIaasConfigInfo
     * @return : BootStrapDeployDTO.IaasConfig
    *****************************************************************/
    public HbBootStrapDeployDTO.IaasConfig setIaasConfigInfo(String type){
    	HbBootStrapDeployDTO.IaasConfig dto = new HbBootStrapDeployDTO.IaasConfig();
        if( type.equalsIgnoreCase("update") ){
            dto.setId("1");
        }
        dto.setIaasType("Google");
        dto.setIaasConfigId("1");
        dto.setTestFlag("Y");
        dto.setIaasType("openstack");
        dto.getId();
        dto.getIaasType();
        dto.getIaasConfigId();
        dto.getTestFlag();
        
        return dto;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : BootstrapVO 정보 설정 (GOOGLE)
     * @title : setBootstrapVOInfoFromGoogle
     * @return : BootstrapVO
    *****************************************************************/
    public HbBootstrapVO setBootstrapVOInfoFromGoogle(String type){
    	HbBootstrapVO vo = new HbBootstrapVO();
        
        if( type.equalsIgnoreCase("update") ){
            vo.setId(1);
            vo.getIaasConfig().setDeployStatus("사용중");
        }
        vo.setIaasType("Google");
        vo.setIaasConfigId(1);
        vo.setId(1);
        vo.setIaasType("openstack");
        vo.setCreateDate(new Date());
        vo.setUpdateDate(new Date());
        vo.setUpdateUserId(principal.getName());
        vo.setCreateUserId(principal.getName());
        vo.setTestFlag("Y");
        
        //Iaas Config Info
//        vo.setIaasConfig(new IaasConfigMgntVO());
//           vo.setIaasAccount(new HashMap<String, Object>());
        vo.getIaasConfig().setId(1);
        vo.getIaasConfig().setAccountId(1);
        vo.getIaasConfig().setAccountName("google-account");
        vo.getIaasConfig().setIaasType("Google");
        vo.getIaasConfig().setIaasConfigAlias("google-config");
        vo.getIaasConfig().setCommonSecurityGroup("internet");
        vo.getIaasConfig().setCommonKeypairPath("google-key");
        vo.getIaasConfig().setCommonAvailabilityZone("asia-northeast1-a");
        vo.getIaasConfig().setCommonRegion(null);
        vo.getIaasConfig().setCommonKeypairName(null);
        vo.getIaasConfig().setVsphereVcentDataCenterName(null);
        vo.getIaasConfig().setVsphereVcenterCluster(null);
        vo.getIaasConfig().setVsphereVcenterDatastore(null);
        vo.getIaasConfig().setVsphereVcenterDiskPath(null);
        vo.getIaasConfig().setVsphereVcenterPersistentDatastore(null);
        vo.getIaasConfig().setVsphereVcenterTemplateFolder(null);
        vo.getIaasConfig().setVsphereVcenterVmFolder(null);
        
        //기본 정보 설정
        vo.setDeploymentName("bosh");
        vo.setDirectorName("bosh");
        vo.setBoshRelease("bosh-release");
        vo.setBoshCpiRelease("bosh-cpi-release");
        vo.setSnapshotSchedule(null);
        vo.setEnableSnapshots("false");
        vo.setNtp("1.kr.pool.ntp.org, 0.asia.pool.ntp.org");
        
        //네트워크 정보 설정
        vo.setSubnetId("sbosh");
        vo.setNetworkName("bosh");
        vo.setPrivateStaticIp("192.168.40.10");
        vo.setSubnetRange("192.168.40.0/24");
        vo.setSubnetGateway("192.168.40.1");
        vo.setSubnetDns("8.8.8.8");
        vo.setPublicStaticIp("35.200.83.25");
        vo.setPublicSubnetId(null);
        vo.setPublicSubnetRange(null);
        vo.setPublicSubnetGateway(null);
        vo.setPublicSubnetDns(null);
        
        //리소스 정보 설정
        vo.setStemcell("light-bosh-stemcell-3363-google-kvm-ubuntu-trusty-go_agent.tgz");
        vo.setCloudInstanceType("n1-standard-2");
        vo.setBoshPassword("cloudc0w");
        vo.setResourcePoolCpu(null);
        vo.setResourcePoolRam(null);
        vo.setResourcePoolDisk(null);
        
        vo.setDeploymentFile("google-microbosh-1.yml");
        vo.setDeployStatus(null);
        vo.setDeployLog("test...");
        
        return vo;
    }
}
