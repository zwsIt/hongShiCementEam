package com.supcon.mes.module_main.ui.util;

import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_main.ui.view.MenuPopwindowBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bluetron.coresdk.model.bean.response.OwnMinAppItem;


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
                    case Constant.HSWorkType.REPAIR_WXGD:
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
                        if (EamApplication.isHongshi()) {
                            menuPopwindowBean.setRouter(Constant.Router.SPARE_PART_RECEIVE);
                        } else {
                            menuPopwindowBean.setPower(true);
                            menuPopwindowBean.setRouter(Constant.Router.SPARE_PART_APPLY_EDIT);
                        }
                        break;
                    case Constant.HSWorkType.TD:
                        if (EamApplication.isHongshi()) { // 红狮跳转
                            menuPopwindowBean.setRouter(Constant.Router.HS_TD_LIST);
                            menuPopwindowBean.setType(Constant.HSWorkType.HS_TD);
                        }else {
                            //移动视图
                            menuPopwindowBean.setRouter(Constant.Router.TD);
                        }
                        break;
                    case Constant.HSWorkType.SD:
                        menuPopwindowBean.setRouter(Constant.Router.SD);
                        break;
                    case Constant.HSWorkType.TSD_CANCEL:
                    case Constant.HSWorkType.TSD_APPROVAL:
                        menuPopwindowBean.setRouter(Constant.Router.TSD_COMMON);
                        break;
                    case Constant.HSWorkType.HS_JX_TICKETS:
                        menuPopwindowBean.setRouter(Constant.Router.OVERHAUL_WORKTICKET_LIST);
                        break;
                    default:
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
                    case Constant.HSWorkType.TSD_STATISTICS:
                        menuPopwindowBean.setRouter(Constant.Router.TSD_COMMON);
                        break;
                    default:
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return works;
    }


    public static List<MenuPopwindowBean> getZZMenu(List<OwnMinAppItem> zzWorks) {
        List<MenuPopwindowBean> works = new ArrayList<>();
        for (OwnMinAppItem ownMinAppItem : zzWorks) {
//          注：  ownMinAppItem.getCreateType() = 1：自建(apptype=1 公众号打开；apptype=2 小程序打开); = 2：supos(apptype=1 公众号打开); = 3 第三方小程序：(apptype=2 小程序打开)。

            if (ownMinAppItem.getCreateType() == 2) continue;

            MenuPopwindowBean menuPopwindowBean = new MenuPopwindowBean();
            menuPopwindowBean.setType(Constant.HSWorkType.ZZ);
            menuPopwindowBean.setPower(true);
            menuPopwindowBean.setName(ownMinAppItem.getAppname());
            menuPopwindowBean.setRouter(ownMinAppItem.getAppId());  // 保证不为空，可跳转
            menuPopwindowBean.setAppItem(ownMinAppItem);
            works.add(menuPopwindowBean);
        }
        return works;
    }
}
