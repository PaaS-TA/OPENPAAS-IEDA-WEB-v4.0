package org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.service;

import java.util.List;

import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao.HbCfDeploymentDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao.HbCfDeploymentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HbCfDeploymentService {
    
    @Autowired HbCfDeploymentDAO hbCfDeploymentDAO;
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF Deployment 정보 조회
     * @title : getHbBCfDeploymentList
     * @return : List<HbCfDeploymentVO>
    ***************************************************/
    public List<HbCfDeploymentVO> getHbBCfDeploymentList(String installStatus) {
        List<HbCfDeploymentVO> CfDeploymentList = hbCfDeploymentDAO.selectCfDeploymentList(installStatus);
        return CfDeploymentList;
    }

}
