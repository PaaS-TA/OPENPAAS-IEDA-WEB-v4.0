package org.openpaas.ieda.hbdeploy.web.deploy.diego.dto;

public class HbDiegoDTO {
    
    private String id;
    private String recid;
    private String iaasType;
    private String diegoConfigName;
    private String defaultConfigInfo;
    private String networkConfigInfo;
    private String resourceConfigInfo;
    private String instanceConfigInfo;
    private int taskId;
    private String deployStatus;
    private String deploymentFile;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRecid() {
        return recid;
    }
    public void setRecid(String recid) {
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
    
    
}
