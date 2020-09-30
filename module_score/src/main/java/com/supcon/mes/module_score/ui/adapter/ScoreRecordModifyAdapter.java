package com.supcon.mes.module_score.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.DealInfoEntity;
import com.supcon.mes.module_score.model.bean.ScoreRecordEntity;

import java.util.List;

/**
 * 单据处理意见
 */
public class ScoreRecordModifyAdapter extends BaseListDataRecyclerViewAdapter<ScoreRecordEntity> {

    public ScoreRecordModifyAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<ScoreRecordEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<ScoreRecordEntity> {

        @BindByTag("itemAreaDot")
        ImageView itemAreaDot;
        @BindByTag("itemAreaLineBottom")
        View itemAreaLineBottom;
        @BindByTag("itemAreaDotLayout")
        RelativeLayout itemAreaDotLayout;
        @BindByTag("dealStaff")
        TextView dealStaff;
        @BindByTag("activityName")
        TextView activityName;
        @BindByTag("dealAdvice")
        TextView dealAdvice;
        @BindByTag("dealTime")
        TextView dealTime;

        public ViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.middle_item_deal_info;
        }

        @Override
        protected void initListener() {
            super.initListener();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void update(ScoreRecordEntity data) {
            if (getAdapterPosition() == getListSize() - 1) {
                itemAreaLineBottom.setVisibility(View.INVISIBLE);
            }

            dealStaff.setText(data.getOperateStaffId().name);
//            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("进行了"+data.dealStaff +"操作，处理结果" + data.operateDes);
            String html = "得分 <font color='#366CBC'>"+data.getAfterScore() + "</font>";
            activityName.setText(Html.fromHtml(html));
//            if (TextUtils.isEmpty(data.dealAdvice)){
                dealAdvice.setVisibility(View.GONE);
//            }else {
//                dealAdvice.setVisibility(View.VISIBLE);
//                dealAdvice.setText(String.format("意见：%s", data.dealAdvice));
//            }
            dealTime.setText(DateUtil.dateFormat(data.getOperateTime(),Constant.TimeString.YEAR_MONTH_DAY_HOUR_MIN_SEC));
        }
    }
}
