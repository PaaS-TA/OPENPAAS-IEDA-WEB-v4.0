package org.openpaas.ieda.hbdeploy.web.deploy.diego.dto;

import java.util.Date;

public class HbDiegoDefaultConfigDTO {
    private String id; //ID
    private String iaasType; //IaaS
    private Date createDate; //생성일자
    private Date updateDate; //수정일자

    //1.1 기본정보    
    private String defaultConfigName; // 기본 정보 별칭
    private String deploymentName; //배포명
    private int directorId; //설치관리자 ID
    private String diegoReleaseName; // 릴리즈명
    private String diegoReleaseVersion; //Diego 릴리즈 버전
    private int cfId; //CF 릴리즈명
    private String cfConfigName; //CF Config 명
    private String gardenReleaseName; //Garden Linux 릴리즈명
    private String gardenReleaseVersion; //Garden Linux 릴리즈 버전
    private String cflinuxfs2rootfsreleaseName; //cflinuxfs2rootf 릴리즈 명
    private String cflinuxfs2rootfsreleaseVersion; //cflinuxfs2rootf 릴리즈 버전
    private String paastaMonitoringUse;//PaaS-TA 모니터링 사용 유무
    private String ingestorIp;//PaaS-TA 모니터링 DB 서버 IP
    private String ingestorPort;//PaaS-TA 모니터링 DB 서버 PORT
    private String keyFile;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getIaasType() {
        return iaasType;
    }
    public void setIaasType(String iaasType) {
        this.iaasType = iaasType;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getDefaultConfigName() {
        return defaultConfigName;
    }
    public void setDefaultConfigName(String defaultConfigName) {
        this.defaultConfigName = defaultConfigName;
    }
    public String getDeploymentName() {
        return deploymentName;
    }
    public void setDeploymentName(String deploymentName) {
        this.deploymentName = deploymentName;
    }
    public int getDirectorId() {
        return directorId;
    }
    public void setDirectorId(int directorId) {
        this.directorId = directorId;
    }
    public String getDiegoReleaseName() {
        return diegoReleaseName;
    }
    public void setDiegoReleaseName(String diegoReleaseName) {
        this.diegoReleaseName = diegoReleaseName;
    }
    public String getDiegoReleaseVersion() {
        return diegoReleaseVersion;
    }
    public void setDiegoReleaseVersion(String diegoReleaseVersion) {
        this.diegoReleaseVersion = diegoReleaseVersion;
    }
    public int getCfId() {
        return cfId;
    }
    public void setCfId(int cfId) {
        this.cfId = cfId;
    }
    public String getCfConfigName() {
        return cfConfigName;
    }
    public void setCfConfigName(String cfConfigName) {
        this.cfConfigName = cfConfigName;
    }
    public String getGardenReleaseName() {
        return gardenReleaseName;
    }
    public void setGardenReleaseName(String gardenReleaseName) {
        this.gardenReleaseName = gardenReleaseName;
    }
    public String getGardenReleaseVersion() {
        return gardenReleaseVersion;
    }
    public void setGardenReleaseVersion(String gardenReleaseVersion) {
        this.gardenReleaseVersion = gardenReleaseVersion;
    }
    public String getCflinuxfs2rootfsreleaseName() {
        return cflinuxfs2rootfsreleaseName;
    }
    public void setCflinuxfs2rootfsreleaseName(String cflinuxfs2rootfsreleaseName) {
        this.cflinuxfs2rootfsreleaseName = cflinuxfs2rootfsreleaseName;
    }
    public String getCflinuxfs2rootfsreleaseVersion() {
        return cflinuxfs2rootfsreleaseVersion;
    }
    public void setCflinuxfs2rootfsreleaseVersion(String cflinuxfs2rootfsreleaseVersion) {
        this.cflinuxfs2rootfsreleaseVersion = cflinuxfs2rootfsreleaseVersion;
    }
    public String getKeyFile() {
        return keyFile;
    }
    public void setKeyFile(String keyFile) {
        this.keyFile = keyFile;
    }
   public String getPaastaMonitoringUse() {
      return paastaMonitoringUse;
   }
   public void setPaastaMonitoringUse(String paastaMonitoringUse) {
      this.paastaMonitoringUse = paastaMonitoringUse;
   }
   public String getIngestorIp() {
      return ingestorIp;
   }
   public void setIngestorIp(String ingestorIp) {
      this.ingestorIp = ingestorIp;
   }
   public String getIngestorPort() {
      return ingestorPort;
   }
   public void setIngestorPort(String ingestorPort) {
      this.ingestorPort = ingestorPort;
   }
    
}
