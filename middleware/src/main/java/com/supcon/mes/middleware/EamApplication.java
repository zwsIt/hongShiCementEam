package com.supcon.mes.middleware;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.text.TextUtils;

import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.LogUtils;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.MBapConstant;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.cache.CacheUtil;
import com.supcon.mes.middleware.alone.AloneManager;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.AccountInfo;
import com.supcon.mes.middleware.model.bean.AccountInfoDao;
import com.supcon.mes.middleware.model.bean.Company;
import com.supcon.mes.middleware.model.bean.DaoMaster;
import com.supcon.mes.middleware.model.bean.DaoSession;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.WorkInfo;
import com.supcon.mes.middleware.util.ChannelUtil;
import com.supcon.mes.middleware.util.CrashHandler;
import com.supcon.mes.middleware.util.CustomDevOpenHelper;
import com.supcon.mes.middleware.util.DatabaseContext;
import com.supcon.mes.middleware.util.WorkHelper;

import java.util.List;


/**
 * Created by wangshizhan on 2017/8/11.
 */

public class EamApplication extends MBapApp {

    private static AloneManager mAloneManager;
    private static DaoSession daoSession;
    private static AccountInfo accountInfo; //用户信息
    private static Staff me;
    public static float fontSizeScale;

    public static AloneManager getAloneManager() {
        return mAloneManager;
    }

    public static AccountInfo getAccountInfo() {
        if (accountInfo == null) {
            accountInfo = dao().getAccountInfoDao().queryBuilder()
                    .where(AccountInfoDao.Properties.UserName.eq(TextUtils.isEmpty(getUserName())?"":getUserName().toLowerCase()), AccountInfoDao.Properties.Ip.eq(getIpPort()))
                    .unique();
        }

        if (accountInfo == null) {
            accountInfo = new AccountInfo();
        }
        return accountInfo;
    }

    public static void setAccountInfo(AccountInfo accountInfo) {
        EamApplication.accountInfo = accountInfo;
    }

    public static Staff me(){

        if(me == null){
            String staffStr = SharedPreferencesUtils.getParam(getAppContext(), Constant.SPKey.STAFF, "");
            me = GsonUtil.gsonToBean(staffStr, Staff.class);
        }

        return me;
    }

   public static void setStaff(Staff staff){
        me = staff;
       SharedPreferencesUtils.setParam(getAppContext(), Constant.SPKey.STAFF, staff.toString());
   }

    @Override
    public void onCreate() {
        super.onCreate();

        if (!BuildConfig.DEBUG) {
            CrashHandler.getInstance().init(this, Constant.CRASH_PATH);
        }
        if (isIsAlone()) {
            mAloneManager = new AloneManager("com.supcon.mes.hongShiCementEam");
            mAloneManager.setup();
            CacheUtil.init(mAloneManager.getHostContext());
        }
        setupDatabase();
        initRouter();
        initUMeng();
        initIP();
        initTvSize();
//        initMenu();
    }

    private void initMenu() {
        List<WorkInfo> workInfoList = WorkHelper.getDefaultWorkList(this);
        EamApplication.dao().getWorkInfoDao().insertOrReplaceInTx(workInfoList);
    }

    private void initTvSize() {
        fontSizeScale = SharedPreferencesUtils.getParam(this, Constant.SPKey.TEXT_SIZE_SETTING, 0.0f);
    }


    private void initUMeng() {


//        PushController pushController = new PushController();
//        pushController.onInit();

    }

    public void initIP(){

        String channel = ChannelUtil.getUMengChannel();
        String port = " ";
        String ip  = SharedPreferencesUtils.getParam(getAppContext(), MBapConstant.SPKey.IP, "");
        LogUtil.d("channel:"+channel+" ip:"+ip);
        if(TextUtils.isEmpty(ip)){

            if (channel.equals("hongshi")) {
                ip = "218.75.97.170";
                port = "8183";
            }
            else if(channel.equals("hailuo")){
                ip = "60.167.69.246";
                port = "8099";
            }
            else if(channel.equals("dev")){
                ip = "192.168.90.49";
                port = "8080";
            }
            else if (channel.equals("134")) {
                ip = "192.168.90.134";
                port = "8080";
            }
            else if(channel.equals("115")){
                ip = "192.168.90.115";
                port = "8080";
            }
            else if(channel.equals("supcon")){
                ip = "10.30.22.227";
                port = "8080";
            }
            else{
                ip = "192.168.90.49";
                port = "8080";
            }
            setIp(ip);
            setPort(port);

            /*String zzIp = SharedPreferencesUtils.getParam(getApplicationContext(), Constant.ZZ.IP, "");
            LogUtil.d("channel:"+channel+" zzip:"+zzIp);
            if(!TextUtils.isEmpty(zzIp)){
                return;
            }
            if (channel.equals("hongshi")) {
                SharedPreferencesUtils.setParam(getApplicationContext(), Constant.ZZ.IP, "zhizhi.hssn.hz.supos.net"*//*"192.168.13.113"*//*);
                SharedPreferencesUtils.setParam(getApplicationContext(), Constant.ZZ.PORT, "8181");
            }
            else if(channel.equals("hailuo")){
                SharedPreferencesUtils.setParam(getApplicationContext(), Constant.ZZ.IP, "60.167.69.246"*//*"192.168.13.113"*//*);
                SharedPreferencesUtils.setParam(getApplicationContext(), Constant.ZZ.PORT, "38043");
            }*/

        }

    }


    public static boolean isDev(){

        String channel = ChannelUtil.getUMengChannel();
        if (channel.equals("dev")) {
            return true;
        }

        return false;
    }

    public static boolean isHongshi(){

        String channel = ChannelUtil.getUMengChannel();
        if (channel.equals("hongshi")) {
            return true;
        }

        return false;
    }

    public static boolean isHailuo(){

        String channel = ChannelUtil.getUMengChannel();
        if (channel.equals("hailuo")) {
            return true;
        }

        return false;
    }

    public static boolean isYNSW(){

        String channel = ChannelUtil.getUMengChannel();
        if (channel.equals("ynsw")) {
            return true;
        }

        return false;
    }


    private void initRouter() {

        MainRouter.getInstance().setup();//初始化所有的IntentRouter

    }

    /**
     * 配置数据库
     */
    private void setupDatabase() {
        //创建数据库equipment.db"
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(isIsAlone() ? mAloneManager.getHostContext() : this, "equipment.db", null);
        CustomDevOpenHelper helper = new CustomDevOpenHelper(isIsAlone() ? mAloneManager.getHostContext() : this/*new DatabaseContext(this,Constant.FILE_PATH)*/, "equipment.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

    public static DaoSession dao() {

        return daoSession;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static boolean offlineLogin(String userName, String pwd) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {
            return false;
        }

        AccountInfoDao accountInfoDao = dao().getAccountInfoDao();
        //先校验是否有同步过用户信息
        List<AccountInfo> userList = accountInfoDao.queryBuilder()
                .where(AccountInfoDao.Properties.UserName.eq(userName))
                .list();
        if (userList.size() == 0) {
//            SnackbarHelper.showError(rootView, "需要在线登录一次");
            return false;
        }

        //如果该用户在线登陆过，校验密码
        List<AccountInfo> list = accountInfoDao.queryBuilder()
                .where(AccountInfoDao.Properties.UserName.eq(userName),
                        AccountInfoDao.Properties.Password.eq(pwd))
                .list();

        if (list.size() == 0) {
//            SnackbarHelper.showError(rootView, "用户名或密码错误");
            return false;
        } else {
            //将用户信息保存到全局变量
            AccountInfo accountInfo = list.get(0);
            setAccountInfo(accountInfo);
            return true;
        }

    }

    private static String zzUrl;

    public static void setZzUrl(String zzUrl) {
        if(!TextUtils.isEmpty(zzUrl)) {
            EamApplication.zzUrl = zzUrl;
            SharedPreferencesUtils.setParam(getAppContext(), Constant.ZZ.URL, zzUrl);
        }

    }

    public static String getZzUrl() {
        if(TextUtils.isEmpty(zzUrl)){
            zzUrl = SharedPreferencesUtils.getParam(getAppContext(), Constant.ZZ.URL, "");
        }
        return zzUrl;
    }


    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = res.getConfiguration();
        if (fontSizeScale > 0.5) {
            config.fontScale = fontSizeScale;
        } else {
            config.fontScale= 1;
        }
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /**
     * 公司id
     * @return
     */
    public static Long getCid(){
        return accountInfo.cid;
    }

    public static void setCompany(Company company){
        SharedPreferencesUtils.setParam(getAppContext(),Constant.SPKey.COMPANY,company == null ? "" : company.toString());
    }
    public static Company getCompany(){
        String companyStr = SharedPreferencesUtils.getParam(getAppContext(),Constant.SPKey.COMPANY,"");
        Company company = GsonUtil.gsonToBean(companyStr,Company.class);
        return company;
    }

    public static void setCompanyCode(Company company){
        SharedPreferencesUtils.setParam(getAppContext(),Constant.SPKey.C_CODE,company == null ? "" : company.code);
    }
    public static String getCompanyCode(){
        return SharedPreferencesUtils.getParam(getAppContext(),Constant.SPKey.C_CODE,"");
    }

    public static String getIpPort(){
        return getIp() + ":" + getPort();
    }
}
