package com.supcon.mes.module_sbda.util;

import android.support.annotation.NonNull;

/**
 * Created by Xushiyun on 2018/6/27.
 * Email:ciruy_victory@gmail.com
 */
public final class Preconditions {
    @SuppressWarnings("ConstantConditions")
    public static @NonNull <T> T checkNotNull(@NonNull final T object) {
        if (object == null) {
            throw new NullPointerException();
        }
        return object;
    }

    private Preconditions() {
    }
}
