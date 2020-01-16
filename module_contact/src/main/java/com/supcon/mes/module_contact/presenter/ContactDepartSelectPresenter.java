package com.supcon.mes.module_contact.presenter;

import android.annotation.SuppressLint;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.ContactEntityDao;
import com.supcon.mes.middleware.model.bean.DepartmentInfo;
import com.supcon.mes.middleware.model.bean.DepartmentInfoDao;
import com.supcon.mes.middleware.model.bean.DepartmentTreeViewEntity;
import com.supcon.mes.middleware.model.bean.ICustomTreeView;
import com.supcon.mes.module_contact.model.contract.ContactDepartSelectContract;

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
public class ContactDepartSelectPresenter extends ContactDepartSelectContract.Presenter {

    private DepartmentTreeViewEntity rootMultiStageEntity;

    @SuppressLint("CheckResult")
    @Override
    public void getDepartmentInfoList(String id) {
        LogUtil.i("" + id);
        List<DepartmentInfo> list = EamApplication.dao().getDepartmentInfoDao().queryBuilder()
//                .where(DepartmentInfoDao.Properties.LayRec.like("?" + id + "?"))
                .orderAsc(DepartmentInfoDao.Properties.LayRec).list();
        Collections.sort(list);

        rootMultiStageEntity = new DepartmentTreeViewEntity();
        DepartmentInfo rootArea = new DepartmentInfo();
        rootArea.layRec = "";
        rootArea.fullPathName = "";
        rootArea.name = EamApplication.getAccountInfo().companyName;
        rootArea.cid = EamApplication.getAccountInfo().cid;
        rootMultiStageEntity.setCurrentEntity(rootArea);

        for (DepartmentInfo departmentInfo : list) {
            sortNode(rootMultiStageEntity, departmentInfo);
        }
        getView().getDepartmentInfoListSuccess(rootMultiStageEntity);
    }

    /**
     * 部门分级
     */
    private void sortNode(DepartmentTreeViewEntity rootNode, DepartmentInfo departmentInfo) {
        List<ICustomTreeView<DepartmentInfo>> rootNodeList = rootNode.getActualChildNodeList();
        DepartmentTreeViewEntity insertedreaMultiStageEntity = new DepartmentTreeViewEntity();
        insertedreaMultiStageEntity.setCurrentEntity(departmentInfo);
        if (departmentInfo.getParentId() == 0) {
            rootNodeList.add(insertedreaMultiStageEntity);
            //Todo:这行代码的效率极其低下,找时间还需要改改
            insertDirectBelongsNode(insertedreaMultiStageEntity);
        } else {
            insertChild(rootNodeList, insertedreaMultiStageEntity);
        }
    }

    private void insertChild(List<ICustomTreeView<DepartmentInfo>> nodeList, DepartmentTreeViewEntity insertedreaMultiStageEntity) {
        DepartmentInfo departmentInfo = insertedreaMultiStageEntity.getCurrentEntity();
        for (ICustomTreeView<DepartmentInfo> iCustomTreeView : nodeList) {
            DepartmentInfo currentEntity = iCustomTreeView.getCurrentEntity();
            if (currentEntity.getId() == departmentInfo.getParentId()) {
                iCustomTreeView.getActualChildNodeList().add(insertedreaMultiStageEntity);
                //Todo:这行代码的效率极其低下,找时间还需要改改
                insertDirectBelongsNode(insertedreaMultiStageEntity);
            } else {
                if (iCustomTreeView.getActualChildNodeList().size() > 0) {
                    insertChild(iCustomTreeView.getActualChildNodeList(), insertedreaMultiStageEntity);
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
    private void insertDirectBelongsNode(DepartmentTreeViewEntity currentNode) {
        Flowable.fromIterable(
                EamApplication.dao()
                        .getContactEntityDao().queryBuilder()
                        .where(ContactEntityDao.Properties.FULLPATHNAME.eq(currentNode.getCurrentEntity().fullPathName))
                        .orderAsc(ContactEntityDao.Properties.NAME)
                        .list())
                .subscribe(new Consumer<ContactEntity>() {
                    @Override
                    public void accept(ContactEntity contactEntity) throws Exception {
//                        if (currentNode.getActualChildNodeList() == null)
//                            currentNode.setChildNodeList(new ArrayList<>());
//                        if (currentNode.getActualChildNodeList().size() == 0 || !currentNode.getActualChildNodeList().get(0).getInfo().endsWith("-direct")) {
//                            DepartmentTreeViewEntity cloneCurrent = currentNode.clone();
//                            DepartmentInfo cloneDepartInfo = cloneCurrent.getCurrentEntity().clone();
//                            cloneDepartInfo.name = "直属部门";
//                            cloneCurrent.setCurrentEntity(cloneDepartInfo);
//                            cloneCurrent.setFatherNode(currentNode);
//                            cloneCurrent.setInfo(currentNode.getInfo() + "-direct");
//                            cloneCurrent.setFullPathName(currentNode.getFullPathName() + "/direct");
//                            currentNode.getActualChildNodeList().add(0, cloneCurrent);
//                        }
//                        DepartmentTreeViewEntity directNode = (DepartmentTreeViewEntity) currentNode.getActualChildNodeList().get(0);
//                        DepartmentTreeViewEntity directNodeClone = directNode.clone();
//                        directNodeClone.setFatherNode(directNode);
//                        DepartmentInfo cloneDepartInfo = directNodeClone.getCurrentEntity().clone();
//                        cloneDepartInfo.userInfo = contactEntity;
//                        directNodeClone.setCurrentEntity(cloneDepartInfo);
//                        directNode.getActualChildNodeList().add(directNodeClone);

                        DepartmentTreeViewEntity directNodeClone = currentNode.clone();
                        directNodeClone.setFatherNode(currentNode);
                        DepartmentInfo cloneDepartInfo = directNodeClone.getCurrentEntity().clone();
                        cloneDepartInfo.userInfo = contactEntity;
                        directNodeClone.setCurrentEntity(cloneDepartInfo);
                        currentNode.getActualChildNodeList().add(directNodeClone);
                    }
                });
    }


}
