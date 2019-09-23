package com.supcon.mes.middleware.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
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
        TextView productSpecif;
        @BindByTag("productCode")
        TextView productCode;

        @BindByTag("eamIc")
        ImageView eamIc;

        public RefProductViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initListener() {
            super.initListener();
            layout_refproduct.setOnClickListener(v -> onItemChildViewClick(v, 0));
        }

        @Override
        protected int layoutId() {
            return R.layout.item_refproduct;
        }

        @Override
        protected void update(SparePartRefEntity data) {
            Good good = data.getProductID();
            eamIc.setImageResource(R.drawable.ic_sparepart);
            productName.setText(String.format(context.getResources().getString(R.string.device_style2), "物品名称:", Util.strFormat(good.productName)));
            productSpecif.setText(String.format(context.getResources().getString(R.string.device_style2), "规格:", Util.strFormat(good.productSpecif)));
            productCode.setText(String.format(context.getResources().getString(R.string.device_style2), "编码:", Util.strFormat(good.productCode)));
        }
    }

}
