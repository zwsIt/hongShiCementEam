package com.supcon.mes.middleware.presenter;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.DepartmentInfo;
import com.supcon.mes.middleware.model.bean.DepartmentInfoDao;
import com.supcon.mes.middleware.model.bean.DepartmentInfoListEntity;
import com.supcon.mes.middleware.model.contract.DepartmentSearchContract;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;

/**
 * Created by wangshizhan on 2018/8/20
 * Email:wangshizhan@supcom.com
 */
public class SearchDepartmentPresenter extends DepartmentSearchContract.Presenter {

    @SuppressLint("CheckResult")
    @Override
    public void listDepartment(String rec) {
        QueryBuilder queryBuilder = EamApplication.dao().getDepartmentInfoDao().queryBuilder();
        rec = TextUtils.isEmpty(rec)? null:rec;
        if (rec != null)
            queryBuilder = queryBuilder.where(DepartmentInfoDao.Properties.Name.like( "%"+rec+"%")).orderAsc(DepartmentInfoDao.Properties.LayRec);
        else {
            //当rec字段为空时，按照使用的频率拉取前十的entity
            queryBuilder = queryBuilder.orderDesc(DepartmentInfoDao.Properties.UseFre).limit(10);
        }
        final List<DepartmentInfo> departmentInfos = (List<DepartmentInfo>)queryBuilder.list();

        Flowable.timer(150, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.io_main())
                .subscribe(aLong -> {
                    if (departmentInfos.size() == 0) {
                        getView().listDepartmentFailed("未搜索到数据");
                    } else {
                        final DepartmentInfoListEntity departmentInfoListEntity = new DepartmentInfoListEntity();
                        departmentInfoListEntity.success = true;
                        departmentInfoListEntity.result = departmentInfos;
                        getView().listDepartmentSuccess(departmentInfoListEntity);
                    }
                });
    }
}
