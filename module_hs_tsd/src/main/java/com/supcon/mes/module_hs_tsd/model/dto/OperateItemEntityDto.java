package com.supcon.mes.module_hs_tsd.model.dto;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.module_hs_tsd.model.bean.ElectricityOffOnEntity;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/10
 * Email zhangwenshuai1@supcon.com
 * Desc 操作事项(停/送电工作票PT)
 */
public class OperateItemEntityDto extends BaseEntity {

    /**
     * caution : 核对工作票内容，检查现场控制按钮钥匙在中控操作位置
     * cautionHeadId : {"id":1042}
     * id : 1448
     * sort : null
     * version : 0
     */

    public String caution; // 安全操作注意事项
//    public ElectricityOffOnEntity cautionHeadId; // 停送电申请表头
    public String id;
    public String sort;
    public String version;
    public String remark;
    public String rowIndex;
}
