package com.supcon.mes.module_olxj.presenter;

import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.AreaMultiStageEntity;
import com.supcon.mes.middleware.model.bean.DepartmentInfo;
import com.supcon.mes.middleware.model.bean.DepartmentInfoDao;
import com.supcon.mes.middleware.model.contract.MultiDepartSelectContract;
import com.supcon.mes.module_olxj.model.contract.XJMultiDepartSelectContract;

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
public class MultiDepartSelectPresenter extends XJMultiDepartSelectContract.Presenter {
    @Override
    public void getDepartmentInfoList(String id) {
        mCompositeSubscription.add(Flowable.just(EamApplication.dao().getDepartmentInfoDao().queryBuilder()
                .orderAsc(DepartmentInfoDao.Properties.LayRec).list())
                .map(areas -> {
                    AreaMultiStageEntity rootMultiStageEntity = new AreaMultiStageEntity();
                    DepartmentInfo rootArea = new DepartmentInfo();
                    rootArea.layRec = "";
                    rootArea.name = "集团";
                    AreaMultiStageEntity currentNode = rootMultiStageEntity;
                    rootMultiStageEntity.setCurrentEntity(rootArea);
                    for (int i = 0; i < areas.size(); i++) {
                        DepartmentInfo area = areas.get(i);
                        currentNode = insertNode(currentNode, area);
                    }
                    return rootMultiStageEntity;
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<AreaMultiStageEntity>() {
                    @Override
                    public void accept(AreaMultiStageEntity areaMultiStageEntity) throws Exception {
                        getView().getDepartmentInfoListSuccess(areaMultiStageEntity);
                    }
                }));
    }

    private AreaMultiStageEntity insertNode(AreaMultiStageEntity currentNode, DepartmentInfo area) {
        DepartmentInfo currentEntity = currentNode.currentEntity;
        AreaMultiStageEntity insertedreaMultiStageEntity = new AreaMultiStageEntity()
                .setFatherNode(currentNode)
                .setCurrentEntity(area);
        if (area.layRec.contains(currentEntity.layRec)) {
            currentNode.getActualChildNodeList().add(insertedreaMultiStageEntity);
        } else {
            AreaMultiStageEntity iterateEntity = currentNode;
            while (iterateEntity.fatherNode() != null) {
                if (area.layRec.contains(iterateEntity.fatherNode().getCurrentEntity().layRec)) {
                    iterateEntity.fatherNode.getActualChildNodeList().add(insertedreaMultiStageEntity);
                    break;
                }
                iterateEntity = (AreaMultiStageEntity) iterateEntity.fatherNode();
            }
        }
        return insertedreaMultiStageEntity;
    }
}
