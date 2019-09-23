package com.supcon.mes.module_txl.ui;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.bumptech.glide.Glide;
import com.supcon.common.view.base.activity.BasePresenterActivity;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomCircleTextImageView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.TxlEntity;
import com.supcon.mes.middleware.util.RequestOptionUtil;
import com.supcon.mes.middleware.util.SystemUtil;
import com.supcon.mes.module_txl.R;

import java.io.File;
import java.util.regex.Pattern;

/**
 * @Author xushiyun
 * @Create-time 7/8/19
 * @Pageage com.supcon.mes.module_txl.ui
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc 通讯录详细视图界面
 */
@Router(Constant.Router.TXL_VIEW)
public class TxlViewActivity extends BasePresenterActivity {
    
    @BindByTag("ivBack")
    ImageView ivBack;
    @BindByTag("tvBack")
    TextView tvBack;
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
    @BindByTag("master")
    CustomTextView master;
    @BindByTag("department")
    CustomTextView department;
    @BindByTag("flow")
    CustomTextView flow;
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
    private TxlEntity mData;
    //匹配手机号码的正则表达式
    private Pattern mPattern = Pattern.compile("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$");
    
    @Override
    protected int getLayoutID() {
        return R.layout.activity_txl_view;
    }
    
    @Override
    protected void onInit() {
        super.onInit();
        mData = (TxlEntity) getIntent().getSerializableExtra(Constant.IntentKey.TXL_ENTITY);
    }
    
    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.txl_view_title);
    }
    
    @Override
    protected void initListener() {
        super.initListener();
        ivBack.setOnClickListener(v -> back());
        tvBack.setOnClickListener(v -> back());
        //电话拨打功能开发
        ivTelphone.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(mData.getMOBILE()) && mPattern.matcher(mData.getMOBILE()).find())
                SystemUtil.callPhone(context, mData.getMOBILE());
            else ToastUtils.show(context, "未设置通话信息！");
        });
        //短信发送功能开发
        ivSms.setOnClickListener(v -> {
                    if (!TextUtils.isEmpty(mData.getMOBILE()) && mPattern.matcher(mData.getMOBILE()).find())
                        SystemUtil.sendSms(context, mData.getMOBILE());
                    else ToastUtils.show(context, "未设置电话信息！");
                }
        );
        //邮件发送功能开发
//        ivEmail.setOnClickListener(v -> {
//            if (!TextUtils.isEmpty(mData.getEMAIL()) && mPattern.matcher(mData.getEMAIL()).find())
//                SystemUtil.sendSms(context, mData.getEMAIL());
//            else ToastUtils.show(context, "未设置邮箱信息！");
//        });
    }
    
    @Override
    protected void initData() {
        super.initData();
//        tvTelephone.setContent(mData.getClass());
        userName.setText(mData.getStaffName());
        userWork.setText(mData.getStaffWork());
        tvTelephone.setContent((String) mData.getMOBILE());
        phone.setContent((String) mData.getMOBILE());
        otherPhone.setContent((String) mData.getMOBILE());
        email.setContent((String) mData.getEMAIL());
        master.setContent((String) mData.getRZSJ());
        department.setContent(mData.getDepartmentName());
        flow.setContent("0");
        
        File file = new File(Constant.IMAGE_SAVE_PATH + "TXL_" + String.valueOf(mData.getStaffId()) + ".jpg");
        if (file.exists()) {
            CustomCircleTextImageView customCircleTextImageView = findViewById(R.id.userIcon);
            Glide.with(customCircleTextImageView.getContext()).load(file)
                    .apply(RequestOptionUtil.getEamRequestOptions(customCircleTextImageView.getContext()))
                    .into(customCircleTextImageView);
        }
    }
}
