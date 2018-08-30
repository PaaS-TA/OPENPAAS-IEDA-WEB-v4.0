package org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dto.HbCfDeploymentInstanceConfigDTO;

public interface HbCfDeploymentInstanceConfigDAO {

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 인스턴 목록 조회
     * @title : selectHbCfDeploymentInstanceConfigInfoList
     * @return : List<HbCfDeploymentInstanceConfigVO>
    *****************************************************************/
    List<HbCfDeploymentInstanceConfigVO> selectHbCfDeploymentInstanceConfigInfoList();

    HbCfDeploymentInstanceConfigVO selectHbCfDeploymentInstanceConfigInfo(@Param("id")int id);

    List<HashMap<String, String>> selectHbCfJobTemplateByReleaseVersion(@Param("map") HashMap<String, String> map);

    void insertHbCfDeploymentInstanceConfigInfo(@Param("instance")HbCfDeploymentInstanceConfigVO vo);

    void updateHbCfDeploymentInstanceConfigInfo(@Param("instance")HbCfDeploymentInstanceConfigVO vo);

    void deleteHbCfDeploymentInstanceConfigInfo(@Param("instance")HbCfDeploymentInstanceConfigDTO dto);

    int selectHbCfDeploymentInstanceConfigByName(@Param("instanceConfigName")String instanceConfigName );

}
