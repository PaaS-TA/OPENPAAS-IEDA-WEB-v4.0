package org.openpaas.ieda.hbdeploy.web.deploy.diego.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.openpaas.ieda.common.exception.CommonException;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoNetworkConfigDAO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dao.HbDiegoNetworkConfigVO;
import org.openpaas.ieda.hbdeploy.web.deploy.diego.dto.HbDiegoNetworkConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class HbDiegoNetworkConfigService {

	@Autowired private MessageSource message;
	@Autowired private HbDiegoNetworkConfigDAO dao;
	
	/****************************************************************
	 * @project : Paas 이종 플랫폼 설치 자동화
	 * @description : DIEGO 네트워크 정보 목록 전체 조회
	 * @title : getNetworkConfigInfoList
	 * @return : List<HbDiegoNetworkConfigVO>
	*****************************************************************/
	public List<HbDiegoNetworkConfigVO> getNetworkConfigInfoList(){
		List<HbDiegoNetworkConfigVO> list = dao.selectNetworkConfigList();
		List<HbDiegoNetworkConfigVO> resultList = new ArrayList<HbDiegoNetworkConfigVO>();
		HbDiegoNetworkConfigVO vo = null;
		if(list != null && list.size() != 0){
			for(int i=0; i<list.size(); i++){
				List<HbDiegoNetworkConfigVO> sameNetworkConfigNameList = dao.selectDiegoNetworkConfigInfoByNameResultVo(list.get(i).getNetworkConfigName());
				if(sameNetworkConfigNameList != null && sameNetworkConfigNameList.size() != 0){
					for(int j = 0; j<sameNetworkConfigNameList.size(); j++){
						if(sameNetworkConfigNameList.size() == 1){
							vo = new HbDiegoNetworkConfigVO();
							vo.setRecid(sameNetworkConfigNameList.get(0).getRecid());
							vo.setId(sameNetworkConfigNameList.get(0).getId());
							vo.setNetworkConfigName(sameNetworkConfigNameList.get(0).getNetworkConfigName());
							vo.setIaasType(sameNetworkConfigNameList.get(0).getIaasType());
							vo.setCloudSecurityGroups(sameNetworkConfigNameList.get(0).getCloudSecurityGroups());
							vo.setSubnetId(sameNetworkConfigNameList.get(0).getSubnetId());
							vo.setSubnetDns(sameNetworkConfigNameList.get(0).getSubnetDns());
							vo.setSubnetRange(sameNetworkConfigNameList.get(0).getSubnetRange());
							vo.setSubnetGateway(sameNetworkConfigNameList.get(0).getSubnetGateway());
							vo.setSubnetReservedFrom(sameNetworkConfigNameList.get(0).getSubnetReservedFrom() + "-" + sameNetworkConfigNameList.get(0).getSubnetReservedTo());
							vo.setSubnetStaticFrom(sameNetworkConfigNameList.get(0).getSubnetStaticFrom() + "-" + sameNetworkConfigNameList.get(0).getSubnetStaticTo());
							resultList.add(vo);
							break;
						}else if(sameNetworkConfigNameList.size() == 2){
							vo = new HbDiegoNetworkConfigVO();
							vo.setRecid(sameNetworkConfigNameList.get(0).getRecid());
							vo.setId(sameNetworkConfigNameList.get(0).getId());
							vo.setNetworkConfigName(sameNetworkConfigNameList.get(0).getNetworkConfigName());
							vo.setIaasType(sameNetworkConfigNameList.get(0).getIaasType());
							vo.setCloudSecurityGroups(sameNetworkConfigNameList.get(0).getCloudSecurityGroups() + "<br>" + sameNetworkConfigNameList.get(1).getCloudSecurityGroups());
							vo.setSubnetId(sameNetworkConfigNameList.get(0).getSubnetId() + "<br>" + sameNetworkConfigNameList.get(1).getSubnetId());
							vo.setSubnetDns(sameNetworkConfigNameList.get(0).getSubnetDns() + "<br>" + sameNetworkConfigNameList.get(1).getSubnetDns());
							vo.setSubnetRange(sameNetworkConfigNameList.get(0).getSubnetRange() + "<br>" + sameNetworkConfigNameList.get(1).getSubnetRange());
							vo.setSubnetGateway(sameNetworkConfigNameList.get(0).getSubnetGateway() + "<br>" + sameNetworkConfigNameList.get(1).getSubnetGateway());
							vo.setSubnetReservedFrom(sameNetworkConfigNameList.get(0).getSubnetReservedFrom() + "-" + sameNetworkConfigNameList.get(0).getSubnetReservedTo()
		                            + "<br>" + sameNetworkConfigNameList.get(1).getSubnetReservedFrom() + "-" + sameNetworkConfigNameList.get(1).getSubnetReservedTo());
							vo.setSubnetStaticFrom(sameNetworkConfigNameList.get(0).getSubnetStaticFrom() + "-" + sameNetworkConfigNameList.get(0).getSubnetStaticTo() 
		                            + "<br>" + sameNetworkConfigNameList.get(1).getSubnetStaticFrom() + "-" + sameNetworkConfigNameList.get(1).getSubnetStaticTo());
							resultList.add(vo);
							break;
						}else if(sameNetworkConfigNameList.size() == 3){
							vo = new HbDiegoNetworkConfigVO();
							vo.setRecid(sameNetworkConfigNameList.get(0).getRecid());
							vo.setId(sameNetworkConfigNameList.get(0).getId());
							vo.setNetworkConfigName(sameNetworkConfigNameList.get(0).getNetworkConfigName());
							vo.setIaasType(sameNetworkConfigNameList.get(0).getIaasType());
							vo.setCloudSecurityGroups(sameNetworkConfigNameList.get(0).getCloudSecurityGroups() 
									+ "<br>" + sameNetworkConfigNameList.get(1).getCloudSecurityGroups()
									+ "<br>" + sameNetworkConfigNameList.get(2).getCloudSecurityGroups());
							vo.setSubnetId(sameNetworkConfigNameList.get(0).getSubnetId() 
									+ "<br>" + sameNetworkConfigNameList.get(1).getSubnetId()
									+ "<br>" + sameNetworkConfigNameList.get(2).getSubnetId());
							vo.setSubnetDns(sameNetworkConfigNameList.get(0).getSubnetDns() 
									+ "<br>" + sameNetworkConfigNameList.get(1).getSubnetDns()
									+ "<br>" + sameNetworkConfigNameList.get(2).getSubnetDns());
							vo.setSubnetRange(sameNetworkConfigNameList.get(0).getSubnetRange() 
									+ "<br>" + sameNetworkConfigNameList.get(1).getSubnetRange()
									+ "<br>" + sameNetworkConfigNameList.get(2).getSubnetRange());
							vo.setSubnetGateway(sameNetworkConfigNameList.get(0).getSubnetGateway() 
									+ "<br>" + sameNetworkConfigNameList.get(1).getSubnetGateway()
									+ "<br>" + sameNetworkConfigNameList.get(2).getSubnetGateway());
							vo.setSubnetReservedFrom(sameNetworkConfigNameList.get(0).getSubnetReservedFrom() + "-" + sameNetworkConfigNameList.get(0).getSubnetReservedTo()
		                            + "<br>" + sameNetworkConfigNameList.get(1).getSubnetReservedFrom() + "-" + sameNetworkConfigNameList.get(1).getSubnetReservedTo()
		                            + "<br>" + sameNetworkConfigNameList.get(2).getSubnetReservedFrom() + "-" + sameNetworkConfigNameList.get(2).getSubnetReservedTo());
							vo.setSubnetStaticFrom(sameNetworkConfigNameList.get(0).getSubnetStaticFrom() + "-" + sameNetworkConfigNameList.get(0).getSubnetStaticTo() 
		                            + "<br>" + sameNetworkConfigNameList.get(1).getSubnetStaticFrom() + "-" + sameNetworkConfigNameList.get(1).getSubnetStaticTo()
		                            + "<br>" + sameNetworkConfigNameList.get(2).getSubnetStaticFrom() + "-" + sameNetworkConfigNameList.get(2).getSubnetStaticTo());
							resultList.add(vo);
							break;
						}
					}
					if(list.get(i).getNetworkConfigName().equals(sameNetworkConfigNameList.get(i).getNetworkConfigName())
							&& i >= list.size()
							){
						break;
					}
						
				}
			}
		}
		return resultList;
	}
	

	
	/****************************************************************
	 * @project : Paas 이종 플랫폼 설치 자동화
	 * @description : DIEGO 네트워크 정보 상세 조회 목록
	 * @title : getNetworkConfigDetailInfoList
	 * @return : List<HbDiegoNetworkConfigVO>
	*****************************************************************/
	public List<HbDiegoNetworkConfigVO> getNetworkConfigDetailInfoList(String networkConfigName){
		List<HbDiegoNetworkConfigVO> result = dao.selectDiegoNetworkConfigInfoByNameResultVo(networkConfigName);
		return result;
	}
	
	/****************************************************************
	 * @project : Paas 이종 플랫폼 설치 자동화
	 * @description : DIEGO 네트워크 정보 저장
	 * @title : saveNetworkConfigInfo
	 * @return : void
	*****************************************************************/
	public void saveNetworkConfigInfo(List<HbDiegoNetworkConfigDTO> dto, Principal principal){
		List<HbDiegoNetworkConfigVO> list = new ArrayList<HbDiegoNetworkConfigVO>();
        int count = 0;
        if(dto != null && dto.size() != 0){
            for(int i=0; i<dto.size(); i++){
                count = dao.selectDiegoNetworkConfigInfoByName(dto.get(i).getNetworkConfigName());
                if(StringUtils.isEmpty(dto.get(i).getId())){
                    if(count > 0) { 
                        throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                                message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
                    }
                }else{
                    HbDiegoNetworkConfigVO vo = dao.selectDiegoNetworkInfoById(dto.get(i).getId());
                    if(vo != null){
                        dao.deleteDiegoNetworkConfigInfoByName(vo.getNetworkConfigName());
                    }
                }
                if(count > 0){
                    dao.deleteDiegoNetworkConfigInfoByName(dto.get(i).getNetworkConfigName());
                }
                HbDiegoNetworkConfigVO vo = new HbDiegoNetworkConfigVO();
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
        dao.insertDiegoNetworkConfigInfo(list);
    }
	
	/****************************************************************
	 * @project : Paas 플랫폼 설치 자동화
	 * @description : DIEGO 네트워크 정보 삭제
	 * @title : deleteNetworkConfigInfo
	 * @return : void
	*****************************************************************/
	public void deleteNetworkConfigInfo(HbDiegoNetworkConfigDTO dto, Principal principa){
	    if(dto.getId() == null || dto.getId().isEmpty()){
	        throw new CommonException(message.getMessage("common.badRequest.exception.code", null, Locale.KOREA),
                    message.getMessage("common.badRequest.message", null, Locale.KOREA), HttpStatus.BAD_REQUEST);
	    }
	    dao.deleteDiegoNetworkConfigInfoByName(dto.getNetworkConfigName());
	}
}
