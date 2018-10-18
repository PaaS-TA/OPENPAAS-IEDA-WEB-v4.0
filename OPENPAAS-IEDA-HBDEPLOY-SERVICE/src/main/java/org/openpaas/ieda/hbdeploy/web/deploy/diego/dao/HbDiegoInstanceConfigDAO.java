package org.openpaas.ieda.hbdeploy.web.deploy.diego.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface HbDiegoInstanceConfigDAO {
    
    /****************************************************************
     * @project : 이종 Paas 플랫폼 설치 자동화
     * @description : 인프라 및 릴리즈 버전 별 job 목록 조회
     * @title : selectDiegoJobTemplatesByReleaseVersion
     * @return : List<HashMap<String,String>>
    *****************************************************************/
    public List<HashMap<String, String>> selectDiegoJobTemplatesByReleaseVersion(@Param("map") HashMap<String, String> map);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 인스턴스 정보 목록 전체 조회
     * @title : selectDiegoInstanceInfoList
     * @return : List<HbDiegoInstanceConfigVO
    *****************************************************************/
    public List<HbDiegoInstanceConfigVO> selectDiegoInstanceInfoList();

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 인스턴스 정보 목록 중복 값 확인
     * @title : selectInstanceConfigByName
     * @return : int
    *****************************************************************/
    public int selectInstanceConfigByName(@Param("instanceConfigName") String instanceConfigName);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 인스턴스 정보 목록 상세 조회
     * @title : selectInstanceConfigById
     * @return : HbDiegoInstanceConfigVO
    *****************************************************************/
    public HbDiegoInstanceConfigVO selectInstanceConfigById(@Param("id") int id);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 인스턴스 정보 삽입
     * @title : selectInstanceConfigById
     * @return : HbDiegoInstanceConfigVO
    *****************************************************************/
    public void insertInstanceConfg(@Param("vo") HbDiegoInstanceConfigVO vo);

    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 인스턴스 정보 수정
     * @title : selectInstanceConfigById
     * @return : HbDiegoInstanceConfigVO
    *****************************************************************/
    public void updateInstanceConfig(@Param("vo") HbDiegoInstanceConfigVO vo);
    
    /****************************************************************
     * @project : Paas 이종 플랫폼 설치 자동화
     * @description : 인스턴스 정보 삭제
     * @title : selectInstanceConfigById
     * @return : HbDiegoInstanceConfigVO
    *****************************************************************/
    public void deleteInstanceConfig(@Param("id") int id);
}
