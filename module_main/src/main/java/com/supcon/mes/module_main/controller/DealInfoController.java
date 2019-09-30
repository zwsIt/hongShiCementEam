package com.supcon.mes.module_main.controller;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_main.model.api.DealInfoAPI;
import com.supcon.mes.module_main.model.bean.FlowProcessEntity;
import com.supcon.mes.module_main.model.bean.ProcessedEntity;
import com.supcon.mes.module_main.model.contract.DealInfoContract;
import com.supcon.mes.module_main.presenter.DealInfoPresenter;
import com.supcon.mes.module_main.ui.adaper.FlowProcessAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

@Presenter(value = {DealInfoPresenter.class})
public class DealInfoController extends BasePresenterController implements DealInfoContract.View {

    private Context context;
    private RecyclerView recyclerView;
    private ProcessedEntity processedEntity;

    public DealInfoController(Context context, RecyclerView flowProcessView, ProcessedEntity data) {
        this.context = context;
        this.recyclerView = flowProcessView;
        this.processedEntity = data;
    }

    public void getDealInfoList() {
        // 如果单据是在编辑或者派工环节，直接添加流程
        if (Constant.TableStatus_CH.DISPATCH.equals(processedEntity.prostatus) || Constant.TableStatus_CH.EDIT.equals(processedEntity.prostatus)) {
            List<FlowProcessEntity> flowProcessEntityList = new ArrayList<>();
            genFlowProEntity(flowProcessEntityList);
            recyclerView.setAdapter(new FlowProcessAdapter(context, flowProcessEntityList));
        } else {
            //eg：openurl = /BEAM2/workList/workRecord/workExecuteEdit.action , /BEAM2/runningState/runningHead/runningStateEdit.action等
            String[] splitArray = processedEntity.openurl.split("/");
            presenterRouter.create(DealInfoAPI.class).listDealInfo(splitArray[2], splitArray[3], processedEntity.tableid);
        }
    }

    private void genFlowProEntity(List<FlowProcessEntity> flowProcessEntityList) {
        FlowProcessEntity flowProcessEntity = new FlowProcessEntity();
        flowProcessEntity.isFinish = false;
        flowProcessEntity.flowProcess = processedEntity.prostatus;
        flowProcessEntity.time = processedEntity.createTime == null ? "--" : DateUtil.dateTimeFormat(processedEntity.createTime);
        flowProcessEntity.dealStaff = processedEntity.staffname;
        flowProcessEntityList.add(flowProcessEntity);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void listDealInfoSuccess(List entity) {
        // 数据处理，获取各个环节的最后一次信息,同时需要添加当前状态
        List<FlowProcessEntity> flowProcessEntityList = new ArrayList<>();
        FlowProcessEntity flowProcessEntity;
        flowProcessEntity = new FlowProcessEntity();
        List<Object> initData = (List<Object>) entity.get(0); // 默认添加第一个
        flowProcessEntity.isFinish = true;
        flowProcessEntity.flowProcess = initData.get(0).toString(); // 活动名称
        flowProcessEntity.time = initData.get(1) == null ? "--" : DateUtil.dateTimeFormat(new BigDecimal((Double)initData.get(1)).longValue()); // 处理时间
        flowProcessEntity.dealStaff = initData.get(4).toString(); // 用户
        flowProcessEntityList.add(flowProcessEntity);

        for (int i = 1 ; i < entity.size();i++){
            List<Object> data = (List<Object>) entity.get(i);
            if (Constant.TableStatus_CH.RECALL.equals(data.get(0).toString())) continue; // 过滤撤回活动
            int count = 0;
            for (int j = 0;j < flowProcessEntityList.size();j++){
                if (!data.get(0).toString().equals(flowProcessEntityList.get(j).flowProcess)){
                    count++;
                    if (count == flowProcessEntityList.size()){ // 添加
                        flowProcessEntity = new FlowProcessEntity();
                        flowProcessEntity.isFinish = true;
                        flowProcessEntity.flowProcess = data.get(0).toString(); // 活动名称
                        flowProcessEntity.time = data.get(1) == null ? "--" : DateUtil.dateTimeFormat(new BigDecimal((Double)data.get(1)).longValue()); // 处理时间
                        flowProcessEntity.dealStaff = data.get(4).toString(); // 用户
                        flowProcessEntityList.add(flowProcessEntity);
                        break;
                    }
                }else {  // 替换
                    flowProcessEntity = flowProcessEntityList.get(j);
                    flowProcessEntity.isFinish = true;
                    flowProcessEntity.flowProcess = data.get(0).toString(); // 活动名称
                    flowProcessEntity.time = data.get(1) == null ? "--" : DateUtil.dateTimeFormat(new BigDecimal((Double)data.get(1)).longValue()); // 处理时间
                    flowProcessEntity.dealStaff = data.get(4).toString(); // 用户
                    break;
                }
            }
        }

        //删除存在和当前活动状态相同者
        for (FlowProcessEntity flowProEntity :flowProcessEntityList){
            if (flowProEntity.flowProcess.equals(processedEntity.prostatus)){
                flowProcessEntityList.remove(flowProEntity);
                break;
            }
        }
        genFlowProEntity(flowProcessEntityList); // 需要添加当前状态
        recyclerView.setAdapter(new FlowProcessAdapter(context, flowProcessEntityList));
    }

    @Override
    public void listDealInfoFailed(String errorMsg) {
        LogUtil.e(errorMsg);
    }

}
