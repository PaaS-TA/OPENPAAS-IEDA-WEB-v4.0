<%
/* =================================================================
 * 작성일 : 2018.06
 * 작성자 : 이동현
 * 상세설명 : CPI 인증서 관리 화면
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
var text_cidr_msg='<spring:message code="common.text.validate.cidr.message"/>';//CIDR 대역을 확인 하세요.
var text_ip_msg = '<spring:message code="common.text.validate.ip.message"/>';//IP을(를) 확인 하세요.
var networkConfigInfo = [];//네트워크 정보
var iaas = "";
var networkLayout = {
        layout2: {
            name: 'layout2',
            padding: 4,
            panels: [
                { type: 'left', size: '65%', resizable: true, minSize: 300 },
                { type: 'main', minSize: 300 }
            ]
        },
        /********************************************************
         *  설명 : 네트워크 정보 목록 Grid
        *********************************************************/
        grid: {
            name: 'network_GroupGrid',
            header: '<b>Network 정보</b>',
            method: 'GET',
                multiSelect: false,
            show: {
                    selectColumn: true,
                    footer: true},
            style: 'text-align: center',
            columns:[
                   { field: 'recid', hidden: true },
                   { field: 'networkConfigName', caption: '네트워크 정보 별칭', size:'50%', style:'text-align:center;' },
                   { field: 'iaasType', caption: '인프라 환경 타입', size:'50%', style:'text-align:center;' ,render: function(record){ 
                       if(record.iaasType.toLowerCase() == "aws"){
                           return "<img src='images/iaasMgnt/aws-icon.png' width='80' height='30' />";
                       }else if (record.iaasType.toLowerCase() == "openstack"){
                           return "<img src='images/iaasMgnt/openstack-icon.png' width='90' height='35' />";
                       }
                   }},
                   { field: 'publicStaticIp', caption: '디렉터 Public IP', size:'50%', style:'text-align:center;'},
                   { field: 'privateStaticIp', caption: '디렉터 Private IP', size:'60%', style:'text-align:center;'},
                   { field: 'subnetId', caption: '서브넷 아이디', size:'50%', style:'text-align:center;'},
                   { field: 'subnetRange', caption: '서브넷 범위', size:'50%', style:'text-align:center;'},
                   { field: 'subnetDns', caption: 'DNS 주소', size:'50%', style:'text-align:center;'}
                  ],
            onSelect : function(event) {
                event.onComplete = function() {
                    $('#deleteBtn').attr('disabled', false);
                    settingNetworkInfo();
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
    $('#network_GroupGrid').w2layout(networkLayout.layout2);
    w2ui.layout2.content('left', $().w2grid(networkLayout.grid));
    w2ui['layout2'].content('main', $('#regPopupDiv').html());
    doSearch();
    
    $("#deleteBtn").click(function(){
        if($("#deleteBtn").attr('disabled') == "disabled") return;
        var selected = w2ui['network_GroupGrid'].getSelection();
        if( selected.length == 0 ){
            w2alert("선택된 정보가 없습니다.", "네트워크 삭제");
            return;
        }
        else {
            var record = w2ui['network_GroupGrid'].get(selected);
            w2confirm({
                title        : "네트워크 정보",
                msg            : "네트워크 정보 ("+record.networkConfigName + ")을 삭제하시겠습니까?",
                yes_text    : "확인",
                no_text        : "취소",
                yes_callBack: function(event){
                    deleteCfNetworkConfigInfo(record.recid, record.networkConfigName);
                },
                no_callBack    : function(){
                    w2ui['network_GroupGrid'].clear();
                    doSearch();
                }
            });
        }
    });
});


/********************************************************
 * 설명 : 네트워크 입력 추가
 * 기능 : addInternalNetworkInputs
 *********************************************************/
 function addInternalNetworkInputs(preDiv, form){
    w2popup.lock("Internal 네트워크 추가 중", true);
    var index = Number(preDiv.split("_")[1])+1;
    var div= preDiv.split("_")[0] + "_"+ index;
    var body_div= "<div class='panel-body'>";
    var field_div_label="<div class='w2ui-field'>"+"<label style='width:40%;text-align: left;padding-left: 20px;'>";
    var text_style="type='text' style='width: 320px; margin-left: 20px;'";
    
    var html= "<div class='panel' style='margin-top:2%;'>";
        html+= "<div  class='panel-heading' style='position:relative;'>";
        html+=    "<b>Internal 네트워크</b>";
        html+=    "<div style='position: absolute;right: 10px; top: 2px;'>";
        html+=        '<span class="btn btn-info btn-sm" onclick="delInternalNetwork(\''+preDiv+'\', '+index+');">삭제</span>';
        html+=    "</div>";
        html+= "</div>";
        html+= body_div;
        html+= field_div_label + "서브넷 아이디" + "</label>"; 
        html+="<div>"+"<input class='form-control' name='subnetId_"+index+"'" + text_style +" placeholder='서브넷 아이디를 입력하세요.'/>"+"</div></div>";
        
        html+= field_div_label + "보안 그룹" + "</label>"; 
        html+= "<div>"+"<input class='form-control' name='cloudSecurityGroups_"+index+"'" + text_style +" placeholder='예) bosh-security, cf-security'/>"+"</div></div>";
        if( iaas.toLowerCase() == "aws" ){
            html+= "<div id='availabilityZoneDiv2' class='w2ui-field'>"+"<label style='width:40%;text-align: left;padding-left: 20px;'>" + "가용 영역" + "</label>"; 
            html+= "<div>"+"<input class='form-control' name='availabilityZone_"+index+"'" + text_style +" placeholder='예) us-west-2'/>"+"</div></div>";
        }
        html+= field_div_label + "서브넷 범위" + "</label>"; 
        html+= "<div >"+"<input class='form-control' name='subnetRange_"+index+"'" + text_style +" placeholder='예) 10.0.0.0/24'/>" + "</div></div>";
        
        html+= field_div_label + "게이트웨이" + "</label>"; 
        html+= "<div>"+ "<input class='form-control' name='subnetGateway_"+index+"'" + text_style +" placeholder='예) 10.0.0.1'/>" + "</div></div>";
        
        html+= field_div_label + "DNS" + "</label>"; 
        html+= "<div>"+ "<input class='form-control' name='subnetDns_"+index+"'" + text_style +" placeholder='예) 8.8.8.8'/>" + "</div></div>";
       
        html+= field_div_label + "IP할당 제외 대역" + "</label>"; 
        html+=     "<div>";
        html+=         "<input class='form-control' name='subnetReservedFrom_"+index+"' type='text' style='display:inline-block; width: 146px; margin-left: 20px;' placeholder='예) 10.0.0.10' />";
        html+=         "<span style='width: 4%; text-align: center;'>&nbsp;&ndash; &nbsp;</span>";
        html+=         "<input class='form-control' name='subnetReservedTo_"+index+"' type='text' style='display:inline-block; width: 146px;' placeholder='예) 10.0.0.20' />";
        html+=     "</div></div>";
        
        html+= field_div_label + "IP할당 대역(최소 20개)" + "</label>"; 
        html+=     "<div>"+"<input class='form-control' name='subnetStaticFrom_"+index+"' type='text' style='display:inline-block; width: 146px; margin-left: 20px;' placeholder='예) 10.0.0.10' />";
        html+=         "<span style='width: 4%; text-align: center;'>&nbsp;&ndash; &nbsp;</span>";
        html+=         "<input class='form-control' name='subnetStaticTo_"+index+"' type='text' style='display:inline-block; width: 146px;' placeholder='예) 10.0.0.20'/>";
        html+=     "</div>";
        html+= "</div></div></div>";
        
        
        $(div).show();
        $(preDiv + " .addInternal").hide();
        $(form + " "+ div).html(html);
        createInternalNetworkValidate(index);
}

 /********************************************************
  * 설명 : 네트워크 입력 삭제
  * 기능 : delNetwork
  *********************************************************/
 function delInternalNetwork(preDiv, index){
      var div= preDiv.split("_")[0] + "_"+ index;
      var form = preDiv.split("Div")[0]+"Form";
      $(form + " "+ div).html("");
      $(preDiv+" .addInternal").css("display","block");
 }

 /********************************************************
  * 설명 : 네트워크 유효성 추가
  * 기능 : createInternalNetworkValidate
  *********************************************************/
function createInternalNetworkValidate(index){
    
    var subnet_message="서브넷 아이디";
    var zone_message = "가용 영역";

    $("[name*='subnetId_"+index+"']").rules("add", {
        required: function(){
            return checkEmpty($("input[name='subnetId_"+index+"']").val());
        }, messages: {required: subnet_message+text_required_msg}
    });
    $("[name*='subnetRange_"+index+"']").rules("add", {
        required: function(){
            return checkEmpty($("input[name='subnetRange_"+index+"']").val());
        },ipv4Range : function(){
            return $(".w2ui-msg-body input[name='subnetRange_"+index+"']").val();  
        }, messages: {required: "서브넷 범위"+text_required_msg}
    });
    
    $("[name*='cloudSecurityGroups_"+index+"']").rules("add", {
        required: function(){
            return checkEmpty($("input[name='cloudSecurityGroups_"+index+"']").val());
        }, messages: {required: "보안 그룹"+text_required_msg}
    });
    
    
    $("[name*='subnetGateway_"+index+"']").rules("add", {
        required: function(){
            return checkEmpty($("input[name='subnetGateway_"+index+"']").val());
        },ipv4: function(){
            return $("input[name='subnetGateway_"+index+"']").val();  
        }, messages: {required: "게이트웨이"+text_required_msg}
    });
    
    $("[name*='subnetDns_"+index+"']").rules("add", {
        required: function(){
            return checkEmpty($("input[name='subnetDns_"+index+"']").val());
        },ipv4: function(){
            if( $("input[name='subnetDns_"+index+"']").val().indexOf(",") > -1 ){
                var list = ($("input[name='subnetDns_"+index+"']").val()).split(",");
                var flag = true;
                for( var i=0; i<list.length; i++ ){
                    var val = validateIpv4(list[i].trim());
                    if( !val ) flag = false;
                }
                if( !flag ) return "";
                else return list[0].trim();
            }else{
                return $("input[name='subnetDns_"+index+"']").val();
            }
        }, messages: {required: "DNS"+text_required_msg}
    });
    
    $("[name*='subnetReservedFrom_"+index+"']").rules("add", {
        required: function(){
            return checkEmpty($("input[name='subnetReservedFrom_"+index+"']").val());
        },ipv4: function(){
            return $("input[name='subnetReservedFrom_"+index+"']").val();  
        }, messages: {required: "IP 할당 제외 대역"+text_required_msg}
    });
    
    $("[name*='subnetReservedTo_"+index+"']").rules("add", {
        required: function(){
            return checkEmpty($("input[name='subnetReservedTo_"+index+"']").val());
        },ipv4: function(){
            return $("input[name='subnetReservedTo_"+index+"']").val();  
        }, messages: {required: "IP 할당 제외 대역"+text_required_msg}
    });
    
    $("[name*='subnetStaticFrom_"+index+"']").rules("add", {
        required: function(){
            return checkEmpty($("input[name='subnetStaticFrom_"+index+"']").val());
        },ipv4: function(){
            return $(".w2ui-msg-body input[name='subnetStaticFrom_"+index+"']").val();  
        }, messages: {required: "IP 할당 대역"+text_required_msg}
    });
    
    $("[name*='subnetStaticTo_"+index+"']").rules("add", {
        required: function(){
            return checkEmpty($("input[name='subnetStaticTo_"+index+"']").val());
        },ipv4: function(){
            return $("input[name='subnetStaticTo_"+index+"']").val();  
        }, messages: {required: "IP 할당 대역"+text_required_msg}
    });

    if( iaas.toLowerCase() == 'aws'){
        $("[name*='availabilityZone_"+index+"']").rules("add", {
            required: function(){
                return checkEmpty($("input[name='availabilityZone_"+index+"']").val());
            }, messages: {required: zone_message + text_required_msg}
        });
    }
    w2popup.unlock();
}


/********************************************************
 * 설명 : 네트워크 화면 설정
 * 기능 : settingNetwork
 *********************************************************/
 function settingNetwork( index ){
     addInternalNetworkInputs('#defaultNetworkInfoDiv_1', "#defaultNetworkInfoForm" );
 }
 
 /********************************************************
  * 설명 : 네트워크 AWS availabilityZoneDiv 활성/비활성화 
  * 기능 : iaasTypeChangeInput
  *********************************************************/
 function iaasTypeChangeInput(value, record){
    iaas = value;
    console.log($("#defaultNetworkInfoDiv_2").html());
    if( value.toUpperCase() == "AWS" ){
        $("#availabilityZoneDiv").show();
        $("#availabilityZoneDiv").css("display", "block");
        if($("#defaultNetworkInfoDiv_2").html() != ""){
            $("#availabilityZoneDiv2").show();
            $("#availabilityZoneDiv2").css("display", "block");
        }
    } else {
        $("#availabilityZoneDiv").hide();
        $("#availabilityZoneDiv").css("display", "none");
        if($("#defaultNetworkInfoDiv_2").html() != ""){
            $("#availabilityZoneDiv2").hide();
            $("#availabilityZoneDiv2").css("display", "none");
        }
    }
}


/********************************************************
 * 설명 : 네트워크 수정 정보 설정
 * 기능 : settingNetworkInfo
 *********************************************************/
function settingNetworkInfo(){
    var selected = w2ui['network_GroupGrid'].getSelection();
    var record = w2ui['network_GroupGrid'].get(selected);
    if(record == null) {
        w2alert("Network 정보 설정 중 에러가 발생 했습니다.");
        return;
    }
    iaas = record.iaasType;
    $("input[name=networkInfoId]").val(record.recid);
    $("input[name=networkConfigName]").val(record.networkConfigName);
    $("input[name=subnetId]").val(record.subnetId);
    $("input[name=privateStaticIp]").val(record.privateStaticIp);
    $("input[name=subnetRange]").val(record.subnetRange);
    $("input[name=subnetGateway]").val(record.subnetGateway);
    $("input[name=subnetDns]").val(record.subnetDns);
    $("input[name=publicStaticIp]").val(record.publicStaticIp);
    $("select[name=iaasType]").val(record.iaasType);
    $("select[name=iaasConfigId]").val(record.iaasConfigAlias);
}

/********************************************************
 * 설명 : 네트워크 정보 목록 조회
 * 기능 : doSearch
 *********************************************************/
function doSearch() {
    networkConfigInfo=[];//네트워크 정보
    iaas = "";
    resetForm();
    
    w2ui['network_GroupGrid'].clear();
    w2ui['network_GroupGrid'].load('/deploy/hbCf/network/list');
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
 * 설명 : 네트워크 정보 등록
 * 기능 : registCfNetworkConfigInfo
 *********************************************************/
function registCfNetworkConfigInfo(form){
    w2popup.lock("등록 중입니다.", true);
    
    var external = {
        iaas               : $("select[name='iaasType']").val(),
        networkConfigName  : $("input[name='networkName']").val(),
        seq                : 0,
        net                : "External",
        publicStaticIP     : $(".w2ui-msg-body input[name='publicStaticTo']").val(),
    }
    networkConfigInfo.push(external);
    
    var cnt = 1
    if( $("#"+form).find(".panel-body").length > 1 ){
        cnt = $("#"+form).find(".panel-body").length;
    }
    for(var i=1; i < cnt; i++){
        var internal = {
            iaas                : $("select[name='iaasType']").val(),
            net                 : "Internal",
            networkConfigName  :  $("input[name='networkName']").val(),
            seq                 : i,
            subnetRange         : $("input[name='subnetRange_"+i+"']").val(),
            subnetGateway       : $("input[name='subnetGateway_"+i+"']").val(),
            subnetDns           : $("input[name='subnetDns_"+i+"']").val(),
            subnetReservedFrom  : $("input[name='subnetReservedFrom_"+i+"']").val(),
            subnetReservedTo    : $("input[name='subnetReservedTo_"+i+"']").val(),
            subnetStaticFrom    : $("input[name='subnetStaticFrom_"+i+"']").val(),
            subnetStaticTo      : $("input[name='subnetStaticTo_"+i+"']").val(),
            networkName         : $("input[name='networkName_"+i+"']").val(),
            subnetId            : $("input[name='subnetId_"+i+"']").val(),
            cloudSecurityGroups : $("input[name='cloudSecurityGroups_"+i+"']").val(),
            availabilityZone    : $("input[name='availabilityZone_"+i+"']").val()
        }
        networkConfigInfo.push(internal);
    }
    
    console.log(networkConfigInfo);
    return;
    $.ajax({
        type : "PUT",
        url : "/deploy/hbCf/network/save",
        contentType : "application/json",
        async : true,
        data : JSON.stringify(networkConfigInfo),
        success : function(data, status) {
            doSearch();
        },
        error : function( e, status ) {
            w2popup.unlock();
            var errorResult = JSON.parse(e.responseText);
            w2alert(errorResult.message, "네트워크 정보 저장");
        }
    });
}

/********************************************************
 * 설명 : 네트워크 정보 삭제
 * 기능 : deleteCfNetworkConfigInfo
 *********************************************************/
function deleteCfNetworkConfigInfo(id, networkConfigName){
    w2popup.lock("삭제 중입니다.", true);
    networkInfo = {
        id : id,
        networkConfigName : networkConfigName
    }
    $.ajax({
        type : "DELETE",
        url : "/deploy/hbCf/network/delete",
        contentType : "application/json",
        async : true,
        data : JSON.stringify(networkInfo),
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
    $().w2destroy('network_GroupGrid');
}
/********************************************************
 * 설명 : 네트워크 정보 리셋
 * 기능 : resetForm
 *********************************************************/
function resetForm(status){
    $(".panel-body").find("p").remove();
    $(".panel-body").children().children().children().css("borderColor", "#bbb");
    $("input[name=networkConfigName]").val("");
    $("input[name=subnetId]").val("");
    $("input[name=privateStaticIp]").val("");
    $("input[name=subnetRange]").val("");
    $("input[name=subnetGateway]").val("");
    $("input[name=subnetDns]").val("");
    $("select[name=iaasType]").val("");
    $("input[name=networkInfoId]").val("");
    $("input[name=publicStaticIp]").val("");
    if(status=="reset"){
        w2ui['network_GroupGrid'].clear();
        doSearch();
    }
    document.getElementById("defaultNetworkInfoForm").reset();
}

</script>
<div id="main">
    <div class="page_site">이종 CF 설치 > <strong>Network 정보 관리</strong></div>
    <!-- 사용자 목록-->
    <div class="pdt20">
        <div class="title fl"> 네트워크 정보 목록</div>
    </div>
    <div id="network_GroupGrid" style="width:100%;  height:700px;"></div>
</div>


<div id="regPopupDiv" hidden="true" >
    <form id="defaultNetworkInfoForm" action="POST" >
    <input type="hidden" name="networkInfoId" />
        <div class="w2ui-page page-0" style="">
            <div class=" panel-network">
                <div class="panel-heading"><b>네트워크 정보</b></div>
                <div class="panel-body" >
                <div class="panel">    
                <div style="margin-left:15px; margin-top:10px;"><b>네트워크 기본 설정 </b></div>
                  <div class="panel-body">
                    <div class="w2ui-field">
                        <label style="width:40%;text-align: left;padding-left: 20px;">Network 별칭</label> 
                        <div>
                            <input class="form-control" name="networkName" type="text" style="width: 320px; margin-left: 20px;" placeholder="예) network-config"/>
                        </div>
                    </div> 
                    <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">클라우드 인프라 환경 </label> 
                       <div>
                           <select class="form-control" onchange="iaasTypeChangeInput(this.value)" name="iaasType" style="width: 320px; margin-left: 20px;">
                               <option value="">인프라 환경을 선택하세요.</option>
                               <option value="aws">AWS</option>
                               <option value="openstack">Openstack</option>
                           </select>
                       </div>
                    </div>
                   <div class="w2ui-field">
                        <label style="width:40%;text-align: left;padding-left: 20px;">CF API TARGET IP</label> 
                        <div>
                            <input class="form-control" name="publicStaticIp" type="text" style="width: 320px; margin-left: 20px;" placeholder="예) 13.157.23.56"/>
                        </div>
                    </div>
                  </div>
                </div>
                </div>
            </div>
        </div>
        
        <div class="w2ui-page page-0" style="margin-top:15px;padding:0 3%;">
            <div class="panel" id="defaultNetworkInfoDiv_1">
                <div style="position:relative; margin-left:15px; margin-top:10px;">
                    <b>Internal 네트워크</b>
                    <div style="position: absolute;right: 10px;top: 5px;">
                        <a class="btn btn-info btn-sm addInternal" onclick="addInternalNetworkInputs('#defaultNetworkInfoDiv_1', '#defaultNetworkInfoForm');">추가</a>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="w2ui-field">
                        <label style="width:40%;text-align: left;padding-left: 20px;">서브넷 아이디</label>
                        <div>
                            <input class="form-control" name="subnetId_1" type="text"  style="width: 320px; margin-left: 20px;" placeholder="서브넷 아이디를 입력하세요."/>
                        </div>
                    </div>
                    <div class="w2ui-field">
                        <label style="width:40%;text-align: left;padding-left: 20px;">보안 그룹</label>
                        <div>
                            <input class="form-control" name="cloudSecurityGroups_1" type="text"  style="width: 320px; margin-left: 20px;" placeholder="예) bosh-security, cf-security"/>
                        </div>
                    </div>
                    <div class="w2ui-field" hidden="true" id="availabilityZoneDiv">
                        <label style="width:40%;text-align: left;padding-left: 20px;">가용 영역</label>
                        <div>
                            <input class="form-control" name="availabilityZone_1" type="text"  style="width: 320px; margin-left: 20px;" placeholder="예) us-west-2"/>
                        </div>
                    </div>
                    <div class="w2ui-field">
                        <label style="width:40%;text-align: left;padding-left: 20px;">서브넷 범위</label>
                        <div>
                            <input class="form-control" name="subnetRange_1" type="text"  style="width: 320px; margin-left: 20px;" placeholder="예) 10.0.0.0/24"/>
                        </div>
                    </div>
                    <div class="w2ui-field">
                        <label style="width:40%;text-align: left;padding-left: 20px;">게이트웨이</label>
                        <div>
                            <input class="form-control" name="subnetGateway_1" type="text"  style="width: 320px; margin-left: 20px;" placeholder="예) 10.0.0.1"/>
                        </div>
                    </div>
                    <div class="w2ui-field">
                        <label style="width:40%;text-align: left;padding-left: 20px;">DNS</label>
                        <div>
                            <input class="form-control" name="subnetDns_1" type="text"  style="width: 320px; margin-left: 20px;" placeholder="예) 8.8.8.8"/>
                        </div>
                    </div>
                    <div class="w2ui-field">
                        <label style="width:40%;text-align: left;padding-left: 20px;">IP할당 제외 대역</label>
                        <div>
                            <input class="form-control" name="subnetReservedFrom_1" type="text" style="display:inline-block; width: 146px; margin-left: 20px;" placeholder="예) 10.0.0.100" />
                            <span style="width: 4%; text-align: center;">&nbsp;&ndash; &nbsp;</span>
                            <input class="form-control" name="subnetReservedTo_1"  type="text" style="display:inline-block; width: 146px;" placeholder="예) 10.0.0.106" />
                        </div>
                    </div>
                    <div class="w2ui-field">
                        <label style="width:40%;text-align: left;padding-left: 20px;">IP할당 대역(최소 20개)</label>
                        <div>
                            <input class="form-control" name="subnetStaticFrom_1"  type="text" style="display:inline-block; width: 146px; margin-left: 20px;"  placeholder="예) 10.0.0.100" />
                            <span style="width: 4%; text-align: center;">&nbsp;&ndash; &nbsp;</span>
                            <input class="form-control" name="subnetStaticTo_1" type="text" style="display:inline-block; width: 146px;" placeholder="예) 10.0.0.106" />
                        </div>
                    </div>
                </div>
            </div>
            <div  id="defaultNetworkInfoDiv_2" hidden="true"></div>
        </div>
        
        

    </form>
    <div id="regPopupBtnDiv" style="text-align: center; margin-top: 5px;">
        <sec:authorize access="hasAuthority('DEPLOY_CF_MENU')">
            <span id="installBtn" onclick="$('#defaultNetworkInfoForm').submit();" class="btn btn-primary">등록</span>
        </sec:authorize>
        <span id="resetBtn" onclick="resetForm('reset');" class="btn btn-info">취소</span>
        <sec:authorize access="hasAuthority('DEPLOY_CF_MENU')">
            <span id="deleteBtn" class="btn btn-danger">삭제</span>
        </sec:authorize>
    </div>
</div>
<script>
$(function() {
    $.validator.addMethod( "ipv4", function( value, element, params ) {
        return /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(params);
    }, text_ip_msg );
    
    $.validator.addMethod( "ipv4Range", function( value, element, params ) {
        return /^((\b|\.)(0|1|2(?!5(?=6|7|8|9)|6|7|8|9))?\d{1,2}){4}(-((\b|\.)(0|1|2(?!5(?=6|7|8|9)|6|7|8|9))?\d{1,2}){4}|\/((0|1|2|3(?=1|2))\d|\d))\b$/.test(params);
    }, text_ip_msg );
    
    
    $("#defaultNetworkInfoForm").validate({
        ignore : "",
        rules:{
           subnetId_1 : {
               required : function(){ return checkEmpty( $("input[name='subnetId_1']").val() ); } 
           },cloudSecurityGroups_1 : {
               required : function(){ return checkEmpty( $("input[name='cloudSecurityGroups_1']").val() ); }
           },availabilityZone_1 : {
               required : function(){ 
                   if( iaas.toLowerCase() == "openstack" ){
                       return false;
                   }else{
                       return checkEmpty( $("input[name='availabilityZone_1']").val() ); 
                   }
               }
           }, subnetRange_1: { 
                required : function(){ return checkEmpty( $("input[name='subnetRange_1']").val() ); }
               ,ipv4Range: function(){  return $("input[name='subnetRange_1']").val(); }
           }, subnetGateway_1: { 
                required: function(){ return checkEmpty( $("input[name='subnetGateway_1']").val() ); }
               ,ipv4    : function(){ return $("input[name='subnetGateway_1']").val(); }
           }, subnetDns_1: { 
               required: function(){ return checkEmpty( $("input[name='subnetDns_1']").val() ); } 
               , ipv4  : function(){ 
                   if( $("input[name='subnetDns_1']").val().indexOf(",") > -1 ){
                       var list = ($("input[name='subnetDns_1']").val()).split(",");
                       var flag = true;
                       for( var i=0; i<list.length; i++ ){
                           var val = validateIpv4(list[i].trim());
                           if( !val ) flag = false;
                       }
                       if( !flag ) return "false";
                       else return list[0].trim();
                   }else{
                       return $("input[name='subnetDns_1']").val();
                   }
               }
           }, subnetReservedFrom_1: { 
                required: function(){ return checkEmpty( $("input[name='subnetReservedFrom_1']").val() ); }
               ,ipv4    : function(){ return $("input[name='subnetReservedFrom_1']").val(); }
           }, subnetReservedTo_1: { 
                required: function(){ return checkEmpty( $(".w2ui-msg-body input[name='subnetReservedTo_1']").val() ); }
               ,ipv4    : function(){ return $("input[name='subnetReservedTo_1']").val(); }
           }, subnetStaticFrom_1: { 
                required: function(){ return checkEmpty( $(".w2ui-msg-body input[name='subnetStaticFrom_1']").val() ); }
               ,ipv4: function(){ return $("input[name='subnetStaticFrom_1']").val(); }
           }, subnetStaticTo_1: { 
                required: function(){ return checkEmpty( $("input[name='subnetStaticTo_1']").val() ); }
               ,ipv4: function(){ return $("input[name='subnetStaticTo_1']").val(); }
           }
        }, messages: {
            subnetId_1            : { required: "서브넷 아이디"+text_required_msg }
            ,cloudSecurityGroups_1 : { required: "보안 그룹"+text_required_msg }
            ,availabilityZone_1    : { required: "가용 영역"+text_required_msg } 
            , subnetRange_1        : { required: "서브넷 범위"+text_required_msg } 
            , subnetGateway_1      : { required: "게이트웨이"+text_required_msg }
            , subnetDns_1          : { required: "DNS" + text_required_msg }
            , subnetReservedFrom_1 : { required:  "IP할당 제외 대역"+text_required_msg } 
            , subnetReservedTo_1   : { required: "IP할당 제외 대역"+text_required_msg }
            , subnetStaticFrom_1   : { required: "IP할당 대역"+text_required_msg }
            , subnetStaticTo_1     : { required: "IP할당 대역"+text_required_msg }
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
            registCfNetworkConfigInfo($(form).attr("id"));
        }
    });
});

function validateIpv4(params){
    return /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(params);
}

</script>