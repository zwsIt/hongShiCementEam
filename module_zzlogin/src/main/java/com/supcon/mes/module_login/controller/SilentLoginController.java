package com.supcon.mes.module_login.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_login.model.api.SilentLoginAPI;
import com.supcon.mes.module_login.model.bean.LoginEntity;
import com.supcon.mes.module_login.model.contract.SilentLoginContract;
import com.supcon.mes.module_login.presenter.SilentLoginPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wangshizhan on 2018/9/6
 * Email:wangshizhan@supcom.com
 */
@Presenter(SilentLoginPresenter.class)
public class SilentLoginController extends BasePresenterController implements SilentLoginContract.View, SilentLoginAPI {

    private boolean isLogining = false;

    @SuppressLint("CheckResult")
    @Override
    public void dologinSuccess(LoginEntity entity) {
        LogUtil.d("后台登陆成功！");
        Flowable.timer(650, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> {
                    EventBus.getDefault().post(new LoginEvent());
                    isLogining = false;
                });
    }

    @Override
    public void dologinFailed(String errorMsg) {
        LogUtil.e("后台登陆错误：" + errorMsg);
        Toast.makeText(EamApplication.getAppContext(), "后台登陆错误：" + errorMsg, Toast.LENGTH_SHORT).show();
        isLogining = false;
    }

    @Override
    public void dologinWithSuposPWSuccess(LoginEntity entity) {

    }

    @Override
    public void dologinWithSuposPWFailed(String errorMsg) {

    }


    @Override
    public void dologin(String username, String pwd) {
        presenterRouter.create(SilentLoginAPI.class).dologin(username, pwd);
        isLogining = true;
    }

    @Override
    public void dologinWithSuposPW(String username, String pwd) {
        presenterRouter.create(SilentLoginAPI.class).dologinWithSuposPW(username, pwd);
        isLogining = true;
    }

    public void silentLogin(Context context) {
        if (isLogining) {
            return;
        }
        boolean hasSupOS = SharedPreferencesUtils.getParam(context, Constant.SPKey.HAS_SUPOS, true);
        if (hasSupOS) {
            dologinWithSuposPW(EamApplication.getUserName(), EamApplication.getPassword());
        } else {
            dologin(EamApplication.getUserName(), EamApplication.getPassword());
        }
    }
}
