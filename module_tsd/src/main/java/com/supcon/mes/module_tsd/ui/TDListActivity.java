package com.supcon.mes.module_tsd.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.supcon.common.BaseConstant;
import com.supcon.common.view.base.activity.BaseWebViewActivity;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.view.js.CallBackFunction;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.AttachmentController;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.module_tsd.IntentRouter;
import com.supcon.mes.module_tsd.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

/**
 * Created by wangshizhan on 2018/12/27
 * Email:wangshizhan@supcom.com
 */
@Router(Constant.Router.TD)
public class TDListActivity extends BaseWebViewActivity {

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

        if(!EamApplication.isDev()){
            ((ViewGroup)findViewById(R.id.leftBtn).getParent()).setBackgroundResource(R.color.mobileValueColor);
            StatusBarUtils.setWindowStatusBarColor(this, R.color.mobileValueColor);
        }

        titleText.setText("停电申请");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_td_list;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        /*setWebChromeClient(new WebChromeClient() {

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    // 网页加载完成
                    pbProgress.setVisibility(View.GONE);
                } else {
                    // 加载中
                    pbProgress.setVisibility(View.VISIBLE);
                    pbProgress.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                LogUtil.d("onCreateWindow");
                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                LogUtil.d("onHideCustomView");
            }

            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);
                LogUtil.d("onCloseWindow");
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

                if(!TextUtils.isEmpty(title) && title.length() > 10){
                    return;
                }
                LogUtil.d("onReceivedTitle:"+title);
                setTitle(title);
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                LogUtil.d("onShowCustomView");
            }

            @Override
            public void onRequestFocus(WebView view) {
                super.onRequestFocus(view);
                LogUtil.d("onRequestFocus");
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                LogUtil.d("onJsAlert url:"+url+" message:"+message+" ");
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                LogUtil.d("onJsConfirm url:"+url+" message:"+message);
                new CustomDialog(context)
                        .twoButtonAlertDialog(message)
                        .bindClickListener(R.id.redBtn, v -> {result.confirm();}, true)
                        .bindClickListener(R.id.grayBtn, v -> {result.cancel();}, true)
                        .show();

                return true;
//                return super.onJsConfirm(view, url, message, result);
            }
        });*/

    }

    @Override
    protected void showDialog(String message, JsResult result) {
        super.showDialog(message, result);
        new CustomDialog(context)
                .twoButtonAlertDialog(message)
                .bindClickListener(R.id.redBtn, v -> {result.confirm();}, true)
                .bindClickListener(R.id.grayBtn, v -> {result.cancel();}, true)
                .show();
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isList && !isFirstIn) {
            onReload();
        }
        isFirstIn = false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {
//        LogUtil.w("RefreshEvent");
//        onReload();

    }

    @Override
    public void sendRefreshEvent() {
        super.sendRefreshEvent();
        if(!isList){
            EventBus.getDefault().post(new RefreshEvent());
        }
    }


    @Override
    public void goNext(String url) {

        Bundle bundle = new Bundle();
        bundle.putString(BaseConstant.WEB_COOKIE, EamApplication.getCooki());
        bundle.putString(BaseConstant.WEB_AUTHORIZATION, EamApplication.getAuthorization());
        bundle.putString(BaseConstant.WEB_URL, url);
        IntentRouter.go(context, Constant.Router.TD, bundle);
        if(!isList){
            back();
            executeBackwardAnim();
        }

    }

    @Override
    protected void uploadFile(File file, CallBackFunction callBackFunction) {

        new AttachmentController().uploadAttachment(new OnAPIResultListener<String>() {
            @Override
            public void onFail(String errorMsg) {

            }

            @Override
            public void onSuccess(String result) {
                LogUtil.d("result:"+result);

                callBackFunction.onCallBack(result+"|"+file.getName());
            }
        }, file);

    }

}
