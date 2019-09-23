package com.supcon.mes.module_data_manage.ui;

import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BasePresenterActivity;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomDownloadView;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.constant.DataModule;
import com.supcon.mes.middleware.model.event.DataUploadEvent;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_data_manage.R;
import com.supcon.mes.module_data_manage.service.DataManagerService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by zhangwenshuai1 on 2018/3/15.
 */
@Router(Constant.Router.SJSC)
public class SJSCActivity extends BasePresenterActivity {

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("rightBtn")
    CustomImageButton rightBtn;


    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("itemXJUpload")
    CustomDownloadView itemXJUpload;

//    @BindByTag("itemQXUpload")
//    CustomDownloadView itemQXUpload;

    @BindByTag("itemYHUpload")
    CustomDownloadView itemYHUpload;

    @BindByTag("upLoadBtn")
    Button upLoadBtn;

    private List<DataModule> uploadList = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        return R.layout.ac_sjsc;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        loaderController.setIntervalTime(500);
    }

    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> {
            finish();
        });

        itemXJUpload.setOnCheckedListener(isChecked -> {
            if (isChecked){
                if(!uploadList.contains(DataModule.XJ))
                    uploadList.add(DataModule.XJ);
            }else {
                if(uploadList.contains(DataModule.XJ))
                    uploadList.remove(DataModule.XJ);
            }
        });

        itemYHUpload.setOnCheckedListener(isChecked -> {
            if(isChecked) {
                if(!uploadList.contains(DataModule.YH))
                    uploadList.add(DataModule.YH);
            }else  {
                if(uploadList.contains(DataModule.YH))
                    uploadList.remove(DataModule.YH);
            }
        });

//        itemQXUpload.setOnCheckedListener(isTagChecked -> {
//            if (isTagChecked){
//                if(!uploadList.contains(DataModule.QX))
//                    uploadList.add(DataModule.QX);
//            }else {
//                if(uploadList.contains(DataModule.QX))
//                    uploadList.remove(DataModule.QX);
//            }
//        });


        RxView.clicks(upLoadBtn).throttleFirst(2, TimeUnit.SECONDS).subscribe(o -> {
            if (uploadList.size() == 0){
                SnackbarHelper.showMessage(rootView,"请选择上传数据");
                return;
            }

            //TODO...
            continueUpload();
        });

        RxView.clicks(rightBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(o -> {
            String text = rightBtn.getText();
            if("全选".equals(text)){
                rightBtn.setText("全不选");
                selectAll();
            }
            else{
                rightBtn.setText("全选");
                unselectAll();
            }
        });


    }

    private void unselectAll() {
        itemXJUpload.setLoadStatus(true);
        itemYHUpload.setLoadStatus(true);
//        itemQXUpload.setLoadStatus(true);
    }

    private void selectAll() {
        itemXJUpload.setLoadStatus(false);
        itemYHUpload.setLoadStatus(false);
//        itemQXUpload.setLoadStatus(false);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.themeColor);
        titleText.setText("数据上传");
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setText("全选");

        String xjMsg = SharedPreferencesUtils.getParam(context, Constant.SPKey.UPLOAD_XJ, "");
        itemXJUpload.setDownloadDate(xjMsg);

//        String qxMsg = SharedPreferencesUtils.getParam(context, Constant.SPKey.UPLOAD_QX, "");
//        itemQXUpload.setDownloadDate(qxMsg);

        String yhMes = SharedPreferencesUtils.getParam(context, Constant.SPKey.UPLOAD_YH, "");
        itemYHUpload.setDownloadDate(yhMes);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent event){
        continueUpload();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUploadResult(DataUploadEvent event){

        if(event.isSuccess()){
            updateViewForResultSuccess(event);
            if(!continueUpload()){
//                onLoadSuccess("");
                closeLoader();
                rightBtn.setText("全选");
            }
        }
        else {
            String msg = ErrorMsgHelper.msgParse(event.getMsg());
            if(TextUtils.isEmpty(msg)){
                return;
            }
            DataModule dataModule = DataModule.getModule(event.getModuleName());

            updateViewForResultFailed(msg, dataModule);

            if (!continueUpload()) {
//                onLoadFailed(msg);
                closeLoader();
                rightBtn.setText("全选");
            }

        }
    }





    private synchronized boolean continueUpload() {
        if(uploadList.size()!=0){

            DataModule dataModule = uploadList.get(0);
            DataManagerService.upload(context, dataModule);
            LogUtil.d("正在上传："+ dataModule.getModuelName());
            onLoading("正在上传"+dataModule.getModuelName());
            startUpload(dataModule);
            return true;
        }

        return false;
    }

    private void startUpload(DataModule dataModule) {
        if(dataModule.equals(DataModule.XJ)){
            itemXJUpload.startLoader();
        }
//        else if(dataModule.equals(DataModule.QX)){
//            itemQXUpload.startLoader();
//        }
        else if(dataModule.equals(DataModule.YH)) {
            itemYHUpload.startLoader();
        }
    }

    private void updateViewForResultFailed(String msg, DataModule dataModule) {

        CustomDownloadView customDownloadView = null;
//        String spKey = null;
        StringBuilder msgSb = new StringBuilder(msg);
        if(dataModule.equals(DataModule.XJ)){
            customDownloadView = itemXJUpload;
//            spKey = Constant.SPKey.UPLOAD_XJ;
        }
//        else if(dataModule.equals(DataModule.QX)){
//            customDownloadView = itemQXUpload;
////            spKey = Constant.SPKey.UPLOAD_QX;
//        }
        else if(dataModule.equals(DataModule.YH)) {
            customDownloadView = itemYHUpload;
        }
        msgSb.append("\t");
        msgSb.append(DateUtil.dateTimeFormat(System.currentTimeMillis()));

        String info = msgSb.toString().split("\t")[0];
//        String date = msgSb.toString().split("\t")[1];

        customDownloadView.stopLoader(false);
        customDownloadView.setDownloadInfo(info, false);
        customDownloadView.setLoadStatus(true);
//        customDownloadView.setDownloadDate("更新时间 "+DateUtil.dateTimeFormat(System.currentTimeMillis()));
//        SharedPreferencesUtils.setParam(context, spKey, msgSb.toString());

    }

    private void updateViewForResultSuccess(DataUploadEvent event) {
        CustomDownloadView customDownloadView = null;
        String spKey = null;
//        StringBuilder msgSb = new StringBuilder(event.getModuleName());
        StringBuilder msgSb = new StringBuilder();
        msgSb.append(event.getSize());
        msgSb.append("条数据上传成功！");
        if(!TextUtils.isEmpty(event.getMsg()))msgSb.append(event.getMsg());
        if(event.getModuleName().equals(DataModule.XJ.getModuelName())){
            customDownloadView = itemXJUpload;
            spKey = Constant.SPKey.UPLOAD_XJ;
        }
//        else if(event.getModuleName().equals(DataModule.QX.getModuelName())){
//            customDownloadView = itemQXUpload;
//            spKey = Constant.SPKey.UPLOAD_QX;
//        }
        else if(event.getModuleName().equals(DataModule.YH.getModuelName())){
            customDownloadView = itemYHUpload;
            spKey = Constant.SPKey.UPLOAD_YH;
        }
        msgSb.append("\t上传时间:");
        msgSb.append(DateUtil.dateTimeFormat(System.currentTimeMillis()));

        String info = msgSb.toString().split("\t")[0];
        String date = msgSb.toString().split("\t")[1];

        customDownloadView.stopLoader(true);
        customDownloadView.setDownloadInfo(info, true);
        customDownloadView.setLoadStatus(true);
        customDownloadView.setDownloadDate(date);
        SharedPreferencesUtils.setParam(context, spKey, date);
    }


}
