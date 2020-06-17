package com.supcon.mes.module_main.ui.util;

import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CustomFilterBean;

import java.util.ArrayList;
import java.util.List;

public class FilterHelper {

    /**
     * @param
     * @return
     * @description 单据类型
     * @author zhangwenshuai1 2018/8/14
     */
    public static List<CustomFilterBean> createTableTypeList() {
        List<CustomFilterBean> filterBeanList = new ArrayList<>();
        CustomFilterBean filterBean;
        filterBean = new CustomFilterBean();
        filterBean.name = "全部单据";
        filterBean.id = "";
        filterBean.type = CustomFilterView.VIEW_TYPE_ALL;
        filterBeanList.add(filterBean);

        filterBean = new CustomFilterBean();
        filterBean.name = "隐患单";
        filterBean.id = Constant.ProcessKey.FAULT_INFO;
        filterBeanList.add(filterBean);

        filterBean = new CustomFilterBean();
        filterBean.name = "维修工单";
        filterBean.id = Constant.ProcessKey.WORK;
        filterBeanList.add(filterBean);

        filterBean = new CustomFilterBean();
        filterBean.name = "检修票";
        filterBean.id = Constant.ProcessKey.WORK_TICKET;
        filterBeanList.add(filterBean);

        filterBean = new CustomFilterBean();
        filterBean.name = "停电票";
        filterBean.id = Constant.ProcessKey.ELE_OFF;
        filterBeanList.add(filterBean);

        filterBean = new CustomFilterBean();
        filterBean.name = "送电票";
        filterBean.id = Constant.ProcessKey.ELE_ON;
        filterBeanList.add(filterBean);

        filterBean = new CustomFilterBean();
        filterBean.name = "巡检";
        filterBean.id = Constant.ProcessKey.POTROL_TASK_WF;
        filterBeanList.add(filterBean);

        return filterBeanList;
    }

}
