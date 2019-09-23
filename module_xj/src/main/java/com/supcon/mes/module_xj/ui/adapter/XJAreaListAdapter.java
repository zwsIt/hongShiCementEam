package com.supcon.mes.module_xj.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.XJAreaEntity;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntity;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntityDao;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntityDao.Properties;
import com.supcon.mes.module_xj.R;

import org.greenrobot.greendao.query.QueryBuilder;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;


/**
 * Created by wangshizhan on 2018/3/27.
 * Email:wangshizhan@supcon.com
 */

public class XJAreaListAdapter extends BaseListDataRecyclerViewAdapter<XJAreaEntity> {

    public XJAreaListAdapter(Context context) {
        super(context);
    }

    public XJAreaListAdapter(Context context, List<XJAreaEntity> list) {
        super(context, list);
    }

    @Override
    protected BaseRecyclerViewHolder<XJAreaEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }


    class ViewHolder extends BaseRecyclerViewHolder<XJAreaEntity> {

        @BindByTag("itemAreaName")
        TextView itemAreaName;  //区域名称

        @BindByTag("itemAreaProgress")
        TextView itemAreaProgress; //区域任务进度

        @BindByTag("itemAreaDot")
        ImageView itemAreaDot;


        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initBind() {
            super.initBind();
        }

        @Override
        protected void initView() {
            super.initView();
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(v -> {
                        XJAreaEntity xjAreaEntity = getItem(getAdapterPosition());
                        onItemChildViewClick(itemView, 0, xjAreaEntity);
                    }
            );
        }


        @Override
        protected int layoutId() {
            return R.layout.item_xj_area;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void update(XJAreaEntity data) {

            itemAreaName.setText((data.areaOrder + 1) + ". " + data.areaName);


            QueryBuilder<XJWorkItemEntity> queryBuilder = EamApplication.dao().getXJWorkItemEntityDao().queryBuilder();

//            int total = xjWorkItemEntityDao.queryBuilder()
//                    .where(XJWorkItemEntityDao.Properties.AreaId.eq(data.areaId), XJWorkItemEntityDao.Properties.TaskId.eq(data.taskId)).count();
//
//            int finied = xjWorkItemEntityDao.queryBuilder()
//                    .where(XJWorkItemEntityDao.Properties.AreaId.eq(data.areaId), XJWorkItemEntityDao.Properties.TaskId.eq(data.taskId), XJWorkItemEntityDao.Properties.IsFinished.eq(true)).count();
//
//            itemAreaProgress.setText(finied+"/"+total);

            List<XJWorkItemEntity> finishedList = new ArrayList<>();
            List<XJWorkItemEntity> workItemEntities = new ArrayList<>();
            Flowable.just(data.areaId)
                    .map(areaId -> {
                        workItemEntities.addAll(queryBuilder
                                .where(Properties.AreaId.eq(areaId), Properties.TaskId.eq(data.taskId),Properties.Ip.eq(EamApplication.getIp()))
                                .list());
                        return workItemEntities;
                    })
                    .flatMap((Function<List<XJWorkItemEntity>, Publisher<XJWorkItemEntity>>) xjWorkItemEntities -> Flowable.fromIterable(xjWorkItemEntities))
                    .filter(xjWorkItemEntity -> xjWorkItemEntity.isFinished)
                    .compose(RxSchedulers.io_main())
                    .subscribe(xjWorkItemEntity ->
                                    finishedList.add(xjWorkItemEntity),
                            throwable -> {
                            },
                            () -> {
                                itemAreaProgress.setText(String.format("%d/%d", finishedList.size(), workItemEntities.size()));
                                if(finishedList.size() == workItemEntities.size()) {
                                    itemAreaName.setTextColor(Color.GRAY);
                                    itemAreaProgress.setTextColor(Color.GRAY);
//                                    itemAreaDot.setImageDrawable();
                                    data.finishType = "1";
                                    EamApplication.dao().getXJAreaEntityDao().update(data);
                                }else {
                                    itemAreaName.setTextColor(Color.parseColor("#366CBC"));
                                    itemAreaProgress.setTextColor(Color.parseColor("#366CBC"));
                                }
                            });
        }
    }
}
