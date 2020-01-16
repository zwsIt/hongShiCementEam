package com.supcon.mes.module_contact.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.ContactEntity;

import java.util.List;

/**
 * Created by wangshizhan on 2019/12/12
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = ContactEntity.class)
public interface PinyinSearchBarAPI {

    void findContactPosition(List<ContactEntity> contactEntities, String word);

}
