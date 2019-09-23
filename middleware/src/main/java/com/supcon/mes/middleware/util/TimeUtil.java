package com.supcon.mes.middleware.util;

import android.annotation.SuppressLint;
import android.util.Range;

import com.supcon.mes.middleware.constant.Constant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/9/2715:04
 */
public final class TimeUtil {
    @SuppressLint("NewApi")
    public static Range<String> genTimePeriod(String timePeriodCh) {
        final Calendar calendar = Calendar.getInstance();
        String startTime;
        String endTime;
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        switch (timePeriodCh) {
            case Constant.Date.TODAY:
                startTime = sdf.format(new Date()) + Constant.TimeString.START_TIME;
                endTime = sdf.format(new Date()) + Constant.TimeString.END_TIME;
                break;
            case Constant.Date.YESTERDAY:
                calendar.add(Calendar.DAY_OF_WEEK, -1);
                startTime = sdf.format(calendar.getTime()) + Constant.TimeString.START_TIME;
                endTime = sdf.format(calendar.getTime()) + Constant.TimeString.END_TIME;
                break;
            case Constant.Date.THIS_WEEK:
                int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                if (day_of_week == 0) {
                    day_of_week = 7;
                }
                calendar.add(Calendar.DATE, -day_of_week + 1);
                startTime = sdf.format(calendar.getTime()) + Constant.TimeString.START_TIME;

                Calendar calendar2 = Calendar.getInstance();
                day_of_week = calendar2.get(Calendar.DAY_OF_WEEK) - 1;
                if (day_of_week == 0) {
                    day_of_week = 7;
                }
                calendar2.add(Calendar.DATE, -day_of_week + 7);
                endTime = sdf.format(calendar2.getTime()) + Constant.TimeString.END_TIME;
                break;
            case Constant.Date.THIS_MONTH:
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                startTime = sdf.format(calendar.getTime()) + Constant.TimeString.START_TIME;
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                endTime = sdf.format(calendar.getTime()) + Constant.TimeString.END_TIME;
                break;
            default:
                startTime = endTime = "";
                break;
        }
        return Range.create(startTime, endTime);
    }
}
