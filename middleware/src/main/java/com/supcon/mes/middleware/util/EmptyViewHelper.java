package com.supcon.mes.middleware.util;

import android.text.TextUtils;

import com.supcon.mes.mbap.beans.EmptyViewEntity;
import com.supcon.mes.middleware.R;

/**
 * Created by wangshizhan on 2018/1/10.
 * Email:wangshizhan@supcon.com
 */

public class EmptyViewHelper {

    private static EmptyViewEntity createEmptyNoBtnEntity(String msg){
        EmptyViewEntity emptyViewEntity = new EmptyViewEntity();
        emptyViewEntity.icon = R.drawable.ic_nodata_notext;
        emptyViewEntity.contentText = TextUtils.isEmpty(msg)?"暂无数据":msg;

        return emptyViewEntity;
    }


    public static EmptyViewEntity createErrorNoBtnEntity(String msg){
        EmptyViewEntity emptyViewEntity = new EmptyViewEntity();
        emptyViewEntity.icon = R.drawable.ic_nodata_notext;
        emptyViewEntity.contentText = TextUtils.isEmpty(msg)?"加载失败！":msg;
//        emptyViewEntity.buttonText = "重试";
        return emptyViewEntity;
    }

    public static EmptyViewEntity createErrorEntity(String msg, String btnText){
        EmptyViewEntity emptyViewEntity = new EmptyViewEntity();
        emptyViewEntity.icon = R.drawable.ic_nodata_notext;
        emptyViewEntity.contentText = TextUtils.isEmpty(msg)?"加载失败！":msg;
        emptyViewEntity.buttonText = btnText;
        return emptyViewEntity;
    }

    private static EmptyViewEntity createEntity(String msg, String btnText){
        EmptyViewEntity emptyViewEntity = new EmptyViewEntity();
        emptyViewEntity.icon = R.drawable.ic_nodata_notext;
        emptyViewEntity.contentText = TextUtils.isEmpty(msg)?"暂无数据":msg;
        emptyViewEntity.buttonText = btnText;

        return emptyViewEntity;
    }

    public static EmptyViewEntity createEmptyEntity(){
        return createEmptyNoBtnEntity("");
    }

    public static EmptyViewEntity createEmptyEntity(String msg){
        return createEmptyNoBtnEntity(msg);
    }

    public static EmptyViewEntity createEmptyEntity(String msg, String btnText){
        return createEntity(msg, btnText);
    }

    public static EmptyViewEntity createNoBtnErrorEmptyEntity(String msg){
        return createErrorNoBtnEntity(msg);
    }

    public static EmptyViewEntity createErrorEmptyEntity(String msg, String btnText){
        return createErrorEntity(msg, btnText);
    }
}
