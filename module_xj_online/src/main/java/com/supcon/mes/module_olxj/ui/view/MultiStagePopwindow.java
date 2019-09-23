package com.supcon.mes.module_olxj.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.model.bean.AreaMultiStageEntity;
import com.supcon.mes.middleware.ui.view.CustomMultiStageView;
import com.supcon.mes.module_olxj.R;

/**
 * @author yangfei.cao
 * @ClassName depot
 * @date 2018/8/13
 * ------------- Description -------------
 */
public class MultiStagePopwindow extends PopupWindow implements PopupWindow.OnDismissListener {
    private View conentView;
    private Activity context;
    private final XJCustomMultiStageView xjCustomMultiStageView;
    private AreaMultiStageEntity entity;

    public MultiStagePopwindow(Activity context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        conentView = inflater.inflate(R.layout.multistage_popup_window, null);
        xjCustomMultiStageView = conentView.findViewById(R.id.multiStageView);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
        setOnDismissListener(this::onDismiss);
    }

    @Override
    public void onDismiss() {
        changeWindowAlfa(1f);//pop消失，透明度恢复
    }

    public void setData(AreaMultiStageEntity entity) {
        this.entity = entity;
        xjCustomMultiStageView.setRootEntity(entity);
    }

    public void setOnItemChildViewClickListener(OnItemChildViewClickListener onItemChildViewClickListener) {
        xjCustomMultiStageView.setOnItemChildViewClickListener(onItemChildViewClickListener);
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!isShowing()) {
            if (entity != null) {
                changeWindowAlfa(0.6f);//改变窗口透明度
                this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            } else {
                ToastUtils.show(context, "未获取到部门信息");
            }
        } else {
            this.dismiss();
        }
    }

    /*
       更改屏幕窗口透明度
    */
    public void changeWindowAlfa(float alfa) {
        WindowManager.LayoutParams params = context.getWindow().getAttributes();
        params.alpha = alfa;
        context.getWindow().setAttributes(params);
    }
}

