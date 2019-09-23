package com.supcon.mes.module_xj.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.middleware.model.bean.XJHistoryEntity;
import com.supcon.mes.module_xj.R;

import java.util.List;


/**
 * Created by wangshizhan on 2018/3/27.
 * Email:wangshizhan@supcon.com
 */

public class XJHistoryListAdapter extends BaseListDataRecyclerViewAdapter<XJHistoryEntity> {

    public XJHistoryListAdapter(Context context) {
        super(context);
    }

    public XJHistoryListAdapter(Context context, List<XJHistoryEntity> list) {
        super(context, list);
    }

    @Override
    protected BaseRecyclerViewHolder<XJHistoryEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }


    class ViewHolder extends BaseRecyclerViewHolder<XJHistoryEntity> {

        @BindByTag("historyItemResult")
        CustomVerticalEditText historyItemResult;

        @BindByTag("historyItemConclusion")
        TextView historyItemConclusion;

        @BindByTag("historyItemDate")
        TextView historyItemDate;


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
            return R.layout.item_xj_history;
        }

        @Override
        protected void update(XJHistoryEntity data) {
            historyItemResult.setInput(data.result);
            historyItemConclusion.setText(data.conclusion);
            historyItemDate.setText(data.dateTime);
        }
    }
}
