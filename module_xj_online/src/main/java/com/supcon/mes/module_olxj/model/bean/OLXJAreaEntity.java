package com.supcon.mes.module_olxj.model.bean;

import com.google.gson.annotations.Expose;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.model.bean.WXGDEam;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wangshizhan on 2019/4/1
 * Email:wangshizhan@supcom.com
 */
public class OLXJAreaEntity extends BaseEntity {
    /**
     * "_code": "1333",
     * "_parentCode": "-1",
     * "attrMap": null,
     * "cid": 1000,
     * "code": "ZC_SNMVXXFJ_RLXC_9",
     * "device": {
     * "code": null,
     * "id": null
     * },
     * "eamInspectionGuideImageAttachementInfo": "1.jpg",
     * "eamInspectionGuideImageDocument": {
     * "id": 1338
     * },
     * "id": 1333,
     * "isDevice": false,
     * "isParent": false,
     * "isRun": true,
     * "isSign": true,
     * "layNo": 1,
     * "layRec": "1333",
     * "name": "制成9#水泥磨V型选粉机与入磨斜槽",
     * "parentId": -1,
     * "remark": null,
     * "signCode": "F2CECCA0",
     * "sort": 20,
     * "sortStr": "0000000020",
     * "status": null,
     * "tableInfoId": null,
     * "tableNo": null,
     * "valid": true,
     * "version": 3,
     * "workGroupID": {
     * "code": "ZCSX_2",
     * "id": 1004
     * }
     */
    public Long id;       //区域ID
    public String name;   //区域名字
    public String _code;
    public int sort;     //区域顺序
    public WXGDEam device;
    public String eamInspectionGuideImageAttachementInfo;
    public AttachmentEntity eamInspectionGuideImageDocument;
    public String remark;     //备注
    public boolean isDevice;//是否装置
    public boolean isRun;
    public boolean isSign;//需要签到
    public OLXJTaskEntity taskID;       //任务ID

    public String finishType;       // 0 ,1 是否结束
    public String signedTime;       //签到时间
    public String signType;         //签到类型
    public String signReason;       //签到原因
    public String signCode;         //签到编码
    public long staffId;
    public LinkedList<OLXJWorkItemEntity> workItemEntities = new LinkedList<>();


    //    public String faultMsg;//新故障信息拼接  本地拼接的
    @Expose
    public String oldfaultMsg;//旧故障信息拼接
}
