
var  table;
layui.use(['form', 'table'], function(){
    var form = layui.form;
    table = layui.table;
    form.render();
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
    $("input[name='telphone']").val(getCookie("telphone"));
    function trim(str) {
        return str.replace(/(^\s*)|(\s*$)/g, "");
    }
    table.render({
        elem: '#test'
        ,url:'/commodityOrderList?telphone='+getCookie("telphone")
        ,toolbar: '#toolbarDemo'
        ,title: '用户数据表'
        ,cols: [
            [
                {field:'customerName', title:'客户名称', width:'30%'}
                ,{field:'commodityName', title:'商品名称', width:'20%'}
                ,{field:'status', title:'订单状态', width:'15%',templet:"#sexTpl"}
                ,{field:'insertTime', title:'订单生成时间', width:'15%',templet: "<div>{{ layui.util.toDateString(d.insertTime)}}</div>"}
                ,{fixed: 'right', title:'操作', toolbar: '#barDemo', width:'20%'}
            ]
        ]
        ,page: true
        /* ,done: function(res, curr,count){
          }*/
    });

    //监听行工具事件
    table.on('tool(test)', function(obj){
        var data = obj.data;
        if(obj.event === 'edit'){
            $("#myModal").modal('show');  //手动关闭
            $("input[name='id']").val(data.id);
        }
    });
});

$("#form1").submit(function(){
    var formData= new FormData(document.getElementById("form1"));
    $.ajax({
        async: false,
        type: "POST",
        url:"/updateCommodityOrder",
        data:formData,
        dataType: "json",
        contentType: false, //不设置内容类型
        processData: false, //不处理数据
        success: function (data) {
            if(data>0){
                alert("修改成功")
                $("#myModal").modal('hide');  //手动关闭
                table.reload("test");
            }else{
                alert("修改失败")
            }
        },
        error: function () {
        }
    })
})

$("#search").on("click",function(){
    var customerName=  $("input[name='customerName']").val();
    table.reload('test', {
        where: { //设定异步数据接口的额外参数，任意设
            customerName:customerName
        }
        ,page: {
            curr: 1 //重新从第 1 页开始
        }
    });

})