package org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao;

import java.util.Date;

public class HbCfDeploymentInstanceConfigVO {
    private Integer id;
    private Integer recid;
    private String cfId; // 
    private String iaasType; // 클라우드 인프라 환경 타입
    
    private String adapter;
    private String api;
    private String cc_worker;
    private String consul;
    private String database;
    private String diego_api;
    private String diego_cell;
    private String haproxy;
    

    private String log_api;
    private String nats;
    private String router;
    private String scheduler;
    private String singleton_blobstore;
    private String tcp_router;
    private String uaa;

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
	public String getCfId() {
		return cfId;
	}
	public void setCfId(String cfId) {
		this.cfId = cfId;
	}
	public String getIaasType() {
		return iaasType;
	}
	public void setIaasType(String iaasType) {
		this.iaasType = iaasType;
	}
	public String getAdapter() {
		return adapter;
	}
	public void setAdapter(String adapter) {
		this.adapter = adapter;
	}
	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}
	public String getCc_worker() {
		return cc_worker;
	}
	public void setCc_worker(String cc_worker) {
		this.cc_worker = cc_worker;
	}
	public String getConsul() {
		return consul;
	}
	public void setConsul(String consul) {
		this.consul = consul;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getDiego_api() {
		return diego_api;
	}
	public void setDiego_api(String diego_api) {
		this.diego_api = diego_api;
	}
	public String getDiego_cell() {
		return diego_cell;
	}
	public void setDiego_cell(String diego_cell) {
		this.diego_cell = diego_cell;
	}
	public String getHaproxy() {
		return haproxy;
	}
	public void setHaproxy(String haproxy) {
		this.haproxy = haproxy;
	}
	public String getLog_api() {
		return log_api;
	}
	public void setLog_api(String log_api) {
		this.log_api = log_api;
	}
	public String getNats() {
		return nats;
	}
	public void setNats(String nats) {
		this.nats = nats;
	}
	public String getRouter() {
		return router;
	}
	public void setRouter(String router) {
		this.router = router;
	}
	public String getScheduler() {
		return scheduler;
	}
	public void setScheduler(String scheduler) {
		this.scheduler = scheduler;
	}
	public String getSingleton_blobstore() {
		return singleton_blobstore;
	}
	public void setSingleton_blobstore(String singleton_blobstore) {
		this.singleton_blobstore = singleton_blobstore;
	}
	public String getTcp_router() {
		return tcp_router;
	}
	public void setTcp_router(String tcp_router) {
		this.tcp_router = tcp_router;
	}
	public String getUaa() {
		return uaa;
	}
	public void setUaa(String uaa) {
		this.uaa = uaa;
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
