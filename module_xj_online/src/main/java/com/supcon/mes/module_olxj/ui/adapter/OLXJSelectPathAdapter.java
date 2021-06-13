package com.supcon.mes.module_olxj.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.model.bean.OLXJTaskEntity;

/**
 * @Author dengbufei
 * @Create-time 2020/6/24
 * @Project ZSCementEam
 * @Package com.supcon.mes.module_olxj.ui.adapter
 * @Desc
 */

public class OLXJSelectPathAdapter extends BaseListDataRecyclerViewAdapter<OLXJTaskEntity> {
    public OLXJSelectPathAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<OLXJTaskEntity> getViewHolder(int viewType) {
        return new SelectPathViewHolder(context,parent);
    }

    class SelectPathViewHolder extends BaseRecyclerViewHolder<OLXJTaskEntity> {

        @BindByTag("tv_name")
        TextView tv_name;
        @BindByTag("iv_method_select")
        ImageView iv_method_select;
        @BindByTag("ll_root")
        LinearLayout ll_root;

        public SelectPathViewHolder(Context context, ViewGroup parent) {
            super(context,parent);
        }

        @Override
        protected void initListener() {
            super.initListener();
            ll_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    OLXJTaskEntity olxjTaskEntity = getItem(position);
                    onItemChildViewClick(v, 0, olxjTaskEntity);
                }
            });
        }

        @Override
        protected int layoutId() {
            return R.layout.item_xj_select_path;
        }

        @Override
        protected void update(OLXJTaskEntity data) {
            if (!TextUtils.isEmpty(data.workGroupID.name)) {
                tv_name.setText(data.workGroupID.name);
            }
            if (data.isSelect) {
                iv_method_select.setImageResource(R.drawable.ic_check_yes);
            } else {
                iv_method_select.setImageResource(R.drawable.ic_check_no);
            }
        }
    }
}
