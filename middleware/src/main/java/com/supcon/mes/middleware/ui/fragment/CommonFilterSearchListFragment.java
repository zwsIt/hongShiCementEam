package com.supcon.mes.middleware.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.middleware.model.bean.CommonLabelEntity;
import com.supcon.mes.middleware.model.bean.CommonLabelListEntity;
import com.supcon.mes.middleware.model.bean.TagEntity;
import com.supcon.mes.middleware.model.event.FilterSearchTabEvent;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.model.api.CommonFilterSearchListAPI;
import com.supcon.mes.middleware.model.contract.CommonFilterSearchListContract;
import com.supcon.mes.middleware.model.factory.SearchContentFactory;
import com.supcon.mes.middleware.presenter.CommonFilterSearchListPresenter;
import com.supcon.mes.middleware.ui.CommonFilterSearchActivity;
import com.supcon.mes.middleware.ui.adapter.CommonFilterSearchLabelAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/11/2114:27
 */
@Presenter(CommonFilterSearchListPresenter.class)
public class CommonFilterSearchListFragment extends BaseRefreshRecyclerFragment<CommonLabelEntity> implements CommonFilterSearchListContract.View {
    @BindByTag("contentView")
    RecyclerView contentView;

    private CommonFilterSearchLabelAdapter mCommonFilterSearchLabelAdapter;

    @Override
    protected void onInit() {
        super.onInit();
        refreshListController.setLoadMoreEnable(false);
        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(true);
    }

    private SearchContentFactory.FilterType getFilterType() {
        return getCommonFilterSearchActivity().getFilterType();
    }

    private Map<String, Object> getParam() {
        return getCommonFilterSearchActivity().getParam();
    }

    private CommonFilterSearchActivity getCommonFilterSearchActivity() {
        return (CommonFilterSearchActivity) getActivity();
    }

    @Override
    protected void initView() {
        super.initView();
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(2, context)));
    }

    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                doFilter();
            }
        });
        mCommonFilterSearchLabelAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                TagEntity tagEntity = (TagEntity) obj;
                EventBus.getDefault().post(FilterSearchTabEvent.nil().tag(tagEntity));
                getCommonFilterSearchActivity().back();
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.frag_filter_search_list;
    }

    @Override
    protected IListAdapter createAdapter() {
        mCommonFilterSearchLabelAdapter = new CommonFilterSearchLabelAdapter(context);
        return mCommonFilterSearchLabelAdapter;
    }

    @Override
    public void getCommonFilterSearchListSuccess(CommonLabelListEntity entity) {
        refreshListController.refreshComplete(entity.getResult());
    }

    @Override
    public void getCommonFilterSearchListFailed(String errorMsg) {
        ToastUtils.show(context, errorMsg);
        refreshListController.refreshComplete(null);
    }

    /**
     * 刷新列表
     */
    @Override
    public void refresh() {
        super.refresh();
        doFilter();
    }

    private void doFilter() {
        presenterRouter.create(CommonFilterSearchListAPI.class).getCommonFilterSearchList(-1,
                CommonFilterSearchListFragment.this.getFilterType(),
                CommonFilterSearchListFragment.this.getParam());
    }
}
