package com.supcon.mes.module_txl.controller;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.mes.mbap.view.CustomCircleTextImageView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.StaffPicController;
import com.supcon.mes.middleware.model.api.StaffPicDownloadAPI;
import com.supcon.mes.middleware.model.bean.IDataInjector;
import com.supcon.mes.middleware.model.bean.ILayoutProvider;
import com.supcon.mes.middleware.model.inter.ITxlEntity;
import com.supcon.mes.module_txl.IntentRouter;
import com.supcon.mes.module_txl.R;

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
public class TxlListItemViewController extends BaseViewController implements ILayoutProvider, IDataInjector<ITxlEntity> {
    @BindByTag("staffName")
    TextView staffName;
    @BindByTag("phone")
    TextView phone;
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
        rootView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.IntentKey.TXL_ENTITY, (Serializable) mData);
            IntentRouter.go(context, Constant.Router.TXL_VIEW, bundle);

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void inject(ITxlEntity data) {
        mData = data;
        userIcon = rootView.findViewById(R.id.userIcon);
        initListener();
        userIcon.setImageDrawable(null);
        presenterRouter.create(StaffPicDownloadAPI.class).getStaffPic(data.getStaffId());
        staffName.setText(data.getStaffName());
        phone.setText(data.getMOBILE());
        company.setText(data.getCompanyName());
        department.setText(data.getDepartmentName());

        userIcon.setTag(R.id.imageid, data.getStaffId());
        new StaffPicController(rootView).initEamPic(data.getStaffId(), userIcon);
    }
}
