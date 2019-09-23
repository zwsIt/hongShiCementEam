package com.supcon.mes.module_olxj.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.model.bean.OLXJGroupEntity;
import com.supcon.mes.module_olxj.util.TextHelper;


/**
 * Created by zhangwenshuai1 on 2018/3/12.
 */

public class OLXJGroupListAdapter extends BaseListDataRecyclerViewAdapter<OLXJGroupEntity> {


    public OLXJGroupListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<OLXJGroupEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<OLXJGroupEntity> implements View.OnClickListener{

        @BindByTag("itemOLXJGroupIndex")
        TextView itemOLXJGroupIndex;  //序号

        @BindByTag("itemOLXJGroupType")
        TextView itemOLXJGroupType;  //巡检类型

        @BindByTag("itemOLXJGroupName")
        TextView itemOLXJGroupName;  //路线

        @BindByTag("itemOLXJGroupRemark")
        CustomTextView itemOLXJGroupRemark; //备注

        @BindByTag("contentView")
        RecyclerView contentView;

        OLXJAreaListAdapter mOLXJAreaListAdapter;


        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initBind() {
            super.initBind();

        }

        @Override
        protected int layoutId() {
            return R.layout.item_olxj_group;
        }

        @Override
        protected void initView() {
            super.initView();
            mOLXJAreaListAdapter = new OLXJAreaListAdapter(context);

        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            onItemChildViewClick(v, 0, getItem(adapterPosition));

        }



        @Override
        protected void update(OLXJGroupEntity data) {
            int position = getAdapterPosition();
            itemOLXJGroupIndex.setText(""+(position+1));
            itemOLXJGroupName.setText(TextHelper.value(data.name));
            itemOLXJGroupRemark.setContent(TextHelper.value(data.remark));
            itemOLXJGroupType.setText(data.valueType !=null?TextHelper.value(data.valueType.value):"巡检");
        }


    }



}
