package com.supcon.mes.module_login.ui;

import android.annotation.SuppressLint;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseActivity;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.ChannelUtil;
import com.supcon.mes.module_login.BuildConfig;
import com.supcon.mes.module_login.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2018/12/20
 * Email:wangshizhan@supcom.com
 */
@Router(Constant.Router.ABOUT)
public class AboutActivity extends BaseActivity {

    @BindByTag("aboutIv")
    ImageView aboutIv;

    @BindByTag("aboutName")
    TextView aboutName;

    @BindByTag("buildVersion")
    TextView buildVersion;

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;

    @BindByTag("titleText")
    TextView titleText;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_about;
    }


    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("关于我们");
        if(EamApplication.isHongshi()) {
            aboutIv.setImageResource(R.drawable.ic_app_launcher_hongshi);
        }
        else{
            aboutIv.setImageResource(R.drawable.ic_app_launcher_hailuo);
        }
        aboutName.setText(ChannelUtil.getAppName());
        StringBuilder versionName = new StringBuilder();
        versionName.append(" V");
        versionName.append(BuildConfig.VERSION_NAME);
        versionName.append(BuildConfig.DEBUG ? "(debug)" : "");

        buildVersion.setText(versionName.toString());
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        back();
                    }
                });

    }
}
