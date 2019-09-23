package com.supcon.mes.middleware.model.event;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.QXGLEntity;

/**
 * Created by shenrong on 2017/12/4.
 */

public class QXGLSaveEvent extends BaseEntity{


    private int position;
    private boolean isDelete;
    private QXGLEntity mQXGLEntity;

    public QXGLSaveEvent() {
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setQXGLEntity(QXGLEntity QXGLEntity) {
        mQXGLEntity = QXGLEntity;
    }

    public QXGLEntity getQXGLEntity() {
        return mQXGLEntity;
    }
}
