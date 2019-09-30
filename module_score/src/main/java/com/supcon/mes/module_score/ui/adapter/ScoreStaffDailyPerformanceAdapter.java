package com.supcon.mes.module_score.ui.adapter;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.github.lzyzsd.circleprogress.Utils;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.middleware.ui.view.FlowLayout;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_score.R;
import com.supcon.mes.module_score.model.bean.ScoreStaffDailyPerformanceEntity;

import java.util.Map;
import java.util.Set;

public class ScoreStaffDailyPerformanceAdapter extends BaseListDataRecyclerViewAdapter<ScoreStaffDailyPerformanceEntity> {

    private boolean isEdit = false;
    private float total;

    public ScoreStaffDailyPerformanceAdapter(Context context) {
        super(context);
    }

    public void setEditable(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public void updateTotal(float total) {
        this.total = total;
    }

    @Override
    protected BaseRecyclerViewHolder<ScoreStaffDailyPerformanceEntity> getViewHolder(int viewType) {
        if (viewType == ListType.TITLE.value()) {
            return new TitleViewHolder(context);
        }
        return new ViewHolder(context);
    }

    @Override
    public int getItemViewType(int position, ScoreStaffDailyPerformanceEntity scoreEamPerformanceEntity) {
        return scoreEamPerformanceEntity.viewType;
    }

    class TitleViewHolder extends BaseRecyclerViewHolder<ScoreStaffDailyPerformanceEntity> {

        @BindByTag("contentTitle")
        TextView contentTitle;

        public TitleViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_score_daily_performance_title;
        }

        @Override
        protected void initView() {
            super.initView();
        }

        @SuppressLint({"StringFormatMatches", "SetTextI18n"})
        @Override
        protected void update(ScoreStaffDailyPerformanceEntity data) {
            Spanned item = HtmlParser.buildSpannedText(String.format(context.getString(R.string.device_style12), data.basicPerformance, data.scoreText), new HtmlTagHandler());
            contentTitle.setText(item);
        }
    }

    class ViewHolder extends BaseRecyclerViewHolder<ScoreStaffDailyPerformanceEntity> {

        @BindByTag("itemIndex")
        TextView itemIndex;
        @BindByTag("scoreItem")
        TextView scoreItem;
        @BindByTag("checkStandard")
        TextView checkStandard;
        @BindByTag("dockPointsLayout")
        LinearLayout dockPointsLayout;

        @BindByTag("expend")
        ImageView expend;

        @BindByTag("titleLayout")
        RelativeLayout titleLayout;


        public ViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_score_daily_performance_content;
        }


        @Override
        protected void initListener() {
            super.initListener();
            titleLayout.setOnClickListener(v -> {
                if (checkStandard.getVisibility() == View.VISIBLE) {
                    rotationExpandIcon(90, 0);
                    checkStandard.setVisibility(View.GONE);
                } else {
                    rotationExpandIcon(0, 90);
                    checkStandard.setVisibility(View.VISIBLE);
                }
            });
        }

        @SuppressLint({"StringFormatMatches", "SetTextI18n"})
        @Override
        protected void update(ScoreStaffDailyPerformanceEntity data) {
            itemIndex.setText(data.Index + ".");
            scoreItem.setText(data.basicPerDetail);
            dockPointsLayout.removeAllViews();
            addview(context, dockPointsLayout, data.dockPointsMap);
            StringBuffer standardBuffer = new StringBuffer();
            for (String standard : data.checkStandardList) {
                standardBuffer.append("<br/>").append(standard);
            }
            String item = String.format(context.getString(R.string.device_style16), "考核标准:"
                    , standardBuffer.toString());
            checkStandard.setText(HtmlParser.buildSpannedText(item, new HtmlTagHandler()));
        }

        //动态添加视图
        public void addview(Context context, LinearLayout layout, Map<String, String> dockPointsMap) {
            for (Map.Entry<String, String> dockPoint : dockPointsMap.entrySet()) {
                String key = dockPoint.getKey();
                String value = dockPoint.getValue();
                if (!TextUtils.isEmpty(key)) {
                    RelativeLayout relativeLayout = new RelativeLayout(context);
                    RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.MarginLayoutParams.MATCH_PARENT, RelativeLayout.MarginLayoutParams.WRAP_CONTENT);
                    rlp.setMargins(0, 0, 0, 10);
                    relativeLayout.setLayoutParams(rlp);

                    TextView textView1 = new TextView(context);
                    relativeLayout.addView(textView1);
                    RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) textView1.getLayoutParams();
                    layoutParams1.setMargins(Util.dpToPx(context, 25), 0, 0, 10);
                    textView1.setLayoutParams(layoutParams1);
                    textView1.setText(key);
                    textView1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    textView1.setTextSize(14);
                    textView1.setTextColor(context.getResources().getColor(R.color.textColorblack));

                    TextView textView2 = new TextView(context);
                    relativeLayout.addView(textView2);
                    RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) textView2.getLayoutParams();
                    layoutParams2.setMargins(0, 0, Util.dpToPx(context, 15), 10);
                    layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    textView2.setLayoutParams(layoutParams2);
                    if (key.contains("设备运转率")) {
                        textView2.setText(Util.big2(Float.valueOf(value)));
                    } else {
                        textView2.setText(value + "分");
                    }
                    textView2.setTextSize(14);
                    textView2.setTextColor(context.getResources().getColor(R.color.textColorlightblack));
                    layout.addView(relativeLayout);
                }
            }
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
