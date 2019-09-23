package com.supcon.mes.module_xj.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomExpandableTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.QXGLEntity;
import com.supcon.mes.middleware.model.bean.XJAreaEntity;
import com.supcon.mes.middleware.model.bean.XJAreaEntityDao;
import com.supcon.mes.middleware.model.bean.XJPathEntity;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntity;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntityDao;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_xj.R;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;


/**
 * Created by zhangwenshuai1 on 2018/3/12.
 */

public class XJPathListAdapter extends BaseListDataRecyclerViewAdapter<XJPathEntity> {

//    private SparseArray<Boolean> expandList = new SparseArray<>();
    private int expandPosition = -1;
    private SparseArray<Boolean> startList = new SparseArray<>();

    public XJPathListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<XJPathEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<XJPathEntity> implements View.OnClickListener{

        @BindByTag("itemSwipeLayout")
        CustomSwipeLayout itemSwipeLayout;

        @BindByTag("main")
        LinearLayout main;

        @BindByTag("itemViewDelBtn")
        TextView itemViewDelBtn;//取消任务

        @BindByTag("itemXJPathIndex")
        TextView itemXJPathIndex;  //序号

//        @BindByTag("taskTableNo")
//        CustomExpandableTextView taskTableNo; //单据编号

        @BindByTag("itemXJPath")
        TextView itemXJPath;  //路线

        @BindByTag("taskResponsiblePerson")
        TextView taskResponsiblePerson; //负责人

        @BindByTag("taskStartEndTime")
        TextView taskStartEndTime;  //起止时间

        @BindByTag("taskStatus")
        TextView taskStatus;  //任务状态

        @BindByTag("taskStartBtn")
        ImageView taskStartBtn;

        @BindByTag("taskExpandBtn")
        ImageView taskExpandBtn;

        @BindByTag("taskAreaListView")
        RecyclerView taskAreaListView;

        XJAreaListAdapter mXJAreaListAdapter;

        private long currentId;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initBind() {
            super.initBind();

        }

        @Override
        protected int layoutId() {
            return R.layout.item_xj_path;
        }

        @Override
        protected void initView() {
            super.initView();
            taskAreaListView.setLayoutManager(new LinearLayoutManager(context));  //线性布局
            mXJAreaListAdapter = new XJAreaListAdapter(context);
            taskAreaListView.setAdapter(mXJAreaListAdapter);
        }

        @Override
        protected void initListener() {
            super.initListener();
//            main.setOnClickListener(this);
            itemViewDelBtn.setOnClickListener(this);
            main.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemSwipeLayout.open();
                    return true;
                }
            });
//            itemView.setOnClickListener(this);  //监听回调

            taskStartBtn.setOnClickListener(v -> {

                XJPathEntity xjPathEntity = getItem(getAdapterPosition());

                //首先判断是可以提前开始或延后开始
                if(!xjPathEntity.isStart && "待检".equals(xjPathEntity.state) ){ //未开始
                    if (startJudge(xjPathEntity)){
                        return;
                    }
                }else if (xjPathEntity.isStart && "待检".equals(xjPathEntity.state)){
                    if (endJudge(xjPathEntity)){
                        return;
                    }

                }

                int position = getAdapterPosition();
                Boolean isStart = startList.get(position);

                if(!isStart && "待检".equals(xjPathEntity.state)){
                    start(position);
                    expand(position);
                }
                else if (isStart && "待检".equals(xjPathEntity.state)){
                    stop(position);
                    shrink(position);
                }

                onItemChildViewClick(taskStartBtn, xjPathEntity.isStart ? 1 : 0, xjPathEntity);

            });

            taskExpandBtn.setOnClickListener(v -> {

                int position = getAdapterPosition();
                boolean isExPand = expandPosition == position;
                if(!isExPand){
                    expand(position);
                    onItemChildViewClick(taskExpandBtn, 1, getItem(position));
                }
                else{
                    shrink(position);
                    onItemChildViewClick(taskExpandBtn, 0, getItem(position));
                }


            });

            mXJAreaListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> ViewHolder.this.onItemChildViewClick(taskAreaListView, action, obj));

        }

        private void start(int position){
            startList.put(position, true);
            notifyItemChanged(position);
        }

        private void stop(int position){
            startList.put(position, false);
            notifyItemChanged(position);
        }

        private void shrink(int position) {
//            if(!expandList.get(position)){
//                return;
//            }
//            //shrink
//            expandList.put(position, false);
            expandPosition = -1;
            notifyItemChanged(position);

        }

        private void expand(int position) {
//            if(expandList.get(position)){
//                return;
//            }
//            //expand
//            expandList.put(position, true);
            int oldPosition = expandPosition;
            expandPosition = position;
            notifyItemChanged(expandPosition);
            notifyItemChanged(oldPosition);
        }

        private void doShrink(){
            taskExpandBtn.setImageResource(R.drawable.ic_btn_zk);
            taskAreaListView.setVisibility(View.GONE);
//            mXJAreaListAdapter.clear();
//            mXJAreaListAdapter.notifyDataSetChanged();
        }

        @SuppressLint("CheckResult")
        private void doExpand(int position){

            taskExpandBtn.setImageResource(R.drawable.ic_btn_sq);
            taskAreaListView.setVisibility(View.VISIBLE);

            XJPathEntity xjPathEntity = getItem(position);

            if(currentId == xjPathEntity.id){
                mXJAreaListAdapter.notifyDataSetChanged();
                return;
            }

            mXJAreaListAdapter.clear();
            List<XJAreaEntity> areas = EamApplication.dao().getXJAreaEntityDao().queryBuilder()
                    .where(XJAreaEntityDao.Properties.TaskId.eq(getItem(position).id),XJAreaEntityDao.Properties.Ip.eq(EamApplication.getIp()))
                    .list();

            if (areas.size() == 0){

                ToastUtils.show(context,"无巡检区域列表");
            }

            mXJAreaListAdapter.addList(areas);
            mXJAreaListAdapter.notifyDataSetChanged();

            currentId = xjPathEntity.id;
//
//            Flowable.timer(200, TimeUnit.MILLISECONDS)
//                    .compose(RxSchedulers.io_main())
//                    .subscribe(new Consumer<Long>() {
//                        @Override
//                        public void accept(Long aLong) throws Exception {
//
//                            XJPathEntity xjPathEntity = getItem(position);
//
//                            if(currentId == xjPathEntity.id){
//                                return;
//                            }
//
//                            mXJAreaListAdapter.clear();
//                            List<XJAreaEntity> areas = EamApplication.dao().getXJAreaEntityDao().queryBuilder()
//                                    .where(XJAreaEntityDao.Properties.TaskId.eq(getItem(position).id))
//                                    .list();
//
//                            if (areas.size() == 0){
//
//                                ToastUtils.show(context,"无巡检区域列表");
//                            }
//
//                            mXJAreaListAdapter.addList(areas);
//                            mXJAreaListAdapter.notifyDataSetChanged();
//
//                            currentId = xjPathEntity.id;
//                        }
//                    });


        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            XJPathEntity item = getItem(adapterPosition);
            itemSwipeLayout.close();
            onItemChildViewClick(v,0,item);  //点击事件传递给Activity

//            int position = getAdapterPosition();
//            if(expandList.get(position)){
//                shrink(position);
//            }
//            else{
//                expand(position);
//            }

        }



        @Override
        protected void update(XJPathEntity data) {
            int position = getAdapterPosition();

//            if(expandList.get(position) == null)
//                expandList.put(getAdapterPosition(), false);
//
            if(startList.get(position) == null)
                startList.put(getAdapterPosition(), false);

//            if(expandList.get(position)){
//                doExpand(position);
//            }
//            else{
//                doShrink();
//            }

            if(expandPosition == position){
                doExpand(position);
            }
            else{
                doShrink();
            }

            if(data.isStart && "待检".equals(data.state)){
                taskStartBtn.setImageResource(R.drawable.ic_btn_end);
            }else if (data.isStart && "已检".equals(data.state)){
                taskStartBtn.setImageResource(R.drawable.ic_btn_start_no);
            }
            else{
                taskStartBtn.setImageResource(R.drawable.ic_btn_start);
            }

            itemXJPathIndex.setText(String.valueOf(position+1));
//            taskTableNo.setText(data.tableNo);
            itemXJPath.setText(data.pathName);
            taskResponsiblePerson.setText(data.responsibler);
            taskStartEndTime.setText(String.format("%s  ~  %s", data.startTime.substring(5, 16), data.endTime.substring(5, 16)));
            taskStatus.setText(data.state);
        }

        /**
         *@author zhangwenshuai1
         *@date 2018/4/4
         *@description 开始时间判断
         *
         */
        private boolean startJudge(XJPathEntity xjPathEntity){
            if (xjPathEntity.startAdv > 0){
                long startTimeLong = DateUtil.dateFormat(xjPathEntity.startTime,"yyyy-MM-dd HH:mm:ss");
                if ((startTimeLong-((xjPathEntity.startAdv)*60*60*1000)) > new Date().getTime()){
                    SnackbarHelper.showError(itemView,"该巡检任务不允许提前"+xjPathEntity.startAdv+"小时开始");
                    return true;
                }
            }
            if (xjPathEntity.startDelay > 0){
                long startTimeLong = DateUtil.dateFormat(xjPathEntity.startTime,"yyyy-MM-dd HH:mm:ss");
                if ((startTimeLong+((xjPathEntity.startDelay)*60*60*1000)) <  new Date().getTime()){
                    SnackbarHelper.showError(itemView,"该巡检任务不允许延迟"+xjPathEntity.startDelay+"小时开始");
                    return true;
                }
            }

            return false;

        }

        /**
         *@author zhangwenshuai1
         *@date 2018/4/4
         *@description 结束时间判断
         *
         */
        private boolean endJudge(XJPathEntity xjPathEntity){
            if (xjPathEntity.endDelay > 0){
                long endTimeLong = DateUtil.dateFormat(xjPathEntity.endTime,"yyyy-MM-dd HH:mm:ss");
                if ((endTimeLong+((xjPathEntity.endDelay)*60*60*1000)) <  new Date().getTime()){
                    SnackbarHelper.showError(itemView,"该巡检任务不允许延迟"+xjPathEntity.endDelay+"小时结束");
                    return true;
                }
            }
//            List<XJAreaEntity> list = InspectionAppication.dao().getXJAreaEntityDao().queryBuilder().where(XJAreaEntityDao.Properties.TaskId.eq(xjPathEntity.id)).list();
            List<XJWorkItemEntity> list = EamApplication.dao().getXJWorkItemEntityDao().queryBuilder().
                    where(XJWorkItemEntityDao.Properties.TaskId.eq(xjPathEntity.id),XJWorkItemEntityDao.Properties.Ip.eq(EamApplication.getIp()),
                    XJWorkItemEntityDao.Properties.IsFinished.eq(false)).limit(1).list();
            if (list.size() > 0){
                SnackbarHelper.showMessage(itemView,"存在未完成的巡检项，不可结束巡检任务");
                return true;
            }

            return false;
        }



    }



}
