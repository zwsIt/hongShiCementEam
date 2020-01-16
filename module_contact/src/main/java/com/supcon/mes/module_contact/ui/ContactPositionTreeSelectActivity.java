package com.supcon.mes.module_contact.ui;

import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BasePresenterActivity;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_contact.R;

/**
 * @Author xushiyun
 * @Create-time 7/22/19
 * @Pageage com.supcon.mes.middleware.ui
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
@Router(value = Constant.Router.CONTACT_POSITION_TREE_SELECT)
public class ContactPositionTreeSelectActivity extends BasePresenterActivity
{
    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_position_depart_tree_select;
    }
}
