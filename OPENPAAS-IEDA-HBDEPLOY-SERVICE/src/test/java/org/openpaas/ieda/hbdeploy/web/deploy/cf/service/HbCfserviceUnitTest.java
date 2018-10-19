package org.openpaas.ieda.hbdeploy.web.deploy.cf.service;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
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
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfVO;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HbCfserviceUnitTest extends BaseHbDeployControllerUnitTest {
    
    @InjectMocks private HbCfService mockHbCfService;
    @Mock private HbCfDAO mockHbCfDAO;
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
     * @description : 이종 CF  목록 정보 조회 Unit Test
     * @title : testGetHbCfList
     * @return : void
    *****************************************************************/
    @Test
    public void testGetHbCfList(){
        List<HbCfVO> expectList = setCfConfigList();
        when(mockHbCfDAO.selectHbCfInfoList(anyString())).thenReturn(expectList);
        List<HbCfVO> resultList = mockHbCfService.getCfInfoList("installAble");
        assertEquals(expectList.size(), resultList.size());
        assertEquals(expectList.get(0).getCfConfigName(), resultList.get(0).getCfConfigName());
        assertEquals(expectList.get(0).getIaasType(), resultList.get(0).getIaasType());
        assertEquals(expectList.get(0).getDefaultConfigInfo(), resultList.get(0).getDefaultConfigInfo());
        assertEquals(expectList.get(0).getNetworkConfigInfo(), resultList.get(0).getNetworkConfigInfo());
        assertEquals(expectList.get(0).getInstanceConfigInfo(), resultList.get(0).getInstanceConfigInfo());
        assertEquals(expectList.get(0).getResourceConfigInfo(), resultList.get(0).getResourceConfigInfo());
        assertEquals(expectList.get(0).getId(), resultList.get(0).getId());
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  상세 정보 조회 Unit Test
     * @title : testGetHbCfList
     * @return : void
    *****************************************************************/
    @Test
    public void testGetHbCfInfo(){
        HbCfVO expectVo = setCfConfig();
        when(mockHbCfDAO.selectHbCfInfoById(anyInt())).thenReturn(expectVo);
        HbCfVO resultVo = mockHbCfService.getCfInfo(0);
        assertEquals(expectVo.getCfConfigName(), resultVo.getCfConfigName());
        assertEquals(expectVo.getIaasType(), resultVo.getIaasType());
        assertEquals(expectVo.getDefaultConfigInfo(), resultVo.getDefaultConfigInfo());
        assertEquals(expectVo.getCfConfigName(), resultVo.getCfConfigName());
        assertEquals(expectVo.getNetworkConfigInfo(), resultVo.getNetworkConfigInfo());
        assertEquals(expectVo.getInstanceConfigInfo(), resultVo.getInstanceConfigInfo());
        assertEquals(expectVo.getResourceConfigInfo(), resultVo.getResourceConfigInfo());
        assertEquals(expectVo.getId(), resultVo.getId());
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  상세 정보 조회 Excepton Unit Test
     * @title : testGetHbCfList
     * @return : void
    *****************************************************************/
    @Test(expected=CommonException.class)
    public void testGetHbCfInfoNull(){
        when(mockHbCfDAO.selectHbCfInfoById(anyInt())).thenReturn(null);
        when(mockMessageSource.getMessage(anyString(), anyObject(), anyObject())).thenReturn("null");
        mockHbCfService.getCfInfo(0);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 CF  설치 정보 결과 값 설정
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
        vo.setTaskId(1);
        return vo;
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
        vo.setTaskId(1);
        list.add(vo);
        return list;
    }
    
}
