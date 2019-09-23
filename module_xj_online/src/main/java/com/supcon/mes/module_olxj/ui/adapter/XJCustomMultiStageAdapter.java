package com.supcon.mes.module_olxj.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.middleware.model.bean.CustomMultiStageEntity;
import com.supcon.mes.module_olxj.ui.view.XJCustomMultiStageView;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @Author xushiyun
 * @Create-time 7/19/19
 * @Pageage com.supcon.mes.middleware.ui.adapter
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public class XJCustomMultiStageAdapter<Data> extends BaseRecyclerViewAdapter<CustomMultiStageEntity<Data>> {
    private List<CustomMultiStageEntity<Data>> contentList = new ArrayList<>();
    private XJCustomMultiStageView.CustomMultiStageViewController mCustomMultiStageViewController;

    public void registerController(XJCustomMultiStageView.CustomMultiStageViewController customMultiStageViewController) {
        mCustomMultiStageViewController = customMultiStageViewController;
    }

    public void setRootEntity(CustomMultiStageEntity<Data> customMultiStageEntity) {
        contentList.clear();
        initContentList(customMultiStageEntity);
        notifyDataSetChanged();
    }

    public void initContentList(CustomMultiStageEntity<Data> rootEntity) {
        rootEntity.setExpanded(true);
        contentList.add(rootEntity);
        rootEntity.getChildNodeList().subscribe(list -> contentList.addAll(list));
    }

    private int getLastChildNodeIndex(int pos) {
        int leftIndex = pos;
        int rightIndex = contentList.size() - 1;
        String layrec = getItem(pos).getInfo();
        while (true) {
            if (leftIndex >= rightIndex) return leftIndex;
            int midIndex = (leftIndex + rightIndex + 1) / 2;
            if (contentList.get(midIndex).getInfo().contains(layrec)) {
                leftIndex = midIndex;
            } else {
                rightIndex = midIndex - 1;
            }
        }
    }

    public XJCustomMultiStageAdapter(Context context) {
        super(context);
    }

    private int getSpanSize(int position) {
        return getItem(position).getInfo().split("-").length;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isLeafNode()) return -1;
        return getSpanSize(position);
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(int viewType) {
        return new StageViewHolder(context);
    }

    @Override
    public CustomMultiStageEntity<Data> getItem(int position) {
        return contentList.get(position);
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public class StageViewHolder extends BaseRecyclerViewHolder<CustomMultiStageEntity<Data>> {

        @BindByTag("areaIcon")
        ImageView areaIcon;
        @BindByTag("areaName")
        TextView areaName;

        public StageViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return mCustomMultiStageViewController.layout();
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(areaIcon)
                    .throttleFirst(200, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        CustomMultiStageEntity<Data> customMultiStageEntity = getItem(getAdapterPosition());
                        if (customMultiStageEntity.isRootEntity()) {
                            return;
                        }
                        customMultiStageEntity.changeExpandStatus();
                        Flowable<List<CustomMultiStageEntity<Data>>> customMultiStageEntities = customMultiStageEntity.getChildNodeList();
                        customMultiStageEntities
                                .flatMap((Function<List<CustomMultiStageEntity<Data>>,
                                        Publisher<CustomMultiStageEntity<Data>>>)
                                        customMultiStageEntities1 -> Flowable.fromIterable(customMultiStageEntities1))
                                .subscribe(customMultiStageEntity1 -> customMultiStageEntity1.setExpanded(false));
                        if (!customMultiStageEntity.isExpanded()) {
                            int lastIndex = getLastChildNodeIndex(getAdapterPosition());
                            List<CustomMultiStageEntity<Data>> sub = new ArrayList<>();
                            sub.addAll(contentList.subList(getAdapterPosition() + 1, lastIndex + 1));
                            contentList.removeAll(sub);
                            notifyItemRangeRemoved(getAdapterPosition() + 1, lastIndex - getAdapterPosition());
                        } else {
                            int childNodeListSize = getItem(getAdapterPosition()).getChildListSize();
                            customMultiStageEntities.subscribe(list -> contentList.addAll(StageViewHolder.this.getAdapterPosition() + 1, list));
                            notifyItemRangeInserted(getAdapterPosition() + 1, childNodeListSize);
                        }
                        viewController.changeStatus();
                    });

            RxView.clicks(areaName)
                    .throttleFirst(200, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            onItemChildViewClick(areaName, 0, getItem(getAdapterPosition()));
                        }
                    });
        }

        private XJCustomMultiStageView.CustomMultiStageViewController viewController;

        @Override
        protected void update(CustomMultiStageEntity<Data> data) {
            viewController = mCustomMultiStageViewController.newInstance(itemView);
            viewController.inject(data, getItemViewType());
        }
    }
}
