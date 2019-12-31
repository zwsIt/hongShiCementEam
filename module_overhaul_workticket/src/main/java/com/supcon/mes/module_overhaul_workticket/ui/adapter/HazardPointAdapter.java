package com.supcon.mes.module_overhaul_workticket.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_overhaul_workticket.R;
import com.supcon.mes.module_overhaul_workticket.model.bean.HazardPointEntity;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * 危险源控制点adapter
 */
public class HazardPointAdapter extends BaseListDataRecyclerViewAdapter<HazardPointEntity> {
    public HazardPointAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<HazardPointEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    public class ViewHolder extends BaseRecyclerViewHolder<HazardPointEntity> {

        @BindByTag("index")
        TextView index;
        @BindByTag("content")
        CustomTextView content;
        @BindByTag("chkBox")
        CheckBox chkBox;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_hazard_point;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();

            RxView.clicks(itemView)
                    .throttleFirst(300, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            HazardPointEntity data = getItem(getAdapterPosition());
                            data.checked = !data.checked;
                            notifyItemChanged(getAdapterPosition());
                            onItemChildViewClick(itemView,0,getList());
                        }
                    });
            chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    HazardPointEntity data = getItem(getAdapterPosition());
                    data.checked = isChecked;
                    onItemChildViewClick(chkBox,0,getList());
                }
            });
        }

        @Override
        protected void update(HazardPointEntity data) {
            index.setText(String.valueOf(getAdapterPosition() + 1));
            content.setContent(data.value);
            chkBox.setChecked(data.checked);
        }
    }

}
