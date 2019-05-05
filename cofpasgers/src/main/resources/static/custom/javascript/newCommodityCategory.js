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

    $("input[name='telphone']").val(getCookie("telphone"));
    table.render({
        elem: '#test'
        , url: '/commodityCategoryList?telphone=' + getCookie("telphone")
        , toolbar: '#toolbarDemo'
        , title: '用户数据表'
        , cols: [
            [
                {field: 'categoryName', title: '商品品类名称', width: 300}
                , {fixed: 'right', title: '操作', toolbar: '#barDemo', width: 300}
            ]
        ]
        , page: true
        /* ,done: function(res, curr,count){
                debugger
                console.log(res)
            }*/
    });

    //头工具栏事件
    table.on('toolbar(test)', function (obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        if (obj.event === "addCommodityCategory") {
            var data = checkStatus.data;
            $("#myModal").modal('show');
            $("#myModalLabel").text("添加商品品类")  //手动开启
            $("input[name='flag']").val("1");
            $("input[name='categoryName']").val("");
        }
    });

    //监听行工具事件
    table.on('tool(test)', function (obj) {
        var data = obj.data;
        if (obj.event === 'del') {
            layer.confirm('真的删除行么', function (index) {
                layer.close(index);
                $.ajax({
                    type: "POST",
                    url: "/delCommodityCategoryList",
                    data: {"id": data.id},
                    dataType: "json",
                    success: function (data) {
                        if (data > 0) {
                            alert("删除成功")
                            $("#myModal").modal('hide');  //手动关闭
                            table.reload("test");
                        } else {
                            alert("删除失败")
                        }
                    },
                    error: function () {
                    }
                })
            });
        } else if (obj.event === 'edit') {
            $("#myModal").modal('show');
            $("#myModalLabel").text("修改商品品类")  //手动开启
            $("input[name='flag']").val("2");
            $("input[name='categoryName']").val(data.categoryName);
            $("input[name='id']").val(data.id);
        }
    });
})

$("#form1").submit(function () {
    var formData = new FormData(document.getElementById("form1"));
    var flag = $("input[name='flag']").val();
    if (flag == 1) {
        $.ajax({
            async: false,
            type: "POST",
            url: "/addCommodityCategoryList",
            data: formData,
            dataType: "json",
            contentType: false, //不设置内容类型
            processData: false, //不处理数据
            success: function (data) {
                if (data == 1) {
                    alert("添加成功")
                    $("#myModal").modal('hide');  //手动关闭
                    table.reload("test");
                } else if (data == 2) {
                    alert("商品种类已经存在")
                } else {
                    alert("添加失败")
                }
            },
            error: function () {
            }
        })
    } else {
        $.ajax({
            async: false,
            type: "POST",
            url: "/updateCommodityCategoryList",
            data: formData,
            dataType: "json",
            contentType: false, //不设置内容类型
            processData: false, //不处理数据
            success: function (data) {
                if (data > 0) {
                    alert("修改成功")
                    $("#myModal").modal('hide');  //手动关闭
                    table.reload("test");
                } else {
                    alert("修改失败")
                }
            },
            error: function () {
            }
        })
    }

})
