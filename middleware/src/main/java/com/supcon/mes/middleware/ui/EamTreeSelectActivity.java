package com.supcon.mes.middleware.ui;

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
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonSearchEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.ui.fragment.EamPortalSelectFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @Author zhangwenshuai
 * @Date 2020/05/25
 * @Desc 设备层级选择(区域位置)
 */
@Router(value = Constant.Router.EAM_TREE_SELECT)
public class EamTreeSelectActivity extends BaseMultiFragmentActivity {

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    CustomImageButton rightBtn;
    @BindByTag("searchView")
    CustomSearchView searchView;

    private EamPortalSelectFragment mEamPortalSelectFragment; // 设备选择门户入口
//    private ContactSearchFragment mContactSearchFragment;
//    private ContactCommonFragment mContactCommonFragment;

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
        mEamPortalSelectFragment = new EamPortalSelectFragment();
//        mContactSearchFragment = new ContactSearchFragment();
//        mContactCommonFragment = new ContactCommonFragment();
        fragments.add(mEamPortalSelectFragment);
//        fragments.add(mContactCommonFragment);
//        fragments.add(mContactSearchFragment);
    }


    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("设备选择");
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
        return R.layout.ac_main_tree_select;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxTextView.textChanges(searchView.editText())
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {

                        if (TextUtils.isEmpty(charSequence)) {
                            showFragment(0);
                        } else {
                            doSearch(charSequence.toString());
                        }
                    }
                });

        RxView.clicks(leftBtn)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        back();
                    }
                });

        RxView.clicks(rightBtn)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {


                        Map<String, ContactEntity> contactEntityMap = null;
//                        if (selectIndex == 0) {
//                            contactEntityMap = mEamPortalSelectFragment.getContactAdapter().getSelectStaffs();
//
//                        } else if (selectIndex == 1) {
//                            contactEntityMap = mContactCommonFragment.getContactAdapter().getSelectStaffs();
//                        } else if (selectIndex == 2) {
//                            contactEntityMap = mContactSearchFragment.getContactAdapter().getSelectStaffs();
//                        }


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
                    }
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
//                        mContactSearchFragment.doSearch(searchContent);
                    }
                });

    }


    public void showFragment(int selectIndex, String title) {
        showFragment(selectIndex);
        if (selectIndex == 1) {
//            mContactSearchFragment.setTitle(title);
//            mContactCommonFragment.setTitle(title);
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
