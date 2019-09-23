package com.supcon.mes.module_olxj.controller;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.mes.mbap.beans.FilterBean;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.view.CustomAdView;
import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.model.bean.OLXJWorkItemEntity;

/**
 * Created by wangshizhan on 2019/1/16
 * Email:wangshizhan@supcom.com
 */
public class OLXJTitleController extends BaseViewController {

    @BindByTag("contentView")
    RecyclerView contentView;

    //    ImageView headerView;
    View headerView;

    @BindByTag("titleBarLayout")
    RelativeLayout titleBarLayout;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("listDeviceFilter")
    CustomFilterView<FilterBean> listDeviceFilter;

    @BindByTag("xjBtnLayout")
    LinearLayout xjBtnLayout;

    private final int headerHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, context.getResources().getDisplayMetrics());

    private BaseListDataRecyclerViewAdapter<OLXJWorkItemEntity> mAdapter;

    private float currentAlpha = -1;

    boolean isTitleVisible = false;

    private int mState;
    private boolean isClick;

    public OLXJTitleController(View rootView) {
        super(rootView);
    }

    @Override
    public void initView() {
        super.initView();
        xjBtnLayout.setVisibility(View.VISIBLE);
//        titleText.setVisibility(View.GONE);
        listDeviceFilter.setVisibility(View.GONE);
    }

    @Override
    public void initListener() {
        super.initListener();
        contentView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mState = newState;
                if (headerView == null && contentView.getChildAt(0) != null) {
                    headerView = contentView.getChildAt(0).findViewById(R.id.itemRecyclerPic);
                }

//                if(newState != RecyclerView.SCROLL_STATE_IDLE){
//                    xjBtnLayout.setVisibility(View.GONE);
//                }
//                else{
//                    xjBtnLayout.setVisibility(View.VISIBLE);
//                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (headerView == null && contentView.getChildAt(0) != null) {
                    headerView = contentView.getChildAt(0).findViewById(R.id.itemRecyclerPic);
                }

                int scroll = getScroll();
//                changeTitle(scroll);
                if (scroll <= headerHeight) {

                } else if (mState == RecyclerView.SCROLL_STATE_SETTLING) {
                } else {
                    if (mAdapter == null) {
                        mAdapter = (BaseListDataRecyclerViewAdapter<OLXJWorkItemEntity>) contentView.getAdapter();
                    }
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) contentView.getLayoutManager();
                    changeFilter(linearLayoutManager.findFirstVisibleItemPosition());
                }

            }
        });
    }

    /**
     * 获取滚动的高度
     *
     * @return
     */
    private int getScroll() {
        View v = contentView.getChildAt(0);//获取listVIew的第一个子View
        if (v instanceof ImageView || v instanceof CustomAdView) {//如果是继承自ImageView
            if (v != null) {
                int top = v.getTop();//获取图片顶部的坐标
                return -top;//这里作为我们滚动的值返回
            } else {
                return 0;
            }
        } else {//如果不是继承自ImageView，说明已经把headerView完全滚出了屏幕,这里减去了标题栏的高度
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 131, context.getResources().getDisplayMetrics());
//            return headerView.getHeight() - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,70, context.getResources().getDisplayMetrics());
        }
    }

    /**
     * 根据滚动的高度，改变标题栏透明度
     *
     * @param scroll
     */
    private void changeTitle(int scroll) {
        if (headerView == null) {
            return;
        }
        int height = headerView.getHeight() - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, context.getResources().getDisplayMetrics());
        float alpha = 1f / height;
        float a = alpha * scroll;//这里范围从0~1，0完全透明，1不透明

        //滚动变化限制，超过这个值，滚动不做改变
        if (a >= 1) {//不透明
            titleBarLayout.getBackground().setAlpha(255);
            isTitleVisible = true;
            currentAlpha = 255;
            if (titleText.getVisibility() == View.GONE) {
                titleText.setVisibility(View.VISIBLE);
                listDeviceFilter.setVisibility(View.VISIBLE);
            }
        } else {
            a = a * 255;//这里转换成背景透明度的值0~255
            titleBarLayout.getBackground().setAlpha((int) a);
            isTitleVisible = false;
            currentAlpha = a;
            if (titleText.getVisibility() == View.VISIBLE) {
                titleText.setVisibility(View.GONE);
                listDeviceFilter.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 根据滚动的高度，改变标题栏透明度
     */
    private void changeFilter(int position) {

        OLXJWorkItemEntity xjWorkItemEntity = getTitleView(position + 1);

        if (xjWorkItemEntity == null) {
            return;
        }

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) contentView.getLayoutManager();
        View view = linearLayoutManager.getChildAt(1);

        if (view == null) {
            return;
        }

        float y1 = view.getY() - contentView.getScrollY() - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, context.getResources().getDisplayMetrics());
        float y2 = view.getY() - contentView.getScrollY() - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());
//        LogUtil.d("getScrollY:"+contentView.getScrollY()+" getY:"+view.getY());
//        LogUtil.d("y1:"+y1+" y2:"+y2);
        if (y1 > 0 & y2 > 0) {
            return;
        }

        if (!isClick) {
            listDeviceFilter.setCurrentItem(xjWorkItemEntity.title);
        }
    }

    public void isCilck(boolean isClick) {
        this.isClick = isClick;
    }

    private OLXJWorkItemEntity getTitleView(int position) {
        if (position >= mAdapter.getList().size()) {
            return null;
        }
        OLXJWorkItemEntity xjWorkItemEntity = mAdapter.getList().get(position);

        if (xjWorkItemEntity.viewType == ListType.TITLE.value()) {
            return xjWorkItemEntity;
        } else if (position > 0) {
            return getTitleView(position - 1);
        }


        return null;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (currentAlpha != -1) {
            titleBarLayout.getBackground().setAlpha((int) currentAlpha);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        titleBarLayout.getBackground().setAlpha(255);
    }
}
