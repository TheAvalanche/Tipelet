function lv_telepit_AppWidgetSet(){var U='',R=' top: -1000px;',pb='" for "gwt:onLoadErrorFn"',nb='" for "gwt:onPropertyErrorFn"',$='");',qb='#',Db='.cache.js',sb='/',yb='//',Bb='1616FCBD917E101EB66F4DE6EA9E9CC0',Cb=':',hb='::',T='<!doctype html>',V='<html><head><\/head><body><\/body><\/html>',kb='=',rb='?',mb='Bad handler "',S='CSS1Compat',Y='Chrome',X='DOMContentLoaded',M='DUMMY',xb='base',vb='baseUrl',H='begin',N='body',G='bootstrap',ub='clear.cache.gif',jb='content',Fb='end',Z='eval("',I='gwt.codesvr.lv.telepit.AppWidgetSet=',J='gwt.codesvr=',ob='gwt:onLoadErrorFn',lb='gwt:onPropertyErrorFn',ib='gwt:property',db='head',O='iframe',tb='img',ab='javascript',P='javascript:""',Eb='loadExternalRefs',K='lv.telepit.AppWidgetSet',Ab='lv.telepit.AppWidgetSet.devmode.js',wb='lv.telepit.AppWidgetSet.nocache.js',gb='lv.telepit.AppWidgetSet::',eb='meta',cb='moduleRequested',bb='moduleStartup',fb='name',Q='position:absolute; width:0; height:0; border:none; left: -1000px;',_='script',zb='selectingPermutation',L='startup',W='undefined';var o=window;var p=document;r(G,H);function q(){var a=o.location.search;return a.indexOf(I)!=-1||a.indexOf(J)!=-1}
function r(a,b){if(o.__gwtStatsEvent){o.__gwtStatsEvent({moduleName:K,sessionId:o.__gwtStatsSessionId,subSystem:L,evtGroup:a,millis:(new Date).getTime(),type:b})}}
lv_telepit_AppWidgetSet.__sendStats=r;lv_telepit_AppWidgetSet.__moduleName=K;lv_telepit_AppWidgetSet.__errFn=null;lv_telepit_AppWidgetSet.__moduleBase=M;lv_telepit_AppWidgetSet.__softPermutationId=0;lv_telepit_AppWidgetSet.__computePropValue=null;lv_telepit_AppWidgetSet.__getPropMap=null;lv_telepit_AppWidgetSet.__gwtInstallCode=function(){};lv_telepit_AppWidgetSet.__gwtStartLoadingFragment=function(){return null};var s=function(){return false};var t=function(){return null};__propertyErrorFunction=null;var u=o.__gwt_activeModules=o.__gwt_activeModules||{};u[K]={moduleName:K};var v;function w(){y();return v}
function x(){y();return v.getElementsByTagName(N)[0]}
function y(){if(v){return}var a=p.createElement(O);a.src=P;a.id=K;a.style.cssText=Q+R;a.tabIndex=-1;p.body.appendChild(a);v=a.contentDocument;if(!v){v=a.contentWindow.document}v.open();var b=document.compatMode==S?T:U;v.write(b+V);v.close()}
function z(k){function l(a){function b(){if(typeof p.readyState==W){return typeof p.body!=W&&p.body!=null}return /loaded|complete/.test(p.readyState)}
var c=b();if(c){a();return}function d(){if(!c){c=true;a();if(p.removeEventListener){p.removeEventListener(X,d,false)}if(e){clearInterval(e)}}}
if(p.addEventListener){p.addEventListener(X,d,false)}var e=setInterval(function(){if(b()){d()}},50)}
function m(c){function d(a,b){a.removeChild(b)}
var e=x();var f=w();var g;if(navigator.userAgent.indexOf(Y)>-1&&window.JSON){var h=f.createDocumentFragment();h.appendChild(f.createTextNode(Z));for(var i=0;i<c.length;i++){var j=window.JSON.stringify(c[i]);h.appendChild(f.createTextNode(j.substring(1,j.length-1)))}h.appendChild(f.createTextNode($));g=f.createElement(_);g.language=ab;g.appendChild(h);e.appendChild(g);d(e,g)}else{for(var i=0;i<c.length;i++){g=f.createElement(_);g.language=ab;g.text=c[i];e.appendChild(g);d(e,g)}}}
lv_telepit_AppWidgetSet.onScriptDownloaded=function(a){l(function(){m(a)})};r(bb,cb);var n=p.createElement(_);n.src=k;p.getElementsByTagName(db)[0].appendChild(n)}
lv_telepit_AppWidgetSet.__startLoadingFragment=function(a){return C(a)};lv_telepit_AppWidgetSet.__installRunAsyncCode=function(a){var b=x();var c=w().createElement(_);c.language=ab;c.text=a;b.appendChild(c);b.removeChild(c)};function A(){var c={};var d;var e;var f=p.getElementsByTagName(eb);for(var g=0,h=f.length;g<h;++g){var i=f[g],j=i.getAttribute(fb),k;if(j){j=j.replace(gb,U);if(j.indexOf(hb)>=0){continue}if(j==ib){k=i.getAttribute(jb);if(k){var l,m=k.indexOf(kb);if(m>=0){j=k.substring(0,m);l=k.substring(m+1)}else{j=k;l=U}c[j]=l}}else if(j==lb){k=i.getAttribute(jb);if(k){try{d=eval(k)}catch(a){alert(mb+k+nb)}}}else if(j==ob){k=i.getAttribute(jb);if(k){try{e=eval(k)}catch(a){alert(mb+k+pb)}}}}}t=function(a){var b=c[a];return b==null?null:b};__propertyErrorFunction=d;lv_telepit_AppWidgetSet.__errFn=e}
function B(){function e(a){var b=a.lastIndexOf(qb);if(b==-1){b=a.length}var c=a.indexOf(rb);if(c==-1){c=a.length}var d=a.lastIndexOf(sb,Math.min(c,b));return d>=0?a.substring(0,d+1):U}
function f(a){if(a.match(/^\w+:\/\//)){}else{var b=p.createElement(tb);b.src=a+ub;a=e(b.src)}return a}
function g(){var a=t(vb);if(a!=null){return a}return U}
function h(){var a=p.getElementsByTagName(_);for(var b=0;b<a.length;++b){if(a[b].src.indexOf(wb)!=-1){return e(a[b].src)}}return U}
function i(){var a=p.getElementsByTagName(xb);if(a.length>0){return a[a.length-1].href}return U}
function j(){var a=p.location;return a.href==a.protocol+yb+a.host+a.pathname+a.search+a.hash}
var k=g();if(k==U){k=h()}if(k==U){k=i()}if(k==U&&j()){k=e(p.location.href)}k=f(k);return k}
function C(a){if(a.match(/^\//)){return a}if(a.match(/^[a-zA-Z]+:\/\//)){return a}return lv_telepit_AppWidgetSet.__moduleBase+a}
function D(){var f=[];var g;var h=[];var i=[];function j(a){var b=i[a](),c=h[a];if(b in c){return b}var d=[];for(var e in c){d[c[e]]=e}if(__propertyErrorFunc){__propertyErrorFunc(a,d,b)}throw null}
s=function(a,b){return b in h[a]};lv_telepit_AppWidgetSet.__getPropMap=function(){var a={};for(var b in h){if(h.hasOwnProperty(b)){a[b]=j(b)}}return a};lv_telepit_AppWidgetSet.__computePropValue=j;o.__gwt_activeModules[K].bindings=lv_telepit_AppWidgetSet.__getPropMap;r(G,zb);if(q()){return C(Ab)}var k;try{k=Bb;var l=k.indexOf(Cb);if(l!=-1){g=parseInt(k.substring(l+1),10);k=k.substring(0,l)}}catch(a){}lv_telepit_AppWidgetSet.__softPermutationId=g;return C(k+Db)}
function E(){if(!o.__gwt_stylesLoaded){o.__gwt_stylesLoaded={}}r(Eb,H);r(Eb,Fb)}
A();lv_telepit_AppWidgetSet.__moduleBase=B();u[K].moduleBase=lv_telepit_AppWidgetSet.__moduleBase;var F=D();E();r(G,Fb);z(F);return true}
lv_telepit_AppWidgetSet.succeeded=lv_telepit_AppWidgetSet();