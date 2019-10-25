package com.supcon.mes.middleware.util;

import android.content.Context;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.mes.middleware.R;

/**
 * Created by wangshizhan on 2018/12/17
 * Email:wangshizhan@supcom.com
 */
public class RequestOptionUtil {

    public static RequestOptions getEamRequestOptions(Context context) {

        return new RequestOptions()
                .placeholder(R.drawable.ic_default_pic3)
                .dontAnimate()
                .override(DisplayUtil.dip2px(110, context), DisplayUtil.dip2px(110, context))
                .centerInside();


    }

    public static RequestOptions getWorkRequestOptions(Context context) {

        return new RequestOptions()
                .placeholder(R.drawable.ic_default_pic3)
                .dontAnimate()
                .override(DisplayUtil.dip2px(80, context), DisplayUtil.dip2px(80, context))
                .centerInside();


    }

    public static RequestOptions getXJHeaderRequestOptions(Context context) {

        return new RequestOptions()
                .placeholder(R.color.bgGray2)
                .dontAnimate()
                .override(DisplayUtil.getScreenHeight(context), DisplayUtil.dip2px(200, context))
                .centerInside();


    }

    public static RequestOptions getNoCacheRequestOptions(){
        return new RequestOptions()
                .placeholder(R.drawable.ic_default_txl_pic)
                .error(R.drawable.ic_default_txl_pic)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过硬盘缓存
                .skipMemoryCache(true);//跳过内存缓存
    }


}
