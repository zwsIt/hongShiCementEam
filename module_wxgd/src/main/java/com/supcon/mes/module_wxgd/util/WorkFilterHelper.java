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

public class WorkFilterHelper {

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
    public static List<FilterBean> createWorkflowList() {
        List<FilterBean> list = new ArrayList<>();

        FilterBean filterBean;

        filterBean = new FilterBean();
        filterBean.name = "不限";
        filterBean.type = 1099;
        list.add(filterBean);
        filterBean = new FilterBean();
        filterBean.name = Constant.WxgdStatus_CH.DISPATCH;
        filterBean.type = 1000;
        list.add(filterBean);
        filterBean = new FilterBean();
        filterBean.name = Constant.WxgdStatus_CH.IMPLEMENT;
        filterBean.type = 1001;
        list.add(filterBean);
        filterBean = new FilterBean();
        filterBean.name = Constant.WxgdStatus_CH.ACCEPTANCE;
        filterBean.type = 1002;
        list.add(filterBean);
        filterBean = new FilterBean();
        filterBean.name = Constant.WxgdStatus_CH.COMPLETE;
        filterBean.type = 1003;
        list.add(filterBean);
        return list;
    }

}
