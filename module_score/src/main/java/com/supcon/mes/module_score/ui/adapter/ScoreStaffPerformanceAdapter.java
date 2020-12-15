package com.supcon.mes.module_score.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.LogUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.listener.OnTextListener;
import com.supcon.mes.mbap.network.Api;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomNumView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.AttachmentDownloadController;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.util.FaultPicHelper;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_score.R;
import com.supcon.mes.module_score.constant.ScoreConstant;
import com.supcon.mes.module_score.controller.ScoreCameraController;
import com.supcon.mes.module_score.model.bean.ScoreDutyEamEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffPerformanceEntity;
import com.supcon.mes.module_score.model.network.ScoreService;
import com.supcon.mes.module_score.ui.ScoreInspectorStaffPerformanceActivity;
import com.supcon.mes.module_score.ui.ScoreMechanicStaffPerformanceActivity;
import com.supcon.mes.module_score.ui.ScoreStaffPerformanceActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

@Deprecated
public class ScoreStaffPerformanceAdapter extends BaseListDataRecyclerViewAdapter<ScoreStaffPerformanceEntity> {

    private boolean isEdit = false;
    private float total;

    private ScoreCameraController mScoreCameraController;

    public ScoreStaffPerformanceAdapter(Context context) {
        super(context);
    }

    public void setEditable(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public void updateTotal(float total) {
        this.total = total;
    }

    @Override
    protected BaseRecyclerViewHolder<ScoreStaffPerformanceEntity> getViewHolder(int viewType) {
        if (viewType == ListType.TITLE.value()) {
            return new TitleViewHolder(context);
        }
        return new ViewHolder(context);
    }

    @Override
    public int getItemViewType(int position, ScoreStaffPerformanceEntity scoreEamPerformanceEntity) {
        return scoreEamPerformanceEntity.viewType;
    }

    class TitleViewHolder extends BaseRecyclerViewHolder<ScoreStaffPerformanceEntity> {

        @BindByTag("contentTitle")
        TextView contentTitle;
        @BindByTag("fraction")
        TextView fraction;

        TitleViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_score_performance_title;
        }

        @Override
        protected void initView() {
            super.initView();
        }

        @Override
        protected void initListener() {
            super.initListener();

        }

        @SuppressLint({"StringFormatMatches", "SetTextI18n"})
        @Override
        protected void update(ScoreStaffPerformanceEntity data) {
            contentTitle.setText(data.project);
            fraction.setText(Util.big0(data.fraction) + "分");
        }
    }

    class ViewHolder extends BaseRecyclerViewHolder<ScoreStaffPerformanceEntity> {
        private AttachmentDownloadController mAttachmentDownloadController;
        @BindByTag("itemIndex")
        TextView itemIndex;
        @BindByTag("scoreItem")
        TextView scoreItem;

        @BindByTag("scoreRadioGroup")
        RadioGroup scoreRadioGroup;
        @BindByTag("scoreRadioBtn1")
        RadioButton scoreRadioBtn1;
        @BindByTag("scoreRadioBtn2")
        RadioButton scoreRadioBtn2;
        @BindByTag("sum")
        CustomNumView sum;
        @BindByTag("handleScore")
        CustomEditText handleScore;
        @BindByTag("itemPics")
        CustomGalleryView itemPics;
        @BindByTag("ufItemPhotoIv")
        ImageView ufItemPhotoIv;


        public ViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_score_performance_content;
        }

        @Override
        protected void initView() {
            super.initView();
//            itemPics = itemView.findViewById(R.id.itemPics);
            sum.setEnabled(isEdit);
            sum.getNumViewInput().setEnabled(false);
            scoreRadioBtn1.setEnabled(isEdit);
            scoreRadioBtn2.setEnabled(isEdit);
            handleScore.setEditable(isEdit);
            handleScore.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            if (!isEdit) {
                itemPics.setEditable(false);
            }

            if (mScoreCameraController == null) {
                if (context instanceof ScoreInspectorStaffPerformanceActivity) {
                    mScoreCameraController = ((ScoreInspectorStaffPerformanceActivity) context).getController(ScoreCameraController.class);
                }
                if (context instanceof ScoreMechanicStaffPerformanceActivity) {
                    mScoreCameraController = ((ScoreMechanicStaffPerformanceActivity) context).getController(ScoreCameraController.class);
                }
                if (context instanceof ScoreStaffPerformanceActivity) {
                    mScoreCameraController = ((ScoreStaffPerformanceActivity) context).getController(ScoreCameraController.class);
                }
                mScoreCameraController.init(Constant.IMAGE_SAVE_SCORE_PATH, Constant.PicType.SCORE_PIC);
            }
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            scoreRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (scoreRadioBtn1.isPressed() || scoreRadioBtn2.isPressed()) {
                    ScoreStaffPerformanceEntity item = getItem(getAdapterPosition());
                    item.result = !item.result;
                    float oldTotal = total - item.scoreEamPerformanceEntity.fraction;
                    if (!item.result) {
                        item.scoreEamPerformanceEntity.setTotalHightScore(item.scoreEamPerformanceEntity.getTotalHightScore() + item.itemScore);
                        if (item.scoreEamPerformanceEntity.getTotalHightScore() >= 0) {
                            item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.getTotalHightScore();
                        }
                    } else {
                        item.scoreEamPerformanceEntity.setTotalHightScore(item.scoreEamPerformanceEntity.getTotalHightScore() - item.itemScore);
                        item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.fraction - item.itemScore;
                    }
                    if (item.scoreEamPerformanceEntity.fraction < 0) {
                        item.scoreEamPerformanceEntity.fraction = 0;
                    } else if (item.scoreEamPerformanceEntity.fraction > item.scoreEamPerformanceEntity.score) {
                        item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.score;
                    }

                    notifyItemChanged(getAdapterPosition()); // 是否显示拍照
//                        if (item.result) {
//                            ufItemPhotoIv.setVisibility(View.VISIBLE); // 是否显示拍照
//                        }else {
//                            ufItemPhotoIv.setVisibility(View.GONE);
//                        }

                    List<ScoreStaffPerformanceEntity> list = getList();
                    int position = list.indexOf(item.scoreEamPerformanceEntity);
                    notifyItemChanged(position);

                    //更新总分数
                    item.scoreEamPerformanceEntity.scoreNum = oldTotal + item.scoreEamPerformanceEntity.fraction;
                    onItemChildViewClick(scoreRadioGroup, 0, item.scoreEamPerformanceEntity);
                }
            });
            sum.setTextListener(text -> {
                ScoreStaffPerformanceEntity item = getItem(getAdapterPosition());
                if (Util.strToInt(text) == item.defaultNumVal) {
                    return;
                }
                float oldTotal = total - item.scoreEamPerformanceEntity.fraction;
                float score = item.itemScore * (Util.strToInt(text) - item.defaultNumVal);
                item.scoreEamPerformanceEntity.setTotalHightScore(item.scoreEamPerformanceEntity.getTotalHightScore() - score);
                if (item.scoreEamPerformanceEntity.getTotalHightScore() >= 0) {
                    item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.getTotalHightScore();
                } else {
                    item.scoreEamPerformanceEntity.fraction = 0;
                }

                if (item.scoreEamPerformanceEntity.fraction > item.scoreEamPerformanceEntity.score) {
                    item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.score;
                }

                item.defaultNumVal = Util.strToInt(text);
                List<ScoreStaffPerformanceEntity> list = getList();
                int position = list.indexOf(item.scoreEamPerformanceEntity);
                notifyItemChanged(position);
                notifyItemChanged(getAdapterPosition()); // 是否显示拍照
//                    if (item.defaultNumVal <= 0) {
//                        ufItemPhotoIv.setVisibility(View.GONE); // 是否显示拍照
//                    }else {
//                        ufItemPhotoIv.setVisibility(View.VISIBLE);
//                    }
                //更新总分数
                item.scoreEamPerformanceEntity.scoreNum = oldTotal + item.scoreEamPerformanceEntity.fraction;
                onItemChildViewClick(scoreRadioGroup, 0, item.scoreEamPerformanceEntity);
            });
            RxTextView.textChanges(handleScore.editText())
                    .skipInitialValue()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn( Schedulers.io())
                    .filter(charSequence -> {
//                        if (!isEdit)return false;
                        handleScore.editText().setSelection(charSequence.length());
                        ScoreStaffPerformanceEntity item = getItem(getAdapterPosition());
                        if (item == null || charSequence.toString().equals(item.resultValue))return false;
                        if (Util.strToFloat(charSequence.toString()) > item.itemScore) {
                            ToastUtils.show(context, "当前项目最高" + item.itemScore + "分");
                            handleScore.setContent(charSequence.subSequence(0, charSequence.length() - 1).toString());
//                            handleScore.editText().setSelection(charSequence.length()-1);
                            return false;
                        }
                        return true;
                    })
                    .subscribe(charSequence -> {
                        ScoreStaffPerformanceEntity item = getItem(getAdapterPosition());
                        item.resultValue = charSequence.toString();
                        int position = getList().indexOf(item.scoreEamPerformanceEntity); // 标题位置
                        float otherTotal = total - item.scoreEamPerformanceEntity.fraction; // 除当前类别的总分数

                        // 标题更新分数
                        item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.fraction + item.lastSubScore - Util.strToFloat(charSequence.toString());
//                        item.lastSubScore = Util.strToFloat(charSequence.toString()); // 记录上次扣分数
                        notifyItemChanged(position);
                        notifyItemChanged(getAdapterPosition());
//                        if (TextUtils.isEmpty(charSequence)) {
//                            ufItemPhotoIv.setVisibility(View.GONE); // 是否显示拍照
//                        }else {
//                            ufItemPhotoIv.setVisibility(View.VISIBLE);
//                        }
                        //更新总分数
                        item.scoreEamPerformanceEntity.scoreNum = otherTotal + item.scoreEamPerformanceEntity.fraction;
                        onItemChildViewClick(handleScore, position, item.scoreEamPerformanceEntity);
                    });
            RxView.clicks(ufItemPhotoIv).throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        mScoreCameraController.setCurrAdapterPosition(getAdapterPosition(), itemPics);
                        itemPics.findViewById(R.id.customCameraIv).performClick();  //调用CustomGalleryView的拍照按钮
//                        mScoreCameraController.showCustomDialog();
                    });
        }

        @SuppressLint({"StringFormatMatches", "SetTextI18n", "CheckResult"})
        @Override
        protected void update(ScoreStaffPerformanceEntity data) {
            mScoreCameraController.addGalleryView(getAdapterPosition(), itemPics, ScoreStaffPerformanceAdapter.this);

            if (data.getAttachFileMultiFileIds() == null && data.getAttachFileFileAddPaths() == null){ // 服务器及本地均未有附件
                itemPics.clear();
            }else {
                initAttachFiles(data, mAttachmentDownloadController, itemPics);
            }

            itemIndex.setText(data.index + ".");
            Spanned item = HtmlParser.buildSpannedText(String.format(context.getString(R.string.device_style12), data.item, Util.big0(data.itemScore == 0 ? data.score : data.itemScore)), new HtmlTagHandler());
            scoreItem.setText(item);

            sum.setNum(data.defaultNumVal);
            data.lastSubScore = Util.strToFloat(data.resultValue); // 赋值上一次扣分数
            handleScore.setContent(data.resultValue);

            if (ScoreConstant.ValueType.T1.equals(data.defaultValueType)) {
                sum.setVisibility(View.VISIBLE);
                scoreRadioGroup.setVisibility(View.GONE);
                handleScore.setVisibility(View.GONE);
            } else if (ScoreConstant.ValueType.T2.equals(data.defaultValueType)) {
                scoreRadioGroup.setVisibility(View.VISIBLE);
                sum.setVisibility(View.GONE);
                handleScore.setVisibility(View.GONE);
            } else if (ScoreConstant.ValueType.T3.equals(data.defaultValueType)) {
                handleScore.setVisibility(View.VISIBLE);
                scoreRadioGroup.setVisibility(View.GONE);
                sum.setVisibility(View.GONE);
            }else {
                handleScore.setVisibility(View.GONE);
                scoreRadioGroup.setVisibility(View.GONE);
                sum.setVisibility(View.GONE);
            }
            scoreRadioBtn1.setText(data.isItemValue);
            scoreRadioBtn2.setText(data.noItemValue);
            scoreRadioBtn1.setChecked(data.result);
            scoreRadioBtn2.setChecked(!data.result);
            if (!isEdit) {
                if (data.result){
                    scoreRadioBtn1.setButtonDrawable(R.drawable.ic_check_box_true_small_gray);
                    scoreRadioBtn2.setButtonDrawable(R.drawable.ic_check_box_false_small);
                }else {
                    scoreRadioBtn1.setButtonDrawable(R.drawable.ic_check_box_false_small);
                    scoreRadioBtn2.setButtonDrawable(R.drawable.ic_check_box_true_small_gray);
                }
            }
            if (isEdit && (data.result || data.defaultNumVal > 0 || !TextUtils.isEmpty(data.resultValue))) {
                ufItemPhotoIv.setVisibility(View.VISIBLE);
            } else {
                ufItemPhotoIv.setVisibility(View.GONE);
            }

        }

        @SuppressLint("CheckResult")
        private void autoCalculationScore(ScoreStaffPerformanceEntity data) {
            Api.getInstance().retrofit.create(ScoreService.class).getDutyEamNew(((ScoreInspectorStaffPerformanceActivity)context).getScoreStaffEntity().patrolWorker.id, ScoreConstant.ScoreType.INSPECTION_STAFF)
                    .subscribeOn(Schedulers.newThread())
                    .onErrorReturn(throwable -> {
                        ScoreDutyEamEntity scoreDutyEamEntity = new ScoreDutyEamEntity();
                        scoreDutyEamEntity.errMsg = throwable.toString();
                        return scoreDutyEamEntity;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(scoreDutyEamEntity -> {
                        if (scoreDutyEamEntity.success){
                            if (data.viewType == 1 && "专业巡检".equals(data.project)){
                                data.scoreEamPerformanceEntity.fraction = scoreDutyEamEntity.professInspScore;
                            }else {
                                data.scoreEamPerformanceEntity.fraction = scoreDutyEamEntity.avgScore;
                            }
                            data.scoreEamPerformanceEntity.scoreNum = total-data.scoreEamPerformanceEntity.fraction;
                            notifyItemChanged(getList().indexOf(data.scoreEamPerformanceEntity));
                            ((ScoreInspectorStaffPerformanceActivity)context).updateTotalScore(data.scoreEamPerformanceEntity);
//                            onItemChildViewClick(itemView,0,data);
//                            updateTotal(total-data.scoreEamPerformanceEntity.fraction);
                        }else {
                            LogUtils.error(scoreDutyEamEntity.errMsg);
                        }
                    });
        }

    }


    private void initAttachFiles(ScoreStaffPerformanceEntity data, AttachmentDownloadController downloadController, CustomGalleryView galleryView) {
        List<AttachmentEntity> attachmentEntities;
        if (downloadController == null) {
            downloadController = new AttachmentDownloadController(Constant.IMAGE_SAVE_SCORE_PATH);
        }
        if (data.getAttachmentEntityList() != null) {
            attachmentEntities = data.getAttachmentEntityList();
        }else {
            AttachmentEntity attachmentEntity;
            attachmentEntities = new ArrayList<>();
            if (data.getAttachFileMultiFileIds() != null) { // 服务器
//                List<String> attachFileIdList = Arrays.asList(data.getAttachFileMultiFileIds().split(","));
//                List<String> attachFileNameList = Arrays.asList(data.getAttachFileMultiFileNames().split(","));
                for (Long id : data.getAttachFileMultiFileIds()) {
                    attachmentEntity = new AttachmentEntity();
                    attachmentEntity.id = id;
                    attachmentEntity.name = data.getAttachFileMultiFileNames().get(data.getAttachFileMultiFileIds().indexOf(id));
                    attachmentEntity.deploymentId = attachmentEntity.id; // 赋值附件id,防止下载过滤
                    attachmentEntities.add(attachmentEntity);
                }
                data.setAttachmentEntityList(attachmentEntities);
            }
            if (data.getAttachFileFileAddPaths() != null) { // 本地添加
//                List<String> attachFileAddPathsList = Arrays.asList(data.getAttachFileFileAddPaths().split(","));
                for (String path : data.getAttachFileFileAddPaths()) {
                    attachmentEntity = new AttachmentEntity();
                    attachmentEntity.id = -1L;
                    attachmentEntity.name = path.substring(path.lastIndexOf("\\")+1);
                    attachmentEntity.deploymentId = attachmentEntity.id; // 赋值附件id,防止下载过滤
                    attachmentEntities.add(attachmentEntity);
                }
                data.setAttachmentEntityList(attachmentEntities);
            }
        }

        downloadController.downloadPic(attachmentEntities, "BEAM_1.0.0_patrolWorkerScore", result -> {
//                gifIv.setVisibility(View.GONE);
            galleryView.setGalleryBeans(result);
        });

//        else {
//            gifIv.setVisibility(View.GONE);
//        }
    }


}
