package com.supcon.mes.push.util;

import android.content.Context;

import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.push.UmengNotificationService;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;

import java.lang.reflect.Field;

/**
 * Created by wangshizhan on 2019/4/23
 * Email:wangshizhan@supcom.com
 */
public class PushHelper {

    public static class PushHelperHolder{

        private static PushHelper instance = new PushHelper();

    }

    public static final String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";
//    private Handler handler;
    private Context context;

    public static PushHelper getInstance(){

        return PushHelperHolder.instance;


    }


    private PushHelper(){

    }

    public void init(Context context, String appKey, String pushSecret){
        this.context = context;

        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(false);
        try {
            Class<?> aClass = Class.forName("com.umeng.commonsdk.UMConfigure");
            Field[] fs = aClass.getDeclaredFields();
            for (Field f:fs){
                LogUtil.d("UMLog ff="+f.getName()+"   "+f.getType().getName());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
//        UMConfigure.init(context, appKey, "Umeng", UMConfigure.DEVICE_TYPE_PHONE,
//                pushSecret);
        UMConfigure.init(context, UMConfigure.DEVICE_TYPE_PHONE, pushSecret);
        //PushSDK初始化(如使用推送SDK，必须调用此方法)
        initUpush();
    }

    private void initUpush() {

        PushAgent mPushAgent = PushAgent.getInstance(context);
//        handler = new Handler(context.getMainLooper());

        //sdk开启通知声音
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        // sdk关闭通知声音
        // mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        // 通知声音由服务端控制
        // mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER);

        // mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        // mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);

        /*UmengMessageHandler messageHandler = new UmengMessageHandler() {

            *//**
             * 通知的回调方法（通知送达时会回调）
             *//*
            @Override
            public void dealWithNotificationMessage(Context context, UMessage msg) {
                //调用super，会展示通知，不调用super，则不展示通知。
                super.dealWithNotificationMessage(context, msg);
            }

            *//**
             * 自定义消息的回调方法
             *//*
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if (isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(context.getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(context.getApplicationContext()).trackMsgDismissed(msg);
                        }
                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                    }
                });
            }

            *//**
             * 自定义通知栏样式的回调方法
             *//*
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                switch (msg.builder_id) {
                    case 1:
                        Notification.Builder builder = new Notification.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
                                R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                        myNotificationView.setImageViewResource(R.id.notification_small_icon,
                                getSmallIconId(context, msg));
                        builder.setContent(myNotificationView)
                                .setSmallIcon(getSmallIconId(context, msg))
                                .setTicker(msg.ticker)
                                .setAutoCancel(true);

                        return builder.getNotification();
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        *//**
         * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * *//*
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {

            @Override
            public void launchApp(Context context, UMessage msg) {
                super.launchApp(context, msg);
            }

            @Override
            public void openUrl(Context context, UMessage msg) {
                super.openUrl(context, msg);
            }

            @Override
            public void openActivity(Context context, UMessage msg) {
                super.openActivity(context, msg);
            }

            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }
        };
        //使用自定义的NotificationHandler
        mPushAgent.setNotificationClickHandler(notificationClickHandler);*/

        //注册推送服务 每次调用register都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                LogUtil.d("UMLog device token: " + deviceToken);
//                context.sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
                SharedPreferencesUtils.setParam(context, "DEVICE_TOKEN", deviceToken);
//                EventBus.getDefault().post(new DeviceTokenEvent(deviceToken));
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtil.e("UMLog register failed: " + s + " " + s1);
//                context.sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }
        });
        //使用完全自定义处理
        mPushAgent.setPushIntentServiceClass(UmengNotificationService.class);

        //华为通道
//        HuaWeiRegister.register(this);
    }

}
