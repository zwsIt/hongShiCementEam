package com.supcon.mes.push.util;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

/**
 * Created by wangshizhan on 2019/4/23
 * Email:wangshizhan@supcom.com
 */
public class NotificationUtil {

    public static void playSystemNotaficationSound(Context context){
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
        r.play();
    }

    public static void playVibrator(Context context){
        Vibrator vibrator = (Vibrator)context. getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);

    }
}
