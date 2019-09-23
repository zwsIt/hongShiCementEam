package com.supcon.mes.module_score.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Spanned;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_score.R;
import com.supcon.mes.module_score.model.bean.ScoreDutyEamEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffPerformanceEntity;

import java.util.List;

public class ScoreStaffEamAdapter extends BaseListDataRecyclerViewAdapter<ScoreDutyEamEntity> {

    public ScoreStaffEamAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<ScoreDutyEamEntity> getViewHolder(int viewType) {
        if (viewType == ListType.TITLE.value()) {
            return new TitleViewHolder(context);
        }
        return new ViewHolder(context);
    }

    @Override
    public int getItemViewType(int position, ScoreDutyEamEntity scoreDutyEamEntity) {
        return scoreDutyEamEntity.viewType;
    }

    class TitleViewHolder extends BaseRecyclerViewHolder<ScoreDutyEamEntity> {

        @BindByTag("contentTitle")
        TextView contentTitle;
        @BindByTag("fraction")
        TextView fraction;

        public TitleViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_score_performance_title;
        }

        @Override
        protected void initView() {
            super.initView();
        }

        @Override
        protected void initListener() {
            super.initListener();

        }

        @SuppressLint({"StringFormatMatches", "SetTextI18n"})
        @Override
        protected void update(ScoreDutyEamEntity data) {
            contentTitle.setText(data.name);
            fraction.setText(Util.big0(data.avgScore) + "分");
        }
    }

    class ViewHolder extends BaseRecyclerViewHolder<ScoreDutyEamEntity> {

        @BindByTag("itemIndex")
        TextView itemIndex;
        @BindByTag("scoreEam")
        TextView scoreEam;
        @BindByTag("scoreResult")
        TextView scoreResult;


        public ViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_score_eam_content;
        }


        @SuppressLint({"StringFormatMatches", "SetTextI18n"})
        @Override
        protected void update(ScoreDutyEamEntity data) {
            itemIndex.setText(getLayoutPosition() + ".");
            scoreEam.setText(String.format(context.getString(R.string.device_style1), data.name, data.code));
            scoreResult.setText(Util.big0(data.score) + "分");
        }

    }
}
