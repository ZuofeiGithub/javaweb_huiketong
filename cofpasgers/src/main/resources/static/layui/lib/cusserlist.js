/** 客服管理 */
;layui.define(["table", "form"], function (t) {
    var user = layui.data('user');
    var $ = layui.$, i = layui.table, n = layui.form;
    i.render({
        elem: "#LAY-app-content-list",
        //url: layui.setter.base + "json/content/list.js",
        url: "/showcusser_list?user_id="+user.user_id,
        cols: [[{type: "checkbox", fixed: "left"}, {field: "id", width: 100, title: "分享ID", sort: !0}, {
            field: "title",
            title: "分享标题",
            minWidth: 100
        }, {field: "context", title: "分享内容"}, {
            field: "image",
            title: "分享图片",
            templet:function (d) {
                return "<img src='http://image.jkt365.com/"+d.image+"' width='30' height='30'>"
            }
        }, {field: "link_url", title: "分享链接"},{field:'sharetype',title:'分享类型',templet:function (d) {
                if(d.sharetype == 1){
                    return "优惠券分享"
                }else if(d.sharetype == 2){
                    return "商品分享"
                }
            }},{field:'goods_id',title:'商品ID',templet:function (d) {
                if(d.goods_id == 0){
                    return "优惠券分享,无商品ID";
                }
            }},{
            title: "操作",
            minWidth: 150,
            align: "center",
            fixed: "right",
            toolbar: "#table-content-list"
        }]],
        page: !0,
        limit: 10,
        limits: [10, 15, 20, 25, 30],
        text: {none: '一条数据也没有^_^'}
    }), i.on("tool(LAY-app-content-list)", function (t) {
        var field = t.data;
        "del" === t.event ? layer.confirm("确定删除此分享？", function (e) {
            $.post('delvoucher',{shareid:field.id},function (resp) {
                if(resp.code == "0"){
                    layer.msg("删除成功");
                    t.del(), layer.close(e)
                }else if(resp.code == "1"){
                    return layer.msg("删除失败");
                }
            });

        }) : "edit" === t.event && layer.open({
            type: 2,
            title: "编辑分享内容",
            content: "voucherform?id=" + field.id,
            maxmin: !0,
            area: ["550px", "550px"],
            btn: ["确定", "取消"],
            yes: function (e, i) {
                var l = window["layui-layer-iframe" + e],
                    a = i.find("iframe").contents().find("#layuiadmin-app-form-edit");
                $()
                l.layui.form.on("submit(layuiadmin-app-form-edit)", function (i) {
                    var l = i.field;
                    console.log(l);
                    t.update({
                        title: l.title,
                        context: l.context,
                        link_url: l.link_url,
                        sharetype: l.sharetype
                    }), n.render(), layer.close(e)
                }), a.trigger("click")
            }
        })
        console.log(field)
    }), i.render({
        elem: "#LAY-app-content-tags",
        url: layui.setter.base + "json/content/tags.js",
        cols: [[{type: "numbers", fixed: "left"}, {field: "id", width: 100, title: "ID", sort: !0}, {
            field: "tags",
            title: "分类名",
            minWidth: 100
        }, {title: "操作", width: 150, align: "center", fixed: "right", toolbar: "#layuiadmin-app-cont-tagsbar"}]],
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-app-content-tags)", function (t) {
        var i = t.data;
        if ("del" === t.event) layer.confirm("确定删除此分类？", function (e) {
            t.del(), layer.close(e)
        }); else if ("edit" === t.event) {
            e(t.tr);
            layer.open({
                type: 2,
                title: "编辑分类",
                content: "../../../views/app/content/tagsform.html?id=" + i.id,
                area: ["450px", "200px"],
                btn: ["确定", "取消"],
                yes: function (e, i) {
                    var n = i.find("iframe").contents().find("#layuiadmin-app-form-tags"),
                        l = n.find('input[name="tags"]').val();
                    l.replace(/\s/g, "") && (t.update({tags: l}), layer.close(e))
                },
                success: function (t, e) {
                    var n = t.find("iframe").contents().find("#layuiadmin-app-form-tags").click();
                    n.find('input[name="tags"]').val(i.tags)
                }
            })
        }
    }), i.render({
        elem: "#LAY-app-content-comm",
        url: layui.setter.base + "json/content/comment.js",
        cols: [[{type: "checkbox", fixed: "left"}, {
            field: "id",
            width: 100,
            title: "ID",
            sort: !0
        }, {field: "reviewers", title: "评论者", minWidth: 100}, {
            field: "content",
            title: "评论内容",
            minWidth: 100
        }, {field: "commtime", title: "评论时间", minWidth: 100, sort: !0}, {
            title: "操作",
            width: 150,
            align: "center",
            fixed: "right",
            toolbar: "#table-content-com"
        }]],
        page: !0,
        limit: 10,
        limits: [10, 15, 20, 25, 30],
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-app-content-comm)", function (t) {
        t.data;
        "del" === t.event ? layer.confirm("确定删除此条评论？", function (e) {
            t.del(), layer.close(e)
        }) : "edit" === t.event && layer.open({
            type: 2,
            title: "编辑评论",
            content: "../../../views/app/content/contform.html",
            area: ["450px", "300px"],
            btn: ["确定", "取消"],
            yes: function (t, e) {
                var n = window["layui-layer-iframe" + t], l = "layuiadmin-app-comm-submit",
                    a = e.find("iframe").contents().find("#" + l);
                n.layui.form.on("submit(" + l + ")", function (e) {
                    e.field;
                    i.reload("LAY-app-content-comm"), layer.close(t)
                }), a.trigger("click")
            },
            success: function (t, e) {
            }
        })
    }), t("vouchersharelist", {})
});