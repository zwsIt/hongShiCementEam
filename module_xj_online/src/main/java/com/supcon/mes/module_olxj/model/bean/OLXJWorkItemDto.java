package com.supcon.mes.module_olxj.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.IDEntity;
import com.supcon.mes.middleware.model.bean.WXGDEam;

/**
 * Created by wangshizhan on 2019/4/1
 * Email:wangshizhan@supcom.com
 */
public class OLXJWorkItemDto extends BaseEntity {

    public String id;
    public String autoAanalysis;
    public String autoGetValue;
    public String autoJudge;
    public String claim;  //标准
    public String content;//内容
    public String control; //是否重录
    public String defaultVal;
    public IDEntity eamID;

    public IDEntity inputStandardID;

    public String isSeismic;
    public String isThermometric;
    public String ispass;//跳检
    public String isphone;//拍照
    public String limitValue;
    public String llimitValue;
    public String normalRange;//正常值

    public String part;//部位
    public IDEntity publicItemID;

    public IDEntity workID;
    public IDEntity workItemID;

}
