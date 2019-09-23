package com.supcon.mes.middleware.model.bean;

import com.supcon.mes.mbap.beans.FilterBean;

import io.reactivex.annotations.Nullable;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class ScreenEntity extends FilterBean {

    public String layRec;

    public String code;

    public Long id;
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Nullable
    @Override
    public boolean equals(Object obj) {
        FilterBean filterBean = (FilterBean) obj;
        if (this.name.trim().equals(filterBean.name.trim()) && this.type == filterBean.type) {
            return true;
        }
        return false;
    }
}
