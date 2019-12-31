package com.supcon.mes.module_sbda_online.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.contract.CommonListContract;
import com.supcon.mes.module_sbda_online.model.bean.EamFileEntity;
import com.supcon.mes.module_sbda_online.model.network.SBDAOnlineHttpClient;

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
public class EamFIleListPresenter extends CommonListContract.Presenter {
    @Override
    public void listCommonObj(int pageNo, Map<String, Object> queryMap,boolean OffOn) {

        mCompositeSubscription.add(
                SBDAOnlineHttpClient.getEamFileList(queryMap)
                        .onErrorReturn(new Function<Throwable, CommonBAPListEntity<EamFileEntity>>() {
                            @Override
                            public CommonBAPListEntity<EamFileEntity> apply(Throwable throwable) throws Exception {
                                CommonBAPListEntity commonBAPListEntity = new CommonBAPListEntity();
                                commonBAPListEntity.errMsg = throwable.toString();
                                return commonBAPListEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonBAPListEntity<EamFileEntity>>() {
                            @Override
                            public void accept(CommonBAPListEntity<EamFileEntity> commonBAPListEntity) throws Exception {
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
