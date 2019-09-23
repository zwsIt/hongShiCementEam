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
import com.supcon.mes.middleware.model.bean.RefLubricateEntity;
import com.supcon.mes.middleware.util.Util;

/**
 * @author yangfei.cao
 * @ClassName RefLubricateAdapter
 * @date 2018/9/5
 * 润滑油添加选择列表Adapter
 * ------------- Description -------------
 */
public class RefLubricateAdapter extends BaseListDataRecyclerViewAdapter<RefLubricateEntity> {
    public RefLubricateAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<RefLubricateEntity> getViewHolder(int viewType) {
        return new RefProductViewHolder(context);
    }

    class RefProductViewHolder extends BaseRecyclerViewHolder<RefLubricateEntity> {
        @BindByTag("layout_refproduct")
        RelativeLayout layout_refproduct;
        @BindByTag("oilName")
        CustomTextView oilName;
        @BindByTag("sum")
        CustomTextView sum;
        @BindByTag("oilType")
        CustomTextView oilType;

        public RefProductViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initListener() {
            super.initListener();
            layout_refproduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemChildViewClick(v, 0);
                }
            });
        }

        @Override
        protected int layoutId() {
            return R.layout.item_lubricate;
        }

        @Override
        protected void update(RefLubricateEntity data) {
            oilName.setContent(data.getLubricateOil().name);
            sum.setContent(Util.big2(data.sum));
            oilType.setContent(data.getOilType().value);
        }
    }

}
