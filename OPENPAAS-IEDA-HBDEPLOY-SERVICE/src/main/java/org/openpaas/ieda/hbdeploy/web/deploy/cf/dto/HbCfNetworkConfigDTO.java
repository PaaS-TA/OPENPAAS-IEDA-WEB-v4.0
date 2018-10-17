package org.openpaas.ieda.hbdeploy.web.deploy.cf.dto;

public class HbCfNetworkConfigDTO {
    private String id;
    private String networkConfigName;
    private String iaasType;//iaas
    private String net;//Internal/External
    private String seq; //시퀀스
    private String publicStaticIP;
    private String subnetRange; //서브넷 범위
    private String subnetGateway; //게이트웨이
    private String subnetDns; //DNS
    private String subnetReservedFrom; //할당된 IP대역 From
    private String subnetReservedTo; //할당된 IP대역 To
    private String subnetStaticFrom; //VM 할당 IP대역 From
    private String subnetStaticTo; //VM 할당 IP대역 To
    private String subnetId; //네트워크 ID
    private String cloudSecurityGroups; //시큐리티 그룹
    private String availabilityZone;//availabilityZone
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNetworkConfigName() {
        return networkConfigName;
    }
    public void setNetworkConfigName(String networkConfigName) {
        this.networkConfigName = networkConfigName;
    }
    public String getIaasType() {
        return iaasType;
    }
    public void setIaasType(String iaasType) {
        this.iaasType = iaasType;
    }
    public String getNet() {
        return net;
    }
    public void setNet(String net) {
        this.net = net;
    }
    public String getSeq() {
        return seq;
    }
    public void setSeq(String seq) {
        this.seq = seq;
    }
    public String getPublicStaticIP() {
        return publicStaticIP;
    }
    public void setPublicStaticIP(String publicStaticIP) {
        this.publicStaticIP = publicStaticIP;
    }
    public String getSubnetRange() {
        return subnetRange;
    }
    public void setSubnetRange(String subnetRange) {
        this.subnetRange = subnetRange;
    }
    public String getSubnetGateway() {
        return subnetGateway;
    }
    public void setSubnetGateway(String subnetGateway) {
        this.subnetGateway = subnetGateway;
    }
    public String getSubnetDns() {
        return subnetDns;
    }
    public void setSubnetDns(String subnetDns) {
        this.subnetDns = subnetDns;
    }
    public String getSubnetReservedFrom() {
        return subnetReservedFrom;
    }
    public void setSubnetReservedFrom(String subnetReservedFrom) {
        this.subnetReservedFrom = subnetReservedFrom;
    }
    public String getSubnetReservedTo() {
        return subnetReservedTo;
    }
    public void setSubnetReservedTo(String subnetReservedTo) {
        this.subnetReservedTo = subnetReservedTo;
    }
    public String getSubnetStaticFrom() {
        return subnetStaticFrom;
    }
    public void setSubnetStaticFrom(String subnetStaticFrom) {
        this.subnetStaticFrom = subnetStaticFrom;
    }
    public String getSubnetStaticTo() {
        return subnetStaticTo;
    }
    public void setSubnetStaticTo(String subnetStaticTo) {
        this.subnetStaticTo = subnetStaticTo;
    }
    public String getSubnetId() {
        return subnetId;
    }
    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }
    public String getCloudSecurityGroups() {
        return cloudSecurityGroups;
    }
    public void setCloudSecurityGroups(String cloudSecurityGroups) {
        this.cloudSecurityGroups = cloudSecurityGroups;
    }
    public String getAvailabilityZone() {
        return availabilityZone;
    }
    public void setAvailabilityZone(String availabilityZone) {
        this.availabilityZone = availabilityZone;
    }
}
