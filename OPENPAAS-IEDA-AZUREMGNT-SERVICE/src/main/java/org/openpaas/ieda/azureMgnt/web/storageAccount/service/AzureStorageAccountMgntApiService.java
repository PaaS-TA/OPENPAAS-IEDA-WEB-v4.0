package org.openpaas.ieda.azureMgnt.web.storageAccount.service;

//import com.microsoft.azure.storage.table.*;
import java.util.HashMap;
import java.util.List;

import org.openpaas.ieda.azureMgnt.web.storageAccount.dto.AzureStorageAccountMgntDTO;
import org.openpaas.ieda.common.web.common.service.CommonApiService;
import org.openpaas.ieda.iaasDashboard.web.account.dao.IaasAccountMgntVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microsoft.azure.credentials.AzureTokenCredentials;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.storage.StorageAccount;
import com.microsoft.azure.management.storage.StorageAccountKey;
//import com.microsoft.azure.management.storage.StorageService;
//import com.microsoft.azure.management.storage.implementation.StorageManager;
import com.microsoft.rest.LogLevel;

@Service
public class AzureStorageAccountMgntApiService {
 @Autowired
    CommonApiService commonApiService;
    
    /****************************************************************
     * @project : Azure 인프라 관리 대시보드
     * @description : Azure TokenCredentials 공통 빌드
     * @title : getAzureClient
     * @return : AzureTokenCredentials
    *****************************************************************/
   public AzureTokenCredentials getAzureClient(IaasAccountMgntVO vo){
       AzureTokenCredentials azure = commonApiService.getAzureCredentialsFromAzure(vo.getCommonAccessUser(),vo.getCommonTenant(), vo.getCommonAccessSecret(), vo.getAzureSubscriptionId());
       
       return azure;
    }
   
   /****************************************************************
     * @project : Azure 인프라 관리 대시보드
     * @description : Azure API를 통해  Storage Account 정보 목록 조회 실제 API 호출
     * @title : getAzureStorageAccountInfoListFromAzure
     * @return : List<StorageAccount>
    *****************************************************************/
   public List<StorageAccount> getAzureStorageAccountInfoListFromAzure(IaasAccountMgntVO vo){
       AzureTokenCredentials azureClient = getAzureClient(vo);
       Azure azure  = Azure.configure()
               .withLogLevel(LogLevel.NONE)
               .authenticate(azureClient)
               .withSubscription(vo.getAzureSubscriptionId());
       List<StorageAccount>  list = azure.storageAccounts().list();
       
       return list;
   }

   /****************************************************************
     * @project : Azure 인프라 관리 대시보드
     * @description : Azure API를 통해  Storage Account 상세 정보 목록 조회 실제 API 호출
     * @title : getAzureStorageAccountDetailInfoFromAzure
     * @return : HashMap<String, Object>
    *****************************************************************/
   public HashMap<String, Object> getAzureStorageAccountDetailInfoFromAzure(IaasAccountMgntVO vo){
       AzureTokenCredentials azureClient = getAzureClient(vo);
       Azure azure  = Azure.configure()
               .withLogLevel(LogLevel.NONE)
               .authenticate(azureClient)
               .withSubscription(vo.getAzureSubscriptionId());
       List<StorageAccount>  list = azure.storageAccounts().list();
       HashMap<String, Object> map = new HashMap<String, Object>();
       map.put("storageAccountList", list);
       return map;
   }
   
   /****************************************************************
     * @project : Azure 인프라 관리 대시보드
     * @description : Azure API를 통해  Storage Account 정보 목록 생성 실제 API 호출
     * @title : createAzureStorageAccountFromAzure
     * @return : void
    *****************************************************************/
   public void createAzureStorageAccountFromAzure(IaasAccountMgntVO vo, AzureStorageAccountMgntDTO dto){
       AzureTokenCredentials azureClient = getAzureClient(vo);
       Azure azure  = Azure.configure()
               .withLogLevel(LogLevel.NONE)
               .authenticate(azureClient)
               .withSubscription(vo.getAzureSubscriptionId());
       String storageAccountName = dto.getStorageAccountName();
       String regionName = dto.getLocation();
       String groupName= dto.getResourceGroupName();
       azure.storageAccounts().define(storageAccountName).withRegion(regionName).withExistingResourceGroup(groupName).withGeneralPurposeAccountKind().create();
   }
   
   /****************************************************************
     * @project : Azure 인프라 관리 대시보드
     * @description : Azure API를 통해  Storage Account 정보 목록 삭제 실제 API 호출
     * @title : deleteAzureStorageAccountFromAzure
     * @return : void
    *****************************************************************/
   public void deleteAzureStorageAccountFromAzure(IaasAccountMgntVO vo, String storageAccountId){
       AzureTokenCredentials azureClient = getAzureClient(vo);
       Azure azure  = Azure.configure()
               .withLogLevel(LogLevel.NONE)
               .authenticate(azureClient)
               .withSubscription(vo.getAzureSubscriptionId());
       azure.storageAccounts().deleteById(storageAccountId);
   }

   /****************************************************************
     * @project : Azure 인프라 관리 대시보드
     * @description : Azure API를 통해  Storage Account Key 조회 실제 API 호출
     * @title : getAzureStorageAccountKeyFromAzure
     * @return : String
    *****************************************************************/	   
   public String getAzureStorageAccountKeyFromAzure(IaasAccountMgntVO vo, String storageAccountName ){
	   String key ="";
	   AzureTokenCredentials azureClient = getAzureClient(vo);
       Azure azure  = Azure.configure()
               .withLogLevel(LogLevel.NONE)
               .authenticate(azureClient)
               .withSubscription(vo.getAzureSubscriptionId());
      for (int i =0; i< azure.storageAccounts().list().size(); i++){
    	String thename = azure.storageAccounts().list().get(i).name();
        if (thename.equals(storageAccountName)){
    	    String storageAccountId = azure.storageAccounts().list().get(i).id();
    	    StorageAccount storageAccount = azure.storageAccounts().getById(storageAccountId);
    	    List<StorageAccountKey> storageAccountKeys = storageAccount.getKeys();
    	    key = storageAccountKeys.get(i).value();
         }
       }
      return key;
   }
   
   /****************************************************************
    * @project : Azure 인프라 관리 대시보드
    * @description : Azure API를 통해  Storage Account Blob Service 정보 목록 조회 실제 API 호출
    * @title : getAzureBlobInfoListFromAzure
    * @return : List<ContainerService>
    *****************************************************************/
/*   public List<ContainerService> getAzureBlobInfoListFromAzure(IaasAccountMgntVO vo){
	   AzureTokenCredentials azureClient = getAzureClient(vo);
	   Azure azure  = Azure.configure()
			   .withLogLevel(LogLevel.NONE)
			   .authenticate(azureClient)
			   .withSubscription(vo.getAzureSubscriptionId());
	   List<ContainerService>  list = azure.containerServices().list();
	   return list;
   }*/
   
   /****************************************************************
     * @project : Azure 인프라 관리 대시보드
     * @description : Azure API를 통해  Storage Account Blob 정보 생성 실제 API 호출
     * @title : createAzureStorageAccountFromAzure
     * @return : void
    *****************************************************************/
   public void createAzureBlobFromAzure(IaasAccountMgntVO vo, AzureStorageAccountMgntDTO dto){
   
   }
   
   /****************************************************************
     * @project : Azure 인프라 관리 대시보드
     * @description : Azure API를 통해  Storage Account Blob 정보  삭제 실제 API 호출
     * @title : deleteAzureBlobFromAzure
     * @return : void
    *****************************************************************/
   public void deleteAzureBlobFromAzure(IaasAccountMgntVO vo, AzureStorageAccountMgntDTO dto){
   
   }
   
   /****************************************************************
     * @project : Azure 인프라 관리 대시보드
     * @description : Azure API를 통해  Storage Account Table 정보 목록 조회 실제 API 호출
     * @title : getAzureStorageTableInfoListFromAzure
     * @return : List<ContainerService>
    *****************************************************************/
   
   /****************************************************************
    * @project : Azure 인프라 관리 대시보드
    * @description : Azure API를 통해  Storage Account Table 정보 생성 실제 API 호출
    * @title : createAzureTableFromAzure
    * @return : void
   *****************************************************************/
  public void createAzureTableFromAzure(IaasAccountMgntVO vo, AzureStorageAccountMgntDTO dto){
  
  }
  
  /****************************************************************
    * @project : Azure 인프라 관리 대시보드
    * @description : Azure API를 통해  Storage Account Table 정보  삭제 실제 API 호출
    * @title : deleteAzureTableFromAzure
    * @return : void
   *****************************************************************/
  public void deleteAzureTableFromAzure(IaasAccountMgntVO vo, AzureStorageAccountMgntDTO dto){
  
  }

}
