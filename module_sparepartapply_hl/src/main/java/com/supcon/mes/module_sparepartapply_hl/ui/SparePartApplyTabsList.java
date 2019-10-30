package com.supcon.mes.module_sparepartapply_hl.ui;

import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_sparepartapply_hl.R;
import com.supcon.mes.module_wxgd.model.bean.SparePartApplyEntity;

/**
 * @description SparePartApplyTabsList 备件领用申请页签列表
 * @author zws 2019/9/27
 */
@Deprecated
@Router(Constant.Router.SPARE_PART_APPLY_LIST)
public class SparePartApplyTabsList extends BaseRefreshRecyclerActivity<SparePartApplyEntity> {
    @Override
    protected IListAdapter<SparePartApplyEntity> createAdapter() {
        return null;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_sparepart_apply_list;
    }
}
