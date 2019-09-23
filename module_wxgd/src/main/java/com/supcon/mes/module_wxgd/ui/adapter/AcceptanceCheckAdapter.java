package com.supcon.mes.module_wxgd.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomVerticalDateView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.mbap.view.CustomVerticalSpinner;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.model.bean.MaintainEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.middleware.model.bean.AcceptanceCheckEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class AcceptanceCheckAdapter extends BaseListDataRecyclerViewAdapter<AcceptanceCheckEntity> {

    private boolean isEditable;

    public AcceptanceCheckAdapter(Context context, boolean isEditable) {
        super(context);
        this.isEditable = isEditable;
    }

    @Override
    protected BaseRecyclerViewHolder<AcceptanceCheckEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    class ViewHolder extends BaseRecyclerViewHolder<AcceptanceCheckEntity> implements OnChildViewClickListener {

        @BindByTag("index")
        TextView index;
        @BindByTag("acceptanceStaffName")
        CustomVerticalTextView acceptanceStaffName;
        @BindByTag("acceptanceStaffCode")
        CustomVerticalTextView acceptanceStaffCode;
        @BindByTag("acceptanceTime")
        CustomVerticalDateView acceptanceTime;
        @BindByTag("acceptanceResult")
        CustomVerticalSpinner acceptanceResult;
        @BindByTag("chkBox")
        CheckBox chkBox;

        @BindByTag("itemSwipeLayout")
        CustomSwipeLayout itemSwipeLayout;
        @BindByTag("main")
        LinearLayout main;
        @BindByTag("itemViewDelBtn")
        TextView itemViewDelBtn;


        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_acceptance;
        }

        @Override
        protected void initView() {
            super.initView();
            if (!isEditable) {
                chkBox.setVisibility(View.GONE);
                itemViewDelBtn.setVisibility(View.GONE);
            }
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();

            acceptanceStaffName.setOnChildViewClickListener(this);
            acceptanceStaffCode.setOnChildViewClickListener(this);
            acceptanceTime.setOnChildViewClickListener(this);
            acceptanceResult.setOnChildViewClickListener(this::onChildViewClick);

            main.setOnLongClickListener(v -> {
                itemSwipeLayout.open();
                return true;
            });
            itemViewDelBtn.setOnClickListener(v -> {
                AcceptanceCheckEntity item = getItem(getAdapterPosition());
                itemSwipeLayout.close();
                new CustomDialog(context)
                        .twoButtonAlertDialog("确认删除该备件：" + Util.strFormat(item.getCheckStaff().name))
                        .bindView(R.id.redBtn, "确认")
                        .bindView(R.id.grayBtn, "取消")
                        .bindClickListener(R.id.redBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                List<AcceptanceCheckEntity> list = getList();
                                AcceptanceCheckAdapter.this.list.remove(getAdapterPosition());
                                EventBus.getDefault().post(new RefreshEvent(item.id));
                            }
                        }, true)
                        .bindClickListener(R.id.grayBtn, null, true)
                        .show();
            });
        }

        @Override
        protected void update(AcceptanceCheckEntity data) {
            index.setText(String.valueOf(getAdapterPosition() + 1));

            if (data.checkStaff != null) {
                acceptanceStaffName.setValue(data.checkStaff.name);
                acceptanceStaffCode.setValue(data.checkStaff.code);
            } else {
                acceptanceStaffName.setValue("");
                acceptanceStaffCode.setValue("");
            }
            acceptanceTime.setDate(data.checkTime == null ? "" : DateUtil.dateTimeFormat(data.checkTime));

            acceptanceResult.setSpinner(data.getCheckResult().value);

        }

        @Override
        public void onChildViewClick(View childView, int action, Object obj) {
            AcceptanceCheckEntity acceptanceCheckEntity = getItem(getAdapterPosition());
            onItemChildViewClick(childView, 0, acceptanceCheckEntity);
        }
    }

}
