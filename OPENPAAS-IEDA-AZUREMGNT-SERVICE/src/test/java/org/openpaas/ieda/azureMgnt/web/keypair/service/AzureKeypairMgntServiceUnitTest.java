package org.openpaas.ieda.azureMgnt.web.keypair.service;

import static org.junit.Assert.assertEquals;

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
import org.openpaas.ieda.azureMgnt.web.common.base.BaseAzureMgntControllerUnitTest;
import org.openpaas.ieda.azureMgnt.web.keypair.dao.AzureKeypairMgntVO;
import org.openpaas.ieda.azureMgnt.web.keypair.dto.AzureKeypairMgntDTO;
import org.openpaas.ieda.iaasDashboard.web.common.service.CommonIaasService;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class AzureKeypairMgntServiceUnitTest extends BaseAzureMgntControllerUnitTest{
@SuppressWarnings("unused")
private Principal principal = null;
    
    @InjectMocks AzureKeypairMgntService mockAzureKeypairMgntService;
    @Mock CommonIaasService mockCommonIaasService;
    @Mock MessageSource mockMessageSource;
    /***************************************************
     * @project : Azure 관리 대시보드
     * @description : 하나의 메소드가 실행되기전 호출
     * @title : setUp
     * @return : void
    ***************************************************/
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        principal = getLoggined();
    }
    
    /***************************************************
     * @project : Azure 관리 대시보드
     * @description : Azure Keypair 목록 조회 TEST
     * @title : testGetAzureKeypairInfoList
     * @return : void
     ***************************************************/
   /* @Test
    public void testGetAzureKeypairInfoList(){
    	List<String> Keypairlist = getResultKeypairListInfo();
        List<AzureKeypairMgntVO> resultList = mockAzureKeypairMgntService.getAzureKeypairList(1);
        assertEquals(resultList.size(), 14);
        assertEquals(resultList.get(0).getKeypairName(), Keypairlist.get(0));
    }*/
    
    /***************************************************
     * @project : Azure 관리 대시보드
     * @description : 해당 정보 목록 값 설정  
     * @title : getResultKeypairListInfo
     * @return : List<KeyPair>
     ***************************************************/
    /*public List<String> getResultKeypairListInfo(){
    	List<String> list = new ArrayList<String>();
    	String keyPair = "test-key-2.pem";
    	list.add(keyPair);
    	return list;
    }*/
    
    /***************************************************
     * @project : 인프라 관리 대시보드
     * @description : Azure Keypair 생성 TEST
     * @title : testCreateKeypair
     * @return : void
     ***************************************************/
    @Test
    public void testCreateKeypair(){
        AzureKeypairMgntDTO dto = setAzureKeypairInfo();
        mockAzureKeypairMgntService.createKeypair(dto);
    }
    
    /***************************************************
    * @project : 인프라 관리 대시보드
    * @description : Azure Keypair 정보 설정
    * @title : setAzureKeypairInfo
    * @return : AzurePublicIpMgntDTO
    ***************************************************/
    public AzureKeypairMgntDTO setAzureKeypairInfo() {
    	AzureKeypairMgntDTO dto = new AzureKeypairMgntDTO();
        dto.setAccountId(1);
        dto.setKeypairName("test-key-a");
        return dto;
    }
    
}
