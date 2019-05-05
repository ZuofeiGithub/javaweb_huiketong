var table;
layui.use(['form', 'table'], function () {
    var form = layui.form;
    table = layui.table;
    form.render();
    form.verify({
        pass: [
            /^\d+$/
            , '排序字段必须为纯数字'
        ]
    });

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
        , url: '/shnagjiaList?telphone=' + getCookie("telphone")
        , toolbar: '#toolbarDemo'
        , title: '用户数据表'
        , cols: [
            [
                {field: 'merName', title: '商家名称', width: '30%'}
                , {field: 'merAddress', title: '商家地址', width: '20%'}
                , {field: 'descript', title: '商家标语', width: '10%'}
                , {field: 'merUrl', title: '网址URL', width: '15%', templet: '#urlTpl'}
                , {field: 'enterId', title: '企业绑定账号', width: '15%'}
                , {fixed: 'right', title: '操作', toolbar: '#barDemo', width: '10%'}
            ]
        ]
        /* ,page: true
          ,done: function(res, curr,count){
           }*/
    });
    //头工具栏事件
    table.on('toolbar(test)', function (obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        if (obj.event === 'addYongjin') {
            $("#myModal").modal('show');  //手动开启
            $("#myModalLabel").text("添加合作商家")  //手动开启
            $("input[name='merName']").val("");
            $("input[name='merAddress']").val("");
            $("input[name='merUrl']").val("");
            $("input[name='merOrder']").val("");
            $("input[name='descript']").val("");
            $("input[name='flag']").val("1");
            $("#myfile").val("");
            $("#previewImg").attr('src', "http://www");
            $("#previewImg").css("display", 'none');
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
                    url: "/deleteShangJia",
                    data: {"id": data.merId},
                    dataType: "json",
                    success: function (data) {
                        if (data > 0) {
                            alert("删除成功")
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
            $("#myModal").modal('show');  //手动开启
            $("#myModalLabel").text("修改合作商家")  //手动开启
            $("input[name='merName']").val(data.merName);
            $("input[name='merAddress']").val(data.merAddress);
            $("input[name='merUrl']").val(data.merUrl);
            $("input[name='merOrder']").val(data.merOrder);
            $("input[name='descript']").val(data.descript);
            $("input[name='flag']").val("2");
            $("input[name='id']").val(data.merId);
            $("#previewImg").attr('src', data.merLogo);
            $("#previewImg").css("display", 'block');
            $("#myfile").val("");
        }
    });
});

$("#myfile").change(function () {
    var imgPath = getObjectURL(this.files[0]);
    $("#previewImg").attr({
        src: imgPath
    })
    $("#previewImg").css("display", 'block');
});

function getObjectURL(file) {
    var url = null;
    if (window.createObjectURL != undefined) {
        url = window.createObjectURL(file);
    } else if (window.URL != undefined) {
        url = window.URL.createObjectURL(file);
    } else if (window.webkitURL != undefined) {
        url = window.webkitURL.createObjectURL(file);
    }
    return url;
}

$("#form1").submit(function () {
    var imgSrc = $('#previewImg')[0].src;
    if (imgSrc == "http://www/") {
        alert("请选择商家LOGO!")
        return false;
    }
    var formData = new FormData(document.getElementById("form1"));
    formData.set("imgSrc", imgSrc);
    var flag = $("input[name='flag']").val();
    if (flag == 1) {
        $.ajax({
            async: false,
            type: "POST",
            url: "/uploadShangJiaInfo",
            data: formData,
            dataType: "json",
            contentType: false, //不设置内容类型
            processData: false, //不处理数据
            success: function (data) {
                if (data > 0) {
                    alert("添加成功")
                    $("#myModal").modal('hide');  //手动关闭
                    table.reload("test");
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
            url: "/updateShangJia",
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

$("#search").on("click", function () {
    var searchMerName = $("input[name='searchMerName']").val();
    table.reload('test', {
        where: { //设定异步数据接口的额外参数，任意设
            searchMerName: searchMerName
        }
        , page: {
            curr: 1 //重新从第 1 页开始
        }
    });

})