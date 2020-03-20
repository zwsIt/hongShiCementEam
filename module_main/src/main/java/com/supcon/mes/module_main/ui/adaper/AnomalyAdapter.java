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
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.network.Api;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_main.IntentRouter;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.model.api.AnomalyAPI;
import com.supcon.mes.module_main.model.bean.AnomalyEntity;
import com.supcon.mes.module_main.model.network.MainService;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

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
        return new ContentViewHolder(context);
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
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .filter(new Predicate<Object>() {
                        @Override
                        public boolean test(Object o) throws Exception {
                            AnomalyEntity anomalyEntity = getItem(getAdapterPosition());
                            if ((Constant.ProcessKey.WORK.equals(anomalyEntity.processKey) || Constant.ProcessKey.FAULT_INFO.equals(anomalyEntity.processKey)) && anomalyEntity.pendingId != null ){
                                return true;
                            }
                            ToastUtils.show(context,"不支持打开页面查看");
                            return false;
                        }
                    })
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            AnomalyEntity anomalyEntity = getItem(getAdapterPosition());
                            switch (anomalyEntity.processKey){
                                case Constant.ProcessKey.WORK:
                                    goWork(anomalyEntity);
                                    break;
                                case Constant.ProcessKey.FAULT_INFO:
                                    goFaultInfo(anomalyEntity);
                                    break;
                                    default:
                                        ToastUtils.show(context,"不支持打开页面查看");
                            }
                        }
                    });
        }

        @SuppressLint("CheckResult")
        @Override
        protected void update(AnomalyEntity data) {
            if (Constant.ProcessKey.WORK.equals(data.processKey) || Constant.ProcessKey.FAULT_INFO.equals(data.processKey)){
                Api.getInstance().retrofit.create(MainService.class).getPendingId(EamApplication.getAccountInfo().userId,data.workTableNo)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(throwable -> {
                    CommonEntity commonEntity = new CommonEntity();
                    commonEntity.errMsg = throwable.toString();
                    return commonEntity;
                }).subscribe(commonEntity -> {
                    if (commonEntity.result != null){
                        data.pendingId = ((Double)commonEntity.result).longValue();
                        anomalyState.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable(R.drawable.ic_statistic_pending),null);
                    }else {
                        data.pendingId = null;
                        anomalyState.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                    }
                });
            }else {
                anomalyState.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            }
            if (TextUtils.isEmpty(data.workTableNo)){
                if ("零部件提醒".equals(data.sourceType)){
                    anomalyTableNo.setText("备件预警");
                }else if ("润滑提醒".equals(data.sourceType)){
                    anomalyTableNo.setText("润滑预警");
                }else if ("维保提醒".equals(data.sourceType)){
                    anomalyTableNo.setText("维保预警");
                }
            }else {
                anomalyTableNo.setText(data.workTableNo);
            }
            anomalyState.setText(Util.strFormat2(data.state));
            anomalySoucretype.setText(data.sourceType);
            anomalyStaff.setText(String.format(context.getString(R.string.device_style6), "待办人:", Util.strFormat(data.staffName)));
            anomalyTime.setText(data.createTime != null ? DateUtil.dateFormat(data.createTime, "yyyy-MM-dd") : "");
            anomalyContent.setText(String.format(context.getString(R.string.device_style6), "内容:", Util.strFormat(data.content)));
            if (!TextUtils.isEmpty(data.state)) {
                if (data.state.equals("派工")) {
                    anomalyState.setTextColor(context.getResources().getColor(R.color.gray));
                } else if (data.state.equals("执行")) {
                    anomalyState.setTextColor(context.getResources().getColor(R.color.yellow));
                } else if (data.state.equals("验收")) {
                    anomalyState.setTextColor(context.getResources().getColor(R.color.blue));
                } else {
                    anomalyState.setTextColor(context.getResources().getColor(R.color.gray));
                }
            } else {
                anomalyState.setTextColor(context.getResources().getColor(R.color.gray));
            }
        }

    }

    /**
     * @description 隐患单跳转
     * @param
     * @return
     * @author zhangwenshuai1 2020/1/8
     *
     */
    private void goFaultInfo(AnomalyEntity anomalyEntity) {
        YHEntity yhEntity = new YHEntity();
        Bundle bundle = new Bundle();
        yhEntity.tableNo = anomalyEntity.workTableNo;
        bundle.putSerializable(Constant.IntentKey.YHGL_ENTITY, yhEntity);
        IntentRouter.go(context, Constant.Router.YH_EDIT, bundle);
    }

    /**
     * @description 工单跳转
     * @param
     * @return
     * @author zhangwenshuai1 2020/1/8
     *
     */
    private void goWork(AnomalyEntity anomalyEntity) {
        if (!TextUtils.isEmpty(anomalyEntity.openUrl)) {
            Bundle bundle = new Bundle();
            WXGDEntity wxgdEntity = new WXGDEntity();
            wxgdEntity.id = -1L;
            wxgdEntity.tableNo = anomalyEntity.workTableNo;
            bundle.putSerializable(Constant.IntentKey.WXGD_ENTITY, wxgdEntity);
            switch (anomalyEntity.openUrl) {
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
        }
    }

}
