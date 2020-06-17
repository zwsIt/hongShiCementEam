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
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.constant.OLXJConstant;
import com.supcon.mes.module_olxj.model.bean.OLXJAreaEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;
import com.supcon.mes.module_olxj.ui.view.TipPopwindow;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Flowable;


/**
 * Created by wangshizhan on 2018/3/27.
 * Email:wangshizhan@supcon.com
 */
public class OLXJAreaRecordsListAdapter extends BaseListDataRecyclerViewAdapter<OLXJWorkItemEntity> {

    private TipPopwindow tipPopwindow;

    public OLXJAreaRecordsListAdapter(Context context) {
        super(context);
        tipPopwindow = new TipPopwindow(((AppCompatActivity) context));
    }

    public OLXJAreaRecordsListAdapter(Context context, List<OLXJWorkItemEntity> list) {
        super(context, list);
    }

    @Override
    protected BaseRecyclerViewHolder<OLXJWorkItemEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }


    class ViewHolder extends BaseRecyclerViewHolder<OLXJWorkItemEntity> {

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

        @BindByTag("itemExceptionNum")
        TextView itemExceptionNum;  //异常项目

        @BindByTag("itemAreaProgress")
        TextView itemAreaProgress; //区域任务进度

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
                OLXJWorkItemEntity xjAreaEntity = getItem(getAdapterPosition());
                        onItemChildViewClick(itemView, 99, xjAreaEntity);
                    }
            );
//            itemAreaFault.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    OLXJWorkItemEntity item = getItem(getAdapterPosition());
//                    if (!TextUtils.isEmpty(item.oldfaultMsg)) {
//                        tipPopwindow.setContent(item.oldfaultMsg);
//                        tipPopwindow.showPopupWindow(itemAreaFault);
//                    } else {
//                        ToastUtils.show(context, "故障信息不存在！");
//                    }
//                }
//            });
        }

        @Override
        protected int layoutId() {
            return R.layout.item_xj_area_records;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void update(OLXJWorkItemEntity data) {
            //边线显示
            itemAreaLineTop.setVisibility(View.VISIBLE);
            itemAreaLineBottom.setVisibility(View.VISIBLE);

            if (getAdapterPosition() == 0) {
                itemAreaLineTop.setVisibility(View.INVISIBLE);
            }
            if (getAdapterPosition() == getListSize() - 1) {
                itemAreaLineBottom.setVisibility(View.INVISIBLE);
            }
            itemAreaName.setText((getAdapterPosition() + 1) + ". " + data.workID.name);
            itemAreaProgress.setText(String.format("%d/%d", (data.totalItem - data.uncheckItem), data.totalItem));
            if (data.getTaskSignID().cardType != null){
                if (Constant.CARD_TYPE.cardType1.equals(data.getTaskSignID().cardType.id)){
                    itemAreaTime.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_nfc,null),null,null,null);
                }else {
                    itemAreaTime.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_sign,null),null,null,null);
                }
            }else {
                itemAreaTime.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.icon_time,null),null,null,null);
            }
            itemAreaTime.setText(data.getTaskSignID().cardTime == null ? "--" : DateUtil.dateFormat(data.getTaskSignID().cardTime, Constant.TimeString.YEAR_MONTH_DAY_HOUR_MIN_SEC));
            if (data.uncheckItem == 0){
                itemAreaName.setTextColor(Color.GRAY);
                itemAreaProgress.setTextColor(Color.GRAY);
            }else {
                itemAreaName.setTextColor(context.getResources().getColor(R.color.xjAreaBlue));
                itemAreaProgress.setTextColor(context.getResources().getColor(R.color.xjAreaBlue));
            }

            if (data.abnormalItem > 0){
                itemExceptionNum.setText("异常项数量：" + data.abnormalItem);
                itemExceptionNum.setVisibility(View.VISIBLE);
                itemAreaDot.setImageDrawable(context.getResources().getDrawable(R.drawable.dot_done_yh));
            }else {
                itemExceptionNum.setVisibility(View.GONE);
                if (data.uncheckItem == data.totalItem){ // 未完成
                    itemAreaDot.setImageDrawable(context.getResources().getDrawable(R.drawable.dot_wait));
                }else {
                    itemAreaDot.setImageDrawable(context.getResources().getDrawable(R.drawable.dot_done));
                }
            }

        }
    }
}
