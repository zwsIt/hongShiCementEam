package com.supcon.mes.module_olxj.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.model.bean.OLXJAreaEntity;
import com.supcon.mes.module_olxj.ui.view.TipPopwindow;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Flowable;


/**
 * Created by wangshizhan on 2018/3/27.
 * Email:wangshizhan@supcon.com
 */
public class OLXJAreaListAdapter extends BaseListDataRecyclerViewAdapter<OLXJAreaEntity> {

    // 用于格式化日期,作为日志文件名的一部分
    private SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
    private TipPopwindow tipPopwindow;

    public OLXJAreaListAdapter(Context context) {
        super(context);
        tipPopwindow = new TipPopwindow(((AppCompatActivity) context));
    }

    public OLXJAreaListAdapter(Context context, List<OLXJAreaEntity> list) {
        super(context, list);
    }

    @Override
    protected BaseRecyclerViewHolder<OLXJAreaEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }


    class ViewHolder extends BaseRecyclerViewHolder<OLXJAreaEntity> {

        @BindByTag("itemAreaLineTop")
        View itemAreaLineTop;  //上边线
        @BindByTag("itemAreaDot")
        ImageView itemAreaDot;  //状态图标
        @BindByTag("itemAreaLineBottom")
        View itemAreaLineBottom;  //下边线

        @BindByTag("itemAreaName")
        TextView itemAreaName;  //区域名称
        @BindByTag("itemAreaTime")
        TextView itemAreaTime;  //巡检时间
        @BindByTag("itemAreaFault")
        ImageView itemAreaFault;  //是否隐患

        @BindByTag("itemAreaProgress")
        TextView itemAreaProgress; //区域任务进度

        private boolean isFault;//是否有异常


        public ViewHolder(Context context) {
            super(context, parent);
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
                        OLXJAreaEntity xjAreaEntity = getItem(getAdapterPosition());
                        onItemChildViewClick(itemView, 0, xjAreaEntity);
                    }
            );
            itemAreaFault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OLXJAreaEntity item = getItem(getAdapterPosition());
                    if (!TextUtils.isEmpty(item.oldfaultMsg)) {
                        tipPopwindow.setContent(item.oldfaultMsg);
                        tipPopwindow.showPopupWindow(itemAreaFault);
                    } else {
                        ToastUtils.show(context, "故障信息不存在！");
                    }
                }
            });
        }

        @Override
        protected int layoutId() {
            return R.layout.item_xj_area;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void update(OLXJAreaEntity data) {
            //边线显示
            itemAreaLineTop.setVisibility(View.VISIBLE);
            itemAreaLineBottom.setVisibility(View.VISIBLE);
            if (getAdapterPosition() == 0) {
                itemAreaLineTop.setVisibility(View.INVISIBLE);
            }
            if (getAdapterPosition() == getListSize() - 1) {
                itemAreaLineBottom.setVisibility(View.INVISIBLE);
            }
            itemAreaName.setText((getAdapterPosition() + 1) + ". " + data.name);

            itemAreaFault.setVisibility(View.INVISIBLE);
            if (TextUtils.isEmpty(data.signedTime)) {
                if (TextUtils.isEmpty(data.oldfaultMsg)) {
                    itemAreaDot.setImageDrawable(context.getResources().getDrawable(R.drawable.dot_wait));
                } else {
                    itemAreaFault.setVisibility(View.VISIBLE);
                    itemAreaDot.setImageDrawable(context.getResources().getDrawable(R.drawable.dot_wait_yh));
                }
                itemAreaTime.setText("未开始");
            }

            //遍历巡检项
            //异常的序号
            AtomicInteger faultPosition = new AtomicInteger();
            //异常信息
//            StringBuffer faultMsg = new StringBuffer();
            AtomicInteger finishedNum = new AtomicInteger();
            Flowable.fromIterable(data.workItemEntities)
                    .subscribe(xjWorkItemEntity -> {
                                if (xjWorkItemEntity.isFinished) {
                                    finishedNum.getAndIncrement();
                                    if (!TextUtils.isEmpty(xjWorkItemEntity.conclusionID) && xjWorkItemEntity.conclusionID.equals("realValue/02")) {
                                        faultPosition.getAndIncrement();
                                        isFault = true;
//                                        faultMsg.append(faultPosition.get()).append(".")
//                                                .append("设备：").append(xjWorkItemEntity.eamID.code + "(").append(xjWorkItemEntity.eamID.name + ")").append("\n")
//                                                .append("隐患现象：").append(xjWorkItemEntity.content).append("\n")
//                                                .append("发现人：").append(EamApplication.getAccountInfo().staffName).append("\n");
                                    }
                                }
                            },
                            throwable -> {
                            },
                            () -> {
                                itemAreaProgress.setText(String.format("%d/%d", finishedNum.get(), data.workItemEntities.size()));
                                if (finishedNum.get() == data.workItemEntities.size()) {
                                    itemAreaName.setTextColor(Color.GRAY);
                                    itemAreaProgress.setTextColor(Color.GRAY);
                                    data.finishType = "1";
                                } else {
                                    data.finishType = "0";
                                    itemAreaName.setTextColor(Color.parseColor("#366CBC"));
                                    itemAreaProgress.setTextColor(Color.parseColor("#366CBC"));
                                }
                                if (!TextUtils.isEmpty(data.signedTime)) {
                                    if (isFault) {
//                                        data.faultMsg = faultMsg.toString();
//                                        itemAreaFault.setVisibility(View.VISIBLE);
                                        itemAreaDot.setImageDrawable(context.getResources().getDrawable(R.drawable.dot_done_yh));
                                    } else {
                                        itemAreaFault.setVisibility(View.INVISIBLE);
                                        itemAreaDot.setImageDrawable(context.getResources().getDrawable(R.drawable.dot_done));
                                    }
                                    itemAreaTime.setText(formatter.format(DateUtil.dateFormat(data.signedTime, "yyyy-MM-dd HH:mm:ss")));
                                }
                            });
        }
    }
}
