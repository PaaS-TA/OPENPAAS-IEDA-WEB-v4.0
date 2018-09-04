package org.openpaas.ieda.hbdeploy.web.deploy.cfDeployment.service;

import java.util.ArrayList;
import java.util.List;

import org.openpaas.ieda.deploy.web.common.dto.ReplaceItemDTO;
import org.openpaas.ieda.deploy.web.deploy.cf.dao.CfVO;
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
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 화면에 입력한 값을 통해 Cloud Config 생성
     * @title : makeReplaceItems
     * @return : List<ReplaceItemDTO>
    ***************************************************/
    public List<ReplaceItemDTO> makeReplaceItems(CfVO vo) {
        List<ReplaceItemDTO> items = new ArrayList<ReplaceItemDTO>();
        int internalCnt = 0;
            if(vo.getNetworks() != null){
                for(int i=0; i<vo.getNetworks().size(); i++){
                    if("INTERNAL".equalsIgnoreCase(vo.getNetworks().get(i).getNet())){
                        internalCnt ++;
                        if(internalCnt == 1){
                            items.add(new ReplaceItemDTO("[net_id]", vo.getNetworks().get(i).getSubnetId())); 
                            items.add(new ReplaceItemDTO("[security_group]", vo.getNetworks().get(i).getCloudSecurityGroups())); 
                            items.add(new ReplaceItemDTO("[range]", vo.getNetworks().get(i).getSubnetRange())); 
                            items.add(new ReplaceItemDTO("[gateway]", vo.getNetworks().get(i).getSubnetGateway())); 
                            items.add(new ReplaceItemDTO("[reserved]", vo.getNetworks().get(i).getSubnetReservedFrom() + " - " + vo.getNetworks().get(i).getSubnetReservedTo() ));
                            items.add(new ReplaceItemDTO("[static]", vo.getNetworks().get(i).getSubnetStaticFrom() + " - " + vo.getNetworks().get(i).getSubnetStaticTo() ));
                            items.add(new ReplaceItemDTO("[dns]", vo.getNetworks().get(i).getSubnetDns()));
                            items.add(new ReplaceItemDTO("[availabilityzone]", vo.getNetworks().get(i).getAvailabilityZone()));
                        } else if(internalCnt == 2 ) {
                            items.add(new ReplaceItemDTO("[net_id2]", vo.getNetworks().get(i).getSubnetId())); 
                            items.add(new ReplaceItemDTO("[security_group2]", vo.getNetworks().get(i).getCloudSecurityGroups())); 
                            items.add(new ReplaceItemDTO("[range2]", vo.getNetworks().get(i).getSubnetRange())); 
                            items.add(new ReplaceItemDTO("[gateway2]", vo.getNetworks().get(i).getSubnetGateway())); 
                            items.add(new ReplaceItemDTO("[reserved2]", vo.getNetworks().get(i).getSubnetReservedFrom() + " - " + vo.getNetworks().get(i).getSubnetReservedTo() ));
                            items.add(new ReplaceItemDTO("[static2]", vo.getNetworks().get(i).getSubnetStaticFrom() + " - " + vo.getNetworks().get(i).getSubnetStaticTo() ));
                            items.add(new ReplaceItemDTO("[dns2]", vo.getNetworks().get(i).getSubnetDns()));
                            items.add(new ReplaceItemDTO("[availabilityzone2]", vo.getNetworks().get(i).getAvailabilityZone()));
                        }
                    }
                }
            }
            items.add(new ReplaceItemDTO("[small_instance_type]", vo.getResource().getSmallFlavor()));
            items.add(new ReplaceItemDTO("[medium_instance_type]", vo.getResource().getMediumFlavor()));
            items.add(new ReplaceItemDTO("[large_instance_type]", vo.getResource().getLargeFlavor()));
        return items;
    }

}
