package com.supcon.mes.middleware.model.bean;

import com.supcon.mes.middleware.ui.view.CustomMultiStageView;

import java.util.List;

import io.reactivex.Flowable;

public interface CustomMultiStageEntity<Data> {

    Boolean isLeafNode();

    Boolean isRootEntity();

    Boolean isExpanded();

    String getInfo();

    void setInfo(String info);

    void changeExpandStatus();

    void setExpanded(Boolean isExpanded);

    CustomMultiStageEntity<Data> fatherNode();

    Data getCurrentEntity();

    Integer getChildListSize();

    Flowable<List<CustomMultiStageEntity<Data>>> getChildNodeList();

    List<CustomMultiStageEntity<Data>> getActualChildNodeList();

}
