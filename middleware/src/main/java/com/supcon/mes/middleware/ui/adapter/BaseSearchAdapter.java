package com.supcon.mes.middleware.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomCheckBox;
import com.supcon.mes.mbap.view.CustomCircleTextImageView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.model.bean.CommonSearchEntity;
import com.supcon.mes.middleware.R;

import java.util.List;
import java.util.Objects;

/**
 * @author Xushiyun
 * @date 2018/5/22
 * Email:ciruy_victory@gmail.com
 */

public class BaseSearchAdapter extends BaseListDataRecyclerViewAdapter<CommonSearchEntity> {
    
    private static final int[] COLORS = {R.color.random_1, R.color.random_2, R.color.random_3};
    public static final int SINGLE = 0;
    public static final int MULTI = 1;
    
    /**
     * 搜索模式：单选，多选
     */
    private boolean isMulti;
    
    public BaseSearchAdapter(Context context, boolean isMulti) {
        super(context);
        this.isMulti = isMulti;
    }
    
    public void setMulti(boolean multi) {
        isMulti = multi;
    }
    
    /**
     * 获取当前点击字母所在adapter位置
     *
     * @param word 查询字母
     * @return 字母所在位置
     */
    public int getPos(String word) {
        final List<CommonSearchEntity> list = getList();
        //如果adapter中并没有任何数据，list获取的结果就为0，获取迭代器就会报错，所以这里如果为null直接返回
        if(null == list) return -1;
        for (CommonSearchEntity commonSearchEntity : list) {
            if (commonSearchEntity.compareWithLetter(word) >= 0) {
                return list.indexOf(commonSearchEntity);
            }
        }
        return -1;
    }
    
    @Override
    protected BaseRecyclerViewHolder<CommonSearchEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }
    
    /**
     * SingleViewHolder和MultiViewHolder的唯一差别为CheckBox的可见和不可见性，其他逻辑完全一致，所以这里我还是打算直接取消两个机制
     */
    class ViewHolder extends BaseRecyclerViewHolder<CommonSearchEntity> implements View.OnClickListener {
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
        
        @BindByTag("itemProperty")
        CustomTextView itemProperty;
        
        
        ViewHolder(Context context) {
            super(context);
        }
        
        @Override
        protected void initListener() {
            super.initListener();
            
            itemView.setOnClickListener(v -> onClick(itemView));
            itemName.setOnClickListener(this);
            itemId.setOnClickListener(this);
            itemProperty.setOnClickListener(this);
            itemCheckBox.setOnCheckedListener(isChecked -> onClick(itemCheckBox));
        }
        
        @Override
        protected int layoutId() {
            return R.layout.item_base_search_new;
        }
        
        @Override
        protected void update(CommonSearchEntity data) {
            if(data== null) return;
            //单选模式下，设置多选框不可见,多选模式下，设置多选框可见
            itemCheckBox.setVisibility(isMulti ? View.VISIBLE : View.GONE);
            final int pos = getAdapterPosition();
            
            Objects.requireNonNull(itemName).setText(data.getSearchName());
            Objects.requireNonNull(itemId).setText(data.getSearchId());
            Objects.requireNonNull(itemProperty).setValue(data.getSearchProperty());
            
            final String currLetter = data.getHeaderLetter();
            final String lastLetter = pos <= 0 ? null : getItem(pos - 1).getHeaderLetter();
            firstLetter.setVisibility((!currLetter.equals(lastLetter)) ? View.VISIBLE : View.INVISIBLE);
            firstLetter.setText(currLetter);
            
            final String searchName = data.getSearchName().trim();
            customCircleTextImageView.setText(searchName.length() > 0 ? searchName.charAt(0) + "" : "");
            
            //设置图标颜色为依次渐变
            customCircleTextImageView.setImageResource(COLORS[pos % 3]);
        }
        
        @Override
        public void onClick(View v) {
            /**
             * 先解决点击问题,不太喜欢放在实现上面,判定点击的组件的id
             * 如果id为checkbox对应的id，则切换checkbox点击状态
             * 否则将点击视图设置为根视图，因为在现在的情况下，点击局部和点击整体的效果是一样的
             */
            if (v.getId() != R.id.itemCheckBox) {
                itemCheckBox.setChecked(!itemCheckBox.isChecked());
            } else {
                v = itemView;
            }
            
            int pos = getAdapterPosition();
            //通过返回的action的数值信息来判断到底操作的是什么模式下的组件
            onItemChildViewClickListener.onItemChildViewClick(v, pos, isMulti ? MULTI : SINGLE,
                    getItem(pos));
        }
    }
}
