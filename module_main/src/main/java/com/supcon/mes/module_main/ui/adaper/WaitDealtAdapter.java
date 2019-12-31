package com.supcon.mes.module_main.ui.adaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_main.IntentRouter;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.controller.DealInfoController;
import com.supcon.mes.module_main.model.bean.EamEntity;
import com.supcon.mes.module_main.model.bean.FlowProcessEntity;
import com.supcon.mes.module_main.model.bean.ProcessedEntity;
import com.supcon.mes.module_main.model.bean.WaitDealtEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/24
 * ------------- Description -------------
 * 待办adapter
 */
public class WaitDealtAdapter extends BaseListDataRecyclerViewAdapter<WaitDealtEntity> {
    private boolean isEdit;

    public WaitDealtAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<WaitDealtEntity> getViewHolder(int viewType) {
        return new ContentViewHolder(context);
    }

    public void setEditable(boolean isEdit) {
        this.isEdit = isEdit;
        notifyDataSetChanged();
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
            waitDealtEntrust.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                }
            });
            RxView.clicks(itemView)
                    .throttleFirst(2, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            WaitDealtEntity item = getItem(getAdapterPosition());
                            if (isEdit) {
                                if (!TextUtils.isEmpty(item.state) && (item.state.equals("派工"))) {
                                    chkBox.performClick();
                                } else {
                                    ToastUtils.show(context, "请先取消派单进去再进入详情操作！");
                                }
                                return;
                            }
                            if (TextUtils.isEmpty(item.processkey)) { // 预警跳转
                                if (item.dataid == null || TextUtils.isEmpty(item.soucretype)) {
                                    ToastUtils.show(context, "未查询到当前单据状态!");
                                    return;
                                }
                                Bundle bundle = new Bundle();
                                if (!TextUtils.isEmpty(item.istemp) && item.soucretype.equals("巡检提醒")) {
                                    bundle.putString(Constant.IntentKey.TABLENO, item.workTableno);
                                    if (item.istemp.equals("1")) {
                                        IntentRouter.go(context, Constant.Router.LSXJ_LIST, bundle);
                                    } else {
                                        IntentRouter.go(context, Constant.Router.JHXJ_LIST, bundle);
                                    }
                                } else {
                                    if (item.peroidtype == null) {
                                        ToastUtils.show(context, "未查询到当前单据周期类型!");
                                        return;
                                    }
                                    bundle.putLong(Constant.IntentKey.WARN_ID, item.dataid);
                                    bundle.putString(Constant.IntentKey.PROPERTY, item.peroidtype.id);
                                    if (item.soucretype.equals("润滑提醒")) {
                                        IntentRouter.go(context, Constant.Router.LUBRICATION_EARLY_WARN, bundle);
                                    } else if (item.soucretype.equals("零部件提醒")) {
                                        IntentRouter.go(context, Constant.Router.SPARE_EARLY_WARN, bundle);
                                    } else if (item.soucretype.equals("维保提醒")) {
                                        IntentRouter.go(context, Constant.Router.MAINTENANCE_EARLY_WARN, bundle);
                                    }
                                }

                            } else {
                                if (!TextUtils.isEmpty(item.workTableno)) {
                                    Bundle bundle = new Bundle();
                                    // 工单跳转
                                    if (Constant.ProcessKey.WORK.equals(item.processkey)) {
                                        if (!TextUtils.isEmpty(item.openurl)) {
                                            bundle.putString(Constant.IntentKey.TABLENO, item.workTableno);
                                            switch (item.openurl) {
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
                                    } else if (Constant.ProcessKey.FAULT_INFO.equals(item.processkey)) {  // 隐患单跳转
                                        YHEntity yhEntity = new YHEntity();
                                        yhEntity.tableNo = item.workTableno;
                                        bundle.putSerializable(Constant.IntentKey.YHGL_ENTITY, yhEntity);
                                        IntentRouter.go(context, Constant.Router.YH_EDIT, bundle);
                                    } else if (Constant.ProcessKey.SPARE_PART_APPLY.equals(item.processkey)) { // 备件领用申请跳转
//                                        if (!EamApplication.isHailuo()) return;
                                        if (item.pendingid == null) {
                                            ToastUtils.show(context,"单据待办ID空");
                                            return;
                                        }
                                        if (!TextUtils.isEmpty(item.openurl)) {
                                            bundle.putLong(Constant.IntentKey.TABLE_ID, item.dataid);
                                            bundle.putLong(Constant.IntentKey.PENDING_ID, item.pendingid);
                                            switch (item.openurl) {
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
                                                    if (Constant.TableStatus_CH.NOTIFY.equals(item.state)){
                                                        bundle.putBoolean(Constant.IntentKey.IS_EDITABLE,false);
                                                    }
                                                    IntentRouter.go(context, Constant.Router.SPARE_PART_APPLY_VIEW, bundle);
                                                    break;
                                            }
                                        }
                                    }else if (Constant.ProcessKey.WORK_TICKET.equals(item.processkey)){ // 检修作业票
                                        if (!TextUtils.isEmpty(item.openurl)) {
                                            bundle.putLong(Constant.IntentKey.TABLE_ID, item.dataid);
                                            bundle.putLong(Constant.IntentKey.PENDING_ID, item.pendingid);
                                            switch (item.openurl) {
                                                case Constant.HSWorkTicketView.EDIT_URL:
                                                    IntentRouter.go(context, Constant.Router.OVERHAUL_WORKTICKET_EDIT, bundle);
                                                    break;
                                                case Constant.HSWorkTicketView.VIEW_URL:
                                                    bundle.putBoolean(Constant.IntentKey.IS_EDITABLE,true);
                                                    IntentRouter.go(context, Constant.Router.OVERHAUL_WORKTICKET_VIEW, bundle);
                                                    break;
                                            }
                                        }
                                    }else if (Constant.ProcessKey.ELE_ON.equals(item.processkey)){ // 送电
                                        String url = "http://" + EamApplication.getIp() + ":" + EamApplication.getPort()
                                                + Constant.WebUrl.SD_LIST_NEW;
                                        bundle.putString(BaseConstant.WEB_AUTHORIZATION, EamApplication.getAuthorization());
                                        bundle.putString(BaseConstant.WEB_COOKIE, EamApplication.getCooki());
                                        bundle.putBoolean(BaseConstant.WEB_HAS_REFRESH, true);
                                        bundle.putBoolean(BaseConstant.WEB_IS_LIST, true);
                                        bundle.putString(BaseConstant.WEB_URL, url);
                                        IntentRouter.go(context, Constant.Router.SD, bundle);
                                    }else if (Constant.ProcessKey.ELE_OFF.equals(item.processkey)){  // 停电

                                        if (EamApplication.isHongshi()){
                                            bundle.putLong(Constant.IntentKey.TABLE_ID, item.dataid);
                                            bundle.putLong(Constant.IntentKey.PENDING_ID, item.pendingid);
                                            IntentRouter.go(context, Constant.Router.HS_ELE_OFF_EDIT, bundle);
                                        }else {
                                            String url = "http://" + EamApplication.getIp() + ":" + EamApplication.getPort()
                                                    + Constant.WebUrl.TD_LIST_NEW;
                                            bundle.putString(BaseConstant.WEB_AUTHORIZATION, EamApplication.getAuthorization());
                                            bundle.putString(BaseConstant.WEB_COOKIE, EamApplication.getCooki());
                                            bundle.putBoolean(BaseConstant.WEB_HAS_REFRESH, true);
                                            bundle.putBoolean(BaseConstant.WEB_IS_LIST, true);
                                            bundle.putString(BaseConstant.WEB_URL, url);
                                            IntentRouter.go(context, Constant.Router.TD, bundle);
                                        }
                                    }
                                }
                            }
                        }
                    });
            chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    WaitDealtEntity item = getItem(getAdapterPosition());
                    item.isCheck = b;
                }
            });
        }

        @Override
        protected int layoutId() {
            return R.layout.hs_item_wait_dealt;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void update(WaitDealtEntity data) {
            if (Constant.ProcessKey.SPARE_PART_APPLY.equals(data.processkey)) {
                tableNo.setVisibility(View.VISIBLE);
                tableNo.setContent(data.workTableno);
            } else {
                tableNo.setVisibility(View.GONE);
            }
            if (isEdit && !TextUtils.isEmpty(data.state) && (data.state.equals("派工"))) {
                chkBox.setVisibility(View.VISIBLE);
                chkBox.setChecked(data.isCheck);
            } else {
                chkBox.setVisibility(View.GONE);
            }
            waitDealtEamName.setText(Util.strFormat(TextUtils.isEmpty(data.eamname) ? data.eamcode : data.eamname));
            if (data.nextduration != null) {
                waitDealtTime.setText(Util.strFormat2(data.nextduration));
            } else {
                waitDealtTime.setText(data.excutetime != null ? DateUtil.dateFormat(data.excutetime, "yyyy-MM-dd HH:mm:ss") : "--");
            }
            waitDealtEamSource.setText(Util.strFormat(data.soucretype));

            // 隐患单、工单、设备验收单、润滑/维保预警 提供内容
            if (Constant.ProcessKey.FAULT_INFO.equals(data.processkey) || Constant.ProcessKey.WORK.equals(data.processkey) || Constant.ProcessKey.CHECK_APPLY_FW.equals(data.processkey)
                    || "润滑提醒".equals(data.soucretype) || "维保提醒".equals(data.soucretype)) {
                waitDealtContent.setVisibility(View.VISIBLE);
                waitDealtContent.setText(String.format(context.getString(R.string.device_style6), "内容:", Util.strFormat(data.content)));
            } else {
                waitDealtContent.setVisibility(View.GONE);
            }
            // 工单、隐患单可委托
            if (Constant.ProcessKey.WORK.equals(data.processkey) || Constant.ProcessKey.FAULT_INFO.equals(data.processkey)) {
                waitDealtEntrust.setVisibility(View.VISIBLE);
            } else {
                waitDealtEntrust.setVisibility(View.GONE);
            }

            if (data.overdateflag.equals("1")) {
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
                }else {
                    waitDealtEamState.setTextColor(context.getResources().getColor(R.color.gray));
                }
            } else {
                waitDealtEamState.setText("");
            }

            if (!TextUtils.isEmpty(data.entrflag) && data.entrflag.equals("0")) {
                waitDealtEntrust.setImageDrawable(context.getResources().getDrawable(R.drawable.btn_entrust));
            } else {
                waitDealtEntrust.setImageDrawable(context.getResources().getDrawable(R.drawable.btn_entrusted));
            }

            if (!"MainActivity".equals(context.getClass().getSimpleName())) {
                // 只处理工单、隐患单、验收单、运行记录、备件领用申请、停送电
                if ((Constant.ProcessKey.WORK.equals(data.processkey) || Constant.ProcessKey.FAULT_INFO.equals(data.processkey))
                        || Constant.ProcessKey.CHECK_APPLY_FW.equals(data.processkey) || Constant.ProcessKey.RUN_STATE_WF.equals(data.processkey)
                        || Constant.ProcessKey.SPARE_PART_APPLY.equals(data.processkey) || Constant.ProcessKey.ELE_OFF.equals(data.processkey)
                        || Constant.ProcessKey.ELE_ON.equals(data.processkey) && !TextUtils.isEmpty(data.openurl)) {
                    ProcessedEntity processedEntity = new ProcessedEntity();
                    processedEntity.prostatus = data.state;
                    processedEntity.openurl = data.openurl;
                    processedEntity.staffname = data.getStaffid().name;
                    processedEntity.tableid = data.tableid;
                    DealInfoController dealInfoController = new DealInfoController(context, flowProcessView, processedEntity);
                    dealInfoController.getDealInfoList();
                }
//                flowProcessShow(data);
            }
        }

        /**
         * 工作流程展示
         *
         * @param data
         */
        private void flowProcessShow(WaitDealtEntity data) {
            List<FlowProcessEntity> list = new ArrayList<>();
            FlowProcessEntity flowProcessEntity;
            if (!TextUtils.isEmpty(data.processkey)) {
                if (Constant.TableStatus_CH.EDIT.equals(data.state) || Constant.TableStatus_CH.DISPATCH.equals(data.state)) {
//                    this.flowProcessView.setVisibility(View.VISIBLE);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = data.state;
                    flowProcessEntity.time = data.endtimeactual != null ? DateUtil.dateTimeFormat(data.endtimeactual) : null;
                    flowProcessEntity.isFinish = false;
                    list.add(flowProcessEntity);
                } else if (Constant.TableStatus_CH.EXECUTE.equals(data.state) || Constant.TableStatus_CH.NOTIFY.equals(data.state)) {
//                    this.flowProcessView.setVisibility(View.VISIBLE);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = "派工";
                    flowProcessEntity.time = data.endtimeactual != null ? DateUtil.dateTimeFormat(data.endtimeactual) : null;
                    flowProcessEntity.isFinish = true;
                    list.add(flowProcessEntity);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = data.state;
                    flowProcessEntity.time = data.endtimeactual != null ? DateUtil.dateTimeFormat(data.endtimeactual) : null;
                    list.add(flowProcessEntity);
                } else if (Constant.TableStatus_CH.ACCEPT.equals(data.state)) {
//                    this.flowProcessView.setVisibility(View.VISIBLE);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = "派工";
                    flowProcessEntity.time = data.endtimeactual != null ? DateUtil.dateTimeFormat(data.endtimeactual) : null;
                    flowProcessEntity.isFinish = true;
                    list.add(flowProcessEntity);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = "执行";
                    flowProcessEntity.time = data.endtimeactual != null ? DateUtil.dateTimeFormat(data.endtimeactual) : null;
                    flowProcessEntity.isFinish = true;
                    list.add(flowProcessEntity);
                    flowProcessEntity = new FlowProcessEntity();
                    flowProcessEntity.flowProcess = data.state;
                    flowProcessEntity.time = data.endtimeactual != null ? DateUtil.dateTimeFormat(data.endtimeactual) : null;
                    list.add(flowProcessEntity);
                } else {
//                    this.flowProcessView.setVisibility(View.GONE);
                }
            }
            this.flowProcessView.setAdapter(new FlowProcessAdapter(context, list)); // 设置数据item
        }


    }
}
