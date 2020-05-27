package com.supcon.mes.middleware.model.bean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by wangshizhan on 2019/12/11
 * Email:wangshizhan@supcom.com
 * 设备区域展示View
 */
public class EamAreaTreeViewEntity implements ICustomTreeView<Area>, Cloneable {
    private ICustomTreeView<Area> fatherNode;
    private Area mArea;
    private List<ICustomTreeView<Area>> childNodeList = new ArrayList<>();
    private Boolean isExpanded = false;

    public EamAreaTreeViewEntity setCurrentEntity(Area currentEntity) {
        this.mArea = currentEntity;
        return this;
    }

    public EamAreaTreeViewEntity setFatherNode(ICustomTreeView<Area> fatherNode) {
        this.fatherNode = fatherNode;
        return this;
    }

    public EamAreaTreeViewEntity setChildNodeList(List<ICustomTreeView<Area>> childNodeList) {
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
        return mArea.id == null;
    }

    @Override
    public Boolean isExpanded() {
        return isExpanded;
    }

    @Override
    public String getInfo() {
        return mArea.layRec;
    }

    @Override
    public void setInfo(String info) {
        mArea.layRec = info;
    }

    @Override
    public String getFullPathName() {
        return mArea.fullPathName;
    }

    @Override
    public void setFullPathName(String fullPathName) {
        mArea.fullPathName = fullPathName;
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
    public ICustomTreeView<Area> fatherNode() {
        return fatherNode;
    }

    @Override
    public Area getCurrentEntity() {
        return mArea;
    }

    @Override
    public Integer getChildListSize() {
        if (childNodeList == null || childNodeList.size() == 0) return 0;
        return childNodeList.size();
    }


    @Override
    public Flowable<List<ICustomTreeView<Area>>> getChildNodeList() {
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
    public EamAreaTreeViewEntity clone() throws CloneNotSupportedException {
        EamAreaTreeViewEntity eamAreaTreeViewEntity = (EamAreaTreeViewEntity) super.clone();
        eamAreaTreeViewEntity.setChildNodeList(new ArrayList<>());
        return eamAreaTreeViewEntity;
    }

    @Override
    public List<ICustomTreeView<Area>> getActualChildNodeList() {
        return childNodeList;
    }

    @Override
    public Integer getLayNo() {
        return mArea.layNo;
    }


}