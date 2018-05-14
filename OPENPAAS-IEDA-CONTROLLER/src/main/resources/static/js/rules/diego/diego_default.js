$(function() {
    $("#defaultInfoForm").validate({
            ignore : [],
            onfocusout: true,
            rules: {
                directorUuid : {
                    required : function(){ return checkEmpty( $(".w2ui-msg-body input[name='directorUuid']").val() ); }
                }, deploymentName: { 
                    required: function(){ return checkEmpty( $(".w2ui-msg-body input[name='deploymentName']").val() ); }
                }, diegoReleases: { 
                    required: function(){ return checkEmpty( $(".w2ui-msg-body select[name='diegoReleases']").val() ); }
                }, gardenReleaseName: { 
                    required: function(){ return checkEmpty( $(".w2ui-msg-body select[name='gardenReleaseName']").val() ); }
                }, cfInfo: { 
                    required: function(){ return checkEmpty( $(".w2ui-msg-body select[name='cfInfo']").val() ); }
                }, cadvisorDriverIp: { 
                    required: function(){
                        if( $(".w2ui-msg-body input:checkbox[name='paastaMonitoring']").is(":checked") ){
                            return checkEmpty( $(".w2ui-msg-body input[name='cadvisorDriverIp']").val() );
                        }else{
                            return false;
                        }
                    }
                }, userAddSsh: {
                    required: function(){
                        if( $(".w2ui-msg-body #userAddSsh").css("display") == "none"  ){
                            return false;
                        }else{
                            return checkEmpty( $(".w2ui-msg-body textarea[name='userAddSsh']").val() ); 
                        }
                    }
                }, osConfReleases: {
                    required: function(){
                        if( $(".w2ui-msg-body #osConfRelease").css("display") == "none"  ){
                            return false;
                        }else{
                            return checkEmpty( $(".w2ui-msg-body select[name='osConfReleases']").val() ); 
                        }
                    }
                }
            }, messages: {
                 directorUuid      : { required:  "설치관리자 UUID" + text_required_msg }
                ,deploymentName    : { required:  "배포 명"+text_required_msg }
                ,diegoReleases       : { required: "DIEGO 릴리즈" + select_required_msg }
                ,cflinuxfs2rootfsrelease        : { required:  "Cf-linuxfs2" + select_required_msg }
                ,gardenReleaseName      : { required:  "Garden 릴리즈"+select_required_msg }
                ,etcdReleases           : { required:  "ETCD 릴리즈"+select_required_msg }
                ,cfInfo      : { required:  "CF 배포 명 "+select_required_msg }
                ,cadvisorDriverIp       : { required:  "PaaS-TA 모니터링"+text_required_msg }
                ,userAddSsh          : { required: "Public SSH KEY" +text_required_msg}
                ,osConfReleases      : { required: "OS-CONF"+select_required_msg}
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
                saveDefaultInfo('after');
            }
        });
});
    