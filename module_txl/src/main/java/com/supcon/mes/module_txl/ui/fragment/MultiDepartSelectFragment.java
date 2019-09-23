package com.supcon.mes.module_txl.ui.fragment;

import android.support.v4.app.Fragment;

import com.supcon.mes.middleware.ui.fragment.BaseInputToggleSearchFragment;

/**
 * @Author xushiyun
 * @Create-time 7/26/19
 * @Pageage com.supcon.mes.middleware.ui.fragment
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public class MultiDepartSelectFragment extends BaseInputToggleSearchFragment {
    @Override
    protected Fragment getFirstFragment() {
        return new MultiStageFragment();
    }
    
    @Override
    protected Fragment getSecondFragment() {
        return new TxlSearchListFragment();
    }
    
    @Override
    protected String titleText() {
        return null;
    }
}
