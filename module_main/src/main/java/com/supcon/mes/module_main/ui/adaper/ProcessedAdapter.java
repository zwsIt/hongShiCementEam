package com.supcon.mes.module_main.ui.adaper;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.BaseConstant;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_login.IntentRouter;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.model.bean.ProcessedEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/24
 * ------------- Description -------------
 * 待办adapter
 */
public class ProcessedAdapter extends BaseListDataRecyclerViewAdapter<ProcessedEntity> {
    public ProcessedAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<ProcessedEntity> getViewHolder(int viewType) {
        return new ContentViewHolder(context);
    }


    class ContentViewHolder extends BaseRecyclerViewHolder<ProcessedEntity> {

        @BindByTag("processTableNo")
        TextView processTableNo;
        @BindByTag("processState")
        TextView processState;
        @BindByTag("processEam")
        CustomTextView processEam;
        @BindByTag("processTime")
        CustomTextView processTime;
        @BindByTag("processStaff")
        CustomTextView processStaff;
        @BindByTag("processContent")
        CustomTextView processContent;

        public ContentViewHolder(Context context) {
            super(context);
        }


        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProcessedEntity item = getItem(getAdapterPosition());
                    String url = "http://" + EamApplication.getIp() + ":" + EamApplication.getPort()
                            + Constant.WebUrl.FLOWVIEW + "&modelCode=" + item.modelcode + "&deploymentId=" + item.deploymentid + "&fvTableInfoId=" + item.tableid;
                    Bundle bundle = new Bundle();
                    bundle.putString(BaseConstant.WEB_AUTHORIZATION, EamApplication.getAuthorization());
                    bundle.putString(BaseConstant.WEB_COOKIE, EamApplication.getCooki());
                    bundle.putString(BaseConstant.WEB_URL, url);
                    bundle.putBoolean(BaseConstant.WEB_HAS_REFRESH, true);
                    bundle.putBoolean(BaseConstant.WEB_IS_LIST, true);
                    IntentRouter.go(context, Constant.Router.PROCESSED_FLOW, bundle);
                }
            });
        }

        @Override
        protected int layoutId() {
            return R.layout.hs_item_process;
        }

        @Override
        protected void update(ProcessedEntity data) {
            processTableNo.setText(Util.strFormat2(data.tableno));
            processState.setText(Util.strFormat2(data.prostatus));
            if (!TextUtils.isEmpty(data.getEamid().name) || !TextUtils.isEmpty(data.getEamid().code)) {
                String eam = String.format(context.getString(R.string.device_style10), data.getEamid().name
                        , data.getEamid().code);
                processEam.setContent(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()).toString());
            }
            processTime.setContent(data.createTime != null ? DateUtil.dateFormat(data.createTime, "yyyy-MM-dd HH:mm:ss") : "");
            processStaff.setContent(Util.strFormat(data.staffname));
            if (!TextUtils.isEmpty(data.content)) {
                processContent.setContent(data.content);
                processContent.setVisibility(View.VISIBLE);
            } else {
                processContent.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(data.newstate)) {
                if (data.newstate.equals("派工")) {
                    processState.setTextColor(context.getResources().getColor(R.color.gray));
                } else if (data.newstate.equals("执行")) {
                    processState.setTextColor(context.getResources().getColor(R.color.yellow));
                } else if (data.newstate.equals("验收")) {
                    processState.setTextColor(context.getResources().getColor(R.color.blue));
                } else if (data.newstate.equals("生效")) {
                    processState.setTextColor(context.getResources().getColor(R.color.green));
                } else if (data.newstate.equals("作废")) {
                    processState.setTextColor(context.getResources().getColor(R.color.red));
                } else {
                    processState.setTextColor(context.getResources().getColor(R.color.gray));
                }
            } else {
                processState.setTextColor(context.getResources().getColor(R.color.gray));
            }
        }
    }
}
