package com.supcon.mes.middleware.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.MBapConstant;
import com.supcon.mes.middleware.EamApplication;

/**
 * Created by wangshizhan on 2019/4/30
 * Email:wangshizhan@supcom.com
 */
public class ChannelUtil {

    public static String getUMengChannel() {
        ApplicationInfo appInfo = null;
        String channelValue = "";
        try {
            appInfo = EamApplication.getAppContext().getPackageManager().getApplicationInfo(EamApplication.getAppContext().getPackageName(), PackageManager.GET_META_DATA);
            Object channelId = appInfo.metaData.get("UMENG_CHANNEL");
            channelValue = TextUtils.isEmpty(channelId.toString()) ? "hongshi" : channelId.toString();
        } catch (PackageManager.NameNotFoundException e) {
            channelValue = "hongshi";
            e.printStackTrace();
        }
        return channelValue;
    }

    public static String getChannel() {
        ApplicationInfo appInfo = null;
        String channelValue = "";
        try {
            appInfo = EamApplication.getAppContext().getPackageManager().getApplicationInfo(EamApplication.getAppContext().getPackageName(), PackageManager.GET_META_DATA);
            Object channelId = appInfo.metaData.get("CHANNEL_VALUE");
            channelValue = TextUtils.isEmpty(channelId.toString()) ? "hongshi" : channelId.toString();
        } catch (PackageManager.NameNotFoundException e) {
            channelValue = "hongshi";
            e.printStackTrace();
        }
        return channelValue;
    }

    public static String getAppName() {
        ApplicationInfo appInfo = null;
        String value = "";
        try {
            appInfo = EamApplication.getAppContext().getPackageManager().getApplicationInfo(EamApplication.getAppContext().getPackageName(), PackageManager.GET_META_DATA);
            Object appName = appInfo.metaData.get("APP_NAME");
            value = TextUtils.isEmpty(appName.toString()) ? "红狮移动平台" : appName.toString();
        } catch (PackageManager.NameNotFoundException e) {
            value = "红狮移动平台";
            e.printStackTrace();
        }
        return value;
    }


    public static String getAppPackage() {
        ApplicationInfo appInfo = null;
        String value = "";
        try {
            appInfo = EamApplication.getAppContext().getPackageManager().getApplicationInfo(EamApplication.getAppContext().getPackageName(), PackageManager.GET_META_DATA);
            Object appPackage = appInfo.metaData.get("APP_SHARE_ID");
            value = TextUtils.isEmpty(appPackage.toString()) ? "com.supcon.mes.cementEam" : appPackage.toString();
        } catch (PackageManager.NameNotFoundException e) {
            value = "com.supcon.mes.cementEam";
            e.printStackTrace();
        }
        return value;
    }



}
