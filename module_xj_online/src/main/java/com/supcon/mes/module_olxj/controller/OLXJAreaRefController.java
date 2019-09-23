package com.supcon.mes.module_olxj.controller;

import android.content.Context;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseDataController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_olxj.model.api.OLXJAreaAPI;
import com.supcon.mes.module_olxj.model.bean.OLXJAreaEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJAreaContract;
import com.supcon.mes.module_olxj.presenter.OLXJAreaListPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2019/4/1
 * Email:wangshizhan@supcom.com
 */
@Presenter(value = {OLXJAreaListPresenter.class})
public class OLXJAreaRefController extends BaseDataController implements OLXJAreaContract.View{

    private long groupId;
    private int cuttentAreaPage = 1;
    private List<OLXJAreaEntity> mAreaEntities = new ArrayList<>();
    private OnSuccessListener<Boolean> mOnSuccessListener;

    public OLXJAreaRefController(Context context) {
        super(context);
    }


    public void getData(long groupId, OnSuccessListener<Boolean> listener){
        this.groupId = groupId;
        if(groupId == 0 ){
            throw new IllegalArgumentException("no groupId, please check it!");
        }
        mOnSuccessListener = listener;
        mAreaEntities.clear();
        getAreaData();
    }


    private void getAreaData(){
        presenterRouter.create(OLXJAreaAPI.class).getOJXJAreaList(groupId, cuttentAreaPage);
    }



    @Override
    public void getOJXJAreaListSuccess(CommonBAPListEntity entity) {

        if(entity.result!=null){
            mAreaEntities.addAll(entity.result);
        }

        if(entity.hasNext){
            cuttentAreaPage ++;
            getAreaData();
        }
        else{
           if(mOnSuccessListener!=null){
               mOnSuccessListener.onSuccess(true);
           }
        }
    }

    @Override
    public void getOJXJAreaListFailed(String errorMsg) {
        LogUtil.e(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void getAbnormalInspectTaskPartSuccess(CommonListEntity entity) {

    }

    @Override
    public void getAbnormalInspectTaskPartFailed(String errorMsg) {

    }

    public List<OLXJAreaEntity> getAreaEntities() {
        return mAreaEntities;
    }

}
