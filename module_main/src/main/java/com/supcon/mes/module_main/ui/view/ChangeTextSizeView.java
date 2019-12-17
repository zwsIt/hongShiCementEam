package com.supcon.mes.module_main.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.supcon.common.view.util.DisplayUtil;
import com.supcon.mes.module_main.R;

import java.util.ArrayList;
import java.util.List;

/**
 * lineWidth  线条的粗细
 * lineColor   线条的颜色
 * totalCount   线条格数
 * circleColor   圆球的颜色
 * circleRadius    圆球的半径
 * textFontColor  字体的颜色
 * smallSize  小“A” 字体大小
 * standerSize  “标准” 字体大小
 * bigSize    大“A” 字体大小
 * defaultPosition  默认位置
 * <p>
 * <p>
 * 实现思路：
 * 1.首先初始化对应的自定义属性及其画笔。
 * 2.绘制ui
 * 3.实现拖动改变事件
 * 4.重写onSizeChange方法，改变对应的变量
 * 5.优化
 */
public class ChangeTextSizeView extends View {
    /**
     * 默认线条的颜色
     */
    private int defaultLineColor = Color.rgb(33, 33, 33);
    /**
     * 默认线条的宽度
     */
    private int defaultLineWidth;

    private int defaultMax = 5;
    /**
     * 默认圆球的大小
     */
    private int defaultCircleColor = Color.WHITE;
    /**
     * 默认圆球的半径
     */
    private int defaultCircleRadius;

    /**
     * 线条粗细
     */

    private int lineWidth;

    // 当前所在位置
    private int currentProgress;

    // 默认位置
    private int defaultPosition = 1;

    // 一共有多少格
    private int max = 4;
    // 线条颜色
    private int lineColor = Color.BLACK;

    //字体颜色
    private int textColor = Color.BLACK;
    //字体大小
    private int smallSize = 14;
    private int standerSize = 16;
    private int bigSize = 28;
    // 控件的宽高
    private int height;
    private int width;
    // 一段的宽度，根据总宽度和总格数计算得来
    private int itemWidth;


    // 圆半径
    private int circleRadius;
    private int circleColor = Color.WHITE;
    // 画笔
    private Paint mLinePaint;
    private Paint mTextPaint;
    private Paint mText1Paint;
    private Paint mText2Paint;
    private Paint mCirclePaint;

    private float circleY;
    private float textScaleX;
    private float text1ScaleX;
    private float text2ScaleX;

    // 滑动过程中x坐标
    private float currentX = 0;
    // 有效数据点
    private List<Point> points = new ArrayList<>();

    public ChangeTextSizeView(Context context) {
        super(context);
    }

    public ChangeTextSizeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 初始化 要绘制的画笔，及其自定义属性
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        defaultLineWidth = DisplayUtil.dip2px(2, context);
        defaultCircleRadius = DisplayUtil.dip2px(35, context);
        lineWidth = DisplayUtil.dip2px(1, context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChangeTextSizeView);
        int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();

        //初始化线条画笔
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(lineColor);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinePaint.setStrokeWidth(lineWidth);

        //文字画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(textColor);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setTextSize(DisplayUtil.sp2px(smallSize, context));
        textScaleX = mTextPaint.measureText("A");

        //文字画笔
        mText1Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mText1Paint.setColor(textColor);
        mText1Paint.setStyle(Paint.Style.FILL_AND_STROKE);
        mText1Paint.setTextSize(DisplayUtil.sp2px(bigSize, context));
        text1ScaleX = mText1Paint.measureText("A");

        //文字画笔
        mText2Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mText2Paint.setColor(textColor);
        mText2Paint.setStyle(Paint.Style.FILL_AND_STROKE);
        mText2Paint.setTextSize(DisplayUtil.sp2px(standerSize, context));
        text2ScaleX = mText2Paint.measureText("标准");

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(circleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);

        // 设置阴影效果
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mCirclePaint.setShadowLayer(2, 0, 0, Color.rgb(33, 33, 33));

    }

    /**
     * 获取自定义属性并且附上默认值
     *
     * @param index
     * @param typedArray
     */
    private void initCustomAttr(int index, TypedArray typedArray) {
        if (index == R.styleable.ChangeTextSizeView_lineColor) {
            lineColor = typedArray.getColor(index, defaultLineColor);
        } else if (index == R.styleable.ChangeTextSizeView_circleColor) {
            circleColor = typedArray.getColor(index, defaultCircleColor);
        } else if (index == R.styleable.ChangeTextSizeView_lineWidth) {
            lineWidth = typedArray.getDimensionPixelSize(index, defaultLineWidth);
        } else if (index == R.styleable.ChangeTextSizeView_circleRadius) {
            circleRadius = typedArray.getDimensionPixelSize(index, defaultCircleRadius);
        } else if (index == R.styleable.ChangeTextSizeView_totalCount) {
            max = typedArray.getInteger(index, defaultMax);
        } else if (index == R.styleable.ChangeTextSizeView_textFontColor) {
            textColor = typedArray.getColor(index, textColor);
        } else if (index == R.styleable.ChangeTextSizeView_smallSize) {
            smallSize = typedArray.getInteger(index, smallSize);
        } else if (index == R.styleable.ChangeTextSizeView_standerSize) {
            standerSize = typedArray.getInteger(index, standerSize);
        } else if (index == R.styleable.ChangeTextSizeView_bigSize) {
            bigSize = typedArray.getInteger(index, bigSize);
        } else if (index == R.styleable.ChangeTextSizeView_defaultPosition) {
            defaultPosition = typedArray.getInteger(index, defaultPosition);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画字
        canvas.drawText("A", points.get(0).x - textScaleX / 2, height / 2 - 50, mTextPaint);

        //画字
        canvas.drawText("标准", points.get(1).x - text2ScaleX / 2, height / 2 - 50, mText2Paint);

        //画字
        canvas.drawText("A", points.get(points.size() - 1).x - text1ScaleX / 2, height / 2 - 50, mText1Paint);

        // 先画中间的横线
        canvas.drawLine(points.get(0).x, height / 2, points.get(points.size() - 1).x, height / 2, mLinePaint);
        // 绘制刻度
        for (Point point : points) {
            canvas.drawLine(point.x + 1, height / 2 - 20, point.x + 1, height / 2 + 20, mLinePaint);
        }

        // 画圆
        if (currentX < circleRadius) {
            currentX = circleRadius;
        }
        if (currentX > width - circleRadius) {
            currentX = width - circleRadius;
        }

        // 实体圆
        canvas.drawCircle(currentX + 1, circleY, circleRadius, mCirclePaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        width = w;
        circleY = height / 2;
        // 横线宽度是总宽度-2个圆的半径
        itemWidth = (w - 2 * circleRadius) / max;
        // 把可点击点保存起来
        for (int i = 0; i <= max; i++) {
            points.add(new Point(circleRadius + i * itemWidth, height / 2));
        }
        //初始刻度
        currentX = points.get(defaultPosition).x;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        currentX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //回到最近的一个刻度点
                Point targetPoint = getNearestPoint(currentX);
                if (targetPoint != null) {
                    // 最终
                    currentX = points.get(currentProgress).x;
                    invalidate();
                }

                if (onChangeCallbackListener != null) {
                    onChangeCallbackListener.onChangeListener(currentProgress);
                }
                break;
        }
        return true;
    }


    /**
     * 获取最近的刻度
     */
    private Point getNearestPoint(float x) {
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (Math.abs(point.x - x) < itemWidth / 2) {
                currentProgress = i;
                return point;
            }
        }
        return null;
    }

    public void setChangeCallbackListener(OnChangeCallbackListener listener) {
        this.onChangeCallbackListener = listener;
    }

    private OnChangeCallbackListener onChangeCallbackListener;

    public interface OnChangeCallbackListener {
        void onChangeListener(int position);
    }

    public void setDefaultPosition(int position) {
        defaultPosition = position;
        if (onChangeCallbackListener != null) {
            onChangeCallbackListener.onChangeListener(defaultPosition);
        }
        invalidate();
    }
}
