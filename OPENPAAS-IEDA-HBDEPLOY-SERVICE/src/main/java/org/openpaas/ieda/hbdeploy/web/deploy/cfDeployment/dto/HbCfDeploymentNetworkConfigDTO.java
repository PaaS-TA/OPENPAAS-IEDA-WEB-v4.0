package org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dto;

import java.sql.Array;
import java.util.Date;

import javax.validation.constraints.NotNull;

public class HbCfDeploymentNetworkConfigDTO {
    
    @NotNull
    private Integer id;
    private String iaasType;
    private String networkName;
    private String direction; //network direction :internal or external
    private String publicStaticIp;
    private Array subnetId;
    private Array securityGroup;
    private Array subnetRange;
    private Array subnetGateway;
    private Array subnetDns;
    private Array subnetReservedIp;
    private Array subnetReservedFrom;
    private Array subnetReservedTo;
    private Array subnetStaticIp;
    private Array subnetStaticFrom;
    private Array subnetStaticTo;
    private Array availabilityZone;
    private String subnetIdString;
    private String securityGroupString;
    private String subnetRangeString;
    private String subnetGatewayString;
    private String subnetDnsString;
    private String subnetReservedIpString;
    private String subnetReservedFromString;
    private String subnetReservedToString;
    private String subnetStaticIpString;
    private String subnetStaticFromString;
    private String subnetStaticToString;
    private String availabilityZoneString;
	private String createUserId;//등록자 아이디
    private String updateUserId;//수정자 아이디
    private Date createDate;//등록일
    private Date updateDate;//수정일
    
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


	public Array getSubnetId() {
		return subnetId;
	}
	public void setSubnetId(Array subnetId) {
		this.subnetId = subnetId;
	}
	public Array getSubnetStaticTo() {
		return subnetStaticTo;
	}
	public void setSubnetStaticTo(Array subnetStaticTo) {
		this.subnetStaticTo = subnetStaticTo;
	}
	public Array getAvailabilityZone() {
		return availabilityZone;
	}
	public void setAvailabilityZone(Array availabilityZone) {
		this.availabilityZone = availabilityZone;
	}
	public void setSubnetStaticFrom(Array subnetStaticFrom) {
		this.subnetStaticFrom = subnetStaticFrom;
	}
	public String getSubnetIdString() {
		return subnetIdString;
	}
	public void setSubnetIdString(String subnetIdString) {
		this.subnetIdString = subnetIdString;
	}
	public String getSecurityGroupString() {
		return securityGroupString;
	}
	public void setSecurityGroupString(String securityGroupString) {
		this.securityGroupString = securityGroupString;
	}
	public String getSubnetRangeString() {
		return subnetRangeString;
	}
	public void setSubnetRangeString(String subnetRangeString) {
		this.subnetRangeString = subnetRangeString;
	}
	public String getSubnetGatewayString() {
		return subnetGatewayString;
	}
	public void setSubnetGatewayString(String subnetGatewayString) {
		this.subnetGatewayString = subnetGatewayString;
	}
	public String getSubnetDnsString() {
		return subnetDnsString;
	}
	public void setSubnetDnsString(String subnetDnsString) {
		this.subnetDnsString = subnetDnsString;
	}
	public String getSubnetReservedIpString() {
		return subnetReservedIpString;
	}
	public void setSubnetReservedIpString(String subnetReservedIpString) {
		this.subnetReservedIpString = subnetReservedIpString;
	}
	public String getSubnetReservedFromString() {
		return subnetReservedFromString;
	}
	public void setSubnetReservedFromString(String subnetReservedFromString) {
		this.subnetReservedFromString = subnetReservedFromString;
	}
	public String getSubnetReservedToString() {
		return subnetReservedToString;
	}
	public void setSubnetReservedToString(String subnetReservedToString) {
		this.subnetReservedToString = subnetReservedToString;
	}
	public String getSubnetStaticIpString() {
		return subnetStaticIpString;
	}
	public void setSubnetStaticIpString(String subnetStaticIpString) {
		this.subnetStaticIpString = subnetStaticIpString;
	}
	public String getSubnetStaticFromString() {
		return subnetStaticFromString;
	}
	public void setSubnetStaticFromString(String subnetStaticFromString) {
		this.subnetStaticFromString = subnetStaticFromString;
	}
	public String getSubnetStaticToString() {
		return subnetStaticToString;
	}
	public void setSubnetStaticToString(String subnetStaticToString) {
		this.subnetStaticToString = subnetStaticToString;
	}
	public String getAvailabilityZoneString() {
		return availabilityZoneString;
	}
	public void setAvailabilityZoneString(String availabilityZoneString) {
		this.availabilityZoneString = availabilityZoneString;
	}
	public Array getSecurityGroup() {
		return securityGroup;
	}
	public void setSecurityGroup(Array securityGroup) {
		this.securityGroup = securityGroup;
	}
	public Array getSubnetRange() {
		return subnetRange;
	}

	public void setSubnetRange(Array subnetRange) {
		this.subnetRange = subnetRange;
	}
	public Array getSubnetGateway() {
		return subnetGateway;
	}
	public void setSubnetGateway(Array subnetGateway) {
		this.subnetGateway = subnetGateway;
	}
	public Array getSubnetDns() {
		return subnetDns;
	}
	public void setSubnetDns(Array subnetDns) {
		this.subnetDns = subnetDns;
	}
	public Array getSubnetReservedIp() {
		return subnetReservedIp;
	}
	public void setSubnetReservedIp(Array subnetReservedIp) {
		this.subnetReservedIp = subnetReservedIp;
	}
	public Array getSubnetReservedFrom() {
		return subnetReservedFrom;
	}
	public void setSubnetReservedFrom(Array subnetReservedFrom) {
		this.subnetReservedFrom = subnetReservedFrom;
	}
	public Array getSubnetReservedTo() {
		return subnetReservedTo;
	}
	public void setSubnetReservedTo(Array subnetReservedTo) {
		this.subnetReservedTo = subnetReservedTo;
	}
	public Array getSubnetStaticIp() {
		return subnetStaticIp;
	}
	public void setSubnetStaticIp(Array subnetStaticIp) {
		this.subnetStaticIp = subnetStaticIp;
	}
	public Array getSubnetStaticFrom() {
		return subnetStaticFrom;
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
