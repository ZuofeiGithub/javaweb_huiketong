var Debug = {}
Debug.reportError = function reportError(sMessage, sUrl, sLine) {
    var str = "";
    str += " 错误信息:" + sMessage + "\n";
    str += " 错误地址:" + sUrl + "\n";
    str += " 错误行数:" + sLine + "\n";
    str += "<=========调用堆栈=========>\n";
    var func = window.onerror.caller;
    var index = 0;
    while (func != null) {
        //str += "第" + index + "个函数：" + func + "\n";
        //str += "第" + index + "个函数：参数表："
        //for(var i=0;i<func.arguments.count;i++)
        //str += func.arguments[i] + ",";
        //}
        str += func;
        str += "\n===================\n";
        func = func.caller;
        index++;
    }
    return true;
}
window.onerror = Debug.reportError;

function initUserInfo() {
    $("#username").text(getCookie("username"));
    $('#rightname').text(getCookie("rightname"));
    $('#logname').text(getCookie('rightname'));
}

var telphone = getCookie("telphone");
$.ajax({
    url: 'isJurisdiction',
    type: 'post',
    data: {'telphone': telphone},
    success: function (data) {
        initUserInfo();
        if (data == "system") {

        } else if (data == "agent") {

        } else if (data == "company") {

        } else {

        }
    }
})


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

function trim(str) {
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
