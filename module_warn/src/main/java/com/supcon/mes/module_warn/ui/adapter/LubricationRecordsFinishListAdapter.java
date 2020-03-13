package com.supcon.mes.module_warn.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.BaseRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.TextHelper;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.CommonListAPI;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.contract.CommonListContract;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.FormDataHelper;
import com.supcon.mes.middleware.util.FormatUtil;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_warn.R;
import com.supcon.mes.module_warn.model.bean.DailyLubricateRecordEntity;
import com.supcon.mes.module_warn.presenter.DailyLubrRecordsFinishPresenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

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
    public int getItemViewType(int position, DailyLubricateRecordEntity dailyLubricateRecordEntity) {
        return dailyLubricateRecordEntity.getViewType();
    }

    @Override
    protected BaseRecyclerViewHolder<DailyLubricateRecordEntity> getViewHolder(int viewType) {
        if (viewType == ListType.TITLE.value()) {
            return new TitleViewHolder(context);
        } else {
            return new ContentViewHolder(context);
        }
    }

    class TitleViewHolder extends BaseRecyclerViewHolder<DailyLubricateRecordEntity> {

        @BindByTag("lubricationPart")
        TextView lubricationPart;
        @BindByTag("expendIv")
        ImageView expendIv;

        public TitleViewHolder(Context context) {
            super(context, parent);
        }


        @Override
        protected int layoutId() {
            return R.layout.item_lubricate_part_title;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView)
                    .throttleFirst(200, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            DailyLubricateRecordEntity entity = getItem(getAdapterPosition());
                            List<DailyLubricateRecordEntity> expendList = entity.getExpendList();
                            if (entity.isExpend()) {
                                entity.setExpend(false);
                                getList().removeAll(expendList);
                                notifyItemRangeRemoved(getAdapterPosition() + 1, expendList.size());
                                notifyItemRangeChanged(getAdapterPosition(), expendList.size());
                            } else {
                                entity.setExpend(true);
                                getList().addAll(getAdapterPosition() + 1,expendList);
                                notifyItemRangeInserted(getAdapterPosition() + 1, expendList.size());
                                notifyItemRangeChanged(getAdapterPosition(), expendList.size());
//                            notifyDataSetChanged();
                            }

                        }
                    });
        }

        @Override
        protected void update(DailyLubricateRecordEntity data) {
            lubricationPart.setText(data.getJwxItemId() == null ? "无部位信息" : data.getJwxItemId().lubricatePart);
            if (data.isExpend()){
                expendIv.setImageResource(R.drawable.ic_shrink);
            }else {
                expendIv.setImageResource(R.drawable.ic_expand);
            }
        }
    }

    class ContentViewHolder extends BaseRecyclerViewHolder<DailyLubricateRecordEntity> {

        @BindByTag("finishStaff")
        TextView finishStaff;
        @BindByTag("finishTime")
        TextView finishTime;

        public ContentViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_lubricate_part_content;
        }

        @Override
        protected void update(DailyLubricateRecordEntity data) {
            finishStaff.setText(String.format("完成人员:%s", data.getDealStaff().name));
            finishTime.setText(String.format("完成时间:%s", DateUtil.dateTimeFormat(data.getDealTime())));
        }
    }

}
