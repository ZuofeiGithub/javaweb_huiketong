var table = $("#cus_table").DataTable({
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
        "url": "getcustomerinfo",
        //"type":'post',
        "data": function (data) {
            return data;
        }
    },
    "columns": [
        {"data": "customer_name", "sClass": "myClassCenter"},
        {"data": "telphone", "sClass": "myClassCenter"},
        {"data": "detail_address", "sClass": "myClassCenter"},
        {"data": "type", "sClass": "myClassCenter"},
        {"data": "scheme", "sClass": "myClassCenter"},
        {"data": "recom_datetime", "sClass": "myClassCenter"},
        {"defaultContent": "<button class='remove_btn am-btn am-btn-primary'>报备</button>", "sClass": "myClassCenter"}
    ],
    "initComplete": function (settings, json) {
        $(".remove_btn").click(function () {
            var $this = $(this);
            var isclick = true;
            $('#cus_table tbody').on('click', 'tr', function (index) {
                $(this).addClass('selected');
                // if ($(this).hasClass('selected')) {
                //     //$(this).removeClass('selected');
                //     //table.row('.selected').remove().draw(false);
                //     var t = table.fnGetData;
                //     console.log(t);
                // } else {
                //     table.$('tr.selected').removeClass('selected');
                // }
                if (isclick) {
                    var data = table.rows(['.selected']).data()[0];
                    if (confirm("是否认证")) {
                        $.ajax({
                            url: '',
                            type: 'post',

                        })
                        index.target.innerText = "已认证";
                        //$this.attr("disabled","disabled");
                        $this.addClass("am-disabled");
                    }
                    isclick = false;
                }

                table.row('.selected').draw(true);
                table.$('tr.selected').removeClass('selected');
            });
        });
    }
});

