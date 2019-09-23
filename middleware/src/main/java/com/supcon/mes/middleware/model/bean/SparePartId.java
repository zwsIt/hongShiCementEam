package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.Good;

public class SparePartId extends BaseEntity {
    public Good productID;
    public Long id;

    public String productCode;
    public String productName;
    public String productModel;
    public String productSpecif;
    public Unit productBaseUnit;

    public Good getProductID() {
        if (productID == null) {
            productID = new Good();
        }
        return productID;
    }
}
