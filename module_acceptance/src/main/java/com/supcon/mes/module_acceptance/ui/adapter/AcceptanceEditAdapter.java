package com.supcon.mes.module_acceptance.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.listener.OnTextListener;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.ui.view.FlowLayout;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_acceptance.R;
import com.supcon.mes.module_acceptance.model.bean.AcceptanceEditEntity;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class AcceptanceEditAdapter extends BaseListDataRecyclerViewAdapter<AcceptanceEditEntity> {

    private float total;
    private boolean edit;

    public AcceptanceEditAdapter(Context context) {
        super(context);
    }


    @Override
    protected BaseRecyclerViewHolder<AcceptanceEditEntity> getViewHolder(int viewType) {
        if (viewType == 0){
            return new MultViewHolder(context);
        }else {
            return new ViewHolder(context);
        }

    }

    @Override
    public int getItemViewType(int position, AcceptanceEditEntity scoreEamPerformanceEntity) {
        if (scoreEamPerformanceEntity.valueType() == AcceptanceEditEntity.EDITMULT){
            return 0;
        }else {
            return 1;
        }
    }

    public void setEdit(boolean b) {
        edit = b;
    }


    class ViewHolder extends BaseRecyclerViewHolder<AcceptanceEditEntity> {

        @BindByTag("itemIndex")
        TextView itemIndex;
        @BindByTag("acceptanceItem")
        TextView acceptanceItem;

        @BindByTag("acceptanceRadioGroup")
        RadioGroup acceptanceRadioGroup;
        @BindByTag("acceptanceRadioBtn1")
        RadioButton acceptanceRadioBtn1;
        @BindByTag("acceptanceRadioBtn2")
        RadioButton acceptanceRadioBtn2;

        @BindByTag("acceptanceConclusion")
        CustomEditText acceptanceConclusion;

        @BindByTag("acceptanceCategory")
        FlowLayout acceptanceCategory;
        @BindByTag("acceptanceTotal")
        CustomTextView acceptanceTotal;


        public ViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_acceptance_edit_content;
        }

        @Override
        protected void initView() {
            super.initView();

        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            acceptanceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (acceptanceRadioBtn1.isPressed() || acceptanceRadioBtn2.isPressed()) {
                        AcceptanceEditEntity item = getItem(getLayoutPosition());
                        item.result = !item.result;
                        if (item.result) {
                            item.conclusion = item.isItemValue;
                        } else {
                            item.conclusion = item.noItemValue;
                        }
                    }
                }
            });
//            acceptanceConclusion.setTextListener(new OnTextListener() {
//                @Override
//                public void onText(String text) {
//                    AcceptanceEditEntity item = getItem(getAdapterPosition());
//                    if (!TextUtils.isEmpty(text) && text.equals(item.conclusion)) {
//                        return;
//                    }
//                    item.conclusion = Util.strFormat2(text);
//                }
//            });
            RxTextView.textChanges(acceptanceConclusion.editText())
                    .skipInitialValue()
                    .skip(2)
                    .subscribe(new Consumer<CharSequence>() {
                        @Override
                        public void accept(CharSequence charSequence) throws Exception {
                            AcceptanceEditEntity item = getItem(getAdapterPosition());
                            if (!TextUtils.isEmpty(charSequence) && charSequence.equals(item.conclusion)) {
                                return;
                            }
                            item.conclusion = Util.strFormat2(charSequence);
                        }
                    });
//            acceptanceConclusion.editText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                    //可以根据需求获取下一个焦点还是上一个
//                    View nextView = v.focusSearch(View.FOCUS_DOWN);
//                    if (nextView != null) {
//                        nextView.requestFocus(View.FOCUS_DOWN);
//                    }
//                    //这里一定要返回true
//                    return true;
//                }
//            });
        }

        @SuppressLint({"StringFormatMatches", "SetTextI18n"})
        @Override
        protected void update(AcceptanceEditEntity data) {
            itemIndex.setText(data.position + ".");
            acceptanceConclusion.setVisibility(View.VISIBLE);
            acceptanceRadioGroup.setVisibility(View.VISIBLE);
            acceptanceCategory.setVisibility(View.VISIBLE);

            acceptanceItem.setText(Util.strFormat2(data.item));

            if (data.valueType() == AcceptanceEditEntity.EDITBOL) {
                acceptanceConclusion.setVisibility(View.GONE);
                acceptanceCategory.setVisibility(View.GONE);
                acceptanceRadioBtn1.setText(data.isItemValue);
                acceptanceRadioBtn2.setText(data.noItemValue);
                acceptanceRadioBtn1.setChecked(data.result);
                acceptanceRadioBtn2.setChecked(!data.result);
            } else if (data.valueType() == AcceptanceEditEntity.EDITMULT){
                acceptanceConclusion.setVisibility(View.GONE);
                acceptanceRadioGroup.setVisibility(View.GONE);
                if (data.categorys.size() > 0) {
                    acceptanceCategory.removeAllViews();
                    acceptanceConclusion.setVisibility(View.GONE);
                    addview(context, acceptanceCategory, data.categorys);
                }
            }else {
                acceptanceRadioGroup.setVisibility(View.GONE);
                acceptanceCategory.setVisibility(View.GONE);

                acceptanceConclusion.setHint("请输入" + Util.strFormat2(data.item));
                acceptanceConclusion.setContent(Util.strFormat2(data.conclusion));
                if (data.valueType() == AcceptanceEditEntity.EDITNUM) {
                    acceptanceConclusion.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                } else if (data.valueType() == AcceptanceEditEntity.EDITTEXT) {
                    acceptanceConclusion.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        }

        //动态添加视图
        public void addview(Context context, FlowLayout layout, List<AcceptanceEditEntity> categorys) {
            int index = 0;
            for (AcceptanceEditEntity acceptanceEditEntity : categorys) {
                CustomEditText customEditText = new CustomEditText(context);
                customEditText.setKey(Util.strFormat2(acceptanceEditEntity.category) + ":");
                customEditText.setEditable(true);
                if (acceptanceEditEntity.valueType() == AcceptanceEditEntity.EDITNUM) {
                    customEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
                acceptanceTotal.setContent(Util.big(acceptanceEditEntity.total));
                customEditText.setContent(acceptanceEditEntity.conclusion);

                customEditText.setTextListener(new OnTextListener() {

                    @SuppressLint("CheckResult")
                    @Override
                    public void onText(String text) {
                        if (!TextUtils.isEmpty(text) && text.equals(acceptanceEditEntity.conclusion)) {
                            return;
                        }
                        total = 0;
                        acceptanceEditEntity.conclusion = Util.strFormat2(text);

                        Flowable.fromIterable(categorys)
                                .subscribe(new Consumer<AcceptanceEditEntity>() {
                                    @Override
                                    public void accept(AcceptanceEditEntity acceptanceEditEntity) throws Exception {
                                        total = total + Util.strToFloat(acceptanceEditEntity.conclusion);
                                    }
                                }, throwable -> {
                                }, new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        Flowable.fromIterable(categorys)
                                                .subscribe(acceptanceEditEntity1 -> acceptanceEditEntity1.total = total);
                                        acceptanceTotal.setContent(Util.big(total));
                                    }
                                });

                    }
                });
                setRaidBtnAttribute(context, customEditText, index);
                layout.addView(customEditText);
                index++;
            }
        }

        @SuppressLint("ResourceType")
        private void setRaidBtnAttribute(Context context, CustomEditText customEditText, int id) {
            if (null == customEditText) {
                return;
            }
            customEditText.setId(id);
            customEditText.setKeyWidth(Util.dpToPx(context, 50));
            customEditText.setKeyHeight(Util.dpToPx(context, 30));
            customEditText.textView().setVisibility(View.VISIBLE);
            ViewGroup.MarginLayoutParams rlp = new ViewGroup.MarginLayoutParams(Util.dpToPx(context, 160), Util.dpToPx(context, 30));
            customEditText.setLayoutParams(rlp);
        }

    }
    class MultViewHolder extends BaseRecyclerViewHolder<AcceptanceEditEntity> {

        @BindByTag("itemIndex")
        TextView itemIndex;
        @BindByTag("acceptanceItem")
        TextView acceptanceItem;
        @BindByTag("titleItemLl")
        LinearLayout titleItemLl;
        @BindByTag("chkBox")
        CheckBox chkBox;
        @BindByTag("itemDetails")
        TextView itemDetails;
        @BindByTag("itemDetailsLl")
        LinearLayout itemDetailsLl;


        public MultViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_acceptance_edit_mult;
        }

        @Override
        protected void initView() {
            super.initView();
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    getItem(getAdapterPosition()).result = isChecked;
                }
            });
        }

        @SuppressLint({"StringFormatMatches", "SetTextI18n"})
        @Override
        protected void update(AcceptanceEditEntity data) {
            if (data.viewType == ListType.TITLE.value()){
                titleItemLl.setVisibility(View.VISIBLE);
                itemDetailsLl.setVisibility(View.GONE);

                itemIndex.setText(data.position + ".");
                acceptanceItem.setText(data.item);

            }else {
                titleItemLl.setVisibility(View.GONE);
                itemDetailsLl.setVisibility(View.VISIBLE);

                itemDetails.setText(data.itemDetail);
                chkBox.setChecked(data.result);
            }
        }

    }
}
