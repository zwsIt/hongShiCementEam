package com.supcon.mes.module_olxj.controller;

import android.content.Context;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseDataController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_olxj.model.api.OLXJWorkListAPI;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkListEntity;
import com.supcon.mes.module_olxj.model.contract.OLXJWorkListContract;
import com.supcon.mes.module_olxj.presenter.OLXJWorkItemPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2019/4/1
 * Email:wangshizhan@supcom.com
 */
@Presenter(OLXJWorkItemPresenter.class)
public class OLXJWorkItemListRefController extends BaseDataController implements OLXJWorkListContract.View{

    private long groupId;
    private int currentPage = 1;
    List<OLXJWorkItemEntity> mOLXJWorkListEntities = new ArrayList<>();
    private OnSuccessListener<List<OLXJWorkItemEntity>> mOnSuccessListener;

    public OLXJWorkItemListRefController(Context context) {
        super(context);
    }

    public void setOnSuccessListener(OnSuccessListener<List<OLXJWorkItemEntity>> listener){
        mOnSuccessListener = listener;
    }



    public void getData(long groupId){
        this.groupId = groupId;
        if(mOLXJWorkListEntities.size()!=0){
            mOLXJWorkListEntities.clear();
        }
        getData();
    }


    private void getData(){
        if(groupId == 0){
            throw new IllegalArgumentException("no taskId or groupId, please check it!");
        }
        presenterRouter.create(OLXJWorkListAPI.class).getWorkItemListRef(groupId, currentPage);
    }

    @Override
    public void getXJWorkItemListSuccess(OLXJWorkListEntity entity) {

    }

    @Override
    public void getXJWorkItemListFailed(String errorMsg) {

    }

    @Override
    public void getWorkItemListSuccess(CommonBAPListEntity entity) {


    }

    @Override
    public void getWorkItemListFailed(String errorMsg) {

    }

    @Override
    public void getWorkItemListRefSuccess(CommonBAPListEntity entity) {
        if(entity.result!=null){
            mOLXJWorkListEntities.addAll(entity.result);
        }

        if(entity.hasNext){
            currentPage ++;
            getData();
        }
        else{

            if(mOnSuccessListener!= null){
                mOnSuccessListener.onSuccess(mOLXJWorkListEntities);
            }

        }
    }

    @Override
    public void getWorkItemListRefFailed(String errorMsg) {

    }
}
