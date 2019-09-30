package com.supcon.mes.module_main.ui.adaper;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.BaseConstant;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
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

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProcessedEntity item = getItem(getAdapterPosition());
                    String url = "http://" + EamApplication.getIp() + ":" + EamApplication.getPort()
                            + Constant.WebUrl.FLOWVIEW + "&modelCode=" + item.modelcode + "&deploymentId=" + item.deploymentid + "&fvTableInfoId=" + item.tableid;
                    Bundle bundle = new Bundle();
                    bundle.putString(BaseConstant.WEB_AUTHORIZATION, EamApplication.getAuthorization());
                    bundle.putString(BaseConstant.WEB_COOKIE, EamApplication.getCooki());
                    bundle.putString(BaseConstant.WEB_URL, url);
                    bundle.putBoolean(BaseConstant.WEB_HAS_REFRESH, true);
                    bundle.putBoolean(BaseConstant.WEB_IS_LIST, true);
                    IntentRouter.go(context, Constant.Router.PROCESSED_FLOW, bundle);
                }
            });
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

            // 只处理工单、隐患单、验收单、运行记录、备件领用申请
            if((Constant.ProcessKey.WORK.equals(data.processkey) || Constant.ProcessKey.FAULT_INFO.equals(data.processkey)
                    || Constant.ProcessKey.CHECK_APPLY_FW.equals(data.processkey) || Constant.ProcessKey.RUN_STATE_WF.equals(data.processkey)
                    || Constant.ProcessKey.SPARE_PART_APPLY.equals(data.processkey)) && !TextUtils.isEmpty(data.openurl)){
                dealInfoController = new DealInfoController(context,flowProcessView,data);
                dealInfoController.getDealInfoList();
                flowProcessView.setVisibility(View.VISIBLE);
            }else {
                flowProcessView.setVisibility(View.GONE);
            }
            if (EamApplication.isHailuo()){
                processTableNo.setText(Util.strFormat2(data.worktableno));
                processTime.setContent(data.workcreatetime != null ? DateUtil.dateFormat(data.workcreatetime, Constant.TimeString.YEAR_MONTH_DAY_HOUR_MIN_SEC) : "");
            }else {
                processTableNo.setText(Util.strFormat2(data.tableno));
                processTime.setContent(data.createTime != null ? DateUtil.dateFormat(data.createTime, Constant.TimeString.YEAR_MONTH_DAY_HOUR_MIN_SEC) : "");
            }

            processState.setText(Util.strFormat2(data.prostatus));
            if (!TextUtils.isEmpty(data.getEamid().name) || !TextUtils.isEmpty(data.getEamid().code)) {
                String eam = String.format(context.getString(R.string.device_style10), data.getEamid().name
                        , data.getEamid().code);
                processEam.setContent(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()).toString());
            }
            processStaff.setContent(Util.strFormat(data.staffname));
            if (!TextUtils.isEmpty(data.content)) {
                processContent.setContent(data.content);
                processContent.setVisibility(View.VISIBLE);
            } else {
                processContent.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(data.newstate)) {
                if (Constant.TableStatus_CH.PRE_DISPATCH.equals(data.newstate)) {
                    processState.setTextColor(context.getResources().getColor(R.color.gray));
                } else if (Constant.TableStatus_CH.PRE_EXECUTE.equals(data.newstate)) {
                    processState.setTextColor(context.getResources().getColor(R.color.yellow));
                } else if (Constant.TableStatus_CH.PRE_ACCEPT.equals(data.newstate)) {
                    processState.setTextColor(context.getResources().getColor(R.color.blue));
                } else if (Constant.TableStatus_CH.END.equals(data.newstate)) {
                    processState.setTextColor(context.getResources().getColor(R.color.green));
                } else if (Constant.TableStatus_CH.CANCEL.equals(data.newstate)) {
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
            if (!TextUtils.isEmpty(data.newstate)) {
                if (Constant.TableStatus_CH.PRE_DISPATCH.equals(data.newstate)) {
                    this.flowProcessView.setVisibility(View.VISIBLE);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = data.newstate;
                    flowProcessEntity.time = data.createTime != null ? DateUtil.dateTimeFormat(data.createTime) : null;
                    flowProcessEntity.isFinish = false;
                    list.add(flowProcessEntity);
                } else if (Constant.TableStatus_CH.PRE_EXECUTE.equals(data.newstate) || Constant.TableStatus_CH.PRE_NOTIFY.equals(data.newstate)) {
                    this.flowProcessView.setVisibility(View.VISIBLE);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = "待派工";
                    flowProcessEntity.time = data.createTime != null ? DateUtil.dateTimeFormat(data.createTime) : null;
                    flowProcessEntity.isFinish = true;
                    list.add(flowProcessEntity);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = data.newstate;
                    flowProcessEntity.time = DateUtil.dateTimeFormat(data.createTime);
                    list.add(flowProcessEntity);
                } else if (Constant.TableStatus_CH.PRE_ACCEPT.equals(data.newstate)) {
                    this.flowProcessView.setVisibility(View.VISIBLE);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = "待派工";
                    flowProcessEntity.time = data.createTime != null ? DateUtil.dateTimeFormat(data.createTime) : null;
                    flowProcessEntity.isFinish = true;
                    list.add(flowProcessEntity);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = "待执行";
                    flowProcessEntity.time = data.createTime != null ? DateUtil.dateTimeFormat(data.createTime) : null;
                    flowProcessEntity.isFinish = true;
                    list.add(flowProcessEntity);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = data.newstate;
                    flowProcessEntity.time = data.createTime != null ? DateUtil.dateTimeFormat(data.createTime) : null;
                    list.add(flowProcessEntity);
                } else if (Constant.TableStatus_CH.END.equals(data.newstate)) {
                    this.flowProcessView.setVisibility(View.VISIBLE);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = "待派工";
                    flowProcessEntity.time = data.createTime != null ? DateUtil.dateTimeFormat(data.createTime) : null;
                    flowProcessEntity.isFinish = true;
                    list.add(flowProcessEntity);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = "待执行";
                    flowProcessEntity.time = data.createTime != null ? DateUtil.dateTimeFormat(data.createTime) : null;
                    flowProcessEntity.isFinish = true;
                    list.add(flowProcessEntity);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = "待验收";
                    flowProcessEntity.time = data.createTime != null ? DateUtil.dateTimeFormat(data.createTime) : null;
                    flowProcessEntity.isFinish = true;
                    list.add(flowProcessEntity);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = data.newstate;
                    flowProcessEntity.time = data.createTime != null ? DateUtil.dateTimeFormat(data.createTime) : null;
                    list.add(flowProcessEntity);
                } else if (Constant.TableStatus_CH.CANCEL.equals(data.newstate)) {
                    this.flowProcessView.setVisibility(View.GONE);
                } else {
                    this.flowProcessView.setVisibility(View.GONE);
                }
            }
            this.flowProcessView.setAdapter(new FlowProcessAdapter(context, list)); // 设置数据item
        }


    }
}
