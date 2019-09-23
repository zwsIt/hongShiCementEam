package com.supcon.mes.middleware.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.TextView;

import com.supcon.mes.middleware.R;


/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/30
 * ------------- Description -------------
 * 重写梯形
 */
@SuppressLint("AppCompatCustomView")
public class TrapezoidView extends TextView {

    /**
     * 梯形边度
     */
    private static final int LABEL_LENGTH = 50;

    private Paint backgroundPaint;
    private Path pathBackground;

    public TrapezoidView(Context context) {
        super(context);
        init();
    }

    public TrapezoidView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TrapezoidView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void draw(Canvas canvas) {
        //计算路径
        calculatePath(getMeasuredWidth(), getMeasuredHeight());
        canvas.drawPath(pathBackground, backgroundPaint);
        super.draw(canvas);
    }

    private void calculatePath(int measuredWidth, int measuredHeight) {
        pathBackground.reset();
        pathBackground.moveTo(0, 0);
        pathBackground.lineTo(measuredWidth, 0);
        pathBackground.lineTo(measuredWidth - LABEL_LENGTH, measuredHeight);
        pathBackground.lineTo(LABEL_LENGTH, measuredHeight);
        pathBackground.close();
    }

    private void init() {
        pathBackground = new Path();
        backgroundPaint = new Paint();
        backgroundPaint.setColor(getResources().getColor(R.color.bgGray));
        backgroundPaint.setStyle(Paint.Style.FILL);
    }
}
