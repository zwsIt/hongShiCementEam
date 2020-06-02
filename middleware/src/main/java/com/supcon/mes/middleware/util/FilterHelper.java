package com.supcon.mes.middleware.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.supcon.common.view.util.DisplayUtil;
import com.supcon.mes.mbap.beans.FilterBean;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.DateBtn;
import com.supcon.mes.middleware.constant.QueryBtnType;
import com.supcon.mes.middleware.model.bean.CustomFilterBean;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.contract.AttachmentContract;
import com.supcon.mes.middleware.ui.view.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangfei.cao
 * @ClassName depot
 * @date 2018/7/6
 * ------------- Description -------------
 */
public class FilterHelper {

    public static List<FilterBean> createDateFilter() {

        List<FilterBean> list = new ArrayList<>();

        FilterBean filterBean = new FilterBean();
        filterBean.name = "今天";
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = "昨天";
        list.add(filterBean);

//        filterBean = new FilterBean();
//        filterBean.name = "明天";
//        list.add(filterBean);
//
//        filterBean = new FilterBean();
//        filterBean.name = "后三天";
//        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = "本周";
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = "本月";
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = "时间不限";
        list.add(filterBean);

        return list;
    }

    public static List<FilterBean> createMonthDateFilter() {

        List<FilterBean> list = new ArrayList<>();

        FilterBean filterBean = new FilterBean();
        filterBean.name = DateBtn.ONE_MONTH.getValue();
        filterBean.type = DateBtn.ONE_MONTH.getType();
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = DateBtn.THREE_MONTH.getValue();
        filterBean.type = DateBtn.THREE_MONTH.getType();
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = DateBtn.SIX_MONTH.getValue();
        filterBean.type = DateBtn.SIX_MONTH.getType();
        list.add(filterBean);

        return list;
    }

    //动态添加视图
    public static void addView(Activity activity, RadioGroup radiogroup, List<FilterBean> filterBeanList) {
        int index = 0;
        for (FilterBean filterBean : filterBeanList) {
            RadioButton button = new RadioButton(activity);
            if (index == 0) {
                button.setChecked(true);
            }
            setRaidBtnAttribute(activity, button, filterBean.name, filterBean.type,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            button.setBackgroundResource(R.drawable.sl_radio_check_list_query); // 列表样式
            radiogroup.addView(button);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button
                    .getLayoutParams();
            layoutParams.setMargins(0, 0, Util.dpToPx(activity, 10), 0);//4个参数按顺序分别是左上右下
            button.setLayoutParams(layoutParams);
            index++;
        }

    }

    public static void addRadioView(Activity activity, RadioGroup radiogroup, List<CustomFilterBean> filterBeanList,int radioWidth,int radioHeight) {
//        int index = 0;
        for (CustomFilterBean filterBean : filterBeanList) {
            RadioButton button = new RadioButton(activity);
            button.setChecked(filterBean.check);
            setRaidBtnAttribute(activity, button, filterBean.name, filterBean.type,new LinearLayout.LayoutParams(DisplayUtil.dip2px(radioWidth,activity), DisplayUtil.dip2px(radioHeight,activity)));
            radiogroup.addView(button);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button
                    .getLayoutParams();
            layoutParams.setMargins(0, 0, Util.dpToPx(activity, 10), 0);//4个参数按顺序分别是左上右下
            button.setLayoutParams(layoutParams);
//            index++;
        }

    }

    public static void addCheckBoxView(Activity activity, FlowLayout flowLayout, List<CustomFilterBean> filterBeanList, int chBoxWidth, int chBoxHeight) {
//        int index = 0;
        for (CustomFilterBean filterBean : filterBeanList) {
            CheckBox checkBox = new CheckBox(activity);
            checkBox.setChecked(filterBean.check);
            setViewAttribute(activity, checkBox, filterBean.name, filterBean.type,new LinearLayout.LayoutParams(DisplayUtil.dip2px(chBoxWidth,activity), DisplayUtil.dip2px(chBoxHeight,activity)));
            flowLayout.addView(checkBox);
            FlowLayout.LayoutParams layoutParams = (FlowLayout.LayoutParams) checkBox.getLayoutParams();
            layoutParams.setMargins(0, Util.dpToPx(activity, 5), Util.dpToPx(activity, 10), Util.dpToPx(activity, 5));//4个参数按顺序分别是左上右下
            checkBox.setLayoutParams(layoutParams);
//            index++;
        }

    }


    @SuppressLint("ResourceType")
    private static void setRaidBtnAttribute(Activity activity, final RadioButton codeBtn, String btnContent, int id, ViewGroup.LayoutParams layoutParams) {
        if (null == codeBtn) {
            return;
        }
//        codeBtn.setBackgroundResource(R.drawable.sh_filter_gray);
        codeBtn.setBackgroundResource(R.drawable.sl_radio_check);
//        codeBtn.setBackground(activity.getResources().getDrawable(R.drawable.sl_radio_check));
        codeBtn.setTextColor(activity.getResources().getColorStateList(R.drawable.tvbg_tag_item));
        codeBtn.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
//        codeBtn.setButtonDrawable(R.drawable.sl_radio_check);
        codeBtn.setId(id);
        codeBtn.setText(btnContent);
        codeBtn.setTextSize(14);
        codeBtn.setGravity(Gravity.CENTER);
        codeBtn.setPadding(Util.dpToPx(activity, 10), Util.dpToPx(activity, 2), Util.dpToPx(activity, 10), Util.dpToPx(activity, 2));
//        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        codeBtn.setLayoutParams(layoutParams);
    }

    @SuppressLint("ResourceType")
    private static void setViewAttribute(Activity activity, final View view, String content, int id, ViewGroup.LayoutParams layoutParams) {
        if (null == view) {
            return;
        }
        if (view instanceof CheckBox){
            CheckBox checkBox = (CheckBox) view;
            checkBox.setBackgroundResource(R.drawable.sl_radio_check);
//        codeBtn.setBackground(activity.getResources().getDrawable(R.drawable.sl_radio_check));
            checkBox.setTextColor(activity.getResources().getColorStateList(R.drawable.tvbg_tag_item));
            checkBox.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
//        codeBtn.setButtonDrawable(R.drawable.sl_radio_check);
            checkBox.setId(id);
            checkBox.setText(content);
            checkBox.setTextSize(14);
            checkBox.setGravity(Gravity.CENTER);
            checkBox.setPadding(Util.dpToPx(activity, 10), Util.dpToPx(activity, 2), Util.dpToPx(activity, 10), Util.dpToPx(activity, 2));
//        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            checkBox.setLayoutParams(layoutParams);
        }

    }

    /**
     * @description 列表单据按钮：仅查待办、查询
     * @param
     * @return
     * @author zhangwenshuai1 2019/12/12
     *
     */
    public static List<FilterBean> queryBtn(){
        List<FilterBean> filterBeanList = new ArrayList<>();
        FilterBean filterBean = new FilterBean();
        filterBean.name = QueryBtnType.PENDING_QUERY.getValue();
        filterBean.type = QueryBtnType.PENDING_QUERY.getType();
        filterBeanList.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = QueryBtnType.ALL_QUERY.getValue();
        filterBean.type = QueryBtnType.ALL_QUERY.getType();
        filterBeanList.add(filterBean);

        return filterBeanList;
    }

    public static List<CustomFilterBean> createFilterBySystemCode(List<SystemCodeEntity> systemCodeEntityList,String checkId,boolean multi) {

        List<CustomFilterBean> list = new ArrayList<>();
//        List<SystemCodeEntity> systemCodeEntityList = SystemCodeManager.getInstance().getSystemCodeListByCode(systemCode);

        CustomFilterBean filterBean;
        for (int i=0;i<systemCodeEntityList.size();i++){
            filterBean = new CustomFilterBean();
            filterBean.name = systemCodeEntityList.get(i).value;
            filterBean.id = systemCodeEntityList.get(i).id;
            filterBean.type = 1000 + i;
            if (multi){
                if (!TextUtils.isEmpty(checkId) && checkId.contains(filterBean.id)){
                    filterBean.check = true;
                }
            }else {
                if (filterBean.id.equals(checkId)){
                    filterBean.check = true;
                }
            }

            list.add(filterBean);
        }
        return list;
    }

}
