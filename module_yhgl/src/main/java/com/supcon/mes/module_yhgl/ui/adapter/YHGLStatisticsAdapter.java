package com.supcon.mes.module_yhgl.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_yhgl.IntentRouter;
import com.supcon.mes.module_yhgl.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

import static com.supcon.mes.middleware.constant.Constant.IntentKey.YHGL_ENTITY;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/8/13
 * ------------- Description -------------
 */
public class YHGLStatisticsAdapter extends BaseListDataRecyclerViewAdapter<YHEntity> {
    public YHGLStatisticsAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<YHEntity> getViewHolder(int viewType) {
        return new WorkViewHolder(context);
    }

    class WorkViewHolder extends BaseRecyclerViewHolder<YHEntity> {
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
            return R.layout.item_yh_statistics;
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
                            YHEntity yhEntity = getItem(getAdapterPosition());
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(YHGL_ENTITY, yhEntity);

                            if (Constant.TableStatus_CH.TAKE_EFFECT.equals(yhEntity.pending.taskDescription)) {
                                IntentRouter.go(context, Constant.Router.YH_LOOK, bundle);
                            } else if (Constant.TableStatus_CH.EDIT.equals(yhEntity.pending.taskDescription) && yhEntity.pending.id != null ) {  // 本人代办
                                IntentRouter.go(context, Constant.Router.YH_EDIT, bundle);
                            } else
                                IntentRouter.go(context, Constant.Router.YH_LOOK, bundle);
                        }
                    });
        }

        @Override
        protected void update(YHEntity data) {
            statisticsPosition.setText(String.valueOf(getAdapterPosition() + 1));
            if (data.faultState != null){
                statisticStatus.setText(data.faultState.value);
                statisticStatus.setCompoundDrawables(null,null,null,null);
                if (Constant.FaultState_ENG.WAIT_DEAL.equals(data.faultState.id)){
                    statisticStatus.setTextColor(context.getResources().getColor(R.color.orange));
                    if (data.pending.id != null){
                        statisticStatus.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable(R.drawable.ic_statistic_pending),null); // 可操作
                    }
                }else if (Constant.FaultState_ENG.DEALING.equals(data.faultState.id)){
                    statisticStatus.setTextColor(context.getResources().getColor(R.color.blue));
                }else{
                    statisticStatus.setTextColor(context.getResources().getColor(R.color.green));
                }
            }else {
                statisticStatus.setText("");
            }

            String eam = String.format(context.getString(R.string.device_style10), Util.strFormat(data.getEamID().name)
                    , Util.strFormat(data.getEamID().code));
            statisticsEam.setText(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()));
            statisticsContent.setText(Util.strFormat2(data.describe));
            statisticsStaff.setText(data.findStaffID != null ? data.findStaffID.name : "");
            statisticsTime.setText(data.findTime != 0 ? DateUtil.dateFormat(data.findTime, "yyyy-MM-dd HH:mm:ss") : "");
        }

    }
}
