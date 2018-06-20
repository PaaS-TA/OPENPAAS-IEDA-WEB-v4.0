package org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dto;

import javax.validation.constraints.NotNull;

public class HbBootstrapDefaultConfigDTO {
    
    @NotNull
    private String id; //id
    @NotNull
    private String deploymentName; //배포명
    @NotNull
    private String directorName; //디렉터 명
    @NotNull
    private String credentialKeyName;
    @NotNull
    private String boshRelease; //bosh 릴리즈
    @NotNull
    private String ntp; //내부 NTP
    @NotNull
    private String boshCpiRelease; //bosh cpi 릴리즈
    @NotNull
    private String enableSnapshots;//스냅샷 사용 유무
    @NotNull
    private String snapshotSchedule;//스냅샷 스케줄
    private String paastaMonitoringUse;// PaaS-TA 모니터링 사용 유무
    private String paastaMonitoringIp; //PaaS-TA 모니터링 사용시 ingrestorIP
    private String influxdbIp;
    private String paastaMonitoringRelease; //PaaS-TA 모니터링 사용시 릴리즈
    private String osConfRelease;
    private String iaasType;
    private String defaultConfigName;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDeploymentName() {
        return deploymentName;
    }
    public void setDeploymentName(String deploymentName) {
        this.deploymentName = deploymentName;
    }
    public String getDirectorName() {
        return directorName;
    }
    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }
    public String getCredentialKeyName() {
        return credentialKeyName;
    }
    public void setCredentialKeyName(String credentialKeyName) {
        this.credentialKeyName = credentialKeyName;
    }
    public String getBoshRelease() {
        return boshRelease;
    }
    public void setBoshRelease(String boshRelease) {
        this.boshRelease = boshRelease;
    }
    public String getNtp() {
        return ntp;
    }
    public void setNtp(String ntp) {
        this.ntp = ntp;
    }
    public String getBoshCpiRelease() {
        return boshCpiRelease;
    }
    public void setBoshCpiRelease(String boshCpiRelease) {
        this.boshCpiRelease = boshCpiRelease;
    }
    public String getEnableSnapshots() {
        return enableSnapshots;
    }
    public void setEnableSnapshots(String enableSnapshots) {
        this.enableSnapshots = enableSnapshots;
    }
    public String getSnapshotSchedule() {
        return snapshotSchedule;
    }
    public void setSnapshotSchedule(String snapshotSchedule) {
        this.snapshotSchedule = snapshotSchedule;
    }
    public String getPaastaMonitoringUse() {
        return paastaMonitoringUse;
    }
    public void setPaastaMonitoringUse(String paastaMonitoringUse) {
        this.paastaMonitoringUse = paastaMonitoringUse;
    }
    public String getPaastaMonitoringIp() {
        return paastaMonitoringIp;
    }
    public void setPaastaMonitoringIp(String paastaMonitoringIp) {
        this.paastaMonitoringIp = paastaMonitoringIp;
    }
    public String getInfluxdbIp() {
        return influxdbIp;
    }
    public void setInfluxdbIp(String influxdbIp) {
        this.influxdbIp = influxdbIp;
    }
    public String getPaastaMonitoringRelease() {
        return paastaMonitoringRelease;
    }
    public void setPaastaMonitoringRelease(String paastaMonitoringRelease) {
        this.paastaMonitoringRelease = paastaMonitoringRelease;
    }
    public String getOsConfRelease() {
        return osConfRelease;
    }
    public void setOsConfRelease(String osConfRelease) {
        this.osConfRelease = osConfRelease;
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
}
