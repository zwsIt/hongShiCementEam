package com.supcon.mes.module_warn.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.CommonListAPI;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.ContractStaffEntity;
import com.supcon.mes.middleware.model.contract.CommonListContract;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.bean.DailyLubricateRecordEntity;
import com.supcon.mes.module_warn.presenter.DailyLubrRecordsFinishPresenter;
import com.supcon.mes.module_warn.ui.adapter.LubricationRecordsFinishListAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/8
 * Email zhangwenshuai1@supcon.com
 * Desc 已完成日常润滑记录列表
 */
@Router(Constant.Router.LUBRICATION_RECORDS_FINISH_LIST)
@Presenter(value = {DailyLubrRecordsFinishPresenter.class})
public class LubricationRecordsFinishListActivity extends BaseRefreshRecyclerActivity<DailyLubricateRecordEntity> implements CommonListContract.View {
    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    CustomImageButton rightBtn;
    @BindByTag("contentView")
    RecyclerView contentView;

    Map<String,Object> queryParams = new HashMap<>();
    String eamCode;

    @Override
    protected void onInit() {
        super.onInit();

        eamCode = getIntent().getStringExtra(Constant.IntentKey.EAM_CODE);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setAutoPullDownRefresh(true);
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(5));
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.themeColor);
        titleText.setText("已完成日常润滑记录");
    }

    @Override
    protected IListAdapter<DailyLubricateRecordEntity> createAdapter() {
        return new LubricationRecordsFinishListAdapter(context);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_daily_lubr_records_finish_list;
    }

    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                queryParams.put(Constant.BAPQuery.EAM_CODE,eamCode);
                presenterRouter.create(CommonListAPI.class).listCommonObj(pageIndex,queryParams,false);
            }
        });
    }

    @Override
    public void listCommonObjSuccess(CommonBAPListEntity entity) {

    }

    @Override
    public void listCommonObjFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }
}
