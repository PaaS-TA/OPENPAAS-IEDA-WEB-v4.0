package org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface HbCfDeploymentDAO {
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Deployment 전체 정보 목록 조회
     * @title : selectCfDeploymentList
     * @return : List<HbCfDeploymentVO> 
    *****************************************************************/
	List<HbCfDeploymentVO> selectCfDeploymentList(@Param("installStatus")String installStatus);
	
	
	
}
