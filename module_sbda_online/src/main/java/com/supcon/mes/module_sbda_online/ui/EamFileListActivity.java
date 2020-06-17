package com.supcon.mes.module_sbda_online.ui;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.listener.OnTitleSearchExpandListener;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.CommonListAPI;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.contract.CommonListContract;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.KeyExpandHelper;
import com.supcon.mes.module_sbda_online.R;
import com.supcon.mes.module_sbda_online.model.bean.EamFileEntity;
import com.supcon.mes.module_sbda_online.presenter.EamFIleListPresenter;
import com.supcon.mes.module_sbda_online.ui.adapter.EamFileListAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * EamFileListActivity 设备文档
 * created by zhangwenshuai1 2019/12/29
 */
@Presenter(EamFIleListPresenter.class)
@Router(Constant.Router.EAM_FILE_LIST)
public class EamFileListActivity extends BaseRefreshRecyclerActivity<EamFileEntity> implements CommonListContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("titleBarLayout")
    RelativeLayout titleBarLayout;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("refreshFrameLayout")
    PtrFrameLayout refreshFrameLayout;

    private EamFileListAdapter mEamFileListAdapter;
    private Map<String, Object> queryParam = new HashMap<>();

    @Override
    protected IListAdapter createAdapter() {
        mEamFileListAdapter = new EamFileListAdapter(this);
        return mEamFileListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_eam_file_list;
    }

    @Override
    protected void onInit() {
        super.onInit();

        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);

        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(3, context)));

        EamEntity eamEntity = (EamEntity) getIntent().getSerializableExtra(Constant.IntentKey.EAM);
        queryParam.put("baseInfo.id",eamEntity.id);
    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setText("设备文档");
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, ""));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> {
            back();
        });
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(CommonListAPI.class).listCommonObj(1,queryParam,false);
            }
        });
        mEamFileListAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                EamFileEntity eamFileEntity = (EamFileEntity) obj;
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void listCommonObjSuccess(CommonBAPListEntity entity) {
        //过滤文件
        List<EamFileEntity> eamFileEntityList = new ArrayList<>();
        Flowable.fromIterable(entity.result)
                .filter(new Predicate() {
                    @Override
                    public boolean test(Object o) throws Exception {
                        EamFileEntity eamFileEntity = (EamFileEntity) o;
                        return eamFileEntity.getDocNameMultiFileNames().contains(".pdf") || eamFileEntity.getDocNameMultiFileNames().contains(".doc")
                                || eamFileEntity.getDocNameMultiFileNames().contains(".ppt") || eamFileEntity.getDocNameMultiFileNames().contains(".xls");
                    }
                }).subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                eamFileEntityList.add((EamFileEntity) o);
            }
        }, throwable -> {

        }, () -> refreshListController.refreshComplete(eamFileEntityList));

    }

    @Override
    public void listCommonObjFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context,ErrorMsgHelper.msgParse(errorMsg));
    }
}
