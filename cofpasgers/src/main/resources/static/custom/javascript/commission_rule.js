$(function () {
    $.post("getcommission", {'login_id': getCookie('telphone')}, function (data, status, xhr) {
        //console.log(status);
        $('#commission_score').val(data.score);
    })

    $('#modify_commission_btn').click(function () {
        var score = $('#commission_score').val();
        $.post("modifycommission", {'login_id': getCookie('telphone'), 'score': score}, function (data) {
            console.log(data.msg);
        });
    })
})