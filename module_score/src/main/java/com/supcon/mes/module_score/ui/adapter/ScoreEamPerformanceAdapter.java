package com.supcon.mes.module_score.ui.adapter;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.middleware.util.EditInputFilter;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_score.R;
import com.supcon.mes.module_score.model.bean.ScoreEamPerformanceEntity;
import com.supcon.mes.middleware.ui.view.FlowLayout;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Predicate;

public class ScoreEamPerformanceAdapter extends BaseListDataRecyclerViewAdapter<ScoreEamPerformanceEntity> {

    private boolean isEdit = false;
    private float total;

    public ScoreEamPerformanceAdapter(Context context) {
        super(context);
    }

    public void setEditable(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public void updateTotal(float total) {
        this.total = total;
    }

    @Override
    protected BaseRecyclerViewHolder<ScoreEamPerformanceEntity> getViewHolder(int viewType) {
        if (viewType == ListType.TITLE.value()) {
            return new TitleViewHolder(context);
        } else if (viewType == ListType.HEADER.value()) {
            return new HeadViewHolder(context);
        }
        return new ViewHolder(context);
    }

    @Override
    public int getItemViewType(int position, ScoreEamPerformanceEntity scoreEamPerformanceEntity) {
        return scoreEamPerformanceEntity.viewType;
    }

    class TitleViewHolder extends BaseRecyclerViewHolder<ScoreEamPerformanceEntity> {

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

        @SuppressLint("StringFormatMatches")
        @Override
        protected void update(ScoreEamPerformanceEntity data) {
            contentTitle.setText(data.scoreStandard);
            fraction.setText(Util.big0(data.getTotalScore()) + "分");
        }
    }

    class ViewHolder extends BaseRecyclerViewHolder<ScoreEamPerformanceEntity> implements CompoundButton.OnCheckedChangeListener {

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

        @BindByTag("checkLayout")
        FlowLayout checkLayout;


        public ViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_score_performance_content;
        }

        @Override
        protected void initListener() {
            super.initListener();
            scoreRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (scoreRadioBtn1.isPressed() || scoreRadioBtn2.isPressed()) {
                        ScoreEamPerformanceEntity item = getItem(getLayoutPosition());
                        item.result = !item.result;
                        float oldTotal = total - item.scoreEamPerformanceEntity.getTotalScore();
                        if (item.result) {
                            item.scoreEamPerformanceEntity.setTotalHightScore(item.scoreEamPerformanceEntity.getTotalHightScore() + item.score);
                            if (item.scoreEamPerformanceEntity.getTotalHightScore() >= 0) {
                                item.scoreEamPerformanceEntity.setTotalScore(item.scoreEamPerformanceEntity.getTotalHightScore());
                            }
                        } else {
                            item.scoreEamPerformanceEntity.setTotalHightScore(item.scoreEamPerformanceEntity.getTotalHightScore() - item.score);
                            item.scoreEamPerformanceEntity.setTotalScore(item.scoreEamPerformanceEntity.getTotalScore() - item.score);
                        }
                        if (item.scoreEamPerformanceEntity.getTotalScore() < 0) {
                            item.scoreEamPerformanceEntity.setTotalScore(0f);
                        } else if (item.scoreEamPerformanceEntity.getTotalScore() > item.scoreEamPerformanceEntity.defaultTotalScore) {
                            item.scoreEamPerformanceEntity.setTotalScore(item.scoreEamPerformanceEntity.defaultTotalScore);
                        }
                        List<ScoreEamPerformanceEntity> list = getList();
                        int position = list.indexOf(item.scoreEamPerformanceEntity);
                        notifyItemChanged(position);
                        //更新总分数
                        item.scoreEamPerformanceEntity.scoreNum = oldTotal + item.scoreEamPerformanceEntity.getTotalScore();
                        onItemChildViewClick(scoreRadioGroup, 0, item.scoreEamPerformanceEntity);
                    }
                }
            });
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void update(ScoreEamPerformanceEntity data) {
            itemIndex.setText(data.Index + ".");
            scoreItem.setText(data.itemDetail + (data.marks.size() > 0 ? "" : "(" + Util.big0(data.score) + ")"));
            checkLayout.removeAllViews();
            scoreRadioGroup.setVisibility(View.VISIBLE);
            checkLayout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(data.isItemValue) && !TextUtils.isEmpty(data.noItemValue)) {
                scoreRadioBtn1.setText(data.isItemValue);
                scoreRadioBtn2.setText(data.noItemValue);
                scoreRadioBtn1.setChecked(data.result);
                scoreRadioBtn2.setChecked(!data.result);
                checkLayout.setVisibility(View.GONE);
            } else {
                scoreRadioGroup.setVisibility(View.GONE);
                addview(context, checkLayout, data.marks, data.marksState);
            }
            scoreRadioBtn1.setEnabled(isEdit);
            scoreRadioBtn2.setEnabled(isEdit);
        }

        @SuppressLint("CheckResult")
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ScoreEamPerformanceEntity item = getItem(getLayoutPosition());
            Boolean result = item.marksState.get(buttonView.getText().toString());
            result = !result;
            item.marksState.put(buttonView.getText().toString(), result);
            float oldTotal = total - item.scoreEamPerformanceEntity.getTotalScore();

            if (result) {
                item.scoreEamPerformanceEntity.setTotalHightScore(item.scoreEamPerformanceEntity.getTotalHightScore() - item.marks.get(buttonView.getText().toString()));
                item.scoreEamPerformanceEntity.setTotalScore(item.scoreEamPerformanceEntity.getTotalScore() - item.marks.get(buttonView.getText().toString()));
            } else {
                item.scoreEamPerformanceEntity.setTotalHightScore(item.scoreEamPerformanceEntity.getTotalHightScore() + item.marks.get(buttonView.getText().toString()));
                if (item.scoreEamPerformanceEntity.getTotalHightScore() >= 0) {
                    item.scoreEamPerformanceEntity.setTotalScore(item.scoreEamPerformanceEntity.getTotalHightScore());
                }
            }
            if (item.scoreEamPerformanceEntity.getTotalScore() < 0) {
                item.scoreEamPerformanceEntity.setTotalScore(0f);
            } else if (item.scoreEamPerformanceEntity.getTotalScore() > item.scoreEamPerformanceEntity.defaultTotalScore) {
                item.scoreEamPerformanceEntity.setTotalScore(item.scoreEamPerformanceEntity.defaultTotalScore);
            }
            List<ScoreEamPerformanceEntity> list = getList();
            int position = list.indexOf(item.scoreEamPerformanceEntity);
            notifyItemChanged(position);
            //更新总分数
            item.scoreEamPerformanceEntity.scoreNum = oldTotal + item.scoreEamPerformanceEntity.getTotalScore();
            onItemChildViewClick(scoreRadioGroup, 0, item.scoreEamPerformanceEntity);
        }

        //动态添加视图
        public void addview(Context context, FlowLayout layout, Map<String, Float> marks, Map<String, Boolean> marksState) {
            int index = 0;
            for (Map.Entry<String, Float> mark : marks.entrySet()) {
                String ss = mark.getKey();
                CheckBox checkBox = new CheckBox(context);
                checkBox.setEnabled(isEdit);
                checkBox.setChecked(marksState.get(ss) != null ? marksState.get(ss) : false);
                setRaidBtnAttribute(context, checkBox, ss, index);
                layout.addView(checkBox);
                index++;
            }
        }

        @SuppressLint("ResourceType")
        private void setRaidBtnAttribute(Context context, CheckBox checkBox, String btnContent, int id) {
            if (null == checkBox) {
                return;
            }
            checkBox.setButtonDrawable(R.drawable.sl_checkbox_selector_small);
            checkBox.setTextColor(context.getResources().getColorStateList(R.drawable.tvbg_tag_item));
            checkBox.setId(id);
            checkBox.setText(btnContent);
            checkBox.setTextSize(14);
            checkBox.setGravity(Gravity.CENTER);
            checkBox.setPadding(Util.dpToPx(context, 5), Util.dpToPx(context, 5), Util.dpToPx(context, 5), Util.dpToPx(context, 5));
            checkBox.setOnCheckedChangeListener(this::onCheckedChanged);
            ViewGroup.MarginLayoutParams rlp = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            checkBox.setLayoutParams(rlp);
        }
    }

    private boolean isWrite;

    class HeadViewHolder extends BaseRecyclerViewHolder<ScoreEamPerformanceEntity> {

        @BindByTag("itemIndex")
        TextView itemIndex;
        @BindByTag("scoreItem")
        TextView scoreItem;
        @BindByTag("scoreRight")
        TextView scoreRight;
        @BindByTag("expend")
        ImageView expend;


        @BindByTag("titleLayout")
        RelativeLayout titleLayout;
        @BindByTag("timeLayout")
        LinearLayout timeLayout;
        @BindByTag("cumulativeRunTime")
        CustomEditText cumulativeRunTime;
        @BindByTag("cumulativeDownTime")
        CustomEditText cumulativeDownTime;


        public HeadViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_score_performance_head;
        }

        @Override
        protected void initView() {
            super.initView();
            EditInputFilter editInputFilter = new EditInputFilter(24);
            editInputFilter.setTip("停机时长不能大于24小时!");
            cumulativeDownTime.editText().setFilters(new InputFilter[]{editInputFilter});
            cumulativeRunTime.setEditable(false);
            cumulativeDownTime.setEditable(isEdit);
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            titleLayout.setOnClickListener(v -> {
                if (timeLayout.getVisibility() == View.VISIBLE) {
                    rotationExpandIcon(90, 0);
                    timeLayout.setVisibility(View.GONE);
                } else {
                    rotationExpandIcon(0, 90);
                    timeLayout.setVisibility(View.VISIBLE);
                }
            });

            RxTextView.textChanges(cumulativeDownTime.editText())
                    .skipInitialValue()
                    .filter(new Predicate<CharSequence>() {
                        @Override
                        public boolean test(CharSequence charSequence) throws Exception {
                            if (!isWrite) {
                                isWrite = true;
                                return false;
                            } else {
                                return isWrite;
                            }
                        }
                    })
                    .subscribe(charSequence -> {
                        ScoreEamPerformanceEntity item = getItem(getAdapterPosition());
                        item.totalRunTime = 24f;
                        if (!TextUtils.isEmpty(charSequence.toString())) {
                            float stopTime = new BigDecimal(charSequence.toString()).floatValue();
                            item.accidentStopTime = stopTime;
                            item.totalRunTime = 24 - stopTime;
                        } else if (item.accidentStopTime != null) {
                            item.accidentStopTime = 0f;
                        }
                        item.resultValue = item.totalRunTime / 24 * 100;
                        scoreRight.setText(Util.big(item.resultValue) + "%");
                        cumulativeRunTime.setContent(Util.big(item.totalRunTime));

                        if (item.accidentStopTime != null) {
                            float oldTotal = total - item.scoreEamPerformanceEntity.getTotalScore();

                            if (item.accidentStopTime > 0 && item.accidentStopTime <= 1) {
                                item.scoreEamPerformanceEntity.setTotalScore(item.scoreEamPerformanceEntity.defaultTotalScore - 5);
                            } else if (item.accidentStopTime > 1) {
                                item.scoreEamPerformanceEntity.setTotalScore(item.scoreEamPerformanceEntity.defaultTotalScore - 10);
                            } else if (item.accidentStopTime == 0) {
                                item.scoreEamPerformanceEntity.setTotalScore(item.scoreEamPerformanceEntity.defaultTotalScore);
                            }
                            List<ScoreEamPerformanceEntity> list = getList();
                            int position = list.indexOf(item.scoreEamPerformanceEntity);
                            notifyItemChanged(position);
                            //更新总分数
                            item.scoreEamPerformanceEntity.scoreNum = oldTotal + item.scoreEamPerformanceEntity.getTotalScore();
                            onItemChildViewClick(cumulativeDownTime, 0, item.scoreEamPerformanceEntity);
                        }
                    });
        }

        @SuppressLint({"SetTextI18n", "StringFormatInvalid", "StringFormatMatches"})
        @Override
        protected void update(ScoreEamPerformanceEntity data) {
            itemIndex.setText(data.Index + ".");
            scoreItem.setText(data.itemDetail + (data.marks.size() > 0 ? "" : "(" + data.score + ")"));
            scoreRight.setText(Util.big(data.resultValue) + "%");

            cumulativeRunTime.setContent(data.totalRunTime == null ? "24" : Util.big(data.totalRunTime));
            isWrite = false;
            cumulativeDownTime.setContent(Util.big(data.accidentStopTime));
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        private void rotationExpandIcon(float from, float to) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);//属性动画
                valueAnimator.setDuration(500);
                valueAnimator.setInterpolator(new DecelerateInterpolator());
                valueAnimator.addUpdateListener(valueAnimator1 -> expend.setRotation((Float) valueAnimator1.getAnimatedValue()));
                valueAnimator.start();
            }
        }
    }
}
