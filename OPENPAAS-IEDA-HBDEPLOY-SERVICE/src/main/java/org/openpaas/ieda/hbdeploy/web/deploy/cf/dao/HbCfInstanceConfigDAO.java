package org.openpaas.ieda.hbdeploy.web.deploy.cf.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface HbCfInstanceConfigDAO {
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 인스턴스 목록 정보 조회
     * @title : selectInstanceConfigList
     * @return : List<HbCfInstanceConfigVO>
    *****************************************************************/
	List<HbCfInstanceConfigVO> selectInstanceConfigList();
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 인스턴스 Job Template 조회
     * @title : selectCfJobTemplatesByReleaseVersion
     * @return : List<HashMap<String, String>>
    *****************************************************************/
	List<HashMap<String, String>> selectCfJobTemplatesByReleaseVersion(@Param("map") HashMap<String, String> map);
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 인스턴스 정보 중복 검사
     * @title : selectCfInstanceConfigByName
     * @return : int
    *****************************************************************/
	int selectInstanceConfigByName(@Param("instanceConfigName") String instanceConfigName);
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 인스턴스 정보 상세 조회
     * @title : selectCfInstanceConfigById
     * @return : HbCfInstanceConfigVO
    *****************************************************************/
	HbCfInstanceConfigVO selectInstanceConfigById(@Param("id") int id);
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 인스턴스 정보 삽입
     * @title : insertInstanceConfg
     * @return : void
    *****************************************************************/
	void insertInstanceConfg(@Param("vo") HbCfInstanceConfigVO vo);

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 인스턴스 정보 수정
     * @title : updateInstanceConfig
     * @return : void
    *****************************************************************/
	void updateInstanceConfig(@Param("vo") HbCfInstanceConfigVO vo);
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 인스턴스 정보 삭제
     * @title : deleteInstanceConfig
     * @return : void
    *****************************************************************/
	void deleteInstanceConfig(@Param("id") int id);
	
}
