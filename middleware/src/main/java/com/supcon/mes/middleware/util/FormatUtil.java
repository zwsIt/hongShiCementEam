package com.supcon.mes.middleware.util;

import java.text.DecimalFormat;

/**
 * Created by wangshizhan on 2018/6/11.
 * Email:wangshizhan@supcon.com
 */

public class FormatUtil {

    public static String double2StringMoney(Double input){

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        return input == null?"":decimalFormat.format(input);

    }

    public static String double2String(Double input){

        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        return input == null?"":decimalFormat.format(input);

    }

}
