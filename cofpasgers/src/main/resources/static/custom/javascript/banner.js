$(function () {
    /**
     * cookie操作
     */
    function setCookie(name, value, iDay) {
        var oDate = new Date();
        oDate.setDate(oDate.getDate() + iDay);
        document.cookie = name + '=' + value + ';expires=' + oDate;
    }

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

    function removeCookie(name) {
        setCookie(name, 1, -1);
    }

    function trim(str) {
        return str.toString().replace(/(^\s*)|(\s*$)/g, "");
    }

    var json = new Array();
    var $modal = $('#my-popup');
    $('#addBanner').click(function () {

        $modal.modal({width: '650px;',show: true,
            backdrop: 'static'})
    })

    layui.use('upload', function () {
        var $ = layui.jquery, upload = layui.upload;
        var fileListView = $('#fileList')
            , uploadListIns = upload.render({
            elem: '#testList'
            , url: '/upload/'
            , accept: 'file'
            , multiple: true
            , auto: false
            , choose: function (obj) {
                var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
                //读取本地文件
                obj.preview(function (index, file, result) {
                    var tr = $(['<tr id="uploadfile-' + index + '">'
                        , '<td>' + file.name + '</td>'
                        // ,'<td><img src="' +result+ '"></td>'
                        , '<td>' + (file.size / 1014).toFixed(1) + 'kb</td>'
                        , '<td>等待上传</td>'
                        , '<td><input type="text" value="" placeholder="输入图片链接地址"></td>'
                        , '<td><input type="text" value="" placeholder="输入图片描述"></td>'
                        , '<td>'
                        , '<button class="layui-btn layui-btn-xs pic-reload layui-hide">重传</button>'
                        , '<button class="layui-btn layui-btn-xs layui-btn-danger pic-delete">删除</button>'
                        , '</td>'
                        , '</tr>'].join(''));

                    //单个重传
                    tr.find('.pic-reload').on('click', function () {
                        obj.upload(index, file);
                    });

                    //删除
                    tr.find('.pic-delete').on('click', function () {
                        delete files[index]; //删除对应的文件
                        tr.remove();
                        uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                    });

                    fileListView.append(tr);
                });
            }
            , done: function (res, index, upload) {
                if (res.code == 0) { //上传成功
                    var tr = fileListView.find('tr#uploadfile-' + index)
                        , tds = tr.children();
                    tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
                    var bannerInfo = {
                        filename: tds.eq(0).text(),
                        pic_url: tds.eq(3).children().val(),
                        pic_desc: tds.eq(4).children().val()
                    };
                    layer.msg("上传成功");
                    json.push(bannerInfo);
                    tds.eq(5).html(''); //清空操作
                    return delete this.files[index]; //删除文件队列已经上传成功的文件
                }
                this.error(index, upload);
            }
            , error: function (index, upload) {
                var tr = fileListView.find('tr#uploadfile-' + index)
                    , tds = tr.children();
                tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
                tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
            }
        });

        $('#banner_submit').click(function () {
            var title = $('#banner_name').val();
            var desc = $('#banner_desc').val();
            var user_id = getCookie('telphone');
            if (trim(title) == "") {
                return layer.msg("轮播图标题不能为空");
            }

            if(trim(desc) == ""){
                return layer.msg("内容描述不能为空");
            }

            uploadListIns.upload();
            setTimeout(function () {
                var dataJson = json;
                // json.length = 0;
                $modal.modal("close");
            },3000);

            // if (trim(banner_desc) == "") {
            //     return alert("输入Banner描述")
            // }
            //
            // if (json.length > 0) {
            //     $.ajax({
            //         url: 'add_banner',
            //         type: 'post',
            //         data: {'banner_name': banner_name, 'banner_desc': banner_desc, 'company_login_id': company_loginid},
            //         success: function (resp1) {
            //             console.log("resp1=" + resp1.code)
            //             if (resp1.code == 1) {
            //                 console.log("有了resp1响应")
            //                 for (var i = 0; i < json.length; i++) {
            //                     $.ajax({
            //                         url: 'add_bannercontext',
            //                         type: 'post',
            //                         data: json[i],
            //                         success: function (resp2) {
            //                             console.log("resp2=" + resp2.code)
            //                             if (resp2.code == 1) {
            //                                 console.log("添加轮播图成功");
            //                                 table.api().ajax.reload();
            //                                 var $modal = $('#my-popup');
            //                                 $modal.modal('close');
            //                             }
            //                         }
            //                     })
            //                 }
            //
            //             } else {
            //                 $.ajax({
            //                     url: 'removebanner',
            //                     type: 'delete',
            //                     data: {'banner_name': banner_name},
            //                     success: function (resp3) {
            //                         if (resp3.code == 1) {
            //                             console.log("删除成功");
            //                             table.api().ajax.reload();
            //                         }
            //                     }
            //                 })
            //             }
            //         }
            //     })
            // } else {
            //     console.log("添加轮播图失败");
            // }
        });

    });

    var table = $('#banner_table_id').dataTable({
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
        "sScrollY": "600px",
        "bScrollCollapse": true,
        "bInfo": false,
        "bLengthChange": false,
        // "serverSide": true,
        "ajax": {
            "url": "get_banner_info",
            //"type":'post',
            "data": function (data) {
                return data;
            }
        },
        "columns": [
            {"data": "name", "sClass": "textcenter"},
            {"data": "descript", "sClass": "textcenter"},
            {
                "data": function (obj) {
                    return getMyDate(obj.create_date);
                }, "sClass": "textcenter"
            },
            {"data": "status", "sClass": "textcenter"},
            {
                "defaultContent": "<div class=\"am-btn-toolbar\">\n" +
                    "                            <div class=\"am-btn-group am-btn-group-xs\">\n" +
                    "                                <button type=\"button\" id=\"addAgent\" class=\"remove_btn am-btn am-btn-default am-btn-success\"\n" +
                    "                                        ><span class=\"am-icon-trash\"></span>删除\n" +
                    "                                </button>\n" +
                    "                            </div>\n" +
                    "                        </div>", "sClass": "margcent"
            }
        ],
        "initComplete": function (settings, json) {
            $(".remove_btn").click(function () {
                $('#banner_table_id tbody').on('click', 'tr', function (index) {
                    $(this).addClass('selected');
                    if ($(this).hasClass('selected')) {
                        var tr = table.fnGetData();
                        $.ajax({
                            url: 'removebannerbyid',
                            type: 'delete',
                            data: {'banner_id': tr[0]['bannerId']},
                            success: function (resp) {
                                table.api().row('.selected').remove().draw(false);
                            }
                        })
                    }

                });
            });
        },
        "fnPreDrawCallback": function () {
            return true;
        },
        "fnDrawCallback": function () {
        }
    });
})