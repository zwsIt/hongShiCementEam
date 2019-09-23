package com.supcon.mes.module_txl.ui.fragment;

import android.support.v4.app.Fragment;

/**
 * @Author xushiyun
 * @Create-time 8/2/19
 * @Pageage com.supcon.mes.module_txl.ui.fragment
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public class SameDepartmentFragment extends BaseTxlSearchFragment {
    private TxlSearchListWithPinyinFragment mTxlSearchListWithPinyinFragment = new TxlSearchListWithPinyinFragment();
    @Override
    protected Fragment getFirstFragment() {
        return mTxlSearchListWithPinyinFragment;
    }
    
    @Override
    protected void onInit() {
        super.onInit();
    }
    
    @Override
    protected String titleText() {
        return "标题";
//        return getArguments().getString(Constant.IntentKey.TITLE_CONTENT);
    }
}
