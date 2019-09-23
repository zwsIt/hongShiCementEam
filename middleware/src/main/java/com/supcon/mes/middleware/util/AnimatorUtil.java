package com.supcon.mes.middleware.util;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class AnimatorUtil {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void rotationExpandIcon(ImageView expend, float from, float to) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);//属性动画
            valueAnimator.setDuration(500);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(valueAnimator1 -> expend.setRotation((Float) valueAnimator1.getAnimatedValue()));
            valueAnimator.start();
        }
    }

}
