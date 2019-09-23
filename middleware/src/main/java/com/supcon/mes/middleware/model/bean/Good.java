package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.constant.Constant;

import java.math.BigDecimal;

/**
 * Created by wangshizhan on 2018/7/12
 * Email:wangshizhan@supcom.com
 */
public class Good extends BaseEntity {

    public Long id;
    public SystemCodeEntity batch;
    public Long cid;
    public String productCode;
    public String productName;
    public String productModel;
    public String productSpecif;
    public Unit productBaseUnit;
    public float pieceNum;
    public BigDecimal productCostPrice; // 参考采购价

    public Unit getProductBaseUnit() {
        if (productBaseUnit == null) {
            productBaseUnit = new Unit();
        }
        return productBaseUnit;
    }
}
