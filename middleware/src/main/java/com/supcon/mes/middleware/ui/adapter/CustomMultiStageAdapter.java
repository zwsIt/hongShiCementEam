package com.supcon.mes.middleware.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.model.bean.TxlEntity;
import com.supcon.mes.middleware.ui.view.CustomMultiStageView;

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
public class CustomMultiStageAdapter<Data> extends BaseRecyclerViewAdapter<CustomMultiStageView.CustomMultiStageEntity<Data>> {
    private List<CustomMultiStageView.CustomMultiStageEntity<Data>> contentList = new ArrayList<>();
    private CustomMultiStageView.CustomMultiStageViewController mCustomMultiStageViewController;
    
    public void registerController(CustomMultiStageView.CustomMultiStageViewController customMultiStageViewController) {
        mCustomMultiStageViewController = customMultiStageViewController;
    }
    
    public void setRootEntity(CustomMultiStageView.CustomMultiStageEntity<Data> customMultiStageEntity) {
        contentList.clear();
        initContentList(customMultiStageEntity);
        notifyDataSetChanged();
    }
    
    public void initContentList(CustomMultiStageView.CustomMultiStageEntity<Data> rootEntity) {
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
    
    public CustomMultiStageAdapter(Context context) {
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
    public CustomMultiStageView.CustomMultiStageEntity<Data> getItem(int position) {
        return contentList.get(position);
    }
    
    @Override
    public int getItemCount() {
        return contentList.size();
    }
    
    public class StageViewHolder extends BaseRecyclerViewHolder<CustomMultiStageView.CustomMultiStageEntity<Data>> {
        
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
            RxView.clicks(itemView)
                    .throttleFirst(200, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
//                        Flowable.just(getItem(getAdapterPosition()))
//                                .doOnNext(customMultiStageEntity -> customMultiStageEntity.changeExpandStatus())
//                                .flatMap(customMultiStageEntity -> customMultiStageEntity.getChildNodeList())
//                                .doOnNext(new Consumer<List<CustomMultiStageView.CustomMultiStageEntity<Data>>>() {
//                                    @Override
//                                    public void accept(List<CustomMultiStageView.CustomMultiStageEntity<Data>> customMultiStageEntities) throws Exception {
//                                        for (CustomMultiStageView.CustomMultiStageEntity<Data> customMultiStageEntity:customMultiStageEntities){
//                                            customMultiStageEntity.setExpanded(false);
//                                        }
//                                    }
//                                });
                        
                        CustomMultiStageView.CustomMultiStageEntity<Data> customMultiStageEntity = getItem(getAdapterPosition());
                        customMultiStageEntity.changeExpandStatus();
                        Flowable<List<CustomMultiStageView.CustomMultiStageEntity<Data>>> customMultiStageEntities = customMultiStageEntity.getChildNodeList();
                        customMultiStageEntities
                                .flatMap((Function<List<CustomMultiStageView.CustomMultiStageEntity<Data>>,
                                        Publisher<CustomMultiStageView.CustomMultiStageEntity<Data>>>)
                                        customMultiStageEntities1 -> Flowable.fromIterable(customMultiStageEntities1))
                                .subscribe(customMultiStageEntity1 -> customMultiStageEntity1.setExpanded(false));
                        if (!customMultiStageEntity.isExpanded()) {
                            int lastIndex = getLastChildNodeIndex(getAdapterPosition());
                            List<CustomMultiStageView.CustomMultiStageEntity<Data>> sub = new ArrayList<>();
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
        }
        
        private CustomMultiStageView.CustomMultiStageViewController viewController;
        
        @Override
        protected void update(CustomMultiStageView.CustomMultiStageEntity<Data> data) {
            itemView.setClickable(!data.isLeafNode());
            viewController = mCustomMultiStageViewController.newInstance(itemView);
            viewController.inject(data, getItemViewType());
        }
    }
}
