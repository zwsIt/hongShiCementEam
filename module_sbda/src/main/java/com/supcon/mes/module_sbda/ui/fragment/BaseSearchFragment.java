package com.supcon.mes.module_sbda.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.model.contract.BaseSearchContract;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Environment: hongruijun
 * Created by Xushiyun on 2018/5/22.
 * Todo: BaseSearchFragment, BaseSearchAdapter, BaseSearchEvent
 */
@SuppressWarnings("unused")
public abstract class BaseSearchFragment<TModel> extends BaseRefreshRecyclerFragment<TModel> implements BaseSearchContract.View {

//    protected abstract CustomHorizontalSearchTitleBar getTitleBar();

//    protected abstract RecyclerView getRecyclerView();


    protected abstract String setTitle();

    protected abstract int getLayoutID();

//
//    public void disableTitleBar() {
//        getTitleBar().setVisibility(View.GONE);
//    }
//
//    public void enableTitleBar() {
//        getTitleBar().setVisibility(View.VISIBLE);
//    }
//
//    // 是否允许右侧按钮
//    public void rightBtnStatus(boolean status) {
//        if (status) getTitleBar().enableRightBtn();
//        else getTitleBar().disableRightBtn();
//    }



    @Override
    protected void initData() {
        super.initData();
//        doSearch(getTitleBar().editText().getText().toString());
    }

    @Override
    protected void initView() {
        super.initView();
        initCallbacks();
//        initRecyclerView();
//        initTitleBar();
//        initButtons();
    }

    protected void initButtons() {

    }

//    private void initTitleBar() {
//        getTitleBar().setTitleText(setTitle());
//    }

    protected void initCallbacks() {
        final CustomHorizontalSearchTitleBar.DisplayCallback displayCallback = new CustomHorizontalSearchTitleBar.DisplayCallback() {
            @Override
            public void onShow() {
                onSearchTitleShow();
            }

            @Override
            public void onClickSearchButton() {

            }

            @Override
            public void onCancel() {
                onSearchTitleCancel();
            }
        };
//        getTitleBar().setDisplayCallBack(displayCallback);

        final CustomHorizontalSearchTitleBar.CallBack animationCallback = new CustomHorizontalSearchTitleBar.CallBack() {
            @Override
            public void beforeAnimation(boolean flag) {
                onBeforeAnimation(flag);
            }

            @Override
            public void duringAnimation(boolean flag) {
                onDuringAnimation(flag);
            }

            @Override
            public void afterAnimation(boolean flag) {
                onAfterAnimation(flag);
            }
        };
//        getTitleBar().setCallBack(animationCallback);
    }

    protected void onAfterAnimation(boolean flag) {
    }

    protected void onDuringAnimation(boolean flag) {
    }

    private void onBeforeAnimation(boolean flag) {
    }

    protected void onSearchTitleShow() {
    }

    protected void onSearchTitleCancel() {
    }

    @Override
    protected void initListener() {
        super.initListener();
//        RxView.clicks(getTitleBar().leftBtn()).throttleFirst(2000, TimeUnit.MILLISECONDS)
//                .subscribe(o -> onClickLeftBtn());
//
//        RxView.clicks(getTitleBar().searchView())
//                .subscribe(o -> onClickSearchView());
//
//        RxTextView.textChanges(getTitleBar().editText()).subscribe(charSequence -> onSearchMessageChanged(charSequence.toString()));
//        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
//            @Override
//            public void onRefresh(int pageIndex) {
//                doSearch(getTitleBar().editText().getText().toString(), pageIndex);
//            }
//        });
//        refreshListController.setOnRefreshListener(() -> doSearch(getTitleBar().editText().getText().toString(), 1));
    }


    protected void onSearchMessageChanged(String charSequence) {
//        doSearch(charSequence);
    }

    protected void onClickSearchView() {
    }

    protected void onClickLeftBtn() {
    }

    //这个方法在这里设置,如果需要实现另外的RecyclerView效果,完全可以重写这个方法
    protected void initRecyclerView() {
//        //这两个方法需要斟酌
//        refreshListController.setAutoPullDownRefresh(true);
//        refreshListController.setLoadMoreEnable(true);

//        getRecyclerView().setLayoutManager(new LinearLayoutManager(context));
//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_gap);
//        getRecyclerView().addItemDecoration(new SpaceItemDecoration(spacingInPixels));
    }

    protected abstract void doSearch(String charSequence, int pageNum);

}
