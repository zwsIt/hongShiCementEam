package com.supcon.mes.middleware.presenter;

import android.annotation.SuppressLint;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.Area;
import com.supcon.mes.middleware.model.bean.AreaListEntity;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.ContactEntityDao;
import com.supcon.mes.middleware.model.bean.DepartmentInfo;
import com.supcon.mes.middleware.model.bean.DepartmentInfoDao;
import com.supcon.mes.middleware.model.bean.DepartmentTreeViewEntity;
import com.supcon.mes.middleware.model.bean.EamAreaTreeViewEntity;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.EamEntityDao;
import com.supcon.mes.middleware.model.bean.ICustomTreeView;
import com.supcon.mes.middleware.model.contract.EamAreaTreeSelectContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by zhangwenshuai on 2020/05/25
 * Email:zhangwenshuai1@supcom.com
 * 设备区域选择Presenter
 */
public class EamAreaTreeSelectPresenter extends EamAreaTreeSelectContract.Presenter {
    private EamAreaTreeViewEntity rootMultiStageEntity;

    @SuppressLint("CheckResult")
    @Override
    public void getEamAreaList(String id) {

        // 在线获取区域位置list
        mCompositeSubscription.add(
                MiddlewareHttpClient.listArea()
                        .onErrorReturn(throwable -> {
                            AreaListEntity areaListEntity = new AreaListEntity();
                            areaListEntity.errMsg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            return areaListEntity;
                        })
                        .subscribe(areaListEntity -> {
                            List<Area> list = areaListEntity.result;
                            if (list == null) {
                                getView().getEamAreaListFailed(areaListEntity.errMsg);
                            } else {
                                rootMultiStageEntity = new EamAreaTreeViewEntity();
                                Area rootArea = new Area();
                                rootArea.layRec = "";
                                rootArea.layNo = 0;
                                rootArea.fullPathName = "";
                                rootArea.name = EamApplication.getAccountInfo().companyName;
                                rootArea.cid = EamApplication.getAccountInfo().cid;
                                rootMultiStageEntity.setCurrentEntity(rootArea);

                                for (Area area : list) {
                                    sortNode(rootMultiStageEntity, area);
                                }
                                getView().getEamAreaListSuccess(rootMultiStageEntity);
                            }
                        })
        );

    }

    /**
     * 区域分级
     */
    private void sortNode(EamAreaTreeViewEntity rootNode, Area area) {
        List<ICustomTreeView<Area>> rootNodeList = rootNode.getActualChildNodeList();
        EamAreaTreeViewEntity insertedAreaMultiStageEntity = new EamAreaTreeViewEntity();
        insertedAreaMultiStageEntity.setCurrentEntity(area);
        if (area.getParentId() == -1) {
            rootNodeList.add(insertedAreaMultiStageEntity);
            //Todo:这行代码的效率极其低下,找时间还需要改改
            insertDirectBelongsNode(insertedAreaMultiStageEntity);
        } else {
            insertChild(rootNodeList, insertedAreaMultiStageEntity);
        }
    }

    private void insertChild(List<ICustomTreeView<Area>> nodeList, EamAreaTreeViewEntity insertedAreaMultiStageEntity) {
        Area area = insertedAreaMultiStageEntity.getCurrentEntity();
        for (ICustomTreeView<Area> iCustomTreeView : nodeList) {
            Area currentEntity = iCustomTreeView.getCurrentEntity();
            if (currentEntity.getId() == area.getParentId()) {
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
    private void insertDirectBelongsNode(EamAreaTreeViewEntity currentNode) {
        List<EamEntity> list = EamApplication.dao().getEamEntityDao().queryBuilder().where(EamEntityDao.Properties.AreaId.eq(currentNode.getCurrentEntity().id))
                .orderAsc(EamEntityDao.Properties.EamAssetCode)
                .list();
        Flowable.fromIterable(list)
                .subscribe(eamEntity -> {
                    EamAreaTreeViewEntity directNodeClone = currentNode.clone();
                    directNodeClone.setFatherNode(currentNode);
                    Area cloneArea = directNodeClone.getCurrentEntity().clone();
                    cloneArea.eamEntity = eamEntity;
                    directNodeClone.setCurrentEntity(cloneArea);
                    currentNode.getActualChildNodeList().add(directNodeClone);
                });
    }


}
