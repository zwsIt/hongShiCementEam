package com.supcon.mes.middleware.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.supcon.common.view.App;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.constant.EamPermission;
import com.supcon.mes.middleware.constant.Module;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntityDao;
import com.supcon.mes.middleware.model.bean.CommonDeviceFilterType;
import com.supcon.mes.middleware.EamApplication;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Environment: hongruijun
 * Created by Xushiyun on 2018/3/14.
 * Desc: 20180323 We can exchange our situations, maybe we do not need to save our data in the database every time we update our data,
 * for singleton is immutable all the runtime of our application,so, as for every data change of ours,we may just get it changed in the DeviceManager
 * but not in the SqliteDatabase,thus we can save a lot of time or memory of our application.
 */

public final class DeviceManager {
    private volatile boolean isReady = false;               //准备是否完成
    private volatile boolean isInitializing = true;         //是否正在初始化
    private CommonDeviceEntityDao dao = null;               //储存CommonDeviceEntity
    private long mUserId = -1;                              //当前登录app的移动设备用户id
    private final Map<Long, CommonDeviceEntity> commonDeviceEntityMap = new LinkedHashMap<>();  //储存每个设备的id和设备信息
    private static final int NUM_PER_PAGE = 30;

    /**
     * For security,every time we new an singleton instance of DeviceManager, we should set its value 'isReady' as false.
     */
    private DeviceManager() {
    }

    private static class DeviceManagerHolder {
        private final static DeviceManager INSTANCE = new DeviceManager();
    }

    public static DeviceManager getInstance() {
        return DeviceManagerHolder.INSTANCE;
    }

    @SuppressWarnings("unused")
    public void resetUserId(long userId) {
        this.mUserId = userId;
    }

    @SuppressWarnings("unused")
    public void resetUserId() {
        this.mUserId = EamApplication.getAccountInfo().userId;
    }

    //实现设备管理初始化操作,这里希望在LoginActivity中执行,用以执行相应数据的初始化操作,在每一次用户登录之后实现DeviceManger的初始化操作
    public void init() {
        if (isReady) {
            LogUtil.e("DeviceManager", "DeviceManager has been initialized already!");
            return;
        }
        isInitializing = true;
        if (EamApplication.getAccountInfo() != null) {
            this.mUserId = EamApplication.getAccountInfo().userId;              //初始化需要加载数据库的用户id信息
        } else {
            this.mUserId = -1;
        }
        this.dao = EamApplication.dao().getCommonDeviceEntityDao();   //初始化设备数据库
        final Map<Long, CommonDeviceEntity> commonDeviceEntityMap = getInstance().commonDeviceEntityMap;
        Flowable.create((FlowableOnSubscribe<CommonDeviceEntity>) e -> {
            //整个项目需要完全维护所有CommonDeviceEntity
            List<CommonDeviceEntity> list = dao.loadAll();
            for (CommonDeviceEntity commonDeviceEntity : list) {
                e.onNext(commonDeviceEntity);
            }
            e.onComplete();
        }, BackpressureStrategy.DROP)
                .subscribeOn(Schedulers.io())
                .subscribe(commonDeviceEntity -> {
                            //遍历各个数组中的成员数据,同时将其eamId作为其key值进行储存,这里需要确定的是eamId必须是唯一的,毕竟eamId在数据库中是作为id进行相应的储存的!
                            commonDeviceEntityMap.put(commonDeviceEntity.eamId, commonDeviceEntity);
                        },
                        Throwable::printStackTrace,
                        () -> {
                            this.isReady = true;
                            this.isInitializing = false;
                        });

    }

    public void release() {
        isReady = false;
        isInitializing = false;
//        SharedPreferencesUtils.setParam(App.getAppContext(), Constant.SPKey.EAM_DEVICE_NEED_DOWNLOAD, true);
    }

    /**
     * 这里按照维护的CommonDeviceEntities值进行数据库中的数据的更新操作,
     * 在每次DeviceManager需要进行重新启动时,应该调用这个方法来将当前已经完成的修改储存到数据库
     */
    public void updateDatabase() {
        if (checkIsReady())
            dao.insertOrReplaceInTx(getCommonDeviceEntities());
    }

    //每次需要进行执行对应的DeviceManager相关操作时,获取是否完成了对应的准备工作的标准
    private boolean checkIsReady() {
        if (!isReady) {
            LogUtil.e("DeviceManager has not been initialized or initialization has not been completed!");
            if (!isInitializing) {
                init();
                LogUtil.e("DeviceManager is still initializing!");
            }
        }
        return isReady;
    }

    public List<CommonDeviceEntity> getCommonDeviceEntities() {
        checkIsReady();
        final List<CommonDeviceEntity> commonDeviceEntities = new LinkedList<>();
        commonDeviceEntities.addAll(commonDeviceEntityMap.values());
        return commonDeviceEntities;
    }


    /**
     * @param moduleName Desc: 设备管理器DeviceManager中的添加设备操作具体应该分成两块,1-更新数据库中的对应设备id的数据 2-更新数据库中未受到遍历的但是原来隶属于这个用户id的所有设备的权限,
     *                   避免在未授权的模块中显示对应的数据的具体信息,而且这两块"相辅相成"
     *                   同时重载insertOrUpdateDevices方法,如果没有指定用户id,那么就默认使用AdviceManager中所储存的UserId
     */
    public void insertOrUpdateDevices(List<CommonDeviceEntity> devices, long userId, String moduleName) {
        checkIsReady();
        if (devices == null || devices.size() == 0) {
            return;
        }
        Map<Long, CommonDeviceEntity> commonDeviceEntityMap = getInstance().commonDeviceEntityMap;
        final List<Long> eamIds = new ArrayList<>();

        //取出设备id
        for (CommonDeviceEntity device : devices) {
            eamIds.add(device.eamId);
//            device.eamPermission = permission;
            device.eamPermissionStr = EamPermission.valueOf(moduleName).name();
            device.userId = userId;
            device.pinYin = PinYinUtils.getPinyin(device.eamName);
            CommonDeviceEntity commonDeviceEntity = getDeviceEntityByEamId(device.eamId);

            if (null != commonDeviceEntity) {
                if (userId == commonDeviceEntity.userId) {
                    commonDeviceEntity.eamPermissionStr += "," + EamPermission.valueOf(moduleName).name();
                    device.eamPermissionStr = commonDeviceEntity.eamPermissionStr;
                } else {
                    device.userId = userId;
                    device.eamPermissionStr = EamPermission.valueOf(moduleName).name();
                }
            } else {
                LogUtil.d("insert eamId:" + device.eamId + " module:" + moduleName);
            }
            commonDeviceEntityMap.put(device.eamId, device);
        }

        updatePermission(eamIds, userId, moduleName);
    }


    private void updatePermission(List<Long> eamIds, long userId, String moduleName) {
        //获取所有数据中userId为userId的设备列表(正选)
        List<CommonDeviceEntity> userExList = getDeviceEntitiesByUserId(userId, false);
        //在之前获取的设备列表结果中所有eamId不为eamIds内的设备列表(反选)
        userExList = getDeviceEntitiesByEamIds(userExList, eamIds, true);
        for (CommonDeviceEntity commonDeviceEntity : userExList) {
            if (EamPermission.hasPermission(moduleName, commonDeviceEntity.eamPermissionStr)) {
                StringBuilder actualEamPermissionStr = new StringBuilder(commonDeviceEntity.eamPermissionStr);
                String eamPermissionStr = commonDeviceEntity.eamPermissionStr;
                if (TextUtils.isEmpty(actualEamPermissionStr.toString())) {
                    return;
                }
                if (eamPermissionStr.contains("," + moduleName))
                    commonDeviceEntity.eamPermissionStr = eamPermissionStr.replace("," + moduleName, "");
                else
                    commonDeviceEntity.eamPermissionStr = eamPermissionStr.replace(moduleName, "");


                LogUtil.d("delete permission eamId:" + commonDeviceEntity.eamId + " module:" + moduleName);
                commonDeviceEntityMap.put(commonDeviceEntity.eamId, commonDeviceEntity);
            }
        }
//        dao.updateInTx(userExList);
    }

    private List getDeviceEntitiesByUserId(long userId, boolean exclude) {
        checkIsReady();
        final List<CommonDeviceEntity> result = new LinkedList<>();
        Flowable.fromIterable(getCommonDeviceEntities())
                //如果exclude为ture,则认为反选,即获取所有userId不为userId的设备信息
                .filter(commonDeviceEntity -> (commonDeviceEntity.userId == userId) != exclude)
                .subscribe(result::add);
        return result;
    }

    public CommonDeviceEntity getDeviceEntityByEamId(long eamId) {
        checkIsReady();
        if (!commonDeviceEntityMap.containsKey(eamId)) {
            LogUtil.e("DeviceManager do not have a specific device whose id is " + eamId);
            return null;
        }
        return commonDeviceEntityMap.get(eamId);
    }

    private List<CommonDeviceEntity> getDeviceEntitiesByEamIds(List<CommonDeviceEntity> commonDeviceEntities, List<Long> eamIds, boolean exclude) {
        final List<CommonDeviceEntity> result = new LinkedList<>();
        Flowable.fromIterable(commonDeviceEntities)
                .filter(commonDeviceEntity -> eamIds.contains(commonDeviceEntity.eamId) != exclude)
                .subscribe(result::add);
        return result;
    }

    /**
     * 获取所有设备的设备类型储存到Set
     */
    @SuppressLint("CheckResult")
    public Set<String> getAllDeviceTypes() {
        checkIsReady();

        final Set<String> result = new HashSet<>();
        Flowable.fromIterable(getCommonDeviceEntities())
                .filter(commonDeviceEntity -> MBapApp.getIp().equals(commonDeviceEntity.ip)
                        && commonDeviceEntity.eamPermissionStr.contains(Module.DeviceCheck.name()))
                .subscribe(new Consumer<CommonDeviceEntity>() {
                    @Override
                    public void accept(CommonDeviceEntity commonDeviceEntity) throws Exception {
                        if(!TextUtils.isEmpty(commonDeviceEntity.eamType))
                        result.add(commonDeviceEntity.eamType);
                    }
                });
        return result;
    }

    public Set<String> getAllDeviceAreas() {
        checkIsReady();
        final Set<String> result = new HashSet<>();
        Flowable.fromIterable(getCommonDeviceEntities())
                .filter(commonDeviceEntity -> MBapApp.getIp().equals(commonDeviceEntity.ip)
                        && commonDeviceEntity.eamPermissionStr.contains(Module.DeviceCheck.name()))
                .subscribe(new Consumer<CommonDeviceEntity>() {
                    @Override
                    public void accept(CommonDeviceEntity commonDeviceEntity) throws Exception {
                        if(!TextUtils.isEmpty(commonDeviceEntity.installPlace))
                        result.add(commonDeviceEntity.installPlace);
                    }
                });
        return result;
    }

    public Set<String> getAllDeviceStatuses() {
        checkIsReady();
        final Set<String> result = new HashSet<>();
        Flowable.fromIterable(getCommonDeviceEntities())
                .filter(commonDeviceEntity -> MBapApp.getIp().equals(commonDeviceEntity.ip)
                        && commonDeviceEntity.eamPermissionStr.contains(Module.DeviceCheck.name()))
                .subscribe(new Consumer<CommonDeviceEntity>() {
                    @Override
                    public void accept(CommonDeviceEntity commonDeviceEntity) throws Exception {
                        if(!TextUtils.isEmpty(commonDeviceEntity.eamState))
                        result.add(commonDeviceEntity.eamState);
                    }
                });
        return result;
    }

    /**
     * 用户有权限的设备
     */
    public List<CommonDeviceEntity> commonSearch(String moduleName, int page, int pageNum) {
        checkIsReady();
        QueryBuilder queryBuilder = EamApplication.dao().getCommonDeviceEntityDao().queryBuilder();
        queryBuilder.where(CommonDeviceEntityDao.Properties.UserId.eq(mUserId))
                .where(CommonDeviceEntityDao.Properties.Ip.eq(MBapApp.getIp()))
                .where(CommonDeviceEntityDao.Properties.EamPermissionStr.like("%" + moduleName + "%"))
                .orderAsc(CommonDeviceEntityDao.Properties.EamName)
                .limit(pageNum)
                .offset((page - 1) * pageNum);
        return queryBuilder.list();
    }

    public List<CommonDeviceEntity> commonSearch(String moduleName, int page) {
        return commonSearch(moduleName, page, NUM_PER_PAGE);
    }

    /**
     * 设备模糊查询通用方法,具体参数,1.模块名 2.模糊搜索字段 3.三个具体搜索字段 4.页码 5.每页查询数目
     */
    public List<CommonDeviceEntity> blurSearch(String moduleName, String blurMes, Map<String, Object> params, int page, int pagePerNum) {
        checkIsReady();
        final String type = params == null ? null : (String) params.get(CommonDeviceFilterType.TYPE.name());
        final String area = params == null ? null : (String) params.get(CommonDeviceFilterType.AREA.name());
        final String status = params == null ? null : (String) params.get(CommonDeviceFilterType.STATUS.name());
        QueryBuilder queryBuilder = EamApplication.dao()
                .getCommonDeviceEntityDao()
                .queryBuilder()
                .where(CommonDeviceEntityDao.Properties.UserId.eq(mUserId))
                .where(CommonDeviceEntityDao.Properties.Ip.eq(MBapApp.getIp()))
                .where(CommonDeviceEntityDao.Properties.EamPermissionStr.like("%" + moduleName + "%"));

        if (!TextUtils.isEmpty(type) && !type.equals("类型不限"))
            queryBuilder = queryBuilder.where(CommonDeviceEntityDao.Properties.EamType.eq(type));
        if (!TextUtils.isEmpty(area) && !area.equals("区域不限"))
            queryBuilder = queryBuilder.where(CommonDeviceEntityDao.Properties.InstallPlace.eq(area));
        if (!TextUtils.isEmpty(status) && !status.equals("状态不限"))
            queryBuilder = queryBuilder.where(CommonDeviceEntityDao.Properties.EamState.eq(EAMStatusHelper.getTypeNum(status)));
        if (!TextUtils.isEmpty(blurMes))
            queryBuilder = queryBuilder.whereOr(CommonDeviceEntityDao.Properties.EamName.like("%" + blurMes + "%"), CommonDeviceEntityDao.Properties.EamCode.like("%" + blurMes + "%"));
        return queryBuilder.orderAsc(CommonDeviceEntityDao.Properties.EamName).limit(pagePerNum).offset((page - 1) * pagePerNum).list();
    }

    public List<CommonDeviceEntity> blurSearch(String moduleName, String blurMes, int pageIndex) {
        return blurSearch(moduleName, blurMes, null, pageIndex, NUM_PER_PAGE);
    }
}
