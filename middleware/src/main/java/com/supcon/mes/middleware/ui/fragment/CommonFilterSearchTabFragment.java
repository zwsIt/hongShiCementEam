package com.supcon.mes.middleware.ui.fragment;

import android.text.TextUtils;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.fragment.BasePresenterFragment;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomFlowTagView;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.model.api.CommonFilterSearchAPI;
import com.supcon.mes.middleware.model.bean.CommonFilterSearchListEntity;
import com.supcon.mes.middleware.model.bean.TagEntity;
import com.supcon.mes.middleware.model.contract.CommonFilterSearchContract;
import com.supcon.mes.middleware.model.event.FilterSearchTabEvent;
import com.supcon.mes.middleware.model.factory.SearchContentFactory;
import com.supcon.mes.middleware.presenter.CommonFilterSearchPresenter;
import com.supcon.mes.middleware.ui.CommonFilterSearchActivity;
import com.supcon.mes.middleware.ui.adapter.TagAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/11/2114:28
 */
@Presenter(CommonFilterSearchPresenter.class)
public class CommonFilterSearchTabFragment extends BasePresenterFragment implements CommonFilterSearchContract.View {
    @BindByTag("deviceTagLayout")
    CustomFlowTagView deviceTagLayout;
    private TagAdapter mTagAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.frag_filter_search_tab;
    }

    @Override
    public void refresh() {
        super.refresh();
    }

    @Override
    protected void onInit() {
        super.onInit();
        mTagAdapter = new TagAdapter(getActivity());
        deviceTagLayout.setTagCheckedMode(CustomFlowTagView.FLOW_TAG_CHECKED_SINGLE);
        deviceTagLayout.setAdapter(mTagAdapter);
        deviceTagLayout.setOnTagSelectListener((parent, integerList) -> {
            if (null == integerList || integerList.size() <= 0) {
                return;
            }
            EventBus.getDefault().post(FilterSearchTabEvent.nil().tag((TagEntity) mTagAdapter.getItem(integerList.get(0))));
            getActivity().finish();
        });
        deviceTagLayout.setOnTagClickListener((parent, view, position) -> getActivity().finish());
    }


    private SearchContentFactory.FilterType getFilterType() {
        return getCommonFilterSearchActivity().getFilterType();
    }

    private CommonFilterSearchActivity getCommonFilterSearchActivity() {
        return (CommonFilterSearchActivity) getActivity();
    }

    private Map<String, Object> getParam() {
        return getCommonFilterSearchActivity().getParam();
    }


    @Override
    protected void initData() {
        super.initData();
        requestAllRecommendTags();
    }

    private void requestAllRecommendTags() {
        presenterRouter.create(CommonFilterSearchAPI.class).getAllRecommendTags(getFilterType(), getParam());
    }


    private void requestAllRecentTags() {
        presenterRouter.create(CommonFilterSearchAPI.class).getRecentTags(getFilterType(), getParam());
    }

    @Override
    public void getRecentTagsSuccess(CommonFilterSearchListEntity entity) {

    }

    @Override
    public void getRecentTagsFailed(String errorMsg) {

    }

    @Override
    public void getAllRecommendTagsSuccess(CommonFilterSearchListEntity entity) {
        mTagAdapter.clearAndAddAll(entity.result);
    }

    @Override
    public void getAllRecommendTagsFailed(String errorMsg) {
        if (TextUtils.isEmpty(errorMsg)) {
            return;
        }
        ToastUtils.show(context, errorMsg);
    }
}
