package com.supcon.mes.middleware.presenter;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.SystemCodeController;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntityDao;
import com.supcon.mes.middleware.model.bean.DepartmentInfoDao;
import com.supcon.mes.middleware.util.GreenDaoUtil;
import com.supcon.mes.middleware.util.PinYinUtils;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.middleware.model.bean.BaseSearchListEntity;
import com.supcon.mes.middleware.model.contract.BaseSearchContract;
import com.supcon.mes.middleware.util.StaffManager;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;

/**
 * @author Xushiyun
 * @date 2018/5/22
 * Email:ciruy_victory@gmail.com
 */

public class BaseSearchPresenter extends BaseSearchContract.Presenter {

    @SuppressLint("CheckResult")
    @Override
    public void baseSearch(int pageIndex,String code, String mes, String mod) {
        if (TextUtils.isEmpty(mod)) {
            LogUtil.e("BaseSearchPresenter", "并没有制定搜索模式，使用默认的搜索模式，如果需要制定搜索模式，请传入COMMON_SAERCH_MODE参数");
        }
        final List commonSearchEntities = genSearchEntityList(pageIndex,code, mes, mod);
        Flowable.timer(150, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.io_main())
                .subscribe(aLong -> {
                    if (commonSearchEntities == null || commonSearchEntities.size() == 0) {
                        if (getView() != null) {
                            getView().baseSearchFailed("未搜索到数据");
                        }
                    } else {
                        final BaseSearchListEntity baseSearchListEntity = new BaseSearchListEntity();
                        baseSearchListEntity.commonSearchEntities = commonSearchEntities;
                        if (getView() != null) {
                            getView().baseSearchSuccess(baseSearchListEntity);
                        }
                    }
                });


    }

    @SuppressLint("CheckResult")
    @Override
    public void baseEamSearch(int pageIndex, String propertyValue, String blurValue, String permission) {
        final List list = EamApplication.dao().getCommonDeviceEntityDao().queryBuilder()
                .where(!TextUtils.isEmpty(propertyValue) ?
                        CommonDeviceEntityDao.Properties.InstallPlace.eq(propertyValue) :
                        GreenDaoUtil.emptyStringCondition())
                .where(CommonDeviceEntityDao.Properties.EamPermissionStr.like("%" + permission + "%"))
                .where(CommonDeviceEntityDao.Properties.EamName.like("%" + blurValue + "%"))
                .where(CommonDeviceEntityDao.Properties.Ip.eq(EamApplication.getIp()))
                .orderAsc(CommonDeviceEntityDao.Properties.PinYin)
                .offset((pageIndex-1)*30)
                .limit(30)
                .list();
        Flowable.timer(150, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.io_main())
                .subscribe(aLong -> {
                    if (list == null) {
                        if (getView() != null) {
                            getView().baseSearchFailed("未搜索到数据");
                        }
                    } else {
                        BaseSearchListEntity baseSearchListEntity = new BaseSearchListEntity();
                        baseSearchListEntity.commonSearchEntities = list;
                        if (getView() != null) {
                            getView().baseSearchSuccess(baseSearchListEntity);
                        }
                    }
                });

    }


    /**
     * @param code 系统编码搜索code
     * @param mes  模糊搜索信息
     * @param mod  搜索模式
     * @return 搜索列表
     */
    @SuppressLint("NewApi")
    private List genSearchEntityList(int pageIndex,String code, String mes, String mod) {
        List result;
        //mod在switch中使用必须不为null
        mod = !TextUtils.isEmpty(mod) ? mod : "";
        switch (mod) {
            case Constant.IntentKey.IS_AREA:
                result = EamApplication.dao().getDepartmentInfoDao().queryBuilder()
                        .whereOr(DepartmentInfoDao.Properties.Name.like("" + mes + "%"),
                                DepartmentInfoDao.Properties.SearchPinyin.like("" + PinYinUtils.getPinyin(mes) + "%"))
                        .offset((pageIndex-1)*20)
                        .limit(20)
                        .orderAsc(DepartmentInfoDao.Properties.SearchPinyin).list();
                break;
            case Constant.IntentKey.IS_ENTITY_CODE:
                result = SystemCodeManager.getInstance().getSystemCodeListByCode(pageIndex,code, mes);
                break;
            /*case Constant.IntentKey.IS_EAM:
                if (TextUtils.isEmpty(code)) {
                    result = EamApplication.dao().getCommonDeviceEntityDao().queryBuilder()
                            .where(CommonDeviceEntityDao.Properties.EamName.like("" + mes + "%")).list();
                } else {
                    result = EamApplication.dao().getCommonDeviceEntityDao().queryBuilder().where(CommonDeviceEntityDao.Properties.InstallPlace.eq(code))
                            .where(CommonDeviceEntityDao.Properties.EamName.like("" + mes + "%")).list();
                }
                break;*/
            case Constant.IntentKey.IS_STAFF:
            default:
                result = StaffManager.getInstance().blurSearchByName(pageIndex,mes);
        }
        return result;
    }
}
