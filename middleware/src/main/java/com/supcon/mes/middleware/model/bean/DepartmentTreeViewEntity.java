package com.supcon.mes.middleware.model.bean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by wangshizhan on 2019/12/11
 * Email:wangshizhan@supcom.com
 */
public class DepartmentTreeViewEntity implements ICustomTreeView<DepartmentInfo>, Cloneable {
    private ICustomTreeView<DepartmentInfo> fatherNode;
    private DepartmentInfo departmentInfo;
    private List<ICustomTreeView<DepartmentInfo>> childNodeList = new ArrayList<>();
    private Boolean isExpanded = false;

    public DepartmentTreeViewEntity setCurrentEntity(DepartmentInfo currentEntity) {
        this.departmentInfo = currentEntity;
        return this;
    }

    public DepartmentTreeViewEntity setFatherNode(ICustomTreeView<DepartmentInfo> fatherNode) {
        this.fatherNode = fatherNode;
        return this;
    }

    public DepartmentTreeViewEntity setChildNodeList(List<ICustomTreeView<DepartmentInfo>> childNodeList) {
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
        return departmentInfo.id == null;
    }

    @Override
    public Boolean isExpanded() {
        return isExpanded;
    }

    @Override
    public String getInfo() {
        return departmentInfo.layRec;
    }

    @Override
    public void setInfo(String info) {
        departmentInfo.layRec = info;
    }

    @Override
    public String getFullPathName() {
        return departmentInfo.fullPathName;
    }

    @Override
    public void setFullPathName(String fullPathName) {
        departmentInfo.fullPathName = fullPathName;
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
    public ICustomTreeView<DepartmentInfo> fatherNode() {
        return fatherNode;
    }

    @Override
    public DepartmentInfo getCurrentEntity() {
        return departmentInfo;
    }

    @Override
    public Integer getChildListSize() {
        if (childNodeList == null || childNodeList.size() == 0) return 0;
        return childNodeList.size();
    }


    @Override
    public Flowable<List<ICustomTreeView<DepartmentInfo>>> getChildNodeList() {
        return Flowable.just(childNodeList)
                .doOnNext(customMultiStageEntities -> {
                    if (customMultiStageEntities == null)
                        customMultiStageEntities = new ArrayList<>();
                    if (customMultiStageEntities.size() == 0) {
                        //Todo: For test,
//                            List<ICustomTreeView<DepartmentInfo>> finalCustomMultiStageEntities = customMultiStageEntities;
//                            Log.e("ciruy", getCurrentEntity().fullPathName + "\n"
//                                    + SupPlantApplication.dao()
//                                    .getTxlEntityDao().queryBuilder()
//                                    .where(TxlEntityDao.Properties.FULLPATHNAME.eq(getCurrentEntity().fullPathName))
//                                    .list().toString());
                    }
                });


    }

    @Override
    public DepartmentTreeViewEntity clone() throws CloneNotSupportedException {
        DepartmentTreeViewEntity DepartmentTreeViewEntity = (DepartmentTreeViewEntity) super.clone();
        DepartmentTreeViewEntity.setChildNodeList(new ArrayList<>());
        return DepartmentTreeViewEntity;
    }

    @Override
    public List<ICustomTreeView<DepartmentInfo>> getActualChildNodeList() {
        return childNodeList;
    }


}