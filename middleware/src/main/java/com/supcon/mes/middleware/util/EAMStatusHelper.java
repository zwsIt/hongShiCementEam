package com.supcon.mes.middleware.util;

/**
 * Created by Xushiyun on 2018/6/7.
 * Email:ciruy_victory@gmail.com
 */

public class EAMStatusHelper {
    public static final String getType(String type) {
        String result = "在用";
        switch (type) {
//            case "01":
//                result = "在用";
//                break;
            case "02":
                result = "禁用";
                break;
            case "03":
                result = "封存";
                break;
            case "04":
                result = "报废";
                break;
        }
        return result;
    }
    public static final String getTypeNum(String type) {
        String result = "01";
        switch (type) {
//            case "在用":
//                result = "01";
//                break;
            case "禁用":
                result = "02";
                break;
            case "封存":
                result = "03";
                break;
            case "报废":
                result = "04";
                break;
        }
        return result;
    }
}
