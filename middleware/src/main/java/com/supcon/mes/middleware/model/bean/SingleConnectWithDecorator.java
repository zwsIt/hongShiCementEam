package com.supcon.mes.middleware.model.bean;

import com.supcon.mes.mbap.view.CustomGalleryView;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/10/2513:19
 * 和Decorator有着单向连接的接口
 */
public interface SingleConnectWithDecorator {
    CustomGalleryView customGalleryView();
    String startName();
}
