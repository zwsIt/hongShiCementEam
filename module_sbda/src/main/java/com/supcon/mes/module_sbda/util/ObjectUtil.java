package com.supcon.mes.module_sbda.util;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.function.Function;

/**
 * Created by Xushiyun on 2018/7/4.
 * Email:ciruy_victory@gmail.com
 */
public class ObjectUtil {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static final <P, R> void commonOperation(Function<P, R> function, P... objs) {
        for (P obj : objs) {
            function.apply(obj);
        }
    }
}
