package com.supcon.mes.module_contact.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.ViewUtil;
import com.supcon.mes.mbap.view.CustomCircleTextImageView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.DepartmentInfo;
import com.supcon.mes.middleware.model.bean.ICustomTreeView;
import com.supcon.mes.middleware.model.bean.PositionEntity;
import com.supcon.mes.module_contact.R;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * @Author xushiyun
 * @Create-time 7/19/19
 * @Pageage com.supcon.mes.middleware.ui.adapter
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public class ContactPositionTreeSelectAdapter extends BaseRecyclerViewAdapter<ICustomTreeView<PositionEntity>> {
    private List<ICustomTreeView<PositionEntity>> contentList = new ArrayList<>();

    public void setRootEntity(ICustomTreeView<PositionEntity> customMultiStageEntity) {
        contentList.clear();
        initContentList(customMultiStageEntity);
        notifyDataSetChanged();
    }

    public void initContentList(ICustomTreeView<PositionEntity> rootEntity) {
        rootEntity.setExpanded(true);
        contentList.add(rootEntity);
        rootEntity.getChildNodeList().subscribe(list -> contentList.addAll(list));
    }

    private int getLastChildNodeIndex(int pos) {
        int leftIndex = pos;
        int rightIndex = contentList.size() - 1;
        String fullPathName = getItem(pos).getFullPathName();
        while (true) {
            if (leftIndex >= rightIndex) return leftIndex;
            int midIndex = (leftIndex + rightIndex + 1) / 2;
            if (contentList.get(midIndex).getFullPathName().contains(fullPathName)) {
                leftIndex = midIndex;
            } else {
                rightIndex = midIndex - 1;
            }
        }
    }

    public ContactPositionTreeSelectAdapter(Context context) {
        super(context);
    }

    private int getSpanSize(int position) {
        return getItem(position).getFullPathName().split("/").length;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isLeafNode()) return -1;
        return getSpanSize(position);
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(int viewType) {
        return new TreeViewHolder(context);
    }

    @Override
    public ICustomTreeView<PositionEntity> getItem(int position) {
        return contentList.get(position);
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    class TreeViewHolder extends BaseRecyclerViewHolder<ICustomTreeView<PositionEntity>> {

        @BindByTag("areaName")
        TextView areaName;

        @BindByTag("areaIconContainer")
        LinearLayout areaIconContainer;
        ICustomTreeView<DepartmentInfo> data;
        @BindByTag("areaIcon")
        ImageView areaIcon;
        @BindByTag("userIcon")
        CustomCircleTextImageView userIcon;
        @BindByTag("spaceView")
        ImageView spaceView;
        @BindByTag("detailInfo")
        TextView detailInfo;

        public TreeViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_depart_tree_contact;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView)
                    .throttleFirst(200, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {

                        ICustomTreeView<PositionEntity> customMultiStageEntity = getItem(getAdapterPosition());
                        customMultiStageEntity.changeExpandStatus();
                        Flowable<List<ICustomTreeView<PositionEntity>>> customMultiStageEntities = customMultiStageEntity.getChildNodeList();
                        customMultiStageEntities
                                .flatMap((Function<List<ICustomTreeView<PositionEntity>>,
                                        Publisher<ICustomTreeView<PositionEntity>>>)
                                        customMultiStageEntities1 -> Flowable.fromIterable(customMultiStageEntities1))
                                .subscribe(customMultiStageEntity1 -> customMultiStageEntity1.setExpanded(false));
                        int childNodeListSize = getItem(getAdapterPosition()).getChildListSize();
                        if (!customMultiStageEntity.isExpanded()) {
                            int lastIndex = getLastChildNodeIndex(getAdapterPosition());
                            List<ICustomTreeView<PositionEntity>> sub = new ArrayList<>();
                            sub.addAll(contentList.subList(getAdapterPosition() + 1, lastIndex + 1));
                            contentList.removeAll(sub);
                            notifyItemRangeRemoved(getAdapterPosition() + 1, lastIndex - getAdapterPosition());
                        } else {
                            customMultiStageEntities.subscribe(list -> contentList.addAll(getAdapterPosition() + 1, list));
                            notifyItemRangeInserted(getAdapterPosition() + 1, childNodeListSize);
                        }
                        changeStatus(getItem(getAdapterPosition()));
                    });

            RxView.clicks(userIcon)
                    .throttleFirst(200, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            ICustomTreeView<PositionEntity> data = getItem(getAdapterPosition());
                            if (data != null && data.getCurrentEntity() != null && data.getCurrentEntity().userInfo != null) {
                                onItemChildViewClick(userIcon, 0, data);
                                data.getCurrentEntity().userInfo.updateTime = System.currentTimeMillis();
                                EamApplication.dao().getContactEntityDao().insertOrReplaceInTx(data.getCurrentEntity().userInfo);
                            }
                        }
                    });

            RxView.clicks(areaName)
                    .throttleFirst(200, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            ICustomTreeView<PositionEntity> data = getItem(getAdapterPosition());
                            if (data != null && data.getCurrentEntity() != null && data.getCurrentEntity().userInfo != null) {
                                onItemChildViewClick(areaName, 0, data);
                                data.getCurrentEntity().userInfo.updateTime = System.currentTimeMillis();
                                EamApplication.dao().getContactEntityDao().insertOrReplaceInTx(data.getCurrentEntity().userInfo);
                            }
                        }
                    });
        }

        @Override
        protected void update(ICustomTreeView<PositionEntity> data) {
            itemView.setClickable(!data.isLeafNode());
            int type = getItemViewType();

            if (data.isLeafNode()) {
                userIcon.setVisibility(VISIBLE);
                areaIcon.setVisibility(GONE);
                String name = data.getCurrentEntity().userInfo.getNAME();
                areaName.setText(name);
                userIcon.setText(name.substring((name.length() - 2 < 0 ? 0 : name.length() - 2)));
                detailInfo.setVisibility(VISIBLE);
//                detailInfo.setText(data.getCurrentEntity().userInfo.et());
                return;
            } else {
                detailInfo.setVisibility(INVISIBLE);
                userIcon.setVisibility(GONE);
                areaIcon.setVisibility(VISIBLE);
                areaName.setText(data.getCurrentEntity().name
//                        + (!data.isRootEntity() ?
//                        "(" + data.getCurrentEntity().layRec + ")" : "")
                );
            }
            if (data.isRootEntity()) {
                areaIcon.setImageResource(R.drawable.ic_multi_home);
            } else if (data.getChildListSize() <= 0 || data.isExpanded()) {
                areaIcon.setImageResource(R.drawable.ic_multi_minus);
            } else {
                areaIcon.setImageResource(R.drawable.ic_multi_add);
            }
            spaceView.setVisibility(data.isRootEntity() ? GONE : INVISIBLE);
            areaIconContainer.post(() -> {
                        spaceView.setVisibility(GONE);
                        ViewUtil.setMarginLeft(areaIconContainer, (data.isRootEntity() || type <= 0 ? 0 : type) * 40);
                    }
            );

        }

        public void changeStatus(ICustomTreeView<PositionEntity> data) {
            if (data.isRootEntity()) {
//                areaIcon.setImageResource(R.drawable.ic_multi_home);
            } else if (data.getChildListSize() <= 0) {
//                areaIcon.setVisibility(INVISIBLE);
            } else if (data.isExpanded()) {
                areaIcon.setImageResource(R.drawable.ic_multi_minus);
            } else {
                areaIcon.setImageResource(R.drawable.ic_multi_add);
            }
        }
    }
}
