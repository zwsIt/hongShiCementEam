package com.supcon.mes.module_main.ui.util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;


public class IntentUtils {

    /**
     * 获取APP初始页面
     *
     * @param context
     * @return
     */

    private static Class<? extends Activity> getRestartActivityClass(Activity context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (intent != null && intent.getComponent() != null) {
            try {
                return (Class<? extends Activity>) Class.forName(intent.getComponent().getClassName());
            } catch (ClassNotFoundException e) {
                //Should not happen, print it to the log!
                Log.e("getLauncherActivity", "Failed when resolving the restart activity class via getLaunchIntentForPackage, stack trace follows!", e);
            }
        }
        return null;
    }

    /**
     * 重启应用
     *
     * @param activity
     */
    public static void restartApp(Activity activity) {
        Intent intent1 = new Intent(activity, getRestartActivityClass(activity));
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        if (intent1.getComponent() != null) {
            intent1.setAction(Intent.ACTION_MAIN);
            intent1.addCategory(Intent.CATEGORY_LAUNCHER);
        }
        activity.finish();
        activity.startActivity(intent1);
        killCurrentProcess();
    }

    private static void killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}
