/**
 * bootstrap对话框
 */

jll = window.jll || {};

jQuery.extend(jll, {
    /**
     * alert框
     * @param {} option
     *   {
     *      title : '',
     *      content : ''
     *   }
     */
    alert: function (option) {
        var $alert = $(
            '<div class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">' +
            '<div class="modal-header">' +
            '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>' +
            '<h3 id="myModalLabel">' + option.title + '</h3>' +
            '</div>' +
            '<div class="modal-body"><p>' + option.content + '</p></div>' +
            '<div class="modal-footer">' +
            '<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>' +
            '</div>' +
            '</div>').appendTo($('body'));
        $alert.on('hidden', function () {
            $alert.remove();
            $alert = null;
        });
        $alert.modal({
            show: true,
            keyboard: option.keyboard !== false
        });
        $alert.find('.modal-footer button').click(function () {
            option.fn && option.fn();
        });
    },

    /**
     * confirm框
     * @param {} option
     *    {
     *      title : '',
     *      content : '',
     *      yes : function,
     *      no : function
     *   }
     */
    confirm: function (option) {
        var $confirm = $(
            '<div class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="confirmModal" aria-hidden="true">' +
            '<div class="modal-header">' +
            '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>' +
            '<h3 id="myModalLabel3">' + option.title + '</h3>' +
            '</div>' +
            '<div class="modal-body">' +
            '<p>' + option.content + '</p>' +
            '</div>' +
            '<div class="modal-footer">' +
            '<button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>' +
            '<button class="btn blue" data-dismiss="modal">确定</button>' +
            '</div>' +
            '</div>').appendTo($('body'));
        var $btns = $confirm.find('.modal-footer button');
        $btns.eq(0).click(option.no || function () {
        });
        $btns.eq(1).click(option.yes || function () {
        });
        $confirm.on('hidden', function () {
            $confirm.remove();
            $confirm = null;
        })
        $confirm.modal('show');
    },


    confirmByNoCancle: function (option) {
        var $confirm = $(
            '<div class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="confirmModal" aria-hidden="true">' +
            '<div class="modal-header">' +
            '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>' +
            '<h3 id="myModalLabel3">' + option.title + '</h3>' +
            '</div>' +
            '<div class="modal-body">' +
            '<p>' + option.content + '</p>' +
            '</div>' +
            '<div class="modal-footer">' +
            //'<button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>' +
            '<button class="btn blue" data-dismiss="modal">确定</button>' +
            '</div>' +
            '</div>').appendTo($('body'));
        var $btns = $confirm.find('.modal-footer button');
        $btns.eq(0).click(option.no || function () {
        });
        $btns.eq(1).click(option.yes || function () {
        });
        $confirm.on('hidden', function () {
            $confirm.remove();
            $confirm = null;
        })
        $confirm.modal('show');
    },


    /**
     * 模态窗
     * @param {} option
     */
    window: function (option) {
        var $win = $(
            '<div class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">' +
            '<div class="modal-header">' +
            '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>' +
            '<h3 id="myModalLabel">' + option.title + '</h3>' +
            '</div>' +
            '<div class="modal-body" style="max-height:500px;"><p>' + option.content + '</p></div>' +
            (option.footer === false ? '' :
                '<div class="modal-footer">' +
                (option.footer ? option.footer : '<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>') +
                '</div>') +
            '</div>').appendTo($('body'));
        $win.on('hidden', function () {
            $win.remove();
            $win = null;
        })
        if (typeof option.onShow === 'function') {
            $win.on('show.bs.modal', option.onShow);
        }
        if (typeof option.onShown === 'function') {
            $win.on('shown.bs.modal', option.onShown);
        }
        if (typeof option.onHide === 'function') {
            $win.on('hide.bs.modal', option.onHide);
        }
        if (typeof option.onHidden === 'function') {
            $win.on('hidden.bs.modal', option.onHidden);
        }
        $win.modal({
            show: true,
            keyboard: option.keyboard !== false,
            backdrop: option.backdrop !== undefined ? option.backdrop : 'static'
        }).css({
            width: option.width,
            'margin-left': function () {
                return -($(this).width() / 2);
            }
        });
        return $win;
    }
});