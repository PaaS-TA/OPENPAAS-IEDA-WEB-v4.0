package org.openpaas.ieda.hbdeploy.web.deploy.cf.service;
import javax.ws.rs.core.Application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.ieda.hbdeploy.web.common.base.BaseHbDeployControllerUnitTest;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfVO;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HbCfDeleteAsyncServiceUnitTest extends BaseHbDeployControllerUnitTest {
	
    @InjectMocks HbCfDeleteAsyncService mockHbCfDeleteAsyncService;
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
     * @description : CF DEPLOYMNET 정보 삭제 Unit Test
     * @title : testDeleteCfInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testDeleteCfInfo(){
    	HbCfVO vo = setCfConfig();
    	mockHbCfDeleteAsyncService.deleteCfInfo(vo);
    }
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : CF DEPLOYMNET 배포 상태 수정 Unit Test
     * @title : testDeleteCfInfo
     * @return : void
    *****************************************************************/
    @Test
    public void testSaveDeployStatus(){
    	HbCfVO vo = setCfConfig();
    	mockHbCfDeleteAsyncService.saveDeployStatus(vo);
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
        return vo;
    }
    
    
    
}
