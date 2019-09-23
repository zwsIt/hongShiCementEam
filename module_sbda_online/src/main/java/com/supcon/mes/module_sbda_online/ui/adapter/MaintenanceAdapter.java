package com.supcon.mes.module_sbda_online.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_sbda_online.R;
import com.supcon.mes.module_sbda_online.model.bean.MaintenanceEntity;

import java.text.SimpleDateFormat;


/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class MaintenanceAdapter extends BaseListDataRecyclerViewAdapter<MaintenanceEntity> {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public MaintenanceAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<MaintenanceEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<MaintenanceEntity> {

        @BindByTag("itemMaintenanceLastDateTv")
        CustomTextView itemMaintenanceLastDateTv;
        @BindByTag("itemMaintenanceNextDateTv")
        CustomTextView itemMaintenanceNextDateTv;
        @BindByTag("itemMaintenanceLastDurationTv")
        CustomTextView itemMaintenanceLastDurationTv;
        @BindByTag("itemMaintenanceNextDurationTv")
        CustomTextView itemMaintenanceNextDurationTv;
        @BindByTag("itemMaintenanceAttachEamTv")
        CustomTextView itemMaintenanceAttachEamTv;
        @BindByTag("itemMaintenanceSparePartIdTv")
        CustomTextView itemMaintenanceSparePartIdTv;

        @BindByTag("itemMaintenanceClaimTv")
        CustomTextView itemMaintenanceClaimTv;
        @BindByTag("itemMaintenanceContentTv")
        CustomTextView itemMaintenanceContentTv;
        @BindByTag("itemMaintenanceMemoTv")
        CustomTextView itemMaintenanceMemoTv;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_maintenance;
        }


        @Override
        protected void update(MaintenanceEntity data) {

            if (data.isDuration()) {
                itemMaintenanceLastDurationTv.setVisibility(View.VISIBLE);
                itemMaintenanceNextDurationTv.setVisibility(View.VISIBLE);
                itemMaintenanceLastDurationTv.setValue(Util.big2(data.lastDuration));
                itemMaintenanceNextDurationTv.setValue(Util.big2(data.nextDuration));
            } else {
                itemMaintenanceLastDateTv.setVisibility(View.VISIBLE);
                itemMaintenanceNextDateTv.setVisibility(View.VISIBLE);
                itemMaintenanceLastDateTv.setValue(data.lastTime != null ? dateFormat.format(data.lastTime) : "");
                itemMaintenanceNextDateTv.setValue(data.nextTime != null ? dateFormat.format(data.nextTime) : "");
            }

            if (!TextUtils.isEmpty(data.getAccessoryEamId().getAttachEamId().code)) {
                itemMaintenanceAttachEamTv.setVisibility(View.VISIBLE);
                itemMaintenanceAttachEamTv.setContent(data.getAccessoryEamId().getAttachEamId().code);
            }
            if (!TextUtils.isEmpty(data.getSparePartId().getProductID().productCode)) {
                itemMaintenanceSparePartIdTv.setVisibility(View.VISIBLE);
                itemMaintenanceSparePartIdTv.setContent(data.getSparePartId().getProductID().productCode);
            }

            itemMaintenanceClaimTv.setValue(data.claim);
            itemMaintenanceContentTv.setValue(data.content);

            if (!TextUtils.isEmpty(data.remark)) {
                itemMaintenanceMemoTv.setVisibility(View.VISIBLE);
                itemMaintenanceMemoTv.setValue(Util.strFormat(data.remark));
            }
        }
    }
}
