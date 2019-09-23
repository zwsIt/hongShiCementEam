package com.supcon.mes.module_wxgd.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.view.View;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
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
 */
public class SparePartConsumeLedgerAdapter extends BaseListDataRecyclerViewAdapter<SparePartsConsumeEntity> {
    public SparePartConsumeLedgerAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SparePartsConsumeEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SparePartsConsumeEntity> {


        @BindByTag("itemSparePartTv")
        CustomTextView itemSparePartTv;

        @BindByTag("itemSpareModelTv")
        CustomTextView itemSpareModelTv;

        @BindByTag("itemSpareUsageTv")
        CustomTextView itemSpareUsageTv;

        @BindByTag("itemSpareConsumptionTv")
        CustomTextView itemSpareConsumptionTv;

        @BindByTag("itemSpareTeamTv")
        CustomVerticalTextView itemSpareTeamTv;

        @BindByTag("itemSpareStaffTv")
        CustomVerticalTextView itemSpareStaffTv;

        @BindByTag("itemSpareTableNoTv")
        CustomTextView itemSpareTableNoTv;


        public ViewHolder(Context context) {
            super(context);
        }


        @Override
        protected int layoutId() {
            return R.layout.item_spare_consume_ledger;
        }

        @Override
        protected void initListener() {
            super.initListener();

            itemView.setOnClickListener(new CustomTextView(context) {
                @Override
                public void onClick(View v) {
                    onItemChildViewClick(itemSpareTableNoTv, 0, getItem(getAdapterPosition()));
                }
            });
        }

        @Override
        protected void update(SparePartsConsumeEntity entity) {
            String eam = String.format(context.getString(R.string.device_style10), Util.strFormat(entity.getProductID().productName)
                    , Util.strFormat(entity.getProductID().productCode));
            itemSparePartTv.setContent(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()).toString());

            String model = String.format(context.getString(R.string.device_style10), Util.strFormat(entity.getProductID().productSpecif)
                    , Util.strFormat(entity.getProductID().productModel));
            itemSpareModelTv.setContent(HtmlParser.buildSpannedText(model, new HtmlTagHandler()).toString());

            itemSpareUsageTv.setContent(Util.big(entity.useQuantity));
            itemSpareConsumptionTv.setContent(Util.big(entity.actualQuantity));

            itemSpareTeamTv.setContent(Util.strFormat(entity.getWorkList().getRepairGroup().name));
            itemSpareStaffTv.setContent(Util.strFormat(entity.getWorkList().getChargeStaff().name));
            itemSpareTableNoTv.setContent(Util.strFormat2(entity.getWorkList().tableNo));
            if (!TextUtils.isEmpty(entity.getWorkList().tableNo)) {
                itemSpareTableNoTv.getCustomValue().getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                itemSpareTableNoTv.getCustomValue().getPaint().setAntiAlias(true);//抗锯齿
            } else {
                itemSpareTableNoTv.getCustomValue().getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
            }
        }
    }
}
