package com.supcon.mes.module_olxj.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.LogUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.constant.OLXJConstant;
import com.supcon.mes.module_olxj.model.bean.OLXJAreaEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJTaskEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;
import com.supcon.mes.module_olxj.ui.OLXJTodayTaskRecordsActivity;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;


/**
 * Created by zhangwenshuai1 on 2018/3/12.
 */

public class OLXJTaskListAdapter extends BaseListDataRecyclerViewAdapter<OLXJTaskEntity> {

    private int expandPosition = -1;
    private List<OLXJAreaEntity> mOLXJAreaEntities;
    private boolean map;
    private ViewHolder viewHolder;

    public OLXJTaskListAdapter(Context context) {
        super(context);
    }

    public void setAreaEntities(List<OLXJAreaEntity> olxjAreaEntities) {
        this.mOLXJAreaEntities = olxjAreaEntities;
//        if(!map)
        notifyDataSetChanged();
    }

    public void setMap(boolean map) {
        this.map = map;
    }

    public void resetExpandPosition() {
        this.expandPosition = -1;
    }

    public void expand() {
        this.expandPosition = 0;
        notifyItemChanged(expandPosition);
    }

    public boolean isAllFinished() {
        if (mOLXJAreaEntities == null) {
            return true;
        }
        for (OLXJAreaEntity areaEntity : mOLXJAreaEntities) {
            if ("0".equals(areaEntity.finishType) || (TextUtils.isEmpty(areaEntity.finishType) && areaEntity.workItemEntities.get(0).getTaskSignID().cardTime == null)) {
                return false;
            }
        }

        return true;
    }

    public boolean isExpand() {
        return expandPosition == viewHolder.getAdapterPosition();
    }

    @Override
    protected BaseRecyclerViewHolder<OLXJTaskEntity> getViewHolder(int viewType) {
        viewHolder = new ViewHolder(context, parent);
        return viewHolder;
    }

    class ViewHolder extends BaseRecyclerViewHolder<OLXJTaskEntity> implements View.OnClickListener {

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

        @BindByTag("taskExpandBtn")
        ImageView taskExpandBtn;

        @BindByTag("taskAreaListView")
        RecyclerView taskAreaListView;


        OLXJAreaListAdapter mOLXJAreaListAdapter;

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
            taskAreaListView.setLayoutManager(new LinearLayoutManager(context));  //线性布局
            mOLXJAreaListAdapter = new OLXJAreaListAdapter(context);
            taskAreaListView.setAdapter(mOLXJAreaListAdapter);
        }

        @Override
        protected void initListener() {
            super.initListener();

            taskExpandBtn.setOnClickListener(v -> {
                if (map) {
                    return;
                }
                int position = getAdapterPosition();
                boolean isExpand = expandPosition == position;
                if (!isExpand) {
                    expand(position);
                    onItemChildViewClick(taskExpandBtn, 1, getItem(position));
                } else {
                    shrink(position);
                    onItemChildViewClick(taskExpandBtn, 0, getItem(position));
                }

            });

            mOLXJAreaListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
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
                mOLXJAreaListAdapter.notifyDataSetChanged();
                return;
            }

            if (mOLXJAreaEntities == null || mOLXJAreaEntities.size() == 0) {
//                ToastUtils.show(context,"无巡检区域数据，请检查系统或联系相关人员咨询 ");
                return;
            }
            mOLXJAreaListAdapter.clear();

            mOLXJAreaListAdapter.addList(mOLXJAreaEntities);
            mOLXJAreaListAdapter.notifyDataSetChanged();

            currentId = taskEntity.id;

        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            OLXJTaskEntity item = getItem(adapterPosition);
            onItemChildViewClick(v, 0, item);  //点击事件传递给Activity

        }


        @Override
        protected void update(OLXJTaskEntity data) {
            int position = getAdapterPosition();
            taskExpandBtn.setVisibility(View.VISIBLE);

            if (data.isStart) {
                taskStatus.setBackgroundResource(R.drawable.sh_task_status_going);
            }

            if (expandPosition == position) {
                doExpand(position);
            } else {
                doShrink();
            }

            itemXJPathIndex.setText(String.valueOf(position + 1));
//            taskTableNo.setText(data.tableNo);
            itemXJPath.setText(data.workGroupID.name);
            taskResponsiblePerson.setText(data.resstaffID.name);
            taskStartEndTime.setText(
                    String.format("%s  ~  %s",
                            DateUtil.dateFormat(data.starTime, "MM-dd HH:mm:ss"),
                            DateUtil.dateFormat(data.endTime, "MM-dd HH:mm:ss")));
            if (context instanceof OLXJTodayTaskRecordsActivity){ // 今日巡检(巡检记录)
                taskStatus.setText(data.linkState.value);
                if (Constant.XJTaskLinkStatus.status5.equals(data.linkState.id) || Constant.XJTaskLinkStatus.status7.equals(data.linkState.id)
                        || Constant.XJTaskLinkStatus.status4.equals(data.linkState.id)){
                    taskStatus.setTextColor(context.getResources().getColor(R.color.red));
                }else {
                    taskStatus.setTextColor(context.getResources().getColor(R.color.xjAreaBlue));
                }

            }else {
                taskStatus.setText(data.state);
            }

        }

        /**
         * @author zhangwenshuai1
         * @date 2018/4/4
         * @description 开始时间判断
         */
        private boolean startJudge(OLXJTaskEntity taskEntity) {

            if (taskEntity.starTime == null) {
                return false;
            }
            if (taskEntity.startAdv > 0) {
                long startTimeLong = taskEntity.starTime;
                if ((startTimeLong - ((taskEntity.startAdv) * 60 * 60 * 1000)) > new Date().getTime()) {
                    SnackbarHelper.showError(itemView, "该巡检任务不允许提前" + taskEntity.startAdv + "小时开始");
                    return true;
                }
            }
            if (taskEntity.startDelay > 0) {
                long startTimeLong = taskEntity.starTime;
                if ((startTimeLong + ((taskEntity.startDelay) * 60 * 60 * 1000)) < new Date().getTime()) {
                    SnackbarHelper.showError(itemView, "该巡检任务不允许延迟" + taskEntity.startDelay + "小时开始");
                    return true;
                }
            }

            return false;

        }

        /**
         * @author zhangwenshuai1
         * @date 2018/4/4
         * @description 结束时间判断
         */
        private boolean endJudge(OLXJTaskEntity taskEntity) {

            if (taskEntity.endTime == null) {

                return false;
            }

            if (taskEntity.endDelay > 0) {
                long endTimeLong = taskEntity.endTime;
                if ((endTimeLong + ((taskEntity.endDelay) * 60 * 60 * 1000)) < new Date().getTime()) {
                    SnackbarHelper.showError(itemView, "该巡检任务不允许延迟" + taskEntity.endDelay + "小时结束");
                    return true;
                }
            }

            return false;
        }

    }

}
