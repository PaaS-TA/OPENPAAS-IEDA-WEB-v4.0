package org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dto.HbCfDeploymentDefaultConfigDTO;

public interface HbCfDeploymentDefaultConfigDAO {

    List<HbCfDeploymentDefaultConfigVO> selectHbCfDeploymentDefaultConfigInfoList();

    HbCfDeploymentDefaultConfigVO selectHbCfDeploymentDefaultConfigInfo(@Param("id")int id, @Param("iaas")String iaas);

    void insertHbCfDeploymentDefaultConfigInfo(@Param("default")HbCfDeploymentDefaultConfigVO vo);

    void updateHbCfDeploymentDefaultConfigInfo(@Param("default")HbCfDeploymentDefaultConfigVO vo);

    void deleteHbCfDeploymentDefaultConfigInfo(@Param("default")HbCfDeploymentDefaultConfigDTO dto);

    int selectHbCfDeploymentDefaultConfigByName(@Param("defaultConfigName")String defaultConfigName);

}