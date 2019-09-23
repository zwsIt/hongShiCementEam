package com.supcon.mes.middleware.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.model.bean.TagEntity;
import com.supcon.mes.middleware.model.event.FilterSearchTabEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/11/2121:05
 */
public class TagAdapter extends BaseAdapter  {
    private final Context mContext;
    private final List<TagEntity> mDataList;
    private OnChildViewClickListener mOnChildViewClickListener;

    public void setOnChildViewClickListener(OnChildViewClickListener onChildViewClickListener) {
        this.mOnChildViewClickListener = onChildViewClickListener;
    }
    public TagAdapter(Context context) {
        this.mContext = context;
        mDataList = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tag_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_tag);
        TagEntity t = mDataList.get(position);
        if (t.isTagChecked()) {
            textView.setTextColor(mContext.getResources().getColor(R.color.filterTextBlue));
            textView.setBackgroundResource(R.drawable.sh_filter_light_blue);
        } else {
            textView.setTextColor(mContext.getResources().getColor(com.supcon.mes.mbap.R.color.textColorlightblack));
            textView.setBackgroundResource(R.drawable.sh_filter_gray);
        }
        textView.setText(t.getTagName());
        return view;
    }

    public void onlyAddAll(List datas) {
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    public void clearAndAddAll(List datas) {
        mDataList.clear();
        onlyAddAll(datas);
    }

}
