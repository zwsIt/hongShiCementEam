package com.supcon.mes.middleware.controller;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.api.AreaQueryAPI;
import com.supcon.mes.middleware.model.bean.Area;
import com.supcon.mes.middleware.model.bean.AreaDao;
import com.supcon.mes.middleware.model.bean.AreaListEntity;
import com.supcon.mes.middleware.model.contract.AreaQueryContract;
import com.supcon.mes.middleware.presenter.AreaPresenter;
import com.supcon.mes.middleware.util.PinYinUtils;

/**
 * Created by wangshizhan on 2018/8/20
 * Email:wangshizhan@supcom.com
 */
@Presenter(AreaPresenter.class)
public class AreaController extends BasePresenterController implements AreaQueryContract.View, AreaQueryAPI{

    @Override
    public void onInit() {
        super.onInit();
        listArea();
    }

    @Override
    public void listAreaSuccess(AreaListEntity entity) {
        AreaDao areaDao = EamApplication.dao().getAreaDao();
        areaDao.deleteAll();
        if(entity.result!=null && entity.result.size()!=0){
            for (Area area: entity.result
                 ) {
                area.pinyin = PinYinUtils.getPinyin(area.name);
                areaDao.insertOrReplace(area);
            }
//            EamApplication.dao().getAreaDao().insertOrReplaceInTx(entity.result);
        }

    }

    @Override
    public void listAreaFailed(String errorMsg) {
        LogUtil.e("AreaController:"+errorMsg);
    }

    @Override
    public void listArea() {
        presenterRouter.create(AreaQueryAPI.class).listArea();
    }


}
