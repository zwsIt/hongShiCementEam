package com.supcon.mes.middleware.model.bean;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by wangshizhan on 2019/12/11
 * Email:wangshizhan@supcom.com
 */
public interface ICustomTreeView<Data> {

    Boolean isLeafNode();

    Boolean isRootEntity();

    Boolean isExpanded();

    String getInfo();

    void setInfo(String info);

    String getFullPathName();

    void setFullPathName(String fullPathName);

    void changeExpandStatus();

    void setExpanded(Boolean isExpanded);

    ICustomTreeView<Data> fatherNode();

    Data getCurrentEntity();

    Integer getChildListSize();

    Flowable<List<ICustomTreeView<Data>>> getChildNodeList();

    List<ICustomTreeView<Data>> getActualChildNodeList();

    Integer getLayNo();

}
