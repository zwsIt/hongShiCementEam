package com.supcon.mes.middleware.model.bean;

import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.ResultEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by wangshizhan on 2017/11/17.
 * Email:wangshizhan@supcon.com
 */

@Entity
public class YXJLEntity extends ResultEntity {

    @Id(autoincrement = true)
    public Long objectId;

    public long id ;

    public String createStaff = MBapApp.getUserName();

    public long createTime = System.currentTimeMillis();

    public String tableNo ;

    public String remark;

    public String router = Constant.Router.YXJL_EDIT;

    public String tableStatus = "草稿";

    public Boolean recallAble; //是否允许撤回
    public Boolean allowProxyAble;   //是否允许委托
    public int dealSet; //处理意见 0可空 / 1必填 2禁填

    public long pendingId;

    @Generated(hash = 2049441653)
    public YXJLEntity(Long objectId, long id, String createStaff, long createTime,
                      String tableNo, String remark, String router, String tableStatus,
                      Boolean recallAble, Boolean allowProxyAble, int dealSet,
                      long pendingId) {
        this.objectId = objectId;
        this.id = id;
        this.createStaff = createStaff;
        this.createTime = createTime;
        this.tableNo = tableNo;
        this.remark = remark;
        this.router = router;
        this.tableStatus = tableStatus;
        this.recallAble = recallAble;
        this.allowProxyAble = allowProxyAble;
        this.dealSet = dealSet;
        this.pendingId = pendingId;
    }

    @Generated(hash = 937722786)
    public YXJLEntity() {
    }

    public Long getObjectId() {
        return this.objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreateStaff() {
        return this.createStaff;
    }

    public void setCreateStaff(String createStaff) {
        this.createStaff = createStaff;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getTableNo() {
        return this.tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRouter() {
        return this.router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public String getTableStatus() {
        return this.tableStatus;
    }

    public void setTableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }

    public Boolean getRecallAble() {
        return this.recallAble;
    }

    public void setRecallAble(Boolean recallAble) {
        this.recallAble = recallAble;
    }

    public Boolean getAllowProxyAble() {
        return this.allowProxyAble;
    }

    public void setAllowProxyAble(Boolean allowProxyAble) {
        this.allowProxyAble = allowProxyAble;
    }

    public int getDealSet() {
        return this.dealSet;
    }

    public void setDealSet(int dealSet) {
        this.dealSet = dealSet;
    }

    public long getPendingId() {
        return this.pendingId;
    }

    public void setPendingId(long pendingId) {
        this.pendingId = pendingId;
    }

}
