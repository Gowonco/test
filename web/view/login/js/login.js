//------------- login.js -------------//

$(document).ready(function() {
	//validate login form 
	// $("#login-form").validate({
	// 	ignore: null,
	// 	ignore: 'input[type="hidden"]',
	// 	errorPlacement: function(error, element) {
	// 		wrap = element.parent();
	// 		wrap1 = wrap.parent();
	// 		if (wrap1.hasClass('checkbox')) {
	// 			error.insertAfter(wrap1);
	// 		} else {
	// 			if (element.attr('type')=='file') {
	// 				error.insertAfter(element.next());
	// 			} else {
	// 				error.insertAfter(element);
	// 			}
	// 		}
	// 	},
	// 	errorClass: 'help-block',
	// 	rules: {
	// 		email: {
	// 			required: true,
	// 			email: true
	// 		},
	// 		password: {
	// 			required: true,
	// 			minlength: 6
	// 		}
	// 	},
	// 	messages: {
	// 		password: {
	// 			required: "密码不能为空",
	// 			minlength: "密码不少于六位字符"
	// 		},
	// 		email: "用户名不能为空"
	// 	},
	// 	highlight: function(element) {
	// 		if ($(element).offsetParent().parent().hasClass('form-group')) {
	// 			$(element).offsetParent().parent().removeClass('has-success').addClass('has-error');
	// 		} else {
	// 			if ($(element).attr('type')=='file') {
	// 				$(element).parent().parent().removeClass('has-success').addClass('has-error');
	// 			}
	// 			$(element).offsetParent().parent().parent().parent().removeClass('has-success').addClass('has-error');
	//
	// 		}
	//     },
	//     unhighlight: function(element,errorClass) {
	//     	if ($(element).offsetParent().parent().hasClass('form-group')) {
	//     		$(element).offsetParent().parent().removeClass('has-error').addClass('has-success');
	// 	    	$(element.form).find("label[for=" + element.id + "]").removeClass(errorClass);
	//     	} else if ($(element).offsetParent().parent().hasClass('checkbox')) {
	//     		$(element).offsetParent().parent().parent().parent().removeClass('has-error').addClass('has-success');
	//     		$(element.form).find("label[for=" + element.id + "]").removeClass(errorClass);
	//     	} else if ($(element).next().hasClass('bootstrap-filestyle')) {
	//     		$(element).parent().parent().removeClass('has-error').addClass('has-success');
	//     	}
	//     	else {
	//     		$(element).offsetParent().parent().parent().removeClass('has-error').addClass('has-success');
	//     	}
	// 	}
	// });
    errorInfoAutoClose();
});
function keyLogin(){
    if(event.keyCode == "13"){
        alert("login");
        $("#btn-submit").click();
    }
}

function login() {
    var params = getNameAndPassword();
    alert("user"+ params);
    // var url = "#";
    // var params = getNameAndPassword();
    // $.post(url,params,function(res){
    //     if(res.resultCode == "100000"){
    //         //window.location.href="https://mp.missionex.com.cn:443/index";
    //     }else if(res.resultCode == "100001" || res.resultCode == "100002"){
    //         //window.location.href="https://mp.missionex.com.cn:443/index/error?msg="+res.msg+"&wrongTitle="+res.wrongTitle;
    //     }else{
    //         //window.wxc.xcConfirm(res.msg, window.wxc.xcConfirm.typeEnum.info)
    //     }
    // })
};

function getNameAndPassword() {
    var params = {
        "name":$("#name").val(),
        "password":$("#password").val()
    };
    return params;
}

function errorInfoAutoClose() {
    setTimeout(function () {
        $("#error-info").fadeOut(1000);
    }, 1000);

}