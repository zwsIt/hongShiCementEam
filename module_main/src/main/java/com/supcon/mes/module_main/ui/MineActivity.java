package com.supcon.mes.module_main.ui;

import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BasePresenterActivity;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomCircleTextImageView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_main.R;

/**
 * @Author xushiyun
 * @Create-time 7/17/19
 * @Pageage com.supcon.mes.module_login.ui
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
@Router(Constant.Router.MINE)
public class MineActivity extends BasePresenterActivity {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("titleBarLayout")
    RelativeLayout titleBarLayout;
    @BindByTag("mineUserIcon")
    CustomCircleTextImageView mineUserIcon;
    @BindByTag("mineUserName")
    CustomTextView mineUserName;
    @BindByTag("mineUserCode")
    CustomTextView mineUserCode;
    @BindByTag("mineUserDepart")
    CustomTextView mineUserDepart;
    @BindByTag("mineUserWork")
    CustomTextView mineUserWork;
    @BindByTag("mineUserMail")
    CustomTextView mineUserMail;

    @Override
    protected int getLayoutID() {
        return R.layout.hs_ac_mine;
    }


    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> back());
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("个人信息");
        mineUserName.setContent(EamApplication.getAccountInfo().getStaffName());
        mineUserDepart.setContent(EamApplication.getAccountInfo().getDepartmentName());
    }
}
