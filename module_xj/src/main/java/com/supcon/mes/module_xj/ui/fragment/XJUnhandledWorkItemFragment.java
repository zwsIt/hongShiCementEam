package com.supcon.mes.module_xj.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.app.annotation.BindByTag;
import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.beans.GalleryBean;
import com.supcon.mes.mbap.beans.SheetEntity;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomSheetDialog;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.XJHistoryEntity;
import com.supcon.mes.middleware.model.bean.XJHistoryEntityDao;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntity;
import com.supcon.mes.middleware.model.event.ImageDeleteEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.ThermometerEvent;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.CameraManager;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.FaultPicHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.module_xj.IntentRouter;
import com.supcon.mes.module_xj.R;
import com.supcon.mes.module_xj.constant.XJConstant;
import com.supcon.mes.module_xj.model.bean.XJHistorySheetEntity;
import com.supcon.mes.module_xj.ui.HSXJWorkItemActivity;
import com.supcon.mes.module_xj.ui.adapter.XJHistorySheetAdapter;
import com.supcon.mes.module_xj.ui.adapter.XJWorkItemListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.Flowable;

/**
 * Created by wangshizhan on 2018/3/27.
 * Email:wangshizhan@supcon.com
 */

public class XJUnhandledWorkItemFragment extends BaseRefreshRecyclerFragment<XJWorkItemEntity> {

    @BindByTag("contentView")
    RecyclerView contentView;

    private XJWorkItemListAdapter mXJWorkItemListAdapter;

    public List<XJWorkItemEntity> getXjUfWorkItemEntities() {
        return xjUfWorkItemEntities;
    }

    private List<XJWorkItemEntity> xjUfWorkItemEntities = new ArrayList<>();
    private SinglePickController<String> mSingPickerCtrl;

    private List<String> resultList = new ArrayList<>();  //结果列表
    private List<SystemCodeEntity> conclusionList = new ArrayList<>(); //结论列表
    private List<String> conclusionStrList = new ArrayList<>(); //结论列表Str
    private List<SystemCodeEntity> passReasonList = new ArrayList<>(); //跳过原因
    private List<String> passReasonStrList = new ArrayList<>(); //跳过原因Str

    private String thermometervalue = ""; // 全局测温值

    private CameraManager cameraManager;

    private int mCameraPosition = -1;

    private CustomGalleryView customGalleryView;

    @Override
    protected IListAdapter createAdapter() {
        mXJWorkItemListAdapter = new XJWorkItemListAdapter(context);
        return mXJWorkItemListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.frag_xj_item_unhandle;
    }


    @Override
    protected void onInit() {
        super.onInit();
        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(false);
        mSingPickerCtrl = new SinglePickController<>(this.getActivity());
        mSingPickerCtrl.setCanceledOnTouchOutside(true);

        cameraManager = new CameraManager(getActivity(), Constant.PicType.XJ_PIC);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        super.initData();
        conclusionList = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.REAL_VALUE);
        passReasonList = SystemCodeManager.getInstance().getSystemCodeListByCode(Constant.SystemCode.PASS_REASON);
        //解析ObjList 到 StrList
        initStrList(conclusionList, passReasonList);
    }

    @Override
    protected void initView() {
        super.initView();
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));

//        ImageView imageView = new ImageView(context);
//        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,DisplayUtil.dip2px(60,context));
//        imageView.setLayoutParams(layoutParams);
//        imageView.setImageResource(R.drawable.pic_banner01);
//        contentView.addView(imageView,0);
        initEmptyView();
    }

    @Override
    protected void onRegisterController() {
        super.onRegisterController();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initListener() {
        super.initListener();

//        contentView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//                ((ScrollView) ((HSXJWorkItemActivity) context).findViewById(R.id.scrollView)).smoothScrollTo(0, contentView.computeVerticalScrollOffset());
//
//            }
//        });

        refreshListController.setOnRefreshListener(() -> {
            //TODO...
            ((HSXJWorkItemActivity) Objects.requireNonNull(getActivity())).refreshData();
        });

        mXJWorkItemListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {

            XJWorkItemEntity xjWorkItemEntity;
            int imgIndex;

            if (obj instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) obj;
                imgIndex = (int) map.get("imgIndex");
                xjWorkItemEntity = (XJWorkItemEntity) map.get("obj");

            } else {
                xjWorkItemEntity = (XJWorkItemEntity) obj;
                imgIndex = 0;
            }

            String tag = (String) childView.getTag();

            switch (tag) {

                case "ufItemSelectResult":
                    if (action == -1) {
                        xjWorkItemEntity.result = null;
                    } else {
                        if (XJConstant.MobileEditType.CHECKBOX.equals(xjWorkItemEntity.editType)) {//多选
                            dialogMoreChoice(xjWorkItemEntity, position);
                        } else {
                            showResultPicker(((CustomSpinner) childView).getSpinnerValue(), xjWorkItemEntity, position);
                        }
                    }

                    break;
                case "ufItemConclusion":
                    if (action == -1) {
                        xjWorkItemEntity.conclusionID = null;
                        xjWorkItemEntity.conclusionName = null;
                    } else {
                        showConclusionPicker(xjWorkItemEntity, position);
                    }

                    break;

                case "ufItemSkipBtn":
                    if (!xjWorkItemEntity.isPass) {
                        SnackbarHelper.showError(rootView, "该巡检项不允许跳过");
                    } else {
                        showSkipReasonPicker(xjWorkItemEntity);
                    }
                    break;

                case "thermometerBtn":
                    xjWorkItemEntity.result = thermometervalue;
                    mXJWorkItemListAdapter.notifyItemChanged(position);
                    break;

                case "fHistoryBtn":

                    showHistories(xjWorkItemEntity);

                    break;
                case "ufItemPics":
                    customGalleryView = (CustomGalleryView) childView;
                    switch (action) {
                        case CustomGalleryView.ACTION_TAKE_PICTURE_FROM_CAMERA:    //拍照保存
                            cameraManager.startCamera(Constant.IMAGE_SAVE_XJPATH);
                            break;
                        case CustomGalleryView.ACTION_TAKE_PICTURE_FROM_GALLERY:
                            cameraManager.startGallery(Constant.IMAGE_SAVE_XJPATH);
                            break;
                        case CustomGalleryView.ACTION_DELETE:   //删除

                            new CustomDialog(context)
                                    .twoButtonAlertDialog("是否删除图片")
                                    .bindView(R.id.redBtn, "")
                                    .bindView(R.id.grayBtn, "")
                                    .bindClickListener(R.id.redBtn, v -> {
                                        customGalleryView = (CustomGalleryView) childView;

                                        customGalleryView.deletePic(imgIndex);

                                        List<String> imgNamesList = Arrays.asList(xjWorkItemEntity.xjImgUrl.split(","));

                                        String xjImgUrl = xjWorkItemEntity.xjImgUrl;
                                        String imgName = imgNamesList.get(imgIndex);

//                                        cameraManager.deleteBitmap(Constant.IMAGE_SAVE_XJPATH + imgName);
                                        cameraManager.deleteBitmap(imgName);

                                        if (xjImgUrl.startsWith(imgName)) {
                                            if (xjImgUrl.equals(imgName)) {
                                                xjWorkItemEntity.xjImgUrl = xjImgUrl.replace(imgName, "");
                                            } else {
                                                xjWorkItemEntity.xjImgUrl = xjImgUrl.replace(imgName + ",", "");
                                            }
                                        } else {
                                            xjWorkItemEntity.xjImgUrl = xjImgUrl.replace("," + imgName, "");
                                        }
                                        if (TextUtils.isEmpty(xjWorkItemEntity.xjImgUrl)) {  //实际拍照字段还原
                                            xjWorkItemEntity.realIsPhone = 0;
                                        }

                                        mXJWorkItemListAdapter.notifyItemChanged(position);

                                    }, true)
                                    .bindClickListener(R.id.grayBtn, v -> {

                                    }, true)
                                    .show();

                            break;

                        case CustomGalleryView.ACTION_VIEW:    //放大查看

                            List<GalleryBean> galleryBeanList = customGalleryView.getGalleryAdapter().getList();

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("images", (Serializable) FaultPicHelper.getImagePathList(galleryBeanList));
                            bundle.putInt("position", imgIndex);  //点击位置索引

                            int[] location = new int[2];
                            childView.getLocationOnScreen(location);  //点击图片的位置
                            bundle.putInt("locationX", location[0]);
                            bundle.putInt("locationY", location[1]);

                            bundle.putInt("width", DisplayUtil.dip2px(100, context));//必须
                            bundle.putInt("height", DisplayUtil.dip2px(100, context));//必须

                            bundle.putBoolean("isEditable", true);  //删除图标
                            getActivity().getWindow().setWindowAnimations(R.style.fadeStyle);
                            IntentRouter.go(context, Constant.Router.IMAGE_VIEW, bundle);


                            break;
                        default:

                    }

                    mCameraPosition = position;
                    break;
                default:
            }

        });

        cameraManager.setOnSuccessListener(new OnSuccessListener<File>() {
            @Override
            public void onSuccess(File result) {
                XJWorkItemEntity xjWorkItemEntity = mXJWorkItemListAdapter.getItem(mCameraPosition);
                if (TextUtils.isEmpty(xjWorkItemEntity.xjImgUrl)) {
                    xjWorkItemEntity.xjImgUrl = cameraManager.getPicPaths();
//                    xjWorkItemEntity.xjImgUrl = result.getAbsolutePath();
                } else {
                    xjWorkItemEntity.xjImgUrl += "," + cameraManager.getPicPaths();
//                    xjWorkItemEntity.xjImgUrl += "," + result.getAbsolutePath();
                }
                if (!TextUtils.isEmpty(xjWorkItemEntity.xjImgUrl)) {  //拍照时才可以修改(拍照但是又X掉，该字段为空)
                    xjWorkItemEntity.realIsPhone = 1;
                }
                mXJWorkItemListAdapter.notifyItemChanged(mCameraPosition);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteImage(ImageDeleteEvent imageDeleteEvent) {
        List<String> picStrs = FaultPicHelper.getImagePathList(customGalleryView.getGalleryAdapter().getList());
        int position = -1;
        boolean isMatch = false;
        for (int i = 0; !isMatch && picStrs.size() > 0; i++) {
            String name = picStrs.get(i);
            if (name.equals(imageDeleteEvent.getPicName())) {
                position = picStrs.indexOf(name);
                isMatch = true;
            }
        }

        if (position != -1) {
//            qxglEditGalleryView.deletePic(position);
            customGalleryView.deletePic(position);
//            deleteBitmap(position, picPaths.get(position));
            cameraManager.deleteBitmap(imageDeleteEvent.getPicName());

            String imgName = imageDeleteEvent.getPicName().substring(imageDeleteEvent.getPicName().lastIndexOf(File.separator) + 1);
            XJWorkItemEntity xjWorkItemEntity = mXJWorkItemListAdapter.getItem(mCameraPosition);
            String xjImgUrl = xjWorkItemEntity.xjImgUrl;
            if (xjImgUrl.startsWith(imgName)) {
                if (xjImgUrl.equals(imgName)) {
                    xjWorkItemEntity.xjImgUrl = xjImgUrl.replace(imgName, "");
                } else {
                    xjWorkItemEntity.xjImgUrl = xjImgUrl.replace(imgName + ",", "");
                }
            } else {
                xjWorkItemEntity.xjImgUrl = xjImgUrl.replace("," + imgName, "");
            }
            if (TextUtils.isEmpty(xjWorkItemEntity.xjImgUrl)) {  //实际拍照字段还原
                xjWorkItemEntity.realIsPhone = 0;
            }

            mXJWorkItemListAdapter.notifyItemChanged(mCameraPosition);

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        cameraManager.onActivityResult(requestCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * @description 历史弹框
     * @author zhangwenshuai1
     * @date 2018/5/8
     */
    @SuppressLint("CheckResult")
    private void showHistories(XJWorkItemEntity xjWorkItemEntity) {
        //查询最近三条（本地数据库会因为数据的下载导致历史增多）
        List<XJHistoryEntity> histories = EamApplication.dao().getXJHistoryEntityDao().queryBuilder()
                .where(XJHistoryEntityDao.Properties.WorkItemId.eq(xjWorkItemEntity.itemId), XJHistoryEntityDao.Properties.Ip.eq(EamApplication.getIp()))
                .orderDesc(XJHistoryEntityDao.Properties.DateTime).limit(3).list();

        if (histories.size() <= 0) {
            SnackbarHelper.showMessage(rootView, "无历史数据可查看");
            return;
        }
        XJHistorySheetAdapter adapter = new XJHistorySheetAdapter(context);
        List<XJHistorySheetEntity> historySheetEntities = new ArrayList<>();

        Flowable.fromIterable(histories)
                .compose(RxSchedulers.io_main())
                .map(xjHistoryEntity -> {

                    XJHistorySheetEntity entity = new XJHistorySheetEntity();
                    entity.id = xjHistoryEntity.id;
                    entity.content = xjHistoryEntity.content;
                    entity.result = xjHistoryEntity.result;
                    entity.conclusion = xjHistoryEntity.conclusion;
                    entity.dateTime = xjHistoryEntity.dateTime;
                    entity.eamName = xjHistoryEntity.eamName;

                    return entity;
                })
                .subscribe(sheetEntity -> historySheetEntities.add(sheetEntity),
                        throwable -> {
                        },
                        () -> new CustomSheetDialog(context)
                                .list("历史记录", historySheetEntities, adapter)
                                .show());


    }


    @SuppressLint("CheckResult")
    public void setUnhandledWorkItemList(List<XJWorkItemEntity> result) {
        if (mXJWorkItemListAdapter != null && mXJWorkItemListAdapter.getList() != null && mXJWorkItemListAdapter.getList().size() != 0) {
            mXJWorkItemListAdapter.clear();
        }
        Flowable.fromIterable(result)
                .compose(RxSchedulers.io_main())
                .filter(xjWorkItemEntity -> !xjWorkItemEntity.isFinished)
                .subscribe(xjWorkItemEntity -> xjUfWorkItemEntities.add(xjWorkItemEntity), throwable -> {

                }, () -> {
                    refreshListController.refreshComplete(xjUfWorkItemEntities);
                    if (xjUfWorkItemEntities.size() <= 0){
                        ((HSXJWorkItemActivity)context).showButtonBar(false,0);
                    }else {
                        ((HSXJWorkItemActivity)context).showButtonBar(true,0);
                    }
                });

    }

    /**
     * @author zhangwenshuai1
     * @date 2018/3/31
     * @description 结果筛选框  单选/是否
     */
    private void showResultPicker(String selectedValue, XJWorkItemEntity xjWorkItemEntity, int position) {

        resultList = Arrays.asList(xjWorkItemEntity.candidateValue.split(","));
        int current = findPosition(selectedValue, resultList);
        mSingPickerCtrl.list(resultList).listener((index, item) -> {
            xjWorkItemEntity.result = resultList.get(index);

            if (xjWorkItemEntity.isAutoJudge && xjWorkItemEntity.normalRange != null) {

                String[] normalRangeArr = xjWorkItemEntity.normalRange.split(",");
                if (Arrays.asList(normalRangeArr).contains(xjWorkItemEntity.result)) {
                    xjWorkItemEntity.conclusionID = "realValue/01";
                    xjWorkItemEntity.conclusionName = "正常";
                } else {
                    xjWorkItemEntity.conclusionID = "realValue/02";
                    xjWorkItemEntity.conclusionName = "异常";
                }

            }

            mXJWorkItemListAdapter.notifyItemChanged(position);

        }).show(current);

    }

    /**
     * @description 多选
     * @author zhangwenshuai1
     * @date 2018/5/2
     */
    private void dialogMoreChoice(XJWorkItemEntity xjWorkItemEntity, int xjPosition) {
        if (xjWorkItemEntity.candidateValue == null || xjWorkItemEntity.candidateValue.isEmpty()) {
            SnackbarHelper.showError(rootView, "无结果候选值");
            return;
        }


        String[] items = xjWorkItemEntity.candidateValue == null ? null : xjWorkItemEntity.candidateValue.split(",");  //候选值列表
        List<SheetEntity> list = new ArrayList<>();
        for (String item : items) {
            SheetEntity sheetEntity = new SheetEntity();
            sheetEntity.name = item;
            list.add(sheetEntity);
        }

        List<Boolean> checkedList = new ArrayList<>();
        for (String s : items) {
            if (xjWorkItemEntity.result != null && xjWorkItemEntity.result.contains(s)) {
                checkedList.add(true);
            } else {
                checkedList.add(false);
            }
        }

        new CustomSheetDialog(context)
                .multiSheet("多选列表", list, checkedList)
                .setOnItemChildViewClickListener((childView, position, action, obj) -> {

                    List<SheetEntity> sheetEntities = GsonUtil.jsonToList(obj.toString(), SheetEntity.class);

                    if (sheetEntities != null && sheetEntities.size() > 0) {

                        xjWorkItemEntity.result = "";
                        for (SheetEntity sheetEntity : sheetEntities) {
                            xjWorkItemEntity.result += sheetEntity.name + ",";
                        }

                        xjWorkItemEntity.result = xjWorkItemEntity.result.substring(0, xjWorkItemEntity.result.length() - 1);

                        mXJWorkItemListAdapter.notifyItemChanged(xjPosition);

                    }
                }).show();

    }

    /**
     * @author zhangwenshuai1
     * @date 2018/3/29
     * @description 结论筛选框
     */
    private void showConclusionPicker(XJWorkItemEntity xjWorkItemEntity, int position) {
        int current = findPosition(xjWorkItemEntity, conclusionList);
        if (conclusionStrList.size() <= 0) {
            return;
        }
        mSingPickerCtrl.list(conclusionStrList).listener((index, item) -> {
            SystemCodeEntity realValueInfo = conclusionList.get(index);  //两个List的index位置一样
            xjWorkItemEntity.conclusionID = realValueInfo.id;
            xjWorkItemEntity.conclusionName = realValueInfo.value;
            mXJWorkItemListAdapter.notifyItemChanged(position);
        }).show(current);

    }


    /**
     * @author zhangwenshuai1
     * @date 2018/4/4
     * @description 跳过原因筛选框
     */
    private void showSkipReasonPicker(XJWorkItemEntity xjWorkItemEntity) {
        if (passReasonStrList.size() <= 0) {
            return;
        }
        mSingPickerCtrl.list(passReasonStrList).listener((index, item) -> {
            SystemCodeEntity passReasonInfo = passReasonList.get(index);
            xjWorkItemEntity.skipReasonID = passReasonInfo.id;
            xjWorkItemEntity.skipReasonName = passReasonInfo.value;
            xjWorkItemEntity.realIsPass = 1;
            xjWorkItemEntity.isFinished = true;
            xjWorkItemEntity.linkState = XJConstant.MobileWiLinkState.SKIP_STATE;//跳检
            xjWorkItemEntity.endTime = DateUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
            xjWorkItemEntity.staffId = EamApplication.getAccountInfo().staffId;
            xjWorkItemEntity.conclusionID = null;
            xjWorkItemEntity.conclusionName = null;
            xjWorkItemEntity.result = null;
            EamApplication.dao().getXJWorkItemEntityDao().update(xjWorkItemEntity);
            EventBus.getDefault().post(new RefreshEvent());
        }).show();
    }

    /**
     * @author zhangwenshuai1
     * @date 2018/4/2
     * @description 选择的位置index
     */
    private int findPosition(String value, List<String> list) {

        for (int i = 0; i < list.size(); i++) {
            if (value.equals(list.get(i))) {
                return i;
            }
        }
        return 0;
    }

    /**
     * @author zhangwenshuai1
     * @date 2018/4/4
     * @description 选择的位置index
     */
    private int findPosition(XJWorkItemEntity xjWorkItemEntity, List<SystemCodeEntity> list) {

        for (int i = 0; i < list.size(); i++) {
            SystemCodeEntity realValueInfo = list.get(i);
            if (xjWorkItemEntity.conclusionID == null) {
                return 0;
            }
            if (xjWorkItemEntity.conclusionID.equals(realValueInfo.id)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * @author zhangwenshuai1
     * @date 2018/4/9
     * @description 解析上拉菜单数据
     */
    private void initStrList(List<SystemCodeEntity> conclusionList, List<SystemCodeEntity> passReasonList) {
        for (SystemCodeEntity realValueInfo : conclusionList) {
            conclusionStrList.add(realValueInfo.value);
        }
        for (SystemCodeEntity passReasonInfo : passReasonList) {
            passReasonStrList.add(passReasonInfo.value);
        }

    }

    /**
     * @author zhangwenshuai1
     * @date 2018/4/9
     * @description 初始化无数据
     */
    private void initEmptyView() {
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, ""));

    }

    public void removeItem(int pos) {
        mXJWorkItemListAdapter.notifyItemRemoved(pos);
        mXJWorkItemListAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getThermometerVal(ThermometerEvent thermometerEvent) {
        LogUtil.i("ThermometerEvent", thermometerEvent.getThermometerVal());
        thermometervalue = thermometerEvent.getThermometerVal().replace("℃", "");

    }


}
