package com.supcon.mes.module_main.ui.adaper;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.model.bean.AnomalyEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/24
 * ------------- Description -------------
 * 异常记录adapter
 */
public class AnomalyAdapter extends BaseListDataRecyclerViewAdapter<AnomalyEntity> {
    public AnomalyAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<AnomalyEntity> getViewHolder(int viewType) {
        return new ContentViewHolder(context);
    }


    class ContentViewHolder extends BaseRecyclerViewHolder<AnomalyEntity> {

        @BindByTag("anomalyTableNo")
        TextView anomalyTableNo;
        @BindByTag("anomalyState")
        TextView anomalyState;
        @BindByTag("anomalyStaff")
        TextView anomalyStaff;
        @BindByTag("anomalyTime")
        TextView anomalyTime;
        @BindByTag("anomalyContent")
        TextView anomalyContent;
        @BindByTag("anomalySoucretype")
        TextView anomalySoucretype;


        public ContentViewHolder(Context context) {
            super(context);
        }


        @Override
        protected int layoutId() {
            return R.layout.hs_item_anomaly;
        }

        @Override
        protected void update(AnomalyEntity data) {
            anomalyTableNo.setText(Util.strFormat2(data.tableno));
            anomalyState.setText(Util.strFormat2(data.state));
            anomalySoucretype.setText(data.soucretype);
            anomalyStaff.setText(String.format(context.getString(R.string.device_style6), "待办人:", Util.strFormat(data.staffname)));
            anomalyTime.setText(data.creatime != null ? DateUtil.dateFormat(data.creatime, "yyyy-MM-dd") : "");
            anomalyContent.setText(String.format(context.getString(R.string.device_style6), "内容:", Util.strFormat(data.content)));
            if (!TextUtils.isEmpty(data.state)) {
                if (data.state.equals("派工")) {
                    anomalyState.setTextColor(context.getResources().getColor(R.color.gray));
                } else if (data.state.equals("执行")) {
                    anomalyState.setTextColor(context.getResources().getColor(R.color.yellow));
                } else if (data.state.equals("验收")) {
                    anomalyState.setTextColor(context.getResources().getColor(R.color.blue));
                } else {
                    anomalyState.setTextColor(context.getResources().getColor(R.color.gray));
                }
            } else {
                anomalyState.setTextColor(context.getResources().getColor(R.color.gray));
            }
        }

    }

}
