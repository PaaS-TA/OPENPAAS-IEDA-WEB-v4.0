package org.openpaas.ieda.hbdeploy.web.deploy.cf.service;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.ieda.deploy.web.common.dao.ManifestTemplateVO;
import org.openpaas.ieda.hbdeploy.web.common.base.BaseHbDeployControllerUnitTest;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfDefaultConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfInstanceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfKeyConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfNetworkConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfResourceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfVO;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HbCfDeployAsyncServiceUnitTest extends BaseHbDeployControllerUnitTest {
    
    @InjectMocks HbCfDeployAsynService mockHbCfDeployAsyncService;
    @Mock HbCfDAO mockHbCfDAO;
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 유닛 테스트가 실행되기 전 호출
     * @title : setUp
     * @return : void
    ***************************************************/
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : CF DEPLOYMNET 정보 수정 Unit Test
     * @title : testSaveDeployStatus
     * @return : void
    *****************************************************************/
    @Test
    public void testSaveDeployStatus(){
        HbCfVO vo = setCfConfig();
        mockHbCfDeployAsyncService.saveDeployStatus(vo);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  Manifest Template 값 설정
     * @title : manifestInfo
     * @return : ManifestTemplateVO
    *****************************************************************/
    private ManifestTemplateVO manifestInfo() {
        ManifestTemplateVO vo = new ManifestTemplateVO();
        vo.setCfTempleate("cf.yml");
        vo.setCommonBaseTemplate("cf-.yml");
        vo.setCommonJobTemplate("instance.yml");
        vo.setCommonOptionTemplate("option.yml");
        vo.setDeployType("cf-");
        vo.setIaasPropertyTemplate("iaas.yml");
        vo.setIaasType("Openstack");
        vo.setId(1);
        vo.setInputTemplate("input.yml");
        vo.setOptionEtc("etc.yml");
        vo.setTemplateVersion("2.7.0");
        vo.setOptionNetworkTemplate("network2.yml");
        vo.setOptionResourceTemplate("resource.yml");
        vo.setMetaTemplate("meta.yml");
        vo.setMinReleaseVersion("2.7.0");
        return vo;
    }
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  설치 목록 조회 결과 값 설정
     * @title : setCfConfigList
     * @return : List<HbCfVO>
    *****************************************************************/
    private HbCfVO setCfConfig() {
        HbCfVO vo = new HbCfVO();
        vo.setCfConfigName("cf-config");
        vo.setDefaultConfigInfo("default-config");
        vo.setIaasType("Openstack");
        vo.setResourceConfigInfo("resource-config");
        vo.setDeployStatus("done");
        vo.setNetworkConfigInfo("network-config");
        vo.setId(1);
        vo.setInstanceConfigInfo("instance-config");
        //기본 정보
        HbCfDefaultConfigVO defaultVo = new HbCfDefaultConfigVO();
        defaultVo.setDefaultConfigName("defalut-config");
        defaultVo.setDomain("cf.com");
        defaultVo.setDomainOrganization("paas");
        defaultVo.setIaasType("Openstack");
        defaultVo.setId(1);
        vo.setDefaultConfigVO(defaultVo);
        //인스턴스 정보
        HbCfInstanceConfigVO instanceVo = new HbCfInstanceConfigVO();
        instanceVo.setInstanceConfigName("instance-config");
        instanceVo.setId(1);
        instanceVo.setApi_worker_z1("1");
        instanceVo.setApi_z1("1");
        instanceVo.setBlobstore_z1("1");
        instanceVo.setClock_z1("1");
        instanceVo.setConsul_z1("1");
        instanceVo.setDoppler_z1("1");
        instanceVo.setEtcd_z1("1");
        instanceVo.setLoggregator_z1("1");
        instanceVo.setPostgres_z1("1");
        instanceVo.setUaa_z1("1");
        instanceVo.setRouter_z1("1");
        instanceVo.setNats_z1("1");
        instanceVo.setLoggregator_z1("1");
        instanceVo.setNetworkConfigInfo("network-config");
        instanceVo.setIaasType("Openstack");
        
        instanceVo.setApi_worker_z2("1");
        instanceVo.setApi_z2("1");
        instanceVo.setBlobstore_z2("1");
        instanceVo.setClock_z2("1");
        instanceVo.setConsul_z2("1");
        instanceVo.setDoppler_z2("1");
        instanceVo.setEtcd_z2("1");
        instanceVo.setLoggregator_z2("1");
        instanceVo.setPostgres_z2("1");
        instanceVo.setUaa_z2("1");
        instanceVo.setRouter_z2("1");
        instanceVo.setNats_z2("1");
        instanceVo.setLoggregator_z2("1");
        vo.setInstanceConfigVO(instanceVo);
        
        //network 정보
        List<HbCfNetworkConfigVO> networkList = new ArrayList<HbCfNetworkConfigVO>();
        HbCfNetworkConfigVO networkVo = new HbCfNetworkConfigVO();
        networkVo.setAvailabilityZone("us-west-1a");
        networkVo.setId(1);
        networkVo.setIaasType("Openstack");
        networkVo.setSeq(1);
        networkVo.setSubnetGateway("172.16.100.1");
        networkVo.setSubnetDns("8.8.8.8");
        networkVo.setSubnetRange("10.0.0.0/24");
        networkVo.setSeq(1);
        networkList.add(networkVo);
        vo.setNetworks(networkList);
        
        //인증서 정보
        HbCfKeyConfigVO credsVo = new HbCfKeyConfigVO();
        credsVo.setCity("seoul");
        credsVo.setCompany("paas-ta");
        credsVo.setCountryCode("seoul");
        credsVo.setDomain("cf.com");
        credsVo.setEmailAddress("leedh@cloud4u.co.kr");
        credsVo.setReleaseVersion("2.7.0");
        credsVo.setReleaseName("cf");
        credsVo.setKeyConfigName("credential-config");
        credsVo.setIaasType("Openstack");
        credsVo.setId(1);
        credsVo.setKeyFileName("openstack-key.yml");
        vo.setKeyConfigVO(credsVo);
        
        //리소스 정보
        HbCfResourceConfigVO resourceVo = new HbCfResourceConfigVO();
        resourceVo.setDirectorId("1");
        resourceVo.setIaasType("Openstack");
        resourceVo.setId(1);
        resourceVo.setResourceConfigName("resource-config");
        resourceVo.setStemcellName("openstakc-ubuntu");
        resourceVo.setStemcellVersion("3621.48");
        vo.setResourceConfigVO(resourceVo);
        return vo;
    }
    
}
