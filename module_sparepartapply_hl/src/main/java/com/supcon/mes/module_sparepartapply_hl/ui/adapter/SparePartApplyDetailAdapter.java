package com.supcon.mes.module_sparepartapply_hl.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomNumView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.middleware.model.bean.SparePartReceiveEntity;
import com.supcon.mes.middleware.util.EditInputFilter;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_sparepartapply_hl.R;

import java.util.List;

/**
 * @description SparePartApplyDetailAdapter
 * @author  2019/10/29
 * 备件领用申请明细Adapter
 */
public class SparePartApplyDetailAdapter extends BaseListDataRecyclerViewAdapter<SparePartReceiveEntity> {

    private boolean editable;
    private boolean isSendStatus; // 是否是备件领用发货状态

    public SparePartApplyDetailAdapter(Context context) {
        super(context);
    }
    public SparePartApplyDetailAdapter(Context context, boolean editable) {
        super(context);
        this.editable = editable;
    }

    @Override
    protected BaseRecyclerViewHolder<SparePartReceiveEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    public void setEditable(boolean editable, boolean isSendStatus) {
        this.editable = editable;
        this.isSendStatus = isSendStatus;
    }

    class ViewHolder extends BaseRecyclerViewHolder<SparePartReceiveEntity> {

        @BindByTag("sparePart")
        CustomTextView sparePart;
        @BindByTag("sparePartSpecificModel")
        CustomTextView sparePartSpecificModel;
        @BindByTag("sum")
        CustomNumView sum;
        @BindByTag("currDemandQuity")
        CustomNumView currDemandQuity; // 领用量
        @BindByTag("remark")
        CustomVerticalEditText remark;

        @BindByTag("itemViewDelBtn")
        TextView itemViewDelBtn;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_sparepart_apply_detail;
        }

        @Override
        protected void initView() {
            super.initView();
            EditInputFilter editInputFilter = new EditInputFilter();
            sum.getNumViewInput().setFilters(new InputFilter[]{editInputFilter});
            currDemandQuity.getNumViewInput().setFilters(new InputFilter[]{editInputFilter});
            if (!editable){
                itemViewDelBtn.setVisibility(View.GONE);
            }
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxTextView.textChanges(sum.getNumViewInput())
                    .skipInitialValue()
                    .subscribe(charSequence -> {
                        SparePartReceiveEntity item = getItem(getAdapterPosition());
                        if (item == null) {
                            return;
                        }
                        if (TextUtils.isEmpty(charSequence)) {
                            item.origDemandQuity = null;
                            return;
                        }
                        item.origDemandQuity = Util.strToDouble(charSequence.toString());
                    });
            RxTextView.textChanges(currDemandQuity.getNumViewInput())
                    .skipInitialValue()
                    .subscribe(charSequence -> {
                        SparePartReceiveEntity item = getItem(getAdapterPosition());
                        if (item == null) {
                            return;
                        }
                        if (TextUtils.isEmpty(charSequence)) {
                            item.currDemandQuity = null;
                            return;
                        }
                        item.currDemandQuity = Util.strToDouble(charSequence.toString());
                    });
            RxTextView.textChanges(remark.editText())
                    .skipInitialValue()
                    .subscribe(charSequence -> {
                        SparePartReceiveEntity item = getItem(getAdapterPosition());
                        if (item == null) {
                            return;
                        }
                        item.remark = charSequence.toString();
                    });
            itemViewDelBtn.setOnClickListener(v -> {
                SparePartReceiveEntity sparePartReceiveEntity = getItem(getAdapterPosition()); // 先拿到对象，防止删除后拿为null

                notifyItemRemoved(getLayoutPosition());
                notifyItemRangeChanged(getLayoutPosition(), getItemCount());
                List<SparePartReceiveEntity> list = getList();
                list.remove(getLayoutPosition());

                if (sparePartReceiveEntity.id != null){
                    onItemChildViewClick(itemViewDelBtn, 0, sparePartReceiveEntity);
                }
            });

        }

        @Override
        protected void update(SparePartReceiveEntity data) {
            Spanned item = HtmlParser.buildSpannedText(String.format(context.getString(R.string.device_style12), Util.strFormat(data.getSparePartId().productName)
                    , Util.strFormat(data.getSparePartId().productCode)), new HtmlTagHandler());
            sparePart.setContent(item.toString());
            Spanned modelSpecif = HtmlParser.buildSpannedText(String.format(context.getString(R.string.device_style12), Util.strFormat(data.getSparePartId().productSpecif)
                    , Util.strFormat(data.getSparePartId().productModel)), new HtmlTagHandler());
            sparePartSpecificModel.setContent(modelSpecif.toString());

            sum.setEditable(editable);
            sum.getNumViewInput().setEnabled(editable);
            remark.setEditable(editable);
            sum.getNumViewInput().setText(data.origDemandQuity == null ? "" : data.origDemandQuity.toString());

            currDemandQuity.setEditable(isSendStatus);
            currDemandQuity.getNumViewInput().setEnabled(isSendStatus);
            currDemandQuity.getNumViewInput().setText(Util.strFormat2(data.currDemandQuity));

        }
    }
}
