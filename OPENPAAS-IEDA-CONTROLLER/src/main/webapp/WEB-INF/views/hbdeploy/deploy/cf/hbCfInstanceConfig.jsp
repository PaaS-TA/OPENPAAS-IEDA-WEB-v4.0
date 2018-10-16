<%
/* =================================================================
 * 상세설명 : 인스턴스 관리 화면
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
var instanceConfigInfo = [];//인증서
var defaultConfigInfo = "";
var networkConfigInfo = "";
var jobsInfo=[];
var list_lock_msg = "목록을 조회 중입니다.";
var iaas = "";
var instanceLayout = {
        layout2: {
            name: 'layout2',
            padding: 4,
            panels: [
                { type: 'left', size: '65%', resizable: true, minSize: 300 },
                { type: 'main', minSize: 300 }
            ]
        },
        /********************************************************
         *  설명 : 인스턴스 목록 Grid
        *********************************************************/
        grid: {
            name: 'cf_instance_config_grid',
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
                   { field: 'instanceConfigName', caption: '인스턴스 정보 명', size:'130px', style:'text-align:center;' },
                   { field: 'iaasType', caption: '인프라 환경 타입', size:'120px', style:'text-align:center;' ,render: function(record){ 
                       if(record.iaasType.toLowerCase() == "aws"){
                           return "<img src='images/iaasMgnt/aws-icon.png' width='80' height='30' />";
                       }else if (record.iaasType.toLowerCase() == "openstack"){
                           return "<img src='images/iaasMgnt/openstack-icon.png' width='90' height='35' />";
                       }
                   }},
                   { field: 'nats_z1', caption: 'nats 수', size:'120px', style:'text-align:center;',render: function(record){ 
                       if(record.nats_z2 != "" && record.nats_z2 != null){
                           return record.nats_z1+"<br>"+record.nats_z2;
                       }else return record.nats_z1
                   }},
                   { field: 'blobstore_z1', caption: 'blobstore 수', size:'120px', style:'text-align:center;',render: function(record){ 
                       if(record.blobstore_z2 != ""  && record.blobstore_z2 != null){
                           return record.blobstore_z1+"<br>"+record.blobstore_z2;
                       }else return record.blobstore_z1
                   }},
                   { field: 'router_z1', caption: 'router 수', size:'120px', style:'text-align:center;',render: function(record){ 
                       if(record.router_z2 != ""  && record.router_z2 != null){
                           return record.router_z1+"<br>"+record.router_z2;
                       }else return record.router_z1
                   }},
                   { field: 'loggregator_z1', caption: 'loggregator 수', size:'120px', style:'text-align:center;',render: function(record){ 
                       if(record.loggregator_z2 != ""  && record.loggregator_z2 != null){
                           return record.loggregator_z1+"<br>"+record.loggregator_z2;
                       }else return record.loggregator_z1
                   }},
                   { field: 'doppler_z1', caption: 'doppler 수', size:'120px', style:'text-align:center;',render: function(record){ 
                       if(record.doppler_z2 != ""  && record.doppler_z2 != null){
                           return record.doppler_z1+"<br>"+record.doppler_z2;
                       }else return record.doppler_z1
                   }},
                   { field: 'etcd_z1', caption: 'etcd 수', size:'120px', style:'text-align:center;',render: function(record){ 
                       if(record.etcd_z2 != ""  && record.etcd_z2 != null){
                           return record.etcd_z1+"<br>"+record.etcd_z2;
                       }else return record.etcd_z1
                   }},
                   { field: 'consul_z1', caption: 'consul 수', size:'120px', style:'text-align:center;',render: function(record){ 
                       if(record.consul_z2 != ""  && record.consul_z2 != null){
                           return record.consul_z1+"<br>"+record.consul_z2;
                       }else return record.consul_z1
                   }},
                   { field: 'clock_z1', caption: 'clock 수', size:'120px', style:'text-align:center;',render: function(record){ 
                       if(record.clock_z2 != "" && record.clock_z2 != null ){
                           return record.clock_z1+"<br>"+record.clock_z2;
                       }else return record.clock_z1
                   }},
                   { field: 'uaa_z1', caption: 'uaa 수', size:'120px', style:'text-align:center;',render: function(record){ 
                       if(record.uaa_z2 != ""  && record.uaa_z2 != null){
                           return record.uaa_z1+"<br>"+record.uaa_z2;
                       }else return record.uaa_z1
                   }},
                   { field: 'api_z1', caption: 'api 수', size:'120px', style:'text-align:center;',render: function(record){ 
                       if(record.api_z2 != ""  && record.api_z2 != null){
                           return record.api_z1+"<br>"+record.api_z2;
                       }else return record.api_z1
                   }},
                   { field: 'api_worker_z1', caption: 'api_worker 수', size:'120px', style:'text-align:center;',render: function(record){ 
                       if(record.api_worker_z2 != ""  && record.api_worker_z2 != null){
                           return record.api_worker_z1+"<br>"+record.api_worker_z2;
                       }else return record.api_worker_z1
                   }},
                   { field: 'postgres_z1', caption: 'postgres 수', size:'120px', style:'text-align:center;',render: function(record){ 
                       if(record.postgres_z2 != ""  && record.postgres_z2 != null){
                           return record.postgres_z1+"<br>"+record.postgres_z2;
                       }else return record.postgres_z1
                   }},
                  ],
            onSelect : function(event) {
                event.onComplete = function() {
                    $('#deleteBtn').attr('disabled', false);
                    settingInstanceConfigInfo();
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
    $('#cf_instance_config_grid').w2layout(instanceLayout.layout2);
    w2ui.layout2.content('left', $().w2grid(instanceLayout.grid));
    w2ui['layout2'].content('main', $('#regPopupDiv').html());
    
    doSearch();
    initView();
    
    $("#deleteBtn").click(function(){
        if($("#deleteBtn").attr('disabled') == "disabled") return;
        var selected = w2ui['cf_instance_config_grid'].getSelection();
        if( selected.length == 0 ){
            w2alert("선택된 정보가 없습니다.", "인스턴스 정보 삭제");
            return;
        }
        else {
            var record = w2ui['cf_instance_config_grid'].get(selected);
            w2confirm({
                title       : "인스턴스",
                msg         : "인스턴스 정보 ("+record.instanceConfigName + ")을 삭제하시겠습니까?",
                yes_text    : "확인",
                no_text     : "취소",
                yes_callBack: function(event){
                    deleteHbCfInstanceConfigInfo(record.id, record.instanceConfigName);
                },
                no_callBack    : function(){
                    w2ui['cf_instance_config_grid'].clear();
                    doSearch();
                }
            });
        }
    });
});

/********************************************************
 * 설명 : 초기 인스턴스 정보 관리 function
 * 기능 : initView
 *********************************************************/
function initView(){
    getCfDefaultConfigListInfo();
}

/********************************************************
 * 설명 : Default Config 정보 목록 조회 및 데이터 설정
 * 기능 : getCfDefaultConfigListInfo
 *********************************************************/
function getCfDefaultConfigListInfo(){
    w2utils.lock($("#layout_layout_panel_main"), list_lock_msg, true);
    var option = "<option value=''>기본 정보 명을 선택하세요.</option>";
    $.ajax({
        type : "GET",
        url : "/deploy/hbCf/instance/defaultConfig/list",
        contentType : "application/json",
        async : true,
        success : function(data, status) {
            if(data.records.length != 0){
                data.records.map(function (obj){
                    if(obj.id == defaultConfigInfo) {
                        option += "<option selected value="+obj.releaseVersion+">"+obj.defaultConfigName+"</option>";
                    } else {
                        option += "<option value="+obj.releaseVersion+">"+obj.defaultConfigName+"</option>";
                    }
                });
            }else if (data.records.length == 0){
                option = "<option value=''>기본 정보가 존재 하지 않습니다.</option>";
            }
            $("#cfDefaultConfig").html(option);
            w2utils.unlock($("#layout_layout_panel_main"));
        },
        error : function( request, status, error ) {
            var errorResult = JSON.parse(request.responseText);
            w2alert(errorResult.message, "CF 기본 정보 목록 조회");
            w2utils.unlock($("#layout_layout_panel_main"));
            doSearch();
            resetForm();
        }
    });
}


/********************************************************
 * 설명 : Network Config 정보 목록 조회 및 데이터 설정
 * 기능 : getCfNetworkConfigListInfo
 *********************************************************/
function getCfNetworkConfigListInfo(value){
    if($("select[name=cfDefaultConfig]").val() == ""){
        w2alert("기본 정보를 선택하세요.");
        $("select[name=cfNetworkConfig]").attr("disabled","disabled");
        $("select[name=cfNetworkConfig]").val("<option value=''>네트워크 정보 명을 선택하세요.</option>");
        $("#cfDetailForm").html("");
    } else {
        if($("select[name=cfNetworkConfig]").attr("disabled") == "disabled"){
            $("select[name=cfNetworkConfig]").removeAttr("disabled");
        }
        w2utils.lock($("#layout_layout_panel_main"), list_lock_msg, true);
        var option = "<option value=''>네트워크 정보 명을 선택하세요.</option>";
        $.ajax({
            type : "GET",
            url : "/deploy/hbCf/instance/networkConfig/list",
            contentType : "application/json",
            async : true,
            success : function(data, status) {
                if(data.records.length != 0){
                    data.records.map(function (obj){
                        if(obj.networkConfigName == networkConfigInfo) {
                            option += "<option selected value="+obj.networkConfigName+">"+obj.networkConfigName+"</option>";
                        } else {
                            option += "<option value="+obj.networkConfigName+">"+obj.networkConfigName+"</option>";
                        }
                    });
                }else if (data.records.length == 0){
                    option = "<option value=''>네트워크 정보가 존재 하지 않습니다.</option>";
                }
                $("#cfNetworkConfig").html(option);
                w2utils.unlock($("#layout_layout_panel_main"));
            },
            error : function( request, status, error ) {
                var errorResult = JSON.parse(request.responseText);
                w2alert(errorResult.message, "CF 네트워크 정보 목록 조회");
                w2utils.unlock($("#layout_layout_panel_main"));
                doSearch();
                resetForm();
            }
        });
    }
}

/********************************************************
 * 설명 : 인스턴스 정보 DIV 호출
 * 기능 : showInstanceResource
 *********************************************************/
function showInstanceResource(value){
    if(value==""){
        w2alert("네트워크 정보를 선택하세요.", "CF 인스턴스 정보 관리");
        $("#cfDetailForm").html("");
        return;
    }
    var networkInfo = networkDetailInfo(value);
    var release_version =  $("select[name=cfDefaultConfig]").val();
    release_version = settingReleaseVersion(release_version);
    jobSetting(release_version, networkInfo);
}

/********************************************************
 * 설명 : 네트워크 정보 상세 조회
 * 기능 : networkDetailInfo
 *********************************************************/
function networkDetailInfo(networkConfigName){
    var networkInfo = [];
    $.ajax({
        type : "GET",
        url : "/deploy/hbCf/network/list/detail/"+networkConfigName,
        async: false,
        contentType : "application/json",
        dataType : "json",
        success : function(data, status) {
            if(data != null && data.records.length > 0){
                networkInfo = data.records;
            } else {
                w2alert("Network 상세 정보 조회 실패, <br> 네트워크 정보를 확인해 주세요.");
                doSearch();
            }
        },
        error : function(request, status, error) {
            var errorResult = JSON.parse(request.responseText);
            w2alert(errorResult.message);
            doSearch();
        }
    });
    return networkInfo;
}

/********************************************************
 * 설명 : CF Job 성정
 * 기능 : jobSetting
 *********************************************************/
function jobSetting(release_version, networkInfo){
    $.ajax({
        type : "GET",
        dataType : "json",
        async: false,
        url : "/deploy/hbCf/instance/jobSetting/list/"+release_version+"/"+'DEPLOY_TYPE_HYBRID_CF',
        contentType : "application/json",
        success : function(data, status) {
            if( !checkEmpty(data) ){
                var div = "";
                var html = "";
                html += '<div class="panel-body">';
                html += '<div id="cfJobListDiv">';
                html += '<p style="color:red;">- 고급 설정 값을 변경하지 않을 경우 아래에 입력 된 기본 값으로 자동 설정됩니다.</p>';
                html += '<p style="color:red;">- 해당 Job의 인스턴스 수는 0-100까지 입력하실 수 있습니다.</p>';
                for( var j=1; j<networkInfo.length; j++ ){
                    html += "<p style='color: #565656;font-size: 13px;font-weight:bolder;margin-top: 20px;'>[Internal 네트워크_"+ j+ "]</p>"
                    for( var i=0; i<data.length; i++ ){
                        if( j == 1 && data[i].zone_z1 == "true" ){
                            html += setJobSettingHtml(data[i], j );
                        }else if( j == 2 && data[i].zone_z2 == "true"  ){
                            html += setJobSettingHtml(data[i], j);
                        }
                    }
                }
                html +='</div></div></div>';
                $("#cfDetailForm").html(html);
            }
        },
        error : function(e, status) {
            w2alert(JSON.parse(e.responseText).message, "CF 인스턴스 정보 관리");
        }
    });
}

/********************************************************
 * 설명 : CF 고급 설정 HTML 설정
 * 기능 : setJobSettingHtml
 *********************************************************/
function setJobSettingHtml(data, j){
    var html = "";
    html += '<ul class="w2ui-field" style="border: 1px solid #c5e3f3;padding: 10px;">';
    html +=     '<li style="display:inline-block; width:35%;">';
    html +=         '<label style="text-align: left;font-size:11px;">'+data.job_name+'_z'+j+'</label>';
    html +=     '</li>';
    html +=     '<li style="display:inline-block; width:60%;vertical-align:middle; line-height:3; text-align:right;">';
    html +=         '<ul>';
    html +=             '<li>';
    html +=                 '<label style="display:inline-block;">인스턴스 수 : </label>&nbsp;&nbsp;&nbsp;';
    html +=                 '<input class="form-control" style="width:60%; display:inline-block;" onblur="instanceControl(this);" onfocusin="instanceControl(this);" onfocusout="instanceControl(this);" maxlength="100" type="number" min="0" max="100" value="1" id="'+data.id+'" name="'+data.job_name+'_z'+j+'"/>';
    html +=              '</li>';
    html +=         '</ul>';
    html +=     '</li>';
    html += '</ul>';
    return html;
}

/********************************************************
 * 설명 : CF Jobs 유효성 추가
 * 기능 : instanceControl
 *********************************************************/
function instanceControl(e){
     if ( e.value != "" && e.value >=0 && e.value<=100) {
         if( $(e).parent().find("p").length > 0 ){
             $(e).parent().find("p").remove();
         }
     }else{
         var name = $(e).attr("name");
         if( jobsInfo.length >0 ){
             for( var i=0; i<jobsInfo.length; i++ ){
                 if( $(e).attr("name") == jobsInfo[i].job_name+"_"+jobsInfo[i].zone ){
//                      e.value= jobsInfo[i].instances;
                 }
             }
         }else{
//              $(e).val("1");
         }
         if( $(e).parent().find("p").length == 0 ){
             $(e).parent().append("<p>0부터 100까지 숫자만 입력 가능 합니다.</p>");
         }
     }
}

/********************************************************
 * 설명 : paasta release version 설정
 * 기능 : settingReleaseVersion
 *********************************************************/
function settingReleaseVersion( version ){
    var releaseVersion = version;
     if( version == "3.0" ){
        releaseVersion = "272";
    } else if(version == "3.1"){
        releaseVersion = "287";
    }
    return releaseVersion;
}

/********************************************************
 * 설명 : 인스턴스 정보 수정 정보 설정
 * 기능 : settingInstanceConfigInfo
 *********************************************************/
function settingInstanceConfigInfo(){
    var selected = w2ui['cf_instance_config_grid'].getSelection();
    var record = w2ui['cf_instance_config_grid'].get(selected);
    if(record == null) {
        w2alert("리소스 정보 설정 중 에러가 발생 했습니다.");
        return;
    }
    iaas = record.iaasType;
    $("input[name=instanceInfoId]").val(record.id);
    $("input[name=instanceConfigName]").val(record.instanceConfigName);
    $("select[name=iaasType]").val(record.iaasType);
    $("select[name=cfDefaultConfig]").val(record.defaultConfigInfo);
    networkConfigInfo = record.networkConfigInfo;
    getCfNetworkConfigListInfo(record.networkConfigInfo);
    showInstanceResource(record.networkConfigInfo);
    $.ajax({
        type : "GET",
        url : "/deploy/hbCf/instance/list/detail/"+record.id,
        async : false,
        contentType : "application/json",
        success : function(data, status) {
            for(var i=0; i<Object.keys(data).length; i++){
                console.log(Object.keys(data)[i]);
                console.log(Object.values(data)[i]);
                if(Object.keys(data)[i].indexOf("_z") != -1){
                	$("input[name='"+Object.keys(data)[i]+"']").val(Object.values(data)[i]);
                }
            }
        },
        error : function( e, status ) {
            w2utils.unlock($("#layout_layout_panel_main"));
            var errorResult = JSON.parse(e.responseText);
            w2alert(errorResult.message, "리소스 정보 저장");
        }
    });
    
}

/********************************************************
 * 설명 : 리솟 목록 조회
 * 기능 : doSearch
 *********************************************************/
function doSearch() {
    instanceConfigInfo = [];
    iaas = "";
    resetForm();
    w2ui['cf_instance_config_grid'].clear();
    w2ui['cf_instance_config_grid'].load('/deploy/hbCf/instance/list');
}
/********************************************************
 * 설명 : 초기 버튼 스타일
 * 기능 : doButtonStyle
 *********************************************************/
function doButtonStyle() {
    $('#deleteBtn').attr('disabled', true);
}
/********************************************************
 * 설명 : 인스턴스 정보  등록
 * 기능 : registHbCfInstanceConfigInfo
 *********************************************************/
function registHbCfInstanceConfigInfo(){
    w2popup.lock("등록 중입니다.", true);
    instanceConfigInfo = {
            id                                 : $("input[name='instanceInfoId']").val(),
            iaasType                           : $("select[name='iaasType']").val(),
            instanceConfigName                 : $("input[name='instanceConfigName']").val(),
            defaultConfigInfo                  : $("select[name='cfDefaultConfig']").val(),
            networkConfigInfo                  : $("select[name='cfNetworkConfig']").val(),
            
            nats_z1                            : $("input[name='nats_z1']").val(),
            blobstore_z1                       : $("input[name='blobstore_z1']").val(),
            router_z1                          : $("input[name='router_z1']").val(),
            loggregator_trafficcontroller_z1   : $("input[name='loggregator_z1']").val(),
            droppler_z1                        : $("input[name='doppler_z1']").val(),
            etcd_z1                            : $("input[name='etcd_z1']").val(),
            consul_z1                          : $("input[name='consul_z1']").val(),
            clock_z1                           : $("input[name='clock_z1']").val(),
            uaa_z1                             : $("input[name='uaa_z1']").val(),
            api_z1                             : $("input[name='api_z1']").val(),
            api_worker_z1                      : $("input[name='api_worker_z1']").val(),
            postgres_z1                        : $("input[name='postgres_z1']").val(),
            nats_z2                           : $("input[name='nats_z2']").val(),
            blobstore_z2                      : $("input[name='blobstore_z2']").val(),
            router_z2                         : $("input[name='router_z2']").val(),
            loggregator_trafficcontroller_z2  : $("input[name='loggregator_z2']").val(),
            droppler_z2                       : $("input[name='doppler_z2']").val(),
            etcd_z2                           : $("input[name='etcd_z2']").val(),
            consul_z2                          : $("input[name='consul_z2']").val(),
            clock_z2                          : $("input[name='clock_z2']").val(),
            uaa_z2                            : $("input[name='uaa_z2']").val(),
            api_z2                            : $("input[name='api_z2']").val(),
            api_worker_z2                     : $("input[name='api_worker_z2']").val(),
            postgres_z2                       : $("input[name='postgres_z2']").val(),
    }
    $.ajax({
        type : "PUT",
        url : "/deploy/hbCf/instance/save",
        contentType : "application/json",
        async : true,
        data : JSON.stringify(instanceConfigInfo),
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
 * 기능 : deleteHbCfInstanceConfigInfo
 *********************************************************/
function deleteHbCfInstanceConfigInfo(id, resourceConfigName){
    w2popup.lock("삭제 중입니다.", true);
    instanceConfigInfo = {
        id : id,
        resourceConfigName : resourceConfigName
    }
    $.ajax({
        type : "DELETE",
        url : "/deploy/hbCf/instance/delete",
        contentType : "application/json",
        async : true,
        data : JSON.stringify(instanceConfigInfo),
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
    $().w2destroy('cf_instance_config_grid');
}
/********************************************************
 * 설명 : 인증서 리셋
 * 기능 : resetForm
 *********************************************************/
function resetForm(status){
    $(".panel-body").find("p").remove();
    $(".panel-body").children().children().children().css("borderColor", "#bbb");
    
    $("select[name=iaasType]").val("");
    $("input[name=instanceInfoId]").val("");
    instanceConfigInfo = [];//인증서
    defaultConfigInfo = "";
    networkConfigInfo = "";
    jobsInfo=[];
    
    $("select[name=cfNetworkConfig]").attr("disabled", "disabled");
    $("select[name=cfNetworkConfig]").html("<option value=''>네트워크 정보 명을 선택하세요.</option>");
    $("select[name=cfNetworkConfig]").val("")
    $("#cfDetailForm").html("");
    
    
    $("input[name='nats_z1']").val("")
    $("input[name='blobstore_z1']").val("")
    $("input[name='router_z1']").val("")
    $("input[name='loggregator_z1']").val("")
    $("input[name='doppler_z1']").val("")
    $("input[name='etcd_z1']").val("")
    $("input[name='consul_z1']").val("")
    $("input[name='clock_z1']").val("")
    $("input[name='uaa_z1']").val("")
    $("input[name='api_z1']").val("")
    $("input[name='api_worker_z1']").val("")
    $("input[name='postgres_z1']").val("")
    $("input[name='nats_z2']").val("")
    $("input[name='blobstore_z2']").val("")
    $("input[name='router_z2']").val("")
    $("input[name='loggregator_z2']").val("")
    $("input[name='doppler_z2']").val("")
    $("input[name='etcd_z2']").val("")
    $("input[name='consul_z2']").val("")
    $("input[name='clock_z2']").val("")
    $("input[name='uaa_z2']").val("")
    $("input[name='api_z2']").val("")
    $("input[name='api_worker_z2']").val("")
    $("input[name='postgres_z2']").val("")
    
    if(status=="reset"){
        w2ui['cf_instance_config_grid'].clear();
        doSearch();
    }
    document.getElementById("settingForm").reset();
}
</script>
<div id="main">
    <div class="page_site"> 이종 CF 설치 > <strong>인스턴스 정보 관리</strong></div>
    <!-- 사용자 목록-->
    <div class="pdt20">
        <div class="title fl">인스턴스 정보 목록</div>
    </div>
    <div id="cf_instance_config_grid" style="width:100%;  height:700px;"></div>

</div>


<div id="regPopupDiv" hidden="true" >
    <form id="settingForm" action="POST" >
    <input type="hidden" name="instanceInfoId" />
        <div class="w2ui-page page-0" style="">
           <div class="panel panel-default">
               <div class="panel-heading"><b>인스턴스 정보 </b></div>
               <div class="panel-body" style="height:330px; overflow-y:auto;">
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;"> 인스턴스 정보 별칭</label>
                       <div>
                           <input class="form-control" name = "instanceConfigName" type="text"  maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="인스턴스 정보 별칭을 입력 하세요."/>
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
                       <label style="width:40%; text-align: left;padding-left: 20px;">기본 정보 명</label>
                       <div>
                            <select class="form-control" id="cfDefaultConfig" onchange="getCfNetworkConfigListInfo(this.value);" name="cfDefaultConfig" style="display:inline-block; width: 320px; margin-left: 20px;">
                                <option value="">기본 정보 명을 선택하세요.</option>
                            </select>
                      </div>
                  </div>
                  
                   <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">네트워크 정보 명</label>
                       <div>
                           <select disabled="disabled" class="form-control" onchange="showInstanceResource(this.value);" id="cfNetworkConfig" name="cfNetworkConfig" style="display:inline-block; width: 320px; margin-left: 20px;">
                               <option value="">네트워크 정보 명을 선택하세요.</option>
                           </select>
                       </div>
                   </div>
               </div>
               <div class="panel-heading" style="position:relative"><b>인스턴스 수 설정<font style="color: red;">(기본/네트워크 정보를 선택해주세요.)</font></b></div>
                   <div class="panel-body" id="cfDetailForm" style="padding:5px 5% 10px 5%;">
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
            registHbCfInstanceConfigInfo();
        }
    });
});
</script>