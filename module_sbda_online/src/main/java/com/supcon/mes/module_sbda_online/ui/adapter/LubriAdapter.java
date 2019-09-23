package com.supcon.mes.module_sbda_online.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_sbda_online.R;
import com.supcon.mes.module_sbda_online.model.bean.LubriEntity;

import java.text.SimpleDateFormat;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class LubriAdapter extends BaseListDataRecyclerViewAdapter<LubriEntity> {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public LubriAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<LubriEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<LubriEntity> {

        @BindByTag("itemLubricationOilTv")
        CustomTextView itemLubricationOilTv;
        @BindByTag("itemLubricationChangeTv")
        TextView itemLubricationChangeTv;
        @BindByTag("itemLubricationNumTv")
        TextView itemLubricationNumTv;
        @BindByTag("itemLubricationPartTv")
        CustomTextView itemLubricationPartTv;
        @BindByTag("itemLubricationAttachEamTv")
        CustomTextView itemLubricationAttachEamTv;
        @BindByTag("itemLubricationSparePartIdTv")
        CustomTextView itemLubricationSparePartIdTv;

        @BindByTag("itemLubricationLastDateTv")
        CustomTextView itemLubricationLastDateTv;
        @BindByTag("itemLubricationNextDateTv")
        CustomTextView itemLubricationNextDateTv;
        @BindByTag("itemLubricationLastDurationTv")
        CustomTextView itemLubricationLastDurationTv;
        @BindByTag("itemLubricationNextDurationTv")
        CustomTextView itemLubricationNextDurationTv;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_lubri;
        }

        @Override
        protected void update(LubriEntity data) {

            itemLubricationOilTv.setValue(data.getLubricateOil().name);
            itemLubricationChangeTv.setText(String.format(context.getString(R.string.device_style1), "加/换油:", Util.strFormat(data.getOilType().value)));
            itemLubricationNumTv.setText(String.format(context.getString(R.string.device_style1), "用量:", Util.big2(data.sum)));
            itemLubricationPartTv.setValue(Util.strFormat(data.lubricatePart));

            if (!TextUtils.isEmpty(data.getAccessoryEamId().getAttachEamId().code)) {
                itemLubricationAttachEamTv.setVisibility(View.VISIBLE);
                itemLubricationAttachEamTv.setContent(data.getAccessoryEamId().getAttachEamId().code);
            }
            if (!TextUtils.isEmpty(data.getSparePartId().getProductID().productCode)) {
                itemLubricationSparePartIdTv.setVisibility(View.VISIBLE);
                itemLubricationSparePartIdTv.setContent(data.getSparePartId().getProductID().productCode);
            }

            if (data.isDuration()) {
                itemLubricationLastDurationTv.setVisibility(View.VISIBLE);
                itemLubricationNextDurationTv.setVisibility(View.VISIBLE);
                itemLubricationLastDurationTv.setValue(Util.big2(data.lastDuration));
                itemLubricationNextDurationTv.setValue(Util.big2(data.nextDuration));
            } else {
                itemLubricationLastDateTv.setVisibility(View.VISIBLE);
                itemLubricationNextDateTv.setVisibility(View.VISIBLE);
                itemLubricationLastDateTv.setValue(data.lastTime != null ? dateFormat.format(data.lastTime) : "");
                itemLubricationNextDateTv.setValue(data.nextTime != null ? dateFormat.format(data.nextTime) : "");
            }

        }
    }
}
