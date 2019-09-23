package com.supcon.mes.module_yhgl.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.module_yhgl.model.dto.GoodDto;

/**
 * SparePartJsonEntity 生成领用户库单参数实体
 * created by zhangwenshuai1 2018/10/24
 */
public class SparePartJsonEntity extends BaseEntity {

    /**
     * id : 1421
     * version : 3
     * productID : {"id":"1008"}
     * checkbox : true
     * sum : 10.00
     * useQuantity :
     * actualQuantity :
     * standingCrop :
     * useState : {"id":"BEAM2011/04"}
     * timesNum : 1
     * sparePartId :
     * rowIndex : 0
     * workList : {"id":"1540"}
     * remark :
     */

    private String id;
    private String version;
    private GoodDto productID;
    private String checkbox;
    private String sum;
    private String useQuantity;
    private String actualQuantity;
    private String standingCrop;
    private SystemCodeEntity useState;
    private String timesNum;
    private String sparePartId;
    private String rowIndex;
    private WorkListBean workList; // 表头工单id
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public GoodDto getProductID() {
        return productID;
    }

    public void setProductID(GoodDto productID) {
        this.productID = productID;
    }

    public String getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(String checkbox) {
        this.checkbox = checkbox;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getUseQuantity() {
        return useQuantity;
    }

    public void setUseQuantity(String useQuantity) {
        this.useQuantity = useQuantity;
    }

    public String getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(String actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public String getStandingCrop() {
        return standingCrop;
    }

    public void setStandingCrop(String standingCrop) {
        this.standingCrop = standingCrop;
    }

    public SystemCodeEntity getUseState() {
        return useState;
    }

    public void setUseState(SystemCodeEntity useState) {
        this.useState = useState;
    }

    public String getTimesNum() {
        return timesNum;
    }

    public void setTimesNum(String timesNum) {
        this.timesNum = timesNum;
    }

    public String getSparePartId() {
        return sparePartId;
    }

    public void setSparePartId(String sparePartId) {
        this.sparePartId = sparePartId;
    }

    public String getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(String rowIndex) {
        this.rowIndex = rowIndex;
    }

    public WorkListBean getWorkList() {
        return workList;
    }

    public void setWorkList(WorkListBean workList) {
        this.workList = workList;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public static class WorkListBean extends BaseEntity {
        /**
         * id : 1540
         */

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
