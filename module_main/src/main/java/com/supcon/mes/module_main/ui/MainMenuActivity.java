package com.supcon.mes.module_main.ui;

import android.annotation.SuppressLint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseControllerActivity;
import com.supcon.common.view.util.LogUtils;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.model.bean.WorkInfo;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.model.api.MainMenuAPI;
import com.supcon.mes.module_main.model.api.MainPendingNumAPI;
import com.supcon.mes.module_main.model.bean.WorkNumEntity;
import com.supcon.mes.module_main.model.contract.MainMenuContract;
import com.supcon.mes.module_main.model.contract.MainPendingNumContract;
import com.supcon.mes.module_main.presenter.MainMenuPresenter;
import com.supcon.mes.module_main.presenter.MainPendingNumPresenter;
import com.supcon.mes.module_main.ui.adaper.WorkAdapter;
import com.supcon.mes.module_main.ui.view.ItemTouchHelperListener;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;


/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/6/7
 * Email zhangwenshuai1@supcon.com
 * Desc 全部应用
 */
@Router(Constant.Router.ALL_MENU_LIST)
@Presenter(value = {MainMenuPresenter.class, MainPendingNumPresenter.class})
public class MainMenuActivity extends BaseControllerActivity implements ItemTouchHelperListener, MainMenuContract.View, MainPendingNumContract.View {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("dragTipTv")
    TextView dragTipTv;
    @BindByTag("myMenuRecycler")
    RecyclerView myMenuRecycler;
    @BindByTag("allMenuRecycler")
    RecyclerView allMenuRecycler;

//    private WorkAdapter myWorkAdapter;
    private WorkAdapter mWorkAdapter;

    private final int SPAN_COUNT = 4;
    public List<WorkInfo> myMenuList;
    private Set<WorkInfo> myMenuSaveSet = new HashSet<>();
    private String myMenuOld;
    private boolean isEdit;

    @Override
    protected int getLayoutID() {
        return R.layout.main_ac_menu_list_new;
    }
    @Override
    protected void onInit() {
        super.onInit();

        GridLayoutManager layoutManager = new GridLayoutManager(context, SPAN_COUNT);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                int viewType = mWorkAdapter.getItemViewType(i);
                if (viewType == WorkInfo.VIEW_TYPE_TITLE || viewType == WorkInfo.VIEW_TYPE_HEADER){
                    return SPAN_COUNT;
                }
                return 1;
            }
        });
        allMenuRecycler.setLayoutManager(layoutManager);
        mWorkAdapter = new WorkAdapter(context);
        allMenuRecycler.setAdapter(mWorkAdapter);

        ItemDragCallback callback = new ItemDragCallback(this);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(allMenuRecycler);


    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(context.getResources().getString(R.string.main_all_menu));
        rightBtn.setImageResource(R.drawable.ic_bottom_ok);
    }

    @Override
    protected void initData() {
        super.initData();
        presenterRouter.create(MainMenuAPI.class).listAllMenu();
        presenterRouter.create(MainMenuAPI.class).listMyMenu(false);

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> onBackPressed());
        rightBtn.setOnClickListener(v -> {
            saveMenuBack();
        });

        mWorkAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            WorkInfo workInfo = (WorkInfo) obj;
            if (action == 1){
                addOrDel(position,workInfo);
            }
        });
    }

    private void addOrDel(int fromPosition, WorkInfo workInfo) {
        if (workInfo == null)
            return;
        int toPosition = 0;
        if (workInfo.mySort == null) { // 添加
            if (myMenuList.size() == 11) {
                ToastUtils.show(context, getString(R.string.main_menu_limit));
                return;
            }
            toPosition = myMenuList.size();
            workInfo.mySort = toPosition;
            workInfo.isAdd = true;
            myMenuList.add(workInfo);
        } else { // 删除
            toPosition = getBackPosition(workInfo);
            workInfo.isAdd = false;
            workInfo.mySort = null;
            myMenuList.remove(workInfo);
        }
        myMenuSaveSet.add(workInfo); // 记录变化项，防止重复添加
        mWorkAdapter.getList().add(toPosition,mWorkAdapter.getList().remove(fromPosition)); // 元素变化
//        Collections.swap(mWorkAdapter.getList(),fromPosition, toPosition);
        mWorkAdapter.notifyItemMoved(fromPosition,toPosition);
        mWorkAdapter.notifyItemRangeChanged(Math.min(fromPosition, toPosition),mWorkAdapter.getList().size() - fromPosition);
    }

     /**
      * @method  回到分组的首位置
      * @description
      * @author: zhangwenshuai
      * @date: 2020/6/8 22:56
      * @param  * @param null
      * @return
      */
    private int getBackPosition(WorkInfo workInfo) {
        for (WorkInfo entity : mWorkAdapter.getList()){
            if (entity.viewType == WorkInfo.VIEW_TYPE_TITLE && entity.layNo.equals(workInfo.layNo)){
                return mWorkAdapter.getList().indexOf(entity);
            }
        }
        return 0;
    }

    @Override
    public void onBackPressed() {
        if (isEdit && !myMenuOld.equals(GsonUtil.gsonString(myMenuList))){
            new CustomDialog(context).twoButtonAlertDialog("是否保存修改？")
                    .bindClickListener(R.id.grayBtn, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }, true)
                    .bindClickListener(R.id.redBtn, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveMenuBack();
                        }
                    }, false)
                    .show();
        }else {
            finish();
        }
    }

    private void saveMenuBack() {
        EamApplication.dao().getWorkInfoDao().saveInTx(myMenuSaveSet);
        EventBus.getDefault().post(new RefreshEvent(Constant.RefreshAction.HOME_APP_MENU, 0));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void listAllMenuSuccess(List entity) {
        mWorkAdapter.setList(entity);
        mWorkAdapter.notifyDataSetChanged();

        presenterRouter.create(MainPendingNumAPI.class).getMainWorkCount(String.valueOf(EamApplication.getAccountInfo().staffId));
    }

    @Override
    public void listMyMenuSuccess(List entity) {
        myMenuList = entity;
        myMenuOld = GsonUtil.gsonString(myMenuList);
    }

    @Override
    public void listMyMenuFailed(String errorMsg) {
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void listAllMenuFailed(String errorMsg) {
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void getMainWorkCountSuccess(CommonBAPListEntity entity) {
        if (entity.result.size() > 0) {
            updateNum(entity.result);
        }
    }

    @Override
    public void getMainWorkCountFailed(String errorMsg) {

    }

    public void setEdit(boolean b) {
        isEdit = b;
        rightBtn.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        dragTipTv.setVisibility(isEdit ? View.VISIBLE : View.GONE);
    }

    @SuppressLint("CheckResult")
    private void updateNum(List<WorkNumEntity> workNumEntities) {
        Flowable.fromIterable(workNumEntities)
                .subscribe(new Consumer<WorkNumEntity>() {
                    @Override
                    public void accept(WorkNumEntity workNumEntity) throws Exception {
                        List<WorkInfo> workInfoList = mWorkAdapter.getList();
                        for (WorkInfo workInfo : workInfoList) {
                            if (workNumEntity.tagName.equals(workInfo.tag)) {
                                workInfo.num = workNumEntity.num;
                                break;
                            }
                        }
                    }
                }, throwable -> {

                }, () -> mWorkAdapter.notifyItemRangeChanged(0,mWorkAdapter.getList().size()-1));
    }

    @Override
    public void onItemMove(RecyclerView.ViewHolder holder, int fromPosition, int targetPosition) {
        if (targetPosition > myMenuList.size()){
            return;
        }
        // 位置交换
        mWorkAdapter.getList().get(fromPosition).mySort = targetPosition;
        mWorkAdapter.getList().get(targetPosition).mySort = fromPosition;
        // 记录变化项，防止重复添加
        myMenuSaveSet.add(mWorkAdapter.getList().get(fromPosition));
        myMenuSaveSet.add(mWorkAdapter.getList().get(targetPosition));
        // 移动
        Collections.swap(mWorkAdapter.getList(),fromPosition, targetPosition);
        mWorkAdapter.notifyItemMoved(fromPosition,targetPosition);
        mWorkAdapter.notifyItemRangeChanged(Math.min(fromPosition, targetPosition),mWorkAdapter.getList().size() - fromPosition);
    }

    @Override
    public void onItemDismiss(RecyclerView.ViewHolder holder) {
        LogUtils.debug(holder.toString());
    }

    @Override
    public void onItemSelect(RecyclerView.ViewHolder holder) {
        holder.itemView.setScaleX(0.8f);
        holder.itemView.setScaleY(0.8f);
    }

    @Override
    public void onItemClear(RecyclerView.ViewHolder holder) {
        holder.itemView.setScaleX(1.0f);
        holder.itemView.setScaleY(1.0f);
    }

}
