package com.supcon.mes.module_data_manage.alone;

import com.supcon.common.view.base.activity.BaseActivity;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_data_manage.IntentRouter;
import com.supcon.mes.module_data_manage.R;

/**
 * Created by wangshizhan on 2018/4/28.
 * Email:wangshizhan@supcon.com
 */

public class AloneActivity extends BaseActivity {


    @Override
    protected int getLayoutID() {
        return R.layout.ac_data_manager_alone;
    }


    @Override
    protected void initListener() {
        super.initListener();

        findViewById(R.id.xzBtn).setOnClickListener(v ->
            IntentRouter.go(context, Constant.Router.SJXZ));
        findViewById(R.id.scBtn).setOnClickListener(v -> IntentRouter.go(context, Constant.Router.SJXZ));
    }
}
