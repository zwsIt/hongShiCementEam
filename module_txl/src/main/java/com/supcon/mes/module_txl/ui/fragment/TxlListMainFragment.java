package com.supcon.mes.module_txl.ui.fragment;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.bumptech.glide.Glide;
import com.supcon.common.view.base.fragment.BasePresenterFragment;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomCircleTextImageView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.TxlEntity;
import com.supcon.mes.middleware.util.RequestOptionUtil;
import com.supcon.mes.module_txl.R;

import java.io.File;

/**
 * @Author xushiyun
 * @Create-time 7/26/19
 * @Pageage com.supcon.mes.module_txl.ui.fragment
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
@Deprecated
public class TxlListMainFragment extends BasePresenterFragment {
    @BindByTag("ivBack")
    ImageView ivBack;
    @BindByTag("tvBack")
    TextView tvBack;
    @BindByTag("usrInfo")
    LinearLayout usrInfo;
    @BindByTag("tvTelephone")
    CustomTextView tvTelephone;
    @BindByTag("email")
    CustomTextView email;
    @BindByTag("master")
    CustomTextView master;
    @BindByTag("department")
    CustomTextView department;
    @BindByTag("flow")
    CustomTextView flow;
    @BindByTag("staffName")
    TextView staffName;
    @BindByTag("positionName")
    TextView positionName;
    private TxlEntity mData;
    @Override
    protected int getLayoutID() {
        return R.layout.activity_txl_view;
    }
    
    @Override
    protected void onInit() {
        super.onInit();
        mData = (TxlEntity) getActivity().getIntent().getSerializableExtra(Constant.IntentKey.TXL_ENTITY);
    }
    
    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(getActivity(), R.color.txl_view_title);
    }
    
    @Override
    protected void initListener() {
        super.initListener();
        ivBack.setOnClickListener(v -> getActivity().onBackPressed());
        tvBack.setOnClickListener(v -> getActivity().onBackPressed());
    }
    
    @Override
    protected void initData() {
        super.initData();
//        tvTelephone.setContent(mData.getClass());
        staffName.setText(mData.getStaffName());
        positionName.setText(mData.getPositionName());
        tvTelephone.setContent(mData.getMOBILE());
        email.setContent(mData.getEMAIL());
        master.setContent(mData.getRZSJ());
        department.setContent(mData.getDepartmentName());

        
        File file = new File(Constant.IMAGE_SAVE_PATH +"TXL_"+ String.valueOf(mData.getStaffId())+".jpg");
        if(file.exists()) {
            CustomCircleTextImageView customCircleTextImageView = getView().findViewById(R.id.userIcon);
            Glide.with(customCircleTextImageView.getContext()).load(file)
                    .apply(RequestOptionUtil.getEamRequestOptions(customCircleTextImageView.getContext()))
                    .into(customCircleTextImageView);
        }
    }
}
