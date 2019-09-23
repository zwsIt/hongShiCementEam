package com.supcon.mes.module_olxj.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.FaultPicHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_olxj.IntentRouter;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.constant.OLXJConstant;
import com.supcon.mes.module_olxj.controller.DeviceDCSParamController;
import com.supcon.mes.module_olxj.controller.OLXJCameraController;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;
import com.supcon.mes.module_olxj.ui.OLXJWorkListUnHandledActivity;

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
 * 巡检项（新的）
 */
public class OLXJWorkListAdapterNew extends BaseListDataRecyclerViewAdapter<OLXJWorkItemEntity> {

    private List<OLXJWorkItemEntity> workItemEntities;

    private HashSet hashSet = new HashSet();//判断dcs是否请求过，防止刷新不断请求
    private boolean isExpand = false;

    public OLXJWorkListAdapterNew(Context context) {
        super(context);
    }

    public void setWorkItem(List<OLXJWorkItemEntity> workItemEntities) {
        this.workItemEntities = workItemEntities;
        if (isExpand) {
            addList(workItemEntities);
        }
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

    class ViewHolderUnFinished extends BaseRecyclerViewHolder<OLXJWorkItemEntity> implements OnChildViewClickListener {

        String oldImgUrl = "";  //原图片url，为测温时刷新视图使用，不然页面显示跳动问题

        @BindByTag("ufEamName")
        TextView ufEamName;  //设备名称

        @BindByTag("ufItemContent")
        TextView ufItemContent;  //内容

        @BindByTag("ufItemSelectResult")
        CustomSpinner ufItemSelectResult;  //选择结果

        @BindByTag("ufItemInputResult")
        CustomEditText ufItemInputResult;  //输入结果

        @BindByTag("ufItemRemark")
        CustomEditText ufItemRemark;  //备注

        @BindByTag("ufItemPics")
        CustomGalleryView ufItemPics; //拍照展示

        @BindByTag("ufItemPhotoBtn")
        Button ufItemPhotoBtn; //拍照

        @BindByTag("ufItemEndBtn")
        Button ufItemEndBtn; //完成

        @BindByTag("ufItemPartEndBtn")
        Button ufItemPartEndBtn; //部位完成


        @BindByTag("ufItemPart")
        CustomTextView ufItemPart;  //部位

        @BindByTag("ufBtnLayout")
        LinearLayout ufBtnLayout;

        @BindByTag("ufPartLayout")
        RelativeLayout ufPartLayout;

        @BindByTag("ufContentLine")
        View ufContentLine;

        @BindByTag("ufItemPriority")
        TextView ufItemPriority;
        @BindByTag("ufItemPriorityLayout")
        LinearLayout ufItemPriorityLayout;

        @BindByTag("ufItemSelectResultSwitch")
        CustomSwitchButton ufItemSelectResultSwitch;

        OLXJCameraController mOLXJCameraController;

        public ViewHolderUnFinished(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_olxj_work_item_unfinished_new;
        }

        @Override
        protected void initBind() {
            super.initBind();
        }

        @Override
        protected void initView() {
            super.initView();
            mOLXJCameraController = ((OLXJWorkListUnHandledActivity) context).getController(OLXJCameraController.class);
            mOLXJCameraController.init(Constant.IMAGE_SAVE_XJPATH, Constant.PicType.XJ_PIC);
        }

        @SuppressLint({"CheckResult", "ClickableViewAccessibility"})
        @Override
        protected void initListener() {
            super.initListener();

            ufItemSelectResult.setOnChildViewClickListener(this);

            ufItemEndBtn.setOnClickListener(v -> {
                OLXJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                xjWorkItemEntity.result = TextUtils.isEmpty(xjWorkItemEntity.result) ? xjWorkItemEntity.defaultVal : xjWorkItemEntity.result;
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
                    onItemChildViewClick(ufItemEndBtn, 0, xjWorkItemEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            ufItemPartEndBtn.setOnClickListener(v -> {
                OLXJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                onItemChildViewClick(ufItemPartEndBtn, 0, xjWorkItemEntity);
            });

            RxTextView.textChanges(ufItemInputResult.editText())
                    .skipInitialValue()
                    .subscribe(charSequence -> {
                        OLXJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                        if (TextUtils.isEmpty(charSequence)) {
                            if (xjWorkItemEntity != null)
                                xjWorkItemEntity.result = charSequence.toString();
                            changeState(false);
                        } else {
                            if ("数字".equals(xjWorkItemEntity.inputStandardID.valueTypeMoblie.value) && OLXJConstant.MobileEditType.INPUTE.equals(xjWorkItemEntity.inputStandardID.editTypeMoblie.id)) {  //值类型判断：字符/数字
                                if (xjWorkItemEntity.autoJudge) {
                                    //结论自动判定
                                    autoJudgeConclusion(xjWorkItemEntity, charSequence.toString());
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
                                changeState(false);
                            } else {
                                xjWorkItemEntity.conclusionID = "realValue/02";
                                xjWorkItemEntity.conclusionName = "异常";
                                changeState(true);
                            }
                            xjWorkItemEntity.result = xjWorkItemEntity.defaultVal;
                        }
                    });

            ufItemSelectResultSwitch.setOnSwitchDataListener(new CustomSwitchButton.OnSwitchDataListener() {
                @Override
                public void onSwitchDataChanged(boolean isSwichOn, String data) {
                    OLXJWorkItemEntity xjWorkItemEntity = getItem(getAdapterPosition());
                    xjWorkItemEntity.result = data;
                    if (xjWorkItemEntity.autoJudge && xjWorkItemEntity.normalRange != null) {

                        String[] normalRangeArr = xjWorkItemEntity.normalRange.split(",");
                        if (Arrays.asList(normalRangeArr).contains(xjWorkItemEntity.result)) {
                            xjWorkItemEntity.conclusionID = "realValue/01";
                            xjWorkItemEntity.conclusionName = "正常";
                            changeState(false);
                        } else {
                            changeState(true);
                            xjWorkItemEntity.conclusionID = "realValue/02";
                            xjWorkItemEntity.conclusionName = "异常";
                        }
                    }
                }
            });
            ufItemPriorityLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isExpand) {
                        ufItemPriority.setText("点击展开");
                        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_zk);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        ufItemPriority.setCompoundDrawables(null, null, drawable, null);
                        getList().removeAll(workItemEntities);
                        notifyItemRangeRemoved(getAdapterPosition() + 1, workItemEntities.size());
                        notifyItemRangeChanged(getAdapterPosition() + 1, workItemEntities.size());
                    } else {
                        ufItemPriority.setText("点击关闭");
                        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_sq);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        ufItemPriority.setCompoundDrawables(null, null, drawable, null);
                        getList().addAll(getAdapterPosition() + 1, workItemEntities);
                        notifyItemRangeInserted(getAdapterPosition() + 1, workItemEntities.size());
                    }
                    isExpand = !isExpand;
                }
            });
        }


        @Override
        protected void update(OLXJWorkItemEntity data) {
//            boolean showPart = isNeedPart(getAdapterPosition(), data);
//            if (showPart) {
//                ufPartLayout.setVisibility(View.VISIBLE);
//                if (data.part == null) {
//                    if (data.eamID != null) {
//                        data.part = data.eamID.name + "的部位";
//                    } else {
//                        data.part = "无部位";
//                    }
//                }
//                ufItemPart.setValue(data.part);
//            } else {
//                ufItemPart.setValue("");
//                ufPartLayout.setVisibility(View.GONE);
//            }
            ufItemPriorityLayout.setVisibility(View.GONE);
            if (data.getPrioritySort() == 1) {
                if (getAdapterPosition() < getListSize() - 1) {
                    if (getItem(getAdapterPosition() + 1).viewType == ListType.TITLE.value()) {
                        ufItemPriorityLayout.setVisibility(View.VISIBLE);
                    }
                } else if (getAdapterPosition() == getListSize() - 1) {
                    if (workItemEntities != null && workItemEntities.size() > 0) {
                        ufItemPriorityLayout.setVisibility(View.VISIBLE);
                    }
                }
                if (isExpand) {
                    ufItemPriority.setText("点击关闭展开");
                    Drawable drawable = context.getResources().getDrawable(R.drawable.ic_sq);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    ufItemPriority.setCompoundDrawables(null, null, drawable, null);
                } else {
                    ufItemPriority.setText("点击展开更多");
                    Drawable drawable = context.getResources().getDrawable(R.drawable.ic_zk);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    ufItemPriority.setCompoundDrawables(null, null, drawable, null);
                }
            }

            mOLXJCameraController.addListener(ufItemPics, getAdapterPosition(), OLXJWorkListAdapterNew.this);
            ufItemContent.setText(data.content);

            if (data.conclusionID != null && data.conclusionID.equals("realValue/02")) {
                changeState(true);
            } else {
                changeState(false);
            }
            if (OLXJConstant.MobileEditType.INPUTE.equals(data.inputStandardID.editTypeMoblie.id)) {   //录入框
                ufItemSelectResult.setVisibility(View.GONE);
                ufItemInputResult.setVisibility(View.VISIBLE);
                ufItemSelectResultSwitch.setVisibility(View.GONE);

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
                    ufItemInputResult.setKey(data.inputStandardID.unitID.name);
                } else {
                    ufItemInputResult.setKey("结果");
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
                ufItemSelectResultSwitch.setVisibility(View.VISIBLE);
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
                ufItemSelectResultSwitch.setVisibility(View.GONE);

                if (data.inputStandardID.unitID != null && !TextUtils.isEmpty(data.inputStandardID.unitID.name)) {
                    ufItemSelectResult.setKey(data.inputStandardID.unitID.name);
                } else {
                    ufItemSelectResult.setText("结果");
                }
                ufItemSelectResult.setNecessary(true);

                if (!TextUtils.isEmpty(data.defaultVal) && TextUtils.isEmpty(data.result)) {
                    ufItemSelectResult.setSpinner(data.defaultVal);
                    data.result = data.defaultVal;
                } else {
                    ufItemSelectResult.setSpinner(data.result);
                }

            }
            if (data.isphone) {
                ufItemPics.setNecessary(true);
            } else {
                ufItemPics.setNecessary(false);
            }

            ufItemRemark.setInput(Util.strFormat2(data.realRemark));

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
        }

        private void changeState(boolean isVisible) {
            if (isVisible) {
                ufBtnLayout.setVisibility(View.VISIBLE);
                ufContentLine.setVisibility(View.VISIBLE);
            } else {
                ufBtnLayout.setVisibility(View.GONE);
                ufContentLine.setVisibility(View.GONE);
            }
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
                changeState(false);
            } else {  //异常
                changeState(true);
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
                    changeState(true);
                } else {  //正常
                    changeState(false);
                    xjWorkItemEntity.conclusionID = "realValue/01";
                    xjWorkItemEntity.conclusionName = "正常";
                }
            } else if (xjWorkItemEntity.normalRange.contains("≥") && xjWorkItemEntity.normalRange.contains("＜")) {
                if (inputResult >= small && inputResult < big) {  //区间内、异常
                    xjWorkItemEntity.conclusionID = "realValue/02";
                    xjWorkItemEntity.conclusionName = "异常";
                    changeState(true);
                } else {  //正常
                    changeState(false);
                    xjWorkItemEntity.conclusionID = "realValue/01";
                    xjWorkItemEntity.conclusionName = "正常";
                }
            } else if (xjWorkItemEntity.normalRange.contains("＞") && xjWorkItemEntity.normalRange.contains("≤")) {
                if (inputResult > small && inputResult <= big) {  //区间内、异常
                    xjWorkItemEntity.conclusionID = "realValue/02";
                    xjWorkItemEntity.conclusionName = "异常";
                    changeState(true);
                } else {  //正常
                    changeState(false);
                    xjWorkItemEntity.conclusionID = "realValue/01";
                    xjWorkItemEntity.conclusionName = "正常";
                }
            } else {
                if (inputResult >= small && inputResult <= big) {  //区间内、异常
                    xjWorkItemEntity.conclusionID = "realValue/02";
                    xjWorkItemEntity.conclusionName = "异常";
                    changeState(true);
                } else {  //正常
                    changeState(false);
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
                    changeState(true);
                    xjWorkItemEntity.conclusionID = "realValue/02";
                    xjWorkItemEntity.conclusionName = "异常";
                } else {  //正常
                    changeState(false);
                    xjWorkItemEntity.conclusionID = "realValue/01";
                    xjWorkItemEntity.conclusionName = "正常";
                }
            } else if (xjWorkItemEntity.normalRange.contains("＞")) {
                if (inputResult <= num) {  //异常
                    changeState(true);
                    xjWorkItemEntity.conclusionID = "realValue/02";
                    xjWorkItemEntity.conclusionName = "异常";
                } else {  //正常
                    changeState(false);
                    xjWorkItemEntity.conclusionID = "realValue/01";
                    xjWorkItemEntity.conclusionName = "正常";
                }
            } else if (xjWorkItemEntity.normalRange.contains("≤")) {
                if (inputResult > num) {  //异常
                    changeState(true);
                    xjWorkItemEntity.conclusionID = "realValue/02";
                    xjWorkItemEntity.conclusionName = "异常";
                } else {  //正常
                    changeState(false);
                    xjWorkItemEntity.conclusionID = "realValue/01";
                    xjWorkItemEntity.conclusionName = "正常";
                }
            } else {
                if (inputResult >= num) {  //异常
                    changeState(true);
                    xjWorkItemEntity.conclusionID = "realValue/02";
                    xjWorkItemEntity.conclusionName = "异常";
                } else {  //正常
                    changeState(false);
                    xjWorkItemEntity.conclusionID = "realValue/01";
                    xjWorkItemEntity.conclusionName = "正常";
                }
            }
            return true;
        }
    }

    class PicViewHolder extends BaseRecyclerViewHolder<OLXJWorkItemEntity> {
        private CustomAdView pic;
        private List<GalleryBean> ads;

        public PicViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_recycler_pic;
        }

        @Override
        protected void initView() {
            super.initView();
            pic = itemView.findViewById(R.id.itemRecyclerPic);
        }

        @Override
        protected void initListener() {
            super.initListener();
            pic.setOnChildViewClickListener(new OnChildViewClickListener() {
                @Override
                public void onChildViewClick(View childView, int action, Object obj) {
                    String headerPicPath = getItem(getAdapterPosition()).headerPicPath;
                    if (!headerPicPath.contains("jpg") && !headerPicPath.contains("png")
                            && !headerPicPath.contains("jfif") && headerPicPath.contains("PNG")) {
                        ToastUtils.show(context, "无图片显示！");
                        return;
                    }

                    Bundle bundle = new Bundle();

                    bundle.putSerializable("images", (Serializable) Arrays.asList(getItem(getAdapterPosition()).headerPicPath.split(",")));
                    bundle.putInt("position", ads.indexOf(obj));  //点击位置索引

                    int[] location = new int[2];
                    childView.getLocationOnScreen(location);  //点击图片的位置
                    bundle.putInt("locationX", location[0]);
                    bundle.putInt("locationY", location[1]);

                    bundle.putInt("width", DisplayUtil.dip2px(100, context));//必须
                    bundle.putInt("height", DisplayUtil.dip2px(100, context));//必须

                    bundle.putBoolean("isEditable", false);  //删除图标
                    ((OLXJWorkListUnHandledActivity) context).getWindow().setWindowAnimations(R.style.fadeStyle);
                    IntentRouter.go(context, Constant.Router.IMAGE_VIEW, bundle);
                }
            });
        }

        @Override

        protected void update(OLXJWorkItemEntity data) {
            if (pic.getItemCount() != 0) {
                pic.clear();
            }
            ads = new ArrayList<>();
            GalleryBean galleryBean;
            if (data.headerPicPath.contains("jpg") || data.headerPicPath.contains("png")
                    || data.headerPicPath.contains("jfif") || data.headerPicPath.contains("PNG")) {
                String[] picUrls = data.headerPicPath.split(",");
                File file;
                for (String url : picUrls) {
                    file = new File(url);
                    if (file.exists() && file.isFile()) {
                        galleryBean = new GalleryBean();
                        galleryBean.localPath = url;
                        ads.add(galleryBean);
                    } else {//巡检图片被清除了，必须放一个临时的图片
                        galleryBean = new GalleryBean();
                        galleryBean.resId = R.drawable.ic_zwtp;
                        ads.add(galleryBean);
                    }

                }
            } else {
                galleryBean = new GalleryBean();
                galleryBean.resId = R.drawable.ic_zwtp;
                ads.add(galleryBean);
            }
            pic.setGalleryBeans(ads);
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
