package com.supcon.mes.middleware.model.bean;

import android.util.Log;

import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.ui.view.CustomMultiStageView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

public class AreaMultiStageEntity implements CustomMultiStageEntity<DepartmentInfo>, Cloneable {
    public CustomMultiStageEntity<DepartmentInfo> fatherNode;
    public DepartmentInfo currentEntity;
    public List<CustomMultiStageEntity<DepartmentInfo>> childNodeList = new ArrayList<>();
    public Boolean isExpanded = false;

    public AreaMultiStageEntity setCurrentEntity(DepartmentInfo currentEntity) {
        this.currentEntity = currentEntity;
        return this;
    }

    public AreaMultiStageEntity setFatherNode(CustomMultiStageEntity<DepartmentInfo> fatherNode) {
        this.fatherNode = fatherNode;
        return this;
    }

    public AreaMultiStageEntity setChildNodeList(List<CustomMultiStageEntity<DepartmentInfo>> childNodeList) {
        this.childNodeList = childNodeList;
        return this;
    }

    @Override
    public Boolean isLeafNode() {
        if (fatherNode() == null) {
            return false;
        }
        return getInfo().equals(fatherNode().getInfo());
    }

    @Override
    public Boolean isRootEntity() {
        return currentEntity.id == null;
    }

    @Override
    public Boolean isExpanded() {
        return isExpanded;
    }

    @Override
    public String getInfo() {
        return currentEntity.layRec;
    }

    @Override
    public void setInfo(String info) {
        currentEntity.layRec = info;
    }

    @Override
    public void changeExpandStatus() {
        isExpanded = !isExpanded;
    }

    @Override
    public void setExpanded(Boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    @Override
    public CustomMultiStageEntity<DepartmentInfo> fatherNode() {
        return fatherNode;
    }

    @Override
    public DepartmentInfo getCurrentEntity() {
        return currentEntity;
    }

    @Override
    public Integer getChildListSize() {
        if (childNodeList == null || childNodeList.size() == 0) {
            return 0;
        }
        return childNodeList.size();
    }


    @Override
    public Flowable<List<CustomMultiStageEntity<DepartmentInfo>>> getChildNodeList() {
        return Flowable.just(childNodeList)
                .doOnNext(customMultiStageEntities -> {
                    if (customMultiStageEntities == null) {
                        customMultiStageEntities = new ArrayList<>();
                    }
                    if (customMultiStageEntities.size() == 0) {
                        //Todo: For test,
                        List<CustomMultiStageEntity<DepartmentInfo>> finalCustomMultiStageEntities = customMultiStageEntities;
//                        Log.e("ciruy", getCurrentEntity().fullPathName + "\n"
//                                + EamApplication.dao()
//                                .getTxlEntityDao().queryBuilder()
//                                .where(TxlEntityDao.Properties.FULLPATHNAME.eq(getCurrentEntity().fullPathName))
//                                .list().toString());
                    }
                });


    }

    @Override
    public AreaMultiStageEntity clone() throws CloneNotSupportedException {
        AreaMultiStageEntity areaMultiStageEntity = (AreaMultiStageEntity) super.clone();
        areaMultiStageEntity.setChildNodeList(new ArrayList<>());
        return areaMultiStageEntity;
    }

    @Override
    public List<CustomMultiStageEntity<DepartmentInfo>> getActualChildNodeList() {
        return childNodeList;
    }


}
