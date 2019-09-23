package com.supcon.mes.module_acceptance.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_acceptance.R;
import com.supcon.mes.module_acceptance.model.bean.AcceptanceEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/6/27
 * ------------- Description -------------
 */
public class AcceptanceListAdapter extends BaseListDataRecyclerViewAdapter<AcceptanceEntity> {
    public AcceptanceListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<AcceptanceEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<AcceptanceEntity> {

        @BindByTag("itemAcceptanceEam")
        CustomTextView itemAcceptanceEam;
        @BindByTag("itemAcceptanceItem")
        CustomTextView itemAcceptanceItem;
        @BindByTag("itemAcceptanceTime")
        CustomTextView itemAcceptanceTime;
        @BindByTag("itemAcceptanceStaff")
        CustomTextView itemAcceptanceStaff;
        @BindByTag("itemAcceptanceDept")
        CustomTextView itemAcceptanceDept;

        public ViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_acceptance_list;
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(v -> onItemChildViewClick(itemView, 0, getItem(getLayoutPosition())));
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void update(AcceptanceEntity data) {
            String eam = String.format(context.getString(R.string.device_style10), Util.strFormat(data.getBeamID().name)
                    , Util.strFormat(data.getBeamID().code));
            itemAcceptanceEam.contentView().setText(HtmlParser.buildSpannedText(eam, new HtmlTagHandler()));

            itemAcceptanceItem.setValue(Util.strFormat(data.checkItem));
            itemAcceptanceTime.setValue(data.applyDate != null ? DateUtil.dateFormat(data.applyDate) : "");
            itemAcceptanceStaff.setContent(Util.strFormat(data.getCheckStaff().name));
            itemAcceptanceDept.setContent(Util.strFormat(data.getDept().name));

        }
    }
}
