package com.supcon.mes.module_txl.ui;

import android.os.Bundle;

import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BasePresenterActivity;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_txl.R;

/**
 * @Author xushiyun
 * @Create-time 8/2/19
 * @Pageage com.supcon.mes.module_txl.ui
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc 带悬浮首字母标题界面开发工作
 */
@Router(Constant.Router.TXL_SEARCH_CONTACT_WITH_HEADER)
public class TxlSearchContactWithHeaderActivity extends BasePresenterActivity {
    @Override
    protected void onInit() {
        super.onInit();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.IntentKey.TITLE_CONTENT, getIntent().getStringExtra(Constant.IntentKey.TITLE_CONTENT));
        getSupportFragmentManager().findFragmentById(R.id.txlSearchContactWithHeaderActivity).setArguments(bundle);
    }
    
    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
    }
    
    @Override
    protected int getLayoutID() {
        return R.layout.ac_txl_search_contact_with_header;
    }
}
