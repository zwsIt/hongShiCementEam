package com.supcon.mes.middleware.util;

import android.content.Context;
import android.text.TextUtils;

import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.WorkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2017/8/16.
 * Email:wangshizhan@supcon.com
 */

public class WorkHelper {

    public static List<WorkInfo> getDefaultWorkList(Context context) {

        List<WorkInfo> list = new ArrayList<>();

        List<WorkInfo> works = null;

        JSONArray worksArray = null;
        try {
            worksArray = new JSONObject(Util.getJson(MBapApp.getAppContext(), "menu.json")).getJSONArray("menu");
            works = GsonUtil.jsonToList(worksArray.toString(), WorkInfo.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (works == null) {
            String cache = SharedPreferencesUtils.getParam(context, Constant.SPKey.WORKS, "");

            if (TextUtils.isEmpty(cache)) {
                works = new ArrayList<>();
            } else {
                works = GsonUtil.jsonToList(cache, WorkInfo.class);
            }
        }

        for (int i = 0;  i < works.size();i++) {
            WorkInfo workInfo = works.get(i);
            workInfo.sort = works.indexOf(workInfo);
            workInfo.ip = EamApplication.getIpPort();
            workInfo.userId = EamApplication.getAccountInfo().userId;
            switch (workInfo.type) {
                case Constant.WorkType.GZFQ:
                    workInfo.defaultMenu = true;
                    workInfo.iconResId = R.drawable.ic_work_gzfq;
                    workInfo.router = Constant.Router.WORK_START_EDIT;
                    break;
                case Constant.WorkType.JHXJ:
                    workInfo.defaultMenu = true;
                    workInfo.iconResId = R.drawable.ic_work_jhxj;
                    workInfo.router = Constant.Router.JHXJ_LIST;
                    break;
                case Constant.WorkType.JHRH:
                    workInfo.iconResId = R.drawable.ic_work_daily_lubr_warn;
                    workInfo.router = Constant.Router.PLAN_LUBRICATION_WARN_TAB;
                    break;
                case Constant.WorkType.LSRH:
                    workInfo.iconResId = R.drawable.ic_work_rh;
                    workInfo.router = Constant.Router.TEMPORARY_LUBRICATION_EARLY_WARN;
                    break;
                case Constant.WorkType.RHYJ:
                    workInfo.iconResId = R.drawable.ic_work_lubr_warn;
                    workInfo.router = Constant.Router.LUBRICATION_EARLY_WARN;
                    break;
                case Constant.WorkType.YHGL:
                    workInfo.iconResId = R.drawable.ic_work_yhgl;
                    workInfo.router = Constant.Router.YH_LIST;
                    break;
                case Constant.WorkType.WXGD:
                    workInfo.defaultMenu = true;
                    workInfo.iconResId = R.drawable.ic_work_wxgd;
                    workInfo.router = Constant.Router.WXGD_LIST_NEW;
                    break;
                case Constant.WorkType.LBJYJ:
                    workInfo.iconResId = R.drawable.ic_work_spare_warn;
                    workInfo.router = Constant.Router.SPARE_EARLY_WARN;
                    break;
                case Constant.WorkType.YWYJ:
                    workInfo.iconResId = R.drawable.ic_work_mainten_warn;
                    workInfo.router = Constant.Router.MAINTENANCE_EARLY_WARN;
                    break;
                case Constant.WorkType.TDSQ:
                    workInfo.iconResId = R.drawable.ic_work_td;
                    workInfo.router = Constant.Router.HS_TD_LIST;
                    break;
                case Constant.WorkType.SDSQ:
                    workInfo.iconResId = R.drawable.ic_work_sd;
                    workInfo.router = Constant.Router.HS_SD_LIST;
                    break;
                case Constant.WorkType.JXZYP:
                    workInfo.defaultMenu = true;
                    workInfo.iconResId = R.drawable.ic_work_sbda;
                    workInfo.router = Constant.Router.OVERHAUL_WORKTICKET_LIST;
                    break;
                case Constant.WorkType.XJTJ:
                    workInfo.iconResId = R.drawable.ic_work_xjtj;
                    workInfo.router = Constant.Router.XJ_STATISTICS;
                    break;
                case Constant.WorkType.YHTJ:
                    workInfo.iconResId = R.drawable.ic_work_yxjl;
                    workInfo.router = Constant.Router.YH_STATISTICS;
                    break;
                case Constant.WorkType.GDTJ:
                    workInfo.iconResId = R.drawable.ic_work_gdtj;
                    workInfo.router = Constant.Router.WXGD_STATISTICS;
                    break;
                case Constant.WorkType.TJTJ:
                    workInfo.iconResId = R.drawable.ic_work_tjjl;
                    workInfo.router = Constant.Router.STOP_POLICE;
                    break;
                case Constant.WorkType.SPARE_PART_CONSUME_LEDGER:
                    workInfo.iconResId = R.drawable.ic_work_quality;
                    workInfo.router = Constant.Router.SPARE_PART_CONSUME_LEDGER;
                    break;
                case Constant.WorkType.SPARE_PART_RECEIVE_RECORD:
                    workInfo.iconResId = R.drawable.ic_work_lsys;
                    workInfo.router = Constant.Router.SPARE_PART_RECEIVE_RECORD;
                    break;
                case Constant.WorkType.TSDTJ:
                    workInfo.iconResId = R.drawable.ic_work_jxjh;
                    workInfo.router = Constant.Router.TSD_COMMON;
                    break;

                default:
                    break;
            }
//            if (workInfo.isOpen) {
                list.add(workInfo);
//            }
        }
        SharedPreferencesUtils.setParam(MBapApp.getAppContext(), Constant.SPKey.WORKS, works.toString());
        return list;

    }

}
