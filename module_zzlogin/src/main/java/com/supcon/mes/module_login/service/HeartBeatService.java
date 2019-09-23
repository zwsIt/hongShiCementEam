package com.supcon.mes.module_login.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.module_login.controller.SilentLoginController;
import com.supcon.mes.module_login.model.contract.HeartBeatContract;
import com.supcon.mes.module_login.presenter.AutoLoginPresenter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wangshizhan on 2017/12/18.
 * Email:wangshizhan@supcon.com
 */

public class HeartBeatService extends IntentService implements HeartBeatContract.View{

    private static final String LOGIN_LOOP_START = "LOGIN_LOOP_START";
    private static final String LOGIN_LOOP_STOP = "LOGIN_LOOP_STOP";
    private static final String LOGIN_LOOP_RESET = "LOGIN_LOOP_RESET";

    private AutoLoginPresenter mAutoLoginPresenter;
    Disposable timer;
    private SilentLoginController mSilentLoginController;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public HeartBeatService() {
        super("HeartBeatService");
        mAutoLoginPresenter = new AutoLoginPresenter();
        mAutoLoginPresenter.attachView(this);
    }

    public static void startLoginLoop(Context context){
        LogUtil.i("startLoginLoop");
        Intent intent = new Intent(context, HeartBeatService.class);
        intent.setAction(LOGIN_LOOP_START);
        context.startService(intent);
    }

    public static void stopLoginLoop(Context context){
        LogUtil.i("stopLoginLoop");
        Intent intent = new Intent(context, HeartBeatService.class);
        intent.setAction(LOGIN_LOOP_STOP);
        context.startService(intent);
    }

    public static void resetLoginLoop(Context context){
        Intent intent = new Intent(context, HeartBeatService.class);
        intent.setAction(LOGIN_LOOP_RESET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        handleAction(action);
    }

    private void handleAction(String action) {

        switch (action){

            case LOGIN_LOOP_START:
                startTimer();
                break;

            case LOGIN_LOOP_RESET:
                resetTimer();
                break;


            case LOGIN_LOOP_STOP:
                stopTimer();
                break;


        }

    }

    public void startLoginLoop(){

        startTimer();
    }

    public void stopLoginLoop(){
        stopTimer();
    }

    public void resetLoginLoop(){

        resetTimer();
    }


    private void startTimer() {
//        LogUtil.i("HeartBeatService startTimer");
        timer = Flowable.timer(30, TimeUnit.SECONDS)
                .subscribe(aLong ->
                    mAutoLoginPresenter.heartBeat());
    }

    private void stopTimer() {
//        LogUtil.i("HeartBeatService stopTimer");
        if(timer!=null){
            timer.dispose();
            timer = null;
        }
    }

    private void resetTimer(){
//        LogUtil.i("HeartBeatService resetTimer");
        stopTimer();
        startTimer();
    }


    @Override
    public void heartBeatSuccess() {
        LogUtil.w("heartBeatSuccess");
        resetTimer();
    }

    @Override
    public void heartBeatFailed(String error) {
        LogUtil.w(error);
        if(error.contains("401")){
            if(mSilentLoginController == null){
                mSilentLoginController = new SilentLoginController();
            }
            mSilentLoginController.silentLogin(this);
        }
        resetTimer();
    }
}
