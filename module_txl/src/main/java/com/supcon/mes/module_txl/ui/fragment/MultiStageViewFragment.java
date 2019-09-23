package com.supcon.mes.module_txl.ui.fragment;

import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BasePresenterFragment;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.model.api.MultiDepartSelectAPI;
import com.supcon.mes.middleware.model.contract.MultiDepartSelectContract;
import com.supcon.mes.middleware.presenter.MultiDepartSelectPresenter;
import com.supcon.mes.middleware.ui.view.CustomMultiStageView;
import com.supcon.mes.module_txl.R;

/**
 * @Author xushiyun
 * @Create-time 7/26/19
 * @Pageage com.supcon.mes.module_txl.ui.fragment
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
@Presenter(MultiDepartSelectPresenter.class)
public class MultiStageViewFragment extends BasePresenterFragment implements MultiDepartSelectContract.View {
    @BindByTag("customMultiStageView")
    CustomMultiStageView customMultiStageView;
//    @BindByTag("leftBtn")
//    ImageButton leftBtn;
//    @BindByTag("titleText")
//    TextView titleText;
//    @BindByTag("rightBtn")
//    ImageButton rightBtn;
//    @BindByTag("titleBarLayout")
//    RelativeLayout titleBarLayout;
//    @BindByTag("customSearchView")
//    CustomSearchView customSearchView;
//
//    @Override
//    protected void initListener() {
//        super.initListener();
//        leftBtn.setOnClickListener(v -> getActivity().onBackPressed());
//    }
    
    @Override
    protected void initData() {
        super.initData();
        presenterRouter.create(MultiDepartSelectAPI.class).getDepartmentInfoList("");
    }
    
    @Override
    protected int getLayoutID() {
        return R.layout.frag_multi_stage;
    }
    
    @Override
    public void getDepartmentInfoListSuccess(MultiDepartSelectPresenter.AreaMultiStageEntity entity) {
        customMultiStageView.setRootEntity(entity);
    }
    
    @Override
    public void getDepartmentInfoListFailed(String errorMsg) {
    
    }
    
}
