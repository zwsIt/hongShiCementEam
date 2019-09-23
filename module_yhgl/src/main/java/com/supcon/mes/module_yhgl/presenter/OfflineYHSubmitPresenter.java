package com.supcon.mes.module_yhgl.presenter;

import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.YHEntityVo;
import com.supcon.mes.module_yhgl.model.contract.OfflineYHSubmitContract;



/**
 * Created by xushiyun on 2018/8/22
 * Email:ciruy.victory@gmail.com
 * @author xushiyun
 */
public class OfflineYHSubmitPresenter extends OfflineYHSubmitContract.Presenter {

    /**
     * 进行dto数据的更新操作
     * @param yhEntityDto 需要更新的dto数据
     */
    @Override
    public void doSubmit(YHEntityVo yhEntityDto) {
        EamApplication.dao().getYHEntityVoDao().insertOrReplace(yhEntityDto);
        getView().doSubmitSuccess(null);
    }

    /**
     * 删除数据信息
     * @param yhEntityDto 需要删除的数据信息
     */
    @Override
    public void doDelete(YHEntityVo yhEntityDto) {
        EamApplication.dao().getYHEntityVoDao().delete(yhEntityDto);
        getView().doDeleteSuccess();
    }
}
