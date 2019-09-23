package com.supcon.mes.middleware.ui;

import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BasePresenterActivity;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;

/**
 * @Author xushiyun
 * @Create-time 7/22/19
 * @Pageage com.supcon.mes.middleware.ui
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
@Router(value = Constant.Router.MULTI_DEPART_SELECT)
public class MultiDepartSelectActivity extends BasePresenterActivity
{
    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
    }
    
    @Override
    protected int getLayoutID() {
        return R.layout.ac_multi_depart_select;
    }
}
