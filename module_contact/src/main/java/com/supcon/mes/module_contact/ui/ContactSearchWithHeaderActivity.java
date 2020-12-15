package com.supcon.mes.module_contact.ui;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseMultiFragmentActivity;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_contact.R;
import com.supcon.mes.module_contact.ui.fragment.ContactCommonFragment;
import com.supcon.mes.module_contact.ui.fragment.ContactSearchFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @Author xushiyun
 * @Create-time 8/2/19
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc 带悬浮首字母标题界面开发工作
 */
@Router(Constant.Router.CONTACT_SEARCH_WITH_HEADER)
public class ContactSearchWithHeaderActivity extends BaseMultiFragmentActivity {

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("searchView")
    CustomSearchView searchView;

    ContactSearchFragment mContactSearchFragment;

    @Override
    protected void onInit() {
        super.onInit();

    }

    @Override
    public int getFragmentContainerId() {
        return R.id.fragmentLayout;
    }

    @Override
    public void createFragments() {
        mContactSearchFragment = new ContactSearchFragment();
        fragments.add(new ContactCommonFragment());
        fragments.add(mContactSearchFragment);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        String title = getIntent().getStringExtra(Constant.IntentKey.TITLE_CONTENT);
        if (!TextUtils.isEmpty(title)) {
            titleText.setText(title);
        } else {
            titleText.setText("搜索");
        }

        String searchContent = getIntent().getStringExtra(Constant.IntentKey.SEARCH_CONTENT);
        showFragment(1);
        if (TextUtils.isEmpty(searchContent)) {

            showFragment(0);
        } else {
            searchView.setInput(searchContent);
            doSearch(searchContent);
        }

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxTextView.textChanges(searchView.editText())
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> {

                    if (TextUtils.isEmpty(charSequence)) {
                        showFragment(0);
                    } else {
                        doSearch(charSequence.toString());
                    }
                });

//        searchView.editText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(!hasFocus){
//                    showFragment(1);
//                }
//            }
//        });

        RxView.clicks(leftBtn)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(o -> back());
    }

    @SuppressLint("CheckResult")
    private void doSearch(String searchContent) {
        Flowable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    showFragment(1);
                    mContactSearchFragment.doSearch(searchContent);
                });

    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_contact_search;
    }
}
