package com.supcon.mes.middleware.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonSearchEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.ui.fragment.EamPortalSelectFragment;
import com.supcon.mes.middleware.ui.fragment.EamSearchSelectFragment;
import com.supcon.mes.middleware.util.NFCHelper;
import com.supcon.mes.middleware.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private EamSearchSelectFragment mEamSearchSelectFragment; // 设备搜索入口
//    private ContactCommonFragment mContactCommonFragment;

    private String searchTag;
    private NFCHelper mNFCHelper;
    private boolean mIsCard;
    public void setCard(boolean card) {
        mIsCard = card;
    }
    public boolean isCard() {
        return mIsCard;
    }
    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        searchTag = getIntent().getStringExtra(Constant.IntentKey.COMMON_SEARCH_TAG);

        mNFCHelper = NFCHelper.getInstance();
        mNFCHelper.setup(this);
        mNFCHelper.setOnNFCListener(nfc -> {
            Map<String, Object> nfcJson = Util.gsonToMaps(nfc);
            if (nfcJson.get("textRecord") == null) {
                ToastUtils.show(context, "标签内容空！");
                return;
            }
            mIsCard = true;
            searchView.setInput((String) nfcJson.get("textRecord"));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNFCHelper != null)
            mNFCHelper.onResumeNFC(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNFCHelper != null)
            mNFCHelper.onPauseNFC(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //获取到Tag对象
        if (mNFCHelper != null)
            mNFCHelper.dealNFCTag(intent);
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.fragmentLayout;
    }

    @Override
    public void createFragments() {
        mEamPortalSelectFragment = new EamPortalSelectFragment();
        mEamSearchSelectFragment = new EamSearchSelectFragment();
//        mContactCommonFragment = new ContactCommonFragment();
        fragments.add(mEamPortalSelectFragment);
        fragments.add(mEamSearchSelectFragment);
//        fragments.add(mContactCommonFragment);

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
        showFragment(1);
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
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        showFragment(0);
                    } else {
                        doSearch(charSequence.toString());
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
                        Map<String, EamEntity> contactEntityMap = null;
//                        if (selectIndex == 0) {
//                            contactEntityMap = mEamPortalSelectFragment.getContactAdapter().getSelectStaffs();
//
//                        } else if (selectIndex == 1) {
//                            contactEntityMap = mContactCommonFragment.getContactAdapter().getSelectStaffs();
//                        } else if (selectIndex == 2) {
//                            contactEntityMap = mContactSearchFragment.getContactAdapter().getSelectStaffs();
//                        }


                        if (contactEntityMap == null) {
                            ToastUtils.show(context, "请选择设备！");
                        }

                        CommonSearchEvent commonSearchEvent = new CommonSearchEvent();
                        commonSearchEvent.flag = searchTag;
                        List<CommonSearchEntity> list = new LinkedList<>();
                        for (Map.Entry<String, EamEntity> entry : contactEntityMap.entrySet()) {
                            EamEntity value = entry.getValue();
                            list.add(value);
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
                        showFragment(1);
                        mEamSearchSelectFragment.doSearch(searchContent);
                    }
                });
    }

    public void showFragment(int selectIndex, String title) {
        showFragment(selectIndex);
//        if (selectIndex == 1) {
//            mContactSearchFragment.setTitle(title);
//            mContactCommonFragment.setTitle(title);
//        }
    }

    @Override
    public void onBackPressed() {
        if (selectIndex != 0) {
            showFragment(0);
            return;
        }
        super.onBackPressed();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mNFCHelper != null) {
            mNFCHelper.release();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEntity(CommonSearchEvent commonSearchEvent) {
        // 全部设备选择返回后 销毁当前页面
        EamEntity eamEntity = (EamEntity) commonSearchEvent.commonSearchEntity;
        eamEntity.updateTime = System.currentTimeMillis();
        EamApplication.dao().getEamEntityDao().save(eamEntity);
        finish();
    }


}
