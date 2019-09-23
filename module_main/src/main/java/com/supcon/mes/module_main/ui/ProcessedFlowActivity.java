package com.supcon.mes.module_main.ui;

import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseWebViewActivity;
import com.supcon.common.view.view.js.BridgeWebView;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.module_main.R;

import org.greenrobot.eventbus.EventBus;


@Router(Constant.Router.PROCESSED_FLOW)
public class ProcessedFlowActivity extends BaseWebViewActivity {

    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("webview")
    BridgeWebView webview;

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("流程描述");
        webview.getSettings().setLoadWithOverviewMode(true);
    }


    @Override
    protected int getLayoutID() {
        return R.layout.ac_process_flow;
    }


    @Override
    public void sendRefreshEvent() {
        super.sendRefreshEvent();
        if (!isList) {
            EventBus.getDefault().post(new RefreshEvent());
        }
    }


}
