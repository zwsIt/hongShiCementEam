package com.supcon.mes.module_olxj.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.Team;

/**
 * Created by wangshizhan on 2019/3/29
 * Email:wangshizhan@supcom.com
 */
public class OLXJTaskEntity extends BaseEntity {

    /**
     * "attrMap": null,
     "cid": 1000,
     "endTime": 1553094960000,
     "id": 1153,
     "linkState": {
        "id": "LinkState/02",
        "value": "已下发"
     },
     "pending": {
         "activityName": null,
         "activityType": null,
         "bulkDealFlag": null,
         "deploymentId": null,
         "id": null,
         "openUrl": null,
         "processId": null,
         "processKey": null,
         "processVersion": null,
         "taskDescription": "生效",
         "userId": null
     },
     "resstaffID": {
        "id": 1001,
        "name": "马晓倩"
     },
     "starTime": 1553091360000,
     "status": 99,
     "tableInfoId": 1481,
     "tableNo": "potrolTaskNew_20190320_076",
     "teamID": {
        "id": null,
        "name": null
     },
     "valid": true,
     "valueType": {
        "id": "mobileEAM001/02",
        "value": "巡检"
     },
     "version": 1,
     "workGroupID": {
        "code": "RJY002",
        "id": 1001,
        "name": "软件园流水线二"
     }
     */
    public long cid;
    public long id;

    public SystemCodeEntity linkState;
    public Staff resstaffID;
    public PendingEntity pending;
    public Long starTime;
    public Long endTime;
    public Long tableInfoId;
    public String tableNo;
    public Team teamID;
    public SystemCodeEntity valueType;
    public OLXJGroupEntity workGroupID;

    public String version;
    public boolean valid;


    public long startDelay;  //开始时间延迟量  整数  单位小时

    public long startAdv;  //开始时间提前量  整数  单位小时

    public long endDelay;  //结束时间延迟量  整数  单位小时

    public String state = "待检";  //任务完成状态，默认待检

    public String startTimeActual; //实际开始时间

    public String endTimeActual; //实际结束时间

    public boolean isStart = false; //是否开始标识
}
