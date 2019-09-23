package com.supcon.mes.module_olxj.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomDateView;
import com.supcon.mes.mbap.view.CustomExpandableTextView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.model.bean.OLXJHistorySheetEntity;

import java.util.List;


/**
 * Created by wangshizhan on 2018/3/27.
 * Email:wangshizhan@supcon.com
 */

public class OLXJHistorySheetAdapter extends BaseListDataRecyclerViewAdapter<OLXJHistorySheetEntity> {

    public OLXJHistorySheetAdapter(Context context) {
        super(context);
    }

    public OLXJHistorySheetAdapter(Context context, List<OLXJHistorySheetEntity> list) {
        super(context, list);
    }

    @Override
    protected BaseRecyclerViewHolder<OLXJHistorySheetEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }


    class ViewHolder extends BaseRecyclerViewHolder<OLXJHistorySheetEntity> {

        @BindByTag("historyItemIndex")
        TextView historyItemIndex;

        @BindByTag("historyItemResult")
        CustomVerticalEditText historyItemResult;

        @BindByTag("historyItemConclusion")
        CustomVerticalEditText historyItemConclusion;

        @BindByTag("historyItemDate")
        CustomDateView historyItemDate;

        @BindByTag("historyItemContent")
        CustomTextView historyItemContent;

        @BindByTag("historyEamName")
        CustomExpandableTextView historyEamName;

        @BindByTag("historyExemption")
        TextView historyExemption;

        @BindByTag("historySkip")
        TextView historySkip;

     public ViewHolder(Context context) {
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

        }


        @Override
        protected int layoutId() {
            return R.layout.item_xj_work_item_history;
        }

        @Override
        protected void update(OLXJHistorySheetEntity data) {
            historyItemIndex.setText(String.valueOf(getAdapterPosition()+1));
            historyItemContent.setValue(data.content);
            historyItemResult.setInput(data.result);
            historyItemConclusion.setInput(data.conclusion);
            historyItemDate.setDate(data.dateTime);
            historyEamName.setText(data.eamName);

            if ("wiLinkState/02".equals(data.linkStateId)){  //免检
                historyExemption.setVisibility(View.VISIBLE);
                historySkip.setVisibility(View.GONE);
            }else {
                historyExemption.setVisibility(View.GONE);
            }

            if ("wiLinkState/03".equals(data.linkStateId)){ //跳检
                historySkip.setVisibility(View.VISIBLE);
                historyExemption.setVisibility(View.GONE);
            }else {
                historySkip.setVisibility(View.GONE);
            }
        }
    }
}
