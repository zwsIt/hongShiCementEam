package com.supcon.mes.module_yhgl.util;

import android.text.TextUtils;

import com.supcon.mes.middleware.controller.SystemCodeController;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/10/3019:04
 */
public class SystemCodeUtil {

    private static final SystemCodeController systemCodeController = new SystemCodeController();
    public static   String getSystemCodeValue(String id) {
        return null==getSystemCodeEntity(id)?
                "":getSystemCodeEntity(id).value;
    }
    public static SystemCodeEntity getSystemCodeEntity(String id) {
        return TextUtils.isEmpty(id)?null:systemCodeController.getSystemCodeEntity(id);
    }
}
