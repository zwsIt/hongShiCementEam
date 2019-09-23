package com.supcon.mes.middleware.util;

import android.annotation.SuppressLint;

import com.supcon.common.view.base.controller.BaseController;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntityDao;

import java.util.List;

/**
 * Created by wangshizhan on 2018/7/18
 * Email:wangshizhan@supcom.com
 */
public class SystemCodeManager extends BaseController {

    private static class SystemCodeManagerHolder{
        private static SystemCodeManager instance = new SystemCodeManager();
    }


    public static SystemCodeManager getInstance() {

        return SystemCodeManagerHolder.instance;
    }

    private SystemCodeManager() {
    }



   @SuppressLint("CheckResult")
   public void setSystemCodeList(List<SystemCodeEntity> systemCodeList){

       EamApplication.dao().getSystemCodeEntityDao().insertOrReplaceInTx(systemCodeList);

   }


    public SystemCodeEntity getSystemCodeEntity(String id){

       return EamApplication.dao().getSystemCodeEntityDao().queryBuilder()
               .where(SystemCodeEntityDao.Properties.Id.eq(id), SystemCodeEntityDao.Properties.Ip.eq(EamApplication.getIp()))
               .unique();
    }

    public SystemCodeEntity getSystemCodeEntity(String entityCode, String value){

        return EamApplication.dao().getSystemCodeEntityDao()
                .queryBuilder()
                .where(SystemCodeEntityDao.Properties.EntityCode.eq(entityCode), SystemCodeEntityDao.Properties.Value.eq(value), SystemCodeEntityDao.Properties.Ip.eq(EamApplication.getIp()))
                .unique();
    }

    public List<SystemCodeEntity> getSystemCodeListByCode(String entityCode){
        return EamApplication.dao().getSystemCodeEntityDao().queryBuilder()
                .where(SystemCodeEntityDao.Properties.EntityCode.eq(entityCode),SystemCodeEntityDao.Properties.Ip.eq(EamApplication.getIp()))
                .orderAsc(SystemCodeEntityDao.Properties.LayRec)
                .list();
    }

    public String getSystemCodeEntityId(String entityCode, String entityName) {
//        final List<SystemCodeEntity> list = EamApplication.dao().getSystemCodeEntityDao().queryBuilder().where(SystemCodeEntityDao.Properties.EntityCode.eq(entityCode),SystemCodeEntityDao.Properties.Value.eq(entityName)).orderAsc(SystemCodeEntityDao.Properties.Id).list();
//        return list.size()==0?null:list.get(0).id;
        return EamApplication.dao().getSystemCodeEntityDao().queryBuilder()
                .where(SystemCodeEntityDao.Properties.EntityCode.eq(entityCode),SystemCodeEntityDao.Properties.Value.eq(entityName), SystemCodeEntityDao.Properties.Ip.eq(EamApplication.getIp()))
                .unique().id;
    }


    public List<SystemCodeEntity> getSystemCodeListByCode(int pageIndex, String entityCode, String mes){
        return EamApplication.dao().getSystemCodeEntityDao().queryBuilder().where(SystemCodeEntityDao.Properties.EntityCode.eq(entityCode), SystemCodeEntityDao.Properties.Value.like("%"+mes+"%"), SystemCodeEntityDao.Properties.Ip.eq(EamApplication.getIp()))
                .offset((pageIndex-1)*20)
                .limit(20)
                .orderAsc(SystemCodeEntityDao.Properties.Id).list();
    }

}
