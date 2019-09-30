package com.supcon.mes.module_wxgd.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
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
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomNumView;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.mbap.view.CustomVerticalSpinner;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.model.bean.LubricateOilsEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wxgd.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * LubricateOilsAdapter  润滑油列表Adapter
 * created by zhangwenshuai1 2018/9/6
 */
public class LubricateOilsAdapter extends BaseListDataRecyclerViewAdapter<LubricateOilsEntity> {
    private boolean editable;
    private String tableStatus; //单据状态

    public LubricateOilsAdapter(Context context, boolean isEditable) {
        super(context);
        this.editable = isEditable;
    }

    @Override
    protected BaseRecyclerViewHolder<LubricateOilsEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setTableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }

    class ViewHolder extends BaseRecyclerViewHolder<LubricateOilsEntity> {

        @BindByTag("itemSwipeLayout")
        CustomSwipeLayout itemSwipeLayout;
        @BindByTag("main")
        LinearLayout main;
        @BindByTag("index")
        TextView index;
        @BindByTag("oilCode")
        CustomVerticalTextView oilCode;
        @BindByTag("oilName")
        CustomVerticalTextView oilName;
        @BindByTag("attachEam")
        CustomVerticalTextView attachEam;
        @BindByTag("sparePartId")
        CustomVerticalTextView sparePartId;
        @BindByTag("sum")
        CustomNumView sum;
        @BindByTag("oilType")
        CustomSpinner oilType;  //加换油
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
            return R.layout.item_lubricate_oils;
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

            RxTextView.textChanges(sum.getNumViewInput())
                    .skipInitialValue()
                    .subscribe(charSequence -> {

                        LubricateOilsEntity lubricateOilsEntity = getItem(getAdapterPosition());

                        if (TextUtils.isEmpty(charSequence)) {
                            if (lubricateOilsEntity != null) {
                                lubricateOilsEntity.oilQuantity = 0F;
                            }
                            return;
                        }
                        if (charSequence.toString().indexOf(".") == 0) {
//                            ToastUtils.show(context,"首位禁止輸入小数点！");
                            sum.getNumViewInput().setText(null);
                            return;
                        }
                        lubricateOilsEntity.oilQuantity = Float.valueOf(charSequence.toString());
                    });

            oilType.setOnChildViewClickListener(new OnChildViewClickListener() {
                @Override
                public void onChildViewClick(View childView, int action, Object obj) {
                    onItemChildViewClick(oilType, action, getItem(getAdapterPosition()));
                }
            });

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
                    LubricateOilsEntity lubricateOilsEntity = getItem(getAdapterPosition());
                    itemSwipeLayout.close();
                    if (!editable) {
                        ToastUtils.show(context, tableStatus + "环节，润滑油不允许删除!");
                        return;
                    }
                    if (TextUtils.isEmpty(lubricateOilsEntity.basicLubricate)) {
                        new CustomDialog(context)
                                .twoButtonAlertDialog("确认删除该润滑油：" + (lubricateOilsEntity.lubricate == null ? "--" : lubricateOilsEntity.lubricate.name))
                                .bindView(R.id.redBtn, "确认")
                                .bindView(R.id.grayBtn, "取消")
                                .bindClickListener(R.id.redBtn, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        List<LubricateOilsEntity> list = LubricateOilsAdapter.this.getList();
                                        list.remove(getAdapterPosition());
                                        EventBus.getDefault().post(new RefreshEvent(lubricateOilsEntity.id));
                                    }
                                }, true)
                                .bindClickListener(R.id.grayBtn, null, true)
                                .show();
                    } else {
                        ToastUtils.show(context, "历史润滑油数据,不允许删除!");
                    }
                }
            });

            RxTextView.textChanges(remark.editText())
                    .skipInitialValue()
                    .subscribe(charSequence -> {
                        LubricateOilsEntity lubricateOilsEntity = getItem(getAdapterPosition());
                        if (lubricateOilsEntity == null) {
                            return;
                        }
                        lubricateOilsEntity.remark = charSequence.toString();
                    });

        }

        @Override
        protected void update(LubricateOilsEntity data) {
            index.setText(String.valueOf(getAdapterPosition() + 1));

            sum.getNumViewInput().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            if (editable && TextUtils.isEmpty(data.basicLubricate)) {
                sum.setEditable(true);
                sum.getNumViewInput().setEnabled(true);
                oilType.setEditable(true);
                remark.setEditable(true);
            } else {
                sum.setEditable(false);
                sum.getNumViewInput().setEnabled(false);
                oilType.setEditable(false);
                remark.setEditable(false);
            }

            sum.getNumViewInput().setText(Util.big(data.oilQuantity));

            oilType.setSpinner(data.oilType != null ? Util.strFormat2(data.oilType.value) : "");
            attachEam.setValue(Util.strFormat2(data.getJwxItemID().getAttachEamId().getAttachEamId().name));
            sparePartId.setValue(Util.strFormat2(data.getJwxItemID().getSparePartId().getProductID().productName));
            oilCode.setValue(data.lubricate != null ? Util.strFormat2(data.lubricate.code) : "");
            oilName.setValue(data.lubricate != null ? Util.strFormat2(data.lubricate.name) : "");
            remark.setInput(data.remark);
        }
    }
}
