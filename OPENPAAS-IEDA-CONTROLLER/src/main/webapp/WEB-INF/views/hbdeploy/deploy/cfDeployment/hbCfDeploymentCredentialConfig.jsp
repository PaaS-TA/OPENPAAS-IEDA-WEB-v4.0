<%
/* =================================================================
 * 작성일 : 2018.07
 * 작성자 : 이정윤
 * 상세설명 : 인증서 정보 관리 화면
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
var credentialConfigInfo = "";//인증서
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
         *  설명 : 인증서 목록 Grid
        *********************************************************/
        grid: {
            name: 'credential_Grid',
            header: '<b>인증서 정보</b>',
            method: 'GET',
                multiSelect: false,
            show: {
                    selectColumn: true,
                    footer: false
                   },
            style: 'text-align: center',
            columns:[
                   { field: 'recid', hidden: true },
                   { field: 'id', hidden: true },
                   { field: 'credentialConfigName', caption: '인증서 명', size:'15%', style:'text-align:center;' },
                   { field: 'iaasType', caption: '인프라 환경 타입', size:'15%', style:'text-align:center;' ,render: function(record){ 
                       if(record.iaasType.toLowerCase() == "aws"){
                           return "<img src='images/iaasMgnt/aws-icon.png' width='80' height='30' />";
                       }else if (record.iaasType.toLowerCase() == "openstack"){
                           return "<img src='images/iaasMgnt/openstack-icon.png' width='90' height='35' />";
                       }
                   }},
                   { field: 'countryCode', caption: '국가 코드', size:'15%', style:'text-align:center;'},
                   { field: 'domain', caption: '도메인', size:'15%', style:'text-align:center;'},
                   { field: 'company', caption: '회사명', size:'10%', style:'text-align:center;'},
                   { field: 'jobTitle', caption: '부서명', size:'10%', style:'text-align:center;'},
                   { field: 'emailAddress', caption: '이메일', size:'10%', style:'text-align:center;'}
                  ],
            onSelect : function(event) {
                event.onComplete = function() {
                    settingDefaultInfo();
                    $('#deleteBtn').attr('disabled', false);
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
    $('#credential_Grid').w2layout(resourceLayout.layout2);
    w2ui.layout2.content('left', $().w2grid(resourceLayout.grid));
    w2ui['layout2'].content('main', $('#regPopupDiv').html());
    doSearch();
    
    $("#deleteBtn").click(function(){
        if($("#deleteBtn").attr('disabled') == "disabled") return;
        var selected = w2ui['credential_Grid'].getSelection();
        if( selected.length == 0 ){
            w2alert("선택된 정보가 없습니다.", "기본  삭제");
            return;
        }
        else {
            var record = w2ui['credential_Grid'].get(selected);
            w2confirm({
                title       : "인증서",
                msg         : "인증서 ("+record.credentialConfigName + ")을 삭제하시겠습니까?",
                yes_text    : "확인",
                no_text     : "취소",
                yes_callBack: function(event){
                    deleteHbCfDeploymentCredentialConfigInfo(record.recid, record.credentialConfigName);
                },
                no_callBack    : function(){
                    w2ui['credential_Grid'].clear();
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
    var selected = w2ui['credential_Grid'].getSelection();
    var record = w2ui['credential_Grid'].get(selected);
    
    console.log(record +"testeeeeee" );
    
    if(record == null) {
        w2alert("인증서 설정 중 에러가 발생 했습니다.");
        return;
    }
    iaas = record.iaasType;
    $("input[name=credentialInfoId]").val(record.recid);
    $("input[name=credentialConfigName]").val(record.credentialConfigName);
    $("select[name=iaasType] :selected").val(record.iaasType);
    $("select[name=countryCode] :selected").html("<option value='"+record.countryCode+"' selected >"+record.countryCode+"</option>");
    $("input[name=city]").val(record.city);
    $("input[name=domain]").val(record.domain);
    $("input[name=company]").val(record.company);
    $("input[name=jobTitle]").val(record.jobTitle);
    $("input[name=emailAddress]").val(record.emailAddress);
    
}

/********************************************************
 * 설명 : 인증서 목록 조회
 * 기능 : doSearch
 *********************************************************/
function doSearch() {
    credentialConfigInfo="";//인증서
    iaas = "";
    resetForm();
    
    w2ui['credential_Grid'].clear();
    w2ui['credential_Grid'].load('/deploy/hbCfDeployment/credentialConfig/list');
    //doButtonStyle(); 
}

/********************************************************
 * 설명 : 초기 버튼 스타일
 * 기능 : doButtonStyle
 *********************************************************/
function doButtonStyle() {
    $('#deleteBtn').attr('disabled', true);
}

/********************************************************
 * 설명 : 인증서 등록
 * 기능 : registHbCfDeploymentCredentialConfigInfo
 *********************************************************/
function registHbCfDeploymentCredentialConfigInfo(){
    w2popup.lock("등록 중입니다.", true);
    
    credentialConfigInfo = {
            id                     : $("input[name='credentialInfoId']").val(),
            iaasType               : $("select[name='iaasType'] :selected").val(),
            credentialConfigName   : $("input[name='credentialConfigName']").val(),
            countryCode            : $("select[name='countryCode'] :selected").val(),
            domain                 : $("input[name='domain']").val(),
            city                   : $("input[name='city']").val(),
            company                : $("input[name='company']").val(),
            jobTitle               : $("input[name='jobTitle']").val(),
            emailAddress           : $("input[name='emailAddress']").val()
    }
    
    $.ajax({
        type : "PUT",
        url : "/deploy/hbCfDeployment/credentialConfig/save",
        contentType : "application/json",
        async : true,
        data : JSON.stringify(credentialConfigInfo),
        success : function(data, status) {
            doSearch();
        },
        error : function( e, status ) {
            w2popup.unlock();
            var errorResult = JSON.parse(e.responseText);
            w2alert(errorResult.message, "인증서 저장");
        }
    });
}

/********************************************************
 * 설명 : 인증서 삭제
 * 기능 : deleteHbCfDeploymentCredentialConfigInfo
 *********************************************************/
function deleteHbCfDeploymentCredentialConfigInfo(id, credentialConfigName){
    w2popup.lock("삭제 중입니다.", true);
    credentialInfo = {
        id : id,
        credentialConfigName : credentialConfigName
    }
    $.ajax({
        type : "DELETE",
        url : "/deploy/hbCfDeployment/credentialConfig/delete",
        contentType : "application/json",
        async : true,
        data : JSON.stringify(credentialInfo),
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
    $().w2destroy('credential_Grid');
}

/********************************************************
 * 설명 : 인증서 리셋
 * 기능 : resetForm
 *********************************************************/
function resetForm(status){
    $(".panel-body").find("p").remove();
    $(".panel-body").children().children().children().css("borderColor", "#bbb");
    $("select[name=iaasType]").val("");
    $("input[name=credentialConfigName]").val("");
    $("select[name=countryCode]").val("");
    $("input[name=domain]").val("");
    $("input[name=city]").val("");
    $("input[name=company]").val("");
    $("input[name=jobTitle]").val("");
    $("input[name=emailAddress]").val("");

    $("input[name=credentialInfoId]").val("");
    if(status=="reset"){
        w2ui['credential_Grid'].clear();
        $("select[name=iaasType]").html("<option value=''>인프라 유형을 선택하세요.</option>");
        $("select[name=countryCode]").html("<option value=''>국가 코드를 선택하세요.</option>");
        doSearch();
    }
    document.getElementById("settingForm").reset();
}

</script>
<div id="main">
    <div class="page_site"> 이종 CF Deployment <strong>Default 정보 관리</strong></div>
    <!-- 사용자 목록-->
    <div class="pdt20">
        <div class="title fl"> 인증서 목록</div>
    </div>
    <div id="credential_Grid" style="width:100%;  height:700px;"></div>

</div>


<div id="regPopupDiv" hidden="true" >
    <form id="settingForm" action="POST" >
    <input type="hidden" name="credentialInfoId" />
        <div class="w2ui-page page-0" style="">
           <div class="panel panel-default">
               <div class="panel-heading"><b>인증서</b></div>
               <div class="panel-body" style="height:615px; overflow-y:auto;">
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;"> 배포 명</label>
                       <div>
                           <input class="form-control" name = "credentialConfigName" type="text"  maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="인증서 명을 입력 하세요."/>
                       </div>
                   </div>
                   
                   <div class="w2ui-field">
                       <label style="width:40%; text-align: left;padding-left: 20px;"> 클라우드 인프라 환경</label>
                       <div>
                           <select class="form-control"  name="iaasType" style="width: 320px; margin-left: 20px;">
                               <option value=""> 인프라 환경을 선택하세요. </option>
                               <option value="aws"> AWS </option>
                               <option value="openstack"> Openstack </option>
                           </select>
                       </div>
                   </div>
                   
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;"> 국가 코드</label>
                       <div>
                           <select class="form-control" id="countryCode" name="countryCode"  style="width: 320px; margin-left: 20px;">
                               <option value=""> 국가 코드를 선택하세요. </option>
                               <option value="KR"> KR </option>
                               
                           </select>
                       </div>
                   </div>

                    <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;"> 도시  </label>
                       <div>
                           <input class="form-control"  name="city" type="text" maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="도시를 입력하세요."/>
                       </div>
                   </div>

                    <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;"> 도메인 </label>
                       <div>
                           <input class="form-control"  name="domain" type="text" maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="도메인을 입력하세요."/>
                       </div>
                   </div>
                   
                    <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;"> 회사 명 </label>
                       <div>
                           <input class="form-control" name="company" type="text" maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="회사 명을 입력하세요."/>
                       </div>
                   </div>
                   
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;"> 부서 명 </label>
                       <div>
                           <input class="form-control" name="jobTitle" type="text" maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="부서 명을 입력하세요."/>
                       </div>
                   </div>
                   
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;"> 이메일 </label>
                       <div>
                           <input class="form-control" name="emailAddress" type="text" maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="이메일을 입력하세요."/>
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
    $.validator.addMethod("validemail", 
           function(value, element) {
                return /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value);
                              }, 
               "Sorry, I've enabled very strict email validation"
           );
    
    $("#settingForm").validate({
        ignore : [],
        //onfocusout: function(element) {$(element).valid()},
        rules: {
            credentialConfigName: { 
                required: function(){
                    return checkEmpty( $("input[name='credentialConfigName']").val() );
                }
            }, iaasType: { 
                required: function(){
                    return checkEmpty( $("select[name='iaasType']").val() );
                }
            }, countryCode: { 
                required: function(){
                    return checkEmpty( $("select[name='countryCode']").val() );
                }
            }, domain: { 
                required: function(){
                    return checkEmpty( $("input[name='domain']").val() );
                }
            }, city: { 
                required: function(){
                    return checkEmpty( $("input[name='city']").val() );
                }
            }, company: { 
                required: function(){
                    return checkEmpty( $("input[name='company']").val() );
                }
            }, jobTitle: { 
                required: function(){
                    return checkEmpty( $("input[name='jobTitle']").val() );
                }
            }, emailAddress: { 
                required: function(){
                    return checkEmpty( $("input[name='emailAddress']").val() );
                }, validemail : function(){
                    return $(".w2ui-msg-body input[name='emailAddress']").val();
                }
            }
        }, messages: {
            credentialConfigName: { 
                required:  "기본  별칭"+text_required_msg
            }, iaasType: { 
                required:  "클라우드 인프라 환경 타입"+select_required_msg,
            }, countryCode: { 
                required:  "국가 코드 "+select_required_msg,
            }, domain: { 
                required:  "도메인 "+text_required_msg,
            }, city: { 
                required:  "도시 "+text_required_msg,
            }, company: { 
                required:  "회사 명 "+text_required_msg,                
            }, jobTitle: { 
                required:  "부서 명 "+text_required_msg,
            }, emailAddress: { 
                required:  "이메일 주소"+text_required_msg,
                validemail : "이메일 형식을 확인하세요.",
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
            registHbCfDeploymentCredentialConfigInfo();
        }
    });
});


</script>