package com.supcon.mes.module_main.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * @Description:
 * @Author: zhangwenshuai
 * @CreateDate: 2020/6/5 20:50
 */
@Deprecated
public class VerticalScrollView extends ScrollView {
    private GestureDetector mGestureDetector;

    public VerticalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev)
                && mGestureDetector.onTouchEvent(ev);
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            // 如果我们滚动更接近水平方向,返回false,让子视图来处理它
            return (Math.abs(distanceY) > Math.abs(distanceX));
        }
    }
//    private float xDistance, yDistance, xLast, yLast;
//
//    public VerticalScrollView(Context context) {
//        super(context);
//    }
//
//    public VerticalScrollView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public VerticalScrollView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                xDistance = yDistance = 0f;
//                xLast = ev.getX();
//                yLast = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                final float curX = ev.getX();
//                final float curY = ev.getY();
//
//                xDistance += Math.abs(curX - xLast);
//                yDistance += Math.abs(curY - yLast);
//                xLast = curX;
//                yLast = curY;
//
//                if (xDistance > yDistance) {
//                    return false;
//                }
//        }
//        return super.onInterceptTouchEvent(ev);
//    }

}
