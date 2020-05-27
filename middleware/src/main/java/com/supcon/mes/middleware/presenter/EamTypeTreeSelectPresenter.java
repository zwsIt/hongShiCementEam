package com.supcon.mes.middleware.presenter;

import android.annotation.SuppressLint;

import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.Area;
import com.supcon.mes.middleware.model.bean.AreaListEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.EamAreaTreeViewEntity;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.EamEntityDao;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.EamTypeTreeViewEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.ICustomTreeView;
import com.supcon.mes.middleware.model.contract.EamAreaTreeSelectContract;
import com.supcon.mes.middleware.model.contract.EamTypeTreeSelectContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.middleware.util.PageParamUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by zhangwenshuai on 2020/05/25
 * Email:zhangwenshuai1@supcom.com
 * 设备类型层级选择Presenter
 */
public class EamTypeTreeSelectPresenter extends EamTypeTreeSelectContract.Presenter {
    private EamTypeTreeViewEntity rootMultiStageEntity;

    @SuppressLint("CheckResult")
    @Override
    public void getEamTypeList(String id) {
        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());
        fastQueryCondEntity.modelAlias = "eamType";

        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page.pageSize", 9999);
        pageMap.put("page.maxPageSize", 9999);
        pageMap.put("page.pageNo", 1);
        // 在线获取区域位置list
        mCompositeSubscription.add(
                MiddlewareHttpClient.listEamTypeRef(fastQueryCondEntity,pageMap)
                        .onErrorReturn(throwable -> {
                            CommonBAPListEntity<EamType> commonBAPListEntity = new CommonBAPListEntity<>();
                            commonBAPListEntity.errMsg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            return commonBAPListEntity;
                        })
                        .subscribe(new Consumer<CommonBAPListEntity<EamType>>() {
                            @Override
                            public void accept(CommonBAPListEntity<EamType> eamTypeCommonBAPListEntity) throws Exception {
                                List<EamType> list = eamTypeCommonBAPListEntity.result;
                                if (list == null) {
                                    getView().getEamTypeListFailed(eamTypeCommonBAPListEntity.errMsg);
                                } else {
                                    rootMultiStageEntity = new EamTypeTreeViewEntity();
                                    EamType root = new EamType();
                                    root.layRec = "";
                                    root.layNo = 0;
                                    root.fullPathName = "";
                                    root.name = EamApplication.getAccountInfo().companyName;
                                    root.cid = EamApplication.getAccountInfo().cid;
                                    rootMultiStageEntity.setCurrentEntity(root);
                                    for (EamType eamType : list) {
                                        sortNode(rootMultiStageEntity, eamType);
                                    }
                                    getView().getEamTypeListSuccess(rootMultiStageEntity);
                                }
                            }
                        })
        );

    }

    /**
     * 区域分级
     */
    private void sortNode(EamTypeTreeViewEntity rootNode, EamType eamType) {
        List<ICustomTreeView<EamType>> rootNodeList = rootNode.getActualChildNodeList();
        EamTypeTreeViewEntity insertedAreaMultiStageEntity = new EamTypeTreeViewEntity();
        insertedAreaMultiStageEntity.setCurrentEntity(eamType);
        if (eamType.getParentId() == -1) {
            rootNodeList.add(insertedAreaMultiStageEntity);
            //Todo:这行代码的效率极其低下,找时间还需要改改
            insertDirectBelongsNode(insertedAreaMultiStageEntity);
        } else {
            insertChild(rootNodeList, insertedAreaMultiStageEntity);
        }
    }

    private void insertChild(List<ICustomTreeView<EamType>> nodeList, EamTypeTreeViewEntity insertedAreaMultiStageEntity) {
        EamType eamType = insertedAreaMultiStageEntity.getCurrentEntity();
        for (ICustomTreeView<EamType> iCustomTreeView : nodeList) {
            EamType currentEntity = iCustomTreeView.getCurrentEntity();
            if (currentEntity.getId() == eamType.getParentId()) {
                iCustomTreeView.getActualChildNodeList().add(insertedAreaMultiStageEntity);
                //Todo:这行代码的效率极其低下,找时间还需要改改
                insertDirectBelongsNode(insertedAreaMultiStageEntity);
            } else {
                if (iCustomTreeView.getActualChildNodeList().size() > 0) {
                    insertChild(iCustomTreeView.getActualChildNodeList(), insertedAreaMultiStageEntity);
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
    private void insertDirectBelongsNode(EamTypeTreeViewEntity currentNode) {
        List<EamEntity> list = EamApplication.dao().getEamEntityDao().queryBuilder().where(EamEntityDao.Properties.EamTypeId.eq(currentNode.getCurrentEntity().id))
                .orderAsc(EamEntityDao.Properties.Id)
                .list();
        Flowable.fromIterable(list)
                .subscribe(new Consumer<EamEntity>() {
                    @Override
                    public void accept(EamEntity eamEntity) throws Exception {
                        EamTypeTreeViewEntity directNodeClone = currentNode.clone();
                        directNodeClone.setFatherNode(currentNode);
                        EamType cloneArea = directNodeClone.getCurrentEntity().clone();
                        cloneArea.eamEntity = eamEntity;
                        directNodeClone.setCurrentEntity(cloneArea);
                        currentNode.getActualChildNodeList().add(directNodeClone);
                    }
                });
    }


}
