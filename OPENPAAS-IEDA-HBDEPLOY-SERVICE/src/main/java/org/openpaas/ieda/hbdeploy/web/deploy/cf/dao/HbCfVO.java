package org.openpaas.ieda.hbdeploy.web.deploy.cf.dao;

import java.util.List;

public class HbCfVO {
    
    private int id;
    private int recid;
    private String cfConfigName;
    private String iaasType;
    private String networkConfigInfo;
    private String keyConfigInfo;
    private String defaultConfigInfo;
    private String resourceConfigInfo;
    private String instanceConfigInfo;
    private String taskId;
    private String deployStatus;
    private String deploymentFile;
    
    private HbCfDefaultConfigVO defaultConfigVO;
    private List<HbCfNetworkConfigVO> networkConfigVO;
    private HbCfKeyConfigVO keyConfigVO;
    private HbCfResourceConfigVO resourceConfigVO;
    private HbCfInstanceConfigVO instanceConfigVO;
    
    
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
    public String getCfConfigName() {
        return cfConfigName;
    }
    public void setCfConfigName(String cfConfigName) {
        this.cfConfigName = cfConfigName;
    }
    public String getIaasType() {
        return iaasType;
    }
    public void setIaasType(String iaasType) {
        this.iaasType = iaasType;
    }
    public String getNetworkConfigInfo() {
        return networkConfigInfo;
    }
    public void setNetworkConfigInfo(String networkConfigInfo) {
        this.networkConfigInfo = networkConfigInfo;
    }
    public String getKeyConfigInfo() {
        return keyConfigInfo;
    }
    public void setKeyConfigInfo(String keyConfigInfo) {
        this.keyConfigInfo = keyConfigInfo;
    }
    public String getDefaultConfigInfo() {
        return defaultConfigInfo;
    }
    public void setDefaultConfigInfo(String defaultConfigInfo) {
        this.defaultConfigInfo = defaultConfigInfo;
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
    public String getTaskId() {
        return taskId;
    }
    public void setTaskId(String taskId) {
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
    public HbCfDefaultConfigVO getDefaultConfigVO() {
        return defaultConfigVO;
    }
    public void setDefaultConfigVO(HbCfDefaultConfigVO defaultConfigVO) {
        this.defaultConfigVO = defaultConfigVO;
    }
    public List<HbCfNetworkConfigVO> getNetworkConfigVO() {
        return networkConfigVO;
    }
    public void setNetworkConfigVO(List<HbCfNetworkConfigVO> networkConfigVO) {
        this.networkConfigVO = networkConfigVO;
    }
    public HbCfKeyConfigVO getKeyConfigVO() {
        return keyConfigVO;
    }
    public void setKeyConfigVO(HbCfKeyConfigVO keyConfigVO) {
        this.keyConfigVO = keyConfigVO;
    }
    public HbCfResourceConfigVO getResourceConfigVO() {
        return resourceConfigVO;
    }
    public void setResourceConfigVO(HbCfResourceConfigVO resourceConfigVO) {
        this.resourceConfigVO = resourceConfigVO;
    }
    public HbCfInstanceConfigVO getInstanceConfigVO() {
        return instanceConfigVO;
    }
    public void setInstanceConfigVO(HbCfInstanceConfigVO instanceConfigVO) {
        this.instanceConfigVO = instanceConfigVO;
    }
}
