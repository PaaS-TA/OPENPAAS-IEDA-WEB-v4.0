package org.openpaas.ieda.hbdeploy.web.deploy.cf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface HbCfResourceConfigDAO {
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 리소스 정보 목록 전체 조회
     * @title : selectCfResourceConfigList
     * @return : List<HbCfDefaultConfigVO>
    *****************************************************************/
	List<HbCfResourceConfigVO> selectCfResourceConfigList();
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 리소스 정보 중복 값 확인
     * @title : selectCfResourceConfigInfoByName
     * @return : int
    *****************************************************************/
	int selectCfResourceConfigInfoByName(@Param("resourceConfigName")String resourceConfigName);

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 리소스 정보 상세 조회
     * @title : selectCfResourceConfigInfoById
     * @return : HbCfResourceConfigVO
    *****************************************************************/
	HbCfResourceConfigVO selectCfResourceConfigInfoById(@Param("id") int id);
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 리소스 정보 삭제
     * @title : deleteCfKeyConfigInfo
     * @return : void
    *****************************************************************/
	void deleteCfResourceConfigInfo(@Param("id") int id);
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 리소스 정보 삽입
     * @title : insertCfResourceConfigInfo
     * @return : void
    *****************************************************************/
	void insertCfResourceConfigInfo(@Param("vo") HbCfResourceConfigVO vo);
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 리소스 정보 수정
     * @title : updateCfResourceConfigInfo
     * @return : void
    *****************************************************************/
	void updateCfResourceConfigInfo(@Param("vo") HbCfResourceConfigVO vo);
	

}
