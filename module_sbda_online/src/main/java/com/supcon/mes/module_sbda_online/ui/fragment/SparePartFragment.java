package com.supcon.mes.module_sbda_online.ui.fragment;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.api.UpdateSupOSStandingCropAPI;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.StandingCropEntity;
import com.supcon.mes.middleware.model.contract.UpdateSupOSStandingCropContract;
import com.supcon.mes.middleware.presenter.UpdateSupOSStandingCropPresenter;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_sbda_online.R;
import com.supcon.mes.module_sbda_online.model.api.SpareAPI;
import com.supcon.mes.module_sbda_online.model.bean.SparePartEntity;
import com.supcon.mes.module_sbda_online.model.bean.SparePartListEntity;
import com.supcon.mes.module_sbda_online.model.contract.SpareContract;
import com.supcon.mes.module_sbda_online.presenter.SparePresenter;
import com.supcon.mes.module_sbda_online.ui.adapter.SparePartAdapter;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/3/29
 * ------------- Description -------------
 * 备件
 */
@Presenter(value = {SparePresenter.class, UpdateSupOSStandingCropPresenter.class})
public class SparePartFragment extends BaseRefreshRecyclerFragment<SparePartEntity> implements SpareContract.View, UpdateSupOSStandingCropContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    private static Long eamId;
    private SparePartAdapter sparePartAdapter;
    private List<SparePartEntity> sparePartEntities;

    public static SparePartFragment newInstance(Long id) {
        eamId = id;
        SparePartFragment fragment = new SparePartFragment();
        return fragment;
    }

    @Override
    protected void initView() {
        super.initView();
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, null));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(15));

    }

    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshListener(() -> {
            presenterRouter.create(SpareAPI.class).spareRecord(eamId);
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_archives_list;
    }

    @Override
    protected IListAdapter createAdapter() {
        sparePartAdapter = new SparePartAdapter(getActivity());
        return sparePartAdapter;
    }

    @SuppressLint("CheckResult")
    @Override
    public void spareRecordSuccess(SparePartListEntity entity) {
        if (entity.result != null && entity.result.size() > 0) {
            sparePartEntities = entity.result;
            if (EamApplication.isHongshi()) {
                StringBuffer sparePartCodes = new StringBuffer();
                Flowable.fromIterable(entity.result)
                        .subscribe(sparePartEntity -> sparePartCodes.append(sparePartEntity.getProductID().productCode).append(","), throwable -> {
                        }, () -> {
                            presenterRouter.create(UpdateSupOSStandingCropAPI.class).updateStandingCrop(sparePartCodes.toString());
                        });
            } else {
                refreshListController.refreshComplete(sparePartEntities);
            }
        } else {
            refreshListController.refreshComplete(null);
        }
    }

    @Override
    public void spareRecordFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
    }

    @SuppressLint("CheckResult")
    @Override
    public void updateStandingCropSuccess(CommonListEntity entity) {
        if (entity.result != null && entity.result.size() > 0) {
            Flowable.fromIterable(((List<StandingCropEntity>) entity.result))
                    .flatMap((Function<StandingCropEntity, Flowable<SparePartEntity>>) standingCropEntity -> {
                        Flowable<SparePartEntity> sparePartEntityFlowable = Flowable.fromIterable(sparePartEntities)
                                .map(sparePartEntity -> {
                                    if (standingCropEntity.productCode.equals(sparePartEntity.getProductID().productCode)) {
                                        sparePartEntity.standingCrop = Float.valueOf(standingCropEntity.useQuantity);
                                    }
                                    return sparePartEntity;
                                });
                        return sparePartEntityFlowable;
                    })
                    .subscribe(sparePartEntity -> {
                    }, throwable -> {
                    }, () -> refreshListController.refreshComplete(sparePartEntities));
        } else {
            refreshListController.refreshComplete(sparePartEntities);
        }
    }

    @Override
    public void updateStandingCropFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
    }
}
