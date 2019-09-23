package com.supcon.mes.middleware.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.widget.ImageView;

import com.supcon.common.view.view.picker.DateTimePicker;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.controllers.BasePickerController;
import com.supcon.mes.middleware.util.AnimatorUtil;

import static com.supcon.common.view.view.picker.DateTimePicker.NONE;
import static com.supcon.common.view.view.picker.DateTimePicker.YEAR_MONTH;
import static com.supcon.common.view.view.picker.DateTimePicker.YEAR_MONTH_DAY;

/**
 * @Author xushiyun
 * @Create-time 8/1/19
 * @Pageage com.supcon.mes.middleware.controller
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public class DatePickController extends BasePickerController<DateTimePicker> implements DialogInterface.OnDismissListener {

    private String[] dateStrs;
    private DateTimePicker.OnYearMonthDayTimePickListener mListener;
    private DateTimePicker.OnYearMonthTimePickListener mTimePickListener;
    private DateTimePicker dateTimePicker;
    private ImageView expend;

    public DatePickController(Activity activity) {
        super(activity);
    }


    public DatePickController date(long date) {
        parseTime(date);
        return this;
    }

    public DatePickController listener(DateTimePicker.OnYearMonthDayTimePickListener listener) {
        this.mListener = listener;
        return this;

    }

    public DatePickController listener(DateTimePicker.OnYearMonthTimePickListener listener) {
        this.mTimePickListener = listener;
        return this;

    }

    public void show(long time) {
        parseTime(time);
        show();

    }

    public void show(long time, ImageView expend) {
        this.expend = expend;
        parseTime(time);
        show();

    }

    public void showMonth(long time) {
        parseTime(time);
        showMonth();

    }

    public void showMonth(long time, ImageView expend) {
        this.expend = expend;
        parseTime(time);
        showMonth();

    }

    private void parseTime(long time) {
        dateStrs = DateUtil.dateFormat(time, "yyyy MM dd HH mm ss").split(" ");
        if (Integer.valueOf(dateStrs[0]) < 2017 || Integer.valueOf(dateStrs[0]) > 2025) {
            dateStrs[0] = "2018";
        }

    }

    public void show() {
        dateTimePicker = new DateTimePicker(activity, YEAR_MONTH_DAY, NONE);
        dateTimePicker.setOnDismissListener(this::onDismiss);
        dateTimePicker.setDateRangeStart(2017, 1, 1);
        dateTimePicker.setDateRangeEnd(2025, 11, 11);
        dateTimePicker.setDividerVisible(isDividerVisible);
        dateTimePicker.setCycleDisable(isCycleDisable);
        dateTimePicker.setCanceledOnTouchOutside(isCancelOutside);
        dateTimePicker.setTextSize(textSize);
        dateTimePicker.setTextColor(textColorFocus, textColorNormal);

        if (isSecondVisible)
            dateTimePicker.setSelectedItem(Integer.valueOf(dateStrs[0]),
                    Integer.valueOf(dateStrs[1]), Integer.valueOf(dateStrs[2]),
                    Integer.valueOf(dateStrs[3]), Integer.valueOf(dateStrs[4]), Integer.valueOf(dateStrs[5]));
        else
            dateTimePicker.setSelectedItem(Integer.valueOf(dateStrs[0]),
                    Integer.valueOf(dateStrs[1]), Integer.valueOf(dateStrs[2]),
                    Integer.valueOf(dateStrs[3]), Integer.valueOf(dateStrs[4]));
        if (mListener != null) {
            dateTimePicker.setOnDateTimePickListener(mListener);
        }
        dateTimePicker.show();
    }

    public void showMonth() {
        dateTimePicker = new DateTimePicker(activity, YEAR_MONTH, NONE);
        dateTimePicker.setOnDismissListener(this::onDismiss);
        dateTimePicker.setDateRangeStart(2017, 1);
        dateTimePicker.setDateRangeEnd(2025, 11);
        dateTimePicker.setDividerVisible(isDividerVisible);
        dateTimePicker.setCycleDisable(isCycleDisable);
        dateTimePicker.setCanceledOnTouchOutside(isCancelOutside);
        dateTimePicker.setTextSize(textSize);
        dateTimePicker.setTextColor(textColorFocus, textColorNormal);
        dateTimePicker.setLabel("-", "", "", "", "");
        dateTimePicker.setSelectedItem(Integer.valueOf(dateStrs[0]),
                Integer.valueOf(dateStrs[1]),
                Integer.valueOf(dateStrs[3]), Integer.valueOf(dateStrs[4]));
        if (mTimePickListener != null) {
            dateTimePicker.setOnDateTimePickListener(mTimePickListener);
        }
        dateTimePicker.show();
    }

    @Override
    public DateTimePicker create() {
        dateTimePicker = new DateTimePicker(activity, YEAR_MONTH_DAY, NONE);
        dateTimePicker.setOnDismissListener(this::onDismiss);
        dateTimePicker.setDateRangeStart(2017, 1, 1);
        dateTimePicker.setDateRangeEnd(2025, 11, 11);
        dateTimePicker.setTimeRangeStart(0, 0);
        dateTimePicker.setTimeRangeEnd(23, 59);
        dateTimePicker.setDividerVisible(isDividerVisible);
        dateTimePicker.setCycleDisable(isCycleDisable);
        dateTimePicker.setCanceledOnTouchOutside(isCancelOutside);
        dateTimePicker.setTextSize(textSize);
        dateTimePicker.setTextColor(textColorFocus, textColorNormal);

        if (isSecondVisible)
            dateTimePicker.setSelectedItem(Integer.valueOf(dateStrs[0]),
                    Integer.valueOf(dateStrs[1]), Integer.valueOf(dateStrs[2]),
                    Integer.valueOf(dateStrs[3]), Integer.valueOf(dateStrs[4]), Integer.valueOf(dateStrs[5]));
        else
            dateTimePicker.setSelectedItem(Integer.valueOf(dateStrs[0]),
                    Integer.valueOf(dateStrs[1]), Integer.valueOf(dateStrs[2]),
                    Integer.valueOf(dateStrs[3]), Integer.valueOf(dateStrs[4]));

        if (mListener != null) {
            dateTimePicker.setOnDateTimePickListener(mListener);
        }
        return dateTimePicker;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (expend != null) {
            AnimatorUtil.rotationExpandIcon(expend, 180, 0);
        }
    }
}
