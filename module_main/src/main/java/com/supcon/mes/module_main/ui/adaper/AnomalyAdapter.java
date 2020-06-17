package com.supcon.mes.module_main.ui.adaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_main.IntentRouter;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.model.bean.AnomalyEntity;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/24
 * ------------- Description -------------
 * 异常记录adapter
 */
public class AnomalyAdapter extends BaseListDataRecyclerViewAdapter<AnomalyEntity> {
    public AnomalyAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<AnomalyEntity> getViewHolder(int viewType) {
        if (viewType == 0){
            return new WarnViewHolder(context);
        }else {
            return new ContentViewHolder(context);
        }
    }

    @Override
    public int getItemViewType(int position, AnomalyEntity anomalyEntity) {
        if (TextUtils.isEmpty(anomalyEntity.workTableNo)){
            return 0;
        }else {
            return 1;
        }
    }

    class ContentViewHolder extends BaseRecyclerViewHolder<AnomalyEntity> {
        @BindByTag("anomalyTableNo")
        TextView anomalyTableNo;
        @BindByTag("anomalyState")
        TextView anomalyState;
        @BindByTag("anomalyStaff")
        TextView anomalyStaff;
        @BindByTag("anomalyTime")
        TextView anomalyTime;
        @BindByTag("anomalyContent")
        TextView anomalyContent;
        @BindByTag("anomalySoucretype")
        TextView anomalySoucretype;

        public ContentViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.hs_item_anomaly;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            RxView.clicks(itemView)
                    .throttleFirst(300, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            AnomalyEntity anomalyEntity = getItem(getAdapterPosition());
                            Bundle bundle = new Bundle();
                            switch (anomalyEntity.processKey){
                                case Constant.ProcessKey.WORK:
                                    goWork(anomalyEntity, bundle);
                                    break;
                                case Constant.ProcessKey.FAULT_INFO:
                                    goFaultInfo(anomalyEntity,bundle);
                                    break;
                                case Constant.ProcessKey.ELE_OFF:
                                    bundle.putLong(Constant.IntentKey.TABLE_ID, anomalyEntity.dataId);
                                    IntentRouter.go(context, Constant.Router.HS_ELE_OFF_VIEW, bundle);
                                    break;
                                case Constant.ProcessKey.ELE_ON:
                                    bundle.putLong(Constant.IntentKey.TABLE_ID, anomalyEntity.dataId);
                                    IntentRouter.go(context, Constant.Router.HS_ELE_ON_VIEW, bundle);
                                    break;
                                case Constant.ProcessKey.WORK_TICKET:
                                    goWorkTicket(anomalyEntity,bundle);
                                    break;
                                    default:
                                        ToastUtils.show(context,"暂不支持打开页面查看");
                            }
                        }
                    });
        }

        @SuppressLint("CheckResult")
        @Override
        protected void update(AnomalyEntity data) {
//            if (Constant.ProcessKey.WORK.equals(data.processKey) || Constant.ProcessKey.FAULT_INFO.equals(data.processKey)){
//                Api.getInstance().retrofit.create(MainService.class).getPendingId(EamApplication.getAccountInfo().userId,data.workTableNo)
//                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .onErrorReturn(throwable -> {
//                    CommonEntity commonEntity = new CommonEntity();
//                    commonEntity.errMsg = throwable.toString();
//                    return commonEntity;
//                }).subscribe(commonEntity -> {
//                    if (commonEntity.result != null){
//                        data.pendingId = ((Double)commonEntity.result).longValue();
//                        anomalyState.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable(R.drawable.ic_statistic_pending),null);
//                    }else {
//                        data.pendingId = null;
//                        anomalyState.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
//                    }
//                });
//            }else {
//                anomalyState.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
//            }
            anomalyTableNo.setText(data.workTableNo);
            anomalyState.setText(Util.strFormat2(data.state));
            anomalySoucretype.setText(data.sourceType);
            anomalyStaff.setText(String.format(context.getString(R.string.device_style6), "待办人:", Util.strFormat(data.staffName)));
            anomalyTime.setText(data.createTime != null ? DateUtil.dateFormat(data.createTime, Constant.TimeString.MONTH_DAY_HOUR_MIN) : "");
            anomalyContent.setText(String.format(context.getString(R.string.device_style6), "内容:", Util.strFormat(data.content)));
            if (!TextUtils.isEmpty(data.state)) {
                if (data.state.equals(Constant.TableStatus_CH.DISPATCH)) {
                    anomalyState.setTextColor(context.getResources().getColor(R.color.orange));
                } else if (data.state.equals(Constant.TableStatus_CH.EXECUTE)) {
                    anomalyState.setTextColor(context.getResources().getColor(R.color.green));
                } else if (data.state.equals(Constant.TableStatus_CH.ACCEPT)) {
                    anomalyState.setTextColor(context.getResources().getColor(R.color.blue));
                } else {
                    anomalyState.setTextColor(context.getResources().getColor(R.color.gray));
                }
            } else {
                anomalyState.setTextColor(context.getResources().getColor(R.color.gray));
            }
        }

    }

    class WarnViewHolder extends BaseRecyclerViewHolder<AnomalyEntity> {

        @BindByTag("sourceType")
        CustomTextView sourceType;
        @BindByTag("time")
        CustomTextView time;
        @BindByTag("content")
        CustomTextView content;

        public WarnViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.main_item_exception_warn;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            RxView.clicks(itemView)
                    .throttleFirst(300, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        AnomalyEntity anomalyEntity = getItem(getAdapterPosition());
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constant.IntentKey.WARN_ID, anomalyEntity.dataId);
                        bundle.putString(Constant.IntentKey.PROPERTY, anomalyEntity.peroidType.id);
                        if (Constant.WarnType.LUBRICATION_WARN.equals(anomalyEntity.sourceType)) {
                            IntentRouter.go(context, Constant.Router.LUBRICATION_EARLY_WARN, bundle);
                        } else if (Constant.WarnType.SPARE_PART_WARN.equals(anomalyEntity.sourceType)) {
                            IntentRouter.go(context, Constant.Router.SPARE_EARLY_WARN, bundle);
                        } else if (Constant.WarnType.MAINTENANCE_WARN.equals(anomalyEntity.sourceType)) {
                            IntentRouter.go(context, Constant.Router.MAINTENANCE_EARLY_WARN, bundle);
                        }

                    });
        }

        @SuppressLint("CheckResult")
        @Override
        protected void update(AnomalyEntity data) {
            sourceType.setContent(Util.strFormat2(data.sourceType));
            if (data.peroidType != null){
                if (Constant.PeriodType.TIME_FREQUENCY.equals(data.peroidType.id)){
                    time.setContent(context.getResources().getString(R.string.nextTime) + DateUtil.dateFormat(data.nextExecuteTime,Constant.TimeString.MONTH_DAY_HOUR_MIN));
                }else {
                    time.setContent(context.getResources().getString(R.string.nextDuration) + data.nextDuration);
                }
            }
            content.setContent(String.format(context.getString(R.string.device_style6), "内容:", Util.strFormat(data.content)));
        }

    }

    /**
     * @description 隐患单跳转
     * @param
     * @return
     * @author zhangwenshuai1 2020/1/8
     *
     */
    private void goFaultInfo(AnomalyEntity anomalyEntity,Bundle bundle) {
        YHEntity yhEntity = new YHEntity();
        yhEntity.tableNo = anomalyEntity.workTableNo;
        bundle.putSerializable(Constant.IntentKey.YHGL_ENTITY,yhEntity);
        IntentRouter.go(context, Constant.Router.YH_LOOK, bundle);
    }

    /**
     * @description 工单跳转
     * @param
     * @return
     * @author zhangwenshuai1 2020/1/8
     *
     */
    private void goWork(AnomalyEntity anomalyEntity,Bundle bundle) {
        WXGDEntity wxgdEntity = new WXGDEntity();
        wxgdEntity.tableNo = anomalyEntity.workTableNo;
        bundle.putSerializable(Constant.IntentKey.WXGD_ENTITY,wxgdEntity);
        IntentRouter.go(context, Constant.Router.WXGD_COMPLETE, bundle);
//        if (!TextUtils.isEmpty(anomalyEntity.openUrl)) {
//            Bundle bundle = new Bundle();
//            WXGDEntity wxgdEntity = new WXGDEntity();
//            wxgdEntity.id = -1L;
//            wxgdEntity.tableNo = anomalyEntity.workTableNo;
//            bundle.putSerializable(Constant.IntentKey.WXGD_ENTITY, wxgdEntity);
//            switch (anomalyEntity.openUrl) {
//                case Constant.WxgdView.RECEIVE_OPEN_URL:
//                    IntentRouter.go(context, Constant.Router.WXGD_RECEIVE, bundle);
//                    break;
//                case Constant.WxgdView.DISPATCH_OPEN_URL:
//                    IntentRouter.go(context, Constant.Router.WXGD_DISPATCHER, bundle);
//                    break;
//                case Constant.WxgdView.VIEW_OPEN_URL:
//                    bundle.putBoolean(Constant.IntentKey.isEdit, false);
//                case Constant.WxgdView.EXECUTE_OPEN_URL:
//                    IntentRouter.go(context, Constant.Router.WXGD_EXECUTE, bundle);
//                    break;
//                case Constant.WxgdView.ACCEPTANCE_OPEN_URL:
//                    IntentRouter.go(context, Constant.Router.WXGD_ACCEPTANCE, bundle);
//                    break;
//            }
//        }
    }

    /**
     * @method
     * @description 跳转检修作业票
     * @author: zhangwenshuai
     * @date: 2020/5/30 15:13
     * @param  * @param null
     * @return
     */
    private void goWorkTicket(AnomalyEntity anomalyEntity, Bundle bundle) {
        if (!TextUtils.isEmpty(anomalyEntity.summary) && anomalyEntity.summary.contains("offApplyTableinfoid")) {
            try {
                String json = anomalyEntity.summary.substring(anomalyEntity.summary.indexOf("*") +1);
                if (GsonUtil.gsonToMaps(json).get("offApplyTableinfoid") != null) {
                    Double offApplyTableInfoId = (Double) GsonUtil.gsonToMaps(json).get("offApplyTableinfoid");
                    bundle.putLong(Constant.IntentKey.ElE_OFF_TABLE_INFO_ID, Objects.requireNonNull(offApplyTableInfoId).longValue()); // 停电作业票tableInfoId
                }
            } catch (Exception e) {
                e.printStackTrace();
                bundle.putLong(Constant.IntentKey.ElE_OFF_TABLE_INFO_ID,-1);
            }
        }
        bundle.putLong(Constant.IntentKey.TABLE_ID, anomalyEntity.dataId);
        IntentRouter.go(context, Constant.Router.OVERHAUL_WORKTICKET_VIEW, bundle);
    }

}
