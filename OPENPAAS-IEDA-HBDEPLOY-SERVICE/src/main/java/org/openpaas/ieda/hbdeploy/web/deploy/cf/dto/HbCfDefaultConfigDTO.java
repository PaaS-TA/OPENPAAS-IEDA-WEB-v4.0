package org.openpaas.ieda.hbdeploy.web.deploy.cf.dto;

import java.sql.Date;

public class HbCfDefaultConfigDTO {
    private String id;
    private String iaasType;
    private String deploymentName;
    private String defaultConfigName;
    private int directorInfo;
    private String loginSecret;
    private String domain;
    private String domainOrganization;
    private String releases;
    private String osConfReleases;
    private String loggregatorReleases;
    private String paastaMonitoring;
    private String ingestorIp;
    private String createUserId;
    private Date createDate;
    private String updateUserId;
    private Date updateDate;
    
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
    public String getDeploymentName() {
        return deploymentName;
    }
    public void setDeploymentName(String deploymentName) {
        this.deploymentName = deploymentName;
    }
    public String getDefaultConfigName() {
        return defaultConfigName;
    }
    public void setDefaultConfigName(String defaultConfigName) {
        this.defaultConfigName = defaultConfigName;
    }
    public int getDirectorInfo() {
        return directorInfo;
    }
    public void setDirectorInfo(int directorInfo) {
        this.directorInfo = directorInfo;
    }
    public String getLoginSecret() {
        return loginSecret;
    }
    public void setLoginSecret(String loginSecret) {
        this.loginSecret = loginSecret;
    }
    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }
    public String getDomainOrganization() {
        return domainOrganization;
    }
    public void setDomainOrganization(String domainOrganization) {
        this.domainOrganization = domainOrganization;
    }
    public String getReleases() {
        return releases;
    }
    public void setReleases(String releases) {
        this.releases = releases;
    }
    public String getOsConfReleases() {
        return osConfReleases;
    }
    public void setOsConfReleases(String osConfReleases) {
        this.osConfReleases = osConfReleases;
    }
    public String getLoggregatorReleases() {
        return loggregatorReleases;
    }
    public void setLoggregatorReleases(String loggregatorReleases) {
        this.loggregatorReleases = loggregatorReleases;
    }
    public String getPaastaMonitoring() {
        return paastaMonitoring;
    }
    public void setPaastaMonitoring(String paastaMonitoring) {
        this.paastaMonitoring = paastaMonitoring;
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
}
