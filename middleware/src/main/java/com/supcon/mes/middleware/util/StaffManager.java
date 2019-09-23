package com.supcon.mes.middleware.util;

import android.annotation.SuppressLint;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.UserInfo;
import com.supcon.mes.middleware.model.bean.UserInfoDao;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Xushiyun on 2018/5/24.
 * Email:ciruy_victory@gmail.com
 */

public class StaffManager {
    private volatile boolean isReady = false;               //准备是否完成
    private volatile boolean isInitializing = true;
    private UserInfoDao dao = null;
    private long mUserId = -1;                              //当前登录app的移动设备用户id

    private volatile int page = 0;
    private static final int NUMBER_PER_PAGE = 20;
    private final Map<Long, UserInfo> userInfoEntityMap = new LinkedHashMap<>();

//    private final List<StaffInfo> sortedStaffInfos = new ArrayList<>();

    private static final class StaffManagerHolder {
        private static final StaffManager INSTANCE = new StaffManager();
    }

    public static StaffManager getInstance() {
        return StaffManagerHolder.INSTANCE;
    }

    @SuppressLint("CheckResult")
    public Map<Character, Integer> getHeadPos() {
        final Map<Character, Integer> map = new HashMap();
        Flowable.fromIterable(getSortedStaffInfoEntities())
                .subscribe(new Consumer<UserInfo>() {
                    char last_c = ' ';

                    @Override
                    public void accept(UserInfo staffInfo) throws Exception {
                        char fl = PinYinUtils.getHeaderLetter(staffInfo.name);
                        LogUtil.e("首字母:" + fl);
                        if (fl != last_c) {
                            map.put(fl, getSortedStaffInfoEntities().indexOf(staffInfo));
                            last_c = fl;
                        }
                    }
                });
        return map;
    }

    /**
     * For security,every time we new an singleton instance of DeviceManager, we should set its value 'isReady' as false.
     */
    private StaffManager() {
    }


    @SuppressLint("CheckResult")
    public void init() {
        if (isReady) {
            LogUtil.e("StaffManager", "StaffManager has been initialized already!");
            return;
        }

        if (EamApplication.getAccountInfo() != null) {
            this.mUserId = EamApplication.getAccountInfo().userId;              //初始化需要加载数据库的用户id信息
        } else {
            this.mUserId = -1;
        }

        this.dao = EamApplication.dao().getUserInfoDao();   //初始化设备数据库

        final Map<Long, UserInfo> userInfoEntityMap = getInstance().userInfoEntityMap;

        List<UserInfo> userInfos = EamApplication.dao().getUserInfoDao().loadAll();
        LogUtil.w(userInfos.toString());

        Flowable.create((FlowableOnSubscribe<UserInfo>) e -> {
            final List<UserInfo> list = dao.loadAll();
            for (UserInfo userInfo : list) {
                e.onNext(userInfo);
            }
            e.onComplete();
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .filter(staffInfo -> staffInfo.host.equals(EamApplication.getIp()))
                .subscribe(staffInfo -> {
                            //遍历各个数组中的成员数据,同时将其eamId作为其key值进行储存,这里需要确定的是eamId必须是唯一的,毕竟eamId在数据库中是作为id进行相应的储存的!
//                            //添加大量模拟数据
//                            for (int i = 0; i < 100; i++)
//                                staffInfoEntityMap.put(staffInfo.id + 10086 + i, staffInfo);

                            userInfoEntityMap.put(staffInfo.id, staffInfo);
                        },
                        Throwable::printStackTrace,
                        () -> {
                            this.isReady = true;
                            this.isInitializing = false;
                        });
    }

    public void updateDatabase() {
        if (checkIsReady())
            dao.insertOrReplaceInTx(getStaffInfoEntities());
    }

    private boolean checkIsReady() {
        if (!isReady) {
            LogUtil.e("StaffManager has not been initialized or initialization has not been completed!");
            if (!isInitializing) {
                init();
                LogUtil.e("StaffManager is still initializing!");
            }
        }
        return isReady;
    }

    public List<UserInfo> getStaffInfoEntities() {
        checkIsReady();
        final List<UserInfo> userInfos = new LinkedList<>();
        userInfos.addAll(userInfoEntityMap.values());
        return userInfos;
    }

    public List<UserInfo> getSortedStaffInfoEntities() {
        checkIsReady();
        final List<UserInfo> userInfos = new LinkedList<>();
        userInfos.addAll(userInfoEntityMap.values());
        Collections.sort(userInfos, (o1, o2) -> {
            int result = 0;
            result = PinYinUtils.getHeaderLetter(o1.name) - PinYinUtils.getHeaderLetter(o2.name);
            return result;
        });
//        LogUtil.e(staffInfos.toString());
        return userInfos;
    }
    public List<UserInfo> blurSearchByName(int pageIndex,String name) {
        return blurSearchByName(name, pageIndex);
    }
    public List<UserInfo> blurSearchByName(String name, int pageNum) {
        checkIsReady();
        String pinyin = "";
        LogUtil.e(name);
        String headPinyin = "%";
        pinyin = PinYinUtils.getPinyin(name);
        headPinyin = PinYinUtils.getSQLBlurSearchHeadString(name);
        final List<UserInfo> userInfos = EamApplication.dao()
                .getUserInfoDao()
                .queryBuilder()
                .where(UserInfoDao.Properties.Host.eq(EamApplication.getIp()))
                .whereOr(UserInfoDao.Properties.Name.like("%"+name+"%"),
                        UserInfoDao.Properties.NamePinyin.like("%"+pinyin+"%")
//                        StaffInfoDao.Properties.NamePinyin.like("%"+headPinyin+"%"),
//                        UserInfoDao.Properties.Id.like("%"+name+"%")
                )
                .orderAsc(UserInfoDao.Properties.NamePinyin)
//                .offset(pageNum==-1?0:(pageNum-1)*NUMBER_PER_PAGE)
//                .limit(pageNum==-1?Integer.MAX_VALUE:NUMBER_PER_PAGE)
                .offset((pageNum-1)*20)
                .limit(20)
                .list();
        LogUtil.e(userInfos.toString());
        return userInfos;
    }

    @SuppressLint("CheckResult")
    public void updateStaffPinyin() {
        final List<UserInfo> list = EamApplication.dao()
                .getUserInfoDao()
                .queryBuilder()
//                .where(StaffInfoDao.Properties.NamePinyin.isNull())
                .list();
        Flowable.fromIterable(list)
                .subscribeOn(Schedulers.io())
                .subscribe(staffInfo -> {
                    final String pinyin = PinYinUtils.getPinyin(staffInfo.name);
                    final Character firstLetter = Objects.requireNonNull(pinyin).length()>0?pinyin.charAt(0):null;
                    if(firstLetter != null&&PinYinUtils.isLetter(firstLetter))
                    staffInfo.namePinyin = Objects.requireNonNull(PinYinUtils.getPinyin(staffInfo.name)).toLowerCase();
                    else staffInfo.namePinyin = "#";
                    EamApplication.dao().getUserInfoDao().update(staffInfo);
                });
    }


}
