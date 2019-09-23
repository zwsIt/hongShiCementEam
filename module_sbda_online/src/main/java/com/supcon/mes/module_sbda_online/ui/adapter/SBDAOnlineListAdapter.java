package com.supcon.mes.module_sbda_online.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.EamPicController;
import com.supcon.mes.module_sbda_online.IntentRouter;
import com.supcon.mes.module_sbda_online.R;
import com.supcon.mes.module_sbda_online.model.bean.SBDAOnlineEntity;

/**
 * Environment: hongruijun
 * Created by Xushiyun on 2018/4/2.
 */

public class SBDAOnlineListAdapter extends BaseListDataRecyclerViewAdapter<SBDAOnlineEntity> {

    public SBDAOnlineListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SBDAOnlineEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SBDAOnlineEntity> implements View.OnClickListener {

        ImageView itemSBDADeviceIc;

        @BindByTag("itemSBDADeviceTitle")
        TextView itemSBDADeviceTitle;

        @BindByTag("itemSBDADeviceCode")
        TextView itemSBDADeviceCode;

        @BindByTag("itemSBDADeviceArea")
        TextView getItemSBDADeviceArea;

        @BindByTag("eamStatus")
        TextView eamStatus;

        @BindByTag("eamFraction")
        Button eamFraction;


        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initBind() {
            super.initBind();
        }

        @Override
        protected void initView() {
            super.initView();
            itemSBDADeviceIc = itemView.findViewById(R.id.itemSBDADeviceIc);
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(this);
            eamFraction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SBDAOnlineEntity item = getItem(getAdapterPosition());
                    if (item != null) {
                        SBDAOnlineEntity.ScoreMerity scoreMerity = item.getScoreMerity();
                        if (TextUtils.isEmpty(scoreMerity.scoreTableNo)) {
                            ToastUtils.show(context, "当前设备暂未评分!");
                            return;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.IntentKey.SCORETABLENO, scoreMerity.scoreTableNo);
                        bundle.putBoolean(Constant.IntentKey.isEdit, false);
                        IntentRouter.go(context, Constant.Router.SCORE_EAM_PERFORMANCE, bundle);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            onItemChildViewClick(v, position, getItem(position));
        }

        @Override
        protected int layoutId() {
            return R.layout.item_online_sbda;           //获取列表自视图布局
        }

        @Override
        protected void update(SBDAOnlineEntity entity) {
            itemSBDADeviceIc.setImageResource(R.drawable.ic_default_pic3);

            new EamPicController().initEamPic(itemSBDADeviceIc, entity.id);

            itemSBDADeviceTitle.setText(entity.name);
            itemSBDADeviceCode.setText("[" + entity.code + "]");
            getItemSBDADeviceArea.setText(entity.installPlace.name);

            eamFraction.setText(String.valueOf(Math.round(entity.score)));

            if (Math.round(entity.score) <= 85) {
                eamFraction.setBackgroundResource(R.drawable.red_btn);
            } else if (Math.round(entity.score) > 85 && Math.round(entity.score) <= 95) {
                eamFraction.setBackgroundResource(R.drawable.yellow_btn);
            } else if (Math.round(entity.score) > 95) {
                eamFraction.setBackgroundResource(R.drawable.green_btn);
            }

            if (TextUtils.isEmpty(entity.stateForDisplay)) {
                eamStatus.setVisibility(View.GONE);
            } else {
                int statusBackgroundRes = R.drawable.eam_status_use;
                eamStatus.setText(entity.stateForDisplay);
                String status = entity.state == null ? "" : entity.state;
                if (status.equals("01"))
                    statusBackgroundRes = R.drawable.eam_status_use;
                else if (status.equals("04"))
                    statusBackgroundRes = R.drawable.eam_status_drop;
                else if (status.equals("03"))
                    statusBackgroundRes = R.drawable.eam_status_delay;
                else if (status.equals("02"))
                    statusBackgroundRes = R.drawable.eam_status_stop;

                eamStatus.setBackgroundResource(statusBackgroundRes);
            }
        }
    }
}

