package com.supcon.mes.middleware.ui;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.listener.OnTitleSearchExpandListener;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.EamTypeRefAPI;
import com.supcon.mes.middleware.model.api.LubricatePartRefAPI;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.LubricatingPartEntity;
import com.supcon.mes.middleware.model.contract.EamTypeRefContract;
import com.supcon.mes.middleware.model.contract.LubricatePartRefContract;
import com.supcon.mes.middleware.model.event.PositionEvent;
import com.supcon.mes.middleware.presenter.EamTypeRefPresenter;
import com.supcon.mes.middleware.presenter.LubricatePartRefPresenter;
import com.supcon.mes.middleware.ui.adapter.EamTypeRefAdapter;
import com.supcon.mes.middleware.ui.adapter.LubricatePartRefAdapter;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.KeyExpandHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * LubricatePartRefActivity 设备类型参照选择
 * created by zhangwenshuai1 2019/11/12
 */
@Presenter(EamTypeRefPresenter.class)
@Router(Constant.Router.EAM_TYPE_REF)
public class EamTypeRefActivity extends BaseRefreshRecyclerActivity<EamType> implements EamTypeRefContract.View {
    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("customSearchView")
    CustomSearchView customSearchView;


    private EamTypeRefAdapter mEamTypeRefAdapter;
    private Map<String, Object> queryParam = new HashMap<>();

    @Override
    protected IListAdapter createAdapter() {
        mEamTypeRefAdapter = new EamTypeRefAdapter(this);
        return mEamTypeRefAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_recycle;
    }

    @Override
    protected void onInit() {
        super.onInit();

        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);

        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(3, context)));
    }

    @Override
    protected void initView() {
        super.initView();
        searchTitleBar.setTitleText("设备类型参照");
        searchTitleBar.disableRightBtn();
        customSearchView.setHint("搜索");
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, ""));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> {
            back();
        });
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(EamTypeRefAPI.class).listEamType(pageIndex,queryParam);
            }
        });
        mEamTypeRefAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                EamType eamType = (EamType) obj;
                EventBus.getDefault().post(eamType);
                EamTypeRefActivity.this.finish();
            }
        });
        searchTitleBar.setOnExpandListener(new OnTitleSearchExpandListener() {
            @Override
            public void onTitleSearchExpand(boolean isExpand) {
                if (isExpand) {
                    customSearchView.setHint("请输入设备类型名称");
                    customSearchView.setInputTextColor(R.color.hintColor);
                } else {
                    customSearchView.setHint("搜索");
                    customSearchView.setInputTextColor(R.color.black);
                }
            }
        });
        RxTextView.textChanges(customSearchView.editText())
                .skipInitialValue()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        doSearch(charSequence.toString().trim());
                    }
                });
        KeyExpandHelper.doActionSearch(customSearchView.editText(), true, () ->
                doSearch(customSearchView.getInput()));
    }

    /**
     * @param
     * @return
     * @description 搜索
     * @author zhangwenshuai1 2018/9/19
     */
    private void doSearch(String searchContent) {
        queryParam.put(Constant.BAPQuery.EAMTYPE_NAME, searchContent);
        refreshListController.refreshBegin();
    }

    @Override
    public void listEamTypeSuccess(CommonBAPListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void listEamTypeFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context,ErrorMsgHelper.msgParse(errorMsg));
    }
}
