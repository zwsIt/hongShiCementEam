package com.supcon.mes.module_sbda.util;

import com.supcon.mes.module_sbda.R;

import java.util.Random;

/**
 * Created by Xushiyun on 2018/8/14.
 * Email:ciruy_victory@gmail.com
 */
public class RandomColorUtils {
    public final static int[] colors = {R.color.random_red, R.color.random_yellow, R.color.random_green,
    R.color.random_blue, R.color.random_purple, R.color.random_pink};

    public static int getRandomColor() {
        final Random random = new Random();
        final int pos =  random.nextInt(6);
        return colors[pos];
    }
}
