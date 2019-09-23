package com.supcon.mes.module_txl.ui.adapter;

import android.content.Context;
import android.view.View;

import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.middleware.model.inter.ITxlEntity;
import com.supcon.mes.module_txl.controller.TxlListItemViewController;

/**
 * @Author xushiyun
 * @Create-time 7/11/19
 * @Pageage com.supcon.mes.module_txl.ui.adapter
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public class TxlListAdapter extends BaseListDataRecyclerViewAdapter<ITxlEntity> {
    private TxlListItemViewController mTxlListItemViewController = new TxlListItemViewController(new View(context));
    
    public TxlListAdapter(Context context) {
        super(context);
    }
    
    @Override
    protected BaseRecyclerViewHolder getViewHolder(int viewType) {
        return new TxlListViewHolder(context);
    }
    
    public class TxlListViewHolder extends BaseRecyclerViewHolder<ITxlEntity> {
        private TxlListItemViewController txlListItemViewController;
        
        public TxlListViewHolder(Context context) {
            super(context,parent);
        }
        
        @Override
        protected int layoutId() {
            return mTxlListItemViewController.layout();
        }
        
        @Override
        protected void update(ITxlEntity data) {
            txlListItemViewController = mTxlListItemViewController.newInstance();
            txlListItemViewController.attachView(itemView);
            txlListItemViewController.inject(data);
        }
    }
}
