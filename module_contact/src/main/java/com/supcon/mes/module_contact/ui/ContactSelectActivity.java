package com.supcon.mes.module_contact.ui;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseMultiFragmentActivity;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonSearchEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.module_contact.R;
import com.supcon.mes.module_contact.ui.fragment.ContactCommonFragment;
import com.supcon.mes.module_contact.ui.fragment.ContactSearchFragment;
import com.supcon.mes.module_contact.ui.fragment.ContactSelectFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @Author xushiyun
 * @Create-time 7/22/19
 * @Pageage com.supcon.mes.middleware.ui
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
@Router(value = Constant.Router.CONTACT_SELECT)
public class ContactSelectActivity extends BaseMultiFragmentActivity {

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    CustomImageButton rightBtn;
    @BindByTag("searchView")
    CustomSearchView searchView;

    private ContactSearchFragment mContactSearchFragment;
    private ContactCommonFragment mContactCommonFragment;
    private ContactSelectFragment mContactSelectFragment;

    private String searchTag;

    @Override
    protected void onInit() {
        super.onInit();
        searchTag = getIntent().getStringExtra(Constant.IntentKey.COMMON_SEARCH_TAG);
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.fragmentLayout;
    }

    @Override
    public void createFragments() {
        mContactSelectFragment = new ContactSelectFragment();
        mContactSearchFragment = new ContactSearchFragment();
        mContactCommonFragment = new ContactCommonFragment();
        fragments.add(mContactSelectFragment);
        fragments.add(mContactCommonFragment);
        fragments.add(mContactSearchFragment);
    }


    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("选人");
        boolean isMulti = getIntent().getBooleanExtra(Constant.IntentKey.IS_MULTI, false);
        if (isMulti) {
            rightBtn.setVisibility(View.VISIBLE);
            rightBtn.setImageResource(R.drawable.sl_top_submit);
        }
        showFragment(2);
//        showFragment(1);
        showFragment(0);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_contact_select;
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

        RxView.clicks(leftBtn)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(o -> back());

        RxView.clicks(rightBtn)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(o -> {

                    Map<String, ContactEntity> contactEntityMap = null;
                    if (selectIndex == 0) {
                        contactEntityMap = mContactSelectFragment.getContactAdapter().getSelectStaffs();

                    } else if (selectIndex == 1) {
                        contactEntityMap = mContactCommonFragment.getContactAdapter().getSelectStaffs();
                    } else if (selectIndex == 2) {
                        contactEntityMap = mContactSearchFragment.getContactAdapter().getSelectStaffs();
                    }

                    if (contactEntityMap == null) {
                        ToastUtils.show(context, "请选择人员！");
                    }

                    CommonSearchEvent commonSearchEvent = new CommonSearchEvent();
                    commonSearchEvent.flag = searchTag;
                    List<CommonSearchEntity> list = new LinkedList<>();
                    for (Map.Entry<String, ContactEntity> entry : contactEntityMap.entrySet()) {
                        ContactEntity value = entry.getValue();
                        CommonSearchStaff commonSearchStaff = new CommonSearchStaff();
                        commonSearchStaff.id = value.getSTAFFID();
                        commonSearchStaff.code = value.getCODE();
                        commonSearchStaff.name = value.getNAME();
                        commonSearchStaff.pinyin = value.getSearchPinyin();
                        commonSearchStaff.department = value.getDEPARTMENTNAME();
                        commonSearchStaff.mainPosition = value.getPOSITIONNAME();
                        list.add(commonSearchStaff);
                    }
                    commonSearchEvent.mCommonSearchEntityList = list;

                    EventBus.getDefault().post(commonSearchEvent);
                    finish();
                });

    }

    @SuppressLint("CheckResult")
    private void doSearch(String searchContent) {

        Flowable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        showFragment(2);
                        mContactSearchFragment.doSearch(searchContent);
                    }
                });

    }


    public void showFragment(int selectIndex, String title) {
        showFragment(selectIndex);
        if (selectIndex == 1) {
            mContactSearchFragment.setTitle(title);
            mContactCommonFragment.setTitle(title);
        }

    }

    @Override
    public void onBackPressed() {

        if (selectIndex != 0) {
            showFragment(0);
            return;
        }

        super.onBackPressed();
    }
}
