<%
/* =================================================================
 * 상세설명 : Bootstrap 설치
 * =================================================================
 * 수정일      작성자    내용     
 * ------------------------------------------------------------------
 * 2016.07    지향은    화면 수정 및 vSphere 클라우드 기능 추가
 * 2016.12    지향은    Bootstrap 목록과 팝업 화면 .jsp 분리 및 설치 버그 수정 
 * 2017.08    지향은    화면 수정 및 Google 클라우드 기능 추가
 * =================================================================
 */ 
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<script type="text/javascript">

/******************************************************************
 * 설명 :    변수 설정
 ***************************************************************** */
var iaas ="";
var cfInfo = "";
var installStatus ="";//설치 상태
var installClient = "";//설치 client
var deleteClient = "";//삭제 client
var bootStrapDeploymentName = new Array();
$(function() {    
    /********************************************************
     * 설명 :  bootstrap 목록 설정
     *********************************************************/
     $('#config_cfGrid').w2grid({
        name: 'config_cfGrid',
        header: '<b>BOOTSTRAP 목록</b>',
        method: 'GET',
         multiSelect: false,
        show: {    
                selectColumn: true,
                footer: true},
        style: 'text-align: center',
        columns:[
              {field: 'recid',     caption: 'recid', hidden: true}
            , {field: 'bootstrapConfigName', caption: 'BOOTSTRAP 정보 별칭', size: '20%'}
            , {field: 'iaasType', caption: '인프라 환경 타입', size:'120px', style:'text-align:center;' ,render: function(record){ 
                if(record.iaasType.toLowerCase() == "aws"){
                    return "<img src='images/iaasMgnt/aws-icon.png' width='80' height='30' />";
                }else if (record.iaasType.toLowerCase() == "openstack"){
                    return "<img src='images/iaasMgnt/openstack-icon.png' width='90' height='35' />";
                }
            }}
            , {field: 'cpiConfigInfo', caption: 'CPI 정보 별칭', size: '20%'}
            , {field: 'defaultConfigInfo', caption: '기본 정보 별칭', size: '20%'}
            , {field: 'networkConfigInfo', caption: '네트워크 정보 별칭', size: '20%'}
            , {field: 'resourceConfigInfo', caption: '리소스 정보 별칭 ', size: '20%'}
            ],
        onSelect : function(event) {
            event.onComplete = function() {
                $('#modifyBtn').attr('disabled', false);
                $('#deleteBtn').attr('disabled', false);
                return;
            }
        },
        onDblClick: function (event) {
            var grid = this;
            // need timer for nicer visual effect that record was selected
            setTimeout(function () {
                w2ui['config_cfGrid2'].add( $.extend({}, grid.get(event.recid), { selected : false }) );
                grid.selectNone();
                grid.remove(event.recid);
            }, 150);
        }
        ,onUnselect : function(event) {
            event.onComplete = function() {
                $('#modifyBtn').attr('disabled', true);
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
    });
    
     $('#config_cfGrid2').w2grid({ 
         name: 'config_cfGrid2', 
         header: '<b>BOOTSTRAP 목록</b>',
         columns:[
             {field: 'recid',     caption: 'recid', hidden: true}
           , {field: 'bootstrapConfigName', caption: 'BOOTSTRAP 정보 별칭', size: '20%'}
           , {field: 'iaasType', caption: '인프라 환경 타입', size:'120px', style:'text-align:center;' ,render: function(record){ 
               if(record.iaasType.toLowerCase() == "aws"){
                   return "<img src='images/iaasMgnt/aws-icon.png' width='80' height='30' />";
               }else if (record.iaasType.toLowerCase() == "openstack"){
                   return "<img src='images/iaasMgnt/openstack-icon.png' width='90' height='35' />";
               }
           }}
           , {field: 'cpiConfigInfo', caption: 'CPI 정보 별칭', size: '20%'}
           , {field: 'defaultConfigInfo', caption: '기본 정보 별칭', size: '20%'}
           , {field: 'networkConfigInfo', caption: '네트워크 정보 별칭', size: '20%'}
           , {field: 'resourceConfigInfo', caption: '리소스 정보 별칭 ', size: '20%'}
           ],
           onSelect : function(event) {
               event.onComplete = function() {
                   $('#installVmBtn').attr('disabled', false);
               }
           },onDblClick: function (event) {
             var record = w2ui['config_cfGrid2'].get(event.recid);
             var grid = this;
             var gridName = "";
             if(record.deployStatus != null){
                 gridName = "config_cfGrid3";
             }else{
                 gridName = "config_cfGrid";
             }
             // need timer for nicer visual effect that record was selected
             setTimeout(function () {
                 w2ui[''+gridName+''].add( $.extend({}, grid.get(event.recid), { selected : false }) );
                 grid.selectNone();
                 grid.remove(event.recid);
             }, 150);
           },onUnselect : function(event) {
               event.onComplete = function() {
                   
               }
           },onLoad:function(event){
               if(event.xhr.status == 403){
                   location.href = "/abuse";
                   event.preventDefault();
               }
           },onError : function(event) {
           }
     });
     
     $('#config_cfGrid3').w2grid({
         name: 'config_cfGrid3',
         header: '<b>BOOTSTRAP 목록</b>',
         method: 'GET',
          multiSelect: false,
         show: {    
                 selectColumn: true,
                 footer: true},
         style: 'text-align: center',
         columns:[
               {field: 'recid',     caption: 'recid', hidden: true}
             , {field: 'bootstrapConfigName', caption: 'BOOTSTRAP 정보 별칭', size: '140px'}
             , {field: 'iaasType', caption: '인프라 환경 타입', size:'120px', style:'text-align:center;' ,render: function(record){ 
                 if(record.iaasType.toLowerCase() == "aws"){
                     return "<img src='images/iaasMgnt/aws-icon.png' width='80' height='30' />";
                 }else if (record.iaasType.toLowerCase() == "openstack"){
                     return "<img src='images/iaasMgnt/openstack-icon.png' width='90' height='35' />";
                 }
             }}
             , {field: 'deployStatus', caption: '배포상태', size: '100px', 
                 render: function(record) {
                     if ( record.deployStatus == 'DEPLOY_STATUS_PROCESSING' )
                         return '<span class="btn btn-primary" style="width:60px">배포중</span>';
                     else if ( record.deployStatus == 'DEPLOY_STATUS_DONE' )
                         return '<span class="btn btn-primary" style="width:60px">성공</span>';
                     else    if ( record.deployStatus == 'DEPLOY_STATUS_CANCELLED' )
                         return '<span class="btn btn-danger" style="width:60px">취소</span>';
                     else    if ( record.deployStatus == 'DEPLOY_STATUS_FAILED' )
                         return '<span class="btn btn-danger" style="width:60px">실패</span>';
                     else    if ( record.deployStatus == 'DEPLOY_STATUS_DELETING' )
                         return '<span class="btn btn-primary" style="width:60px">삭제중</span>';
                     else
                         return '&ndash;';
                        }
               }
             , {field: 'deployLog', caption: '배포로그', size: '100px',
                 render: function(record) {
                     if ( (record.deployStatus == 'DEPLOY_STATUS_DONE' || record.deployStatus == 'DEPLOY_STATUS_FAILED') && record.deployLog != null ) {
                            return '<span id="" class="btn btn-primary" style="width:60px" onClick="getHbDeployLogMsg( \''+record.id+'\',\''+record.iaasType+'\');">로그보기</span>';
                     } else {
                         return '&ndash;';
                     }
                 }
               }
             , {field: 'networkConfigVo.subnetId', caption: '네트워크 ID', size: '200px'}
             , {field: 'networkConfigVo.subnetRange', caption: '서브넷 범위', size: '100px'}
             , {field: 'networkConfigVo.publicStaticIp', caption: '디렉터 공인 IP', size: '100px'}
             , {field: 'networkConfigVo.privateStaticIp', caption: '디렉터 내부 IP', size: '100px'}
             , {field: 'networkConfigVo.subnetGateway', caption: '게이트웨이', size: '100px'}
             , {field: 'networkConfigVo.subnetDns', caption: 'DNS', size: '100px'}
             , {field: 'defaultConfigVo.ntp', caption: 'NTP', size: '100px'}
             , {field: 'resourceConfigVo.stemcellName', caption: '스템셀', size: '340px'}
             , {field: 'resourceConfigVo.instanceType', caption: '인스턴스 유형', size: '100px'}
             , {field: 'resourceConfigVo.vmPassword', caption: 'VM 비밀번호', size: '100px'}
             , {field: 'deploymentFile', caption: '배포파일명', size: '250px',
                 render: function(record) {
                     if ( record.deploymentFile != null ){
                         var deplymentParam = {
                                 service : "bootstrap"
                                ,iaas    : record.iaas
                                ,id      : record.id
                             } 
                         var fileName = record.deploymentFile;
                         return '<a style="color:#333;" href="/common/deploy/download/manifest/' + fileName +'" onclick="window.open(this.href); return false;">' + record.deploymentFile + '</a>';
                   }else {
                        return '&ndash;';
                      }
                  }
              }
             ],
         onSelect : function(event) {
             event.onComplete = function() {
                 $('#modifyVmBtn').attr('disabled', false);
                 $('#deleteVmBtn').attr('disabled', false);
                 return;
             }
         },onDblClick: function (event) {
             var grid = this;
             // need timer for nicer visual effect that record was selected
             setTimeout(function () {
                 w2ui['config_cfGrid2'].add( $.extend({}, grid.get(event.recid), { selected : false }) );
                 grid.selectNone();
                 grid.remove(event.recid);
             }, 150);
         },onUnselect : function(event) {
             event.onComplete = function() {
                 $('#modifyVmBtn').attr('disabled', true);
                 $('#deleteVmBtn').attr('disabled', true);
                 return;
             }
         },onLoad:function(event){
             if(event.xhr.status == 403){
                 location.href = "/abuse";
                 event.preventDefault();
             }
         },onError : function(event) {
         }
     });
    
    /******************************************************************
     * 설명 : BootStrap 설치 버튼
     ***************************************************************** */
     $("#installBtn").click(function(){
         w2popup.open({
            width   : 730,
            height  : 460,
            title : '<b>이종 CF 정보 등록</b>',
            body : $("#cfRegistInfoDiv").html(),
            buttons: $("#cfRegistInfoBtnDiv").html(),
            modal : true,
            onOpen:function(event){
                event.onComplete = function(){
                    getCfInstanceInfo();
                    getCfResourceInfo();
                    getCfKeyInfo();
                    getCfNetworkInfo();
                    getCfDefaultInfo();
                }
            },onClose:function(event){
                w2ui['config_cfGrid'].clear();
                doSearch();
            }
        });
     });
     
     /******************************************************************
     * 설명 : BootStrap 수정 버튼
     ***************************************************************** */
    $("#modifyBtn").click(function(){
        if($("#modifyBtn").attr('disabled') == "disabled") return;
        
        var selected = w2ui['config_cfGrid'].getSelection();
        if( selected.length == 0 ){
            w2alert("선택된 정보가 없습니다.", "CF 등록 정보 수정");
            return;
        }
        var record = w2ui['config_cfGrid'].get(selected);
        cfInfo = record;
        w2popup.open({
            width   : 730,
            height  : 460,
            title : '<b>이종 CF 정보 수정</b>',
            body : $("#cfRegistInfoDiv").html(),
            buttons: $("#cfRegistInfoBtnDiv").html(),
            modal : true,
            onOpen:function(event){
                event.onComplete = function(){
                    $(".w2ui-msg-body input[name='cfInfoId']").val(record.id)
                    $(".w2ui-msg-body input[name='cfConfigName']").val(record.bootstrapConfigName)
                    $(".w2ui-msg-body select[name='iaasType']").val(record.iaasType)
                    getCfInstanceInfo();
                    getCfResourceInfo();
                    getCfKeyInfo();
                    getCfNetworkInfo();
                    getCfDefaultInfo();
                }
            },onClose:function(event){
                w2ui['config_cfGrid'].clear();
                doSearch();
            }
        });
     });
     
     /******************************************************************
     * 설명 : BootStrap 삭제 버튼
     ***************************************************************** */
    $("#deleteBtn").click(function(){
        if($("#deleteBtn").attr('disabled') == "disabled") return;
        
        var selected = w2ui['config_cfGrid'].getSelection();
        var record = w2ui['config_cfGrid'].get(selected);
        var message = "";
        
        if ( record.bootstrapConfigName ){
            message = "CF 정보 " + record.bootstrapConfigName + ")를 삭제하시겠습니까?";
        }else message = "선택된 CF을 삭제하시겠습니까?";
        
        w2confirm({
            title        : "CF 삭제",
            msg          : message,
            yes_text     : "확인",
            yes_callBack : function(event){
                deleteBootstrapInfo(record);
            },
            no_text : "취소",
            no_callBack : function(event){
                w2ui['config_cfGrid'].clear();
                doSearch();
            }
        });
     });
    /******************************************************************
     * 설명 : BootStrap 설치 버튼
     ***************************************************************** */
    $("#installVmBtn").click(function(){
        if($("#installVmBtn").attr('disabled') == "disabled") return;
        
        var selectAll = w2ui['config_cfGrid2'].selectAll();

        var selected = w2ui['config_cfGrid2'].getSelection();
        
        if(selected.length == 3) {
            w2alert("최대 2개의 CF이 설치 가능 합니다. ", "CF 설치");
            return;
        }else if(selected.length == 0){
            w2alert("설치할 CF이 없습니다.", "CF 설치");
            return;
        }
        
        var record = new Array();
        
        for(var i=0; i<selected.length; i++){
            record.push(w2ui['config_cfGrid2'].get(selected[i]));
            createSettingFile(record[i]);
        }
        if(record == ""){
            w2alert("배포할 CF 이 존재하지 않음");
        }else{
            firstInstallPopup(record);
        }
    });
    
    /******************************************************************
     * 설명 : BootStrap 삭제 버튼
     ***************************************************************** */
    $("#deleteVmBtn").click(function(){
        if($("#deleteVmBtn").attr('disabled') == "disabled") return;
        
        var selected = w2ui['config_cfGrid3'].getSelection();
        var record = w2ui['config_cfGrid3'].get(selected);
        var message = "";
        
        if ( record.bootstrapConfigName ){
            message = "CF 정보 " + record.bootstrapConfigName + ")를 삭제하시겠습니까?";
        }else message = "선택된 CF을 삭제하시겠습니까?";
        
        w2confirm({
            title        : "CF 삭제",
            msg          : message,
            yes_text     : "확인",
            yes_callBack : function(event){
                deleteBootstrapVmInfo(record);
            },
            no_text : "취소",
            no_callBack : function(event){
                w2ui['config_cfGrid3'].clear();
                doSearch();
            }
        });
     });
    
    doSearch();
});

/******************************************************************
 * 설명 : BootStrap 수정 버튼
 ***************************************************************** */
 $("#modifyVmBtn").click(function(){
     if($("#modifyVmBtn").attr('disabled') == "disabled") return;
     
     var selected = w2ui['config_cfGrid3'].getSelection();
     if( selected.length == 0 ){
         w2alert("선택된 정보가 없습니다.", "CF 등록 정보 수정");
         return;
     }
     var record = w2ui['config_cfGrid3'].get(selected);
     cfInfo = record;
     w2popup.open({
         width   : 730,
         height  : 460,
         title : '<b>이종 CF 정보 수정</b>',
         body : $("#cfModifyRegistInfoDiv").html(),
         buttons: $("#cfModifyRegistInfoBtnDiv").html(),
         modal : true,
         onOpen:function(event){
             event.onComplete = function(){
                $(".w2ui-msg-body input[name='bootstrapInfoId']").val(record.id)
                $(".w2ui-msg-body input[name='bootstrapConfigName']").val(record.bootstrapConfigName)
                $(".w2ui-msg-body select[name='iaasType']").val(record.iaasType)
                getCfInstanceInfo();
                getCfResourceInfo();
                getCfKeyInfo();
                getCfNetworkInfo();
                getCfDefaultInfo();
             }
         },onClose:function(event){
             w2ui['config_cfGrid3'].clear();
             doSearch();
         }
     });
  });

/******************************************************************
 * 기능 : privateInstallPopup
 * 설명 : Private Type Boostrap 설치
 ***************************************************************** */
var bootstrapInstallSocket = null;
function firstInstallPopup(bootstrapInfo){
    console.log(bootstrapInfo);
    
    var firstDeploy = bootstrapInfo[0];
    
    if(!lockFileSet(firstDeploy.bootstrapConfigName)) return;
    var message = firstDeploy.iaasType + " CF ";
    var requestParameter = {
           id : firstDeploy.id,
           iaasType: firstDeploy.iaasType
    };
    w2popup.open({
        title   : "<b>"+firstDeploy.iaasType.toUpperCase()+" 클라우드 환경 CF 설치</b>",
        width   : 800,
        height  : 620,
        modal   : true,
        showMax : true,
        body    : $("#InstallDiv1").html(),
        buttons : $("#InstallDivButtons").html(),
        onOpen : function(event){
            event.onComplete = function(){
                if(bootstrapInstallSocket != null) bootstrapInstallSocket = null;
                if(installClient != null) installClient = null;
                bootstrapInstallSocket = new SockJS('/deploy/hbBootstrap/install/bootstrapInstall');
                installClient = Stomp.over(bootstrapInstallSocket);
                installClient.connect({}, function(frame) {
                    installClient.subscribe('/user/deploy/hbBootstrap/install/logs', function(data){
                        var installLogs = $(".w2ui-msg-body #installLogs");
                        var response = JSON.parse(data.body);
                        if ( response.messages != null ){
                            for ( var i=0; i < response.messages.length; i++) {
                                installLogs.append(response.messages[i] + "\n").scrollTop( installLogs[0].scrollHeight );
                            }
                            if ( response.state.toLowerCase() != "started" ) {
                                if ( response.state.toLowerCase() == "done" )    message = message + " 설치가 완료되었습니다."; 
                                if ( response.state.toLowerCase() == "error" ) message = message + " 설치 중 오류가 발생하였습니다.";
                                if ( response.state.toLowerCase() == "cancelled" ) message = message + " 설치 중 취소되었습니다.";
                                
                                installStatus = response.state.toLowerCase();
                                $('.w2ui-msg-buttons #deployPopupBtn').prop("disabled", false);
                                
                                if(bootstrapInfo.length == 2){
                                    installClient.disconnect(secondInstallPopup(bootstrapInfo[1]));
                                }else{
                                    installClient.disconnect();
                                }
                                w2alert(message, "BOOTSTRAP 설치");
                            }
                        }
                    });
                    installClient.send('/send/deploy/hbBootstrap/install/bootstrapInstall', {}, JSON.stringify(requestParameter));
                });
            }
        }, onClose : function(event){
               event.onComplete = function(){
                   w2ui['config_cfGrid2'].clear();
                   if( installClient != ""  ){
                       installClient.disconnect();
                   }
                   popupClose();
               }
           }
    });
}
/******************************************************************
 * 기능 : InstallPopup1
 * 설명 : Boostrap 설치1
 ***************************************************************** */
function secondInstallPopup(bootstrapInfo){
    if(installStatus != "done") return;
    if(!lockFileSet(bootstrapInfo.deploymentFile)) return;
    
    var message = bootstrapInfo.iaasType+" BOOTSTRAP ";
    var requestParameter = {
           id : bootstrapInfo.id,
           iaasType: bootstrapInfo.iaasType
    };
    w2popup.open({
        title   : "<b>"+ bootstrapInfo.iaasType.toUpperCase()+" 클라우드 환경 CF 설치</b>",
        width   : 800,
        height  : 620,
        modal   : true,
        showMax : true,
        body    : $("#InstallDiv2").html(),
        buttons : $("#InstallDivButtons").html(),
        onOpen : function(event){
            event.onComplete = function(){
                if(bootstrapInstallSocket != null) bootstrapInstallSocket = null;
                if(installClient != null) installClient = null;
                bootstrapInstallSocket = new SockJS('/deploy/hbBootstrap/install/bootstrapInstall');
                installClient = Stomp.over(bootstrapInstallSocket);
                installClient.connect({}, function(frame) {
                    installClient.subscribe('/user/deploy/hbBootstrap/install/logs', function(data){
                        var installLogs = $(".w2ui-msg-body #installLogs");
                        var response = JSON.parse(data.body);
                        if ( response.messages != null ){
                            for ( var i=0; i < response.messages.length; i++) {
                                installLogs.append(response.messages[i] + "\n").scrollTop( installLogs[0].scrollHeight );
                            }
                            if ( response.state.toLowerCase() != "started" ) {
                                if ( response.state.toLowerCase() == "done" ) message = message + " 설치가 완료되었습니다."; 
                                if ( response.state.toLowerCase() == "error" ) message = message + " 설치 중 오류가 발생하였습니다.";
                                if ( response.state.toLowerCase() == "cancelled" ) message = message + " 설치 중 취소되었습니다.";
                                
                                installStatus = response.state.toLowerCase();
                                $('.w2ui-msg-buttons #deployPopupBtn').prop("disabled", false);
                                    
                                installClient.disconnect();
                                w2alert(message, "BOOTSTRAP 설치");
                            }
                        }
                    });
                    installClient.send('/send/deploy/hbBootstrap/install/bootstrapInstall', {}, JSON.stringify(requestParameter));
                });
            }
        }, onClose : function(event){
               event.onComplete = function(){
                   w2ui['config_cfGrid2'].clear();
                   if( installClient != ""  ){
                       installClient.disconnect();
                   }
                   popupClose();
               }
           }
    });
}

/******************************************************************
 * 기능 : lockFileSet
 * 설명 : Lock 파일 생성
 ***************************************************************** */
var lockFile = false;
function lockFileSet(deployFile){
    if(!checkEmpty(deployFile) ){
        var FileName = "hybird_cf";
        var message = "현재 다른 설치 관리자가 CF를 사용 중 입니다.";
        lockFile = commonLockFile("<c:url value='/common/deploy/lockFile/"+FileName+"'/>",message);
    }
    return lockFile;
}


/******************************************************************
 * 기능 : getCfDefaultInfo
 * 설명 : CF 기본 정보 조회
 ***************************************************************** */
function getCfDefaultInfo(){
    $.ajax({
        type : "GET",
        url : "/deploy/hbCf/default/list",
        contentType : "application/json",
        async : true,
        success : function(data, status) {
            if( data.length == 0 ){
                return;
            }
            var options = "<option value=''>CF 기본 정보를 선택하세요.</option>";
            for( var i=0; i<data.records.length; i++ ){
                if( data.records[i].defaultConfigName == cfInfo.defaultConfigInfo ){
                    options += "<option value='"+data.records[i].defaultConfigName+"' selected>"+data.records[i].defaultConfigName+"</option>";
                }else options += "<option value='"+data.records[i].defaultConfigName+"'>"+data.records[i].defaultConfigName+"</option>";
            }
            $("select[name='defaultConfigInfo']").html(options);
        },
        error : function( e, status ) {
            w2alert("CF Default 정보 "+search_data_fail_msg, "CF 설치");
        }
    });
}

/******************************************************************
 * 기능 : getCfNetworkInfo
 * 설명 : CF 네트워크 정보 조회
 ***************************************************************** */
function getCfNetworkInfo(){
    $.ajax({
        type : "GET",
        url : "/deploy/hbCf/network/list",
        contentType : "application/json",
        async : true,
        success : function(data, status) {
            if( data.length == 0 ){
                return;
            }
            var options = "<option value=''>CF 네트워크 정보를 선택하세요.</option>";
            for( var i=0; i<data.records.length; i++ ){
                if( data.records[i].networkConfigName == cfInfo.networkConfigInfo ){
                    options += "<option value='"+data.records[i].networkConfigName+"' selected>"+data.records[i].networkConfigName+"</option>";
                }else options += "<option value='"+data.records[i].networkConfigName+"'>"+data.records[i].networkConfigName+"</option>";
            }
            $("select[name='networkConfigInfo']").html(options);
        },
        error : function( e, status ) {
            w2alert("네트워크 기본 정보 "+search_data_fail_msg, "CF 설치");
        }
    });
}

/******************************************************************
 * 기능 : getCfKeyInfo
 * 설명 : CF Key 정보 조회
 ***************************************************************** */
function getCfKeyInfo(){
    $.ajax({
        type : "GET",
        url : "/deploy/hbCf/key/list",
        contentType : "application/json",
        async : true,
        success : function(data, status) {
            if( data.length == 0 ){
                return;
            }
            var options = "<option value=''>CF Key 정보를 선택하세요.</option>";
            for( var i=0; i<data.records.length; i++ ){
                if( data.records[i].keyConfigName == cfInfo.keyConfigInfo ){
                    options += "<option value='"+data.records[i].keyConfigName+"' selected>"+data.records[i].keyConfigName+"</option>";
                }else options += "<option value='"+data.records[i].keyConfigName+"'>"+data.records[i].keyConfigName+"</option>";
            }
            $("select[name='keyConfigInfo']").html(options);
        },
        error : function( e, status ) {
            w2alert("CF Key 정보 "+search_data_fail_msg, "CF 설치");
        }
    });
}

/******************************************************************
 * 기능 : getCfResourceInfo
 * 설명 : CF 리소스 정보 조회
 ***************************************************************** */
function getCfResourceInfo(){
    $.ajax({
        type : "GET",
        url : "/deploy/hbCf/resource/list",
        contentType : "application/json",
        async : true,
        success : function(data, status) {
            if( data.length == 0 ){
                return;
            }
            var options = "<option value=''>CF 리소스 정보를 선택하세요.</option>";
            for( var i=0; i<data.records.length; i++ ){
                if( data.records[i].resourceConfigName == cfInfo.resourceConfigInfo ){
                    options += "<option value='"+data.records[i].resourceConfigName+"' selected>"+data.records[i].resourceConfigName+"</option>";
                }else options += "<option value='"+data.records[i].resourceConfigName+"'>"+data.records[i].resourceConfigName+"</option>";
            }
            $("select[name='resourceConfigInfo']").html(options);
        },
        error : function( e, status ) {
            w2alert("CF 리소스 정보 "+search_data_fail_msg, "CF 설치");
        }
    });
}

/******************************************************************
 * 기능 : getCfInstanceInfo
 * 설명 : CF 인스턴스 정보 조회
 ***************************************************************** */
function getCfInstanceInfo(){
    $.ajax({
        type : "GET",
        url : "/deploy/hbCf/instance/list",
        contentType : "application/json",
        async : true,
        success : function(data, status) {
            if( data.length == 0 ){
                return;
            }
            var options = "<option value=''>CF 인스턴스 정보를 선택하세요.</option>";
            for( var i=0; i<data.records.length; i++ ){
                if( data.records[i].instanceConfigName == cfInfo.instanceConfigInfo ){
                    options += "<option value='"+data.records[i].instanceConfigName+"' selected>"+data.records[i].instanceConfigName+"</option>";
                }else options += "<option value='"+data.records[i].instanceConfigName+"'>"+data.records[i].instanceConfigName+"</option>";
            }
            $("select[name='instanceConfigInfo']").html(options);
        },
        error : function( e, status ) {
            w2alert("CF 인스턴스 정보 "+search_data_fail_msg, "CF 설치");
        }
    });
}

/******************************************************************
 * 기능 : saveBootstrapInfo
 * 설명 : Bootstrap 정보 저장
 ***************************************************************** */
function saveBootstrapInfo(){
    w2popup.lock( save_lock_msg, true); 
    cfInfo = {
        id                     : $(".w2ui-msg-body input[name='cfInfoId']").val(),
        cfConfigName           : $(".w2ui-msg-body input[name='cfConfigName']").val(),
        iaasType               : $(".w2ui-msg-body select[name='iaasType']").val(),
        networkConfigInfo      : $(".w2ui-msg-body select[name='networkConfigInfo']").val(),
        keyConfigInfo          : $(".w2ui-msg-body select[name='keyConfigInfo']").val(),
        defaultConfigInfo      : $(".w2ui-msg-body select[name='defaultConfigInfo']").val(),
        resourceConfigInfo     : $(".w2ui-msg-body select[name='resourceConfigInfo']").val(),
        instanceConfigInfo     : $(".w2ui-msg-body select[name='instanceConfigInfo']").val()
    }
    
    $.ajax({
        type : "PUT",
        url : "/deploy/hbCf/install/save",
        contentType : "application/json",
        async : true,
        data : JSON.stringify(cfInfo),
        success : function(status) {
            // ajax가 성공할때 처리...
            w2popup.unlock();
            w2popup.close();
            w2ui['config_cfGrid'].clear();
            w2ui['config_cfGrid2'].clear();
            doSearch();
        },
        error : function(request, status, error) {
            // ajax가 실패할때 처리...
            w2popup.unlock();
            w2ui['config_cfGrid'].clear();
            w2ui['config_cfGrid2'].clear();
            doSearch();
            w2popup.close();
            var errorResult = JSON.parse(request.responseText);
            w2alert(errorResult.message);
        }
    });
}

/******************************************************************
 * 기능 : deleteBootstrapInfo
 * 설명 : Bootstrap 정보 삭제
 ***************************************************************** */
function deleteBootstrapInfo(record){
    cfInfo = {
            id                     : record.id,
            bootstrapConfigName    : record.bootstrapConfigName
    }
    $.ajax({
        type : "DELETE",
        url : "/deploy/hbBootstrap/delete/data",
        contentType : "application/json",
        async : true,
        data : JSON.stringify(cfInfo),
        success : function(status) {
            // ajax가 성공할때 처리...
            w2popup.unlock();
            w2popup.close();
            w2ui['config_cfGrid'].clear();
            w2ui['config_cfGrid2'].clear();
            doSearch();
        },
        error : function(request, status, error) {
            // ajax가 실패할때 처리...
            w2popup.unlock();
            w2ui['config_cfGrid'].clear();
            doSearch();
            w2popup.close();
            var errorResult = JSON.parse(request.responseText);
            w2alert(errorResult.message);
        }
    });
}

/******************************************************************
 * 기능 : deleteBootstrapVmInfo
 * 설명 : Bootstrap VM 삭제
 ***************************************************************** */
function deleteBootstrapVmInfo(record){
    var requestParameter = {
            id:record.id,
            iaasType:record.iaasType
    };
    if ( record.deployStatus == null || record.deployStatus == '' ) {
        // 단순 레코드 삭제
        var url = "/deploy/bootstrap/delete/data";
        $.ajax({
            type : "DELETE",
            url : url,
            data : JSON.stringify(requestParameter),
            contentType : "application/json",
            success : function(data, status) {
                bootStrapDeploymentName = [];
                gridReload();
            },
            error : function(request, status, error) {
                var errorResult = JSON.parse(request.responseText);
                w2alert(errorResult.message, "BOOTSTRAP 삭제");
            }
        });
    } else {
        if(!lockFileSet(record.deploymentFile)) return;
        var message = "BOOTSTRAP";
        var body = '<textarea id="deleteLogs" style="width:95%;height:90%;overflow-y:visible;resize:none;background-color: #FFF; margin:2%" readonly="readonly"></textarea>';
        
        w2popup.open({
            width   : 700,
            height  : 500,
            title   : "<b>BOOTSTRAP 삭제</b>",
            body    : body,
            buttons : '<button class="btn" style="float: right; padding-right: 15%;" onclick="popupComplete();">닫기</button>',
            modal   : true,
            showMax : true,
            onOpen  : function(event){
                event.onComplete = function(){
                    var socket = new SockJS('/deploy/hbBootstrap/delete/instance');
                    deleteClient = Stomp.over(socket);
                     deleteClient.connect({}, function(frame) {
                        deleteClient.subscribe('/user/deploy/hbBootstrap/delete/logs', function(data){
                            
                            var deleteLogs = $(".w2ui-msg-body #deleteLogs");
                            var response = JSON.parse(data.body);
                            
                            if ( response.messages != null ) {
                                   for ( var i=0; i < response.messages.length; i++) {
                                       deleteLogs.append(response.messages[i] + "\n").scrollTop( deleteLogs[0].scrollHeight );
                                   }
                                   if ( response.state.toLowerCase() != "started" ) {
                                    if ( response.state.toLowerCase() == "done" )    message = message + " 삭제가 완료되었습니다."; 
                                    if ( response.state.toLowerCase() == "error" ) message = message + " 삭제 중 오류가 발생하였습니다.";
                                    if ( response.state.toLowerCase() == "cancelled" ) message = message + " 삭제 중 취소되었습니다.";
                                    
                                    installStatus = response.state.toLowerCase();
                                    deleteClient.disconnect();
                                    w2alert(message, "BOOTSTRAP 삭제");
                                   }
                            }
                        });
                        deleteClient.send('/send/deploy/hbBootstrap/delete/instance', {}, JSON.stringify(requestParameter));
                    });
                }
            }, onClose : function (event){
                event.onComplete= function(){
                    bootStrapDeploymentName = [];
                    w2ui['config_cfGrid3'].clear();
                    if( deleteClient != ""  ){
                        deleteClient.disconnect();
                    }
                    popupClose();
                }
            } 
        });
    }
}

/******************************************************************
 * 기능 : 팝업창 닫을 경우
 * 설명 : popupClose
 ***************************************************************** */
function popupClose() {
   //grid Reload
   doSearch();
   doButtonStyle();
}


/******************************************************************
 * 기능 : createSettingFile
 * 설명 : 배포 파일 생성
 ***************************************************************** */
function createSettingFile(data){
    console.log('create'+data);
    deploymentInfo = {
            iaasType       : data.iaasType,
            id : data.id
    }
    $.ajax({
        type : "POST",
        url : "/deploy/hbBootstrap/install/createSettingFile",
        contentType : "application/json",
        async : true,
        data : JSON.stringify(deploymentInfo),
        success : function(status) {
            getDeployInfo(data.deploymentFile);
        },
        error :function(request, status, error) {
            var errorResult = JSON.parse(request.responseText);
            w2alert(errorResult.message, "BOOTSTRAP 배포 파일 생성");
        }
    });
}

/******************************************************************
 * 기능 : getDeployInfo
 * 설명 : Manifest 파일 내용 출력
 ***************************************************************** */
function getDeployInfo(deployFileName){
    console.log(deployFileName);
    $.ajax({
        type : "GET",
        url :"/common/use/deployment/"+deployFileName,
        contentType : "application/json",
        async : true,
        success : function(data, status) {
            if(status == "success"){
                $(".w2ui-msg-body #deployInfo").text(data);
            }
        },
        error : function( e, status ) {
            w2alert("Temp 파일을 가져오는 중 오류가 발생하였습니다. ", "BOOTSTRAP 설치");
        }
    });
}

/******************************************************************
 * 기능 : doSearch
 * 설명 : Bootstrap 목록 조회
 ***************************************************************** */
function doSearch() {
    //doButtonStyle();
    cfInfo = "";
    w2ui['config_cfGrid'].load("<c:url value='/deploy/hbCf/list/installAble'/>",
            function (){ doButtonStyle(); });
    w2ui['config_cfGrid3'].load("<c:url value='/deploy/hbCf/list/installed'/>");
}
 /******************************************************************
  * 기능 : doButtonStyle
  * 설명 : Button 제어
  ***************************************************************** */
function doButtonStyle(){
    //Button Style init
    $('#modifyBtn').attr('disabled', true);
    $('#deleteBtn').attr('disabled', true);
    $('#deleteVmBtn').attr('disabled', true);
    $('#modifyVmBtn').attr('disabled', true);
}
 
/******************************************************************
 * 기능 : getDeployLogMsg
 * 설명 : 설치 로그 조회
 ***************************************************************** */
function getHbDeployLogMsg(id,iaas){
    $.ajax({
        type        : "GET",
        url         : "/deploy/hbBootstrap/list/"+id+"/"+iaas,
        contentType : "application/json",
        success     : function(data, status){
            if(!checkEmpty(data)) {
                deployLogMsgPopup(data);
            } else {
                w2alert("배포 로그가 존재 하지 않습니다.",  "BOOTSTRAP 배포로그");
            }
        },
        error : function(request, status, error) {
            var errorResult = JSON.parse(request.responseText);
            w2alert(errorResult.message, "BOOTSTRAP 배포로그");
        }
    });
}

/******************************************************************
 * 기능 : deployLogMsgPopup
 * 설명 : 배포 로그 팝업창
 ***************************************************************** */
function deployLogMsgPopup(msg){
    var body = '<textarea id="deployLogMsg" style="margin-left:2%;width:95%;height:93%;overflow-y:visible;resize:none;background-color: #FFF; margin:2%" readonly="readonly"></textarea>';
    
    w2popup.open({
        width   : 800,
        height  : 700,
        title   : "<b>BOOTSTRAP 배포로그"+"</b>",
        body    : body,
        buttons : '<button class="btn" style="float: right; padding-right: 15%;" onclick="w2popup.close();">닫기</button>',
        showMax : true,
        onOpen  : function(event){
            event.onComplete = function(){
                $("#deployLogMsg").text(msg);
            }
        }
    });    
}


 /******************************************************************
  * 기능 : clearMainPage
  * 설명 : 다른페이지 이동시 Bootstrap Grid clear
  ***************************************************************** */
function clearMainPage() {
    $().w2destroy('config_cfGrid');
    $().w2destroy('config_cfGrid2');
    $().w2destroy('config_cfGrid3');
}

 /******************************************************************
  * 설명 : 화면 리사이즈시 호출 
  ***************************************************************** */
$( window ).resize(function() {
    setLayoutContainerHeight();
});

/******************************************************************
 * 기능 : popupComplete
 * 설명 : 설치 화면 닫기
 ***************************************************************** */
function popupComplete(){
    var msg;
    if(installStatus == "done" || installStatus == "error"){
        msg = $(".w2ui-msg-title b").text() + " 화면을 닫으시겠습니까?";
    }else{
        msg = $(".w2ui-msg-title b").text() + " 화면을 닫으시겠습니까?<BR>(닫은 후에도 완료되지 않는 설치 또는 삭제 작업은 계속 진행됩니다.)";
    }
    w2confirm({
        title   : $(".w2ui-msg-title b").text(),
        msg     : msg,
        yes_text: "확인",
        yes_callBack : function(envent){
            popupClose();
            w2popup.close();
        },
        no_text : "취소"
    });
}
</script>

<div id="main">
    <div class="page_site">이종 CF 설치 > <strong>이종 CF 설치</strong></div>
    <!-- BOOTSTRAP 목록-->
    <div class="pdt20"> 
        <div class="title fl">배포 가능 한 Private/Public CF 목록 (더블 클릭) </div>
        <div class="fr"> 
            <sec:authorize access="hasAuthority('CONFIG_DIRECTOR_MENU')">
            <span id="installBtn" class="btn btn-primary"  style="width:120px">정보 등록</span>
            </sec:authorize>
            &nbsp;
            <sec:authorize access="hasAuthority('CONFIG_DIRECTOR_MENU')">
            <span id="modifyBtn" class="btn btn-info" style="width:120px">정보 수정</span>
            </sec:authorize>
            &nbsp;
            <sec:authorize access="hasAuthority('CONFIG_DIRECTOR_MENU')">
            <span id="deleteBtn" class="btn btn-danger" style="width:120px">정보 삭제</span>
            </sec:authorize>
        </div>
    </div>
    <div id="config_cfGrid" style="width:100%; height:300px"></div>
    
    
    <div class="pdt20"> 
        <div class="title fl">배포 할 Private/Public CF 목록 (더블 클릭)</div>
        <div class="fr"> 
            <sec:authorize access="hasAuthority('CONFIG_DIRECTOR_MENU')">
            <span id="installVmBtn" class="btn btn-primary"  style="width:120px">VM 설치</span>
            </sec:authorize>
        </div>
    </div>
    <div id="config_cfGrid2" style="width:100%; height:300px"></div>
    
    <div class="pdt20"> 
        <div class="title fl">배포 한 Private/Public CF 목록 </div>
        <div class="fr">
            <sec:authorize access="hasAuthority('CONFIG_DIRECTOR_MENU')">
            <span id="modifyVmBtn" class="btn btn-info"  style="width:120px">VM 수정</span>
            </sec:authorize>
            <sec:authorize access="hasAuthority('CONFIG_DIRECTOR_MENU')">
            <span id="deleteVmBtn" class="btn btn-danger"  style="width:120px">VM 삭제</span>
            </sec:authorize>
        </div>
    </div>
    <div id="config_cfGrid3" style="width:100%; height:300px"></div>
</div>

<div id="cfRegistInfoDiv" style="width:100%;height:100%;" hidden="true">
    <form id="settingForm" action="POST">
    <input class="form-control" name = "cfInfoId" type="hidden"/>
        <div class="w2ui-page page-0" style="margin-top:10px;padding:0 3%;">
            <div class="panel panel-info"> 
                <div class="panel-heading" style = "text-align: left; font-size:15px;"><b>이종 CF 설치 정보</b></div>
                <div class="panel-body" style="padding:5px 5% 7px 5%;">
                   <div class="w2ui-field">
                       <label style="width:40%; text-align: left;padding-left: 20px;">CF 정보 별칭</label>
                       <div>
                           <input class="form-control" name = "cfConfigName" type="text"  maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="CF 정보 별칭을 입력 하세요."/>
                       </div>
                   </div>
                  <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">클라우드 인프라 환경</label>
                       <div>
                           <select class="form-control" onchange="" name="iaasType" style="width: 320px; margin-left: 20px;">
                               <option value="">인프라 환경을 선택하세요.</option>
                               <option value="aws">AWS</option>
                               <option value="openstack">Openstack</option>
                           </select>
                       </div>
                   </div>
                   <div class="w2ui-field">
                      <label style="width:40%;text-align: left;padding-left: 20px;">CF 기본 정보 별칭</label>
                      <div style="width: 60%">
                          <select class="form-control" name="defaultConfigInfo" onchange="" style="width: 320px; margin-left: 20px;">
                              <option value="">CF 기본 정보를 선택하세요.</option>
                          </select>
                      </div>
                   </div>
                   <div class="w2ui-field">
                      <label style="width:40%;text-align: left;padding-left: 20px;">CF 네트워크 정보 별칭</label>
                      <div style="width: 60%">
                          <select class="form-control" name="networkConfigInfo" onchange="" style="width: 320px; margin-left: 20px;">
                              <option value="">CF 네트워크 정보를 선택하세요.</option>
                          </select>
                      </div>
                    </div>
                    <div class="w2ui-field">
                      <label style="width:40%;text-align: left;padding-left: 20px;">CF Key 정보 별칭</label>
                      <div style="width: 60%">
                          <select class="form-control" name="keyConfigInfo" onchange="" style="width: 320px; margin-left: 20px;">
                              <option value="">CF Key 정보를 선택하세요.</option>
                          </select>
                      </div>
                    </div>
                    <div class="w2ui-field">
                      <label style="width:40%;text-align: left;padding-left: 20px;">CF 리소스 정보 별칭</label>
                      <div style="width: 60%">
                          <select class="form-control" name="resourceConfigInfo" onchange="" style="width: 320px; margin-left: 20px;">
                              <option value="">CF 리소스 정보를 선택하세요.</option>
                          </select>
                      </div>
                    </div>
                    
                    <div class="w2ui-field">
                      <label style="width:40%;text-align: left;padding-left: 20px;">CF 인스턴스 정보 별칭</label>
                      <div style="width: 60%">
                          <select class="form-control" name="instanceConfigInfo" onchange="" style="width: 320px; margin-left: 20px;">
                              <option value="">CF 인스턴스 정보를 선택하세요.</option>
                          </select>
                      </div>
                    </div>
                    
                </div>
            </div>
        </div>
        <div class="w2ui-buttons" id="cfRegistInfoBtnDiv" hidden="true">
            <button class="btn" id="registBootstrapInfoBtn" onclick="$('#settingForm').submit();">확인</button>
            <button class="btn" id="popClose" onclick="w2popup.close();">취소</button>
        </div>
    </form>
</div>

<div id="cfModifyRegistInfoDiv" style="width:100%;height:100%;" hidden="true">
    <form id="settingModifyForm" action="POST">
    <input class="form-control" name = "bootstrapInfoId" type="hidden"/>
        <div class="w2ui-page page-0" style="margin-top:10px;padding:0 3%;">
            <div class="panel panel-info"> 
                <div class="panel-heading" style = "text-align: left; font-size:15px;"><b>이종 BOOTSTRAP 설치 정보</b></div>
                <div class="panel-body" style="padding:5px 5% 7px 5%;">
                   <div class="w2ui-field">
                       <label style="width:40%; text-align: left;padding-left: 20px;">BOOTSTRAP 정보 별칭</label>
                       <div>
                           <input class="form-control" name = "bootstrapConfigName" type="text"  maxlength="100" style="width: 320px; margin-left: 20px;" placeholder="BOOTSTRAP 정보 별칭을 입력 하세요."/>
                       </div>
                   </div>
                  <div class="w2ui-field">
                       <label style="width:40%;text-align: left;padding-left: 20px;">클라우드 인프라 환경</label>
                       <div>
                           <select class="form-control" onchange="" name="iaasType" style="width: 320px; margin-left: 20px;">
                               <option value="">수정할 인프라 환경을 선택하세요.</option>
                               <option value="aws">AWS</option>
                               <option value="openstack">Openstack</option>
                           </select>
                       </div>
                   </div>
                   <div class="w2ui-field">
                      <label style="width:40%;text-align: left;padding-left: 20px;">BOOTSTRAP CPI 정보 별칭</label>
                      <div style="width: 60%">
                          <select class="form-control" name="cpiConfigInfo" onchange="" style="width: 320px; margin-left: 20px;">
                              <option value="">수정할 BOOTSTRAP CPI 정보를 선택하세요.</option>
                          </select>
                      </div>
                   </div>
                   <div class="w2ui-field">
                      <label style="width:40%;text-align: left;padding-left: 20px;">BOOTSTRAP 기본 정보 별칭</label>
                      <div style="width: 60%">
                          <select class="form-control" name="defaultConfigInfo" onchange="" style="width: 320px; margin-left: 20px;">
                              <option value="">수정할 BOOTSTRAP 기본 정보를 선택하세요.</option>
                          </select>
                      </div>
                    </div>
                    <div class="w2ui-field">
                      <label style="width:40%;text-align: left;padding-left: 20px;">BOOTSTRAP 네트워크 정보 별칭</label>
                      <div style="width: 60%">
                          <select class="form-control" name="networkConfigInfo" onchange="" style="width: 320px; margin-left: 20px;">
                              <option value="">수정할 BOOTSTRAP 네트워크 정보를 선택하세요.</option>
                          </select>
                      </div>
                    </div>
                    <div class="w2ui-field">
                      <label style="width:40%;text-align: left;padding-left: 20px;">BOOTSTRAP 리소스 정보 별칭</label>
                      <div style="width: 60%">
                          <select class="form-control" name="resourceConfigInfo" onchange="" style="width: 320px; margin-left: 20px;">
                              <option value="">수정할 BOOTSTRAP 리소스 정보를 선택하세요.</option>
                          </select>
                      </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="w2ui-buttons" id="cfModifyRegistInfoBtnDiv" hidden="true">
            <button class="btn" id="modifyRegistBootstrapInfoBtn" onclick="$('#settingForm').submit();">확인</button>
            <button class="btn" id="popModifyRegistClose" onclick="w2popup.close();">취소</button>
        </div>
    </form>
</div>

<!-- Deploy DIV -->
<div id="DeployDiv" style="width:100%;height:100%;" hidden="true">
    <div style="margin-left:2%;display:inline-block;width:97%;padding-top:20px;">
        <ul class="progressStep_6" >
            <li class="active">배포 파일 정보</li>
            <li class="before">설치</li>
        </ul>
    </div>
    <div style="width:93%;height:84%;float: left;display: inline-block;margin:10px 0 0 1%;">
        <textarea id="deployInfo" style="width:100%;height:99%;overflow-y:visible;resize:none;background-color: #FFF;margin-left:3%;" readonly="readonly"></textarea>
    </div>
    <div class="w2ui-buttons" id="DeployBtnDiv" hidden="true">
        <button class="btn" style="float: right; padding-right: 15%" onclick="confirmDeploy('after');">다음>></button>
    </div>
</div>

<!-- Install DIV -->
<div id="InstallDiv1" style="width:100%;height:100%;" hidden="true">
    <div style="margin-left:2%;display:inline-block;width:97%;padding-top:20px;">
        <ul class="progressStep_7" >
            <li style="font-size: 15px; width: 370px;" class="active">1 CF Install Log</li>
            <li style="font-size: 15px; width: 370px;" class="before">2 CF Install Log</li>
        </ul>
    </div>
    <div style="width:93%;height:84%;float: left;display: inline-block;margin:10px 0 0 1%;">
        <textarea id="installLogs" style="width:100%;height:99%;overflow-y:visible;resize:none;background-color: #FFF;margin-left:3%;" readonly="readonly"></textarea>
    </div>
    <div class="w2ui-buttons" id="InstallDivButtons" hidden="true">
            <button class="btn" style="float: right; padding-right: 15%" onclick="popupComplete();">닫기</button>
    </div>
</div>

<!-- Install DIV -->
<div id="InstallDiv2" style="width:100%;height:100%;" hidden="true">
    <div style="margin-left:2%;display:inline-block;width:97%;padding-top:20px;">
        <ul class="progressStep_7" >
            <li style="font-size: 15px; width: 370px;" class="pass">1 CF Install Log</li>
            <li style="font-size: 15px; width: 370px;" class="active">2 CF Install Log</li>
        </ul>
    </div>
    <div style="width:93%;height:84%;float: left;display: inline-block;margin:10px 0 0 1%;">
        <textarea id="installLogs" style="width:100%;height:99%;overflow-y:visible;resize:none;background-color: #FFF;margin-left:3%;" readonly="readonly"></textarea>
    </div>
    <div class="w2ui-buttons" id="InstallDivButtons" hidden="true">
            <button class="btn" style="float: right; padding-right: 15%" onclick="popupComplete();">닫기</button>
    </div>
</div>

<script>
$(function() {
      $.validator.addMethod("sqlInjection", function(value, element, params) {
            return checkInjectionBlacklist(params);
          },text_injection_msg);
    $("#settingForm").validate({
        ignore : "",
        onfocusout: true,
        rules: {
            roleName : {
                required : function(){
                  return checkEmpty( $(".w2ui-msg-body input[name='roleName']").val() );
                    }, sqlInjection : function(){
                      return $(".w2ui-msg-body input[name='roleName']").val();
                    }
            },
            roleDescription : {
                required : function(){
                  return checkEmpty( $(".w2ui-msg-body input[name='roleDescription']").val() );
                    }, sqlInjection : function(){
                      return $(".w2ui-msg-body input[name='roleDescription']").val();
                    }
             }
        }, messages: {
            roleName: { 
                 required:  "권한 그룹명" + text_required_msg
            },
            roleDescription: { 
                required:  "설명" + text_required_msg
            }
        }, unhighlight: function(element) {
            setSuccessStyle(element);
        },errorPlacement: function(error, element) {
            //do nothing
        }, invalidHandler: function(event, validator) {
            var errors = validator.numberOfInvalids();
            if (errors) {
                setInvalidHandlerStyle(errors, validator);
            }
        }, submitHandler: function (form) {
            saveBootstrapInfo();
        }
    });
});
</script>