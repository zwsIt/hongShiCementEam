package com.supcon.mes.middleware.util;

import com.supcon.mes.mbap.beans.FilterBean;

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
}
