package com.supcon.mes.module_warn.presenter;

import com.supcon.mes.middleware.model.contract.CommonListContract;
import com.supcon.mes.module_warn.model.network.EarlyWarnHttpClient;

import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/8
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class DailyLubrRecordsFinishPresenter extends CommonListContract.Presenter {
    @Override
    public void listCommonObj(int pageNo, Map<String, Object> queryMap, boolean OffOn) {
        mCompositeSubscription.add(
                EarlyWarnHttpClient.dailyLubricateTaskQuery()
        );
    }
}
