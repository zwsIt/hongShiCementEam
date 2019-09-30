package com.supcon.mes.module_wxgd.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;

/**
 * @description SparePartApplyEntity  备件领用申请实体
 * @author zws 2019/9/27
 */
public class SparePartApplyEntity extends BaseEntity {
    /**
     * applyStaff : {"id":1285,"name":"xtcs001"}
     * applyTime : 1569558000147
     * approvalStatus : {"id":"BEAM2_2016/01","value":"审批"}
     * attrMap : {"BEAM2_1_0_0_sparePart_sparePartTabsListdg1566822775061_LISTPT_ASSO_bf9db180_6725_42e5_a3a6_6c78e78b4b9a":"户外车贴-食堂清洗池标贴","BEAM2_1_0_0_sparePart_sparePartTabsListdg1566822775061_LISTPT_ASSO_87b0a0dd_4a17_429c_9de1_66d35b8f0722":"02101100054"}
     * cid : 1000
     * id : 1074
     * pending : {"id":1348594,"openUrl":null,"taskDescription":"审批","userId":null}
     * repairWork : {"id":2002,"tableNo":"workList_20190926_005"}
     * status : 88
     * tableInfoId : 191300
     * tableNo : sparePart_20190927_002
     * valid : true
     * version : 1
     */

    private Staff applyStaff; // 申请人
    private Long applyTime; // 申请时间
    private SystemCodeEntity approvalStatus; // 申请状态
    private AttrMapBean attrMap; // 备件领用申请明细
    private Long cid;
    private Long id;
    private PendingEntity pending;
    private WXGDEntity repairWork; // 维修工单
    private int status; // 状态
    private int tableInfoId;
    private String tableNo; // 单据编号
    private boolean valid;
    private int version; // 版本

    public Staff getApplyStaff() {
        return applyStaff;
    }

    public void setApplyStaff(Staff applyStaff) {
        this.applyStaff = applyStaff;
    }

    public Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    public SystemCodeEntity getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(SystemCodeEntity approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public AttrMapBean getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(AttrMapBean attrMap) {
        this.attrMap = attrMap;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PendingEntity getPending() {
        return pending;
    }

    public void setPending(PendingEntity pending) {
        this.pending = pending;
    }

    public WXGDEntity getRepairWork() {
        return repairWork;
    }

    public void setRepairWork(WXGDEntity repairWork) {
        this.repairWork = repairWork;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTableInfoId() {
        return tableInfoId;
    }

    public void setTableInfoId(int tableInfoId) {
        this.tableInfoId = tableInfoId;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public static class AttrMapBean {
        /**
         * BEAM2_1_0_0_sparePart_sparePartTabsListdg1566822775061_LISTPT_ASSO_bf9db180_6725_42e5_a3a6_6c78e78b4b9a : 户外车贴-食堂清洗池标贴
         * BEAM2_1_0_0_sparePart_sparePartTabsListdg1566822775061_LISTPT_ASSO_87b0a0dd_4a17_429c_9de1_66d35b8f0722 : 02101100054
         */

        private String BEAM2_1_0_0_sparePart_sparePartTabsListdg1566822775061_LISTPT_ASSO_bf9db180_6725_42e5_a3a6_6c78e78b4b9a;
        private String BEAM2_1_0_0_sparePart_sparePartTabsListdg1566822775061_LISTPT_ASSO_87b0a0dd_4a17_429c_9de1_66d35b8f0722;

        public String getBEAM2_1_0_0_sparePart_sparePartTabsListdg1566822775061_LISTPT_ASSO_bf9db180_6725_42e5_a3a6_6c78e78b4b9a() {
            return BEAM2_1_0_0_sparePart_sparePartTabsListdg1566822775061_LISTPT_ASSO_bf9db180_6725_42e5_a3a6_6c78e78b4b9a;
        }

        public void setBEAM2_1_0_0_sparePart_sparePartTabsListdg1566822775061_LISTPT_ASSO_bf9db180_6725_42e5_a3a6_6c78e78b4b9a(String BEAM2_1_0_0_sparePart_sparePartTabsListdg1566822775061_LISTPT_ASSO_bf9db180_6725_42e5_a3a6_6c78e78b4b9a) {
            this.BEAM2_1_0_0_sparePart_sparePartTabsListdg1566822775061_LISTPT_ASSO_bf9db180_6725_42e5_a3a6_6c78e78b4b9a = BEAM2_1_0_0_sparePart_sparePartTabsListdg1566822775061_LISTPT_ASSO_bf9db180_6725_42e5_a3a6_6c78e78b4b9a;
        }

        public String getBEAM2_1_0_0_sparePart_sparePartTabsListdg1566822775061_LISTPT_ASSO_87b0a0dd_4a17_429c_9de1_66d35b8f0722() {
            return BEAM2_1_0_0_sparePart_sparePartTabsListdg1566822775061_LISTPT_ASSO_87b0a0dd_4a17_429c_9de1_66d35b8f0722;
        }

        public void setBEAM2_1_0_0_sparePart_sparePartTabsListdg1566822775061_LISTPT_ASSO_87b0a0dd_4a17_429c_9de1_66d35b8f0722(String BEAM2_1_0_0_sparePart_sparePartTabsListdg1566822775061_LISTPT_ASSO_87b0a0dd_4a17_429c_9de1_66d35b8f0722) {
            this.BEAM2_1_0_0_sparePart_sparePartTabsListdg1566822775061_LISTPT_ASSO_87b0a0dd_4a17_429c_9de1_66d35b8f0722 = BEAM2_1_0_0_sparePart_sparePartTabsListdg1566822775061_LISTPT_ASSO_87b0a0dd_4a17_429c_9de1_66d35b8f0722;
        }
    }
}
