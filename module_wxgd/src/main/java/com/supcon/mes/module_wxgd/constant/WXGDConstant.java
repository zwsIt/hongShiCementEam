package com.supcon.mes.module_wxgd.constant;

/**
 * WXGDConstant
 * created by zhangwenshuai1 2018/11/5
 */
public interface WXGDConstant {
    /**
     * 优先级
     */
    interface Priority{
        String emergency =  "BEAM2007/01"; // 紧急
        String priority =  "BEAM2007/02"; // 优先
    }

    interface URL{
        String PRE_URL =  "/BEAM2/workList/workRecord/";
    }
    /**
     * 是否停电
     */
    interface EleOff {
        String yes =  "BEAM2_019/01"; // 是
        String no =  "BEAM2_019/02"; // 否
    }
    interface HeaderData{
        String HEADER_DATA_INCLUDES = "faultInfo.id,faultInfo.tableInfoId";
    }

}
