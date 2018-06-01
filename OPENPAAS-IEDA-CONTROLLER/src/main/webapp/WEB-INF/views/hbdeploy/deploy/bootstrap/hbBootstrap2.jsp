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
var bootStrapDeploymentName = new Array();
$(function() {    
    /********************************************************
     * 설명 :  bootstrap 목록 설정
     *********************************************************/
     $('#config_bootstrapGrid').w2grid({
        name: 'config_bootstrapGrid',
        header: '<b>BOOTSTRAP 목록</b>',
        method: 'GET',
         multiSelect: false,
        show: {    
                selectColumn: true,
                footer: true},
        style: 'text-align: center',
        columns:[
              {field: 'recid',     caption: 'recid', hidden: true}
            , {field: 'directorName', caption: 'BOOTSTRAP 명', size: '20%'}
            , {field: 'directorName', caption: 'CPI 정보 명', size: '20%'}
            , {field: 'iaas', caption: 'IaaS', size: '20%'
                , render: function(record) {
                    return record.iaas.toLowerCase();
                }
            }
            , {field: 'boshRelease', caption: '기본 정보 명', size: '20%'}
            , {field: 'boshCpiRelease', caption: '네트워크 정보 명', size: '20%'}
            , {field: 'subnetId', caption: '리소스 정보 명 ', size: '20%'}
            , {field: 'subnetRange', caption: '디렉터 인증서 명', size: '20%'}
            ],
        onSelect : function(event) {
            event.onComplete = function() {
                $('#modifyBtn').attr('disabled', false);
                $('#deleteBtn').attr('disabled', false);
                return;
            }
        },
        onClick: function (event) {
            var grid = this;
            // need timer for nicer visual effect that record was selected
            setTimeout(function () {
                w2ui['config_bootstrapGrid2'].add( $.extend({}, grid.get(event.recid), { selected : false }) );
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
    
     $('#config_bootstrapGrid2').w2grid({ 
         name: 'config_bootstrapGrid2', 
         header: '<b>BOOTSTRAP 목록</b>',
         columns: [                
             {field: 'recid',     caption: 'recid', hidden: true}
             , {field: 'directorName', caption: 'BOOTSTRAP 명', size: '20%'}
             , {field: 'directorName', caption: 'CPI 정보 명', size: '20%'}
             , {field: 'iaas', caption: 'IaaS', size: '20%'
                 , render: function(record) {
                     return record.iaas.toLowerCase();
                 }
             }
             , {field: 'boshRelease', caption: '기본 정보 명', size: '20%'}
             , {field: 'boshCpiRelease', caption: '네트워크 정보 명', size: '20%'}
             , {field: 'subnetId', caption: '리소스 정보 명 ', size: '20%'}
             , {field: 'subnetRange', caption: '디렉터 인증서 명', size: '20%'}
         ],
         onClick: function (event) {
             var grid = this;
             // need timer for nicer visual effect that record was selected
             setTimeout(function () {
                 w2ui['config_bootstrapGrid'].add( $.extend({}, grid.get(event.recid), { selected : false }) );
                 grid.selectNone();
                 grid.remove(event.recid);
             }, 150);
         }
     });
     
     $('#config_bootstrapGrid3').w2grid({
         name: 'config_bootstrapGrid3',
         header: '<b>BOOTSTRAP 목록</b>',
         method: 'GET',
          multiSelect: false,
         show: {    
                 selectColumn: true,
                 footer: true},
         style: 'text-align: center',
         columns:[
               {field: 'recid',     caption: 'recid', hidden: true}
               , {field: 'deployStatus', caption: '배포상태', size: '100px', 
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
             , {field: 'directorName', caption: 'BOOTSTRAP 명', size: '20%'}
             , {field: 'directorName', caption: 'CPI 정보 명', size: '20%'}
             , {field: 'iaas', caption: 'IaaS', size: '20%'
                 , render: function(record) {
                     return record.iaas.toLowerCase();
                 }
             }
             , {field: 'boshRelease', caption: '기본 정보 명', size: '20%'}
             , {field: 'boshCpiRelease', caption: '네트워크 정보 명', size: '20%'}
             , {field: 'subnetId', caption: '리소스 정보 명 ', size: '20%'}
             , {field: 'subnetRange', caption: '디렉터 인증서 명', size: '20%'}
             ],
         onSelect : function(event) {
             event.onComplete = function() {
                 $('#modifyBtn').attr('disabled', false);
                 $('#deleteBtn').attr('disabled', false);
                 return;
             }
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
    
    /******************************************************************
     * 설명 : BootStrap 설치 버튼
     ***************************************************************** */
     $("#installBtn").click(function(){
         iaasSelectPopup();
     });
     
     /******************************************************************
     * 설명 : BootStrap 수정 버튼
     ***************************************************************** */
    $("#modifyBtn").click(function(){
        if($("#modifyBtn").attr('disabled') == "disabled") return;
        
        var selected = w2ui['config_bootstrapGrid'].getSelection();
        if( selected.length == 0 ){
            w2alert("선택된 정보가 없습니다.", "BOOTSTRAP 수정");
            return;
        }
        var record = w2ui['config_bootstrapGrid'].get(selected);
            
        $("#bootstrapPopupDiv").load("/deploy/bootstrap/install/bootstrapPopup",function(event){
            iaas = record.iaas.toLowerCase();
            getBootstrapData(record);
        });
     });
     
     /******************************************************************
     * 설명 : BootStrap 삭제 버튼
     ***************************************************************** */
    $("#deleteBtn").click(function(){
        if($("#deleteBtn").attr('disabled') == "disabled") return;
        
        var selected = w2ui['config_bootstrapGrid'].getSelection();
        var record = w2ui['config_bootstrapGrid'].get(selected);
        var message = "";
        
        if ( record.deploymentName ){
            message = "BOOTSTRAP (배포명 : " + record.deploymentName + ")를 삭제하시겠습니까?";
        }else message = "선택된 BOOTSTRAP을 삭제하시겠습니까?";
        
        w2confirm({
            title        : "BOOTSTRAP 삭제",
            msg          : message,
            yes_text     : "확인",
            yes_callBack : function(event){
                $("#bootstrapPopupDiv").load("/deploy/bootstrap/install/bootstrapPopup",function(event){
                    deletePop(record);
                });
            },
            no_text : "취소",
            no_callBack : function(event){
                w2ui['config_bootstrapGrid'].clear();
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
    w2ui['config_bootstrapGrid'].load("<c:url value='/deploy/bootstrap/list'/>",
            function (){ doButtonStyle(); });
    w2ui['config_bootstrapGrid3'].load("<c:url value='/deploy/bootstrap/list'/>");
}

/******************************************************************
 * 기능 : iaasSelectPopup
 * 설명 : Bootstrap Iaas 선택 팝업
 ***************************************************************** */
function iaasSelectPopup() {
    w2confirm({
        width   : 730,
        height  : 300,
        title : '<b>BOOTSTRAP 설치</b>',
        msg : $("#GoogleInfoDiv").html(),
        modal : true,
        yes_text : "확인",
        no_text : "취소",
        yes_callBack : function(){
            iaas = $(".w2ui-msg-body select[name='iaas']").val();
            if(iaas){
                $("#bootstrapPopupDiv").load("/deploy/bootstrap/install/bootstrapPopup",function(event){
                    if( iaas == "AWS" ) awsPopup();
                    else if( iaas == "Openstack" ) openstackPopup();
                    else if( iaas == "vSphere" ) vSpherePopup();
                    else if( iaas == "Google" ) googlePopup();
                    else if( iaas == "Azure") azurePopup();
                 });       
             }else{
                 w2alert("BOOTSTRAP을 설치할 클라우드 환경을 선택하세요");
             }
         },no_callBack : function(event){
             w2ui['config_bootstrapGrid'].clear();
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
function getDeployLogMsg(id){
    $.ajax({
        type        : "GET",
        url         : "/deploy/bootstrap/list/"+id,
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
  * 기능 : clearMainPage
  * 설명 : 다른페이지 이동시 Bootstrap Grid clear
  ***************************************************************** */
function clearMainPage() {
    $().w2destroy('config_bootstrapGrid');
    $().w2destroy('config_bootstrapGrid2');
    $().w2destroy('config_bootstrapGrid3');
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
        <div class="title fl">배포 가능 한 Private/Public BOOTSTRAP 목록</div>
        <div class="fr"> 
            <sec:authorize access="hasAuthority('DEPLOY_BOOTSTRAP_INSTALL')">
            <span id="installBtn" class="btn btn-primary"  style="width:120px">정보 등록</span>
            </sec:authorize>
            &nbsp;
            <sec:authorize access="hasAuthority('DEPLOY_BOOTSTRAP_INSTALL')">
            <span id="modifyBtn" class="btn btn-info" style="width:120px">정보 수정</span>
            </sec:authorize>
            &nbsp;
            <sec:authorize access="hasAuthority('DEPLOY_BOOTSTRAP_DELETE')">
            <span id="deleteBtn" class="btn btn-danger" style="width:120px">정보 삭제</span>
            </sec:authorize>
        </div>
    </div>
    <div id="config_bootstrapGrid" style="width:100%; height:300px"></div>
    
    
    <div class="pdt20"> 
        <div class="title fl">배포 할 Private/Public BOOTSTRAP 목록</div>
        <div class="fr"> 
            <sec:authorize access="hasAuthority('DEPLOY_BOOTSTRAP_INSTALL')">
            <span id="installBtn" class="btn btn-primary"  style="width:120px">VM 설치</span>
            </sec:authorize>
        </div>
    </div>
    <div id="config_bootstrapGrid2" style="width:100%; height:300px"></div>
    
    <div class="pdt20"> 
        <div class="title fl">배포 한 Private/Public BOOTSTRAP 목록</div>
        <div class="fr"> 
            <sec:authorize access="hasAuthority('DEPLOY_BOOTSTRAP_INSTALL')">
            <span id="installBtn" class="btn btn-danger"  style="width:120px">VM 삭제</span>
            </sec:authorize>
        </div>
    </div>
    <div id="config_bootstrapGrid3" style="width:100%; height:300px"></div>
</div>

<div id="GoogleInfoDiv" style="width:100%;height:100%;" hidden="true">
    <div class="w2ui-page page-0" style="margin-top:-15px;padding:0 3%;">
        <div class="panel panel-info"> 
            <div class="panel-heading"><b>BOOTSTRAP 설치 정보</b></div>
            <div class="panel-body" style="padding:5px 5% 10px 5%;">
                <div class="w2ui-field">
                  <label style="text-align: left;width:40%;font-size:11px;">BOOTSTRAP CPI 정보</label>
                  <div style="width: 60%">
                      <select name="iaasConfigId" onchange="settingIaasConfigInfo(this.value);" style="width:80%;">
                          <option value="">1</option>
                          <option value="">2</option>
                          <option value="">3</option>
                      </select>
                  </div>
                </div>
               <div class="w2ui-field">
                  <label style="text-align: left;width:40%;font-size:11px;">BOOTSTRAP 기본 정보</label>
                  <div style="width: 60%">
                      <select name="iaasConfigId" onchange="settingIaasConfigInfo(this.value);" style="width:80%;">
                          <option value="">1</option>
                          <option value="">2</option>
                          <option value="">3</option>
                      </select>
                  </div>
                </div>
                <div class="w2ui-field">
                  <label style="text-align: left;width:40%;font-size:11px;">BOOTSTRAP 네트워크 정보</label>
                  <div style="width: 60%">
                      <select name="iaasConfigId" onchange="settingIaasConfigInfo(this.value);" style="width:80%;">
                          <option value="">1</option>
                          <option value="">2</option>
                          <option value="">3</option>
                      </select>
                  </div>
                </div>
                <div class="w2ui-field">
                  <label style="text-align: left;width:40%;font-size:11px;">BOOTSTRAP 리소스 정보</label>
                  <div style="width: 60%">
                      <select name="iaasConfigId" onchange="settingIaasConfigInfo(this.value);" style="width:80%;">
                          <option value="">1</option>
                          <option value="">2</option>
                          <option value="">3</option>
                      </select>
                  </div>
                </div>
            </div>
        </div>
    </div>
    <div class="w2ui-buttons" id="googleInfoBtnDiv" hidden="true">
        <button class="btn" style="float: left; padding-right: 15%" onclick="settingBeforePrivateIaaSInfo();">이전</button>
        <button class="btn" style="float: right; padding-right: 15%" onclick="saveIaasConfigInfo();">다음>></button>
    </div>
</div>

<div id="bootstrapPopupDiv"></div>