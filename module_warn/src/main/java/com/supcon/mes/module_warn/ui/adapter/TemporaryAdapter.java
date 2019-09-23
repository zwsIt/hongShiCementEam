package com.supcon.mes.module_warn.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.bean.DailyLubricateTaskEntity;
import com.supcon.mes.module_warn.model.bean.TemLubricateTaskEntity;

public class TemporaryAdapter extends BaseListDataRecyclerViewAdapter<TemLubricateTaskEntity> {


    public TemporaryAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<TemLubricateTaskEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<TemLubricateTaskEntity> {

        @BindByTag("itemLubriOilTv")
        CustomTextView itemLubriOilTv;
        @BindByTag("itemLubriChangeTv")
        TextView itemLubriChangeTv;
        @BindByTag("itemLubriNumTv")
        TextView itemLubriNumTv;
        @BindByTag("itemLubriPartTv")
        CustomTextView itemLubriPartTv;

        @BindByTag("chkBox")
        CheckBox chkBox;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_tem_lubri_part;
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TemLubricateTaskEntity item = getItem(getAdapterPosition());
                    item.isCheck = !item.isCheck;
                    notifyItemChanged(getAdapterPosition());
//                    if (checkPosition != -1) {
//                        if (checkPosition != getAdapterPosition()) {
//                            LubricationWarnEntity item1 = getItem(checkPosition);
//                            item1.isCheck = false;
//                        }
//                        notifyItemChanged(checkPosition);
//                    }
//                    checkPosition = getAdapterPosition();
//                    onItemChildViewClick(itemView, checkPosition, getItem(checkPosition));
                }
            });
        }

        @Override
        protected void update(TemLubricateTaskEntity data) {
            chkBox.setVisibility(View.VISIBLE);
            if (data.isLubri) {
                chkBox.setVisibility(View.GONE);
            }
            itemLubriOilTv.setContent(data.getLubricateOil().name);
            itemLubriChangeTv.setText(String.format(context.getString(R.string.device_style1), "加/换油:", Util.strFormat(data.getOilType().value)));
            itemLubriNumTv.setText(String.format(context.getString(R.string.device_style1), "用量:", Util.big2(data.sum)));
            itemLubriPartTv.setValue(Util.strFormat(data.lubricatePart));

            if (data.isCheck) {
                chkBox.setChecked(true);
            } else {
                chkBox.setChecked(false);
            }
        }
    }


}
