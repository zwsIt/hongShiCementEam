package com.supcon.mes.module_olxj.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalSpinner;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.FaultPicHelper;
import com.supcon.mes.module_olxj.IntentRouter;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.constant.OLXJConstant;
import com.supcon.mes.module_olxj.controller.OLXJCameraController;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;

import java.util.Arrays;
import java.util.List;


/**
 * Created by wangshizhan on 2018/3/27.
 * Email:wangshizhan@supcon.com
 */

public class OLXJWorkListFinishAdapter extends BaseListDataRecyclerViewAdapter<OLXJWorkItemEntity> {

    private boolean isXJFinished = false;

    public OLXJWorkListFinishAdapter(Context context) {
        super(context);
    }

    public OLXJWorkListFinishAdapter(Context context, List<OLXJWorkItemEntity> list) {
        super(context, list);
    }

    public OLXJWorkListFinishAdapter(Context context, boolean isXJFinished) {
        super(context);
        this.isXJFinished = isXJFinished;
    }

    @Override
    protected BaseRecyclerViewHolder<OLXJWorkItemEntity> getViewHolder(int viewType) {
        return new ViewHolderFinished(context);
    }


    class ViewHolderFinished extends BaseRecyclerViewHolder<OLXJWorkItemEntity> implements OnChildViewClickListener {

        @BindByTag("workItemIndex")
        TextView workItemIndex;

        @BindByTag("xjEamName")
        TextView xjEamName;  //设备名称

        @BindByTag("xjItemContent")
        CustomTextView xjItemContent;  //内容

        @BindByTag("fItemSelectResult")
        CustomSpinner fItemSelectResult;  //选择结果

        @BindByTag("fItemInputResult")
        CustomTextView fItemInputResult;  //输入结果

        @BindByTag("fItemConclusion")
        CustomVerticalSpinner fItemConclusion;  //结论

        @BindByTag("fItemRemark")
        CustomEditText fItemRemark;  //备注

        @BindByTag("fItemPics")
        CustomGalleryView fItemPics; //拍照

        @BindByTag("fReRecordBtn")
        Button fReRecordBtn; //重录

        @BindByTag("fExemption")
        TextView fExemption; //免检

        @BindByTag("fSkip")
        TextView fSkip; //跳检

        @BindByTag("fItemPart")
        CustomTextView fItemPart;  //部位

        @BindByTag("llNormalRange")
        LinearLayout llNormalRange;
        @BindByTag("fItemNormalRange")
        CustomTextView fItemNormalRange;

        @BindByTag("customGalleryInclude")
        View customGalleryInclude;

        @BindByTag("buttonBar")
        LinearLayout buttonBar;

        @BindByTag("viewDivide")
        View viewDivide;
        OLXJCameraController mOLXJCameraController;

        public ViewHolderFinished(Context context) {
            super(context);
        }

        @Override
        protected void initBind() {
            super.initBind();
        }

        @Override
        protected void initView() {
            super.initView();
            mOLXJCameraController = new OLXJCameraController(itemView);
//            mOLXJCameraController = ((OLXJWorkListHandledActivity)context).getController(OLXJCameraController.class);
            mOLXJCameraController.init(Constant.IMAGE_SAVE_XJPATH, Constant.PicType.XJ_PIC);
        }

        @Override
        protected void initListener() {
            super.initListener();

            fReRecordBtn.setOnClickListener(v -> {
                OLXJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                onItemChildViewClick(fReRecordBtn, 0, xjWorkItemEntity);
            });

            xjEamName.setOnClickListener(v -> {
                OLXJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                if (xjWorkItemEntity.eamID == null) {
                    ToastUtils.show(context, "无设备详情可查看！");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putLong(Constant.IntentKey.SBDA_ENTITY_ID, xjWorkItemEntity.eamID.id);
                IntentRouter.go(context, Constant.Router.SBDA_VIEW, bundle);
            });


        }


        @Override
        protected int layoutId() {
            return R.layout.item_olxj_work_item_finished;
        }

        @Override
        protected void update(OLXJWorkItemEntity data) {
            mOLXJCameraController.addListener(fItemPics, getAdapterPosition(),OLXJWorkListFinishAdapter.this);

            if(isXJFinished){
                fReRecordBtn.setVisibility(View.GONE);
            }
            else{
                fReRecordBtn.setVisibility(View.VISIBLE);
            }

            xjItemContent.setValue(data.content);
            fItemRemark.setEditable(false);
            fItemRemark.setInput(data.realRemark);
            fItemPart.setValue(data.part);

            if (TextUtils.isEmpty(data.normalRange)) {
                llNormalRange.setVisibility(View.GONE);
            } else {
                llNormalRange.setVisibility(View.VISIBLE);
            }
            fItemNormalRange.setContent(data.normalRange);

            if ("wiLinkState/02".equals(data.linkState)) {  //免检
                fExemption.setVisibility(View.VISIBLE);
                fSkip.setVisibility(View.GONE);
            } else {
                fExemption.setVisibility(View.GONE);
            }

            if ("wiLinkState/03".equals(data.linkState)) { //跳检
                fSkip.setVisibility(View.VISIBLE);
                fExemption.setVisibility(View.GONE);
            } else {
                fSkip.setVisibility(View.GONE);
            }

            if (OLXJConstant.MobileEditType.INPUTE.equals(data.inputStandardID.editTypeMoblie.id)) {   //录入
                fItemSelectResult.setVisibility(View.GONE);
                fItemInputResult.setVisibility(View.VISIBLE);

                fItemInputResult.setContent(data.result);

            } else if (OLXJConstant.MobileEditType.WHETHER.equals(data.inputStandardID.editTypeMoblie.id) || OLXJConstant.MobileEditType.RADIO.equals(data.inputStandardID.editTypeMoblie.id)) {  //是否  单选
                fItemInputResult.setVisibility(View.GONE);
                fItemSelectResult.setVisibility(View.VISIBLE);

                fItemSelectResult.setSpinner(data.result);

            } else if (OLXJConstant.MobileEditType.CHECKBOX.equals(data.inputStandardID.editTypeMoblie.id)) {  //多选
                fItemInputResult.setVisibility(View.GONE);
                fItemSelectResult.setVisibility(View.VISIBLE);

                fItemSelectResult.setSpinner(data.result);
            }

            if ("realValue/02".equals(data.conclusionID)) {
//                fItemConclusion.setSpinnerColor(context.getResources().getColor(R.color.customRed));
                fItemConclusion.setContentTextColor(context.getResources().getColor(R.color.customRed));
            } else {
//                fItemConclusion.setSpinnerColor(context.getResources().getColor(R.color.textColorlightblack));
                fItemConclusion.setContentTextColor(context.getResources().getColor(R.color.textColorlightblack));
            }
            fItemConclusion.setSpinner(data.conclusionName);

            if (!TextUtils.isEmpty(data.xjImgUrl)) {
                customGalleryInclude.setVisibility(View.VISIBLE);
                fItemPics.setPadding(0, 5, 10, 5);
                String[] imgUrl = data.xjImgUrl.split(",");
                FaultPicHelper.initPics(Arrays.asList(imgUrl), fItemPics);
            } else {
                customGalleryInclude.setVisibility(View.GONE);
                fItemPics.setPadding(0, 0, 10, 0);
                fItemPics.clear();
            }

            if (data.control && !OLXJConstant.MobileWiLinkState.EXEMPTION_STATE.equals(data.linkState)) {
                buttonBar.setVisibility(View.VISIBLE);
                viewDivide.setVisibility(View.VISIBLE);
            } else {
                buttonBar.setVisibility(View.GONE);
                viewDivide.setVisibility(View.GONE);
            }

        }

        @Override
        public void onChildViewClick(View childView, int action, Object obj) {
            OLXJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
            onItemChildViewClick(childView, action, xjWorkItemEntity);
        }
    }


}
