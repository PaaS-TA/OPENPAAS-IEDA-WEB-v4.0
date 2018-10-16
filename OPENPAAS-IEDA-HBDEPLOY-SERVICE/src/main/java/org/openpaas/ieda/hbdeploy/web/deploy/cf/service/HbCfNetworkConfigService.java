package org.openpaas.ieda.hbdeploy.web.deploy.cf.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfNetworkConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dao.HbCfNetworkConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.cf.dto.HbCfNetworkConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HbCfNetworkConfigService {
    
    @Autowired private MessageSource message;
    @Autowired private HbCfNetworkConfigDAO dao;
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 네트워크 정보 목록 전체 조회
     * @title : getNetworkConfigInfoList
     * @return : List<HbCfNetworkConfigVO>
    *****************************************************************/
    public List<HbCfNetworkConfigVO> getNetworkConfigInfoList() {
        List<HbCfNetworkConfigVO> list = dao.selectCfNetworkConfigList();
        List<HbCfNetworkConfigVO> resultList = new ArrayList<HbCfNetworkConfigVO>();
        HbCfNetworkConfigVO vo = null;
        if(list != null && list.size() != 0){
            for(int i=0; i<list.size(); i++){
                if("EXTERNAL".equalsIgnoreCase(list.get(i).getNet())) {
                    List<HbCfNetworkConfigVO> sameNetworkConfigNameList = dao.selectCfDefaultConfigInfoByNameResultVo(list.get(i).getNetworkConfigName());
                    if(sameNetworkConfigNameList != null && sameNetworkConfigNameList.size() != 0){
                        for(int j = 0; j<sameNetworkConfigNameList.size(); j++){
                            if(sameNetworkConfigNameList.size() == 2){
                                vo = new HbCfNetworkConfigVO();
                                vo.setRecid(sameNetworkConfigNameList.get(0).getRecid());
                                vo.setId(sameNetworkConfigNameList.get(0).getId());
                                vo.setNetworkConfigName(sameNetworkConfigNameList.get(0).getNetworkConfigName());
                                vo.setIaasType(sameNetworkConfigNameList.get(0).getIaasType());
                                vo.setPublicStaticIP(sameNetworkConfigNameList.get(0).getPublicStaticIP());
                                vo.setCloudSecurityGroups(sameNetworkConfigNameList.get(1).getCloudSecurityGroups());
                                vo.setSubnetId(sameNetworkConfigNameList.get(1).getSubnetId());
                                vo.setSubnetDns(sameNetworkConfigNameList.get(1).getSubnetDns());
                                vo.setSubnetRange(sameNetworkConfigNameList.get(1).getSubnetRange());
                                vo.setSubnetGateway(sameNetworkConfigNameList.get(1).getSubnetGateway());
                                vo.setSubnetReservedFrom(sameNetworkConfigNameList.get(1).getSubnetReservedFrom() + "-" + sameNetworkConfigNameList.get(1).getSubnetReservedTo());
                                vo.setSubnetStaticFrom(sameNetworkConfigNameList.get(1).getSubnetStaticFrom() + "-" + sameNetworkConfigNameList.get(1).getSubnetStaticTo() );
                                resultList.add(vo);
                                break;
                            } else if(sameNetworkConfigNameList.size() == 3){
                                vo = new HbCfNetworkConfigVO();
                                vo.setRecid(sameNetworkConfigNameList.get(0).getRecid());
                                vo.setId(sameNetworkConfigNameList.get(0).getId());
                                vo.setNetworkConfigName(sameNetworkConfigNameList.get(0).getNetworkConfigName());
                                vo.setIaasType(sameNetworkConfigNameList.get(0).getIaasType());
                                vo.setSubnetDns(sameNetworkConfigNameList.get(1).getSubnetDns() + "<br>" + sameNetworkConfigNameList.get(2).getSubnetDns());
                                vo.setPublicStaticIP(sameNetworkConfigNameList.get(0).getPublicStaticIP());
                                vo.setCloudSecurityGroups(sameNetworkConfigNameList.get(1).getCloudSecurityGroups()+ "<br>"+ sameNetworkConfigNameList.get(2).getCloudSecurityGroups());
                                vo.setSubnetId(sameNetworkConfigNameList.get(1).getSubnetId()+ "<br>" + sameNetworkConfigNameList.get(2).getSubnetId());
                                vo.setSubnetRange(sameNetworkConfigNameList.get(1).getSubnetRange() + "<br>" + sameNetworkConfigNameList.get(2).getSubnetRange());
                                vo.setSubnetGateway(sameNetworkConfigNameList.get(1).getSubnetGateway() + "<br>" + sameNetworkConfigNameList.get(2).getSubnetGateway());
                                vo.setSubnetReservedFrom(sameNetworkConfigNameList.get(1).getSubnetReservedFrom() + "-" + sameNetworkConfigNameList.get(1).getSubnetReservedTo()
                                        + "<br>" + sameNetworkConfigNameList.get(2).getSubnetReservedFrom() + "-" + sameNetworkConfigNameList.get(2).getSubnetReservedTo());
                                vo.setSubnetStaticFrom(sameNetworkConfigNameList.get(1).getSubnetStaticFrom() + "-" + sameNetworkConfigNameList.get(1).getSubnetStaticTo() 
                                        + "<br>" + sameNetworkConfigNameList.get(2).getSubnetStaticFrom() + "-" + sameNetworkConfigNameList.get(2).getSubnetStaticTo());
                                resultList.add(vo);
                                break;
                            }
                        }
                    }
                    
                }
            }
        }
        return resultList;
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
                    if(StringUtils.isEmpty(dto.get(i).getId())) {
                        if(count > 0){
                            throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                                    message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
                        }
                    } else {
                        HbCfNetworkConfigVO vo = dao.selectCfDefaultConfigInfoById(Integer.parseInt(dto.get(i).getId()));
                        dao.deleteCfNetworkConfigInfoByName(vo.getNetworkConfigName());
                    }
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
        dao.insertCfNetworkConfigInfo(list);
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 네트워크 정보 상세 조회
     * @title : getNetworkConfigInfoListDetail
     * @return : void
    *****************************************************************/
    public List<HbCfNetworkConfigVO> getNetworkConfigInfoListDetail(String networkConfigName) {
        List<HbCfNetworkConfigVO> result = dao.selectCfDefaultConfigInfoByNameResultVo(networkConfigName);
        return result;
    }
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : CF 네트워크 정보 삭제
     * @title : deleteNetworkConfigInfo
     * @return : void
    *****************************************************************/
    public void deleteNetworkConfigInfo(HbCfNetworkConfigDTO dto, Principal principal) {
        if(StringUtils.isEmpty(dto.getNetworkConfigName())){
            throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                    message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
        
        }
        dao.deleteCfNetworkConfigInfoByName(dto.getNetworkConfigName());
    }

}
