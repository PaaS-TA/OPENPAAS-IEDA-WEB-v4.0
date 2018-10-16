package org.openpaas.ieda.hbdeploy.web.deploy.cf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface HbCfKeyConfigDAO {
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Key 정보 목록 전체 조회
     * @title : selectKeyConfigInfoList
     * @return : List<HbCfDefaultConfigVO>
    *****************************************************************/
    List<HbCfDefaultConfigVO> selectKeyConfigInfoList();
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Key 정보 목록 전체 조회
     * @title : selectCfKeyConfigInfoByName
     * @return : int
    *****************************************************************/
	int selectCfKeyConfigInfoByName(@Param("keyConfigName") String keyConfigName);

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Key 정보 목록 전체 조회
     * @title : selectCfKeyConfigInfoById
     * @return : HbCfKeyConfigVO
    *****************************************************************/
	HbCfKeyConfigVO selectCfKeyConfigInfoById(@Param("id") int id);

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Key 정보 삽입
     * @title : insertCfKeyConfigInfo
     * @return : void
    *****************************************************************/
	void insertCfKeyConfigInfo(@Param("vo") HbCfKeyConfigVO vo);

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Key 정보 수정
     * @title : updateCfKeyConfigInfo
     * @return : void
    *****************************************************************/
	void updateCfKeyConfigInfo(@Param("vo") HbCfKeyConfigVO vo);
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Key 정보 삭제
     * @title : deleteCfKeyConfigInfo
     * @return : void
    *****************************************************************/
	void deleteCfKeyConfigInfo(@Param("id") int id);

}
