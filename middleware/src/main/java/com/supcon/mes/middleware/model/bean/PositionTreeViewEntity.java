package com.supcon.mes.middleware.model.bean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by wangshizhan on 2019/12/11
 * Email:wangshizhan@supcom.com
 */
public class PositionTreeViewEntity implements ICustomTreeView<PositionEntity>, Cloneable {
    private ICustomTreeView<PositionEntity> fatherNode;
    private PositionEntity positionEntity;
    private List<ICustomTreeView<PositionEntity>> childNodeList = new ArrayList<>();
    private Boolean isExpanded = false;

    public PositionTreeViewEntity setCurrentEntity(PositionEntity currentEntity) {
        this.positionEntity = currentEntity;
        return this;
    }

    public PositionTreeViewEntity setFatherNode(ICustomTreeView<PositionEntity> fatherNode) {
        this.fatherNode = fatherNode;
        return this;
    }

    public PositionTreeViewEntity setChildNodeList(List<ICustomTreeView<PositionEntity>> childNodeList) {
        this.childNodeList = childNodeList;
        return this;
    }

    @Override
    public Boolean isLeafNode() {
        if (fatherNode() == null) return false;
        return getInfo().equals(fatherNode().getInfo());
    }

    @Override
    public Boolean isRootEntity() {
        return positionEntity.id == null;
    }

    @Override
    public Boolean isExpanded() {
        return isExpanded;
    }

    @Override
    public String getInfo() {
        return positionEntity.layRec;
    }

    @Override
    public void setInfo(String info) {
        positionEntity.layRec = info;
    }

    @Override
    public String getFullPathName() {
        return positionEntity.fullPathName;
    }

    @Override
    public void setFullPathName(String fullPathName) {
        positionEntity.fullPathName = fullPathName;
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
    public ICustomTreeView<PositionEntity> fatherNode() {
        return fatherNode;
    }

    @Override
    public PositionEntity getCurrentEntity() {
        return positionEntity;
    }

    @Override
    public Integer getChildListSize() {
        if (childNodeList == null || childNodeList.size() == 0) return 0;
        return childNodeList.size();
    }


    @Override
    public Flowable<List<ICustomTreeView<PositionEntity>>> getChildNodeList() {
        return Flowable.just(childNodeList)
                .doOnNext(customMultiStageEntities -> {
                    if (customMultiStageEntities == null)
                        customMultiStageEntities = new ArrayList<>();
                    if (customMultiStageEntities.size() == 0) {
                    }
                });


    }

    @Override
    public PositionTreeViewEntity clone() throws CloneNotSupportedException {
        PositionTreeViewEntity DepartmentTreeViewEntity = (PositionTreeViewEntity) super.clone();
        DepartmentTreeViewEntity.setChildNodeList(new ArrayList<>());
        return DepartmentTreeViewEntity;
    }

    @Override
    public List<ICustomTreeView<PositionEntity>> getActualChildNodeList() {
        return childNodeList;
    }


}