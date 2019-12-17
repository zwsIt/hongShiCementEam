package com.supcon.mes.middleware.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.model.bean.LubricatingPartEntity;
import com.supcon.mes.middleware.model.bean.RefLubricateEntity;
import com.supcon.mes.middleware.util.Util;

/**
 * LubricatePartRefAdapter 润滑部位参照Adapter
 * created by zhangwenshuai1 2019/11/5
 */
public class LubricatePartRefAdapter extends BaseListDataRecyclerViewAdapter<LubricatingPartEntity> {
    public LubricatePartRefAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<LubricatingPartEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<LubricatingPartEntity> {

        @BindByTag("lubPart")
        CustomTextView lubPart;
        @BindByTag("eamType")
        CustomTextView eamType;
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
            return R.layout.item_lubricate_part_ref;
        }

        @Override
        protected void update(LubricatingPartEntity data) {
            lubPart.setContent(data.getLubPart());
            eamType.setContent(data.getEamTypeId().name);
            remark.setContent(data.getRemark());
        }
    }

}
