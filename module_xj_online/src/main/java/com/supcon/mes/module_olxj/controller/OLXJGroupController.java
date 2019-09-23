package com.supcon.mes.module_olxj.controller;

import android.view.View;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.mes.module_olxj.presenter.OLXJGroupPresenter;

/**
 * Created by wangshizhan on 2019/3/29
 * Email:wangshizhan@supcom.com
 */
@Presenter(OLXJGroupPresenter.class)
public class OLXJGroupController extends BaseViewController {




    public OLXJGroupController(View rootView) {
        super(rootView);
    }

    @Override
    public void initData() {
        super.initData();



    }
}
