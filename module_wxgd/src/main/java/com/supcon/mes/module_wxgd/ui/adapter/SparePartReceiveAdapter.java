package com.supcon.mes.module_wxgd.ui.adapter;

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
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomNumView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.SparePartEntity;
import com.supcon.mes.middleware.model.bean.SparePartReceiveEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EditInputFilter;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wxgd.R;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.List;

public class SparePartReceiveAdapter extends BaseListDataRecyclerViewAdapter<SparePartReceiveEntity> {
    public SparePartReceiveAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SparePartReceiveEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<SparePartReceiveEntity> {

        @BindByTag("sparePart")
        CustomTextView sparePart;
        @BindByTag("sparePartSpecificModel")
        CustomTextView sparePartSpecificModel;
        @BindByTag("sum")
        CustomNumView sum;
        @BindByTag("remark")
        CustomVerticalEditText remark;

        @BindByTag("itemViewDelBtn")
        TextView itemViewDelBtn;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_sparepart_receiver;
        }

        @Override
        protected void initView() {
            super.initView();
            EditInputFilter editInputFilter = new EditInputFilter();
            sum.getNumViewInput().setFilters(new InputFilter[]{editInputFilter});
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
                        item.origDemandQuity = Util.strToFloat(charSequence.toString());
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
            itemViewDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemRemoved(getLayoutPosition());
                    notifyItemRangeChanged(getLayoutPosition(), getItemCount());
                    List<SparePartReceiveEntity> list = getList();
                    list.remove(getLayoutPosition());
                    onItemChildViewClick(itemViewDelBtn, 0, getAdapterPosition());
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
        }
    }
}
