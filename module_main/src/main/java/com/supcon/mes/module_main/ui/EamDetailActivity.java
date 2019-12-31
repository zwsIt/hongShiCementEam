package com.supcon.mes.module_main.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.BaseConstant;
import com.supcon.common.view.base.activity.BaseControllerActivity;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.EamPicController;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.ui.view.TrapezoidView;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.module_login.model.bean.WorkInfo;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.IntentRouter;
import com.supcon.mes.module_main.model.api.AnomalyAPI;
import com.supcon.mes.module_main.model.api.EamDetailAPI;
import com.supcon.mes.module_main.model.contract.AnomalyContract;
import com.supcon.mes.module_main.model.contract.EamDetailContract;
import com.supcon.mes.module_main.presenter.AnomalyPresenter;
import com.supcon.mes.module_main.presenter.EamDetailPresenter;
import com.supcon.mes.module_main.ui.adaper.AnomalyAdapter;
import com.supcon.mes.module_main.ui.adaper.WorkAdapter;
import com.supcon.mes.module_main.ui.view.SimpleRatingBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;



/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/30
 * ------------- Description -------------
 * 设备详情
 */
@Router(Constant.Router.EAM_DETAIL)
@Presenter(value = {AnomalyPresenter.class, EamDetailPresenter.class})
public class EamDetailActivity extends BaseControllerActivity implements AnomalyContract.View, EamDetailContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    ImageView eamPic;
    //待办
    @BindByTag("anomalyLayout")
    LinearLayout anomalyLayout;
    @BindByTag("anomalyRecycler")
    RecyclerView anomalyRecycler;
    @BindByTag("eamWorkRecycler")
    RecyclerView eamWorkRecycler;
    @BindByTag("eamName")
    TrapezoidView eamName;

    @BindByTag("eamLayout")
    RelativeLayout eamLayout;
    @BindByTag("starLevel")
    SimpleRatingBar starLevel;
    @BindByTag("eamScore")
    TextView eamScore;

    private EamEntity mEamEntity;
    private AnomalyAdapter anomalyAdapter;
    private WorkAdapter workAdapter;
    private TextView waitMore;

    @Override
    protected int getLayoutID() {
        return R.layout.hs_ac_eam_detail;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        mEamEntity = (EamEntity) getIntent().getSerializableExtra(Constant.IntentKey.EAM);
    }

    @Override
    protected void initView() {
        super.initView();
        View waitTitle = rootView.findViewById(R.id.hs_anomaly_title);
        ((TextView) waitTitle.findViewById(R.id.contentTitleLabel)).setText("异常记录");
        waitMore = waitTitle.findViewById(R.id.contentTitleSettingIc);
        waitMore.setVisibility(View.VISIBLE);
        waitMore.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.IntentKey.EAM, mEamEntity);
            IntentRouter.go(this, Constant.Router.ANOMALY, bundle);
        });
        View workTitle = rootView.findViewById(R.id.hs_eam_work_title);
        ((TextView) workTitle.findViewById(R.id.contentTitleLabel)).setText("我的工作");
        eamPic = findViewById(R.id.eamPic);
        anomalyRecycler.setLayoutManager(new LinearLayoutManager(context));
        anomalyAdapter = new AnomalyAdapter(this);
        anomalyRecycler.setAdapter(anomalyAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(context, 4);
        eamWorkRecycler.setLayoutManager(layoutManager);
        workAdapter = new WorkAdapter(this);
        eamWorkRecycler.setAdapter(workAdapter);
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
                        back();
                    }
                });

        workAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.IntentKey.EAM, mEamEntity);
                switch (position) {
                    case 0:
                        IntentRouter.go(EamDetailActivity.this, Constant.Router.OLXJ_EAM_UNHANDLED, bundle);
                        break;
                    case 1:
                        IntentRouter.go(EamDetailActivity.this, Constant.Router.TEMPORARY_LUBRICATION_EARLY_WARN, bundle);
                        break;
                    case 2:
                        IntentRouter.go(EamDetailActivity.this, Constant.Router.ACCEPTANCE_LIST, bundle);
                        break;
                    case 3:
                        bundle.putLong(Constant.IntentKey.EAM_ID,mEamEntity.id);
                        IntentRouter.go(context, Constant.Router.EAM_FILE_LIST, bundle);
//                        bundle.putString(BaseConstant.WEB_AUTHORIZATION, EamApplication.getAuthorization());
//                        bundle.putString(BaseConstant.WEB_COOKIE, EamApplication.getCooki());
//                        bundle.putBoolean(BaseConstant.WEB_HAS_REFRESH, false);
//                        bundle.putBoolean(BaseConstant.WEB_IS_LIST, true);
//                        bundle.putString(BaseConstant.WEB_URL, "http://192.168.6.50:8979/aspose/tempFile/preview?path=ZDpcVnhCQVBcQkFQLVNFfjFcYmFwLXdvcmtzcGFjZS91cGxvYWRzXDIwMTlcMTJcMjFcMTA3NV8yMDE5MTIyMTEwMTgxMzU0M1zpl6jmiLfpl67popjmlbTnkIYuZG9jeA&type=pdf");
//                        bundle.putString(BaseConstant.WEB_URL,"http://192.168.91.60:8080/msService/baseService/workbench/fileViewUrl?id=1197&type=pdf");
//                        bundle.putString(BaseConstant.WEB_URL, "http://192.168.91.60:8080/greenDill/static/foundation/PDF/web/viewer.html?file=%2FmsService%2FbaseService%2Faspose%2FpdfStreamHandeler%3FfilePath%3DC%3A%2FVxBAP%2Fbap-cloud-server%2Ftools%2Finit%2F..%2F..%2F..%2Fbap-server%2Fbap-workspace%2Fstatic%2FbaseService%2Faspose%2FtempFile%2Fpptx%25E6%2596%2587%25E4%25BB%25B6%25E7%25B1%25BB%25E5%259E%258B.pdf");
//                        bundle.putString(BaseConstant.WEB_URL, "http://192.168.90.134:8979/aspose/tempFile/preview?" +
//                                "path=RDpcQkFQXEJBUC1TRX4xXGJhcC13b3Jrc3BhY2UvdXBsb2Fkc1wyMDE5XDEyXDE3XDEwMDBfMjAxOTEyMTcyMTQwMjg3OTRc5p2t5bee5biC6IGM56ew57O757uf6LWE5paZ5aGr5YaZ5pON5L2c5Y-C6ICDLnBkZg&type=pdf");
//                        bundle.putString(BaseConstant.WEB_URL, "http://192.168.90.134:8979/aspose/tempFile/preview?" +
//                                "path=RDpcQkFQXEJBUC1TRX4xXGJhcC13b3Jrc3BhY2UvdXBsb2Fkc1wyMDE5XDEyXDE4XDEwMDBfMjAxOTEyMTgxMDA4MzQ3NzNc5Yi25oiQ5LiJ57q_5beh5qOA6KGoVjIuMC5kb2N4&type=pdf");
//                        bundle.putString(BaseConstant.WEB_URL, "http://192.168.90.134:8979/aspose/tempFile/preview?" +
//                                "path=RDpcQkFQXEJBUC1TRX4xXGJhcC13b3Jrc3BhY2UvdXBsb2Fkc1wyMDE5XDEyXDE4XDEwMDBfMjAxOTEyMTgxMDA4MzQ3NzVc5beh5qOA5Yy65Z-fX-W3oeajgOWMuuWfny54bHM&type=pdf");
//                        bundle.putString(BaseConstant.WEB_URL,"http://192.168.90.134:8979/aspose/tempFile/preview?path=RDpcQkFQXEJBUC1TRX4xXGJhcC13b3Jrc3BhY2UvdXBsb2Fkc1wyMDE5XDEyXDE4XDEwMDBfMjAxOTEyMTgxMTE0NDY1ODJcd3d3d-aIkS54bHN4&type=html");
//                        IntentRouter.go(context, Constant.Router.FILE_VIEW, bundle);
                        break;
                }
            }
        });
        eamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.EAM_CODE, mEamEntity.code);
                IntentRouter.go(EamDetailActivity.this, Constant.Router.SCORE_EAM_LIST, bundle);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Map<String, Object> param = new HashMap<>();
        param.put(Constant.BAPQuery.EAMCODE, mEamEntity.code);
        presenterRouter.create(AnomalyAPI.class).getAnomalyList(1, 2, param);

    }

    @Override
    protected void initData() {
        super.initData();
        List workInfos = new ArrayList<>();
        WorkInfo workInfo1 = new WorkInfo();
        workInfo1.name = "临时巡检";
        workInfo1.iconResId = R.drawable.menu_aew_selector;
        workInfos.add(workInfo1);
        WorkInfo workInfo2 = new WorkInfo();
        workInfo2.name = "临时润滑";
        workInfo2.iconResId = R.drawable.menu_lubricate_selector;
        workInfos.add(workInfo2);
        WorkInfo workInfo3 = new WorkInfo();
        workInfo3.name = "验收评分";
        workInfo3.iconResId = R.drawable.menu_score_selector;
        workInfos.add(workInfo3);
        WorkInfo workInfo4 = new WorkInfo();
        workInfo4.name = "文档记录";
        workInfo4.iconResId = R.drawable.menu_print_selector;
        workInfos.add(workInfo4);
        workAdapter.setList(workInfos);
        workAdapter.notifyDataSetChanged();

        eamName.setText(mEamEntity.name);
        presenterRouter.create(EamDetailAPI.class).getEamScore(mEamEntity.id);
        new EamPicController().initEamPic(eamPic, mEamEntity.id);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent loginEvent) {
        Map<String, Object> param = new HashMap<>();
        param.put(Constant.BAPQuery.EAMCODE, mEamEntity.code);
        presenterRouter.create(AnomalyAPI.class).getAnomalyList(1, 2, param);
    }


    @Override
    public void getAnomalyListSuccess(CommonBAPListEntity entity) {
        if (entity.result.size() > 0) {
            anomalyLayout.setVisibility(View.GONE);
        } else {
            anomalyLayout.setVisibility(View.VISIBLE);
        }
        anomalyAdapter.setList(entity.result);
        anomalyAdapter.notifyDataSetChanged();
        if (entity.totalCount > 0) {
            Spanned item = HtmlParser.buildSpannedText(String.format(context.getString(R.string.device_style15), "更多", entity.totalCount), new HtmlTagHandler());
            waitMore.setText(item);
        }
    }

    @Override
    public void getAnomalyListFailed(String errorMsg) {
        LogUtil.e("获取待办失败:" + errorMsg);
        if (errorMsg.contains("401")) {
            ToastUtils.show(context,ErrorMsgHelper.msgParse(errorMsg));
        }
        anomalyLayout.setVisibility(View.VISIBLE);
        anomalyAdapter.setList(null);
        anomalyAdapter.notifyDataSetChanged();
    }

    @Override
    public void getEamScoreSuccess(CommonEntity entity) {
        // 默认100分
        eamScore.setText(((String) entity.result));
        float star = Float.valueOf((String) entity.result) / 20;
        starLevel.setRating(star);
    }

    @Override
    public void getEamScoreFailed(String errorMsg) {
        LogUtil.e(errorMsg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
