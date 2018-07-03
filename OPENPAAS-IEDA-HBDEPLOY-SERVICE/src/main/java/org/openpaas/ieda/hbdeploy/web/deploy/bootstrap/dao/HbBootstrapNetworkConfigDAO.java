package org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dto.HbBootstrapNetworkConfigDTO;

public interface HbBootstrapNetworkConfigDAO {
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CPI 정보 목록 조회
     * @title : selectBootstrapNetworkConfigInfoList
     * @return : List<HbBootstrapCpiVO>
    *****************************************************************/
    List<HbBootstrapNetworkConfigVO> selectBootstrapNetworkConfigInfoList();
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CPI 정보 상세 조회
     * @title : saveCpiInfo
     * @return : HbBootstrapCpiVO
    *****************************************************************/
    HbBootstrapNetworkConfigVO selectBootstrapNetworkConfigInfo(@Param("id")int id, @Param("iaas")String iaas);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CPI 정보 등록
     * @title : saveCpiInfo
     * @return : HbBootstrapCpiVO
    *****************************************************************/
    void insertBootStrapNetworkConfigInfo(@Param("network")HbBootstrapNetworkConfigVO vo);

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CPI 정보 수정
     * @title : saveCpiInfo
     * @return : HbBootstrapCpiVO
    *****************************************************************/
    void updateBootStrapNetworkConfigInfo(@Param("network")HbBootstrapNetworkConfigVO vo);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CPI 정보 삭제
     * @title : saveCpiInfo
     * @return : HbBootstrapCpiVO
    *****************************************************************/
    void deleteBootStrapNetworkConfigInfo(@Param("network")HbBootstrapNetworkConfigDTO dto);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CPI 정보 조회
     * @title : selectbootstrapcpifconfigByName
     * @return : void
    *****************************************************************/
	int selectBootstrapNetworkConfigByName(@Param("networkConfigName")String networkConfigName);
}
