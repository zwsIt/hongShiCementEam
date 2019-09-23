package com.supcon.mes.module_xj.presenter;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.alone.AloneApp;
import com.supcon.mes.middleware.model.bean.XJPathEntity;
import com.supcon.mes.middleware.model.bean.XJPathEntityDao;
import com.supcon.mes.module_xj.model.bean.XJPathListEntity;
import com.supcon.mes.module_xj.model.contract.XJPathContract;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangwenshuai1 on 2018/3/12.
 */

public class XJPathListPresenter extends XJPathContract.Presenter {

    @Override
    public void getXJPathList(Map<String,Object> queryParam) {
        LogUtil.d("getXJPath_queryParam:"+queryParam.toString());
        List<XJPathEntity> list;  //查询数据列表
        XJPathEntityDao  xjPathEntityDao = EamApplication.dao().getXJPathEntityDao();  //查询dao

        String state = (String) queryParam.get("state");
        String startTime = (String)queryParam.get("startTime");
        String endTime = (String)queryParam.get("endTime");
        String ip = EamApplication.getIp();
        if (startTime == null && endTime == null){
            list = xjPathEntityDao.queryBuilder().where(XJPathEntityDao.Properties.State.eq(state),
                    XJPathEntityDao.Properties.ResstaffId.eq(EamApplication.getAccountInfo().staffId),
                    XJPathEntityDao.Properties.Ip.eq(ip)).list();
        }else {
            list = xjPathEntityDao.queryBuilder().where(XJPathEntityDao.Properties.State.eq(state),XJPathEntityDao.Properties.StartTime.le(endTime),
                    XJPathEntityDao.Properties.EndTime.ge(startTime),XJPathEntityDao.Properties.ResstaffId.eq(EamApplication.getAccountInfo().staffId),
                    XJPathEntityDao.Properties.Ip.eq(ip)).list();
        }

        XJPathListEntity xjPathListEntity = new XJPathListEntity();
        xjPathListEntity.result = list;
        xjPathListEntity.success = true;

        getView().getXJPathListSuccess(xjPathListEntity);

    }
}
