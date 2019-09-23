package com.supcon.mes.module_sbda_online.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_sbda_online.R;
import com.supcon.mes.module_sbda_online.model.bean.RepairEntity;

import java.text.SimpleDateFormat;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class RepairAdapter extends BaseListDataRecyclerViewAdapter<RepairEntity> {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public RepairAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<RepairEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<RepairEntity> {

        @BindByTag("itemRepairTableNoTv")
        CustomTextView itemRepairTableNoTv;
        @BindByTag("itemRepairDescribeTv")
        CustomVerticalTextView itemRepairDescribeTv;
        @BindByTag("itemRepairPriorityTv")
        CustomVerticalTextView itemRepairPriorityTv;
        @BindByTag("itemRepairFindStaffTv")
        CustomVerticalTextView itemRepairFindStaffTv;
        @BindByTag("itemRepairFindTimeTv")
        CustomVerticalTextView itemRepairFindTimeTv;
        @BindByTag("itemRepairRepairTypeTv")
        CustomVerticalTextView itemRepairRepairTypeTv;
        @BindByTag("itemRepairSourceTv")
        CustomVerticalTextView itemRepairSourceTv;
        @BindByTag("eamStatus")
        TextView eamStatus;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_repair;
        }

        @Override
        protected void update(RepairEntity data) {

            itemRepairTableNoTv.setValue(data.tableNo);
            itemRepairDescribeTv.setValue(Util.strFormat(data.getFaultInfo().describe));
            itemRepairPriorityTv.setValue(Util.strFormat(data.getFaultInfo().getPriority().value));
            itemRepairFindStaffTv.setValue(Util.strFormat(data.getFaultInfo().getFindStaffID().name));
            itemRepairFindTimeTv.setValue(dateFormat.format(data.getFaultInfo().findTime));
            itemRepairRepairTypeTv.setValue(Util.strFormat(data.getFaultInfo().getRepairType().value));
            itemRepairSourceTv.setValue(Util.strFormat(data.getWorkSource().value));

            int statusBackgroundRes = R.drawable.eam_status_use;
            String status = data.getWorkSource().id;
            if (status.equals("BEAM049/01"))
                statusBackgroundRes = R.drawable.eam_status_use;
            else if (status.equals("BEAM049/04"))
                statusBackgroundRes = R.drawable.eam_status_drop;
            else if (status.equals("BEAM049/03"))
                statusBackgroundRes = R.drawable.eam_status_delay;
            else if (status.equals("BEAM049/02"))
                statusBackgroundRes = R.drawable.eam_status_stop;
            eamStatus.setText(data.getWorkSource().value);
            eamStatus.setBackgroundResource(statusBackgroundRes);
        }
    }
}
