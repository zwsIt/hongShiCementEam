package com.supcon.mes.module_olxj.model.bean;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.ValueEntity;
import com.supcon.mes.middleware.model.bean.WXGDEam;

/**
 * Created by wangshizhan on 2019/4/1
 * Email:wangshizhan@supcom.com
 */
public class OLXJWorkItemEntity extends BaseEntity implements Comparable<OLXJWorkItemEntity> {

    /**
     * "autoAanalysis": false,
     * "autoGetValue": false,
     * "autoJudge": true,
     * "claim": null,
     * "content": "无漏灰",
     * "control": true,
     * "defaultVal": "正常",
     * "eamID": {
     * "code": "BEAM_TZ_002",
     * "id": 1005,
     * "name": "8#磨1#废石秤"
     * },
     * "id": 36176,
     * "inputStandardID": {
     * "decimalPlace": null,
     * "editTypeMoblie": {
     * "id": "mobileEAM054/02",
     * "value": "单选"
     * },
     * "id": 1011,
     * "name": "是否正常",
     * "standardCode": "BZ006",
     * "unitID": null,
     * "valueName": "正常,不正常",
     * "valueTypeMoblie": {
     * "id": "mobileEAM055/01",
     * "value": "字符"
     * }
     * },
     * "isSeismic": false,
     * "isThermometric": false,
     * "ispass": true,
     * "isphone": false,
     * "limitValue": null,
     * "llimitValue": null,
     * "normalRange": "正常",
     * "part": "斜槽管道",
     * "pstaffid": null,
     * "publicItemID": null,
     * "remark": null,
     * "signWorkID": {
     * "id": 1354,
     * "name":"区域名称"
     * },
     * "sort": 1,
     * "taskID": {
     * "id": 1190
     * },
     * "taskSignID": null,
     * "version": 1,
     * "workID": {
     * "id": 1354,
     * "name":"区域名称"
     * },
     * "workItemID": {
     * "id": 1967,
     * "remark": null
     * }
     */

    public long id;
    public boolean autoAanalysis;
    public boolean autoGetValue;
    public boolean autoJudge;
    public String claim;  //标准
    public String content;//内容
    public boolean control; //是否重录
    public String defaultVal;
    public WXGDEam eamID;

    public OLXJInputStandard inputStandardID;

    public boolean isSeismic;
    public boolean isThermometric;
    public boolean ispass;//跳检
    public boolean isphone;//拍照
    public String limitValue;
    public String llimitValue;
    public String normalRange;//正常值

    public String part;//部位
    public Long pstaffid;
    public Object publicItemID;
    public OLXJArea signWorkID;

    public OLXJArea workID;
    public OLXJWorkItem workItemID;
    public OLXJTaskEntity taskID;

    public String remark;

    public String concluse;
    public long concluseTime;
    public String result;   //结果
    public String conclusionID;  //结论ID
    public String conclusionName;  //结论名称
    public String realRemark;  //备注
    public String endTime;  //结束时间
    public String skipReasonID;//跳过原因ID
    public String skipReasonName; //跳过原因名称
    public String linkState = "wiLinkState/01"; //状态,默认待检
    public boolean isFinished = false;
    public Long staffId;
    public boolean isPhonere;//实际是否拍照
    public boolean realispass;
    public String xjImgUrl; //图片路径，逗号相隔
    public String realValue;
    public String itemnumber;//逻辑位号

    public int sort;//排序
    private int prioritySort;

    public ValueEntity priority;//优先级

    public long tableInfoId;
    @Expose
    public String title;
    @Expose
    public int viewType = 0;
    @Expose
    public String headerPicPath;

    @Override
    public int compareTo(@NonNull OLXJWorkItemEntity o) {

//        if (this.eamID == null || o.eamID == null) {
//            return 0;
//        }
//
//        if (this.eamID.id.equals(o.eamID.id) && this.part != null) {
//            if (o.part == null) return 1;
//            return this.part.compareTo(o.part);
//        }
//
//        return (int) (this.eamID.id - o.eamID.id);
        if (this.getPrioritySort() == o.getPrioritySort()) {
            return this.sort - o.sort;
        } else {
            return o.getPrioritySort() - this.getPrioritySort();
        }
    }

    public int getPrioritySort() {
        if (priority == null) {
            prioritySort = 0;
        } else {
            if (priority.id.equals("mobileEAM_056/01")) {
                prioritySort = 1;
            } else if (priority.id.equals("mobileEAM_056/02")) {
                prioritySort = 0;
            }
        }
        return prioritySort;
    }
}
