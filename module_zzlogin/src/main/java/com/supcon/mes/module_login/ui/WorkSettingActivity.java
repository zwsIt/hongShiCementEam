package com.supcon.mes.module_login.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_login.R;
import com.supcon.mes.module_login.model.api.WorkSettingAPI;
import com.supcon.mes.module_login.model.bean.WorkInfo;
import com.supcon.mes.module_login.model.contract.WorkSettingContract;
import com.supcon.mes.module_login.model.event.WorkRefreshEvent;
import com.supcon.mes.module_login.presenter.WorkSettingPresneter;
import com.supcon.mes.module_login.ui.adapter.WorkSettingAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by wangshizhan on 2017/11/16.
 * Email:wangshizhan@supcon.com
 */

@Router(Constant.Router.WORK_SETTING)
@Presenter(value = {WorkSettingPresneter.class})
public class WorkSettingActivity extends BaseRefreshRecyclerActivity<WorkInfo> implements WorkSettingContract.View{

    @BindByTag("contentView")
    RecyclerView recyclerView;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("rightBtn")
    ImageButton rightBtn;

    WorkSettingAdapter mAdapter;

    List<WorkInfo> works;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_work_setting;
    }

    @Override
    protected void onInit() {
        super.onInit();
        works = GsonUtil.jsonToList(SharedPreferencesUtils.getParam(context, Constant.SPKey.WORKS, ""), WorkInfo.class);
        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(false);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("功能项设置");

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.work_gap);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

//        rightBtn.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> {

            onBackPressed();
        });

        rightBtn.setOnClickListener(v -> {
            presenterRouter.create(WorkSettingAPI.class).setWork(works);
        });

        mAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
//            works.get(position).isOpen = (boolean) obj;
//            mPresenterRouter.create(WorkSettingAPI.class).setWork(works);
            WorkInfo workInfo = ((WorkInfo) obj);
            boolean isMatch = false;
            for(int i =0; i< works.size() && !isMatch;i++){
                if(workInfo.name.equals(works.get(i).name)){
                    works.get(i).isOpen = workInfo.isOpen;
                    presenterRouter.create(WorkSettingAPI.class).setWork(works);
                    isMatch = true;
                }
            }

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

        Flowable.timer(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        EventBus.getDefault().post(new WorkRefreshEvent());
                    }
                });
    }

    @Override
    protected IListAdapter<WorkInfo> createAdapter() {
        mAdapter = new WorkSettingAdapter(context);
        return mAdapter;
    }

    @Override
    protected void initData() {
        super.initData();
        Flowable.fromIterable(works)
                .compose(RxSchedulers.io_main())
                .filter(new Predicate<WorkInfo>() {
                    @Override
                    public boolean test(WorkInfo workInfo) throws Exception {
                        return workInfo.type!= Constant.WorkType.MORE && workInfo.viewType == 0;
                    }
                })
                .subscribe(new Consumer<WorkInfo>() {
                    @Override
                    public void accept(WorkInfo workInfo) throws Exception {
                        mAdapter.addData(workInfo);
                        mAdapter.notifyItemInserted(mAdapter.getItemCount()-1);
                    }
                });
//        refreshListController.refreshComplete(works);
    }

    @Override
    public void setWorkSuccess() {
        refreshListController.refreshComplete();
//        finish();
    }

    @Override
    public void setWorkFailed(String errorMsg) {
        refreshListController.refreshComplete();
    }
}
