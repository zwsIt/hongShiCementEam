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
import com.supcon.mes.module_warn.model.bean.LubricationWarnEntity;

import java.text.SimpleDateFormat;


/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class LubricationWarnAdapter extends BaseListDataRecyclerViewAdapter<LubricationWarnEntity> {

    private int checkPosition = -1;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public LubricationWarnAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<LubricationWarnEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<LubricationWarnEntity> {

        @BindByTag("itemLubriEquipmentNameTv")
        CustomTextView itemLubriEquipmentNameTv;
        @BindByTag("itemLubriOilTv")
        CustomTextView itemLubriOilTv;
        @BindByTag("itemLubriChangeTv")
        TextView itemLubriChangeTv;
        @BindByTag("itemLubriNumTv")
        TextView itemLubriNumTv;
        @BindByTag("itemLubriPartTv")
        CustomTextView itemLubriPartTv;
        @BindByTag("itemLubriLastDateTv")
        CustomTextView itemLubriLastDateTv;
        @BindByTag("itemLubriNextDateTv")
        CustomTextView itemLubriNextDateTv;
        @BindByTag("itemLubriLastDurationTv")
        CustomTextView itemLubriLastDurationTv;
        @BindByTag("itemLubriNextDurationTv")
        CustomTextView itemLubriNextDurationTv;
        @BindByTag("itemLubriClaimTv")
        CustomTextView itemLubriClaimTv;
        @BindByTag("itemLubriContentTv")
        CustomTextView itemLubriContentTv;

        @BindByTag("itemLubriAttachEamTv")
        CustomTextView itemLubriAttachEamTv;
        @BindByTag("itemLubriSparePartIdTv")
        CustomTextView itemLubriSparePartIdTv;

        @BindByTag("chkBox")
        CheckBox chkBox;
        @BindByTag("lubriLayout")
        RelativeLayout lubriLayout;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_lubrication_warn;
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LubricationWarnEntity item = getItem(getAdapterPosition());
                    if (item == null) {
                        return;
                    }
                    item.isCheck = !item.isCheck;
                    notifyItemChanged(getAdapterPosition());
                    if (checkPosition != -1) {
                        if (checkPosition != getAdapterPosition()) {
                            LubricationWarnEntity item1 = getItem(checkPosition);
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
        protected void update(LubricationWarnEntity data) {
            String eam = String.format(context.getString(R.string.device_style10), Util.strFormat(data.getEamID().name)
                    , Util.strFormat(data.getEamID().code));
            itemLubriEquipmentNameTv.contentView().setText(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()));

            itemLubriOilTv.setContent(data.getLubricateOil().name);
            itemLubriChangeTv.setText(String.format(context.getString(R.string.device_style1), "加/换油:", Util.strFormat(data.getOilType().value)));
            itemLubriNumTv.setText(String.format(context.getString(R.string.device_style1), "用量:", Util.big2(data.sum)));
            
            itemLubriPartTv.setValue(Util.strFormat(data.lubricatePart));
            itemLubriLastDurationTv.setVisibility(View.GONE);
            itemLubriNextDurationTv.setVisibility(View.GONE);
            itemLubriLastDateTv.setVisibility(View.GONE);
            itemLubriNextDateTv.setVisibility(View.GONE);
            if (data.isDuration()) {
                itemLubriLastDurationTv.setVisibility(View.VISIBLE);
                itemLubriNextDurationTv.setVisibility(View.VISIBLE);
                itemLubriLastDurationTv.setValue(Util.big2(data.lastDuration));
                itemLubriNextDurationTv.setValue(Util.big2(data.nextDuration));
                if (data.currentDuration > data.nextDuration) {
                    itemLubriNextDurationTv.setContentTextColor(context.getResources().getColor(R.color.customRed));
                } else {
                    itemLubriNextDurationTv.setContentTextColor(context.getResources().getColor(R.color.textColorGray));
                }
            } else {
                itemLubriLastDateTv.setVisibility(View.VISIBLE);
                itemLubriNextDateTv.setVisibility(View.VISIBLE);
                itemLubriLastDateTv.setValue(data.lastTime != null ? dateFormat.format(data.lastTime) : "");
                itemLubriNextDateTv.setValue(data.nextTime != null ? dateFormat.format(data.nextTime) : "");
                long currentTime = System.currentTimeMillis();
                if (data.nextTime < currentTime) {
                    itemLubriNextDateTv.setContentTextColor(context.getResources().getColor(R.color.customRed));
                } else {
                    itemLubriNextDateTv.setContentTextColor(context.getResources().getColor(R.color.textColorGray));
                }
            }

            if (!TextUtils.isEmpty(data.getAccessoryEamId().getAttachEamId().name)) {
                itemLubriAttachEamTv.setVisibility(View.VISIBLE);
                itemLubriAttachEamTv.setContent(data.getAccessoryEamId().getAttachEamId().name);
            } else {
                itemLubriAttachEamTv.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(data.getSparePartId().getProductID().productName)) {
                itemLubriSparePartIdTv.setVisibility(View.VISIBLE);
                itemLubriSparePartIdTv.setContent(data.getSparePartId().getProductID().productName);
            } else {
                itemLubriSparePartIdTv.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(data.claim)) {
                itemLubriClaimTv.setVisibility(View.VISIBLE);
                itemLubriClaimTv.setContent(data.claim);
            }

            if (!TextUtils.isEmpty(data.content)) {
                itemLubriContentTv.setVisibility(View.VISIBLE);
                itemLubriContentTv.setContent(data.content);
            }

            if (data.isCheck) {
                chkBox.setChecked(true);
            } else {
                chkBox.setChecked(false);
            }
        }
    }
}
