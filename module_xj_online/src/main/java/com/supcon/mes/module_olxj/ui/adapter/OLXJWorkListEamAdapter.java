package com.supcon.mes.module_olxj.ui.adapter;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.GalleryBean;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomAdView;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomSwitchButton;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalSpinner;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.EamPicController;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.FaultPicHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_olxj.IntentRouter;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.constant.OLXJConstant;
import com.supcon.mes.module_olxj.controller.DeviceDCSParamController;
import com.supcon.mes.module_olxj.controller.OLXJCameraController;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;
import com.supcon.mes.module_olxj.ui.OLXJWorkListEamUnHandledActivity;
import com.supcon.mes.module_olxj.ui.OLXJWorkListUnHandledActivity;
import com.supcon.mes.sb2.util.SB2ThermometerHelper;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 巡检到设备 先留着 防止又要改回去
 *
 * @deprecated
 */
public class OLXJWorkListEamAdapter extends BaseListDataRecyclerViewAdapter<OLXJWorkItemEntity> {

    private SB2ThermometerHelper sp2ThermometerHelper;
    private HashSet hashSet = new HashSet();//判断dcs是否请求过，防止刷新不断请求

    public OLXJWorkListEamAdapter(Context context) {
        super(context);
        initThermometer();  //初始化测温
    }

    public OLXJWorkListEamAdapter(Context context, List<OLXJWorkItemEntity> list) {
        super(context, list);
    }

    @Override
    protected BaseRecyclerViewHolder<OLXJWorkItemEntity> getViewHolder(int viewType) {

        if (viewType == ListType.HEADER.value()) {
            return new PicViewHolder(context);
        } else if (viewType == ListType.TITLE.value()) {
            return new TitleViewHolder(context);
        } /*else if (viewType == -1) {
            return new ViewHolderFinished(context);
        }*/
        return new ViewHolderUnFinished(context);
    }


    @Override
    public int getItemViewType(int position, OLXJWorkItemEntity workItemEntity) {

        return workItemEntity.isFinished ? -1 : workItemEntity.viewType;
    }

    /**
     * @description 测温
     * @author zhangwenshuai1
     * @date 2018/4/28
     */
    private void initThermometer() {
        sp2ThermometerHelper = SB2ThermometerHelper.getInstance();
    }


    class ViewHolderUnFinished extends BaseRecyclerViewHolder<OLXJWorkItemEntity> implements OnChildViewClickListener {

        String oldImgUrl = "";  //原图片url，为测温时刷新视图使用，不然页面显示跳动问题

        @BindByTag("workItemIndex")
        TextView workItemIndex;

        @BindByTag("ufEamName")
        TextView ufEamName;  //设备名称

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

        @BindByTag("ufItemPics")
        CustomGalleryView ufItemPics; //拍照展示

        @BindByTag("ufItemPhotoBtn")
        Button ufItemPhotoBtn; //拍照

        @BindByTag("ufItemSaveBtn")
        Button ufItemSaveBtn; //保存

        @BindByTag("ufItemEndBtn")
        Button ufItemEndBtn; //完成

        @BindByTag("ufItemPartEndBtn")
        Button ufItemPartEndBtn; //部位完成

        @BindByTag("ufItemEditBtn")
        Button ufItemEditBtn; //录数据按钮

        @BindByTag("ufItemSkipBtn")
        Button ufItemSkipBtn; //跳过

        @BindByTag("fHistoryBtn")
        Button fHistoryBtn; //历史

        @BindByTag("thermometerBtn")
        Button thermometerBtn;  //测温,为了解决触动闪烁问题，使用TextView控件

        @BindByTag("vibrationBtn")
        Button vibrationBtn;

        @BindByTag("ufItemRemarkBtn")
        Button ufItemRemarkBtn;

        @BindByTag("ufItemPart")
        CustomTextView ufItemPart;  //部位

        @BindByTag("llNormalRange")
        LinearLayout llNormalRange;

        @BindByTag("ufItemNormalRange")
        CustomTextView ufItemNormalRange;

        @BindByTag("ufResultLayout")
        LinearLayout ufResultLayout;

        @BindByTag("ufBtnLayout")
        LinearLayout ufBtnLayout;

        @BindByTag("ufPartLayout")
        RelativeLayout ufPartLayout;

        @BindByTag("ufContentLine")
        View ufContentLine;

        @BindByTag("ufResultLine")
        View ufResultLine;

        @BindByTag("ufResultVerticalLine")
        View ufResultVerticalLine;

        @BindByTag("ufItemSelectResultSwitchLayout")
        LinearLayout ufItemSelectResultSwitchLayout;

        @BindByTag("ufItemSelectResultSwitch")
        CustomSwitchButton ufItemSelectResultSwitch;

        OLXJCameraController mOLXJCameraController;

        public ViewHolderUnFinished(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_olxj_work_item_unfinished;
        }

        @Override
        protected void initBind() {
            super.initBind();
        }

        @Override
        protected void initView() {
            super.initView();
            mOLXJCameraController = ((OLXJWorkListEamUnHandledActivity) context).getController(OLXJCameraController.class);
            mOLXJCameraController.init(Constant.IMAGE_SAVE_XJPATH, Constant.PicType.XJ_PIC);
        }

        @SuppressLint({"CheckResult", "ClickableViewAccessibility"})
        @Override
        protected void initListener() {
            super.initListener();

            ufItemSelectResult.setOnChildViewClickListener(this);
            ufItemConclusion.setOnChildViewClickListener(this);

            ufItemSaveBtn.setOnClickListener(v -> {

                OLXJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                xjWorkItemEntity.realRemark = ufItemRemark.getInput().trim();
                SnackbarHelper.showMessage(itemView, "保存成功");

            });

            ufItemEndBtn.setOnClickListener(v -> {
                OLXJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                if (TextUtils.isEmpty(xjWorkItemEntity.result)) {
                    SnackbarHelper.showError(itemView, "请填写结果");
                    return;
                }

                if (xjWorkItemEntity.isphone && xjWorkItemEntity.isPhonere == false) {
                    SnackbarHelper.showError(itemView, "该巡检项要求拍照");
                    return;
                }

                oldImgUrl = "";

                try {
                    xjWorkItemEntity.realRemark = ufItemRemark.getInput().trim();
                    xjWorkItemEntity.endTime = DateUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
                    xjWorkItemEntity.isFinished = true;
                    xjWorkItemEntity.linkState = OLXJConstant.MobileWiLinkState.FINISHED_STATE;
                    xjWorkItemEntity.staffId = EamApplication.getAccountInfo().staffId;

                    EventBus.getDefault().post(new RefreshEvent(Constant.RefreshAction.XJ_WORK_END, getAdapterPosition()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            ufItemPartEndBtn.setOnClickListener(v -> {
                OLXJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                onItemChildViewClick(ufItemPartEndBtn, 0, xjWorkItemEntity);
            });

            ufItemSkipBtn.setOnClickListener(v -> {
                OLXJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                oldImgUrl = "";
                onItemChildViewClick(ufItemSkipBtn, 0, xjWorkItemEntity);
            });


            RxTextView.textChanges(ufItemInputResult.editText())
                    .skipInitialValue()
                    .subscribe(charSequence -> {
                        OLXJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                        if (TextUtils.isEmpty(charSequence)) {
                            if (xjWorkItemEntity != null)
                                xjWorkItemEntity.result = charSequence.toString();
                        } else {
                            if ("数字".equals(xjWorkItemEntity.inputStandardID.valueTypeMoblie.value) && OLXJConstant.MobileEditType.INPUTE.equals(xjWorkItemEntity.inputStandardID.editTypeMoblie.id)) {  //值类型判断：字符/数字

                                if (xjWorkItemEntity.autoJudge) {

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

                                    if (OLXJConstant.MobileEditType.INPUTE.equals(xjWorkItemEntity.inputStandardID.editTypeMoblie.id)) { //录入模式
                                        if (!charSequence.toString().matches("^-?[0.0-9]+$") || charSequence.toString().indexOf(".") == 0) {
                                            if ("-".equals(charSequence.toString())) {
                                                return;
                                            }
                                            ToastUtils.show(context, "请输入数字类型");
                                        } else {

                                            if (charSequence.toString().indexOf(".") > 0) {
                                                if (xjWorkItemEntity.inputStandardID.decimalPlace != null) {
                                                    if (charSequence.toString().substring(charSequence.toString().indexOf(".") + 1).length() > Integer.parseInt(xjWorkItemEntity.inputStandardID.decimalPlace)) {
                                                        ToastUtils.show(context, "结果将四舍五入保留" + xjWorkItemEntity.inputStandardID.decimalPlace + "位");
                                                    }
                                                }
                                            }

                                            BigDecimal bigDecimal = new BigDecimal(charSequence.toString());
                                            if (xjWorkItemEntity.inputStandardID.decimalPlace != null) {
                                                xjWorkItemEntity.result = bigDecimal.setScale(Integer.parseInt(xjWorkItemEntity.inputStandardID.decimalPlace), BigDecimal.ROUND_HALF_UP).toString();
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
                    OLXJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                    if (xjWorkItemEntity != null) {
                        ufItemInputResult.setInput(xjWorkItemEntity.result);
                    }

                }
            });

            RxTextView.textChanges(ufItemRemark.editText())
//                    .debounce(1, TimeUnit.SECONDS)
                    .skipInitialValue()
                    .subscribe(charSequence -> {
                        OLXJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
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
                if (sp2ThermometerHelper == null) {
                    SnackbarHelper.showError(itemView, "设备启动测温异常，请检查设备配置测温模块");
                    return true;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!sp2ThermometerHelper.startOrEnd(true)) {
                        SnackbarHelper.showError(itemView, "设备启动测温异常，请检查设备配置测温模块");
                    } else {
                        //TODO  success
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    sp2ThermometerHelper.startOrEnd(false);
                }

                OLXJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                onItemChildViewClick(thermometerBtn, 0, xjWorkItemEntity);
                return true;
            });

            ufItemPhotoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ufItemPics.findViewById(R.id.customCameraIv).performClick();  //调用CustomGalleryView的拍照按钮
                }
            });


            RxTextView.textChanges(ufItemSelectResult.getCustomSpinner())
                    .subscribe(charSequence -> {
                        OLXJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());

                        if (xjWorkItemEntity != null && xjWorkItemEntity.autoJudge && xjWorkItemEntity.normalRange != null &&
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

            vibrationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    onItemChildViewClick(vibrationBtn, 0, getItem(getAdapterPosition()));
                }
            });

            ufItemRemarkBtn.setOnClickListener(v -> {

                if (ufItemRemark.getVisibility() != View.VISIBLE) {
                    ufItemRemark.setVisibility(View.VISIBLE);
                } else {
                    ufItemRemark.setVisibility(View.GONE);
                }

            });

            ufItemEditBtn.setOnClickListener(v -> {

                if (ufBtnLayout.getVisibility() != View.VISIBLE) {
                    ufBtnLayout.setVisibility(View.VISIBLE);
                    ufResultLayout.setVisibility(View.VISIBLE);
                    ufContentLine.setVisibility(View.VISIBLE);
                    ufResultLine.setVisibility(View.VISIBLE);
                } else {
                    ufBtnLayout.setVisibility(View.GONE);
                    ufResultLayout.setVisibility(View.GONE);
                    ufContentLine.setVisibility(View.GONE);
                    ufResultLine.setVisibility(View.GONE);
                }

            });
            ufItemSelectResultSwitch.setOnSwitchDataListener(new CustomSwitchButton.OnSwitchDataListener() {
                @Override
                public void onSwitchDataChanged(boolean isSwichOn, String data) {
                    OLXJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                    xjWorkItemEntity.result = data;
//                    if(isSwichOn){
//                        xjWorkItemEntity.result = "是";
//                    }else{
//                        xjWorkItemEntity.result = "否";
//                    }

                    if (xjWorkItemEntity.autoJudge && xjWorkItemEntity.normalRange != null) {

                        String[] normalRangeArr = xjWorkItemEntity.normalRange.split(",");
                        if (Arrays.asList(normalRangeArr).contains(xjWorkItemEntity.result)) {
                            xjWorkItemEntity.conclusionID = "realValue/01";
                            xjWorkItemEntity.conclusionName = "正常";
                        } else {
                            xjWorkItemEntity.conclusionID = "realValue/02";
                            xjWorkItemEntity.conclusionName = "异常";
                        }
                    }
                }
            });

        }


        @Override
        protected void update(OLXJWorkItemEntity data) {
            boolean showPart = isNeedPart(getAdapterPosition(), data);
            if (showPart) {
                ufPartLayout.setVisibility(View.VISIBLE);
                if (data.part == null) {
                    if (data.eamID != null) {
                        data.part = data.eamID.name + "的部位";
                    } else {
                        data.part = "无部位";
                    }
                }
                ufItemPart.setValue(data.part);
            } else {
                ufItemPart.setValue("");
                ufPartLayout.setVisibility(View.GONE);
            }
            mOLXJCameraController.addListener(ufItemPics, getAdapterPosition(), OLXJWorkListEamAdapter.this);
            ufItemContent.setValue(data.content);
            if (TextUtils.isEmpty(data.normalRange)) {
                llNormalRange.setVisibility(View.GONE);
                ufResultVerticalLine.setVisibility(View.GONE);
            } else {
                llNormalRange.setVisibility(View.VISIBLE);
                ufResultVerticalLine.setVisibility(View.VISIBLE);
            }
            ufItemNormalRange.setContent(data.normalRange);

            if (OLXJConstant.MobileEditType.INPUTE.equals(data.inputStandardID.editTypeMoblie.id)) {   //录入框
                ufItemSelectResult.setVisibility(View.GONE);
                ufItemInputResult.setVisibility(View.VISIBLE);
                ufItemSelectResultSwitchLayout.setVisibility(View.GONE);

                if (data.isThermometric) {  //要求测温
                    ufItemInputResult.setEditable(false);
                    ufItemInputResult.setHint("测温值");
                } else if (data.isSeismic) {
                    ufItemInputResult.setEditable(false);
                    ufItemInputResult.setHint("测振值");
                } else {
                    ufItemInputResult.setEditable(true);
                    ufItemInputResult.setHint("输入结果");
                }

                if (data.inputStandardID.unitID != null && !TextUtils.isEmpty(data.inputStandardID.unitID.name)) {
                    ufItemInputResult.setText("结果 (" + data.inputStandardID.unitID.name + ")");
                    ufItemInputResult.setKeyWidth(DisplayUtil.dip2px(80, context));
                } else {
                    ufItemInputResult.setText("结果");
                    ufItemInputResult.setKeyWidth(DisplayUtil.dip2px(60, context));
                }
                ufItemInputResult.setNecessary(true);

                if (!TextUtils.isEmpty(data.defaultVal) && TextUtils.isEmpty(data.result)) {
                    ufItemInputResult.setInput(data.defaultVal);
                } else {
                    ufItemInputResult.setInput(data.result);
                }

                if ("数字".equals(data.inputStandardID.valueTypeMoblie.value)) {
                    ufItemInputResult.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);  //支持正负浮点数、整数
                } else {
                    ufItemInputResult.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                }

            } else if (OLXJConstant.MobileEditType.WHETHER.equals(data.inputStandardID.editTypeMoblie.id)) {  //是否
                ufItemInputResult.setVisibility(View.GONE);
                ufItemSelectResult.setVisibility(View.GONE);
                ufItemSelectResultSwitchLayout.setVisibility(View.VISIBLE);

//                if (data.inputStandardID.unitID != null && !TextUtils.isEmpty(data.inputStandardID.unitID.name)) {
//                    ufItemSelectResult.setText("结果 (" + data.inputStandardID.unitID.name + ")");
//                    ufItemSelectResult.setKeyWidth(DisplayUtil.dip2px(80, context));
//                }
//                else{
//                    ufItemSelectResult.setText("结果");
//                    ufItemSelectResult.setKeyWidth(DisplayUtil.dip2px(60, context));
//                }
//                ufItemSelectResult.setNecessary(true);
                if (!TextUtils.isEmpty(data.inputStandardID.valueName))
                    ufItemSelectResultSwitch.setValues(data.inputStandardID.valueName.split(","));
                if (!TextUtils.isEmpty(data.defaultVal) && TextUtils.isEmpty(data.result)) {
                    ufItemSelectResultSwitch.setSwitchStatus("正常".equals(data.defaultVal) || "是".equals(data.defaultVal) ? 1 : 0);
                    data.result = data.defaultVal;
                } else {
                    ufItemSelectResultSwitch.setSwitchStatus("正常".equals(data.result) || "是".equals(data.result) ? 1 : 0);
                }

            } else if (OLXJConstant.MobileEditType.RADIO.equals(data.inputStandardID.editTypeMoblie.id) || OLXJConstant.MobileEditType.CHECKBOX.equals(data.inputStandardID.editTypeMoblie.id)) {  //多选
                ufItemInputResult.setVisibility(View.GONE);
                ufItemSelectResult.setVisibility(View.VISIBLE);
                ufItemSelectResultSwitchLayout.setVisibility(View.GONE);

                if (data.inputStandardID.unitID != null && !TextUtils.isEmpty(data.inputStandardID.unitID.name)) {
                    ufItemSelectResult.setKey("结果 (" + data.inputStandardID.unitID.name + ")");
                    ufItemSelectResult.setKeyWidth(DisplayUtil.dip2px(80, context));
                } else {
                    ufItemSelectResult.setText("结果");
                    ufItemSelectResult.setKeyWidth(DisplayUtil.dip2px(60, context));
                }
                ufItemSelectResult.setNecessary(true);

                if (!TextUtils.isEmpty(data.defaultVal) && TextUtils.isEmpty(data.result)) {
                    ufItemSelectResult.setSpinner(data.defaultVal);
                    data.result = data.defaultVal;
                } else {
                    ufItemSelectResult.setSpinner(data.result);
                }

            }

            if (data.autoJudge) {
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

            if (data.isphone) {
                ufItemPics.setNecessary(true);
            } else {
                ufItemPics.setNecessary(false);
            }

            if (!TextUtils.isEmpty(data.realRemark)) {
                ufItemRemark.setInput(data.realRemark);
                ufItemRemark.setVisibility(View.VISIBLE);
            } else {
                ufItemRemark.setInput("");
                ufItemRemark.setVisibility(View.GONE);
            }


            //保存图片
            if (!TextUtils.isEmpty(data.xjImgUrl)) {

                ufItemPics.setPadding(0, 5, 10, 5);
                if (oldImgUrl.equals(data.xjImgUrl)) {
                    return;
                }

                FaultPicHelper.initPics(Arrays.asList(data.xjImgUrl.split(",")), ufItemPics);
                oldImgUrl = data.xjImgUrl;
                ufItemPics.setTextHeight(DisplayUtil.dip2px(0, context));
            } else if (data.isphone) {
                ufItemPics.setTextHeight(DisplayUtil.dip2px(30, context));
            } else {

                ufItemPics.setTextHeight(DisplayUtil.dip2px(0, context));
                ufItemPics.setPadding(0, 0, 10, 0);
                ufItemPics.clear();
                oldImgUrl = "";
            }

            if (data.ispass) {
                ufItemSkipBtn.setVisibility(View.VISIBLE);
            } else {
                ufItemSkipBtn.setVisibility(View.GONE);
            }
//            if (data.isThermometric) {
//                thermometerBtn.setVisibility(View.VISIBLE);
//            } else {
//                thermometerBtn.setVisibility(View.GONE);
//            }

//            if (data.isSeismic) {
//                vibrationBtn.setVisibility(View.VISIBLE);
//            } else {
//                vibrationBtn.setVisibility(View.GONE);
//            }
        }

        private boolean isNeedPart(int postion, OLXJWorkItemEntity curEntity) {

            if (postion == 0) {
                return true;
            }
            OLXJWorkItemEntity preEntity = getItem(postion - 1);
            String prePart = null;
            String curPart = null;
            String preEam = null;
            String curEam = null;
            if (preEntity.eamID != null) {
                preEam = preEntity.eamID.name;
            }
            if (curEntity.eamID != null) {
                curEam = curEntity.eamID.name;
            }
            if (preEntity.part == null) {
                prePart = "" + preEam + "的部位@SUPCON";
            } else {
                prePart = preEntity.part;
            }
            if (curEntity.part == null) {
                curPart = "" + curEam + "的部位@SUPCON";
            } else {
                curPart = curEntity.part;
            }
            if (preEam != null && preEam.equals(curEam) && prePart.equals(curPart)) {
                return false;
            }
            return true;
        }


        @Override
        public void onChildViewClick(View childView, int action, Object obj) {
            OLXJWorkItemEntity workItemEntity = getItem(getAdapterPosition());  //注：参数obj为空，实现的接口方法，接口中obj为null，非item对象
            onItemChildViewClick(childView, action, workItemEntity);
        }


        /**
         * @author zhangwenshuai1
         * @date 2018/4/11
         * @description 结论自动变动
         */
        private boolean autoJudgeConclusion(OLXJWorkItemEntity xjWorkItemEntity, String charSequence) {
            if (!charSequence.matches("^-?[0.0-9.0]+$") || charSequence.indexOf(".") == 0) {
                if ("-".equals(charSequence)) {
                    return false;
                }
                ToastUtils.show(context, "请输入数字类型");
            } else {

                if (charSequence.indexOf(".") > 0) {
                    if (xjWorkItemEntity.inputStandardID.decimalPlace != null) {
                        if (charSequence.substring(charSequence.indexOf(".") + 1).length() > Integer.parseInt(xjWorkItemEntity.inputStandardID.decimalPlace)) {
                            ToastUtils.show(context, "结果将四舍五入保留" + xjWorkItemEntity.inputStandardID.decimalPlace + "位");
                        }
                    }
                }

                BigDecimal bigDecimal = new BigDecimal(charSequence);
                if (xjWorkItemEntity.inputStandardID.decimalPlace != null) {
                    xjWorkItemEntity.result = bigDecimal.setScale(Integer.parseInt(xjWorkItemEntity.inputStandardID.decimalPlace), BigDecimal.ROUND_HALF_UP).toString();
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
        private boolean intervalJudge(OLXJWorkItemEntity xjWorkItemEntity, String charSequence) {
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
        private boolean orJudge(OLXJWorkItemEntity xjWorkItemEntity, String charSequence) {

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
        private boolean unequalJudge(OLXJWorkItemEntity xjWorkItemEntity, String charSequence) {

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

    class PicViewHolder extends BaseRecyclerViewHolder<OLXJWorkItemEntity> {
        private ImageView pic;

        public PicViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_eam_pic;
        }

        @Override
        protected void initView() {
            super.initView();
            pic = itemView.findViewById(R.id.itemRecyclerPic);
        }

        @Override
        protected void update(OLXJWorkItemEntity data) {
            Long picPath = Long.valueOf(data.headerPicPath);
            new EamPicController().initEamPic(pic, picPath);
        }
    }

    class TitleViewHolder extends BaseRecyclerViewHolder<OLXJWorkItemEntity> {

        @BindByTag("itemRecyclerTitle")
        TextView itemRecyclerTitle;

        @BindByTag("dividedViewIc")
        TextView dividedViewIc;

        private DeviceDCSParamController mDeviceDCSParamController;

        public TitleViewHolder(Context context) {
            super(context, parent);

        }

        @Override
        protected int layoutId() {
            return R.layout.item_recycler_title;
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemRecyclerTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OLXJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                    if ("".equals(xjWorkItemEntity.eamID.id)) {
                        ToastUtils.show(context, "无设备详情可查看！");
                        return;
                    }

                    Bundle bundle = new Bundle();
                    bundle.putLong(Constant.IntentKey.SBDA_ONLINE_EAMID, xjWorkItemEntity.eamID.id);
                    bundle.putString(Constant.IntentKey.SBDA_ONLINE_EAMCODE, xjWorkItemEntity.eamID.code);
                    IntentRouter.go(context, Constant.Router.SBDA_ONLINE_VIEW, bundle);
                }
            });
        }

        @Override
        protected void initView() {
            super.initView();
        }

        @Override
        protected void update(OLXJWorkItemEntity data) {
            if (data.eamID == null || TextUtils.isEmpty(data.eamID.name)) {
                itemRecyclerTitle.setText("无关联设备");
            } else {
                itemRecyclerTitle.setText(data.eamID.name);

                if (!hashSet.contains(data.eamID.id)) {
                    hashSet.add(data.eamID.id);
                    if (mDeviceDCSParamController == null) {
                        mDeviceDCSParamController = new DeviceDCSParamController(itemView, data.eamID.id);
                        mDeviceDCSParamController.initView();
                        mDeviceDCSParamController.initData();
                    } else {
                        mDeviceDCSParamController.getDeviceParams(data.eamID.id);
                    }
                }
            }
        }
    }
}
