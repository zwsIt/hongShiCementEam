package com.supcon.mes.middleware.model.factory;

import java.util.List;
import java.util.Map;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/11/2310:13
 */
public interface BaseFilterSearchListFactory {
    List getFilterSearchList(int pageIndex,Map<String, Object> param);
}
