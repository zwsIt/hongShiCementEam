package com.supcon.mes.module_xj.model.bean;

import com.supcon.mes.mbap.beans.SheetEntity;

/**
 * Created by wangshizhan on 2018/5/17.
 * Email:wangshizhan@supcon.com
 */

public class XJHistorySheetEntity extends SheetEntity {

    public long id;
    public String content;//内容
    public String result; // 结果
    public String conclusion; //结论
    public String dateTime; //日期时间
    public Long eamId; //设备id
    public String eamName; //设备名称
    public String linkStateId;  //明细状态id
}
