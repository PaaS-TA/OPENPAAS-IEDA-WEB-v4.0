<%
/* =================================================================
 * 상세설명 : Resource 관리 화면
 * =================================================================
 */ 
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri = "http://www.springframework.org/tags" %>

<script type="text/javascript">
var save_lock_msg = '<spring:message code="common.save.data.lock"/>';//등록 중 입니다.
var text_required_msg = '<spring:message code="common.text.vaildate.required.message"/>';//을(를) 입력하세요.
var select_required_msg='<spring:message code="common.select.vaildate.required.message"/>';//을(를) 선택하세요.
var search_data_fail_msg ='클라우드 인프라 환경을 선택하세요.';
var country_parent_code = '<spring:message code="common.code.country.code.parent"/>';//ieda_common_code country 조회
var resourceConfigInfo = [];//인증서
var stemcellInfo = "";
var directorInfo = "";
var list_lock_msg = "목록을 조회 중입니다.";
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
         *  설명 : 리소스 목록 Grid
        *********************************************************/
        grid: {
            name: 'cf_resource_config_grid',
            header: '<b>리소스 정보</b>',
            method: 'GET',
                multiSelect: false,
            show: {
                    selectColumn: true,
                    footer: true },
            style: 'text-align: center',
            columns:[
                   { field: 'recid', hidden: true },
                   { field: 'id', hidden: true },
                   { field: 'resourceConfigName', caption: '리소스 정보 명', size:'130px', style:'text-align:center;' },
                   { field: 'iaasType', caption: '인프라 환경 타입', size:'120px', style:'text-align:center;' ,render: function(record){ 
                       if(record.iaasType.toLowerCase() == "aws"){
                           return "<img src='images/iaasMgnt/aws-icon.png' width='80' height='30' />";
                       }else if (record.iaasType.toLowerCase() == "openstack"){
                           return "<img src='images/iaasMgnt/openstack-icon.png' width='90' height='35' />";
                       }
                   }},
                   { field: 'smallFlavor', caption: 'Small Instance Type', size:'150px', style:'text-align:center;'},
                   { field: 'mediumFlavor', caption: 'Medium Instance Type', size:'150px', style:'text-align:center;'},
                   { field: 'largeFlavor', caption: 'Large Instance Type', size:'150px', style:'text-align:center;'},
                   { field: 'stemcellName', caption: '스템셀 명', size:'300px', style:'text-align:center;'},
                   { field: 'stemcellVersion', caption: '스템셀 버전', size:'120px', style:'text-align:center;'},
                   { field: 'boshPassword', caption: '스템셀 Password', size:'150px', style:'text-align:center;'}
                  ],
            onSelect : function(event) {
                event.onComplete = function() {
                    $('#deleteBtn').attr('disabled', false);
                    settingResourceConfigInfo();
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
            }
    }
}
$(function(){
    $('#cf_resource_config_grid').w2layout(resourceLayout.layout2);
    w2ui.layout2.content('left', $().w2grid(resourceLayout.grid));
    w2ui['layout2'].content('main', $('#regPopupDiv').html());
    
    doSearch();
    
    $("#deleteBtn").click(function(){
        if($("#deleteBtn").attr('disabled') == "disabled") return;
        var selected = w2ui['cf_resource_config_grid'].getSelection();
        if( selected.length == 0 ){
            w2alert("선택된 정보가 없습니다.", "리소스 정보 삭제");
            return;
        }
        else {
            var record = w2ui['cf_resource_config_grid'].get(selected);
            w2confirm({
                title       : "리소스",
                msg         : "리소스 ("+record.resourceConfigName + ")을 삭제하시겠습니까?",
                yes_text    : "확인",
                no_text     : "취소",
                yes_callBack: function(event){
                    deleteHbCfResourceConfigInfo(record.id, record.resourceConfigName);
                },
                no_callBack    : function(){
                    w2ui['cf_resource_config_grid'].clear();
                    doSearch();
                }
            });
        }
    });
});

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
                w2alert(errorResult.message, "CF 리소스 정보 관리");
                w2utils.unlock($("#layout_layout_panel_main"));
                resetForm();
            }
        });
    }
}

/******************************************************************
 * 기능 : getStemcellList
 * 설명 : 업로드 된 스템셀 목록 조회
 ***************************************************************** */
function getStemcellList(directorInfo){
    var option = "<option value=''>스템셀을 선택하세요.</option>";
    if(directorInfo == ""){
        $("select[name=stemcells]").attr("disabled", "disabled");
        $("select[name='stemcells']").html(option);
    } else {
        w2utils.lock($("#layout_layout_panel_main"), list_lock_msg, true);
        if($("select[name=stemcells]").attr("disabled") == "disabled"){
            $("select[name=stemcells]").removeAttr("disabled");
        }
        var url = "/common/hbDeploy/stemcell/list/"+directorInfo+"";
        $.ajax({
            type : "GET",
            url : url,
            contentType : "application/json",
            async : true,
            success : function(data, status) {
                if(data.records.length != 0){
                    data.records.map(function (obj){
                        if(obj.stemcellFileName == stemcellInfo) {
                            option += "<option selected "+obj.stemcellFileName+"/"+obj.stemcellVersion+">"+obj.stemcellFileName+"/"+obj.stemcellVersion+"</option>";
                        }else {
                            option += "<option "+obj.stemcellFileName+"/"+obj.stemcellVersion+">"+obj.stemcellFileName+"/"+obj.stemcellVersion+"</option>";
                        }
                        
                    });
                }else if (data.records.length == 0){
                    option = "<option value=''>스템셀이 존재 하지 않습니다.</option>";
                }
                $("#stemcells").html(option);
                w2utils.unlock($("#layout_layout_panel_main"));
            },
            error : function(request, status, error) {
                var errorResult = JSON.parse(request.responseText);
                w2alert(errorResult.message, "리소스 정보 저장");
                w2utils.unlock($("#layout_layout_panel_main"));
                resetForm();
            }
        });
    }
}



/********************************************************
 * 설명 : 리소스 정보 수정 정보 설정
 * 기능 : settingResourceConfigInfo
 *********************************************************/
function settingResourceConfigInfo(){
    var selected = w2ui['cf_resource_config_grid'].getSelection();
    var record = w2ui['cf_resource_config_grid'].get(selected);
    
    if(record == null) {
        w2alert("리소스 정보 설정 중 에러가 발생 했습니다.");
        return;
    }
    iaas = record.iaasType;
    $("input[name=resourceInfoId]").val(record.id);
    $("input[name=resourceConfigName]").val(record.resourceConfigName);
    $("select[name=iaasType]").val(record.iaasType);
    $("input[name=boshPassword]").val(record.boshPassword);
    $("input[name=smallFlavor]").val(record.smallFlavor);
    $("input[name=mediumFlavor]").val(record.mediumFlavor);
    $("input[name=largeFlavor]").val(record.largeFlavor);
    directorInfo = record.directorId;
    stemcellInfo = record.stemcellName;
    getHbDirectorList(iaas);
    getStemcellList(directorInfo);
    
    
}

/********************************************************
 * 설명 : 리솟 목록 조회
 * 기능 : doSearch
 *********************************************************/
function doSearch() {
    resourceConfigInfo = [];
    stemcellInfo = "";
    directorInfo = "";
    iaas = "";
    resetForm();
    w2ui['cf_resource_config_grid'].clear();
    w2ui['cf_resource_config_grid'].load('/deploy/hbCf/resource/list');
}
/********************************************************
 * 설명 : 초기 버튼 스타일
 * 기능 : doButtonStyle
 *********************************************************/
function doButtonStyle() {
    $('#deleteBtn').attr('disabled', true);
}
/********************************************************
 * 설명 : Resource 정보  등록
 * 기능 : registHbCfResourceConfigInfo
 *********************************************************/
function registHbCfResourceConfigInfo(){
    w2popup.lock("등록 중입니다.", true);
    var stemcellInfo = $("select[name='stemcells']").val();
    var stemcellName = stemcellInfo.split("/")[0];
    var stemcellVersion = stemcellInfo.split("/")[1];
    
    resourceConfigInfo = {
            id                     : $("input[name='resourceInfoId']").val(),
            iaasType               : $("select[name='iaasType']").val(),
            resourceConfigName     : $("input[name='resourceConfigName']").val(),
            boshPassword           : $("input[name='boshPassword']").val(),
            smallFlavor            : $("input[name='smallFlavor']").val(),
            mediumFlavor           : $("input[name='mediumFlavor']").val(),
            largeFlavor            : $("input[name='largeFlavor']").val(),
            directorInfo           : $("select[name='directorInfo']").val(),
            stemcellName           : stemcellName,
            stemcellVersion        : stemcellVersion
    }
    $.ajax({
        type : "PUT",
        url : "/deploy/hbCf/resource/save",
        contentType : "application/json",
        async : true,
        data : JSON.stringify(resourceConfigInfo),
        success : function(data, status) {
            w2utils.unlock($("#layout_layout_panel_main"));
            doSearch();
        },
        error : function( e, status ) {
            w2utils.unlock($("#layout_layout_panel_main"));
            var errorResult = JSON.parse(e.responseText);
            w2alert(errorResult.message, "리소스 정보 저장");
        }
    });
}
/********************************************************
 * 설명 : 리소스 정보 삭제
 * 기능 : deleteHbCfResourceConfigInfo
 *********************************************************/
function deleteHbCfResourceConfigInfo(id, resourceConfigName){
    w2popup.lock("삭제 중입니다.", true);
    resourceConfigInfo = {
        id : id,
        resourceConfigName : resourceConfigName
    }
    $.ajax({
        type : "DELETE",
        url : "/deploy/hbCf/resource/delete",
        contentType : "application/json",
        async : true,
        data : JSON.stringify(resourceConfigInfo),
        success : function(status) {
            doSearch();
            
        }, error : function(request, status, error) {
            doSearch();
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
    $().w2destroy('cf_resource_config_grid');
}
/********************************************************
 * 설명 : 인증서 리셋
 * 기능 : resetForm
 *********************************************************/
function resetForm(status){
    $(".panel-body").find("p").remove();
    $(".panel-body").children().children().children().css("borderColor", "#bbb");
    $("select[name=iaasType]").val("");
    $("input[name=resourceConfigName]").val("");
    
    $("select[name=directorInfo]").attr("disabled", "disabled");
    $("select[name=stemcells]").attr("disabled", "disabled");
    $("select[name=directorInfo]").html("<option value=''>디렉터 정보를 선택하세요.</option>");
    $("select[name=directorInfo]").val("")
    $("select[name=stemcells]").html("<option value=''>스템셀을 선택하세요.</option>");
    $("select[name=stemcells]").val("")
    
    stemcellInfo = "";
    directorInfo = "";
    $("input[name=resourceInfoId]").val("");
    if(status=="reset"){
        w2ui['cf_resource_config_grid'].clear();
        doSearch();
    }
    document.getElementById("settingForm").reset();
}
</script>
<div id="main">
    <div class="page_site"> 이종 CF 설치 > <strong>리소스 정보 관리</strong></div>
    <!-- 사용자 목록-->
    <div class="pdt20">
        <div class="title fl"> 리소스 정보 목록</div>
    </div>
    <div id="cf_resource_config_grid" style="width:100%;  height:700px;"></div>

</div>


<div id="regPopupDiv" hidden="true" >
    <form id="settingForm" action="POST" >
    <input type="hidden" name="resourceInfoId" />
        <div class="w2ui-page page-0" style="">
           <div class="panel panel-default">
               <div class="panel-heading"><b>리소스 정보 </b></div>
               <div class="panel-body" style="height:615px; overflow-y:auto;">
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;"> 리소스 정보 별칭</label>
                       <div>
                           <input class="form-control" name = "resourceConfigName" type="text"  maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="리소스 정보 별칭을 입력 하세요."/>
                       </div>
                   </div>
                   
                   <div class="w2ui-field">
                       <label style="width:40%; text-align: left;padding-left: 20px;"> 클라우드 인프라 환경</label>
                       <div>
                           <select class="form-control" onchange="getHbDirectorList(this.value);"  name="iaasType" style="width: 320px; margin-left: 20px;">
                               <option value=""> 인프라 환경을 선택하세요. </option>
                               <option value="aws"> AWS </option>
                               <option value="openstack"> Openstack </option>
                           </select>
                       </div>
                   </div>
                   <div class="w2ui-field">
                       <label style="width:40%; text-align: left;padding-left: 20px;">디렉터 명</label>
                       <div>
                            <select disabled class="form-control" onchange="getStemcellList(this.value)" id="directorInfo" name="directorInfo" style="display:inline-block; width: 320px; margin-left: 20px;">
                                <option value="">디렉터 명을 선택하세요.</option>
                            </select>
                      </div>
                  </div>
                  
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">Stemcell 명</label>
                       <div>
                           <select class="form-control" disabled="disabled" id="stemcells" name="stemcells" style="display:inline-block; width: 320px; margin-left: 20px;">
                               <option value="">스템셀을 선택하세요.</option>
                           </select>
                       </div>
                   </div>
                   
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">Stemcell Password</label>
                       <div>
                           <input class="form-control"  name="boshPassword" type="text" maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="Stemcell Password을 입력하세요."/>
                       </div>
                   </div>
                   
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">인스턴스 유형 Small </label>
                       <div>
                           <input class="form-control"  name="smallFlavor" type="text" maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="Small 인스턴스 유형 입력하세요."/>
                       </div>
                   </div>
                    <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">인스턴스 유형 Medium </label>
                       <div>
                           <input class="form-control"  name="mediumFlavor" type="text" maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="Medium 인스턴스 유형 입력하세요."/>
                       </div>
                   </div>
                    <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">인스턴스 유형 Large</label>
                       <div>
                           <input class="form-control"  name="largeFlavor" type="text" maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="Large 인스턴스 유형 입력하세요."/>
                       </div>
                   </div>
                   
               </div>
           </div>
        </div>
    </form>
    <div id="regPopupBtnDiv" style="text-align: center; margin-top: 5px;">
        <sec:authorize access="hasAuthority('DEPLOY_HBCF_RESOURCE_ADD')">
            <span id="installBtn" onclick="$('#settingForm').submit();" class="btn btn-primary">등록</span>
        </sec:authorize>
        <span id="resetBtn" onclick="resetForm('reset');" class="btn btn-info">취소</span>
        <sec:authorize access="hasAuthority('DEPLOY_HBCF_RESOURCE_DELETE')">
            <span id="deleteBtn" class="btn btn-danger">삭제</span>
        </sec:authorize>
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
            resourceConfigName: { 
                required: function(){
                    return checkEmpty( $("input[name='resourceConfigName']").val() );
                }
            }, iaasType: { 
                required: function(){
                    return checkEmpty( $("select[name='iaasType']").val() );
                }
            }, directorInfo: { 
                required: function(){
                    return checkEmpty( $("select[name='directorInfo']").val() );
                }
            }, stemcells: { 
                required: function(){
                    return checkEmpty( $("select[name='stemcells']").val() );
                }
            }, boshPassword: { 
                required: function(){
                    return checkEmpty( $("input[name='boshPassword']").val() );
                }
            }, smallFlavor: { 
                required: function(){
                    return checkEmpty( $("input[name='smallFlavor']").val() );
                }
            }, mediumFlavor: { 
                required: function(){
                    return checkEmpty( $("input[name='mediumFlavor']").val() );
                }
            }, largeFlavor: { 
                required: function(){
                    return checkEmpty( $("input[name='largeFlavor']").val() );
                }
            }
        }, messages: {
            resourceConfigName: { 
                required:  "리소스 정보 별칭"+text_required_msg
            }, iaasType: { 
                required:  "클라우드 인프라 환경 타입"+select_required_msg,
            }, directorInfo: { 
                required:  "디렉터 정보"+select_required_msg,
            }, stemcells: { 
                required:  "스템셀 명"+select_required_msg,
            }, boshPassword: { 
                required:  "스템셀 패스워드"+text_required_msg,
            }, smallFlavor: { 
                required:  "Small Instance Type"+text_required_msg,
            }, mediumFlavor: { 
                required:  "Medium Instance Type"+text_required_msg,
            }, largeFlavor: { 
                required:  "Large Instacne Type"+text_required_msg,
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
            w2utils.lock($("#layout_layout_panel_main"), save_lock_msg, true);
            registHbCfResourceConfigInfo();
        }
    });
});
</script>