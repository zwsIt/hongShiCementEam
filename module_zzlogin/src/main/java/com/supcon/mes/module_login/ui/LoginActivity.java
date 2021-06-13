package com.supcon.mes.module_login.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseControllerActivity;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.picker.SinglePicker;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.MBapConstant;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.PatternUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.constant.DataModule;
import com.supcon.mes.middleware.controller.CompanyController;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.Company;
import com.supcon.mes.middleware.model.bean.ModuleAuthorization;
import com.supcon.mes.middleware.model.bean.ModuleAuthorizationListEntity;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.ProcessKeyUtil;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_login.BuildConfig;
import com.supcon.mes.module_login.IntentRouter;
import com.supcon.mes.module_login.R;
import com.supcon.mes.module_login.model.api.LoginAPI;
import com.supcon.mes.module_login.model.api.MineAPI;
import com.supcon.mes.module_login.model.api.ZhiZhiUrlQueryAPI;
import com.supcon.mes.module_login.model.bean.LoginEntity;
import com.supcon.mes.module_login.model.contract.LoginContract;
import com.supcon.mes.module_login.model.contract.MineContract;
import com.supcon.mes.module_login.model.contract.ZhiZhiUrlQueryContract;
import com.supcon.mes.module_login.presenter.LoginPresenter;
import com.supcon.mes.module_login.presenter.MinePresenter;
import com.supcon.mes.module_login.presenter.ZhiZhiUrlQueryPresenter;
import com.supcon.mes.module_login.service.HeartBeatService;
import com.supcon.mes.push.controller.DeviceTokenController;
//import com.yanzhenjie.permission.Action;
//import com.yanzhenjie.permission.AndPermission;
//import com.yanzhenjie.permission.runtime.Permission;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wangshizhan on 2017/8/11.
 */

@Router(Constant.Router.LOGIN)
@Presenter(value = {LoginPresenter.class, ZhiZhiUrlQueryPresenter.class, MinePresenter.class})
@Controller(value = {CompanyController.class, DeviceTokenController.class})
public class LoginActivity extends BaseControllerActivity implements LoginContract.View, ZhiZhiUrlQueryContract.View, MineContract.View {
    private static final int PERMISSION_READ_EXTERNAL_STORAGE = 0;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 1;
    private static final int PERMISSION_REQUEST_CODE = 99;
    @BindByTag("companySpinner")
    CustomSpinner companySpinner;

    @BindByTag("usernameInput")
    CustomEditText usernameInput;

    @BindByTag("pwdInput")
    CustomEditText pwdInput;

    @BindByTag("loginLogo")
    ImageView loginLogo;

    @BindByTag("loginBg")
    RelativeLayout loginBg;

    @BindByTag("buildVersion")
    CustomTextView buildVersion;
    @BindByTag("loginBtn")
    Button loginBtn;

    private boolean isFirstIn = false;
    private boolean loginInvalid;

    private int loginLogoId = 0;
    private int loginBgId = 0;

    private ImageView spinnerIv; // 下拉选择公司
    private Long mCompanyId;
    private Company mCompany;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_login_multi_org;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //无title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onInit() {
        super.onInit();
//        EventBus.getDefault().register(this);
        loginInvalid = getIntent().getBooleanExtra(Constant.IntentKey.LOGIN_INVALID, false);
        isFirstIn = getIntent().getBooleanExtra(Constant.IntentKey.FIRST_LOGIN, false);
        loginLogoId = getIntent().getIntExtra(Constant.IntentKey.LOGIN_LOGO_ID, 0);
        loginBgId = getIntent().getIntExtra(Constant.IntentKey.LOGIN_BG_ID, 0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    SinglePickController mSinglePickController = new SinglePickController<String>(this);
    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        companySpinner.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {

                //加载本地数据库：company
                List<Company> companyList = EamApplication.dao().getCompanyDao().loadAll();

                if (companyList.size() <= 0){
                    ToastUtils.show(context,"请先同步公司信息数据");
                    return;
                }

                List<String> list = new ArrayList<>();
                for (Company company : companyList){
                    list.add(company.name);
                }

                mSinglePickController.textSize(20);
                mSinglePickController.list(list)
                        .listener(new SinglePicker.OnItemPickListener() {
                            @Override
                            public void onItemPicked(int index, Object item) {
                                companySpinner.setContent(item.toString());
                                companySpinner.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
                                // 获取companyId
                                mCompany = companyList.get(index);
                                mCompanyId = mCompany.id;
                            }
                        }).show(companySpinner.getContent());
            }
        });

        RxView.clicks(loginBtn)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(o -> {
                    //如果返回结果为false说明存在格式上的错误
                    if (!checkInput()) {
                        return;
                    }

                    if (TextUtils.isEmpty(SharedPreferencesUtils.getParam(context, MBapConstant.SPKey.IP, ""))) {
                        SnackbarHelper.showError(rootView, "必须设置服务器IP地址!");
                        return;
                    }

                    onLoading("正在登陆...");
//                    presenterRouter.create(ZhiZhiUrlQueryAPI.class).getZhizhiUrl();


                    boolean hasSupOS = SharedPreferencesUtils.getParam(context, Constant.SPKey.HAS_SUPOS, BuildConfig.HAS_SUPOS);
                    if (TextUtils.isEmpty(EamApplication.getAccountInfo().userName)){
                        EamApplication.getAccountInfo().userName = usernameInput.getContent().trim();
                    }

                    if (hasSupOS) {
                        presenterRouter.create(LoginAPI.class).dologinWithSuposPW(usernameInput.getContent().trim(), pwdInput.getContent().trim());
                    } else {
                        presenterRouter.create(LoginAPI.class).dologin(usernameInput.getContent().trim(), pwdInput.getContent().trim(),mCompanyId);
                    }
                });


        RxView.clicks(findViewById(R.id.loginSettingLayout))
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(o -> IntentRouter.go(this, Constant.Router.SETTING)
                );
    }

    @Override
    protected void initView() {
        super.initView();
        setSwipeBackEnable(false);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.transparent);

        usernameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        pwdInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pwdInput.editText().setTransformationMethod(PasswordTransformationMethod.getInstance());
        usernameInput.setInput(MBapApp.getUserName());
        pwdInput.setInput(MBapApp.getPassword());
        companySpinner.setContent(SharedPreferencesUtils.getParam(context,Constant.SPKey.C_NAME,""));
        mCompanyId = SharedPreferencesUtils.getParam(context,Constant.SPKey.C_ID,-1L);

        if (loginLogoId != 0)
            loginLogo.setImageResource(loginLogoId);

        if (loginBgId != 0) {
            loginBg.setBackgroundResource(loginBgId);
        }

        StringBuilder versionName = new StringBuilder();
        versionName.append(" V");
        versionName.append(BuildConfig.VERSION_NAME);
        versionName.append(BuildConfig.DEBUG ? "(debug)" : "");

        buildVersion.setValue(versionName.toString());

        if (EamApplication.isHongshi()) {
            loginBtn.setBackgroundResource(R.drawable.bg_btn_hongshi_login2);
        } else {
            loginBtn.setBackgroundResource(R.drawable.bg_btn_login2);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (loginInvalid)
            finish();
        else {
            finish();

            EamApplication.exitApp();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (loginInvalid) {
            ToastUtils.show(context, "登陆已失效，请重新登陆！");
        }

        HeartBeatService.stopLoginLoop(this);

        if (SharedPreferencesUtils.getParam(context, Constant.SPKey.HAS_SUPOS, BuildConfig.HAS_SUPOS)) {
            pwdInput.setHint("输入SupOS密码");
        } else {
            pwdInput.setHint("输入密码");
        }

    }

    @SuppressWarnings("all")
    private boolean checkUsername() {

        return TextUtils.isEmpty(usernameInput.getInput().trim());

    }

    @SuppressWarnings("all")
    private boolean checkPwd() {

        return TextUtils.isEmpty(pwdInput.getInput().trim());

    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(companySpinner.getContent().trim())){
            ToastUtils.show(context,"请选择公司登陆");
            return false;
        }
        if (TextUtils.isEmpty(usernameInput.getInput().trim())) {
            ToastUtils.show(context, "用户名不允许为空!");
            return false;
        }
        if (!PatternUtil.checkUserName(usernameInput.getInput().trim()/*,"^[A-Za-z0-9\\u4e00-\\u9fa5]+$"*/)) {
            ToastUtils.show(context, "用户名/密码不合法！");
            return false;
        }
        //以上只单纯检测纯格式方面的错误
        if (TextUtils.isEmpty(pwdInput.getInput().trim())) {
            ToastUtils.show(context, "密码不允许为空!");
            return false;
        }
        //密码方面并没有特别的限制,所以这里并不特备进行验证,只进行是否为空的验证

        return true;
    }

    @Override
    public void dologinSuccess(LoginEntity userEntity) {

        dologinResult(userEntity);

    }

    private void dologinResult(LoginEntity userEntity) {
        //如果登陆成功, 取出用户必备信息
        if (!loginInvalid) {
            //在线模式使用心跳防止session过期
            HeartBeatService.startLoginLoop(this);

            onLoading("正在检查授权...");
            StringBuilder sb = new StringBuilder("");
            sb.append(Constant.ModuleAuthorization.mobileEAM).append(",").append(Constant.ModuleAuthorization.BEAM2);
            presenterRouter.create(LoginAPI.class).getLicenseInfo(sb.toString());

        } else {
            //跳转到主页

            loaderController.showMsgAndclose("登陆成功！", true, 200, () -> {
                LoginActivity.this.finish();
                Flowable.timer(650, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(v -> {
                            EventBus.getDefault().post(new LoginEvent());
                        });
            });
        }
    }

    @Override
    public void dologinFailed(String errMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errMsg));

    }

    @Override
    public void dologinWithTokenSuccess(LoginEntity entity) {
        dologinSuccess(entity);
    }

    @Override
    public void dologinWithTokenFailed(String errorMsg) {
        dologinFailed(ErrorMsgHelper.msgParse(errorMsg));

    }

    @Override
    public void dologinWithSuposPWSuccess(LoginEntity entity) {
        dologinResult(entity);
    }

    @Override
    public void dologinWithSuposPWFailed(String errorMsg) {
        dologinFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    private void doLogout() {
        presenterRouter.create(MineAPI.class).logout();
    }

    @Override
    public void getLicenseInfoSuccess(ModuleAuthorizationListEntity entity) {

        //将授权信息放入缓存
        for (ModuleAuthorization moduleAuthorization : entity.result) {

            switch (moduleAuthorization.moduleCode) {
                case Constant.ModuleAuthorization.mobileEAM:
                    SharedPreferencesUtils.setParam(context, Constant.ModuleAuthorization.mobileEAM, true/*moduleAuthorization.isAuthorized*/);
                    break;
                case Constant.ModuleAuthorization.BEAM2:
                    SharedPreferencesUtils.setParam(context, Constant.ModuleAuthorization.BEAM2, true/*moduleAuthorization.isAuthorized*/);
                    break;
                default:
                    break;
            }
        }

        //存入数据库
        EamApplication.dao().getModuleAuthorizationDao().insertOrReplaceInTx(entity.result);

        onLoading("正在获取用户信息...");
        presenterRouter.create(LoginAPI.class).getAccountInfo(usernameInput.getInput());

    }

    @Override
    public void getLicenseInfoFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
        doLogout();
    }

    @Override
    public void getAccountInfoSuccess() {
//        downloadBase();
        //跳转到主页
        goMain();
    }

    @Override
    public void getAccountInfoFailed(String errorMsg) {
        if (errorMsg != null && errorMsg.contains("403")) {
            errorMsg = "获取用户信息失败，请检查用户查看权限！";
        }

        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
        doLogout();
    }


    private void goMain() {
        MBapApp.setIsLogin(true);
        MBapApp.setUserName(usernameInput.getInput().trim());
        MBapApp.setPassword(pwdInput.getInput().trim());
//        SharedPreferencesUtils.setParam(context,Constant.SPKey.COMPANY,mCompany == null ? "" : mCompany.toString());

        if (mCompany == null){
            mCompany = EamApplication.getCompany();
        }
        EamApplication.setCompanyCode(mCompany);
        ProcessKeyUtil.updateProcessKey();

        SharedPreferencesUtils.setParam(context,Constant.SPKey.C_NAME,companySpinner.getContent());
        SharedPreferencesUtils.setParam(context,Constant.SPKey.C_ID,mCompanyId);

        onLoadSuccessAndExit("登陆成功！", () -> {

            //跳转到主页
//            if (isFirstIn) {
            IntentRouter.go(context, Constant.Router.MAIN_REDLION);
            isFirstIn = false;
//            }
            LoginActivity.this.finish();
        });

    }

    private void downloadBase() {
        List<String> downloadModules = new ArrayList<>();
//        downloadModules.add(DataModule.EAM_BASE.getModuelName());
//        downloadModules.add(DataModule.XJ_BASE.getModuelName());
        downloadModules.add(DataModule.EAM_DEVICE.getModuelName());
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(Constant.IntentKey.DOWNLOAD_MODULES, (ArrayList<String>) downloadModules);
        bundle.putBoolean(Constant.IntentKey.DOWNLOAD_VISIBLE, false);
        IntentRouter.go(context, Constant.Router.SJXZ, bundle);
    }

    @Override
    public void getZhizhiUrlSuccess(BapResultEntity entity) {
        if (!TextUtils.isEmpty(entity.zhizhiUrl)) {
            EamApplication.setZzUrl(entity.zhizhiUrl);

        }
    }

    @Override
    public void getZhizhiUrlFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void logoutSuccess() {
        LogUtil.d("登出成功");
    }

    @Override
    public void logoutFailed(String errorMsg) {
        LogUtil.d("登出失败");
    }

}
