package com.supcon.mes.module_overhaul_workticket.controller;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.view.CustomListWidget;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_overhaul_workticket.R;
import com.supcon.mes.module_overhaul_workticket.constant.OperateType;
import com.supcon.mes.module_overhaul_workticket.model.api.SafetyMeasuresAPI;
import com.supcon.mes.module_overhaul_workticket.model.bean.SafetyMeasuresEntity;
import com.supcon.mes.module_overhaul_workticket.model.bean.SafetyMeasuresList;
import com.supcon.mes.module_overhaul_workticket.model.contract.SafetyMeasuresContract;
import com.supcon.mes.module_overhaul_workticket.presenter.SafetyMeasuresPresenter;
import com.supcon.mes.module_overhaul_workticket.ui.adapter.SafetyMeasuresAdapter;

import java.util.List;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/11
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@Presenter(value = {SafetyMeasuresPresenter.class})
public class SafetyMeasuresController extends BaseViewController implements SafetyMeasuresContract.View {

    private Long tableId; // 单据ID

    @BindByTag("safetyMeasuresWidget")
    CustomListWidget<SafetyMeasuresEntity> safetyMeasuresWidget;

    public SafetyMeasuresController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
        tableId = getIntent().getLongExtra(Constant.IntentKey.TABLE_ID,1008);
    }

    @Override
    public void initView() {
        super.initView();
        safetyMeasuresWidget.setTitleBgColor(Color.parseColor("#F9F9F9"));
        RecyclerView contentView = safetyMeasuresWidget.findViewById(R.id.contentView);
        contentView.setBackgroundColor(context.getResources().getColor(R.color.line_gray));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(3, context)));

        safetyMeasuresWidget.setAdapter(new SafetyMeasuresAdapter(context));
    }

    @Override
    public void initData() {
        super.initData();
        presenterRouter.create(SafetyMeasuresAPI.class).listSafetyMeasures(tableId);
    }

    @Override
    public void listSafetyMeasuresSuccess(SafetyMeasuresList entity) {
        // 数据特殊处理
        List<SafetyMeasuresEntity> safetyMeasuresEntityList = entity.result;
        if (safetyMeasuresEntityList.size() > 0){
            if (safetyMeasuresEntityList.get(0) != null){
                safetyMeasuresEntityList.get(0).setType(OperateType.VIDEO.getType());
            }
            if (safetyMeasuresEntityList.get(2) != null){
                safetyMeasuresEntityList.get(2).setType(OperateType.NFC.getType());
            }
            if (safetyMeasuresEntityList.get(4) != null){
                safetyMeasuresEntityList.get(4).setType(OperateType.PHOTO.getType());
            }
            if (safetyMeasuresEntityList.get(5) != null){
                safetyMeasuresEntityList.get(5).setType(OperateType.PHOTO.getType());
            }
            if (safetyMeasuresEntityList.get(7) != null){
                safetyMeasuresEntityList.get(7).setType(OperateType.PHOTO.getType());
            }
            safetyMeasuresWidget.setData(safetyMeasuresEntityList);
        }

    }

    @Override
    public void listSafetyMeasuresFailed(String errorMsg) {
        LogUtil.w(errorMsg);
    }
}
