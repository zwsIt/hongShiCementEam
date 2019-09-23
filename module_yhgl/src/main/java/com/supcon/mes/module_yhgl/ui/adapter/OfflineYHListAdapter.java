package com.supcon.mes.module_yhgl.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.beans.GalleryBean;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.EamPicController;
import com.supcon.mes.middleware.controller.OfflineCameraController;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.model.bean.YHEntityVo;
import com.supcon.mes.middleware.util.DeviceManager;
import com.supcon.mes.module_yhgl.IntentRouter;
import com.supcon.mes.module_yhgl.R;
import com.supcon.mes.middleware.util.FaultPicHelper;
import com.supcon.mes.module_yhgl.util.SystemCodeUtil;

import java.util.List;

import static com.supcon.mes.mbap.adapter.GalleryAdapter.FILE_TYPE_PICTURE;
import static com.supcon.mes.mbap.adapter.GalleryAdapter.FILE_TYPE_VIDEO;


/**
 * @author wangshizhan
 * @date 2017/8/16
 * Email:wangshizhan@supcon.com
 */

public class OfflineYHListAdapter extends BaseListDataRecyclerViewAdapter<YHEntityVo> {

    private EamPicController mEamPicController;

    public OfflineYHListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<YHEntityVo> getViewHolder(int viewType) {
        return new WorkViewHolder(context);
    }

    class WorkViewHolder extends BaseRecyclerViewHolder<YHEntityVo> implements View.OnClickListener {

        @BindByTag("itemTableNoTv")
        CustomTextView itemTableNoTv;

        @BindByTag("itemYHPersonTv")
        TextView itemYHPersonTv;

        @BindByTag("itemYHDateTv")
        TextView itemYHDateTv;

        @BindByTag("itemInfo")
        ImageView itemInfo;

        @BindByTag("itemYHDeviceName")
        CustomTextView itemYHDeviceName;

//        @BindByTag("itemYHDeviceIc")
        ImageView itemYHDeviceIc;

        @BindByTag("itemYHDeviceCode")
        CustomTextView itemYHDeviceCode;

        @BindByTag("itemYHDescription")
        CustomVerticalTextView itemYHDescription;

        @BindByTag("itemTableStatus")
        TextView itemTableStatus;

        @BindByTag("itemTablePriority")
        TextView itemTablePriority;

        @BindByTag("itemGalleryView")
        CustomGalleryView itemGalleryView;

        @BindByTag("itemYHPosition")
        TextView itemYHPosition;

        @BindByTag("itemWXType")
        TextView itemWXType;

        @BindByTag("itemYHType")
        TextView itemYHType;

        private OfflineCameraController mOfflineCameraController;


        WorkViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initBind() {
            super.initBind();
        }

        @Override
        protected void initView() {
            super.initView();
            itemYHDeviceIc = itemView.findViewById(R.id.itemYHDeviceIc);

            itemYHDescription.setKeyHeight(0);
            itemYHDeviceName.setTextStyle(Typeface.BOLD);
            itemTableNoTv.setTextStyle(Typeface.BOLD);

            itemTableNoTv.setIntercept(false);
            itemYHDeviceName.setIntercept(false);
            itemYHDeviceCode.setIntercept(false);

            mEamPicController = new EamPicController();
        }


        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(this);
            itemTableStatus.setOnClickListener(this);
            itemInfo.setOnClickListener(this);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_yh;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void update(YHEntityVo data) {
            itemGalleryView.clear();
            itemTableNoTv.setValue("Fault" + DateUtil.dateFormat(data.getTableId(), "yyyyMMdd_hh_mm_ss"));
            if (data.getFindStaffId() != null) {
                itemYHPersonTv.setText(data.getFindStaffName());
            }

            if (TextUtils.isEmpty(data.getEamName())) {
                itemYHDeviceName.setValue("未选择设备");
                itemYHDeviceCode.setValue("");
            } else {
                itemYHDeviceName.setValue(data.getEamName());
            }

            if (null != data.getEamId()) {
                CommonDeviceEntity commonDeviceEntity = DeviceManager.getInstance().getDeviceEntityByEamId(data.getEamId());
                if (null != commonDeviceEntity) {
                    if (!TextUtils.isEmpty(commonDeviceEntity.getEamName())) {
                        itemYHDeviceName.setValue(commonDeviceEntity.getEamName());
                    }
                    if (!TextUtils.isEmpty(commonDeviceEntity.getEamCode())) {
                        itemYHDeviceCode.setValue(commonDeviceEntity.getEamCode());
                    }
                }

                mEamPicController.initEamPic(itemYHDeviceIc, data.getEamId());
            }
            else{
                itemYHDeviceIc.setImageResource(R.drawable.ic_default_pic3);
            }
            if (TextUtils.isEmpty(data.getDescription())) {
                itemYHDescription.setVisibility(View.GONE);
            } else {
                itemYHDescription.setVisibility(View.VISIBLE);
                itemYHDescription.setValue(data.getDescription());
            }
            if (null != data.getFindDate()) {
                itemYHDateTv.setText(data.getFindDate());
            } else {
                itemYHDateTv.setText(null);
            }
            if (null != data.getFaultType()) {
                itemYHType.setText("隐患类型：" + SystemCodeUtil.getSystemCodeValue(data.getFaultType()));
                itemYHType.setVisibility(View.VISIBLE);
            } else {
                itemYHType.setVisibility(View.GONE);
            }

            if (data.getRepairType() != null) {
                itemWXType.setText("维修类型：" + SystemCodeUtil.getSystemCodeValue(data.getRepairType()));
                itemWXType.setVisibility(View.VISIBLE);
            } else {
                itemWXType.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(data.getAreaInstallName())) {
                itemYHPosition.setText(/*"区域位置："+*/data.getAreaInstallName());
                itemYHPosition.setVisibility(View.VISIBLE);
            } else {
                itemYHPosition.setVisibility(View.GONE);
            }

            if (data.getPriority() != null && ("BEAM2007/01".equals(data.getPriority()) || "BEAM2007/02".equals(data.getPriority()))) {
                itemTablePriority.setVisibility(View.VISIBLE);
                itemTablePriority.setText(SystemCodeUtil.getSystemCodeValue(data.getPriority()));
            } else {
                itemTablePriority.setVisibility(View.GONE);
            }

            if (!data.getStatus()) {
                itemTableStatus.setText("编辑中");
                itemTableStatus.setTextColor(context.getResources().getColor(R.color.table_edit));
                itemTableStatus.setBackgroundResource(R.drawable.sh_table_status_edit);
            } else {
                itemTableStatus.setText("待上传");
                itemTableStatus.setTextColor(context.getResources().getColor(R.color.table_check));
                itemTableStatus.setBackgroundResource(R.drawable.sh_table_status_check);
            }


            initGalleryView(data);

        }

        private void initGalleryView(YHEntityVo vo) {
            itemGalleryView.setEditable(false);
            FaultPicHelper.initPics(vo.getLocalPicPaths().split(","), itemGalleryView);
            mOfflineCameraController = new OfflineCameraController(itemView);
            mOfflineCameraController.addGalleryView(getAdapterPosition(), itemGalleryView);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            YHEntityVo yhEntity = getItem(position);
            onItemChildViewClick(v, 0, yhEntity);
        }

    }

    public void onDestroy(){

        if(mEamPicController!=null){
            mEamPicController.onDestroy();
        }

    }
}
