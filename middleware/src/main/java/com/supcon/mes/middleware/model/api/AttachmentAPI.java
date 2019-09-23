package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.AttachmentListEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;

import java.io.File;

/**
 * Created by wangshizhan on 2018/8/17
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = {AttachmentListEntity.class, AttachmentListEntity.class, String.class, BapResultEntity.class})
public interface AttachmentAPI {

    void getAttachments(long tableId);

    void queryAttachments(long tableId);

    void uploadAttachment(File file);

    void deleteAttachment(long id);

}
