package com.supcon.mes.middleware.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Xushiyun on 2018/8/8.
 * Email:ciruy_victory@gmail.com
 */
public class ChangeColorTextView extends android.support.v7.widget.AppCompatTextView {
    private boolean clicked = false;
    public ChangeColorTextView(Context context) {
        super(context,null);
    }

    public ChangeColorTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
    }


    public ChangeColorTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initClickListener();
    }

    private void initClickListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
