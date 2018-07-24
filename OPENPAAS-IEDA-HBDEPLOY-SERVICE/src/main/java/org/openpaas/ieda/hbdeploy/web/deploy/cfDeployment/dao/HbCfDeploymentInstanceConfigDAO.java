package org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao;

import java.util.List;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao.HbCfDeploymentInstanceConfigVO;

public interface HbCfDeploymentInstanceConfigDAO {
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 인스턴 목록 조회
     * @title : selectCfDeploymentInstanceConfigInfoList
     * @return : List<HbCfDeploymentInstanceConfigVO>
    *****************************************************************/
    List<HbCfDeploymentInstanceConfigVO> selectCfDeploymentInstanceConfigInfoList();

}
