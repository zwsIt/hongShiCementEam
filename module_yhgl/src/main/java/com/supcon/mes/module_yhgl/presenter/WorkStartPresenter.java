package com.supcon.mes.module_yhgl.presenter;

import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_yhgl.model.contract.WorkStartContract;
import com.supcon.mes.module_yhgl.model.network.YHGLHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @Description:
 * @Author: zhangwenshuai
 * @CreateDate: 2020/5/23 11:03
 */
public class WorkStartPresenter extends WorkStartContract.Presenter {
    @Override
    public void workStartSubmit(String workStartDTO) {
        mCompositeSubscription.add(
                YHGLHttpClient.workInitiation(workStartDTO)
                .onErrorReturn(new Function<Throwable, CommonEntity>() {
                    @Override
                    public CommonEntity apply(Throwable throwable) throws Exception {
                        CommonEntity commonEntity = new CommonEntity();
                        commonEntity.errMsg = HttpErrorReturnUtil.getErrorInfo(throwable);
                        return commonEntity;
                    }
                })
                .subscribe(new Consumer<CommonEntity>() {
                    @Override
                    public void accept(CommonEntity commonEntity) throws Exception {
                        if (commonEntity.success){
                            getView().workStartSubmitSuccess(commonEntity);
                        }else {
                            getView().workStartSubmitFailed(commonEntity.errMsg);
                        }
                    }
                })
        );
    }
}
