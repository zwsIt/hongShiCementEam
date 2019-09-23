package com.supcon.mes.push.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.supcon.common.view.util.LogUtil;
import com.umeng.commonsdk.framework.UMEnvelopeBuild;
import com.umeng.commonsdk.framework.UMFrUtils;
import com.umeng.commonsdk.internal.crash.UMCrashManager;
import com.umeng.commonsdk.statistics.common.DeviceConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by wangshizhan on 2019/4/23
 * Email:wangshizhan@supcom.com
 */
public class ManifestUtil {

    private static final String TAG = "ManifestUtil";
    private static final String KEY_SHARED_PREFERENCES_NAME = "umeng_common_config";
    private static final String KEY_APP_KEY = "appkey";
    private static final String PUSH_SECRET = "PUSH_SECRET";
    private static final String KEY_LAST_APP_KEY = "last_appkey";
    private static final String KEY_CHANNEL = "channel";
    public static final String UNKNOW = "";
    public static final String MOBILE_NETWORK = "2G/3G";
    public static final String WIFI = "Wi-Fi";
    public static final int DEFAULT_TIMEZONE = 8;
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String SD_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final Pattern pattern = Pattern.compile("UTDID\">([^<]+)");
    private static Object spLock = new Object();

    public ManifestUtil() {
    }

    public static void setMultiProcessSP(Context var0, String var1, String var2) {
        try {
            Object var3 = spLock;
            synchronized(spLock) {
                if (var0 == null || TextUtils.isEmpty(var1) || var2 == null) {
                    return;
                }

                SharedPreferences var4 = null;
                if (isMainProgress(var0)) {
                    var4 = var0.getApplicationContext().getSharedPreferences("umeng_common_config", 0);
                } else {
                    String var5 = UMFrUtils.getSubProcessName(var0);
                    var4 = var0.getApplicationContext().getSharedPreferences(var5 + "_" + "umeng_common_config", 0);
                }

                if (var4 != null) {
                    var4.edit().putString(var1, var2).commit();
                }
            }
        } catch (Exception var8) {
            ;
        } catch (Throwable var9) {
            ;
        }

    }

    public static String getMultiProcessSP(Context var0, String var1) {
        try {
            Object var2 = spLock;
            synchronized(spLock) {
                if (var0 != null && !TextUtils.isEmpty(var1)) {
                    SharedPreferences var3 = null;
                    if (isMainProgress(var0)) {
                        var3 = var0.getApplicationContext().getSharedPreferences("umeng_common_config", 0);
                    } else {
                        String var4 = UMFrUtils.getSubProcessName(var0);
                        var3 = var0.getApplicationContext().getSharedPreferences(var4 + "_" + "umeng_common_config", 0);
                    }

                    return var3 != null ? var3.getString(var1, (String)null) : null;
                } else {
                    return null;
                }
            }
        } catch (Exception var7) {
            return null;
        } catch (Throwable var8) {
            return null;
        }
    }

    public static void setLastAppkey(Context var0, String var1) {
        try {
            if (var0 == null || var1 == null) {
                return;
            }

            setMultiProcessSP(var0, "last_appkey", var1);
        } catch (Exception var3) {
            if (LogUtil.showLog) {
                LogUtil.e("set last app key e is " + var3);
            }

            UMCrashManager.reportCrash(var0, var3);
        } catch (Throwable var4) {
            if (LogUtil.showLog) {
                LogUtil.e("set last app key e is " + var4);
            }

            UMCrashManager.reportCrash(var0, var4);
        }

    }

    public static String getLastAppkey(Context var0) {
        try {
            return var0 == null ? null : getMultiProcessSP(var0, "last_appkey");
        } catch (Exception var2) {
            if (LogUtil.showLog) {
                LogUtil.e("get last app key e is " + var2);
            }

            UMCrashManager.reportCrash(var0, var2);
            return null;
        } catch (Throwable var3) {
            if (LogUtil.showLog) {
                LogUtil.e("get last app key e is " + var3);
            }

            UMCrashManager.reportCrash(var0, var3);
            return null;
        }
    }

    public static void setAppkey(Context var0, String var1) {
        try {
            if (var0 == null || var1 == null) {
                return;
            }

            setMultiProcessSP(var0, "appkey", var1);
        } catch (Exception var3) {
            if (LogUtil.showLog) {
                LogUtil.e("set app key e is " + var3);
            }

            UMCrashManager.reportCrash(var0, var3);
        } catch (Throwable var4) {
            if (LogUtil.showLog) {
                LogUtil.e("set app key e is " + var4);
            }

            UMCrashManager.reportCrash(var0, var4);
        }

    }

    public static String getAppkey(Context var0) {
        try {
            return var0 == null ? null : getMultiProcessSP(var0, "appkey");
        } catch (Exception var2) {
            if (LogUtil.showLog) {
                LogUtil.e("get app key e is " + var2);
            }

            UMCrashManager.reportCrash(var0, var2);
            return null;
        } catch (Throwable var3) {
            if (LogUtil.showLog) {
                LogUtil.e("get app key e is " + var3);
            }

            UMCrashManager.reportCrash(var0, var3);
            return null;
        }
    }

    public static void setChannel(Context var0, String var1) {
        try {
            if (var0 == null || var1 == null) {
                return;
            }

            setMultiProcessSP(var0, "channel", var1);
        } catch (Exception var3) {
            if (LogUtil.showLog) {
                LogUtil.e("set channel e is " + var3);
            }

            UMCrashManager.reportCrash(var0, var3);
        } catch (Throwable var4) {
            if (LogUtil.showLog) {
                LogUtil.e("set channel e is " + var4);
            }

            UMCrashManager.reportCrash(var0, var4);
        }

    }

    public static String getChannel(Context var0) {
        try {
            return var0 == null ? null : getMultiProcessSP(var0, "channel");
        } catch (Exception var2) {
            if (LogUtil.showLog) {
                LogUtil.e("get channel e is " + var2);
            }

            UMCrashManager.reportCrash(var0, var2);
            return null;
        } catch (Throwable var3) {
            if (LogUtil.showLog) {
                LogUtil.e("get channel e is " + var3);
            }

            UMCrashManager.reportCrash(var0, var3);
            return null;
        }
    }

    public static String getUTDID(Context var0) {
        try {
            if (var0 == null) {
                return null;
            } else {
                try {
                    Class var1 = Class.forName("com.ut.device.UTDevice");
                    Method var2 = var1.getMethod("getUtdid", Context.class);
                    return (String)var2.invoke((Object)null, var0.getApplicationContext());
                } catch (Exception var3) {
                    return readUTDId(var0);
                }
            }
        } catch (Exception var4) {
            if (LogUtil.showLog) {
                LogUtil.e("get utiid e is " + var4);
            }

            return null;
        } catch (Throwable var5) {
            if (LogUtil.showLog) {
                LogUtil.e("get utiid e is " + var5);
            }

            return null;
        }
    }

    private static String readUTDId(Context var0) {
        if (var0 == null) {
            return null;
        } else {
            File var1 = getFile(var0);
            if (var1 != null && var1.exists()) {
                try {
                    FileInputStream var2 = new FileInputStream(var1);

                    String var3;
                    try {
                        var3 = parseId(readStreamToString(var2));
                    } finally {
                        safeClose(var2);
                    }

                    return var3;
                } catch (Exception var8) {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    private static void safeClose(InputStream var0) {
        if (var0 != null) {
            try {
                var0.close();
            } catch (Exception var2) {
                ;
            }
        }

    }

    private static String parseId(String var0) {
        if (var0 == null) {
            return null;
        } else {
            Matcher var1 = pattern.matcher(var0);
            return var1.find() ? var1.group(1) : null;
        }
    }

    private static String readStreamToString(InputStream var0) throws IOException {
        InputStreamReader var1 = new InputStreamReader(var0);
        char[] var2 = new char[1024];
        boolean var3 = false;
        StringWriter var4 = new StringWriter();

        int var5;
        while(-1 != (var5 = var1.read(var2))) {
            var4.write(var2, 0, var5);
        }

        return var4.toString();
    }

    private static File getFile(Context var0) {
        if (var0 == null) {
            return null;
        } else if (!checkPermission(var0, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            return null;
        } else {
            if (Environment.getExternalStorageState().equals("mounted")) {
                File var1 = Environment.getExternalStorageDirectory();

                try {
                    return new File(var1.getCanonicalPath(), ".UTSystemConfig/Global/Alvin2.xml");
                } catch (Exception var3) {
                    ;
                }
            }

            return null;
        }
    }

    public static String[] getGPU(GL10 var0) {
        try {
            String[] var1 = new String[2];
            String var2 = var0.glGetString(7936);
            String var3 = var0.glGetString(7937);
            var1[0] = var2;
            var1[1] = var3;
            return var1;
        } catch (Exception var4) {
            if (LogUtil.showLog) {
                LogUtil.e("Could not read gpu infor, e is " + var4);
            }

            return new String[0];
        } catch (Throwable var5) {
            if (LogUtil.showLog) {
                LogUtil.e("Could not read gpu infor, e is " + var5);
            }

            return new String[0];
        }
    }

    public static String getCPU() {
        try {
            String var0 = null;
            FileReader var1 = null;
            BufferedReader var2 = null;

            try {
                var1 = new FileReader("/proc/cpuinfo");
                if (var1 != null) {
                    try {
                        var2 = new BufferedReader(var1, 1024);
                        var0 = var2.readLine();
                        var2.close();
                        var1.close();
                    } catch (IOException var4) {
                        if (LogUtil.showLog) {
                            LogUtil.e("Could not read from file /proc/cpuinfo, e is " + var4);
                        }
                    }
                }
            } catch (FileNotFoundException var5) {
                if (LogUtil.showLog) {
                    LogUtil.e("Could not read from file /proc/cpuinfo, e is " + var5);
                }
            }

            if (var0 != null) {
                int var3 = var0.indexOf(58) + 1;
                var0 = var0.substring(var3);
                return var0.trim();
            } else {
                return "";
            }
        } catch (Exception var6) {
            if (LogUtil.showLog) {
                LogUtil.e("get cpu e is " + var6);
            }

            return "";
        } catch (Throwable var7) {
            if (LogUtil.showLog) {
                LogUtil.e("get cpu e is " + var7);
            }

            return "";
        }
    }

    public static String getImsi(Context var0) {
        try {
            TelephonyManager var1 = (TelephonyManager)var0.getSystemService(Context.TELEPHONY_SERVICE);
            String var2 = null;
            if (checkPermission(var0, "android.permission.READ_PHONE_STATE") && var1 != null) {
                var2 = var1.getSubscriberId();
            }

            return var2;
        } catch (Exception var3) {
            if (LogUtil.showLog) {
                LogUtil.e("get imei e is " + var3);
            }

            UMCrashManager.reportCrash(var0, var3);
            return null;
        } catch (Throwable var4) {
            if (LogUtil.showLog) {
                LogUtil.e("get imei e is " + var4);
            }

            UMCrashManager.reportCrash(var0, var4);
            return null;
        }
    }

    public static String getRegisteredOperator(Context var0) {
        if (var0 == null) {
            return null;
        } else {
            try {
                TelephonyManager var1 = (TelephonyManager)var0.getSystemService(Context.TELEPHONY_SERVICE);
                if (var1 == null) {
                    return null;
                } else {
                    String var2 = null;
                    if (checkPermission(var0, "android.permission.READ_PHONE_STATE")) {
                        var2 = var1.getNetworkOperator();
                    }

                    return var2;
                }
            } catch (Exception var3) {
                if (LogUtil.showLog) {
                    LogUtil.e("get registered operator e is " + var3);
                }

                UMCrashManager.reportCrash(var0, var3);
                return null;
            } catch (Throwable var4) {
                if (LogUtil.showLog) {
                    LogUtil.e("get registered operator e is " + var4);
                }

                UMCrashManager.reportCrash(var0, var4);
                return null;
            }
        }
    }

    public static String getNetworkOperatorName(Context var0) {
        if (var0 == null) {
            return null;
        } else {
            try {
                TelephonyManager var1 = (TelephonyManager)var0.getSystemService(Context.TELEPHONY_SERVICE);
                if (!checkPermission(var0, "android.permission.READ_PHONE_STATE")) {
                    return "";
                } else {
                    return var1 == null ? "" : var1.getNetworkOperatorName();
                }
            } catch (Exception var2) {
                if (LogUtil.showLog) {
                    LogUtil.e("get network operator e is " + var2);
                }

                UMCrashManager.reportCrash(var0, var2);
                return "";
            } catch (Throwable var3) {
                if (LogUtil.showLog) {
                    LogUtil.e("get network operator e is " + var3);
                }

                UMCrashManager.reportCrash(var0, var3);
                return "";
            }
        }
    }

    public static String getDisplayResolution(Context var0) {
        if (var0 == null) {
            return "";
        } else {
            try {
                DisplayMetrics var1 = new DisplayMetrics();
                WindowManager var2 = (WindowManager)((WindowManager)var0.getSystemService(Context.WINDOW_SERVICE));
                if (var2 == null) {
                    return "";
                } else {
                    var2.getDefaultDisplay().getMetrics(var1);
                    int var3 = var1.widthPixels;
                    int var4 = var1.heightPixels;
                    String var5 = var4 + "*" + var3;
                    return var5;
                }
            } catch (Exception var6) {
                if (LogUtil.showLog) {
                    LogUtil.e("get display resolution e is " + var6);
                }

                UMCrashManager.reportCrash(var0, var6);
                return "";
            } catch (Throwable var7) {
                if (LogUtil.showLog) {
                    LogUtil.e("get display resolution e is " + var7);
                }

                UMCrashManager.reportCrash(var0, var7);
                return "";
            }
        }
    }

    public static String[] getNetworkAccessMode(Context var0) {
        String[] var1 = new String[]{"", ""};
        if (var0 == null) {
            return var1;
        } else {
            try {
                if (!checkPermission(var0, "android.permission.ACCESS_NETWORK_STATE")) {
                    var1[0] = "";
                    return var1;
                }

                ConnectivityManager var2 = (ConnectivityManager)var0.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (var2 == null) {
                    var1[0] = "";
                    return var1;
                }

                NetworkInfo var3 = var2.getNetworkInfo(1);
                if (var3 != null && var3.getState() == NetworkInfo.State.CONNECTED) {
                    var1[0] = "Wi-Fi";
                    return var1;
                }

                NetworkInfo var4 = var2.getNetworkInfo(0);
                if (var4 != null && var4.getState() == NetworkInfo.State.CONNECTED) {
                    var1[0] = "2G/3G";
                    var1[1] = var4.getSubtypeName();
                    return var1;
                }
            } catch (Exception var5) {
                if (LogUtil.showLog) {
                    LogUtil.e("get network access mode e is " + var5);
                }

                UMCrashManager.reportCrash(var0, var5);
            } catch (Throwable var6) {
                if (LogUtil.showLog) {
                    LogUtil.e("get network access mode e is " + var6);
                }

                UMCrashManager.reportCrash(var0, var6);
            }

            return var1;
        }
    }

    public static boolean isSdCardWrittenable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static Locale getLocale(Context var0) {
        if (var0 == null) {
            return null;
        } else {
            try {
                Locale var1 = null;

                try {
                    Configuration var2 = new Configuration();
                    var2.setToDefaults();
                    Settings.System.getConfiguration(var0.getContentResolver(), var2);
                    if (var2 != null) {
                        var1 = var2.locale;
                    }
                } catch (Exception var3) {
                    if (LogUtil.showLog) {
                        LogUtil.e("fail to read user config locale, e is " + var3);
                    }
                }

                if (var1 == null) {
                    var1 = Locale.getDefault();
                }

                return var1;
            } catch (Exception var4) {
                if (LogUtil.showLog) {
                    LogUtil.e("get locale e is " + var4);
                }

                UMCrashManager.reportCrash(var0, var4);
                return null;
            } catch (Throwable var5) {
                if (LogUtil.showLog) {
                    LogUtil.e("get locale e is " + var5);
                }

                UMCrashManager.reportCrash(var0, var5);
                return null;
            }
        }
    }

    public static String getMac(Context var0) {
        if (var0 == null) {
            return null;
        } else {
            try {
                WifiManager var1 = (WifiManager)var0.getSystemService(Context.WIFI_SERVICE);
                if (var1 == null) {
                    return null;
                } else if (checkPermission(var0, "android.permission.ACCESS_WIFI_STATE")) {
                    WifiInfo var2 = var1.getConnectionInfo();
                    return var2.getMacAddress();
                } else {
                    if (LogUtil.showLog) {
                        LogUtil.e("Could not get mac address.[no permission android.permission.ACCESS_WIFI_STATE");
                    }

                    return "";
                }
            } catch (Exception var3) {
                if (LogUtil.showLog) {
                    LogUtil.e("get mac e is " + var3);
                }

                UMCrashManager.reportCrash(var0, var3);
                return null;
            } catch (Throwable var4) {
                if (LogUtil.showLog) {
                    LogUtil.e("get mac e is " + var4);
                }

                UMCrashManager.reportCrash(var0, var4);
                return null;
            }
        }
    }

    public static String getOperator(Context var0) {
        if (var0 == null) {
            return "Unknown";
        } else {
            try {
                TelephonyManager var1 = (TelephonyManager)var0.getSystemService(Context.TELEPHONY_SERVICE);
                if (var1 == null) {
                    return "Unknown";
                }

                return var1.getNetworkOperator();
            } catch (Exception var2) {
                if (LogUtil.showLog) {
                    LogUtil.e("get get operator e is " + var2);
                }

                UMCrashManager.reportCrash(var0, var2);
            } catch (Throwable var3) {
                if (LogUtil.showLog) {
                    LogUtil.e("get get operator e is " + var3);
                }

                UMCrashManager.reportCrash(var0, var3);
            }

            return "Unknown";
        }
    }

    public static String getSubOSName(Context var0) {
        if (var0 == null) {
            return null;
        } else {
            try {
                String var1 = null;
                Properties var2 = getBuildProp();

                try {
                    var1 = var2.getProperty("ro.miui.ui.version.name");
                    if (TextUtils.isEmpty(var1)) {
                        if (isFlyMe()) {
                            var1 = "Flyme";
                        } else if (!TextUtils.isEmpty(getYunOSVersion(var2))) {
                            var1 = "YunOS";
                        }
                    } else {
                        var1 = "MIUI";
                    }
                } catch (Exception var4) {
                    var1 = null;
                    UMCrashManager.reportCrash(var0, var4);
                }

                return var1;
            } catch (Exception var5) {
                if (LogUtil.showLog) {
                    LogUtil.e("get sub os name e is " + var5);
                }

                UMCrashManager.reportCrash(var0, var5);
                return null;
            } catch (Throwable var6) {
                if (LogUtil.showLog) {
                    LogUtil.e("get sub os name e is " + var6);
                }

                UMCrashManager.reportCrash(var0, var6);
                return null;
            }
        }
    }

    public static String getSubOSVersion(Context var0) {
        if (var0 == null) {
            return null;
        } else {
            try {
                String var1 = null;
                Properties var2 = getBuildProp();

                try {
                    var1 = var2.getProperty("ro.miui.ui.version.name");
                    if (TextUtils.isEmpty(var1)) {
                        if (isFlyMe()) {
                            try {
                                var1 = getFlymeVersion(var2);
                            } catch (Exception var5) {
                                ;
                            }
                        } else {
                            try {
                                var1 = getYunOSVersion(var2);
                            } catch (Exception var4) {
                                ;
                            }
                        }
                    }
                } catch (Exception var6) {
                    var1 = null;
                    UMCrashManager.reportCrash(var0, var6);
                }

                return var1;
            } catch (Exception var7) {
                if (LogUtil.showLog) {
                    LogUtil.e("get sub os version e is " + var7);
                }

                UMCrashManager.reportCrash(var0, var7);
                return null;
            } catch (Throwable var8) {
                if (LogUtil.showLog) {
                    LogUtil.e("get sub os version e is " + var8);
                }

                UMCrashManager.reportCrash(var0, var8);
                return null;
            }
        }
    }

    private static String getYunOSVersion(Properties var0) {
        String var1 = var0.getProperty("ro.yunos.version");
        return !TextUtils.isEmpty(var1) ? var1 : null;
    }

    private static String getFlymeVersion(Properties var0) {
        try {
            String var1 = var0.getProperty("ro.build.display.id").toLowerCase(Locale.getDefault());
            if (var1.contains("flyme os")) {
                return var1.split(" ")[2];
            }
        } catch (Exception var2) {
            ;
        }

        return null;
    }

    public static Properties getBuildProp() {
        Properties var0 = new Properties();
        FileInputStream var1 = null;

        try {
            var1 = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
            var0.load(var1);
        } catch (IOException var11) {
            ;
        } finally {
            if (var1 != null) {
                try {
                    var1.close();
                } catch (IOException var10) {
                    ;
                }
            }

        }

        return var0;
    }

    private static boolean isFlyMe() {
        try {
            Method var0 = Build.class.getMethod("hasSmartBar");
            return true;
        } catch (Exception var1) {
            return false;
        }
    }

    public static String getDeviceType(Context var0) {
        try {
            String var1 = Context.TELEPHONY_SERVICE;
            if (var0 == null) {
                return var1;
            } else {
                boolean var2 = (var0.getResources().getConfiguration().screenLayout & 15) >= 3;
                if (var2) {
                    var1 = "Tablet";
                } else {
                    var1 = Context.TELEPHONY_SERVICE;
                }

                return var1;
            }
        } catch (Exception var3) {
            if (LogUtil.showLog) {
                LogUtil.e("get device type e is " + var3);
            }

            UMCrashManager.reportCrash(var0, var3);
            return null;
        } catch (Throwable var4) {
            if (LogUtil.showLog) {
                LogUtil.e("get device type e is " + var4);
            }

            UMCrashManager.reportCrash(var0, var4);
            return null;
        }
    }

    public static String getAppVersionCode(Context var0) {
        if (var0 == null) {
            return "";
        } else {
            try {
                PackageInfo var1 = var0.getPackageManager().getPackageInfo(var0.getPackageName(), 0);
                int var2 = var1.versionCode;
                return String.valueOf(var2);
            } catch (Exception var3) {
                if (LogUtil.showLog) {
                    LogUtil.e("get app version code e is " + var3);
                }

                return "";
            } catch (Throwable var4) {
                if (LogUtil.showLog) {
                    LogUtil.e("get app version code e is " + var4);
                }

                return "";
            }
        }
    }

    public static String getAppVersinoCode(Context var0, String var1) {
        if (var0 != null && var1 != null) {
            try {
                PackageInfo var2 = var0.getPackageManager().getPackageInfo(var1, 0);
                int var3 = var2.versionCode;
                return String.valueOf(var3);
            } catch (Exception var4) {
                if (LogUtil.showLog) {
                    LogUtil.e("get app version code e is " + var4);
                }

                return "";
            } catch (Throwable var5) {
                if (LogUtil.showLog) {
                    LogUtil.e("get app version code e is " + var5);
                }

                return "";
            }
        } else {
            return "";
        }
    }

    public static String getAppVersionName(Context var0) {
        if (var0 == null) {
            return "";
        } else {
            try {
                PackageInfo var1 = var0.getPackageManager().getPackageInfo(var0.getPackageName(), 0);
                return var1.versionName;
            } catch (PackageManager.NameNotFoundException var2) {
                if (LogUtil.showLog) {
                    LogUtil.e("get app version name e is " + var2);
                }

                return "";
            } catch (Throwable var3) {
                if (LogUtil.showLog) {
                    LogUtil.e("get app version name e is " + var3);
                }

                return "";
            }
        }
    }

    public static String getAppVersionName(Context var0, String var1) {
        if (var0 != null && var1 != null) {
            try {
                PackageInfo var2 = var0.getPackageManager().getPackageInfo(var1, 0);
                return var2.versionName;
            } catch (PackageManager.NameNotFoundException var3) {
                if (LogUtil.showLog) {
                    LogUtil.e("get app version name e is " + var3);
                }

                UMCrashManager.reportCrash(var0, var3);
                return "";
            } catch (Throwable var4) {
                if (LogUtil.showLog) {
                    LogUtil.e("get app version name e is " + var4);
                }

                UMCrashManager.reportCrash(var0, var4);
                return "";
            }
        } else {
            return "";
        }
    }

    public static boolean checkPermission(Context var0, String var1) {
        boolean var2 = false;
        if (var0 == null) {
            return false;
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    Class var3 = Class.forName("android.content.Context");
                    Method var4 = var3.getMethod("checkSelfPermission", String.class);
                    int var5 = (Integer)var4.invoke(var0, var1);
                    if (var5 == 0) {
                        var2 = true;
                    } else {
                        var2 = false;
                    }
                } catch (Exception var6) {
                    UMCrashManager.reportCrash(var0, var6);
                    var2 = false;
                }
            } else {
                PackageManager var7 = var0.getPackageManager();
                if (var7.checkPermission(var1, var0.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                    var2 = true;
                }
            }

            return var2;
        }
    }

    private static String byte2HexFormatted(byte[] var0) {
        StringBuilder var1 = new StringBuilder(var0.length * 2);

        for(int var2 = 0; var2 < var0.length; ++var2) {
            String var3 = Integer.toHexString(var0[var2]);
            int var4 = var3.length();
            if (var4 == 1) {
                var3 = "0" + var3;
            }

            if (var4 > 2) {
                var3 = var3.substring(var4 - 2, var4);
            }

            var1.append(var3.toUpperCase());
            if (var2 < var0.length - 1) {
                var1.append(':');
            }
        }

        return var1.toString();
    }

    public static boolean isDebug(Context var0) {
        if (var0 == null) {
            return false;
        } else {
            try {
                return (var0.getApplicationInfo().flags & 2) != 0;
            } catch (Exception var2) {
                UMCrashManager.reportCrash(var0, var2);
                return false;
            }
        }
    }

    public static String getAppName(Context var0) {
        String var1 = null;
        if (var0 == null) {
            return null;
        } else {
            try {
                PackageInfo var2 = var0.getPackageManager().getPackageInfo(var0.getPackageName(), 0);
                var1 = var2.applicationInfo.loadLabel(var0.getPackageManager()).toString();
            } catch (Exception var3) {
                if (LogUtil.showLog) {
                    LogUtil.e("get app name e is " + var3);
                }

                UMCrashManager.reportCrash(var0, var3);
            } catch (Throwable var4) {
                if (LogUtil.showLog) {
                    LogUtil.e("get app name e is " + var4);
                }

                UMCrashManager.reportCrash(var0, var4);
            }

            return var1;
        }
    }

    public static String MD5(String var0) {
        try {
            if (var0 == null) {
                return null;
            } else {
                try {
                    byte[] var1 = var0.getBytes();
                    MessageDigest var2 = MessageDigest.getInstance("MD5");
                    var2.reset();
                    var2.update(var1);
                    byte[] var3 = var2.digest();
                    StringBuffer var4 = new StringBuffer();

                    for(int var5 = 0; var5 < var3.length; ++var5) {
                        var4.append(String.format("%02X", var3[var5]));
                    }

                    return var4.toString();
                } catch (Exception var6) {
                    return var0.replaceAll("[^[a-z][A-Z][0-9][.][_]]", "");
                }
            }
        } catch (Exception var7) {
            if (LogUtil.showLog) {
                LogUtil.e("MD5 e is " + var7);
            }

            return null;
        } catch (Throwable var8) {
            if (LogUtil.showLog) {
                LogUtil.e("MD5 e is " + var8);
            }

            return null;
        }
    }

    public static String getFileMD5(File var0) {
        try {
            MessageDigest var1 = null;
            FileInputStream var2 = null;
            byte[] var3 = new byte[1024];

            try {
                if (!var0.isFile()) {
                    return "";
                }

                var1 = MessageDigest.getInstance("MD5");
                var2 = new FileInputStream(var0);

                while(true) {
                    int var4;
                    if ((var4 = var2.read(var3, 0, 1024)) == -1) {
                        var2.close();
                        break;
                    }

                    var1.update(var3, 0, var4);
                }
            } catch (Exception var6) {
                return null;
            }

            BigInteger var5 = new BigInteger(1, var1.digest());
            return String.format("%1$032x", var5);
        } catch (Exception var7) {
            if (LogUtil.showLog) {
                LogUtil.e("get file MD5 e is " + var7);
            }

            return null;
        } catch (Throwable var8) {
            if (LogUtil.showLog) {
                LogUtil.e("get file MD5 e is " + var8);
            }

            return null;
        }
    }

    public static String encryptBySHA1(String var0) {
        try {
            MessageDigest var1 = null;
            String var2 = null;
            byte[] var3 = var0.getBytes();

            try {
                var1 = MessageDigest.getInstance("SHA1");
                var1.update(var3);
                var2 = bytes2Hex(var1.digest());
            } catch (Exception var5) {
                return null;
            }

            return var2;
        } catch (Exception var6) {
            if (LogUtil.showLog) {
                LogUtil.e("encrypt by SHA1 e is " + var6);
            }

            return null;
        } catch (Throwable var7) {
            if (LogUtil.showLog) {
                LogUtil.e("encrypt by SHA1 e is " + var7);
            }

            return null;
        }
    }

    private static String bytes2Hex(byte[] var0) {
        String var1 = "";
        String var2 = null;

        for(int var3 = 0; var3 < var0.length; ++var3) {
            var2 = Integer.toHexString(var0[var3] & 255);
            if (var2.length() == 1) {
                var1 = var1 + "0";
            }

            var1 = var1 + var2;
        }

        return var1;
    }

    public static String getUMId(Context var0) {
        String var1 = null;

        try {
            if (var0 != null) {
                var1 = UMEnvelopeBuild.imprintProperty(var0.getApplicationContext(), "umid", (String)null);
            }
        } catch (Exception var3) {
            UMCrashManager.reportCrash(var0, var3);
        }

        return var1;
    }

    public static String getDeviceToken(Context var0) {
        String var1 = null;
        if (var0 != null) {
            var0 = var0.getApplicationContext();

            try {
                Class var2 = Class.forName("com.umeng.message.MessageSharedPrefs");
                if (var2 != null) {
                    Method var3 = var2.getMethod("getInstance", Context.class);
                    if (var3 != null) {
                        Object var4 = var3.invoke(var2, var0);
                        if (var4 != null) {
                            Method var5 = var2.getMethod("getDeviceToken");
                            if (var5 != null) {
                                Object var6 = var5.invoke(var4);
                                if (var6 != null && var6 instanceof String) {
                                    var1 = (String)var6;
                                }
                            }
                        }
                    }
                }
            } catch (Throwable var7) {
                ;
            }
        }

        return var1;
    }

    public static String getAppkeyByXML(Context var0) {
        Object var1 = null;

        try {
            PackageManager var2 = var0.getPackageManager();
            ApplicationInfo var3 = var2.getApplicationInfo(var0.getPackageName(), PackageManager.GET_META_DATA);
            if (var3 != null) {
                String var4 = var3.metaData.getString("UMENG_APPKEY");
                if (var4 != null) {
                    return var4.trim();
                }

                if (LogUtil.showLog) {
                    LogUtil.i("Could not read UMENG_APPKEY meta-data from AndroidManifest.xml.");
                }
            }
        } catch (Throwable var5) {
            ;
        }

        return null;
    }

    public static String getMessageSecretByXML(Context var0) {
        Object var1 = null;

        try {
            PackageManager var2 = var0.getPackageManager();
            ApplicationInfo var3 = var2.getApplicationInfo(var0.getPackageName(), PackageManager.GET_META_DATA);
            if (var3 != null) {
                String var4 = var3.metaData.getString("UMENG_MESSAGE_SECRET");
                if (var4 != null) {
                    return var4.trim();
                }

                if (LogUtil.showLog) {
                    LogUtil.i("Could not read UMENG_APPKEY meta-data from AndroidManifest.xml.");
                }
            }
        } catch (Throwable var5) {
            ;
        }

        return null;
    }

    public static String getChannelByXML(Context var0) {
        Object var1 = null;

        try {
            PackageManager var2 = var0.getPackageManager();
            ApplicationInfo var3 = var2.getApplicationInfo(var0.getPackageName(), PackageManager.GET_META_DATA);
            if (var3 != null && var3.metaData != null) {
                Object var4 = var3.metaData.get("UMENG_CHANNEL");
                if (var4 != null) {
                    String var5 = var4.toString();
                    if (var5 != null) {
                        return var5.trim();
                    }

                    if (LogUtil.showLog) {
                        LogUtil.i("Could not read UMENG_CHANNEL meta-data from AndroidManifest.xml.");
                    }
                }
            }
        } catch (Throwable var6) {
            ;
        }

        return null;
    }

    public static boolean checkPath(String var0) {
        try {
            Class var1 = Class.forName(var0);
            return var1 != null;
        } catch (ClassNotFoundException var2) {
            return false;
        }
    }

    public static boolean checkAndroidManifest(Context var0, String var1) {
        try {
            ComponentName var2 = new ComponentName(var0.getApplicationContext().getPackageName(), var1);
            PackageManager var3 = var0.getApplicationContext().getPackageManager();
            var3.getActivityInfo(var2, 0);
            return true;
        } catch (PackageManager.NameNotFoundException var5) {
            return false;
        }
    }

    public static boolean checkIntentFilterData(Context var0, String var1) {
        Intent var2 = new Intent();
        var2.setAction("android.intent.action.VIEW");
        var2.addCategory("android.intent.category.DEFAULT");
        var2.addCategory("android.intent.category.BROWSABLE");
        String var3 = "tencent" + var1 + ":";
        Uri var4 = Uri.parse(var3);
        var2.setData(var4);
        List var5 = var0.getApplicationContext().getPackageManager().queryIntentActivities(var2, PackageManager.GET_RESOLVED_FILTER);
        if (var5.size() > 0) {
            Iterator var6 = var5.iterator();

            ResolveInfo var7;
            do {
                if (!var6.hasNext()) {
                    return false;
                }

                var7 = (ResolveInfo)var6.next();
            } while(var7.activityInfo == null || !var7.activityInfo.packageName.equals(var0.getApplicationContext().getPackageName()));

            return true;
        } else {
            return false;
        }
    }

    public static boolean checkResource(Context var0, String var1, String var2) {
        int var3 = var0.getApplicationContext().getResources().getIdentifier(var1, var2, var0.getApplicationContext().getPackageName());
        return var3 > 0;
    }

    public static boolean checkMetaData(Context var0, String var1) {
        PackageManager var2 = var0.getApplicationContext().getPackageManager();
        ApplicationInfo var3 = null;

        try {
            var3 = var2.getApplicationInfo(var0.getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
            if (var3 != null) {
                Object var4 = var3.metaData.get(var1);
                if (var4 != null) {
                    return true;
                }

                return false;
            }
        } catch (PackageManager.NameNotFoundException var5) {
            ;
        }

        return false;
    }

    public static String getAppMD5Signature(Context var0) {
        String var1 = DeviceConfig.getAppMD5Signature(var0);
        if (!TextUtils.isEmpty(var1)) {
            var1 = var1.replace(":", "");
            var1 = var1.toLowerCase();
        }

        return var1;
    }

    public static String getAppSHA1Key(Context var0) {
        return DeviceConfig.getAppSHA1Key(var0);
    }

    public static String getAppHashKey(Context var0) {
        return DeviceConfig.getAppHashKey(var0);
    }

    public static int getTargetSdkVersion(Context var0) {
        return var0.getApplicationInfo().targetSdkVersion;
    }

    public static boolean isMainProgress(Context var0) {
        boolean var1 = false;

        try {
            String var2 = UMFrUtils.getCurrentProcessName(var0);
            String var3 = var0.getApplicationContext().getPackageName();
            if (!TextUtils.isEmpty(var2) && !TextUtils.isEmpty(var3) && var2.equals(var3)) {
                var1 = true;
            }
        } catch (Exception var4) {
            ;
        }

        return var1;
    }

    public static boolean isApplication(Context var0) {
        boolean var1 = false;

        try {
            String var2 = var0.getApplicationContext().getClass().getSuperclass().getName();
            if (!TextUtils.isEmpty(var2) && var2.equals("android.app.Application")) {
                var1 = true;
            }
        } catch (Exception var3) {
            ;
        }

        return var1;
    }


}
