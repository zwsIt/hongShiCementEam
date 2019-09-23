package com.supcon.mes.middleware.model.inter;

import com.supcon.common.view.base.controller.BaseViewController;

/**
 * @Author xushiyun
 * @Create-time 8/15/19
 * @Pageage com.supcon.mes.middleware.model.inter
 * @Project eam
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public interface IViewControllerProvider<T extends BaseViewController> {
    T viewController();
}
