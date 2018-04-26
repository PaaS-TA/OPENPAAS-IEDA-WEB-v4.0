package org.openpaas.ieda.deploy.web.information.manifest;

import static org.junit.Assert.assertEquals;
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
import org.openpaas.ieda.deploy.web.common.base.BaseDeployControllerUnitTest;
import org.openpaas.ieda.deploy.web.config.setting.service.DirectorConfigService;
import org.openpaas.ieda.deploy.web.information.manifest.dao.ManifestDAO;
import org.openpaas.ieda.deploy.web.information.manifest.dao.ManifestVO;
import org.openpaas.ieda.deploy.web.information.manifest.service.ManifestService;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration

public class ManifestMgntServiceUnitTest extends BaseDeployControllerUnitTest{
    
    @InjectMocks 
    private ManifestService mockManifestService;
    @Mock 
    private DirectorConfigService mockDirectorConfigService;
    private ManifestDAO mockManifestDao;
    @Mock
    private MessageSource mockMessageSource;
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description :  
     * @title : setUp
     * @return : void
    *****************************************************************/
    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        getLoggined();
    }
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description :  
     * @title : testGetManifestList
     * @return : void
    *****************************************************************/
    @Test(expected=NullPointerException.class)
    public void testGetManifestList() {
        List<ManifestVO> expectList = setDefualtManifestVO();
        when(mockManifestDao.selectManifestList()).thenReturn(expectList);
        List<ManifestVO> resultList = mockManifestService.getManifestList();
        assertEquals(expectList.get(0).getId(), resultList.get(0).getId());
        assertEquals(expectList.get(0).getRecid(), resultList.get(0).getRecid());
        assertEquals(expectList.get(0).getIaas(), resultList.get(0).getIaas());
        assertEquals(expectList.get(0).getFileName(), resultList.get(0).getFileName());
        assertEquals(expectList.get(0).getDescription(), resultList.get(0).getDescription());
        assertEquals(expectList.get(0).getDeploymentName(), resultList.get(0).getDeploymentName());
        assertEquals(expectList.get(0).getPath(), resultList.get(0).getPath());
        assertEquals(expectList.get(0).getDeployStatus(), resultList.get(0).getDeployStatus());
        assertEquals(expectList.get(0).getCreateUserId(), resultList.get(0).getCreateUserId());
        assertEquals(expectList.get(0).getUpdateUserId(), resultList.get(0).getUpdateUserId());
    }
    
    /****************************************************************
     * @project : Paas 플랫폼 설치 자동화
     * @description :  기본 Manifest 정보 설정
     * @title : setDefualtManifestVO
     * @return : ManifestVO
    *****************************************************************/
    public List<ManifestVO> setDefualtManifestVO(){
        List<ManifestVO> list = new ArrayList<ManifestVO>();
        ManifestVO vo = new ManifestVO();
        vo.setId(1);
        vo.setRecid(1);
        vo.setIaas("openstack");
        vo.setFileName("openstack_manifest");
        vo.setDescription("opstck_manifest_Description");
        vo.setDeploymentName("opstk_deployment_name");
        vo.setPath("/home/local/manifest");
        vo.setDeployStatus("Y");
        vo.setCreateUserId("admin");
        vo.setUpdateUserId("admin");
        list.add(vo);
        return list;
    }
}
