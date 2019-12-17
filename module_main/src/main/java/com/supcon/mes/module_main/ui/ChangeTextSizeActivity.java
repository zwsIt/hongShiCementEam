package com.supcon.mes.module_main.ui;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BasePresenterActivity;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.ui.util.IntentUtils;
import com.supcon.mes.module_main.ui.view.ChangeTextSizeView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


@Router(value = Constant.Router.TEXT_SIZE_SETTING)
public class ChangeTextSizeActivity extends BasePresenterActivity {
    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("rightBtn")
    ImageButton rightBtn;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("cts_font_change")
    ChangeTextSizeView changeTextSizeView;

    @BindByTag("cts_font_prompt")
    TextView exampleTv;

    private float fontSizeScale;
    private boolean isChange;//用于监听字体大小是否有改动
    private int defaultPos = 1;
    private CustomDialog customDialog;

    @Override
    protected int getLayoutID() {
        return R.layout.hs_ac_change_textsize;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        if (EamApplication.fontSizeScale != 0) {
            defaultPos = (int) ((EamApplication.fontSizeScale - 0.875) / 0.125);
        }
        titleText.setText("字体设置");
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        backNotification();
                    }
                });
        changeTextSizeView.setChangeCallbackListener(new ChangeTextSizeView.OnChangeCallbackListener() {
            @Override
            public void onChangeListener(int position) {
                int dimension = getResources().getDimensionPixelSize(R.dimen.dp_14);
                //根据position 获取字体倍数
                fontSizeScale = (float) (0.875 + 0.125 * position);
                //放大后的sp单位
                double v = fontSizeScale * (int) DisplayUtil.px2sp(dimension, ChangeTextSizeActivity.this);
                //改变当前页面大小
                changeTextSize((int) v);
                isChange = !(position == defaultPos);
            }
        });
        changeTextSizeView.setDefaultPosition(defaultPos);
    }

    private void backNotification() {
        if (isChange) {
            customDialog = new CustomDialog(ChangeTextSizeActivity.this).layout(R.layout.dialog_change_ts,
                    DisplayUtil.getScreenWidth(context) * 2 / 3, WRAP_CONTENT)
                    .bindView(R.id.blueBtn, "确定")
                    .bindView(R.id.grayBtn, "取消")
                    .bindClickListener(R.id.blueBtn, new View.OnClickListener() {
                        @Override
                        public void onClick(View v12) {
                            saveData();
                        }
                    }, false)
                    .bindClickListener(R.id.grayBtn, null, true);
            customDialog.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            customDialog.show();
        } else {
            back();
        }
    }

    @SuppressLint("CheckResult")
    private void saveData() {
        SharedPreferencesUtils.setParam(ChangeTextSizeActivity.this.getApplication(), Constant.SPKey.TEXT_SIZE_SETTING, fontSizeScale);
        Flowable.timer(100, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //重启app
                        IntentUtils.restartApp(ChangeTextSizeActivity.this);
                    }
                });


    }

    private void changeTextSize(int dimension) {
        exampleTv.setTextSize(dimension);
    }

    /**
     * 重新配置缩放系数
     *
     * @return
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = res.getConfiguration();
        config.fontScale = 1;//1 设置正常字体大小的倍数
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    public void onBackPressed() {
        backNotification();
    }
}
