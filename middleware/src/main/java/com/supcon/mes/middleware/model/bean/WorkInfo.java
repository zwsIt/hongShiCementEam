package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

//import cn.bluetron.coresdk.model.bean.response.OwnMinAppItem;

/**
 * Created by wangshizhan on 2017/8/16.
 * Email:wangshizhan@supcon.com
 */
@Entity(indexes = {@Index(value = "ip ASC, userId ASC, name ASC", unique = true)})
public class WorkInfo extends BaseEntity {
    @Transient
    public static final int VIEW_TYPE_CONTENT = 0;
    @Transient
    public static final int VIEW_TYPE_HEADER = -1;
    @Transient
    public static final int VIEW_TYPE_TITLE = VIEW_TYPE_CONTENT+1;

    @Id(autoincrement = true)
    public Long id;
    public int viewType = VIEW_TYPE_CONTENT; // 视图类型

    public String name;  // 业务模块名称
    public int iconResId; // 本地资源icon
    public String iconUrl; // 网络资源icon
    public int num; // 待办数量
    public int type; // 业务模块类型
    public String router;
    public boolean isOpen;
    public String pendingUrl; // 业务模块请求url
    public Integer sort; // 排序
    public String ip;
    public Long userId;
    public Integer mySort; // 自定义排序
    public boolean isAdd; // 是否添加
    public Long layNo; // 分组类别
    public boolean defaultMenu; // 默认菜单
    public String tag; //
    @Generated(hash = 1606975754)
    public WorkInfo(Long id, int viewType, String name, int iconResId,
            String iconUrl, int num, int type, String router, boolean isOpen,
            String pendingUrl, Integer sort, String ip, Long userId, Integer mySort,
            boolean isAdd, Long layNo, boolean defaultMenu, String tag) {
        this.id = id;
        this.viewType = viewType;
        this.name = name;
        this.iconResId = iconResId;
        this.iconUrl = iconUrl;
        this.num = num;
        this.type = type;
        this.router = router;
        this.isOpen = isOpen;
        this.pendingUrl = pendingUrl;
        this.sort = sort;
        this.ip = ip;
        this.userId = userId;
        this.mySort = mySort;
        this.isAdd = isAdd;
        this.layNo = layNo;
        this.defaultMenu = defaultMenu;
        this.tag = tag;
    }
    @Generated(hash = 206444138)
    public WorkInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getViewType() {
        return this.viewType;
    }
    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIconResId() {
        return this.iconResId;
    }
    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
    public String getIconUrl() {
        return this.iconUrl;
    }
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    public int getNum() {
        return this.num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getRouter() {
        return this.router;
    }
    public void setRouter(String router) {
        this.router = router;
    }
    public boolean getIsOpen() {
        return this.isOpen;
    }
    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
    public String getPendingUrl() {
        return this.pendingUrl;
    }
    public void setPendingUrl(String pendingUrl) {
        this.pendingUrl = pendingUrl;
    }
    public Integer getSort() {
        return this.sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public String getIp() {
        return this.ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Integer getMySort() {
        return this.mySort;
    }
    public void setMySort(Integer mySort) {
        this.mySort = mySort;
    }
    public boolean getIsAdd() {
        return this.isAdd;
    }
    public void setIsAdd(boolean isAdd) {
        this.isAdd = isAdd;
    }
    public Long getLayNo() {
        return this.layNo;
    }
    public void setLayNo(Long layNo) {
        this.layNo = layNo;
    }
    public boolean getDefaultMenu() {
        return this.defaultMenu;
    }
    public void setDefaultMenu(boolean defaultMenu) {
        this.defaultMenu = defaultMenu;
    }
    public String getTag() {
        return this.tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    

    



//    public OwnMinAppItem appItem;
}
