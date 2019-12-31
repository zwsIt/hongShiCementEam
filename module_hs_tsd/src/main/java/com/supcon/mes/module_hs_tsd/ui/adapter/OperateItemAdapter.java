package com.supcon.mes.module_hs_tsd.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_hs_tsd.R;
import com.supcon.mes.module_hs_tsd.model.bean.OperateItemEntity;

/**
 * 操作项Adapter
 */
public class OperateItemAdapter extends BaseListDataRecyclerViewAdapter<OperateItemEntity> {

    public OperateItemAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<OperateItemEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<OperateItemEntity> {

        @BindByTag("index")
        TextView index;
        @BindByTag("content")
        CustomTextView content;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_operate_item;
        }

        @Override
        protected void update(OperateItemEntity data) {
            index.setText(String.valueOf(getAdapterPosition() +1));
            content.setContent(data.getCaution());
        }
    }

}
