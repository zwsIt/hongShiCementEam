package com.supcon.mes.middleware.ui;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseVPFragmentActivity;
import com.supcon.common.view.base.fragment.BaseFragment;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.mbap.view.NoScrollViewPager;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.factory.SearchContentFactory;
import com.supcon.mes.middleware.ui.fragment.CommonFilterSearchListFragment;
import com.supcon.mes.middleware.ui.fragment.CommonFilterSearchTabFragment;
import com.supcon.mes.middleware.util.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/11/2113:16
 */
@Router(Constant.Router.COMMON_FILETER_SEARCH)
public class CommonFilterSearchActivity extends BaseVPFragmentActivity {
    /**
     * 右侧取消按钮
     */
    @BindByTag("rightBtnCancel")
    TextView rightBtnCancel;
    /**
     * 左侧搜索框
     */
    @BindByTag("filterSearchView")
    CustomSearchView filterSearchView;

    @BindByTag("searchContentVp")
    NoScrollViewPager searchContentVp;

    CommonFilterSearchListFragment mCommonFilterSearchListFragment;
    CommonFilterSearchTabFragment mCommonFilterSearchTabFragment;

    SearchContentFactory.FilterType mFilterType = SearchContentFactory.FilterType.DEVICE;

    Map<String, Object> param;

    public SearchContentFactory.FilterType getFilterType() {
        return mFilterType;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    @Override
    public void refresh() {
        super.refresh();
        for (BaseFragment fragment : fragments) fragment.refresh();
    }

    @Override
    protected void onInit() {
        super.onInit();
        mFilterType = (SearchContentFactory.FilterType) getIntent().getSerializableExtra(Constant.IntentKey.FILTER_SEARCH_TYPE);
        if (null == mFilterType) {
            mFilterType = SearchContentFactory.FilterType.DEVICE;
        }
        param = Util.gsonToMaps(getIntent().getStringExtra(Constant.IntentKey.FILTER_SEARCH_PARAM));
        if (null == param) {
            param = new HashMap<>(0);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        searchContentVp.setNoScroll(true);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_common_filter_search;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        RxView.clicks(rightBtnCancel)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> back());

        RxTextView.textChanges(filterSearchView.editText())
                .skipInitialValue()
                .debounce(1, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        searchContentVp.setCurrentItem(0, false);
                        return;
                    }
                    param.put(Constant.FilterSearchParam.DEVICE_BLUR, charSequence);
                    refresh();
                    searchContentVp.setCurrentItem(1, false);
                });
    }

    @Override
    public int getViewPagerId() {
        return R.id.searchContentVp;
    }

    @Override
    public void createFragments() {
        mCommonFilterSearchTabFragment = new CommonFilterSearchTabFragment();
        mCommonFilterSearchListFragment = new CommonFilterSearchListFragment();
        fragments.add(0, mCommonFilterSearchTabFragment);
        fragments.add(1, mCommonFilterSearchListFragment);
    }
}
