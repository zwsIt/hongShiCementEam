package com.supcon.mes.module_wxgd.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomNumView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.SparePartEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wxgd.R;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.List;

public class SparePartAdapter extends BaseListDataRecyclerViewAdapter<SparePartEntity> {

    private boolean editable;
    private int repairSum;
    private String tableStatus; //单据状态
    private String tableAction; //单据ViewAction

    public SparePartAdapter(Context context, boolean editable) {
        super(context);
        this.editable = editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setTableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }

    @Override
    protected BaseRecyclerViewHolder<SparePartEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    /**
     * @param
     * @return
     * @description 设置工单维修次数变量，用于判断数量是否可编辑
     * @author zhangwenshuai1 2018/9/5
     */
    public void setRepairSum(int repairSum) {
        this.repairSum = repairSum;
    }


    /**
     * @param
     * @return
     * @description 维修工单单据状态Url
     * @author zhangwenshuai1 2018/10/11
     */
    public void setTableAction(String tableAction) {
        this.tableAction = tableAction;
    }

    class ViewHolder extends BaseRecyclerViewHolder<SparePartEntity> {

        @BindByTag("index")
        TextView index;
        @BindByTag("sparePartName")
        CustomTextView sparePartName;
        @BindByTag("sum")
        CustomNumView sum;
        @BindByTag("sparePartSpecific")
        CustomTextView sparePartSpecific;
        @BindByTag("accessoryName")
        CustomTextView accessoryName;

        @BindByTag("remark")
        CustomVerticalEditText remark;

        @BindByTag("itemSwipeLayout")
        CustomSwipeLayout itemSwipeLayout;
        @BindByTag("main")
        LinearLayout main;
        @BindByTag("itemViewDelBtn")
        TextView itemViewDelBtn;
        @BindByTag("useQuantity")
        CustomTextView useQuantity;
        @BindByTag("actualQuantity")
        CustomNumView actualQuantity;
        @BindByTag("standingCrop")
        CustomTextView standingCrop;
        @BindByTag("useState")
        CustomTextView useState;
        @BindByTag("chkBox")
        CheckBox chkBox;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_sparepart;
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

            itemViewDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SparePartEntity sparePartEntity = getItem(getAdapterPosition());
                    itemSwipeLayout.close();

                    if (!editable) {
                        ToastUtils.show(context, tableStatus + "环节，备件不允许删除!");
                        return;
                    }
                    if (sparePartEntity.isWarn && sparePartEntity.sparePartId != null) {
                        ToastUtils.show(context, "来源“备件更换到期预警”的备件不允许删除!", 3000);
                        return;
                    }
                    if (!Constant.SparePartUseStatus.NO_USE.equals(sparePartEntity.getUseState().id)) {
                        ToastUtils.show(context, "已领用或领用中状态的备件不允许删除!", 3000);
                        return;
                    }
                    if (sparePartEntity.timesNum >= repairSum) {
                        new CustomDialog(context)
                                .twoButtonAlertDialog("确认删除该备件：" + (sparePartEntity.productID == null ? "--" : sparePartEntity.productID.productName))
                                .bindView(R.id.redBtn, "确认")
                                .bindView(R.id.grayBtn, "取消")
                                .bindClickListener(R.id.redBtn, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        notifyItemRemoved(getLayoutPosition());
                                        notifyItemRangeChanged(getLayoutPosition(), getItemCount());
                                        List<SparePartEntity> list = SparePartAdapter.this.getList();
                                        list.remove(getLayoutPosition());

                                        EventBus.getDefault().post(new RefreshEvent(sparePartEntity.id));
                                    }
                                }, true)
                                .bindClickListener(R.id.grayBtn, null, true)
                                .show();
                    } else {
                        ToastUtils.show(context, "历史备件数据,不允许删除!");
                    }
                }
            });

            RxTextView.textChanges(sum.getNumViewInput())
                    .skipInitialValue()
                    .subscribe(charSequence -> {

                        SparePartEntity sparePartEntity = getItem(getAdapterPosition());

                        if (TextUtils.isEmpty(charSequence)) {
                            if (sparePartEntity != null) {
                                sparePartEntity.sum = null;
                            }
                            return;
                        }
                        if (charSequence.toString().indexOf(".") == 0) {
//                            ToastUtils.show(context,"首位禁止輸入小数点！");
                            sum.getNumViewInput().setText(null);
                            return;
                        }
                        sparePartEntity.sum = new BigDecimal(charSequence.toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                    });

            RxTextView.textChanges(remark.editText())
                    .skipInitialValue()
                    .subscribe(charSequence -> {
                        SparePartEntity sparePartEntity = getItem(getAdapterPosition());
                        if (sparePartEntity == null) {
                            return;
                        }
                        sparePartEntity.remark = charSequence.toString();
                    });

            chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SparePartEntity sparePartEntity = getItem(getAdapterPosition());
                    if (isChecked) {
                        onItemChildViewClick(chkBox, 1, sparePartEntity);
                    } else {
                        onItemChildViewClick(chkBox, 0, sparePartEntity);
                    }
                }
            });
            RxTextView.textChanges(actualQuantity.getNumViewInput())
                    .skipInitialValue()
                    .filter(charSequence -> {
                        SparePartEntity sparePartEntity = getItem(getAdapterPosition());
                        if (TextUtils.isEmpty(charSequence) && sparePartEntity != null) {
                            sparePartEntity.actualQuantity = null;
                            return false;
                        }
                        return true;
                    })
                    .subscribe(charSequence -> {
                        SparePartEntity sparePartEntity = getItem(getAdapterPosition());

//                        if (TextUtils.isEmpty(charSequence)) {
//                            if (sparePartEntity != null) {
//                                sparePartEntity.actualQuantity = null;
//                            }
//                            return;
//                        }
                        if (charSequence.toString().indexOf(".") == 0) {
//                            ToastUtils.show(context,"首位禁止輸入小数点！");
                            actualQuantity.getNumViewInput().setText(null);
                            return;
                        }
                        sparePartEntity.actualQuantity = new BigDecimal(charSequence.toString());
                    });

        }

        @Override
        protected void update(SparePartEntity data) {
            index.setText(String.valueOf(getAdapterPosition() + 1));
//            sum.getNumViewInput().setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});  //输入长度万级
            sum.getNumViewInput().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            actualQuantity.getNumViewInput().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            if (editable && data.timesNum == repairSum && !Constant.SparePartUseStatus.USEING.equals(data.getUseState().id)) {
                sum.setEditable(true);
                sum.getNumViewInput().setEnabled(true);
                remark.setEditable(true);
                chkBox.setVisibility(View.VISIBLE);
            } else {
                sum.setEditable(false);
                sum.getNumViewInput().setEnabled(false);
                remark.setEditable(false);
                chkBox.setVisibility(View.GONE);
            }
            chkBox.setChecked(false);//还原状态false
//            if ((Constant.WxgdView.DISPATCH_OPEN_URL.equals(tableAction) || Constant.WxgdView.RECEIVE_OPEN_URL.equals(tableAction)) && data.timesNum == repairSum) {
//                actualQuantity.setVisibility(View.GONE);
//            } else {
//                actualQuantity.setVisibility(View.VISIBLE);
//            }
            if (Constant.WxgdView.EXECUTE_OPEN_URL.equals(tableAction) && Constant.SparePartUseStatus.NO_USE.equals(data.getUseState().id) && data.timesNum == repairSum) {
                actualQuantity.setEditable(true);
                actualQuantity.getNumViewInput().setEnabled(true);
            } else {
                actualQuantity.setEditable(false);
                actualQuantity.getNumViewInput().setEnabled(false);
            }
//            sum.setNum(data.sum != null ? data.sum.doubleValue() : 0);
            sum.getNumViewInput().setText(data.sum != null ? String.valueOf(data.sum.setScale(2, BigDecimal.ROUND_HALF_UP)) : "");

            sparePartName.setValue(data.productID != null ? Util.strFormat2(data.productID.productName) : "");
            sparePartSpecific.setValue(data.productID != null ? Util.strFormat2(data.productID.productSpecif) : "");
            accessoryName.setContent(Util.strFormat2(data.accessoryName));

            useQuantity.setValue(data.useQuantity != null ? String.valueOf(data.useQuantity.setScale(2, BigDecimal.ROUND_HALF_UP)) : "");

            if (!Constant.SparePartUseStatus.USEING.equals(data.getUseState().id)) {
                actualQuantity.getNumViewInput().setText(data.actualQuantity == null ?
                        ((data.useQuantity == null || "0.00".equals(data.useQuantity.toString())) ?
                                ((data.sum == null || "0.00".equals(data.sum.toString())) ? "" : String.valueOf(data.sum.setScale(2, BigDecimal.ROUND_HALF_UP)))
                                : String.valueOf(data.useQuantity.setScale(2, BigDecimal.ROUND_HALF_UP)))
                        : String.valueOf(data.actualQuantity.setScale(2, BigDecimal.ROUND_HALF_UP))
                );
            } else {
                actualQuantity.getNumViewInput().setText(data.actualQuantity == null ?
                        ((data.useQuantity == null || "0.00".equals(data.useQuantity.toString())) ? ""
                                : String.valueOf(data.useQuantity.setScale(2, BigDecimal.ROUND_HALF_UP)))
                        : String.valueOf(data.actualQuantity.setScale(2, BigDecimal.ROUND_HALF_UP))
                );
            }
            standingCrop.setValue(data.standingCrop == null ? "" : String.valueOf(data.standingCrop.setScale(2, BigDecimal.ROUND_HALF_UP)));
            useState.setValue(Util.strFormat2(data.getUseState().value));
            remark.setInput(data.remark);
        }
    }
}
