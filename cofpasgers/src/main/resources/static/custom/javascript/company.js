$(function () {
    /**
     * 当是公司登陆的时候telphone就是Go公司的登陆ID
     * @type {*|string}
     */
    var company_login_id = getCookie("telphone");
    $.ajax({
        url: "get_current_com_info",
        type: 'post',
        data: {'user_id': company_login_id},
        success: function (resp) {
            $("#com_name").text(resp.enterName)
            $("#com_per").text(resp.enterLegalperson);
            $('#com_add').text(resp.enterAddress);
        }
    })
})