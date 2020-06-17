package com.supcon.mes.module_main.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.supcon.mes.mbap.view.NoScrollViewPager;

/**
 * @Description:
 * @Author: zhangwenshuai
 * @CreateDate: 2020/6/5 21:13
 */
@Deprecated
public class CustomViewPaper extends NoScrollViewPager {
    public CustomViewPaper(@NonNull Context context) {
        super(context);
    }
    public CustomViewPaper(@NonNull Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if(h > height) height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
