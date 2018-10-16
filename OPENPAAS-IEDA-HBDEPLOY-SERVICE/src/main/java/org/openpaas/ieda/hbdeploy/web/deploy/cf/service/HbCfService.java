package org.openpaas.ieda.hbdeploy.web.deploy.cf.service;

import java.security.Principal;
import java.util.List;

import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HbCfService {
	@Autowired private HbCfDAO dao;
	
	
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 정보 목록 전체 조회
     * @title : getCfInfoList
     * @return : List<HbCfVO>
    *****************************************************************/
	public List<HbCfVO> getCfInfoList(String installStatus) {
		List<HbCfVO> list = dao.selectHbCfInfoList(installStatus);
		return list;
	}
	
	public void saveCfInfo(HbCfDTO dto, Principal principal) {
		// TODO Auto-generated method stub
		
	}

}
