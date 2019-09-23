package com.supcon.mes.module_login.alone;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_login.IntentRouter;
import com.supcon.mes.module_login.R;

/**
 * Created by wangshizhan on 2018/4/28.
 * Email:wangshizhan@supcon.com
 */

public class LoginAloneActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(() -> {

            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.FIRST_LOGIN, true);
            IntentRouter.go(LoginAloneActivity.this, Constant.Router.LOGIN, bundle);
            finish();
        }, 500);
    }
}
