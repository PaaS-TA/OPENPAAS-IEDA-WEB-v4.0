package org.openpaas.ieda.hbdeploy.web.deploy.diego.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface HbDiegoNetworkConfigDAO {

	/****************************************************************
	 * @project : Paas 이종 플랫폼 설치 자동화
	 * @description : DIEGO 네트워크 정보 목록 조회
	 * @title : selectNetworkConfigList
	 * @return : List<HbDiegoNetworkConfigVO>
	*****************************************************************/
	List<HbDiegoNetworkConfigVO> selectNetworkConfigList();
	
	/****************************************************************
	 * @project : Paas 이종 플랫폼 설치 자동화
	 * @description : DIEGO 네트워크 정보(별칭)로 중복 조회
	 * @title : selectDiegoDefaultConfigInfoByName
	 * @return : int
	*****************************************************************/
	int selectDiegoDefaultConfigInfoByName(@Param("networkConfigName") String networkConfigName);
	
	/****************************************************************
	 * @project : Paas 이종 플랫폼 설치 자동화
	 * @description : DIEGO 네트워크 정보(별칭)로 상세 조회
	 * @title : selectDiegoNetworkConfigInfoByNameResultVo
	 * @return : List<HbDiegoNetworkConfigVO>
	*****************************************************************/
	List<HbDiegoNetworkConfigVO> selectDiegoNetworkConfigInfoByNameResultVo(@Param("networkConfigName") String networkConfigName);
	
	/****************************************************************
	 * @project : Paas 플랫폼 설치 자동화
	 * @description : DIEGO 네트워크 정보(id) 상세 조회
	 * @title : selectDiegoDefaultInfoById
	 * @return : HbDiegoNetworkConfigVO
	*****************************************************************/
	HbDiegoNetworkConfigVO selectDiegoDefaultInfoById(@Param("id")String id);
	
	/****************************************************************
	 * @project : Paas 이종 플랫폼 설치 자동화
	 * @description : DIEGO 네트워크 정보 저장
	 * @title : insertDiegoNetworkConfigInfo
	 * @return : void
	*****************************************************************/
	void insertDiegoNetworkConfigInfo(@Param("networks") List<HbDiegoNetworkConfigVO> list);
	
	/****************************************************************
	 * @project : Paas 이종 플랫폼 설치 자동화
	 * @description : DIEGO 네트워크 정보 삭제
	 * @title : deleteDiegoNetworkConfigInfoByName
	 * @return : void
	*****************************************************************/
	void deleteDiegoNetworkConfigInfoByName(@Param("networkConfigName") String networkConfigName);
	
}
