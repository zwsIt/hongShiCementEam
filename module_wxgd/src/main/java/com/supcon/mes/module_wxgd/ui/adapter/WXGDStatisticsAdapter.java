package com.supcon.mes.module_wxgd.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wxgd.R;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/8/13
 * ------------- Description -------------
 */
public class WXGDStatisticsAdapter extends BaseListDataRecyclerViewAdapter<WXGDEntity> {
    public WXGDStatisticsAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<WXGDEntity> getViewHolder(int viewType) {
        return new WorkViewHolder(context);
    }

    class WorkViewHolder extends BaseRecyclerViewHolder<WXGDEntity> {
        @BindByTag("statisticsPosition")
        TextView statisticsPosition;
        @BindByTag("statisticsEam")
        TextView statisticsEam;
        @BindByTag("statisticsContent")
        TextView statisticsContent;
        @BindByTag("statisticsStaff")
        TextView statisticsStaff;
        @BindByTag("statisticsTime")
        TextView statisticsTime;

        public WorkViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_wxgd_statistics;
        }

        @Override
        protected void update(WXGDEntity data) {
            statisticsPosition.setText(String.valueOf(getAdapterPosition() + 1));

            String eam = String.format(context.getString(R.string.device_style10), Util.strFormat(data.eamID.name)
                    , Util.strFormat(data.eamID.code));
            statisticsEam.setText(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()));
            statisticsContent.setText(Util.strFormat2(data.workOrderContext));
            statisticsStaff.setText(Util.strFormat(data.getChargeStaff().name) );
            statisticsTime.setText(data.createTime != null ? DateUtil.dateFormat(data.createTime, "yyyy-MM-dd") : "");
        }

    }
}
