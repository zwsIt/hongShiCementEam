package com.supcon.mes.module_main.ui.adaper;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.bumptech.glide.Glide;
import com.supcon.common.view.base.adapter.HeaderRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.middleware.util.RequestOptionUtil;
import com.supcon.mes.module_login.model.bean.WorkInfo;
import com.supcon.mes.module_main.R;


public class WorkAdapter extends HeaderRecyclerViewAdapter<WorkInfo> {

    public WorkAdapter(Context context) {
        super(context);

    }

    @Override
    public int getItemViewType(int position, WorkInfo workInfo) {

        return workInfo.viewType;
    }

    @Override
    protected BaseRecyclerViewHolder<WorkInfo> getViewHolder(int viewType) {

        return new ContentViewHolder(context);
    }


    class ContentViewHolder extends BaseRecyclerViewHolder<WorkInfo> {

        ImageView workIcon;

        @BindByTag("workName")
        TextView workName;

        @BindByTag("workNum")
        TextView workNum;

        public ContentViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected void initView() {
            super.initView();
            workIcon = itemView.findViewById(R.id.workIcon);
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(v -> {
                onItemChildViewClick(itemView, 0, getItem(getAdapterPosition()));
            });
        }

        @Override
        protected int layoutId() {
            return R.layout.hs_item_work;
        }

        @Override
        protected void update(WorkInfo data) {
            if (data.iconResId != 0) {
                workIcon.setImageResource(data.iconResId);
            } else {
                Glide.with(context).load(data.iconUrl).apply(RequestOptionUtil.getWorkRequestOptions(context)).into(workIcon);
            }
            workName.setText(data.name);
            if (data.num > 0) {
                workNum.setVisibility(View.VISIBLE);
                if (data.num < 99) {
                    workNum.setText(String.valueOf(data.num));
                } else {
                    workNum.setText("99+");
                }
            } else {
                workNum.setVisibility(View.GONE);

            }
        }
    }
}
