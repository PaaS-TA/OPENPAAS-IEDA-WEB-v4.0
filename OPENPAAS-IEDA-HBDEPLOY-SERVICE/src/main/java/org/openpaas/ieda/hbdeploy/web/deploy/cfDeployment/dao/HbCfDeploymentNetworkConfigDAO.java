package org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dto.HbCfDeploymentNetworkConfigDTO;

public interface HbCfDeploymentNetworkConfigDAO {
	/****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 네트워크 목록 조회
     * @title : insertHbCfDeploymentNetworkConfigInfo
     * @return : List<HbCfDeploymentNetworkConfigVO>
    *****************************************************************/
    List<HbCfDeploymentNetworkConfigVO> selectHbCfDeploymentNetworkConfigInfoList();

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 네트워크 정보 상세 조회
     * @title : selectHbCfDeploymentNetworkConfigInfo
     * @return : HbCfDeploymentNetworkConfigVO
    *****************************************************************/
    HbCfDeploymentNetworkConfigVO selectHbCfDeploymentNetworkConfigInfo(@Param("id")int id, @Param("iaas")String iaas);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 네트워크 정보 등록
     * @title : insertHbCfDeploymentNetworkConfigInfo
     * @return : HbCfDeploymentNetworkConfigVO
    *****************************************************************/
    List<HbCfDeploymentNetworkConfigVO> insertHbCfDeploymentNetworkConfigInfo(@Param("network")HbCfDeploymentNetworkConfigVO vo);

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 네트워크 정보 수정
     * @title : updateHbCfDeploymentNetworkConfigInfo
     * @return : void
    *****************************************************************/
    void updateHbCfDeploymentNetworkConfigInfo(@Param("network")HbCfDeploymentNetworkConfigVO vo);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 네트워크 정보 삭제
     * @title : deleteHbCfDeploymentNetworkConfigInfo
     * @return : void
    *****************************************************************/
    void deleteHbCfDeploymentNetworkConfigInfo(@Param("network")HbCfDeploymentNetworkConfigDTO dto);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 네트워크 정보 이름으로 조회
     * @title : selectHbCfDeploymentNetworkConfigByName
     * @return : int
    *****************************************************************/
	int selectHbCfDeploymentNetworkConfigByName(@Param("networkName")String networkName);
}
