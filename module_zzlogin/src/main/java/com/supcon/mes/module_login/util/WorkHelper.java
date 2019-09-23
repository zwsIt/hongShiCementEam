package com.supcon.mes.module_login.util;

import android.content.Context;
import android.text.TextUtils;

import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_login.R;
import com.supcon.mes.module_login.model.bean.WorkInfo;

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
            worksArray = new JSONObject(Util.getJson(MBapApp.getAppContext(), "work.json")).getJSONArray("work");
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

        for (WorkInfo workInfo : works) {

            switch (workInfo.type) {


                case Constant.WorkType.XJGL:
                    workInfo.iconResId = R.drawable.ic_work_jhxj;
                    workInfo.router = Constant.Router.XJGL_LIST;
                    break;
                case Constant.WorkType.JHXJ:
                    workInfo.iconResId = R.drawable.ic_work_jhxj;
                    workInfo.router = Constant.Router.JHXJ_LIST;
                    break;
                case Constant.WorkType.LSXJ:
                    workInfo.iconResId = R.drawable.ic_work_lsxj;
                    workInfo.router = Constant.Router.LSXJ_LIST;
                    break;
                case Constant.WorkType.BJSQ:
                    workInfo.iconResId = R.drawable.ic_work_bjsq2;
                    workInfo.router = Constant.Router.BJSQ_LIST;
                    break;
                case Constant.WorkType.YXJL:
                    workInfo.iconResId = R.drawable.ic_work_yxjl;
                    workInfo.router = Constant.Router.YXJL_LIST;
                    break;
//                    case Constant.WorkType.QXGL:
//                        object.put("iconResId", R.drawable.ic_work_qxgl_small);
//                        object.put("router", Constant.Router.QXGL_LIST);
//                        break;
                case Constant.WorkType.YHGL:
                    workInfo.iconResId = R.drawable.ic_work_yhgl;
                    workInfo.router = Constant.Router.YH_LIST;
                    break;
                case Constant.WorkType.WXGD:
                    workInfo.iconResId = R.drawable.ic_work_wxgd;
                    workInfo.router = Constant.Router.WXGD_LIST;
                    break;
                case Constant.WorkType.SJSC:
                    workInfo.iconResId = R.drawable.ic_work_sjsc;
                    workInfo.router = Constant.Router.SJSC;
                    break;
                case Constant.WorkType.SJXZ:
                    workInfo.iconResId = R.drawable.ic_work_sjxz;
                    workInfo.router = Constant.Router.SJXZ;
                    break;
                case Constant.WorkType.RH:
                    workInfo.iconResId = R.drawable.ic_work_rh;
                    workInfo.router = Constant.Router.RH;
                    break;
                case Constant.WorkType.BY:
                    workInfo.iconResId = R.drawable.ic_work_by;
                    workInfo.router = Constant.Router.BY;
                    break;
                case Constant.WorkType.LXYH:
                    workInfo.iconResId = R.drawable.ic_work_lxyh;
                    workInfo.router = Constant.Router.OFFLINE_YH_LIST;
                    break;
                case Constant.WorkType.TD:
                    workInfo.iconResId = R.drawable.ic_work_td;
                    workInfo.router = Constant.Router.TD;
                    break;
                case Constant.WorkType.SD:
                    workInfo.iconResId = R.drawable.ic_work_sd;
                    workInfo.router = Constant.Router.SD;
                    break;

                case Constant.WorkType.SBDA:

                    workInfo.iconResId = R.drawable.ic_data_sbda;
                    workInfo.router = Constant.Router.SBDA_LIST;
                    break;
                case Constant.WorkType.SBDA_ONLINE:

                    workInfo.iconResId = R.drawable.ic_data_sbda;
                    workInfo.router = Constant.Router.SBDA_ONLINE_LIST;
                    break;
                case Constant.WorkType.STOP_POLICE:

                    workInfo.iconResId = R.drawable.ic_work_tjjl;
                    workInfo.router = Constant.Router.STOP_POLICE;
                    break;
                case Constant.WorkType.SPARE_EARLY_WARN:

                    workInfo.iconResId = R.drawable.ic_work_spare_warn;
                    workInfo.router = Constant.Router.SPARE_EARLY_WARN;
                    break;
                case Constant.WorkType.LUBRICATION_EARLY_WARN:

                    workInfo.iconResId = R.drawable.ic_work_lubr_warn;
                    workInfo.router = Constant.Router.LUBRICATION_EARLY_WARN;
                    break;
                case Constant.WorkType.DAILY_LUBRICATION_EARLY_WARN:
                    workInfo.iconResId = R.drawable.ic_work_daily_lubr_warn;
                    workInfo.router = Constant.Router.DAILY_LUBRICATION_EARLY_WARN;
                    break;
                case Constant.WorkType.MAINTENANCE_EARLY_WARN:

                    workInfo.iconResId = R.drawable.ic_work_mainten_warn;
                    workInfo.router = Constant.Router.MAINTENANCE_EARLY_WARN;
                    break;
                case Constant.WorkType.SCORE_EAM_LIST:

                    workInfo.iconResId = R.drawable.ic_work_pf;
                    workInfo.router = Constant.Router.SCORE_EAM_LIST;
                    break;
                case Constant.WorkType.ACCEPTANCE_LIST:

                    workInfo.iconResId = R.drawable.ic_work_lsys;
                    workInfo.router = Constant.Router.ACCEPTANCE_LIST;
                    break;
                case Constant.WorkType.SCORE_INSPECTOR_PERSON_LIST:

                    workInfo.iconResId = R.drawable.ic_work_xj;
                    workInfo.router = Constant.Router.SCORE_INSPECTOR_STAFF_LIST;
                    break;
                case Constant.WorkType.SCORE_MECHANIC_PERSON_LIST:

                    workInfo.iconResId = R.drawable.ic_work_jx;
                    workInfo.router = Constant.Router.SCORE_MECHANIC_STAFF_LIST;
                    break;
                case Constant.WorkType.JXJH:

                    workInfo.iconResId = R.drawable.ic_work_jxjh;
//                    workInfo.router = Constant.Router.STOP_POLICE;
                    break;
                case Constant.WorkType.DXJH:

                    workInfo.iconResId = R.drawable.ic_work_dxjh;
//                    workInfo.router = Constant.Router.STOP_POLICE;
                    break;
                case Constant.WorkType.XJLX:

                    workInfo.iconResId = R.drawable.ic_data_xjlx;
                    workInfo.router = Constant.Router.XJLX_LIST;
                    break;
                case Constant.WorkType.XJQY:

                    workInfo.iconResId = R.drawable.ic_data_xjqy;
                    workInfo.router = Constant.Router.XJQY_LIST;
                    break;
                case Constant.WorkType.XJBB:

                    workInfo.iconResId = R.drawable.ic_data_xjbb;
                    workInfo.router = Constant.Router.XJBB;
                    break;

                case Constant.WorkType.TSD_RECORD:

                    workInfo.iconResId = R.drawable.ic_data_sbda;
                    workInfo.router = Constant.Router.TSD_RECORD;
                    break;
                default:

                    break;
            }
            if (workInfo.isOpen) {
                list.add(workInfo);
            }
        }

        SharedPreferencesUtils.setParam(MBapApp.getAppContext(), Constant.SPKey.WORKS, works.toString());

        return list;

    }

}
