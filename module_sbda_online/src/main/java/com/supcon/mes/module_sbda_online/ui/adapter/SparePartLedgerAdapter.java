package com.supcon.mes.module_sbda_online.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_sbda_online.R;
import com.supcon.mes.module_sbda_online.model.bean.SparePartsLedgerEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/3
 * ------------- Description -------------
 */
public class SparePartLedgerAdapter extends BaseListDataRecyclerViewAdapter<SparePartsLedgerEntity> {
    public SparePartLedgerAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SparePartsLedgerEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SparePartsLedgerEntity> {


        @BindByTag("itemSpareLedgerNameTv")
        CustomTextView itemSpareLedgerNameTv;

        @BindByTag("itemSpareLedgerModelTv")
        CustomTextView itemSpareLedgerModelTv;

        @BindByTag("itemSpareLedgerTypeTv")
        CustomTextView itemSpareLedgerTypeTv;

        @BindByTag("itemSpareLedgerFileStateTv")
        CustomTextView itemSpareLedgerFileStateTv;

        @BindByTag("eamStatus")
        TextView eamStatus;


        public ViewHolder(Context context) {
            super(context);
        }


        @Override
        protected int layoutId() {
            return R.layout.item_spare_ledger;           //获取列表自视图布局
        }

        @Override
        protected void update(SparePartsLedgerEntity entity) {
            String eam = String.format(context.getString(R.string.device_style10), Util.strFormat(entity.name)
                    , Util.strFormat(entity.code));
            itemSpareLedgerNameTv.contentView().setText(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()));

            itemSpareLedgerModelTv.setValue(entity.model);

            itemSpareLedgerTypeTv.setValue(entity.getEamType().name);
            itemSpareLedgerFileStateTv.setValue(entity.getFileState().value);

            if (TextUtils.isEmpty(entity.stateForDisplay)) {
                eamStatus.setVisibility(View.GONE);
            } else {
                int statusBackgroundRes = R.drawable.eam_status_use;
                eamStatus.setText(entity.stateForDisplay);
                String status = entity.state == null ? "" : entity.state;
                if (status.equals("01"))
                    statusBackgroundRes = R.drawable.eam_status_use;
                else if (status.equals("04"))
                    statusBackgroundRes = R.drawable.eam_status_drop;
                else if (status.equals("03"))
                    statusBackgroundRes = R.drawable.eam_status_delay;
                else if (status.equals("02"))
                    statusBackgroundRes = R.drawable.eam_status_stop;

                eamStatus.setBackgroundResource(statusBackgroundRes);
            }
        }
    }
}
