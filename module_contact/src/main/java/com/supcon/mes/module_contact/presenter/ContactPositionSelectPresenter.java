package com.supcon.mes.module_contact.presenter;

import android.annotation.SuppressLint;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.ContactEntityDao;
import com.supcon.mes.middleware.model.bean.DepartmentInfo;
import com.supcon.mes.middleware.model.bean.DepartmentInfoDao;
import com.supcon.mes.middleware.model.bean.ICustomTreeView;
import com.supcon.mes.middleware.model.bean.PositionEntity;
import com.supcon.mes.middleware.model.bean.PositionEntityDao;
import com.supcon.mes.middleware.model.bean.PositionTreeViewEntity;
import com.supcon.mes.module_contact.model.contract.ContactPositionSelectContract;

import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * @Author xushiyun
 * @Create-time 7/22/19
 * @Pageage com.supcon.mes.middleware.presenter
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public class ContactPositionSelectPresenter extends ContactPositionSelectContract.Presenter {

    private PositionTreeViewEntity rootMultiStageEntity;

    @SuppressLint("CheckResult")
    @Override
    public void getPositionInfoList(String id) {
        LogUtil.i("" + id);
        List<PositionEntity> list = EamApplication.dao().getPositionEntityDao().queryBuilder()
//                .where(DepartmentInfoDao.Properties.LayRec.like("?" + id + "?"))
                .orderAsc(PositionEntityDao.Properties.LayRec).list();
        Collections.sort(list);

        rootMultiStageEntity = new PositionTreeViewEntity();
        PositionEntity rootArea = new PositionEntity();
        rootArea.layRec = "";
        rootArea.fullPathName = "";
        rootArea.name = EamApplication.getAccountInfo().companyName;
        rootArea.cid = EamApplication.getAccountInfo().cid;
        rootMultiStageEntity.setCurrentEntity(rootArea);

        for (PositionEntity positionEntity : list) {
            sortNode(rootMultiStageEntity, positionEntity);
        }
        getView().getPositionInfoListSuccess(rootMultiStageEntity);
    }

    /**
     * 部门分级
     */
    private void sortNode(PositionTreeViewEntity rootNode, PositionEntity positionEntity) {
        List<ICustomTreeView<PositionEntity>> rootNodeList = rootNode.getActualChildNodeList();
        PositionTreeViewEntity insertedreaMultiStageEntity = new PositionTreeViewEntity();
        insertedreaMultiStageEntity.setCurrentEntity(positionEntity);
        if (positionEntity.getParentId() == 0) {
            rootNodeList.add(insertedreaMultiStageEntity);
            //Todo:这行代码的效率极其低下,找时间还需要改改
            insertDirectBelongsNode(insertedreaMultiStageEntity);
        } else {
            insertChild(rootNodeList, insertedreaMultiStageEntity);
        }
    }

    private void insertChild(List<ICustomTreeView<PositionEntity>> nodeList, PositionTreeViewEntity insertedreaMultiStageEntity) {
        PositionEntity positionEntity = insertedreaMultiStageEntity.getCurrentEntity();
        for (ICustomTreeView<PositionEntity> iCustomTreeView : nodeList) {
            PositionEntity currentEntity = iCustomTreeView.getCurrentEntity();
            if (currentEntity.getId() == positionEntity.getParentId()) {
                iCustomTreeView.getActualChildNodeList().add(insertedreaMultiStageEntity);
                //Todo:这行代码的效率极其低下,找时间还需要改改
                insertDirectBelongsNode(insertedreaMultiStageEntity);
                return;
            } else {
                if (!currentEntity.getFullPathName().contains("direct")) {
                    if (iCustomTreeView.getActualChildNodeList().size() > 0) {
                        insertChild(iCustomTreeView.getActualChildNodeList(), insertedreaMultiStageEntity);
                    }
                }
            }
        }
    }

    /**
     * 创建直属部门信息,bap和e-message的信息组织结构存在着一些不同,e-message的配置中所有人员都处于末节点中
     *
     * @param currentNode
     */
    @SuppressLint("CheckResult")
    private void insertDirectBelongsNode(PositionTreeViewEntity currentNode) {
        //因为没有岗位全路径,先找到部门,同时比对部门全路径和岗位名称
        DepartmentInfo departmentInfo = EamApplication.dao().getDepartmentInfoDao().queryBuilder()
                .where(DepartmentInfoDao.Properties.Id.eq(currentNode.getCurrentEntity().getDepartment().id))
                .unique();
        if (departmentInfo == null) {
            LogUtil.e("没有找到所属部门!");
            return;
        }
        Flowable.fromIterable(
                EamApplication.dao()
                        .getContactEntityDao().queryBuilder()
                        .where(ContactEntityDao.Properties.FULLPATHNAME.eq(departmentInfo.getFullPathName()))
                        .where(ContactEntityDao.Properties.POSITIONNAME.eq(currentNode.getCurrentEntity().getName()))
                        .orderAsc(ContactEntityDao.Properties.NAME)
                        .list())
                .subscribe(new Consumer<ContactEntity>() {
                    @Override
                    public void accept(ContactEntity contactEntity) throws Exception {
//                        if (currentNode.getActualChildNodeList() == null)
//                            currentNode.setChildNodeList(new ArrayList<>());
//                        if (currentNode.getActualChildNodeList().size() == 0 || !currentNode.getActualChildNodeList().get(0).getInfo().endsWith("-direct")) {
//                            PositionTreeViewEntity cloneCurrent = currentNode.clone();
//                            PositionEntity clonePositionInfo = cloneCurrent.getCurrentEntity().clone();
//                            clonePositionInfo.name = "直属岗位";
//                            cloneCurrent.setCurrentEntity(clonePositionInfo);
//                            cloneCurrent.setFatherNode(currentNode);
//                            cloneCurrent.setInfo(currentNode.getInfo() + "-direct");
//                            cloneCurrent.setFullPathName(currentNode.getFullPathName() + "/direct");
//                            currentNode.getActualChildNodeList().add(0, cloneCurrent);
//                        }
//                        PositionTreeViewEntity directNode = (PositionTreeViewEntity) currentNode.getActualChildNodeList().get(0);
//                        PositionTreeViewEntity directNodeClone = directNode.clone();
//                        directNodeClone.setFatherNode(directNode);
//                        PositionEntity clonePositionInfo = directNodeClone.getCurrentEntity().clone();
//                        clonePositionInfo.userInfo = contactEntity;
//                        directNodeClone.setCurrentEntity(clonePositionInfo);
//                        directNode.getActualChildNodeList().add(directNodeClone);
//
//
//                        if (currentNode.getActualChildNodeList().size() == 0 || !currentNode.getActualChildNodeList().get(0).getInfo().endsWith("-direct")) {
//                            PositionTreeViewEntity cloneCurrent = currentNode.clone();
//                            PositionEntity clonePositionInfo = cloneCurrent.getCurrentEntity().clone();
//                            clonePositionInfo.name = "直属岗位";
//                            cloneCurrent.setCurrentEntity(clonePositionInfo);
//                            cloneCurrent.setFatherNode(currentNode);
//                            cloneCurrent.setInfo(currentNode.getInfo() + "-direct");
//                            cloneCurrent.setFullPathName(currentNode.getFullPathName() + "/direct");
//                            currentNode.getActualChildNodeList().add(0, cloneCurrent);
//                        }

                        PositionTreeViewEntity directNodeClone = currentNode.clone();
                        directNodeClone.setFatherNode(currentNode);
                        PositionEntity clonePositionInfo = directNodeClone.getCurrentEntity().clone();
                        clonePositionInfo.userInfo = contactEntity;
                        directNodeClone.setCurrentEntity(clonePositionInfo);
                        currentNode.getActualChildNodeList().add(directNodeClone);
                    }
                });
    }


}
