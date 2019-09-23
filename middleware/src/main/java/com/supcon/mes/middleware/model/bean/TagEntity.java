package com.supcon.mes.middleware.model.bean;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/11/2121:06
 */
public interface TagEntity {
    String getTagName();
    String getTagId();
    String getTagCode();
    boolean isTagChecked();
    String getTagEnv();
    void setTagName(String name);
    void setTagId(String id);
    void setTagCode(String code);
    void setTagChecked(boolean checked);
}
