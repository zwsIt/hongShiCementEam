package com.supcon.mes.module_txl.ui.fragment;

import android.support.v4.app.Fragment;

import com.supcon.mes.middleware.ui.fragment.BaseInputToggleSearchFragment;

/**
 * @Author xushiyun
 * @Create-time 7/26/19
 * @Pageage com.supcon.mes.module_txl.ui.fragment
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public abstract class BaseTxlSearchFragment extends BaseInputToggleSearchFragment {
    private SearchTxlListFragment mSearchTxlListFragment = new SearchTxlListFragment();
    @Override
    protected Fragment getFirstFragment() {
        return null;
    }
    
    @Override
    protected Fragment getSecondFragment() {
        return mSearchTxlListFragment;
    }
    
    @Override
    protected String titleText() {
        return null;
    }
}
