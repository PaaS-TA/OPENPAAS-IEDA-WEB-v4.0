<%
/* =================================================================
 * 작성일 : 2018.07
 * 작성자 : 이정윤
 * 상세설명 : Default 정보 관리 화면
 * =================================================================
 */ 
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri = "http://www.springframework.org/tags" %>

<script type="text/javascript">
var text_required_msg = '<spring:message code="common.text.vaildate.required.message"/>';//을(를) 입력하세요.
var select_required_msg='<spring:message code="common.select.vaildate.required.message"/>';//을(를) 선택하세요.
var search_data_fail_msg ='클라우드 인프라 환경을 선택하세요.';
var defaultConfigInfo = "";//기본 정보
var iaas = "";
var resourceLayout = {
        layout2: {
            name: 'layout2',
            padding: 4,
            panels: [
                { type: 'left', size: '65%', resizable: true, minSize: 300 },
                { type: 'main', minSize: 300 }
            ]
        },
        /********************************************************
         *  설명 : 기본 정보 목록 Grid
        *********************************************************/
        grid: {
            name: 'default_Grid',
            header: '<b>Default 정보</b>',
            method: 'GET',
            multiSelect: false,
            show: {
                selectColumn: true,
                footer: true},
            style: 'text-align: center',
            columns:[
                   { field: 'recid', hidden: true },
                   { field: 'id', hidden: true },
                   { field: 'defaultConfigName', caption: '배포 명 & 기본 정보 별칭', size:'20%', style:'text-align:center;' },
                   { field: 'iaasType', caption: '인프라 환경 타입', size:'20%', style:'text-align:center;' ,render: function(record){ 
                       if(record.iaasType.toLowerCase() == "aws"){
                           return "<img src='images/iaasMgnt/aws-icon.png' width='80' height='30' />";
                       }else if (record.iaasType.toLowerCase() == "openstack"){
                           return "<img src='images/iaasMgnt/openstack-icon.png' width='90' height='35' />";
                       }
                   }},
                   { field: 'cfDeploymentVersion', caption: 'CF Deployment 버전', size:'20%', style:'text-align:center;'},
                   { field: 'domain', caption: '도메인', size:'20%', style:'text-align:center;'},
                   { field: 'domainOrganization', caption: '기본 조직명', size:'20%', style:'text-align:center;'},
                   { field: 'cfDbType', caption: 'CF Database 유형', size:'20%', style:'text-align:center;'},
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
            actions: {
                Reset: function () {
                    this.clear();
                },
                Save: function () {
                    var errors = this.validate();
                    if (errors.length > 0) return;
                    if (this.recid == 0) {
                        w2ui.grid.add($.extend(true, { recid: w2ui.grid.records.length + 1 }, this.record));
                        w2ui.grid.selectNone();
                        this.clear();
                    } else {
                        w2ui.grid.set(this.recid, this.record);
                        w2ui.grid.selectNone();
                        this.clear();
                    }
                }
            }
        }
    }
}

$(function(){
    $('#default_Grid').w2layout(resourceLayout.layout2);
    w2ui.layout2.content('left', $().w2grid(resourceLayout.grid));
    w2ui['layout2'].content('main', $('#regPopupDiv').html());
    doSearch();
    
    $("#deleteBtn").click(function(){
        if($("#deleteBtn").attr('disabled') == "disabled") return;
        var selected = w2ui['default_Grid'].getSelection();
        if( selected.length == 0 ){
            w2alert("선택된 정보가 없습니다.", "기본  삭제");
            return;
        }
        else {
            var record = w2ui['default_Grid'].get(selected);
            w2confirm({
                title       : "기본 정보",
                msg         : "기본 정보 ("+record.defaultConfigName + ")을 삭제하시겠습니까?",
                yes_text    : "확인",
                no_text     : "취소",
                yes_callBack: function(event){
                    deleteHbCfDeploymentDefaultConfigInfo(record.recid, record.defaultConfigName);
                },
                no_callBack    : function(){
                    w2ui['default_Grid'].clear();
                    doSearch();
                }
            });
        }
    });
});

/********************************************************
 * 설명 : 기본  수정 정보 설정
 * 기능 : settingDefaultInfo
 *********************************************************/
function settingDefaultInfo(){
    var selected = w2ui['default_Grid'].getSelection();
    var record = w2ui['default_Grid'].get(selected);
    var option = "";
    if(record == null) {
        w2alert("기본 정보 설정 중 에러가 발생 했습니다.");
        return;
    }
    iaas = record.iaasType;
    $("input[name=defaultInfoId]").val(record.recid);
    $("input[name=defaultConfigName]").val(record.defaultConfigName);
    $("select[name=iaasType]").val(record.iaasType);
    $("select[name=cfDeploymentVersion]").html("<option value='"+record.cfDeploymentVersion+"' selected >"+record.cfDeploymentVersion+"</option>");
    $("input[name=domain]").val(record.domain);
    $("input[name=domainOrganization]").val(record.domainOrganization);
    
    option += "<option value='"+record.cfDbType+"' selected >"+record.cfDbType+"</option>";
    
     if("mySql" == record.cfDbType){
        option += "<option value='postgres'> Postgres </option>";
       }else{
           option += "<option value='mySql'> MySQL </option>";
       } 
    $("select[name=cfDbType]").html(option);
}

/********************************************************
 * 설명 : 기본 정보 목록 조회
 * 기능 : doSearch
 *********************************************************/
function doSearch() {
    defaultConfigInfo="";//기본 정보
    iaas = "";
    resetForm();
    
    w2ui['default_Grid'].clear();
    w2ui['default_Grid'].load('/deploy/hbCfDeployment/defaultConfig/list');
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
 * 기능 : registHbCfDeploymentDefaultConfigInfo
 *********************************************************/
function registHbCfDeploymentDefaultConfigInfo(){
    w2popup.lock("등록 중입니다.", true);
    defaultConfigInfo = {
            id                     : $("input[name='defaultInfoId']").val(),
            iaasType               : $("select[name='iaasType']").val(),
            defaultConfigName      : $("input[name='defaultConfigName']").val(),
            cfDeploymentVersion    : $("select[name='cfDeploymentVersion'] :selected").val(),
            domain                 : $("input[name='domain']").val(),
            domainOrganization     : $("input[name='domainOrganization']").val(),
            cfDbType               : $("select[name='cfDbType'] :selected").val(),
    }
    $.ajax({
        type : "PUT",
        url : "/deploy/hbCfDeployment/defaultConfig/save",
        contentType : "application/json",
        async : true,
        data : JSON.stringify(defaultConfigInfo),
        success : function(data, status) {
            doSearch();
        },
        error : function( e, status ) {
            w2popup.unlock();
            var errorResult = JSON.parse(e.responseText);
            w2alert(errorResult.message, "기본 정보 저장");
        }
    });
}

/********************************************************
 * 설명 : 기본 정보 삭제
 * 기능 : deleteHbCfDeploymentDefaultConfigInfo
 *********************************************************/
function deleteHbCfDeploymentDefaultConfigInfo(id, defaultConfigName){
    w2popup.lock("삭제 중입니다.", true);
    defaultInfo = {
        id : id,
        defaultConfigName : defaultConfigName
    }
    $.ajax({
        type : "DELETE",
        url : "/deploy/hbCfDeployment/defaultConfig/delete",
        contentType : "application/json",
        async : true,
        data : JSON.stringify(defaultInfo),
        success : function(status) {
            w2popup.unlock();
            w2popup.close();
            doSearch();
        }, error : function(request, status, error) {
            w2popup.unlock();
            w2popup.close();
            var errorResult = JSON.parse(request.responseText);
            w2alert(errorResult.message);
        }
    });
}

/********************************************************
 * 설명 : CF Deployment version 명 조회
 * 기능 : getCfDeployment
 *********************************************************/
 function getCfDeploymentVersionList(iaasType) {
    var option ="";
    $.ajax({
        type : "GET",
        url :"/common/deploy/list/releaseInfo/cfDeployment/"+iaasType, 
        contentType : "application/json",
        success : function(data, status) {
            releases = new Array();
            if( data != null){
                option = "<option value=''>CF Deployment를 선택하세요.</option>";
                data.map(function(obj) {
                      if( defaultConfigInfo.releaseName == obj.releaseType && defaultConfigInfo.releaseVersion == obj.templateVersion){
                       option += "<option value='"+obj.releaseType+"/"+obj.templateVersion+"' selected>"+obj.releaseType+"/"+obj.templateVersion+"</option>";
                    }else{
                    option += "<option value='"+obj.releaseType+"/"+obj.templateVersion+"'>"+obj.releaseType+"/"+obj.templateVersion+"</option>";    
                    } 
                });
            }
            $("select#cfDeploymentVersion").html(option);
            //setInputDisplay(defaultInfo.releaseName+"/"+defaultInfo.releaseVersion);
            //setDisabledMonitoring(defaultInfo.releaseName+"/"+defaultInfo.releaseVersion);
        },
        error : function(e, status) {
            w2popup.unlock();
            w2alert("Cf Deployment List 를 가져오는데 실패하였습니다.", "CF Deployment");
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
    $().w2destroy('default_Grid');
}

/********************************************************
 * 설명 : 기본 정보 리셋
 * 기능 : resetForm
 *********************************************************/
function resetForm(status){
	
    $(".panel-body").find("p").remove();
    $(".panel-body").children().children().children().css("borderColor", "#bbb");
    $("input[name=defaultConfigName]").val("");
    $("select[name=iaasType]").val("");
    $("select[name=cfDeploymentVersion]").val("");
    $("input[name=domain]").val("");
    $("input[name=domainOrganization]").val("");
    $("select[name=cfDbType]").val("");
    $("input[name=defaultInfoId]").val("");
        var option ="";
        $("select[name=cfDeploymentVersion]").html("<option value=''> CF Deployment 버전을 선택하세요.</option>");
        option += "<option value=''> CF Database 유형을 선택하세요.</option>";
        option += "<option value='mySql'> MySQL </option>";
        option += "<option value='postgres'> Postgres </option>";
        $("select[name=cfDbType]").html(option);
    if(status=="reset"){
        w2ui['default_Grid'].clear();
        doSearch();
    }
    document.getElementById("settingForm").reset();
}

</script>
<div id="main">
    <div class="page_site">이종 CF Deployment > <strong>Default 정보 관리</strong></div>
    <!-- 사용자 목록-->
    <div class="pdt20">
        <div class="title fl"> 기본 정보 목록</div>
    </div>
    <div id="default_Grid" style="width:100%;  height:700px;"></div>

</div>


<div id="regPopupDiv" hidden="true" >
    <form id="settingForm" action="POST" >
    <input type="hidden" name="defaultInfoId" />
        <div class="w2ui-page page-0" style="">
           <div class="panel panel-default">
               <div class="panel-heading"><b>기본 정보</b></div>
               <div class="panel-body" style="height:615px; overflow-y:auto;">
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">배포 명 & 기본 정보 별칭</label>
                       <div>
                           <input class="form-control" name = "defaultConfigName" type="text"  maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="배포명 & 기본 정보 별칭을 입력 하세요."/>
                       </div>
                   </div>
                   
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">클라우드 인프라 환경</label>
                       <div>
                           <select class="form-control" onchange="getCfDeploymentVersionList(this.value);" name="iaasType" style="width: 320px; margin-left: 20px;">
                               <option value="nothing">인프라 환경을 선택하세요.</option>
                               <option value="aws">AWS</option>
                               <option value="openstack">Openstack</option>
                           </select>
                       </div>
                   </div>
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">CF Deployment 버전 명</label>
                       <div>
                           <select class="form-control" id="cfDeploymentVersion" name="cfDeploymentVersion"  style="width: 320px; margin-left: 20px;">
                               <option value=""> 클라우드 인 환경을  먼저 선택하세요.</option>
                           </select>
                       </div>
                   </div>
                    <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;"> CF 도메인 </label>
                       <div>
                           <input class="form-control"  name="domain" type="text" maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="CF 도메인을 입력하세요."/>
                       </div>
                   </div>
                    <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;"> CF 기본 조직명 </label>
                       <div>
                           <input class="form-control" name="domainOrganization" type="text" maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="CF 기본 조직명을 입력하세요."/>
                       </div>
                   </div>
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;"> CF Database 유형 </label>
                       <div>
                           <select class="form-control" name="cfDbType" style="width: 320px; margin-left: 20px;">
                                <option value="">CF Database 유형을 선택하세요.</option>
                                <option value="mySql"> MySQL </option>
                                <option value="postgres"> Postgres </option>
                           </select>
                       </div>
                   </div>
                   
               </div>
           </div>
        </div>
    </form>
    <div id="regPopupBtnDiv" style="text-align: center; margin-top: 5px;">
        <span id="installBtn" onclick="$('#settingForm').submit();" class="btn btn-primary">등록</span>
        <span id="resetBtn" onclick="resetForm('reset');" class="btn btn-info">취소</span>
        <span id="deleteBtn" class="btn btn-danger">삭제</span>
    </div>
</div>
<script>
$(function() {
    
    
    $("#settingForm").validate({
        ignore : [],
        //onfocusout: function(element) {$(element).valid()},
        rules: {
            defaultConfigName: { 
                required: function(){
                    return checkEmpty( $("input[name='defaultConfigName']").val() );
                }
            }, iaasType: { 
                required: function(){
                    return checkEmpty( $("select[name='iaasType']").val() );
                }
            }, cfDeploymentVersion: { 
                required: function(){
                    return checkEmpty( $("select[name='cfDeploymentVersion']").val() );
                }
            }, domain: { 
                required: function(){
                    return checkEmpty( $("input[name='domain']").val() );
                }
            }, domainOrganization: { 
                required: function(){
                    return checkEmpty( $("input[name='domainOrganization']").val() );
                }
            }, cfDbType: { 
                required: function(){
                    return checkEmpty( $("select[name='cfDbType']").val() );
                }
            }
        }, messages: {
            defaultConfigName: { 
                required:  "배포 명 & 기본 정보 별칭"+text_required_msg
            }, iaasType: { 
                required:  "클라우드 인프라 환경 타입"+select_required_msg,
            }, cfDeploymentVersion: { 
                required:  "CF Deployment 버전"+select_required_msg,
            }, domain: { 
                required:  "도메인 "+text_required_msg,
            }, domainOrganization: { 
                required:  "기본 조직명 "+text_required_msg,
            }, cfDbType: { 
                required:  "Cf DB 유형"+select_required_msg,
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
        	registHbCfDeploymentDefaultConfigInfo();
        }
    });
});


</script>