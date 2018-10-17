package org.openpaas.ieda.hbdeploy.web.deploy.diego.dao;

import java.util.Date;

public class HbDiegoDefaultConfigVO {
    
    private Integer id; //id
    private Integer recid;
    private String iaasType;//IaaS 유형
    
    private String createUserId;//생성 사용자 ID
    private Date createDate; //생성일자
    private String updateUserId;//갱신 사용자 ID
    private Date updateDate; //수정일자
    
    //1.1 기본정보    
    private String defaultConfigName;
    private String deploymentName; //배포명
    private String directorId; //설치관리자 ID
    private String diegoReleaseName; //DIEGO 릴리즈명
    private String diegoReleaseVersion; //DIEGO 릴리즈 버전
    private int cfId;
    private String cfConfigName;
    private String cfReleaseName; //CF 릴리즈명
    private String cfReleaseVersion; //CF 릴리즈 버전
    private String gardenReleaseName; //Garden-Linux 릴리즈명
    private String gardenReleaseVersion; //Garden-Linux 릴리즈 버전
    private String cflinuxfs2rootfsreleaseName; //cflinuxfs2rootf 릴리즈 명
    private String cflinuxfs2rootfsreleaseVersion; //cflinuxfs2rootf 릴리즈 버전
    private String paastaMonitoringUse;//PaaS-TA 모니터링 사용 유무
    private String ingestorIp;//PaaS-TA 모니터링 DB 서버 IP
    private String keyFile;//key 파일명
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getRecid() {
        return recid;
    }
    public void setRecid(Integer recid) {
        this.recid = recid;
    }
    public String getIaasType() {
        return iaasType;
    }
    public void setIaasType(String iaasType) {
        this.iaasType = iaasType;
    }
    public String getCreateUserId() {
        return createUserId;
    }
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateUserId() {
        return updateUserId;
    }
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
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
    public String getDirectorId() {
        return directorId;
    }
    public void setDirectorId(String directorId) {
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
    public String getCfReleaseName() {
        return cfReleaseName;
    }
    public void setCfReleaseName(String cfReleaseName) {
        this.cfReleaseName = cfReleaseName;
    }
    public String getCfReleaseVersion() {
        return cfReleaseVersion;
    }
    public void setCfReleaseVersion(String cfReleaseVersion) {
        this.cfReleaseVersion = cfReleaseVersion;
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
    public String getKeyFile() {
        return keyFile;
    }
    public void setKeyFile(String keyFile) {
        this.keyFile = keyFile;
    }
}
