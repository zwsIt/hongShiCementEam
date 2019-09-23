package com.supcon.mes.module_olxj.util;

import android.content.Context;

import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XJJudgeHelper {
    private Context context;
    private static XJJudgeHelper xjJudgeHelper;

    public XJJudgeHelper(Context context) {
        this.context = context;
    }

    public static XJJudgeHelper getInstance(Context context) {
        if (xjJudgeHelper == null) {
            xjJudgeHelper = new XJJudgeHelper(context);
        }
        return xjJudgeHelper;
    }

    /**
     * @author zhangwenshuai1
     * @date 2018/4/11
     * @description 结论自动变动
     */
    public boolean autoJudgeConclusion(OLXJWorkItemEntity xjWorkItemEntity, String charSequence) {
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
}
