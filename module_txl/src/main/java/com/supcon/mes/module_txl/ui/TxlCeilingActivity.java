package com.supcon.mes.module_txl.ui;

import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.mes.middleware.model.bean.TxlEntity;

/**
 * @Author xushiyun
 * @Create-time 7/29/19
 * @Pageage com.supcon.mes.module_txl.ui
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public class TxlCeilingActivity extends BaseRefreshRecyclerActivity<TxlEntity> {
    @Override
    protected IListAdapter<TxlEntity> createAdapter() {
        return null;
    }
    
    @Override
    protected int getLayoutID() {
        return 0;
    }
}
