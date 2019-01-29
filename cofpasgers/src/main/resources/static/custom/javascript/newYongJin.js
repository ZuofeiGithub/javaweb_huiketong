var layedit;
var  table;
var index ;
layui.use(['form', 'table','layedit'], function(){
    var form = layui.form;
    table = layui.table;
    layedit = layui.layedit;
    layedit.set({
        uploadImage: {
            url: '/uploadYongJinImg' //接口url
            ,type: 'post' //默认post
        }
    });
    index= layedit.build('demo'); //建立编辑器
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
        ,url:'/yongjinList?telphone='+getCookie("telphone")
        ,toolbar: '#toolbarDemo'
        ,title: '用户数据表'
        ,cols: [
            [
                {field:'title', title:'分享标题', width:'30%'}
                ,{field:'money', title:'分享佣金(元)', width:'20%'}
                ,{field:'datetime', title:'创建时间', width:'30%'}
                /*    ,{field:'url', title:'链接地址', width:'15%', templet: '#urlTpl'}*/
                ,{fixed: 'right', title:'操作', toolbar: '#barDemo', width:'20%'}
            ]
        ]
        ,page: true
        /*   ,done: function(res, curr,count){
               debugger
               console.log(res)
           }*/
    });
    //头工具栏事件
    table.on('toolbar(test)', function(obj){
        var checkStatus = table.checkStatus(obj.config.id);
        if(obj.event === 'addYongjin'){
            $("#myModal").modal('show');  //手动开启
            $("#myModalLabel").text("添加佣金")  //手动开启
            $("input[name='title']").val("");
            $("input[name='flag']").val("1");
            $("input[name='url']").val("");
            layedit.setContent(index,"");
            $("input[name='money']").val("");
            $("#myfile").val("");
            $("#previewImg").attr('src',"http://www");
            $("#previewImg").css("display",'none');
        }
    });

    //监听行工具事件
    table.on('tool(test)', function(obj){
        var data = obj.data;
        if(obj.event === 'del'){
            layer.confirm('真的删除行么', function(index){
                layer.close(index);
                $.ajax({
                    type: "POST",
                    url:"/deleteYongjin",
                    data:{"id":data.id},
                    dataType: "json",
                    success: function (data) {
                        if(data>0){
                            alert("删除成功")
                            table.reload("test");
                        }else{
                            alert("删除失败")
                        }
                    },
                    error: function () {
                    }
                })

            });
        } else if(obj.event === 'edit'){
            $("#myModal").modal('show');  //手动开启
            $("#myModalLabel").text("修改佣金")  //手动开启
            $("input[name='title']").val(data.title);
            $("input[name='url']").val(data.url);
            layedit.setContent(index,data.context);
            $("input[name='money']").val(data.money);
            $("input[name='flag']").val("2");
            $("input[name='id']").val(data.id);
            $("#previewImg").attr('src',data.imgUrl);
            $("#previewImg").css("display",'block');
            $("#myfile").val("");
        }
    });
});

$("#myfile").change(function(){
    var imgPath = getObjectURL(this.files[0]);
    $("#previewImg").attr({
        src:imgPath
    })
    $("#previewImg").css("display",'block');
});
function getObjectURL(file) {
    var url = null ;
    if (window.createObjectURL!=undefined) {
        url = window.createObjectURL(file) ;
    } else if (window.URL!=undefined) {
        url = window.URL.createObjectURL(file) ;
    } else if (window.webkitURL!=undefined) {
        url = window.webkitURL.createObjectURL(file) ;
    }
    return url ;
}

$("#form1").submit(function(){
    var yongjinContent=  layedit.getContent(index);
    if(yongjinContent==null||yongjinContent==""){
        alert("请输入分享内容!")
        return false;
    }
    var imgSrc= $('#previewImg')[0].src;
    if(imgSrc=="http://www/"){
        alert("请选择分享图片!")
        return false;
    }
    var formData= new FormData(document.getElementById("form1"));
    formData.set("yongjinContent",yongjinContent);
    formData.set("imgSrc",imgSrc);

    var flag=  $("input[name='flag']").val();
    if(flag==1){
        $.ajax({
            async: false,
            type: "POST",
            url:"/uploadYongJinInfo",
            data:formData,
            dataType: "json",
            contentType: false, //不设置内容类型
            processData: false, //不处理数据
            success: function (data) {
                if(data>0){
                    alert("添加成功")
                    $("#myModal").modal('hide');  //手动关闭
                    table.reload("test");
                }else{
                    alert("添加失败")
                }
            },
            error: function () {
            }
        })
    }else{
        $.ajax({
            async: false,
            type: "POST",
            url:"/updateYongjin",
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
    }

})

$("#search").on("click",function(){
    var searchTitle=  $("input[name='searchTitle']").val();
    table.reload('test', {
        where: { //设定异步数据接口的额外参数，任意设
            searchTitle:searchTitle
        }
        ,page: {
            curr: 1 //重新从第 1 页开始
        }
    });

})