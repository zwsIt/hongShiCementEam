package com.supcon.mes.module_login.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.bumptech.glide.Glide;
import com.supcon.common.view.base.activity.BaseActivity;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.LogUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.GalleryBean;
import com.supcon.mes.mbap.listener.ImageTouchListener;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.ImageDeleteEvent;
import com.supcon.mes.module_login.R;
import com.supcon.mes.mbap.view.CustomImageView;
import com.supcon.mes.mbap.view.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Created by wangshizhan on 2017/12/20.
 * Email:wangshizhan@supcon.com
 */

@Router(Constant.Router.IMAGE_VIEW)
public class ImageViewActivity extends BaseActivity {

    @BindByTag("imageViewContainer")
    NoScrollViewPager imageViewContainer;

    @BindByTag("pageNum")
    TextView pageNum;

    @BindByTag("imageviewBack")
    ImageView imageviewBack;

    @BindByTag("imageviewDelete")
    ImageView imageviewDelete;

    @BindByTag("imageviewTitle")
    TextView imageviewTitle;

    private List<String> mDatas;
    private List<RelativeLayout> mImageViews;
    private int mPosition;
    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;

    private boolean isEditable = false;

//    private Matrix initMatrix = new Matrix(); // 初始状态
    // 縮放控制
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    // 不同状态的表示：
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;

    private PointF oriPoint = new PointF(); // 初始点

    // 定义第一个按下的点，两只接触点的中点，以及初始的两指按下的距离：
    private PointF startPoint = new PointF();
    private PointF midPoint = new PointF();
    private float oriDis = 1f;
    private ImageView mImageView; // 当前展示图片


    @Override
    protected int getLayoutID() {
        return R.layout.ac_imageview;
    }

    @Override
    protected void onInit() {
        super.onInit();

        mDatas = getIntent().getStringArrayListExtra("images");
        mPosition = getIntent().getIntExtra("position", 0);
        mLocationX = getIntent().getIntExtra("locationX", 0);
        mLocationY = getIntent().getIntExtra("locationY", 0);
        mWidth = getIntent().getIntExtra("width", 0);
        mHeight = getIntent().getIntExtra("height", 0);
        isEditable = getIntent().getBooleanExtra("isEditable", false);
        if(mDatas == null ){
            mDatas = new ArrayList<>();
        }

    }

    @Override
    protected void initView() {
        super.initView();

        if(mDatas.size() == 0 ){
            pageNum.setVisibility(View.GONE);
        }
        else{
            pageNum.setText(mPosition + 1 + "/" + mDatas.size());
        }

        mImageViews = new ArrayList<>();
        for (String string : mDatas){

            RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.ly_imageview, null);

            CustomImageView imageViewContainer = relativeLayout.findViewById(R.id.img);
            imageViewContainer.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
            imageViewContainer.transformIn();
            imageViewContainer.setOnClickListener(v -> onBackPressed());
            imageViewContainer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    zoomImage(v,event);
                    return true; //拦截
                }
            });

            if(string!=null && string.contains(".mp4")) {
                ImageView playImageView = relativeLayout.findViewById(R.id.playIv);
                playImageView.setVisibility(View.VISIBLE);
                playImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String localPath = string.replace("thumbnail", "").replace(".jpg",".mp4");

                        Uri uri = Uri.parse(localPath);//调用系统自带的播放器

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        LogUtil.d(uri.toString());
                        intent.setDataAndType(uri, "video/mp4");
                        startActivity(intent);
                    }
                });
            }

//            imageViewContainer.setFocusableInTouchMode(true);
//            imageViewContainer.setFocusable(true);
//            imageViewContainer.setScaleType(ImageView.ScaleType.MATRIX);
//            initImageView(imageViewContainer);

            Glide.with(this).load(string).into(imageViewContainer);

            mImageViews.add(relativeLayout);
        }

        imageViewContainer.setOffscreenPageLimit(mDatas.size()-1);
        imageViewContainer.setAdapter( new ImageAdapter(mDatas));
        imageViewContainer.setCurrentItem(mPosition);

        if(isEditable){
            imageviewDelete.setVisibility(View.VISIBLE);
        }

//        Disposable disposable = Flowable.fromIterable(mImageViews)
//                .delay(200, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<CustomImageView>() {
//                    @Override
//                    public void accept(CustomImageView customImageView) throws Exception {
//                        initImageView(customImageView);
//                    }
//                });
    }

    private void zoomImage(View v, MotionEvent event) {
        mImageView = (ImageView) v;
        mImageView.setScaleType(ImageView.ScaleType.MATRIX); // 支持缩放、移动
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // 单指
            case MotionEvent.ACTION_DOWN:
                matrix.set(mImageView.getImageMatrix());
                savedMatrix.set(matrix);
                startPoint.set(event.getX(), event.getY());
                if (oriPoint.x == 0 && oriPoint.y == 0){
                    oriPoint.set(startPoint);
                }
                mode = DRAG;
                break;
            // 双指
            case MotionEvent.ACTION_POINTER_DOWN:
                oriDis = distance(event);
                if (oriDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                    savedMatrix.set(matrix);
                    midPoint = middle(event);
                    mode = ZOOM;
                }
                break;
            // 手指放开
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            // 单指滑动事件
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    // 是一个手指拖动
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);
                } else if (mode == ZOOM) {
                    // 两个手指滑动
                    float newDist = distance(event);
                    if (newDist > 10f) { // 两个手指并拢在一起的时候像素大于10
                        matrix.set(savedMatrix);
                        float scale = newDist / oriDis/* < 1.0f ? 1.0f : newDist / oriDis*/;
                        matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                    }
                }
                break;
        }
//         设置ImageView的Matrix
        mImageView.setImageMatrix(matrix);
    }

    // 计算两个触摸点之间的距离
    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return Float.valueOf(String.valueOf(Math.sqrt(x * x + y * y))) ;
    }

    // 计算两个触摸点的中点
    private PointF middle(MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        return new PointF(x / 2, y / 2);
    }

    public static float ScreenW;//屏幕的宽
    public static float ScreenH;//屏幕的高
    private void initImageView(ImageView imageView) {
        //获取屏幕高宽度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ScreenW = displayMetrics.widthPixels;
        ScreenH= displayMetrics.heightPixels;
        //把图片转变为bitmap 然后获取图片的高宽度
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
//        Bitmap bitmap = bitmapDrawable.getBitmap();
        /**
         * float withs=bitmap.getWidth()
         * float withs=bitmap.getHeight()
         * */
        //1这里是先声明一个矩阵来容纳图片的矩阵
        Matrix MatrixImags = imageView.getImageMatrix();
        //2把矩阵移动 （ postTranslate）         这里就用到了我们获取的屏幕高宽
        //屏幕高，宽度 /2- 图片高，宽/2
//        MatrixImags.postTranslate(((ScreenW/2)-(bitmap.getWidth()/2)), ((ScreenH/2)-(bitmap.getHeight()/2)));
//        MatrixImags.postTranslate(ScreenW, ScreenH);
        //在把变化后的矩阵给设置进去
        imageView.setImageMatrix(MatrixImags);
        //调用图片触摸事件
        imageView.setOnTouchListener(new ImageTouchListener(imageView));


    }


    @Override
    protected void initListener() {
        super.initListener();

        imageviewBack.setOnClickListener(v -> onBackPressed());

        imageviewDelete.setOnClickListener(v -> {

            new CustomDialog(context)
                    .imageViewActivityAlertDialog("是否删除?")
                    .bindView(R.id.grayBtn, "取消")
                    .bindView(R.id.redBtn, "确定")
                    .bindClickListener(R.id.grayBtn, null, true)
                    .bindClickListener(R.id.redBtn, v3 -> {

                        EventBus.getDefault().post(new ImageDeleteEvent(mDatas.get(mPosition), mPosition));

                        mDatas.remove(mPosition);
                        mImageViews.remove(mPosition);

                        if(mPosition >= 1){
                            mPosition --;
                        }
                        else if(mDatas.size()!=0){
                            mPosition = 0;
                        }
                        else{
                            finish();
                        }
                        pageNum.setText(String.valueOf(mPosition+1+"/"+mDatas.size()));
                        imageViewContainer.setAdapter(new ImageAdapter(mDatas));
                        imageViewContainer.setCurrentItem(mPosition);


                    }, true)
                    .show();

        });

        imageViewContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                pageNum.setText(String.valueOf(mPosition+1+"/"+mDatas.size()));

                // 还原上一张图片状态
                if (oriPoint.x != startPoint.x && oriPoint.y != startPoint.y){
                    RelativeLayout relativeLayout = mImageViews.get(position);
                    CustomImageView imageViewContainer = relativeLayout.findViewById(R.id.img);
                    mImageView.setImageMatrix(imageViewContainer.getImageMatrix());
                }
                oriPoint.x = 0;oriPoint.y = 0;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        mImageView = null;
        finish();
//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private class ImageAdapter extends PagerAdapter {


        List<String> datas;

        public ImageAdapter(List<String> datas){
            this.datas = datas;
        }


        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(mImageViews.get(position));
            return mImageViews.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            container.removeViewAt(position);
//            mImageViews.remove(position);
        }
    }
}
