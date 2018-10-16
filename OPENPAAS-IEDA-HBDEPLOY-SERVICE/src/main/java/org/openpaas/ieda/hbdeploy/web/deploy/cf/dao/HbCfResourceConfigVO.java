package org.openpaas.ieda.hbdeploy.web.deploy.cf.dao;

import java.sql.Date;

public class HbCfResourceConfigVO {
    
    private Integer id;
    private Integer recid;
    private String resourceConfigName;
    private String iaasType;
    private String boshPassword;//VM 비밀번호
    private String stemcellName;//스템셀명
    private String stemcellVersion;//스템셀 버전
    private String smallFlavor;//small Resource type
    private String mediumFlavor;//medium Resource type
    private String largeFlavor;//large Resource type
    private String directorId;
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
    public String getResourceConfigName() {
        return resourceConfigName;
    }
    public void setResourceConfigName(String resourceConfigName) {
        this.resourceConfigName = resourceConfigName;
    }
    public String getIaasType() {
        return iaasType;
    }
    public void setIaasType(String iaasType) {
        this.iaasType = iaasType;
    }
    public String getBoshPassword() {
        return boshPassword;
    }
    public void setBoshPassword(String boshPassword) {
        this.boshPassword = boshPassword;
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
    public String getDirectorId() {
        return directorId;
    }
    public void setDirectorId(String directorId) {
        this.directorId = directorId;
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
