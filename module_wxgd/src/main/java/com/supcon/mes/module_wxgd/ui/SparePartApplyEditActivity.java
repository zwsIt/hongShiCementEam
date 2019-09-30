package com.supcon.mes.module_wxgd.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.view.CustomDateView;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomListWidget;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.LinkController;
import com.supcon.mes.middleware.controller.OnlineCameraController;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.controller.SparePartApplyDetailController;
import com.supcon.mes.module_wxgd.controller.TableInfoController;
import com.supcon.mes.module_wxgd.model.api.SparePartApplyAPI;
import com.supcon.mes.module_wxgd.model.bean.SparePartApplyHeaderInfoEntity;
import com.supcon.mes.module_wxgd.model.contract.SparePartApplyContract;
import com.supcon.mes.module_wxgd.presenter.SparePartApplyPresenter;
import com.supcon.mes.module_wxgd.util.SparePartReceiveMapManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description SparePartApplyEditActivity 备件领用申请编辑
 * @author  2019/9/27
 */
@Router(value = Constant.Router.SPARE_PART_APPLY_EDIT)
@Controller(value = {LinkController.class, TableInfoController.class, SparePartApplyDetailController.class,OnlineCameraController.class})
@Presenter(value = {SparePartApplyPresenter.class})
public class SparePartApplyEditActivity extends BaseRefreshActivity implements SparePartApplyContract.View {
    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    CustomImageButton rightBtn;
    @BindByTag("titleLayout")
    RelativeLayout titleLayout;
    @BindByTag("applyStaff")
    CustomTextView applyStaff;
    @BindByTag("department")
    CustomTextView department;
    @BindByTag("position")
    CustomTextView position;
    @BindByTag("applyTime")
    CustomDateView applyTime;
    @BindByTag("explain")
    CustomVerticalEditText explain;
    @BindByTag("remark")
    CustomVerticalEditText remark;
    @BindByTag("sparePartListWidget")
    CustomListWidget sparePartListWidget;
    @BindByTag("contentView")
    ScrollView contentView;
    @BindByTag("refreshFrameLayout")
    PtrFrameLayout refreshFrameLayout;
    @BindByTag("commentInput")
    CustomEditText commentInput;
    @BindByTag("workFlowView")
    CustomWorkFlowView workFlowView;
    @BindByTag("workFlowBar")
    LinearLayout workFlowBar;

    public Long tableId; // 单据ID
    private Long pendingId;
    private DatePickController datePickController;
    private SparePartApplyHeaderInfoEntity sparePartApplyHeaderInfoEntity;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_spare_part_apply_edit;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);

        tableId = getIntent().getLongExtra(Constant.IntentKey.TABLE_ID,0);
        pendingId = getIntent().getLongExtra(Constant.IntentKey.PENDING_ID,0);

        refreshController.setAutoPullDownRefresh(true);
        refreshController.setPullDownRefreshEnabled(true);

        datePickController = new DatePickController(this);
        datePickController.setCycleDisable(false);
        datePickController.setCanceledOnTouchOutside(true);
        datePickController.setSecondVisible(true);
        datePickController.textSize(18);

        getController(SparePartApplyDetailController.class).setEditable(true);
    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setText("备件领用申请编辑");
        getController(LinkController.class).initPendingTransition(workFlowView,pendingId);
    }

    protected void initTableInfoData() {
        String includes = "id,createStaffId,createTime,version,deploymentId,tableInfoId,applyStaff.id,applyStaff.code,applyStaff.name," +
                "applyStaff.mainPosition.name,applyStaff.mainPosition.department,applyStaff.mainPosition.department.name," +
                "applyTime,explain,remark,repairWork.id,repairWork.content,repairWork.eamID.name,repairWork.eamID.code";
        getController(TableInfoController.class).getSparePartApplyTableInfo(tableId,includes);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getSparePartApplyTableInfo(SparePartApplyHeaderInfoEntity entity){
        this.sparePartApplyHeaderInfoEntity = entity;
        //回填单据表头信息
        applyStaff.setContent(entity.getApplyStaff().name);
        department.setContent(entity.getApplyStaff().getMainPosition().getDepartment().name);
        position.setContent(entity.getApplyStaff().getMainPosition().name);
        applyTime.setContent(DateUtil.dateTimeFormat(entity.getApplyTime()));
        explain.setContent(entity.getExplain());
        remark.setContent(entity.getRemark());
        refreshController.refreshComplete();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStaffInfo(CommonSearchEvent commonSearchEvent) {
        if (commonSearchEvent.commonSearchEntity instanceof CommonSearchStaff) {
            CommonSearchStaff searchStaff = (CommonSearchStaff) commonSearchEvent.commonSearchEntity;
            applyStaff.setValue(searchStaff.name);

            //更新人员：
            sparePartApplyHeaderInfoEntity.getApplyStaff().id = searchStaff.id;
            sparePartApplyHeaderInfoEntity.getApplyStaff().name = searchStaff.name;
            sparePartApplyHeaderInfoEntity.getApplyStaff().code = searchStaff.code;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> onBackPressed());
        refreshController.setOnRefreshListener(() -> initTableInfoData());
        applyStaff.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
            } else {
                IntentRouter.go(context, Constant.Router.STAFF);
            }
        });
        applyTime.setOnChildViewClickListener((childView, action, obj) -> {
            if (action == -1) {
            } else {
                datePickController.listener((year, month, day, hour, minute, second) -> {
                    String dateStr = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                    applyTime.setContent(dateStr);
                }).show(TextUtils.isEmpty(applyTime.getContent()) ? new Date().getTime() : DateUtil.dateFormat(applyTime.getContent(),Constant.TimeString.YEAR_MONTH_DAY_HOUR_MIN_SEC));
            }

        });
        workFlowView.setOnChildViewClickListener(new OnChildViewClickListener() {

            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                WorkFlowVar workFlowVar = (WorkFlowVar) obj;
                switch (action) {
                    case 0:
                        doSave("");
                        break;
                    case 1:
                        doSubmit(workFlowVar);
                        break;
                    case 2:
                        doSubmit(workFlowVar);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * @param
     * @return
     * @description 单据保存
     * @author zhangwenshuai1 2019/9/28
     */
    private void doSave(String str) {
        submit(null);
    }
    /**
     * @param
     * @param workFlowVar
     * @return
     * @description 单据提交
     * @author zhangwenshuai1 2019/9/28
     */
    private void doSubmit(WorkFlowVar workFlowVar) {
        List<WorkFlowEntity> workFlowEntityList = new ArrayList<>();
        WorkFlowEntity workFlowEntity = new WorkFlowEntity();
        workFlowEntity.dec = workFlowVar.dec;
        workFlowEntity.type = workFlowVar.outcomeMapJson.get(0).type;
        workFlowEntity.outcome = workFlowVar.outCome;
        workFlowEntityList.add(workFlowEntity);

        submit(workFlowEntityList);
    }

    private void submit(List<WorkFlowEntity> workFlowEntities) {
        WorkFlowEntity workFlowEntity = null;
        if (workFlowEntities != null && workFlowEntities.size() != 0) {
            workFlowEntity = workFlowEntities.get(0);
        } else {
            workFlowEntity = new WorkFlowEntity();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("bap_validate_user_id", String.valueOf(EamApplication.getAccountInfo().userId));
        map.put("apply.createStaffId", sparePartApplyHeaderInfoEntity.getCreateStaffId());
        map.put("apply.createTime", DateUtil.dateTimeFormat(sparePartApplyHeaderInfoEntity.getCreateTime()));
        map.put("apply.createPositionId", EamApplication.getAccountInfo().positionId);
        map.put("viewCode", "BEAM2_1.0.0_sparePart_sparePartEdit");
        map.put("modelName", "Apply");
        map.put("datagridKey", "BEAM2_sparePart_apply_sparePartEdit_datagrids");
        map.put("viewselect", "sparePartEdit");
        map.put("id", tableId);
        map.put("deploymentId", sparePartApplyHeaderInfoEntity.getDeploymentId());
        map.put("apply.version", sparePartApplyHeaderInfoEntity.getVersion());
        if (workFlowEntities != null) {//保存为空
            map.put("workFlowVar.outcomeMapJson", workFlowEntities);
            map.put("workFlowVar.dec", workFlowEntity.dec);
            map.put("workFlowVar.outcome", workFlowEntity.outcome);
            map.put("operateType", "submit");
        } else {
            map.put("operateType", "save");
        }
        map.put("workFlowVar.comment", commentInput.getInput());
        map.put("taskDescription", "BEAM2_1.0.0.sparePartApply.task340");
        map.put("activityName", "task340");
        map.put("pendingId",pendingId);
        //表头信息,取修改后最新数据
        map.put("apply.applyStaff.id", sparePartApplyHeaderInfoEntity.getApplyStaff().id);
        map.put("apply.repairWork.id", sparePartApplyHeaderInfoEntity.getRepairWork().id);
        map.put("apply.applyTime", applyTime.getContent());
        map.put("apply.explain", explain.getContent());
        map.put("apply.remark", remark.getContent());
        map.put("__file_upload", true);

        List list = SparePartReceiveMapManager.dataChange(sparePartListWidget.getData());
        map.put("dg1535943746846ModelCode", "BEAM2_1.0.0_sparePart_SparePartApply");
        map.put("dg1535943746846ListJson", list.toString());
        map.put("dgLists['dg1535943746846']", list.toString());

        Map<String, Object> attachmentMap = new HashMap<>();
        getController(OnlineCameraController.class).doSave(attachmentMap);
        if (attachmentMap.size() != 0) {
            attachmentMap.put("linkId", String.valueOf(sparePartApplyHeaderInfoEntity.getTableInfoId()));
        }
        onLoading("单据处理中...");
        presenterRouter.create(SparePartApplyAPI.class).submitSparePartApply(map, attachmentMap);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void submitSparePartApplySuccess(BapResultEntity entity) {
        onLoadSuccessAndExit("处理成功！", new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                SparePartApplyEditActivity.this.finish();
                EventBus.getDefault().post(new RefreshEvent());
            }
        });
    }

    @Override
    public void submitSparePartApplyFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }
}
