package com.supcon.mes.module_xj.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.app.annotation.BindByTag;
import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.mes.mbap.beans.GalleryBean;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.XJExemptionEntity;
import com.supcon.mes.middleware.model.bean.XJExemptionEntityDao.Properties;
import com.supcon.mes.middleware.model.bean.XJPathEntity;
import com.supcon.mes.middleware.model.bean.XJPathEntityDao;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.FaultPicHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_xj.IntentRouter;
import com.supcon.mes.module_xj.R;
import com.supcon.mes.module_xj.constant.XJConstant;
import com.supcon.mes.module_xj.ui.HSXJWorkItemActivity;
import com.supcon.mes.module_xj.ui.XJWorkItemActivity;
import com.supcon.mes.module_xj.ui.adapter.XJWorkItemListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by wangshizhan on 2018/3/27.
 * Email:wangshizhan@supcon.com
 */

public class XJHandledWorkItemFragment extends BaseRefreshRecyclerFragment<XJWorkItemEntity> {

    private XJWorkItemListAdapter mXJWorkItemListAdapter;

    public List<XJWorkItemEntity> getXjWorkItemEntities() {
        return xjWorkItemEntities;
    }

    private List<XJWorkItemEntity> xjWorkItemEntities = new ArrayList<>();

    @BindByTag("contentView")
    RecyclerView contentView;

    @Override
    protected IListAdapter createAdapter() {
        mXJWorkItemListAdapter = new XJWorkItemListAdapter(context);
        return mXJWorkItemListAdapter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((HSXJWorkItemActivity)getActivity()).refreshData();
    }

    @Override
    protected void onInit() {
        super.onInit();
        refreshListController.setPullDownRefreshEnabled(false);
        refreshListController.setAutoPullDownRefresh(false);
    }

    @Override
    protected void initView() {
        super.initView();
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));

        initEmptyView();
    }
    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshListener(() -> {
            //TODO...
            ((XJWorkItemActivity)getActivity()).refreshData();
        });

        mXJWorkItemListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            XJWorkItemEntity xjWorkItemEntity;
            int imgIndex;
            if (obj instanceof Integer){
                imgIndex = (int)obj;
                xjWorkItemEntity = null;
            }else {
                xjWorkItemEntity = (XJWorkItemEntity)obj;
                imgIndex = 0;

            }

            String tag = (String) childView.getTag();
            switch (tag){

                case "fReRecordBtn":

                    if (!xjWorkItemEntity.control || XJConstant.MobileWiLinkState.EXEMPTION_STATE.equals(xjWorkItemEntity.linkState)){  //禁修改(结论不可修改或免检)

                        SnackbarHelper.showMessage(rootView,"该巡检项不允许重录");

                    }else {
                        new CustomDialog(context)
                                .twoButtonAlertDialog("是否重录数据？")
                                .bindView(R.id.redBtn,"是")
                                .bindView(R.id.grayBtn,"否")
                                .bindClickListener(R.id.grayBtn,v -> {
                                    //TODO
                                },true)
                                .bindClickListener(R.id.redBtn,v -> {

                                    xjWorkItemEntity.isFinished = false;
                                    xjWorkItemEntity.endTime = null;
                                    xjWorkItemEntity.linkState = XJConstant.MobileWiLinkState.WAIT_STATE;
                                    xjWorkItemEntity.realIsPass = 0;
                                    xjWorkItemEntity.skipReasonID = null;
                                    xjWorkItemEntity.skipReasonName = null;

                                    EamApplication.dao().getXJWorkItemEntityDao().update(xjWorkItemEntity);

//                                    ((HSXJWorkItemActivity)context).doDeleteEditYh(xjWorkItemEntity);

                                    //TODO...免检项还原
                                    if (XJConstant.MobileEditType.WHETHER.equals(xjWorkItemEntity.editType) || XJConstant.MobileEditType.RADIO.equals(xjWorkItemEntity.editType)){
                                        /*List<XJExemptionEntity> xjExemptionEntities = EamApplication.dao().getXJExemptionEntityDao().queryBuilder()
                                                .where(new WhereCondition.StringCondition("itemId = "+xjWorkItemEntity.itemId),new WhereCondition.StringCondition("result = "+xjWorkItemEntity.result)).list();*/
                                        QueryBuilder queryBuilder = EamApplication.dao().getXJExemptionEntityDao().queryBuilder();
                                        queryBuilder.where(Properties.ItemId.eq(xjWorkItemEntity.itemId),Properties.Result.eq(TextUtils.isEmpty(xjWorkItemEntity.result) ? "" : xjWorkItemEntity.result),Properties.Ip.eq(EamApplication.getIp()));
                                        List<XJExemptionEntity> xjExemptionEntities = queryBuilder.list();
                                        queryBuilder.LOG_SQL = true;
                                        queryBuilder.LOG_VALUES = true;

                                        for (XJExemptionEntity xjExemptionEntity : xjExemptionEntities){
                                            for (XJWorkItemEntity xjWorkItemEntity1 : xjWorkItemEntities) {
                                                if (xjExemptionEntity.exemptionItemId.equals(xjWorkItemEntity1.itemId) && xjWorkItemEntity1.isFinished){
                                                    xjWorkItemEntity1.linkState = XJConstant.MobileWiLinkState.WAIT_STATE;
                                                    xjWorkItemEntity1.isFinished = false;
                                                    xjWorkItemEntity1.endTime = null;
                                                    EamApplication.dao().getXJWorkItemEntityDao().update(xjWorkItemEntity1);
                                                    break;
                                                }

                                            }
                                        }
                                    }
                                    xjWorkItemEntities.remove(position);
                                    EventBus.getDefault().post(new RefreshEvent(Constant.RefreshAction.XJ_WORK_REINPUT, position));

                                    //TODO...判断任务状态是否修改为待检
                                    XJPathEntity xjPathEntity = EamApplication.dao().getXJPathEntityDao().queryBuilder()
                                            .where(XJPathEntityDao.Properties.Id.eq(xjWorkItemEntity.taskId),XJPathEntityDao.Properties.Ip.eq(EamApplication.getIp()))
                                            .list().get(0);
                                    if (Constant.XJPathStateType.PAST_CHECK_STATE.equals(xjPathEntity.state)){
                                        xjPathEntity.state = Constant.XJPathStateType.WAIT_CHECK_STATE;
                                        EamApplication.dao().getXJPathEntityDao().update(xjPathEntity);
                                    }

                                },true)
                                .show();
                    }

                    break;

                case "fItemPics":

                    if (action == CustomGalleryView.ACTION_VIEW){
                        CustomGalleryView customGalleryView = (CustomGalleryView) childView;
                        List<GalleryBean> galleryBeanList = customGalleryView.getGalleryAdapter().getList();

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("images", (Serializable) FaultPicHelper.getImagePathList(galleryBeanList));
                        bundle.putInt("position",imgIndex);  //点击位置索引

                        int[] location = new int[2];
                        childView.getLocationOnScreen(location);  //点击图片的位置
                        bundle.putInt("locationX",location[0]);
                        bundle.putInt("locationY",location[1]);

                        bundle.putInt("width", DisplayUtil.dip2px(100, context));//必须
                        bundle.putInt("height", DisplayUtil.dip2px(100, context));//必须
                        getActivity().getWindow().setWindowAnimations(R.style.fadeStyle);
                        IntentRouter.go(context, Constant.Router.IMAGE_VIEW, bundle);
                    }

                    break;
            }

        });





    }

    @Override
    protected int getLayoutID() {
        return R.layout.frag_xj_item_handled;
    }

    @SuppressLint("CheckResult")
    public void setHandledWorkItemList(List<XJWorkItemEntity> result) {
        if(mXJWorkItemListAdapter!=null && mXJWorkItemListAdapter.getList().size()!=0){
            mXJWorkItemListAdapter.clear();
        }
        Flowable.fromIterable(result)
                .compose(RxSchedulers.io_main())
                .filter(xjWorkItemEntity -> xjWorkItemEntity.isFinished)
                .subscribe(xjWorkItemEntity -> xjWorkItemEntities.add(xjWorkItemEntity), throwable -> {
                }, () -> {
                    refreshListController.refreshComplete(xjWorkItemEntities);
                    if (xjWorkItemEntities.size() <= 0){
                        ((HSXJWorkItemActivity)context).showButtonBar(false,1);
                    }else {
                        ((HSXJWorkItemActivity)context).showButtonBar(true,1);
                    }
                });

    }

    public void removeItem(int pos) {
        mXJWorkItemListAdapter.notifyItemRemoved(pos);
    }


    /**
     *@author zhangwenshuai1
     *@date 2018/4/9
     *@description 初始化无数据
     *
     */
    private void initEmptyView(){
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, ""));

    }

}
