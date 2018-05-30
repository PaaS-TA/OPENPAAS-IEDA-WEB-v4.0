package org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dao;

import java.util.Date;
import java.util.HashMap;

import org.openpaas.ieda.deploy.web.information.iassConfig.dao.IaasConfigMgntVO;

public class HbBootstrapVO {

    private Integer id; // id
    private String iaasType; // iaas 유형
    private Integer iaasConfigId;//인프라 환경 설정 아이디
    private String createUserId; // 설치 사용자
    private String updateUserId; // 설치 수정 사용자
    private Date createDate; // 생성일자
    private Date updateDate; // 수정일자
    private String testFlag; //통합 테스트 사용
    
    /** Iaas Config Info **/
    private IaasConfigMgntVO iaasConfig;
    private HashMap<String, Object> iaasAccount;
    
    /** Default Info **/
    private String deploymentName; // 배포명
    private String directorName; // 디렉터명
    private String credentialKeyName;
    private String boshRelease; // BOSH 릴리즈
    private String boshCpiRelease; // BOSH API 릴리즈
    private String snapshotSchedule;//스냅샷 스케줄
    private String enableSnapshots;//스냅샷 사용 유무
    private String ntp; // NTP
    private String paastaMonitoringUse; //PaaS-TA 모니터링 사용 유무
    private String paastaMonitoringIp; //PaaS-TA 모니터링 사용시 ingrestorIP
    private String influxdbIp;
    private String paastaMonitoringRelease; //PaaS-TA 모니터링 사용시 릴리즈
    private String osConfRelease;
    /** Network Info **/
    private String subnetId; // 네트워크id
    private String privateStaticIp; // 디렉터 내부 ip
    private String subnetRange; // 서브넷 범위
    private String subnetGateway; // 게이트웨이
    private String subnetDns; // DNS
    
    private String publicStaticIp; //디렉터 공인 ip
    private String publicSubnetId; //public 네트워크id
    private String publicSubnetRange; //public 서브넷 범위 
    private String publicSubnetGateway; //public 게이트웨이
    private String publicSubnetDns; //public DNS
    private String networkName;//네트워크 명

    /** Resource Info **/
    private String stemcell; // 스템셀
    private String cloudInstanceType; // 인스턴스 유형
    private String boshPassword; //VM 비밀번호
    private String resourcePoolCpu;//리소스 풀 CPU
    private String resourcePoolRam;//리소스 풀 RAM
    private String resourcePoolDisk;//리소스 풀 DISK

    private String deploymentFile; // 배포파일
    private String deployStatus; // 배포상태
    private String deployLog; // 배포로그
    private String bootstrapType;
    public HbBootstrapVO(){
        iaasConfig = new IaasConfigMgntVO();
        iaasAccount = new HashMap<String, Object>();
    }
    /** Hybrid Info **/
    private Integer hybridBootStrapId;
    private String publicBootStrapId;
    private String privateBootStrapId;
    private String privateDeploymentFileName;
    private String setHybridDbTableName;
    
    
    private Integer hyPriId; // id
    private String hyPriIaasType; // iaas 유형
    private String hyPubIaasType; // iaas 유형
    private Integer hyPriIaasConfigId;//인프라 환경 설정 아이디
    private String hyPriCreateUserId; // 설치 사용자
    private String hyPriUpdateUserId; // 설치 수정 사용자
    private Date hyPriCreateDate; // 생성일자
    private Date hyPriUpdateDate; // 수정일자
    private String hyPriTestFlag; //통합 테스트 사용
    
    /** Default Info **/
    private String hyPriDeploymentName; // 배포명
    private String hyPriDirectorName; // 디렉터명
    private String hyPriCredentialKeyName;
    private String hyPriBoshRelease; // BOSH 릴리즈
    private String hyPriBoshCpiRelease; // BOSH API 릴리즈
    private String hyPriSnapshotSchedule;//스냅샷 스케줄
    private String hyPriEnableSnapshots;//스냅샷 사용 유무
    private String hyPriNtp; // NTP
    private String hyPriPaastaMonitoringUse; //PaaS-TA 모니터링 사용 유무
    private String hyPriPaastaMonitoringIp; //PaaS-TA 모니터링 사용시 ingrestorIP
    private String hyPriInfluxdbIp;
    private String hyPriPaastaMonitoringRelease; //PaaS-TA 모니터링 사용시 릴리즈
    private String hyPriOsConfRelease;
    /** Network Info **/
    private String hyPriSubnetId; // 네트워크id
    private String hyPriPrivateStaticIp; // 디렉터 내부 ip
    private String hyPriSubnetRange; // 서브넷 범위
    private String hyPriSubnetGateway; // 게이트웨이
    private String hyPriSubnetDns; // DNS
    
    private String hyPriPublicStaticIp; //디렉터 공인 ip
    private String hyPriPublicSubnetId; //public 네트워크id
    private String hyPriPublicSubnetRange; //public 서브넷 범위 
    private String hyPriPublicSubnetGateway; //public 게이트웨이
    private String hyPriPublicSubnetDns; //public DNS
    private String hyPriNetworkName;//네트워크 명

    /** Resource Info **/
    private String hyPriStemcell; // 스템셀
    private String hyPriCloudInstanceType; // 인스턴스 유형
    private String hyPriBoshPassword; //VM 비밀번호
    private String hyPriResourcePoolCpu;//리소스 풀 CPU
    private String hyPriResourcePoolRam;//리소스 풀 RAM
    private String hyPriResourcePoolDisk;//리소스 풀 DISK

    private String hyPriDeploymentFile; // 배포파일
    private String hyPriDeployStatus; // 배포상태
    private String hyPriDeployLog; // 배포로그
    private String hyPriBootstrapType;;
    
    public Integer getId() {
        return id;
    }
    public String getIaasType() {
        return iaasType;
    }
    public Integer getIaasConfigId() {
        return iaasConfigId;
    }
    public String getCreateUserId() {
        return createUserId;
    }
    public String getUpdateUserId() {
        return updateUserId;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setIaasType(String iaasType) {
        this.iaasType = iaasType;
    }
    public void setIaasConfigId(Integer iaasConfigId) {
        this.iaasConfigId = iaasConfigId;
    }
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }
    public void setUpdateDate(Date updateDate) {
        if(updateDate == null) {
            this.updateDate = null;
        } else {
            this.updateDate = new Date(updateDate.getTime());
        }
    }
    public Date getUpdateDate() {
        if(updateDate == null) {
            return null;
        } else {
            return new Date(updateDate.getTime());
        }
    }
    public Date getCreateDate() {
        if(createDate == null) {
            return null;
        } else {
            return new Date(createDate.getTime());
        }
    }
    
    public void setCreateDate(Date createDate) {
        if(createDate == null) {
            this.createDate = null;
        } else {
            this.createDate = new Date(createDate.getTime());
        }
    }
    public String getOsConfRelease() {
        return osConfRelease;
    }
    public void setOsConfRelease(String osConfRelease) {
        this.osConfRelease = osConfRelease;
    }
    public String getDeploymentName() {
        return deploymentName;
    }
    public void setDeploymentName(String deploymentName) {
        this.deploymentName = deploymentName;
    }
    public String getDirectorName() {
        return directorName;
    }
    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }
    public String getCredentialKeyName() {
        return credentialKeyName;
    }
    public void setCredentialKeyName(String credentialKeyName) {
        this.credentialKeyName = credentialKeyName;
    }
    public String getBoshRelease() {
        return boshRelease;
    }
    public void setBoshRelease(String boshRelease) {
        this.boshRelease = boshRelease;
    }
    public String getBoshCpiRelease() {
        return boshCpiRelease;
    }
    public void setBoshCpiRelease(String boshCpiRelease) {
        this.boshCpiRelease = boshCpiRelease;
    }
    public String getSnapshotSchedule() {
        return snapshotSchedule;
    }
    public void setSnapshotSchedule(String snapshotSchedule) {
        this.snapshotSchedule = snapshotSchedule;
    }
    public String getEnableSnapshots() {
        return enableSnapshots;
    }
    public void setEnableSnapshots(String enableSnapshots) {
        this.enableSnapshots = enableSnapshots;
    }
    public String getSubnetId() {
        return subnetId;
    }
    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }
    public String getSubnetRange() {
        return subnetRange;
    }
    public void setSubnetRange(String subnetRange) {
        this.subnetRange = subnetRange;
    }
    public String getSubnetGateway() {
        return subnetGateway;
    }
    public void setSubnetGateway(String subnetGateway) {
        this.subnetGateway = subnetGateway;
    }
    public String getSubnetDns() {
        return subnetDns;
    }
    public void setSubnetDns(String subnetDns) {
        this.subnetDns = subnetDns;
    }
    public String getNtp() {
        return ntp;
    }
    public void setNtp(String ntp) {
        this.ntp = ntp;
    }
    public String getPublicStaticIp() {
        return publicStaticIp;
    }
    public void setPublicStaticIp(String publicStaticIp) {
        this.publicStaticIp = publicStaticIp;
    }
    public String getPublicSubnetId() {
        return publicSubnetId;
    }
    public void setPublicSubnetId(String publicSubnetId) {
        this.publicSubnetId = publicSubnetId;
    }
    public String getPublicSubnetRange() {
        return publicSubnetRange;
    }
    public void setPublicSubnetRange(String publicSubnetRange) {
        this.publicSubnetRange = publicSubnetRange;
    }
    public String getPublicSubnetGateway() {
        return publicSubnetGateway;
    }
    public void setPublicSubnetGateway(String publicSubnetGateway) {
        this.publicSubnetGateway = publicSubnetGateway;
    }
    public String getPublicSubnetDns() {
        return publicSubnetDns;
    }
    public void setPublicSubnetDns(String publicSubnetDns) {
        this.publicSubnetDns = publicSubnetDns;
    }
    public String getStemcell() {
        return stemcell;
    }
    public void setStemcell(String stemcell) {
        this.stemcell = stemcell;
    }
    public String getCloudInstanceType() {
        return cloudInstanceType;
    }
    public void setCloudInstanceType(String cloudInstanceType) {
        this.cloudInstanceType = cloudInstanceType;
    }
    public String getBoshPassword() {
        return boshPassword;
    }
    public void setBoshPassword(String boshPassword) {
        this.boshPassword = boshPassword;
    }
    public String getResourcePoolCpu() {
        return resourcePoolCpu;
    }
    public void setResourcePoolCpu(String resourcePoolCpu) {
        this.resourcePoolCpu = resourcePoolCpu;
    }
    public String getResourcePoolRam() {
        return resourcePoolRam;
    }
    public void setResourcePoolRam(String resourcePoolRam) {
        this.resourcePoolRam = resourcePoolRam;
    }
    public String getResourcePoolDisk() {
        return resourcePoolDisk;
    }
    public void setResourcePoolDisk(String resourcePoolDisk) {
        this.resourcePoolDisk = resourcePoolDisk;
    }
    public String getDeploymentFile() {
        return deploymentFile;
    }
    public void setDeploymentFile(String deploymentFile) {
        this.deploymentFile = deploymentFile;
    }
    public String getDeployStatus() {
        return deployStatus;
    }
    public void setDeployStatus(String deployStatus) {
        this.deployStatus = deployStatus;
    }
    public String getDeployLog() {
        return deployLog;
    }
    public void setDeployLog(String deployLog) {
        this.deployLog = deployLog;
    }
    public String getNetworkName() {
        return networkName;
    }
    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }
    public IaasConfigMgntVO getIaasConfig() {
        return iaasConfig;
    }
    public HashMap<String, Object> getIaasAccount() {
        return iaasAccount;
    }
    public void setIaasConfig(IaasConfigMgntVO iaasConfig) {
        this.iaasConfig = iaasConfig;
    }
    public void setIaasAccount(HashMap<String, Object> iaasAccount) {
        this.iaasAccount = iaasAccount;
    }

    public String getPrivateStaticIp() {
        return privateStaticIp;
    }

    public void setPrivateStaticIp(String privateStaticIp) {
        this.privateStaticIp = privateStaticIp;
    }

    public String getTestFlag() {
        return testFlag;
    }

    public void setTestFlag(String testFlag) {
        this.testFlag = testFlag;
    }

    public String getPaastaMonitoringUse() {
        return paastaMonitoringUse;
    }

    public void setPaastaMonitoringUse(String paastaMonitoringUse) {
        this.paastaMonitoringUse = paastaMonitoringUse;
    }

    public String getPaastaMonitoringIp() {
        return paastaMonitoringIp;
    }

    public void setPaastaMonitoringIp(String paastaMonitoringIp) {
        this.paastaMonitoringIp = paastaMonitoringIp;
    }

    public String getPaastaMonitoringRelease() {
        return paastaMonitoringRelease;
    }

    public void setPaastaMonitoringRelease(String paastaMonitoringRelease) {
        this.paastaMonitoringRelease = paastaMonitoringRelease;
    }

    public String getInfluxdbIp() {
        return influxdbIp;
    }

    public void setInfluxdbIp(String influxdbIp) {
        this.influxdbIp = influxdbIp;
    }
    public Integer getHybridBootStrapId() {
        return hybridBootStrapId;
    }
    public void setHybridBootStrapId(Integer hybridBootStrapId) {
        this.hybridBootStrapId = hybridBootStrapId;
    }
    public String getPublicBootStrapId() {
        return publicBootStrapId;
    }
    public void setPublicBootStrapId(String publicBootStrapId) {
        this.publicBootStrapId = publicBootStrapId;
    }
    public String getPrivateBootStrapId() {
        return privateBootStrapId;
    }
    public void setPrivateBootStrapId(String privateBootStrapId) {
        this.privateBootStrapId = privateBootStrapId;
    }
    public String getBootstrapType() {
        return bootstrapType;
    }
    public void setBootstrapType(String bootstrapType) {
        this.bootstrapType = bootstrapType;
    }
    public String getPrivateDeploymentFileName() {
        return privateDeploymentFileName;
    }
    public void setPrivateDeploymentFileName(String privateDeploymentFileName) {
        this.privateDeploymentFileName = privateDeploymentFileName;
    }
    public String getSetHybridDbTableName() {
        return setHybridDbTableName;
    }
    public void setSetHybridDbTableName(String setHybridDbTableName) {
        this.setHybridDbTableName = setHybridDbTableName;
    }
    public Integer getHyPriId() {
        return hyPriId;
    }
    public void setHyPriId(Integer hyPriId) {
        this.hyPriId = hyPriId;
    }
    public String getHyPriIaasType() {
        return hyPriIaasType;
    }
    public void setHyPriIaasType(String hyPriIaasType) {
        this.hyPriIaasType = hyPriIaasType;
    }
    public String getHyPubIaasType() {
        return hyPubIaasType;
    }
    public void setHyPubIaasType(String hyPubIaasType) {
        this.hyPubIaasType = hyPubIaasType;
    }
    public Integer getHyPriIaasConfigId() {
        return hyPriIaasConfigId;
    }
    public void setHyPriIaasConfigId(Integer hyPriIaasConfigId) {
        this.hyPriIaasConfigId = hyPriIaasConfigId;
    }
    public String getHyPriCreateUserId() {
        return hyPriCreateUserId;
    }
    public void setHyPriCreateUserId(String hyPriCreateUserId) {
        this.hyPriCreateUserId = hyPriCreateUserId;
    }
    public String getHyPriUpdateUserId() {
        return hyPriUpdateUserId;
    }
    public void setHyPriUpdateUserId(String hyPriUpdateUserId) {
        this.hyPriUpdateUserId = hyPriUpdateUserId;
    }
    public Date getHyPriCreateDate() {
        return hyPriCreateDate;
    }
    public void setHyPriCreateDate(Date hyPriCreateDate) {
        this.hyPriCreateDate = hyPriCreateDate;
    }
    public Date getHyPriUpdateDate() {
        return hyPriUpdateDate;
    }
    public void setHyPriUpdateDate(Date hyPriUpdateDate) {
        this.hyPriUpdateDate = hyPriUpdateDate;
    }
    public String getHyPriTestFlag() {
        return hyPriTestFlag;
    }
    public void setHyPriTestFlag(String hyPriTestFlag) {
        this.hyPriTestFlag = hyPriTestFlag;
    }
    public String getHyPriDeploymentName() {
        return hyPriDeploymentName;
    }
    public void setHyPriDeploymentName(String hyPriDeploymentName) {
        this.hyPriDeploymentName = hyPriDeploymentName;
    }
    public String getHyPriDirectorName() {
        return hyPriDirectorName;
    }
    public void setHyPriDirectorName(String hyPriDirectorName) {
        this.hyPriDirectorName = hyPriDirectorName;
    }
    public String getHyPriCredentialKeyName() {
        return hyPriCredentialKeyName;
    }
    public void setHyPriCredentialKeyName(String hyPriCredentialKeyName) {
        this.hyPriCredentialKeyName = hyPriCredentialKeyName;
    }
    public String getHyPriBoshRelease() {
        return hyPriBoshRelease;
    }
    public void setHyPriBoshRelease(String hyPriBoshRelease) {
        this.hyPriBoshRelease = hyPriBoshRelease;
    }
    public String getHyPriBoshCpiRelease() {
        return hyPriBoshCpiRelease;
    }
    public void setHyPriBoshCpiRelease(String hyPriBoshCpiRelease) {
        this.hyPriBoshCpiRelease = hyPriBoshCpiRelease;
    }
    public String getHyPriSnapshotSchedule() {
        return hyPriSnapshotSchedule;
    }
    public void setHyPriSnapshotSchedule(String hyPriSnapshotSchedule) {
        this.hyPriSnapshotSchedule = hyPriSnapshotSchedule;
    }
    public String getHyPriEnableSnapshots() {
        return hyPriEnableSnapshots;
    }
    public void setHyPriEnableSnapshots(String hyPriEnableSnapshots) {
        this.hyPriEnableSnapshots = hyPriEnableSnapshots;
    }
    public String getHyPriNtp() {
        return hyPriNtp;
    }
    public void setHyPriNtp(String hyPriNtp) {
        this.hyPriNtp = hyPriNtp;
    }
    public String getHyPriPaastaMonitoringUse() {
        return hyPriPaastaMonitoringUse;
    }
    public void setHyPriPaastaMonitoringUse(String hyPriPaastaMonitoringUse) {
        this.hyPriPaastaMonitoringUse = hyPriPaastaMonitoringUse;
    }
    public String getHyPriPaastaMonitoringIp() {
        return hyPriPaastaMonitoringIp;
    }
    public void setHyPriPaastaMonitoringIp(String hyPriPaastaMonitoringIp) {
        this.hyPriPaastaMonitoringIp = hyPriPaastaMonitoringIp;
    }
    public String getHyPriInfluxdbIp() {
        return hyPriInfluxdbIp;
    }
    public void setHyPriInfluxdbIp(String hyPriInfluxdbIp) {
        this.hyPriInfluxdbIp = hyPriInfluxdbIp;
    }
    public String getHyPriPaastaMonitoringRelease() {
        return hyPriPaastaMonitoringRelease;
    }
    public void setHyPriPaastaMonitoringRelease(String hyPriPaastaMonitoringRelease) {
        this.hyPriPaastaMonitoringRelease = hyPriPaastaMonitoringRelease;
    }
    public String getHyPriOsConfRelease() {
        return hyPriOsConfRelease;
    }
    public void setHyPriOsConfRelease(String hyPriOsConfRelease) {
        this.hyPriOsConfRelease = hyPriOsConfRelease;
    }
    public String getHyPriSubnetId() {
        return hyPriSubnetId;
    }
    public void setHyPriSubnetId(String hyPriSubnetId) {
        this.hyPriSubnetId = hyPriSubnetId;
    }
    public String getHyPriPrivateStaticIp() {
        return hyPriPrivateStaticIp;
    }
    public void setHyPriPrivateStaticIp(String hyPriPrivateStaticIp) {
        this.hyPriPrivateStaticIp = hyPriPrivateStaticIp;
    }
    public String getHyPriSubnetRange() {
        return hyPriSubnetRange;
    }
    public void setHyPriSubnetRange(String hyPriSubnetRange) {
        this.hyPriSubnetRange = hyPriSubnetRange;
    }
    public String getHyPriSubnetGateway() {
        return hyPriSubnetGateway;
    }
    public void setHyPriSubnetGateway(String hyPriSubnetGateway) {
        this.hyPriSubnetGateway = hyPriSubnetGateway;
    }
    public String getHyPriSubnetDns() {
        return hyPriSubnetDns;
    }
    public void setHyPriSubnetDns(String hyPriSubnetDns) {
        this.hyPriSubnetDns = hyPriSubnetDns;
    }
    public String getHyPriPublicStaticIp() {
        return hyPriPublicStaticIp;
    }
    public void setHyPriPublicStaticIp(String hyPriPublicStaticIp) {
        this.hyPriPublicStaticIp = hyPriPublicStaticIp;
    }
    public String getHyPriPublicSubnetId() {
        return hyPriPublicSubnetId;
    }
    public void setHyPriPublicSubnetId(String hyPriPublicSubnetId) {
        this.hyPriPublicSubnetId = hyPriPublicSubnetId;
    }
    public String getHyPriPublicSubnetRange() {
        return hyPriPublicSubnetRange;
    }
    public void setHyPriPublicSubnetRange(String hyPriPublicSubnetRange) {
        this.hyPriPublicSubnetRange = hyPriPublicSubnetRange;
    }
    public String getHyPriPublicSubnetGateway() {
        return hyPriPublicSubnetGateway;
    }
    public void setHyPriPublicSubnetGateway(String hyPriPublicSubnetGateway) {
        this.hyPriPublicSubnetGateway = hyPriPublicSubnetGateway;
    }
    public String getHyPriPublicSubnetDns() {
        return hyPriPublicSubnetDns;
    }
    public void setHyPriPublicSubnetDns(String hyPriPublicSubnetDns) {
        this.hyPriPublicSubnetDns = hyPriPublicSubnetDns;
    }
    public String getHyPriNetworkName() {
        return hyPriNetworkName;
    }
    public void setHyPriNetworkName(String hyPriNetworkName) {
        this.hyPriNetworkName = hyPriNetworkName;
    }
    public String getHyPriStemcell() {
        return hyPriStemcell;
    }
    public void setHyPriStemcell(String hyPriStemcell) {
        this.hyPriStemcell = hyPriStemcell;
    }
    public String getHyPriCloudInstanceType() {
        return hyPriCloudInstanceType;
    }
    public void setHyPriCloudInstanceType(String hyPriCloudInstanceType) {
        this.hyPriCloudInstanceType = hyPriCloudInstanceType;
    }
    public String getHyPriBoshPassword() {
        return hyPriBoshPassword;
    }
    public void setHyPriBoshPassword(String hyPriBoshPassword) {
        this.hyPriBoshPassword = hyPriBoshPassword;
    }
    public String getHyPriResourcePoolCpu() {
        return hyPriResourcePoolCpu;
    }
    public void setHyPriResourcePoolCpu(String hyPriResourcePoolCpu) {
        this.hyPriResourcePoolCpu = hyPriResourcePoolCpu;
    }
    public String getHyPriResourcePoolRam() {
        return hyPriResourcePoolRam;
    }
    public void setHyPriResourcePoolRam(String hyPriResourcePoolRam) {
        this.hyPriResourcePoolRam = hyPriResourcePoolRam;
    }
    public String getHyPriResourcePoolDisk() {
        return hyPriResourcePoolDisk;
    }
    public void setHyPriResourcePoolDisk(String hyPriResourcePoolDisk) {
        this.hyPriResourcePoolDisk = hyPriResourcePoolDisk;
    }
    public String getHyPriDeploymentFile() {
        return hyPriDeploymentFile;
    }
    public void setHyPriDeploymentFile(String hyPriDeploymentFile) {
        this.hyPriDeploymentFile = hyPriDeploymentFile;
    }
    public String getHyPriDeployStatus() {
        return hyPriDeployStatus;
    }
    public void setHyPriDeployStatus(String hyPriDeployStatus) {
        this.hyPriDeployStatus = hyPriDeployStatus;
    }
    public String getHyPriDeployLog() {
        return hyPriDeployLog;
    }
    public void setHyPriDeployLog(String hyPriDeployLog) {
        this.hyPriDeployLog = hyPriDeployLog;
    }
    public String getHyPriBootstrapType() {
        return hyPriBootstrapType;
    }
    public void getHyPriBootstrapType(String hyPriBootstrapType) {
        this.hyPriBootstrapType = hyPriBootstrapType;
    }

}