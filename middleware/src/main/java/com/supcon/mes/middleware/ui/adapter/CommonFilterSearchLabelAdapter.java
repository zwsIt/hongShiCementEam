package com.supcon.mes.middleware.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.model.bean.CommonLabelEntity;
import com.supcon.mes.middleware.model.bean.TagEntity;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/11/239:54
 */
public class CommonFilterSearchLabelAdapter extends BaseListDataRecyclerViewAdapter<TagEntity> {
    public CommonFilterSearchLabelAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(int viewType) {
        return new ViewHolder(context);
    }


    class ViewHolder extends BaseRecyclerViewHolder<TagEntity> implements View.OnClickListener {
        @BindByTag("itemText")
        TextView itemText;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_common_filter_search_list;
        }

        @Override
        protected void update(TagEntity data) {
            itemText.setText(data.getTagName());
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemChildViewClick(v, 0, getItem(getAdapterPosition()));
        }

    }
}
