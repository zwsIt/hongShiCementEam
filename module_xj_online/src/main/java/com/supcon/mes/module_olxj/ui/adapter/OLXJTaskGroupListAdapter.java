package com.supcon.mes.module_olxj.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.model.bean.OLXJTaskEntity;
import com.supcon.mes.module_olxj.util.TextHelper;


/**
 * Created by zhangwenshuai1 on 2018/3/12.
 */

public class OLXJTaskGroupListAdapter extends BaseListDataRecyclerViewAdapter<OLXJTaskEntity> {


    public OLXJTaskGroupListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<OLXJTaskEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<OLXJTaskEntity> implements View.OnClickListener {

        @BindByTag("taskIndex")
        TextView taskIndex;  //序号

        @BindByTag("taskType")
        TextView taskType;  //巡检类型

        @BindByTag("taskGroup")
        TextView taskGroup;  //巡检路线名

        @BindByTag("taskTeam")
        TextView taskTeam;  //团队

        @BindByTag("taskStartEndTime")
        TextView taskStartEndTime; //备注

        @BindByTag("taskGetBtn")
        Button taskGetBtn;


        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initBind() {
            super.initBind();

        }

        @Override
        protected int layoutId() {
            return R.layout.item_olxj_task_group;
        }

        @Override
        protected void initView() {
            super.initView();

        }

        @Override
        protected void initListener() {
            super.initListener();
            taskGetBtn.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            onItemChildViewClick(v, 0, getItem(adapterPosition));

        }


        @Override
        protected void update(OLXJTaskEntity data) {
            int position = getAdapterPosition();
            taskIndex.setText("" + (position + 1));
            taskGroup.setText(TextHelper.value(data.workGroupID.name));
            if (data.teamID != null) {
                taskTeam.setText(TextHelper.value(data.teamID.name));
            }
            if (data.resstaffID != null) {
                taskTeam.setText(TextHelper.value(data.resstaffID.name));
            }
            taskType.setText(data.valueType != null ? TextHelper.value(data.valueType.value) : "巡检");
            taskStartEndTime.setText(String.format("%s  ~  %s",
                    DateUtil.dateFormat(data.starTime, "MM-dd HH:mm:ss"), DateUtil.dateFormat(data.endTime, "MM-dd HH:mm:ss")));
        }


    }


}
