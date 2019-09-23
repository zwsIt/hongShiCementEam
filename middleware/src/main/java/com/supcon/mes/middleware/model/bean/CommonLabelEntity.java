package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/11/2114:15
 */
@Entity
public class CommonLabelEntity extends BaseEntity implements TagEntity {
    private String name;
    private String id;
    private String code;
    private boolean isChecked;

    public static CommonLabelEntity nil() {
        return new CommonLabelEntity();
    }

    public CommonLabelEntity code(String code) {
        this.code = code;
        return this;
    }


    public CommonLabelEntity name(String name) {
        this.name = name;
        return this;
    }

    public CommonLabelEntity id(String id) {
        this.id = id;
        return this;
    }

    public CommonLabelEntity isChecked(boolean isChecked) {
        this.isChecked = isChecked;
        return this;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static CommonLabelEntity createWithAll(String name, String id, boolean isChecked) {
        return nil().name(name).id(id).isChecked(isChecked);
    }

    public static CommonLabelEntity createEntityWithName(String name) {
        return nil().name(name);
    }

    @Generated(hash = 1020275793)
    public CommonLabelEntity(String name, String id, String code, boolean isChecked) {
        this.name = name;
        this.id = id;
        this.code = code;
        this.isChecked = isChecked;
    }

    @Generated(hash = 2108512030)
    public CommonLabelEntity() {
    }

    @Override
    public String getTagName() {
        return this.name;
    }

    @Override
    public String getTagId() {
        return id;
    }

    @Override
    public String getTagCode() {
        return code;
    }

    @Override
    public boolean isTagChecked() {
        return isChecked;
    }

    @Override
    public String getTagEnv() {
        return null;
    }

    @Override
    public void setTagName(String name) {
        this.name = name;
    }

    @Override
    public void setTagId(String id) {
this.id = id;
    }

    @Override
    public void setTagCode(String code) {
        this.code =code;

    }

    @Override
    public void setTagChecked(boolean checked) {
this.isChecked = checked;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return this.id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean getIsChecked() {
        return this.isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
