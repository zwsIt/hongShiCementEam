package com.supcon.mes.module_olxj.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_olxj.model.bean.AbnormalEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJAreaEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJAreaContract;
import com.supcon.mes.module_olxj.model.network.OLXJClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2019/4/2
 * Email:wangshizhan@supcom.com
 */
public class OLXJAreaListPresenter extends OLXJAreaContract.Presenter {
    @Override
    public void getOJXJAreaList(String taskTableNo, long groupId, int pageNo) {
        /*Flowable<CommonBAPListEntity<OLXJAreaEntity>> http;
        if (TextUtils.isEmpty(taskTableNo)){
            http = OLXJClient.queryOLXJArea(groupId, pageNo);
        }else {

            Map<String,Object> pageMap = new HashMap<>();
            pageMap.put("page.pageNo",pageNo);
            pageMap.put("page.pageSize","500");
            pageMap.put("page.maxPageSize","500");

            Map<String,Object> queryMap = new HashMap<>();
            queryMap.put(Constant.BAPQuery.TABLE_NO,taskTableNo);
            FastQueryCondEntity fastQueryCondEntity = new FastQueryCondEntity();
            fastQueryCondEntity.modelAlias = "potrolTPartWF";
            JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.createJoinSubcondEntity(queryMap,"MOBILEEAM_POTROL_TASKWFS,ID,MOBILEEAM_POTROLTPARTWFS,TASKID");
            fastQueryCondEntity.subconds.add(joinSubcondEntity);

            http = OLXJClient.signGatherList(pageMap,fastQueryCondEntity);
        }*/
        mCompositeSubscription.add(
                OLXJClient.queryOLXJArea(groupId, pageNo).onErrorReturn(new Function<Throwable, CommonBAPListEntity<OLXJAreaEntity>>() {
                            @Override
                            public CommonBAPListEntity<OLXJAreaEntity> apply(Throwable throwable) throws Exception {
                                CommonBAPListEntity<OLXJAreaEntity> commonBAPListEntity = new CommonBAPListEntity<>();
                                commonBAPListEntity.success = false;
                                commonBAPListEntity.errMsg = throwable.toString();
                                return commonBAPListEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonBAPListEntity<OLXJAreaEntity>>() {
                            @Override
                            public void accept(CommonBAPListEntity<OLXJAreaEntity> olxjAreaEntityCommonBAPListEntity) throws Exception {
                                if (olxjAreaEntityCommonBAPListEntity.result != null) {
                                    Objects.requireNonNull(getView()).getOJXJAreaListSuccess(olxjAreaEntityCommonBAPListEntity);
                                } else {
                                    Objects.requireNonNull(getView()).getOJXJAreaListFailed(olxjAreaEntityCommonBAPListEntity.errMsg);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Objects.requireNonNull(getView()).getOJXJAreaListFailed(throwable.toString());
                            }
                        }));
    }

    @Override
    public void getAbnormalInspectTaskPart(long groupId, int isTemp) {
        mCompositeSubscription.add(
                OLXJClient.getAbnormalInspectTaskPart(groupId, isTemp)
                        .onErrorReturn(new Function<Throwable, CommonListEntity<AbnormalEntity>>() {
                            @Override
                            public CommonListEntity<AbnormalEntity> apply(Throwable throwable) throws Exception {
                                CommonListEntity<AbnormalEntity> commonBAPListEntity = new CommonListEntity<>();
                                commonBAPListEntity.success = false;
                                commonBAPListEntity.errMsg = throwable.toString();
                                return commonBAPListEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonListEntity<AbnormalEntity>>() {
                            @Override
                            public void accept(CommonListEntity<AbnormalEntity> abnormalEntity) throws Exception {
                                if (abnormalEntity.result != null) {
                                    Objects.requireNonNull(getView()).getAbnormalInspectTaskPartSuccess(abnormalEntity);
                                } else {
                                    Objects.requireNonNull(getView()).getAbnormalInspectTaskPartFailed(abnormalEntity.errMsg);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Objects.requireNonNull(getView()).getAbnormalInspectTaskPartFailed(throwable.toString());
                            }
                        }));
    }
}
