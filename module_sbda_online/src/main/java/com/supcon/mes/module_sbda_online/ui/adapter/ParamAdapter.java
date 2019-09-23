package com.supcon.mes.module_sbda_online.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_sbda_online.R;
import com.supcon.mes.module_sbda_online.model.bean.LubriEntity;
import com.supcon.mes.module_sbda_online.model.bean.ParamEntity;

import java.text.SimpleDateFormat;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class ParamAdapter extends BaseListDataRecyclerViewAdapter<ParamEntity> {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ParamAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<ParamEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<ParamEntity> {

        @BindByTag("paramName")
        CustomTextView paramName;
        @BindByTag("paramValue")
        CustomTextView paramValue;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_param;
        }

        @Override
        protected void update(ParamEntity data) {
            paramName.setContent(Util.strFormat2(data.paramName));
            paramValue.setContent(Util.strFormat2(data.paramValue) + (TextUtils.isEmpty(data.unit) ? "" : data.unit));
        }
    }
}
