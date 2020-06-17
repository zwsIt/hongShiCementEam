package com.supcon.mes.module_warn.ui.adapter;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.constant.WarnConstant;
import com.supcon.mes.module_warn.model.bean.DailyLubricateTaskEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * @author zhangwenshuai 2020/5/21
 * 计划润滑(日常润滑预警)
 */
public class PlanLubricationListAdapter extends BaseListDataRecyclerViewAdapter<DailyLubricateTaskEntity> {
    private boolean editable;
    private Map<String, Boolean> permission;

    public PlanLubricationListAdapter(Context context, boolean isEditable) {
        super(context);
        this.editable = isEditable;
    }

    @Override
    public int getItemViewType(int position, DailyLubricateTaskEntity lubricationWarnEntity) {
        return lubricationWarnEntity.viewType;
    }

    @Override
    protected BaseRecyclerViewHolder<DailyLubricateTaskEntity> getViewHolder(int viewType) {
        if (viewType == ListType.TITLE.value()) {
            return new TitleViewHolder(context);
        }else if (viewType == ListType.HEADER.value()){
            return new ButtonViewHolder(context);
        }
        return new ViewHolder(context);
    }

    public void setPermissionBtn(Map<String, Boolean> result) {
        permission = result;
    }


    class TitleViewHolder extends BaseRecyclerViewHolder<DailyLubricateTaskEntity> {

        @BindByTag("itemIndex")
        TextView itemIndex;
        @BindByTag("eam")
        TextView eam;
        @BindByTag("expend")
        ImageView expend;

        public TitleViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.warn_item_plan_lubrication_title;
        }

        @Override
        protected void initView() {
            super.initView();
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemChildViewClick(itemView,getAdapterPosition(),getItem(getAdapterPosition()));
//                    mOnScrollListener.scrollTo(adapterPosition);
                }
            });
//            receive.setOnClickListener(v -> onItemChildViewClick(receive, 0, getItem(getLayoutPosition())));
        }

        @SuppressLint("StringFormatMatches")
        @Override
        protected void update(DailyLubricateTaskEntity data) {
            eam.setText(String.format("%s(%s)", data.getEamID().name, data.getEamID().eamAssetCode));
            itemIndex.setText(String.valueOf(data.position));
            if (data.isExpand){
                expend.setImageResource(R.drawable.ic_shrink);
            }else {
                expend.setImageResource(R.drawable.ic_expand);
            }
        }
    }
    class ButtonViewHolder extends BaseRecyclerViewHolder<DailyLubricateTaskEntity> {

        @BindByTag("finish")
        Button finish;
        @BindByTag("delay")
        Button delay;
        @BindByTag("btnLayout")
        LinearLayout btnLayout;

        public ButtonViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.warn_item_button_operate;
        }

        @Override
        protected void initView() {
            super.initView();
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemChildViewClick(itemView,getAdapterPosition(),getItem(getAdapterPosition()));
                }
            });

            RxView.clicks(finish).throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            onItemChildViewClick(finish,getAdapterPosition(),getItem(getAdapterPosition()));
                        }
                    });
            RxView.clicks(delay).throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            onItemChildViewClick(delay,getAdapterPosition(),getItem(getAdapterPosition()));
                        }
                    });
        }

        @SuppressLint("StringFormatMatches")
        @Override
        protected void update(DailyLubricateTaskEntity data) {
            for (String operateCode : permission.keySet()){
                switch (operateCode){
                    case WarnConstant.OperateCode.FINISH:
                        if (!permission.get(operateCode).booleanValue()){
                            finish.setVisibility(View.GONE);
                        }
                        break;
                    case WarnConstant.OperateCode.PLAN_DELAY_SET:
                        if (!permission.get(operateCode).booleanValue()){
                            delay.setVisibility(View.GONE);
                        }
                        break;
                }
            }
        }
    }
    class ViewHolder extends BaseRecyclerViewHolder<DailyLubricateTaskEntity> {

        @BindByTag("itemLubriOilTv")
        CustomTextView itemLubriOilTv;
        @BindByTag("itemLubriChangeTv")
        TextView itemLubriChangeTv;
        @BindByTag("itemLubriNumTv")
        TextView itemLubriNumTv;
        @BindByTag("itemLubriPartTv")
        CustomTextView itemLubriPartTv;
        @BindByTag("itemLubriNextTime")
        CustomTextView itemLubriNextTime;

        @BindByTag("chkBox")
        CheckBox chkBox;

        public ViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_daily_lubri_part;
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(v -> {
                DailyLubricateTaskEntity data = getItem(getAdapterPosition());
                data.isCheck = !data.isCheck;
                notifyItemChanged(getAdapterPosition());
            });
        }

        @Override
        protected void update(DailyLubricateTaskEntity data) {
            itemLubriOilTv.setContent(data.getLubricateOil().name);
            itemLubriChangeTv.setText(String.format(context.getString(R.string.device_style1), "加/换油:", Util.strFormat(data.getOilType().value)));
            itemLubriNumTv.setText(String.format(context.getString(R.string.device_style1), "点数:", String.valueOf(data.lubricatingnumber)));
            itemLubriPartTv.setValue(Util.strFormat(data.lubricatePart));
            itemLubriNextTime.setContent(data.nextTime != null ? DateUtil.dateFormat(data.nextTime) : "--");
            if (data.isCheck) {
                chkBox.setChecked(true);
            } else {
                chkBox.setChecked(false);
            }
        }
    }
}
