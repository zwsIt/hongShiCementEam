package com.supcon.mes.module_olxj.ui.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.model.bean.DeviceDCSEntity;
import com.supcon.mes.module_olxj.R;

import java.util.List;

/**
 * Created by wangshizhan on 2019/5/29
 * Email:wangshizhan@supcom.com
 */
public class DeviceDCSParamAdapter extends BaseListDataRecyclerViewAdapter<DeviceDCSEntity> {

    public DeviceDCSParamAdapter(Context context) {
        super(context);
    }

    public DeviceDCSParamAdapter(Context context, List<DeviceDCSEntity> list) {
        super(context, list);
    }

    @Override
    protected BaseRecyclerViewHolder<DeviceDCSEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }


    class ViewHolder extends BaseRecyclerViewHolder<DeviceDCSEntity> {

        @BindByTag("itemDCSParam")
        CustomTextView itemDCSParam;

        @BindByTag("itemDCSParamMax")
        CustomTextView itemDCSParamMax;

        @BindByTag("itemDCSParamMin")
        CustomTextView itemDCSParamMin;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_olxj_device_dcs_param;
        }

        @Override
        protected void initView() {
            super.initView();
            itemDCSParam.keyView().setMaxLines(2);
            itemDCSParam.contentView().setPadding(20, 0, 0, 0);
        }

        @Override
        protected void update(DeviceDCSEntity data) {
            itemDCSParam.setContentTextColor(context.getResources().getColor(R.color.customGreen));
            itemDCSParam.setKey(data.name);
//            itemDCSParam.setKey(String.format(context.getString(R.string.dcs_param), data.name, data.itemNumber));
            itemDCSParam.setContent(data.latestValue);

            if (!TextUtils.isEmpty(data.latestValue) && data.latestValue.contains("关")) {
                itemDCSParam.setContentTextColor(context.getResources().getColor(R.color.customRed));
            }

            if (!TextUtils.isEmpty(data.maxValue)) {
                itemDCSParamMax.setContent(data.maxValue);
                if (!TextUtils.isEmpty(data.latestValue) && !TextUtils.isEmpty(data.minValue)
                        && TextUtils.isDigitsOnly(data.latestValue) && TextUtils.isDigitsOnly(data.maxValue)) {
                    if (Long.parseLong(data.latestValue) > Long.parseLong(data.maxValue)) {
                        itemDCSParam.setContentTextColor(context.getResources().getColor(R.color.customRed));
                    }
                }
            }
            if (!TextUtils.isEmpty(data.minValue)) {
                itemDCSParamMin.setContent(data.minValue);
                if (!TextUtils.isEmpty(data.latestValue) && !TextUtils.isEmpty(data.minValue)
                        && TextUtils.isDigitsOnly(data.latestValue) && TextUtils.isDigitsOnly(data.minValue)) {
                    if (Long.parseLong(data.latestValue) < Long.parseLong(data.minValue)) {
                        itemDCSParam.setContentTextColor(context.getResources().getColor(R.color.customRed));
                    }
                }
            }
        }
    }

}
