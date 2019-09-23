package com.supcon.mes.module_txl.ui.fragment;

import android.support.v4.app.Fragment;

/**
 * @Author xushiyun
 * @Create-time 7/25/19
 * @Pageage com.supcon.mes.module_txl.ui.fragment
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public class TxlListFragment extends BaseTxlSearchFragment {
    @Override
    protected String titleText() {
        return "通讯录";
    }
    
    @Override
    protected Fragment getFirstFragment() {
        return new TxlSearchListFragment();
    }
}
