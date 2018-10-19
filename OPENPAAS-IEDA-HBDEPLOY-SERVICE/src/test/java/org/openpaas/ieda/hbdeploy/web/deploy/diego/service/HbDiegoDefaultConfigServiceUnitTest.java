package org.openpaas.ieda.hbdeploy.web.deploy.diego.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

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
import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.hbdeploy.web.common.base.BaseHbDeployControllerUnitTest;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoDefaultConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoDefaultConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoDefaultConfigDTO;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HbDiegoDefaultConfigServiceUnitTest extends BaseHbDeployControllerUnitTest{
	
	@InjectMocks private HbDiegoDefaultConfigService mockHbDiegoDefaultConfigService;
    @Mock private MessageSource mockMessageSource;
    @Mock private HbDiegoDefaultConfigDAO mockHbDiegoDefaultConfigDAO;
    
    private Principal principal = null;
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 유닛 테스트가 실행되기 전 호출
     * @title : setUp
     * @return : void
    ***************************************************/
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        principal = getLoggined();
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 DIEGO 기본 정보 목록 조회 Unit Test
     * @title : testGetDefaultConfigInfoList
     * @return : void
    *****************************************************************/
    @Test
    public void testGetDefaultConfigInfoList(){
        List<HbDiegoDefaultConfigVO> expectList = setDiegoDefaultConfigList();
        when(mockHbDiegoDefaultConfigDAO.selectDiegoDefaultInfoList()).thenReturn(expectList);
        List<HbDiegoDefaultConfigVO> resultList = mockHbDiegoDefaultConfigService.getDefaultConfigInfoList();
        assertEquals(expectList.size(), resultList.size());
        assertEquals(expectList.get(0).getDefaultConfigName(), resultList.get(0).getDefaultConfigName());
        assertEquals(expectList.get(0).getIaasType(), resultList.get(0).getIaasType());
        assertEquals(expectList.get(0).getId(), resultList.get(0).getId());
        assertEquals(expectList.get(0).getDeploymentName(), resultList.get(0).getDeploymentName());
        assertEquals(expectList.get(0).getCflinuxfs2rootfsreleaseName(), resultList.get(0).getCflinuxfs2rootfsreleaseName());
        assertEquals(expectList.get(0).getCflinuxfs2rootfsreleaseVersion(), resultList.get(0).getCflinuxfs2rootfsreleaseVersion());
        assertEquals(expectList.get(0).getCfReleaseName(), resultList.get(0).getCfReleaseName());
        assertEquals(expectList.get(0).getCfReleaseVersion(), resultList.get(0).getCfReleaseVersion());
        assertEquals(expectList.get(0).getDefaultConfigName(), resultList.get(0).getDefaultConfigName());
        assertEquals(expectList.get(0).getDiegoReleaseName(), resultList.get(0).getDiegoReleaseName());
        assertEquals(expectList.get(0).getDiegoReleaseVersion(), resultList.get(0).getDiegoReleaseVersion());
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 기본 정보 설정 목록 조회 값 설정
     * @title : setDiegoDefaultConfigList
     * @return : List<HbDiegoDefaultConfigVO>
    *****************************************************************/
	private List<HbDiegoDefaultConfigVO> setDiegoDefaultConfigList() {
        List<HbDiegoDefaultConfigVO> list = new ArrayList<HbDiegoDefaultConfigVO>();
        HbDiegoDefaultConfigVO vo = new HbDiegoDefaultConfigVO();
        vo.setId(1000);
        vo.setIaasType("OPENSTACK");
        vo.setDefaultConfigName("openstack_default");
        vo.setDeploymentName("openstack_diego");
        vo.setDirectorId("1");
        vo.setCfId(1);
        vo.setCfConfigName("cf_config");
        vo.setCfReleaseName("cf-release");
        vo.setCfReleaseVersion("272");
        vo.setDiegoReleaseName("diego-release");
        vo.setDiegoReleaseVersion("1.25.3");
        vo.setCflinuxfs2rootfsreleaseName("cflinuxfs2-rootfs");
        vo.setCflinuxfs2rootfsreleaseVersion("3.0");
        vo.setGardenReleaseName("garden-runc");
        vo.setGardenReleaseVersion("1.9.3");
        vo.setPaastaMonitoringUse("false");
        vo.setCreateUserId("admin");
        list.add(vo);
        return list;
	}
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  기본 정보 삭제 Unit Test
     * @title : testDeleteDefaultConfigInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testDeleteDefaultConfigInfo(){
        HbDiegoDefaultConfigDTO dto = new HbDiegoDefaultConfigDTO();
        dto.setId("1");
        dto.setIaasType("Openstack");
        mockHbDiegoDefaultConfigService.deleteDiegoDefaultConfigInfo(dto, principal);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  기본 정보 삭제 Exception Unit Test
     * @title : testDeleteDefaultConfigInfoEmpty
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testDeleteDefaultConfigInfoEmpty(){
    	HbDiegoDefaultConfigDTO dto = new HbDiegoDefaultConfigDTO();
        dto.setIaasType("Openstack");
        when(mockMessageSource.getMessage(anyString(), anyObject(), anyObject())).thenReturn("empty");
        mockHbDiegoDefaultConfigService.deleteDiegoDefaultConfigInfo(dto, principal);
    }
    
}
