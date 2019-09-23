package com.supcon.mes.module_txl.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.bumptech.glide.Glide;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.mes.mbap.view.CustomCircleTextImageView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.StaffPicDownloadAPI;
import com.supcon.mes.middleware.model.bean.IDataInjector;
import com.supcon.mes.middleware.model.bean.ILayoutProvider;
import com.supcon.mes.middleware.model.contract.StaffPicDownloadContract;
import com.supcon.mes.middleware.presenter.StaffPicPresenter;
import com.supcon.mes.middleware.util.RequestOptionUtil;
import com.supcon.mes.module_txl.IntentRouter;
import com.supcon.mes.module_txl.R;
import com.supcon.mes.middleware.model.inter.ITxlEntity;

import java.io.File;
import java.io.Serializable;

/**
 * @Author xushiyun
 * @Create-time 7/8/19
 * @Pageage com.supcon.mes.module_txl.controller
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
@Presenter(value = StaffPicPresenter.class)
public class TxlListItemViewController extends BaseViewController implements ILayoutProvider, IDataInjector<ITxlEntity>, StaffPicDownloadContract.View {
    @BindByTag("userName")
    TextView userName;
    @BindByTag("userJob")
    TextView userJob;
    @BindByTag("company")
    TextView company;
    @BindByTag("department")
    TextView department;
    //    @BindByTag("userIcon")
    CustomCircleTextImageView userIcon;
    private View rootView;
    
    private ITxlEntity mData;
    
    public TxlListItemViewController(View rootView) {
        super(rootView);
        this.rootView = rootView;
    }
    
    public TxlListItemViewController newInstance() {
        View view = (new View(context));
        return new TxlListItemViewController(view);
    }
    
    @Override
    public void attachView(View rootView) {
        super.attachView(rootView);
        this.rootView = rootView;
    }
    
    @Override
    public int layout() {
        return R.layout.item_txl_list;
    }
    
    @Override
    public void initListener() {
        super.initListener();
        rootView.setOnClickListener(v-> {
            Bundle bundle  =new Bundle();
            bundle.putSerializable(Constant.IntentKey.TXL_ENTITY, (Serializable) mData);
            IntentRouter.go(context, Constant.Router.TXL_VIEW,bundle);
            
        });
    }
    
    @Override
    public void inject(ITxlEntity data) {
        mData = data;
        userIcon = rootView.findViewById(R.id.userIcon);
        initListener();
        userIcon.setImageDrawable(null);
        presenterRouter.create(StaffPicDownloadAPI.class).getStaffPic(data.getStaffId());
        userName.setText(data.getStaffName());
        userJob.setText(data.getStaffWork());
        company.setText(data.getCompanyName());
        department.setText(data.getDepartmentName());
    }
    
    @Override
    public void getStaffPicSuccess(File entity) {
        Glide.with(userIcon.getContext()).load(entity).apply(RequestOptionUtil.getEamRequestOptions(userIcon.getContext())).into(userIcon);
    }
    
    @Override
    public void getStaffPicFailed(String errorMsg) {
    
    }
}
