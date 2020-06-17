package com.supcon.mes.module_main.ui.view;

import android.support.v7.widget.RecyclerView;

public interface ItemTouchHelperListener {
    //数据交换
    void onItemMove(RecyclerView.ViewHolder holder, int fromPosition, int targetPosition);
    //数据删除
    void onItemDismiss(RecyclerView.ViewHolder holder);
    //drag或者swipe选中
    void onItemSelect(RecyclerView.ViewHolder holder);
    //状态清除
    void onItemClear(RecyclerView.ViewHolder holder);

}
