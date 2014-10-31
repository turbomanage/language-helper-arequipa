(function(){var g=this,h=function(a){var b=typeof a;if("object"==b)if(a){if(a instanceof Array)return"array";if(a instanceof Object)return b;var c=Object.prototype.toString.call(a);if("[object Window]"==c)return"object";if("[object Array]"==c||"number"==typeof a.length&&"undefined"!=typeof a.splice&&"undefined"!=typeof a.propertyIsEnumerable&&!a.propertyIsEnumerable("splice"))return"array";if("[object Function]"==c||"undefined"!=typeof a.call&&"undefined"!=typeof a.propertyIsEnumerable&&!a.propertyIsEnumerable("call"))return"function"}else return"null";
else if("function"==b&&"undefined"==typeof a.call)return"object";return b},aa=function(a,b,c){return a.call.apply(a.bind,arguments)},ba=function(a,b,c){if(!a)throw Error();if(2<arguments.length){var d=Array.prototype.slice.call(arguments,2);return function(){var c=Array.prototype.slice.call(arguments);Array.prototype.unshift.apply(c,d);return a.apply(b,c)}}return function(){return a.apply(b,arguments)}},l=function(a,b,c){l=Function.prototype.bind&&-1!=Function.prototype.bind.toString().indexOf("native code")?
aa:ba;return l.apply(null,arguments)},m=function(a,b){function c(){}c.prototype=b.prototype;a.w=b.prototype;a.prototype=new c;a.v=function(a,c,e){return b.prototype[c].apply(a,Array.prototype.slice.call(arguments,2))}};Function.prototype.bind=Function.prototype.bind||function(a,b){if(1<arguments.length){var c=Array.prototype.slice.call(arguments,1);c.unshift(this,a);return l.apply(null,c)}return l(this,a)};var n=function(a){if(Error.captureStackTrace)Error.captureStackTrace(this,n);else{var b=Error().stack;b&&(this.stack=b)}a&&(this.message=String(a))};m(n,Error);n.prototype.name="CustomError";var ca=function(a,b){for(var c=a.split("%s"),d="",f=Array.prototype.slice.call(arguments,1);f.length&&1<c.length;)d+=c.shift()+f.shift();return d+c.join("%s")},p=String.prototype.trim?function(a){return a.trim()}:function(a){return a.replace(/^[\s\xa0]+|[\s\xa0]+$/g,"")},q=function(a,b){return a<b?-1:a>b?1:0};var r=function(a,b){b.unshift(a);n.call(this,ca.apply(null,b));b.shift()};m(r,n);r.prototype.name="AssertionError";var s=function(a,b,c,d){var f="Assertion failed";if(c)var f=f+(": "+c),e=d;else a&&(f+=": "+a,e=b);throw new r(""+f,e||[]);},v=function(a,b,c){a||s("",null,b,Array.prototype.slice.call(arguments,2))},da=function(a,b,c){var d=typeof a;("object"==d&&null!=a||"function"==d)&&1==a.nodeType||s("Expected Element but got %s: %s.",[h(a),a],b,Array.prototype.slice.call(arguments,2));return a};var w=Array.prototype,x=w.forEach?function(a,b,c){v(null!=a.length);w.forEach.call(a,b,c)}:function(a,b,c){for(var d=a.length,f="string"==typeof a?a.split(""):a,e=0;e<d;e++)e in f&&b.call(c,f[e],e,a)},ea=w.map?function(a,b,c){v(null!=a.length);return w.map.call(a,b,c)}:function(a,b,c){for(var d=a.length,f=Array(d),e="string"==typeof a?a.split(""):a,k=0;k<d;k++)k in e&&(f[k]=b.call(c,e[k],k,a));return f};var y=function(a,b){for(var c in a)b.call(void 0,a[c],c,a)},fa=function(a){var b=0,c;for(c in a)b++;return b};var z;t:{var A=g.navigator;if(A){var B=A.userAgent;if(B){z=B;break t}}z=""};var ga=-1!=z.indexOf("Opera")||-1!=z.indexOf("OPR"),C=-1!=z.indexOf("Trident")||-1!=z.indexOf("MSIE"),D=-1!=z.indexOf("Gecko")&&-1==z.toLowerCase().indexOf("webkit")&&!(-1!=z.indexOf("Trident")||-1!=z.indexOf("MSIE")),E=-1!=z.toLowerCase().indexOf("webkit"),F=function(){var a=g.document;return a?a.documentMode:void 0},G=function(){var a="",b;if(ga&&g.opera)return a=g.opera.version,"function"==h(a)?a():a;D?b=/rv\:([^\);]+)(\)|;)/:C?b=/\b(?:MSIE|rv)[: ]([^\);]+)(\)|;)/:E&&(b=/WebKit\/(\S+)/);b&&(a=
(a=b.exec(z))?a[1]:"");return C&&(b=F(),b>parseFloat(a))?String(b):a}(),H={},I=function(a){var b;if(!(b=H[a])){b=0;for(var c=p(String(G)).split("."),d=p(String(a)).split("."),f=Math.max(c.length,d.length),e=0;0==b&&e<f;e++){var k=c[e]||"",oa=d[e]||"",pa=/(\d*)(\D*)/g,qa=/(\d*)(\D*)/g;do{var t=pa.exec(k)||["","",""],u=qa.exec(oa)||["","",""];if(0==t[0].length&&0==u[0].length)break;b=q(0==t[1].length?0:parseInt(t[1],10),0==u[1].length?0:parseInt(u[1],10))||q(0==t[2].length,0==u[2].length)||q(t[2],u[2])}while(0==
b)}b=H[a]=0<=b}return b},J=g.document,ha=J&&C?F()||("CSS1Compat"==J.compatMode?parseInt(G,10):5):void 0;var K=function(a,b){this.d=a;this.c=b;this.d.g.addListener(this.s,this);this.c.i.addListener(this.r,this)};K.prototype.s=function(){if(this.c.expand()){var a=this.d;v(a.b);a.a.style.visibility="hidden"}};K.prototype.r=function(a){"close"==a&&(this.d.show(),this.c.collapse())};var L;if(!(L=!D&&!C)){var M;if(M=C)M=C&&9<=ha;L=M}L||D&&I("1.9.1");C&&I("9");var N=function(a){return a.contentDocument||a.contentWindow.document},O=function(a){var b;(b=a.contentWindow)||(b=(a=N(a))?a.parentWindow||a.defaultView:window);return b};var P=function(){this.m=[]};P.prototype.addListener=function(a,b){b&&(a=l(a,b));this.m.push(a)};var Q=function(a,b){x(a.m,function(a){a(b)})};var S=function(a,b){this.a=a;this.k=b;this.a.frameBorder=0;this.a.style.borderStyle="none";this.a.style.position="fixed";R(this,this.k)},T={transform:["ms","webkit"],transition:["webkit"]};S.prototype.e=function(a){var b={};x(arguments,l(function(a){b[a]=this.k[a]},this));R(this,b)};var R=function(a,b){y(b,l(function(a,b){U(this,b,a)},a))},U=function(a,b,c){a.a.style[b]=c;b in T&&x(T[b],l(function(a){this.a.style[a+(b[0].toUpperCase()+b.substring(1))]=c},a))};var ia=function(){return da(document.querySelector("script[data-helpouts-embed]"))},V=function(a){return ia().getAttribute("data-helpouts-"+a)},X=function(a,b){var c=W+a;if(0<fa(b)){var d=[];y(b,function(a,b){d.push(encodeURIComponent(b)+"="+encodeURIComponent(a))});c+="?"+d.join("&")}return c},ja=function(a,b){var c=X(a,b),d=new XMLHttpRequest;d.open("GET",c,!0);return d},W,ka=ia(),Y=document.createElement("a");Y.href=ka.src;W=Y.protocol+"//"+Y.host;var la=function(a,b){b=p(b);b=b.replace(/\s+/g,"-");b=b.toLowerCase();a&&0!=b.lastIndexOf(a,0)&&(b=a+"-"+b);return b};var Z=function(a){S.call(this,a,ma);this.i=new P;this.j=!1;this.l="true"==V("standalone");this.l||(window.addEventListener("message",l(this.u,this)),a.name="helpouts-widget",a.src=X("/ps/widget",na()))};m(Z,S);
var ma={top:"0",right:"0",height:"100%",width:"365px",transition:"all .4s cubic-bezier(0.4, 0.0, 0.2, 1)",background:"#f5f5f5","box-shadow":"0px 0px 6px 3px rgba(0, 0, 0, 0.4)","z-index":"100000",transform:"translateZ(0) translateX(371px)"},na=function(){var a={};window.gotee_widget_compilemode&&(a.compile=window.gotee_widget_compilemode);var b=V("vertical"),c;c=V("tags").split(",");var d=V("prefix");c=ea(c,l(la,null,d));c=c.join(",");b&&c&&(a.vertical=b,a.tags=c);a.origin=window.location.toString();
return a};Z.prototype.u=function(a){if(a.origin==W){var b=a.data;"load"==a.data?ra(this):Q(this.i,b)}};Z.prototype.expand=function(){if(this.l)return window.open(X("/partner/ask",na())),!1;U(this,"transform","translateZ(0) translateX(0)");this.j=!0;ra(this);return!0};var ra=function(a){O(a.a).postMessage("AllowFrame: HelpoutsWidget",W);a.j&&O(a.a).postMessage("AllowAPI: HelpoutsWidget",W)};Z.prototype.collapse=function(){this.e("transform")};var sa={BASEURL:W},ta=function(a){y(sa,function(b,c){a=a.split("{{"+c+"}}").join(b)});return a};var $=function(a){var b=V("trigger");this.f=b?b:"0";S.call(this,a,{width:"100px",height:"80px",right:"0px",top:"android"==V("prefix")||"1"==this.f?"150px":"200px",transition:"all 0.25s ease-in-out",visibility:"","z-index":"1000"});this.b=!1;this.g=new P;a.onload=l(this.t,this);a.name="helpouts-trigger";a.src="javascript:"};m($,S);
$.prototype.t=function(){var a;t:{switch(this.f){case "0":case "1":a=ta('<style>    body {    color: white;    overflow-x: hidden;    cursor: pointer;    margin: 0;    padding: 0;  }    .trigger {    background-color: #4285F4;    border-top-left-radius: 8px;    border-bottom-left-radius: 8px;    display: -webkit-flex;    display: -ms-flexbox;    display: flex;    -webkit-flex-direction: row;        -ms-flex-direction: row;            flex-direction: row;    -webkit-justify-content: flex-end;        -ms-flex-pack: end;            justify-content: flex-end;    -webkit-align-items: center;        -ms-flex-align: center;            align-items: center;    height: 50px;    overflow-y: visible;  }    .logo {    -webkit-transition: opacity 0.5s margin-top 0.5s width 0.5s height 0.5s;            transition: opacity 0.5s margin-top 0.5s width 0.5s height 0.5s;    -webkit-flex-shrink: 0;        -ms-flex-negative: 0;            flex-shrink: 0;  }    .google-logo {    padding-left: 15px;    margin-top: 8px;    width: 35px;    height: 33px;    background-color: #4285F4;  }    .text {    font-family: sans-serif;    font-size: 16px;    white-space: nowrap;    -webkit-flex-grow: 1;        -ms-flex-positive: 1;            flex-grow: 1;  }    .helpouts-logo {    -webkit-align-self: flex-start;        -ms-flex-item-align: start;            align-self: flex-start;    width: 50px;    height: 60px;    background: -webkit-linear-gradient(#4285F4 50px, transparent 0px);    background: linear-gradient(#4285F4 50px, transparent 0px);  }</style><div class="trigger">  <img class="logo google-logo" src="{{BASEURL}}/ps/res/images/trigger-googlelogo-blue.png"></img>  <span class="text">Click here to talk to an expert</span>  <img class="logo helpouts-logo" src="{{BASEURL}}/ps/res/images/trigger-helpoutslogo-cyan.png"></img></div>');break t;
case "2":a=ta('<style>    body {    color: white;    overflow-x: hidden;    cursor: pointer;    margin: 0;    padding: 0;  }    .trigger {    background-color: #4285F4;    border-top-left-radius: 8px;    border-bottom-left-radius: 8px;    display: -webkit-flex;    display: -ms-flexbox;    display: flex;    -webkit-flex-direction: row;        -ms-flex-direction: row;            flex-direction: row;    -webkit-justify-content: flex-end;        -ms-flex-pack: end;            justify-content: flex-end;    -webkit-align-items: center;        -ms-flex-align: center;            align-items: center;    height: 50px;    overflow-y: visible;  }    .logo {    -webkit-transition: opacity 0.5s margin-top 0.5s width 0.5s height 0.5s;            transition: opacity 0.5s margin-top 0.5s width 0.5s height 0.5s;    -webkit-flex-shrink: 0;        -ms-flex-negative: 0;            flex-shrink: 0;  }    .text {    font-family: sans-serif;    font-size: 16px;    white-space: nowrap;    -webkit-flex-grow: 1;        -ms-flex-positive: 1;            flex-grow: 1;  }    .helpouts-logo {    -webkit-align-self: flex-start;        -ms-flex-item-align: start;            align-self: flex-start;    width: 50px;    height: 60px;    background: -webkit-linear-gradient(#4285F4 50px, transparent 0px);    background: linear-gradient(#4285F4 50px, transparent 0px);  }</style><div class="trigger">  <span class="text">Help</span>  <img class="logo helpouts-logo" src="{{BASEURL}}/ps/res/images/trigger-helpoutslogo-cyan.png"></img></div>');
break t}a=""}var b=N(this.a);b.body.innerHTML=a;this.b=!0;this.a.addEventListener("mouseover",l(this.q,this));this.a.addEventListener("mouseleave",l(this.p,this));b.addEventListener("click",l(this.o,this));b.addEventListener("visibilitychange",l(this.n,this))};$.prototype.n=function(){"visible"==N(this.a).visibilityState&&0==this.a.offsetTop&&(this.a.style.display="none",this.a.style.display="")};var ua=function(a){ja("/ps/event",{src:window.location.href,type:a}).send()};
$.prototype.show=function(){v(this.b);this.e("visibility")};$.prototype.q=function(){this.h=setTimeout(l(function(){ua(1)},this),500);v(this.b);this.a.style.width="320px"};$.prototype.p=function(){this.h&&clearTimeout(this.h);v(this.b);this.e("width","height")};$.prototype.o=function(){ua(2);Q(this.g,null)};var va=function(){if((C?I(10):D?I(31):E&&I(536))&&920<=window.innerWidth){var a=ja("/ps/show",{src:window.location.href});a.onload=function(){if(200==a.status){var b=document.createElement("iframe"),c=document.createElement("iframe");document.body.appendChild(b);document.body.appendChild(c);b=new $(b);c=new Z(c);new K(b,c)}};a.send()}},wa=function(){return"complete"==document.readyState?(va(),!0):!1};wa()||document.addEventListener("readystatechange",wa);
})();