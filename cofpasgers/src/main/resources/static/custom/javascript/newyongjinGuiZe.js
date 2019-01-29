layui.use(['form','jquery','table'], function(){
    var form = layui.form,$ = layui.jquery,table = layui.table;
    form.render();
    form.verify({
        pass: [
            /^\d+$/
            ,'排序字段必须为纯数字'
        ]
    });

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
    /**
     * cookie操作
     */

    $("input[name='telphone']").val(getCookie("telphone"));
    function trim(str) {
        return str.replace(/(^\s*)|(\s*$)/g, "");
    }
});

$("#form1").submit(function(){
    var formData= new FormData(document.getElementById("form1"));
    $.ajax({
        async: false,
        type: "POST",
        url:"/addYongjinGuiZe",
        data:formData,
        dataType: "json",
        contentType: false, //不设置内容类型
        processData: false, //不处理数据
        success: function (data) {
            if(data>0){
                alert("添加成功")
                // $("#myModal").modal('hide');  //手动关闭
                // table.reload("test");
            }else{
                alert("添加失败")
            }
        },
        error: function () {
        }
    })
})