layui.use('upload', function () {
    var $ = layui.jquery, upload = layui.upload;
    var logo_name = null,uploaderror = false;

    function getMyDate(time) {
        var oDate = new Date(time),
            oYear = oDate.getFullYear(),
            oMonth = oDate.getMonth() + 1,
            oDay = oDate.getDate(),
            oHour = oDate.getHours(),
            oMin = oDate.getMinutes(),
            oSen = oDate.getSeconds(),
            oTime = oYear + '-' + getzf(oMonth) + '-' + getzf(oDay) + ' ' + getzf(oHour) + ':' + getzf(oMin) + ':' + getzf(oSen);//最后拼接时间
        return oTime;
    };

    //补0操作,当时间数据小于10的时候，给该数据前面加一个0
    function getzf(num) {
        if (parseInt(num) < 10) {
            num = '0' + num;
        }
        return num;
    }

    //普通图片上传
    var uploadInst = upload.render({
        elem: '#upload_logo'
        , url: '/upload/'
        , auto: false//选择完成之后不自动上传
        ,choose: function(obj){
            //将每次选择的文件追加到文件队列
            var files = obj.pushFile();

            //预读本地文件，如果是多文件，则会遍历。(不支持ie8/9)
            obj.preview(function(index, file, result){
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
            //如果上传失败
            if (res.code > 0) {
                uploaderror = true;
            }
            uploaderror = false;
            //上传成功
        }
        , error: function () {
            //演示失败状态，并实现重传
            // var demoText = $('#demoText');
            // demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
            // demoText.find('.demo-reload').on('click', function () {
            //     uploadInst.uploadfile();
            // });
            uploaderror = true;
        }
    });

    function createTable() {
        table = $('#enter_table').DataTable({
            language: {
                "processing": "正在加载中......",
                "processing": true,
                "zeroRecords": "对不起，查询不到相关数据！",
                "infoEmpty": "没找到数据",
                "emptyTable": "暂无数据",
                "zeroRecords": "没有找到记录",
                "oPaginate": {
                    "sFirst": "第一页",
                    "sPrevious": " 上一页 ",
                    "sNext": " 下一页 ",
                    "sLast": " 最后一页 "
                },
                "sSearch": "搜索",
            },
            "bInfo": false,
            "bLengthChange": false,
            "destroy": true,
            retrieve: true,
            // "bPaginate": false,
            "bPaginate": true,
            "iDisplayLength": 10,// 每页显示行数

            "ajax": {
                "url": "get_eateries",
                //"type":'post',
                "data": function (data) {
                    return data;
                }
            },
            "columns": [
                {
                    "data": "id",
                    // "name": "enterName",
                    // "sDefaultContent": "",  //默认空字符串
                    "sClass": "text-center"
                },
                {"data": "enterName"},
                {"data": "enterLoginName"},
                {
                    "data": function (obj) {
                        return obj.enterAddress;//通过调用函数来格式化所获取的时间戳
                    }
                }
                // }, {
                //     "defaultContent": "<button class='am-icon-remove remove_btn'></button>"
                // }
            ],
            // "initComplete": function (settings, json) {
            //     $('.remove_btn').click(function () {
            //         $('#enter_table tbody').on('click', 'tr', function () {
            //             $(this).addClass('selected');
            //             if ($(this).hasClass('selected')) {
            //                 //$(this).removeClass('selected');
            //                 table.row('.selected').remove().draw(false);
            //             } else {
            //                 table.$('tr.selected').removeClass('selected');
            //             }
            //         });
            //
            //     });
            // }
        });
    }

    function ClearContent() {
        $("#enter_name").val("");
        $("#enter_legal").val("");
        $("#enter_telphone").val("");
        $("#enter_address").val("");
        $("#establish_date").val("");
        $("#enter_status").val("");
        $("#enter_order").val("");
        $("#person_num").val("");
        $("#comment_id").val("");
    }

    function rePaintTables() {
        //tables.destroy();//销毁原先的table
        createTable();
        table.ajax.reload()
        //tables.draw(false);
    }

    createTable();


    $("#establish_date").datetimepicker({
        minView: "month",//设置只显示到月份
        format: 'yyyy-mm-dd',
        language: 'zh-CN',
        autoclose:true
    });

    $("#save_commpany_info_btn").click(function () {
        if (uploaderror) {
            return layer.msg("图片上传错误");
        }
        var enter_name = $("#enter_name").val();
        var enter_legal = $("#enter_legal").val();
        var enter_telphone = $("#enter_telphone").val();
        var enter_address = $("#enter_address").val();
        var establish_date = $("#establish_date").val();
        var enter_status = $("#enter_status").val();
        var enter_order = $("#enter_order").val();
        var person_num = $("#person_num").val();
        var comment_id = $("#comment_id").val();
        var logo = logo_name;
        var jsonData = {
            "enter_name": enter_name,
            "enter_legal": enter_legal,
            "enter_telphone": enter_telphone,
            "enter_address": enter_address,
            "establish_date": establish_date,
            "enter_status": enter_status,
            "enter_order": enter_order,
            "person_num": person_num,
            "comment_id": comment_id,
            "logo": logo
        }
        $.ajax({
            url: 'addeter',
            type: 'post',
            data: jsonData,
            success: function (data) {
                if (data == "true") {
                    rePaintTables();
                    ClearContent();
                    uploadInst.upload();
                    return layer.msg("添加企业成功");
                }
                rePaintTables();
                return layer.msg(data);
            }
        })
    });
    //layui use
})

