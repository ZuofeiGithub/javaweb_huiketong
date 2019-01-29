jll = this.jll || {};
jll.domains = {
    //wx : 'http://192.168.0.101:8080/jll-wechat'
    wx : 'http://weixin.vipjll.com'
};
var tapmode = 'tapmode';

function addCls(el, cls) {
    if('classList' in el){
        el.classList.add(cls);
    }else{
        var preCls = el.className;
        var newCls = preCls +' '+ cls;
        el.className = newCls;
    }
    return el;
}

function removeCls(el, cls) {
    if('classList' in el){
        el.classList.remove(cls);
    }else{
        var preCls = el.className;
        var newCls = preCls.replace(cls, '');
        el.className = newCls;
    }
    return el;
}

function touchStart() {
    var n = this;
    var cls = n.getAttribute(tapmode);
    addCls(n, cls);
}

function touchEnd() {
    var n = this;
    var cls = n.getAttribute(tapmode);
    removeCls(n, cls);
}

function parseTapMode () {
    var nodes = document.querySelectorAll('[' + tapmode + ']');
    for (var i = 0; i < nodes.length; i++) {
        var node = nodes[i];
        node.removeEventListener('touchstart', touchStart, false);
        node.removeEventListener('touchend', touchEnd, false);
        node.addEventListener('touchstart', touchStart, false);
        node.addEventListener('touchend', touchEnd, false);
    };
}

parseTapMode();