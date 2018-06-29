package org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.openpaas.ieda.hbdeploy.web.deploy.bootstrap.dto.HbBootStrapDeployDTO;

public interface HbBootstrapDAO {
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Bootstrap 전체 목록 조회
     * @title : selectBootstrapList
     * @return : List<BootstrapVO>
    *****************************************************************/
    List<HbBootstrapVO> selectBootstrapList();
    
    /****************************************************************
     * @param iaas 
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : Bootstrap 상세조회
     * @title : selectBootstrapInfo
     * @return : BootstrapVO
    *****************************************************************/
    HbBootstrapVO selectBootstrapInfo(@Param("id")int id, @Param("iaas")String iaas);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : BootStrap 정보 저장
     * @title : insertBootStrapInfo
     * @return : int
    *****************************************************************/
    int insertBootStrapInfo(@Param("bootstrap")HbBootstrapVO vo);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : BootStrap 정보 수정
     * @title : updateBootStrapInfo
     * @return : int
    *****************************************************************/
    int updateBootStrapInfo(@Param("bootstrap")HbBootstrapVO vo);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : BootStrap 정보 삭제
     * @title : deleteBootstrapInfo
     * @return : void
    *****************************************************************/
    void deleteBootstrapInfo(@Param("dto")HbBootStrapDeployDTO.Delete dto);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 클라우드 bootstrap 관리 디비 정보 삽입
     * @title : insertHybridBootstrapMgntInfo
     * @return : void
    *****************************************************************/
    void insertHybridBootstrapMgntInfo(@Param("bootstrap") HbBootstrapVO vo);
    
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 클라우드 bootstrap 관리 디비 정보 수정
     * @title : updateHybridBootstrapMgntInfo
     * @return : void
    *****************************************************************/
    void updateHybridBootstrapMgntInfo(@Param("bootstrap") HbBootstrapVO vo);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 이종 클라우드 bootstrap 관리 디비 정보 수정
     * @title : updateHybridBootstrapMgntInfo
     * @return : void
    *****************************************************************/
    HbBootstrapVO selectHbBootstrapMgntFromPrvateDeploymentFileName(@Param("deploymentFileName") String privateDeploymentFileName);
    
    /***************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : hybridboostrap 설치 정보 조회
     * @title : selectInstallBootstrapInfo
     * @return : Boolean
    ***************************************************/
    HbBootstrapVO selectInstallBootstrapInfo(@Param("privateBootstrapId") int privateBootstrapId,@Param("publicBootStrapId") int publicBootStrapId);
}
