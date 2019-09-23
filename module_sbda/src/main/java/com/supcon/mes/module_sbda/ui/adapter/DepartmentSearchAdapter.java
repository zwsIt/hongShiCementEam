package com.supcon.mes.module_sbda.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ScreenUtils;
import com.supcon.mes.mbap.utils.ViewUtil;
import com.supcon.mes.mbap.view.CustomCheckBox;
import com.supcon.mes.mbap.view.CustomCircleTextImageView;
import com.supcon.mes.middleware.model.bean.CommonSearchEntity;
import com.supcon.mes.middleware.model.bean.DepartmentInfo;
import com.supcon.mes.middleware.util.PinYinUtils;
import com.supcon.mes.module_sbda.R;

import java.util.List;
import java.util.Objects;


/**
 * Created by Xushiyun on 2018/5/22.
 * Email:ciruy_victory@gmail.com
 */

public class DepartmentSearchAdapter extends BaseListDataRecyclerViewAdapter<DepartmentInfo> {

    private static final int[] colors = {R.color.random_1, R.color.random_2, R.color.random_3};

    private String recCode;
    private boolean isSingle = true;
    private int minLen = Integer.MAX_VALUE;

    private int COMMON_PADDING = ViewUtil.dpToPx(context, 30);

    public DepartmentSearchAdapter(Context context, boolean isSingle, String code) {
        super(context);
        this.isSingle = true;
        recCode = code;
    }

    public DepartmentSearchAdapter(Context context, boolean isSingle) {
        super(context);
        this.isSingle = isSingle;
    }

    private int genMinLen() {
        if(minLen != Integer.MAX_VALUE) return minLen;
        int result = Integer.MAX_VALUE;
        List<DepartmentInfo> list = getList();
        for (int i = 0; i < list.size(); i++) {
            String rec = list.get(i).layRec;
            int len = TextUtils.isEmpty(rec)?0:rec.split("-").length;
            if(len < result) result = len;
        }
        return result;
    }

    public int getPos(String word) {
        final List<DepartmentInfo> list = getList();
        for (CommonSearchEntity commonSearchEntity : list) {
            final String searchPinyin = commonSearchEntity.getSearchPinyin();
            final String pinyin = TextUtils.isEmpty(searchPinyin) ?
                    PinYinUtils.getPinyin(commonSearchEntity.getSearchName()).charAt(0) + "" : searchPinyin;

            if (pinyin.charAt(0) >= word.toUpperCase().charAt(0)) {
                final int pos = list.indexOf(commonSearchEntity);
                LogUtil.e("pos:" + pos + " word:" + word);
                return pos;
            }
        }
        return -1;
    }

    @Override
    protected BaseRecyclerViewHolder<DepartmentInfo> getViewHolder(int viewType) {
        return isSingle ? new SingleViewHolder(context) : new MultiViewHolder(context);
    }

    class MultiViewHolder extends BaseRecyclerViewHolder<DepartmentInfo> implements View.OnClickListener {
        @BindByTag("itemName")
        TextView itemName;

        @BindByTag("itemId")
        TextView itemId;

        @BindByTag("itemCheckBox")
        CustomCheckBox itemCheckBox;

        @BindByTag("firstLetter")
        TextView firstLetter;

        @BindByTag("customCircleTextImageView")
        CustomCircleTextImageView customCircleTextImageView;


        public MultiViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initBind() {
            super.initBind();
        }

        @Override
        protected void initView() {
            super.initView();
        }

        @Override
        protected void initListener() {
            super.initListener();

            itemView.setOnClickListener(this);
            itemName.setOnClickListener(this);
            itemId.setOnClickListener(this);
            itemCheckBox.setOnCheckedListener(isChecked -> onClick(itemCheckBox));
        }

        @Override
        protected int layoutId() {
            return R.layout.item_base_search_new;
        }

        @Override
        protected void update(DepartmentInfo data) {
            if (data == null) return;
            final int pos = getAdapterPosition();
            final String namePinyin = getNamePinyin(data);
            final String lastNamePinyin = pos <= 0 ? null : getNamePinyin(getItem(pos - 1));

            if (!TextUtils.isEmpty(data.getSearchName()) && itemName != null)
                itemName.setText(data.getSearchName());
            if (itemId != null)
                itemId.setText(data.getSearchId() + "");

            final Character fLetter = namePinyin.toUpperCase().charAt(0);
            final Character lastLetter = lastNamePinyin == null ? null : lastNamePinyin.toUpperCase().charAt(0);
            firstLetter.setVisibility((fLetter != lastLetter) ? View.VISIBLE : View.INVISIBLE);
            firstLetter.setText((fLetter + "").toUpperCase());

            final String searchName = data.getSearchName();
            customCircleTextImageView.setText(searchName.length() > 0 ? "" + searchName.charAt(0) : "");
            customCircleTextImageView.setImageResource(colors[pos % 3]);
        }

        @NonNull
        private String getNamePinyin(CommonSearchEntity data) {
            String namePinyin = TextUtils.isEmpty(data.getSearchPinyin()) ? PinYinUtils.getPinyin(data.getSearchName()) : data.getSearchPinyin().toUpperCase();
            namePinyin = namePinyin == null ? "" : namePinyin;
            return namePinyin;
        }


        @Override
        public void onClick(View v) {
            //先解决点击问题,不太喜欢放在实现上面
            if (v.getId() != R.id.itemCheckBox) itemCheckBox.setChecked(!itemCheckBox.isChecked());
            else v = itemView;

            onItemChildViewClickListener.onItemChildViewClick(v, getAdapterPosition(), 0, getItem(getAdapterPosition()));
        }

    }


    class SingleViewHolder extends BaseRecyclerViewHolder<DepartmentInfo> implements View.OnClickListener {
        @BindByTag("itemName")
        TextView itemName;

        @BindByTag("itemId")
        TextView itemId;

        @BindByTag("itemCheckBox")
        CustomCheckBox itemCheckBox;

        @BindByTag("firstLetter")
        TextView firstLetter;

        @BindByTag("customCircleTextImageView")
        CustomCircleTextImageView customCircleTextImageView;


        public SingleViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initBind() {
            super.initBind();
        }

        @Override
        protected void initView() {
            super.initView();
        }

        @Override
        protected void initListener() {
            super.initListener();

            itemView.setOnClickListener(this);
            itemName.setOnClickListener(this);
            itemId.setOnClickListener(this);
            itemCheckBox.setOnCheckedListener(isChecked -> onClick(itemCheckBox));
        }

        @Override
        protected int layoutId() {
            return R.layout.item_base_search_new;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void update(DepartmentInfo data) {
            if (data == null) return;
            itemCheckBox.setVisibility(View.GONE);//设置多选框不可见
            final String rec = data.layRec==null?"":data.layRec;
            int aaa = rec.split("-").length;
//             int len = (rec.split("-").length)-genMinLen();
            int len = 1;
             len = len<0?0:len;
            final int paddingLeft = COMMON_PADDING*(len);
            ViewUtil.setPaddingLeft(itemView, paddingLeft);
            final int pos = getAdapterPosition();
            final String namePinyin = getNamePinyin(data);
            final String lastNamePinyin = pos <= 0 ? null : getNamePinyin(getItem(pos - 1));



            if (!TextUtils.isEmpty(data.getSearchName()) && itemName != null)
                itemName.setText(data.getSearchName());
            if (itemId != null)
                itemId.setText(data.getLayRec());

            firstLetter.setVisibility(View.GONE );
//            final Character fLetter = namePinyin.toUpperCase().charAt(0);
//            final Character lastLetter = lastNamePinyin == null ? null : lastNamePinyin.toUpperCase().charAt(0);
//            firstLetter.setVisibility((fLetter != lastLetter) ? View.VISIBLE : View.INVISIBLE);
//            firstLetter.setText((fLetter + ""));

            final String searchName = data.getSearchName();
            customCircleTextImageView.setText(searchName.length() > 0 ? "" + searchName.charAt(0) : "");
            customCircleTextImageView.setImageResource(colors[pos%3]);
        }

        @NonNull
        private String getNamePinyin(CommonSearchEntity data) {
            String namePinyin = null;
            namePinyin = TextUtils.isEmpty(data.getSearchPinyin()) ? PinYinUtils.getPinyin(data.getSearchName()) : data.getSearchPinyin().toUpperCase();
            namePinyin = namePinyin == null ? "" : namePinyin;
            return namePinyin;
        }


        @Override
        public void onClick(View v) {
            //先解决点击问题,不太喜欢放在实现上面
            if (v.getId() != R.id.itemCheckBox) itemCheckBox.setChecked(!itemCheckBox.isChecked());
            else v = itemView;

            onItemChildViewClickListener.onItemChildViewClick(v, getAdapterPosition(), 0, getItem(getAdapterPosition()));
        }
    }
}
