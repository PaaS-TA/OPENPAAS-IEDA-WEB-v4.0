<%
/* =================================================================
 * 작성일 : 2018.06
 * 작성자 : 이동현
 * 상세설명 : 기본 인증서 관리 화면
 * =================================================================
 */ 
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri = "http://www.springframework.org/tags" %>

<script type="text/javascript">
var text_required_msg = '<spring:message code="common.text.vaildate.required.message"/>';//을(를) 입력하세요.
var text_injection_msg='<spring:message code="common.text.validate.sqlInjection.message"/>';//입력하신 값은 입력하실 수 없습니다.
var select_required_msg='<spring:message code="common.select.vaildate.required.message"/>';//을(를) 선택하세요.
var text_ip_msg = '<spring:message code="common.text.validate.ip.message"/>';//IP을(를) 확인 하세요.

var list_lock_msg = "목록을 조회 중입니다.";
var defaultInfo = []; //기본 정보
var directorInfo = "";
var iaas = "";

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
         *  설명 : CF DefaultInfo 그리드
        *********************************************************/
        grid: {
            name: 'default_GroupGrid',
            header: '<b>기본 정보</b>',
            method: 'GET',
                multiSelect: false,
            show: {
                    selectColumn: true,
                    footer: true},
            style: 'text-align: center',
            columns:[
                   { field: 'recid', hidden: true },
                   { field: 'defaultConfigName', caption: '기본 정보 별칭', size:'150px', style:'text-align:center;' },
                   { field: 'iaasType', caption: '인프라 환경 타입', size:'120px', style:'text-align:center;' ,render: function(record){ 
                       if(record.iaasType.toLowerCase() == "aws"){
                           return "<img src='images/iaasMgnt/aws-icon.png' width='80' height='30' />";
                       }else if (record.iaasType.toLowerCase() == "openstack"){
                           return "<img src='images/iaasMgnt/openstack-icon.png' width='90' height='35' />";
                       }
                   }},
                   { field: 'deploymentName', caption: '배포 명', size:'140px', style:'text-align:center;'},
                   { field: 'releaseName', caption: 'CF 릴리즈 명', size:'140px', style:'text-align:center;'},
                   { field: 'releaseVersion', caption: 'CF 릴리즈 버전', size:'100px', style:'text-align:center;'},
                   { field: 'domain', caption: 'CF 도메인', size:'140px', style:'text-align:center;'},
                   { field: 'domainOrganization', caption: 'CF 기본 조직 명', size:'120px', style:'text-align:center;'},
                   { field: 'loginSecret', caption: 'CF 로그인 비밀번호', size:'150px', style:'text-align:center;'}
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
            },onLoad:function(event){
                if(event.xhr.status == 403){
                    location.href = "/abuse";
                    event.preventDefault();
                }
            },onError : function(event) {
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
                    deleteCfDefaultConfigInfo(record.recid, record.defaultConfigName);
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
 * 설명 : 초기 화면 View
 * 기능 : initView
 *********************************************************/
function initView(){
     $("input[name='ingestorIp']").attr("disabled", true);
     checkPaasTAMonitoringUseYn();
}

/********************************************************f
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
                w2alert(errorResult.message, "CF 기본 정보저장");
                w2utils.unlock($("#layout_layout_panel_main"));
                resetForm();
            }
        });
    }
}

function getReleaseList(directorId){
    if(directorId == ""){
        w2alert("디렉터 정보가 존재 하지 않습니다.", "CF 기본 정보 저장");
        return;
    }else {
        if($("select[name=releases]").attr("disabled") == "disabled"){
            $("select[name=releases]").removeAttr("disabled");
        }
        getCfRelease(directorId);
    }
}

/********************************************************
 * 설명 : CF 릴리즈 설치 지원 버전 목록 조회
 * 기능 : getReleaseVersionList
 *********************************************************/
function getReleaseVersionList(){
     var contents = "";
     $.ajax({
        type :"GET",
        url :"/common/deploy/list/releaseInfo/cf/"+iaas, 
        contentType :"application/json",
        success :function(data, status) {
            if ( !checkEmpty(data) ) {
                contents = "<table id='popoverTable'><tr><th>릴리즈 유형</th><th>릴리즈 버전</th></tr>";
                data.map(function(obj) {
                    contents += "<tr><td>" + obj.releaseType+ "</td><td>" +  obj.minReleaseVersion +"</td></tr>";
                });
                contents += "</table>";
                $('.cf-info').attr('data-content', contents);
            }
        },
        error :function(request, status, error) {
            var errorResult = JSON.parse(request.responseText);
            w2alert(errorResult.message, "CF 릴리즈 설치 지원 버전");
        }
    });
}

/********************************************************
 * 설명 : CF 릴리즈 조회
 * 기능 : getCfRelease
 *********************************************************/
function getCfRelease(directorId) {
    w2utils.lock($("#layout_layout_panel_main"), list_lock_msg, true);
    $.ajax({
        type : "GET",
        url : "/common/deploy/release/list/cf/"+directorId,
        contentType : "application/json",
        success : function(data, status) {
            releases = new Array();
            if( data.records != null){
                w2popup.unlock();
                var option = "<option value=''>CF 릴리즈를 선택하세요.</option>";
                data.records.map(function(obj) {
                    releases.push(obj.version);
                    if( defaultInfo.releaseName == obj.name && defaultInfo.releaseVersion == obj.version){
                        option += "<option value='"+obj.name+"/"+obj.version+"' selected>"+obj.name+"/"+obj.version+"</option>";
                    }else{
                        option += "<option value='"+obj.name+"/"+obj.version+"'>"+obj.name+"/"+obj.version+"</option>";    
                    }
                });
            }
            $("select[name='releases']").html(option);
            w2utils.unlock($("#layout_layout_panel_main"));
        },
        error : function(e, status) {
            w2utils.unlock($("#layout_layout_panel_main"));
            w2popup.unlock();
            w2alert("Cf Release List 를 가져오는데 실패하였습니다.", "CF 설치");
        }
    });
}

/********************************************************
 * 설명 : 272 이상일 경우 화면 설정
 * 기능 : setInputDisplay
 *********************************************************/
function setInputDisplay(val){
    var name = val.split("/")[0];
    var version = val.split("/")[1];
    if( Number(version) >= 272 || (name.indexOf("paasta-controller") > -1 && compare(version, "3.0") > -1 ) || (name.indexOf("paasta-controller") > -1 && compare(version, "3.1") > -1 )){
        $("#loggregator").css("display", "block");
        if($("select[name=loggregatorReleases]").attr("disabled") == "disabled"){
            $("select[name=loggregatorReleases]").removeAttr("disabled");
        }
        if( Number(version) == "3.1" || Number(version) == "287" ){
            $("#loggregator").css("display", "none");
            $("#loggregator").val("");
        }
    }
    
    if( !checkEmpty(val) || val != "undefined/undefined"){
        var cfReleaseName = val.split("/")[0];
        var cfReleaseVersion = val.split("/")[1];
        //paasta-controller v2.0.0 이상 PaaS-TA 모니터링 지원 checkbox
        if( cfReleaseName.indexOf("paasta-controller") > -1 && ( compare(cfReleaseVersion, "2.0") > -1 || compare(cfReleaseVersion, "3.0") > -1 || compare(cfReleaseVersion, "3.1") > -1) ){
            $('#paastaMonitoring').attr('disabled',false);
        }else{
            if( $("input:checkbox[name='paastaMonitoring']").is(":checked")){
                $("input:checkbox[name='paastaMonitoring']").prop('checked',false);
                checkPaasTAMonitoringUseYn();
            }
            $('#paastaMonitoring').attr('disabled',true);
        }
    }
    getLoggregatorRelease();
}

/********************************************************
 * 설명 : Loggregator 릴리즈 조회
 * 기능 : getLoggregatorRelease
 *********************************************************/
function getLoggregatorRelease(){
    w2utils.lock($("#layout_layout_panel_main"), list_lock_msg, true);
    var option = "";
    option += "<option value=''>Loggregator 릴리즈를 선택하세요.</option>";
    $.ajax({
        type : "GET",
        url : "/common/deploy/release/list/loggregator",
        contentType : "application/json",
        success : function(data, status) {
            w2popup.unlock();
            if( data.records.length > 0){
                data.records.map(function(obj) {
                    if(defaultInfo.loggregatorReleaseName == obj.name && defaultInfo.loggregatorReleaseVersion == obj.version){
                        option += "<option selected value='"+obj.name+"/"+obj.version+"'>"+obj.name+"/"+obj.version+"</option>";
                    } else {
                        option += "<option value='"+obj.name+"/"+obj.version+"'>"+obj.name+"/"+obj.version+"</option>";
                    }
                    
                });
            }else{
                option ="<option value=''>Loggregator 릴리즈가 필요합니다.</option>"
            }
            $("select[name='loggregatorReleases']").html(option);
            w2utils.unlock($("#layout_layout_panel_main"));
        },
        error : function(e, status) {
            w2utils.unlock($("#layout_layout_panel_main"));
            w2alert("Cf Release List 를 가져오는데 실패하였습니다.", "CF 설치");
        }
    });
}


/********************************************************
 * 설명 : paasta-controller v2.0 이상에서 지원 
 * 기능 : setDisabledMonitoring
 *********************************************************/
function setDisabledMonitoring(val){
    if( !checkEmpty(val) || val != "undefined/undefined"){
        var cfReleaseName = val.split("/")[0];
        var cfReleaseVersion = val.split("/")[1];
        //paasta-controller v2.0.0 이상 PaaS-TA 모니터링 지원 checkbox
        if( cfReleaseName.indexOf("paasta-controller") > -1 && ( compare(cfReleaseVersion, "2.0") > -1 || compare(cfReleaseVersion, "3.0") > -1 || compare(cfReleaseVersion, "3.1") > -1) ){
            $('#paastaMonitoring').attr('disabled',false);
        }else{
            if( $("input:checkbox[name='paastaMonitoring']").is(":checked")){
                $("input:checkbox[name='paastaMonitoring']").prop('checked',false);
                checkPaasTAMonitoringUseYn();
            }
            $('#paastaMonitoring').attr('disabled',true);
        }
    }
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
    defaultInfo = [];//인프라 환경 설정 정ㅈ보
    iaas = "";
    resetForm();
    w2ui['default_GroupGrid'].clear();
    //w2ui['regPopupDiv'].clear();
    w2ui['default_GroupGrid'].load('/deploy/hbCf/default/list');
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
 * 설명 : 기본 정보 등록
 * 기능 : registCfDefaultConfigInfo
 *********************************************************/
function registCfDefaultConfigInfo(){
    w2utils.lock($("#layout_layout_panel_main"), "정보 저장 중", true);
    
    
    defaultInfo = {
            iaasType             : $("select[name=iaasType]").val(),
            defaultConfigName    : $("input[name=defaultConfigName]").val(),
            id                   : $("input[name=defaultId]").val(),
            deploymentName       : $("input[name=deploymentName]").val(),
            directorInfo         : $("select[name=directorInfo]").val(),
            loginSecret          : $("input[name=loginSecret]").val(),
            domain               : $("input[name=domain]").val(),
            domainOrganization   : $("input[name=domainOrganization]").val(),
            releases             : $("select[name=releases]").val(),
            osConfReleases       : $("select[name=osConfReleases]").val(),
            loggregatorReleases  : $("select[name=loggregatorReleases]").val(),
            paastaMonitoring     : $("input:checkbox[name=paastaMonitoring]:checked").val(),
            ingestorIp           : $("input[name=ingestorIp]").val()
    }
    $.ajax({
        type : "PUT",
        url : "/deploy/hbCf/default/save",
        contentType : "application/json",
        async : true,
        data : JSON.stringify(defaultInfo),
        success : function(data, status) {
            w2utils.unlock($("#layout_layout_panel_main"));
            doSearch();
        },
        error : function( e, status ) {
            w2utils.unlock($("#layout_layout_panel_main"));
            var errorResult = JSON.parse(e.responseText);
            w2alert(errorResult.message, "기본 정보 저장");
        }
    });
}

/********************************************************
 * 설명 : 기본 정보 수정 데아터 설정
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
    $("select[name=iaasType]").val(record.iaasType);
    $("input[name='deploymentName']").val(record.deploymentName);
    $("input[name='directorName']").val(record.directorName);
    $("input[name='domain']").val(record.domain);
    $("input[name='domainOrganization']").val(record.domainOrganization);
    $("input[name='loginSecret']").val(record.loginSecret);
    $("input[name='ingestorIp']").val(record.ingestorIp);
    
    if( !checkEmpty(record.paastaMonitoringUse) ){
        $('input:checkbox[name=paastaMonitoring]').attr("checked", true);
        $("input[name='ingestorIp']").removeAttr("disabled");
    }
    
    directorInfo = record.directorId;
    getHbDirectorList(iaas);
    getReleaseList(directorInfo);
    setInputDisplay(record.releaseName+"/"+record.releaseVersion);
    setDisabledMonitoring(record.releaseName+"/"+record.releaseVersion);

}
/********************************************************
 * 설명 : 기본 정보 삭제
 * 기능 : deleteCfDefaultConfigInfo
 *********************************************************/
function deleteCfDefaultConfigInfo(id, defaultConfigName){
    defaultInfo = {
        id : id,
        defaultConfigName : defaultConfigName
    }
    $.ajax({
        type : "DELETE",
        url : "/deploy/hbCf/default/delete",
        contentType : "application/json",
        async : true,
        data : JSON.stringify(defaultInfo),
        success : function(status) {
            doSearch();
        }, error : function(request, status, error) {
            var errorResult = JSON.parse(request.responseText);
            w2alert(errorResult.message);
        }
    });
}

/********************************************************
 * 설명 : 화면 리사이즈시 호출
 *********************************************************/
$( window ).resize(function() {
    setLayoutContainerHeight();
});

/********************************************************
 * 설명 : Lock 팝업 메세지 Function
 * 기능 : lock
 *********************************************************/
function lock (msg) {
    w2popup.lock(msg, true);
}
/********************************************************
 * 설명 : 다른 페이지 이동 시 호출 Function
 * 기능 : clearMainPage
 *********************************************************/
function clearMainPage() {
    $().w2destroy('layout2');
    $().w2destroy('default_GroupGrid');
}
/********************************************************
 * 설명 : 기본 정보 리셋
 * 기능 : resetForm
 *********************************************************/
function resetForm(status){
    defaultInfo = [];
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
    $("select[name=releases]").html("<option value='' >CF 릴리즈를 선택하세요.</option>");
    $("select[name=releases]").attr("disabled", "disabled");
    
    $("select[name=loggregatorReleases]").html("<option value='' >Loggregator 릴리즈를 선택하세요.</option>");
    $("select[name=loggregatorReleases]").attr("disabled", "disabled");
    $("input[name='paastaMonitoring']").attr("checked", false);
    $("input[name=ingestorIp]").attr("disabled", "disabled");
    
    $("#loggregator").hide();
    
    if(status=="reset"){
        w2ui['default_GroupGrid'].clear();
        doSearch();
    }
    document.getElementById("settingForm").reset();
}

</script>
<div id="main">
    <div class="page_site">이종 CF 설치 > <strong>기본 정보 관리</strong></div>
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
                       <label style="width:40%;text-align: left;padding-left: 20px;">CF 릴리즈</label>
                       <div>
                           <select disabled class="form-control"  onchange = "setInputDisplay(this.value)"; name="releases" style="width: 320px; margin-left: 20px;">
                               <option value="" >CF 릴리즈를 선택하세요.</option>
                           </select>
                       </div>
                   </div>
                   <div class="w2ui-field" id="loggregator" style="display:none;">
                       <label style="width:40%;text-align: left;padding-left: 20px;">LOGGREGATOR 릴리즈</label>
                       <div>
                            <select disabled class="form-control" name="loggregatorReleases" style="width: 320px; margin-left: 20px;">
                                <option value="">LOGGREGATOR 릴리즈를 선택하세요.</option>
                            </select>
                       </div>
                   </div>
                   <div class="w2ui-field" id="osConfRelease" style="display:none;">
                       <label style="width:40%;text-align: left;padding-left: 20px;">OS-CONF 릴리즈</label>
                       <div>
                           <select class="form-control" disabled name="osConfReleases" style="width: 320px; margin-left: 20px;" onchange="checkBoshVersion(this.value)">
                               <option value="" >OS-CONF 릴리즈를 선택하세요.</option>
                           </select>
                       </div>
                   </div>
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">기본 조직명</label>
                       <div>
                           <input class="form-control" name = "domainOrganization" type="text"  maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="기본 조직명을 입력하세요."/>
                       </div>
                   </div>
                   
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">CF 도메인</label>
                       <div>
                           <input class="form-control" name = "domain" type="text"  maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="CF 도메인을 입력하세요."/>
                       </div>
                   </div>
                   
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">CF 로그인 패스워드</label>
                       <div>
                           <input class="form-control" name = "loginSecret" type="text"  maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="CF 로그인 패스워드를 입력하세요."/>
                       </div>
                   </div>
                   
                   <div class="w2ui-field">
                        <label style="width:40%;text-align: left;padding-left: 20px;">PaaS-TA 모니터링</label>
                        <div style="width: 60%">
                            <input style="margin-left: 20px;" name="paastaMonitoring" type="checkbox" id="paastaMonitoring" onclick="checkPaasTAMonitoringUseYn(this.value);" disabled/>사용
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
        <sec:authorize access="hasAuthority('CONFIG_DIRECTOR_MENU')">
            <span id="installBtn" onclick="$('#settingForm').submit();" class="btn btn-primary">등록</span>
        </sec:authorize>
        <span id="resetBtn" onclick="resetForm('reset');" class="btn btn-info">취소</span>
        <sec:authorize access="hasAuthority('CONFIG_DIRECTOR_MENU')">
            <span id="deleteBtn" class="btn btn-danger">삭제</span>
        </sec:authorize>
        
    </div>
</div>


<script>
$(function() {
    $("#settingForm").validate({
        ignore : [],
        rules: {
            defaultConfigName : {
                required : function(){
                    return checkEmpty( $("input[name='defaultConfigName']").val() );
                }
            }, deploymentName: { 
                required: function(){
                    return checkEmpty( $("input[name='deploymentName']").val() );
                }
            },  directorInfo: { 
                required: function(){
                    return checkEmpty( $("select[name='directorInfo']").val() );
                }
            }, ntp: { 
                required: function(){
                    return checkEmpty( $("input[name='ntp']").val() );
                }
            }, releases: { 
                required: function(){
                    return checkEmpty( $("select[name='releases']").val() );
                }
            }, ingestorIp : {
                  required: function(){
                      if( $("#paastaMonitoring:checked").val() == "on"){
                           return checkEmpty( $("input[name='ingestorIp']").val() );
                      }else{
                           return false;
                      }
                 },ipv4 : function(){
                      if( $(" #paastaMonitoring:checked").val() == "on"){
                           return $("input[name='ingestorIp']").val()
                      }else{
                           return "0.0.0.0";
                      }
                 }
            },  iaasType: { 
                required: function(){
                    return checkEmpty( $("select[name='iaasType']").val() );
                }
          },  domainOrganization: { 
              required: function(){
                  return checkEmpty( $("select[name='domainOrganization']").val() );
              }
          },  domain: { 
              required: function(){
                  return checkEmpty( $("select[name='domain']").val() );
              }
          },  loginSecret: { 
              required: function(){
                  return checkEmpty( $("select[name='loginSecret']").val() );
              }
          }
        }, messages: {
            defaultConfigName: { 
                 required:  "기본 정보 별칭" + text_required_msg
            }, deploymentName: { 
                 required:  "배포 명" + text_required_msg
            }, directorInfo: { 
                required:  "디렉터 정보"+select_required_msg
            }, releases: { 
                required:  "CF 릴리즈"+select_required_msg
            }, ntp: { 
                required:  "NTP"+text_required_msg
            }, domainOrganization: { 
                required:  "CF 조직" + text_required_msg
            }, domain: { 
                required:  "CF 도메인"+text_required_msg
            }, loginSecret: { 
                required:  "CF 패스워드"+text_required_msg
            }, iaasType: {
                required: "클라우드 인프라 환경"+select_required_msg
            }, ingestorIp: {
                required: "ingestor IP"+text_required_msg
                ,ipv4:  "ingestor IP"+text_ip_msg
            }
            
        }, unhighlight: function(element) {
            setHybridSuccessStyle(element);
        },errorPlacement: function(error, element) {
            //do nothing
        }, invalidHandler: function(event, validator) {
            var errors = validator.numberOfInvalids();
            if (errors) {
                setHybridInvalidHandlerStyle(errors, validator);
            }
        }, submitHandler: function (form) {
            registCfDefaultConfigInfo();
        }
    });
});
</script>