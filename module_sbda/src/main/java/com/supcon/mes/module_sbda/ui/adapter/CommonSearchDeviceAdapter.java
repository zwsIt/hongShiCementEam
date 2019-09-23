package com.supcon.mes.module_sbda.ui.adapter;

import android.content.Context;
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

public class CommonSearchDeviceAdapter extends BaseListDataRecyclerViewAdapter<CommonDeviceEntity>  {

    public CommonSearchDeviceAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<CommonDeviceEntity> getViewHolder(int viewType) {
        return new CommonSearchDeviceAdapter.MultiDeviceViewHolder(context);
    }

    class MultiDeviceViewHolder extends BaseRecyclerViewHolder<CommonDeviceEntity> implements View.OnClickListener{

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
        }

        @Override
        protected void initListener() {
            super.initListener();

            itemView.setOnClickListener(v -> {
                itemAddDeviceCheckBox.setChecked(!itemAddDeviceCheckBox.isChecked());
                onClick(v);
            });

            itemAddDeviceName.setOnChildViewClickListener(new OnChildViewClickListener() {
                @Override
                public void onChildViewClick(View childView, int action, Object obj) {
                    itemAddDeviceCheckBox.setChecked(!itemAddDeviceCheckBox.isChecked());
                    onClick(childView);
                }
            });

            itemAddDevicePlace.setOnChildViewClickListener(new OnChildViewClickListener() {
                @Override
                public void onChildViewClick(View childView, int action, Object obj) {
                    itemAddDeviceCheckBox.setChecked(!itemAddDeviceCheckBox.isChecked());
                    onClick(childView);
                }
            });

//            itemAddDeviceCheckBox.setOnCheckedListener(isTagChecked -> {
//                int position = getAdapterPosition();
//                CommonDeviceEntity commonDeviceEntity = getItem(position);
//                onItemChildViewClick(itemAddDeviceCheckBox, isTagChecked?1:0, commonDeviceEntity);
//            });


        }

        @Override
        protected int layoutId() {
            return R.layout.item_add_device;
        }

        @Override
        protected void update(CommonDeviceEntity data){
            itemAddDeviceName.setText(String.format(context.getResources().getString(R.string.device), data.eamName, data.eamCode));
            itemAddDevicePlace.setText(data.installPlace);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            CommonDeviceEntity commonDeviceEntity = getItem(position);
            onItemChildViewClick(v, itemAddDeviceCheckBox.isChecked()?1:0, commonDeviceEntity);
        }
    }


}
