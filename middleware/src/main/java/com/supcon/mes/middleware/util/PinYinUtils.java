package com.supcon.mes.middleware.util;

import android.text.TextUtils;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class PinYinUtils {

    public static String getPinyin(String origin) {
        try {
            return PinyinHelper.convertToPinyinString(origin, "", PinyinFormat.WITHOUT_TONE).toUpperCase(); // ni,hao,shi,jie
        } catch (PinyinException e) {

        }
        return null;
    }

    public static List<Object> getSortedListByPingyin(List list, BiFunction<String, String, Boolean> biFunction) {
        final List result = new ArrayList();
        Collections.sort(list, (o1, o2) -> 0);
        return result;
    }

    private static String getPinyinWithSpace(String origin) {
        try {
            return PinyinHelper.convertToPinyinString(origin.replace(" ", ""), " ", PinyinFormat.WITHOUT_TONE); // ni,hao,shi,jie
        } catch (PinyinException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSQLBlurSearchHeadString(String origin) {
        if (TextUtils.isEmpty(origin)) return "%";
        final StringBuilder stringBuilder = new StringBuilder();
        final String[] array = getPinyinWithSpace(origin.trim()).split(" ");
        for (String anArray : array) {
            stringBuilder.append("%").append(anArray.charAt(0));
        }
        return stringBuilder.append("%").toString();
    }

    public final static Character getHeaderLetter(String origin) {
        if (TextUtils.isEmpty(origin)) {
            return '#';
        }
        char[] chars = getPinyin(origin).trim().toCharArray();
        char letter = chars.length == 0 ? '#' : chars[0];
        letter = isLetter(letter) ? letter : '#';
        return letter;
    }

    //65-90 97-122
    public final static boolean isLetter(Character c) {
        return (c >= 65 && c <= 90) || (c >= 97 && c <= 122);
    }

    /**
     * 解决字符串为null的情况
     *
     * @param s
     * @return
     */
    public final static String reformatStr(String s) {
        return !TextUtils.isEmpty(s) ? s : "";
    }
}