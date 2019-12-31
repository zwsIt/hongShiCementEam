package com.supcon.mes.middleware.ui;

import android.annotation.SuppressLint;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseWebViewActivity;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.view.js.BridgeWebView;
import com.supcon.common.view.view.js.BridgeWebViewClientNew;
import com.supcon.common.view.view.js.CallBackFunction;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.AttachmentController;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

/**
 * FileViewActivity
 * created by zhangwenshuai1 2019/12/20
 * w文档预览
 */
@Router(Constant.Router.FILE_VIEW)
public class FileViewActivity extends BaseWebViewActivity {

    @BindByTag("titleText")
    TextView titleText;

    private boolean isFirstIn = true;

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        super.initView();
//        webView.setWebChromeClient(new CustomInnerChromeClient());
        webView.setWebViewClient(new CustomWebViewClient(webView));
        ((ViewGroup) findViewById(R.id.leftBtn).getParent()).setBackgroundResource(R.color.h5Theme);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.h5Theme);

        titleText.setText("文档预览");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_file_view;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void showDialog(String message, JsResult result) {
        super.showDialog(message, result);
        new CustomDialog(context)
                .twoButtonAlertDialog(message)
                .bindClickListener(R.id.redBtn, v -> {
                    result.confirm();
                }, true)
                .bindClickListener(R.id.grayBtn, v -> {
                    result.cancel();
                }, true)
                .show();
    }

    @Override
    protected void initData() {
        super.initData();
//        webView.loadUrl("http://mozilla.github.io/pdf.js/web/viewer.html?file=" + getIntent().getStringExtra(BaseConstant.WEB_URL));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isList && !isFirstIn) {
            onReload();
        }
        isFirstIn = false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {
//        onReload();
    }

    @Override
    public void sendRefreshEvent() {
        super.sendRefreshEvent();
        if (!isList) {
            EventBus.getDefault().post(new RefreshEvent());
        }
    }


    @Override
    public void goNext(String url) {


//        String firstUrl = url.substring(0,url.indexOf("file=") +5);
//        String encodeUrl = url.substring(url.indexOf("file=") +5);
//        try {
//            encodeUrl = URLEncoder.encode(encodeUrl,"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        Bundle bundle = new Bundle();
//        bundle.putString(BaseConstant.WEB_COOKIE, EamApplication.getCooki());
//        bundle.putString(BaseConstant.WEB_AUTHORIZATION, EamApplication.getAuthorization());
//        bundle.putString(BaseConstant.WEB_URL, firstUrl + encodeUrl);
//        IntentRouter.go(context, Constant.Router.FILE_VIEW, bundle);
//        if (!isList) {
//            back();
//            executeBackwardAnim();
//        }

    }

    @Override
    protected void uploadFile(File file, CallBackFunction callBackFunction) {

        new AttachmentController().uploadAttachment(new OnAPIResultListener<String>() {
            @Override
            public void onFail(String errorMsg) {

            }

            @Override
            public void onSuccess(String result) {
                LogUtil.d("result:" + result);

                callBackFunction.onCallBack(result + "|" + file.getName());
            }
        }, file);

    }

    /**
     * CustomWebViewClient
     * created by zhangwenshuai1 2019/12/21
     * 内部类：为解决重定向url 不解码
     */
    private class CustomWebViewClient extends BridgeWebViewClientNew {
        public CustomWebViewClient(BridgeWebView webView) {
            super(webView);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            return super.shouldOverrideUrlLoading(view, url);
            return false;// WebView加载该Url
        }
    }


    /**
     * TSDCommonActivity
     * created by zhangwenshuai1 2019/11/27
     * 内部类：为解决加载bar隐藏
     */
//    private class CustomInnerChromeClient extends DefaultWebChromeClient {
//        @Override
//        public void onProgressChanged(WebView view, int newProgress) {
//            super.onProgressChanged(view, newProgress);
//            pbProgress.setVisibility(View.GONE);
//        }
//    }

}
