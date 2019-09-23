package com.supcon.mes.module_login.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author yangfei.cao
 * @ClassName inspection
 * @date 2018/4/24
 * ------------- Description -------------
 */
public class PushService extends Service {

    public PushService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Flowable.interval(0, 5, TimeUnit.SECONDS)
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mLong -> {
//                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                    // 在API11之后构建Notification的方式
//                    Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
//                    Intent nfIntent = new Intent(this, MainActivity.class);
//                    builder.setContentIntent(PendingIntent.
//                            getActivity(this, 0, nfIntent, PendingIntent.FLAG_UPDATE_CURRENT)) // 设置PendingIntent
//                            .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
//                                    R.mipmap.ic_app_launcher)) // 设置下拉列表中的图标(大图标)
//                            .setContentTitle("推送的title") // 设置下拉列表里的标题
//                            .setSmallIcon(R.mipmap.ic_app_launcher) // 设置状态栏内的小图标
//                            .setContentText("推送的内容") // 设置上下文内容
//                            .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
//
//                    Notification notification = builder.build(); // 获取构建好的Notification
//                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
////                    notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
//                    manager.notify(1, notification);
                });

//        // 参数一：唯一的通知标识；参数二：通知消息。
//        startForeground(startId, notification);// 开始前台服务
        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static void startPush(Context context) {
        Intent intent = new Intent(context, PushService.class);
        context.startService(intent);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                startPush(context);
            }
        }
    };

}
