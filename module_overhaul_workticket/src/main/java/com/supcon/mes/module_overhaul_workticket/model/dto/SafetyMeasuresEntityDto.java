package com.supcon.mes.module_overhaul_workticket.model.dto;

import com.supcon.common.com_http.BaseEntity;

import java.util.List;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/24
 * Email zhangwenshuai1@supcon.com
 * Desc 检修作业票表单submit实体
 */
public class SafetyMeasuresEntityDto extends BaseEntity {
    //    private WorkTicketEntity headId; // 表头检修作业票
    public String id;
    public String isExecuted; // 是否执行
    public String safetyMeasure; // 安全措施
    public String operateType; // 操作类型:参照枚举类OperateType
    public String sort;
    public String version;
    public String rowIndex;

    //附件
    public List<Long> attachFileMultiFileIds;
    public List<String> attachFileMultiFileNames;
    public List<String> attachFileFileAddPaths; // 存储路径
    public List<Long> attachFileFileDeleteIds; // 附件删除ids
}
