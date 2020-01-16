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
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.Department;
import com.supcon.mes.middleware.model.bean.DepartmentInfo;
import com.supcon.mes.middleware.model.bean.ICustomTreeView;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.module_contact.R;

import org.greenrobot.eventbus.EventBus;
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
public class DepartmentSelectAdapter extends BaseRecyclerViewAdapter<ICustomTreeView<DepartmentInfo>> {
    private List<ICustomTreeView<DepartmentInfo>> contentList = new ArrayList<>();

    public void setRootEntity(ICustomTreeView<DepartmentInfo> customMultiStageEntity) {
        contentList.clear();
        initContentList(customMultiStageEntity);
        notifyDataSetChanged();
    }

    public void initContentList(ICustomTreeView<DepartmentInfo> rootEntity) {
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

    public DepartmentSelectAdapter(Context context) {
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
    public ICustomTreeView<DepartmentInfo> getItem(int position) {
        return contentList.get(position);
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    class TreeViewHolder extends BaseRecyclerViewHolder<ICustomTreeView<DepartmentInfo>> {

        @BindByTag("areaName")
        TextView areaName;
        @BindByTag("areaIconContainer")
        LinearLayout areaIconContainer;
        ICustomTreeView<Department> data;
        @BindByTag("areaIcon")
        ImageView areaIcon;
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

                        ICustomTreeView<DepartmentInfo> customMultiStageEntity = getItem(getAdapterPosition());
                        customMultiStageEntity.changeExpandStatus();
                        Flowable<List<ICustomTreeView<DepartmentInfo>>> customMultiStageEntities = customMultiStageEntity.getChildNodeList();
                        customMultiStageEntities
                                .flatMap((Function<List<ICustomTreeView<DepartmentInfo>>,
                                        Publisher<ICustomTreeView<DepartmentInfo>>>)
                                        customMultiStageEntities1 -> Flowable.fromIterable(customMultiStageEntities1))
                                .subscribe(customMultiStageEntity1 -> customMultiStageEntity1.setExpanded(false));
                        int childNodeListSize = getItem(getAdapterPosition()).getChildListSize();
                        if (!customMultiStageEntity.isExpanded()) {
                            int lastIndex = getLastChildNodeIndex(getAdapterPosition());
                            List<ICustomTreeView<DepartmentInfo>> sub = new ArrayList<>();
                            sub.addAll(contentList.subList(getAdapterPosition() + 1, lastIndex + 1));
                            contentList.removeAll(sub);
                            notifyItemRangeRemoved(getAdapterPosition() + 1, lastIndex - getAdapterPosition());
                        } else {
                            customMultiStageEntities.subscribe(list -> contentList.addAll(getAdapterPosition() + 1, list));
                            notifyItemRangeInserted(getAdapterPosition() + 1, childNodeListSize);
                        }
                        changeStatus(getItem(getAdapterPosition()));
                    });

            RxView.clicks(areaIcon)
                    .throttleFirst(200, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            ICustomTreeView<DepartmentInfo> data = getItem(getAdapterPosition());
                            if (data != null && data.getCurrentEntity() != null && !EamApplication.getAccountInfo().companyName.equals(data.getCurrentEntity().name)) {
                                EventBus.getDefault().post(new SelectDataEvent<>(data.getCurrentEntity()));
                                onItemChildViewClick(areaIcon, 0);
                            }
                        }
                    });

            RxView.clicks(areaName)
                    .throttleFirst(200, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            ICustomTreeView<DepartmentInfo> data = getItem(getAdapterPosition());
                            if (data != null && data.getCurrentEntity() != null && !EamApplication.getAccountInfo().companyName.equals(data.getCurrentEntity().name)) {
                                EventBus.getDefault().post(new SelectDataEvent<>(data.getCurrentEntity()));
                                onItemChildViewClick(areaIcon, 0);
                            }
                        }
                    });
        }

        @Override
        protected void update(ICustomTreeView<DepartmentInfo> data) {
            itemView.setClickable(!data.isLeafNode());
            int type = getItemViewType();

            detailInfo.setVisibility(INVISIBLE);
            areaIcon.setVisibility(VISIBLE);
            areaName.setText(data.getCurrentEntity().name
//                        + (!data.isRootEntity() ?
//                        "(" + data.getCurrentEntity().layRec + ")" : "")
            );

            if (data.isRootEntity()) {
                areaIcon.setImageResource(R.drawable.ic_multi_home);
            } else if (data.getChildListSize() <= 0 || data.isExpanded()) {
                areaIcon.setImageResource(R.drawable.ic_multi_minus);
            } else {
                areaIcon.setImageResource(R.drawable.ic_multi_add);
            }
//            LogUtil.e(data.getCurrentEntity().toString(), "徐诗韵1234444: " + type + "");
            spaceView.setVisibility(data.isRootEntity() ? GONE : INVISIBLE);
            areaIconContainer.post(() -> {
                        spaceView.setVisibility(GONE);
                        ViewUtil.setMarginLeft(areaIconContainer, (data.isRootEntity() || type <= 0 ? 0 : type) * areaIcon.getWidth());
                    }
            );
        }

        public void changeStatus(ICustomTreeView<DepartmentInfo> data) {
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
