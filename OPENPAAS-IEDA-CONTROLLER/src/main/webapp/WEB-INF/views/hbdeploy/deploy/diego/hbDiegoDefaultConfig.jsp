<%
/* =================================================================
 * 작성일 : 2018.06
 * 작성자 : 배병욱
 * 상세설명 : DIEGO 기본 정보 관리 화면
 * =================================================================
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri = "http://www.springframework.org/tags" %>

<script type="text/javascript">
var search_data_fail_msg = '<spring:message code="common.data.select.fail"/>'//목록을 가져오는데 실패하였습니다.
var save_lock_msg='<spring:message code="common.save.data.lock"/>';//등록 중 입니다.
var save_info_msg = '<spring:message code="common.save.data.info.lock"/>';//저장 중 입니다.
var text_required_msg='<spring:message code="common.text.vaildate.required.message"/>';//을(를) 입력하세요.
var select_required_msg='<spring:message code="common.select.vaildate.required.message"/>';//을(를) 선택하세요.
var text_ip_msg = '<spring:message code="common.text.validate.ip.message"/>';//IP을(를) 확인 하세요.
var deploy_type = '<spring:message code="common.deploy.type.diego.name"/>';//CF DEPLOY TYPE VALUE
var country_parent_code = '<spring:message code="common.code.country.code.parent"/>';//ieda_common_code country 조회
var list_lock_msg = "목록을 조회 중입니다.";

//setting variable
var defaultInfo = ""; //기본 정보
var directorInfo = "";
var iaas = "";
var diegoId = "";
var cfInfo = "";
var diegoInfo = "";

var defaultLayout = {
        layout2: {
            name: 'layout2',
            padding: 4,
            panels: [
                { type: 'left', size: '65%', resizable: true, minSize: 300 },
                { type: 'main', minSize: 300 }
            ]
        },
        /********************************************************
         *  설명 : DIEGO DefaultInfo 그리드
        *********************************************************/
        grid: {
            name: 'default_GroupGrid',
            header: '<b>기본 정보</b>',
            method: 'GET',
            multiSelect: false,
            show: {
                selectColumn: true,
                footer: true
            },
            style: 'text-align: center',
            columns:[
                   { field: 'recid', hidden: true },
                   { field: 'id', caption:'ID', hidden:true},
                   { field: 'directorId', caption: 'Director id', hidden:true},
                   { field: 'defaultConfigName', caption: '기본 정보 별칭', size:'150px', style:'text-align:center;' },
                   { field: 'iaasType', caption: '인프라 환경 타입', size:'120px', style:'text-align:center;' ,render: function(record){ 
                       if(record.iaasType.toLowerCase() == "aws"){
                           return "<img src='images/iaasMgnt/aws-icon.png' width='80' height='30' />";
                       }else if (record.iaasType.toLowerCase() == "openstack"){
                           return "<img src='images/iaasMgnt/openstack-icon.png' width='90' height='35' />";
                       }
                   }},
                   { field: 'deploymentName', caption: '배포 명', size:'140px', style:'text-align:center;'},
                   { field: 'diegoRelease', caption: 'DIEGO 릴리즈', size:'140px', style:'text-align:center;'
                       , render :function(record){
                           if( !checkEmpty(record.diegoReleaseName) && !checkEmpty(record.diegoReleaseVersion) ){
                               return record.diegoReleaseName +"/"+ record.diegoReleaseVersion;
                           }
                           else{
                               return "&ndash;"
                           }
                       }
                   },
                   { field: 'gardenRelease', caption: 'garden 릴리즈', size:'140px', style:'text-align:center;'
                       , render :function(record){
                           if( !checkEmpty(record.gardenReleaseName) && !checkEmpty(record.gardenReleaseVersion) ){
                               return record.gardenReleaseName +"/"+ record.gardenReleaseVersion;
                           }else{
                               return "&ndash;"
                           }
                       }
                   },
                   { field: 'cflinuxRelease', caption: 'cflinuxfs2 릴리즈', size:'140px', style:'text-align:center;'
                       , render :function(record){
                           if( !checkEmpty(record.cflinuxfs2rootfsreleaseName) && !checkEmpty(record.cflinuxfs2rootfsreleaseVersion) ){
                               return record.cflinuxfs2rootfsreleaseName +"/"+ record.cflinuxfs2rootfsreleaseVersion;
                           }
                           else{
                               return "&ndash;"
                           }
                       }
                   },
                   { field: 'cfConfigName', caption: '연동 CF 별칭 정보', size:'140px', style:'text-align:center;'}
            ],
            onSelect : function(event) {
                event.onComplete = function() {
                    $('#deleteBtn').attr('disabled', false);
                    settingDefaultInfo();
                    return;
                }
            },
            onUnselect : function(event) {
                event.onComplete = function() {
                    resetForm();
                    $('#deleteBtn').attr('disabled', true);
                    return;
                }
            },
            onLoad:function(event){
                if(event.xhr.status == 403){
                    location.href = "/abuse";
                    event.preventDefault();
                }
                getReleases();//릴리즈 조회    
            },
            onError : function(event) {
            },
            form: { 
                header: 'Edit Record',
                name: 'regPopupDiv',
                fields: [
                    { name: 'recid', type: 'text', html: { caption: 'ID', attr: 'size="10" readonly' } },
                    { name: 'fname', type: 'text', required: true, html: { caption: 'First Name', attr: 'size="40" maxlength="40"' } },
                    { name: 'lname', type: 'text', required: true, html: { caption: 'Last Name', attr: 'size="40" maxlength="40"' } },
                    { name: 'email', type: 'email', html: { caption: 'Email', attr: 'size="30"' } },
                    { name: 'sdate', type: 'date', html: { caption: 'Date', attr: 'size="10"' } }
                ],
            }
        }
}

$(function(){
    $('#default_GroupGrid').w2layout(defaultLayout.layout2);
    w2ui.layout2.content('left', $().w2grid(defaultLayout.grid));
    w2ui['layout2'].content('main', $('#regPopupDiv').html());
    doSearch();
    
    initView();
    
    $("#deleteBtn").click(function(){
        if($("#deleteBtn").attr('disabled') == "disabled") return;
        var selected = w2ui['default_GroupGrid'].getSelection();
        if( selected.length == 0 ){
            w2alert("선택된 정보가 없습니다.", "디렉터 인증서 삭제");
            return;
        }
        else {
            var record = w2ui['default_GroupGrid'].get(selected);
            w2confirm({
                title        : "기본 정보",
                msg            : "기본 정보 ("+record.defaultConfigName + ")을 삭제하시겠습니까?",
                yes_text    : "확인",
                no_text        : "취소",
                yes_callBack: function(event){
                    deleteDiegoDefaultConfigInfo(record.recid, record.defaultConfigName);
                },
                no_callBack    : function(){
                    w2ui['default_GroupGrid'].clear();
                    doSearch();
                }
            });
        }
    });
});

/********************************************************
 * 설명 : Diego 설치 릴리즈 목록 조회 
 * 기능 : getReleases
 *********************************************************/
function getReleases(){
     $('[data-toggle="popover"]').popover();
     getReleaseVersionList();
    cfInfo = new Array(); //CF  릴리즈
    gardenReleaseName = new Array(); //Garden-Linux 릴리즈
    diegoReleases = new Array(); //DIEGO 릴리즈
    stemcells = new Array(); //STEMCELL
    //화면 LOCK
    w2popup.lock("릴리즈를 조회 중입니다.", true);
    getCfRelease(); //Diego 릴리즈 조회
}

/********************************************************
 * 설명 :  Diego 릴리즈 설치 지원 버전 목록 조회
 * 기능 : getReleaseVersionList
 *********************************************************/
function getReleaseVersionList(){
    var contents = "";
    contents = "<table id='popoverTable'><tr><th>릴리즈 유형</th><th>릴리즈 버전</th></tr>";
    contents += "<tr><td>diego</td><td>1.25.1</td></tr><tr><td>diego</td><td>1.25.3</td></tr><tr><td>paasta-container</td><td>3.0</td></tr><tr><td>diego</td><td>1.34.0</td></tr><tr><td>paasta-container</td><td>3.1</td></tr>";
    contents += "</table>";
    $('.diego-info').attr('data-content', contents);
}

/********************************************************
 * 설명 : CF 정보 조회
 * 기능 : getCfRelease
 *********************************************************/
var arrayCFInfoJSON = [];
function getCfRelease() {
    if($("select[name=cfInfo]").attr("disabled") == "disabled"){
        $("select[name=cfInfo]").removeAttr("disabled");
    }
    $.ajax({
        type :"GET",
        url :"/deploy/hbDiego/list/cf",
        contentType :"application/json",
        async :true,
        success :function(data, status) {
            if(data.records.length==0){
                var option = "<option value=''>Diego 연동 CF가 존재하지않습니다.</option>";
                $("select[name='cfInfo']").html(option);
                cfInfoYn = true;
            }
            cfInfo = new Array();
            if(data.records != null){
                var option = "";
                option += '<option value="">Diego 연동 CF 정보 별칭을 선택 하세요.</option>';
                data.records.map(function(obj) {
                    var getCfInfo =  $("#getCfInfo");
                        cfInfo.push(obj.cfConfigName);
                        if(obj.cfConfigName == defaultInfo.cfConfigName){
                            option += '<option selected value="'+obj.cfConfigName+'">'+obj.cfConfigName+'</option>';
                        }else{
                            option += '<option value="'+obj.cfConfigName+'">'+obj.cfConfigName+'</option>';
                        }
                        $("input[name='cfReleaseVersion']").val(obj.defaultConfigVO.releaseVersion);
                        $("select[name='cfInfo']").html(option);
                        $("input[name='cfKeyFile']").val(obj.keyConfigVO.keyFileName);
                        arrayCFInfoJSON=data;
                });
            }
            //getDiegoRelease();
            if( defaultInfo != "" ){
                setCfDeployFile(defaultInfo.cfConfigName);
            }
        },
        error :function(e, status) {
            w2alert("CF 정보를 가져오는데 실패하였습니다.", "DIEGO 설치");
        }
    });
}

/********************************************************
 * 설명 : 인프라 환경 별 디렉터 정보 조회
 * 기능 : getHbDirectorList
 *********************************************************/
function getHbDirectorList(iaas){
    var option = "<option value=''>디렉터 정보를 선택하세요.</option>";
    if(iaas == ""){
        $("select[name=directorInfo]").attr("disabled", "disabled");
        $("select[name='directorInfo']").html(option);
    } else {
        w2utils.lock($("#layout_layout_panel_main"), list_lock_msg, true);
        if($("select[name=directorInfo]").attr("disabled") == "disabled"){
            $("select[name=directorInfo]").removeAttr("disabled");
        }
        getReleaseVersionList(iaas);
        $.ajax({
            type : "GET",
            url : "/common/hbDeploy/director/list/"+iaas+"",
            contentType : "application/json",
            async : true,
            success : function(data, status) {
                if(data.records.length != 0){
                    data.records.map(function (obj){
                        if(obj.iedaDirectorConfigSeq == directorInfo) {
                            option += "<option selected value="+obj.iedaDirectorConfigSeq+">"+obj.directorName+"/"+obj.directorUrl+"</option>";
                        } else {
                            option += "<option value="+obj.iedaDirectorConfigSeq+">"+obj.directorName+"/"+obj.directorUrl+"</option>";
                        }
                    });
                }else if (data.records.length == 0){
                    option = "<option value=''>디렉터가 존재 하지 않습니다.</option>";
                }
                $("#directorInfo").html(option);
                w2utils.unlock($("#layout_layout_panel_main"));
            },
            error : function( request, status, error ) {
                var errorResult = JSON.parse(request.responseText);
                w2alert(errorResult.message, "리소스 정보 저장");
                w2utils.unlock($("#layout_layout_panel_main"));
                resetForm();
            }
        });
    }
}

/********************************************************
 * 설명 : 기본 정보 수정 데이터 설정
 * 기능 : settingDefaultInfo
 *********************************************************/
function settingDefaultInfo(){
    var selected = w2ui['default_GroupGrid'].getSelection();
    var record = w2ui['default_GroupGrid'].get(selected);
    if(record == null) {
        w2alert("기본 정보 설정 중 에러가 발생 했습니다.");
        return;
    }
    iaas = record.iaasType;
    defaultInfo = record;
    
    $("input[name=defaultId]").val(record.id);
    $("input[name=defaultConfigName]").val(record.defaultConfigName);
    $("select[name=iaasType]").val(record.iaasType.toLowerCase());
    $("input[name='deploymentName']").val(record.deploymentName);
    $("select[name='directorInfo']").val(record.directorName);
    $("select[name='diegoReleases']").val(record.diegoReleaseName);
    $("select[name='gardenReleases']").val(record.gardenReleaseName);
    $("select[name='cfLinuxReleases']").val(record.cfLinuxReleaseName);
    $("input[name='ingestorIp']").val(record.ingestorIp);
    
    if( !checkEmpty(record.paastaMonitoringUse) ){
        if(record.paastaMonitoringUse == 'false'){
            $('input:checkbox[name=paastaMonitoring]').attr("checked", false);
        }else{
            $('input:checkbox[name=paastaMonitoring]').attr("checked", true);
            $('input:checkbox[name=paastaMonitoring]').removeAttr("disabled");
            $("input[name='ingestorIp']").removeAttr("disabled");
        }
    }
    
    directorInfo = record.directorId;
    getHbDirectorList(iaas);
    getReleaseList(directorInfo);
    getCfRelease();
    //setInputDisplay(record.releaseName+"/"+record.releaseVersion);
    //setDisabledMonitoring(record.diegoRelease+"/"+record.releaseVersion);

}

/********************************************************
 * 설명 : 초기 화면 View
 * 기능 : initView
 *********************************************************/
function initView(){
     $("input[name='ingestorIp']").attr("disabled", true);
     checkPaasTAMonitoringUseYn();
}

/********************************************************
 * 설명 : PaaS-TA 모니터링 사용 체크 검사
 * 기능 : checkPaasTAMonitoringUseYn
 *********************************************************/
function checkPaasTAMonitoringUseYn(value){
    var cnt = $("input[name=paastaMonitoring]:checkbox:checked").length;
    if(cnt > 0 ){
        $("input[name='ingestorIp']").attr("disabled", false);
    }else{
        $("input[name='ingestorIp']").css({"border-color" : "rgb(187, 187, 187)"}).parent().find(".isMessage").text("");
         //값 초기화
        $("input[name='ingestorIp']").val("");
        //Read-only
        $("input[name='ingestorIp']").attr("disabled", true);
    }
}

/********************************************************
 * 설명 : 기본 정보 목록 조회
 * 기능 : doSearch
 *********************************************************/
function doSearch() {
    defaultInfo = [];//인프라 환경 설정 정보
    iaas = "";
    resetForm();
    w2ui['default_GroupGrid'].clear();
    //w2ui['regPopupDiv'].clear();
    w2ui['default_GroupGrid'].load('/deploy/hbDiego/default/list');
    doButtonStyle(); 
}

/********************************************************
 * 설명 : 초기 버튼 스타일
 * 기능 : doButtonStyle
 *********************************************************/
function doButtonStyle() {
    $('#deleteBtn').attr('disabled', true);
}

/********************************************************
 * 설명 : 전역변수 초기화 
 * 기능 : initSetting
 *********************************************************/
function initSetting(){
    defaultInfo = ""; //기본 정보
    directorInfo = "";
    iaas = "";
    diegoId = "";
    diegoInfo = "";
    doSearch();
}

/********************************************************
 * 설명 : 팝업창 닫을 경우
 * 기능 : popupClose
 *********************************************************/
function popupClose() {
     $().w2destroy("layout2");
    //params init
    initSetting();
}

/********************************************************
 * 설명 : 기본 정보 리셋
 * 기능 : resetForm
 *********************************************************/
function resetForm(status){
    defaultInfo = [];
    cfInfo = "";
    directorInfo = "";
    $(".panel-body").find("p").remove();
    $(".panel-body").children().children().children().css("borderColor", "#bbb");
    $("input[name=deploymentName]").val("");
    $("select[name=directorInfo]").val("");
    $("input[name=ingestorIp]").val("");
    $("input[name=defaultId]").val("");
    
    $("select[name=directorInfo]").html("<option value='' >디렉터 명을 선택하세요.</option>");
    $("select[name=directorInfo]").attr("disabled", "disabled");
    
    $("select[name=iaasType]").val("");
    $("select[name=diegoReleases]").html("<option value='' >DIEGO 릴리즈를 선택하세요.</option>");
    $("select[name=diegoReleases]").attr("disabled", "disabled");
    
    $("select[name=gardenReleases]").html("<option value='' >garden 릴리즈를 선택하세요.</option>");
    $("select[name=gardenReleases]").attr("disabled", "disabled");
    
    $("select[name=cfLinuxReleases]").html("<option value='' >cflinuxfs2 릴리즈를 선택하세요.</option>");
    $("select[name=cfLinuxReleases]").attr("disabled", "disabled");
    
    $("select[name=cfInfo]").html("<option value='' >Diego 연동 CF 정보 별칭을 선택하세요.</option>");
    $("select[name=cfInfo]").attr("disabled", "disabled");
    
    $("input[name='paastaMonitoring']").attr("checked", false);
    $("input[name=ingestorIp]").attr("disabled", "disabled");
    
    if(status=="reset"){
        w2ui['default_GroupGrid'].clear();
        doSearch();
    }
    document.getElementById("settingForm").reset();
}

/********************************************************
 * 설명 : 다른 페이지 이동 시 호출 Function
 * 기능 : clearMainPage
 *********************************************************/
function clearMainPage() {
    $().w2destroy('default_GroupGrid');
    $().w2destroy('layout2');
}

/********************************************************
 * 설명 : DIEGO 디렉터 유무 확인
 * 기능 : getReleaseList(value)
 *********************************************************/
function getReleaseList(directorId){
    if(directorId == ""){
        $("select[name=diegoReleases]").html("<option value='' >DIEGO 릴리즈를 선택하세요.</option>");
        $("select[name=diegoReleases]").attr("disabled", "disabled");
        $("select[name=gardenReleases]").html("<option value='' >DIEGO 릴리즈를 선택하세요.</option>");
        $("select[name=gardenReleases]").attr("disabled", "disabled");
        $("select[name=cfLinuxReleases]").html("<option value='' >DIEGO 릴리즈를 선택하세요.</option>");
        $("select[name=cfLinuxReleases]").attr("disabled", "disabled");
        w2alert("디렉터 정보가 존재 하지 않습니다.", "리소스 정보 저장");
        return;
    }else {
        if($("select[name=diegoReleases]").attr("disabled") == "disabled"){
            $("select[name=diegoReleases]").removeAttr("disabled");
        }
        getDiegoRelease(directorId);
    }
}

/********************************************************
 * 설명 : DIEGO 릴리즈 조회
 * 기능 : getDiegoRelease
 *********************************************************/
function getDiegoRelease(directorId) {
    w2utils.lock($("#layout_layout_panel_main"), list_lock_msg, true);
    $.ajax({
        type : "GET",
        url : "/common/deploy/release/list/diego/"+directorId,
        contentType : "application/json",
        success : function(data, status) {
            releases = new Array();
            if( data.records != null){
                w2popup.unlock();
                var option = "<option value=''>DIEGO 릴리즈를 선택하세요.</option>";
                data.records.map(function(obj) {
                    releases.push(obj.version);
                    if( defaultInfo.diegoReleaseName == obj.name && defaultInfo.diegoReleaseVersion == obj.version){
                        option += "<option value='"+obj.name+"/"+obj.version+"' selected>"+obj.name+"/"+obj.version+"</option>";
                    }else{
                        option += "<option value='"+obj.name+"/"+obj.version+"'>"+obj.name+"/"+obj.version+"</option>";    
                    }
                });
            }
            $("select[name='diegoReleases']").html(option);
            w2utils.unlock($("#layout_layout_panel_main"));
            if($("select[name=gardenReleases]").attr("disabled") == "disabled"){
                $("select[name=gardenReleases]").removeAttr("disabled");
            }
            if($("select[name=cfLinuxReleases]").attr("disabled") == "disabled"){
                $("select[name=cfLinuxReleases]").removeAttr("disabled");
            }
            getgardenRelease(directorId);
            getcflinuxfs2RootfsRelease(directorId)
        },
        error : function(e, status) {
            w2utils.unlock($("#layout_layout_panel_main"));
            w2popup.unlock();
            w2alert("DIEGO Release List 를 가져오는데 실패하였습니다.", "DIEGO 설치");
        }
    });
}

/********************************************************
 * 설명 : garden-Linux 릴리즈 조회
 * 기능 : getgardenRelease
 *********************************************************/
function getgardenRelease(directorId) {
    $.ajax({
        type :"GET",
        url :"/common/deploy/release/list/garden-runc/"+directorId,
        contentType :"application/json",
        async :true,
        success :function(data, status) {
            gardenReleaseName = new Array();
            if( data.records != null){
                var option = "<option value=''>Garden 릴리즈를 선택하세요.</option>";
                data.records.map(function(obj) {
                    if( defaultInfo.gardenReleaseName == obj.name && defaultInfo.gardenReleaseVersion == obj.version){
                        option += "<option value='"+obj.name+"/"+obj.version+"' selected>"+obj.name+"/"+obj.version+"</option>";
                    }else{
                        option += "<option value='"+obj.name+"/"+obj.version+"'>"+obj.name+"/"+obj.version+"</option>";
                    }
                });
            }
            $("select[name='gardenReleases']").html(option);
        },
        error :function(e, status) {
            w2alert("Garden Linux Release List 를 가져오는데 실패하였습니다.", "DIEGO 설치");
        }
    });
}

/********************************************************
 * 설명 : cflinuxfs2Rootfs 릴리즈 조회 
 * 기능 : getcflinuxfs2RootfsRelease
 *********************************************************/
function getcflinuxfs2RootfsRelease(directorId){
    $.ajax({
        type :"GET",
        url :"/common/deploy/release/list/cflinuxfs2-rootfs/"+directorId,
        contentType :"application/json",
        async :true,
        success :function(data, status) {
            cflinuxfs2rootfsrelease = new Array();
            if( data.records != null){
                var option = "<option value=''>Cf-linuxfs2 릴리즈를 선택하세요.</option>";
                data.records.map(function(obj) {
                    if( defaultInfo.cflinuxfs2rootfsreleaseName == obj.name && defaultInfo.cflinuxfs2rootfsreleaseVersion == obj.version){
                        option += "<option value='"+obj.name+"/"+obj.version+"' selected>"+obj.name+"/"+obj.version+"</option>";
                        //$('.w2ui-msg-body #cflinux').css('display','block');
                    }else{
                        option += "<option value='"+obj.name+"/"+obj.version+"'>"+obj.name+"/"+obj.version+"</option>";    
                    }
                });
            }
            $("select[name='cfLinuxReleases']").html(option);
        },
        error :function(e, status) {
            w2popup.unlock();
            w2alert("getcflinuxfs2RootfsRelease List 를 가져오는데 실패하였습니다.", "DIEGO 설치");
        }
    });
}


/********************************************************
 * 설명 : paasta-container v2.0 이상에서 지원
 * 기능 : setDisabledMonitoring
 *********************************************************/
function setDisabledMonitoring(val){
    if( !checkEmpty(val)){
        var diegoReleaseName = val.split("/")[0];
        var diegoReleaseVersion = val.split("/")[1];
        //paasta-container v2.0 이상 PaaS-TA 모니터링 지원 checkbox
        if( diegoReleaseName.indexOf("paasta-container") > -1 && compare(diegoReleaseVersion, "2.0") > -1){
            $(' #paastaMonitoring').attr('disabled',false);
        }else{
            $(' #paastaMonitoring').attr('disabled',true);
            if( $(" input:checkbox[name='paastaMonitoring']").is(":checked")){
                $(" input:checkbox[name='paastaMonitoring']").prop('checked',false);
                checkPaasTAMonitoringUseYn(diegoReleaseName+"/"+diegoReleaseVersion);
            }
        }
    }
    
}

/********************************************************
 * 설명 : PaaS-TA 모니터링 사용 체크 검사
 * 기능 : checkPaasTAMonitoringUseYn
 *********************************************************/
function checkPaasTAMonitoringUseYn(value){
    var cnt = $("input[name='paastaMonitoring']:checkbox:checked").length;
    if(cnt > 0 ){
        $(" input[name='ingestorIp']").attr("disabled", false);
    }else{
        $(" input[name='ingestorIp']").css({"border-color" : "rgb(187, 187, 187)"}).parent().find(".isMessage").text("");
        //값 초기화
        $(" input[name='ingestorIp']").val("");
        //Read-only
        $(" input[name='ingestorIp']").attr("disabled", true);
    }
     
}

/********************************************************
 * 설명 : CF 배포 파일 설정
 * 기능 : setCfDeployFile
 *********************************************************/
function setCfDeployFile(value){
    var cf_id;
    var cfDeploymentFile;
    var cfReleaseVersion;
    var cfKey;
    for(var i=0;i<arrayCFInfoJSON.records.length;i++){
        if(value==arrayCFInfoJSON.records[i].cfConfigName){
            cf_id = arrayCFInfoJSON.records[i].id;
            cfDeploymentFile = arrayCFInfoJSON.records[i].deploymentFile;
            cfReleaseVersion = arrayCFInfoJSON.records[i].defaultConfigVO.releaseVersion;
            cfKey = arrayCFInfoJSON.records[i].keyConfigVO.keyFileName;
            break;
        }
    }
    $("input[name='cfReleaseVersion']").val(cfReleaseVersion);
    $("input[name='cfId']").val(cf_id);
    $("input[name='cfDeploymentFile']").val(cfDeploymentFile);
    $("input[name='cfKeyFile']").val(cfKey);
}

/********************************************************
 * 설명 :  기본정보 저장
 * 기능 : saveDefaultInfo
 *********************************************************/
function registDiegoDefaultConfigInfo() {

    var diegoRelease = $(" select[name='diegoReleases']").val();
    var gardenRelease = $(" select[name='gardenReleases']").val();
    var cflinuxRelease = $(" select[name='cfLinuxReleases']").val();
    
    var cfName = $(" select[name='cfInfo']").val();
    var monitoringUse = "";
    if( $("input:checkbox[name='paastaMonitoring']").is(":checked")){
        monitoringUse = "true";
    }else{
        monitoringUse = "false";
    }
    defaultInfo = {
                id                              : $("input[name=defaultId]").val(),
                iaasType                        : $("select[name='iaasType']").val().toUpperCase(),
                platform                        : "diego",
                defaultConfigName               : $("input[name='defaultConfigName']").val(),
                deploymentName                  : $("input[name='deploymentName']").val(),
                directorId                      : $("select[name='directorInfo']").val(),
                diegoReleaseName                : diegoRelease.split("/")[0],
                diegoReleaseVersion             : diegoRelease.split("/")[1],
                cfConfigName                    : cfName,
                cfId                            : $("input[name='cfId']").val(),
                cfDeploymentFile                : $("input[name='cfDeploymentFile']").val(),
                cfReleaseVersion                : $("input[name='cfReleaseVersion']").val(),
                keyFile                         : $("input[name='cfKeyFile']").val(),
                gardenReleaseName               : gardenRelease.split("/")[0],
                gardenReleaseVersion            : gardenRelease.split("/")[1],
                cflinuxfs2rootfsreleaseName     : cflinuxRelease.split("/")[0],
                cflinuxfs2rootfsreleaseVersion  : cflinuxRelease.split("/")[1],
                paastaMonitoringUse             : monitoringUse,
                ingestorIp                      : $("input[name='ingestorIp']").val(),
    }
    $.ajax({
        type :"PUT",
        url :"/deploy/hbDiego/default/save",
        contentType :"application/json",
        data :JSON.stringify(defaultInfo),
        success :function(data, status) {
            w2utils.unlock($("#layout_layout_panel_main"));
            doSearch();
        },
        error :function(request, status, error) {
            var errorResult = JSON.parse(request.responseText);
            w2alert(errorResult.message, "diego  기본정보 등록");
            return;
        }
    });
}

/********************************************************
 * 설명 : 기본 정보 삭제
 * 기능 : deleteDiegoDefaultConfigInfo
 *********************************************************/
function deleteDiegoDefaultConfigInfo(id, defaultConfigName){
    defaultInfo = {
        id: id,
        defaultConfigName: defaultConfigName
    }
    $.ajax({
        type: "DELETE",
        url: "/deploy/hbdiego/default/delete",
        contentType: "application/json",
        async: true,
        data: JSON.stringify(defaultInfo),
        success: function(status){
            doSearch();
        },
        error: function(request, status, error){
            var errorResult = JSON.parse(request.responseText);
            w2alert(errorResult.message);
        }
    });
}
</script>

<div id="main">
    <div class="page_site">이종 DIEGO 설치 > <strong>기본 정보 관리</strong></div>
    <!-- 사용자 목록-->
    <div class="pdt20">
        <div class="title fl"> 기본 정보 목록</div>
    </div>
    <div id="default_GroupGrid" style="width:100%;  height:700px;"></div>
</div>

<div id="regPopupDiv" hidden="true" >
    <form id="settingForm" action="POST">
    <input type="hidden" name="defaultId" />
        <div class="w2ui-page page-0" style="">
           <div class="panel panel-default">
               <div class="panel-heading"><b>기본 정보</b></div>
               <div class="panel-body" style="height:615px; overflow-y:auto;">
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">기본 정보 별칭</label>
                       <div>
                           <input class="form-control" name = "defaultConfigName" type="text"  maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="기본 정보 별칭을 입력 하세요."/>
                       </div>
                   </div>
                  <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">클라우드 인프라 환경</label>
                       <div>
                           <select class="form-control" onchange="getHbDirectorList(this.value);" name="iaasType" style="width: 320px; margin-left: 20px;">
                               <option value="">인프라 환경을 선택하세요.</option>
                               <option value="aws">AWS</option>
                               <option value="openstack">Openstack</option>
                           </select>
                       </div>
                   </div>
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">배포명</label>
                       <div>
                           <input class="form-control" name = "deploymentName" type="text"  maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="배포명을 입력 하세요."/>
                       </div>
                   </div>
                   
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">디렉터 명</label>
                       <div>
                            <select disabled class="form-control" onchange="getReleaseList(this.value);" id ="directorInfo" name="directorInfo" style="width: 320px; margin-left: 20px;">
                                <option value="">디렉터를 선택하세요.</option>
                            </select>
                       </div>
                   </div>
                   
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">DIEGO 릴리즈
                           <span class="glyphicon glyphicon glyphicon-question-sign diego-info" style="cursor:pointer;font-size: 14px;color: #157ad0;" data-toggle="popover"  data-trigger="click" data-html="true" title="설치 지원 버전 목록"></span>
                       </label>
                       <div>
                           <select class="form-control" disabled name="diegoReleases" style="width: 320px; margin-left: 20px;" onchange="setDisabledMonitoring(this.value);">
                               <option value="" >DIEGO 릴리즈를 선택하세요.</option>
                           </select>
                       </div>
                   </div>
                   
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">garden-runc 릴리즈</label>
                       <div>
                           <select class="form-control" disabled  name="gardenReleases" style="width: 320px; margin-left: 20px;">
                               <option value="" >garden 릴리즈를 선택하세요.</option>
                           </select>
                       </div>
                   </div>
                   
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">cflinuxfs2 릴리즈</label>
                       <div>
                           <select class="form-control" name="cfLinuxReleases" style="width: 320px; margin-left: 20px;">
                               <option value="" >cflinuxfs2 릴리즈를 선택하세요.</option>
                           </select>
                       </div>
                   </div>
                   
                    <div class="w2ui-field" >
                        <label style="text-align:left; width:40%; padding-left: 20px; ">DIEGO 연동 CF 정보</label>
                        <div id="getCfInfo">
                            <select class="form-control" name="cfInfo" onchange="setCfDeployFile(this.value);" style="width: 320px; margin-left: 20px;">
                                <option value="">Diego 연동 CF 정보 별칭을 선택 하세요.</option>
                            </select>
                        </div>
                        <input name="cfId" type="hidden"/>
                        <input name="cfDeploymentFile" type="hidden"/>
                        <input name="cfReleaseVersion" type="hidden"/>
                        <input name="cfKeyFile" type="hidden"/>
                    </div>
                    
                    <div class="w2ui-field">
                        <label style="text-align:left; width:40%; padding-left: 20px; ">PaaS-TA 모니터링
                        <span class="glyphicon glyphicon glyphicon-question-sign paastaMonitoring-info" style="cursor:pointer;font-size: 14px;color: #157ad0;" data-toggle="popover"  data-trigger="click" data-html="true" title="PaaS-TA 모니터링"></span>
                        </label>
                        <div style=" width: 60%;">
                            <input name="paastaMonitoring" type="checkbox" id="paastaMonitoring" onchange="checkPaasTAMonitoringUseYn(this);" disabled />사용
                            <input name="cfPaastaMonitoring" type="hidden" id="cfPaastaMonitoring" />
                        </div>
                    </div>
                    
                    <div class="w2ui-field">
                        <label style="width:40%;text-align: left;padding-left: 20px;">Ingestor 서버 IP</label>
                        <div>
                            <input class="form-control" name="ingestorIp" type="text" style="width: 320px; margin-left: 20px;" disabled placeholder="예)10.0.0.0" />
                        </div>
                    </div>
                    
                </div>
           </div>
        </div>
    </form>
    <div id="regPopupBtnDiv" style="text-align: center; margin-top: 5px;">
        <sec:authorize access="hasAuthority('DEPLOY_HBCF_DEFAULT_ADD')">
        </sec:authorize>
            <span id="installBtn" onclick="$('#settingForm').submit();" class="btn btn-primary">등록</span>
        <span id="resetBtn" onclick="resetForm('reset');" class="btn btn-info">취소</span>
        <sec:authorize access="hasAuthority('DEPLOY_HBCF_DEFAULT_DELETE')">
        </sec:authorize>
            <span id="deleteBtn" class="btn btn-danger">삭제</span>
        
    </div>
</div>

<script>
$(function(){
    $("#settingForm").validate({
        ignore: [],
        rules: {
            defaultConfigName: {
                required: function(){
                    return checkEmpty( $("input[name='defaultConfigName']").val() );
                }
            },
            iaasType: {
                required: function(){
                    return checkEmpty( $("select[name='iaasType']").val() );
                }
            },
            deploymentName: {
                required: function(){
                    return checkEmpty( $("input[name='deploymentName']").val() );
                }
            },
            directorInfo: {
                required: function(){
                    return checkEmpty( $("select[name='directorInfo']").val() );
                }
            },
            cfInfo: {
                required: function(){
                    return checkEmpty( $("select[name='cfInfo']").val() );
                }
            },
            diegoReleases: {
                required: function(){
                    return checkEmpty( $("select[name='diegoReleases']").val() );
                }
            },
            gardenReleases: {
                required: function(){
                    return checkEmpty( $("select[name='gardenReleases']").val() );
                }
            },
            cfLinuxReleases: {
                required: function(){
                    return checkEmpty( $("select[name='cfLinuxReleases']").val() );
                }
            },
            ingestorIp: {
               required: function(){
                    if( $("#paastaMonitoring:checked").val() == "on"){
                         return checkEmpty( $("input[name='ingestorIp']").val() );
                    }else{
                         return false;
                    }
               },
               ipv4 : function(){
                    if( $("#paastaMonitoring:checked").val() == "on"){
                         return $("input[name='ingestorIp']").val()
                    }else{
                         return "0.0.0.0";
                    }
               }
            },
        },
        messages:{
            defaultConfigName: {
                required: "기본 정보 별칭"+text_required_msg
            },
            iaasType: {
                required: "IaaS 정보"+select_required_msg
            },
            deploymentName: {
                required: "배포 명"+text_required_msg
            },
            directorInfo: {
                required: "디렉터 명"+select_required_msg
            },
            diegoReleases: {
                required: "DIEGO 릴리즈"+select_required_msg
            },
            gardenReleases: {
                required: "garden 릴리즈"+select_required_msg
            },
            cfLinuxReleases: {
                required: "cflinux 릴리즈"+select_required_msg
            },
            ingestorIp: {
                required: "ingestor 서버 Ip"+text_required_msg
            },
            cfInfo: {
                required: "CF 정보"+select_required_msg
            }
        },
        unhighlight: function(element) {
            setHybridSuccessStyle(element);
        },
        errorPlacement: function(error, element) {
            //do nothing
        },
        invalidHandler: function(event, validator) {
            var errors = validator.numberOfInvalids();
            if (errors) {
                setHybridInvalidHandlerStyle(errors, validator);
            }
        },
        submitHandler: function (form) {
            registDiegoDefaultConfigInfo();
        }
    });
});
</script>