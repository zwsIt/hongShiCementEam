package com.supcon.mes.middleware.ui.fragment;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.fragment.BasePresenterFragment;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.mbap.view.NoScrollViewPager;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.model.bean.IDoFilter;
import com.supcon.mes.middleware.model.bean.TxlSearchType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author xushiyun
 * @Create-time 7/24/19
 * @Pageage com.supcon.mes.middleware.ui.fragment
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public abstract class BaseInputToggleSearchFragment extends BasePresenterFragment {
    
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("searchView")
    CustomSearchView searchView;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("titleBarLayout")
    RelativeLayout titleBarLayout;
    @BindByTag("contentFragment")
    LinearLayout contentFragment;
    @BindByTag("noScrollViewPager")
    NoScrollViewPager noScrollViewPager;
    @BindByTag("fitInStatusBar")
    View fitInStatusBar;
    private String searMes;
    
    protected abstract Fragment getFirstFragment();
    
    protected abstract Fragment getSecondFragment();
    
    protected abstract String titleText();
    
    private List<Fragment> mFragments = new ArrayList<>();
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private List<String> searchType = Arrays.asList("员工编号", "员工姓名", "岗位名称");
    private TxlSearchType[] searchTypeP = TxlSearchType.values();
    
    private String typeName;
    private String typeValue;
    
    @Override
    protected int getLayoutID() {
        return R.layout.frag_base_input_toggle_search;
    }
    boolean fitInstatusBarEnable = false;
    public void fitInstatusBarEnable(Boolean enable){
        fitInstatusBarEnable = enable;
        if(fitInStatusBar!=null){
        fitInStatusBar.setVisibility(enable?View.VISIBLE:View.GONE);
        }
    }
    @Override
    protected void initView() {
        super.initView();
        titleBarLayout.setVisibility(View.GONE);
        titleText.setText(titleText());
        mFragments.addAll(Arrays.asList(getFirstFragment(), getSecondFragment()));
        mFragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mFragments.get(i);
            }
            
            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        noScrollViewPager.setAdapter(mFragmentPagerAdapter);
        searchView.setSelectTypeTextColorRes(R.color.black);
        searchView.setSelectTypeEnabled(true);
        searchView.setInputTextColor(R.color.black);
        searchView.setTypeSelectContent(Arrays.asList("员工编号", "员工姓名", "岗位名称"));
        searchView.setOnTypeSelectListener(type -> {
            searchView.setHint("搜索" + type);
            typeName = type;
            typeValue = searchTypeP[searchType.indexOf(typeName)].name();
            doFilter();
        });
        fitInStatusBar.setVisibility(fitInstatusBarEnable?View.VISIBLE:View.GONE);
    }
    
    
    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxTextView.textChanges(searchView.editText())
                .skipInitialValue()
                .observeOn(Schedulers.io())
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    noScrollViewPager.setCurrentItem(TextUtils.isEmpty(charSequence) ? 0 : 1);
                    if (!TextUtils.isEmpty(charSequence)) {
                        if (getSecondFragment() instanceof IDoFilter) {
                            searMes = charSequence.toString();
                            doFilter();
                        }
                    }
                });
        leftBtn.setOnClickListener(v -> Objects.requireNonNull(BaseInputToggleSearchFragment.this.getActivity()).onBackPressed());
        
    }
    
    private void doFilter() {
        ((IDoFilter) getSecondFragment()).doFilter(1, typeValue, searMes);
    }
}
