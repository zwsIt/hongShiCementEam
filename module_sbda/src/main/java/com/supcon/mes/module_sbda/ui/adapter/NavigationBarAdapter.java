package com.supcon.mes.module_sbda.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseAdapter;
import com.supcon.common.view.base.adapter.BaseRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.base.adapter.viewholder.BaseViewHolder;
import com.supcon.mes.module_sbda.R;

import java.util.ArrayList;
import java.util.List;

public class NavigationBarAdapter extends BaseRecyclerViewAdapter<String> {
    List<String> mList = new ArrayList<>();
    public NavigationBarAdapter(Context context, List list) {
        super(context);
        mList.clear();
        if(list!=null)
        mList.addAll(list);
    }

    public void setList(List list) {
        mList.clear();
        if(list!=null)
            mList.addAll(list);
    }

    public List<String> getList() {
        return mList;
    }

    @Override
    protected BaseRecyclerViewHolder<String> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    @Override
    public String getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends BaseRecyclerViewHolder<String> implements View.OnClickListener {
        @BindByTag("name")
        TextView name;
        @BindByTag("dir")
        ImageView dir;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_search_nav;
        }

        @Override
        protected void update(String data) {
            name.setText(data);
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
