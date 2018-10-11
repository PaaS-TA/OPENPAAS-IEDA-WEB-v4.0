package org.openpaas.ieda.hbdeploy.web.deploy.cf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface HbCfNetworkConfigDAO {
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 네트워크 정보 목록 전체 조회
     * @title : selectNetworkConfigList
     * @return : List<HbCfNetworkConfigVO>
    *****************************************************************/
	List<HbCfNetworkConfigVO> selectNetworkConfigList();
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 네트워크 별칭 기준으로 중복 정보 조회 
     * @title : selectCfDefaultConfigInfoByName
     * @return : int
    *****************************************************************/
	int selectCfDefaultConfigInfoByName(@Param("networkConfigName") String networkConfigName);
	
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 네트워크 별칭 기준으로 네트워크 정보 삭제
     * @title : deleteCfNetworkConfigInfoByName
     * @return : int
    *****************************************************************/
	void deleteCfNetworkConfigInfoByName(@Param("networkConfigName") String networkConfigName);

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 네트워크 정보 저장
     * @title : insertNetworkInfo
     * @return : void
    *****************************************************************/
	void insertNetworkInfo(@Param("networks") List<HbCfNetworkConfigVO> list);

}
