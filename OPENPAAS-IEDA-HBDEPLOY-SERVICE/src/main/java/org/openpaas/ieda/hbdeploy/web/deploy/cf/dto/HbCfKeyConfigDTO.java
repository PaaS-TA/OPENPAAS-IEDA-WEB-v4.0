package org.openpaas.ieda.hbdeploy.web.deploy.cf.dto;

public class HbCfKeyConfigDTO {
    
    private String id;
    private String iaasType;
    private String keyConfigName;
    private String keyFileName;
    private String defaultConfig;
    private String countryCode;
    private String domain;
    private String city;
    private String company;
    private String jobTitle;
    private String releaseName;
    private String releaseVersion;
    private String emailAddress;
    
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
    public String getKeyConfigName() {
        return keyConfigName;
    }
    public void setKeyConfigName(String keyConfigName) {
        this.keyConfigName = keyConfigName;
    }
    public String getKeyFileName() {
        return keyFileName;
    }
    public void setKeyFileName(String keyFileName) {
        this.keyFileName = keyFileName;
    }
    public String getDefaultConfig() {
        return defaultConfig;
    }
    public void setDefaultConfig(String defaultConfig) {
        this.defaultConfig = defaultConfig;
    }
    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
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
    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
