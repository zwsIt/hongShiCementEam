package com.supcon.mes.middleware.ui.adapter;

import android.content.Context;
import android.view.View;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.LubricatingPartEntity;

/**
 * LubricatePartRefAdapter 设备类型参照Adapter
 * created by zhangwenshuai1 2019/11/12
 */
public class EamTypeRefAdapter extends BaseListDataRecyclerViewAdapter<EamType> {
    public EamTypeRefAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<EamType> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<EamType> {

        @BindByTag("name")
        CustomTextView name;
        @BindByTag("code")
        CustomTextView code;
        @BindByTag("remark")
        CustomTextView remark;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemChildViewClick(v, 0,getItem(getAdapterPosition()));
                }
            });
        }

        @Override
        protected int layoutId() {
            return R.layout.item_eam_type_ref;
        }

        @Override
        protected void update(EamType data) {
            name.setContent(data.name);
            code.setContent(data.code);
            remark.setContent(data.remark);
        }
    }

}
