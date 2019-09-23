package com.supcon.mes.module_login.controller;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.module_login.model.api.PendingNumAPI;
import com.supcon.mes.module_login.model.bean.PendingNumEntity;
import com.supcon.mes.module_login.model.contract.PendingNumContract;
import com.supcon.mes.module_login.presenter.PendingNumPresenter;

/**
 * Created by wangshizhan on 2018/12/7
 * Email:wangshizhan@supcom.com
 */
@Presenter(PendingNumPresenter.class)
public class PendingNumController extends BaseViewController implements PendingNumContract.View{

    @BindByTag("rightBtn")
    ImageButton rightBtn;

    @BindByTag("pendingNum")
    TextView pendingNum;

    public PendingNumController(View rootView) {
        super(rootView);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenterRouter.create(PendingNumAPI.class).queryPendingNum(EamApplication.getAccountInfo().userId);
    }


    @Override
    public void queryPendingNumSuccess(PendingNumEntity entity) {
        if(entity.pendingCount!=0){
            pendingNum.setText(""+entity.pendingCount);
            pendingNum.setVisibility(View.VISIBLE);
        }
        else{
            pendingNum.setVisibility(View.GONE);
        }
    }

    @Override
    public void queryPendingNumFailed(String errorMsg) {
        LogUtil.e(""+errorMsg);
        pendingNum.setVisibility(View.GONE);
    }
}
