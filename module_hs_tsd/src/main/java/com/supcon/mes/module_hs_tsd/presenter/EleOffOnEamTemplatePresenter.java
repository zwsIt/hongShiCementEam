package com.supcon.mes.module_hs_tsd.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.model.contract.CommonListContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_hs_tsd.model.bean.EleOffOnTemplate;
import com.supcon.mes.module_hs_tsd.model.network.HttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/28
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class EleOffOnEamTemplatePresenter extends CommonListContract.Presenter {
    @Override
    public void listCommonObj(int pageNo, Map<String, Object> queryMap,boolean OffOn) {

        FastQueryCondEntity fastQueryCondEntity = new FastQueryCondEntity();
        JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.createJoinSubcondEntity(queryMap,"EAM_BaseInfo,EAM_ID,BEAMELE_ONOROFF_TEMPLATES,EAM_ID");
        fastQueryCondEntity.subconds = new ArrayList<>();
        fastQueryCondEntity.subconds.add(joinSubcondEntity);
        fastQueryCondEntity.modelAlias = "onoroffTemplate";

        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page.pageSize", "20");
        pageMap.put("page.maxPageSize", "500");
        pageMap.put("page.pageNo", pageNo);

        Flowable<CommonBAPListEntity<EleOffOnTemplate>> http;
        if (OffOn){
            http = HttpClient.eleOnTemplateRef(fastQueryCondEntity,pageMap);
        }else {
            http = HttpClient.eleOffTemplateRef(fastQueryCondEntity,pageMap);
        }

        mCompositeSubscription.add(
                http.onErrorReturn(new Function<Throwable, CommonBAPListEntity<EleOffOnTemplate>>() {
                            @Override
                            public CommonBAPListEntity<EleOffOnTemplate> apply(Throwable throwable) throws Exception {
                                CommonBAPListEntity commonBAPListEntity = new CommonBAPListEntity();
                                commonBAPListEntity.errMsg = throwable.toString();
                                return commonBAPListEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonBAPListEntity<EleOffOnTemplate>>() {
                            @Override
                            public void accept(CommonBAPListEntity<EleOffOnTemplate> commonBAPListEntity) throws Exception {
                                if (TextUtils.isEmpty(commonBAPListEntity.errMsg) && commonBAPListEntity.result != null) {
                                    getView().listCommonObjSuccess(commonBAPListEntity);
                                } else {
                                    getView().listCommonObjFailed(commonBAPListEntity.errMsg);
                                }
                            }
                        })
        );
    }
}
