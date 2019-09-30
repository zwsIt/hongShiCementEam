package com.supcon.mes.module_wxgd.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.AttachmentController;
import com.supcon.mes.middleware.controller.AttachmentDownloadController;
import com.supcon.mes.middleware.controller.EamPicController;
import com.supcon.mes.middleware.controller.OnlineCameraController;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.model.bean.AttachmentListEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.constant.WXGDConstant;

import java.util.List;

public class WXGDListAdapter extends BaseListDataRecyclerViewAdapter<WXGDEntity> {

    private AttachmentDownloadController mDownloadController;

    public WXGDListAdapter(Context context) {
        super(context);

    }

    @Override
    protected BaseRecyclerViewHolder<WXGDEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<WXGDEntity> implements View.OnClickListener {

        @BindByTag("tableNo")
        TextView tableNo;
        @BindByTag("tableStatus")
        TextView tableStatus;
//        @BindByTag("eamName")
//        CustomTextView eamName;

        @BindByTag("eamName")
        TextView eamName;
        @BindByTag("location")
        CustomTextView location;
        @BindByTag("repairGroup")
        CustomTextView repairGroup;
        @BindByTag("chargeStaff")
        CustomTextView chargeStaff;
        @BindByTag("workSource")
        TextView workSource;
        @BindByTag("repairType")
        CustomTextView repairType;
        @BindByTag("faultInfoType")
        CustomTextView faultInfoType;
        @BindByTag("priority")
        TextView priority;
        @BindByTag("claim")
        CustomTextView claim;
        @BindByTag("faultInfoDescribe")
        CustomTextView faultInfoDescribe;
        @BindByTag("content")
        CustomTextView content;

        @BindByTag("receiveBtn")
        Button receiveBtn;

        @BindByTag("contentLl")
        LinearLayout contentLl;
        @BindByTag("faultInfoTypeLl")
        LinearLayout faultInfoTypeLl;  //包含隐患类型和优先级
        @BindByTag("faultInfoDescribeLl")
        LinearLayout faultInfoDescribeLl;
        @BindByTag("receiveBtnLl")
        LinearLayout receiveBtnLl;

        ImageView itemWXGDDeviceIc;

        private OnlineCameraController mOnlineCameraController;
        private AttachmentController mAttachmentController;


        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        public void onClick(View v) {
            WXGDEntity wxgdEntity = getItem(getAdapterPosition());
            onItemChildViewClick(v, 0, wxgdEntity);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_wxgd_list;
        }

        @Override
        protected void initBind() {
            super.initBind();
        }

        @Override
        protected void initView() {
            super.initView();
            itemWXGDDeviceIc = itemView.findViewById(R.id.itemWXGDDeviceIc);
        }

        @Override
        protected void initListener() {
            super.initListener();

            receiveBtn.setOnClickListener(this::onClick);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WXGDEntity wxgdEntity = getItem(getAdapterPosition());

                    if (wxgdEntity == null) {
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.IntentKey.WXGD_ENTITY, wxgdEntity);

                    if (wxgdEntity.pending != null && !TextUtils.isEmpty(wxgdEntity.pending.openUrl)) {
                        switch (wxgdEntity.pending.openUrl) {
                            case Constant.WxgdView.RECEIVE_OPEN_URL:
                                IntentRouter.go(context, Constant.Router.WXGD_RECEIVE, bundle);
                                break;
                            case Constant.WxgdView.DISPATCH_OPEN_URL:
                                IntentRouter.go(context, Constant.Router.WXGD_DISPATCHER, bundle);
                                break;
                            case Constant.WxgdView.VIEW_OPEN_URL:
                                bundle.putBoolean(Constant.IntentKey.isEdit, false);
                            case Constant.WxgdView.EXECUTE_OPEN_URL:
                                IntentRouter.go(context, Constant.Router.WXGD_EXECUTE, bundle);
                                break;
                            case Constant.WxgdView.ACCEPTANCE_OPEN_URL:
                                IntentRouter.go(context, Constant.Router.WXGD_ACCEPTANCE, bundle);
                                break;
                            default:
                                IntentRouter.go(context, Constant.Router.WXGD_EXECUTE, bundle);
                                break;
                        }
                    } else {
                        IntentRouter.go(context, Constant.Router.WXGD_COMPLETE, bundle);
                    }
                }
            });

            eamName.setOnClickListener(v -> goSBDA());
            itemWXGDDeviceIc.setOnClickListener(v -> goSBDA());
        }

        private void goSBDA() {
            WXGDEntity wxgdEntity = getItem(getAdapterPosition());
            if (wxgdEntity.eamID == null || wxgdEntity.eamID.id == null) {
                ToastUtils.show(context, "无设备详情可查看！");
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putLong(Constant.IntentKey.SBDA_ONLINE_EAMID, wxgdEntity.eamID.id);
            bundle.putString(Constant.IntentKey.SBDA_ONLINE_EAMCODE, wxgdEntity.eamID.code);
            IntentRouter.go(context, Constant.Router.SBDA_ONLINE_VIEW, bundle);
        }

        @Override
        protected void update(WXGDEntity data) {

            tableNo.setText(data.tableNo);
            tableStatus.setText(data.pending == null ? "" : data.pending.taskDescription);
            eamName.setText(data.eamID == null ? "" : data.eamID.name);
            location.setValue((data.eamID.installPlace != null && data.eamID.installPlace.name != null) ? data.eamID.installPlace.name : "--");
            repairGroup.setValue((data.repairGroup != null && data.repairGroup.name != null) ? data.repairGroup.name : "--");
            chargeStaff.setValue(Util.strFormat(data.getChargeStaff().name));
            workSource.setText((data.workSource != null && !TextUtils.isEmpty(data.workSource.value)) ? data.workSource.value : "--");

            if (data.eamID != null && data.eamID.id != null) {
                new EamPicController().initEamPic(itemWXGDDeviceIc, data.eamID.id);
            } else {
                itemWXGDDeviceIc.setImageResource(R.drawable.ic_default_pic3);
            }

            if (data.workSource == null) {
                faultInfoTypeLl.setVisibility(View.GONE);
                faultInfoDescribeLl.setVisibility(View.GONE);
                repairType.setVisibility(View.GONE);
                contentLl.setVisibility(View.GONE);
                claim.setVisibility(View.GONE);
            } else {


                if (Constant.WxgdWorkSource.patrolcheck.equals(data.workSource.id)) {
                    workSource.setBackgroundResource(R.color.repairOrange);
                    tableStatus.setTextColor(context.getResources().getColor(R.color.repairOrange));
                    tableStatus.setBackgroundResource(R.drawable.sh_bg_worksource_tablestatus_repair);
                }
                if (Constant.WxgdWorkSource.lubrication.equals(data.workSource.id) || Constant.WxgdWorkSource.maintenance.equals(data.workSource.id)) {
                    workSource.setBackgroundResource(R.color.lubricateGreen);
                    tableStatus.setTextColor(context.getResources().getColor(R.color.lubricateGreen));
                    tableStatus.setBackgroundResource(R.drawable.sh_bg_worksource_tablestatus_lubricate_maintenance);
                }
                if (Constant.WxgdWorkSource.sparepart.equals(data.workSource.id)) {
                    workSource.setBackgroundResource(R.color.sparePartBlue);
                    tableStatus.setTextColor(context.getResources().getColor(R.color.sparePartBlue));
                    tableStatus.setBackgroundResource(R.drawable.sh_bg_worksource_tablestatus_sparepart);
                }
                if (Constant.WxgdWorkSource.other.equals(data.workSource.id)) {
                    workSource.setBackgroundResource(R.color.faultWarn);
                    tableStatus.setTextColor(context.getResources().getColor(R.color.faultWarn));
                    tableStatus.setBackgroundResource(R.drawable.sh_bg_worksource_tablestatus_faultinfo);
                }

                if (Constant.WxgdWorkSource.lubrication.equals(data.workSource.id) || Constant.WxgdWorkSource.maintenance.equals(data.workSource.id) || Constant.WxgdWorkSource.sparepart.equals(data.workSource.id)) {
                    priority.setVisibility(View.GONE);
                    faultInfoTypeLl.setVisibility(View.GONE);
                    faultInfoDescribeLl.setVisibility(View.GONE);
                    repairType.setVisibility(View.GONE);
                    contentLl.setVisibility(View.VISIBLE);
                    claim.setVisibility(View.VISIBLE);

                    claim.setValue(data.claim);
                    content.setKey("工单内容");
//                    content.setValue(data.content);
                    content.setContent(data.workOrderContext == null ? "" : data.workOrderContext);
                } else {
                    contentLl.setVisibility(View.GONE);
                    claim.setVisibility(View.GONE);
                    repairType.setVisibility(View.VISIBLE);
                    faultInfoTypeLl.setVisibility(View.VISIBLE);
                    faultInfoDescribeLl.setVisibility(View.VISIBLE);

                    repairType.setValue(data.faultInfo.repairType == null ? "" : data.faultInfo.repairType.value);
                    faultInfoType.setValue(data.faultInfo.faultInfoType == null ? "" : data.faultInfo.faultInfoType.value);
                    if (data.faultInfo.priority != null && (WXGDConstant.Priority.emergency.equals(data.faultInfo.priority.id) || WXGDConstant.Priority.priority.equals(data.faultInfo.priority.id))) {
                        priority.setVisibility(View.VISIBLE);
                        priority.setText(data.faultInfo.priority.value);

                        ViewGroup.LayoutParams lp = tableNo.getLayoutParams();
                        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        lp.width = DisplayUtil.dip2px(160, context);
                        tableNo.setLayoutParams(lp);
                    } else {
                        ViewGroup.LayoutParams lp = tableNo.getLayoutParams();
                        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        lp.width = DisplayUtil.dip2px(180, context);
                        tableNo.setLayoutParams(lp);

                        priority.setVisibility(View.GONE);
                    }
                    priority.setText(data.faultInfo.priority == null ? "" : data.faultInfo.priority.value);
                    faultInfoDescribe.setValue(data.faultInfo == null ? "" : data.faultInfo.describe);
                }
            }

            if (Constant.WxgdView.RECEIVE_OPEN_URL.equals(data.pending == null ? "" : data.pending.openUrl)) {
                receiveBtnLl.setVisibility(View.VISIBLE);
            } else {
                receiveBtnLl.setVisibility(View.GONE);
            }

            mOnlineCameraController = new OnlineCameraController(itemView);
//            mOnlineCameraController.addGalleryView(getAdapterPosition(), itemGalleryView);
            if (data.attachmentEntities != null) {
//                downloadAttachment(data.attachmentEntities);
            } else {

                if (mAttachmentController == null) {
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
//                        downloadAttachment(entity.result);
                    }
                }, data.tableInfoId);
            }

        }

        private void downloadAttachment(List<AttachmentEntity> attachmentEntities) {

            if (mDownloadController == null) {
                mDownloadController = new AttachmentDownloadController(Constant.IMAGE_SAVE_YHPATH);
            }

//            mDownloadController.downloadYHPic(attachmentEntities, "BEAM2_1.0.0_faultInfo",
//                    result -> itemGalleryView.setGalleryBeans(result));
        }

    }

    public void onDestroy() {

        if (mDownloadController != null) {
            mDownloadController.dispose();
        }

    }
}
