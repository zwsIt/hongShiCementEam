package com.supcon.mes.module_main.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.supcon.mes.middleware.model.bean.WorkInfo;
import com.supcon.mes.module_main.ui.view.ItemTouchHelperListener;

import java.util.List;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG;

/**
 * @Description:
 * @Author: zhangwenshuai
 * @CreateDate: 2020/6/9 16:45
 */
public class ItemDragCallback extends ItemTouchHelper.Callback {

    ItemTouchHelperListener mItemTouchHelperListener;
    Context mContext;

    ItemDragCallback(Context context) {
        this.mContext = context;
    }

    //决定拖拽/滑动的方向
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        if (mContext instanceof MainMenuActivity){
            if (position >= ((MainMenuActivity)mContext).myMenuList.size()) {
                return 0;
            }
        }
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    //和位置交换有关,可用于实现drag功能
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();   //拖动的position
        int toPosition = target.getAdapterPosition();     //释放的position
        if (mContext instanceof ItemTouchHelperListener) {
            ((ItemTouchHelperListener)mContext).onItemMove(viewHolder,fromPosition, toPosition);
        }
        return true;
    }

    //和滑动有关,可用于实现swipe功能
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (mContext instanceof ItemTouchHelperListener) {
            ((ItemTouchHelperListener)mContext).onItemDismiss(viewHolder);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        if (dX != 0 && dY != 0 || isCurrentlyActive) {
//        }
    }

    //和目标View的状态改变有关,例如drag,swipe,ide
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && mContext instanceof ItemTouchHelperListener)
            ((ItemTouchHelperListener)mContext).onItemSelect(viewHolder);
    }

    //和移除View的状态有关,通常用于清除在onSelectedChanged,onChildDraw中对View设置的动画
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (mContext instanceof ItemTouchHelperListener) {
            ((ItemTouchHelperListener)mContext).onItemClear(viewHolder);
        }
    }
    //是否长按启用拖拽功能,默认是true
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    //是否支持滑动,默认true
    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

}
