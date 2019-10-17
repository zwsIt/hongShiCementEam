package com.supcon.mes.module_wxgd.controller;

import android.util.Log;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.utils.controllers.BasePickerController;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.middleware.model.bean.StandingCropEntity;
import com.supcon.mes.module_wxgd.model.api.SparePartListAPI;
import com.supcon.mes.module_wxgd.model.contract.SparePartListContract;
import com.supcon.mes.module_wxgd.presenter.SparePartListPresenter;

import java.math.BigDecimal;
import java.util.List;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/10/8
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@Presenter(value = {SparePartListPresenter.class})
public class AutoRefreshSPStandingCropController extends BasePresenterController implements SparePartListContract.View {

    private CustomTextView standingCrop;
    private String productCode;

    public AutoRefreshSPStandingCropController() {
    }
    public AutoRefreshSPStandingCropController(CustomTextView standingCrop, String productCode) {
        this.standingCrop = standingCrop;
        this.productCode = productCode;
    }

    public void initData(){
        presenterRouter.create(SparePartListAPI.class).updateStandingCrop(productCode);
    }

    @Override
    public void updateStandingCropSuccess(CommonListEntity entity) {
        if (entity.result.size() > 0){
            StandingCropEntity standingCropEntity = (StandingCropEntity) entity.result.get(0);
            standingCrop.setValue(standingCropEntity.useQuantity == null ? "" : standingCropEntity.useQuantity);
        }
    }

    @Override
    public void updateStandingCropFailed(String errorMsg) {
        LogUtil.e(errorMsg);
    }

    @Override
    public void generateSparePartApplySuccess(ResultEntity entity) {

    }

    @Override
    public void generateSparePartApplyFailed(String errorMsg) {

    }
}
