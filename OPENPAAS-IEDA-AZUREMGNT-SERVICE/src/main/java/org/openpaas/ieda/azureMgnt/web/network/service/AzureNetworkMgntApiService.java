package org.openpaas.ieda.azureMgnt.web.network.service;

import java.util.HashMap;
import java.util.List;

import org.openpaas.ieda.azureMgnt.web.network.dto.AzureNetworkMgntDTO;
import org.openpaas.ieda.common.web.common.service.CommonApiService;
import org.openpaas.ieda.iaasDashboard.web.account.dao.IaasAccountMgntVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microsoft.azure.credentials.AzureTokenCredentials;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.network.Network;
import com.microsoft.azure.management.resources.ResourceGroup;
import com.microsoft.azure.management.resources.ResourceGroups;
import com.microsoft.rest.LogLevel;

@Service
public class AzureNetworkMgntApiService {

    @Autowired
    CommonApiService commonApiService;

    /****************************************************************
     * @project : Azure 인프라 관리 대시보드
     * @description : Azure TokenCredentials 공통 빌드
     * @title : getAzureClient
     * @return : AzureTokenCredentials
     *****************************************************************/
    public AzureTokenCredentials getAzureClient(IaasAccountMgntVO vo) {
        AzureTokenCredentials azure = commonApiService.getAzureCredentialsFromAzure(vo.getCommonAccessUser(),
                vo.getCommonTenant(), vo.getCommonAccessSecret(), vo.getAzureSubscriptionId());
        return azure;
    }

    /****************************************************************
     * @project : Azure 인프라 관리 대시보드
     * @description : Azure API를 통해 Virtural Network 목록 정보 조회
     * @title : getAzureNetworkInfoListApiFromAzure
     * @return : List<Network>
     *****************************************************************/
    public List<Network> getAzureNetworkInfoListApiFromAzure(IaasAccountMgntVO vo) {
        AzureTokenCredentials azureClient = getAzureClient(vo);
        Azure azure = Azure.configure().withLogLevel(LogLevel.NONE).authenticate(azureClient)
                .withSubscription(vo.getAzureSubscriptionId());
        List<Network> list = azure.networks().list();
        return list;
    }

    /****************************************************************
     * @project : Azure 인프라 관리 대시보드
     * @description : Azure API를 통해 Virtural Network 상세 정보 조회
     * @title : getAzureNetworkDetailInfoFromAzure
     * @return : HashMap<String, Object>
     *****************************************************************/
    public HashMap<String, Object> getAzureNetworkDetailInfoFromAzure(IaasAccountMgntVO vo) {
        AzureTokenCredentials azureClient = getAzureClient(vo);
        Azure azure = Azure.configure().withLogLevel(LogLevel.NONE).authenticate(azureClient)
                .withSubscription(vo.getAzureSubscriptionId());
        List<Network> networkList = azure.networks().list();
        // networkList.get(0).manager().resourceManager().genericResources().list().contains("resorceType");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("networkList", networkList);
        return map;
    }

    /****************************************************************
     * @project : Azure 인프라 관리 대시보드
     * @description : Azure API를 통해 Virtural Network 생성
     * @title : createAzureNetworkFromAzure
     * @return : Network
     *****************************************************************/
    public void createAzureNetworkFromAzure(IaasAccountMgntVO vo, String regionName, AzureNetworkMgntDTO dto) {
        AzureTokenCredentials azureClient = getAzureClient(vo);
        Azure azure = Azure.configure().withLogLevel(LogLevel.BASIC).authenticate(azureClient)
                .withSubscription(vo.getAzureSubscriptionId());
        com.microsoft.azure.management.resources.fluentcore.arm.Region region = com.microsoft.azure.management.resources.fluentcore.arm.Region
                .findByLabelOrName(regionName);

        // this NSG definition block traffic to and from the public Internet
        /*
         * NetworkSecurityGroup backEndSubnetNsg = azure.networkSecurityGroups()
         * .define("vnet1BackEndSubnetNsgName") .withRegion(region)
         * .withExistingResourceGroup(dto.getResourceGroupName())
         * .defineRule("DenyInternetInComing") .denyInbound()
         * .fromAddress("INTERNET") .fromAnyPort() .toAnyAddress() .toAnyPort()
         * .withAnyProtocol() .attach() .defineRule("DenyInternetOutGoing")
         * .denyOutbound() .fromAnyAddress() .fromAnyPort()
         * .toAddress("INTERNET") .toAnyPort() .withAnyProtocol() .attach()
         * .create();
         */

        // create a virtual network with two subnets
        /*
         * Network network = azure.networks().define(dto.getNetworkName())
         * .withRegion(region)
         * .withExistingResourceGroup(dto.getResourceGroupName())
         * .withAddressSpace(dto.getNetworkAddressRangeCidr())
         * .withSubnet(dto.getSubnetName(), dto.getSubnetAddressRangeCidr())
         * .defineSubnet("vnet1BackEndSubnetName")
         * .withAddressPrefix("backEndSubnetAddressRangeCidr")
         * .withExistingNetworkSecurityGroup(backEndSubnetNsg) .attach()
         * .create();
         */

        // create a virtual network with one subnet
        azure.networks().define(dto.getNetworkName()).withRegion(region)
                .withExistingResourceGroup(dto.getResourceGroupName())
                .withAddressSpace(dto.getNetworkAddressSpaceCidr())
                .withSubnet(dto.getSubnetName(), dto.getSubnetAddressRangeCidr()).create();
    }

    /****************************************************************
     * @project : Azure 인프라 관리 대시보드
     * @description : Azure API를 통해 Virtural Network 삭제
     * @title : deleteAzureNetworkFromAzure
     * @return : void
     *****************************************************************/
     public void deleteAzureNetworkFromAzure (IaasAccountMgntVO vo, String regionName, AzureNetworkMgntDTO dto) {
         AzureTokenCredentials azureClient = getAzureClient(vo);
         Azure azure = Azure.configure().withLogLevel(LogLevel.BASIC).authenticate(azureClient)
                 .withSubscription(vo.getAzureSubscriptionId());
         azure.networks().deleteById(dto.getNetworkId());
     }
     
    /****************************************************************
     * @project : Azure 인프라 관리 대시보드
     * @description : Azure API를 통해 리소스 그룹 목록 정보 조회
     * @title : getResourceGroupInfoListApiFromAzure
     * @return : List<ResourceGroup>
     *****************************************************************/
    public List<ResourceGroup> getResourceGroupInfoListApiFromAzure(IaasAccountMgntVO vo) {

        AzureTokenCredentials azureClient = getAzureClient(vo);
        Azure azure = Azure.configure().withLogLevel(LogLevel.NONE).authenticate(azureClient)
                .withSubscription(vo.getAzureSubscriptionId());
        ResourceGroups resource = azure.resourceGroups();
        List<ResourceGroup> list = resource.list();
        return list;
    }

    /***************************************************
     * @project : 인프라 관리 대시보드
     * @description :Azure API를 통해 MS Azure 계정 Subscription ID 가져오기
     * @title : getSubscriptionInfoFromAzure
     * @return : String
     ***************************************************/
    public String getSubscriptionInfoFromAzure(IaasAccountMgntVO vo, String subscriptionId) {
        AzureTokenCredentials azureClient = getAzureClient(vo);
        Azure azure = Azure.configure().withLogLevel(LogLevel.BASIC).authenticate(azureClient)
                .withSubscription(subscriptionId);
        String subscription = azure.subscriptions().getById(subscriptionId).displayName().toString();
        return subscription;
    }

}
