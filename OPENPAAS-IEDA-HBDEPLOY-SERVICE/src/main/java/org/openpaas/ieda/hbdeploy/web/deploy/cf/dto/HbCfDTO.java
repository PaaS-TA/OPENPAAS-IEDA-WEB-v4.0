package org.openpaas.ieda.hbdeploy.web.deploy.cf.dto;

public class HbCfDTO {
    
    private String id;
    private String cfConfigName;
    private String iaasType;
    private String networkConfigInfo;
    private String keyConfigInfo;
    private String defaultConfigInfo;
    private String resourceConfigInfo;
    private String instanceConfigInfo;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    
    
}
