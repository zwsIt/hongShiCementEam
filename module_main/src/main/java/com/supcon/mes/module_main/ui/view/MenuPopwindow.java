package com.supcon.mes.module_main.ui.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.ModuleAuthorization;
import com.supcon.mes.middleware.model.bean.ModuleAuthorizationDao;
import com.supcon.mes.middleware.ui.view.PopwinBackView;
import com.supcon.mes.module_login.BuildConfig;
import com.supcon.mes.module_main.R;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * @author yangfei.cao
 * @ClassName depot
 * @date 2018/8/13
 * ------------- Description -------------
 */
public class MenuPopwindow extends PopupWindow implements PopupWindow.OnDismissListener {
    private View conentView;
    private RecyclerView lvContent;
    private final MyAdapter myAdapter;
    private List<MenuPopwindowBean> beans;

    public static final int left = 0;
    public static final int right = 1;
    private PopwinBackView popwinBackView;
    private Activity context;

    public MenuPopwindow(Activity context, List<MenuPopwindowBean> list) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        conentView = inflater.inflate(R.layout.menu_popup_window, null);
        lvContent = conentView.findViewById(R.id.lv_toptitle_menu);
        popwinBackView = conentView.findViewById(R.id.pv_triangle);
        lvContent.setLayoutManager(new GridLayoutManager(context, 2));
        myAdapter = new MyAdapter(context);
        myAdapter.setList(list);
        lvContent.setAdapter(myAdapter);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w * 3 / 4);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
        beans = new LinkedList<>();
        beans.addAll(list);
        setOnDismissListener(this::onDismiss);
    }

    public void setOnItemChildViewClickListener(OnItemChildViewClickListener onItemChildViewClickListener) {
        myAdapter.setOnItemChildViewClickListener(onItemChildViewClickListener);
    }

    @Override
    public void onDismiss() {
        changeWindowAlfa(1f);//pop消失，透明度恢复
    }

    class MyAdapter extends BaseListDataRecyclerViewAdapter<MenuPopwindowBean> {


        public MyAdapter(Context context) {
            super(context);
        }

        @Override
        protected BaseRecyclerViewHolder<MenuPopwindowBean> getViewHolder(int viewType) {
            return new ViewHolder(context);
        }

        class ViewHolder extends BaseRecyclerViewHolder<MenuPopwindowBean> {

            @BindByTag("menuTip")
            TextView menuTip;
            @BindByTag("menuName")
            TextView menuName;
            @BindByTag("menuNum")
            TextView menuNum;


            public ViewHolder(Context context) {
                super(context, parent);
            }

            @Override
            protected int layoutId() {
                return R.layout.menu_popup_window_item;
            }

            @Override
            protected void initView() {
                super.initView();
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MenuPopwindowBean item = getItem(getAdapterPosition());
                        switch (item.getRouter()) {
                            case Constant.Router.XJGL_LIST:
                            case Constant.Router.JHXJ_LIST:
                            case Constant.Router.LSXJ_LIST:
                            case Constant.Router.XJLX_LIST:
                            case Constant.Router.XJQY_LIST:
                            case Constant.Router.XJBB:
                                if (!SharedPreferencesUtils.getParam(context, Constant.ModuleAuthorization.mobileEAM, false)) {
                                    if (!queryModuleAuthorized(Constant.ModuleAuthorization.mobileEAM)) {
                                        ToastUtils.show(context, "移动巡检模块未授权，请联系相关管理人员，确保授权并重启该app");
                                        return;
                                    }
                                }
                                break;
                            case Constant.Router.SBDA_LIST:
                            case Constant.Router.SBDA_ONLINE_LIST:
                            case Constant.Router.STOP_POLICE:
                            case Constant.Router.YH_LIST:
                            case Constant.Router.WXGD_LIST:
                            case Constant.Router.OFFLINE_YH_LIST:
                            case Constant.Router.BY:
                            case Constant.Router.RH:
                            case Constant.Router.YXJL_LIST:
                            case Constant.Router.BJSQ_LIST:
                                if (!SharedPreferencesUtils.getParam(context, Constant.ModuleAuthorization.BEAM2, false)) {
                                    if (!queryModuleAuthorized(Constant.ModuleAuthorization.BEAM2)) {
                                        ToastUtils.show(context, "设备模块未授权，请联系相关管理人员，确保授权并重启该app");
                                        return;
                                    }

                                }
                                break;
                            case Constant.Router.SD:
                            case Constant.Router.TD:
                            case Constant.Router.SJSC:
                            case Constant.Router.SJXZ:
                            case Constant.Router.SPARE_EARLY_WARN:
                            case Constant.Router.LUBRICATION_EARLY_WARN:
                            case Constant.Router.DAILY_LUBRICATION_EARLY_WARN:
                            case Constant.Router.MAINTENANCE_EARLY_WARN:
                            case Constant.Router.SCORE_EAM_LIST:
                            case Constant.Router.SCORE_INSPECTOR_STAFF_LIST:
                            case Constant.Router.SCORE_MECHANIC_STAFF_LIST:
                            case Constant.Router.ACCEPTANCE_LIST:
                                break;
                            default:
                                ToastUtils.show(context, "暂无数据！");
                                return;
                        }
                        onItemChildViewClick(itemView, 0, getItem(getLayoutPosition()));
                    }
                });
            }

            @Override
            protected void initListener() {
                super.initListener();
                itemView.setOnClickListener(v -> onItemChildViewClick(itemView, 0, getItem(getLayoutPosition())));
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void update(MenuPopwindowBean data) {
                menuName.setText(data.getName());
                if (data.getNum() > 0) {
                    menuNum.setVisibility(View.VISIBLE);
                    menuNum.setText(String.valueOf(data.getNum()));
                    menuTip.setSelected(true);
                } else {
                    menuNum.setVisibility(View.INVISIBLE);
                    menuTip.setSelected(false);
                }
            }
        }

        private boolean queryModuleAuthorized(String moduleCode) {
            ModuleAuthorization moduleAuthorization = EamApplication.dao().getModuleAuthorizationDao().queryBuilder()
                    .where(ModuleAuthorizationDao.Properties.ModuleCode.eq(moduleCode)).unique();

            return BuildConfig.DEBUG || moduleAuthorization != null && moduleAuthorization.isAuthorized;
        }
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent, int gravity, int mark) {
        if (!isShowing()) {
            changeWindowAlfa(0.6f);//改变窗口透明度
            parent.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int[] location = new int[2];
            parent.getLocationOnScreen(location);
            //优先进行计算
            this.getContentView().measure(0, 0);
            //之后通过此方法回去就可以了
            int measuredHeight = this.getContentView().getMeasuredHeight();
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) popwinBackView.getLayoutParams();
            int w = context.getWindowManager().getDefaultDisplay().getWidth();
            int margin;
            int x = 0;
            if (gravity == left) {
                if (mark == 1) {
                    margin = w * 3 / 40;
                    x = location[0] - w * 3 / 4 + w * 3 / 40 + parent.getWidth() / 2;
                } else {
                    margin = w * 3 / 16;
                    x = location[0] - w * 3 / 4 + w * 3 / 16 + parent.getWidth() / 2;
                }
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                layoutParams.rightMargin = margin;
            } else if (gravity == right) {
                if (mark == 1) {
                    margin = w * 3 / 40;
                    x = location[0] + parent.getWidth() / 2 - w * 3 / 40;
                } else {
                    margin = w * 3 / 16;
                    x = location[0] + parent.getWidth() / 2 - w * 3 / 16;
                }
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                layoutParams.leftMargin = margin;
            }
            this.showAtLocation(parent, Gravity.NO_GRAVITY, x, location[1] - measuredHeight);
            popwinBackView.setLayoutParams(layoutParams);
        } else {
            this.dismiss();
        }
    }

    @SuppressLint("CheckResult")
    public boolean refreshList(List<MenuPopwindowBean> list) {
        beans = new LinkedList<>();
        Flowable.fromIterable(list)
                .filter(MenuPopwindowBean::isPower)
                .subscribe(menuPopwindowBean -> beans.add(menuPopwindowBean), throwable -> {
                }, () -> {
                    if (beans.size() > 0) {
                        myAdapter.setList(beans);
                        myAdapter.notifyDataSetChanged();
                    }
                });
        if (beans.size() > 0) {
            return true;
        } else {
            ToastUtils.show(context, "当前模块未分配操作权限!");
            return false;
        }
    }

    public List<MenuPopwindowBean> getBeans() {
        return beans;
    }

    /*
       更改屏幕窗口透明度
    */
    public void changeWindowAlfa(float alfa) {
        WindowManager.LayoutParams params = context.getWindow().getAttributes();
        params.alpha = alfa;
        context.getWindow().setAttributes(params);
    }
}

