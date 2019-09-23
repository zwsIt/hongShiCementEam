package com.supcon.mes.middleware.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @Author xushiyun
 * @Create-time 7/29/19
 * @Pageage com.supcon.mes.middleware.util
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc 在此工具中进行对于系统方法的调用，例如拨号功能等
 */
public class SystemUtil {
    /**
     * 拨打电话（直接拨打电话）
     * @param phoneNum 电话号码
     */
    public static void callPhoneAuto(Context context,String phoneNum){
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }
    
    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(Context context,String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }
    
    public static void sendSms(Context context, String phone){
        Uri uri = Uri.parse("smsto:"+phone);
        Intent intentMessage = new Intent(Intent.ACTION_VIEW,uri);
        context.startActivity(intentMessage);
    }
}
