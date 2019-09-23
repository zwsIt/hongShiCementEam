package com.supcon.mes.module_sbda.util;

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

    public final static String getPinyin(String origin) throws PinyinException {
        return PinyinHelper.convertToPinyinString(origin, "", PinyinFormat.WITHOUT_TONE); // ni,hao,shi,jie
    }

    public final static List<Object> getSortedListByPingyin(List list, BiFunction<String, String, Boolean> biFunction) {
        final List result = new ArrayList();
        Collections.sort(list, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return 0;
            }
        });
        return result;
    }

    public final static String getPinyinWithSpace(String origin) throws PinyinException {
        return PinyinHelper.convertToPinyinString(origin.replace(" ", ""), " ", PinyinFormat.WITHOUT_TONE); // ni,hao,shi,jie
    }

    public final static String getSQLBlurSearchHeadString(String origin) throws PinyinException {
        if(TextUtils.isEmpty(origin)) return "%";
        final StringBuilder stringBuilder = new StringBuilder();
        final String[] array = getPinyinWithSpace(origin.trim()).split(" ");
        for (int i = 0; i < array.length; i++) {
            stringBuilder.append("%" + array[i].charAt(0));
        }
        return stringBuilder.append("%").toString();
    }

    public final static Character getHeaderLetter(String origin) throws PinyinException {
        return Character.toUpperCase(getPinyin(origin).trim().toCharArray()[0]);
    }

}