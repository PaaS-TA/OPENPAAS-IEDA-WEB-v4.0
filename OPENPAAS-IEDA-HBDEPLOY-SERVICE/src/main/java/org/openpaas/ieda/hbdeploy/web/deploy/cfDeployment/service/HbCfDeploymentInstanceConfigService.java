package org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.service;

import java.util.List;

import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao.HbCfDeploymentInstanceConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.dao.HbCfDeploymentInstanceConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class HbCfDeploymentInstanceConfigService {
	
    @Autowired private MessageSource message;
    @Autowired private  HbCfDeploymentInstanceConfigDAO bootstrapInstanceDao;
    
    public List< HbCfDeploymentInstanceConfigVO> getInstanceConfigInfoList() {
        List< HbCfDeploymentInstanceConfigVO> list = bootstrapInstanceDao.selectCfDeploymentInstanceConfigInfoList();
        return list;
    }
}
