package com.supcon.mes.module_xj.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalSpinner;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.XJExemptionEntity;
import com.supcon.mes.middleware.model.bean.XJWorkItemEntity;
import com.supcon.mes.middleware.model.bean.YHEntityVo;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.FaultPicHelper;
import com.supcon.mes.middleware.util.SP2ThermometerHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_xj.IntentRouter;
import com.supcon.mes.module_xj.R;
import com.supcon.mes.module_xj.constant.XJConstant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.query.Query;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by wangshizhan on 2018/3/27.
 * Email:wangshizhan@supcon.com
 */

public class XJWorkItemListAdapter extends BaseListDataRecyclerViewAdapter<XJWorkItemEntity> {

    private SP2ThermometerHelper sp2ThermometerHelper;

    public XJWorkItemListAdapter(Context context) {
        super(context);
        initThermometer();  //初始化测温
    }

    public XJWorkItemListAdapter(Context context, List<XJWorkItemEntity> list) {
        super(context, list);
    }

    @Override
    public int getItemViewType(int position, XJWorkItemEntity xjWorkItemEntity) {
        return xjWorkItemEntity.isFinished ? 1 : 0;
    }

    @Override
    protected BaseRecyclerViewHolder<XJWorkItemEntity> getViewHolder(int viewType) {
        if (viewType == 1) {
            return new ViewHolderFinished(context);
        }
        return new ViewHolderUnFinished(context);
    }

    /**
     * @description 测温
     * @author zhangwenshuai1
     * @date 2018/4/28
     */
    private void initThermometer() {
        sp2ThermometerHelper = SP2ThermometerHelper.getInstance();
//        sp2ThermometerHelper.setup((Activity) context);
//        sp2ThermometerHelper.setOnThermometerListener(thermometerVal -> EventBus.getDefault().post(new ThermometerEvent(thermometerVal)));
    }


    class ViewHolderUnFinished extends BaseRecyclerViewHolder<XJWorkItemEntity> implements OnChildViewClickListener {

        String oldImgUrl = "";  //原图片url，为测温时刷新视图使用，不然页面显示跳动问题

        @BindByTag("workItemIndex")
        TextView workItemIndex;

        @BindByTag("eamName")
        CustomTextView eamName;  //设备名称

        @BindByTag("ufItemContent")
        CustomTextView ufItemContent;  //内容

        @BindByTag("ufItemSelectResult")
        CustomSpinner ufItemSelectResult;  //选择结果

        @BindByTag("ufItemInputResult")
        CustomEditText ufItemInputResult;  //输入结果

        @BindByTag("ufItemConclusion")
        CustomVerticalSpinner ufItemConclusion;  //结论

        @BindByTag("ufItemRemark")
        CustomEditText ufItemRemark;  //备注

        @BindByTag("customGalleryLl")
        LinearLayout customGalleryLl;

        @BindByTag("ufItemPics")
        CustomGalleryView ufItemPics; //拍照展示

        @BindByTag("takePhoto")
        ImageView takePhoto; //拍照

        @BindByTag("ufItemSaveBtn")
        Button ufItemSaveBtn; //保存

        @BindByTag("ufItemEndBtn")
        Button ufItemEndBtn; //完成

        @BindByTag("ufItemSkipBtn")
        Button ufItemSkipBtn; //跳过

        @BindByTag("yhRecordBtn")
        Button yhRecordBtn; //缺陷录入

        @BindByTag("fHistoryBtn")
        Button fHistoryBtn; //历史

        @BindByTag("thermometerBtn")
        Button thermometerBtn;  //测温,为了解决触动闪烁问题，使用TextView控件

        @BindByTag("ufItemPart")
        CustomTextView ufItemPart;  //部位
        @BindByTag("llNormalRange")
        LinearLayout llNormalRange;
        @BindByTag("ufItemNormalRange")
        CustomTextView ufItemNormalRange;
        @BindByTag("customGalleryInclude")
        View customGalleryInclude;
        @BindByTag("dividedView")
        View dividedView;
        @BindByTag("dividedViewIc")
        View dividedViewIc;


        public ViewHolderUnFinished(Context context) {
            super(context);
        }

        @Override
        protected void initBind() {
            super.initBind();
        }

        @Override
        protected void initView() {
            super.initView();

        }

        @SuppressLint({"CheckResult", "ClickableViewAccessibility"})
        @Override
        protected void initListener() {
            super.initListener();

            ufItemSelectResult.setOnChildViewClickListener(this);
            ufItemConclusion.setOnChildViewClickListener(this);

            ufItemSaveBtn.setOnClickListener(v -> {

                XJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                xjWorkItemEntity.realRemark = ufItemRemark.getInput().trim();
                EamApplication.dao().getXJWorkItemEntityDao().update(xjWorkItemEntity);
                SnackbarHelper.showMessage(itemView, "保存成功");

            });

            ufItemEndBtn.setOnClickListener(v -> {
                XJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());

                if (TextUtils.isEmpty(xjWorkItemEntity.result)) {
                    SnackbarHelper.showError(itemView, "请填写结果");
                    return;
                }
//                if (TextUtils.isEmpty(xjWorkItemEntity.conclusionID)) {
//                    SnackbarHelper.showError(itemView, "请填写结论");
//                    return;
//                }
                if (xjWorkItemEntity.isPhone && xjWorkItemEntity.realIsPhone == 0) {
                    SnackbarHelper.showError(itemView, "该巡检项要求拍照");
                    return;
                }

                oldImgUrl = "";

                xjWorkItemEntity.realRemark = ufItemRemark.getInput().trim();
                xjWorkItemEntity.endTime = DateUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
                xjWorkItemEntity.isFinished = true;
                xjWorkItemEntity.linkState = XJConstant.MobileWiLinkState.FINISHED_STATE;
                xjWorkItemEntity.staffId = EamApplication.getAccountInfo().staffId;
                EamApplication.dao().getXJWorkItemEntityDao().update(xjWorkItemEntity);

//                if (XJConstant.MobileConclusion.AB_NORMAL.equals(xjWorkItemEntity.conclusionID)){
//                    ((HSXJWorkItemActivity)context).doSaveEditYh(xjWorkItemEntity);
//                }

                //TODO 免检逻辑:单选或是否存在免检规则
                /*传入任何SQL片段到WHERE字句*/
                if (XJConstant.MobileEditType.WHETHER.equals(xjWorkItemEntity.editType) || XJConstant.MobileEditType.RADIO.equals(xjWorkItemEntity.editType)) {
//                    List<XJExemptionEntity> xjExemptionEntities = EamApplication.dao().getXJExemptionEntityDao().queryBuilder()
//                            .where(new WhereCondition.StringCondition("itemId = "+xjWorkItemEntity.itemId),new WhereCondition.StringCondition("result = "+xjWorkItemEntity.result)).list();
//                    QueryBuilder queryBuilder = EamApplication.dao().getXJExemptionEntityDao().queryBuilder();
                    Query query = EamApplication.dao().getXJExemptionEntityDao()
                            .queryRawCreate("WHERE T.ITEM_ID = ? AND T.RESULT = ? AND T.IP = ? ", xjWorkItemEntity.itemId, TextUtils.isEmpty(xjWorkItemEntity.result) ? "" : xjWorkItemEntity.result, EamApplication.getIp());
                    List<XJExemptionEntity> xjExemptionEntities = query.list();

                    List<XJWorkItemEntity> xjWorkItemEntityList = XJWorkItemListAdapter.this.getList();

                    for (XJExemptionEntity xjExemptionEntity : xjExemptionEntities) {
                        for (XJWorkItemEntity xjWorkItemEntity1 : xjWorkItemEntityList) {
                            if (xjExemptionEntity.exemptionItemId.equals(xjWorkItemEntity1.itemId) && !xjWorkItemEntity1.isFinished) {
                                xjWorkItemEntity1.linkState = XJConstant.MobileWiLinkState.EXEMPTION_STATE;
                                xjWorkItemEntity1.isFinished = true;
                                xjWorkItemEntity1.endTime = DateUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
                                xjWorkItemEntity1.staffId = EamApplication.getAccountInfo().staffId;
                                xjWorkItemEntity1.result = null;  // 存在默认值时，需要清空
                                xjWorkItemEntity1.conclusionName = null;
                                xjWorkItemEntity1.conclusionID = null;
                                EamApplication.dao().getXJWorkItemEntityDao().update(xjWorkItemEntity1);
                                break;
                            }

                        }
                    }
                }
                List<XJWorkItemEntity> xjWorkItemEntityList = XJWorkItemListAdapter.this.getList();
                xjWorkItemEntityList.remove(getAdapterPosition());
                ToastUtils.show(context, "成功完成");
                EventBus.getDefault().post(new RefreshEvent(Constant.RefreshAction.XJ_WORK_END, getAdapterPosition()));
            });

            ufItemSkipBtn.setOnClickListener(v -> {
                XJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                oldImgUrl = "";
                onItemChildViewClick(ufItemSkipBtn, 0, xjWorkItemEntity);
            });


            RxView.clicks(yhRecordBtn).throttleFirst(2, TimeUnit.SECONDS).subscribe(o -> {
                XJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                if (xjWorkItemEntity.equipmentId == null || TextUtils.isEmpty(xjWorkItemEntity.equipmentId)) {
                    SnackbarHelper.showMessage(itemView, "无设备信息，不能录入缺陷信息");
                    return;
                }

//                CommonDeviceEntity commonDeviceEntity = EamApplication.dao()
//                        .getCommonDeviceEntityDao()
//                        .queryBuilder()
//                        .where(CommonDeviceEntityDao.Properties.EamId.eq(xjWorkItemEntity.equipmentId))
//                        .where(CommonDeviceEntityDao.Properties.Ip.eq(EamApplication.getIp()))
//                        .unique();


//                if (commonDeviceEntity == null){
//                    SnackbarHelper.showMessage(itemView,"未从本地数据库查询到设备信息");
//                    return;
//                }

                /*YHEntity yhEntity = new YHEntity();
                yhEntity.findStaffID = EamApplication.me();
                yhEntity.findTime = System.currentTimeMillis();
                yhEntity.cid = SharedPreferencesUtils.getParam(App.getAppContext(), Constant.CID, 0L);
                WXGDEam wxgdEam = new WXGDEam();
                wxgdEam.name = commonDeviceEntity.eamName;
                wxgdEam.code = commonDeviceEntity.eamCode;
                wxgdEam.model = commonDeviceEntity.eamModel;
                wxgdEam.id  =  commonDeviceEntity.eamId;
                yhEntity.eamID = wxgdEam;*/

                YHEntityVo yhEntityVo = new YHEntityVo();
                yhEntityVo.setFindStaffName(EamApplication.getAccountInfo().staffName);
                yhEntityVo.setFindStaffId(EamApplication.getAccountInfo().staffId);
                yhEntityVo.setFindDate(DateUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                yhEntityVo.setSrcID(xjWorkItemEntity.taskId);
                yhEntityVo.setTaskId(xjWorkItemEntity.id);
                yhEntityVo.setAreaInstallId("".equals(xjWorkItemEntity.eamAreaId) ? null : Long.valueOf(xjWorkItemEntity.eamAreaId));
                yhEntityVo.setAreaInstallName(xjWorkItemEntity.eamAreaName);
                yhEntityVo.setEamId(Long.valueOf(xjWorkItemEntity.equipmentId));
                yhEntityVo.setEamName(xjWorkItemEntity.equipmentName);
//                yhEntityVo.setEamModel(commonDeviceEntity != null ? commonDeviceEntity.eamModel : null);
                yhEntityVo.setSourceTypeFunc();

                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.IntentKey.YHGL_ENTITY, yhEntityVo);
                IntentRouter.go(context, Constant.Router.OFFLINE_YH_EDIT, bundle);
            });

            RxTextView.textChanges(ufItemInputResult.editText())
                    .skipInitialValue()
                    .subscribe(charSequence -> {
                        XJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                        if (TextUtils.isEmpty(charSequence)) {
                            if (xjWorkItemEntity != null)
                                xjWorkItemEntity.result = charSequence.toString();
                        } else {
                            if ("数字".equals(xjWorkItemEntity.valueType) && XJConstant.MobileEditType.INPUTE.equals(xjWorkItemEntity.editType)) {  //值类型判断：字符/数字

                                if (xjWorkItemEntity.isAutoJudge) {

                                    //结论自动判定
                                    if (autoJudgeConclusion(xjWorkItemEntity, charSequence.toString())) {
                                        if ("realValue/02".equals(xjWorkItemEntity.conclusionID)) {
//                                            ufItemConclusion.setSpinnerColor(context.getResources().getColor(R.color.customRed));
                                            ufItemConclusion.setContentTextColor(context.getResources().getColor(R.color.customRed));
                                        } else {
//                                            ufItemConclusion.setSpinnerColor(Color.parseColor("#000000"));
                                            ufItemConclusion.setContentTextColor(context.getResources().getColor(R.color.black));
                                        }
                                        ufItemConclusion.setSpinner(xjWorkItemEntity.conclusionName);
                                    }

                                } else {

                                    if (XJConstant.MobileEditType.INPUTE.equals(xjWorkItemEntity.editType)) { //录入模式
                                        if (!charSequence.toString().matches("^-?[0.0-9]+$") || charSequence.toString().indexOf(".") == 0) {
                                            if ("-".equals(charSequence.toString())) {
                                                return;
                                            }
                                            ToastUtils.show(context, "请输入数字类型");
                                        } else {

                                            if (charSequence.toString().indexOf(".") > 0) {
                                                if (xjWorkItemEntity.decimal != null) {
                                                    if (charSequence.toString().substring(charSequence.toString().indexOf(".") + 1).length() > Integer.parseInt(xjWorkItemEntity.decimal)) {
                                                        ToastUtils.show(context, "结果将四舍五入保留" + xjWorkItemEntity.decimal + "位");
                                                    }
                                                }
                                            }

                                            BigDecimal bigDecimal = new BigDecimal(charSequence.toString());
                                            if (xjWorkItemEntity.decimal != null) {
                                                xjWorkItemEntity.result = bigDecimal.setScale(Integer.parseInt(xjWorkItemEntity.decimal), BigDecimal.ROUND_HALF_UP).toString();
                                            }

                                        }
                                    }

                                }

                            } else {
                                xjWorkItemEntity.result = charSequence.toString();
                            }

                        }

                    });

            ufItemInputResult.editText().setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    //TODO...
                } else {
                    XJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                    if (xjWorkItemEntity != null) {
                        ufItemInputResult.setInput(xjWorkItemEntity.result);
                    }

                }
            });

            RxTextView.textChanges(ufItemRemark.editText())
//                    .debounce(1, TimeUnit.SECONDS)
                    .skipInitialValue()
                    .subscribe(charSequence -> {
                        XJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                        if (TextUtils.isEmpty(charSequence)) {
                            if (xjWorkItemEntity != null)
                                xjWorkItemEntity.realRemark = "";
                        } else {
                            xjWorkItemEntity.realRemark = charSequence.toString();
                        }

                    });

            fHistoryBtn.setOnClickListener(v -> {
                onItemChildViewClick(fHistoryBtn, 0, getItem(getAdapterPosition()));

            });

            //触发测温
            thermometerBtn.setOnTouchListener((v, motionEvent) -> {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!sp2ThermometerHelper.startOrEnd(true)) {
                        SnackbarHelper.showError(itemView, "设备启动测温异常，请检查设备配置测温模块");
                    } else {
                        //TODO  success
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    sp2ThermometerHelper.startOrEnd(false);
                }

                XJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                onItemChildViewClick(thermometerBtn, 0, xjWorkItemEntity);
                return true;
            });

            /* *//*说明：对TextView 类型增加点击事件，不然MotionEvent只会执行ACTION_DOWN事件，UP事件不执行，若为Button类型则无需增加单击事件*//*
            thermometerBtn.setOnClickListener(v -> {});*/

            ufItemPics.setOnChildViewClickListener((childView, action, obj) -> {

                //限制9张
//                if (action == CustomGalleryView.ACTION_TAKE_PICTURE_FROM_CAMERA) {
//                    List<GalleryBean> galleryBeanList = ufItemPics.getGalleryAdapter().getList();
//                    if (galleryBeanList != null && galleryBeanList.size() >= 9) {
//                        ToastUtils.show(context, "限制9张照片");
//                        return;
//                    }
//                }

                Map<String, Object> map = new HashMap<>();
                map.put("imgIndex", Integer.valueOf(obj.toString()));
                map.put("obj", getItem(getAdapterPosition()));

                onItemChildViewClick(ufItemPics, action, map);

            });

            takePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ufItemPics.findViewById(R.id.customCameraIv).performClick();  //调用CustomGalleryView的拍照按钮
                }
            });

            eamName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    XJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                    if ("".equals(xjWorkItemEntity.equipmentId)) {
                        ToastUtils.show(context, "无设备详情可查看！");
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putLong(Constant.IntentKey.SBDA_ENTITY_ID, Long.valueOf(xjWorkItemEntity.equipmentId));
                    IntentRouter.go(context, Constant.Router.SBDA_VIEW, bundle);
                }
            });

            RxTextView.textChanges(ufItemSelectResult.getCustomSpinner())
                    .subscribe(charSequence -> {
                        XJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());

                        if (xjWorkItemEntity != null && xjWorkItemEntity.isAutoJudge && xjWorkItemEntity.normalRange != null &&
                                TextUtils.isEmpty(xjWorkItemEntity.result) && !TextUtils.isEmpty(xjWorkItemEntity.defaultVal)) {

                            String[] normalRangeArr = xjWorkItemEntity.normalRange.split(",");
                            if (Arrays.asList(normalRangeArr).contains(xjWorkItemEntity.defaultVal)) {
                                xjWorkItemEntity.conclusionID = "realValue/01";
                                xjWorkItemEntity.conclusionName = "正常";
                            } else {
                                xjWorkItemEntity.conclusionID = "realValue/02";
                                xjWorkItemEntity.conclusionName = "异常";
                            }
                            xjWorkItemEntity.result = xjWorkItemEntity.defaultVal;

                        }
//                        notifyItemChanged(getAdapterPosition());
                    });

        }

        @Override
        protected int layoutId() {
            return R.layout.item_xj_work_item_unfinished;
        }

        @Override
        protected void update(XJWorkItemEntity data) {

            if (getAdapterPosition() == 0) {
                dividedViewIc.setVisibility(View.VISIBLE);
                eamName.setVisibility(View.VISIBLE);
                ufItemPart.setVisibility(View.VISIBLE);
                dividedView.setVisibility(View.GONE);
            } else {
                if (!TextUtils.isEmpty(data.equipmentId) && data.equipmentId.equals(getItem(getAdapterPosition() - 1).equipmentId)) {
                    dividedViewIc.setVisibility(View.GONE);
                    eamName.setVisibility(View.GONE);
                    dividedView.setVisibility(View.GONE);
                } else {
                    dividedViewIc.setVisibility(View.VISIBLE);
                    eamName.setVisibility(View.VISIBLE);
                    dividedView.setVisibility(View.VISIBLE);
                }

                if (!TextUtils.isEmpty(data.part) && data.part.equals(getItem(getAdapterPosition()-1).part)){
                    ufItemPart.setVisibility(View.GONE);
                }else {
                    ufItemPart.setVisibility(View.VISIBLE);
                }

            }

            workItemIndex.setText(String.valueOf(getAdapterPosition() + 1));
            if ("".equals(data.equipmentName)) {
                eamName.setContent("无关联设备");
            } else {
                eamName.setContent(data.equipmentName);
            }

            ufItemContent.setValue(data.itemContent);
            ufItemPart.setValue(data.part);

            if (TextUtils.isEmpty(data.normalRange)) {
                llNormalRange.setVisibility(View.GONE);
            } else {
                llNormalRange.setVisibility(View.VISIBLE);
            }
            ufItemNormalRange.setContent(data.normalRange);

            if (XJConstant.MobileEditType.INPUTE.equals(data.editType)) {   //录入框
                ufItemSelectResult.setVisibility(View.GONE);
                ufItemInputResult.setVisibility(View.VISIBLE);

                if (data.isThermometric) {  //要求测温
                    ufItemInputResult.setEditable(false);
                    ufItemInputResult.setHint("测温值");
                    thermometerBtn.setVisibility(View.VISIBLE);
                } else {
                    ufItemInputResult.setEditable(true);
                    ufItemInputResult.setHint("请输入结果");
                    thermometerBtn.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(data.unitName)) {
                    ufItemInputResult.setText("结果 (单位:" + data.unitName + ")");
                    ufItemInputResult.setNecessary(true);
                }

                if (!TextUtils.isEmpty(data.defaultVal) && TextUtils.isEmpty(data.result)) {
                    ufItemInputResult.setInput(data.defaultVal);
                } else {
                    ufItemInputResult.setInput(data.result);
                }

                if ("数字".equals(data.valueType)) {
                    ufItemInputResult.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);  //支持正负浮点数、整数
                } else {
                    ufItemInputResult.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                }

            } else if (XJConstant.MobileEditType.RADIO.equals(data.editType) || XJConstant.MobileEditType.WHETHER.equals(data.editType)) {  //是否  单选
                ufItemInputResult.setVisibility(View.GONE);
                ufItemSelectResult.setVisibility(View.VISIBLE);
                thermometerBtn.setVisibility(View.GONE);

                if (!TextUtils.isEmpty(data.unitName)) {
                    ufItemSelectResult.setText("结果 (单位:" + data.unitName + ")");
                    ufItemSelectResult.setNecessary(true);
                }


                if (!TextUtils.isEmpty(data.defaultVal) && TextUtils.isEmpty(data.result)) {
                    ufItemSelectResult.setSpinner(data.defaultVal);
                    data.result = data.defaultVal;
                } else {
                    ufItemSelectResult.setSpinner(data.result);
                }

            } else if (XJConstant.MobileEditType.CHECKBOX.equals(data.editType)) {  //多选
                ufItemInputResult.setVisibility(View.GONE);
                ufItemSelectResult.setVisibility(View.VISIBLE);
                thermometerBtn.setVisibility(View.GONE);

                if (!TextUtils.isEmpty(data.unitName)) {
                    ufItemSelectResult.setText("结果 (单位:" + data.unitName + ")");
                    ufItemSelectResult.setNecessary(true);
                }


                if (!TextUtils.isEmpty(data.defaultVal) && TextUtils.isEmpty(data.result)) {
                    ufItemSelectResult.setSpinner(data.defaultVal);
                    data.result = data.defaultVal;
                } else {
                    ufItemSelectResult.setSpinner(data.result);
                }

            }

            if (data.isAutoJudge) {
                ufItemConclusion.setEditable(false);
                ufItemConclusion.setNecessary(false);
            } else {
                ufItemConclusion.setEditable(true);
                ufItemConclusion.setNecessary(true);
            }
            ufItemConclusion.setSpinner(data.conclusionName);

            if ("realValue/02".equals(data.conclusionID)) {
                ufItemConclusion.setContentTextColor(context.getResources().getColor(R.color.customRed));
            } else {
                ufItemConclusion.setContentTextColor(context.getResources().getColor(R.color.textColorlightblack));
            }

            if (data.isPhone) {
                ufItemPics.setNecessary(true);
            } else {
                ufItemPics.setNecessary(false);
            }

            ufItemRemark.setInput(data.realRemark);

            //保存图片
            if (!TextUtils.isEmpty(data.xjImgUrl)) {
//                customGalleryLl.setVisibility(View.VISIBLE);
//                ufItemPics.setVisibility(View.VISIBLE);
//                ufItemPics.findViewById(R.id.customGallery).setVisibility(View.VISIBLE);
//                ufItemPics.getGalleryAdapter().notifyDataSetChanged();
                customGalleryInclude.setVisibility(View.VISIBLE);
                ufItemPics.setPadding(0, 5, 10, 5);
                if (oldImgUrl.equals(data.xjImgUrl)) {
                    return;
                }

                FaultPicHelper.initPics(Arrays.asList(data.xjImgUrl.split(",")), ufItemPics);
                oldImgUrl = data.xjImgUrl;
            } else {
//                customGalleryLl.setVisibility(View.GONE);
                customGalleryInclude.setVisibility(View.GONE);
                ufItemPics.setPadding(0, 0, 10, 0);
                ufItemPics.clear();
                oldImgUrl = "";
            }

            if (data.isPass){
                ufItemSkipBtn.setVisibility(View.VISIBLE);
            }else {
                ufItemSkipBtn.setVisibility(View.GONE);
            }
            if (data.isThermometric){
                thermometerBtn.setVisibility(View.VISIBLE);
            }else {
                thermometerBtn.setVisibility(View.GONE);
            }

        }


        @Override
        public void onChildViewClick(View childView, int action, Object obj) {
            XJWorkItemEntity workItemEntity = getItem(getAdapterPosition());  //注：参数obj为空，实现的接口方法，接口中obj为null，非item对象
            onItemChildViewClick(childView, action, workItemEntity);
        }


        /**
         * @author zhangwenshuai1
         * @date 2018/4/11
         * @description 结论自动变动
         */
        private boolean autoJudgeConclusion(XJWorkItemEntity xjWorkItemEntity, String charSequence) {
            if (!charSequence.matches("^-?[0.0-9.0]+$") || charSequence.indexOf(".") == 0) {
                if ("-".equals(charSequence)) {
                    return false;
                }
                ToastUtils.show(context, "请输入数字类型");
            } else {

                if (charSequence.indexOf(".") > 0) {
                    if (xjWorkItemEntity.decimal != null) {
                        if (charSequence.substring(charSequence.indexOf(".") + 1).length() > Integer.parseInt(xjWorkItemEntity.decimal)) {
                            ToastUtils.show(context, "结果将四舍五入保留" + xjWorkItemEntity.decimal + "位");
                        }
                    }
                }

                BigDecimal bigDecimal = new BigDecimal(charSequence);
                if (xjWorkItemEntity.decimal != null) {
                    xjWorkItemEntity.result = bigDecimal.setScale(Integer.parseInt(xjWorkItemEntity.decimal), BigDecimal.ROUND_HALF_UP).toString();
                }


                //不同正常值范围解析判定
                if (xjWorkItemEntity.normalRange != null) {
                    if (xjWorkItemEntity.normalRange.contains("~")) {  //区间形式eg: (-12.45~-1.00)

                        return intervalJudge(xjWorkItemEntity, charSequence);

                    } else if (xjWorkItemEntity.normalRange.contains("|")) {   //区间之外形式，eg：≥-15.5|≤-35.6，≤-35.6|≥-15.5

                        return orJudge(xjWorkItemEntity, charSequence);

                    } else {  // ≥ 或 ≤ 或 ＞ 或 ＜

                        return unequalJudge(xjWorkItemEntity, charSequence);

                    }
                }

            }

            return false;
        }

        /**
         * @author zhangwenshuai1
         * @date 2018/4/12
         * @description 区间形式判定 eg: -12.45~-1.00
         */
        private boolean intervalJudge(XJWorkItemEntity xjWorkItemEntity, String charSequence) {
            String[] numArr = xjWorkItemEntity.normalRange.split("~");
            double small = Double.parseDouble(numArr[0]);
            double big = Double.parseDouble(numArr[1]);
            double inputResult = Double.parseDouble(charSequence);

            if (inputResult >= small && inputResult <= big) {  //区间内、正常

                xjWorkItemEntity.conclusionID = "realValue/01";
                xjWorkItemEntity.conclusionName = "正常";

            } else {  //异常

                xjWorkItemEntity.conclusionID = "realValue/02";
                xjWorkItemEntity.conclusionName = "异常";

            }

            return true;
        }

        /**
         * @author zhangwenshuai1
         * @date 2018/4/12
         * @description 区间形式判定 eg: ≥-15.5|≤-35.6，≤-35.6|≥-15.5
         */
        private boolean orJudge(XJWorkItemEntity xjWorkItemEntity, String charSequence) {

            String regExp = "(≥|≤|＞|＜)?";

            Pattern pattern = Pattern.compile(regExp);

            Matcher matcher = pattern.matcher(xjWorkItemEntity.normalRange);

            String[] numArr = matcher.replaceAll("").split("\\|");

            double small = Double.parseDouble(numArr[0]);
            double big = Double.parseDouble(numArr[1]);

            if (small > big) {
                small = big;
                big = Double.parseDouble(numArr[0]);
            }

            double inputResult = Double.parseDouble(charSequence);

            if (xjWorkItemEntity.normalRange.contains("≥") && xjWorkItemEntity.normalRange.contains("≤")) {
                if (inputResult > small && inputResult < big) {  //区间内、异常
                    xjWorkItemEntity.conclusionID = "realValue/02";
                    xjWorkItemEntity.conclusionName = "异常";
                } else {  //正常
                    xjWorkItemEntity.conclusionID = "realValue/01";
                    xjWorkItemEntity.conclusionName = "正常";
                }
            } else if (xjWorkItemEntity.normalRange.contains("≥") && xjWorkItemEntity.normalRange.contains("＜")) {
                if (inputResult >= small && inputResult < big) {  //区间内、异常
                    xjWorkItemEntity.conclusionID = "realValue/02";
                    xjWorkItemEntity.conclusionName = "异常";
                } else {  //正常
                    xjWorkItemEntity.conclusionID = "realValue/01";
                    xjWorkItemEntity.conclusionName = "正常";
                }
            } else if (xjWorkItemEntity.normalRange.contains("＞") && xjWorkItemEntity.normalRange.contains("≤")) {
                if (inputResult > small && inputResult <= big) {  //区间内、异常
                    xjWorkItemEntity.conclusionID = "realValue/02";
                    xjWorkItemEntity.conclusionName = "异常";
                } else {  //正常
                    xjWorkItemEntity.conclusionID = "realValue/01";
                    xjWorkItemEntity.conclusionName = "正常";
                }
            } else {
                if (inputResult >= small && inputResult <= big) {  //区间内、异常
                    xjWorkItemEntity.conclusionID = "realValue/02";
                    xjWorkItemEntity.conclusionName = "异常";
                } else {  //正常
                    xjWorkItemEntity.conclusionID = "realValue/01";
                    xjWorkItemEntity.conclusionName = "正常";
                }
            }

            return true;
        }

        /**
         * @author zhangwenshuai1
         * @date 2018/4/12
         * @description 区间形式判定 eg: ≥ 或 ≤ 或 ＞ 或 ＜
         */
        private boolean unequalJudge(XJWorkItemEntity xjWorkItemEntity, String charSequence) {

            String regExp = "(≥|≤|＞|＜)?";
            Pattern pattern = Pattern.compile(regExp);
            Matcher matcher = pattern.matcher(xjWorkItemEntity.normalRange);
            double num = Double.parseDouble(matcher.replaceAll(""));

            double inputResult = Double.parseDouble(charSequence);

            if (xjWorkItemEntity.normalRange.contains("≥")) {
                if (inputResult < num) {  //异常
                    xjWorkItemEntity.conclusionID = "realValue/02";
                    xjWorkItemEntity.conclusionName = "异常";
                } else {  //正常
                    xjWorkItemEntity.conclusionID = "realValue/01";
                    xjWorkItemEntity.conclusionName = "正常";
                }
            } else if (xjWorkItemEntity.normalRange.contains("＞")) {
                if (inputResult <= num) {  //异常
                    xjWorkItemEntity.conclusionID = "realValue/02";
                    xjWorkItemEntity.conclusionName = "异常";
                } else {  //正常
                    xjWorkItemEntity.conclusionID = "realValue/01";
                    xjWorkItemEntity.conclusionName = "正常";
                }
            } else if (xjWorkItemEntity.normalRange.contains("≤")) {
                if (inputResult > num) {  //异常
                    xjWorkItemEntity.conclusionID = "realValue/02";
                    xjWorkItemEntity.conclusionName = "异常";
                } else {  //正常
                    xjWorkItemEntity.conclusionID = "realValue/01";
                    xjWorkItemEntity.conclusionName = "正常";
                }
            } else {
                if (inputResult >= num) {  //异常
                    xjWorkItemEntity.conclusionID = "realValue/02";
                    xjWorkItemEntity.conclusionName = "异常";
                } else {  //正常
                    xjWorkItemEntity.conclusionID = "realValue/01";
                    xjWorkItemEntity.conclusionName = "正常";
                }
            }

            return true;
        }


    }

    class ViewHolderFinished extends BaseRecyclerViewHolder<XJWorkItemEntity> implements OnChildViewClickListener {

        @BindByTag("workItemIndex")
        TextView workItemIndex;

        @BindByTag("eamName")
        CustomTextView eamName;  //设备名称

        @BindByTag("xjItemContent")
        CustomTextView xjItemContent;  //内容

        @BindByTag("fItemSelectResult")
        CustomSpinner fItemSelectResult;  //选择结果

        @BindByTag("fItemInputResult")
        CustomTextView fItemInputResult;  //输入结果

        @BindByTag("fItemConclusion")
        CustomVerticalSpinner fItemConclusion;  //结论

        @BindByTag("fItemRemark")
        CustomEditText fItemRemark;  //备注

        @BindByTag("fItemPics")
        CustomGalleryView fItemPics; //拍照

        @BindByTag("fReRecordBtn")
        Button fReRecordBtn; //重录

        @BindByTag("fExemption")
        TextView fExemption; //免检

        @BindByTag("fSkip")
        TextView fSkip; //跳检

        @BindByTag("fItemPart")
        CustomTextView fItemPart;  //部位

        @BindByTag("customGalleryInclude")
        View customGalleryInclude;

        @BindByTag("buttonBar")
        LinearLayout buttonBar;

        @BindByTag("viewDivide")
        View viewDivide;
        @BindByTag("dividedView")
        View dividedView;
        @BindByTag("dividedViewIc")
        View dividedViewIc;


        public ViewHolderFinished(Context context) {
            super(context);
        }

        @Override
        protected void initBind() {
            super.initBind();
        }

        @Override
        protected void initView() {
            super.initView();
        }

        @Override
        protected void initListener() {
            super.initListener();

//            fHistoryBtn.setOnClickListener(v -> {
//                XJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
//                onItemChildViewClick(fHistoryBtn,0,xjWorkItemEntity);
//            });

            fReRecordBtn.setOnClickListener(v -> {
                XJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                onItemChildViewClick(fReRecordBtn, 0, xjWorkItemEntity);
            });

            fItemPics.setOnChildViewClickListener((childView, action, obj) -> {

//                Map<String,Object> map = new HashMap<>();
//                map.put("imgIndex",Integer.valueOf(obj.toString()));
                onItemChildViewClick(fItemPics, action, obj);

            });

            eamName.setOnClickListener(v -> {
                XJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                if ("".equals(xjWorkItemEntity.equipmentId)) {
                    ToastUtils.show(context, "无设备详情可查看！");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putLong(Constant.IntentKey.SBDA_ENTITY_ID, Long.valueOf(xjWorkItemEntity.equipmentId));
                IntentRouter.go(context, Constant.Router.SBDA_VIEW, bundle);
            });


        }


        @Override
        protected int layoutId() {
            return R.layout.item_xj_work_item_finished;
        }

        @Override
        protected void update(XJWorkItemEntity data) {
            if (getAdapterPosition() == 0) {
                fItemPart.setVisibility(View.VISIBLE);
                dividedViewIc.setVisibility(View.VISIBLE);
                eamName.setVisibility(View.VISIBLE);
                dividedView.setVisibility(View.GONE);
            } else {
                if (!TextUtils.isEmpty(data.equipmentId) && data.equipmentId.equals(getItem(getAdapterPosition() - 1).equipmentId)) {
                    dividedViewIc.setVisibility(View.GONE);
                    eamName.setVisibility(View.GONE);
                    dividedView.setVisibility(View.GONE);
                } else {
                    dividedViewIc.setVisibility(View.VISIBLE);
                    eamName.setVisibility(View.VISIBLE);
                    dividedView.setVisibility(View.VISIBLE);
                }

                if (!TextUtils.isEmpty(data.part) && data.part.equals(getItem(getAdapterPosition()-1).part)){
                    fItemPart.setVisibility(View.GONE);
                }else {
                    fItemPart.setVisibility(View.VISIBLE);
                }

            }
            workItemIndex.setText(String.valueOf(getAdapterPosition() + 1));
            if ("".equals(data.equipmentName)) {
                eamName.setContent("无关联设备");
            } else {
                eamName.setContent(data.equipmentName);
            }

            xjItemContent.setValue(data.itemContent);
            fItemRemark.setEditable(false);
            fItemRemark.setInput(data.realRemark);

            fItemPart.setValue(data.part);

            if ("wiLinkState/02".equals(data.linkState)) {  //免检
                fExemption.setVisibility(View.VISIBLE);
                fSkip.setVisibility(View.GONE);
            } else {
                fExemption.setVisibility(View.GONE);
            }

            if ("wiLinkState/03".equals(data.linkState)) { //跳检
                fSkip.setVisibility(View.VISIBLE);
                fExemption.setVisibility(View.GONE);
            } else {
                fSkip.setVisibility(View.GONE);
            }

            if (XJConstant.MobileEditType.INPUTE.equals(data.editType)) {   //录入
                fItemSelectResult.setVisibility(View.GONE);
                fItemInputResult.setVisibility(View.VISIBLE);

                fItemInputResult.setContent(data.result);

            } else if (XJConstant.MobileEditType.WHETHER.equals(data.editType) || XJConstant.MobileEditType.RADIO.equals(data.editType)) {  //是否  单选
                fItemInputResult.setVisibility(View.GONE);
                fItemSelectResult.setVisibility(View.VISIBLE);

                fItemSelectResult.setSpinner(data.result);

            } else if (XJConstant.MobileEditType.CHECKBOX.equals(data.editType)) {  //多选
                fItemInputResult.setVisibility(View.GONE);
                fItemSelectResult.setVisibility(View.VISIBLE);

                fItemSelectResult.setSpinner(data.result);
            }

            if ("realValue/02".equals(data.conclusionID)) {
//                fItemConclusion.setSpinnerColor(context.getResources().getColor(R.color.customRed));
                fItemConclusion.setContentTextColor(context.getResources().getColor(R.color.customRed));
            } else {
//                fItemConclusion.setSpinnerColor(context.getResources().getColor(R.color.textColorlightblack));
                fItemConclusion.setContentTextColor(context.getResources().getColor(R.color.textColorlightblack));
            }
            fItemConclusion.setSpinner(data.conclusionName);

            if (!TextUtils.isEmpty(data.xjImgUrl)) {
                customGalleryInclude.setVisibility(View.VISIBLE);
                fItemPics.setPadding(0, 5, 10, 5);
                String[] imgUrl = data.xjImgUrl.split(",");
                FaultPicHelper.initPics(Arrays.asList(imgUrl), fItemPics);
            }else {
                customGalleryInclude.setVisibility(View.GONE);
                fItemPics.setPadding(0, 0, 10, 0);
                fItemPics.clear();
            }

            if (data.control && !XJConstant.MobileWiLinkState.EXEMPTION_STATE.equals(data.linkState)){
                buttonBar.setVisibility(View.VISIBLE);
                viewDivide.setVisibility(View.VISIBLE);
            }else {
                buttonBar.setVisibility(View.GONE);
                viewDivide.setVisibility(View.GONE);
            }

        }

        @Override
        public void onChildViewClick(View childView, int action, Object obj) {
            XJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
            onItemChildViewClick(childView, action, xjWorkItemEntity);
        }
    }


}
