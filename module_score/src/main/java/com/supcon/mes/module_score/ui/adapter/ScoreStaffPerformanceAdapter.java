package com.supcon.mes.module_score.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Spanned;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.listener.OnTextListener;
import com.supcon.mes.mbap.view.CustomNumView;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_score.R;
import com.supcon.mes.module_score.model.bean.ScoreStaffPerformanceEntity;

import java.util.List;

public class ScoreStaffPerformanceAdapter extends BaseListDataRecyclerViewAdapter<ScoreStaffPerformanceEntity> {

    private boolean isEdit = false;
    private float total;

    public ScoreStaffPerformanceAdapter(Context context) {
        super(context);
    }

    public void setEditable(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public void updateTotal(float total) {
        this.total = total;
    }

    @Override
    protected BaseRecyclerViewHolder<ScoreStaffPerformanceEntity> getViewHolder(int viewType) {
        if (viewType == ListType.TITLE.value()) {
            return new TitleViewHolder(context);
        }
        return new ViewHolder(context);
    }

    @Override
    public int getItemViewType(int position, ScoreStaffPerformanceEntity scoreEamPerformanceEntity) {
        return scoreEamPerformanceEntity.viewType;
    }

    class TitleViewHolder extends BaseRecyclerViewHolder<ScoreStaffPerformanceEntity> {

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
        protected void update(ScoreStaffPerformanceEntity data) {
            contentTitle.setText(data.project);
            fraction.setText(Util.big0(data.fraction) + "分");
        }
    }

    class ViewHolder extends BaseRecyclerViewHolder<ScoreStaffPerformanceEntity> {

        @BindByTag("itemIndex")
        TextView itemIndex;
        @BindByTag("scoreItem")
        TextView scoreItem;

        @BindByTag("scoreRadioGroup")
        RadioGroup scoreRadioGroup;
        @BindByTag("scoreRadioBtn1")
        RadioButton scoreRadioBtn1;
        @BindByTag("scoreRadioBtn2")
        RadioButton scoreRadioBtn2;

        @BindByTag("sum")
        CustomNumView sum;


        public ViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_score_performance_content;
        }

        @Override
        protected void initView() {
            super.initView();
            sum.setEnabled(isEdit);
            sum.getNumViewInput().setEnabled(false);
            scoreRadioBtn1.setEnabled(isEdit);
            scoreRadioBtn2.setEnabled(isEdit);
        }

        @Override
        protected void initListener() {
            super.initListener();
            scoreRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (scoreRadioBtn1.isPressed() || scoreRadioBtn2.isPressed()) {
                        ScoreStaffPerformanceEntity item = getItem(getLayoutPosition());
                        item.result = !item.result;
                        float oldTotal = total - item.scoreEamPerformanceEntity.fraction;
                        if (!item.result) {
                            item.scoreEamPerformanceEntity.setTotalHightScore(item.scoreEamPerformanceEntity.getTotalHightScore() + item.itemScore);
                            if (item.scoreEamPerformanceEntity.getTotalHightScore() >= 0) {
                                item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.getTotalHightScore();
                            }
                        } else {
                            item.scoreEamPerformanceEntity.setTotalHightScore(item.scoreEamPerformanceEntity.getTotalHightScore() - item.itemScore);
                            item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.fraction - item.itemScore;
                        }
                        if (item.scoreEamPerformanceEntity.fraction < 0) {
                            item.scoreEamPerformanceEntity.fraction = 0;
                        } else if (item.scoreEamPerformanceEntity.fraction > item.scoreEamPerformanceEntity.score) {
                            item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.score;
                        }
                        List<ScoreStaffPerformanceEntity> list = getList();
                        int position = list.indexOf(item.scoreEamPerformanceEntity);
                        notifyItemChanged(position);
                        //更新总分数
                        item.scoreEamPerformanceEntity.scoreNum = oldTotal + item.scoreEamPerformanceEntity.fraction;
                        onItemChildViewClick(scoreRadioGroup, 0, item.scoreEamPerformanceEntity);
                    }
                }
            });
            sum.setTextListener(new OnTextListener() {
                @Override
                public void onText(String text) {
                    ScoreStaffPerformanceEntity item = getItem(getLayoutPosition());
                    if (Util.strToInt(text) == item.defaultNumVal) {
                        return;
                    }
                    float oldTotal = total - item.scoreEamPerformanceEntity.fraction;
                    float score = item.itemScore * (Util.strToInt(text) - item.defaultNumVal);
                    item.scoreEamPerformanceEntity.setTotalHightScore(item.scoreEamPerformanceEntity.getTotalHightScore() - score);
                    if (item.scoreEamPerformanceEntity.getTotalHightScore() >= 0) {
                        item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.getTotalHightScore();
                    } else {
                        item.scoreEamPerformanceEntity.fraction = 0;
                    }

                    if (item.scoreEamPerformanceEntity.fraction > item.scoreEamPerformanceEntity.score) {
                        item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.score;
                    }

                    item.defaultNumVal = Util.strToInt(text);
                    List<ScoreStaffPerformanceEntity> list = getList();
                    int position = list.indexOf(item.scoreEamPerformanceEntity);
                    notifyItemChanged(position);
                    //更新总分数
                    item.scoreEamPerformanceEntity.scoreNum = oldTotal + item.scoreEamPerformanceEntity.fraction;
                    onItemChildViewClick(scoreRadioGroup, 0, item.scoreEamPerformanceEntity);
                }
            });
        }

        @SuppressLint({"StringFormatMatches", "SetTextI18n"})
        @Override
        protected void update(ScoreStaffPerformanceEntity data) {
            itemIndex.setText(data.Index + ".");
            Spanned item = HtmlParser.buildSpannedText(String.format(context.getString(R.string.device_style12), data.item, Util.big0(data.itemScore)), new HtmlTagHandler());
            scoreItem.setText(item);
            scoreRadioGroup.setVisibility(View.VISIBLE);
            sum.setVisibility(View.VISIBLE);

            if (data.isEdit()) {
                scoreRadioGroup.setVisibility(View.GONE);
                sum.setNum(data.defaultNumVal);
            } else {
                sum.setVisibility(View.GONE);
            }
            scoreRadioBtn1.setText(data.isItemValue);
            scoreRadioBtn2.setText(data.noItemValue);
            scoreRadioBtn1.setChecked(data.result);
            scoreRadioBtn2.setChecked(!data.result);
        }

    }
}
