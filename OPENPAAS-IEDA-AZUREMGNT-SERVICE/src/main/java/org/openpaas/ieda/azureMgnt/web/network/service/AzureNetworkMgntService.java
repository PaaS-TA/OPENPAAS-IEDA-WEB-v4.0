package org.openpaas.ieda.azureMgnt.web.network.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.openpaas.ieda.azureMgnt.web.network.dao.AzureNetworkMgntVO;
import org.openpaas.ieda.azureMgnt.web.network.dto.AzureNetworkMgntDTO;
import org.openpaas.ieda.iaasDashboard.web.account.dao.IaasAccountMgntVO;
import org.openpaas.ieda.iaasDashboard.web.common.service.CommonIaasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.microsoft.azure.management.network.Network;

@Service
public class AzureNetworkMgntService {
	
	@Autowired AzureNetworkMgntVO azureNetworkMgntVO;
	@Autowired AzureNetworkMgntDTO azureNetworkMgntDTO;
	@Autowired AzureNetworkMgntApiService azureNetworkMgntApiService;
	@Autowired CommonIaasService commonIaasService;
    @Autowired MessageSource message;
    /***************************************************
     * @project : AZURE 인프라 관리 대시보드
     * @description : AZURE 계정 정보가 실제 존재 하는지 확인 및 상세 조회
     * @title : getAzureAccountInfo
     * @return : IaasAccountMgntVO
     ***************************************************/
     public IaasAccountMgntVO getAzureAccountInfo(Principal principal, int accountId){
         return commonIaasService.getIaaSAccountInfo(principal, accountId, "azure");
     }
     
     /***************************************************
      * @project : Azure 관리 대시보드
      * @description : NETWORK 목록 조회
      * @title : getAzureNetworkInfoList
      * @return : List<AzureResourceGroupMgntVO>
      ***************************************************/
      public List<AzureNetworkMgntVO> getAzureNetworkInfoList(Principal principal, int accountId) {
        
          IaasAccountMgntVO vo = getAzureAccountInfo(principal, accountId);
          String subName = getAzureSubscriptionNameInfo(principal, accountId, vo.getAzureSubscriptionId());
          List<Network> azureNetworkList = azureNetworkMgntApiService.getAzureNetworkInfoListApiFromAzure(vo);
          List<AzureNetworkMgntVO> list = new ArrayList<AzureNetworkMgntVO>();
          for (int i=0; i<azureNetworkList.size(); i++ ){
              Network network = azureNetworkList.get(i);
              AzureNetworkMgntVO azureRgVo = new AzureNetworkMgntVO();
              azureRgVo.setNetworkName(network.name());
              azureRgVo.setLocation(network.regionName());
              azureRgVo.setResourceGroupName(network.resourceGroupName());
              azureRgVo.setResourceType(network.type());
              if(network.addressSpaces().size() != 0){
            	 azureRgVo.setNetworkAddressSpaceCidr(network.addressSpaces().get(0).toString());
              }
              //DNS server name
              if(network.dnsServerIPs().size() != 0){
            	 azureRgVo.setDnsServer(network.dnsServerIPs().get(0).toString());
              }
              azureRgVo.setDnsServer("Default (Azure-provided)");
              azureRgVo.setSubscriptionName(subName);
              azureRgVo.setAzureSubscriptionId(vo.getAzureSubscriptionId());
              azureRgVo.setAccountId(vo.getId());
              azureRgVo.setRecid(i);
              list.add(azureRgVo);
          }
          return list;
      }
      
      /***************************************************
       * @project : 인프라 관리 대시보드
       * @description : Azure 구독 명 조회
       * @title : getAzureSubNameInfo
       * @return : String
       ***************************************************/
      public String getAzureSubscriptionNameInfo(Principal principal, int accountId, String subscriptionId){
          IaasAccountMgntVO vo =  getAzureAccountInfo(principal, accountId);
          String subName = azureNetworkMgntApiService.getSubscriptionInfoFromAzure(vo, subscriptionId);
          return  subName;
      }
    
}
