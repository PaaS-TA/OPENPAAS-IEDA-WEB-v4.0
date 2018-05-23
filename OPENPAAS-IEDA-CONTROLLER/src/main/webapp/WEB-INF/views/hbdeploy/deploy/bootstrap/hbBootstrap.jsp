<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<script type="text/javascript">

/******************************************************************
 * 설명 :    변수 설정
 ***************************************************************** */
var iaas ="";
var publicIaas = "";
var privateIaas = "";
var bootStrapDeploymentName = new Array();
$(function() {
    /********************************************************
     * 설명 :  bootstrap 목록 설정
     *********************************************************/
     $('#config_hybrid_bootstrapGrid').w2grid({
        name: 'config_hybrid_bootstrapGrid',
        header: '<b>BOOTSTRAP 목록</b>',
        method: 'GET',
         multiSelect: false,
        show: {    
                selectColumn: true,
                footer: true},
        style: 'text-align: center',
        columns:[
              {field: 'recid',     caption: 'recid', hidden: true}
            , {field: 'hybridBootStrapId', caption: 'ID', hidden: true}
            , {field: 'hyPriDeploymentName',     caption: 'Private 배포 명', hidden: true}
            , {field: 'hyPubDeploymentName', caption: 'public 배포 명', hidden: true, render: function(record) {
                if(record.hyPubDeploymentName != null){
                    return record.hyPubDeploymentName
                } else {
                    return "-"
                }
            }}
            , {field: 'hyPriDeployStatus', caption: 'Private 배포상태', size: '100px', 
                render: function(record) {
                    if ( record.hyPriDeployStatus == 'DEPLOY_STATUS_PROCESSING' )
                        return '<span class="btn btn-primary" style="width:60px">배포중</span>';
                    else if ( record.hyPriDeployStatus == 'DEPLOY_STATUS_DONE' )
                        return '<span class="btn btn-primary" style="width:60px">성공</span>';
                    else if ( record.hyPriDeployStatus == 'DEPLOY_STATUS_CANCELLED' )
                        return '<span class="btn btn-danger" style="width:60px">취소</span>';
                    else if ( record.hyPriDeployStatus == 'DEPLOY_STATUS_FAILED' )
                        return '<span class="btn btn-danger" style="width:60px">실패</span>';
                    else if ( record.hyPriDeployStatus == 'DEPLOY_STATUS_DELETING' )
                        return '<span class="btn btn-primary" style="width:60px">삭제중</span>';
                    else
                        return '&ndash;';
                    }
              }
            , {field: 'deployStatus', caption: 'Public 배포상태', size: '100px', 
                render: function(record) {
                    if ( record.deployStatus == 'DEPLOY_STATUS_PROCESSING' )
                        return '<span class="btn btn-primary" style="width:60px">배포중</span>';
                    else if ( record.deployStatus == 'DEPLOY_STATUS_DONE' )
                        return '<span class="btn btn-primary" style="width:60px">성공</span>';
                    else if ( record.deployStatus == 'DEPLOY_STATUS_CANCELLED' )
                        return '<span class="btn btn-danger" style="width:60px">취소</span>';
                    else if ( record.deployStatus == 'DEPLOY_STATUS_FAILED' )
                        return '<span class="btn btn-danger" style="width:60px">실패</span>';
                    else if ( record.deployStatus == 'DEPLOY_STATUS_DELETING' )
                        return '<span class="btn btn-primary" style="width:60px">삭제중</span>';
                    else
                        return '&ndash;';
                    }
              }
            , {field: 'hyPriDeployLog', caption: '배포로그', size: '160px', 
                render: function(record) {
                    if ( (record.hyPriDeployStatus == 'DEPLOY_STATUS_DONE' || record.hyPriDeployStatus == 'DEPLOY_STATUS_FAILED') && record.hyPriDeployLog != null ) {
                        if((record.deployStatus == 'DEPLOY_STATUS_FAILED' || record.deployStatus == 'DEPLOY_STATUS_DONE' ) && record.deployLog != null ){
                            return '<span id="" class="btn btn-primary" style="width:120px;" onClick="getDeployLogMsg( \''+record.hyPriId+'\', \''+record.hyPriIaas+'\');">Private 로그보기</span><br><span id="" class="btn btn-primary" style="width:120px; margin-top:5px;" onClick="getDeployLogMsg( \''+record.id+'\', \''+record.hyPubIaas+'\');">Public 로그보기</span>';
                        }
                        return '-<br><span id="" class="btn btn-primary" style="width:120px;" onClick="getDeployLogMsg( \''+record.hyPriId+'\', \''+record.hyPriIaas+'\');">Private 로그보기</span><br>-';
                     } else {
                         if ( (record.deployStatus == 'DEPLOY_STATUS_DONE' || record.deployStatus == 'DEPLOY_STATUS_FAILED') && record.deployLog != null ) {
                             return '<br><span id="" class="btn btn-primary" style="width:120px;" onClick="getDeployLogMsg( \''+record.id+'\', \''+record.hyPubIaas+'\');">Public 로그보기</span><br>-';
                           }
                             return '-<br>-';
                     }
                 }
              }
            , {field: 'bootstrapType', caption: 'BOOTSTRAP 유형', size: '120px' ,
                render: function(record) {
                    if(record.bootstrapType.indexOf('null') != -1){
                        return record.bootstrapType.replace("null", "-");
                    } else {
                        return record.bootstrapType;
                    }
                }
            }
            , {field: 'iaas', caption: 'IaaS', size: '100px', render: function(record) {
                if(record.iaas.indexOf('null') != -1){
                    return record.iaas.replace("null", "-");
                } else {
                    return record.iaas ;
                }
            }}
            , {field: 'deploymentName', caption: '배포명', size: '120px', render : function(record){
                if(record.deploymentName.indexOf("null") != -1){
                    return record.deploymentName.replace(/null/gi, "-");
                }else {
                    bootStrapDeploymentName.push(record.deploymentName);
                    return record.deploymentName;
               }
            }}
            
            , {field: 'directorName', caption: '디렉터 명', size: '100px', render : function(record){
                if(record.directorName.indexOf("null") != -1){
                    return record.directorName.replace(/null/gi, "-");
                }else {
                    return record.directorName;
               }
            }}
            , {field: 'boshRelease', caption: 'BOSH 릴리즈', size: '100px', render : function(record){
                if(record.boshRelease.indexOf("null") != -1){
                    return record.boshRelease.replace(/null/gi, "-");
                }else {
                    return record.boshRelease;
               }
            }}
            , {field: 'boshCpiRelease', caption: 'BOSH CPI 릴리즈', size: '200px', render : function(record){
                if(record.boshCpiRelease.indexOf("null") != -1){
                    return record.boshCpiRelease.replace(/null/gi, "-");
                }else {
                    return record.boshCpiRelease;
               }
            }}
            , {field: 'publicStaticIp', caption: '디렉터 공인 IP', size: '100px', render : function(record){
                if(record.publicStaticIp.indexOf("null") != -1){
                    return record.publicStaticIp.replace(/null/gi, "-");
                }else {
                    return record.publicStaticIp;
               }
            }}
            , {field: 'privateStaticIp', caption: '디렉터 내부 IP', size: '100px', render : function(record){
                if(record.privateStaticIp.indexOf("null") != -1){
                    return record.privateStaticIp.replace(/null/gi, "-");
                }else {
                    return record.privateStaticIp;
               }
            }}
            ,{field: 'stemcell', caption: '스템셀', size: '320px', render : function(record){
                if(record.stemcell.indexOf("null") != -1){
                    return record.stemcell.replace(/null/gi, "-");
                }else {
                    return record.stemcell;
               }
            }}
            , {field: 'instanceType', caption: '인스턴스 유형', size: '100px', render : function(record){
                if(record.instanceType.indexOf("null") != -1){
                    return record.instanceType.replace(/null/gi, "-");
                }else {
                    return record.instanceType;
               }
            }}
            , {field: 'boshPassword', caption: 'VM 비밀번호', size: '100px', render : function(record){
                if(record.boshPassword.indexOf("null") != -1){
                    return record.boshPassword.replace(/null/gi, "-");
                }else {
                    return record.boshPassword;
               }
            }}
            , {field: 'hyPriDeploymentFile', caption: '배포파일명', size: '180px',
                   render: function(record) {
                       var privateFileName = record.hyPriDeploymentFile;
                       var pubilcFileName = record.deploymentFile;
                       if ( record.hyPriDeploymentFile != null ){
                           if(pubilcFileName != null){
                               return '<a style="color:#333;" href="/common/deploy/download/manifest/' + pubilcFileName +'" onclick="window.open(this.href); return false;">' + record.deploymentFile + '</a><br><a style="color:#333;" href="/common/deploy/download/manifest/' + privateFileName +'" onclick="window.open(this.href); return false;">' + record.hyPriDeploymentFile + '</a>';
                           }
                           return '-<br><a style="color:#333;" href="/common/deploy/download/manifest/' + privateFileName +'" onclick="window.open(this.href); return false;">' + record.hyPriDeploymentFile + '</a>';
                     } else {
                         if(pubilcFileName != null){
                             return '<a style="color:#333;" href="/common/deploy/download/manifest/' + pubilcFileName +'" onclick="window.open(this.href); return false;">' + record.deploymentFile + '</a><br>-'
                        }
                         return '-<br>-';
                        }
                    }
                }
            , {field: 'createdDate', caption: '생성일자', size: '100px', hidden: true}
            , {field: 'updatedDate', caption: '수정일자', size: '100px', hidden: true}
            ],
        onSelect : function(event) {
            event.onComplete = function() {
                $('#modifyBtn').attr('disabled', false);
                $('#deleteBtn').attr('disabled', false);
                return;
            }
        },
        onUnselect : function(event) {
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
     
    /******************************************************************
     * 설명 : BootStrap 설치 버튼
     ***************************************************************** */
     $("#installBtn").click(function(){
         w2ui['config_hybrid_bootstrapGrid'].reset();
         iaasSelectPopup();
     });
     
     /******************************************************************
     * 설명 : BootStrap 수정 버튼
     ***************************************************************** */
    $("#modifyBtn").click(function(){
        if($("#modifyBtn").attr('disabled') == "disabled") return;
        
        var selected = w2ui['config_hybrid_bootstrapGrid'].getSelection();
        if( selected.length == 0 ){
            w2alert("선택된 정보가 없습니다.", "BOOTSTRAP 수정");
            return;
        }
        var record = w2ui['config_hybrid_bootstrapGrid'].get(selected);
        
        $("#bootstrapPopupDiv").load("/deploy/hbBootstrap/install/bootstrapPopup",function(event){
            if(record.hyPriIaas == null){
                 w2confirm({
                     title        : "BOOTSTRAP 수정",
                     width : 490, 
                     height : 200,
                     msg : "수정 할 Private BOOTSTRAP 정보가 존재 하지 않습니다.<br><b>Public BOOTSTRAP 정보를 확인 후 삭제 해주세요.</b>",
                     yes_text     : "확인",
                     yes_callBack : function(event){
                        getBootstrapData(record.hyPubIaas.toLowerCase(), record.id);
                     },
                     no_text : "취소",
                     no_callBack : function(event){
                         w2ui['config_hybrid_bootstrapGrid'].clear();
                         doSearch();
                     }
                 });
            } else {
                iaas = record.hyPriIaas.toLowerCase();
                getBootstrapData(iaas, record.hyPriId);
            }
        });
     });
     
     /******************************************************************
     * 설명 : BootStrap 삭제 버튼
     ***************************************************************** */
    $("#deleteBtn").click(function(){
        if($("#deleteBtn").attr('disabled') == "disabled") return;
        var selected = w2ui['config_hybrid_bootstrapGrid'].getSelection();
        var record = w2ui['config_hybrid_bootstrapGrid'].get(selected);
        var message = "";
        if ( record.hyPubDeploymentName != null && record.hyPriDeploymentName != null ){
            message = "BOOTSTRAP <br> (배포명 : " + record.hyPriDeploymentName + ", " + record.hyPubDeploymentName+ ")를<br> 삭제하시겠습니까?";
        }else message = "선택된 BOOTSTRAP을 삭제하시겠습니까?";
        
        w2confirm({
            title        : "BOOTSTRAP 삭제",
            msg          : message,
            yes_text     : "확인",
            yes_callBack : function(event){
                $("#bootstrapPopupDiv").load("/deploy/hbBootstrap/install/bootstrapPopup",function(event){
                    if(record.hyPriId != null){
                        privateBootstrapDelete(record);
                    } else {
                        publicBootstrapDelete(record, "onlyPublic");
                    }
                });
            },
            no_text : "취소",
            no_callBack : function(event){
                w2ui['config_hybrid_bootstrapGrid'].clear();
                doSearch();
            }
        });
     });
     //조회
    doSearch();
});



/******************************************************************
 * 기능 : doSearch
 * 설명 : Bootstrap 목록 조회
 ***************************************************************** */
function doSearch() {
    //doButtonStyle();
    w2ui['config_hybrid_bootstrapGrid'].load("<c:url value='/deploy/hbBootstrap/list'/>",
            function (){ doButtonStyle(); });
}

/******************************************************************
 * 기능 : iaasSelectPopup
 * 설명 : Bootstrap Iaas 선택 팝업
 ***************************************************************** */
function iaasSelectPopup() {
    w2confirm({
        width : 550, 
        height : 300,
        title : '<b>이기종 BOOTSTRAP 설치</b>',
        msg : $("#bootSelectBody").html(),
        modal : true,
        yes_text : "확인",
        no_text : "취소",
        yes_callBack : function(){
            publicIaas = $(".w2ui-msg-body select[name='publicIaas'] :selected").val();
            privateIaas = $(".w2ui-msg-body select[name='privateIaas'] :selected").val();
            if(privateIaas){
                $("#bootstrapPopupDiv").load("/deploy/hbBootstrap/install/bootstrapPopup",function(event){
                    if(privateIaas == "Openstack"){
                        iaas = "Openstack";
                        openstackPopup();
                    } else if (privateIaas == "vSphere"){
                        iaas = "vSphere";
                        vSpherePopup();
                    }
                 });
             }else{
                 w2alert("BOOTSTRAP을 설치할 클라우드 환경을 선택하세요");
             }
         },no_callBack : function(event){
             w2ui['config_hybrid_bootstrapGrid'].clear();
             doSearch();
         }
    });
}

 /******************************************************************
  * 기능 : doButtonStyle
  * 설명 : Button 제어
  ***************************************************************** */
function doButtonStyle(){
    //Button Style init
    $('#modifyBtn').attr('disabled', true);
    $('#deleteBtn').attr('disabled', true);
}
 
/******************************************************************
 * 기능 : getDeployLogMsg
 * 설명 : 설치 로그 조회
 ***************************************************************** */
function getDeployLogMsg(id, iaas){
    $.ajax({
        type        : "GET",
        url         : "/deploy/hbBootstrap/list/"+id+"/"+iaas,
        contentType : "application/json",
        success     : function(data, status){
            if(!checkEmpty(data)){
                deployLogMsgPopup(data);
            }else{
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
 * 기능 : settingBeforePrivateIaaSInfo
 * 설명 : Public BOOTSTRAP 환경의 인프라 환경 화면에서 이전 버튼 클릭 시 Event
 ***************************************************************** */
function settingBeforePrivateIaaSInfo(){
    var selected = w2ui['config_hybrid_bootstrapGrid'].getSelection();
    var record = w2ui['config_hybrid_bootstrapGrid'].get(selected);
    $("#bootstrapPopupDiv").load("/deploy/hbBootstrap/install/bootstrapPopup",function(event){
        iaas = record.hyPriIaas.toLowerCase();
        getBootstrapData(iaas, record.hyPriId);
    });
}

/******************************************************************
 * 기능 : bootstrapDeleteDataSetting
 * 설명 : BOOTSTRAP 삭제 데이터 설정
 ***************************************************************** */
function bootstrapDeleteDataSetting(record, type){
    var requestParameter = [];
    if(type == 'private') {
        requestParameter = {
                id:record.hyPriId,
                iaasType:record.iaas.split('<br>')[1],
                bootstrapType: record.bootstrapType.split('<br>')[1]
        };
    }else if(type == 'public'){
        requestParameter = {
                id:record.id,
                iaasType:record.iaas.split('<br>')[0],
                bootstrapType: record.bootstrapType.split('<br>')[0]
        };
    }else if(type == "hybrid"){
        requestParameter = {
                id:record.hybridBootStrapId,
                iaasType: "hybrid",
                bootstrapType: "hybrid"
        };
    }
    return requestParameter;
}

/******************************************************************
 * 기능 : privateBootstrapDelete
 * 설명 : Private BOOTSTRAP 삭제 메소드
 ***************************************************************** */
function privateBootstrapDelete(record){
    var requestParameter = [];
    
    if ( record.hyPriDeployStatus == null || record.hyPriDeployStatus == '' ) {
        requestParameter = bootstrapDeleteDataSetting(record, "private");
        bootstrapJustDataDelete(requestParameter);
        if(( record.id != null && record.id != '') && ( record.deployStatus == null || record.deployStatus == '' ) ){
            requestParameter= bootstrapDeleteDataSetting(record, "public");
            bootstrapJustDataDelete(requestParameter);
        }
        requestParameter = bootstrapDeleteDataSetting(record, "hybrid");
        bootstrapJustDataDelete(requestParameter);
        
    } else {
        if(!lockFileSet(record.hyPriDeploymentFile)) return;
        var message = "BOOTSTRAP";
        var body = '<textarea id="deleteLogs" style="width:95%;height:90%;overflow-y:visible;resize:none;background-color: #FFF; margin:2%" readonly="readonly"></textarea>';
        w2popup.open({
            width   : 700,
            height  : 500,
            title   : "<b>"+record.hyPriIaas+" 클라우드 환경 BOOTSTRAP 삭제</b>",
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
                                       if ( response.state.toLowerCase() == "done" ) message = message + " 삭제가 완료되었습니다."; 
                                       if ( response.state.toLowerCase() == "error" ) message = message + " 삭제 중 오류가 발생하였습니다.";
                                       if ( response.state.toLowerCase() == "cancelled" ) message = message + " 삭제 중 취소되었습니다.";
                                       
                                       installStatus = response.state.toLowerCase();
                                       if(( record.id != null && record.id != '') && ( record.deployStatus == null || record.deployStatus == '' ) ){
                                           requestParameter = bootstrapDeleteDataSetting(record, "public");
                                           bootstrapJustDataDelete(requestParameter);
                                           requestParameter = bootstrapDeleteDataSetting(record, "hybrid");
                                           bootstrapJustDataDelete(requestParameter);
                                           deleteClient.disconnect();
                                       } else {
                                           deleteClient.disconnect();
                                           if(installStatus == 'done'){
                                               w2popup.close();
                                               deleteClient = "";
                                               publicBootstrapDelete(record);
                                           }
                                       }
                                       w2alert(message, "BOOTSTRAP 삭제");
                                       
                                   }
                            }
                        });
                        requestParameter = bootstrapDeleteDataSetting(record, "private");
                        deleteClient.send('/send/deploy/hbBootstrap/delete/instance', {}, JSON.stringify(requestParameter));
                    });
                }
            }, onClose : function (event){
                event.onComplete= function(){
                    bootStrapDeploymentName = [];
                    w2ui['config_hybrid_bootstrapGrid'].reset();
                    if( deleteClient != "" ){
                        deleteClient.disconnect();
                    }
                    popupClose();
                }
            } 
        });
    }
 }
 
/******************************************************************
 * 기능 : publicBootstrapDelete
 * 설명 : Public BOOTSTRAP 삭제 메소드
 ***************************************************************** */
 function publicBootstrapDelete(record, type){
    if(installStatus!='done' && type != "onlyPublic") return;
    var requestParameter = [];
    if ( record.deployStatus == null || record.deployStatus == '' ) {
        requestParameter= bootstrapDeleteDataSetting(record, "public");
        bootstrapJustDataDelete(requestParameter);
        requestParameter = bootstrapDeleteDataSetting(record, "hybrid");
        bootstrapJustDataDelete(requestParameter);
        
    } else {
        if(!lockFileSet(record.deploymentFile)) return;
        var message = "BOOTSTRAP";
        var body = '<textarea id="deleteLogs" style="width:95%;height:90%;overflow-y:visible;resize:none;background-color: #FFF; margin:2%" readonly="readonly"></textarea>';
        w2popup.open({
            width   : 700,
            height  : 500,
            title   : "<b>"+record.hyPubIaas+" 클라우드 환경 BOOTSTRAP 삭제</b>",
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
                                    if ( response.state.toLowerCase() == "done" ) message = message + " 삭제가 완료되었습니다."; 
                                    if ( response.state.toLowerCase() == "error" ) message = message + " 삭제 중 오류가 발생하였습니다.";
                                    if ( response.state.toLowerCase() == "cancelled" ) message = message + " 삭제 중 취소되었습니다.";
                                    
                                    installStatus = response.state.toLowerCase();
                                    if(installStatus == 'done'){
                                        requestParameter = bootstrapDeleteDataSetting(record, "hybrid");
                                        bootstrapJustDataDelete(requestParameter);
                                    }
                                    deleteClient.disconnect();
                                    w2alert(message, "BOOTSTRAP 삭제");
                                   }
                            }
                        });
                        requestParameter = bootstrapDeleteDataSetting(record, "public");
                        deleteClient.send('/send/deploy/hbBootstrap/delete/instance', {}, JSON.stringify(requestParameter));
                    });
                }
            }, onClose : function (event){
                event.onComplete= function(){
                    bootStrapDeploymentName = [];
                    w2ui['config_hybrid_bootstrapGrid'].reset();
                    if( deleteClient != "" ){
                        deleteClient.disconnect();
                    }
                    popupClose();
                }
            } 
        });
    }
 }
 
/******************************************************************
 * 기능 : bootstrapJustDataDelete
 * 설명 : BOOTSTRAP 단순 데이터 삭제 공통 기능
 ***************************************************************** */
 function bootstrapJustDataDelete(requestParameter){
     var url = "/deploy/hbBootstrap/delete/data";
     $.ajax({
         type : "DELETE",
         async : false,
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
 }
 
/******************************************************************
 * 기능 : privateInstallPopup
 * 설명 : Private Type Boostrap 설치
 ***************************************************************** */
var bootstrapInstallSocket = null;
function privateInstallPopup(bootstrapInfo){
    if(!lockFileSet(bootstrapInfo.hyPriDeploymentFile)) return;
    var message = "Private "+bootstrapInfo.hyPriIaasType+" BOOTSTRAP ";
    var requestParameter = {
           id : bootstrapInfo.hyPriId,
           iaasType: bootstrapInfo.hyPriIaasType
    };
    
    w2popup.open({
        title   : "<b>Pirvate "+bootstrapInfo.hyPriIaasType+" 클라우드 환경 BOOTSTRAP 설치</b>",
        width   : 800,
        height  : 620,
        modal   : true,
        showMax : true,
        body    : $("#"+bootstrapInfo.hyPriBootstrapType+"InstallDiv").html(),
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
                                    
                                installClient.disconnect(publicInstallPopup(bootstrapInfo));
                                w2alert(message, "Private "+data.hyPriIaasType+" BOOTSTRAP 설치");
                            }
                        }
                    });
                    installClient.send('/send/deploy/hbBootstrap/install/bootstrapInstall', {}, JSON.stringify(requestParameter));
                });
            }
        }, onClose : function(event){
               event.onComplete = function(){
                   w2ui['config_hybrid_bootstrapGrid'].reset();
                   if( installClient != ""  ){
                       installClient.disconnect();
                   }
                   popupClose();
               }
           }
    });
}

/******************************************************************
 * 기능 : publicInstallPopup
 * 설명 : Public Type Boostrap 설치
 ***************************************************************** */
function publicInstallPopup(bootstrapInfo){
    if(installStatus != "done") return;
    if(!lockFileSet(bootstrapInfo.deploymentFile)) return;
    
    var message = "Public "+bootstrapInfo.hyPubIaasType+" BOOTSTRAP ";
    var requestParameter = {
           id : bootstrapInfo.id,
           iaasType: bootstrapInfo.hyPubIaasType
    };
    w2popup.open({
        title   : "<b> Public "+bootstrapInfo.hyPubIaasType+" 클라우드 환경 BOOTSTRAP 설치</b>",
        width   : 800,
        height  : 620,
        modal   : true,
        showMax : true,
        body    : $("#"+bootstrapInfo.bootstrapType+"InstallDiv").html(),
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
                   w2ui['config_hybrid_bootstrapGrid'].reset();
                   if( installClient != ""  ){
                       installClient.disconnect();
                   }
                   popupClose();
               }
           }
    });
}

 /******************************************************************
  * 기능 : clearMainPage
  * 설명 : 다른페이지 이동시 Bootstrap Grid clear
  ***************************************************************** */
function clearMainPage() {
    $().w2destroy('config_hybrid_bootstrapGrid');
}

 /******************************************************************
  * 설명 : 화면 리사이즈시 호출 
  ***************************************************************** */
$( window ).resize(function() {
    setLayoutContainerHeight();
});
</script>

<div id="main">
    <div class="page_site">플랫폼 설치 > <strong>BOOTSTRAP 설치</strong></div>
    <!-- BOOTSTRAP 목록-->
    <div class="pdt20"> 
        <div class="title fl">Public/Private BOOTSTRAP 목록</div>
        <div class="fr"> 
            <sec:authorize access="hasAuthority('DEPLOY_BOOTSTRAP_INSTALL')">
            <span id="installBtn" class="btn btn-primary"  style="width:120px">설&nbsp;&nbsp;치</span>
            </sec:authorize>
            &nbsp;
            <sec:authorize access="hasAuthority('DEPLOY_BOOTSTRAP_INSTALL')">
            <span id="modifyBtn" class="btn btn-info" style="width:120px">수&nbsp;&nbsp;정</span>
            </sec:authorize>
            &nbsp;
            <sec:authorize access="hasAuthority('DEPLOY_BOOTSTRAP_DELETE')">
            <span id="deleteBtn" class="btn btn-danger" style="width:120px">삭&nbsp;&nbsp;제</span>
            </sec:authorize>
        </div>
    </div>
    <div id="config_hybrid_bootstrapGrid" style="width:100%; height:718px"></div>    
</div>

<div id="privateBootSelectBody" style="width:100%; height: 80px;" hidden="true">
    <div class="w2ui-lefted" >
        <b>Private BOOTSTRAP이 존재 하지 않습니다</b>.<br>
        BOOTSTRAP를 설치할 <b>Private 클라우드 환경</b>을 선택하세요<br />
        <br />
    </div>
    <div style="width:40%;margin: 0 auto;">
        <select class="form-control" name="privateIaas">
            <option value="Openstack">Openstack</option>
            <option value="vSphere">vSphere</option>
        </select>
    </div>
</div>


<div id="bootSelectBody" style="width:100%; height: 80px;" hidden="true">
    <div class="w2ui-lefted" style="text-align: center;">
        BOOTSTRAP를 설치할 <b>Public 클라우드 환경</b>을 선택하세요<br />
        <br />
    </div>
    <div style="width:40%;margin: 0 auto;">
        <select class="form-control" name="publicIaas">
            <option value="AWS">AWS</option>
            <option value="Google">Google</option>
            <option value="Azure">Azure</option>
        </select>
    </div>
    
    
    <div class="w2ui-lefted" style="margin-top: 38px;" >
        BOOTSTRAP를 설치할 <b>Private 클라우드 환경</b>을 선택하세요<br />
        <br />
    </div>
    <div style="width:40%;margin: 0 auto;">
        <select class="form-control" name="privateIaas">
            <option value="Openstack">Openstack</option>
            <option value="vSphere">vSphere</option>
        </select>
    </div>
</div>

<div id="bootstrapPopupDiv"></div>