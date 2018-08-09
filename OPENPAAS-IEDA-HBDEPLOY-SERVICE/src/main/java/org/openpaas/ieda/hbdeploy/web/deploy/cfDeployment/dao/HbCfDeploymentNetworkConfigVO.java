package org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao;

import java.sql.Array;
import java.util.Date;

import javax.validation.constraints.NotNull;

public class HbCfDeploymentNetworkConfigVO {
    @NotNull
    private Integer id;
    @NotNull
    private Integer recid;
    private String iaasType;
    private String networkName;
    private String direction; //network direction :internal or external
    private String publicStaticIp;
    private Array subnetId;
    private Array securityGroup;
    private Array subnetRange;
    private Array subnetGateway;
    private Array subnetDns;
    private Array subnetReservedFrom;
    private Array subnetReservedTo;
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
    public Integer getRecid() {
        return recid;
    }
    public void setRecid(Integer recid) {
        this.recid = recid;
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
