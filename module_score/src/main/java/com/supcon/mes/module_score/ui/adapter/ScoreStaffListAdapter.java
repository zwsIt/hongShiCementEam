package com.supcon.mes.module_score.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_score.R;
import com.supcon.mes.module_score.model.bean.ScoreEamEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffEntity;

public class ScoreStaffListAdapter extends BaseListDataRecyclerViewAdapter<ScoreStaffEntity> {
    private String title;

    public ScoreStaffListAdapter(String title, Context context) {
        super(context);
        this.title = title;
    }

    @Override
    protected BaseRecyclerViewHolder<ScoreStaffEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<ScoreStaffEntity> {

        @BindByTag("itemScoreStaff")
        CustomTextView itemScoreStaff;
        @BindByTag("itemScoreNum")
        CustomTextView itemScoreNum;
        @BindByTag("itemScoreTime")
        CustomTextView itemScoreTime;


        public ViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_staff_score;
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(v -> onItemChildViewClick(itemView, 0, getItem(getLayoutPosition())));
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void update(ScoreStaffEntity data) {
            String staff = String.format(context.getString(R.string.device_style10), Util.strFormat(data.getPatrolWorker().name)
                    , Util.strFormat(data.getPatrolWorker().code));
            itemScoreStaff.contentView().setText(HtmlParser.buildSpannedText(staff, new HtmlTagHandler()));
            itemScoreStaff.setKey(title);
            itemScoreNum.setKey(title + "得分");
            itemScoreNum.setValue(Util.big(data.score));
            itemScoreTime.setValue(data.scoreData != null ? DateUtil.dateFormat(data.scoreData) : "");
        }
    }
}
