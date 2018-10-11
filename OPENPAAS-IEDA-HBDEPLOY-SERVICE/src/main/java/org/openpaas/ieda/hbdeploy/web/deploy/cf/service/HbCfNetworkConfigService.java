package org.openpaas.ieda.hbdeploy.web.deploy.cf.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfDefaultConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfNetworkConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfNetworkConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfNetworkConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HbCfNetworkConfigService {
    
    @Autowired private HbCfNetworkConfigDAO dao;
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 네트워크 정보 목록 전체 조회
     * @title : getNetworkConfigInfoList
     * @return : List<HbCfNetworkConfigVO>
    *****************************************************************/
    public List<HbCfNetworkConfigVO> getNetworkConfigInfoList() {
        List<HbCfNetworkConfigVO> list = dao.selectNetworkConfigList();
        return list;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 네트워크 정보 저장
     * @title : saveNetworkConfigInfo
     * @return : void
    *****************************************************************/
    public void saveNetworkConfigInfo(List<HbCfNetworkConfigDTO> dto, Principal principal) {
        List<HbCfNetworkConfigVO> list = new ArrayList<HbCfNetworkConfigVO>();
        int count = 0;
        if(dto != null && dto.size() != 0){
            for(int i=0; i<dto.size(); i++){
                if("EXTERNAL".equalsIgnoreCase(dto.get(i).getNet())) {
                    count = dao.selectCfDefaultConfigInfoByName(dto.get(i).getNetworkConfigName());
                    if(count > 0) {
                        dao.deleteCfNetworkConfigInfoByName(dto.get(i).getNetworkConfigName());
                    }
                }
                HbCfNetworkConfigVO vo = new HbCfNetworkConfigVO();
                vo.setAvailabilityZone(dto.get(i).getAvailabilityZone());
                vo.setCloudSecurityGroups(dto.get(i).getCloudSecurityGroups());
                vo.setIaasType(dto.get(i).getIaasType());
                vo.setNet(dto.get(i).getNet());
                vo.setNetworkConfigName(dto.get(i).getNetworkConfigName());
                vo.setPublicStaticIP(dto.get(i).getPublicStaticIP());
                vo.setSeq(Integer.parseInt(dto.get(i).getSeq()));
                vo.setSubnetDns(dto.get(i).getSubnetDns());
                vo.setSubnetGateway(dto.get(i).getSubnetGateway());
                vo.setSubnetId(dto.get(i).getSubnetId());
                vo.setSubnetRange(dto.get(i).getSubnetRange());
                vo.setSubnetReservedFrom(dto.get(i).getSubnetReservedFrom());
                vo.setSubnetReservedTo(dto.get(i).getSubnetReservedTo());
                vo.setSubnetStaticFrom(dto.get(i).getSubnetStaticFrom());
                vo.setSubnetStaticTo(dto.get(i).getSubnetStaticTo());
                vo.setCreateUserId(principal.getName());
                vo.setUpdateUserId(principal.getName());
                list.add(vo);
            }
        }
        dao.insertNetworkInfo(list);
    }

}
