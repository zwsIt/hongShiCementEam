package com.supcon.mes.module_main.ui.adaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.ProcessKeyUtil;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_main.IntentRouter;
import com.supcon.mes.module_main.R;
import com.supcon.mes.middleware.controller.DealInfoController;
import com.supcon.mes.middleware.model.bean.ProcessedEntity;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
        CustomTextView processTableNo;
        @BindByTag("processState")
        TextView processState;
        @BindByTag("processEam")
        TextView processEam;
        @BindByTag("processTime")
        TextView processTime;
        @BindByTag("processStaff")
        TextView processStaff;
        @BindByTag("processContent")
        TextView processContent;
        @BindByTag("flowProcessView")
        RecyclerView flowProcessView;
        @BindByTag("moreTvLl")
        LinearLayout moreTvLl;

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
                        goTable(processedEntity);
                    });
            RxView.clicks(moreTvLl)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(o -> IntentRouter.go(context, Constant.Router.PENDING_LIST));
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
            return R.layout.main_item_processed;
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
            if (!"MainActivity".equals(context.getClass().getSimpleName())) {
                // 只处理工单、隐患单、设备验收单、运行记录、备件领用申请、停送电、检修作业票
                if((ProcessKeyUtil.WORK.equals(data.processKey) || ProcessKeyUtil.FAULT_INFO.equals(data.processKey)
                        || ProcessKeyUtil.CHECK_APPLY_FW.equals(data.processKey) || ProcessKeyUtil.RUN_STATE_WF.equals(data.processKey)
                        || ProcessKeyUtil.SPARE_PART_APPLY.equals(data.processKey) || ProcessKeyUtil.ELE_OFF.equals(data.processKey)
                        || ProcessKeyUtil.ELE_ON.equals(data.processKey) || ProcessKeyUtil.WORK_TICKET.equals(data.processKey)) && !TextUtils.isEmpty(data.openUrl)){
                    dealInfoController = new DealInfoController(context,flowProcessView,data);
                    dealInfoController.getDealInfoList();
                    flowProcessView.setVisibility(View.VISIBLE);
                }else {
                    flowProcessView.setVisibility(View.GONE);
                }
            }else {
                if (getAdapterPosition() == getItemCount() - 1)
                    moreTvLl.setVisibility(View.VISIBLE);
            }

            processTableNo.setContent(Util.strFormat2(data.workTableNo));
            processTime.setText(data.workCreateTime != null ? DateUtil.dateFormat(data.workCreateTime, Constant.TimeString.MONTH_DAY_HOUR_MIN) : "");

            processState.setText(Util.strFormat2(data.proStatus));
            if (!TextUtils.isEmpty(data.getEamId().name) || !TextUtils.isEmpty(data.getEamId().eamAssetCode)) {
                String eam = String.format(context.getString(R.string.device_style10), data.getEamId().name
                        , data.getEamId().eamAssetCode);
                processEam.setText(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()).toString());
            }else {
                processEam.setText("");
            }
            if (TextUtils.isEmpty(data.staffName)){
                processStaff.setVisibility(View.GONE);
            }else {
                processStaff.setVisibility(View.VISIBLE);
                processStaff.setText(String.format("%s：%s", context.getResources().getString(R.string.main_pending_staff), data.staffName));
            }

            if (TextUtils.isEmpty(data.content)) {
                processContent.setVisibility(View.GONE);
            } else {
                processContent.setVisibility(View.VISIBLE);
                processContent.setText(data.content);
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
        }
    }

    private void goTable(ProcessedEntity processedEntity) {
        Bundle bundle = new Bundle();
        processedEntity.tableId = processedEntity.tableId == null ? -1L : processedEntity.tableId;

        if (processedEntity.processKey.equals(ProcessKeyUtil.WORK_TICKET)){
            goWorkTicket(processedEntity, bundle);
        }else if (processedEntity.processKey.equals(ProcessKeyUtil.ELE_ON)){
            bundle.putLong(Constant.IntentKey.TABLE_ID, processedEntity.tableId);
            IntentRouter.go(context,Constant.Router.HS_ELE_ON_VIEW,bundle);
        }else if (processedEntity.processKey.equals(ProcessKeyUtil.ELE_OFF)){
            bundle.putLong(Constant.IntentKey.TABLE_ID, processedEntity.tableId);
            IntentRouter.go(context,Constant.Router.HS_ELE_OFF_VIEW,bundle);
        }else if (processedEntity.processKey.equals(ProcessKeyUtil.FAULT_INFO)){
            YHEntity yhEntity = new YHEntity();
            yhEntity.tableNo = processedEntity.workTableNo;
            bundle.putSerializable(Constant.IntentKey.YHGL_ENTITY,yhEntity);
            IntentRouter.go(context, Constant.Router.YH_LOOK, bundle);
        }else if (processedEntity.processKey.equals(ProcessKeyUtil.WORK)){
            WXGDEntity wxgdEntity = new WXGDEntity();
            wxgdEntity.tableNo = processedEntity.workTableNo;
            bundle.putSerializable(Constant.IntentKey.WXGD_ENTITY,wxgdEntity);
            IntentRouter.go(context, Constant.Router.WXGD_COMPLETE, bundle);
        }else {
            ToastUtils.show(context, context.getResources().getString(R.string.main_processed_table_no_view));
        }
        /*switch (processedEntity.processKey){
            case ProcessKeyUtil.WORK_TICKET:
                goWorkTicket(processedEntity, bundle);
                break;
            case ProcessKeyUtil.ELE_ON:
                bundle.putLong(Constant.IntentKey.TABLE_ID, processedEntity.tableId);
                IntentRouter.go(context,Constant.Router.HS_ELE_ON_VIEW,bundle);
                break;
            case ProcessKeyUtil.ELE_OFF:
                bundle.putLong(Constant.IntentKey.TABLE_ID, processedEntity.tableId);
                IntentRouter.go(context,Constant.Router.HS_ELE_OFF_VIEW,bundle);
                break;
//                case ProcessKeyUtil.RUN_STATE_WF:
//                    break;
//                case ProcessKeyUtil.CHECK_APPLY_FW:
//                    break;
            case ProcessKeyUtil.FAULT_INFO:
                YHEntity yhEntity = new YHEntity();
                yhEntity.tableNo = processedEntity.workTableNo;
                bundle.putSerializable(Constant.IntentKey.YHGL_ENTITY,yhEntity);
                IntentRouter.go(context, Constant.Router.YH_LOOK, bundle);
                break;
            case ProcessKeyUtil.WORK:
                WXGDEntity wxgdEntity = new WXGDEntity();
                wxgdEntity.tableNo = processedEntity.workTableNo;
                bundle.putSerializable(Constant.IntentKey.WXGD_ENTITY,wxgdEntity);
                IntentRouter.go(context, Constant.Router.WXGD_COMPLETE, bundle);
                break;
            default:
                ToastUtils.show(context, context.getResources().getString(R.string.main_processed_table_no_view));
        }*/
    }

    /**
     * @method
     * @description 跳转检修作业票
     * @author: zhangwenshuai
     * @date: 2020/5/30 15:13
     * @param  * @param null
     * @return
     */
    private void goWorkTicket(ProcessedEntity processedEntity, Bundle bundle) {
        if (!TextUtils.isEmpty(processedEntity.summary) && processedEntity.summary.contains("offApplyTableinfoid")) {
            try {
                String json = processedEntity.summary.substring(processedEntity.summary.indexOf("*") +1);
                if (GsonUtil.gsonToMaps(json).get("offApplyTableinfoid") != null) {
                    Double offApplyTableInfoId = (Double) GsonUtil.gsonToMaps(json).get("offApplyTableinfoid");
                    bundle.putLong(Constant.IntentKey.ElE_OFF_TABLE_INFO_ID, Objects.requireNonNull(offApplyTableInfoId).longValue()); // 停电作业票tableInfoId
                }
            } catch (Exception e) {
                e.printStackTrace();
                bundle.putLong(Constant.IntentKey.ElE_OFF_TABLE_INFO_ID,-1);
            }
        }
        bundle.putLong(Constant.IntentKey.TABLE_ID, processedEntity.tableId);
        IntentRouter.go(context, Constant.Router.OVERHAUL_WORKTICKET_VIEW, bundle);
    }
}
