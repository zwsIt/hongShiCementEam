package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * 单据处理意见实体
 */
public class DealInfoEntity extends BaseEntity {
    public String activityName; // 活动名称
    public Long dealTime; // 处理时间
    public String dealAdvice; // 处理意见
    public String linkCode; // 迁移线code(提交或者驳回才有)
    public String dealStaff; // 人员
    public String operateDes; // 操作描述:保存、提交...
    public String assignStaff; // 指定人员
    public String dealType; // 处理类型  普通NORMAL , 驳回REJECT, 转发FORWARD,作废INVALID,撤回RECALL
    public String entrustedStaff; // 委托人员
    public Long createTime; // 待办创建时间
    public String signPath; // 签名路径
    public Integer activityType; // 活动类型 1开始2结束3作废4人工5通知6自由7会签8选择9分发
    public Integer linkType; // 迁移线类型 1.普通、2驳回、3作废、4通知
    public Long DealStaffId; // 处理人id
}
