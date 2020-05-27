package com.supcon.mes.middleware.util;

import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.model.bean.BapResultEntity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/12
 * Email zhangwenshuai1@supcon.com
 * Desc http请求错误解析
 */
public class HttpErrorReturnUtil {
    /**
     *
     * @param throwable
     * @return
     */
    public static String getErrorInfo(Throwable throwable) {
        String msg = throwable.toString();
        if (throwable instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
            if (responseBody != null && responseBody.charStream() != null) {
                /*try {
                    return responseBody.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                try {
                    BapResultEntity error = GsonUtil.gsonToBean(responseBody.string(), BapResultEntity.class);
                    return error.errMsg;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return msg;
    }
}
