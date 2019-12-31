package com.supcon.mes.module_overhaul_workticket.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.module_overhaul_workticket.IntentRouter;
import com.supcon.mes.module_overhaul_workticket.R;
import com.supcon.mes.module_overhaul_workticket.model.bean.WorkTicketEntity;

import java.util.concurrent.TimeUnit;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/11
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class WorkTicketAdapter extends BaseListDataRecyclerViewAdapter<WorkTicketEntity> {

    public WorkTicketAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<WorkTicketEntity> getViewHolder(int viewType) {
        return new WorkTicketViewHolder(context);
    }

    private class WorkTicketViewHolder extends BaseRecyclerViewHolder<WorkTicketEntity> {


        @BindByTag("itemTableNo")
        CustomTextView itemTableNo;
        @BindByTag("itemTableStatus")
        TextView itemTableStatus;
        @BindByTag("itemTableLayout")
        RelativeLayout itemTableLayout;
        @BindByTag("itemEam")
        TextView itemEam;
        @BindByTag("itemChargeStaff")
        TextView itemChargeStaff;
        @BindByTag("itemPersonLayout")
        LinearLayout itemPersonLayout;

        public WorkTicketViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_work_ticket;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        WorkTicketEntity workTicketEntity = getItem(getAdapterPosition());
                        PendingEntity pendingEntity = workTicketEntity.getPending();
                        if (pendingEntity == null){
                            ToastUtils.show(context,"代办为空,请刷新");
                            return;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constant.IntentKey.TABLE_ID, workTicketEntity.getId());
                        bundle.putLong(Constant.IntentKey.ElE_OFF_TABLE_INFO_ID, workTicketEntity.getOffApplyTableinfoid() == null ? -1 :workTicketEntity.getOffApplyTableinfoid()); // 停电作业票tableInfoId
                        bundle.putString(Constant.IntentKey.HAZARD_CONTRL_POINT,workTicketEntity.getHazardsourContrpointForDisplay());
                        if (workTicketEntity.getPending().id == null){ // 无代办、生效
                            IntentRouter.go(context,Constant.Router.OVERHAUL_WORKTICKET_VIEW,bundle);
                        }else {
                            bundle.putLong(Constant.IntentKey.PENDING_ID, workTicketEntity.getPending().id);
                            switch (workTicketEntity.getPending().taskDescription) {
                                case Constant.TableStatus_CH.EDIT:
                                    IntentRouter.go(context,Constant.Router.OVERHAUL_WORKTICKET_EDIT,bundle);
                                    break;
                                case Constant.TableStatus_CH.REVIEW:
                                case Constant.TableStatus_CH.REVIEW1:
                                    bundle.putBoolean(Constant.IntentKey.IS_EDITABLE,true);
                                case Constant.TableStatus_CH.TAKE_EFFECT:
                                default:
                                    IntentRouter.go(context,Constant.Router.OVERHAUL_WORKTICKET_VIEW,bundle);
                                    break;
                            }
                        }

                    });
        }

        @Override
        protected void update(WorkTicketEntity data) {
            itemTableNo.setContent(data.getTableNo());
            itemTableStatus.setText(data.getPending().taskDescription);
            itemEam.setText(TextUtils.isEmpty(data.getEamId().name) ? "" : String.format("%s(%s)", data.getEamId().name, data.getEamId().code));
            itemChargeStaff.setText(data.getChargeStaff().name);
            itemTableStatus.setTextColor(context.getResources().getColor(R.color.white));
            if (Constant.TableStatus_CH.EDIT.equals(data.getPending().taskDescription)) {
                itemTableStatus.setBackground(context.getResources().getDrawable(R.drawable.sh_edit_bg));
            } else if (Constant.TableStatus_CH.REVIEW.equals(data.getPending().taskDescription)) {
                itemTableStatus.setBackground(context.getResources().getDrawable(R.drawable.sh_review_bg));
            } else if (Constant.TableStatus_CH.TAKE_EFFECT.equals(data.getPending().taskDescription)) {
                itemTableStatus.setBackground(context.getResources().getDrawable(R.drawable.sh_end_bg));
                itemTableStatus.setTextColor(context.getResources().getColor(R.color.bgBtnGray));
            } else {
                itemTableStatus.setBackground(context.getResources().getDrawable(R.drawable.sh_edit_bg));
            }
        }
    }

}
