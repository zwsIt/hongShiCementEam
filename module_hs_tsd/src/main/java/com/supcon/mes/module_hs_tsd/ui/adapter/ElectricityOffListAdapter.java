package com.supcon.mes.module_hs_tsd.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.supcon.mes.module_hs_tsd.IntentRouter;
import com.supcon.mes.module_hs_tsd.R;
import com.supcon.mes.module_hs_tsd.model.bean.ElectricityOffOnEntity;

import java.util.concurrent.TimeUnit;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/11
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class ElectricityOffListAdapter extends BaseListDataRecyclerViewAdapter<ElectricityOffOnEntity> {

    public ElectricityOffListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<ElectricityOffOnEntity> getViewHolder(int viewType) {
        return new WorkTicketViewHolder(context);
    }

    private class WorkTicketViewHolder extends BaseRecyclerViewHolder<ElectricityOffOnEntity> {


        @BindByTag("itemTableNo")
        CustomTextView itemTableNo;
        @BindByTag("itemTableStatus")
        TextView itemTableStatus;
        @BindByTag("itemTableLayout")
        RelativeLayout itemTableLayout;
        @BindByTag("itemEam")
        TextView itemEam;
        @BindByTag("itemApplyStaff")
        TextView itemApplyStaff;

        public WorkTicketViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_elec_off;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        ElectricityOffOnEntity workTicketEntity = getItem(getAdapterPosition());
                        PendingEntity pendingEntity = workTicketEntity.getPending();
                        if (pendingEntity == null){
                            ToastUtils.show(context,"代办为空,请刷新");
                            return;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constant.IntentKey.TABLE_ID, workTicketEntity.getId());
                        if (workTicketEntity.getPending().id == null){ // 无代办、生效
                            IntentRouter.go(context,Constant.Router.HS_ELE_OFF_VIEW,bundle);
                        }else {
                            bundle.putLong(Constant.IntentKey.PENDING_ID, workTicketEntity.getPending().id);
                            switch (workTicketEntity.getPending().taskDescription) {
                                case Constant.TableStatus_CH.ELE_OFF:
                                    IntentRouter.go(context,Constant.Router.HS_ELE_OFF_EDIT,bundle);
                                    break;
                                case Constant.TableStatus_CH.REVIEW:
                                case Constant.TableStatus_CH.REVIEW1:
                                    bundle.putBoolean(Constant.IntentKey.IS_EDITABLE,true);
                                case Constant.TableStatus_CH.TAKE_EFFECT:
                                default:
                                    IntentRouter.go(context,Constant.Router.HS_ELE_OFF_VIEW,bundle);
                                    break;
                            }
                        }

                    });
        }

        @Override
        protected void update(ElectricityOffOnEntity data) {
            itemTableNo.setContent(data.getTableNo());
            itemTableStatus.setText(data.getPending().taskDescription);
            itemEam.setText(TextUtils.isEmpty(data.getEamID().name) ? "" : String.format("%s(%s)", data.getEamID().name, data.getEamID().code));
            itemApplyStaff.setText(data.getApplyStaff().name);
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
