package com.supcon.mes.module_sbda.ui.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.mes.mbap.view.CustomCheckBox;
import com.supcon.mes.mbap.view.CustomExpandableTextView;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.module_sbda.R;


/**
 * Created by wangshizhan on 2017/8/16.
 * Email:wangshizhan@supcon.com
 */

public class AddDeviceAdapter extends BaseListDataRecyclerViewAdapter<CommonDeviceEntity> {

    private volatile boolean isSingle = false;

    private SparseBooleanArray checkList;

    public AddDeviceAdapter(Context context, boolean isSingle) {
        super(context);
        this.isSingle = isSingle;
        if (!isSingle) {
            checkList = new SparseBooleanArray();
        }
    }

    public AddDeviceAdapter(Context context) {
        this(context, false);
    }


    public void setCheck(int position, boolean check) {
        checkList.put(position, check);
    }

    @Override
    protected BaseRecyclerViewHolder<CommonDeviceEntity> getViewHolder(int viewType) {

        if (isSingle)
            return new SingleDeviceViewHolder(context);
        return new MultiDeviceViewHolder(context);
    }

    class MultiDeviceViewHolder extends BaseRecyclerViewHolder<CommonDeviceEntity> implements View.OnClickListener {

        @BindByTag("itemAddDeviceName")
        CustomExpandableTextView itemAddDeviceName;

        @BindByTag("itemAddDeviceCheckBox")
        CustomCheckBox itemAddDeviceCheckBox;

        @BindByTag("itemAddDevicePlace")
        CustomExpandableTextView itemAddDevicePlace;

        MultiDeviceViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initBind() {
            super.initBind();
        }

        @Override
        protected void initView() {
            super.initView();
            itemAddDeviceCheckBox.setVisibility(View.VISIBLE);
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(v -> {
                itemAddDeviceCheckBox.setChecked(!itemAddDeviceCheckBox.isChecked());
                MultiDeviceViewHolder.this.onClick(v);
            });
            itemAddDeviceName.setOnChildViewClickListener((childView, action, obj) -> {
                itemAddDeviceCheckBox.setChecked(!itemAddDeviceCheckBox.isChecked());
                onClick(itemAddDeviceCheckBox);
            });

            itemAddDevicePlace.setOnChildViewClickListener((childView, action, obj) -> {
                itemAddDeviceCheckBox.setChecked(!itemAddDeviceCheckBox.isChecked());
                onClick(itemAddDeviceCheckBox);
            });

            itemAddDeviceCheckBox.setOnCheckedListener(isChecked -> {
                int position = getAdapterPosition();
                CommonDeviceEntity commonDeviceEntity = getItem(position);
                onItemChildViewClick(itemAddDeviceCheckBox, isChecked ? 1 : 0, commonDeviceEntity);
                checkList.put(position, isChecked);
            });


        }

        @Override
        protected int layoutId() {
            return R.layout.item_add_device;
        }

        @Override
        protected void update(CommonDeviceEntity data) {
            if (null != checkList && checkList.size() != 0) {
                Boolean isChecked = checkList.get(getAdapterPosition());
                itemAddDeviceCheckBox.setChecked(isChecked);
            }

            itemAddDeviceName.setText(String.format(context.getResources().getString(R.string.device), data.eamName, data.eamCode));
            itemAddDevicePlace.setText(data.installPlace);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            CommonDeviceEntity commonDeviceEntity = getItem(position);
            onItemChildViewClick(v, itemAddDeviceCheckBox.isChecked() ? 1 : 0, commonDeviceEntity);
        }
    }


    class SingleDeviceViewHolder extends BaseRecyclerViewHolder<CommonDeviceEntity> implements View.OnClickListener {

        @BindByTag("itemAddDeviceName")
        CustomExpandableTextView itemAddDeviceName;

        @BindByTag("itemAddDevicePlace")
        CustomExpandableTextView itemAddDevicePlace;

        SingleDeviceViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initBind() {
            super.initBind();
        }

        @Override
        protected void initView() {
            super.initView();
        }

        @Override
        protected void initListener() {
            super.initListener();

            itemView.setOnClickListener(v -> SingleDeviceViewHolder.this.onClick(v));
            itemAddDeviceName.setOnChildViewClickListener((childView, action, obj) -> onClick(childView));
            itemAddDevicePlace.setOnChildViewClickListener((childView, action, obj) -> onClick(childView));
        }

        @Override
        protected int layoutId() {
            return R.layout.item_add_device;
        }

        @Override
        protected void update(CommonDeviceEntity data) {
            itemAddDeviceName.setText(String.format(context.getResources().getString(R.string.device), data.eamName, data.eamCode));
            itemAddDevicePlace.setText(data.installPlace);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            CommonDeviceEntity commonDeviceEntity = getItem(position);
            onItemChildViewClick(v, 0, commonDeviceEntity);
            commonDeviceEntity.updateTime = System.currentTimeMillis();
            commonDeviceEntity.frequency += 1;
        }
    }
}
