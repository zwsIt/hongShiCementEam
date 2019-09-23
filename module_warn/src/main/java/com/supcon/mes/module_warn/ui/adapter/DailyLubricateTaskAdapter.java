package com.supcon.mes.module_warn.ui.adapter;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.bean.DailyLubricateTaskEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DailyLubricateTaskAdapter extends BaseListDataRecyclerViewAdapter<DailyLubricateTaskEntity> {
    private OnScrollListener mOnScrollListener;
    private boolean editable;

    public DailyLubricateTaskAdapter(Context context, boolean isEditable) {
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
        }
        return new ViewHolder(context);
    }


    class TitleViewHolder extends BaseRecyclerViewHolder<DailyLubricateTaskEntity> {

        @BindByTag("itemIndex")
        TextView itemIndex;
        @BindByTag("dutyStaff")
        TextView dutyStaff;
        @BindByTag("eamNum")
        TextView eamNum;
        @BindByTag("receive")
        Button receive;
        @BindByTag("expend")
        ImageView expend;

        public TitleViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_daily_lubri_task_title;
        }

        @Override
        protected void initView() {
            super.initView();
            if (!editable) {
                receive.setVisibility(View.GONE);
            }
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    DailyLubricateTaskEntity item = getItem(getAdapterPosition());
                    if (item.viewType == ListType.TITLE.value()) {
                        Collection<DailyLubricateTaskEntity> values = item.lubricationMap.values();
                        List<DailyLubricateTaskEntity> valueList = new ArrayList(values);
                        if (item.isExpand) {
                            item.isExpand = false;
                            getList().removeAll(valueList);
                            notifyItemRangeRemoved(getAdapterPosition() + 1, valueList.size());
                            notifyItemRangeChanged(0, getItemCount(), valueList);
                            rotationExpandIcon(90, 0);
                        } else {
                            item.isExpand = true;
                            getList().addAll(getAdapterPosition() + 1, valueList);
                            notifyItemRangeInserted(getAdapterPosition() + 1, valueList.size());
                            notifyItemRangeChanged(0, getItemCount(), valueList);
                            rotationExpandIcon(0, 90);
                        }
                        mOnScrollListener.scrollTo(adapterPosition);
                    }
                }
            });
            receive.setOnClickListener(v -> onItemChildViewClick(receive, 0, getItem(getLayoutPosition())));
        }

        @SuppressLint("StringFormatMatches")
        @Override
        protected void update(DailyLubricateTaskEntity data) {
            String staff = String.format(context.getString(R.string.device_style13), "润滑责任人:"
                    , Util.strFormat2(data.getEamID().getInspectionStaff().name));
            dutyStaff.setText(HtmlParser.buildSpannedText(staff, new HtmlTagHandler()));
            String num = String.format(context.getString(R.string.device_style13), "设备数量:"
                    , data.lubricationMap.size());
            eamNum.setText(HtmlParser.buildSpannedText(num, new HtmlTagHandler()));
            itemIndex.setText(String.valueOf(data.position));

            if (getAdapterPosition() != 0) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, Util.dpToPx(context, 5), 0, 0);
                itemView.setLayoutParams(params);
            }
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        private void rotationExpandIcon(float from, float to) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);//属性动画
                valueAnimator.setDuration(500);
                valueAnimator.setInterpolator(new DecelerateInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        expend.setRotation((Float) valueAnimator.getAnimatedValue());
                    }
                });
                valueAnimator.start();
            }
        }
    }

    class ViewHolder extends BaseRecyclerViewHolder<DailyLubricateTaskEntity> {

        @BindByTag("eamName")
        TextView eamName;

        public ViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_daily_lubri_task;
        }

        @Override
        protected void initListener() {
            super.initListener();
            eamName.setOnClickListener(v -> onItemChildViewClick(eamName, 0, getItem(getAdapterPosition())));
        }

        @Override
        protected void update(DailyLubricateTaskEntity data) {
            String name = String.format(context.getString(R.string.device_style10)
                    , Util.strFormat2(data.getEamID().name), Util.strFormat2(data.getEamID().code));
            eamName.setText(HtmlParser.buildSpannedText(name, new HtmlTagHandler()));
        }
    }

    /**
     * 滚动监听接口
     */
    public interface OnScrollListener {
        void scrollTo(int pos);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }
}
