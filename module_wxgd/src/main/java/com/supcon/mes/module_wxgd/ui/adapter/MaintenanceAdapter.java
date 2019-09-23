package com.supcon.mes.module_wxgd.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.model.bean.MaintainEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wxgd.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MaintenanceAdapter extends BaseListDataRecyclerViewAdapter<MaintainEntity> {

    public boolean editable;
    private String tableStatus; //单据状态

    public MaintenanceAdapter(Context context, boolean editable) {
        super(context);
        this.editable = editable;
    }

    @Override
    protected BaseRecyclerViewHolder<MaintainEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setTableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }

    class ViewHolder extends BaseRecyclerViewHolder<MaintainEntity> {

        @BindByTag("itemSwipeLayout")
        CustomSwipeLayout itemSwipeLayout;
        @BindByTag("main")
        LinearLayout main;
        @BindByTag("index")
        TextView index;
        @BindByTag("itemViewDelBtn")
        TextView itemViewDelBtn;

        @BindByTag("sparePartName")
        CustomVerticalTextView sparePartName;
        @BindByTag("attachEam")
        CustomVerticalTextView attachEam;

        @BindByTag("timeLayout")
        LinearLayout timeLayout;
        @BindByTag("durationLayout")
        LinearLayout durationLayout;

        @BindByTag("lastTime")
        CustomVerticalTextView lastTime;
        @BindByTag("nextTime")
        CustomVerticalTextView nextTime;

        @BindByTag("lastDuration")
        CustomVerticalTextView lastDuration;
        @BindByTag("nextDuration")
        CustomVerticalTextView nextDuration;

        @BindByTag("claim")
        CustomVerticalEditText claim;
        @BindByTag("content")
        CustomVerticalEditText content;

        @BindByTag("chkBox")
        CheckBox chkBox;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_wxgd_maintenance;
        }

        @Override
        protected void initView() {
            super.initView();
            chkBox.setVisibility(View.GONE);
            if (!editable) {
                itemViewDelBtn.setVisibility(View.GONE);
            }
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();


            main.setOnLongClickListener(v -> {
                itemSwipeLayout.open();
                return true;
            });

            itemViewDelBtn.setOnClickListener(v -> {
                MaintainEntity maintainEntity = getItem(getAdapterPosition());
                itemSwipeLayout.close();
                if (!editable) {
                    ToastUtils.show(context, tableStatus + "环节，维修人员不允许删除!");
                    return;
                }
                if (TextUtils.isEmpty(maintainEntity.basicJwx)) {
                    new CustomDialog(context)
                            .twoButtonAlertDialog("确认删除该维保业务吗?")
                            .bindView(R.id.redBtn, "确认")
                            .bindView(R.id.grayBtn, "取消")
                            .bindClickListener(R.id.redBtn, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    List<MaintainEntity> list = MaintenanceAdapter.this.getList();
                                    list.remove(getAdapterPosition());
                                    EventBus.getDefault().post(new RefreshEvent(maintainEntity.id));
                                }
                            }, true)
                            .bindClickListener(R.id.grayBtn, null, true)
                            .show();
                } else {
                    ToastUtils.show(context, "所选数据为历史备件数据，不允许删除！");
                }

            });
        }

        @Override
        protected void update(MaintainEntity data) {
            index.setText(String.valueOf(getAdapterPosition() + 1));
            sparePartName.setContent(Util.strFormat(data.getJwxItem().getSparePartId().getProductID().productName));
            attachEam.setContent(Util.strFormat(data.getJwxItem().getAttachEamId().getAttachEamId().name));

            if (data.getJwxItem().isDuration()) {
                durationLayout.setVisibility(View.VISIBLE);
                lastDuration.setValue(Util.big2(data.getJwxItem().lastDuration));
                nextDuration.setValue(Util.big2(data.getJwxItem().nextDuration));
            } else {
                timeLayout.setVisibility(View.VISIBLE);
                lastTime.setContent(data.getJwxItem().lastTime != null ? DateUtil.dateFormat(data.getJwxItem().lastTime) : "");
                nextTime.setContent(data.getJwxItem().nextTime != null ? DateUtil.dateFormat(data.getJwxItem().nextTime) : "");
            }

            claim.setContent(Util.strFormat(data.getJwxItem().claim));
            content.setContent(Util.strFormat(data.getJwxItem().content));

        }
    }

}
