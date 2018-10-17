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
		return list;
	}
	
	/****************************************************************
	 * @project : Paas 플랫폼 설치 자동화
	 * @description : DIEGO 네트워크 정보 상세 조회
	 * @title : getNetworkConfigDetailInfo
	 * @return : HbDiegoNetworkConfigVO
	*****************************************************************/
	public HbDiegoNetworkConfigVO getNetworkConfigDetailInfo(HbDiegoNetworkConfigDTO dto){
		HbDiegoNetworkConfigVO vo = new HbDiegoNetworkConfigVO();
		int check = dao.selectDiegoDefaultConfigInfoByName(dto.getNetworkConfigName());
		if(check!=1){
			throw new CommonException(message.getMessage("common.badRequest.exception.code", null, Locale.KOREA),
					message.getMessage("common.badRequest.message", null, Locale.KOREA), HttpStatus.BAD_REQUEST);
		}
		vo = dao.selectDiegoDefaultInfoById(dto.getId());
		return vo;
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
                count = dao.selectDiegoDefaultConfigInfoByName(dto.get(i).getNetworkConfigName());
                if(StringUtils.isEmpty(dto.get(i).getNetworkConfigName())){
                    if(count > 0) {
                        throw new CommonException(message.getMessage("common.conflict.exception.code", null, Locale.KOREA),
                                message.getMessage("hybrid.configMgnt.alias.conflict.message.exception", null, Locale.KOREA), HttpStatus.CONFLICT);
                    }
                }else{
                    HbDiegoNetworkConfigVO vo = dao.selectDiegoDefaultInfoById(dto.get(i).getId());
                    dao.deleteDiegoNetworkConfigInfoByName(vo.getNetworkConfigName());
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
