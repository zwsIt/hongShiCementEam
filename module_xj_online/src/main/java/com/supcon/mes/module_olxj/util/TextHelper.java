package com.supcon.mes.module_olxj.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangshizhan on 2019/3/29
 * Email:wangshizhan@supcom.com
 */
public class TextHelper {


    public static String value(String s){

        return TextUtils.isEmpty(s)?"":s;

    }

    /**
     * 判断是否是number
     */
    public static boolean isNumber(String str){
        Pattern pattern = Pattern.compile("^-?\\d+\\.?\\d+$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


}
