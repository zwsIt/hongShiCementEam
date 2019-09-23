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
@Router(Constant.Router.SD)
public class SDListActivity extends BaseWebViewActivity {

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
        if(!EamApplication.isDev()) {
            ((ViewGroup) findViewById(R.id.leftBtn).getParent()).setBackgroundResource(R.color.mobileValueColor);
            StatusBarUtils.setWindowStatusBarColor(this, R.color.mobileValueColor);
        }
        titleText.setText("送电申请");
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

    @Override
    public void unauthorized() {
        super.unauthorized();
    }

    @Override
    public void serverError() {
        super.serverError();
    }

    @Override
    public void sendRefreshEvent() {
        super.sendRefreshEvent();
        if(!isList){
            EventBus.getDefault().post(new RefreshEvent());
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {
//        LogUtil.w("RefreshEvent");
//        onReload();

    }

    @Override
    protected void onReload() {
        webView.reload();
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
    public void goNext(String url) {

        Bundle bundle = new Bundle();
        bundle.putString(BaseConstant.WEB_COOKIE, EamApplication.getCooki());
        bundle.putString(BaseConstant.WEB_AUTHORIZATION, EamApplication.getAuthorization());
        bundle.putString(BaseConstant.WEB_URL, url);

        IntentRouter.go(context, Constant.Router.SD, bundle);
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
