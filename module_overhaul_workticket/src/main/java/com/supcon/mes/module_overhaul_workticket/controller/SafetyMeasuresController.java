package com.supcon.mes.module_overhaul_workticket.controller;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.view.CustomListWidget;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.ListEvent;
import com.supcon.mes.module_overhaul_workticket.R;
import com.supcon.mes.module_overhaul_workticket.constant.SafetyMeasureDetails;
import com.supcon.mes.module_overhaul_workticket.model.api.SafetyMeasuresAPI;
import com.supcon.mes.module_overhaul_workticket.model.bean.SafetyMeasuresEntity;
import com.supcon.mes.module_overhaul_workticket.model.bean.SafetyMeasuresList;
import com.supcon.mes.module_overhaul_workticket.model.contract.SafetyMeasuresContract;
import com.supcon.mes.module_overhaul_workticket.presenter.SafetyMeasuresPresenter;
import com.supcon.mes.module_overhaul_workticket.ui.adapter.SafetyMeasuresAdapter;
import com.supcon.mes.module_overhaul_workticket.ui.adapter.SafetyMeasuresViewAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
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
    private Long eleOffTableInfoId; // 停电票tableInfoId
    private ImageView customListWidgetEdit;

    @BindByTag("safetyMeasuresWidget")
    CustomListWidget<SafetyMeasuresEntity> safetyMeasuresWidget;
    private List<SafetyMeasuresEntity> safetyMeasuresEntityList = new ArrayList<>();
    private boolean editable;

    public SafetyMeasuresController(View rootView) {
        super(rootView);
    }

    public SafetyMeasuresController setEditable(boolean editable){
        this.editable = editable;
        return this;
    }

    @Override
    public void onInit() {
        super.onInit();
        tableId = getIntent().getLongExtra(Constant.IntentKey.TABLE_ID,-1);
        eleOffTableInfoId = getIntent().getLongExtra(Constant.IntentKey.ElE_OFF_TABLE_INFO_ID,-1);
    }

    @Override
    public void initView() {
        super.initView();
        safetyMeasuresWidget.setTitleBgColor(Color.parseColor("#F9F9F9"));
        safetyMeasuresWidget.setShowText("");
        customListWidgetEdit = safetyMeasuresWidget.findViewById(R.id.customListWidgetEdit);  // 换做加载icon
        RecyclerView contentView = safetyMeasuresWidget.findViewById(R.id.contentView);
        contentView.setBackgroundColor(context.getResources().getColor(R.color.line_gray));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(3, context)));

        if (editable){
            SafetyMeasuresAdapter safetyMeasuresAdapter = new SafetyMeasuresAdapter(context);
            safetyMeasuresWidget.setAdapter(safetyMeasuresAdapter);
            safetyMeasuresAdapter.initCamera();
            safetyMeasuresAdapter.setEleOffTableInfoId(eleOffTableInfoId);
        }else {
            SafetyMeasuresViewAdapter adapter= new SafetyMeasuresViewAdapter(context);
            adapter.initCamera();
            adapter.setEleOffTableInfoId(eleOffTableInfoId);
            safetyMeasuresWidget.setAdapter(adapter);
        }

    }

    @Override
    public void initData() {
        super.initData();
        if (tableId == -1){
            initSafetyMeasuresDetails();
        }
    }

    public void listSafetyMeas(){
        Glide.with(context).asGif().load(R.drawable.preloader_1).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(customListWidgetEdit);
        presenterRouter.create(SafetyMeasuresAPI.class).listSafetyMeasures(tableId);
    }

    private void initSafetyMeasuresDetails() {
        SafetyMeasuresList entity = new SafetyMeasuresList();
        List<SafetyMeasuresEntity> safetyMeasuresEntityList = new ArrayList<>();
        SafetyMeasuresEntity safetyMeasuresEntity;
        for (SafetyMeasureDetails detail :SafetyMeasureDetails.values()){
            safetyMeasuresEntity = new SafetyMeasuresEntity();
            safetyMeasuresEntity.setSafetyMeasure(detail.getValue());
            safetyMeasuresEntity.setOperateType(detail.getOperateType());
            safetyMeasuresEntityList.add(safetyMeasuresEntity);
        }
        entity.result = safetyMeasuresEntityList;
        listSafetyMeasuresSuccess(entity);
    }

    @Override
    public void initListener() {
        super.initListener();
//        safetyMeasuresAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
//            @Override
//            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
//                String tag = (String) childView.getTag();
//                switch(tag){
//                    case "videoIv":
////                        onlineCameraController.startVideo();
//                        break;
//                    case "":
//                        break;
//                }
//            }
//        });
    }

    @Override
    public void listSafetyMeasuresSuccess(SafetyMeasuresList entity) {
        safetyMeasuresEntityList = entity.result;
        safetyMeasuresWidget.setData(safetyMeasuresEntityList);
        EventBus.getDefault().post(new ListEvent(Constant.EventFlag.WORK_TICKET_PT, safetyMeasuresEntityList));

        customListWidgetEdit.setVisibility(View.GONE);

    }

    @Override
    public void listSafetyMeasuresFailed(String errorMsg) {
        LogUtil.w(errorMsg);
        customListWidgetEdit.setImageResource(R.drawable.ic_error);
    }

    public List<SafetyMeasuresEntity> getSafetyMeasuresEntityList() {
        return safetyMeasuresEntityList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
