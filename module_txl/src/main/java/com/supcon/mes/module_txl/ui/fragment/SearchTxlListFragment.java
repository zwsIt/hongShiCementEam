package com.supcon.mes.module_txl.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.middleware.model.bean.IDoFilter;
import com.supcon.mes.middleware.model.bean.TxlListEntity;
import com.supcon.mes.middleware.model.bean.TxlSearchType;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_txl.R;
import com.supcon.mes.module_txl.model.api.TxlListAPI;
import com.supcon.mes.module_txl.model.contract.TxlListContract;
import com.supcon.mes.module_txl.presenter.TxlListPresenter;
import com.supcon.mes.module_txl.ui.adapter.SearchTxlListAdapter;

/**
 * @Author xushiyun
 * @Create-time 7/26/19
 * @Pageage com.supcon.mes.module_txl.ui.fragment
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
@Presenter(TxlListPresenter.class)
public class SearchTxlListFragment extends BaseRefreshRecyclerFragment implements TxlListContract.View, IDoFilter {
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("refreshFrameLayout")
    PtrFrameLayout refreshFrameLayout;
    private SearchTxlListAdapter mSearchTxlListAdapter;
    
    
    @Override
    protected IListAdapter createAdapter() {
        mSearchTxlListAdapter = new SearchTxlListAdapter(context);
        return mSearchTxlListAdapter;
    }
    
    @Override
    protected void initView() {
        super.initView();
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, "未搜索到数据!"));
        refreshListController.setLoadMoreEnable(true);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setOnRefreshPageListener(pageIndex -> doFilter(pageIndex));
        
        
        contentView.setLayoutManager(new LinearLayoutManager(context));
        final int spacingInPixels = 10;
        contentView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
    }
    
    @Override
    public void doFilter() {
        refreshListController.refreshBegin();
    }
    
    @Override
    public void doFilter(int pageNum) {
        presenterRouter.create(TxlListAPI.class).getTxlList(pageNum, staffCode, staffName, positionName);
    }
    
    private String staffCode;
    private String staffName;
    private String positionName;
    
    @Override
    public void doFilter(int page, String filterType, String filterValue) {
//        presenterRouter.create(TxlListAPI.class).getTxlList(page,
        staffCode = getFilterValue(TxlSearchType.STAFF_CODE.name(), filterType, filterValue);
        staffName = getFilterValue(TxlSearchType.SRAFF_NAME.name(), filterType, filterValue);
        positionName = getFilterValue(TxlSearchType.POSITION_NAME.name(), filterType, filterValue);
        doFilter();
    }
    
    private String getFilterValue(String filterTypeOri, String filterTypeGoal, String filterValue) {
        return filterTypeOri.equals(filterTypeGoal) ? filterValue : "";
    }
    
    @Override
    public void doFilter(String mes) {
        doFilter();
    }
    
    @Override
    protected int getLayoutID() {
        return R.layout.frag_search_txl_list;
    }
    
    @Override
    public void getTxlListSuccess(TxlListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }
    
    @Override
    public void getTxlListFailed(String errorMsg) {
        ToastUtils.show(context, errorMsg);
        refreshListController.refreshComplete();
    }
}
