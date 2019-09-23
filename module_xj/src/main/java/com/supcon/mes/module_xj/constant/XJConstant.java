package com.supcon.mes.module_xj.constant;

/**
 * Constant 巡检模块常量类
 * created by zhangwenshuai1 2018/10/29
 */
public interface XJConstant {

    /**
     * 巡检项-编辑方式
     */
    interface MobileEditType {
        String INPUTE = "mobileEAM054/01"; // 录入
        String RADIO = "mobileEAM054/02"; // 单选
        String CHECKBOX = "mobileEAM054/03"; // 多选
        String WHETHER = "mobileEAM054/04"; // 是否
    }

    /**
     * 巡检项-检查状态
     */
    interface MobileWiLinkState {
        String WAIT_STATE = "wiLinkState/01"; //待检
        String EXEMPTION_STATE = "wiLinkState/02"; //免检
        String SKIP_STATE = "wiLinkState/03"; //跳检
        String FINISHED_STATE = "wiLinkState/04"; //已检
    }

    /**
     * 巡检项-检查状态
     */
    interface MobileConclusion {
        String NORMAL = "realValue/01"; //正常
        String AB_NORMAL = "realValue/02"; //异常
    }


}
