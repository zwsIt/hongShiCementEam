package com.supcon.mes.module_sbda.ui.adapter;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.middleware.model.bean.UserInfo;
import com.supcon.mes.module_sbda.R;


/**
 * Created by Xushiyun on 2018/5/22.
 * Email:ciruy_victory@gmail.com
 */

public class CommonSearchAdapter extends BaseListDataRecyclerViewAdapter<UserInfo> {
    @Override
    protected BaseRecyclerViewHolder<UserInfo> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    public CommonSearchAdapter(Context context) {
        super(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<UserInfo> implements View.OnClickListener {
        @BindByTag("name")
        TextView name;

        @BindByTag("id")
        TextView id;

        ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(this);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_common_search;
        }

        @Override
        protected void update(UserInfo data) {
            if (data != null && !TextUtils.isEmpty(data.name) && name != null)
                name.setText(data.name);
            if (data != null && id != null)
                id.setText(data.staff.code+"");
            setViewColor(data);
        }


        @Override
        public void onClick(View v) {
            changeViewBoxColor();
            onItemChildViewClickListener.onItemChildViewClick(v,getAdapterPosition(), 0, getItem(getAdapterPosition()));
        }

        private void changeViewBoxColor() {
            final UserInfo userInfo = getItem(getAdapterPosition());
            userInfo.clicked = !userInfo.clicked;
            setViewColor(userInfo);
        }

        private void setViewColor(UserInfo userInfo) {
            if(userInfo.clicked) {
                setTextColor(R.color.SBDAListBlue, name, id);
                itemView.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.eam_type_blue));
            }else {
                setTextColor(R.color.hintColor, id);
                setTextColor(R.color.textColorlightblack, name);
                itemView.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.eam_type_gray));
            }
        }


        private void setTextColor(@ColorRes int color, TextView... textViews) {
            for (TextView textView:textViews) {
                textView.setTextColor(itemView.getContext().getResources().getColor(color));
            }
        }
    }
}
