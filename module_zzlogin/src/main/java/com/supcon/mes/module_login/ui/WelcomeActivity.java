package com.supcon.mes.module_login.ui;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;

import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_login.IntentRouter;
import com.supcon.mes.module_login.R;
import com.supcon.mes.module_login.service.HeartBeatService;
import com.supcon.mes.sb2.config.SB2Config;
import com.supcon.mes.sb2.config.SB2Constant;

/**
 * Created by wangshizhan on 2017/8/11.
 */

public class WelcomeActivity extends Activity {
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (EamApplication.isHongshi()) {
            getWindow().getDecorView().setBackgroundResource(R.drawable.layer_welcome_hs);
        } else if(EamApplication.isHailuo()){
            getWindow().getDecorView().setBackgroundResource(R.drawable.layer_welcome_hl);
        }else if(EamApplication.isYNSW()){
            getWindow().getDecorView().setBackgroundResource(R.drawable.layer_welcome_ynsw);
        }
        else{
            getWindow().getDecorView().setBackgroundResource(R.drawable.layer_welcome);
        }

        StatusBarUtils.setWindowStatusBarColor(this, R.color.white);

        new Handler().postDelayed(() -> {
            if (MBapApp.isIsLogin()) {
                
                Bundle bundle = new Bundle();
                if(Build.MODEL.contains("T50")
                        || Build.MODEL.contains("T55")
                        || Build.MODEL.contains("T60")
                        || Build.MODEL.contains("SD60")
                        || Build.MODEL.contains("SD55")){
                    bundle.putInt(SB2Constant.IntentKey.SB2_CONFIG_CODE, SB2Config.BARCORD | SB2Config.TEMPERATURE | SB2Config.UHF);
                }
                
                if (EamApplication.isHongshi()) {
                    IntentRouter.go(WelcomeActivity.this, Constant.Router.MAIN_REDLION, bundle);
                } else {
                    IntentRouter.go(WelcomeActivity.this, Constant.Router.MAIN_REDLION, bundle);
                }
                //在线模式使用心跳防止session过期
                HeartBeatService.startLoginLoop(this);
            } else {
                Bundle bundle = new Bundle();
                if (EamApplication.isHailuo()) {
                    bundle.putInt(Constant.IntentKey.LOGIN_BG_ID, R.drawable.bg_login_hl);
                    bundle.putInt(Constant.IntentKey.LOGIN_LOGO_ID, R.drawable.ic_login_logo_hl);
                }
                else if(EamApplication.isYNSW()){
                    bundle.putInt(Constant.IntentKey.LOGIN_BG_ID, R.drawable.bg_login_ynsw);
                    bundle.putInt(Constant.IntentKey.LOGIN_LOGO_ID, R.drawable.ic_login_logo);
                }
                else if(EamApplication.isHongshi()){
                    bundle.putInt(Constant.IntentKey.LOGIN_BG_ID, R.drawable.bg_login_hs);
                    bundle.putInt(Constant.IntentKey.LOGIN_LOGO_ID, R.drawable.ic_login_logo_hs);
                }

                bundle.putBoolean(Constant.IntentKey.FIRST_LOGIN, true);
                IntentRouter.go(WelcomeActivity.this, Constant.Router.LOGIN, bundle);
            }
            
            finish();
        }, 100);
    }
    
    
}
