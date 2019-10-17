package com.supcon.mes.module_wxgd.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/8/13
 * ------------- Description -------------
 */
public class WXGDStatisticsAdapter extends BaseListDataRecyclerViewAdapter<WXGDEntity> {
    public WXGDStatisticsAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<WXGDEntity> getViewHolder(int viewType) {
        return new WorkViewHolder(context);
    }

    class WorkViewHolder extends BaseRecyclerViewHolder<WXGDEntity> {
        @BindByTag("statisticsPosition")
        TextView statisticsPosition;
        @BindByTag("statisticsEam")
        TextView statisticsEam;
        @BindByTag("statisticsContent")
        TextView statisticsContent;
        @BindByTag("statisticsStaff")
        TextView statisticsStaff;
        @BindByTag("statisticsTime")
        TextView statisticsTime;
        @BindByTag("statisticStatus")
        TextView statisticStatus;

        public WorkViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_wxgd_statistics;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            WXGDEntity wxgdEntity = getItem(getAdapterPosition());
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Constant.IntentKey.WXGD_ENTITY, wxgdEntity);
                            bundle.putBoolean(Constant.IntentKey.STATISTIC_SORCE,true);  // 仅适用于跳转-->完成工单
                            if (wxgdEntity.pending != null && !TextUtils.isEmpty(wxgdEntity.pending.taskDescription)) {
                                switch (wxgdEntity.pending.taskDescription) {
                                    case Constant.TableStatus_CH.EDIT:
                                    case Constant.TableStatus_CH.DISPATCH:
                                        if (wxgdEntity.pending.id == null){
                                            IntentRouter.go(context, Constant.Router.WXGD_COMPLETE, bundle);
                                        }else {
                                            IntentRouter.go(context, Constant.Router.WXGD_DISPATCHER, bundle);
                                        }
                                        break;
                                    case Constant.TableStatus_CH.CONFIRM:
                                        if (wxgdEntity.pending.id == null){
                                            IntentRouter.go(context, Constant.Router.WXGD_COMPLETE, bundle);
                                        }else {
                                            IntentRouter.go(context, Constant.Router.WXGD_RECEIVE, bundle);
                                        }
                                    case Constant.TableStatus_CH.EXECUTE:
                                    case Constant.TableStatus_CH.EXECUTE_NOTIFY:
                                        if (wxgdEntity.pending.id == null){
                                            IntentRouter.go(context, Constant.Router.WXGD_COMPLETE, bundle);
                                        }else {
                                            IntentRouter.go(context, Constant.Router.WXGD_EXECUTE, bundle);
                                        }
                                        break;
                                    case Constant.TableStatus_CH.NOTIFY:
                                        bundle.putBoolean(Constant.IntentKey.isEdit, false);
                                        if (wxgdEntity.pending.id == null){
                                            IntentRouter.go(context, Constant.Router.WXGD_COMPLETE, bundle);
                                        }else {
                                            IntentRouter.go(context, Constant.Router.WXGD_EXECUTE, bundle);
                                        }
                                        break;
                                    case Constant.TableStatus_CH.ACCEPT:
                                        if (wxgdEntity.pending.id == null){
                                            IntentRouter.go(context, Constant.Router.WXGD_COMPLETE, bundle);
                                        }else {
                                            IntentRouter.go(context, Constant.Router.WXGD_ACCEPTANCE, bundle);
                                        }
                                        break;
                                    default:
                                        IntentRouter.go(context, Constant.Router.WXGD_COMPLETE, bundle);
                                        break;
                                }
                            } else {
                                IntentRouter.go(context, Constant.Router.WXGD_COMPLETE, bundle);
                            }

                        }
                    });
        }

        @Override
        protected void update(WXGDEntity data) {
            statisticsPosition.setText(String.valueOf(getAdapterPosition() + 1));
            if (data.workState != null){
                statisticStatus.setText(data.workState.value);
                statisticStatus.setCompoundDrawables(null,null,null,null);
                if (Constant.WorkState_ENG.DISPATCH.equals(data.workState.id)){
                    statisticStatus.setTextColor(context.getResources().getColor(R.color.orange));
                    if (data.pending.id != null){
                        statisticStatus.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable(R.drawable.ic_statistic_pending),null); // 可操作
                    }
                }else if (Constant.WorkState_ENG.TAKE_EFFECT.equals(data.workState.id)){
                    statisticStatus.setTextColor(context.getResources().getColor(R.color.green));
                }else{
                    statisticStatus.setTextColor(context.getResources().getColor(R.color.blue));
                    if (data.pending.id != null){
                        statisticStatus.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable(R.drawable.ic_statistic_pending_blue),null); // 可操作
                    }
                }
            }else {
                statisticStatus.setText("");
            }
            String eam = String.format(context.getString(R.string.device_style10), Util.strFormat(data.eamID.name)
                    , Util.strFormat(data.eamID.code));
            statisticsEam.setText(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()));
            statisticsContent.setText(Util.strFormat2(data.workOrderContext));
            statisticsStaff.setText(Util.strFormat(data.getChargeStaff().name) );
            statisticsTime.setText(data.createTime != null ? DateUtil.dateFormat(data.createTime, "yyyy-MM-dd HH:mm:ss") : "");
        }

    }
}
