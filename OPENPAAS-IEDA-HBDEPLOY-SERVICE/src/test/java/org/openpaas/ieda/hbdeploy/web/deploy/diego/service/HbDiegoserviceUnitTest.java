package org.openpaas.ieda.hbdeploy.web.deploy.diego.service;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
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
import org.openpaas.ieda.hbdeploy.web.common.base.BaseHbDeployControllerUnitTest;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoDefaultConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoInstanceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoNetworkConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoResourceConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoDTO;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HbDiegoserviceUnitTest extends BaseHbDeployControllerUnitTest {
    
    @InjectMocks private HbDiegoService mockHbDiegoService;
    @Mock private HbDiegoDAO mockHbDiegoDAO;
    @Mock private MessageSource mockMessageSource;
    
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
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 Diego  목록 정보 조회 Unit Test
     * @title : testGetHbDiegoList
     * @return : void
    *****************************************************************/
    @Test
    public void testGetHbDiegoList(){
        List<HbDiegoVO> expectList = setDiegoConfigList();
        when(mockHbDiegoDAO.selectHbDiegoInfoList(anyString())).thenReturn(expectList);
        List<HbDiegoVO> resultList = mockHbDiegoService.getDiegoInfoList("installAble");
        assertEquals(expectList.size(), resultList.size());
        assertEquals(expectList.get(0).getDiegoConfigName(), resultList.get(0).getDiegoConfigName());
        assertEquals(expectList.get(0).getIaasType(), resultList.get(0).getIaasType());
        assertEquals(expectList.get(0).getDefaultConfigInfo(), resultList.get(0).getDefaultConfigInfo());
        assertEquals(expectList.get(0).getNetworkConfigInfo(), resultList.get(0).getNetworkConfigInfo());
        assertEquals(expectList.get(0).getInstanceConfigInfo(), resultList.get(0).getInstanceConfigInfo());
        assertEquals(expectList.get(0).getResourceConfigInfo(), resultList.get(0).getResourceConfigInfo());
        assertEquals(expectList.get(0).getId(), resultList.get(0).getId());
    }

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 Diego 삭제  Unit Test
     * @title : testDeleteDiegoInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testDeleteDiegoInfo(){
        HbDiegoDTO dto = setDiegoInfo();
        mockHbDiegoService.deleteDiegoInfo(dto);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 Diego 삭제 Empty Unit Test
     * @title : testDeleteDiegoInfoIdEmpty
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testDeleteDiegoInfoIdEmpty(){
        HbDiegoDTO dto = new HbDiegoDTO();
        mockHbDiegoService.deleteDiegoInfo(dto);
    }
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 Diego 배포 정보 Replace Unit Test
     * @title : testSetReplaceItems
     * @return : void
    *****************************************************************/
    @Test
    public void testSetReplaceItems(){
    	HbDiegoVO vo = setResultDiegoInfo();
    	mockHbDiegoService.setReplaceItems(vo);
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
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Diego 정보 설정
     * @title : setResultDiegoInfo
     * @return : HbDiegoVO
    *****************************************************************/
    private HbDiegoVO setResultDiegoInfo(){
        HbDiegoVO vo = new HbDiegoVO();
        vo.setDiegoConfigName("diego-config");
        vo.setDefaultConfigInfo("default-config");
        vo.setIaasType("Openstack");
        vo.setResourceConfigInfo("resource-config");
        vo.setDeployStatus("done");
        vo.setNetworkConfigInfo("network-config");
        vo.setId(1);
        vo.setInstanceConfigInfo("instance-config");
        vo.setTaskId(1);
        vo.setDiegoConfigName("diego-config");
        vo.setDefaultConfigInfo("default-config");
        vo.setIaasType("Openstack");
        vo.setResourceConfigInfo("resource-config");
        vo.setDeployStatus("done");
        vo.setNetworkConfigInfo("network-config");
        vo.setId(1);
        vo.setInstanceConfigInfo("instance-config");
        //기본 정보
        HbDiegoDefaultConfigVO defaultVo = new HbDiegoDefaultConfigVO();
        defaultVo.setId(1000);
        defaultVo.setIaasType("OPENSTACK");
        defaultVo.setDefaultConfigName("openstack_default");
        defaultVo.setDeploymentName("openstack_diego");
        defaultVo.setDirectorId("1");
        defaultVo.setCfId(1);
        defaultVo.setCfConfigName("cf_config");
        defaultVo.setCfReleaseName("cf-release");
        defaultVo.setCfReleaseVersion("272");
        defaultVo.setDiegoReleaseName("diego-release");
        defaultVo.setDiegoReleaseVersion("1.25.3");
        defaultVo.setCflinuxfs2rootfsreleaseName("cflinuxfs2-rootfs");
        defaultVo.setCflinuxfs2rootfsreleaseVersion("3.0");
        defaultVo.setGardenReleaseName("garden-runc");
        defaultVo.setGardenReleaseVersion("1.9.3");
        defaultVo.setPaastaMonitoringUse("false");
        vo.setDefaultConfigVO(defaultVo);
        //인스턴스 정보
        HbDiegoInstanceConfigVO instanceVo = new HbDiegoInstanceConfigVO();
        instanceVo.setInstanceConfigName("instance-config");
        instanceVo.setId(1);
        instanceVo.setId(1);
        instanceVo.setIaasType("Openstack");
        instanceVo.setDefaultConfigInfo("default-config");
        instanceVo.setInstanceConfigName("instance");
        instanceVo.setNetworkConfigInfo("network-config");
        instanceVo.setAccess_z1("1");
        instanceVo.setBrain_z1("1");
        instanceVo.setCc_bridge_z1("1");
        instanceVo.setCell_z1("1");
        instanceVo.setDatabase_z1("1");
        instanceVo.setAccess_z2("1");
        instanceVo.setBrain_z2("1");
        instanceVo.setCc_bridge_z2("1");
        instanceVo.setCell_z2("1");
        instanceVo.setDatabase_z2("1");
        
        instanceVo.setAccess_z3("1");
        instanceVo.setBrain_z3("1");
        instanceVo.setCc_bridge_z3("1");
        instanceVo.setCell_z3("1");
        instanceVo.setDatabase_z3("1");
        vo.setInstanceConfigVO(instanceVo);
        
        //network 정보
        List<HbDiegoNetworkConfigVO> networkList = new ArrayList<HbDiegoNetworkConfigVO>();
        HbDiegoNetworkConfigVO networkVo = new HbDiegoNetworkConfigVO();
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
        
        //리소스 정보
        HbDiegoResourceConfigVO resourceVo = new HbDiegoResourceConfigVO();
        resourceVo.setDirectorId("1");
        resourceVo.setIaasType("Openstack");
        resourceVo.setId(1);
        resourceVo.setResourceConfigName("resource-config");
        resourceVo.setStemcellName("openstakc-ubuntu");
        resourceVo.setBoshPassword("admin");
        resourceVo.setStemcellVersion("3621.48");
        vo.setResourceConfigVO(resourceVo);
        return vo;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 Diego  설치 목록 조회 결과 값 설정
     * @title : setDiegoConfigList
     * @return : List<HbDiegoVO>
    *****************************************************************/
    private List<HbDiegoVO> setDiegoConfigList() {
        List<HbDiegoVO> list = new ArrayList<HbDiegoVO>();
        HbDiegoVO vo = new HbDiegoVO();
        vo.setDiegoConfigName("diego-config");
        vo.setDefaultConfigInfo("default-config");
        vo.setIaasType("Openstack");
        vo.setResourceConfigInfo("resource-config");
        vo.setDeployStatus("done");
        vo.setNetworkConfigInfo("network-config");
        vo.setId(1);
        vo.setInstanceConfigInfo("instance-config");
        vo.setTaskId(1);
        vo.setDiegoConfigName("diego-config");
        vo.setDefaultConfigInfo("default-config");
        vo.setIaasType("Openstack");
        vo.setResourceConfigInfo("resource-config");
        vo.setDeployStatus("done");
        vo.setNetworkConfigInfo("network-config");
        vo.setId(1);
        vo.setInstanceConfigInfo("instance-config");
        //기본 정보
        HbDiegoDefaultConfigVO defaultVo = new HbDiegoDefaultConfigVO();
        defaultVo.setId(1000);
        defaultVo.setIaasType("OPENSTACK");
        defaultVo.setDefaultConfigName("openstack_default");
        defaultVo.setDeploymentName("openstack_diego");
        defaultVo.setDirectorId("1");
        defaultVo.setCfId(1);
        defaultVo.setCfConfigName("cf_config");
        defaultVo.setCfReleaseName("cf-release");
        defaultVo.setCfReleaseVersion("272");
        defaultVo.setDiegoReleaseName("diego-release");
        defaultVo.setDiegoReleaseVersion("1.25.3");
        defaultVo.setCflinuxfs2rootfsreleaseName("cflinuxfs2-rootfs");
        defaultVo.setCflinuxfs2rootfsreleaseVersion("3.0");
        defaultVo.setGardenReleaseName("garden-runc");
        defaultVo.setGardenReleaseVersion("1.9.3");
        defaultVo.setPaastaMonitoringUse("false");
        vo.setDefaultConfigVO(defaultVo);
        //인스턴스 정보
        HbDiegoInstanceConfigVO instanceVo = new HbDiegoInstanceConfigVO();
        instanceVo.setInstanceConfigName("instance-config");
        instanceVo.setId(1);
        instanceVo.setId(1);
        instanceVo.setIaasType("Openstack");
        instanceVo.setDefaultConfigInfo("default-config");
        instanceVo.setInstanceConfigName("instance");
        instanceVo.setNetworkConfigInfo("network-config");
        instanceVo.setAccess_z1("1");
        instanceVo.setBrain_z1("1");
        instanceVo.setCc_bridge_z1("1");
        instanceVo.setCell_z1("1");
        instanceVo.setDatabase_z1("1");
        instanceVo.setAccess_z2("1");
        instanceVo.setBrain_z2("1");
        instanceVo.setCc_bridge_z2("1");
        instanceVo.setCell_z2("1");
        instanceVo.setDatabase_z2("1");
        
        instanceVo.setAccess_z3("1");
        instanceVo.setBrain_z3("1");
        instanceVo.setCc_bridge_z3("1");
        instanceVo.setCell_z3("1");
        instanceVo.setDatabase_z3("1");
        vo.setInstanceConfigVO(instanceVo);
        
        //network 정보
        List<HbDiegoNetworkConfigVO> networkList = new ArrayList<HbDiegoNetworkConfigVO>();
        HbDiegoNetworkConfigVO networkVo = new HbDiegoNetworkConfigVO();
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
        
        //리소스 정보
        HbDiegoResourceConfigVO resourceVo = new HbDiegoResourceConfigVO();
        resourceVo.setDirectorId("1");
        resourceVo.setIaasType("Openstack");
        resourceVo.setId(1);
        resourceVo.setResourceConfigName("resource-config");
        resourceVo.setStemcellName("openstakc-ubuntu");
        resourceVo.setStemcellVersion("3621.48");
        vo.setResourceConfigVO(resourceVo);
        list.add(vo);
        return list;
    }
    
}
