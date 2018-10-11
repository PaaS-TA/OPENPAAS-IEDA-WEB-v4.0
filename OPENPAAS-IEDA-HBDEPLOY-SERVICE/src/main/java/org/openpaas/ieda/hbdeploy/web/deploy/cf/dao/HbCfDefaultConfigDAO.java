package org.openpaas.ieda.hbdeploy.web.deploy.cf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfDefaultConfigDTO;

public interface HbCfDefaultConfigDAO {
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 기본 정보 목록 전체 조회
     * @title : selectDefaultConfigInfoList
     * @return : List<HbCfDefaultConfigVO> 
    *****************************************************************/
    List<HbCfDefaultConfigVO> selectCfDefaultConfigInfoList();
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 별칭 기준 CF 기본 정보 Count 조회
     * @title : selectCfDefaultConfigInfoByName
     * @return : int
    *****************************************************************/
    int selectCfDefaultConfigInfoByName(@Param("defaultConfigName") String defaultConfigName);

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 아이디 기준 CF 기본 정보 조회
     * @title : selectCfDefaultInfoById
     * @return : HbCfDefaultConfigVO
    *****************************************************************/
    HbCfDefaultConfigVO selectCfDefaultInfoById(@Param("id") int id);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 아이디 기준 CF 기본 정보 삽입
     * @title : insertCfDefaultInfo
     * @return : void
    *****************************************************************/
	void insertCfDefaultInfo(@Param("vo") HbCfDefaultConfigVO vo);

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 아이디 기준 CF 기본 정보 수정
     * @title : updateCfDefaultInfo
     * @return : void
    *****************************************************************/
	void updateCfDefaultInfo(@Param("vo") HbCfDefaultConfigVO vo);

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 아이디 기준 CF 기본 정보 삭제
     * @title : deleteCfDefaultConfigInfo
     * @return : void
    *****************************************************************/
	void deleteCfDefaultConfigInfo(@Param("dto") HbCfDefaultConfigDTO dto);
    
}
