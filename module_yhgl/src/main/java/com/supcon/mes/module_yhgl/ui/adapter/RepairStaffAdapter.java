package com.supcon.mes.module_yhgl.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
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
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.model.bean.RepairStaffEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.module_yhgl.R;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.List;

public class RepairStaffAdapter extends BaseListDataRecyclerViewAdapter<RepairStaffEntity> {

    public boolean editable;
    private String tableStatus; //单据状态

    public RepairStaffAdapter(Context context, boolean editable) {
        super(context);
        this.editable = editable;
    }

    @Override
    protected BaseRecyclerViewHolder<RepairStaffEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setTableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }

    class ViewHolder extends BaseRecyclerViewHolder<RepairStaffEntity> implements OnChildViewClickListener {

        @BindByTag("itemSwipeLayout")
        CustomSwipeLayout itemSwipeLayout;
        @BindByTag("main")
        LinearLayout main;
        @BindByTag("index")
        TextView index;
        @BindByTag("repairStaffDelete")
        ImageView repairStaffDelete;
        @BindByTag("repairStaffName")
        CustomVerticalTextView repairStaffName;
        @BindByTag("workHour")
        CustomVerticalEditText workHour;
        @BindByTag("actualStartTime")
        CustomVerticalDateView actualStartTime;
        @BindByTag("actualEndTime")
        CustomVerticalDateView actualEndTime;
        @BindByTag("remark")
        CustomVerticalEditText remark;
        @BindByTag("itemViewDelBtn")
        TextView itemViewDelBtn;
        @BindByTag("chkBox")
        CheckBox chkBox;


        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_yhgl_repairstaff;
        }

        @Override
        protected void initView() {
            super.initView();
            if (!editable) {
                chkBox.setVisibility(View.GONE);
                itemViewDelBtn.setVisibility(View.GONE);
            }
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();

            repairStaffName.setOnChildViewClickListener(this);

            RxTextView.textChanges(workHour.editText())
                    .skipInitialValue()
                    .subscribe(charSequence -> {
                        RepairStaffEntity repairStaffEntity = getItem(getAdapterPosition());
                        if (TextUtils.isEmpty(charSequence)) {
                            if (repairStaffEntity != null) {
                                repairStaffEntity.workHour = BigDecimal.valueOf(0);
                            }
                            return;
                        }
                        if (charSequence.toString().indexOf(".") == 0) {
//                            ToastUtils.show(context,"首位禁止輸入小数点！");
                            workHour.setInput(null);
                            return;
                        }
                        repairStaffEntity.workHour = new BigDecimal(charSequence.toString());
                    });

            actualStartTime.setOnChildViewClickListener(new OnChildViewClickListener() {
                @Override
                public void onChildViewClick(View childView, int action, Object obj) {
                    onItemChildViewClick(actualStartTime, action, getItem(getAdapterPosition()));
                }
            });
//            actualStartTime.setOnChildViewClickListener(this::onChildViewClick);
            actualEndTime.setOnChildViewClickListener(this::onChildViewClick);

            main.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemSwipeLayout.open();
                    return true;
                }
            });

            itemViewDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RepairStaffEntity repairStaffEntity = getItem(getAdapterPosition());
                    itemSwipeLayout.close();
                    if (!editable) {
                        ToastUtils.show(context, tableStatus + "环节，维修人员不允许删除!");
                        return;
                    }
                    new CustomDialog(context)
                            .twoButtonAlertDialog("确认删除该人员：" + (repairStaffEntity.repairStaff == null ? "--" : repairStaffEntity.repairStaff.name))
                            .bindView(R.id.redBtn, "确认")
                            .bindView(R.id.grayBtn, "取消")
                            .bindClickListener(R.id.redBtn, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    List<RepairStaffEntity> list = RepairStaffAdapter.this.getList();
                                    list.remove(getAdapterPosition());
                                    EventBus.getDefault().post(new RefreshEvent(repairStaffEntity.id));
                                }
                            }, true)
                            .bindClickListener(R.id.grayBtn, null, true)
                            .show();
                }
            });

            RxTextView.textChanges(remark.editText())
                    .skipInitialValue()
                    .subscribe(charSequence -> {
                        RepairStaffEntity repairStaffEntity = getItem(getAdapterPosition());
                        if (repairStaffEntity == null) {
                            return;
                        }
                        repairStaffEntity.remark = charSequence.toString();
                    });

        }

        @Override
        protected void update(RepairStaffEntity data) {
            index.setText(String.valueOf(getAdapterPosition() + 1));

            if (editable) {
                workHour.setEditable(true);
                remark.setEditable(true);
                actualStartTime.setEditable(true);
                actualEndTime.setEditable(true);
                remark.setEditable(true);
            } else {
                workHour.setEditable(false);
                remark.setEditable(false);
                actualStartTime.setEditable(false);
                actualEndTime.setEditable(false);
                remark.setEditable(false);
            }

            if (data.repairStaff != null) {
                repairStaffName.setValue(data.repairStaff.name);
            } else {
                repairStaffName.setValue("");
            }

            workHour.setInput(data.workHour == null ? "" : String.valueOf(data.workHour.setScale(2, BigDecimal.ROUND_HALF_UP)));
            workHour.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            if (data.startTime != null) {
                actualStartTime.setDate(DateUtil.dateTimeFormat(data.startTime));
            } else {
                actualStartTime.setDate(null);
            }

            if (data.endTime != null) {
                actualEndTime.setDate(DateUtil.dateTimeFormat(data.endTime));
            } else {
                actualEndTime.setDate(null);
            }

            remark.setInput(data.remark);
        }

        @Override
        public void onChildViewClick(View childView, int action, Object obj) {
            RepairStaffEntity repairStaffEntity = getItem(getAdapterPosition());
            onItemChildViewClick(childView, action, repairStaffEntity);
        }
    }

}
