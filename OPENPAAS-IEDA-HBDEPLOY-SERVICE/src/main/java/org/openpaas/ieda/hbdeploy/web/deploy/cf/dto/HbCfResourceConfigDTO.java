package org.openpaas.ieda.hbdeploy.web.deploy.cf.dto;

public class HbCfResourceConfigDTO {
    
    private String id;
    private String iaasType;
    private String resourceConfigName;
    private String directorInfo;
    private String stemcellName;
    private String stemcellVersion;
    private String boshPassword;
    private String smallFlavor;
    private String mediumFlavor;
    private String largeFlavor;

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
    public String getResourceConfigName() {
        return resourceConfigName;
    }
    public void setResourceConfigName(String resourceConfigName) {
        this.resourceConfigName = resourceConfigName;
    }
    public String getDirectorInfo() {
        return directorInfo;
    }
    public void setDirectorInfo(String directorInfo) {
        this.directorInfo = directorInfo;
    }
    public String getStemcellName() {
        return stemcellName;
    }
    public void setStemcellName(String stemcellName) {
        this.stemcellName = stemcellName;
    }
    public String getStemcellVersion() {
        return stemcellVersion;
    }
    public void setStemcellVersion(String stemcellVersion) {
        this.stemcellVersion = stemcellVersion;
    }
    public String getBoshPassword() {
        return boshPassword;
    }
    public void setBoshPassword(String boshPassword) {
        this.boshPassword = boshPassword;
    }
    public String getSmallFlavor() {
        return smallFlavor;
    }
    public void setSmallFlavor(String smallFlavor) {
        this.smallFlavor = smallFlavor;
    }
    public String getMediumFlavor() {
        return mediumFlavor;
    }
    public void setMediumFlavor(String mediumFlavor) {
        this.mediumFlavor = mediumFlavor;
    }
    public String getLargeFlavor() {
        return largeFlavor;
    }
    public void setLargeFlavor(String largeFlavor) {
        this.largeFlavor = largeFlavor;
    }

}
