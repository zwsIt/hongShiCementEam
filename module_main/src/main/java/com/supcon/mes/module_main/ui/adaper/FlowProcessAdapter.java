package com.supcon.mes.module_main.ui.adaper;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.model.bean.FlowProcessEntity;

import java.util.List;

/**
 * 单据流程
 */
public class FlowProcessAdapter extends BaseListDataRecyclerViewAdapter<FlowProcessEntity> {

    public FlowProcessAdapter(Context context) {
        super(context);
    }

    public FlowProcessAdapter(Context context, List<FlowProcessEntity> list) {
        super(context, list);
    }

    @Override
    protected BaseRecyclerViewHolder<FlowProcessEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<FlowProcessEntity> {

        @BindByTag("itemFlowLineLeft")
        View itemFlowLineLeft;
        @BindByTag("itemFlowDot")
        ImageView itemFlowDot;
        @BindByTag("itemFlowLineRight")
        View itemFlowLineRight;
        @BindByTag("itemFlowName")
        TextView itemFlowName;
        @BindByTag("itemFlowTime")
        TextView itemFlowTime;
        @BindByTag("itemFlowStaff")
        TextView itemFlowStaff;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.hs_item_flow_process;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void update(FlowProcessEntity data) {
            if (Constant.TableStatus_CH.PRE_DISPATCH.equals(data.flowProcess) || Constant.TableStatus_CH.EDIT.equals(data.flowProcess) || Constant.TableStatus_CH.DISPATCH.equals(data.flowProcess)) {
                if (data.isFinish) {
                    itemFlowDot.setImageDrawable(context.getDrawable(R.drawable.ic_edit_end));
                    itemFlowLineRight.setVisibility(View.VISIBLE);
                    itemFlowTime.setVisibility(View.VISIBLE);
                } else {
                    itemFlowDot.setImageDrawable(context.getDrawable(R.drawable.ic_edit_ing));
                    itemFlowLineRight.setVisibility(View.GONE);
                    itemFlowTime.setVisibility(View.GONE);
                }
            } else if (Constant.TableStatus_CH.PRE_EXECUTE.equals(data.flowProcess) || Constant.TableStatus_CH.PRE_NOTIFY.equals(data.flowProcess)
                    || Constant.TableStatus_CH.EXECUTE.equals(data.flowProcess)) {
                if (data.isFinish) {
                    itemFlowDot.setImageDrawable(context.getDrawable(R.drawable.ic_excute_end));
                    itemFlowLineRight.setVisibility(View.VISIBLE);
                    itemFlowTime.setVisibility(View.VISIBLE);
                } else {
                    itemFlowDot.setImageDrawable(context.getDrawable(R.drawable.ic_excute_ing));
                    itemFlowLineRight.setVisibility(View.GONE);
                    itemFlowTime.setVisibility(View.GONE);
                }
            } else if (Constant.TableStatus_CH.NOTIFY.equals(data.flowProcess)) {
                if (data.isFinish) {
                    itemFlowDot.setImageDrawable(context.getDrawable(R.drawable.ic_notify_end));
                    itemFlowLineRight.setVisibility(View.VISIBLE);
                    itemFlowTime.setVisibility(View.VISIBLE);
                } else {
                    itemFlowDot.setImageDrawable(context.getDrawable(R.drawable.ic_notify_ing));
                    itemFlowLineRight.setVisibility(View.GONE);
                    itemFlowTime.setVisibility(View.GONE);
                }
            }else if (Constant.TableStatus_CH.REVIEW.equals(data.flowProcess) || Constant.TableStatus_CH.CONFIRM.equals(data.flowProcess)) {
                if (data.isFinish) {
                    itemFlowDot.setImageDrawable(context.getDrawable(R.drawable.ic_review_end));
                    itemFlowLineRight.setVisibility(View.VISIBLE);
                    itemFlowTime.setVisibility(View.VISIBLE);
                } else {
                    itemFlowDot.setImageDrawable(context.getDrawable(R.drawable.ic_review_ing));
                    itemFlowLineRight.setVisibility(View.GONE);
                    itemFlowTime.setVisibility(View.GONE);
                }
            } else if (Constant.TableStatus_CH.PRE_ACCEPT.equals(data.flowProcess) || Constant.TableStatus_CH.ACCEPT.equals(data.flowProcess)) {
                if (data.isFinish) {
                    itemFlowDot.setImageDrawable(context.getDrawable(R.drawable.ic_check_end));
                    itemFlowLineRight.setVisibility(View.VISIBLE);
                    itemFlowTime.setVisibility(View.VISIBLE);
                } else {
                    itemFlowDot.setImageDrawable(context.getDrawable(R.drawable.ic_check_ing));
                    itemFlowLineRight.setVisibility(View.GONE);
                    itemFlowTime.setVisibility(View.GONE);
                }
            } else if (Constant.TableStatus_CH.END.equals(data.flowProcess)) {
                if (data.isFinish) {
                    itemFlowDot.setImageDrawable(context.getDrawable(R.drawable.ic_end_end));
                    itemFlowLineRight.setVisibility(View.VISIBLE);
                    itemFlowTime.setVisibility(View.VISIBLE);

                } else {
                    itemFlowDot.setImageDrawable(context.getDrawable(R.drawable.ic_end_ing));
                    itemFlowLineRight.setVisibility(View.GONE);
                    itemFlowTime.setVisibility(View.GONE);
                }
            } else {
                itemFlowLineRight.setVisibility(View.GONE);
                itemFlowTime.setVisibility(View.GONE);
            }
            itemFlowName.setText(data.flowProcess);
            itemFlowStaff.setText(data.dealStaff);
            if (!TextUtils.isEmpty(data.time) && !"--".equals(data.time)) {
                itemFlowTime.setText(data.time.substring(5, data.time.length() - 3));
            } else {
                itemFlowTime.setText("--");
            }
        }
    }
}
