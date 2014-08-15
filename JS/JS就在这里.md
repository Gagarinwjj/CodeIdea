#互联网是开放的世界，代码就在这里。以下示例，举一反三。

##百度开源[UEditor](http://ueditor.baidu.com/website/index.html "调试工具查看源码")

1. 封装的工具包`utils.js`
```javascript
var util = function () {
    return {
        on:function (obj, type, handler) {
            var types = this.isArray(type) ? type : [type],
                k = types.length,
                d;
            if (!obj.addEventListener) {
                //绑定obj 为this
                d = function (evt) {
                    evt = evt || window.event;
                    var el = evt.srcElement;
                    return handler.call(el, evt);
                };
                handler._d = d;
            }
            if (k) while (k--) {
                type = types[k];
                if (obj.addEventListener) {
                    obj.addEventListener(type, handler, false);
                } else {
                    obj.attachEvent('on' + type, d);
                }
            }
            obj = null;
        },
        un:function (obj, type, handler) {
            var types = this.isArray(type) ? type : [type],
                k = types.length;
            if (k) while (k--) {
                type = types[k];
                if (obj.removeEventListener) {
                    obj.removeEventListener(type, handler, false);
                } else {
                    obj.detachEvent('on' + type, handler._d || handler);
                }
            }
        },
        isEmpty:function (data) {
            return data.replace(/[ ]/g, "") != "" ? data : "无";
        },
        getEvent:function (event) {
            return event ? event : window.event;
        },
        getTarget:function (event) {
            return event.target || event.srcElement;
        },
        setInnerText:function (element, text) {
            if (typeof element.textContent == "string")
                element.textContent = text;
            else
                element.innerText = text;
        },
        $G:function (id) {
            return document.getElementById(id)
        },
        getFirstNode:function (ele) {
            return ele.firstChild.nodeType == 1 ? ele.firstChild : ele.firstElementChild;
        },
        getElementsByClassName:function (clsName) {
            var doc = document;
            if (!doc.getElementsByClassName) {
                var clsArr = [];
                var reg = new RegExp("\\b" + clsName + "\\b");
                var eleArr = doc.getElementsByTagName("*");
                for (var i = 0, eleobj; eleobj = eleArr[i++];) {
                    if (reg.test(eleobj.className))
                        clsArr.push(eleobj);
                }
                return clsArr;
            }
            else {
                return doc.getElementsByClassName(clsName);
            }
        },
        getCharCode:function (event) {
            return event.keyCode || event.which || event.charCode;
        },
        getStyleValue:function(ele,attr){
            var doc=document;
            var style=ele.currentStyle||doc.defaultView.getComputedStyle(ele,null);
            return parseInt(style[attr].replace(/px/g,""));
        },
        getBrowerVersion:function(){
            var agent = navigator.userAgent.toLowerCase(),
                opera = window.opera,
                browser = {
                    ie		: !!window.ActiveXObject,
                    webkit	: ( agent.indexOf( ' applewebkit/' ) > -1 ),
                    quirks : ( document.compatMode == 'BackCompat' ),
                    opera	: ( !!opera && opera.version )
                };
            if ( browser.ie ){
                browser.version = parseFloat( agent.match( /msie (\d+)/ )[1] );
            }
            browser.gecko = ( navigator.product == 'Gecko' && !browser.webkit && !browser.opera );
            return browser;
        },
        isArray:function (obj) {
            return Object.prototype.toString.call(obj) === '[object Array]';
        },
        request:function (option) {
            var ajaxRequest = creatAjaxRequest();
            if (ajaxRequest == null) {
                alert("您的浏览器不支持AJAX！");
                return;
            }
            ajaxRequest.onreadystatechange = function () {
                if (ajaxRequest.readyState == 4) {
                    if (ajaxRequest.status >= 200 && ajaxRequest.status < 300 || ajaxRequest.status == 304) {
                        option.onSuccess(ajaxRequest.responseText);
                    }
                }
                else {
                    if (option.hasLoading)
                        util.$G(option.loading_Id).innerHTML = "<div class='hook_con'><img class='loading_pic' src='images/loading.gif'/></div>";
                }
            };
            ajaxRequest.open("post", option.url, true);
            ajaxRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            ajaxRequest.send(option.param);
        }
    };

    /**
     * 创建一个ajaxRequest对象
     */
    function creatAjaxRequest() {
        var xmlHttp = null;
        if (window.XMLHttpRequest) {
            xmlHttp = new XMLHttpRequest();
        } else {
            try {
                xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (e) {
                try {
                    xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
                } catch (e) {
                }
            }
        }
        return xmlHttp;
    }
}();
```

2. 工具包使用`effect.js`

```javascript
/**
 * Created by JetBrains PhpStorm.
 * User: xuheng
 * Date: 12-4-9
 * Time: 上午10:16
 * To change this template use File | Settings | File Templates.
 */

/**
 * 订阅邮件
 * @param txtId
 * @param btnId
 */
function subEmail(txtId, btnId) {
    function checkEmail(data) {
        if (data.length == 0) {
            alert("数据不能为空!");
            return false;
        }
        var pattern = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
        var flag = pattern.test(data);
        if (!flag) {
            alert("邮箱地址不合法！");
            return false;
        }
        return true;
    }

    var txtBox = util.$G(txtId);
    util.on(txtBox, "click", function (event) {
        var target = util.getTarget(util.getEvent(event));
        txtBox.value = "";
        txtBox.style.color = "#000";
    });

    var tmpHandler = function () {
        if (checkEmail(txtBox.value)) {
            util.request(
                {
                    url:"../build/build_email.php",
                    onSuccess:function (responseTxt) {
                        alert("感谢您的订阅，我们发布新版本时会通过邮件告诉您");
                        txtBox.value = "";
                    },
                    param:"q=" + txtBox.value + "&rand=" + Math.random(),
                    loading_Id:null,
                    hasLoading:false
                });
        }
    };
    util.on(util.$G(btnId), "click", tmpHandler);
    util.on(util.$G(txtId), "keyup", function (event) {
        var evt = util.getEvent(event);
        if (util.getCharCode(evt) == 13 && !!evt.ctrlKey) {
            tmpHandler();
        }
    });
}

/**
 * 切换面板
 *@param obj
 */
function switchTab(obj) {
    var ltab = util.$G(obj.ltab),
        rtab = util.$G(obj.rtab),
        r2tab = util.$G(obj.r2tab),
        lpanel = util.$G(obj.lpanel),
        rpanel = util.$G(obj.rpanel),
        r2panel = util.$G(obj.r2panel);

    util.on(ltab, "click", function () {
        ltab.className = "cur";
        rtab.className = "border-rt";
        r2tab.className = "border-rt";

        lpanel.style.display = "block";
        rpanel.style.display = "none";
        r2panel.style.display = "none";
    });

    util.on(rtab, "click", function () {
        ltab.className = "border-lt";
        rtab.className = "cur";
        r2tab.className = "border-rt";

        lpanel.style.display = "none";
        rpanel.style.display = "block";
        r2panel.style.display = "none";
    });

    util.on(r2tab, "click", function () {
        ltab.className = "border-lt";
        rtab.className = "border-rt";
        r2tab.className = "cur";

        lpanel.style.display = "none";
        rpanel.style.display = "none";
        r2panel.style.display = "block";
    });
}

/**
 * 展开闭合
 *@param issueCls
 * @param conCls
 */
function Pop(issueCls, conCls, isDoc) {
    var doc = document;
    this.popUpId = 0;
    this.popDownId = 0;
    this.isDoc = isDoc;

    this.issueArr = util.getElementsByClassName(issueCls);
    this.conArr = util.getElementsByClassName(conCls);
    this.init();
}
Pop.prototype = {
    init:function () {
        var _this = this,
            version = 0;
        if (!!window.ActiveXObject) {
            version = parseFloat(navigator.userAgent.toLowerCase().match(/msie (\d+)/)[1]);
        }
        for (var i = 0; i < this.issueArr.length; i++) {
            var issueObj = this.issueArr[i],
                conObj = this.conArr[i],
                heightSum = conObj.scrollHeight;
            //ie6 7下搜索动画显示不全
            if (this.isDoc && (version == 7 || version == 6)) {
                heightSum += 30;
            }

            util.on(issueObj, "click", function (issueObj, conObj, heightSum, _this) {
                return function (event) {
                    _this.closeOther(util.getTarget(util.getEvent(event)));
                    if (conObj.offsetHeight == 0) {
                        _this.popUpId = setInterval(function () {
                            _this.popUp(conObj, heightSum)
                        }, 30);
                    }
                    else if (conObj.offsetHeight == heightSum) {
                        _this.popDownId = setInterval(function () {
                            _this.popDown(conObj)
                        }, 30);
                    }
                }
            }(issueObj, conObj, heightSum, _this));
        }
    },
    popUp:function (con, heightSum) {
        var height = con.offsetHeight;
        var speed = Math.ceil((heightSum - height) / 8);
        if (con.offsetHeight == heightSum)
            clearInterval(this.popUpId);
        else
            con.style.height = height + speed + "px";
    },
    popDown:function (con) {
        var height = con.offsetHeight;
        var speed = Math.ceil(height / 8);
        if (con.offsetHeight == 0)
            clearInterval(this.popDownId);
        else
            con.style.height = height - speed + "px";
    },
    closeOther:function (target) {
        var nextNode = target.nextSibling.nodeType == 1 ? target.nextSibling : target.nextElementSibling;
        for (var i = 0; i < this.conArr.length; i++) {
            var conObj = this.conArr[i];
            if (nextNode != conObj && conObj.offsetHeight != 0) {
                clearInterval(this.popUpId);
                conObj.style.height = "0px";
            }
        }
    }
};

/**
 * 返回顶端
 */
function ToTop(btnId) {
    this.backTopId = 0;
    this.btn = util.$G(btnId);
    this.init();
}
ToTop.prototype = {
    init:function () {
        var _this = this,
            version = 0;
        if (!!window.ActiveXObject) {
            version = parseFloat(navigator.userAgent.toLowerCase().match(/msie (\d+)/)[1]);
        }
        util.on(this.btn, "click", function () {
            _this.backTopId = setInterval(function () {
                _this.backTop()
            }, 30);
        });
        util.on(window, "scroll", function () {
            var doc = document,
                scrTop = doc.documentElement.scrollTop || doc.body.scrollTop;

            _this.btn.style.display = scrTop >= 100 ? "block" : "none";
            //ie6下返回顶端位置
            if (version == 6)  _this.btn.style.top = 600 + scrTop + "px";
        });
    },
    backTop:function () {
        var doc = document,
            scrTop = doc.documentElement.scrollTop || doc.body.scrollTop,
            speed = Math.ceil(scrTop / 4);
        if (scrTop == 0)
            clearInterval(this.backTopId);
        else
            doc.documentElement.scrollTop = doc.body.scrollTop = scrTop - speed;
    }
};
/**
 * family展示
 */
function initFamily() {
    var doc = document,
        familyBtn = util.$G("J_family");

    function getViewportElement() {
        var browser=util.getBrowerVersion();
        return (browser.ie && browser.quirks) ?
            document.body : document.documentElement;
    }

    function setImgCenter() {
        var show = util.$G("J_show"),
            viewportEl = getViewportElement(),
            width = (window.innerWidth || viewportEl.clientWidth) | 0,
            height = (window.innerHeight || viewportEl.clientHeight) | 0,
            scrTop = doc.documentElement.scrollTop || doc.body.scrollTop;

        show.style.cssText = "left:" + (width / 2 - show.offsetWidth / 2)
            + "px;top:" + (height / 2 - show.offsetHeight / 2+scrTop) + "px;";
    }

    util.on(familyBtn, "click", function () {
        if (!util.$G("J_mask")) {
            var fragement = doc.createDocumentFragment(),
                mask = doc.createElement("div"),
                show = doc.createElement("div");

            mask.id = "J_mask";
            mask.className = "mask";
            fragement.appendChild(mask);

            show.id = "J_show";
            show.className = "show";
            show.innerHTML = "<img src='images/member-2014.jpg' alt='UEditor全家福'>" +
                "<a class='close' id='J_close'></a>";
            fragement.appendChild(show);

            util.$G("wrapper").appendChild(fragement);
            setImgCenter();

            util.on(util.$G("J_close"), "click", function () {
                util.$G("J_mask").style.display = "none";
                util.$G("J_show").style.display = "none";
            });
        } else {
            util.$G("J_mask").style.display = "";
            util.$G("J_show").style.display = "";
            setImgCenter();
        }
    });
}
/**
 * 边框旋转
 */
function border_move() {
    var borTop = util.$G("J_borTop"),
        borBottom = util.$G("J_borBottom"),
        borLeft = util.$G("J_borLeft"),
        borRight = util.$G("J_borRight"),

        left = util.getStyleValue(borTop, 'left'),
        top = util.getStyleValue(borLeft, 'top');

    setInterval(function () {
        if (left < 0) {
            left += 2;
            borRight.style.top = left + "px";
            borTop.style.left = left + "px";
        } else left = -1500;

        if (top > -3000) {
            top -= 2;
            borBottom.style.left = top + "px";
            borLeft.style.top = top + "px";
        } else top = -1500;
    }, 60);
}

/**
 * 切换面板
 *@param obj
 */
function switchTab(obj) {
    var ltab = util.$G(obj.ltab),
        rtab = util.$G(obj.rtab),
        r2tab = util.$G(obj.r2tab),
        lpanel = util.$G(obj.lpanel),
        rpanel = util.$G(obj.rpanel),
        r2panel = util.$G(obj.r2panel);

    util.on(ltab, "click", function () {
        ltab.className = "cur";
        rtab.className = "border-rt";
        r2tab.className = "border-rt";

        lpanel.style.display = "block";
        rpanel.style.display = "none";
        r2panel.style.display = "none";
    });

    util.on(rtab, "click", function () {
        ltab.className = "border-lt";
        rtab.className = "cur";
        r2tab.className = "border-rt";

        lpanel.style.display = "none";
        rpanel.style.display = "block";
        r2panel.style.display = "none";
    });

    util.on(r2tab, "click", function () {
        ltab.className = "border-lt";
        rtab.className = "border-rt";
        r2tab.className = "cur";

        lpanel.style.display = "none";
        rpanel.style.display = "none";
        r2panel.style.display = "block";
    });
}
```

##有道学堂[有道词典•看天下](http://xue.youdao.com/zx/ '有道词典•看天下')

1. 判断手机类型`mobile.js`

```javascript
var isMobile = {
    Android: function() {
        return navigator.userAgent.match(/Android/i);
    },
    BlackBerry: function() {
        return navigator.userAgent.match(/BlackBerry/i);
    },
    iOS: function() {
        return navigator.userAgent.match(/iPhone|iPad|iPod/i);
    },
    Opera: function() {
        return navigator.userAgent.match(/Opera Mini/i);
    },
    Windows: function() {
        return navigator.userAgent.match(/IEMobile/i);
    },
    any: function() {
        return (isMobile.Android() || isMobile.BlackBerry() || isMobile.iOS() || isMobile.Opera() || isMobile.Windows());
    }
};
```
2. cookie判断`referer.js`

```javascript
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) 
        return unescape(r[2]);
    return null;
}

function getCookies(prefix) {
    if (document.cookie.length > 0)
    {
        var cookies = "";
        start = 0;
        n_start = document.cookie.indexOf(prefix + "_", start);
        if (n_start != -1)
        {
            v_start = n_start + prefix.length + 1;
            v_end = document.cookie.indexOf(";", v_start);
            if (v_end == -1) 
                v_end = document.cookie.length;
            cookies += document.cookie.substring(n_start, v_end);
            start = v_end;
            n_start = document.cookie.indexOf(prefix + "_", start)
        }
        return cookies;
    }
    return ""
}
```
3. Ajax请求`wti_like_post.js`
```javascript
jQuery(document).ready(function(){
     jQuery(".like img, .unlike img").click(function(){
          var task = jQuery(this).attr("rel");
          var post_id = jQuery(this).attr("pID");
          
          jQuery("#status").html("&nbsp;&nbsp;").addClass("loading-img");
          
          jQuery.ajax({
               type: "POST",
               url: "/zx/wp-content/plugins/wti-like-post/wti_like.php",
               data: "post_id=" + post_id + "&task=" + task + "&num=" + Math.random(),
               success: function(data){
                    jQuery("#status").removeClass("loading-img").empty().html(data.msg);
                    if (data.error == '0') {
                        jQuery.ajax({
                            type: "POST",
                            url: "http://xue.youdao.com/wtilike",
                            data: "epid=" + post_id + "&task=" + task,
                            success: function(data){
                                jQuery("#lc").html(data.likeCount);
                                jQuery("#unlc").html(data.unlikeCount);
                            },
                            dataType: "json"
                        });
                    }
               },
               dataType: "json"
          });

     });
});
```