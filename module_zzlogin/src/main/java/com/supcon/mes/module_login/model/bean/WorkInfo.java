package com.supcon.mes.module_login.model.bean;

import com.supcon.common.com_http.BaseEntity;

import cn.bluetron.coresdk.model.bean.response.OwnMinAppItem;

/**
 * Created by wangshizhan on 2017/8/16.
 * Email:wangshizhan@supcon.com
 */

public class WorkInfo extends BaseEntity {
    public static final int VIEW_TYPE_CONTENT = 0;
    public static final int VIEW_TYPE_HEADER = -1;
    public static final int VIEW_TYPE_TITLE = VIEW_TYPE_CONTENT+1;

    public int viewType = VIEW_TYPE_CONTENT;
    public String name;
    public int iconResId;
    public String iconUrl;
    public int num;
    public int type;
    public String router;
    public boolean isOpen;
    public String pendingUrl;

    public OwnMinAppItem appItem;
}
