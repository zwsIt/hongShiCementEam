package com.supcon.mes.middleware;

import android.content.Context;

import com.supcon.common.com_router.api.IRouter;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.MBapApp;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by wangshizhan on 2018/4/28.
 * Email:wangshizhan@supcon.com
 */

public class MainRouter implements IRouter {

    private static final String MODULE_DATA_MANAGER_ROUTER = "com.supcon.mes.module_data_manage.IntentRouter";
    private static final String MODULE_LOGIN_ROUTER = "com.supcon.mes.module_login.IntentRouter";
    private static final String MODULE_YH_ROUTER = "com.supcon.mes.module_yhgl.IntentRouter";
    //    private static final String MODULE_YXJL_ROUTER = "com.supcon.mes.module_yxjl.IntentRouter";
//    private static final String MODULE_QXGL_ROUTER = "com.supcon.mes.module_qxgl.IntentRouter";
    private static final String MODULE_XJ_ROUTER = "com.supcon.mes.module_xj.IntentRouter";
    private static final String MODULE_OLXJ_ROUTER = "com.supcon.mes.module_olxj.IntentRouter";
    private static final String MODULE_SBDA_ROUTER = "com.supcon.mes.module_sbda.IntentRouter";
    private static final String MODULE_WXGD_ROUTER = "com.supcon.mes.module_wxgd.IntentRouter";
    private static final String MODULE_MIDDLE_ROUTER = "com.supcon.mes.middleware.IntentRouter";
    private static final String MODULE_TSD = "com.supcon.mes.module_tsd.IntentRouter";
    private static final String MODULE_SBDA_ONLINE = "com.supcon.mes.module_sbda_online.IntentRouter";
    private static final String MODULE_EARLY_WARN = "com.supcon.mes.module_warn.IntentRouter";
    private static final String MODULE_SCORE = "com.supcon.mes.module_score.IntentRouter";
    private static final String MODULE_ACCEPTANCE = "com.supcon.mes.module_acceptance.IntentRouter";
    private static final String MODULE_MAIN = "com.supcon.mes.module_main.IntentRouter";

    private static class MainRouterHolder {
        private static MainRouter instance = new MainRouter();
    }


    public static MainRouter getInstance() {

        return MainRouterHolder.instance;
    }

    List<String> modules = new ArrayList<>();

    private MainRouter() {

        modules.add(MODULE_DATA_MANAGER_ROUTER);
        modules.add(MODULE_LOGIN_ROUTER);
        modules.add(MODULE_YH_ROUTER);
//        modules.add(MODULE_YXJL_ROUTER);
//        modules.add(MODULE_QXGL_ROUTER);
        modules.add(MODULE_XJ_ROUTER);
        modules.add(MODULE_OLXJ_ROUTER);
        modules.add(MODULE_SBDA_ROUTER);
        modules.add(MODULE_WXGD_ROUTER);
        modules.add(MODULE_MIDDLE_ROUTER);
        modules.add(MODULE_TSD);
        modules.add(MODULE_SBDA_ONLINE);
        modules.add(MODULE_EARLY_WARN);
        modules.add(MODULE_SCORE);
        modules.add(MODULE_ACCEPTANCE);
        modules.add(MODULE_MAIN);
    }


    public void setup() {

        for (String module : modules) {

            try {
                Class clazz = Class.forName(module);
                Method method = clazz.getMethod("go", new Class[]{Context.class, String.class});
                method.invoke(MBapApp.getAppContext(), "");

            } catch (Exception e) {
                LogUtil.e(e.getMessage());
            }

        }
    }

}
