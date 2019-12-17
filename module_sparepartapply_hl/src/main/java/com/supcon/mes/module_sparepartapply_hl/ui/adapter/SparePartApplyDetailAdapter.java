package com.supcon.mes.module_sparepartapply_hl.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomNumView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.SparePartReceiveEntity;
import com.supcon.mes.middleware.util.EditInputFilter;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_sparepartapply_hl.R;
import com.supcon.mes.module_sparepartapply_hl.ui.SparePartApplyDetailList;
import com.supcon.mes.module_wxgd.IntentRouter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * @description SparePartApplyDetailAdapter
 * @author  2019/10/29
 * 备件领用申请明细Adapter
 */
public class SparePartApplyDetailAdapter extends BaseListDataRecyclerViewAdapter<SparePartReceiveEntity> {

    private boolean editable;
    private boolean isSendStatus; // 是否是备件领用发货状态
    private boolean mIsWork;

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

    public void setEditable(boolean mIsWork, boolean editable, boolean isSendStatus) {
        this.mIsWork = mIsWork;
        this.editable = editable;
        this.isSendStatus = isSendStatus;
    }

    class ViewHolder extends BaseRecyclerViewHolder<SparePartReceiveEntity> {

        @BindByTag("sparePart")
        CustomTextView sparePart;
        @BindByTag("sparePartSpecificModel")
        CustomTextView sparePartSpecificModel;
        @BindByTag("sparePartPrice")
        CustomTextView sparePartPrice;
        @BindByTag("sparePartTotal")
        CustomTextView sparePartTotal;
        @BindByTag("origDemandQuity")
        CustomNumView origDemandQuity; // 申请数量
        @BindByTag("origDemandQuityView")
        CustomTextView origDemandQuityView; // 申请数量View
        @BindByTag("currDemandQuity")
        CustomNumView currDemandQuity; // 领用量
        @BindByTag("currDemandQuityView")
        CustomTextView currDemandQuityView; // 申请数量View
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
            origDemandQuity.getNumViewInput().setFilters(new InputFilter[]{editInputFilter});
            currDemandQuity.getNumViewInput().setFilters(new InputFilter[]{editInputFilter});
            if (mIsWork || !editable){
                itemViewDelBtn.setVisibility(View.GONE);
            }
            sparePart.setEditable(editable);

            sparePart.setTextFont(Typeface.DEFAULT);
            sparePartSpecificModel.setTextFont(Typeface.DEFAULT);
            sparePartPrice.setTextFont(Typeface.DEFAULT);
            sparePartTotal.setTextFont(Typeface.DEFAULT);
            ((TextView)origDemandQuity.findViewById(R.id.numViewText)).setTypeface(Typeface.DEFAULT);
            ((TextView)currDemandQuity.findViewById(R.id.numViewText)).setTypeface(Typeface.DEFAULT);
            remark.setTextFont(Typeface.DEFAULT);

            if (!editable){
                origDemandQuity.setVisibility(View.GONE);
                origDemandQuityView.setVisibility(View.VISIBLE);
                origDemandQuityView.setTextFont(Typeface.DEFAULT);
            }

        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxTextView.textChanges(origDemandQuity.getNumViewInput())
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
                        item.origDemandQuity = Util.str2BigDecimal(charSequence.toString());

                        // 计算总价
                        if (editable && item.price != null){
                            item.total = Util.bigDecimalScale(item.price.multiply(item.origDemandQuity),2);
                            sparePartTotal.setContent(item.total.toString());
                        }

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
                        item.currDemandQuity = Util.str2BigDecimal(charSequence.toString());
                        // 计算总价
                        if (isSendStatus && item.price != null){
                            item.total = Util.bigDecimalScale(item.price.multiply(item.currDemandQuity),2);
                            sparePartTotal.setContent(item.total.toString());
                        }
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

            sparePart.setOnChildViewClickListener((childView, action, obj) -> onItemChildViewClick(sparePart,action,getItem(getAdapterPosition())));

        }

        @Override
        protected void update(SparePartReceiveEntity data) {
            Spanned item = HtmlParser.buildSpannedText(String.format(context.getString(R.string.device_style12), Util.strFormat(data.getSparePartId().productName)
                    , Util.strFormat(data.getSparePartId().productCode)), new HtmlTagHandler());
            sparePart.setContent(item.toString());
            sparePart.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
            Spanned modelSpecif = HtmlParser.buildSpannedText(String.format(context.getString(R.string.device_style12), Util.strFormat(data.getSparePartId().productSpecif)
                    , Util.strFormat(data.getSparePartId().productModel)), new HtmlTagHandler());
            sparePartSpecificModel.setContent(modelSpecif.toString());
            sparePartPrice.setContent(data.price == null ? "" : String.format("%s%s", data.price, TextUtils.isEmpty(data.getSparePartId().getProductBaseUnit().name) ? "" : String.format("/%s", data.getSparePartId().getProductBaseUnit().name)));
            sparePartTotal.setContent(Util.strFormat2(data.total));

            origDemandQuity.setEditable(editable);
            origDemandQuity.getNumViewInput().setEnabled(editable);
            remark.setEditable(editable);
            origDemandQuity.getNumViewInput().setText(Util.strFormat2(data.origDemandQuity));
            origDemandQuityView.setContent(Util.strFormat2(data.origDemandQuity));

            if (isSendStatus){
                if (data.currDemandQuity == null){
                    data.currDemandQuity = data.origDemandQuity;
                }
                currDemandQuity.setVisibility(View.VISIBLE);
                currDemandQuity.setEditable(isSendStatus);
                currDemandQuity.getNumViewInput().setEnabled(isSendStatus);
                currDemandQuity.getNumViewInput().setText(Util.strFormat2(data.currDemandQuity));
            }else {
                currDemandQuity.setVisibility(View.GONE);
            }

        }
    }
}
