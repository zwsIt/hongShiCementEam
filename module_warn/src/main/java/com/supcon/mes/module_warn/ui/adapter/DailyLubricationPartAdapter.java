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

public class DailyLubricationPartAdapter extends BaseListDataRecyclerViewAdapter<DailyLubricateTaskEntity> {

    private boolean editable;

    public DailyLubricationPartAdapter(Context context) {
        super(context);
    }

    public void setEditable( boolean isEditable) {
        this.editable = isEditable;
    }
    @Override
    protected BaseRecyclerViewHolder<DailyLubricateTaskEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<DailyLubricateTaskEntity> {

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
            return R.layout.item_daily_lubri_part;
        }

        @Override
        protected void initView() {
            super.initView();
            if (!editable) {
                chkBox.setVisibility(View.GONE);
            }
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DailyLubricateTaskEntity item = getItem(getAdapterPosition());
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
        protected void update(DailyLubricateTaskEntity data) {

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
