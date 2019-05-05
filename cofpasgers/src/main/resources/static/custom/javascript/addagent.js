/**
 * cookie操作
 */
"use strict";

function trim(str) {
    return str.toString().replace(/(^\s*)|(\s*$)/g, "");
}

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

if (getCookie("usertype") == "1") {
    var user_id = getCookie("telphone");
    $.ajax({
        url: 'agentcompanyinfo',
        type: 'post',
        data: {'user_id': user_id},
        success: function (data) {
            if (trim(data) != "") {
                $('#selectComm').append('<option value="' + data['enterName'] + '">' + data['enterName'] + '</option>')
            } else {
                $('#selectComm').append('<option value="0">暂无公司信息</option>')
            }
        }
    })
} else if (getCookie("usertype") == "2") {
    $.ajax({
        url: 'getcommonyinfo',
        type: 'get',
        success: function (data) {
            if (trim(data) != "") {
                for (var i in data) {
                    $('#selectComm').append('<option value="' + data[i]['enterName'] + '">' + data[i]['enterName'] + '</option>')
                }
            } else if (trim(data) == "") {
                $('#selectComm').append('<option value="0">暂无公司信息</option>');
            }

        }
    })
} else if (getCookie("usertype") == "3") {
    //公司
    var enter_login_id = getCookie("telphone");
    $.ajax({
        url: 'companyinfo',
        type: 'post',
        data: {'login_id': enter_login_id},
        success: function (data) {
            if (data != null) {
                $('#selectComm').append('<option value="' + data['enterName'] + '">' + data['enterName'] + '</option>')
            }
        }
    })
}

$("#sureSubmit").click(function () {
    var agentType = $("#agentType  option:selected").val();
    var agentName = $("#agentName").val();
    var loginName = $("#loginName").val();
    var agentTelphone = $("#telphone").val();
    var loginPass = $("#loginPass").val();
    var loginrePass = $("#loginrePass").val();
    //var selectComm = $("#selectComm option:selected").val();
    var selectComm = [];
    $("#selectComm :selected").each(function () {
        selectComm.push($(this).val());
    });
    var superiorTelphone = getCookie("telphone");
    $.ajax({
        url: 'addagent',
        type: 'post',
        data: {
            'agent_type': agentType,
            'agent_name': agentName,
            'login_name': loginName,
            'agent_telphone': agentTelphone,
            'login_pass': loginPass,
            'login_repass': loginrePass,
            'superior_telphone': superiorTelphone,
            'company': selectComm
        },
        success: function (data) {
            if (data.code == "1") {
                parent.layer.msg("添加经纪人成功");
                setTimeout(function () {
                    parent.layer.closeAll();
                    // var index = parent.layer.getFrameIndex(window.name);
                    // parent.layer.close(index);//关闭当前页
                    // window.parent.location.replace(location.href)//刷新父级页面

                }, 3000);
                // var $modal = $('#your-modal');
                // $modal.modal();
            } else {
                parent.layer.msg(data.msg);
                return;
            }

        }
    })

})





