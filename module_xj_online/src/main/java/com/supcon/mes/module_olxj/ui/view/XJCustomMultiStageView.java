package com.supcon.mes.module_olxj.ui.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.base.view.BaseLinearLayout;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.ViewUtil;
import com.supcon.mes.mbap.view.CustomCircleTextImageView;
import com.supcon.mes.middleware.R;
import com.supcon.mes.middleware.model.bean.CustomMultiStageEntity;
import com.supcon.mes.middleware.model.bean.DepartmentInfo;
import com.supcon.mes.middleware.model.bean.IDataInjector;
import com.supcon.mes.middleware.model.bean.ILayoutProvider;
import com.supcon.mes.middleware.ui.view.CustomMultiStageView;
import com.supcon.mes.module_olxj.ui.adapter.XJCustomMultiStageAdapter;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @Author xushiyun
 * @Create-time 7/18/19
 * @Pageage com.supcon.mes.middleware.ui.view
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public class XJCustomMultiStageView<Data> extends BaseLinearLayout {
    @BindByTag("contentView")
    private RecyclerView contentView;
    private XJCustomMultiStageAdapter mCustomMultiStageAdapter;
    private CustomMultiStageEntity<Data> rootEntity;

    public XJCustomMultiStageView(Context context) {
        this(context, null);
    }

    public XJCustomMultiStageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int layoutId() {
        return R.layout.v_multi_stage;
    }

    @Override
    protected void initView() {
        super.initView();
        contentView = rootView.findViewById(R.id.contentView);
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(10));
        mCustomMultiStageAdapter = new XJCustomMultiStageAdapter(context);
        mCustomMultiStageAdapter.registerController(new CustomMultiStageViewController(new View(context)));
        contentView.setAdapter(mCustomMultiStageAdapter);
    }

    public void setRootEntity(CustomMultiStageEntity<Data> customMultiStageEntity) {
        this.rootEntity = customMultiStageEntity;
        mCustomMultiStageAdapter.setRootEntity(customMultiStageEntity);
    }

    public void setOnItemChildViewClickListener(OnItemChildViewClickListener onItemChildViewClickListener) {
        mCustomMultiStageAdapter.setOnItemChildViewClickListener(onItemChildViewClickListener);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    public class CustomMultiStageViewController extends BaseViewController implements ILayoutProvider, IDataInjector<CustomMultiStageEntity<DepartmentInfo>> {

        @BindByTag("areaName")
        TextView areaName;
        @BindByTag("areaIconContainer")
        LinearLayout areaIconContainer;
        CustomMultiStageEntity<DepartmentInfo> data;
        @BindByTag("areaIcon")
        ImageView areaIcon;
        @BindByTag("userIcon")
        CustomCircleTextImageView userIcon;
        @BindByTag("spaceView")
        ImageView spaceView;
        @BindByTag("detailInfo")
        TextView detailInfo;

        public CustomMultiStageViewController(View rootView) {
            super(rootView);
        }

        @Override
        public void attachView(View rootView) {
            super.attachView(rootView);
        }

        @Override
        public void inject(CustomMultiStageEntity<DepartmentInfo> data) {

        }

        @Override
        public void inject(CustomMultiStageEntity<DepartmentInfo> data, int type) {
            this.data = data;
            if (data.isLeafNode()) {
                userIcon.setVisibility(VISIBLE);
                areaIcon.setVisibility(GONE);
                if (data.getCurrentEntity().userInfo != null) {
                    String name = data.getCurrentEntity().userInfo.getNAME();
                    areaName.setText(name);
                    userIcon.setText(name.substring((name.length() - 2 < 0 ? 0 : name.length() - 2)));
                }
                detailInfo.setVisibility(VISIBLE);
//                detailInfo.setText(data.getCurrentEntity().userInfo.et());
                return;
            } else {
                detailInfo.setVisibility(INVISIBLE);
                userIcon.setVisibility(GONE);
                areaIcon.setVisibility(VISIBLE);
                areaName.setText(data.getCurrentEntity().name
//                        + (!data.isRootEntity() ?
//                        "(" + data.getCurrentEntity().layRec + ")" : "")
                );
            }
            if (data.isRootEntity()) {
                areaIcon.setImageResource(R.drawable.ic_multi_home);
            } else if (data.getChildListSize() <= 0) {
                areaIcon.setVisibility(INVISIBLE);
            } else if (data.isExpanded()) {
                areaIcon.setImageResource(R.drawable.ic_multi_minus);
            } else {
                areaIcon.setImageResource(R.drawable.ic_multi_add);
            }
//            LogUtil.e(data.getCurrentEntity().toString(), "徐诗韵1234444: " + type + "");
            spaceView.setVisibility(data.isRootEntity() ? GONE : INVISIBLE);
            areaIconContainer.post(() -> {
                        spaceView.setVisibility(GONE);
                        ViewUtil.setMarginLeft(areaIconContainer, (data.isRootEntity() || type <= 0 ? 0 : type) * areaIcon.getWidth());
                    }
            );
        }

        public void changeStatus() {
            if (data.isRootEntity()) {
//                areaIcon.setImageResource(R.drawable.ic_multi_home);
            } else if (data.getChildListSize() <= 0) {
//                areaIcon.setVisibility(INVISIBLE);
            } else if (data.isExpanded()) {
                areaIcon.setImageResource(R.drawable.ic_multi_minus);
            } else {
                areaIcon.setImageResource(R.drawable.ic_multi_add);
            }
        }


        @Override
        public int layout() {
            return R.layout.item_multi_stage;
        }

        public CustomMultiStageViewController newInstance(View rootView) {
            return new CustomMultiStageViewController(rootView);
        }
    }

}
