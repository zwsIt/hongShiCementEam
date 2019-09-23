package com.supcon.mes.module_login.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.bumptech.glide.Glide;
import com.supcon.common.view.base.adapter.HeaderRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.ModuleAuthorization;
import com.supcon.mes.middleware.model.bean.ModuleAuthorizationDao;
import com.supcon.mes.middleware.util.RequestOptionUtil;
import com.supcon.mes.module_login.BuildConfig;
import com.supcon.mes.module_login.R;
import com.supcon.mes.module_login.model.bean.WorkInfo;

/**
 * Created by wangshizhan on 2017/8/16.
 * Email:wangshizhan@supcon.com
 */

public class WorkAdapter extends HeaderRecyclerViewAdapter<WorkInfo> {

    public WorkAdapter(Context context) {
        super(context);

    }

    @Override
    public int getItemViewType(int position, WorkInfo workInfo) {

        return workInfo.viewType;
    }

    @Override
    protected BaseRecyclerViewHolder<WorkInfo> getViewHolder(int viewType) {

        if (viewType == WorkInfo.VIEW_TYPE_TITLE) {
            return new TitleViewHolder(context);
        }
        return new ContentViewHolder(context);
    }

    class TitleViewHolder extends BaseRecyclerViewHolder<WorkInfo> {

        @BindByTag("contentTitleIc")
        TextView contentTitleIc;

        @BindByTag("contentTitleLabel")
        TextView contentTitleLabel;

        @BindByTag("contentTitleSettingIc")
        ImageView contentTitleSettingIc;

        public TitleViewHolder(Context context) {
            super(context);
        }


        @Override
        protected void initListener() {
            super.initListener();
            contentTitleSettingIc.setOnClickListener(v -> onItemChildViewClick(contentTitleSettingIc, 1));
        }

        @Override
        protected int layoutId() {
            return R.layout.item_work_title;
        }

        @Override
        protected void update(WorkInfo data) {
            contentTitleLabel.setText(data.name);

            if (data.type == -1 && BuildConfig.DEBUG) {
                contentTitleSettingIc.setVisibility(View.VISIBLE);
            } else {
                contentTitleSettingIc.setVisibility(View.GONE);
            }
        }
    }

    class ContentViewHolder extends BaseRecyclerViewHolder<WorkInfo> {

//        @BindByTag("workIcon")
        ImageView workIcon;

        @BindByTag("workName")
        TextView workName;

        @BindByTag("workNum")
        TextView workNum;


        public ContentViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initView() {
            super.initView();
            workIcon = itemView.findViewById(R.id.workIcon);
        }

        @Override
        protected void initListener() {
            super.initListener();

            itemView.setOnClickListener(v -> {

                if (onItemChildViewClickListener != null) {

                    int position = getAdapterPosition();

                    WorkInfo workInfo = getItem(position);

                    if(workInfo == null){
                        return;
                    }

                    //添加模块授权判断
                    if(workInfo.appItem == null)
                    switch (workInfo.router) {
                        case Constant.Router.XJGL_LIST:
                        case Constant.Router.JHXJ_LIST:
                        case Constant.Router.LSXJ_LIST:
                        case Constant.Router.XJLX_LIST:
                        case Constant.Router.XJQY_LIST:
                        case Constant.Router.XJBB:
                            if (!SharedPreferencesUtils.getParam(context, Constant.ModuleAuthorization.mobileEAM,false)) {
                                if (!queryModuleAuthorized(Constant.ModuleAuthorization.mobileEAM)) {
                                    ToastUtils.show(context,"移动巡检模块未授权，请联系相关管理人员，确保授权并重启该app");
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
                            if (!SharedPreferencesUtils.getParam(context, Constant.ModuleAuthorization.BEAM2,false)) {
                                if (!queryModuleAuthorized(Constant.ModuleAuthorization.BEAM2)) {
                                    ToastUtils.show(context,"设备模块未授权，请联系相关管理人员，确保授权并重启该app");
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
//                            ToastUtils.show(context,"暂无数据！");
//                            return;
                            break;
                    }


                    onItemChildViewClick(itemView, 0, workInfo);
                }

            });

        }

        @Override
        protected int layoutId() {

            return R.layout.item_work;
        }

        @Override
        protected void update(WorkInfo data) {

            if(data.iconResId!=0){
                workIcon.setImageResource(data.iconResId);
            }
            else{
                Glide.with(context).load(data.iconUrl).apply(RequestOptionUtil.getWorkRequestOptions(context)).into(workIcon);
            }
            workName.setText(data.name);

            if (data.num > 0) {
                workNum.setVisibility(View.VISIBLE);
                if (data.num < 99) {
                    workNum.setText(String.valueOf(data.num));
                } else {
                    workNum.setText("99+");
                }

            } else {
                workNum.setVisibility(View.GONE);

            }


        }
    }

    private boolean queryModuleAuthorized(String moduleCode) {
//        Cursor cursor = EamApplication.dao().getDatabase().rawQuery("select * from MODULE_AUTHORIZATION where MODULE_NAME = ? ",new String[]{moduleName} );
        ModuleAuthorization moduleAuthorization = EamApplication.dao().getModuleAuthorizationDao().queryBuilder()
                .where(ModuleAuthorizationDao.Properties.ModuleCode.eq(moduleCode)).unique();

        return BuildConfig.DEBUG || moduleAuthorization != null && moduleAuthorization.isAuthorized;
    }
}
