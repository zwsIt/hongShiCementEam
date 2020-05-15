package com.supcon.mes.module_contact.ui;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BasePresenterActivity;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomCircleTextImageView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.StaffPicController;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.util.SystemUtil;
import com.supcon.mes.module_contact.R;

import java.util.regex.Pattern;

/**
 * @Author xushiyun
 * @Create-time 7/8/19
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc 通讯录详细视图界面
 */
@Router(Constant.Router.CONTACT_VIEW)
public class ContactViewActivity extends BasePresenterActivity {

    @BindByTag("ivBack")
    ImageView ivBack;
    @BindByTag("usrInfo")
    LinearLayout usrInfo;
    @BindByTag("tvTelephone")
    CustomTextView tvTelephone;
    @BindByTag("phone")
    CustomTextView phone;
    @BindByTag("otherPhone")
    CustomTextView otherPhone;
    @BindByTag("email")
    CustomTextView email;
    @BindByTag("department")
    CustomTextView department;
    @BindByTag("company")
    CustomTextView company;
    @BindByTag("userName")
    TextView userName;
    @BindByTag("userWork")
    TextView userWork;
    @BindByTag("ivTelphone")
    ImageView ivTelphone;
    @BindByTag("ivSms")
    ImageView ivSms;
    @BindByTag("ivEmail")
    ImageView ivEmail;

    CustomCircleTextImageView userIcon;
    private ContactEntity mData;
    //匹配手机号码的正则表达式
    private Pattern mPattern = Pattern.compile("^[1][3,4,5,7,8,9][0-9]{9}$"/*"^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$"*/);

    @Override
    protected int getLayoutID() {
        return R.layout.ac_txl_view;
    }

    @Override
    protected void onInit() {
        super.onInit();
        mData = (ContactEntity) getIntent().getSerializableExtra(Constant.IntentKey.CONTACT_ENTITY);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.txl_view_title);
        userIcon = findViewById(R.id.userIcon);
    }

    @Override
    protected void initListener() {
        super.initListener();
        ivBack.setOnClickListener(v -> back());
        //电话拨打功能开发
        ivTelphone.setOnClickListener(v -> {

            if (!TextUtils.isEmpty(mData.getMOBILE()) && mPattern.matcher(mData.getMOBILE()).find())
                SystemUtil.callPhone(context, mData.getMOBILE());
            else ToastUtils.show(context, "电话信息为空或号码错误！");
        });
        //短信发送功能开发
        ivSms.setOnClickListener(v -> {
                    if (!TextUtils.isEmpty(mData.getMOBILE()) && mPattern.matcher(mData.getMOBILE()).find())
                        SystemUtil.sendSms(context, mData.getMOBILE());
                    else ToastUtils.show(context, "电话信息为空或号码错误！");
                }
        );
        //邮件发送功能开发
        ivEmail.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(mData.getEMAIL()))
                SystemUtil.sendEmail(context, mData.getEMAIL());
            else ToastUtils.show(context, "未设置邮箱信息！");
        });
    }

    @Override
    protected void initData() {
        super.initData();
//        tvTelephone.setContent(mData.getClass());
        userName.setText(mData.getNAME());
        userWork.setText(mData.getPOSITIONNAME());
        tvTelephone.setContent(mData.getMOBILE());
        phone.setContent(mData.getMOBILE());
        otherPhone.setContent(mData.getMOBILE());
        email.setContent(mData.getEMAIL());


        if (TextUtils.isEmpty(mData.getDEPARTMENTNAME())) {
            department.setContent(mData.getDEPARTMENTNAME());
        } else {
            department.setContent(mData.getFULLPATHNAME());
        }

        if (!TextUtils.isEmpty(mData.getCOMPANYNAME())) {
            company.setContent(mData.getCOMPANYNAME());
        } else {
            company.setContent("");
        }

        userIcon.setTag(R.id.imageid, mData.getSTAFFID());
        new StaffPicController(rootView).initStaffPic(mData.getSTAFFID(), userIcon,mData.getPICTURE());
    }
}
