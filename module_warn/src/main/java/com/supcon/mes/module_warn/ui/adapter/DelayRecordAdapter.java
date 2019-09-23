package com.supcon.mes.module_warn.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.bean.DelayRecordEntity;
import com.supcon.mes.module_warn.model.bean.LubricationWarnEntity;

import java.text.SimpleDateFormat;


/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class DelayRecordAdapter extends BaseListDataRecyclerViewAdapter<DelayRecordEntity> {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public DelayRecordAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<DelayRecordEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<DelayRecordEntity> {

        @BindByTag("itemEquipmentNameTv")
        CustomTextView itemEquipmentNameTv;
        @BindByTag("itemDelayAfterTv")
        CustomTextView itemDelayAfterTv;
        @BindByTag("itemDelayBeforeTv")
        CustomTextView itemDelayBeforeTv;
        @BindByTag("itemDelayReasonTv")
        CustomTextView itemDelayReasonTv;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_delay_record;
        }

        @Override
        protected void initListener() {
            super.initListener();
        }

        @Override
        protected void update(DelayRecordEntity data) {
            String eam = String.format(context.getString(R.string.device_style10), Util.strFormat(data.getDelayEamId().name)
                    , Util.strFormat(data.getDelayEamId().code));
            itemEquipmentNameTv.contentView().setText(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()));

            itemDelayAfterTv.setValue(data.afterDelDate != null ? dateFormat.format(data.afterDelDate) : "");
            itemDelayBeforeTv.setValue(data.beforeDelDate != null ? dateFormat.format(data.beforeDelDate) : "");

            itemDelayReasonTv.setContent(Util.strFormat(data.delayReason));
        }
    }
}
