package com.supcon.mes.module_sbda.ui;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BasePresenterActivity;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.utils.KeyboardUtil;
import com.supcon.mes.mbap.utils.ScanInputHelper;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.constant.Module;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.model.event.DeviceAddEvent;
import com.supcon.mes.middleware.model.event.SynDeviceEevent;
import com.supcon.mes.middleware.util.DeviceManager;
import com.supcon.mes.middleware.util.SearchTitleBarHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_sbda.R;
import com.supcon.mes.module_sbda.model.api.SearchDeviceAPI;
import com.supcon.mes.module_sbda.model.bean.SearchDeviceEntity;
import com.supcon.mes.module_sbda.model.contract.SearchDeviceContract;
import com.supcon.mes.module_sbda.presenter.SearchDevicePresenter;
import com.supcon.mes.module_sbda.ui.fragment.AddDeviceFragment;
import com.supcon.mes.module_sbda.ui.fragment.CommonSearchDeviceFragment;
import com.supcon.mes.module_sbda.util.FragmentUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wangshizhan on 2017/8/11.
 */

@Router(Constant.Router.ADD_DEVICE)
@Presenter(value = {SearchDevicePresenter.class})
public class AddDeviceActivity extends BasePresenterActivity implements SearchDeviceContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("addDeviceChooseNum")
    TextView addDeviceChooseNum;

    @BindByTag("customSearchView")
    CustomSearchView titleSearchView;

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;

    @BindByTag("frameLayoutContent")
    FrameLayout frameLayoutContent;
    @BindByTag("rlContent")
    RelativeLayout rl_content;

    @BindByTag("bottomEnsureBar")
    LinearLayout bottomEnsureBar;

    private String module;

    private AddDeviceFragment addDeviceFragment;
    private CommonSearchDeviceFragment commonSearchDeviceFragment;


    private final List<CommonDeviceEntity> mSelectDevice = new ArrayList<>();
    private final Set<String> selectDeviceSet = new HashSet<>();

    private boolean isSingle = false;


    @Override
    protected void onInit() {
        super.onInit();
        module = getIntent().getStringExtra(Constant.IntentKey.MODULE);
        isSingle = Module.Fault.name().equals(module);
        EventBus.getDefault().register(this);
    }

    public String getModule() {
        return module;
    }

    public void addDevice(CommonDeviceEntity commonDeviceEntity) {
        if (!selectDeviceSet.contains(commonDeviceEntity.eamCode)) {
            selectDeviceSet.add(commonDeviceEntity.eamCode);
        }
        if (!mSelectDevice.contains(commonDeviceEntity))
            mSelectDevice.add(commonDeviceEntity);
        updateNum();
    }

    public void sendDevice(CommonDeviceEntity commonDeviceEntity) {
        mSelectDevice.add(commonDeviceEntity);
        EventBus.getDefault().post(new DeviceAddEvent(mSelectDevice.toString()));
        DeviceManager.getInstance().updateDatabase();
        finish();
//        updateRecentDeviceCache();
    }


    private void updateNum() {

        if (mSelectDevice.size() != 0) {
            addDeviceChooseNum.setTextColor(getResources().getColor(R.color.textColorlightblack));
        } else {
            addDeviceChooseNum.setTextColor(getResources().getColor(R.color.hintColor));
        }
        addDeviceChooseNum.setText(String.format(getResources().getString(R.string.device_num_choose), "" + mSelectDevice.size()));
    }

    public void deleteDevice(CommonDeviceEntity commonDeviceEntity) {
        //删除设备触发事件,根据类似id的eamCode来进行数据的删除
        if (selectDeviceSet.contains(commonDeviceEntity.eamCode)) {
            selectDeviceSet.remove(commonDeviceEntity.eamCode);
        }
//        if (mSelectDevice.contains(runStateDeviceEntity)) {
//            mSelectDevice.remove(runStateDeviceEntity);
        operateSelectDevice(commonDeviceEntity);
//        }
        updateNum();
    }

    private void operateSelectDevice(CommonDeviceEntity commonDeviceEntity) {
        for (int i = 0; i < mSelectDevice.size(); i++) {
            if (Objects.equals(mSelectDevice.get(i).eamCode, commonDeviceEntity.eamCode)) {
                mSelectDevice.remove(i);
            }
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_sbda_add_device;
    }

    @Override
    protected void initView() {
        super.initView();

        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleSearchView.setInputTextColor(getResources().getColor(R.color.textColor5251));
        searchTitleBar.setTitleText("添加设备");
        initFragments();

        searchTitleBar.disableRightBtn();
        searchTitleBar.enableRemainMode();
        searchTitleBar.setDisplayCallBack(new CustomHorizontalSearchTitleBar.DisplayCallback() {
            @Override
            public void onShow() {
                displaySearchFragment(Boolean.TRUE);
            }

            @Override
            public void onClickSearchButton() {
                AddDeviceActivity.this.hideSearchKeyboard();
            }

            @Override
            public void onCancel() {
                displaySearchFragment(Boolean.FALSE);
            }
        });
        //单选模式不需要显示对应的底部状态栏
//        bottomEnsureBar.setVisibility(isSingle? View.GONE:View.VISIBLE);
    }

    private void initFragments() {
        addDeviceFragment = new AddDeviceFragment();
        commonSearchDeviceFragment = new CommonSearchDeviceFragment();
        FragmentUtil.addFragments(getSupportFragmentManager(),R.id.frameLayoutContent, addDeviceFragment, commonSearchDeviceFragment);
        displaySearchFragment(false);
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onReceiveSearchDevice(SearchDeviceEntity searchDeviceEntity) {
        final CommonDeviceEntity commonDeviceEntity = searchDeviceEntity.result.get(0);
        addDevice(commonDeviceEntity);
        SynDeviceEevent synDeviceEevent = new SynDeviceEevent(commonDeviceEntity);
        synDeviceEevent.setChecked(Boolean.TRUE);
        EventBus.getDefault().post(synDeviceEevent);
    }

    public void displaySearchFragment(boolean flag) {
        if (searchTitleBar.getStatus() != flag) searchTitleBar.toggle();
        FragmentUtil.transFragment(getSupportFragmentManager(),
                flag ? addDeviceFragment : commonSearchDeviceFragment,
                flag ? commonSearchDeviceFragment : addDeviceFragment);
    }

    @Override
    protected void initListener() {
        super.initListener();
        //点击返回按钮结束当前活动
        leftBtn.setOnClickListener(v -> finish());
        //确认添加设备按钮触发事件
        RxView.clicks(rootView.findViewById(R.id.addDeviceSureBtn))
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                            LogUtil.i("添加设备:" + mSelectDevice.toString());

                            if (mSelectDevice.size() != 0) {
                                EventBus.getDefault().post(new DeviceAddEvent(mSelectDevice.toString()));
                                //每次点击添加设备按钮,更新内存中最近添加数据信息
                                updateRecentDeviceCache();
                            } else {

                                SnackbarHelper.showError(rootView, "请选择设备!");
                            }


                        }
                );

//        RxView.clicks(titleSearchView.searchView())
//                .throttleFirst(2, TimeUnit.SECONDS)
//                .subscribe(o ->
//                        commonSearchDeviceFragment.doSearch(titleSearchView.getInput().trim())
////                        presenterRouter.create(SearchDeviceAPI.class).searchDevice(titleSearchView.getInput().trim())
//                );

        RxTextView.textChanges(titleSearchView.editText())
                .skipInitialValue()
                .debounce(1, TimeUnit.SECONDS)
                .subscribe(charSequence -> {
                    commonSearchDeviceFragment.doSearch(titleSearchView.getInput().trim());

                });

        titleSearchView.setOnListItemSelectedListener(new CustomSearchView.OnItemSelectedListener() {
            @Override
            public void onItemSelect(String s) {
                presenterRouter.create(SearchDeviceAPI.class).searchDevice(s);
            }
        });
//        RxView.clicks(searchTitleBar.title()).throttleFirst(2000, TimeUnit.MILLISECONDS)
//                .subscribe(o -> IntentRouter.go(AddDeviceActivity.this, Constant.Router.SBDA_SEARCH_CONTACT));
    }

    @Override
    protected void initData() {
        super.initData();

        initWords();
    }

    private void initWords() {

        Flowable.just(titleSearchView)
                .subscribeOn(Schedulers.newThread())
                .subscribe(customSearchView -> {
                    List<String> words = new ArrayList<>();
                    words.addAll(Arrays.asList(ScanInputHelper.getWordsArray()));
                    ((Activity) context).runOnUiThread(() -> customSearchView.initWords(words));
                });
    }

    private void updateRecentDeviceCache() {

        Flowable.fromIterable(mSelectDevice)
                .observeOn(Schedulers.io())
                .subscribe(commonDeviceEntity -> {
                    commonDeviceEntity.updateTime = System.currentTimeMillis();
                    commonDeviceEntity.frequency += 1;
                }, throwable -> {

                }, () -> {
                    EamApplication.dao().getCommonDeviceEntityDao().updateInTx(mSelectDevice);
                    finish();
                });


    }

    @Override
    public void searchDeviceSuccess(SearchDeviceEntity searchDeviceEntity) {
        LogUtil.i("searchDeviceEntity:" + searchDeviceEntity.toString());
        //当在搜索栏中输入文字并点击搜索时,ViewPager页面切换到第二页,mRecentDeviceFragment添加搜索结果中的所有设备信息
        if (searchDeviceEntity.result != null && searchDeviceEntity.result.size() != 0) {
//            yxjlTab.setCurrentTab(1);
            SearchTitleBarHelper.addDeviceList(searchDeviceEntity.result);
            EventBus.getDefault().post(searchDeviceEntity);
//            mRecentDeviceFragment.addDevices(searchDeviceEntity.result);
        }
    }

    @Override
    public void searchDeviceFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
    }

    public  void hideSearchKeyboard() {
        KeyboardUtil.hideKeyboard(this, searchTitleBar.editText());
    }

}
