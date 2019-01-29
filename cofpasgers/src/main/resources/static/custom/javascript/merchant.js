$(function () {
    var merLogo = "";
    layui.use('upload', function () {
        var $ = layui.jquery, upload = layui.upload;
        //普通图片上传
        var uploadInst = upload.render({
            elem: '#merchant_logo'
            , url: '/upload/'
            , auto: false//选择完成之后不自动上传
            , choose: function (obj) {
                //将每次选择的文件追加到文件队列
                var files = obj.pushFile();

                //预读本地文件，如果是多文件，则会遍历。(不支持ie8/9)
                obj.preview(function (index, file, result) {
                    console.log(index); //得到文件索引
                    console.log(file); //得到文件对象
                    merLogo = file.name;
                    $('#logo_img').attr('src', result); //图片链接（base64）
                    //obj.resetFile(index, file, '123.jpg'); //重命名文件名，layui 2.3.0 开始新增

                    //这里还可以做一些 append 文件列表 DOM 的操作

                    //obj.upload(index, file); //对上传失败的单个文件重新上传，一般在某个事件中使用
                    //delete files[index]; //删除列表中对应的文件，一般在某个事件中使用
                });
            }
            , before: function (obj) {
                //预读本地文件示例，不支持ie8
                obj.preview(function (index, file, result) {
                    $('#logo_img').attr('src', result); //图片链接（base64）
                    logo_name = file.name;
                });
            }
            , done: function (res) {
                if (res.code > 0) {
                    uploaderror = true;
                }
                uploaderror = false;
            }
            , error: function () {
                uploaderror = true;
            }
        });
    });
    var table = $('#merchant_table_id').dataTable({
        "language": {
            "oPaginate": {
                "sFirst": "第一页",
                "sPrevious": " 上一页 ",
                "sNext": " 下一页 ",
                "sLast": " 最后一页 "
            },
            "sSearch": "搜索",
            "emptyTable": "无可用数据",
            "autoWidth": false,
        },
        "bInfo": false,
        "bLengthChange": false,
        // "serverSide": true,
        "ajax": {
            "url": "getMerInfo",
            "type": 'post',
            'data': {'login_id': getCookie("telphone")},
            // "data": function (data) {
            //     console.log("data");
            //     console.log(data);
            //     return data;
            // }
        },
        "columns": [
            {
                "data": function (obj) {
                    return ' <img class="am-radius" alt="140*140" src="http://s.amazeui.org/media/i/demos/bw-2014-06-19.jpg?imageView/1/w/1000/h/600/q/80" width="100" height="100"/>';
                }, "sClass": "textcenter"
            },
            {
                "data": function (obj) {
                    return obj.merName;
                }, "sClass": "textcenter"
            },
            {
                "data": function (obj) {
                    return obj.merAddress;
                }, "sClass": "textcenter"
            },
            {
                "data": function (obj) {
                    return obj.merUrl;
                }, "sClass": "textcenter"
            },
            {
                "data": function (obj) {
                    return obj.enterId;
                }, "sClass": "textcenter"
            },
            {
                "defaultContent": "<div class=\"am-btn-toolbar\">\n" +
                    "                            <div class=\"am-btn-group am-btn-group-xs\">\n" +
                    "                                <button type=\"button\" id=\"addAgent\" class=\"remove_btn am-btn am-btn-default am-btn-success\"\n" +
                    "                                        ><span class=\"am-icon-trash\"></span>删除\n" +
                    "                                </button>\n" +
                    "                            </div>\n" +
                    "                        </div>", "sClass": ""
            }
        ],
        "initComplete": function (settings, json) {
            $('#merchant_table_id tbody').on('click', 'tr', function (index) {
                $(this).addClass('selected');
                if ($(this).hasClass('selected')) {
                    var tr = table.fnGetData();
                    $(".remove_btn").click(function () {

                        console.log(tr);
                    });
                }
            });
        }
    });

    $('#add_merchant_btn').click(function () {
        var merchant_name = $('#merchant_name').val();
        var merchant_address = $('#merchant_address').val();
        var merchant_url = $('#merchant_url').val();
        var merchant_order = $('#merchant_order').val();
        var merchant_logo = merLogo;
        var enter_id = getCookie("telphone");
        $.post('addMerchant',
            {
                'merName': merchant_name,
                'merAddress': merchant_address,
                'merUrl': merchant_url,
                'merOrder': merchant_order,
                'merLogo': merchant_logo,
                'enterId': enter_id
            },
            function (resp) {
                if (resp.code == 1) {
                    //console.log("添加合作商家成功");
                    table.api().ajax.reload();
                } else {
                    //console.log("添加合作商家失败");
                    var pop = $('#add_merchant_btn').popover({
                        content: resp.msg,
                        theme: 'danger sm'
                    })
                    setTimeout(function () {
                        pop.popover('close');
                    }, 1000)
                }
            })
    })
})