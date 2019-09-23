package com.supcon.mes.module_main.ui.util;

import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_main.ui.view.MenuPopwindowBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MenuHelper {

    public static List<MenuPopwindowBean> getAewMenu() {
        List<MenuPopwindowBean> works = new ArrayList<>();
        JSONArray worksArray = null;
        try {
            worksArray = new JSONObject(Util.getJson(MBapApp.getAppContext(), "hswork.json")).getJSONArray("aew");
            works = GsonUtil.jsonToList(worksArray.toString(), MenuPopwindowBean.class);
            for (MenuPopwindowBean menuPopwindowBean : works) {
                switch (menuPopwindowBean.getType()) {
                    case Constant.HSWorkType.JHXJ:
                        menuPopwindowBean.setRouter(Constant.Router.JHXJ_LIST);
                        break;
                    case Constant.HSWorkType.LSXJ:
                        menuPopwindowBean.setRouter(Constant.Router.LSXJ_LIST);
                        break;
                    case Constant.HSWorkType.YHGL:
                        menuPopwindowBean.setRouter(Constant.Router.YH_LIST);
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return works;
    }

    public static List<MenuPopwindowBean> getLubricateMenu() {
        List<MenuPopwindowBean> works = new ArrayList<>();
        JSONArray worksArray = null;
        try {
            worksArray = new JSONObject(Util.getJson(MBapApp.getAppContext(), "hswork.json")).getJSONArray("lubricate");
            works = GsonUtil.jsonToList(worksArray.toString(), MenuPopwindowBean.class);
            for (MenuPopwindowBean menuPopwindowBean : works) {
                switch (menuPopwindowBean.getType()) {
                    case Constant.HSWorkType.PLAN_LUBRICATION_EARLY_WARN:
                        menuPopwindowBean.setRouter(Constant.Router.PLAN_LUBRICATION_EARLY_WARN);
                        break;
                    case Constant.HSWorkType.TEMPORARY_LUBRICATION_EARLY_WARN:
                        menuPopwindowBean.setRouter(Constant.Router.TEMPORARY_LUBRICATION_EARLY_WARN);
                        break;
                    case Constant.HSWorkType.LUBRICATION_EARLY_WARN:
                        menuPopwindowBean.setRouter(Constant.Router.LUBRICATION_EARLY_WARN);
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return works;
    }

    public static List<MenuPopwindowBean> getRepairMenu() {
        List<MenuPopwindowBean> works = new ArrayList<>();
        JSONArray worksArray = null;
        try {
            worksArray = new JSONObject(Util.getJson(MBapApp.getAppContext(), "hswork.json")).getJSONArray("repair");
            works = GsonUtil.jsonToList(worksArray.toString(), MenuPopwindowBean.class);
            for (MenuPopwindowBean menuPopwindowBean : works) {
                switch (menuPopwindowBean.getType()) {
                    case Constant.HSWorkType.DAILY_WXGD:
                        menuPopwindowBean.setRouter(Constant.Router.WXGD_LIST);
                        break;
                    case Constant.HSWorkType.REPAIR_WXGD:
                        menuPopwindowBean.setRouter(Constant.Router.WXGD_LIST);
                        break;
                    case Constant.HSWorkType.OHAUL_WXGD:
                        menuPopwindowBean.setRouter(Constant.Router.WXGD_LIST);
                        break;
                    case Constant.HSWorkType.SPARE_EARLY_WARN:
                        menuPopwindowBean.setRouter(Constant.Router.SPARE_EARLY_WARN);
                        break;
                    case Constant.HSWorkType.MAINTENANCE_EARLY_WARN:
                        menuPopwindowBean.setRouter(Constant.Router.MAINTENANCE_EARLY_WARN);
                        break;
                    case Constant.HSWorkType.SPARE_PART_RECEIVE:
                        menuPopwindowBean.setRouter(Constant.Router.SPARE_PART_RECEIVE);
                        break;
                    case Constant.HSWorkType.TD:
                        menuPopwindowBean.setRouter(Constant.Router.TD);
                        break;
                    case Constant.HSWorkType.SD:
                        menuPopwindowBean.setRouter(Constant.Router.SD);
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return works;
    }

    public static List<MenuPopwindowBean> getFormMenu() {
        List<MenuPopwindowBean> works = new ArrayList<>();
        JSONArray worksArray = null;
        try {
            worksArray = new JSONObject(Util.getJson(MBapApp.getAppContext(), "hswork.json")).getJSONArray("form");
            works = GsonUtil.jsonToList(worksArray.toString(), MenuPopwindowBean.class);
            for (MenuPopwindowBean menuPopwindowBean : works) {
                switch (menuPopwindowBean.getType()) {
                    case Constant.HSWorkType.XJ_STATISTICS:
                        menuPopwindowBean.setRouter(Constant.Router.XJ_STATISTICS);
                        break;
                    case Constant.HSWorkType.YH_STATISTICS:
                        menuPopwindowBean.setRouter(Constant.Router.YH_STATISTICS);
                        break;
                    case Constant.HSWorkType.WXGD_STATISTICS:
                        menuPopwindowBean.setRouter(Constant.Router.WXGD_STATISTICS);
                        break;
                    case Constant.HSWorkType.STOP_POLICE:
                        menuPopwindowBean.setRouter(Constant.Router.STOP_POLICE);
                        break;
                    case Constant.HSWorkType.SPARE_PART_CONSUME_LEDGER:
                        menuPopwindowBean.setRouter(Constant.Router.SPARE_PART_CONSUME_LEDGER);
                        break;
                    case Constant.HSWorkType.SPARE_PART_RECEIVE_RECORD:
                        menuPopwindowBean.setRouter(Constant.Router.SPARE_PART_RECEIVE_RECORD);
                        break;

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return works;
    }
}
