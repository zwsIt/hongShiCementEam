package com.supcon.mes.module_login.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.fragment.BaseControllerFragment;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.SystemUtil;
import com.supcon.mes.mbap.view.CustomArrowView;
import com.supcon.mes.mbap.view.CustomCacheView;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomPotraitView;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.StaffPicController;
import com.supcon.mes.middleware.util.ChannelUtil;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.PhoneUtil;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_login.IntentRouter;
import com.supcon.mes.module_login.R;
import com.supcon.mes.module_login.controller.PasswordController;
import com.supcon.mes.module_login.model.api.MineAPI;
import com.supcon.mes.module_login.model.contract.MineContract;
import com.supcon.mes.module_login.presenter.MinePresenter;
import com.supcon.mes.module_login.ui.MainActivity;


/**
 * Created by wangshizhan on 2017/8/11.
 */
@Presenter(value = {MinePresenter.class})
@Controller(StaffPicController.class)
public class MineFragment extends BaseControllerFragment implements MineContract.View, View.OnClickListener {

    @BindByTag("logout")
    Button logout;

    @BindByTag("minePotrait")
    CustomPotraitView minePotrait;

    @BindByTag("mineClear")
    CustomCacheView mineClear;

    @BindByTag("mineUpdate")
    CustomArrowView mineUpdate;

    @BindByTag("mineShare")
    CustomArrowView mineShare;

    @BindByTag("mineFeedback")
    CustomArrowView mineFeedback;

    @BindByTag("mineSettings")
    CustomArrowView mineSettings;

    @BindByTag("mineAbout")
    CustomArrowView mineAbout;

    @BindByTag("infoContent")
    TextView infoContent;

    private Uri uri;


    @Override
    protected int getLayoutID() {
        return R.layout.frag_mine;
    }

    @Override
    protected void onInit() {
        super.onInit();
    }

    @Override
    protected void initView() {
        super.initView();

        infoContent.setText(PhoneUtil.getDeviceSN());
    }

    @Override
    protected void initListener() {
        super.initListener();

        logout.setOnClickListener(v -> {
            onLoading("正在登出...");
            presenterRouter.create(MineAPI.class).logout();

        });

        minePotrait.setOnClickListener(this);
        mineClear.setOnClickListener(this);
        mineUpdate.setOnClickListener(this);
        mineShare.setOnClickListener(this);
        mineFeedback.setOnClickListener(this);
        mineSettings.setOnClickListener(this);
        mineAbout.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        super.initData();
    }

    private void updateCacheSize() {
        try {
            String cacheSize = SystemUtil.getTotalCacheSize(getActivity());
            mineClear.setCacheSize(cacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        updateCacheSize();

    }

    public void onClick(View view) {

        if (view.getId() == R.id.minePotrait) {
            LogUtil.d("minePotrait");
        } else if (view.getId() == R.id.mineClear) {
            LogUtil.d("mineClear");
            //点击清理缓存按钮后,进行相应的缓存清理工作
            new CustomDialog(context)
                    .twoButtonAlertDialog("是否清除缓存?")
                    .bindView(R.id.redBtn, "确定")
                    .bindView(R.id.grayBtn, "取消")
                    .bindClickListener(R.id.redBtn, new View.OnClickListener() {
                        @Override
                        public void onClick(View v1) {
                            try {
                                String cacheSize = SystemUtil.getTotalCacheSize(getActivity());
                                //执行删除操作
                                SystemUtil.clearAllCache(context);
                                ToastUtils.show(getActivity(), "缓存已清理,共清理" + cacheSize + "缓存空间");
                                updateCacheSize();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, true)
                    .bindClickListener(R.id.grayBtn, null, true)
                    .show();
        } else if (view.getId() == R.id.mineUpdate) {
            LogUtil.d("mineUpdate");
            String channel = ChannelUtil.getUMengChannel();
            if (channel.equals("hongshi")) {
                uri = Uri.parse("https://www.pgyer.com/zDk8?from=singlemessage");
            } else if (channel.equals("hailuo")) {
                uri = Uri.parse("https://www.pgyer.com/70NM?from=singlemessage");
            } else {
                uri = Uri.parse("https://www.pgyer.com");
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (view.getId() == R.id.mineShare) {
            LogUtil.d("mineShare");
        } else if (view.getId() == R.id.mineFeedback) {
            LogUtil.d("mineFeedback");
            SnackbarHelper.showError(rootView, "暂未实现");
        } else if (view.getId() == R.id.mineSettings) {
            LogUtil.d("mineSettings");
            IntentRouter.go(getContext(), Constant.Router.SETTING);
        } else if (view.getId() == R.id.mineAbout) {
            LogUtil.d("mineAbout");
//            SnackbarHelper.showError(rootView, "暂未实现");
            IntentRouter.go(context, Constant.Router.ABOUT);
        }


        ((MainActivity) getActivity()).toggleDrawer();
    }


    @Override
    public void logoutSuccess() {
//        MBapApp.setIsLogin(false);
//        MBapApp.setPassword("");
//        //用户信息清空
//        EamApplication.setAccountInfo(null);
//        HeartBeatService.stopLoginLoop(getContext());
//        DeviceManager.getInstance().release();
        onLoadSuccessAndExit("登出成功！", () -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.FIRST_LOGIN, false);
            if (!EamApplication.isHongshi()) {
                bundle.putInt(Constant.IntentKey.LOGIN_BG_ID, R.drawable.bg_login_hl);
                bundle.putInt(Constant.IntentKey.LOGIN_LOGO_ID, R.drawable.ic_login_logo_hl);
            }
            IntentRouter.go(getContext(), Constant.Router.LOGIN, bundle);
            ((MainActivity) getActivity()).toggleDrawer();
        });

    }

    @Override
    public void logoutFailed(String error) {
        onLoadFailed(ErrorMsgHelper.msgParse(error));
    }


}
