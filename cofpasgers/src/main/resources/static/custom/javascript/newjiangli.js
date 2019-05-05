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
        , url: '/jiangliList?telphone=' + getCookie("telphone")
        , toolbar: '#toolbarDemo'
        , title: '用户数据表'
        , cols: [
            [
                {field: 'angentname', title: '经纪人', width: '10%'}
                , {field: 'cusname', title: '客户姓名', width: '10%'}
                , {field: 'cusnamePhone', title: '客户联系电话', width: '10%'}
                , {field: 'money', title: '收益金额(元)', width: '10%'}
                , {
                field: 'addTime',
                title: '创建时间',
                width: '20%',
                templet: "<div>{{ layui.util.toDateString(d.addTime)}}</div>"
            }
                , {field: 'descript', title: '备注', width: '10%'}
                , {field: 'commissionlevel', title: '对应佣金层级', width: '15%'}
                , {field: 'commissionpercentage', title: '对应佣金占比', width: '15%'}
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
    var cusname = $("input[name='cusname']").val();
    table.reload('test', {
        where: { //设定异步数据接口的额外参数，任意设
            angentname: angentname
            , cusname: cusname
        }
        , page: {
            curr: 1 //重新从第 1 页开始
        }
    });
})