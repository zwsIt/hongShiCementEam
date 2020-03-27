package com.supcon.mes.middleware.controller;

import android.content.Context;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseDataController;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.middleware.BuildConfig;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.BuildVersionAPI;
import com.supcon.mes.middleware.model.bean.BuildVersionEntity;
import com.supcon.mes.middleware.model.contract.BuildVersionContract;
import com.supcon.mes.middleware.presenter.BuildVersionPresenter;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SystemUtil;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by wangshizhan on 2019/12/26
 * Email:wangshizhan@supcom.com
 * app版本检测
 */
@Presenter(BuildVersionPresenter.class)
public class BuildVersionController extends BaseDataController implements BuildVersionContract.View {

    private CustomDialog mCustomDialog;

    public BuildVersionController(Context context) {
        super(context);
    }


    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    @Override
    public void initData() {
        super.initData();
        presenterRouter.create(BuildVersionAPI.class).getLatestBuildVersion(Constant.Fir.HONG_SHI_APP_ID);
    }


    @Override
    public void getLatestBuildVersionSuccess(BuildVersionEntity entity) {

        if(!TextUtils.isEmpty(entity.build) && TextUtils.isDigitsOnly(entity.build)){
            int newBuild = Integer.parseInt(entity.build);

            if(newBuild > BuildConfig.VERSION_CODE){
                showDialog(entity);
            }
        }

    }

    private void showDialog(BuildVersionEntity entity) {

//        List<String> items = new ArrayList<>();
//        items.add("版本："+entity.build);
//        items.add("包名："+entity.versionShort);
//        items.add("更新日志："+(TextUtils.isEmpty(entity.changelog)?"":entity.changelog));


        CustomDialog customDialog = new CustomDialog(context, R.style.UpdateDialog)
                .layout(R.layout.ly_update_dialog, DisplayUtil.dip2px(300, context), WRAP_CONTENT);
        ((TextView)customDialog.getDialog().findViewById(R.id.msgText)).setMovementMethod(ScrollingMovementMethod.getInstance()); // 超出内容滚动
        customDialog.bindView(R.id.msgText,"版本："+entity.versionShort +"（ Build "+entity.build+" ）"
                        +"\n时间："+ DateUtil.dateFormat(entity.updated_at*1000)
                        +"\n日志："+(TextUtils.isEmpty(entity.changelog)?"":entity.changelog))
//                        +"\n\n是否前往下载更新？")
//                .twoButtonAlertDialog("检测到版本更新，"
//                        +"\n版本："+entity.build
//                        +"\n包名："+entity.versionShort
//                        +"\n更新日志："+(TextUtils.isEmpty(entity.changelog)?"":entity.changelog)
//                        +"\n是否前往下载更新？")
//                .list(items, null)
                .bindClickListener(R.id.redBtn, (view)-> SystemUtil.openBrowser(context, entity.update_url), true)
                .bindClickListener(R.id.grayBtn, null, true)
                .show();


    }

    @Override
    public void getLatestBuildVersionFailed(String errorMsg) {
        LogUtil.e(ErrorMsgHelper.msgParse(errorMsg));
    }
}
