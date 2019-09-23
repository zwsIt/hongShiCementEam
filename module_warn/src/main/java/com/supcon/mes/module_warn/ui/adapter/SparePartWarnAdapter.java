package com.supcon.mes.module_warn.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.bean.SparePartWarnEntity;

import java.text.SimpleDateFormat;


/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class SparePartWarnAdapter extends BaseListDataRecyclerViewAdapter<SparePartWarnEntity> {

    private int checkPosition = -1;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public SparePartWarnAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SparePartWarnEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SparePartWarnEntity> {

        @BindByTag("itemSpareEquipmentNameTv")
        CustomTextView itemSpareEquipmentNameTv;
        @BindByTag("itemSpareProductNameTv")
        CustomTextView itemSpareProductNameTv;
        @BindByTag("itemSpareSpecifModelTv")
        CustomTextView itemSpareSpecifModelTv;
        @BindByTag("itemSpareAttachEamTv")
        CustomTextView itemSpareAttachEamTv;

        @BindByTag("itemSpareSpecifLastDateTv")
        CustomTextView itemSpareSpecifLastDateTv;
        @BindByTag("itemSpareSpecifNextDateTv")
        CustomTextView itemSpareSpecifNextDateTv;
        @BindByTag("itemSpareLastDurationTv")
        CustomTextView itemSpareLastDurationTv;
        @BindByTag("itemSpareNextDurationTv")
        CustomTextView itemSpareNextDurationTv;

        @BindByTag("itemMaintenanceMemoTv")
        CustomTextView itemMaintenanceMemoTv;

        @BindByTag("chkBox")
        CheckBox chkBox;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_sparepart_warn;
        }

        @Override
        protected void initListener() {
            super.initListener();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SparePartWarnEntity item = getItem(getAdapterPosition());
                    if (item == null) {
                        return;
                    }
                    item.isCheck = !item.isCheck;
                    notifyItemChanged(getAdapterPosition());
                    if (checkPosition != -1) {
                        if (checkPosition != getAdapterPosition()) {
                            SparePartWarnEntity item1 = getItem(checkPosition);
                            item1.isCheck = false;
                        }
                        notifyItemChanged(checkPosition);
                    }
                    checkPosition = getAdapterPosition();
                    onItemChildViewClick(itemView, checkPosition, getItem(checkPosition));
                }
            });
        }

        @Override
        protected void update(SparePartWarnEntity data) {
            String eam = String.format(context.getString(R.string.device_style10), Util.strFormat(data.getEamID().name)
                    , Util.strFormat(data.getEamID().code));
            itemSpareEquipmentNameTv.contentView().setText(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()));

            String product = String.format(context.getString(R.string.device_style10), Util.strFormat(data.getProductID().productName)
                    , Util.strFormat(data.getProductID().productCode));
            itemSpareProductNameTv.contentView().setText(HtmlParser.buildSpannedText(product, new HtmlTagHandler()));

            String specifModel = String.format(context.getString(R.string.device_style10), Util.strFormat(data.getProductID().productSpecif)
                    , Util.strFormat(data.getProductID().productModel));
            itemSpareSpecifModelTv.contentView().setText(HtmlParser.buildSpannedText(specifModel, new HtmlTagHandler()));

            if (!TextUtils.isEmpty(data.getAccessoryEamId().getAttachEamId().name)) {
                itemSpareAttachEamTv.setVisibility(View.VISIBLE);
                itemSpareAttachEamTv.setContent(data.getAccessoryEamId().getAttachEamId().name);
            } else {
                itemSpareAttachEamTv.setVisibility(View.GONE);
            }

            itemSpareLastDurationTv.setVisibility(View.GONE);
            itemSpareNextDurationTv.setVisibility(View.GONE);
            itemSpareSpecifLastDateTv.setVisibility(View.GONE);
            itemSpareSpecifNextDateTv.setVisibility(View.GONE);
            if (data.isDuration()) {
                itemSpareLastDurationTv.setVisibility(View.VISIBLE);
                itemSpareNextDurationTv.setVisibility(View.VISIBLE);
                itemSpareLastDurationTv.setValue(Util.big2(data.lastDuration));
                itemSpareNextDurationTv.setValue(Util.big2(data.nextDuration));
                if (data.currentDuration > data.nextDuration) {
                    itemSpareNextDurationTv.setContentTextColor(context.getResources().getColor(R.color.customRed));
                } else {
                    itemSpareNextDurationTv.setContentTextColor(context.getResources().getColor(R.color.textColorGray));
                }
            } else {
                itemSpareSpecifLastDateTv.setVisibility(View.VISIBLE);
                itemSpareSpecifNextDateTv.setVisibility(View.VISIBLE);
                itemSpareSpecifLastDateTv.setValue(data.lastTime != null ? dateFormat.format(data.lastTime) : "");
                itemSpareSpecifNextDateTv.setValue(data.nextTime != null ? dateFormat.format(data.nextTime) : "");
                long currentTime = System.currentTimeMillis();
                if (data.nextTime < currentTime) {
                    itemSpareSpecifNextDateTv.setContentTextColor(context.getResources().getColor(R.color.customRed));
                } else {
                    itemSpareSpecifNextDateTv.setContentTextColor(context.getResources().getColor(R.color.textColorGray));
                }
            }

            if (!TextUtils.isEmpty(data.spareMemo)) {
                itemMaintenanceMemoTv.setVisibility(View.VISIBLE);
                itemMaintenanceMemoTv.setContent(data.spareMemo);
            }
            if (data.isCheck) {
                chkBox.setChecked(true);
            } else {
                chkBox.setChecked(false);
            }
        }
    }
}
