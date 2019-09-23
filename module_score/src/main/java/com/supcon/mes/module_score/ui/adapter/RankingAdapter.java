package com.supcon.mes.module_score.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_score.R;
import com.supcon.mes.module_score.model.bean.ScoreStaffEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/31
 * ------------- Description -------------
 */
public class RankingAdapter extends BaseListDataRecyclerViewAdapter<ScoreStaffEntity> {

    private int rank = -1;

    public RankingAdapter(Context context) {
        super(context);
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    protected BaseRecyclerViewHolder<ScoreStaffEntity> getViewHolder(int viewType) {
        return new ContentViewHolder(context);
    }

    class ContentViewHolder extends BaseRecyclerViewHolder<ScoreStaffEntity> {

        @BindByTag("rankingLayout")
        RelativeLayout rankingLayout;
        @BindByTag("ranking")
        TextView ranking;
        @BindByTag("name")
        TextView name;
        @BindByTag("depot")
        TextView depot;

        @BindByTag("score")
        TextView score;


        public ContentViewHolder(Context context) {
            super(context);
        }


        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemChildViewClick(itemView, 0, getItem(getLayoutPosition()));
                }
            });
        }

        @Override
        protected int layoutId() {
            return R.layout.item_staff_ranking;
        }

        @Override
        protected void update(ScoreStaffEntity data) {
            rankingLayout.setBackground(context.getResources().getDrawable(R.drawable.ranking_stroke));
            ranking.setText("");
//            if (getAdapterPosition() == 0) {
//                ranking.setBackground(context.getResources().getDrawable(R.drawable.pic_no1));
//            } else if (getAdapterPosition() == 1) {
//                ranking.setBackground(context.getResources().getDrawable(R.drawable.pic_no2));
//            } else if (getAdapterPosition() == 2) {
//                ranking.setBackground(context.getResources().getDrawable(R.drawable.pic_no3));
//            } else {
            ranking.setTextColor(context.getResources().getColor(R.color.color_9f9f9f));
            ranking.setText(String.valueOf(getAdapterPosition() + 1));
            ranking.setBackground(null);
//            }
            name.setText(data.getPatrolWorker().name);
            depot.setText(Util.strFormat(data.getPatrolWorker().getMainPosition().getDepartment().name));
            score.setText(Util.big(data.score));
            if (rank == (getAdapterPosition() + 1)) {
//                if (rank > 3) {
                ranking.setTextColor(context.getResources().getColor(R.color.color_dd4351));
//                }
                rankingLayout.setBackground(context.getResources().getDrawable(R.drawable.ranking_stroke_select));
            }
        }
    }
}
