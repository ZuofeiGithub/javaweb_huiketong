;(function () {
    $.Extension = function () {
        var m_table = null;
        var m_json = new Array();
        var m_upload_success = false;

        function set_success(isSuccess) {
            m_upload_success = isSuccess;
        }

        // this.get_success = function() {
        //     return m_upload_success;
        // }
        this.uploadFile = function () {
            layui.use('upload', function () {
                var $ = layui.jquery, upload = layui.upload;
                var extenListView = $('#extension_List')
                    , uploadListIns = upload.render(
                    {
                        elem: '#testList',
                        url: '/upload/',
                        accept: 'file',
                        multiple: true,
                        auto: false,
                        bindAction: '#extension_submit',
                        choose: function (obj) {
                            var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
                            //读取本地文件
                            obj.preview(function (index, file, result) {
                                var tr = $(['<tr id="uploadfile-' + index + '">'
                                    , '<td>' + file.name + '</td>'
                                    , '<td>' + (file.size / 1014).toFixed(1) + 'kb</td>'
                                    , '<td>等待上传</td>'
                                    , '<td><input type="text" value="" placeholder="输入图片链接地址"></td>'
                                    , '<td><input type="text" value="" placeholder="输入图片描述"></td>'
                                    , '<td>'
                                    , '<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
                                    , '<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
                                    , '</td>'
                                    , '</tr>'].join(''));

                                //单个重传
                                tr.find('.demo-reload').on('click', function () {
                                    obj.upload(index, file);
                                });

                                //删除
                                tr.find('.demo-delete').on('click', function () {
                                    delete files[index]; //删除对应的文件
                                    tr.remove();
                                    uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                                });

                                extenListView.append(tr);
                            });
                        }
                        , done: function (res, index, upload) {
                            if (res.code == 0) { //上传成功
                                var tr = extenListView.find('tr#uploadfile-' + index)
                                    , tds = tr.children();
                                tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
                                var extension_info = {
                                    filename: tds.eq(0).text(),
                                    pic_url: tds.eq(3).children().val(),
                                    pic_desc: tds.eq(4).children().val()
                                };
                                // if (trim(extension_info.pic_url) == "" || trim(extension_info.pic_desc) == "") {
                                //     set_success(false)
                                // } else {
                                set_success(true);
                                // }
                                m_json.push(extension_info);
                                tds.eq(5).html(''); //清空操作
                                return delete this.files[index]; //删除文件队列已经上传成功的文件
                            }
                            this.error(index, upload);
                        }
                        , error: function (index, upload) {
                            set_success(false);
                            var tr = extenListView.find('tr#uploadfile-' + index)
                                , tds = tr.children();
                            tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
                            tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
                        }
                    });
            });
        };
        /**
         * cookie操作
         */
        this.SetCookie = function setCookie(name, value, iDay) {
            var oDate = new Date();
            oDate.setDate(oDate.getDate() + iDay);
            document.cookie = name + '=' + value + ';expires=' + oDate;
        };
        /**
         * 获取Cookie
         */
        this.GetCookie = function getCookie(name) {
            var arr = document.cookie.split('; ');
            for (var i = 0; i < arr.length; i++) {
                var arr2 = arr[i].split('=');
                if (trim(arr2[0]) === name) {
                    return unescape(trim(arr2[1]));
                }
            }
            return '';
        };

        this.RemoveCookie = function removeCookie(name) {
            this.SetCookie(name, 1, -1);
        };

        function trim(str) {
            return str.toString().replace(/(^\s*)|(\s*$)/g, "");
        };

        this.AddListener = function (id) {
            $(id).click(function () {
                var $modal = $('#my-popup');
                $modal.modal({width: '900px;'})
            })
        };

        this.InitDatable = function () {
            var table = $('#extension_table_id').dataTable({
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
                    "url": "get_extension_info",
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
                    //console.log("渲染完成")
                    $(".remove_btn").click(function () {
                        var $this = $(this);
                        var isclick = true;
                        $('#extension_table_id tbody').on('click', 'tr', function (index) {
                            $(this).addClass('selected');
                            if ($(this).hasClass('selected')) {
                                var tr = table.fnGetData();
                                $.ajax({
                                    url: 'removeextensionbyid',
                                    type: 'delete',
                                    data: {'extension_id': tr[0]['extensionId']},
                                    success: function (resp) {
                                        table.api().row('.selected').remove().draw(false);
                                    }
                                })
                            }
                        });
                    });
                },
                "fnDrawCallback": function () {

                }
            });
            m_table = table;
        };
        this.Table = function () {
            return m_table;
        };

        this.SubmitExtension = function () {

            $('#extension_submit').click(function () {
                if (m_upload_success) {
                    var extension_name = $('#extension_name').val();
                    var extension_desc = $('#extension_desc').val();
                    var company_loginid = getCookie('telphone');
                    if (trim(extension_name) == "") {
                        return alert("输入分享标题")
                    }
                    if (trim(extension_desc) == "") {
                        return alert("输入分享内容")
                    }
                    $.ajax({
                        url: 'add_extension',
                        type: 'post',
                        data: {
                            'extension_name': extension_name,
                            'extension_desc': extension_desc,
                            'company_login_id': company_loginid
                        },
                        success: function (resp1) {
                            //console.log("resp1=" + resp1.code)
                        }
                    })
                } else {
                    //console.log("添加分享内容失败");
                }
            });


        }

        return this;
    };
})(jQuery);

$(function () {
    var extension = new $.Extension();
    extension.AddListener("#addextension");
    extension.uploadFile();
    extension.SubmitExtension();
    var $textArea = $('[name=myue]');
    var editor = UE.getEditor('myue');
    var $form = $('#ue-form');

    $form.validator({
        submit: function () {
            // 同步编辑器数据
            editor.sync();

            var formValidity = this.isFormValid();

            // 表单验证未成功，而且未成功的第一个元素为 UEEditor 时，focus 编辑器
            if (!formValidity && $form.find('.' + this.options.inValidClass).eq(0).is($textArea)) {
                editor.focus();
            }

            console.warn('验证状态：', formValidity ? '通过' : '未通过');

            return false;
        }
    });

    // 编辑器内容变化时同步到 textarea
    editor.addListener('contentChange', function () {
        editor.sync();

        // 触发验证
        $('[name=myue]').trigger('change');
    });
})


