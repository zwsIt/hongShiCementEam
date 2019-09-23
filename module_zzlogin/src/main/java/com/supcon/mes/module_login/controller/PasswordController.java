package com.supcon.mes.module_login.controller;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.custom.ICustomView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.module_login.R;
import com.supcon.mes.module_login.model.api.PasswordAPI;
import com.supcon.mes.module_login.model.contract.PasswordContract;
import com.supcon.mes.module_login.presenter.PasswordPresenter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2019/8/6
 * Email:wangshizhan@supcom.com
 */
@Presenter(PasswordPresenter.class)
public class PasswordController extends BaseViewController implements PasswordContract.View {


    @BindByTag("usernameInput")
    ICustomView usernameInput;

    @BindByTag("oldPwdInput")
    ICustomView oldPwdInput;

    @BindByTag("newPwdInput")
    ICustomView newPwdInput;

    @BindByTag("newPwdConfirmInput")
    ICustomView newPwdConfirmInput;

    @BindByTag("cancelBtn")
    Button cancelBtn;

    @BindByTag("sureBtn")
    Button sureBtn;

    private Dialog mDialog;
    private boolean isOldPwdRight = false, isConfirmed = false;
    private String newPwd ;

    public static View getView(Context context){

        return LayoutInflater.from(context).inflate(R.layout.ly_password_modify_dialog, null);

    }

    public PasswordController(View rootView, boolean isDialog){
        this(rootView);
        if(isDialog){
            if(mDialog == null){
                mDialog = new Dialog(context);
            }
            mDialog.setCancelable(true);
            mDialog.setCanceledOnTouchOutside(true);
//            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setContentView(rootView);
        }

    }


    public PasswordController(View rootView) {
        super(rootView);
    }

    public void init(){
        onInit();
        initView();
        initListener();
        initData();
    }

    @Override
    public void onInit() {
        super.onInit();
    }

    @Override
    public void initView() {
        super.initView();
        usernameInput.setContent(EamApplication.getUserName());
        oldPwdInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPwdInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPwdConfirmInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        oldPwdInput.editText().setTransformationMethod(PasswordTransformationMethod.getInstance());
        newPwdInput.editText().setTransformationMethod(PasswordTransformationMethod.getInstance());
        newPwdConfirmInput.editText().setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    @SuppressLint("CheckResult")
    @Override
    public void initListener() {
        super.initListener();


        RxTextView.textChanges(oldPwdInput.editText())
                .skipInitialValue()
                .debounce(20, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        if(!TextUtils.isEmpty(charSequence)){
                            presenterRouter.create(PasswordAPI.class).checkPwd(charSequence.toString());
                        }
                    }
                });


        RxTextView.textChanges(newPwdInput.editText())
                .skipInitialValue()
                .debounce(20, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        if(!TextUtils.isEmpty(charSequence)){
                            newPwd = charSequence.toString();
                        }
                        else{
                            newPwd = "";
                        }
                    }
                });

        RxTextView.textChanges(newPwdConfirmInput.editText())
                .skipInitialValue()
                .debounce(20, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        if(!TextUtils.isEmpty(charSequence) && newPwd.equals(charSequence.toString())){
                            isConfirmed = true;
                        }
                        else{
                            isConfirmed = false;
                        }
                    }
                });

        RxView.clicks(cancelBtn)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        close();
                    }
                });

        RxView.clicks(sureBtn)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                        if(checkBeforeSubmit(new HashMap<>())) {
                            presenterRouter.create(PasswordAPI.class).saveNewPwd(oldPwdInput.getContent(), newPwdInput.getContent());
                        }
                    }
                });
    }

    public void open(){
        if(mDialog!=null){
            mDialog.show();
        }
    }

    public void close(){
        if(mDialog!=null){
            mDialog.hide();
        }

        oldPwdInput.setContent("");
        newPwdInput.setContent("");
        newPwdConfirmInput.setContent("");
        isOldPwdRight = false;
        isConfirmed = false;
    }


    @Override
    public boolean checkBeforeSubmit(Map<String, Object> map) {

        if(TextUtils.isEmpty(oldPwdInput.getContent())){
            ToastUtils.show(context, "请输入原密码！");
            return false;
        }

        if(!isOldPwdRight){
            ToastUtils.show(context, "原密码不正确，请检查！");
            return false;
        }

        if(TextUtils.isEmpty(newPwdInput.getContent())){
            ToastUtils.show(context, "请输入新密码！");
            return false;
        }

        if(TextUtils.isEmpty(newPwdConfirmInput.getContent())){
            ToastUtils.show(context, "请确认新密码！");
            return false;
        }

        if(!isConfirmed){
            ToastUtils.show(context, "新密码两次输入不一致，请检查！");
            return false;
        }

        return super.checkBeforeSubmit(map);
    }



    @Override
    public void checkPwdSuccess(BapResultEntity entity) {
        LogUtil.d("checkPwdSuccess");
        isOldPwdRight = true;
    }

    @Override
    public void checkPwdFailed(String errorMsg) {
        LogUtil.e("checkPwdFailed");
        isOldPwdRight = false;
//        ToastUtils.show(context, "原密码不正确！");
    }

    @Override
    public void saveNewPwdSuccess(BapResultEntity entity) {
        close();
        ToastUtils.show(context, "密码修改成功！");
    }

    @Override
    public void saveNewPwdFailed(String errorMsg) {
        ToastUtils.show(context, ""+errorMsg);
    }
}
