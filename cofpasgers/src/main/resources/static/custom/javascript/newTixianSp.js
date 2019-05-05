var table;
layui.use(['form', 'table'], function () {
    var form = layui.form;
    table = layui.table;
    form.render();//自定义验证规则
    /**
     * cookie操作
     */
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

    table.render({
        elem: '#test'
        , url: '/tixianList?telphone=' + getCookie("telphone")
        , toolbar: '#toolbarDemo'
        , title: '用户数据表'
        , cols: [
            [
                {
                    field: 'drawalTime',
                    title: '提现时间',
                    width: '20%',
                    templet: "<div>{{ layui.util.toDateString(d.drawalTime,'yyyy-MM-dd')}}</div>"
                }
                , {field: 'angentname', title: '经纪人', width: '20%'}
                , {field: 'money', title: '提现金额(元)', width: '10%'}
                , {field: 'bankName', title: '银行名称', width: '10%'}
                , {field: 'bankNum', title: '银行卡号', width: '10%'}
                , {field: 'status', title: '状态', width: '10%', templet: '#statusTpl'}
                , {field: 'descript', title: '备注', width: '10%'}
                , {fixed: 'right', title: '操作', toolbar: '#barDemo', width: '10%'}
            ]
        ]
        , page: true
        /* ,done: function(res, curr,count){
                debugger
                console.log(res)
            }*/
    });

    //监听行工具事件
    table.on('tool(test)', function (obj) {
        var data = obj.data;
        if (obj.event === 'edit') {
            $("#myModal").modal('show');  //手动开启
            $("input[name='id']").val(data.id);
        }
    });
})

$("#form1").submit(function () {
    var formData = new FormData(document.getElementById("form1"));
    $.ajax({
        type: "POST",
        url: "/updateTixian",
        data: formData,
        dataType: "json",
        contentType: false, //不设置内容类型
        processData: false, //不处理数据
        success: function (data) {
            if (data > 0) {
                alert("审批成功")
                $("#myModal").modal('hide');  //手动关闭
                table.reload("test");
            } else {
                alert("审批失败")
            }
        },
        error: function () {
        }
    })
})

$("#search").on("click", function () {
    var angentname = $("input[name='angentname']").val();
    table.reload('test', {
        where: { //设定异步数据接口的额外参数，任意设
            angentname: angentname
        }
        , page: {
            curr: 1 //重新从第 1 页开始
        }
    });
})