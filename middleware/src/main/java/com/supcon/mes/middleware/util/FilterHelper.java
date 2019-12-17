package com.supcon.mes.middleware.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.supcon.mes.mbap.beans.FilterBean;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.QueryBtnType;

import java.util.ArrayList;
import java.util.Arrays;
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

    //动态添加视图
    public static void addView(Activity activity, RadioGroup radiogroup, List<FilterBean> filterBeanList) {
        int index = 0;
        for (FilterBean filterBean : filterBeanList) {
            RadioButton button = new RadioButton(activity);
            if (index == 0) {
                button.setChecked(true);
            }
            setRaidBtnAttribute(activity, button, filterBean.name, filterBean.type);
            radiogroup.addView(button);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button
                    .getLayoutParams();
            layoutParams.setMargins(Util.dpToPx(activity, 5), 0, 0, 0);//4个参数按顺序分别是左上右下
            button.setLayoutParams(layoutParams);
            index++;
        }


    }


    @SuppressLint("ResourceType")
    private static void setRaidBtnAttribute(Activity activity, final RadioButton codeBtn, String btnContent, int id) {
        if (null == codeBtn) {
            return;
        }
        codeBtn.setBackgroundResource(R.drawable.sh_filter_gray);
        codeBtn.setTextColor(activity.getResources().getColorStateList(R.drawable.tvbg_tag_item));
        codeBtn.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
        codeBtn.setId(id);
        codeBtn.setText(btnContent);
        codeBtn.setTextSize(14);
        codeBtn.setGravity(Gravity.CENTER);
        codeBtn.setPadding(Util.dpToPx(activity, 10), Util.dpToPx(activity, 2), Util.dpToPx(activity, 10), Util.dpToPx(activity, 2));
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        codeBtn.setLayoutParams(rlp);
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
}
