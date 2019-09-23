package com.supcon.mes.module_txl.ui.fragment;

import android.support.v4.app.Fragment;

/**
 * @Author xushiyun
 * @Create-time 7/26/19
 * @Pageage com.supcon.mes.middleware.ui.fragment
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
//@Presenter(MultiDepartSelectPresenter.class)
public class MultiStageFragment extends BaseTxlSearchFragment
//        implements MultiDepartSelectContract.View
{
    private MultiStageViewFragment mMultiStageViewFragment = new MultiStageViewFragment();
    @Override
    protected Fragment getFirstFragment() {
        return mMultiStageViewFragment;
    }
    
    @Override
    protected String titleText() {
        return "组织";
    }
}
