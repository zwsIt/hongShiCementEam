package com.supcon.mes.module_hs_tsd.ui.adapter;

import android.content.Context;
import android.view.View;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_hs_tsd.R;
import com.supcon.mes.module_hs_tsd.model.bean.EleOffOnTemplate;

/**
 * EleOffOnEamTemplateAdapter 设备停送电Adapter
 * created by zhangwenshuai1 2019/12/28
 */
public class EleOffOnEamTemplateAdapter extends BaseListDataRecyclerViewAdapter<EleOffOnTemplate> {
    public EleOffOnEamTemplateAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<EleOffOnTemplate> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<EleOffOnTemplate> {

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
            return R.layout.item_ele_template_eam;
        }

        @Override
        protected void update(EleOffOnTemplate data) {
            name.setContent(data.eamId.name);
            code.setContent(data.eamId.code);
            remark.setContent(data.remark);
        }
    }

}
