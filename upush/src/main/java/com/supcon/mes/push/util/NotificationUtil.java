package com.supcon.mes.push.util;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

/**
 * Created by wangshizhan on 2019/4/23
 * Email:wangshizhan@supcom.com
 */
public class NotificationUtil {
    private static Ringtone mRingtone; // 成员变量，防止app后台杀死后启动后连续推送提示
    public static void playSystemNotaficationSound(Context context){
        if (mRingtone == null){
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mRingtone = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
        }

        if (!mRingtone.isPlaying()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                mRingtone.setVolume(1.0f);
            }
            mRingtone.play();
            playVibrator(context);
        }

    }

    public static void playVibrator(Context context){
        Vibrator vibrator = (Vibrator)context. getSystemService(Context.VIBRATOR_SERVICE);
//        long[] vibrationPattern = new long[]{0, 5000}; // an array of longs of times for which to turn the vibrator on or off
//        vibrator.vibrate(vibrationPattern,2); // 重复2次
        vibrator.vibrate(5000);

    }
}
