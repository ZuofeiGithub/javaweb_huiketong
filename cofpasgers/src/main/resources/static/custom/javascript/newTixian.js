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
                , {field: 'angentname', title: '经纪人', width: '15%'}
                , {field: 'money', title: '提现金额(元)', width: '15%'}
                , {field: 'status', title: '状态', width: '15%', templet: '#statusTpl'}
                , {
                field: 'updateTime',
                title: '操作时间',
                width: '20%',
                templet: "<div>{{ layui.util.toDateString(d.updateTime,'yyyy-MM-dd')}}</div>"
            }
                , {field: 'descript', title: '备注', width: '15%'}
            ]
        ]
        , page: true
        /* ,done: function(res, curr,count){
                debugger
                console.log(res)
            }*/
    });
})
$("#search").on("click", function () {
    var angentname = $("input[name='angentname']").val();
    var status = $("select[name='status']").val();
    table.reload('test', {
        where: { //设定异步数据接口的额外参数，任意设
            angentname: angentname
            , status: status
        }
        , page: {
            curr: 1 //重新从第 1 页开始
        }
    });
})