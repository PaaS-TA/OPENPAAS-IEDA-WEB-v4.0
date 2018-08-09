package org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dto.HbCfDeploymentCredentialConfigDTO;

public interface HbCfDeploymentCredentialConfigDAO {

    List<HbCfDeploymentCredentialConfigVO> selectHbCfDeploymentCredentialConfigInfoList();

    HbCfDeploymentCredentialConfigVO selectHbCfDeploymentCredentialConfigInfo(@Param("id")int id, @Param("iaas")String iaas);

    void insertHbCfDeploymentCredentialConfigInfo(@Param("credential")HbCfDeploymentCredentialConfigVO vo);

    void updateHbCfDeploymentCredentialConfigInfo(@Param("credential")HbCfDeploymentCredentialConfigVO vo);

    void deleteHbCfDeploymentCredentialConfigInfo(@Param("credential")HbCfDeploymentCredentialConfigDTO dto);

    int selectHbCfDeploymentCredentialConfigByName(@Param("credentialConfigName")String credentialConfigName);

}
