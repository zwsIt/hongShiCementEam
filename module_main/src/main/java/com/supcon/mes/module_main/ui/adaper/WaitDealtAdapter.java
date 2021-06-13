package com.supcon.mes.module_main.ui.adaper;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.App;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.middleware.util.ProcessKeyUtil;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_main.IntentRouter;
import com.supcon.mes.module_main.R;
import com.supcon.mes.middleware.controller.DealInfoController;
import com.supcon.mes.middleware.model.bean.ProcessedEntity;
import com.supcon.mes.module_main.model.bean.WaitDealtEntity;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Predicate;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/24
 * ------------- Description -------------
 * 待办adapter
 */
public class WaitDealtAdapter extends BaseListDataRecyclerViewAdapter<WaitDealtEntity> {
    private boolean isEdit;
    private boolean isSubordinate;

    public WaitDealtAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<WaitDealtEntity> getViewHolder(int viewType) {
        return new ContentViewHolder(context);
    }

    public void setEditable(boolean isEdit) {
        this.isEdit = isEdit;
//        notifyDataSetChanged();
    }

    public void setSubordinate(boolean isSubordinate) {
        this.isSubordinate = isSubordinate;
    }

    class ContentViewHolder extends BaseRecyclerViewHolder<WaitDealtEntity> {

        @BindByTag("waitDealtEamName")
        TextView waitDealtEamName;
        @BindByTag("waitDealtTime")
        TextView waitDealtTime;
        @BindByTag("waitDealtEamSource")
        TextView waitDealtEamSource;
        @BindByTag("waitDealtEamState")
        TextView waitDealtEamState;
        @BindByTag("waitDealtContent")
        TextView waitDealtContent;

        @BindByTag("waitDealtEntrust")
        ImageView waitDealtEntrust;
        @BindByTag("chkBox")
        CheckBox chkBox;

        @BindByTag("flowProcessView")
        RecyclerView flowProcessView;

        @BindByTag("tableNo")
        CustomTextView tableNo;
        @BindByTag("moreTvLl")
        LinearLayout moreTvLl;

        public ContentViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initView() {
            super.initView();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            flowProcessView.setLayoutManager(linearLayoutManager); // 水平线性布局
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            waitDealtEntrust.setOnClickListener(view -> {
                WaitDealtEntity item = getItem(getAdapterPosition());
                if (isEdit) {
                    if (!TextUtils.isEmpty(item.state) && item.state.equals("派工")) {
                        chkBox.performClick();
                    } else {
                        ToastUtils.show(context, "请先取消派单进去再进入详情操作！");
                    }
                    return;
                }
                onItemChildViewClick(view, 0, getItem(getAdapterPosition()));
            });
            RxView.clicks(itemView)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .filter(new Predicate<Object>() {
                        @Override
                        public boolean test(Object o) throws Exception {
                            if (isSubordinate) {  // 我的下属  进查看视图
                                Map<String,Object> pending = new HashMap();
                                pending.put("staffName", EamApplication.getAccountInfo().staffName);
                                pending.put("table", getItem(getAdapterPosition()).processKey);
                                MobclickAgent.onEventObject(App.getAppContext(),"subordinate_pending_click",pending);
                                onItemChildViewClick(itemView, getAdapterPosition(), getItem(getAdapterPosition()));
                                return false;
                            }
                            return true;
                        }
                    })
                    .subscribe(o -> {
                        WaitDealtEntity item = getItem(getAdapterPosition());
                        Map<String,Object> pending = new HashMap(5);
                        pending.put("staffName", EamApplication.getAccountInfo().staffName);
                        pending.put("table", item.processKey);
                        MobclickAgent.onEventObject(App.getAppContext(),"mine_pending_click",pending);
                        if (isEdit) {
                            if (!TextUtils.isEmpty(item.state) && (item.state.equals("派工"))) {
                                chkBox.performClick();
                            } else {
                                ToastUtils.show(context, "请先取消派单进去再进入详情操作！");
                            }
                            return;
                        }
                        if (!TextUtils.isEmpty(item.workTableNo)) {
                            Bundle bundle = new Bundle();
                            if (Constant.EntityCode.POTROL_TASK_WF.equals(item.targetEntityCode)) {// 巡检
                                bundle.putString(Constant.IntentKey.TABLENO, item.workTableNo);
                                if (item.isTemp.equals("1")) {
                                    IntentRouter.go(context, Constant.Router.LSXJ_LIST, bundle);
                                } else {
                                    IntentRouter.go(context, Constant.Router.JHXJ_LIST, bundle);
                                }
                            } else if (Constant.EntityCode.WORK.equals(item.targetEntityCode)) { // 工单跳转
                                if (!TextUtils.isEmpty(item.openUrl)) {
                                    WXGDEntity wxgdEntity = new WXGDEntity();
                                    wxgdEntity.id = item.tableId;
                                    wxgdEntity.tableNo = item.workTableNo;
                                    bundle.putSerializable(Constant.IntentKey.WXGD_ENTITY, wxgdEntity);
//                                  bundle.putString(Constant.IntentKey.TABLENO, item.workTableNo);
                                    switch (item.openUrl) {
                                        case Constant.WxgdView.RECEIVE_OPEN_URL:
                                            IntentRouter.go(context, Constant.Router.WXGD_RECEIVE, bundle);
                                            break;
                                        case Constant.WxgdView.DISPATCH_OPEN_URL:
                                            IntentRouter.go(context, Constant.Router.WXGD_DISPATCHER, bundle);
                                            break;
                                        case Constant.WxgdView.VIEW_OPEN_URL:
                                            bundle.putBoolean(Constant.IntentKey.isEdit, false);
                                        case Constant.WxgdView.EXECUTE_OPEN_URL:
                                            IntentRouter.go(context, Constant.Router.WXGD_EXECUTE, bundle);
                                            break;
                                        case Constant.WxgdView.ACCEPTANCE_OPEN_URL:
                                            IntentRouter.go(context, Constant.Router.WXGD_ACCEPTANCE, bundle);
                                            break;
                                    }
                                } else {
                                    ToastUtils.show(context, "未查询到工单状态状态!");
                                }
                            } else if (Constant.EntityCode.FAULT_INFO.equals(item.targetEntityCode)) {  // 隐患单跳转
                                YHEntity yhEntity = new YHEntity();
                                yhEntity.id = item.tableId;
                                yhEntity.tableNo = item.workTableNo;
                                bundle.putSerializable(Constant.IntentKey.YHGL_ENTITY, yhEntity);
                                IntentRouter.go(context, Constant.Router.YH_EDIT, bundle);
                            } else if (Constant.EntityCode.SPARE_PART_APPLY.equals(item.targetEntityCode)) { // 备件领用申请跳转
                                if (item.pendingId == null) {
                                    ToastUtils.show(context, "单据待办ID空");
                                    return;
                                }
                                if (!TextUtils.isEmpty(item.openUrl)) {
                                    bundle.putLong(Constant.IntentKey.TABLE_ID, item.tableId);
                                    bundle.putLong(Constant.IntentKey.PENDING_ID, item.pendingId);
                                    switch (item.openUrl) {
                                        case Constant.HLSparePartView.EDIT_URL:
                                            IntentRouter.go(context, Constant.Router.SPARE_PART_APPLY_EDIT, bundle);
                                            break;
                                        case Constant.HLSparePartView.SUBMIT_EDIT_URL:
                                            IntentRouter.go(context, Constant.Router.SPARE_PART_APPLY_SUBMIT_EDIT, bundle);
                                            break;
                                        case Constant.HLSparePartView.SEND_EDIT_URL:
                                            IntentRouter.go(context, Constant.Router.SPARE_PART_APPLY_SEND_EDIT, bundle);
                                            break;
                                        case Constant.HLSparePartView.VIEW_URL:
                                            if (Constant.TableStatus_CH.NOTIFY.equals(item.state)) {
                                                bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, false);
                                            }
                                            IntentRouter.go(context, Constant.Router.SPARE_PART_APPLY_VIEW, bundle);
                                            break;
                                    }
                                }
                            } else if (Constant.EntityCode.WORK_TICKET.equals(item.targetEntityCode)) { // 检修作业票
                                if (!TextUtils.isEmpty(item.openUrl)) {
                                    bundle.putLong(Constant.IntentKey.TABLE_ID, item.tableId);
                                    bundle.putLong(Constant.IntentKey.PENDING_ID, item.pendingId);
                                    // 通过摘要summary解析停电tableInfoId
                                    if (!TextUtils.isEmpty(item.summary) && item.summary.contains("offApplyTableinfoid")) {
                                        try {
                                            String json = item.summary.substring(item.summary.indexOf("*") + 1);
                                            if (GsonUtil.gsonToMaps(json).get("offApplyTableinfoid") != null) {
                                                Double offApplyTableInfoId = (Double) GsonUtil.gsonToMaps(json).get("offApplyTableinfoid");
                                                bundle.putLong(Constant.IntentKey.ElE_OFF_TABLE_INFO_ID, Objects.requireNonNull(offApplyTableInfoId).longValue()); // 停电作业票tableInfoId
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            bundle.putLong(Constant.IntentKey.ElE_OFF_TABLE_INFO_ID, -1);
//                                                    ToastUtils.show(context, "摘要格式转换json异常，请检查摘要是否正常");
//                                                    return;
                                        }
                                    }
                                    switch (item.openUrl) {
                                        case Constant.HSWorkTicketView.EDIT_URL:
                                            IntentRouter.go(context, Constant.Router.OVERHAUL_WORKTICKET_EDIT, bundle);
                                            break;
                                        case Constant.HSWorkTicketView.SAFE_VIEW_URL:
                                            bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, true); // 安全员视图，编辑拍照
                                        case Constant.HSWorkTicketView.VIEW_URL:
                                        default:
                                            bundle.putString(Constant.IntentKey.TABLE_STATUS, item.state); // 单据状态
                                            bundle.putString(Constant.IntentKey.ACTIVITY_NAME, item.activityName);
                                            IntentRouter.go(context, Constant.Router.OVERHAUL_WORKTICKET_VIEW, bundle);
                                            break;

                                    }
                                }
                            } else if (Constant.EntityCode.ELE_ON_OFF.equals(item.targetEntityCode)) { // 停送电
                                bundle.putLong(Constant.IntentKey.TABLE_ID, item.tableId);
                                bundle.putLong(Constant.IntentKey.PENDING_ID, item.pendingId);
                                bundle.putString(Constant.IntentKey.TABLE_STATUS, item.state); // 单据状态
                                bundle.putString(Constant.IntentKey.ACTIVITY_NAME, item.activityName);
                                if (item.peroidType == null) return;
                                if (Constant.EleOffOn.ELE_ON.equals(item.peroidType.id)){ // 送电
                                    if (Constant.HSEleOnView.EDIT_URL.equals(item.openUrl)) {
                                        IntentRouter.go(context, Constant.Router.HS_ELE_ON_EDIT, bundle);
                                    } else {
                                        IntentRouter.go(context, Constant.Router.HS_ELE_ON_VIEW, bundle);
                                    }
                                }else { // 停电
                                    if (Constant.HSEleOffView.EDIT_URL.equals(item.openUrl)) {
                                        IntentRouter.go(context, Constant.Router.HS_ELE_OFF_EDIT, bundle);
                                    } else {
                                        IntentRouter.go(context, Constant.Router.HS_ELE_OFF_VIEW, bundle);
                                    }
                                }
                            } else if (Constant.EntityCode.CHECK_APPLY_FW.equals(item.targetEntityCode)) { // 设备验收单
                                if (!TextUtils.isEmpty(item.openUrl)) {
                                    bundle.putString(Constant.IntentKey.TABLENO, item.workTableNo);
                                    switch (item.openUrl) {
                                        case Constant.CheckApply.EDIT_URL:
                                            IntentRouter.go(context, Constant.Router.ACCEPTANCE_EDIT, bundle);
                                            break;
                                        case Constant.CheckApply.VIEW_URL: // 通知
                                            bundle.putBoolean(Constant.IntentKey.isEdit, true);
                                            IntentRouter.go(context, Constant.Router.ACCEPTANCE_VIEW, bundle);
                                            break;
                                        default:
                                            IntentRouter.go(context, Constant.Router.ACCEPTANCE_VIEW, bundle);
                                    }


                                }

                            }
                        }
                    });
            chkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                WaitDealtEntity item = getItem(getAdapterPosition());
                item.isCheck = b;
            });
            RxView.clicks(moreTvLl)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(o -> IntentRouter.go(context, Constant.Router.PENDING_LIST));
        }

        @Override
        protected int layoutId() {
            return R.layout.hs_item_wait_dealt;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void update(WaitDealtEntity data) {
            if (TextUtils.isEmpty(data.workTableNo)) {
                tableNo.setVisibility(View.GONE);
            } else {
                tableNo.setVisibility(View.VISIBLE);
                tableNo.setContent(data.workTableNo);
            }
            if (isEdit /*&& !TextUtils.isEmpty(data.state) && (data.state.equals("派工"))*/) {
                chkBox.setVisibility(View.VISIBLE);
                chkBox.setChecked(data.isCheck);
            } else {
                chkBox.setVisibility(View.GONE);
            }
            waitDealtEamName.setText(TextUtils.isEmpty(data.eamAssetcode) ? data.eamName : data.eamName + "(" + data.eamAssetcode + ")");
            if (data.nextDuration != null) {
                waitDealtTime.setText(Util.strFormat2(data.nextDuration));
            } else {
                waitDealtTime.setText(data.excuteTime != null ? DateUtil.dateFormat(data.excuteTime, Constant.TimeString.MONTH_DAY_HOUR_MIN) : "--");
            }
            waitDealtEamSource.setText("来源:" + Util.strFormat(data.sourceType));

            // 隐患单、工单、设备验收单、润滑/维保预警 提供内容
            if (Constant.EntityCode.FAULT_INFO.equals(data.targetEntityCode) || Constant.EntityCode.WORK.equals(data.targetEntityCode) || Constant.EntityCode.CHECK_APPLY_FW.equals(data.targetEntityCode)
                    || "润滑提醒".equals(data.sourceType) || "维保提醒".equals(data.sourceType)) {
                waitDealtContent.setVisibility(View.VISIBLE);
                waitDealtContent.setText(String.format(context.getString(R.string.device_style6), "内容:", Util.strFormat(data.content)));
            } else {
                waitDealtContent.setVisibility(View.GONE);
            }
            if (isSubordinate) {
                waitDealtEntrust.setVisibility(View.GONE);
            } else {
                // 工单、隐患单、检修作业票、停送电可委托
                if (Constant.EntityCode.WORK.equals(data.targetEntityCode) || Constant.EntityCode.FAULT_INFO.equals(data.targetEntityCode)
                        || Constant.EntityCode.WORK_TICKET.equals(data.targetEntityCode) || Constant.EntityCode.ELE_ON_OFF.equals(data.targetEntityCode)) {
                    waitDealtEntrust.setVisibility(View.VISIBLE);
                    if ("0".equals(data.entrFlag)) {
                        waitDealtEntrust.setImageDrawable(context.getResources().getDrawable(R.drawable.btn_entrust));
                    } else {
                        waitDealtEntrust.setImageDrawable(context.getResources().getDrawable(R.drawable.btn_entrusted));
                    }
                } else {
                    waitDealtEntrust.setVisibility(View.GONE);
                }
            }

            if (data.overDateFlag != null && data.overDateFlag.equals("1")) {
                waitDealtEamSource.setTextColor(context.getResources().getColor(R.color.orange));
            } else {
                waitDealtEamSource.setTextColor(context.getResources().getColor(R.color.gray));
            }
            if (!TextUtils.isEmpty(data.state)) {
                waitDealtEamState.setText(data.state);
                if (Constant.TableStatus_CH.EDIT.equals(data.state) || Constant.TableStatus_CH.DISPATCH.equals(data.state)) {
                    waitDealtEamState.setTextColor(context.getResources().getColor(R.color.gray));
                } else if (Constant.TableStatus_CH.EXECUTE.equals(data.state) || Constant.TableStatus_CH.NOTIFY.equals(data.state)) {
                    waitDealtEamState.setTextColor(context.getResources().getColor(R.color.yellow));
                } else if (Constant.TableStatus_CH.ACCEPT.equals(data.state)) {
                    waitDealtEamState.setTextColor(context.getResources().getColor(R.color.blue));
                } else {
                    waitDealtEamState.setTextColor(context.getResources().getColor(R.color.gray));
                }
            } else {
                waitDealtEamState.setText("");
            }

            if (!"MainActivity".equals(context.getClass().getSimpleName())) {
                // 只处理工单、隐患单、验收单、运行记录、备件领用申请、停送电、检修作业票
                if ((Constant.EntityCode.WORK.equals(data.targetEntityCode) || Constant.EntityCode.FAULT_INFO.equals(data.targetEntityCode))
                        || Constant.EntityCode.CHECK_APPLY_FW.equals(data.targetEntityCode) || Constant.EntityCode.RUN_STATE_WF.equals(data.targetEntityCode)
                        || Constant.EntityCode.SPARE_PART_APPLY.equals(data.targetEntityCode) || Constant.EntityCode.ELE_ON_OFF.equals(data.targetEntityCode)
                        || Constant.EntityCode.WORK_TICKET.equals(data.targetEntityCode) && !TextUtils.isEmpty(data.openUrl)) {
                    ProcessedEntity processedEntity = new ProcessedEntity();
                    processedEntity.proStatus = data.state;
                    processedEntity.openUrl = data.openUrl;
                    processedEntity.staffName = data.getStaffid().name;
                    processedEntity.tableInfoId = data.tableInfoId;
                    DealInfoController dealInfoController = new DealInfoController(context, flowProcessView, processedEntity);
                    dealInfoController.getDealInfoList();
                    flowProcessView.setVisibility(View.VISIBLE);
                } else {
                    flowProcessView.setVisibility(View.GONE);
                }
            } else {
                if (getAdapterPosition() == getItemCount() - 1)
                    moreTvLl.setVisibility(View.VISIBLE);
            }
        }
    }
}
