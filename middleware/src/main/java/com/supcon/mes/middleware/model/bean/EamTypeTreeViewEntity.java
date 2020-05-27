package com.supcon.mes.middleware.model.bean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by wangshizhan on 2019/12/11
 * Email:wangshizhan@supcom.com
 * 设备区域展示View
 */
public class EamTypeTreeViewEntity implements ICustomTreeView<EamType>, Cloneable {
    private ICustomTreeView<EamType> fatherNode;
    private EamType mEamType;
    private List<ICustomTreeView<EamType>> childNodeList = new ArrayList<>();
    private Boolean isExpanded = false;

    public EamTypeTreeViewEntity setCurrentEntity(EamType currentEntity) {
        this.mEamType = currentEntity;
        return this;
    }

    public EamTypeTreeViewEntity setFatherNode(ICustomTreeView<EamType> fatherNode) {
        this.fatherNode = fatherNode;
        return this;
    }

    public EamTypeTreeViewEntity setChildNodeList(List<ICustomTreeView<EamType>> childNodeList) {
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
        return mEamType.id == null;
    }

    @Override
    public Boolean isExpanded() {
        return isExpanded;
    }

    @Override
    public String getInfo() {
        return mEamType.layRec;
    }

    @Override
    public void setInfo(String info) {
        mEamType.layRec = info;
    }

    @Override
    public String getFullPathName() {
        return mEamType.fullPathName;
    }

    @Override
    public void setFullPathName(String fullPathName) {
        mEamType.fullPathName = fullPathName;
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
    public ICustomTreeView<EamType> fatherNode() {
        return fatherNode;
    }

    @Override
    public EamType getCurrentEntity() {
        return mEamType;
    }

    @Override
    public Integer getChildListSize() {
        if (childNodeList == null || childNodeList.size() == 0) return 0;
        return childNodeList.size();
    }


    @Override
    public Flowable<List<ICustomTreeView<EamType>>> getChildNodeList() {
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
    public EamTypeTreeViewEntity clone() throws CloneNotSupportedException {
        EamTypeTreeViewEntity eamTypeTreeViewEntity = (EamTypeTreeViewEntity) super.clone();
        eamTypeTreeViewEntity.setChildNodeList(new ArrayList<>());
        return eamTypeTreeViewEntity;
    }

    @Override
    public List<ICustomTreeView<EamType>> getActualChildNodeList() {
        return childNodeList;
    }

    @Override
    public Integer getLayNo() {
        return mEamType.layNo;
    }


}