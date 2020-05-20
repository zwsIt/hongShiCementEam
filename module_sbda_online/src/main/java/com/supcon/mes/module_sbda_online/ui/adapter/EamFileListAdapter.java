package com.supcon.mes.module_sbda_online.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.app.annotation.BindByTag;
import com.supcon.common.BaseConstant;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.module_sbda_online.IntentRouter;
import com.supcon.mes.module_sbda_online.R;
import com.supcon.mes.module_sbda_online.controller.EamFileUrlController;
import com.supcon.mes.module_sbda_online.model.bean.EamFileEntity;
import com.supcon.mes.module_sbda_online.model.bean.EamFileViewUrlEntity;

/**
 * EamFileListAdapter 设备文档Adapter
 * created by zhangwenshuai1 2019/12/28
 */
public class EamFileListAdapter extends BaseListDataRecyclerViewAdapter<EamFileEntity> {

    public EamFileListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<EamFileEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<EamFileEntity> {

        private EamFileUrlController eamFileUrlController;

        @BindByTag("name")
        CustomTextView name;
        @BindByTag("fileIv")
        ImageView fileIv;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(v -> {
//                    onItemChildViewClick(v, 0,getItem(getAdapterPosition()));
                EamFileEntity data = getItem(getAdapterPosition());
                if (TextUtils.isEmpty(data.getViewUrl())){
                    ToastUtils.show(context,data.getViewErrorMsg());
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(BaseConstant.WEB_AUTHORIZATION, EamApplication.getAuthorization());
                bundle.putString(BaseConstant.WEB_COOKIE, EamApplication.getCooki());
                bundle.putBoolean(BaseConstant.WEB_HAS_REFRESH, false);
                bundle.putBoolean(BaseConstant.WEB_IS_LIST, false);
                bundle.putString(BaseConstant.WEB_URL, data.getViewUrl());
                IntentRouter.go(context, Constant.Router.FILE_VIEW, bundle);
            });
        }

        @Override
        protected int layoutId() {
            return R.layout.item_eam_file;
        }

        @Override
        protected void update(EamFileEntity data) {
            if (!TextUtils.isEmpty(data.getDocNameMultiFileIds()) && TextUtils.isEmpty(data.getViewUrl())){
                if (eamFileUrlController == null){
                    eamFileUrlController = new EamFileUrlController();
                }
                eamFileUrlController.getEamFileViewUrl(Long.parseLong(data.getDocNameMultiFileIds()), new OnAPIResultListener<EamFileViewUrlEntity>() {
                    @Override
                    public void onFail(String errorMsg) {
                        data.setViewErrorMsg(errorMsg);
                    }

                    @Override
                    public void onSuccess(EamFileViewUrlEntity result) {
                        data.setViewUrl(result.url);
                    }
                });
            }

            name.setContent(data.getDocNameMultiFileNames());
            if (data.getDocNameMultiFileNames().contains(".pdf")) {
                fileIv.setImageResource(R.drawable.ic_pdf);
            } else if (data.getDocNameMultiFileNames().contains(".doc")) {
                fileIv.setImageResource(R.drawable.ic_doc);
            } else if (data.getDocNameMultiFileNames().contains(".xls")) {
                fileIv.setImageResource(R.drawable.ic_xls);
            } else if (data.getDocNameMultiFileNames().contains(".ppt")) {
                fileIv.setImageResource(R.drawable.ic_ppt);
            } else {
                fileIv.setImageResource(R.drawable.ic_file);
            }
        }
    }

}
