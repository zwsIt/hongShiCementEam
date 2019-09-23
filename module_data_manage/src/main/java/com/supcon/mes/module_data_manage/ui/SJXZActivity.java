package com.supcon.mes.module_data_manage.ui;

import android.text.TextUtils;
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
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.constant.DataModule;
import com.supcon.mes.middleware.model.bean.XJHistoryEntity;
import com.supcon.mes.middleware.model.bean.XJHistoryEntityDao;
import com.supcon.mes.middleware.model.event.DataDownloadEvent;
import com.supcon.mes.middleware.model.event.DataParseEvent;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_data_manage.R;
import com.supcon.mes.module_data_manage.controller.SingleDataController;
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

@Router(Constant.Router.SJXZ)
public class SJXZActivity extends BasePresenterActivity {

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("rightBtn")
    CustomImageButton rightBtn;

    @BindByTag("itemTaskDownload")
    CustomDownloadView itemTaskDownload;

    @BindByTag("itemXJBaseDownload")
    CustomDownloadView itemXJBaseDownload;

    @BindByTag("itemEamBaseDownload")
    CustomDownloadView itemEamBaseDownload;

    @BindByTag("itemEamDeviceDownload")
    CustomDownloadView itemEamDeviceDownload;

    @BindByTag("downLoadBtn")
    Button downLoadBtn;

    private List<DataModule> downloadList = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        return R.layout.ac_sjxz;
    }


    @Override
    protected void onInit() {
        super.onInit();
        List<String> modules = getIntent().getStringArrayListExtra(Constant.IntentKey.DOWNLOAD_MODULES);
        if(modules!=null)
        for (String module:modules){
            downloadList.add(DataModule.getModule(module));
        }

        //后台下载，没有试图
        if(!getIntent().getBooleanExtra(Constant.IntentKey.DOWNLOAD_VISIBLE, true)){

            if(modules != null) {

                for(DataModule module :downloadList)
                {
                    DataManagerService.download(context, module);
                }

            }
            else{
                LogUtil.e("未发现可以后台下载数据！");
            }

            finish();
        }

        loaderController.setIntervalTime(500);
        EventBus.getDefault().register(this);

    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("数据下载");
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setText("全选");

        String eamBase = null;
        String eamMsg = null;
        String xjBaseMsg = null;
        String xjMsg = null;

        if(EamApplication.getAccountInfo().userId!=0
                /*&& EamApplication.getAccountInfo().userName.equals(EamApplication.getUserName())
                && EamApplication.getAccountInfo().password.equals(EamApplication.getPassword())*/
                && EamApplication.getAccountInfo().ip.equals(EamApplication.getIp())){
        }
        else if(!downloadList.contains(DataModule.XJ_BASE)){
            downloadList.add(DataModule.XJ_BASE);
        }
        xjBaseMsg = SharedPreferencesUtils.getParam(context, Constant.SPKey.DOWNLOAD_XJ_BASE, "");
        xjMsg = SharedPreferencesUtils.getParam(context, Constant.SPKey.DOWNLOAD_XJ, "");
        eamBase = SharedPreferencesUtils.getParam(context, Constant.SPKey.DOWNLOAD_EAM_BASE, "");
        eamMsg = SharedPreferencesUtils.getParam(context, Constant.SPKey.DOWNLOAD_EAM_DEVICE, "");

        itemXJBaseDownload.setDownloadDate(xjBaseMsg);
        itemTaskDownload.setDownloadDate(xjMsg);
        itemEamBaseDownload.setDownloadDate(eamBase);
        itemEamDeviceDownload.setDownloadDate(eamMsg);

        if(downloadList.size()!=0){
            SnackbarHelper.showMessage(rootView, "为保证APP正常运行，请下载必需的基础数据");
        }
        for (DataModule dataModule : downloadList){


            if(dataModule == DataModule.XJ_BASE){
                itemXJBaseDownload.setLoadStatus(false);
            }

            if(dataModule == DataModule.XJ){
                itemTaskDownload.setLoadStatus(false);
            }

            if(dataModule == DataModule.EAM_BASE){
                itemEamBaseDownload.setLoadStatus(false);
            }

            if(dataModule == DataModule.EAM_DEVICE){
                itemEamDeviceDownload.setLoadStatus(false);
            }



        }


    }

    @Override
    protected void initListener() {
        super.initListener();

        leftBtn.setOnClickListener(v -> {
            finish();
        });

        itemXJBaseDownload.setOnCheckedListener(isChecked -> {
            if (isChecked){
                if(!downloadList.contains(DataModule.XJ_BASE))
                    downloadList.add(DataModule.XJ_BASE);
            }else {
                if(downloadList.contains(DataModule.XJ_BASE))
                    downloadList.remove(DataModule.XJ_BASE);
            }
        });

        itemTaskDownload.setOnCheckedListener(isChecked -> {
            if (isChecked){
                if(!downloadList.contains(DataModule.XJ))
                    downloadList.add(DataModule.XJ);
            }else {
                if(downloadList.contains(DataModule.XJ))
                    downloadList.remove(DataModule.XJ);
            }
        });

        itemEamDeviceDownload.setOnCheckedListener(isChecked -> {
            if (isChecked){
                if(!downloadList.contains(DataModule.EAM_DEVICE))
                    downloadList.add(DataModule.EAM_DEVICE);
            }else {
                if(downloadList.contains(DataModule.EAM_DEVICE))
                    downloadList.remove(DataModule.EAM_DEVICE);
            }
        });

        itemEamBaseDownload.setOnCheckedListener(isChecked -> {
            if (isChecked){
                if(!downloadList.contains(DataModule.EAM_BASE))
                    downloadList.add(DataModule.EAM_BASE);
            }else {
                if(downloadList.contains(DataModule.EAM_BASE))
                    downloadList.remove(DataModule.EAM_BASE);
            }
        });

        RxView.clicks(downLoadBtn).throttleFirst(2, TimeUnit.SECONDS).subscribe(o -> {
            if (downloadList.size()==0){
                SnackbarHelper.showMessage(rootView,"请选择下载数据");
                return;
            }

            //TODO...
            continueDownload();
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
//        itemXJBaseDownload.setLoadStatus(true);
        itemTaskDownload.setLoadStatus(true);
        itemEamDeviceDownload.setLoadStatus(true);
//        itemEamBaseDownload.setLoadStatus(true);
    }

    private void selectAll() {
//        itemXJBaseDownload.setLoadStatus(false);
        itemTaskDownload.setLoadStatus(false);
//        itemEamBaseDownload.setLoadStatus(false);
        itemEamDeviceDownload.setLoadStatus(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent event){
            continueDownload();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownloadResult(DataDownloadEvent event){
        if(event.isSuccess()){
//            onLoading("下载完成， 正在解析数据...");
        }
        else{
            String msg = ErrorMsgHelper.msgParse(event.getMsg());
            if(TextUtils.isEmpty(msg)){//属于未登陆的情况
                return;
            }
            DataModule dataModule = DataModule.getModule(event.getModuleName());
            updateViewForResultFailed(msg, dataModule);
            if(downloadList.contains(dataModule)){
                downloadList.remove(dataModule);
            }
            if(!continueDownload()){
//                onLoadFailed(msg);
                closeLoader();
                rightBtn.setText("全选");
            }

        }

    }

    private void updateViewForResultFailed(String msg, DataModule dataModule) {

        CustomDownloadView customDownloadView = null;
        String key = null;
        StringBuilder sb = new StringBuilder(msg);
        if(DataModule.XJ.equals(dataModule)){
            customDownloadView = itemTaskDownload;
            key = Constant.SPKey.DOWNLOAD_XJ;
        }
        else if(DataModule.XJ_BASE.equals(dataModule)){
            customDownloadView = itemXJBaseDownload;
            key = Constant.SPKey.DOWNLOAD_XJ_BASE;
        }
        else if(DataModule.EAM_DEVICE.equals(dataModule)){
            customDownloadView = itemEamDeviceDownload;
            key = Constant.SPKey.DOWNLOAD_EAM_DEVICE;
        }
        else if(DataModule.EAM_BASE.equals(dataModule)){
            customDownloadView = itemEamBaseDownload;
            key = Constant.SPKey.DOWNLOAD_EAM_BASE;
        }
        sb.append("\t");
        sb.append("\t更新时间");
        sb.append(DateUtil.dateTimeFormat(System.currentTimeMillis()));

        String info = sb.toString().split("\t")[0];
//        String date = sb.toString().split("\t")[1];

        customDownloadView.stopLoader(false);
        customDownloadView.setDownloadInfo(info, false);
        customDownloadView.setLoadStatus(true);

        if(!TextUtils.isEmpty(msg) && msg.contains("服务不存在")){//基本信息下载失败，后续不用再下载了，直接报错
            if(Constant.SPKey.DOWNLOAD_EAM_BASE.equals(key)){
                if(downloadList.contains(DataModule.EAM_DEVICE)){
                    downloadList.remove(DataModule.EAM_DEVICE);
                    itemEamDeviceDownload.stopLoader(false);
                    itemEamDeviceDownload.setDownloadInfo(info, false);
                    itemEamDeviceDownload.setLoadStatus(true);
//                    SharedPreferencesUtils.setParam(context, Constant.SPKey.DOWNLOAD_EAM_DEVICE, sb.toString());
                }
            }
            else if(Constant.SPKey.DOWNLOAD_XJ_BASE.equals(key)){
                if(downloadList.contains(DataModule.XJ)){
                    downloadList.remove(DataModule.XJ);
                    itemTaskDownload.stopLoader(false);
                    itemTaskDownload.setDownloadInfo(info, false);
                    itemTaskDownload.setLoadStatus(true);
//                    SharedPreferencesUtils.setParam(context, Constant.SPKey.DOWNLOAD_XJ, sb.toString());
                }
            }
        }
    }

    private void updateViewForResultSuccess(DataParseEvent event) {

        CustomDownloadView customDownloadView = null;
        String key = null;
        StringBuilder msgSb = new StringBuilder();
//        msgSb.append(event.getModuleName());
        if(event.getModuleName().equals(DataModule.XJ.getModuelName())){
            msgSb.append(event.getSize());
            msgSb.append("条数据");
            customDownloadView = itemTaskDownload;
            key = Constant.SPKey.DOWNLOAD_XJ;
        }
        else if(event.getModuleName().equals(DataModule.XJ_BASE.getModuelName())){
            customDownloadView = itemXJBaseDownload;
            key = Constant.SPKey.DOWNLOAD_XJ_BASE;
        }
        else if(event.getModuleName().equals(DataModule.EAM_DEVICE.getModuelName())){
//            msgSb.append(event.getSize());
//            msgSb.append("条数据");
            customDownloadView = itemEamDeviceDownload;
            key = Constant.SPKey.DOWNLOAD_EAM_DEVICE;
        }
        else if(event.getModuleName().equals(DataModule.EAM_BASE.getModuelName())){
            customDownloadView = itemEamBaseDownload;
            key = Constant.SPKey.DOWNLOAD_EAM_BASE;
        }
        msgSb.append("下载成功!");
        msgSb.append("\t更新时间：");
        msgSb.append(DateUtil.dateTimeFormat(System.currentTimeMillis()));

        String info = msgSb.toString().split("\t")[0];
        String date = msgSb.toString().split("\t")[1];

        customDownloadView.stopLoader(true);
        customDownloadView.setDownloadInfo(info, true);
        customDownloadView.setLoadStatus(true);

        customDownloadView.setDownloadDate(date);
        SharedPreferencesUtils.setParam(context, key, date);

        //TODO...历史表仅保留最近三条
        if(event.getModuleName().equals(DataModule.XJ.getModuelName()) && event.getSize() > 0 ){
            dealHistory();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onParseResult(DataParseEvent event){

        if(event.isSuccess()){
            updateViewForResultSuccess(event);
            if(!continueDownload()){
//                onLoadSuccess();
                closeLoader();
                rightBtn.setText("全选");
            }

        }
        else{
            String msg = ErrorMsgHelper.msgParse(event.getMsg());
            DataModule dataModule = DataModule.getModule(event.getModuleName());
            updateViewForResultFailed(msg, dataModule);
            if(!continueDownload()){
//                onLoadFailed(msg);
                closeLoader();
                rightBtn.setText("全选");
            }
        }

    }



    private boolean continueDownload() {

        if(downloadList.size()!=0){
            DataModule dataModule = downloadList.get(0);
            DataManagerService.download(context, dataModule);
            LogUtil.d("正在下载："+ dataModule.getModuelName());
            onLoading("正在下载"+dataModule.getModuelName());
            startDownload(dataModule);
            return true;
        }

        return false;
    }

    private void startDownload(DataModule dataModule) {
        if(dataModule.equals(DataModule.XJ)){
            itemTaskDownload.startLoader();
        }
        else if(dataModule.equals(DataModule.XJ_BASE)){
            itemXJBaseDownload.startLoader();
        }
        else if(dataModule.equals(DataModule.EAM_DEVICE)){
            itemEamDeviceDownload.startLoader();
        }
        else if(dataModule.equals(DataModule.EAM_BASE)){
            itemEamBaseDownload.startLoader();
        }
    }


    /**
     * @description 处理历史表仅保留最近三条历史数据
     * @author zhangwenshuai1
     * @date 2018/5/16
     *
     */
    private void dealHistory(){
        XJHistoryEntityDao xjHistoryEntityDao = EamApplication.dao().getXJHistoryEntityDao();
        List<XJHistoryEntity> xjHistoryEntityList = xjHistoryEntityDao.queryBuilder().orderAsc(XJHistoryEntityDao.Properties.WorkItemId,XJHistoryEntityDao.Properties.DateTime).list();
        for (XJHistoryEntity xjHistoryEntity : xjHistoryEntityList){
            List<XJHistoryEntity> entities = xjHistoryEntityDao.queryBuilder().where(XJHistoryEntityDao.Properties.WorkItemId.eq(xjHistoryEntity.workItemId))
                    .orderAsc(XJHistoryEntityDao.Properties.WorkItemId,XJHistoryEntityDao.Properties.DateTime).list();
            if (entities.size() > 3){
                for (int i= 0 ;i< entities.size()-3;i++){
                    EamApplication.dao().getXJHistoryEntityDao().delete(entities.get(i));
                }
            }
        }
    }



}
