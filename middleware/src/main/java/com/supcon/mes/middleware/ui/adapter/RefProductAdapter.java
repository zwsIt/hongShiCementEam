package com.supcon.mes.middleware.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.SparePartRefEntity;
import com.supcon.mes.middleware.util.Util;

/**
 * @author yangfei.cao
 * @ClassName eam
 * @date 2018/9/5
 * ------------- Description -------------
 * 备件添加选择列表Adapter
 */
public class RefProductAdapter extends BaseListDataRecyclerViewAdapter<SparePartRefEntity> {
    public RefProductAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<SparePartRefEntity> getViewHolder(int viewType) {
        return new RefProductViewHolder(context);
    }

    class RefProductViewHolder extends BaseRecyclerViewHolder<SparePartRefEntity> {
        @BindByTag("layout_refproduct")
        RelativeLayout layout_refproduct;
        @BindByTag("productName")
        TextView productName;
        @BindByTag("productSpecif")
        CustomTextView productSpecif;
        @BindByTag("standingCrop")
        CustomTextView standingCrop;

        public RefProductViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initListener() {
            super.initListener();
            layout_refproduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RefProductViewHolder.this.onItemChildViewClick(v, 0);
//                    onItemChildViewClick(v,0,getItem(getAdapterPosition()));
                }
            });
        }

        @Override
        protected int layoutId() {
            return R.layout.item_refproduct;
        }

        @Override
        protected void update(SparePartRefEntity data) {
            Good good = data.getProductID();
            if (good == null) return;
            productName.setText(String.format("名称(编码):        %s(%s)", good.productName, good.productCode));
            productSpecif.setContent(String.format("规格(型号):        %s(%s)", Util.strFormat(good.productSpecif), Util.strFormat(good.productModel)));
            standingCrop.setContent(String.format("现存量(单价):    %s(%s)", Util.strFormat(data.getStandingCrop()),Util.strFormat(good.productCostPrice)));
        }
    }

}
