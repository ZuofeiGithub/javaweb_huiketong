$(function () {

    if ((/iphone|ipod|ipad.*os 5/gi).test(navigator.appVersion)) {
        window.onpageshow = function (event) {
            if (event.persisted) {
                window.location.reload()
            }
        };
    } else {
        $(window).bind("unload", function () {
        });
        onload = function () {
            getVerify($("#imgVerify")[0]);
        }
    }
    getVerify($("#imgVerify")[0]);

    //事件
    $("#imgVerify").click(function () {
        getVerify(this);
    });
    $("#verif_code").focus(function () {
        $("#verifcode_error").addClass("am-hide");
    })

    layui.use('layer', function () {
        var $ = layui.jquery, layer = layui.layer;
        $("#login").click(function () {
            var user_telphone = $("#user_telphone").val();
            var user_passward = $("#user_passward").val();
            var verif_code = $("#verif_code").val();
            if (user_telphone == "") {
                layer.msg("请输入用户名");
                return;
            }
            if (user_passward == "") {
                layer.msg("请输入密码");
                return;
            }
            if (verif_code == "") {
                layer.msg("请输入验证码");
                return;
            }
            $.ajax({
                url: '/login',
                type: 'post',
                data: {'telphone': user_telphone, 'passward': user_passward, 'verif_code': verif_code},
                success: function (data) {
                    var json = JSON.parse(data);
                    var loginmsg = json.islogin;
                    switch (loginmsg) {
                        case 0:
                            layer.msg("用户不存在");
                            break;
                        case 1:
                            layer.msg("密码错误");
                            break;
                        case 2:
                            layer.msg("登录成功");
                            loginSuccess(json.telphone, json.username, json.usertype,json.rightname,json.headphoto);
                            break;
                        case 3:
                            layer.msg("验证码错误");
                            break;
                        case 4:
                            layer.msg("验证码失效");
                            break;
                    }
                }
            })
        })
        remoberMe();
    });

    //登录成功
    function loginSuccess(telphone, username, usertype,rightname,headphoto) {
        location.href = "admin";
        setCookie("telphone", telphone);
        setCookie("username", username);
        setCookie("usertype", usertype);
        setCookie("rightname",rightname);
        setCookie("headphoto",headphoto);
    }

    //获取验证码
    function getVerify(obj) {
        obj.src = "/getVerify?" + Math.random();
    }

    //记住密码
    function remoberMe() {
        $("#remeberme").change(function () {
            var isChecked = $('#remeberme').get(0).checked;
            if (isChecked) {
                var user_telphone = $("#user_telphone").val();
                var user_passward = $("#user_passward").val();
                if (user_telphone != "" && user_passward != "") {
                    setCookie("telphone", user_telphone);
                    setCookie("passward", user_passward);
                } else {
                    layer.msg("用户名密码不能为空");
                    $("#remeberme").removeAttr("checked");//取消选中
                    return;
                }
                setCookie("ischecked", true);
            } else {
                setCookie("ischecked", false);
                removeCookie("telphone");
                removeCookie("passward");
            }
        })
        if (getCookie("ischecked") == "true") {
            $("#remeberme").attr("checked", 'true');//全选
            $("#user_telphone").val(getCookie("telphone"));
            $("#user_passward").val(getCookie("passward"));
        }
        if (getCookie("ischecked") == "false") {
            $("#remeberme").removeAttr("checked");//取消选中
        }
    }

    $('#login_form_id').keydown(function(e){
        var e = e || window.event;
        if(e.keyCode==32)  //按键 ASCII 码值
        {
            if($("#remeberme").get(0).checked)
            {
                $("#remeberme").removeAttr("checked");
                return false;
            }else{
                $("#remeberme").attr("checked", 'true');//全选
                return false;
            }
        }
        return true;
    })

    /**
     * cookie操作
     */
    function setCookie(name, value, iDay) {
        var oDate = new Date();
        oDate.setDate(oDate.getDate() + iDay);
        document.cookie = name + '=' + value + ';expires=' + oDate;
    }

    function getCookie(name) {
        var arr = document.cookie.split('; ');
        for (var i = 0; i < arr.length; i++) {
            var arr2 = arr[i].split('=');
            if (trim(arr2[0]) === name) {
                return unescape(trim(arr2[1]));
            }
        }
        return '';
    }

    function removeCookie(name) {
        setCookie(name, 1, -1);
    }

    function trim(str) {
        return str.replace(/(^\s*)|(\s*$)/g, "");
    }

    $("#login_form_id").bind("keydown", function (e) {

        // 兼容FF和IE和Opera

        var theEvent = e || window.event;

        var code = theEvent.keyCode || theEvent.which || theEvent.charCode;

        if (code == 13) {

            //回车执行查询

            $("#login").click();

        }

    });
})