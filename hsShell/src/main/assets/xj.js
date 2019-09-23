//notation: js file can only use this kind of comments
//since comments will cause error when use in webview.loadurl,
//comments will be remove by java use regexp
(function() {

    var bridge;

    function init(){

    }

    function checkJSBridge(){

        if(window.WebViewJavascriptBridge){

            if(!bridge){
                bridge = window.WebViewJavascriptBridge;
                connectWebViewJavascriptBridge(function(bridge) {

                    });
            }



            return true;
        }
        else{
            console.log('WebViewJavascriptBridge false');
            return false;
        }
    }
    function connectWebViewJavascriptBridge(callback) {
        if (window.WebViewJavascriptBridge) {
            callback(WebViewJavascriptBridge)
        } else {
            window.document.addEventListener(
                'WebViewJavascriptBridgeReady'
                , function() {
                    callback(WebViewJavascriptBridge)
                },
                false
            );
        }
    }

    function areaClick(id, code) {
        console.log("areaClick id:"+id+" code:"+code);
        if(!checkJSBridge()){
            return;
        }
        bridge.callHandler(
            'areaClick'
            , {'id': id, 'code':code}
            , function(responseData) {

            }
        );
    }

    window.xjjs = {
        init: init,
        checkJSBridge: checkJSBridge,
        connectWebViewJavascriptBridge:connectWebViewJavascriptBridge,
        areaClick: areaClick
    };





})();
