package com.supcon.mes.module_wxgd.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.supcon.mes.mbap.beans.FilterBean;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wxgd.R;

import java.util.ArrayList;
import java.util.List;

public class FilterHelper {

    /**
     * @param
     * @return
     * @description 工单来源过滤条件
     * @author zhangwenshuai1 2018/8/14
     */
    public static List<FilterBean> createWorkSourceList() {
        List<FilterBean> list = new ArrayList<>();

        FilterBean filterBean;

        filterBean = new FilterBean();
        filterBean.name = "来源不限";
        list.add(filterBean);
        filterBean = new FilterBean();
        filterBean.name = Constant.WorkSource_CN.patrolcheck;
        list.add(filterBean);
        filterBean = new FilterBean();
        filterBean.name = Constant.WorkSource_CN.lubrication;
        list.add(filterBean);
        filterBean = new FilterBean();
        filterBean.name = Constant.WorkSource_CN.maintenance;
        list.add(filterBean);
        filterBean = new FilterBean();
        filterBean.name = Constant.WorkSource_CN.sparepart;
        list.add(filterBean);
        filterBean = new FilterBean();
        filterBean.name = Constant.WorkSource_CN.other;
        list.add(filterBean);
        return list;
    }

    //维修类型
    public static List<FilterBean> createRepairTypeList() {
        List<SystemCodeEntity> list = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.YH_WX_TYPE);
        List<FilterBean> filterBeanList = new ArrayList<>();
        FilterBean filterBean;
        filterBean = new FilterBean();
        filterBean.name = "类型不限";
        filterBean.type = CustomFilterView.VIEW_TYPE_ALL;
        filterBeanList.add(filterBean);
        for (SystemCodeEntity entity : list) {
            filterBean = new FilterBean();
            filterBean.name = entity.value;
            filterBeanList.add(filterBean);
        }
        return filterBeanList;
    }

    //优先级
    public static List<FilterBean> createPriorityList() {
        List<SystemCodeEntity> list = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.YH_PRIORITY);
        List<FilterBean> filterBeanList = new ArrayList<>();
        FilterBean filterBean;
        filterBean = new FilterBean();
        filterBean.name = "级别不限";
        filterBean.type = CustomFilterView.VIEW_TYPE_ALL;
        filterBeanList.add(filterBean);
        for (SystemCodeEntity entity : list) {
            filterBean = new FilterBean();
            filterBean.name = entity.value;
            filterBeanList.add(filterBean);
        }
        return filterBeanList;
    }

    //工作流状态
    public static List<String> createWorkflowList() {
        List<String> list = new ArrayList<>();
        list.add("不限");
        list.add(Constant.WxgdStatus_CH.DISPATCH);
//        list.add(Constant.WxgdStatus_CH.CONFIRM);
        list.add(Constant.WxgdStatus_CH.IMPLEMENT);
        list.add(Constant.WxgdStatus_CH.ACCEPTANCE);
        list.add(Constant.WxgdStatus_CH.COMPLETE);
        return list;
    }

    //动态添加视图
    public static void addview(Activity activity, RadioGroup radiogroup, List<String> str, int id) {
        int index = 0;
        for (String ss : str) {
            RadioButton button = new RadioButton(activity);
            if (index == 0) {
                button.setChecked(true);
            }
            setRaidBtnAttribute(activity, button, ss, Integer.parseInt(String.valueOf(id) + index));
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

}
