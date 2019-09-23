package com.supcon.mes.module_olxj.util;

import com.supcon.mes.mbap.beans.FilterBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2018/1/2.
 * Email:wangshizhan@supcon.com
 */

public class FilterHelper {


    public static List<FilterBean> createDateFilter(){

        List<FilterBean> list = new ArrayList<>();

        FilterBean filterBean = new FilterBean();
        filterBean.name = "今天";
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = "昨天";
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = "明天";
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = "后三天";
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = "本周";
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = "本月";
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = "全部";
        list.add(filterBean);


        return list;
    }

    public static List<FilterBean> createTableStatusFilter(){

        List<FilterBean> list = new ArrayList<>();

        FilterBean filterBean = new FilterBean();
        filterBean.name = "编辑";
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = "审批";
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = "生效";
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = "全部";
        list.add(filterBean);

        return list;
    }

    public static List<FilterBean> createObjectFilter(List<String> names){

        List<FilterBean> list = new ArrayList<>();

        for(String name : names){
            FilterBean filterBean = new FilterBean();
            filterBean.name = name;
            list.add(filterBean);
        }


        return list;
    }

    /**
     * 创建巡检任务完成否状态值
     * @return
     */
    public static List<FilterBean> createXJPathStateFilter(){
        List<FilterBean> list = new ArrayList<>();
        FilterBean filterBean = null;

        filterBean = new FilterBean();
        filterBean.name = "待检";
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = "已检";
        list.add(filterBean);

        return list;
    }


}
