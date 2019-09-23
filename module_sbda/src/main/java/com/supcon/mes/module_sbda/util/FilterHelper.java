package com.supcon.mes.module_sbda.util;

import android.annotation.SuppressLint;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.beans.FilterBean;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.middleware.util.*;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;

/**
 * Created by wangshizhan on 2018/1/2.
 * Email:wangshizhan@supcon.com
 */

public class FilterHelper {


    public static List<FilterBean> createDateFilter() {

        List<FilterBean> list = new ArrayList<>();

        FilterBean filterBean = new FilterBean();
        filterBean.name = "今天";
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = "三天内";
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = "本周";
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = "本月";
        list.add(filterBean);

        filterBean = new FilterBean();
        filterBean.name = "时间不限";
        filterBean.type = CustomFilterView.VIEW_TYPE_ALL;
        list.add(filterBean);


        return list;
    }

    public static List<FilterBean> createTableStatusFilter() {

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
        filterBean.name = "状态不限";
        filterBean.type = CustomFilterView.VIEW_TYPE_ALL;
        list.add(filterBean);

        return list;
    }

    public static List<FilterBean> createObjectFilter(List<String> names) {

        List<FilterBean> list = new ArrayList<>();

        for (String name : names) {
            FilterBean filterBean = new FilterBean();
            filterBean.name = name;
            list.add(filterBean);
        }
        return list;
    }

    @SuppressLint("CheckResult")
    public static List<FilterBean> createDeviceTypeFilter() {
        final List<FilterBean> list = new ArrayList<>();
        FilterBean all = new FilterBean();
        all.name = "类型不限";
        all.type = CustomFilterView.VIEW_TYPE_ALL;
        list.add(all);
        Flowable.fromIterable(DeviceManager.getInstance().getAllDeviceTypes())
                .subscribe(type -> {
                    FilterBean filterBean = new FilterBean();
                    filterBean.name = type;
                    list.add(filterBean);
                }, throwable -> {
                }, () -> {
                });
        LogUtil.e(list.toString());
        return list;
    }

    public static List<FilterBean> createDeviceAreaFilter() {
        final List<FilterBean> list = new ArrayList<>();
        FilterBean all = new FilterBean();
        all.name = "区域不限";
        all.type = CustomFilterView.VIEW_TYPE_ALL;
        list.add(all);
        LogUtil.e(DeviceManager.getInstance().getAllDeviceTypes().toString());
        Flowable.fromIterable(DeviceManager.getInstance().getAllDeviceAreas())
                .subscribe(type -> {
                    FilterBean filterBean = new FilterBean();
                    filterBean.name = type;
                    list.add(filterBean);
                }, throwable -> {
                }, () -> {
                });
        LogUtil.e(list.toString());
        return list;
    }

    public static List<FilterBean> createDeviceStatusFilter() {
        final List<FilterBean> list = new ArrayList<>();
        FilterBean all = new FilterBean();
        all.name = "状态不限";
        all.type = CustomFilterView.VIEW_TYPE_ALL;
        list.add(all);
        LogUtil.e(DeviceManager.getInstance().getAllDeviceTypes().toString());
        Flowable.fromIterable(DeviceManager.getInstance().getAllDeviceStatuses())
                .subscribe(type -> {
                    FilterBean filterBean = new FilterBean();
                    filterBean.name = EAMStatusHelper.getType(type);
                    list.add(filterBean);
                }, throwable -> {
                }, () -> {
                });
        LogUtil.e(list.toString());
        return list;
    }

    /**
     *@author zhangwenshuai1
     *@date 2018/4/17
     *@description 创建巡检任务完成否状态值
     *
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

    /**
     *@author zhangwenshuai1
     *@date 2018/4/17
     *@description 巡检时间过滤
     *
     */
    public static List<FilterBean> xjCreateDateFilter(){

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


}
