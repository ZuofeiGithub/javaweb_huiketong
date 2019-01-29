Date.prototype.format = function(format){
    var paddNum = function(num){
      num += "";
      return num.replace(/^(\d)$/,"0$1");
    }
    //指定格式字符
    var cfg = {
       yyyy : this.getFullYear() //年 : 4位
      ,yy : this.getFullYear().toString().substring(2)//年 : 2位
      ,M  : this.getMonth() + 1  //月 : 如果1位的时候不补0
      ,MM : paddNum(this.getMonth() + 1) //月 : 如果1位的时候补0
      ,d  : this.getDate()   //日 : 如果1位的时候不补0
      ,dd : paddNum(this.getDate())//日 : 如果1位的时候补0
      ,hh : paddNum(this.getHours())  //时
      ,mm : paddNum(this.getMinutes()) //分
      ,ss : paddNum(this.getSeconds()) //秒
    }
    format || (format = "yyyy-MM-dd hh:mm:ss");
    return format.replace(/([a-z])(\1)*/ig,function(m){return cfg[m];});
}

String.prototype.isBlank = function(){
    return /^\s*$/.test(this);
}

String.prototype.isMobile = function(){
    return /^1[3|4|5|7|8|9][0-9]{9}$/.test(this);
}

if(!"".trim) {
    String.prototype.trim = function(){
        return this.replace(/(^\s*)|(\s*$)/g, "");
    }
}

jll = window.jll || {};

jll.domains = {
    //source: 'http://7bv8ab.com1.z0.glb.clouddn.com/',
    source: 'http://source.vipjll.com/'
};
jll.px1Img = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==';

jll.isWx = function() {
    return (/MicroMessenger/.test(navigator.userAgent));
};

jll.isApp = function() {
    return (/jll-app/.test(navigator.userAgent));
}

jll.crypto = function(source) {
    source = CryptoJS.SHA256(source).toString();
    return CryptoJS.HmacSHA256(source, 'vipjll@517').toString();
};

if(window.Promise) {
    jll._ajax = function(opt) {
        return new Promise(function(resolve, reject) {
            jll.ajax(opt, resolve, reject);
        });
    }
}

jll.ajax = function(opt, fn, reject, final) {
    if($api.isString(opt)) opt = {url : opt};
    opt.method = opt.method || 'get';
    opt.dataType = opt.dataType || 'json';
    opt.timeout = opt.timeout || 60000;
    $.ajax({
        url : jll.path + opt.url,
        type : opt.method,
        data : opt.data,
        dataType : opt.dataType,
        timeout : opt.timeout,
        success : function(ret){
            if(opt.dataType == 'json' && ret === '') {
                ret = {};
            }
            if(ret && ret['authErr']) {
                jll.popLogin();
            } else {
                fn(ret);
            }
        },
        error : function(xhr, error, ex) {
            if(xhr.responseText == '__requiredLogin-noLogin') jll.popLogin();
            else reject && reject(xhr.responseText, error, ex);
        },
        complete : function(xhr) {
            final && final(xhr);
        }
    });
}

jll.getSystemType = function() {
    return (/iphone|ipad/gi).test(navigator.appVersion) ? 'ios' : 'android';
}

jll.setCity = function(city) {
    $.cookie('city_location', city.cityCode + '_' + city.cityName, {path: '/', domain : '.vipjll.com', expires : 30 * 24 * 3600});
}

jll.getCity = function() {
    return $.cookie('city_location');
}

jll.popLogin = function() {
    document.location.href = jll.path + "/user/login?redirect_url=" + encodeURIComponent(document.location.href);
}

jll.closeLogin = function() {
    api.closeWin({
        name: 'login-win',
        animation : {
            type: 'reveal',
            subType: 'from_top',
            duration: 250
        }
    });
}

jll.login = function(ret, loginType) {
    if(arguments.length == 0) {
        if(jll.hadLoggedIn()) {
            return true;
        } else {
            jll.popLogin();
            return false;
        }
    } else if($api.isFunction(ret)) {
        if(!jll.hadLoggedIn()) {
            jll.popLogin();
        } else {
            ret();
        }
    } else {
        var custId = ret['app-cust-id'];
        jll.push.bindUser(custId);
        this.setLoginType(loginType);
        this.setUser(custId);
        this.setToken(ret['app-token']);
        api.sendEvent({
            name: 'jll-login'
        });
        jll.closeLogin();
    }
}

jll.pager = function(opt, fn, fail, finalFn) {
    var pageCountParam
    var pageParam
    var pageCount;
    var targetPage;
    var currentPage;
    var totalParam;
    var listParam;
    var totalCount;
    var ifInit;
    var hasPrevious;
    var ifn;

    function request(reqData, afn) {
        reqData = reqData || {};
        reqData[pageCountParam] = pageCount;
        reqData[pageParam] = targetPage;
        jll.ajax({
            url : opt.url,
            method : 'post',
            data : reqData
        }, function(ret, err) {
            if(ret) {
                totalCount = ret[totalParam];
                var list = ret[listParam];
                if(list && list.length > 0) {
                    currentPage = targetPage;
                    hasPrevious = currentPage > 1;
                }
                (afn || ifn)(list, ret, {
                    totalCount : totalCount,
                    currentPage : currentPage
                });
            } else {
                tooltip('加载数据出错');
            }
        }, fail, finalFn);
    }

    this.to = function(pageNum, afn) {
        targetPage = pageNum;
        request(opt.data, afn);
    };

    this.next = function(afn) {
        this.to(currentPage + 1, afn);
    };

    this.previous = function(afn) {
        if(!hasPrevious) return;
        this.to(currentPage - 1, afn);
    };

    this.reset = function(opt, fn) {
        pageCountParam = opt.pageCountParam || 'pageCount';
        pageParam = opt.pageParam || 'page';
        pageCount = opt.pageCount || 10;
        targetPage = opt.page || 1;
        currentPage = targetPage - 1;
        totalParam = opt.totalParam || 'total';
        listParam = opt.listParam || 'list';
        totalCount = 0;
        ifInit = !!opt.ifInit;
        hasPrevious = false;
        ifn = fn;
    }

    this.getCurrentPage = function() {
        return currentPage;
    }

    this.getPageCount = function() {
        return pageCount;
    }

    this.init = this.next;

    this.reset(opt, fn);
    if(ifInit) this.init();
}

jll.flowPager = function(opt, fn, dataFn, fail, finalFn) {
    var normalMode = opt.normalMode;
    var ifInit = opt.ifInit;
    var bottom = (opt.bottom || 300) / (window.viewScale || 1);
    var filterLength = opt.filterLength;
    var filterKeys = opt.filterKeys;
    if(filterKeys && $api.isString(filterKeys)) {
        filterKeys = [filterKeys];
    }
    var filterData = [];

    opt.page = 1;
    opt.ifInit = false;
    var requestData = deepCopy(opt.data = opt.data || {});
    var pager = new jll.pager(opt, fn, fail, finalFn);
    var pageCount = pager.getPageCount();
    filterLength = filterLength || pageCount * 2;

    var me  = this, canFooterRefresh = false, refreshFooter;
    var ifRefreshFooter = opt.ifRefreshFooter === false ? false : true;

    if(ifRefreshFooter) {
        var refreshFooterParam = {};
        if(opt.refreshFooterAppendTo) refreshFooterParam['appendTo'] = opt.refreshFooterAppendTo;
        refreshFooter = new jll.refreshFooter(refreshFooterParam);
        
        
        $(window).scroll(function () {
            if ((bottom + $(window).scrollTop()) >= ($(document).height() - $(window).height())) {
                //当底部基本距离+滚动的高度〉=文档的高度-窗体的高度时；
                //我们需要去异步加载数据了
                if(canFooterRefresh) {
                    canFooterRefresh = false;
                    refreshFooter.showLoading();
                    me.next();
                }
            }
        });
    }

    function get(afn, page) {
        afn = afn || fn;
        page = page || 1;
        pager.to(page, function(list, ret) {
            if(!list && $api.isArray(ret)) list = ret;
            if(list && list.length > 0) {
                dataFn = dataFn || function(){};
                dataFn(opt.data, list);
                //pager.reset(opt, fn);
            }
            if(refreshFooter) {
                var noMore = !list || list.length < pageCount;
                refreshFooter.setNoMore(noMore);
                canFooterRefresh = !noMore;
            }
            if(list && list.length > 0 && filterKeys) {
                list = $api.filter(list, function(v){
                    return $api.filter(filterData, function(fd){
                        var flag = true;
                        $api.each(filterKeys, function(key){
                            if(v[key] !== fd[key]) flag = false;
                        });
                        return flag;
                    }).length == 0;
                });

                $api.each(list, function(fd){
                    var ap = {};
                    $api.each(filterKeys, function(key){
                        ap[key] = fd[key];
                    });
                    filterData.push(ap);
                });

                if(filterData.length > filterLength) {
                    filterData.splice(0, filterData.length - filterLength);
                }
            }
            afn(list, ret);
        });
    };

    this.hideRefreshFooter = function() {
        refreshFooter.hide();
    }

    this.showRefreshFooter = function() {
        refreshFooter.show();
    }
    
    this.showLoading = function() {
        refreshFooter.setNoMore(false);
        refreshFooter.showLoading();
        refreshFooter.show();
    }

    this.refresh = function(afn) {
        filterData = [];
        opt.data = deepCopy(requestData);
        pager.reset(opt, fn);
        this.init(afn);
    };

    this.setData = function(data) {
        requestData = data;
        opt.data = deepCopy(requestData);
    };

    this.next = function(afn) {
        get(afn, normalMode ? pager.getCurrentPage() + 1 : 1);
    };

    this.init = function(afn) {
        afn = afn || fn;
        get(function(list, ret){
            if(list && list.length > 0) {
                //firstKeyVal = list[0][dataKeyName];
            }
            afn(list, ret);
        });
    };

    if(ifInit) this.init();

}

jll.refreshFooter = function(opt) {
    opt = $api.extend({
        loadingText: '正在加载...',
        noMoreText: '没有更多了',
        appendTo: 'body'
    }, opt);
    
    var id = new Date().getTime();
    
    this.init = function() {
        var refreshFooter = 
            '<div class="loading-more refresh-footer id-' + id + '">' + 
                '<!--<span class="iconfont icon-loading2 rotate"></span>--><span class="loading-text ">' + opt.loadingText+ '</span>' + 
            '</div>';
        $api.append($api.dom(opt.appendTo), refreshFooter);
    };

    this.setNoMore = function(noMore) {
        $api.addCls($api.dom('.refresh-footer.id-' + id + ' .icon-loading2'), 'hide');
        if(noMore) {
            $api.text($api.dom('.refresh-footer.id-' + id + ' .loading-text'), opt.noMoreText);
            $api.css($api.dom('.refresh-footer.id-' + id), 'opacity:1;');
        } else {
            $api.text($api.dom('.refresh-footer.id-' + id + ' .loading-text'), opt.loadingText);
            $api.css($api.dom('.refresh-footer.id-' + id), 'opacity:0;');
        }
    };

    this.showLoading = function() {
        $api.removeCls($api.dom('.refresh-footer.id-' + id + ' .icon-loading2'), 'hide');
        $api.css($api.dom('.refresh-footer.id-' + id), 'opacity:1;');
    };

    this.hide = function() {
        $api.addCls($api.dom('.refresh-footer.id-' + id), 'hide');
    }

    this.show = function() {
        $api.removeCls($api.dom('.refresh-footer.id-' + id), 'hide');
    }
    
    $('.loading-more.refresh-footer').remove();
    this.init();
}

jll.optImgSize = function(size) {
    if(jll.getSystemType() == 'android' && window.viewScale === 1) {
        size *= (window.devicePixelRatio || 1);
    }
    return size;
}

jll.sorcePrefix = function(src) {
    if(!/^http/.test(src)) src = jll.domains.source + src;
    return src;
}

/**
 * 最小宽高的图片原比例缩放，自动判断加域名前缀，自动适配安卓分辨率
 * @param  {String} src 
 * @param  {Num} w 
 * @param  {Num} h 
 * @return {String} 
 */
jll.imgOuterWH = function(src, w, h) {
    return jll.sorcePrefix(src)  + '?imageMogr2/thumbnail/!' + jll.optImgSize(w) + 'x' + jll.optImgSize(h) + 'r/quality/100';
}

/**
 * 最小宽高的图片原比例缩放后按宽高裁剪，自动判断加域名前缀，自动适配安卓分辨率
 * @param  {String} src 
 * @param  {Num} w 
 * @param  {Num} h 
 * @param  {String} gravity 图片重心（九宫格）参数，默认为Center
 * @return {String} 
 */
jll.imgOuterCenterWH = function(src, w, h, gravity) {
    w = jll.optImgSize(w);
    h = jll.optImgSize(h);
    gravity = gravity || 'Center';
    return jll.sorcePrefix(src)  + '?imageMogr2/thumbnail/!' + w + 'x' + h + 'r/gravity/' + gravity + '/crop/' + w + 'x' + h + '/quality/100';
}

/**
 * 不超过宽度的图片原比例缩放，自动适配安卓分辨率
 * @param  {String} src 
 * @param  {Num} w 
 * @return {String} 
 */
jll.imgInnerW = function(src, w) {
    return jll.sorcePrefix(src) + '?imageMogr2/thumbnail/' + jll.optImgSize(w) + '>/quality/100';
}

/**
 * 不超过宽高的图片原比例缩放，自动适配安卓分辨率
 * @param  {String} src 
 * @param  {Num} w 
 * @param  {Num} h
 * @return {String} 
 */
jll.imgInnerWH = function(src, w, h) {
    return jll.sorcePrefix(src) + '?imageMogr2/thumbnail/' + jll.optImgSize(w) + 'x' + jll.optImgSize(h) +'>/quality/100';
}

jll.imgLoaded = function(el, fn) {
    if($) {
        $(el).one('load', fn).each(function() {
            if(this.complete) $(this).load();
        });
    }
}

jll.imgAvatar = function(src) {
    var w = 80, h = 80, gravity = 'Center';
    return jll.sorcePrefix(src)  + '?imageMogr2/thumbnail/!' + w + 'x' + h + 'r/gravity/' + gravity + '/crop/' + w + 'x' + h + '/quality/100';
}

jll.imgTimeline = function(src) {
    var w = 200, h = 200, gravity = 'Center';
    return jll.sorcePrefix(src)  + '?imageMogr2/thumbnail/!' + w + 'x' + h + 'r/gravity/' + gravity + '/crop/' + w + 'x' + h + '/quality/100';
}

jll.imgLazyLoad = function($imgs) {
    $imgs = $imgs || $('img.lazy-load');
    $imgs.each(function(index, el) {
        var $el = $(el);
        var width = $el.attr('data-width') || $el.width();
        var height = $el.attr('data-height') || $el.height();
        $el.attr('data-original', jll.imgOuterCenterWH($el.attr('data-original'), width, height));
    });
    $imgs.lazyload({
        placeholder : jll.px1Img,   
        effect : 'fadeIn',
        threshold : 100
    });
}

/**
 * [showScrollImgs description]
 * @param  {[type]} param {
 *                          imgs: [],
 *                          imgsDesc: []/String
 *                          index: Num,
 *                          save: true,
 *                          deleteFn: fn,
 *                          actionDesc: String,
 *                          actionFn: fn
 *                      }
 * @return {[type]}     [description]
 */
jll.showScrollImgs = function(param) {
    var wrapCss = param.wrapCss || 'jll-scroll-imgs';
    var imgs = param.imgs;
    var index = param.index || 0;
    var activeIndex = index;
    var imgsDesc = param.imgsDesc;
    var actionDesc = param.actionDesc;
    var lazy = param.lazy || false;
    var $content;

    function showOrder(s) {
        activeIndex = s.activeIndex;
        $('#img-order', $content).text(activeIndex + 1 + ' / ' + imgs.length);
        if($.isArray(imgsDesc)) {
            $('.img-desc', $content).text(imgsDesc[activeIndex]);
        }
    }

    function inout() {
        $('.bar', $content).toggleClass('bar-show');
        $('.action-wrap', $content).toggleClass('action-down');
    }

    function closeMe() {
        releaseFixBody();
        $('.' + wrapCss).remove();
    }

    function init() {
        fixBody();
        
        var tpl = ' \
        <style type="text/css"> \
            .{{wrap}} { \
                display: block; \
                position: fixed; \
                top: 0; \
                left: 0; \
                right: 0; \
                bottom: 0; \
                z-index: 1000; \
                overflow: hidden; \
                background-color: #000; \
            } \
            .{{wrap}} .bar { \
                position: absolute; \
                width: 100%; \
                top: 0; \
                left: 0; \
                padding: 0; \
                opacity: 1; \
                z-index: 2; \
                -webkit-transition: all .2s ease; \
                transition: all .2s ease; \
            } \
            .{{wrap}} .bar-show { \
                opacity: 0; \
            } \
            .{{wrap}} #header{ \
                background: #000; \
                border: none; \
            } \
            .{{wrap}} #header *, .{{wrap}} #header .iconfont { \
                color: #fff; \
            } \
            .{{wrap}} #header .icon-shanchu { \
                font-size: .4rem; \
                padding: .2rem; \
                margin-right: -0.2rem; \
            } \
            .{{wrap}} .swiper-container { \
                width: 100%; \
                height: 100%; \
            } \
            .{{wrap}} .swiper-slide { \
                text-align: center; \
                font-size: 18px; \
                background: #000; \
                display: -webkit-box; \
                display: -ms-flexbox; \
                display: -webkit-flex; \
                display: flex; \
                -webkit-box-pack: center; \
                -ms-flex-pack: center; \
                -webkit-justify-content: center; \
                justify-content: center; \
                -webkit-box-align: center; \
                -ms-flex-align: center; \
                -webkit-align-items: center; \
                align-items: center; \
            } \
            .{{wrap}} .swiper-slide img { \
                width: 100%; \
            } \
            .{{wrap}} .action-wrap { \
                position: absolute; \
                z-index: 2; \
                width: 100%; \
                left: 0; \
                bottom: 0; \
                color: #fff; \
                -webkit-transition: all .2s ease; \
                transition: all .2s ease; \
            } \
            .{{wrap}} .action-down { \
                -webkit-transform: translate3d(0,0.76rem,0); \
                transform: translate3d(0,0.76rem,0); \
            } \
            .{{wrap}} .img-desc { \
                font-size: .22rem; \
                padding: .22rem; \
            } \
            .{{wrap}} .action-btn { \
                height: .76rem; \
                line-height: .76rem; \
                text-align: center; \
                font-size: .3rem; \
                background-color: #333; \
            } \
            .{{wrap}} .pinch-zoom-container { \
                width: 100%; \
                overflow: visible !important; \
            } \
            .{{wrap}} .pinch-zoom, \
            .{{wrap}} .pinch-zoom img{ \
                width: 100%; \
                -webkit-user-drag: none; \
            } \
        </style> \
        <div class="{{wrap}}"> \
            <div class="bar"> \
                <div id="header" class="header"> \
                    <i class="iconfont icon-arrow-back back" tapmode=""></i> \
                    <div class="header-center-blank"></div> \
                    <div class="header-center" id="img-order">1/3</div> \
                    <i class="iconfont icon-save hide" tapmode=""></i> \
                    <i class="iconfont icon-shanchu hide" tapmode=""></i> \
                </div> \
            </div> \
            <div class="swiper-container" tapmode=""> \
                <div class="swiper-wrapper"> \
                    {{#each imgs}} \
                        <div class="swiper-slide"> \
                            <div class="pinch-zoom"> \
                            <img {{#if ../lazy}}data-src="{{this}}" class="swiper-lazy"{{else}}src="{{this}}"{{/if}}> \
                            </div> \
                            {{#if ../lazy}} \
                            <div class="swiper-lazy-preloader swiper-lazy-preloader-white"></div> \
                            {{/if}} \
                        </div> \
                    {{/each}} \
                </div> \
            </div> \
            <div class="action-wrap hide"> \
                <div class="img-desc hide" tapmode=""></div> \
                <div class="action-btn hide" tapmode=""></div> \
            </div> \
        </div>';

        $content = $(Handlebars.compile(tpl)({imgs:imgs, lazy:lazy, wrap:wrapCss}));
        $content.appendTo($('body'));

        $('#img-order', $content).text(activeIndex + 1 + ' / ' + imgs.length);
        if(param.save) $('.icon-save', $content).show();
        if(param.deleteFn) $('.icon-shanchu', $content).show();
        if(imgsDesc || actionDesc) {
            $('.action-wrap', $content).show();
            if(imgsDesc) {
                $('.img-desc', $content).text($.isArray(imgsDesc) ? imgsDesc[index] : imgsDesc);
                $('.img-desc', $content).show();
            }
            if(actionDesc) {
                $('.action-btn', $content).text(actionDesc);
                $('.action-btn', $content).show();
            }
        }

        $('div.pinch-zoom', $content).each(function () {
            new RTP.PinchZoom($(this), {});
        });

        var swiper = $('.swiper-container', $content).swiper({
            preloadImages: lazy ? false: true,
            lazyLoading: lazy ? true : false,
            initialSlide : activeIndex,
            onSlideChangeEnd: function(s){
                showOrder(s);
            }
        });

        $('.swiper-container', $content).click(inout);
        $('.img-desc', $content).click(inout);
        $('.back', $content).click(closeMe);
        $('.icon-save', $content).click(function(){});
        $('.icon-shanchu', $content).click(function(){
            imgs.splice(activeIndex, 1);
            param.deleteFn && param.deleteFn(activeIndex, imgs);
            if(imgs.length == 0) {
                closeMe();
            } else {
                swiper.removeSlide(activeIndex);
                showOrder(swiper);
            }
        });
        if(param.actionFn) {
            $('.action-btn', $content).click(function(){
                param.actionFn(activeIndex);
            });
        }
    }

    closeMe();
    init();
}

jll.baiduPosition = function(latitude, longitude, fn) {
    var cbName =  'bdNameFromCoords';
    if(!window[cbName]) {
        window[cbName] = function(ret) {
            var pos;
            if(ret.status == 0) {
                var cityName = ret.result.addressComponent.city;
                $api.each(jll.getSupportCitys(), function(city){
                    if(city.paraCode2 == cityName.slice(0, -1)) {
                        pos = {};
                        pos.cityCode = city.paraCode1;
                        pos.cityName = city.paraCode2;
                        pos.proviceCode = city.paraCode3;
                        pos.proviceName = city.paraCode4;
                        return $api.breaker;
                    }
                });
            }
            fn && fn(pos);
        };
    }
    loadJs('http://api.map.baidu.com/geocoder/v2/?ak=RlNT6U7WgwZPX4FDiKRIMwI0&callback=' 
        + cbName + '&location=' + latitude + ',' + longitude + '&output=json');
}

jll.position = function(fn) {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position){
            jll.baiduPosition(position.coords.latitude, position.coords.longitude, fn);
        });
    } else {
        fn && fn();
    }
}

jll.setPageTitle = function(titleStr) {
    var $body = $('body');
    document.title = titleStr;
    // hack在微信等webview中无法修改document.title的情况
    var $iframe = $('<iframe src="https://www.baidu.com/favicon.ico"></iframe>');
    $iframe.on('load',function() {
        setTimeout(function() {
            $iframe.off('load').remove();
        }, 0);
    }).appendTo($body);
}

function deepCopy(source) {
    var result = {};
    for (var key in source) {
        if(source.hasOwnProperty(key))
            result[key] = typeof source[key]==='object' ? deepCoyp(source[key]) : source[key];
    }
    return result; 
}

function tooltip(msg, duration, location) {
    api.toast({
        msg: msg,
        duration: duration || 2000,
        location: location || 'middle'
    });
}

function fixBody() {
    var scrollTop= $(document).scrollTop();
    $('body').css({top: '-' + scrollTop + 'px', position: 'fixed'});
    $('body').addClass('jll-body-fixed');
}

function releaseFixBody() {
    if($('body').hasClass('jll-body-fixed')) {
        $('body').removeClass('jll-body-fixed');
        var top = $('body').css('top').match(/\d+/);
        $('body').css({position: '', top: ''});
        if(top) {
            var scrollTop = top[0] - 0;
            $(document).scrollTop(scrollTop);
        }
    }
}

function showLoading(title) {
    hideLoading();
    var scrollTop= $(document).scrollTop();
    $('<div style="position:absolute;width:100%;height:100%;left:0;top:' + scrollTop + 'px;background-color:rgba(0,0,0,0.3);z-index:9999;" class="jll-loading-toast hv-center">' + 
        '<div style="background-color:rgba(50,50,50,1);padding:0.2rem 0.2rem;color:#fff;border-radius:0.1rem;font-size:0.2rem;text-align:center;">' +
            '<div class="iconfont icon-loading2 rotate" style="width:0.3rem;height:0.3rem;font-size:0.3rem;margin-bottom:0.15rem;display: inline-block;"></div>' +
            '<div>' + title + '</div>' +
        '</div>' + 
    '</div>').appendTo($('body'));
    fixBody();
}

function hideLoading() {
    releaseFixBody();
    $('.jll-loading-toast').remove();
}

function getPxNum(px) {
    return px.match(/\d+/)[0] - 0;
}

function addCommas(nStr) {
    nStr += '';
    x = nStr.split('.');
    x1 = x[0];
    x2 = x.length > 1 ? '.' + x[1] : '';
    /*var rgx = /(\d+)(\d{3})/;
    while (rgx.test(x1)) {
        x1 = x1.replace(rgx, '$1' + ',' + '$2');
    }*/
    x1 = x1.replace(/\d+?(?=(?:\d{3})+$)/ig, "$&,");
    return x1 + x2;
}

function loadJs(path) {
    var head = document.getElementsByTagName('head')[0];
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = path;
    head.appendChild(script);
}

function loadCss(path) {
    var head = document.getElementsByTagName('head')[0];
    var link = document.createElement('link');
    link.rel = 'stylesheet';
    link.type = 'text/css';
    link.href = path;
    head.appendChild(link);
}

(function(){
    if(jll.getSystemType() == 'android') {
        loadCss(jll.path + '/custom/invite/inviteagent/res/style/android.css');
    }
})();