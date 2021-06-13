package com.supcon.mes.push.controller;

import com.supcon.common.view.App;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.push.util.ManifestUtil;
import com.supcon.mes.push.util.PushHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * Created by wangshizhan on 2019/4/30
 * Email:wangshizhan@supcom.com
 */
public class PushController extends BasePresenterController {


    @Override
    public void onInit() {
        super.onInit();
        LogUtil.d("PushController init");
        String appKey = ManifestUtil.getAppkeyByXML(App.getAppContext());
        String pushSecret = ManifestUtil.getMessageSecretByXML(App.getAppContext());

        // Applicaiton.onCreate函数中调用预初始化函数UMConfigure.preInit()
        UMConfigure.preInit(App.getAppContext(), appKey, ManifestUtil.getChannelByXML(App.getAppContext()));
//        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO); // 默认  MobclickAgent.PageMode.AUTO
        PushHelper.getInstance().init(App.getAppContext(), appKey, pushSecret);
    }
}
