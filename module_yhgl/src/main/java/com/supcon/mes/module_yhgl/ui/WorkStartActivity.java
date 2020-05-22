package com.supcon.mes.module_yhgl.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseControllerActivity;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomDateView;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.module_yhgl.IntentRouter;
import com.supcon.mes.module_yhgl.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

@Router(value = Constant.Router.WORK_START_EDIT)
public class WorkStartActivity extends BaseControllerActivity {

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    CustomImageButton rightBtn;
    @BindByTag("titleLayout")
    RelativeLayout titleLayout;
    @BindByTag("workStartStaff")
    CustomTextView workStartStaff;
    @BindByTag("eamCode")
    CustomTextView eamCode;
    @BindByTag("eamName")
    CustomTextView eamName;
    @BindByTag("workContactStaff")
    CustomTextView workContactStaff;
    @BindByTag("workEndTime")
    CustomDateView workEndTime;
    @BindByTag("workPriority")
    CustomSpinner workPriority;

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.themeColor);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fault_ac_work_start;
    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setText(getResources().getString(R.string.fault_work_start_edit));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> onBackPressed());
        workStartStaff.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF,true);
                bundle.putBoolean(Constant.IntentKey.IS_MULTI,false);
                bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG,workStartStaff.getTag().toString());
                IntentRouter.go(context,Constant.Router.CONTACT_SELECT,bundle);
            }
        });
        eamCode.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IntentKey.IS_MAIN_EAM,true);
                bundle.putBoolean(Constant.IntentKey.IS_MULTI,false);
                bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG,eamCode.getTag().toString());
                IntentRouter.go(context,Constant.Router.EAM,bundle);
            }
        });
        workContactStaff.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF,true);
                bundle.putBoolean(Constant.IntentKey.IS_MULTI,false);
                bundle.putString(Constant.IntentKey.COMMON_SEARCH_TAG,workContactStaff.getTag().toString());
                IntentRouter.go(context,Constant.Router.CONTACT_SELECT,bundle);
            }
        });

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEntity(CommonSearchEvent commonSearchEvent) {
        if (commonSearchEvent.commonSearchEntity instanceof CommonSearchStaff) {
            CommonSearchStaff staff = (CommonSearchStaff) commonSearchEvent.commonSearchEntity;
            if (workStartStaff.getTag().toString().equals(commonSearchEvent.flag)) {
                workStartStaff.setContent(staff.name);
            }else if (workContactStaff.getTag().toString().equals(commonSearchEvent.flag)){
                workContactStaff.setContent(staff.name);
            }
        }else if (commonSearchEvent.commonSearchEntity instanceof EamEntity){
            EamEntity eamEntity = (EamEntity) commonSearchEvent.commonSearchEntity;
            eamName.setContent(eamEntity.name);
            eamCode.setContent(eamEntity.eamAssetCode);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
