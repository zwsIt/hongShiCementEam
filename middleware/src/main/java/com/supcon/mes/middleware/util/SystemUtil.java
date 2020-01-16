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

    public static void sendEmail(Context context, String email){
        String[] emails = {email}; // 需要注意，email必须以数组形式传入
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822"); // 设置邮件格式
        intent.putExtra(Intent.EXTRA_EMAIL, email); // 接收人
        intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
        intent.putExtra(Intent.EXTRA_SUBJECT, "这是邮件的主题部分"); // 主题
        intent.putExtra(Intent.EXTRA_TEXT, "这是邮件的正文部分"); // 正文
        context.startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
    }

    public static void openBrowser(Context context, String url){
//        Intent intent = new Intent();
//        intent.setData(Uri.parse(url));//Url 就是你要打开的网址
//        intent.setAction(Intent.ACTION_VIEW);
//        context.startActivity(intent); //启动浏览器
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }
}
