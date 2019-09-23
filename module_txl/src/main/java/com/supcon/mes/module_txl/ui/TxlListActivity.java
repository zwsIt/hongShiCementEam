package com.supcon.mes.module_txl.ui;

import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BasePresenterActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_txl.R;

/**
 * 通讯录列表功能开发
 */
@Router(value = Constant.Router.TXL_LIST)
public class TxlListActivity extends BasePresenterActivity {
    @Override
    protected int getLayoutID() {
        return R.layout.ac_txl_list;
    }
    
    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
    }
}
