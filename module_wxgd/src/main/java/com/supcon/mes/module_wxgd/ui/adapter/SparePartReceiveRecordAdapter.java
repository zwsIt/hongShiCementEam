package com.supcon.mes.module_wxgd.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.model.bean.SparePartsConsumeEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/3
 * ------------- Description -------------
 * 备件领用记录Adapter
 */
public class SparePartReceiveRecordAdapter extends BaseListDataRecyclerViewAdapter<SparePartsConsumeEntity> {
    public SparePartReceiveRecordAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SparePartsConsumeEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SparePartsConsumeEntity> {


        @BindByTag("itemSparePartReceiveTv")
        CustomTextView itemSparePartReceiveTv;

        @BindByTag("itemSpareReceiveModelTv")
        CustomTextView itemSpareReceiveModelTv;

        @BindByTag("itemSpareReceiveEamTv")
        CustomTextView itemSpareReceiveEamTv;

        @BindByTag("itemSpareReceiveUsageTv")
        CustomTextView itemSpareReceiveUsageTv;

        @BindByTag("itemSpareReceiveStaffTv")
        CustomTextView itemSpareReceiveStaffTv;


        public ViewHolder(Context context) {
            super(context);
        }


        @Override
        protected int layoutId() {
            return R.layout.item_spare_receive_record;
        }

        @Override
        protected void update(SparePartsConsumeEntity entity) {
            String eam = String.format(context.getString(R.string.device_style10), Util.strFormat(entity.getProductID().productName)
                    , Util.strFormat(entity.getProductID().productCode));
            itemSparePartReceiveTv.setContent(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()).toString());

            String model = String.format(context.getString(R.string.device_style10), Util.strFormat(entity.getProductID().productSpecif)
                    , Util.strFormat(entity.getProductID().productModel));
            itemSpareReceiveModelTv.setContent(HtmlParser.buildSpannedText(model, new HtmlTagHandler()).toString());

            itemSpareReceiveEamTv.setContent(Util.strFormat(entity.getWorkList().getEamID().name));

            itemSpareReceiveStaffTv.setContent(Util.strFormat(entity.getReceiptor().name));

            itemSpareReceiveUsageTv.setContent(Util.big(entity.useQuantity));

        }
    }
}
