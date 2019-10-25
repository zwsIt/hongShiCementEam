package com.supcon.mes.module_sbda_online.screen;

import android.annotation.SuppressLint;

import com.supcon.mes.middleware.model.bean.ScreenEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2018/1/2.
 * Email:wangshizhan@supcon.com
 */

public class FilterHelper {

    @SuppressLint("CheckResult")
    public static List<ScreenEntity> createStateFilter() {
        List<ScreenEntity> list = new ArrayList<>();
        list.add(ScreenState.UNLIMIT.getScreenEntity());
        list.add(ScreenState.INUSE.getScreenEntity());
        list.add(ScreenState.PROHIBIT.getScreenEntity());
        list.add(ScreenState.SEALUP.getScreenEntity());
        list.add(ScreenState.SCRAP.getScreenEntity());
        return list;
    }

    @SuppressLint("CheckResult")
    public static List<ScreenEntity> createEamTypeFilter() {
        List<ScreenEntity> list = new ArrayList<>();
        list.add(EamType.UNLIMITE.getScreenEntity());
        list.add(EamType.CRUSHER.getScreenEntity());
        list.add(EamType.RAWMILL.getScreenEntity());
        list.add(EamType.CEMENTMILL.getScreenEntity());
        list.add(EamType.ROLLERPRESS.getScreenEntity());
        list.add(EamType.ROTARYCELLAR.getScreenEntity());
        return list;
    }
}
