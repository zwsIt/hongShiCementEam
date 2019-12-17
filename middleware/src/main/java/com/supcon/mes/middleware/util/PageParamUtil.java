package com.supcon.mes.middleware.util;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/12
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class PageParamUtil {
    public static Map<String, Object> pageQueryParam(int pageNo,int pageSize){
        Map<String, Object> pageQueryParam = new HashMap<>();
        pageQueryParam.put("page.pageSize", pageSize);
        pageQueryParam.put("page.maxPageSize", 500);
        pageQueryParam.put("page.pageNo", pageNo);

        return pageQueryParam;
    }
}
