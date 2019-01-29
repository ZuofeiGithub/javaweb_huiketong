var layedit;
var table;
var index ;
var form;
layui.use(['form', 'table','layedit','upload'], function(){
     var upload=layui.upload;
     form = layui.form;
    table = layui.table;
    layedit = layui.layedit;
    console.log("这里已经执行");

    layedit.set({
        uploadImage: {
            url: '/uploadYongJinImg' //接口url
            ,type: 'post' //默认post
        }
    });

    //多文件列表示例
    var demoListView = $('#demoList')
        ,uploadListIns = upload.render({
        elem: '#testList'
        ,url: '/addCommodityImg'
        ,accept: 'file'
        ,multiple: true
        ,auto: false
        ,bindAction: '#testListAction'
        , before: function (obj) {
            var id= $("input[name='id']").val();//添加参数
            this.data ={"id":id};
        }
        ,choose: function(obj){
            var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
            //读取本地文件
            obj.preview(function(index, file, result){
                var tr = $(['<tr id="upload-'+ index +'">'
                    ,'<td><img style="width:200px;height:100px;margin-right:10px" src="'+ result +'" alt="'+ file.name +'" class="layui-upload-img"></td>'
                    ,'<td>'+ (file.size/1014).toFixed(1) +'kb</td>'
                    ,'<td>等待上传</td>'
                    ,'<td>'
                    ,'<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
                    ,'<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
                    ,'</td>'
                    ,'</tr>'].join(''));

                //单个重传
                tr.find('.demo-reload').on('click', function(){
                    obj.upload(index, file);
                });

                //删除
                tr.find('.demo-delete').on('click', function(){
                    delete files[index]; //删除对应的文件
                    tr.remove();
                    uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                });

                demoListView.append(tr);
            });
        }
        ,done: function(res, index, upload){
            if(res.code == 0){ //上传成功
                table.reload("commodityImg");
                var tr = demoListView.find('tr#upload-'+ index)
                    ,tds = tr.children();
                tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
                tds.eq(3).html(''); //清空操作
                return delete this.files[index]; //删除文件队列已经上传成功的文件
            }
            this.error(index, upload);

        }
        ,error: function(index, upload){
            var tr = demoListView.find('tr#upload-'+ index)
                ,tds = tr.children();
            tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
            tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
        }
    });
    index= layedit.build('productDetails'); //产品详情建立编辑器
    form.render();
    // cookie操作
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
    $("input[name='telphone']").val(getCookie("telphone"));
  //下拉框赋值
   getSelect()

    table.render({
        elem: '#test'
        ,url:'/commodityList?telphone='+getCookie("telphone")
        ,toolbar: '#toolbarDemo'
        ,title: '用户数据表'
        ,cols: [
            [
                {field:'commodityName', title:'商品名称', width:'20%'}
                ,{field:'originalPrice', title:'商品原价', width:'20%'}
                ,{field:'activityPrice', title:'活动特价', width:'10%'}
                ,{field:'depositMoney', title:'预付定金', width:'10%'}
                ,{field:'danwei', title:'单位', width:'10%'}
                ,{field:'concernedPeople', title:'关注人数', width:'10%'}
                ,{fixed: 'right', title:'操作', toolbar: '#barDemo', width:'20%'}
            ]
        ]
        ,page: true
      /*  ,done: function(res, curr,count){
           }*/
    });
    //头工具栏事件
    table.on('toolbar(test)', function(obj){
        var checkStatus = table.checkStatus(obj.config.id);
        if(obj.event === 'addYongjin'){
            $("#myModal").modal('show');  //手动开启
            $("#myModalLabel").text("添加商品")  //手动开启
            $("input[name='flag']").val("1");
            layedit.setContent(index,"");
            $("input[name='commodityName']").val("");
            $("input[name='danwei']").val("");
            $("input[name='originalPrice']").val("");
            $("input[name='activityPrice']").val("");
            $("input[name='depositMoney']").val("");
            $("textarea[name='activityDescription']").val("");
            $("#commodityCategoryId").find("option[value='']").prop("selected",true);
            $("#commodityStyleId").find("option[value='']").prop("selected",true);
            form.render();

        }else if(obj.event === 'search_by_name'){
            table.render({
                elem: '#test'
                ,url:'/commodityList?telphone='+getCookie("telphone")+'&name='+$('#com-name').val()
                ,toolbar: '#toolbarDemo'
                ,title: '用户数据表'
                ,cols: [
                    [
                        {field:'commodityName', title:'商品名称', width:'20%'}
                        ,{field:'originalPrice', title:'商品原价', width:'20%'}
                        ,{field:'activityPrice', title:'活动特价', width:'10%'}
                        ,{field:'depositMoney', title:'预付定金', width:'10%'}
                        ,{field:'danwei', title:'单位', width:'10%'}
                        ,{field:'concernedPeople', title:'关注人数', width:'10%'}
                        ,{fixed: 'right', title:'操作', toolbar: '#barDemo', width:'20%'}
                    ]
                ]
                ,page: true
                /*  ,done: function(res, curr,count){
                     }*/
            });
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
                    url:"/delCommodity",
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
            debugger
            $("#myModal").modal('show');  //手动开启
            $("#myModalLabel").text("修改商品")  //手动开启
            $("input[name='flag']").val("2");
            $("input[name='id']").val(data.id);
            layedit.setContent(index,data.productDetails);
            $("input[name='commodityName']").val(data.commodityName);
            $("input[name='originalPrice']").val(data.originalPrice);
            $("input[name='danwei']").val(data.danwei);
            $("input[name='activityPrice']").val(data.activityPrice);
            $("input[name='depositMoney']").val(data.depositMoney);
            $("textarea[name='activityDescription']").val(data.activityDescription);
            $("#commodityCategoryId").find("option[value="+ data.categoryId+"]").prop("selected",true);
            $("#commodityStyleId").find("option[value="+ data.styleId+"]").prop("selected",true);
            form.render();


        }else{
            $("#myModalImg").modal('show');  //手动开启
            $("input[name='id']").val(data.id);
            $(".layui-upload-file").val("");
            demoListView.html("");
            table.render({
                elem: '#commodityImg'
                ,url:'/commodityImgList?commodityd='+data.id
                ,title: '用户数据表'
                ,cols: [
                    [
                        {field:'commodityImgUrl', title:'商品图片', width:400,templet:"#sexTpl"}
                        ,{fixed: 'right', title:'操作', toolbar: '#barDemoImg', width:200}
                    ]
                ]
                ,page: true
                /*   ,done: function(res, curr,count){
                       debugger
                       console.log(res)
                   }*/
            });
            table.on('tool(commodityImg)', function(obj){
                var data = obj.data;
                if(obj.event === 'delImg'){
                    layer.confirm('真的删除行么', function(index){
                        layer.close(index);
                        $.ajax({
                            type: "POST",
                            url:"/delCommodityImg",
                            data:{"id":data.id},
                            dataType: "json",
                            success: function (data) {
                                if(data>0){
                                    alert("删除图片成功")
                                    table.reload("commodityImg");
                                }else{
                                    alert("删除图片失败")
                                }
                            },
                            error: function () {
                            }
                        })

                    });
                }
            })
        }
    });
});

$("#form1").submit(function(){
    var yongjinContent=  layedit.getContent(index);
    if(yongjinContent==null||yongjinContent==""){
        alert("请输入产品详情!")
        return false;
    }
    var formData= new FormData(document.getElementById("form1"));
    formData.set("productDetails",yongjinContent);
    var flag=  $("input[name='flag']").val();
    if(flag==1){
        $.ajax({
            async: false,
            type: "POST",
            url:"/addCommodity",
            data:formData,
            dataType: "json",
            contentType: false, //不设置内容类型
            processData: false, //不处理数据
            success: function (data) {
                if(data==1){
                    alert("添加成功")
                    $("#myModal").modal('hide');  //手动关闭
                    table.reload("test");
                }else if(data==2){
                    alert("商品名称已经存在")
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
            url:"/updateCommodity",
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

//下拉框赋值
function getSelect(){
    var telphone= $("input[name='telphone']").val();
     $.ajax({
         type: "POST",
         url:"/findAllByCompanyId",
         data:{"telphone":telphone},
         dataType: "json",
         success: function (data){
             for(var i=0;i<data.length;i++){
                 $("#commodityCategoryId").append("<option value="+data[i].id +">"+ data[i].categoryName+"</option>");
             }
             form.render('select');
         },
         error: function () {
             alert("错误")
         }
     })

    $.ajax({
        type: "POST",
        url:"/findAllCommodityStyle",
        data:{"telphone":telphone},
        dataType: "json",
        success: function (data){
            for(var i=0;i<data.length;i++){
                $("#commodityStyleId").append("<option value="+data[i].id +">"+ data[i].typeName+"</option>");
            }
            form.render('select');
        },
        error: function () {
            alert("错误")
        }
    })
}