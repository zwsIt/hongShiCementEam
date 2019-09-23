package com.supcon.mes.module_sbda_online.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_sbda_online.IntentRouter;
import com.supcon.mes.module_sbda_online.R;
import com.supcon.mes.module_sbda_online.model.bean.SparePartEntity;

import java.text.SimpleDateFormat;


/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class SparePartAdapter extends BaseListDataRecyclerViewAdapter<SparePartEntity> {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public SparePartAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SparePartEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SparePartEntity> {

        @BindByTag("itemSparePartNameTv")
        CustomTextView itemSparePartNameTv;
        @BindByTag("itemSparePartNumTv")
        CustomTextView itemSparePartNumTv;
        @BindByTag("itemSparePartStandingCropTv")
        CustomTextView itemSparePartStandingCropTv;
        @BindByTag("itemSparePartModelSpecifTv")
        CustomTextView itemSparePartModelSpecifTv;
        @BindByTag("itemSparePartAttachEamTv")
        CustomTextView itemSparePartAttachEamTv;
        @BindByTag("itemSparePartLastDateTv")
        CustomTextView itemSparePartLastDateTv;
        @BindByTag("itemSparePartNextDateTv")
        CustomTextView itemSparePartNextDateTv;
        @BindByTag("itemSparePartLastDurationTv")
        CustomTextView itemSparePartLastDurationTv;
        @BindByTag("itemSparePartNextDurationTv")
        CustomTextView itemSparePartNextDurationTv;
        @BindByTag("itemSparePartMemoTv")
        CustomTextView itemSparePartMemoTv;
        @BindByTag("itemSparePartDeviceBtn")
        Button itemSparePartDeviceBtn;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_spare_part;
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemSparePartDeviceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SparePartEntity item = getItem(getAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putLong(Constant.IntentKey.SPARE_PART_LEDGER_ID, item.getProductID().id);
                    IntentRouter.go(context, Constant.Router.SPARE_PART_LEDGER, bundle);
                }
            });
        }

        @Override
        protected void update(SparePartEntity data) {

            if (EamApplication.isHongshi()) {
                itemSparePartStandingCropTv.setVisibility(View.VISIBLE);
            }
            String eam = String.format(context.getString(R.string.device_style10), Util.strFormat(data.getProductID().productName)
                    , Util.strFormat(data.getProductID().productCode));
            itemSparePartNameTv.contentView().setText(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()));

            itemSparePartNumTv.setValue(Util.big2(data.depleteSum) + Util.strFormat2(data.getProductID().getProductBaseUnit().name));
            itemSparePartStandingCropTv.setValue(Util.big0(data.standingCrop) + Util.strFormat2(data.getProductID().getProductBaseUnit().name));
            if (TextUtils.isEmpty(data.getProductID().productModel) && TextUtils.isEmpty(data.getProductID().productSpecif)) {
                itemSparePartModelSpecifTv.setVisibility(View.GONE);
            } else {
                String modelSpecif = String.format(context.getString(R.string.device_style10), Util.strFormat(data.getProductID().productModel)
                        , Util.strFormat(data.getProductID().productSpecif));
                itemSparePartModelSpecifTv.contentView().setText(HtmlParser.buildSpannedText(modelSpecif, new HtmlTagHandler()));
            }

            if (!TextUtils.isEmpty(data.getAccessoryEamId().getAttachEamId().code)) {
                itemSparePartAttachEamTv.setVisibility(View.VISIBLE);
                itemSparePartAttachEamTv.setContent(data.getAccessoryEamId().getAttachEamId().code);
            }

            if (data.isDuration()) {
                itemSparePartLastDurationTv.setVisibility(View.VISIBLE);
                itemSparePartNextDurationTv.setVisibility(View.VISIBLE);
                itemSparePartLastDurationTv.setValue(Util.big2(data.lastDuration));
                itemSparePartNextDurationTv.setValue(Util.big2(data.nextDuration));
            } else {
                itemSparePartLastDateTv.setVisibility(View.VISIBLE);
                itemSparePartNextDateTv.setVisibility(View.VISIBLE);
                itemSparePartLastDateTv.setValue(data.lastTime != null ? dateFormat.format(data.lastTime) : "");
                itemSparePartNextDateTv.setValue(data.nextTime != null ? dateFormat.format(data.nextTime) : "");
            }

            if (!TextUtils.isEmpty(data.spareMemo)) {
                itemSparePartMemoTv.setVisibility(View.VISIBLE);
                itemSparePartMemoTv.setValue(Util.strFormat(data.spareMemo));
            }
        }
    }
}
