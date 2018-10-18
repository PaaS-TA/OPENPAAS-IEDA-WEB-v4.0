package org.openpaas.ieda.hbdeploy.web.deploy.diego.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface HbDiegoDAO {
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Diego 설치 목록 조회
     * @title : selectHbDiegoInfoList
     * @return : List<HbDiegoVO>
    *****************************************************************/
    List<HbDiegoVO> selectHbDiegoInfoList(@Param("installStatus") String installStatus);

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Diego 설치 정보 중복 값 확인
     * @title : selectHbDiegoInfoByName
     * @return : int
    *****************************************************************/
	int selectHbDiegoInfoByName(@Param("diegoConfigName") String diegoConfigName);
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Diego 설치 정보 상세 조회
     * @title : selectHbDiegoInfoByName
     * @return : HbDiegoVO
    *****************************************************************/
	HbDiegoVO selectHbDiegoInfoById(@Param("id") int id);

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Diego 설치 정보 삽입
     * @title : void
     * @return : void
    *****************************************************************/
	void insertHbDiegoInfo(@Param("vo") HbDiegoVO vo);

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Diego 설치 정보 수정
     * @title : updateHbCfInfo
     * @return : void
    *****************************************************************/
	void updateHbDiegoInfo(@Param("vo") HbDiegoVO vo);
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Diego 설치 정보 삭제
     * @title : deleteHbDiegoInfo
     * @return : void
    *****************************************************************/
	void deleteHbDiegoInfo(int parseInt);

}
