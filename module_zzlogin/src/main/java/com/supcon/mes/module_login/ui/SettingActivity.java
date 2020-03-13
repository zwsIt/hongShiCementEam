package com.supcon.mes.module_login.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BasePresenterActivity;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.SwitchButton;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.MBapConstant;
import com.supcon.mes.mbap.network.Api;
import com.supcon.mes.mbap.utils.KeyHelper;
import com.supcon.mes.mbap.utils.PatternUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomArrowView;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.CompanyQueryAPI;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.contract.CompanyQueryContract;
import com.supcon.mes.middleware.presenter.CompanyQueryPresenter;
import com.supcon.mes.middleware.util.DeviceManager;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_login.BuildConfig;
import com.supcon.mes.module_login.IntentRouter;
import com.supcon.mes.module_login.R;
import com.supcon.mes.module_login.controller.PasswordController;
import com.supcon.mes.module_login.model.api.MineAPI;
import com.supcon.mes.module_login.model.contract.MineContract;
import com.supcon.mes.module_login.presenter.MinePresenter;
import com.supcon.mes.module_login.util.EditTextHelper;

import org.checkerframework.checker.signature.qual.BinaryNameInUnnamedPackage;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

import static com.supcon.mes.mbap.utils.PatternUtil.URL_PATTERN;

/**
 * Created by wangshizhan on 2017/8/16.
 */
@Router(Constant.Router.SETTING)
@Presenter(value = {MinePresenter.class, CompanyQueryPresenter.class})
public class SettingActivity extends BasePresenterActivity implements MineContract.View, CompanyQueryContract.View {
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("ipInput")
    CustomEditText ipInput;
    @BindByTag("urlInput")
    CustomEditText urlInput;
    @BindByTag("portInput")
    CustomEditText portInput;
    @BindByTag("urlSwitchBtn")
    SwitchButton urlSwitchBtn;
    @BindByTag("SupOSSwitchBtn")
    SwitchButton SupOSSwitchBtn;
    @BindByTag("offlineSwitchBtn")
    SwitchButton offlineSwitchBtn;

    @BindByTag("pwdSettings")
    CustomArrowView pwdSettings;
    @BindByTag("tvSizeChangeSettings")
    CustomArrowView tvSizeChangeSettings;
    @BindByTag("companySyncTv")
    TextView companySyncTv;
    @BindByTag("companySyncRl")
    RelativeLayout companySyncRl;


    private PasswordController mPasswordController;


    private StringBuilder originalParam = new StringBuilder();

    private boolean isUrlEnabled = false;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_setting;
    }

    @Override
    protected void onInit() {
        super.onInit();
        isUrlEnabled = SharedPreferencesUtils.getParam(this, MBapConstant.SPKey.URL_ENABLE, false);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("设置");
        rightBtn.setImageResource(R.drawable.sl_top_submit);
        rightBtn.setVisibility(View.VISIBLE);

        ipInput.setInputType(InputType.TYPE_CLASS_PHONE);
        ipInput.setInput(SharedPreferencesUtils.getParam(this, MBapConstant.SPKey.IP, ""));

        portInput.setInputType(InputType.TYPE_CLASS_PHONE);
        portInput.setInput(SharedPreferencesUtils.getParam(this, MBapConstant.SPKey.PORT, ""));

        urlSwitchBtn.setChecked(SharedPreferencesUtils.getParam(this, MBapConstant.SPKey.URL_ENABLE, false));


        EditTextHelper.setEditTextInhibitInputSpace(ipInput.editText());
        EditTextHelper.setEditTextInhibitInputSpace(portInput.editText());
        EditTextHelper.setEditTextInhibitInputSpace(urlInput.editText());

        EditTextHelper.setEditTextInhibitInputSpeChat(portInput.editText());

        initHost(isUrlEnabled);


        if (BuildConfig.HAS_SUPOS /*|| BuildConfig.DEBUG*/) {
            ((ViewGroup) SupOSSwitchBtn.getParent()).setVisibility(View.VISIBLE);
            SupOSSwitchBtn.setChecked(SharedPreferencesUtils.getParam(context, Constant.SPKey.HAS_SUPOS, BuildConfig.HAS_SUPOS));
        }

    }

    private void initHost(boolean isUrlEnabled) {
        if (isUrlEnabled) {
            urlInput.setVisibility(View.VISIBLE);
            ipInput.setVisibility(View.GONE);
            portInput.setVisibility(View.GONE);
            ipInput.setInput("");
            portInput.setInput("");

            urlInput.setInput(SharedPreferencesUtils.getParam(this, MBapConstant.SPKey.URL, ""));

        } else {
            urlInput.setVisibility(View.GONE);
            urlInput.setInput("");

            ipInput.setVisibility(View.VISIBLE);
            portInput.setVisibility(View.VISIBLE);

            ipInput.setInputType(InputType.TYPE_CLASS_PHONE);
            ipInput.setInput(SharedPreferencesUtils.getParam(this, MBapConstant.SPKey.IP, ""));

            portInput.setInputType(InputType.TYPE_CLASS_PHONE);
            portInput.setInput(SharedPreferencesUtils.getParam(this, MBapConstant.SPKey.PORT, ""));
        }
    }


    @Override
    protected void initData() {
        originalParam
                .append(ipInput.getInput())
                .append(portInput.getInput())
                .append(urlInput.getInput());

        if (EamApplication.isIsLogin()) {
            companySyncRl.setVisibility(View.GONE);
            pwdSettings.setVisibility(View.VISIBLE);
            tvSizeChangeSettings.setVisibility(View.VISIBLE);
        }

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        rightBtn.setOnClickListener(v -> {

            if (checkIsModified()) {
                if (doCheck()) {
                    doSave();
                }
            } else {
                finish();
                executeBackwardAnim();
            }

        });


        KeyHelper.doActionNext(ipInput.editText(), portInput.editText(), false);
        KeyHelper.doActionNext(portInput.editText(), true, () -> rightBtn.performClick());

        leftBtn.setOnClickListener(v -> onBackPressed());

        urlSwitchBtn.setOnCheckedChangeListener((switchButton, b) -> {

            isUrlEnabled = b;

            initHost(isUrlEnabled);

            SharedPreferencesUtils.setParam(context, MBapConstant.SPKey.URL_ENABLE, b);
        });

        pwdSettings.setOnClickListener(v -> showPwdDialog());
        tvSizeChangeSettings.setOnClickListener(v -> IntentRouter.go(context, Constant.Router.TEXT_SIZE_SETTING));

        SupOSSwitchBtn.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                SharedPreferencesUtils.setParam(context, Constant.SPKey.HAS_SUPOS, isChecked);
            }
        });

        RxView.clicks(companySyncTv).throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (doCheck()){
                            onLoading("公司数据同步中");
                            save();
                            presenterRouter.create(CompanyQueryAPI.class).listCompany();
                        }
                    }
                });
    }

    private void showPwdDialog() {

        if (!EamApplication.isIsLogin()) {
            ToastUtils.show(context, "请先登录！");
            return;
        }

        if (mPasswordController == null) {
            mPasswordController = new PasswordController(PasswordController.getView(context), true);
            mPasswordController.init();
        }

        mPasswordController.open();
    }

    @Override
    public void onBackPressed() {

/*        //验证页面是否已被修改
        if (checkIsModified()) {
            new CustomDialog(context)
                    .twoButtonAlertDialog("页面已经被修改，是否要保存?")
                    .bindView(R.id.redBtn, "保存")
                    .bindView(R.id.grayBtn, "离开")
                    .bindClickListener(R.id.redBtn, v1 -> doSave(), true)
                    .bindClickListener(R.id.grayBtn, v3-> super.onBackPressed(),true)
                    .show();
        } else {
            super.onBackPressed();
        }*/
        //验证页面是否已被修改
        if (checkIsModified()) {
            new CustomDialog(context)
                    .twoButtonAlertDialog("页面已经被修改，是否要保存?")
                    .bindView(R.id.redBtn, "保存")
                    .bindView(R.id.grayBtn, "离开")
                    .bindClickListener(R.id.redBtn, v1 -> {
                        if (doCheck()) {
                            doSave();
                        }
                    }, true)
                    .bindClickListener(R.id.grayBtn, v3 -> super.onBackPressed(), true)
                    .show();


        } else {
            super.onBackPressed();
        }
    }


/*
    private void doSaveUrl() {
        if(TextUtils.isEmpty(urlInput.getInput())){
            ToastUtils.show(this, "必须设置服务器地址！");
            return;
        }

        if(!PatternUtil.checkInput(urlInput.getInput(), URL_PATTERN)){
            ToastUtils.show(this, "请输入正确的服务器地址！");
            return;
        }

        MBapApp.setUrl(urlInput.getInput().trim());
        LogUtil.d("url:"+ MBapApp.getUrl());

        MBapApp.setIp("");
        MBapApp.setPort("");

        //使用模式  在线 / 离线
        SharedPreferencesUtils.setParam(this, MBapConstant.SPKey.OFFLINE_ENABLE, offlineSwitchBtn.isTagChecked());

        Api.getInstance().rebuild();

        finish();

    }
*/

/*    private void doSave() {
        if(TextUtils.isEmpty(ipInput.getInput())){
            ToastUtils.show(this, "必须设置服务器IP地址！");
            return;
        }

        if(!PatternUtil.checkIP(ipInput.getInput())){
            ToastUtils.show(this, "请输入正确的服务器IP！");
            return;
        }

        if(TextUtils.isEmpty(portInput.getInput())){
            ToastUtils.show(this, "必须设置端口！");
            return;
        }

        if(!PatternUtil.checkPort(portInput.getInput())){
            ToastUtils.show(this, "请输入正确的端口！");
            return;
        }

        MBapApp.setIp(ipInput.getInput().trim());
        LogUtil.d("ip:"+ MBapApp.getIp());

        MBapApp.setPort(portInput.getInput().trim());
        LogUtil.d("port:"+ MBapApp.getPort());

        MBapApp.setUrl("");

        //使用模式  在线 / 离线
        SharedPreferencesUtils.setParam(this, MBapConstant.SPKey.OFFLINE_ENABLE, offlineSwitchBtn.isTagChecked());

        Api.getInstance().rebuild();

        finish();

    }*/

    private void save() {

        if (isUrlEnabled) {
            MBapApp.setUrl(urlInput.getInput().trim());
            LogUtil.d("url:" + MBapApp.getUrl());

            MBapApp.setIp("");
            MBapApp.setPort("");
        } else {
            MBapApp.setIp(ipInput.getInput().trim());
            LogUtil.d("ip:" + MBapApp.getIp());

            MBapApp.setPort(portInput.getInput().trim());
            LogUtil.d("port:" + MBapApp.getPort());

            MBapApp.setUrl("");
        }


        Api.getInstance().rebuild();


    }

    private boolean doCheck() {

        if (!isUrlEnabled) {
            if (TextUtils.isEmpty(ipInput.getInput())) {
                ToastUtils.show(this, "请设置服务器IP地址");
                return false;
            }

            if (!PatternUtil.checkIP(ipInput.getInput())) {
                ToastUtils.show(this, "请输入正确的服务器IP地址");
                return false;
            }

            if (TextUtils.isEmpty(portInput.getInput())) {
                ToastUtils.show(this, "请设置端口");
                return false;
            }

            if (!PatternUtil.checkPort(portInput.getInput())) {
                ToastUtils.show(this, "请输入正确的端口");
                return false;
            }
        } else {
            if (TextUtils.isEmpty(urlInput.getInput())) {
                ToastUtils.show(this, "必须设置服务器地址！");
                return false;
            }

            if (!PatternUtil.checkInput(urlInput.getInput(), URL_PATTERN)) {
                ToastUtils.show(this, "请输入正确的服务器地址！");
                return false;
            }
        }


        return true;
    }

    private void doSave() {

        if (MBapApp.isIsLogin()) {
            new CustomDialog(context)
                    .twoButtonAlertDialog("网络设置已被修改，是否要重新登陆?")
                    .bindView(R.id.redBtn, "重新登陆")
                    .bindView(R.id.grayBtn, "取消")
                    .bindClickListener(R.id.redBtn, v1 -> {
                        onLoading("正在登出...");
                        presenterRouter.create(MineAPI.class).logout();
                    }, true)
                    .bindClickListener(R.id.grayBtn, null, true)
                    .show();
        } else {

            DeviceManager.getInstance().release();
            save();
            finish();
        }

    }

    /**
     * 验证页面是否已被修改
     */
    private boolean checkIsModified() {

        StringBuilder latestParam = new StringBuilder();
        latestParam.append(ipInput.getInput())
                .append(portInput.getInput())
                .append(urlInput.getInput());

        return !latestParam.toString().equals(originalParam.toString());

    }

    @Override
    public void logoutSuccess() {
//        LogUtil.w("logoutSuccess");
//        MBapApp.setIsLogin(false);
//        MBapApp.setPassword("");
//        //用户信息清空
//        EamApplication.setAccountInfo(null);
//        HeartBeatService.stopLoginLoop(context);
//        DeviceManager.getInstance().release();
        save();
        onLoadSuccessAndExit("登出成功！", () -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.FIRST_LOGIN, false);
            if (EamApplication.isHailuo()) {
                bundle.putInt(Constant.IntentKey.LOGIN_BG_ID, R.drawable.bg_login_hl);
                bundle.putInt(Constant.IntentKey.LOGIN_LOGO_ID, R.drawable.ic_login_logo_hl);
            } else if (EamApplication.isYNSW()) {
                bundle.putInt(Constant.IntentKey.LOGIN_BG_ID, R.drawable.bg_login_ynsw);
                bundle.putInt(Constant.IntentKey.LOGIN_LOGO_ID, R.drawable.ic_login_logo);
            } else if (EamApplication.isHongshi()) {
                bundle.putInt(Constant.IntentKey.LOGIN_BG_ID, R.drawable.bg_login_hs);
                bundle.putInt(Constant.IntentKey.LOGIN_LOGO_ID, R.drawable.ic_login_logo_hs);
            }
            IntentRouter.go(context, Constant.Router.LOGIN, bundle);
            finish();
        });
    }

    @Override
    public void logoutFailed(String errorMsg) {
        LogUtil.w("logoutFailed:" + errorMsg);
        onLoadFailed("登出失败！");
    }

    @Override
    public void listCompanySuccess(CommonListEntity entity) {
        //插入本地
        if (entity.result.size() > 0){
            EamApplication.dao().getCompanyDao().deleteAll();
            EamApplication.dao().getCompanyDao().insertOrReplaceInTx(entity.result);
        }
        onLoadSuccess("下载成功");
    }

    @Override
    public void listCompanyFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }
}
