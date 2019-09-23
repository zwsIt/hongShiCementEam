package com.supcon.mes.module_sbda.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BasePresenterActivity;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.TextTypeHelper;
import com.supcon.mes.mbap.view.CustomExpandableTextView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.util.EAMStatusHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_sbda.R;
import com.supcon.mes.module_sbda.model.api.SBDAViewAPI;
import com.supcon.mes.module_sbda.model.bean.SBDAViewEntity;
import com.supcon.mes.module_sbda.model.contract.SBDAViewContract;
import com.supcon.mes.module_sbda.presenter.SBDAViewPresenter;

import static com.supcon.mes.module_sbda.util.Preconditions.checkNotNull;


/**
 * Environment: hongruijun
 * Created by Xushiyun on 2018/4/4.
 */
@Router(Constant.Router.SBDA_VIEW)
@Presenter(SBDAViewPresenter.class)
public class SBDAViewActivity extends BasePresenterActivity implements SBDAViewContract.View{
    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("eamCode")
    CustomTextView eamCode;

    @BindByTag("eamName")
    CustomTextView eamName;

    @BindByTag("eamModel")
    CustomTextView eamModel;

    @BindByTag("eamType")
    CustomTextView eamType;

    @BindByTag("eamState")
    CustomTextView eamState;

    @BindByTag("fileState")
    CustomTextView fileState;

    @BindByTag("eamUseDept")
    CustomTextView eamUserDept;

    @BindByTag("eamSpecif")
    CustomTextView eamSpecif;

    @BindByTag("eamDutyStaff")
    CustomTextView eamDutyStaff;

    @BindByTag("eamAbc")
    CustomTextView eamAbc;

    @BindByTag("eamUseDate")
    CustomTextView eamUseDate;

    @BindByTag("produceCode")
    CustomTextView produceCode;

    @BindByTag("produceFirm")
    CustomTextView produceFirm;

    @BindByTag("produceDate")
    CustomTextView produceDate;

    @BindByTag("specialType")
    CustomTextView specialType;

    @BindByTag("installPlace")
    CustomTextView installPlace;

    @BindByTag("installFirm")
    CustomTextView installFirm;

    @BindByTag("areaNum")
    CustomTextView areaNum;

    @BindByTag("fileDate")
    CustomTextView fileDate;

    @BindByTag("useYear")
    CustomTextView useYear;

    @BindByTag("haveRunState")
    CustomTextView haveRunState;

    @BindByTag("specialty")
    CustomTextView specialty;

    private CommonDeviceEntity commonDeviceEntity;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_sbda_view;
    }

    @Override
    protected void onInit() {
        super.onInit();
    }

    @Override
    protected void initData() {
        super.initData();
        titleText.setText("设备详情");
        commonDeviceEntity = (CommonDeviceEntity) getIntent().getSerializableExtra(Constant.IntentKey.SBDA_ENTITY);
        if(commonDeviceEntity != null)
        updateData();
        else {
            final Long eamId = getIntent().getLongExtra(Constant.IntentKey.SBDA_ENTITY_ID, -1);
            if(eamId!= -1) {
                presenterRouter.create(SBDAViewAPI.class).getSBDAItem(eamId);
            }
        }
    }

    private void updateData() {
        if (null == commonDeviceEntity) return;
        LogUtil.e(commonDeviceEntity.toString());
        setTextValue(eamCode, commonDeviceEntity.eamCode);
        setTextValue(eamName, commonDeviceEntity.eamName);
        setTextValue(eamModel, commonDeviceEntity.eamModel);
        setTextValue(eamType, commonDeviceEntity.eamType);
        setTextValue(eamState, EAMStatusHelper.getType(commonDeviceEntity.eamState));
        setTextValue(eamUserDept, commonDeviceEntity.eamUseDept);
        setTextValue(eamSpecif, commonDeviceEntity.eamSpecif);
        setTextValue(eamDutyStaff, commonDeviceEntity.eamDutyStaff);
        setTextValue(eamAbc, commonDeviceEntity.eamAbc);
        setTextValue(eamUseDate, transformDateFormat(commonDeviceEntity.eamUseDate));
        setTextValue(produceCode, commonDeviceEntity.produceCode);
        setTextValue(produceFirm, commonDeviceEntity.produceFirm);
        setTextValue(produceDate, transformDateFormat(commonDeviceEntity.produceDate));
        setTextValue(specialType, commonDeviceEntity.specialType);
        setTextValue(installPlace, commonDeviceEntity.installPlace);
        setTextValue(installFirm, commonDeviceEntity.installFirm);
        setTextValue(areaNum, commonDeviceEntity.areaNum);
        setTextValue(fileDate, transformDateFormat(commonDeviceEntity.fileDate));
        setTextValue(useYear, String.valueOf(commonDeviceEntity.useYear));
        setTextValue(haveRunState, commonDeviceEntity.haveRunState ? "是" : "否");
        setTextValue(specialty, commonDeviceEntity.specialty);
        setTextValue(fileState, commonDeviceEntity.fileState);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
    }

    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener((View view) ->
                onBackPressed()
        );
    }

    private String transformDateFormat(String date) {
        if(TextUtils.isEmpty(date)) return "";
        long date_l = DateUtil.dateFormat(date);
        return DateUtil.dateFormat(date_l, "yyyy-MM-dd");
    }

    private void setTextValue(Object textView, String text) {
        if(TextUtils.isEmpty(text) || "null".equals(text.trim()))   return;
        if(textView instanceof CustomTextView){
            ((CustomTextView) textView).setValue(text);
        }
        else if(textView instanceof CustomExpandableTextView)
        {
            ((CustomExpandableTextView) textView).setText(text);
        }
    }


    @Override
    public void getSBDAItemSuccess(SBDAViewEntity entity) {
        commonDeviceEntity = entity.entity;
        updateData();
    }

    @Override
    public void getSBDAItemFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
    }
}