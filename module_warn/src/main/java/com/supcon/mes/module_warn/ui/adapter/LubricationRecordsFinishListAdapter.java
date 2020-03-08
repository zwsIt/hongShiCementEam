package com.supcon.mes.module_warn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.BaseRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.CommonListAPI;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.contract.CommonListContract;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.bean.DailyLubricateRecordEntity;
import com.supcon.mes.module_warn.presenter.DailyLubrRecordsFinishPresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/8
 * Email zhangwenshuai1@supcon.com
 * Desc 已完成日常润滑记录Adapter
 */
public class LubricationRecordsFinishListAdapter extends BaseListDataRecyclerViewAdapter<DailyLubricateRecordEntity> {

    public LubricationRecordsFinishListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<DailyLubricateRecordEntity> getViewHolder(int viewType) {
        return null;
    }

    class ViewHolder extends BaseRecyclerViewHolder<DailyLubricateRecordEntity> {

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return 0;
        }

        @Override
        protected void update(DailyLubricateRecordEntity data) {

        }
    }

}
