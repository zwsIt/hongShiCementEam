package com.supcon.mes.module_login.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.view.SwitchButton;
import com.supcon.mes.module_login.R;
import com.supcon.mes.module_login.model.bean.WorkInfo;

/**
 * Created by wangshizhan on 2017/8/16.
 * Email:wangshizhan@supcon.com
 */

public class WorkSettingAdapter extends BaseListDataRecyclerViewAdapter<WorkInfo> {

    public WorkSettingAdapter(Context context) {
        super(context);

    }

    @Override
    protected BaseRecyclerViewHolder<WorkInfo> getViewHolder(int viewType) {
        return new WorkViewHolder(context);
    }



    class WorkViewHolder extends BaseRecyclerViewHolder<WorkInfo> {

        @BindByTag("workIcon")
        ImageView workIcon;

        @BindByTag("workName")
        TextView workName;

        @BindByTag("workSwitchBtn")
        SwitchButton workSwitchBtn;


        public WorkViewHolder(Context context) {
            super(context);
        }


        @Override
        protected void initListener() {
            super.initListener();
            workSwitchBtn.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                    WorkInfo workInfo = getItem(getAdapterPosition());
                    workInfo.isOpen = isChecked;
                    onItemChildViewClickListener.onItemChildViewClick(view, getAdapterPosition(), 0, workInfo);
                }
            });
        }

        @Override
        protected int layoutId() {
            return R.layout.item_work_st;
        }

        @Override
        protected void update(WorkInfo data) {

            workIcon.setImageResource(data.iconResId);
            workName.setText(data.name);
            workSwitchBtn.setChecked(data.isOpen);

        }
    }
}
