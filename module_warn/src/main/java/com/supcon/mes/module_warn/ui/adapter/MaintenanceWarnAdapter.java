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
import com.supcon.mes.module_warn.model.bean.MaintenanceWarnEntity;

import java.text.SimpleDateFormat;


/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class MaintenanceWarnAdapter extends BaseListDataRecyclerViewAdapter<MaintenanceWarnEntity> {

    private int checkPosition = -1;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public MaintenanceWarnAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<MaintenanceWarnEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<MaintenanceWarnEntity> {

        @BindByTag("itemMaintenanceEquipmentNameTv")
        CustomTextView itemMaintenanceEquipmentNameTv;
        @BindByTag("itemMaintenanceAttachEamTv")
        CustomTextView itemMaintenanceAttachEamTv;
        @BindByTag("itemMaintenanceSparePartIdTv")
        CustomTextView itemMaintenanceSparePartIdTv;
        @BindByTag("itemMaintenanceLastDateTv")
        CustomTextView itemMaintenanceLastDateTv;
        @BindByTag("itemMaintenanceNextDateTv")
        CustomTextView itemMaintenanceNextDateTv;
        @BindByTag("itemMaintenanceLastDurationTv")
        CustomTextView itemMaintenanceLastDurationTv;
        @BindByTag("itemMaintenanceNextDurationTv")
        CustomTextView itemMaintenanceNextDurationTv;
        @BindByTag("itemMaintenanceClaimTv")
        CustomTextView itemMaintenanceClaimTv;
        @BindByTag("itemMaintenanceContentTv")
        CustomTextView itemMaintenanceContentTv;

        @BindByTag("chkBox")
        CheckBox chkBox;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_maintenance_warn;
        }

        @Override
        protected void initListener() {
            super.initListener();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaintenanceWarnEntity item = getItem(getAdapterPosition());
                    if (item == null) {
                        return;
                    }
                    item.isCheck = !item.isCheck;
                    notifyItemChanged(getAdapterPosition());
                    if (checkPosition != -1) {
                        if (checkPosition != getAdapterPosition()) {
                            MaintenanceWarnEntity item1 = getItem(checkPosition);
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
        protected void update(MaintenanceWarnEntity data) {
            String eam = String.format(context.getString(R.string.device_style10), Util.strFormat(data.getEamID().name)
                    , Util.strFormat(data.getEamID().code));
            itemMaintenanceEquipmentNameTv.contentView().setText(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()));

            if (!TextUtils.isEmpty(data.getAccessoryEamId().getAttachEamId().name)) {
                itemMaintenanceAttachEamTv.setVisibility(View.VISIBLE);
                itemMaintenanceAttachEamTv.setContent(data.getAccessoryEamId().getAttachEamId().name);
            } else {
                itemMaintenanceAttachEamTv.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(data.getSparePartId().getProductID().productName)) {
                itemMaintenanceSparePartIdTv.setVisibility(View.VISIBLE);
                itemMaintenanceSparePartIdTv.setContent(data.getSparePartId().getProductID().productName);
            } else {
                itemMaintenanceSparePartIdTv.setVisibility(View.GONE);
            }

            itemMaintenanceLastDateTv.setVisibility(View.GONE);
            itemMaintenanceNextDateTv.setVisibility(View.GONE);
            itemMaintenanceLastDurationTv.setVisibility(View.GONE);
            itemMaintenanceNextDurationTv.setVisibility(View.GONE);
            if (data.isDuration()) {
                itemMaintenanceLastDurationTv.setVisibility(View.VISIBLE);
                itemMaintenanceNextDurationTv.setVisibility(View.VISIBLE);
                itemMaintenanceLastDurationTv.setValue(Util.big2(data.lastDuration));
                itemMaintenanceNextDurationTv.setValue(Util.big2(data.nextDuration));
                if (data.currentDuration > data.nextDuration) {
                    itemMaintenanceNextDurationTv.setContentTextColor(context.getResources().getColor(R.color.customRed));
                } else {
                    itemMaintenanceNextDurationTv.setContentTextColor(context.getResources().getColor(R.color.textColorGray));
                }
            } else {
                itemMaintenanceLastDateTv.setVisibility(View.VISIBLE);
                itemMaintenanceNextDateTv.setVisibility(View.VISIBLE);
                itemMaintenanceLastDateTv.setValue(data.lastTime != null ? dateFormat.format(data.lastTime) : "");
                itemMaintenanceNextDateTv.setValue(data.nextTime != null ? dateFormat.format(data.nextTime) : "");
                long currentTime = System.currentTimeMillis();
                if (data.nextTime < currentTime) {
                    itemMaintenanceNextDateTv.setContentTextColor(context.getResources().getColor(R.color.customRed));
                } else {
                    itemMaintenanceNextDateTv.setContentTextColor(context.getResources().getColor(R.color.textColorGray));
                }
            }
            if (!TextUtils.isEmpty(data.claim)) {
                itemMaintenanceClaimTv.setVisibility(View.VISIBLE);
                itemMaintenanceClaimTv.setContent(data.claim);
            } else {
                itemMaintenanceClaimTv.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(data.content)) {
                itemMaintenanceContentTv.setVisibility(View.VISIBLE);
                itemMaintenanceContentTv.setContent(data.content);
            } else {
                itemMaintenanceContentTv.setVisibility(View.GONE);
            }

            if (data.isCheck) {
                chkBox.setChecked(true);
            } else {
                chkBox.setChecked(false);
            }
        }
    }
}
