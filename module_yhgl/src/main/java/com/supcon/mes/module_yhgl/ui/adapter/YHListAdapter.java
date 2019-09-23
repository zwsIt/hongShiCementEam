package com.supcon.mes.module_yhgl.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.AttachmentController;
import com.supcon.mes.middleware.controller.AttachmentDownloadController;
import com.supcon.mes.middleware.controller.EamPicController;
import com.supcon.mes.middleware.controller.OnlineCameraController;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.model.bean.AttachmentListEntity;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.module_yhgl.IntentRouter;
import com.supcon.mes.module_yhgl.R;

import java.util.List;


/**
 * Created by wangshizhan on 2017/8/16.
 * Email:wangshizhan@supcon.com
 */

public class YHListAdapter extends BaseListDataRecyclerViewAdapter<YHEntity> {

    private AttachmentDownloadController mDownloadController;

    public YHListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<YHEntity> getViewHolder(int viewType) {
        return new WorkViewHolder(context);
    }

    class WorkViewHolder extends BaseRecyclerViewHolder<YHEntity> implements View.OnClickListener {

        @BindByTag("itemTableNoTv")
        CustomTextView itemTableNoTv;

        @BindByTag("itemYHPersonTv")
        TextView itemYHPersonTv;

        @BindByTag("itemYHDateTv")
        TextView itemYHDateTv;

        @BindByTag("itemInfo")
        ImageView itemInfo;

//        @BindByTag("itemYHDeviceIc")
        ImageView itemYHDeviceIc;

        @BindByTag("itemYHDeviceName")
        CustomTextView itemYHDeviceName;

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
        private OnlineCameraController mOnlineCameraController;
        private AttachmentController mAttachmentController;

        public WorkViewHolder(Context context) {
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
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(this);
            itemTableStatus.setOnClickListener(this);
            itemInfo.setOnClickListener(this);

            itemYHDeviceName.setOnClickListener(v -> goSBDA());
            itemYHDeviceIc.setOnClickListener(v -> goSBDA());
            itemYHDeviceCode.setOnClickListener(v -> goSBDA());
        }

        private void goSBDA() {
            YHEntity yhEntity = getItem(getAdapterPosition());
            if (yhEntity.eamID == null || yhEntity.eamID.id == null) {
                ToastUtils.show(context, "无设备详情可查看！");
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putLong(Constant.IntentKey.SBDA_ONLINE_EAMID, yhEntity.eamID.id);
            bundle.putString(Constant.IntentKey.SBDA_ONLINE_EAMCODE, yhEntity.eamID.code);
            IntentRouter.go(context, Constant.Router.SBDA_ONLINE_VIEW, bundle);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_yh;
        }

        @Override
        protected void update(YHEntity data) {
            itemGalleryView.clear();
            itemTableNoTv.setValue(data.tableNo);
            if (data.findStaffID != null) {
                itemYHPersonTv.setText(data.findStaffID.name);
            }else {
                itemYHPersonTv.setText(null);
            }

            if (data.eamID == null || TextUtils.isEmpty(data.eamID.name)) {
                itemYHDeviceName.setValue("未选择设备");
                itemYHDeviceCode.setValue("");
            } else {
                itemYHDeviceName.setValue(data.eamID.name);
                itemYHDeviceCode.setValue(data.eamID.code);
            }

            if(data.eamID!=null && data.eamID.id!=null){
                new EamPicController().initEamPic(itemYHDeviceIc, data.eamID.id);
            }
            else{
                itemYHDeviceIc.setImageResource(R.drawable.ic_default_pic3);
            }

            if (TextUtils.isEmpty(data.describe)) {
                itemYHDescription.setVisibility(View.GONE);
            } else {
                itemYHDescription.setVisibility(View.VISIBLE);
                itemYHDescription.setValue(data.describe);
            }
            if (0 != data.findTime) {
                itemYHDateTv.setText(DateUtil.dateFormat(data.findTime, "yyyy-MM-dd HH:mm:ss"));
            } else {
                itemYHDateTv.setText(null);
            }

            if (data.faultInfoType != null && data.faultInfoType.value != null) {
                itemYHType.setText("隐患类型：" + data.faultInfoType.value);
                itemYHType.setVisibility(View.VISIBLE);
            } else {
                itemYHType.setVisibility(View.GONE);
            }

            if (data.repairType != null && data.repairType.value != null) {
                itemWXType.setText("维修类型：" + data.repairType.value);
                itemWXType.setVisibility(View.VISIBLE);
            } else {
                itemWXType.setVisibility(View.GONE);
            }

            if (data.areaInstall != null && !TextUtils.isEmpty(data.areaInstall.name)) {
                itemYHPosition.setText(/*"区域位置："+*/data.areaInstall.name);
                itemYHPosition.setVisibility(View.VISIBLE);
            } else {
                itemYHPosition.setVisibility(View.GONE);
            }

            if (data.priority != null && ("BEAM2007/01".equals(data.priority.id) || "BEAM2007/02".equals(data.priority.id))) {
                itemTablePriority.setText(data.priority.value);
                itemTablePriority.setVisibility(View.VISIBLE);
            } else {
                itemTablePriority.setVisibility(View.GONE);
            }

            if (data.pending != null) {
                itemTableStatus.setText(data.pending.taskDescription);

                switch (data.pending.taskDescription) {
                    case "编辑":     //编辑页面
                        itemTableStatus.setTextColor(context.getResources().getColor(R.color.table_edit));
                        itemTableStatus.setBackgroundResource(R.drawable.sh_table_status_edit);
                        break;

                    case "审核":    //审批页面
                        itemTableStatus.setTextColor(context.getResources().getColor(R.color.table_check));
                        itemTableStatus.setBackgroundResource(R.drawable.sh_table_status_check);
                        break;

                    case "生效":        //查看页面
                        itemTableStatus.setTextColor(context.getResources().getColor(R.color.table_check));
                        itemTableStatus.setBackgroundResource(R.drawable.sh_table_status_check);
                        break;

                    default:
                        itemTableStatus.setTextColor(context.getResources().getColor(R.color.table_local));
                        itemTableStatus.setBackgroundResource(R.drawable.sh_table_status_local);
                        break;
                }
            }
            mOnlineCameraController = new OnlineCameraController(itemView);
            mOnlineCameraController.addGalleryView(getAdapterPosition(), itemGalleryView);
            if (data.attachmentEntities != null) {
                downloadAttachment(data.attachmentEntities);
            } else {

                if(mAttachmentController == null){
                    mAttachmentController = new AttachmentController();
                }

                mAttachmentController.refreshGalleryView(new OnAPIResultListener<AttachmentListEntity>() {
                    @Override
                    public void onFail(String errorMsg) {
                    }

                    @Override
                    public void onSuccess(AttachmentListEntity entity) {
                        if (entity.result != null && entity.result.size() == 0) {
                            return;
                        }
                        data.attachmentEntities = entity.result;
                        downloadAttachment(entity.result);
                    }
                }, data.tableInfoId);
            }

        }

        private void downloadAttachment(List<AttachmentEntity> attachmentEntities) {

            if(mDownloadController == null){
                mDownloadController = new AttachmentDownloadController(Constant.IMAGE_SAVE_YHPATH);
            }

            mDownloadController.downloadYHPic(attachmentEntities, "BEAM2_1.0.0_faultInfo",
                            result -> itemGalleryView.setGalleryBeans(result));
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            YHEntity yhEntity = getItem(position);
            onItemChildViewClick(v, 0, yhEntity);
        }
    }

    public void onDestroy(){

        if(mDownloadController!=null){
            mDownloadController.dispose();
        }

    }

}
