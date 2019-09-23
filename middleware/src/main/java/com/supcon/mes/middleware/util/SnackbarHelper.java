package com.supcon.mes.middleware.util;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.supcon.common.view.App;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.NetWorkUtil;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.middleware.R;

/**
 * Created by wangshizhan on 2017/8/19.
 * Email:wangshizhan@supcon.com
 */

public class SnackbarHelper {

    public static void showMessage(View container, CharSequence msg){

        Snackbar snackbar =
                Snackbar.make(container, msg, Snackbar.LENGTH_SHORT);
        setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
        snackbar.show();


    }

    public static void showError(View container, String msg){
        Snackbar snackbar = null;
        try {
            snackbar = Snackbar.make(container, msg, Snackbar.LENGTH_LONG);
        }
        catch (IllegalArgumentException e){

            LogUtil.e("IllegalArgumentException");
        }

        if(snackbar == null || TextUtils.isEmpty(msg)){
            return;
        }

        setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));

        snackbar.setText(ErrorMsgHelper.msgParse(msg));
        snackbar.show();


    }


    public static void setSnackbarMessageTextColor(Snackbar snackbar, int color) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }

}
