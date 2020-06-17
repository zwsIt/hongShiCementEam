package com.supcon.mes.module_olxj.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.model.bean.OLXJTaskEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;

import java.util.List;


/**
 * Created by zhangwenshuai1 on 2018/3/12.
 * 今日巡检
 */

public class OLXJTaskRecordsListAdapter extends BaseListDataRecyclerViewAdapter<OLXJTaskEntity> {

    private int expandPosition = -1;
    private List<OLXJWorkItemEntity> mOLXJAreaEntities;
    private ViewHolder viewHolder;

    public OLXJTaskRecordsListAdapter(Context context) {
        super(context);
    }

    public void setAreaEntities(List<OLXJWorkItemEntity> olxjAreaEntities) {
        this.mOLXJAreaEntities = olxjAreaEntities;
        notifyDataSetChanged();
    }

    public void resetExpandPosition() {
        this.expandPosition = -1;
    }

    public void expand() {
        this.expandPosition = 0;
        notifyItemChanged(expandPosition);
    }

    public boolean isExpand() {
        return expandPosition == viewHolder.getAdapterPosition();
    }

    @Override
    protected BaseRecyclerViewHolder<OLXJTaskEntity> getViewHolder(int viewType) {
        viewHolder = new ViewHolder(context, parent);
        return viewHolder;
    }

    class ViewHolder extends BaseRecyclerViewHolder<OLXJTaskEntity> {

        @BindByTag("itemXJPathIndex")
        TextView itemXJPathIndex;  //序号

        @BindByTag("itemXJPath")
        TextView itemXJPath;  //路线

        @BindByTag("taskResponsiblePerson")
        TextView taskResponsiblePerson; //负责人

        @BindByTag("taskStartEndTime")
        TextView taskStartEndTime;  //起止时间

        @BindByTag("taskStatus")
        TextView taskStatus;  //任务状态

        @BindByTag("rateRl")
        RelativeLayout rateRl;
        @BindByTag("rate")
        TextView rate; // 巡检效率

        @BindByTag("taskExpandBtn")
        ImageView taskExpandBtn;

        @BindByTag("taskAreaListView")
        RecyclerView taskAreaListView;

        OLXJAreaRecordsListAdapter mOLXJAreaRecordsListAdapter;

        private long currentId;

        public ViewHolder(Context context, ViewGroup parent) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_olxj_task;
        }

        @Override
        protected void initView() {
            super.initView();
            rateRl.setVisibility(View.VISIBLE);

            taskAreaListView.setLayoutManager(new LinearLayoutManager(context));  //线性布局
            mOLXJAreaRecordsListAdapter = new OLXJAreaRecordsListAdapter(context);
            taskAreaListView.setAdapter(mOLXJAreaRecordsListAdapter);
        }

        @Override
        protected void initListener() {
            super.initListener();
            taskExpandBtn.setOnClickListener(v -> {
                int position = getAdapterPosition();
                boolean isExpand = expandPosition == position;
                if (!isExpand) {
                    expand(position);
                    onItemChildViewClick(taskExpandBtn, 1, getItem(position));
                } else {
                    shrink(position);
                }
            });

            mOLXJAreaRecordsListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
                onItemChildViewClick(taskAreaListView, action, obj);
            });
        }

        private void shrink(int position) {
            expandPosition = -1;
            notifyItemChanged(position);

        }

        private void expand(int position) {
            expandPosition = position;
            notifyItemChanged(expandPosition);
        }

        private void doShrink() {
            taskExpandBtn.setImageResource(R.drawable.ic_zk);
            taskAreaListView.setVisibility(View.GONE);
        }

        @SuppressLint("CheckResult")
        private void doExpand(int position) {
            taskExpandBtn.setImageResource(R.drawable.ic_sq);
            taskAreaListView.setVisibility(View.VISIBLE);

            OLXJTaskEntity taskEntity = getItem(position);

            if (currentId == taskEntity.id) {
                mOLXJAreaRecordsListAdapter.notifyDataSetChanged();
                return;
            }

            mOLXJAreaRecordsListAdapter.clear();

            if (mOLXJAreaEntities == null || mOLXJAreaEntities.size() == 0) {
//                ToastUtils.show(context,"无巡检区域数据，请检查系统或联系相关人员咨询 ");
                return;
            }

            mOLXJAreaRecordsListAdapter.addList(mOLXJAreaEntities);
            mOLXJAreaRecordsListAdapter.notifyDataSetChanged();

            currentId = taskEntity.id;

        }

        @Override
        protected void update(OLXJTaskEntity data) {
            int position = getAdapterPosition();
            if (data.isStart) {
                taskStatus.setBackgroundResource(R.drawable.sh_task_status_going);
            }

            if (expandPosition == position) {
                doExpand(position);
            } else {
                doShrink();
            }

            itemXJPathIndex.setText(String.valueOf(position + 1));
            rate.setText(Util.formatPercent(data.xjRate == null ? 0 : data.xjRate.doubleValue(),2));
            itemXJPath.setText(data.workGroupID.name);
            taskResponsiblePerson.setText(data.resstaffID.name);
            taskStartEndTime.setText(
                    String.format("%s  ~  %s",
                            DateUtil.dateFormat(data.starTime, "MM-dd HH:mm:ss"),
                            DateUtil.dateFormat(data.endTime, "MM-dd HH:mm:ss")));
            taskStatus.setText(data.linkState.value);
            if (Constant.XJTaskLinkStatus.status5.equals(data.linkState.id) || Constant.XJTaskLinkStatus.status7.equals(data.linkState.id)
                    || Constant.XJTaskLinkStatus.status4.equals(data.linkState.id)){
                taskStatus.setTextColor(context.getResources().getColor(R.color.red));
            }else {
                taskStatus.setTextColor(context.getResources().getColor(R.color.xjAreaBlue));
            }

        }

    }

}
