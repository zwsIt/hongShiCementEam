package com.supcon.mes.module_main.controller;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;

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
import java.util.Iterator;
import java.util.List;

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
        if (Constant.TableStatus_CH.DISPATCH.equals(processedEntity.proStatus) || Constant.TableStatus_CH.EDIT.equals(processedEntity.proStatus) || Constant.TableStatus_CH.SPARE_PART_APPLY.equals(processedEntity.proStatus)
         || Constant.TableStatus_CH.ELE_ON.equals(processedEntity.proStatus) || Constant.TableStatus_CH.ELE_OFF.equals(processedEntity.proStatus)) {
            List<FlowProcessEntity> flowProcessEntityList = new ArrayList<>();
            genFlowProEntity(flowProcessEntityList);
            FlowProcessAdapter flowProcessAdapter = new FlowProcessAdapter(context, flowProcessEntityList);
            recyclerView.setAdapter(flowProcessAdapter);
            flowProcessAdapter.notifyDataSetChanged();
        } else {
            //eg：openurl = /BEAM2/workList/workRecord/workExecuteEdit.action , /BEAM2/runningState/runningHead/runningStateEdit.action等
            String url = processedEntity.openUrl.substring(0, processedEntity.openUrl.lastIndexOf("/"));
            presenterRouter.create(DealInfoAPI.class).listDealInfo(url, processedEntity.tableInfoId);
        }
    }

    private void genFlowProEntity(List<FlowProcessEntity> flowProcessEntityList) {
        FlowProcessEntity flowProcessEntity = new FlowProcessEntity();
        flowProcessEntity.isFinish = false;
        flowProcessEntity.flowProcess = processedEntity.proStatus;
        flowProcessEntity.time = processedEntity.createTime == null ? "--" : DateUtil.dateTimeFormat(processedEntity.createTime);
        flowProcessEntity.dealStaff = processedEntity.staffName;
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
        flowProcessEntity.time = initData.get(1) == null ? "--" : DateUtil.dateTimeFormat(new BigDecimal((Double) initData.get(1)).longValue()); // 处理时间
        flowProcessEntity.dealStaff = initData.get(4).toString(); // 用户
        flowProcessEntityList.add(flowProcessEntity);

        for (int i = 1; i < entity.size(); i++) {
            List<Object> data = (List<Object>) entity.get(i);
            if (Constant.TableStatus_CH.RECALL.equals(data.get(0).toString())) continue; // 过滤撤回活动
            int count = 0;
            for (int j = 0; j < flowProcessEntityList.size(); j++) {
                if (!data.get(0).toString().equals(flowProcessEntityList.get(j).flowProcess)) {
                    count++;
                    if (count == flowProcessEntityList.size()) { // 添加
                        flowProcessEntity = new FlowProcessEntity();
                        flowProcessEntity.isFinish = true;
                        flowProcessEntity.flowProcess = data.get(0).toString(); // 活动名称
                        flowProcessEntity.time = data.get(1) == null ? "--" : DateUtil.dateTimeFormat(new BigDecimal((Double) data.get(1)).longValue()); // 处理时间
                        flowProcessEntity.dealStaff = data.get(4).toString(); // 用户
                        flowProcessEntityList.add(flowProcessEntity);
                        break;
                    }
                } else {  // 替换
                    flowProcessEntity = flowProcessEntityList.get(j);
                    flowProcessEntity.isFinish = true;
                    flowProcessEntity.flowProcess = data.get(0).toString(); // 活动名称
                    flowProcessEntity.time = data.get(1) == null ? "--" : DateUtil.dateTimeFormat(new BigDecimal((Double) data.get(1)).longValue()); // 处理时间
                    flowProcessEntity.dealStaff = data.get(4).toString(); // 用户
                    break;
                }
            }
        }
        // 通知状态删除执行、验收只显示通知
//        for (FlowProcessEntity flowProEntity :flowProcessEntityList){
//            if ((Constant.TableStatus_CH.EXECUTE.equals(flowProEntity.flowProcess) && Constant.TableStatus_CH.NOTIFY.equals(processedEntity.prostatus))){
//                flowProcessEntityList.remove(flowProEntity);
//                break;
//            }
//        }
        Iterator<FlowProcessEntity> iterator = flowProcessEntityList.iterator();
        FlowProcessEntity flowProcess;
        while (iterator.hasNext()) {
            flowProcess = iterator.next();
            if ((Constant.TableStatus_CH.EXECUTE.equals(flowProcess.flowProcess) || Constant.TableStatus_CH.ACCEPT.equals(flowProcess.flowProcess))
                    && Constant.TableStatus_CH.NOTIFY.equals(processedEntity.proStatus)) {
                iterator.remove();
            }
        }
        // 过滤驳回后出现的颠倒流程
        List<FlowProcessEntity> flowProcessEntityFilterList = new ArrayList<>();
        for (FlowProcessEntity processEntity : flowProcessEntityList) {
            if (processEntity.flowProcess.equals(processedEntity.proStatus)) { // 和当前状态相同则退出循环
                break;
            } else {
                flowProcessEntityFilterList.add(processEntity); // 不同则添加
            }
        }
        genFlowProEntity(flowProcessEntityFilterList); // 需要添加当前状态
        FlowProcessAdapter flowProcessAdapter = new FlowProcessAdapter(context, flowProcessEntityFilterList);
        recyclerView.setAdapter(flowProcessAdapter);
        flowProcessAdapter.notifyDataSetChanged();
    }

    @Override
    public void listDealInfoFailed(String errorMsg) {
        LogUtil.e(errorMsg);
    }

}
