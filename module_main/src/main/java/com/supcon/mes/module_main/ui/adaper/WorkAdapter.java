package com.supcon.mes.module_main.ui.adaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.BaseConstant;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.HeaderRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.ModuleAuthorization;
import com.supcon.mes.middleware.model.bean.ModuleAuthorizationDao;
import com.supcon.mes.middleware.util.RequestOptionUtil;
import com.supcon.mes.module_login.BuildConfig;
import com.supcon.mes.module_login.R;
import com.supcon.mes.middleware.model.bean.WorkInfo;
import com.supcon.mes.module_main.IntentRouter;
import com.supcon.mes.module_main.ui.MainActivity;
import com.supcon.mes.module_main.ui.MainMenuActivity;
import com.supcon.mes.module_main.ui.fragment.HomeFragment;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by wangshizhan on 2017/8/16.
 * Email:wangshizhan@supcon.com
 */

public class WorkAdapter extends BaseListDataRecyclerViewAdapter<WorkInfo> {

    private boolean isEdit;
    private boolean eamHome; // 设备首页

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
        }else if (viewType == WorkInfo.VIEW_TYPE_HEADER){
            return new HeaderViewHolder(context);
        }
        return new ContentViewHolder(context);
    }

    public void setEamHome(boolean b) {
        this.eamHome = b;
    }

    class HeaderViewHolder extends BaseRecyclerViewHolder<WorkInfo> {

        @BindByTag("allMenuTv")
        TextView allMenuTv;

        public HeaderViewHolder(Context context) {
            super(context);
        }


        @Override
        protected void initListener() {
            super.initListener();
        }

        @Override
        protected int layoutId() {
            return R.layout.item_work_header;
        }

        @Override
        protected void update(WorkInfo data) {
            allMenuTv.setText(data.name);
        }
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
        }
    }

    class ContentViewHolder extends BaseRecyclerViewHolder<WorkInfo> {

        ImageView workIcon;

        @BindByTag("workName")
        TextView workName;

        @BindByTag("workNum")
        TextView workNum;
        @BindByTag("btn")
        ImageView btn;


        public ContentViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initView() {
            super.initView();
            workIcon = itemView.findViewById(R.id.workIcon);
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();

            RxView.clicks(itemView)
                    .filter(o -> {
                        if (eamHome){
                            onItemChildViewClick(itemView, 0, getItem(getAdapterPosition()));
                            return false;
                        }
                        if (isEdit){
                            onItemChildViewClick(itemView, 1, getItem(getAdapterPosition()));
                            return false;
                        }
                        return true;
                    }).subscribe(new Consumer<Object>() {
                @Override
                public void accept(Object o) throws Exception {
                    WorkInfo workInfo = getItem(getAdapterPosition());
                    if(TextUtils.isEmpty(workInfo.router)){
                        return;
                    }
                    Bundle bundle = new Bundle();
                    if (workInfo.type == Constant.WorkType.TSDTJ) {
                        bundle.putString(BaseConstant.WEB_AUTHORIZATION, EamApplication.getAuthorization());
                        bundle.putString(BaseConstant.WEB_COOKIE, EamApplication.getCooki());
                        bundle.putBoolean(BaseConstant.WEB_HAS_REFRESH, false);
                        bundle.putBoolean(BaseConstant.WEB_IS_LIST, true);
                        bundle.putString(BaseConstant.WEB_URL, "http://" + EamApplication.getIp() + ":" + EamApplication.getPort() + Constant.WebUrl.TSD_STATISTICS + "&date=" + System.currentTimeMillis());
                    }
                    IntentRouter.go(context,workInfo.router,bundle);
//                    onItemChildViewClick(itemView, 0, getItem(getAdapterPosition()));
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (context instanceof MainMenuActivity) {
                    if (isEdit){
                        return false;
                    }
                     // 首次长按 可编辑
                    isEdit = true;
                    ((MainMenuActivity)context).setEdit(true);
                    notifyDataSetChanged();
                    return false;
                }
                return true;
            });

        }

        @Override
        protected int layoutId() {
            return R.layout.item_work;
        }

        @Override
        protected void update(WorkInfo data) {
            if (workIcon.getTag(R.id.imageid) != null && !workIcon.getTag(R.id.imageid).equals(data.iconUrl)){
                Glide.with(context).clear(workIcon);
            }
            if (data.iconResId != 0) {
                workIcon.setImageResource(data.iconResId);
            } else {
                Glide.with(context).load(data.iconUrl).apply(RequestOptionUtil.getWorkRequestOptions(context)).into(workIcon);
                workIcon.setTag(R.id.imageid,data.iconUrl);
            }
            workName.setText(data.name);
            if (data.num > 0 && !isEdit) {
                workNum.setVisibility(View.VISIBLE);
                if (data.num < 99) {
                    workNum.setText(String.valueOf(data.num));
                } else {
                    workNum.setText("99+");
                }
            } else {
                workNum.setVisibility(View.GONE);
            }

            if (isEdit){
                btn.setVisibility(View.VISIBLE);
                if (data.isAdd){
                    btn.setImageResource(R.drawable.ic_menu_delete);
                }else {
                    btn.setImageResource(R.drawable.ic_menu_add);
                }
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
