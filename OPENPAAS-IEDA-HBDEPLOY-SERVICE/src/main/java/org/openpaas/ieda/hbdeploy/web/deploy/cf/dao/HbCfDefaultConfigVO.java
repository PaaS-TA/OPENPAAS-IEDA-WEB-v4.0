package org.openpaas.ieda.hbdeploy.web.deploy.cf.dao;

import java.sql.Date;

public class HbCfDefaultConfigVO {
    
    private Integer id; //id
    private Integer recid;
    private String iaasType; //iaas 유형
    private String defaultConfigName;
    private String deploymentName; //배포명
    private int directorId; //설치관리자 UUID
    private String releaseName; //릴리즈명
    private String releaseVersion; //릴리즈버전
    private String domain; //도메인
    private String loginSecret; //로그인 비밀번호
    private String paastaMonitoringUse;//PaaS-TA 모니터링 사용 유무
    private String ingestorIp;//PaaS-TA 모니터링 DB 서버 IP
    private String domainOrganization;
    private String loggregatorReleaseName;
    private String loggregatorReleaseVersion;
    private String createUserId;
    private Date createDate;
    private String updateUserId;
    private Date updateDate;
    
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
    public String getReleaseName() {
        return releaseName;
    }
    public void setReleaseName(String releaseName) {
        this.releaseName = releaseName;
    }
    public String getReleaseVersion() {
        return releaseVersion;
    }
    public void setReleaseVersion(String releaseVersion) {
        this.releaseVersion = releaseVersion;
    }
    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }
    public String getLoginSecret() {
        return loginSecret;
    }
    public void setLoginSecret(String loginSecret) {
        this.loginSecret = loginSecret;
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
    public String getDomainOrganization() {
        return domainOrganization;
    }
    public void setDomainOrganization(String domainOrganization) {
        this.domainOrganization = domainOrganization;
    }
    public String getLoggregatorReleaseName() {
        return loggregatorReleaseName;
    }
    public void setLoggregatorReleaseName(String loggregatorReleaseName) {
        this.loggregatorReleaseName = loggregatorReleaseName;
    }
    public String getLoggregatorReleaseVersion() {
        return loggregatorReleaseVersion;
    }
    public void setLoggregatorReleaseVersion(String loggregatorReleaseVersion) {
        this.loggregatorReleaseVersion = loggregatorReleaseVersion;
    }
}
