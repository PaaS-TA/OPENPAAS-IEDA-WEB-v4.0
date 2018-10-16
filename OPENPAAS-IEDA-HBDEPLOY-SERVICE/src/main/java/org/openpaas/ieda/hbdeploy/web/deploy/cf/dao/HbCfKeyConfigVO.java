package org.openpaas.ieda.hbdeploy.web.deploy.cf.dao;

import java.sql.Date;

public class HbCfKeyConfigVO {
    
    private Integer id;
    private Integer recid;
    private String keyConfigName;
    private String defaultConfigInfo;
    private String keyFileName;
    private String iaasType; // 클라우드 인프라 환경 타입
    private String domain;
    private String countryCode;
    private String city;
    private String company;
    private String jobTitle;
    private String emailAddress;
    private String releaseName;
    private String releaseVersion;
    private String createUserId;
    private String updateUserId;
    private Date createDate;
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
    public String getKeyConfigName() {
        return keyConfigName;
    }
    public void setKeyConfigName(String keyConfigName) {
        this.keyConfigName = keyConfigName;
    }
    public String getDefaultConfigInfo() {
        return defaultConfigInfo;
    }
    public void setDefaultConfigInfo(String defaultConfigInfo) {
        this.defaultConfigInfo = defaultConfigInfo;
    }
    public String getKeyFileName() {
        return keyFileName;
    }
    public void setKeyFileName(String keyFileName) {
        this.keyFileName = keyFileName;
    }
    public String getIaasType() {
        return iaasType;
    }
    public void setIaasType(String iaasType) {
        this.iaasType = iaasType;
    }
    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }
    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getJobTitle() {
        return jobTitle;
    }
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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
    public String getCreateUserId() {
        return createUserId;
    }
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
    public String getUpdateUserId() {
        return updateUserId;
    }
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
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
}
