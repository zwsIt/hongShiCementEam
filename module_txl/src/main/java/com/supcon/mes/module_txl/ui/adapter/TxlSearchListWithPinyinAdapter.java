package com.supcon.mes.module_txl.ui.adapter;

import android.content.Context;
import android.view.View;

import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.middleware.model.bean.TxlEntity;
import com.supcon.mes.module_txl.controller.TxlListItemViewController;

/**
 * @Author xushiyun
 * @Create-time 7/26/19
 * @Pageage com.supcon.mes.module_txl.ui.adapter
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public class TxlSearchListWithPinyinAdapter extends BaseListDataRecyclerViewAdapter<TxlEntity> {
    
    private TxlListItemViewController mTxlListItemViewController = new TxlListItemViewController(new View(context));
    public TxlSearchListWithPinyinAdapter(Context context) {
        super(context);
    }
    
    @Override
    protected BaseRecyclerViewHolder getViewHolder(int viewType) {
        return new HomeRecyclerViewHolder(context);
    }
    
    public class HomeRecyclerViewHolder extends BaseRecyclerViewHolder<TxlEntity> {
    
        private TxlListItemViewController txlListItemViewController;
    
        public HomeRecyclerViewHolder(Context context) {
            super(context, parent);
        }
        
        @Override
        protected int layoutId() {
            return mTxlListItemViewController.layout();
        }
        
        @Override
        protected void update(TxlEntity data) {
            txlListItemViewController = mTxlListItemViewController.newInstance();
            txlListItemViewController.attachView(itemView);
            txlListItemViewController.inject(data);
        }
    }
    
    
}
