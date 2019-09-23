package com.supcon.mes.module_sbda_online.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.text.TextUtils;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.fragment.BaseRefreshFragment;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomExpandableTextView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.EAMStatusHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_sbda_online.R;
import com.supcon.mes.module_sbda_online.model.api.RoutineAPI;
import com.supcon.mes.module_sbda_online.model.api.SBDAOnlineListAPI;
import com.supcon.mes.module_sbda_online.model.bean.RoutineCommonEntity;
import com.supcon.mes.module_sbda_online.model.bean.RoutineEntity;
import com.supcon.mes.module_sbda_online.model.bean.SBDAOnlineEntity;
import com.supcon.mes.module_sbda_online.model.bean.SBDAOnlineListEntity;
import com.supcon.mes.module_sbda_online.model.contract.RoutineContract;
import com.supcon.mes.module_sbda_online.model.contract.SBDAOnlineListContract;
import com.supcon.mes.module_sbda_online.presenter.RoutinePresenter;
import com.supcon.mes.module_sbda_online.presenter.SBDAOnlineListPresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/3/29
 * ------------- Description -------------
 * 常规
 */
@Presenter(value = {SBDAOnlineListPresenter.class, RoutinePresenter.class})
public class RoutineFragment extends BaseRefreshFragment implements RoutineContract.View, SBDAOnlineListContract.View {

    @BindByTag("eamCode")
    CustomTextView eamCode;

    @BindByTag("eamName")
    CustomTextView eamName;

    @BindByTag("eamModel")
    CustomTextView eamModel;

    @BindByTag("eamType")
    CustomTextView eamType;

    @BindByTag("eamState")
    CustomTextView eamState;

    @BindByTag("eamUseDept")
    CustomTextView eamUserDept;

    @BindByTag("eamDutyStaff")
    CustomTextView eamDutyStaff;

    @BindByTag("eamAbc")
    CustomTextView eamAbc;

    @BindByTag("installPlace")
    CustomTextView installPlace;

    @BindByTag("areaNum")
    CustomTextView areaNum;

    @BindByTag("electricStaff")
    CustomTextView electricStaff;

    @BindByTag("inspectionStaff")
    CustomTextView inspectionStaff;

    @BindByTag("repairStaff")
    CustomTextView repairStaff;

//    @BindByTag("produceCode")
//    CustomTextView produceCode;
//
//    @BindByTag("produceFirm")
//    CustomTextView produceFirm;
//
//    @BindByTag("produceDate")
//    CustomTextView produceDate;

//    @BindByTag("installFirm")
//    CustomTextView installFirm;

    //    @BindByTag("fileDate")
//    CustomTextView fileDate;
//
//    @BindByTag("useYear")
//    CustomTextView useYear;
//
//    @BindByTag("haveRunState")
//    CustomTextView haveRunState;
//
//    @BindByTag("specialty")
//    CustomTextView specialty;
    @BindByTag("cumulativeRunTime")
    CustomTextView cumulativeRunTime;

    @BindByTag("cumulativeDownTime")
    CustomTextView cumulativeDownTime;

    @BindByTag("cumulativeDownNum")
    CustomTextView cumulativeDownNum;

    private static Long eamId;
    private static String eamCodes;

    private Handler handler = new Handler();
    private Runnable runnable;

    public static RoutineFragment newInstance(Long id, String code) {
        eamId = id;
        eamCodes = code;
        RoutineFragment fragment = new RoutineFragment();
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_routine;
    }

    @Override
    protected void initView() {
        super.initView();
        refreshController.setAutoPullDownRefresh(true);
        refreshController.setPullDownRefreshEnabled(false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        refreshController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!TextUtils.isEmpty(eamCodes)) {
                    Map<String, Object> queryParam = new HashMap<>();
                    queryParam.put(Constant.BAPQuery.EAM_CODE, eamCodes);
                    presenterRouter.create(SBDAOnlineListAPI.class).getSearchSBDA(queryParam, 1);
                }
                presenterRouter.create(RoutineAPI.class).getEamOtherInfo(eamId);
            }
        });
    }

    @Override
    public void getSearchSBDASuccess(SBDAOnlineListEntity entity) {
        if (entity.result.size() > 0) {
            SBDAOnlineEntity sbdaOnlineEntity = entity.result.get(0);
            setTextValue(eamCode, sbdaOnlineEntity.code);
            setTextValue(eamName, sbdaOnlineEntity.name);
            setTextValue(eamModel, sbdaOnlineEntity.model);
            setTextValue(eamType, sbdaOnlineEntity.getEamType().name);
            setTextValue(eamState, EAMStatusHelper.getType(sbdaOnlineEntity.state));
            setTextValue(eamUserDept, sbdaOnlineEntity.getUseDept().name);
            setTextValue(eamDutyStaff, sbdaOnlineEntity.getDutyStaff().name);
            setTextValue(eamAbc, sbdaOnlineEntity.abcForDisplay);
            setTextValue(installPlace, sbdaOnlineEntity.getInstallPlace().name);
            setTextValue(areaNum, sbdaOnlineEntity.areaNum);
            setTextValue(electricStaff, sbdaOnlineEntity.getElectricStaff().name);
            setTextValue(inspectionStaff, sbdaOnlineEntity.getInspectionStaff().name);
            setTextValue(repairStaff, sbdaOnlineEntity.getRepairStaff().name);
        }
        refreshController.refreshComplete();
    }

    @Override
    public void getSearchSBDAFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshController.refreshComplete();
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void getEamOtherInfoSuccess(RoutineCommonEntity entity) {

        if (!TextUtils.isEmpty(entity.result.getRunInfos().lastValue)) {
            if (entity.result.getRunInfos().lastValue.equals("1")) {
                timeRun(entity.result.getRunInfos());
            }
        }
        String run = transFormTime(strTimeFormat(entity.result.getRunInfos().runDay), strTimeFormat(entity.result.getRunInfos().runHH)
                , strTimeFormat(entity.result.getRunInfos().runMM), strTimeFormat(entity.result.getRunInfos().runSS));
        cumulativeRunTime.setValue(run);
        String stop = transFormTime(strTimeFormat(entity.result.getRunInfos().stopDay), strTimeFormat(entity.result.getRunInfos().stopHH)
                , strTimeFormat(entity.result.getRunInfos().stopMM), strTimeFormat(entity.result.getRunInfos().stopSS));
        setTextValue(cumulativeDownTime, stop);
        setTextValue(cumulativeDownNum, strTimeFormat(entity.result.getRunInfos().stopNum));
    }

    @Override
    public void getEamOtherInfoFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }


    private int day, hour, minute, second;

    public void timeRun(RoutineEntity.RunInfo runInfo) {
        try {
            day = Util.strToInt(runInfo.runDay);
            hour = Util.strToInt(runInfo.runHH);
            minute = Util.strToInt(runInfo.runMM);
            second = Util.strToInt(runInfo.runSS);
            runnable = new Runnable() {
                public void run() {
                    if (0 <= second && second < 59) {
                        second += 1;
                    } else {
                        second = 0;
                        if (minute < 59) {
                            minute += 1;
                        } else {
                            minute = 0;
                            if (hour < 23) {
                                hour += 1;
                            } else {
                                hour = 0;
                                day += 1;
                            }
                        }
                    }
                    String run = transFormTime(strTimeFormat(String.valueOf(day)), strTimeFormat(String.valueOf(hour))
                            , strTimeFormat(String.valueOf(minute)), strTimeFormat(String.valueOf(second)));
                    cumulativeRunTime.setValue(run);
                    //发送到主线程
                    handler.postDelayed(this, 1000);
                }
            };
            handler.postDelayed(runnable, 1000); // 开始Timer
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

//    private void updateData() {
//        if (null == sbdaOnlineEntity) return;
//        LogUtil.e(sbdaOnlineEntity.toString());
//        setTextValue(eamCode, sbdaOnlineEntity.code);
//        setTextValue(eamName, sbdaOnlineEntity.name);
//        setTextValue(eamModel, sbdaOnlineEntity.model);
//        setTextValue(eamType, sbdaOnlineEntity.getEamType().name);
//        setTextValue(eamState, EAMStatusHelper.getType(sbdaOnlineEntity.state));
//        setTextValue(eamUserDept, sbdaOnlineEntity.getUseDept().name);
//        setTextValue(eamDutyStaff, sbdaOnlineEntity.getDutyStaff().name);
//        setTextValue(eamAbc, sbdaOnlineEntity.abcForDisplay);
//        setTextValue(installPlace, sbdaOnlineEntity.getInstallPlace().name);
//        setTextValue(areaNum, sbdaOnlineEntity.areaNum);
//        setTextValue(eamSpecif, sbdaOnlineEntity.model);
//        setTextValue(eamUseDate, transformDateFormat(sbdaOnlineEntity.useDate));
//        setTextValue(produceCode, sbdaOnlineEntity.produceCode);
//        setTextValue(produceFirm, sbdaOnlineEntity.produceFirm);
//        setTextValue(produceDate, transformDateFormat(sbdaOnlineEntity.produceDate));
//        setTextValue(installFirm, sbdaOnlineEntity.installFirm);
//        setTextValue(fileDate, transformDateFormat(sbdaOnlineEntity.fileDate));
//        setTextValue(useYear, String.valueOf(sbdaOnlineEntity.useYear));
//        setTextValue(haveRunState, sbdaOnlineEntity.haveRunState ? "是" : "否");
//        setTextValue(specialty, sbdaOnlineEntity.specialtyNew);
//        setTextValue(fileState, sbdaOnlineEntity.getFileState().value);
//    }

    private String transformDateFormat(Long date) {
        if (date == null) return "";
        return DateUtil.dateFormat(date, "yyyy-MM-dd hh:mm:ss");
    }

    private void setTextValue(Object textView, String text) {
        if (TextUtils.isEmpty(text) || "null".equals(text.trim())) return;
        if (textView instanceof CustomTextView) {
            ((CustomTextView) textView).setValue(text);
        } else if (textView instanceof CustomExpandableTextView) {
            ((CustomExpandableTextView) textView).setText(text);
        }
    }

    @SuppressLint("StringFormatMatches")
    private String transFormTime(String day, String hour, String minute, String second) {
        String time = String.format(getString(R.string.device_style9, day, hour, minute, second));
        return time;
    }

    /**
     * int如果一位前边加0 如01
     *
     * @param str
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String strTimeFormat(String str) {
        if (str == null || TextUtils.isEmpty(str)) {
            return "0";
        }
        str = String.format("%02d", Util.strToInt(str));
        return str;
    }


}
