var MyCrontab = {};

layui.use(['layer', 'jquery'], function () {
    var layer = layui.layer, $ = layui.$

    function checkLastItem(arr, i) {
        return arr.length == (i + 1);
    }

    function addMenuItem() {
        $.ajax({
            url: "/sys/menus",
            method: 'POST',
            success: function (res) {
                //console.log(res);
                var html = "";
                for (var i = 0; i < res.data.length; i++) {
                    var strli = '<li class="active">';
                    //console.log(res.data[i].name)
                    if (res.data[i].url == '#') {
                        strli = strli + '<a class="J_menuItem" href="javascript:;" name="tabMenuItem" data-index="6"><i class="fa fa-rocket"></i><span class="nav-label">' + res.data[i].name + '</span></a>';

                    } else {
                        strli = strli + '<a class="J_menuItem" href="' + res.data[i].url + '" name="tabMenuItem" data-index="7"><i class="fa fa-rocket"></i><span class="nav-label">' + res.data[i].name + '</span></a>';
                    }
                    // if(res[i].pId == "0" && !checkLastItem(res, i) && res[i + 1].pId != "0") {
                    //     strli = strli + "<dl class=\"layui-nav-child\" >";
                    //     for(; !checkLastItem(res, i) && res[i + 1].pId != "0"; i++) {
                    //         strli = strli + "<dd>"+getAhtml(res[i+1])+"</dd>";
                    //     }
                    //     strli = strli + "</dl>";
                    // }
                    strli = strli + "</li>";
                    html = html + strli;
                }
                $("#menu").append(html);
            }
        })
    }

    //addMenuItem();
});

/**
 * 滚动到指定选项卡
 */
MyCrontab.scrollToTab = function (element) {
    var marginLeftVal = MyCrontab.calSumWidth($(element).prevAll()),
        marginRightVal = MyCrontab.calSumWidth($(element).nextAll());
    // 可视区域非tab宽度
    var tabOuterWidth = MyCrontab.calSumWidth($(".content-tabs").children().not(".J_menuTabs"));
    //可视区域tab宽度
    var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
    //实际滚动宽度
    var scrollVal = 0;
    if ($(".page-tabs-content").outerWidth() < visibleWidth) {
        scrollVal = 0;
    } else if (marginRightVal <= (visibleWidth - $(element).outerWidth(true) - $(element).next().outerWidth(true))) {
        if ((visibleWidth - $(element).next().outerWidth(true)) > marginRightVal) {
            scrollVal = marginLeftVal;
            var tabElement = element;
            while ((scrollVal - $(tabElement).outerWidth()) > ($(".page-tabs-content").outerWidth() - visibleWidth)) {
                scrollVal -= $(tabElement).prev().outerWidth();
                tabElement = $(tabElement).prev();
            }
        }
    } else if (marginLeftVal > (visibleWidth - $(element).outerWidth(true) - $(element).prev().outerWidth(true))) {
        scrollVal = marginLeftVal - $(element).prev().outerWidth(true);
    }
    $('.page-tabs-content').animate({
        marginLeft: 0 - scrollVal + 'px'
    }, "fast");
};


/**
 * 计算元素集合的总宽度
 */
MyCrontab.calSumWidth = function (elements) {
    var width = 0;
    $(elements).each(function () {
        width += $(this).outerWidth(true);
    });
    return width;
};


$(function () {
    function Jurisdiction() {
        var usertype = getCookie("usertype");
        var user_id = getCookie("telphone");
        var headphoto = getCookie('headphoto');
        if (usertype == "2") {
            $('#top_item').text("系統管理")
            $('#menu').append('<li><a class="J_menuItem" href="enterprise?user_id='+user_id+'" name="tabMenuItem" data-index="6">企业管理</a></li>')
        } else if (usertype == "3") {
            $('#top_item').text("企业管理")
            $('#menuicon').removeClass("fa-user").addClass("fa-sitemap")
            $('#headphoto').attr('src', 'http://image.jkt365.com/' + headphoto)
            // $('#menu li a').attr('href', 'agent').attr('data-index', "5").text("经纪人管理");
            $('#menu')
                .append('<li><a class="J_menuItem" id="enterId" href="enterinfomain" name="tabMenuItem" data-index="6">企业信息</a></li>')
                .append('<li><a class="J_menuItem" href="lunBoJsp" name="tabMenuItem" data-index="6">轮播图</a></li>')
                .append('<li><a class="J_menuItem layui-hide" href="banner_url_context_edit" name="tabMenuItem" data-index="6">轮播图跳转内容管理</a></li>')
                // .append('<li><a class="J_menuItem" href="agent" name="tabMenuItem" data-index="6">经纪人管理</a></li>')
                // .append('<li><a class="J_menuItem" href="customer" name="tabMenuItem" data-index="6">客户管理</a></li>')
                .append('<li><a class="J_menuItem" href="yongjinJsp" name="tabMenuItem" data-index="6">分享赚佣金</a></li>')
                .append('<li><a class="J_menuItem layui-hide" href="shnagjiaJsp" name="tabMenuItem" data-index="6">合作商家</a></li>')
                .append('<li><a class="J_menuItem" id="rule_comm" href="yongjinGuiZeJsp" name="tabMenuItem" data-index="6">佣金规则</a></li>')
                .append('<li><a class="J_menuItem" id="fenyong" href="fenyongJsp" name="tabMenuItem" data-index="6">分佣指南</a></li>')
                .append('<li><a class="J_menuItem layui-hide" id="integral" href="integralRuleJsp" name="tabMenuItem" data-index="6">积分规则</a></li>')
                .append('<li><a class="J_menuItem layui-hide" id="cus_ser" href="cus_ser_list" name="tabMenuItem" data-index="6">客服</a></li>')
                .append('<li><a class="J_menuItem" id="extend" href="exten" name="tabMenuItem" data-index="6">推广活动</a></li>')
                .append('<li><a class="J_menuItem" id="extend" href="onlinecollege" name="tabMenuItem" data-index="6">在线学院</a></li>');

            $("#rule_comm").attr("href", 'yongjinGuiZeJsp?user_id=' + user_id);
            $("#enterId").attr("href", "enterinfomain?user_id=" + user_id);
            $("#integral").attr("href", "integralRuleJsp?user_id=" + user_id);
            $("#fenyong").attr("href", "fenyongJsp?user_id=" + user_id);

            $("#side-menu").append('<li id="broker"></li>')
            $('#broker').append('<a><i class="fa fa-user-secret fa-fw" style="margin-right: 5px"></i><span class="nav-label">经纪人管理</span><span class="fa fa-angle-left" id="arrowId1" style="float: right"></span></a>')
            $("#broker").append('<ul class="nav nav-second-level collapse" aria-expanded="true" id="menu4" style=""></ul>');
            $('#menu4').append('<li><a class="J_menuItem" href="agent" name="tabMenuItem" data-index="6">经纪人</a></li>');
            $('#menu4').append('<li><a class="J_menuItem" href="agentsort" name="tabMenuItem" data-index="6">经纪人排行</a></li>');

            $("#side-menu").append('<li id="customer"></li>')
            $('#customer').append('<a><i class="fa fa-user fa-fw" style="margin-right: 5px"></i><span class="nav-label">客户管理</span><span class="fa fa-angle-left" id="arrowId1" style="float: right"></span></a>')
            $("#customer").append('<ul class="nav nav-second-level collapse" aria-expanded="true" id="menu5" style=""></ul>');
            $('#menu5').append('<li><a class="J_menuItem" href="customer" name="tabMenuItem" data-index="6">客户</a></li>');
            $('#menu5').append('<li><a class="J_menuItem" href="dealcustomer" name="tabMenuItem" data-index="6">已签约客户</a></li>');

            $("#side-menu").append('<li id="caiwu"></li>');
            $("#caiwu").append('<a><i class="fa fa-money fa-fw" style="margin-right: 5px"></i><span class="nav-label">财务管理</span><span class="fa fa-angle-left" id="arrowId1" style="float: right"></span></a>');
            $("#caiwu").append('<ul class="nav nav-second-level collapse" aria-expanded="true" id="menu1" style=""></ul>');
            $("#menu1").append('<li><a class="J_menuItem" href="jiangliJsp" name="tabMenuItem" data-index="5">奖励明细</a></li>');
            $("#menu1").append('<li><a class="J_menuItem" href="tixianJsp" name="tabMenuItem" data-index="5">提现明细</a></li>');
            $("#menu1").append('<li><a class="J_menuItem" href="tixianspJsp" name="tabMenuItem" data-index="5">提现审批</a></li>');

            $("#side-menu").append('<li id="caiwu1"></li>');
            $("#caiwu1").append('<a><i class="fa fa-gift fa-fw" style="margin-right: 5px;"></i><span class="nav-label">商品管理</span><span class="fa fa-angle-left" id="arrowId2" style="float: right"></span></a>');
            $("#caiwu1").append('<ul class="nav nav-second-level collapse" aria-expanded="true" id="menu2" style=""></ul>');
            $("#menu2").append('<li><a class="J_menuItem layui-hide" href="commodityCategoryJsp" name="tabMenuItem" data-index="5">品类管理</a></li>')
                .append('<li><a class="J_menuItem layui-hide" href="commodityStyleJsp" name="tabMenuItem" data-index="5">风格管理</a></li>')
                .append('<li><a class="J_menuItem" href="commodityJsp" name="tabMenuItem" data-index="5">分享商品</a></li>')
                .append('<li><a class="J_menuItem layui-hide" href="orderManagementJsp" name="tabMenuItem" data-index="5">订单管理</a></li>');

            $("#side-menu").append('<li id="voucher"></li>');
            $("#voucher").append('<a><i class="fa fa-heart fa-fw" style="margin-right: 5px"></i><span class="nav-label">优惠券</span><span class="fa fa-angle-left" id="arrowId3" style="float: right"></span></a>');
            $("#voucher").append('<ul class="nav nav-second-level collapse" aria-expanded="true" id="menu3" style=""></ul>');
            $("#menu3").append('<li><a class="J_menuItem" id="vouchershareid" href="/voucher_share" name="tabMenuItem" data-index="5">优惠券管理</a></li>');

            $('#menu1').on('hidden.bs.collapse', function () {
                $('#arrowId1').removeClass('fa-angle-down').addClass('fa-angle-left');
                $('#caiwu').removeClass("active")
            }).on('show.bs.collapse', function () {
                $('#arrowId1').removeClass('fa-angle-left').addClass('fa-angle-down');
                $('#caiwu').addClass("active")
            })
            $('#menu2').on('hidden.bs.collapse', function () {
                $('#arrowId2').removeClass('fa-angle-down').addClass('fa-angle-left');
            }).on('show.bs.collapse', function () {
                $('#arrowId2').removeClass('fa-angle-left').addClass('fa-angle-down');
            })
            $('#menu3').on('hidden.bs.collapse', function () {
                $('#arrowId3').removeClass('fa-angle-down').addClass('fa-angle-left');
            }).on('show.bs.collapse', function () {
                $('#arrowId3').removeClass('fa-angle-left').addClass('fa-angle-down');
            })

        } else {
            window.href = "/";
            return layer.msg("您没有权限")
        }
    }

    Jurisdiction();

    //计算元素集合的总宽度
    function calSumWidth(elements) {
        MyCrontab.calSumWidth(elements);
    }

    //滚动到指定选项卡
    function scrollToTab(element) {
        MyCrontab.scrollToTab(element);
    }

    //查看左侧隐藏的选项卡
    function scrollTabLeft() {
        var marginLeftVal = Math.abs(parseInt($('.page-tabs-content').css('margin-left')));
        // 可视区域非tab宽度
        var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".J_menuTabs"));
        //可视区域tab宽度
        var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
        //实际滚动宽度
        var scrollVal = 0;
        if ($(".page-tabs-content").width() < visibleWidth) {
            return false;
        } else {
            var tabElement = $(".J_menuTab:first");
            var offsetVal = 0;
            while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) {//找到离当前tab最近的元素
                offsetVal += $(tabElement).outerWidth(true);
                tabElement = $(tabElement).next();
            }
            offsetVal = 0;
            if (calSumWidth($(tabElement).prevAll()) > visibleWidth) {
                while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth) && tabElement.length > 0) {
                    offsetVal += $(tabElement).outerWidth(true);
                    tabElement = $(tabElement).prev();
                }
                scrollVal = calSumWidth($(tabElement).prevAll());
            }
        }
        $('.page-tabs-content').animate({
            marginLeft: 0 - scrollVal + 'px'
        }, "fast");
    }

    //查看右侧隐藏的选项卡
    function scrollTabRight() {
        var marginLeftVal = Math.abs(parseInt($('.page-tabs-content').css('margin-left')));
        // 可视区域非tab宽度
        var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".J_menuTabs"));
        //可视区域tab宽度
        var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
        //实际滚动宽度
        var scrollVal = 0;
        if ($(".page-tabs-content").width() < visibleWidth) {
            return false;
        } else {
            var tabElement = $(".J_menuTab:first");
            var offsetVal = 0;
            while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) {//找到离当前tab最近的元素
                offsetVal += $(tabElement).outerWidth(true);
                tabElement = $(tabElement).next();
            }
            offsetVal = 0;
            while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth) && tabElement.length > 0) {
                offsetVal += $(tabElement).outerWidth(true);
                tabElement = $(tabElement).next();
            }
            scrollVal = calSumWidth($(tabElement).prevAll());
            if (scrollVal > 0) {
                $('.page-tabs-content').animate({
                    marginLeft: 0 - scrollVal + 'px'
                }, "fast");
            }
        }
    }

    //通过遍历给菜单项加上data-index属性
    $(".J_menuItem").each(function (index) {
        if (!$(this).attr('data-index')) {
            $(this).attr('data-index', index);
        }
    });


    function menuItem() {
        // 获取标识数据
        var dataUrl = $(this).attr('href'),
            dataIndex = $(this).data('index'),
            menuName = $.trim($(this).text()),
            flag = true;
        if (dataUrl == undefined || $.trim(dataUrl).length == 0) return false;
        // 选项卡菜单已存在
        $('.J_menuTab').each(function () {
            //console.log($(this).data('id'));
            if ($(this).data('id') == dataUrl) {
                if (!$(this).hasClass('active')) {
                    $(this).addClass('active').siblings('.J_menuTab').removeClass('active');
                    scrollToTab(this);
                    // 显示tab对应的内容区
                    $('.J_mainContent .J_iframe').each(function () {
                        if ($(this).data('id') == dataUrl) {
                            $(this).show().siblings('.J_iframe').hide();
                            $(this).attr('src', $(this).attr('src'));
                            return false;
                        }
                    });
                }
                flag = false;
                return false;
            }
        });

        // 选项卡菜单不存在
        if (flag) {
            var str = '<a href="javascript:;" class="active J_menuTab" data-id="' + dataUrl + '">' + menuName + ' <i class="fa fa-times-circle"></i></a>';
            $('.J_menuTab').removeClass('active');

            // 添加选项卡对应的iframe
            var str1 = '<iframe class="J_iframe" name="iframe' + dataIndex + '" width="100%" height="100%" src="' + dataUrl + '" frameborder="0" data-id="' + dataUrl + '" seamless></iframe>';
            $('.J_mainContent').find('iframe.J_iframe').hide().parents('.J_mainContent').append(str1);

            //显示loading提示
            var loading = layer.load();

            $('.J_mainContent iframe:visible').load(function () {
                //iframe加载完成后隐藏loading提示
                layer.close(loading);
            });
            // 添加选项卡
            $('.J_menuTabs .page-tabs-content').append(str);
            scrollToTab($('.J_menuTab.active'));
        }
        return false;
    }

    $('.J_menuItem').on('click', menuItem);

    // 关闭选项卡菜单
    function closeTab() {
        var closeTabId = $(this).parents('.J_menuTab').data('id');
        var currentWidth = $(this).parents('.J_menuTab').width();

        // 当前元素处于活动状态
        if ($(this).parents('.J_menuTab').hasClass('active')) {

            // 当前元素后面有同辈元素，使后面的一个元素处于活动状态
            if ($(this).parents('.J_menuTab').next('.J_menuTab').size()) {

                var activeId = $(this).parents('.J_menuTab').next('.J_menuTab:eq(0)').data('id');
                highLightMenuItem(activeId);  //高亮对应的tab菜单
                $(this).parents('.J_menuTab').next('.J_menuTab:eq(0)').addClass('active');

                $('.J_mainContent .J_iframe').each(function () {
                    if ($(this).data('id') == activeId) {
                        $(this).show().siblings('.J_iframe').hide();
                        return false;
                    }
                });

                var marginLeftVal = parseInt($('.page-tabs-content').css('margin-left'));
                if (marginLeftVal < 0) {
                    $('.page-tabs-content').animate({
                        marginLeft: (marginLeftVal + currentWidth) + 'px'
                    }, "fast");
                }

                //  移除当前选项卡
                $(this).parents('.J_menuTab').remove();

                // 移除tab对应的内容区
                $('.J_mainContent .J_iframe').each(function () {
                    if ($(this).data('id') == closeTabId) {
                        $(this).remove();
                        return false;
                    }
                });
            }

            // 当前元素后面没有同辈元素，使当前元素的上一个元素处于活动状态
            if ($(this).parents('.J_menuTab').prev('.J_menuTab').size()) {
                var activeId = $(this).parents('.J_menuTab').prev('.J_menuTab:last').data('id');
                $(this).parents('.J_menuTab').prev('.J_menuTab:last').addClass('active');
                $('.J_mainContent .J_iframe').each(function () {
                    if ($(this).data('id') == activeId) {
                        $(this).show().siblings('.J_iframe').hide();
                        return false;
                    }
                });

                //  移除当前选项卡
                $(this).parents('.J_menuTab').remove();

                // 移除tab对应的内容区
                $('.J_mainContent .J_iframe').each(function () {
                    if ($(this).data('id') == closeTabId) {
                        $(this).remove();
                        return false;
                    }
                });
                highLightMenuItem(activeId);//高亮对应的tab菜单
            }
        }
        // 当前元素不处于活动状态
        else {
            //  移除当前选项卡
            $(this).parents('.J_menuTab').remove();

            // 移除相应tab对应的内容区
            $('.J_mainContent .J_iframe').each(function () {
                if ($(this).data('id') == closeTabId) {
                    $(this).remove();
                    return false;
                }
            });
            scrollToTab($('.J_menuTab.active'));
        }
        return false;
    }

    $('.J_menuTabs').on('click', '.J_menuTab i', closeTab);

    //关闭其他选项卡
    function closeOtherTabs() {
        $('.page-tabs-content').children("[data-id]").not(":first").not(".active").each(function () {
            $('.J_iframe[data-id="' + $(this).data('id') + '"]').remove();
            $(this).remove();
        });
        $('.page-tabs-content').css("margin-left", "0");
    }

    $('.J_tabCloseOther').on('click', closeOtherTabs);

    //滚动到已激活的选项卡
    function showActiveTab() {
        scrollToTab($('.J_menuTab.active'));
    }

    $('.J_tabShowActive').on('click', showActiveTab);


    // 点击选项卡菜单
    function activeTab() {
        if (!$(this).hasClass('active')) {
            var currentId = $(this).data('id');
            highLightMenuItem(currentId);  //高亮对应的tab菜单
            // 显示tab对应的内容区
            $('.J_mainContent .J_iframe').each(function () {
                if ($(this).data('id') == currentId) {
                    $(this).show().siblings('.J_iframe').hide();
                    return false;
                }
            });
            $(this).addClass('active').siblings('.J_menuTab').removeClass('active');
            scrollToTab(this);
        }
    }

    $('.J_menuTabs').on('click', '.J_menuTab', activeTab);

    //刷新iframe
    function refreshTab() {
        var target = $('.J_iframe[data-id="' + $(this).data('id') + '"]');
        var url = target.attr('src');
        //显示loading提示
        // var loading = layer.load();
        // target.attr('src', url).load(function () {
        //     //关闭loading提示
        //     layer.close(loading);
        // });
    }

    $('.J_menuTabs').on('dblclick', '.J_menuTab', refreshTab);

    // 左移按扭
    $('.J_tabLeft').on('click', scrollTabLeft);

    // 右移按扭
    $('.J_tabRight').on('click', scrollTabRight);

    // 关闭全部
    $('.J_tabCloseAll').on('click', function () {
        $('.page-tabs-content').children("[data-id]").not(":first").each(function () {
            $('.J_iframe[data-id="' + $(this).data('id') + '"]').remove();
            $(this).remove();
        });
        $('.page-tabs-content').children("[data-id]:first").each(function () {
            $('.J_iframe[data-id="' + $(this).data('id') + '"]').show();
            $(this).addClass("active");
        });
        $('.page-tabs-content').css("margin-left", "0");
    });

});
