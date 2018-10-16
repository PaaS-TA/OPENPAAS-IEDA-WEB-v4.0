package org.openpaas.ieda.hbdeploy.web.deploy.cf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface HbCfDAO {
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 정보 목록 전체 조회
     * @title : selectHbCfInfoList
     * @return : List<HbCfVO>
    *****************************************************************/
	List<HbCfVO> selectHbCfInfoList(@Param("installStatus") String installStatus);

}
