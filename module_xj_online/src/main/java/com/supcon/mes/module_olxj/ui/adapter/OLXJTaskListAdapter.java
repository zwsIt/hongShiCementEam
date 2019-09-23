package com.supcon.mes.module_olxj.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.model.bean.OLXJAreaEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJTaskEntity;

import java.util.Date;
import java.util.List;


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

    public void resertExpandPosition() {
        this.expandPosition = -1;
    }

    public void expand() {
        this.expandPosition = 0;
        notifyItemChanged(expandPosition);
    }
//    public void setLisenter(RecyclerView contentView) {
//        contentView.setOnTouchListener(new RecyclerViewOnTouchListener());
//
//    }

    public boolean isAllFinished() {
        boolean result = true;
        if (mOLXJAreaEntities == null) {
            return result;
        }
        for (OLXJAreaEntity areaEntity : mOLXJAreaEntities) {
            if (!"1".equals(areaEntity.finishType)) {
                return false;
            }
        }

        return result;
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

        @BindByTag("listLayout")
        LinearLayout listLayout;
//        @BindByTag("mapLayout")
//        LinearLayout mapLayout;

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

//        @BindByTag("progressBar")
//        ProgressBar progressBar;
//        @BindByTag("webView")
//        BridgeWebView webView;

        OLXJAreaListAdapter mOLXJAreaListAdapter;

        private long currentId;

        private boolean isFold = true;

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

//            webView.setFocusableInTouchMode(true);
//            webView.setWebViewClient(new MapWebViewClient(webView));
//            webView.setWebChromeClient(new MyWebChromeClient());
//            WebSettings settings = webView.getSettings();
////            settings.setAppCacheEnabled(true);
//            settings.setJavaScriptEnabled(true);
//            settings.setAllowFileAccess(true);
//            settings.setDatabaseEnabled(true);
//            settings.setDomStorageEnabled(true);
//            settings.setGeolocationEnabled(true);
//            settings.setUseWideViewPort(true);
//
//            settings.setBuiltInZoomControls(true); // 显示放大缩小
//            settings.setSupportZoom(true); // 可以缩放
//            settings.setDisplayZoomControls(true);
//            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//
//            settings.setLoadWithOverviewMode(true);

        }

        @Override
        protected void initListener() {
            super.initListener();

            taskExpandBtn.setOnClickListener(v -> {
                if (map) {
                    return;
                }
                int position = getAdapterPosition();
                boolean isExPand = expandPosition == position;
                if (!isExPand) {
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


//            if(webView!=null){
//                webView.registerHandler("areaClick", new BridgeHandler() {
//
//                    @Override
//                    public void handler(String data, CallBackFunction function) {
//                        LogUtil.e("areaClick" + data);
//                        try {
//                            JSONObject jsonObject = new JSONObject(data);
//                            String code = jsonObject.getString("id");
//                            if(mOLXJAreaEntities == null){
//                                onItemChildViewClick(taskExpandBtn, 0, getItem(0));
//                            }
//                            else if (!TextUtils.isEmpty(code))
//                                for (OLXJAreaEntity areaEntity : mOLXJAreaEntities) {
//                                    if (code.equals(areaEntity._code)) {
//                                        onItemChildViewClick(taskAreaListView, 0, areaEntity);
//                                        return;
//                                    }
//                                }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                });
//            }
        }

        private void shrink(int position) {
            expandPosition = -1;
            notifyItemChanged(position);

        }

        private void expand(int position) {
//            int oldPosition = expandPosition;
            expandPosition = position;
            notifyItemChanged(expandPosition);
//            notifyItemChanged(oldPosition);
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

            mOLXJAreaListAdapter.clear();


            if (mOLXJAreaEntities == null || mOLXJAreaEntities.size() == 0) {

//                ToastUtils.show(context,"无巡检区域列表");
                return;
            }

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
//            if (map) {
//                listLayout.setVisibility(View.GONE);
//                taskExpandBtn.setVisibility(View.GONE);
//                mapLayout.setVisibility(View.VISIBLE);
//
//                //当页面正在加载时，禁止链接的点击事件
//                Map<String, String> header = new HashMap<>();
//                String url = "http://" + EamApplication.getIp() + ":" + EamApplication.getPort()
//                        + Constant.WebUrl.XJ + data.id + "&WorkGroupID=" + data.workGroupID.id;
//                if (!TextUtils.isEmpty(EamApplication.getCooki())) {
//                    header.put("Cookie", EamApplication.getCooki());
//                }
//                if (!TextUtils.isEmpty(EamApplication.getAuthorization())) {
//                    header.put("Authorization", EamApplication.getAuthorization());
//                }
//
//                webView.loadUrl(url, header);
//            } else {
//                mapLayout.setVisibility(View.GONE);
            listLayout.setVisibility(View.VISIBLE);
            taskExpandBtn.setVisibility(View.VISIBLE);
//            }


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
            taskStatus.setText(data.state);

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
