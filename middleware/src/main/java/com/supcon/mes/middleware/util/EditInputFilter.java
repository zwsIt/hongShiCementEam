package com.supcon.mes.middleware.util;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.EamApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yangfei.cao
 * @ClassName depot
 * @date 2018/10/22
 * ------------- Description -------------
 */
public class EditInputFilter implements InputFilter {

    /**
     * 最大数字
     */
    public float MAX_VALUE = Float.MAX_VALUE;

    String tip = "数量不能大于(" + MAX_VALUE + ")";
    /**
     * 小数点后的数字的位数
     */
    public static final int POINTER_LENGTH = 2;

    private static final String POINTER = ".";

    Pattern p;

    public EditInputFilter(float max) {
        MAX_VALUE = max;
        init();
    }

    public EditInputFilter() {
        init();
    }

    private void init() {
        //用于匹配输入的是0-9  .  这几个数字和字符
        p = Pattern.compile("([0-9]|\\.)*");
    }

    //更新最带数
    public void upMaxValue(Float max) {
        if (max == null) {
            max = 0f;
        }
        MAX_VALUE = max;
    }

    /**
     * source    新输入的字符串
     * start    新输入的字符串起始下标，一般为0
     * end    新输入的字符串终点下标，一般为source长度-1
     * dest    输入之前文本框内容
     * dstart    原内容起始坐标，一般为0
     * dend    原内容终点坐标，一般为dest长度-1
     */

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {

        String sourceText = source.toString();
        String destText = dest.toString();
        //验证删除等按键
        if (TextUtils.isEmpty(sourceText)) {
            if (dstart == 0 && destText.indexOf(POINTER) == 1) {//保证小数点不在第一个位置
                return "0";
            }
            return "";
        }
        Matcher matcher = p.matcher(source);
        //已经输入小数点的情况下，只能输入数字
        if (destText.contains(POINTER)) {
            if (!matcher.matches()) {
                return "";
            } else {
                if (POINTER.equals(source)) { //只能输入一个小数点
                    return "";
                }
            }
            //验证小数点精度，保证小数点后只能输入两位
            int index = destText.indexOf(POINTER);
            int length = destText.trim().length() - index;
            if (length > POINTER_LENGTH && dstart > index) {
                return "";
            }
        } else {
            //没有输入小数点的情况下，只能输入小数点和数字，但首位不能输入小数点和0
            if (!matcher.matches()) {
                return "";
            } else {
                if ((POINTER.equals(source)) && dstart == 0) {//第一个位置输入小数点的情况
                    return "0.";
                } else if ("0".equals(source) && dstart == 0) {
                    //用于修复能输入多位0
                    return "0.";
                }
            }
        }
        String first = destText.substring(0, dstart);

        String second = destText.substring(dstart, destText.length());
        String sum = first + sourceText + second;
        //验证输入金额的大小
        float sumText = Float.parseFloat(sum);
        if (sumText > Util.big2Float(MAX_VALUE)) {
            ToastUtils.show(EamApplication.getAppContext(), tip);
            return dest.subSequence(dstart, dend);
        }
        return dest.subSequence(dstart, dend) + sourceText;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}