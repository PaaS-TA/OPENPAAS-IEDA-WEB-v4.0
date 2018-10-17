package org.openpaas.ieda.hbdeploy.web.deploy.diego.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface HbDiegoResourceConfigDAO {
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 리소스 정보 목록 조회
     * @title : selectResourceConfigInfoList
     * @return : List<HbDiegoResourceConfigVO>
    *****************************************************************/
	List<HbDiegoResourceConfigVO> selectResourceConfigInfoList();
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 리소스 정보 중복 검사
     * @title : selectResourceConfigInfoByName
     * @return : int
    *****************************************************************/
	int selectResourceConfigInfoByName(@Param("resourceConfigName") String resourceConfigName);
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 리소스 정보 상세 조회
     * @title : selectResourceConfigInfoById
     * @return : HbDiegoResourceConfigVO
    *****************************************************************/
	HbDiegoResourceConfigVO selectResourceConfigInfoById(@Param("id") int id);

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 리소스 정보 삽입
     * @title : insertResourceConfigInfo
     * @return : void
    *****************************************************************/
	void insertResourceConfigInfo(@Param("vo") HbDiegoResourceConfigVO vo);

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 리소스 정보 수정
     * @title : updateResourceConfigInfo
     * @return : void
    *****************************************************************/
	void updateResourceConfigInfo(@Param("vo") HbDiegoResourceConfigVO vo);
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 리소스 정보 삭제
     * @title : deleteResourceConfigInfo
     * @return : void
    *****************************************************************/
	void deleteResourceConfigInfo(@Param("id") int id);
	
}
