package org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dto;

import javax.validation.constraints.NotNull;

public class HbCfDeploymentNetworkConfigDTO {
    
    @NotNull
    private Integer id;
    private String iaasType;
    private String networkName;
    private String direction; //network direction :internal or external
    private String publicStaticIp;
    private String subnetId;
    private String securityGroup;
    private String subnetRange;
    private String subnetGateway;
    private String subnetDns;
    private String subnetRange2;
    private String subnetGateway2;
    private String subnetDns2;
    private String subnetReservedFrom;
    private String subnetReservedTo;
    private String subnetReservedFrom2;
    private String subnetReservedTo2;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getIaasType() {
		return iaasType;
	}
	public void setIaasType(String iaasType) {
		this.iaasType = iaasType;
	}
	public String getNetworkName() {
        return networkName;
    }
    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }
    public String getDirection() {
        return direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }
    public String getPublicStaticIp() {
        return publicStaticIp;
    }
    public void setPublicStaticIp(String publicStaticIp) {
        this.publicStaticIp = publicStaticIp;
    }
    public String getSubnetId() {
        return subnetId;
    }
    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }
    public String getSecurityGroup() {
        return securityGroup;
    }
    public void setSecurityGroup(String securityGroup) {
        this.securityGroup = securityGroup;
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
    public String getSubnetRange2() {
		return subnetRange2;
	}
	public void setSubnetRange2(String subnetRange2) {
		this.subnetRange2 = subnetRange2;
	}
	public String getSubnetGateway2() {
		return subnetGateway2;
	}
	public void setSubnetGateway2(String subnetGateway2) {
		this.subnetGateway2 = subnetGateway2;
	}
	public String getSubnetDns2() {
		return subnetDns2;
	}
	public void setSubnetDns2(String subnetDns2) {
		this.subnetDns2 = subnetDns2;
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
	public String getSubnetReservedFrom2() {
		return subnetReservedFrom2;
	}
	public void setSubnetReservedFrom2(String subnetReservedFrom2) {
		this.subnetReservedFrom2 = subnetReservedFrom2;
	}
	public String getSubnetReservedTo2() {
		return subnetReservedTo2;
	}
	public void setSubnetReservedTo2(String subnetReservedTo2) {
		this.subnetReservedTo2 = subnetReservedTo2;
	}
    
}
