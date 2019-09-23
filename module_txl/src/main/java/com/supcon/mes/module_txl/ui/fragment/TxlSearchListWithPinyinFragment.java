package com.supcon.mes.module_txl.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.model.bean.DataUtil;
import com.supcon.mes.middleware.model.bean.TxlEntity;
import com.supcon.mes.middleware.model.bean.TxlListEntity;
import com.supcon.mes.middleware.model.listener.PowerGroupListener;
import com.supcon.mes.middleware.ui.view.PinyinSearchBar;
import com.supcon.mes.middleware.ui.view.SectionDecoration;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.PinYinUtils;
import com.supcon.mes.middleware.util.ScreenUtil;
import com.supcon.mes.module_txl.R;
import com.supcon.mes.module_txl.model.api.TxlListAPI;
import com.supcon.mes.module_txl.model.contract.TxlListContract;
import com.supcon.mes.module_txl.presenter.TxlListPresenter;
import com.supcon.mes.module_txl.ui.adapter.TxlSearchListWithPinyinAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author xushiyun
 * @Create-time 7/26/19
 * @Pageage com.supcon.mes.module_txl.ui.fragment
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
@Presenter(value = {TxlListPresenter.class})
public class TxlSearchListWithPinyinFragment extends BaseRefreshRecyclerFragment<TxlEntity> implements TxlListContract.View {
    TxlSearchListWithPinyinAdapter mTxlSearchListWithPinyinAdapter;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("refreshFrameLayout")
    PtrFrameLayout refreshFrameLayout;
    @BindByTag("pinyinSearchBar")
    PinyinSearchBar pinyinSearchBar;
    
    /**
     * 该集合用来盛放所有item对应悬浮栏的内容
     */
    private List<DataUtil> dataList = new ArrayList<>();
    /**
     * 用来盛放接口返回的数据
     **/
    private List<TxlEntity> beanList = new ArrayList<>();
    
    @Override
    protected void initData() {
        super.initData();
        beanList = new ArrayList<>();
        dataList = new ArrayList<>();
    }
    
    /**
     * 给子item添加父布局
     */
    private void setPullAction() {
    }
    
    @Override
    protected void onInit() {
        super.onInit();
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setLoadMoreEnable(true);
    }
    
    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshPageListener(pageIndex -> presenterRouter.create(TxlListAPI.class).getTxlList(pageIndex,null,null,null));
    }
    
    @Override
    protected void initView() {
        super.initView();
        initRecyclerView();
    }
    
    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        contentView.setLayoutManager(new LinearLayoutManager(context));
//        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(3, context)));
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, ""));
        //添加悬浮布局
        initDecoration();
    }
    
    @Override
    protected IListAdapter createAdapter() {
        mTxlSearchListWithPinyinAdapter = new TxlSearchListWithPinyinAdapter(context);
        return mTxlSearchListWithPinyinAdapter;
    }
    
    /**
     * 添加悬浮布局
     */
    private void initDecoration() {
        SectionDecoration decoration = SectionDecoration.Builder
                .init(new PowerGroupListener() {
                    @Override
                    public String getGroupName(int position) {
                        //获取组名，用于判断是否是同一组
                        if (dataList.size() > position) {
                            
                            return dataList.get(position).getName();
                        }
                        return null;
                    }
                    
                    @Override
                    public View getGroupView(int position) {
                        //获取自定定义的组View
                        if (dataList.size() > position) {
                            View view = LayoutInflater.from(context).inflate(R.layout.item_group, null, false);
                            ((TextView) view.findViewById(R.id.tv)).setText(dataList.get(position).getName());
                            ((ImageView) view.findViewById(R.id.iv)).setImageResource(dataList.get(position).getIcon());
                            return view;
                        } else {
                            return null;
                        }
                    }
                })
                //设置高度
                .setGroupHeight(ScreenUtil.dip2px(context, 40))
                .build();
        contentView.addItemDecoration(decoration);
    }
    
    @Override
    protected int getLayoutID() {
        return R.layout.frag_txl_search_header;
    }
    
    
    @Override
    public void getTxlListSuccess(TxlListEntity entity) {
        if (entity.result == null) {
            refreshListController.refreshComplete(entity.result);
            return;
        }
        for (int i = 0; i < entity.result.size(); i++) {
            TxlEntity txlEntity = entity.result.get(i);
            DataUtil dataUtil = new DataUtil();
            dataUtil.setName(PinYinUtils.getHeaderLetter(txlEntity.getNAME()) + "");
            dataList.add(dataUtil);
        }
        refreshListController.refreshComplete(entity.result);
    }
    
    @Override
    public void getTxlListFailed(String errorMsg) {
        ToastUtils.show(context, errorMsg);
        refreshListController.refreshComplete();
    }
}
