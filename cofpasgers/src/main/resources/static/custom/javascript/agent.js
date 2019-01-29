$(function () {
    var user_id = getCookie("telphone");
    // $.ajax({
    //     url:'get_company_agent',
    //     type:'post',
    //     data:{'user_id':user_id},
    //     success:function (resp) {
    //     }
    // })

    $('#datetimepicker').datetimepicker({
        format: 'yyyy-mm-dd hh:ii',
        language: 'zh-CN'
    });
    $('#agent_table_id').DataTable({
        language: {
            // "sLengthMenu": "每页显示 _MENU_ 条记录",
            "sZeroRecords": "对不起，查询不到任何相关数据",
            // "sInfoEmtpy": "找不到相关数据",
            // "sInfoFiltered": "数据表中共为 _MAX_ 条记录)",
            // "sProcessing": "正在加载中...",
            //
            // "Copy": "拷贝",
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
        "bFilter": true,
        "bPaginate": true,
        "iDisplayLength": 2,// 每页显示行数

        "ajax": {
            "url": "get_company_agent",
            "type": 'post',
            "data": {'user_id': user_id}
            // "data": function (data) {
            //     return data;
            // }
        },
        "columns": [
            {"data": "agentName", "sClass": "myClassCenter"},
            {
                "data": function (obj) {
                    if (obj.type == 0) {
                        return "公司员工"
                    } else if (obj.type == 1) {
                        return "企业"
                    }
                }, "sClass": "myClassCenter"
            },
            {"data": "imposer", "sClass": "myClassCenter"},
            {"data": "telphone", "sClass": "myClassCenter"},
            {"data": "initCode", "sClass": "myClassCenter"},
            {"data":function (obj) {
                    return getMyDate(obj.regDatetime);//通过调用函数来格式化所获取的时间戳
                }, "sClass": "myClassCenter"},
            {"data": "superiorName", "sClass": "myClassCenter"},
            {"data": "topName", "sClass": "myClassCenter"},
            {"data": "reconCustomNam", "sClass": "myClassCenter"},
            {"data": "dealCustomNum", "sClass": "myClassCenter"},
            {"data": "initAgentNam", "sClass": "myClassCenter"},
            {"data": "reconProfitMoney", "sClass": "myClassCenter"},
            {"data": "initProfitMoney", "sClass": "myClassCenter"},
            {"data": "accountBalance", "sClass": "myClassCenter"},
            {
                "defaultContent": "<div class=\"am-btn-toolbar\">\n" +
                    "                            <div class=\"am-btn-group am-btn-group-xs\">\n" +
                    "                                <button type=\"button\" class=\"am-btn am-btn-default am-btn-success\"\n" +
                    "                                        data-am-modal=\"{target: '#my-popup'}\">认证\n" +
                    "                                </button>\n" +
                    "                            </div>\n" +
                    "                        </div>",
                "sClass": "myClassCenter"
            }
        ]
    });

    layui.use('layer', function () {
        var $ = layui.jquery, layer = layui.layer;
        $('#addAgent').click(function () {
            layer.open({
                title: "添加内部经纪人",
                type: 2
                , offset: '20px',
                area: ['400px', '750px'],
                content: 'addagent',
                success: function (layero, index) {
                    layer.iframeAuto(index);
                },
                resize: false,
                btnAlign: 'c'
                , shade: 0
            });
        })
    });
});
// $.getScript("../assets/js/amazeui.min.js");
