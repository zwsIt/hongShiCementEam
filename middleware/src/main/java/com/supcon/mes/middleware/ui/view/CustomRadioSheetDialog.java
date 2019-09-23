package com.supcon.mes.middleware.ui.view;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.mes.mbap.beans.SheetEntity;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StateListHelper;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.middleware.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2018/5/16.
 * Email:wangshizhan@supcon.com
 */

public class CustomRadioSheetDialog {

    private BottomSheetDialog mDialog;
    private CustomDialog.OnHideListener mFinishListener;
    private Context mContext;
    private OnItemChildViewClickListener mOnItemChildViewClickListener;

    public CustomRadioSheetDialog(Context context) {
        this(context, 0);
    }


    public CustomRadioSheetDialog(Context context, int themeResId) {
        mContext = context;
        if (themeResId == 0)
            mDialog = new BottomSheetDialog(context);
        else
            mDialog = new BottomSheetDialog(context, themeResId);

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
    }

    public CustomRadioSheetDialog setOnItemChildViewClickListener(OnItemChildViewClickListener onItemChildViewClickListener) {
        mOnItemChildViewClickListener = onItemChildViewClickListener;
        return this;
    }

    public <T extends SheetEntity> CustomRadioSheetDialog multiSheet(String title, List<T> list) {
        return multiSheet(title, list, null);
    }

    public <T extends SheetEntity> CustomRadioSheetDialog multiSheet(String title, List<T> list, List<Boolean> checkedList) {

        Context context = mContext;

        View root = LayoutInflater.from(mContext).inflate(R.layout.ly_custom_radio_sheet_dialog, null);
        mDialog.setContentView(root);

        if (!TextUtils.isEmpty(title)) {
            TextView textView = root.findViewById(R.id.msgText);
            textView.setText(title);
            textView.setVisibility(View.VISIBLE);
        }

        List<RadioButton> radioButtons = new ArrayList<>();

        RadioGroup layout = root.findViewById(R.id.sheetContainer);
        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, DisplayUtil.dip2px(50, context));
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.topMargin = DisplayUtil.dip2px(1, context);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(1, context));

        layout.setBackgroundResource(R.color.white);
        for (int i = 0; i < list.size(); i++) {
            final SheetEntity item = list.get(i);
            RadioButton radioButton = new RadioButton(context);
            radioButton.setId(i);
            radioButton.setText(item.name);
            radioButton.setButtonDrawable(R.drawable.sl_checkbox_selector_small);
            radioButton.setChecked(null == checkedList ? false : checkedList.get(i));
            radioButton.setEnabled(true);
            radioButton.setGravity(Gravity.CENTER);
            radioButtons.add(radioButton);
            layout.addView(radioButton, lp);
            View line = LayoutInflater.from(context).inflate(R.layout.ly_line_light, null);
            layout.addView(line, lp2);
        }

        Button cancelBtn = root.findViewById(R.id.cancelBtn);
        cancelBtn.setText("确定");
        cancelBtn.setOnClickListener(v -> {
            List<SheetEntity> checkItems = new ArrayList<>();
            for (int i = 0; i < radioButtons.size(); i++) {

                RadioButton radioButton = radioButtons.get(i);
                if (radioButton.isChecked()) {
                    checkItems.add(list.get(i));
                }
            }
            if (mOnItemChildViewClickListener != null)
                mOnItemChildViewClickListener.onItemChildViewClick(cancelBtn, -1, 0, GsonUtil.gsonString(checkItems));

            dismiss();
        });

        return this;
    }

    public <T extends SheetEntity> CustomRadioSheetDialog sheet(String title, List<T> list) {

        Context context = mContext;

        View root = LayoutInflater.from(mContext).inflate(R.layout.ly_custom_sheet_dialog, null);
        mDialog.setContentView(root);

        if (!TextUtils.isEmpty(title)) {
            TextView textView = root.findViewById(R.id.msgText);
            textView.setText(title);
            textView.setVisibility(View.VISIBLE);
        }

//        RecyclerView recyclerView = root.findViewById(R.id.contentView);
//
//        BaseListDataRecyclerViewAdapter adapter = new SheetAdapter(mContext);
//
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        int spacingInPixels = DisplayUtil.dip2px(1, context);
//        recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
//        adapter.addList(list);
//        adapter.notifyDataSetChanged();
        LinearLayout layout = root.findViewById(R.id.sheetContainer);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(50, context));
        lp.gravity = Gravity.CENTER;
        lp.topMargin = DisplayUtil.dip2px(1, context);

        for (SheetEntity item : list) {
            TextView textView = new TextView(context);
            textView.setTextColor(context.getResources().getColor(R.color.textColorlightblack));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textView.setText(item.name);
            textView.setGravity(Gravity.CENTER);
            layout.addView(textView, lp);
            textView.setBackground(StateListHelper.newSelector(context, R.drawable.sh_white, R.drawable.sh_white_p, -1, -1));
            textView.setOnClickListener(v -> {
                if (mOnItemChildViewClickListener != null)
                    mOnItemChildViewClickListener.onItemChildViewClick(textView, layout.indexOfChild(textView), 0, item);

                dismiss();
            });
        }

        root.findViewById(R.id.cancelBtn).setOnClickListener(v -> dismiss());

        return this;
    }

    public <T> CustomRadioSheetDialog list(String title, List<T> list, BaseListDataRecyclerViewAdapter adapter) {
        Context context = mContext;
        View root = LayoutInflater.from(mContext).inflate(R.layout.ly_custom_list_dialog, null);

        if (!TextUtils.isEmpty(title)) {
            TextView textView = root.findViewById(R.id.msgText);
            textView.setText(title);
            textView.setVisibility(View.VISIBLE);
        }

        root.setBackgroundColor(mContext.getResources().getColor(R.color.bgGray));
        mDialog.setContentView(root);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        int spacingInPixels = DisplayUtil.dip2px(1, context);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        if (mOnItemChildViewClickListener != null) {
            adapter.setOnItemChildViewClickListener(mOnItemChildViewClickListener);
        }

        adapter.addList(list);
        adapter.notifyDataSetChanged();


        return this;
    }


    public void hide() {

        if (mDialog != null) {
            mDialog.hide();
        }
    }

    public void dismiss() {

        if (mDialog != null) {
            mDialog.dismiss();
        }

    }

    public void show() {

        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

}
