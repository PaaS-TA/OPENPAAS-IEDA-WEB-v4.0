package org.openpaas.ieda.hbdeploy.web.deploy.diego.dao;

import java.sql.Date;
import java.util.List;

public class HbDiegoVO {
    
    private int id;
    private int recid;
    private String iaasType;
    private String diegoConfigName;
    private String defaultConfigInfo;
    private String networkConfigInfo;
    private String resourceConfigInfo;
    private String instanceConfigInfo;
    
    
    private int taskId;
    private String deployStatus;
    private String deploymentFile;
    
    private HbDiegoDefaultConfigVO defaultConfigVO;
    private List<HbDiegoNetworkConfigVO> networks;
    private HbDiegoResourceConfigVO resourceConfigVO;
    private HbDiegoInstanceConfigVO instanceConfigVO;
    
    private String createUserId;
    private Date createDate;
    private String updateUserId;
    private Date updateDate;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getRecid() {
        return recid;
    }
    public void setRecid(int recid) {
        this.recid = recid;
    }
    public String getIaasType() {
        return iaasType;
    }
    public void setIaasType(String iaasType) {
        this.iaasType = iaasType;
    }
    public String getDiegoConfigName() {
        return diegoConfigName;
    }
    public void setDiegoConfigName(String diegoConfigName) {
        this.diegoConfigName = diegoConfigName;
    }
    public String getDefaultConfigInfo() {
        return defaultConfigInfo;
    }
    public void setDefaultConfigInfo(String defaultConfigInfo) {
        this.defaultConfigInfo = defaultConfigInfo;
    }
    public String getNetworkConfigInfo() {
        return networkConfigInfo;
    }
    public void setNetworkConfigInfo(String networkConfigInfo) {
        this.networkConfigInfo = networkConfigInfo;
    }
    public String getResourceConfigInfo() {
        return resourceConfigInfo;
    }
    public void setResourceConfigInfo(String resourceConfigInfo) {
        this.resourceConfigInfo = resourceConfigInfo;
    }
    public String getInstanceConfigInfo() {
        return instanceConfigInfo;
    }
    public void setInstanceConfigInfo(String instanceConfigInfo) {
        this.instanceConfigInfo = instanceConfigInfo;
    }
    public int getTaskId() {
        return taskId;
    }
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
    public String getDeployStatus() {
        return deployStatus;
    }
    public void setDeployStatus(String deployStatus) {
        this.deployStatus = deployStatus;
    }
    public String getDeploymentFile() {
        return deploymentFile;
    }
    public void setDeploymentFile(String deploymentFile) {
        this.deploymentFile = deploymentFile;
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
    public HbDiegoDefaultConfigVO getDefaultConfigVO() {
        return defaultConfigVO;
    }
    public void setDefaultConfigVO(HbDiegoDefaultConfigVO defaultConfigVO) {
        this.defaultConfigVO = defaultConfigVO;
    }
    public List<HbDiegoNetworkConfigVO> getNetworks() {
        return networks;
    }
    public void setNetworks(List<HbDiegoNetworkConfigVO> networks) {
        this.networks = networks;
    }
    public HbDiegoResourceConfigVO getResourceConfigVO() {
        return resourceConfigVO;
    }
    public void setResourceConfigVO(HbDiegoResourceConfigVO resourceConfigVO) {
        this.resourceConfigVO = resourceConfigVO;
    }
    public HbDiegoInstanceConfigVO getInstanceConfigVO() {
        return instanceConfigVO;
    }
    public void setInstanceConfigVO(HbDiegoInstanceConfigVO instanceConfigVO) {
        this.instanceConfigVO = instanceConfigVO;
    }

}
