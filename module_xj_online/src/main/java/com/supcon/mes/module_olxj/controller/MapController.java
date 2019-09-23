package com.supcon.mes.module_olxj.controller;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.view.js.BaseBridgeWebViewClient;
import com.supcon.common.view.view.js.BridgeHandler;
import com.supcon.common.view.view.js.BridgeUtil;
import com.supcon.common.view.view.js.BridgeWebView;
import com.supcon.common.view.view.js.CallBackFunction;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_olxj.model.bean.OLXJAreaEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJTaskEntity;
import com.supcon.mes.module_olxj.model.event.AreaRefreshEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangshizhan on 2019/5/22
 * Email:wangshizhan@supcom.com
 */
public class MapController extends BaseViewController {

    @BindByTag("mapLayout")
    LinearLayout mapLayout;
    @BindByTag("progressBar")
    ProgressBar progressBar;
    @BindByTag("webView")
    BridgeWebView webView;
    private boolean isShow = false;
    //地图区域点击触发事件
    private OnMapAreaClickListener mOnMapAreaClickListener;
    //在线巡检区域数据列表
    private List<OLXJAreaEntity> mOLXJAreaEntities;

    public MapController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    
    /**
     * 刷新地图视图
     * @param areaRefreshEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAreaRefresh(AreaRefreshEvent areaRefreshEvent) {
        if(isShow) {
            webView.reload();
        }
    }

    @Override
    public void initView() {
        super.initView();
        webView.setFocusableInTouchMode(true);
        webView.setWebViewClient(new MapWebViewClient(webView));
        webView.setWebChromeClient(new MyWebChromeClient());
        WebSettings settings = webView.getSettings();
//        settings.setAppCacheEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setUseWideViewPort(true);

        settings.setBuiltInZoomControls(true); // 显示放大缩小
        settings.setSupportZoom(true); // 可以缩放
        settings.setDisplayZoomControls(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        settings.setLoadWithOverviewMode(true);
    }

    @Override
    public void initListener() {
        super.initListener();

        if(webView!=null){
            webView.registerHandler("areaClick", new BridgeHandler() {

                @Override
                public void handler(String data, CallBackFunction function) {
                    LogUtil.e("areaClick" + data);
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String code = jsonObject.getString("id");
                        if(mOLXJAreaEntities == null){

                        }
                        else if (!TextUtils.isEmpty(code))
                            for (OLXJAreaEntity areaEntity : mOLXJAreaEntities) {
                                if (code.equals(areaEntity._code)) {
                                    if(mOnMapAreaClickListener!=null){
                                        mOnMapAreaClickListener.onAreaClick(areaEntity);
                                    }
                                    return;
                                }
                            }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });
        }

    }

    public void setOLXJAreaEntities(List<OLXJAreaEntity> OLXJAreaEntities) {
        mOLXJAreaEntities = OLXJAreaEntities;
    }

    public void setOnMapAreaClickListener(OnMapAreaClickListener onMapAreaClickListener) {
        mOnMapAreaClickListener = onMapAreaClickListener;
    }

    public void show(OLXJTaskEntity data) {
        isShow = true;
        if (data==null){
            mapLayout.setVisibility(View.INVISIBLE);
            return;
        }
        mapLayout.setVisibility(View.VISIBLE);
        
        //当页面正在加载时，禁止链接的点击事件
        Map<String, String> header = new HashMap<>();
        String url = "http://" + EamApplication.getIp() + ":" + EamApplication.getPort()
                + Constant.WebUrl.XJ + data.id + "&WorkGroupID=" + data.workGroupID.id;
        if (!TextUtils.isEmpty(EamApplication.getCooki())) {
            header.put("Cookie", EamApplication.getCooki());
        }
        if (!TextUtils.isEmpty(EamApplication.getAuthorization())) {
            header.put("Authorization", EamApplication.getAuthorization());
        }
        //session过期导致问题
        webView.loadUrl(url, header);
    }

    public void hide() {
        isShow = false;
        mapLayout.setVisibility(View.GONE);

    }


//    private class MyWebViewClient extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            return true;
//        }
//
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
//                    , context.getResources().getDisplayMetrics().heightPixels - Util.dpToPx(context, 160));
//            webView.setLayoutParams(lp);
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//
//
//        }
//
//        @Override
//        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//            super.onReceivedError(view, request, error);
//        }
//    }

    private class MapWebViewClient extends BaseBridgeWebViewClient {

        public MapWebViewClient(BridgeWebView webView) {
            super(webView);
        }

        @Override
        protected boolean dealUrl(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                    , LinearLayout.LayoutParams.MATCH_PARENT/*DisplayUtil.getScreenHeight(context) - DisplayUtil.dip2px(180, context)*/);
            webView.setLayoutParams(lp);
            BridgeUtil.webViewLoadLocalJs(webView, "xj.js");

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                // 网页加载完成
                progressBar.setVisibility(View.GONE);
            } else {
                // 加载中
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }


//    public class RecyclerViewOnTouchListener implements View.OnTouchListener {
//
//
//        private int mLastY;
//        private int mCurrentY;
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//
//            //获取WebView所在item的顶部相对于其父控件（即RecyclerView的父控件）的距离
//            if (mapLayout.getVisibility() != View.VISIBLE
//                    || !isInView(mapLayout, event)) {
//                return false;
//            }
//
//            //计算dy，用来判断滑动方向。dy<0-->向上滑动；dy>0-->向下滑动。
//            int dy = 0;
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    mLastY = (int) event.getY();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    mCurrentY = (int) event.getY();
//                    dy = mCurrentY - mLastY;
//                    mLastY = mCurrentY;
//                    break;
//                case MotionEvent.ACTION_CANCEL:
//                case MotionEvent.ACTION_UP:
//                    dy = (int) (event.getY() - mLastY);
//                    mLastY = 0;
//                    mCurrentY = 0;
//                    break;
//            }
//
//            //如果WebView顶部距离其父控件距离未0，即WebView顶部滑动到RecyclerView父控件顶部重合时，
//            // 此时需要拦截滑动事件交给WebView处理。
//            if (shouldIntercept(webView, dy)) {
//                return webView.onTouchEvent(event);
//            }
//            return true;
//        }
//
//        /**
//         * 是否拦截滑动事件，判断的逻辑是：<br/>
//         * 1,如果是向上滑动，并且webview能够向上滑动，则拦截事件；<br/>
//         * 2,如果是向下滑动，并且webview能够向下滑动，则拦截事件。
//         *
//         * @param view 判断能够滑动的view
//         * @param dy   滑动间距
//         * @return true拦截，false不拦截。
//         */
//        private boolean shouldIntercept(View view, int dy) {
//            //canScrollVertically方法的第二个参数direction，传1时返回是否能够向上滑动，传-1时返回能否向下滑动。
//            //dy<0-->向上滑动；dy>0-->向下滑动。
//            boolean scrollUp = dy < 0 && ViewCompat.canScrollVertically(view, 1);
//            boolean scrollDown = dy > 0 && ViewCompat.canScrollVertically(view, -1);
//            return scrollUp || scrollDown || dy == 0;
//        }
//
//        /**
//         * 判断触摸的点是否在View范围内
//         */
//        private boolean isInView(View v, MotionEvent event) {
//            Rect frame = new Rect();
//            v.getHitRect(frame);
//            float eventX = event.getX();
//            float eventY = event.getY();
//            return frame.contains((int) eventX, (int) eventY);
//        }
//    }

    public interface OnMapAreaClickListener {

        void onAreaClick(OLXJAreaEntity areaEntity);

    }
}


