package com.supcon.mes.module_sbda_online.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_sbda_online.R;
import com.supcon.mes.module_sbda_online.model.bean.SubsidiaryEntity;

import java.text.SimpleDateFormat;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class SubsidiaryAdapter extends BaseListDataRecyclerViewAdapter<SubsidiaryEntity> {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public SubsidiaryAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SubsidiaryEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SubsidiaryEntity> {

        @BindByTag("itemEquipmentNameTv")
        CustomTextView itemEquipmentNameTv;
        @BindByTag("itemEquipmentNumTv")
        TextView itemEquipmentNumTv;
        @BindByTag("itemEquipmentTypeTv")
        TextView itemEquipmentTypeTv;
        @BindByTag("itemEquipmentModelTv")
        CustomTextView itemEquipmentModelTv;
        @BindByTag("itemEquipmentProduceFirmTv")
        CustomTextView itemEquipmentProduceFirmTv;
        @BindByTag("itemEquipmentProduceCodeTv")
        CustomTextView itemEquipmentProduceCodeTv;
        @BindByTag("itemEquipmentDateTv")
        CustomTextView itemEquipmentDateTv;
        @BindByTag("itemEquipmentMemoTv")
        CustomTextView itemEquipmentMemoTv;
        @BindByTag("eamStatus")
        TextView eamStatus;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_subsidiary;
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(v -> onItemChildViewClick(itemView,0,getItem(getLayoutPosition())));
        }

        @Override
        protected void update(SubsidiaryEntity data) {

            String eam = String.format(context.getString(R.string.device_style10), Util.strFormat(data.getAttachEamId().name)
                    , Util.strFormat(data.attachEamId.code));
            itemEquipmentNameTv.contentView().setText(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()));

            itemEquipmentNumTv.setText(String.format(context.getString(R.string.device_style1), "数量:", Util.big2(data.sum)));
            itemEquipmentTypeTv.setText(String.format(context.getString(R.string.device_style1), "设备类型:", Util.strFormat(data.getAttachEamId().getEamType().name)));
            itemEquipmentModelTv.setValue(Util.strFormat(data.getAttachEamId().model));
            itemEquipmentProduceFirmTv.setValue(Util.strFormat(data.getAttachEamId().produceFirm));
            itemEquipmentProduceCodeTv.setValue(Util.strFormat(data.getAttachEamId().produceCode));
            itemEquipmentDateTv.setValue(data.getAttachEamId().getProduceDate() != null ? dateFormat.format(data.getAttachEamId().getProduceDate()) : "");

            if (!TextUtils.isEmpty(data.attachMemo)) {
                itemEquipmentMemoTv.setVisibility(View.VISIBLE);
                itemEquipmentMemoTv.setValue(String.format(context.getString(R.string.device_style2), "备注:", Util.strFormat(data.attachMemo)));
            }
            if (TextUtils.isEmpty(data.getAttachEamId().stateForDisplay)) {
                eamStatus.setVisibility(View.GONE);
            } else {
                int statusBackgroundRes = R.drawable.eam_status_use;
                eamStatus.setText(data.getAttachEamId().stateForDisplay);
                String status = data.getAttachEamId().state == null ? "" : data.getAttachEamId().state;
                if (status.equals("01"))
                    statusBackgroundRes = R.drawable.eam_status_use;
                else if (status.equals("04"))
                    statusBackgroundRes = R.drawable.eam_status_drop;
                else if (status.equals("03"))
                    statusBackgroundRes = R.drawable.eam_status_delay;
                else if (status.equals("02"))
                    statusBackgroundRes = R.drawable.eam_status_stop;

                eamStatus.setBackgroundResource(statusBackgroundRes);
            }
        }
    }
}
