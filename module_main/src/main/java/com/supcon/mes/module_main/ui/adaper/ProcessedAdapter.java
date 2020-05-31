package com.supcon.mes.module_main.ui.adaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.BaseConstant;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_login.IntentRouter;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.controller.DealInfoController;
import com.supcon.mes.module_main.model.bean.FlowProcessEntity;
import com.supcon.mes.module_main.model.bean.ProcessedEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/24
 * ------------- Description -------------
 * 待办adapter
 */
public class ProcessedAdapter extends BaseListDataRecyclerViewAdapter<ProcessedEntity> {
    public ProcessedAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<ProcessedEntity> getViewHolder(int viewType) {
        return new ContentViewHolder(context);
    }

    class ContentViewHolder extends BaseRecyclerViewHolder<ProcessedEntity> {

        @BindByTag("processTableNo")
        TextView processTableNo;
        @BindByTag("processState")
        TextView processState;
        @BindByTag("processEam")
        CustomTextView processEam;
        @BindByTag("processTime")
        CustomTextView processTime;
        @BindByTag("processStaff")
        CustomTextView processStaff;
        @BindByTag("processContent")
        CustomTextView processContent;
        @BindByTag("flowProcessView")
        RecyclerView flowProcessView;
        private DealInfoController dealInfoController;

        public ContentViewHolder(Context context) {
            super(context);
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView)
                    .subscribe(o -> {
                        ProcessedEntity processedEntity = getItem(getAdapterPosition());
                        if (TextUtils.isEmpty(processedEntity.processKey)){
                            ToastUtils.show(context,context.getString(R.string.main_data_exception) + processedEntity.processKey);
                            return;
                        }
                        onItemChildViewClick(itemView,getAdapterPosition(),processedEntity);
                    });
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String url = "http://" + EamApplication.getIp() + ":" + EamApplication.getPort()
//                            + Constant.WebUrl.FLOWVIEW + "&modelCode=" + item.modelCode + "&deploymentId=" + item.deploymentId + "&fvTableInfoId=" + item.tableInfoId;
//                    Bundle bundle = new Bundle();
//                    bundle.putString(BaseConstant.WEB_AUTHORIZATION, EamApplication.getAuthorization());
//                    bundle.putString(BaseConstant.WEB_COOKIE, EamApplication.getCooki());
//                    bundle.putString(BaseConstant.WEB_URL, url);
//                    bundle.putBoolean(BaseConstant.WEB_HAS_REFRESH, true);
//                    bundle.putBoolean(BaseConstant.WEB_IS_LIST, true);
//                    IntentRouter.go(context, Constant.Router.PROCESSED_FLOW, bundle);
//                }
//            });
        }

        @Override
        protected int layoutId() {
            return R.layout.hs_item_process;
        }

        @Override
        protected void initView() {
            super.initView();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            flowProcessView.setLayoutManager(linearLayoutManager); // 水平线性布局
        }

        @Override
        protected void update(ProcessedEntity data) {

            // 只处理工单、隐患单、设备验收单、运行记录、备件领用申请、停送电、检修作业票
            if((Constant.ProcessKey.WORK.equals(data.processKey) || Constant.ProcessKey.FAULT_INFO.equals(data.processKey)
                    || Constant.ProcessKey.CHECK_APPLY_FW.equals(data.processKey) || Constant.ProcessKey.RUN_STATE_WF.equals(data.processKey)
                    || Constant.ProcessKey.SPARE_PART_APPLY.equals(data.processKey) || Constant.ProcessKey.ELE_OFF.equals(data.processKey)
                    || Constant.ProcessKey.ELE_ON.equals(data.processKey) || Constant.ProcessKey.WORK_TICKET.equals(data.processKey)) && !TextUtils.isEmpty(data.openUrl)){
                dealInfoController = new DealInfoController(context,flowProcessView,data);
                dealInfoController.getDealInfoList();
                flowProcessView.setVisibility(View.VISIBLE);
            }else {
                flowProcessView.setVisibility(View.GONE);
            }
//            if (EamApplication.isHailuo()){
                processTableNo.setText(Util.strFormat2(data.workTableNo));
                processTime.setContent(data.workCreateTime != null ? DateUtil.dateFormat(data.workCreateTime, Constant.TimeString.YEAR_MONTH_DAY_HOUR_MIN_SEC) : "");
//            }else {
//                processTableNo.setText(Util.strFormat2(data.tableno));
//                processTime.setContent(data.createTime != null ? DateUtil.dateFormat(data.createTime, Constant.TimeString.YEAR_MONTH_DAY_HOUR_MIN_SEC) : "");
//            }

            processState.setText(Util.strFormat2(data.proStatus));
            if (!TextUtils.isEmpty(data.getEamId().name) || !TextUtils.isEmpty(data.getEamId().eamAssetCode)) {
                String eam = String.format(context.getString(R.string.device_style10), data.getEamId().name
                        , data.getEamId().eamAssetCode);
                processEam.setContent(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()).toString());
            }else {
                processEam.setContent("");
            }
            if (TextUtils.isEmpty(data.staffName)){
                processStaff.setVisibility(View.GONE);
            }else {
                processStaff.setVisibility(View.VISIBLE);
                processStaff.setContent(data.staffName);
            }

            if (TextUtils.isEmpty(data.content)) {
                processContent.setVisibility(View.GONE);
            } else {
                processContent.setVisibility(View.VISIBLE);
                processContent.setContent(data.content);
            }
            if (!TextUtils.isEmpty(data.tableState)) {
                if (Constant.TableStatus_CH.PRE_DISPATCH.equals(data.tableState)) {
                    processState.setTextColor(context.getResources().getColor(R.color.gray));
                } else if (Constant.TableStatus_CH.PRE_EXECUTE.equals(data.tableState)) {
                    processState.setTextColor(context.getResources().getColor(R.color.yellow));
                } else if (Constant.TableStatus_CH.PRE_ACCEPT.equals(data.tableState)) {
                    processState.setTextColor(context.getResources().getColor(R.color.blue));
                } else if (Constant.TableStatus_CH.END.equals(data.tableState)) {
                    processState.setTextColor(context.getResources().getColor(R.color.green));
                } else if (Constant.TableStatus_CH.CANCEL.equals(data.tableState)) {
                    processState.setTextColor(context.getResources().getColor(R.color.red));
                } else {
                    processState.setTextColor(context.getResources().getColor(R.color.gray));
                }
            } else {
                processState.setTextColor(context.getResources().getColor(R.color.gray));
            }
//            flowProcessShow(data);
        }

        /**
         * 工作流程展示
         *
         * @param data
         */
        private void flowProcessShow(ProcessedEntity data) {
            List<FlowProcessEntity> list = new ArrayList<>();
            FlowProcessEntity flowProcessEntity;
            if (!TextUtils.isEmpty(data.tableState)) {
                if (Constant.TableStatus_CH.PRE_DISPATCH.equals(data.tableState)) {
                    this.flowProcessView.setVisibility(View.VISIBLE);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = data.tableState;
                    flowProcessEntity.time = data.workCreateTime != null ? DateUtil.dateTimeFormat(data.workCreateTime) : null;
                    flowProcessEntity.isFinish = false;
                    list.add(flowProcessEntity);
                } else if (Constant.TableStatus_CH.PRE_EXECUTE.equals(data.tableState) || Constant.TableStatus_CH.PRE_NOTIFY.equals(data.tableState)) {
                    this.flowProcessView.setVisibility(View.VISIBLE);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = "待派工";
                    flowProcessEntity.time = data.workCreateTime != null ? DateUtil.dateTimeFormat(data.workCreateTime) : null;
                    flowProcessEntity.isFinish = true;
                    list.add(flowProcessEntity);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = data.tableState;
                    flowProcessEntity.time = DateUtil.dateTimeFormat(data.workCreateTime);
                    list.add(flowProcessEntity);
                } else if (Constant.TableStatus_CH.PRE_ACCEPT.equals(data.tableState)) {
                    this.flowProcessView.setVisibility(View.VISIBLE);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = "待派工";
                    flowProcessEntity.time = data.workCreateTime != null ? DateUtil.dateTimeFormat(data.workCreateTime) : null;
                    flowProcessEntity.isFinish = true;
                    list.add(flowProcessEntity);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = "待执行";
                    flowProcessEntity.time = data.workCreateTime != null ? DateUtil.dateTimeFormat(data.workCreateTime) : null;
                    flowProcessEntity.isFinish = true;
                    list.add(flowProcessEntity);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = data.tableState;
                    flowProcessEntity.time = data.workCreateTime != null ? DateUtil.dateTimeFormat(data.workCreateTime) : null;
                    list.add(flowProcessEntity);
                } else if (Constant.TableStatus_CH.END.equals(data.tableState)) {
                    this.flowProcessView.setVisibility(View.VISIBLE);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = "待派工";
                    flowProcessEntity.time = data.workCreateTime != null ? DateUtil.dateTimeFormat(data.workCreateTime) : null;
                    flowProcessEntity.isFinish = true;
                    list.add(flowProcessEntity);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = "待执行";
                    flowProcessEntity.time = data.workCreateTime != null ? DateUtil.dateTimeFormat(data.workCreateTime) : null;
                    flowProcessEntity.isFinish = true;
                    list.add(flowProcessEntity);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = "待验收";
                    flowProcessEntity.time = data.workCreateTime != null ? DateUtil.dateTimeFormat(data.workCreateTime) : null;
                    flowProcessEntity.isFinish = true;
                    list.add(flowProcessEntity);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = data.tableState;
                    flowProcessEntity.time = data.workCreateTime != null ? DateUtil.dateTimeFormat(data.workCreateTime) : null;
                    list.add(flowProcessEntity);
                } else if (Constant.TableStatus_CH.CANCEL.equals(data.tableState)) {
                    this.flowProcessView.setVisibility(View.GONE);
                } else {
                    this.flowProcessView.setVisibility(View.GONE);
                }
            }
            this.flowProcessView.setAdapter(new FlowProcessAdapter(context, list)); // 设置数据item
        }


    }
}
