package org.openpaas.ieda.hbdeploy.web.deploy.diego.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface HbDiegoDefaultConfigDAO {

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 기본 정보 목록 조회
     * @title : selectDiegoDefaultInfoList
     * @return : List<HbDiegoDefaultConfigVO>
    *****************************************************************/
    List<HbDiegoDefaultConfigVO> selectDiegoDefaultInfoList();
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 기본 정보 선택 조회(ID)
     * @title : selectDiegoDefaultConfigInfoById
     * @return : HbDiegoDefaultConfigVO
    *****************************************************************/
    HbDiegoDefaultConfigVO selectDiegoDefaultConfigInfoById(@Param("id")int id);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 기본 정보 선택 조회(Name)
     * @title : selectDiegoDefaultConfigInfoByName
     * @return : int
    *****************************************************************/
    int selectDiegoDefaultConfigInfoByName (@Param("defaultConfigName")String defaultConfigName);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 배포명 중복 조회
     * @title : selectDiegoDeploymentNameDuplication
     * @return : int
    *****************************************************************/
    int selectDiegoDeploymentNameDuplication (@Param("diego")HbDiegoDefaultConfigVO diego);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 기본 정보 삽입
     * @title : insertDiegoDefaultConfigInfo
     * @return : void
    *****************************************************************/
    void insertDiegoDefaultConfigInfo (@Param("diego")HbDiegoDefaultConfigVO diego);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 기본 정보 갱신
     * @title : updateDiegoDefaultConfigInfo
     * @return : void
    *****************************************************************/
    void updateDiegoDefaultConfigInfo (@Param("diego")HbDiegoDefaultConfigVO diego);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : DIEGO 기본 정보 삭제
     * @title : deleteDiegoDefaultConfigInfo
     * @return : void
    *****************************************************************/
    void deleteDiegoDefaultConfigInfo (@Param("id")String id);
}
