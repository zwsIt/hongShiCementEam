package com.supcon.mes.middleware.util;

import org.greenrobot.greendao.query.WhereCondition;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/10/3113:38
 */
public final class GreenDaoUtil {
    public static WhereCondition.StringCondition emptyStringCondition() {
        return new WhereCondition.StringCondition("1=1");
    }
}
