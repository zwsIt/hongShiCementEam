package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2018/8/17
 * Email:wangshizhan@supcom.com
 */
public class AttachmentEntity extends BaseEntity {

    public long id;
    public long linkId;
    public long mainModelId;//若为单据表体，与linkId相同
    public long deploymentId;
    public String fileType; //attachment 普通附件， pic 图片文件
    public String propertyCode;
    public String path;
    public String type;
    public long size;
    public String name;
}
