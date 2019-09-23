package com.supcon.mes.module_main.ui.adaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_main.IntentRouter;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.model.bean.WaitDealtEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/24
 * ------------- Description -------------
 * 待办adapter
 */
public class WaitDealtAdapter extends BaseListDataRecyclerViewAdapter<WaitDealtEntity> {
    private boolean isEdit;

    public WaitDealtAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<WaitDealtEntity> getViewHolder(int viewType) {
        return new ContentViewHolder(context);
    }

    public void setEditable(boolean isEdit) {
        this.isEdit = isEdit;
        notifyDataSetChanged();
    }

    class ContentViewHolder extends BaseRecyclerViewHolder<WaitDealtEntity> {

        @BindByTag("waitDealtEamName")
        TextView waitDealtEamName;
        @BindByTag("waitDealtTime")
        TextView waitDealtTime;
        @BindByTag("waitDealtEamSource")
        TextView waitDealtEamSource;
        @BindByTag("waitDealtEamState")
        TextView waitDealtEamState;

        @BindByTag("waitDealtEntrust")
        ImageView waitDealtEntrust;
        @BindByTag("chkBox")
        CheckBox chkBox;

        public ContentViewHolder(Context context) {
            super(context);
        }


        @Override
        protected void initListener() {
            super.initListener();
            waitDealtEntrust.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WaitDealtEntity item = getItem(getAdapterPosition());
                    if (isEdit) {
                        if (!TextUtils.isEmpty(item.state) && item.state.equals("派工")) {
                            chkBox.performClick();
                        } else {
                            ToastUtils.show(context, "请先取消派单进去再进入详情操作！");
                        }
                        return;
                    }
                    onItemChildViewClick(view, 0, getItem(getAdapterPosition()));
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WaitDealtEntity item = getItem(getAdapterPosition());
                    if (isEdit) {
                        if (!TextUtils.isEmpty(item.state) && (item.state.equals("派工") || item.state.equals("编辑"))) {
                            chkBox.performClick();
                        } else {
                            ToastUtils.show(context, "请先取消派单进去再进入详情操作！");
                        }
                        return;
                    }
                    if (TextUtils.isEmpty(item.processkey)) {
                        if (item.dataid == null || TextUtils.isEmpty(item.soucretype)) {
                            ToastUtils.show(context, "未查询到当前单据状态!");
                            return;
                        }
                        if (!TextUtils.isEmpty(item.istemp) && item.soucretype.equals("巡检提醒")) {
                            if (item.istemp.equals("1")) {
                                IntentRouter.go(context, Constant.Router.LSXJ_LIST);
                            } else {
                                IntentRouter.go(context, Constant.Router.JHXJ_LIST);
                            }
                        } else {
                            if (TextUtils.isEmpty(item.peroidtype)) {
                                ToastUtils.show(context, "未查询到当前单据周期类型!");
                                return;
                            }
                            Bundle bundle = new Bundle();
                            bundle.putLong(Constant.IntentKey.WARN_ID, item.dataid);
                            bundle.putString(Constant.IntentKey.PROPERTY, item.peroidtype);
                            if (item.soucretype.equals("润滑提醒")) {
                                IntentRouter.go(context, Constant.Router.LUBRICATION_EARLY_WARN, bundle);
                            } else if (item.soucretype.equals("零部件提醒")) {
                                IntentRouter.go(context, Constant.Router.SPARE_EARLY_WARN, bundle);
                            } else if (item.soucretype.equals("维保提醒")) {
                                IntentRouter.go(context, Constant.Router.MAINTENANCE_EARLY_WARN, bundle);
                            }
                        }

                    } else {
                        if (!TextUtils.isEmpty(item.tableno)) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.IntentKey.TABLENO, item.tableno);
                            if (item.processkey.equals("work")) {
                                if (!TextUtils.isEmpty(item.openurl)) {
                                    switch (item.openurl) {
                                        case Constant.WxgdView.RECEIVE_OPEN_URL:
                                            IntentRouter.go(context, Constant.Router.WXGD_RECEIVE, bundle);
                                            break;
                                        case Constant.WxgdView.DISPATCH_OPEN_URL:
                                            IntentRouter.go(context, Constant.Router.WXGD_DISPATCHER, bundle);
                                            break;
                                        case Constant.WxgdView.EXECUTE_OPEN_URL:
                                            IntentRouter.go(context, Constant.Router.WXGD_EXECUTE, bundle);
                                            break;
                                        case Constant.WxgdView.ACCEPTANCE_OPEN_URL:
                                            IntentRouter.go(context, Constant.Router.WXGD_ACCEPTANCE, bundle);
                                            break;
                                    }
                                } else {
                                    ToastUtils.show(context, "未查询到工单状态状态!");
                                }
                            } else if (item.processkey.equals("faultInfoFW")) {
                                IntentRouter.go(context, Constant.Router.YH_EDIT, bundle);
                            }
                        }
                    }
                }
            });
            chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    WaitDealtEntity item = getItem(getAdapterPosition());
                    item.isCheck = b;
                }
            });
        }

        @Override
        protected int layoutId() {
            return R.layout.hs_item_wait_dealt;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void update(WaitDealtEntity data) {
            if (isEdit && !TextUtils.isEmpty(data.state) && (data.state.equals("派工") || data.state.equals("编辑"))) {
                chkBox.setVisibility(View.VISIBLE);
                chkBox.setChecked(data.isCheck);
            } else {
                chkBox.setVisibility(View.GONE);
            }
            waitDealtEamName.setText(Util.strFormat(TextUtils.isEmpty(data.eamname) ? data.eamcode : data.eamname));
            if (data.nextduration != null) {
                waitDealtTime.setText(Util.strFormat2(data.nextduration));
            } else {
                waitDealtTime.setText(data.excutetime != null ? DateUtil.dateFormat(data.excutetime, "yyyy-MM-dd HH:mm:ss") : "--");
            }
            waitDealtEamSource.setText(Util.strFormat(data.soucretype));

            if (data.overdateflag.equals("1")) {
                waitDealtEamSource.setTextColor(context.getResources().getColor(R.color.orange));
            } else {
                waitDealtEamSource.setTextColor(context.getResources().getColor(R.color.gray));
            }
            if (!TextUtils.isEmpty(data.state)) {
                waitDealtEamState.setText(data.state);
                if (data.state.equals("编辑") || data.state.equals("派工")) {
                    waitDealtEamState.setTextColor(context.getResources().getColor(R.color.gray));
                } else if (data.state.equals("执行")) {
                    waitDealtEamState.setTextColor(context.getResources().getColor(R.color.yellow));
                } else if (data.state.equals("验收")) {
                    waitDealtEamState.setTextColor(context.getResources().getColor(R.color.blue));
                }
            } else {
                waitDealtEamState.setText("");
            }
            if (TextUtils.isEmpty(data.processkey)) {
                waitDealtEntrust.setVisibility(View.GONE);
            } else {
                waitDealtEntrust.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(data.entrflag) && data.entrflag.equals("0")) {
                waitDealtEntrust.setImageDrawable(context.getResources().getDrawable(R.drawable.btn_entrust));
            } else {
                waitDealtEntrust.setImageDrawable(context.getResources().getDrawable(R.drawable.btn_entrusted));
            }
        }
    }
}
