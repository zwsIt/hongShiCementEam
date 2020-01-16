package com.supcon.mes.module_contact.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.view.CustomCircleTextImageView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.module_contact.IntentRouter;
import com.supcon.mes.module_contact.R;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author xushiyun
 * @Create-time 7/11/19
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public class ContactAdapter extends BaseListDataRecyclerViewAdapter<ContactEntity> {
    private static final int[] colors = {R.color.random_1, R.color.random_2, R.color.random_3};
    private boolean isSelectStaff, isMulti;
    private Map<String, ContactEntity> selectStaffs;
    private String selectStaffStr;
    private String searchTag;
    public ContactAdapter(Context context, boolean isSelectStaff, boolean isMulti, String selectStaffStr, String searchTag) {
        super(context);
        this.isSelectStaff = isSelectStaff;
        this.isMulti = isMulti;
        this.selectStaffStr = selectStaffStr;
        this.searchTag = searchTag;

        if (isMulti) {
            selectStaffs = new HashMap<>();
            if (!TextUtils.isEmpty(selectStaffStr)) {
                List<ContactEntity> contactEntities = GsonUtil.jsonToList(selectStaffStr, ContactEntity.class);

                for (ContactEntity contactEntity : contactEntities) {
                    selectStaffs.put(contactEntity.getCODE(), contactEntity);
                }

            }
        }
    }

    public ContactAdapter(Context context) {
        super(context);
    }


    public Map<String, ContactEntity> getSelectStaffs() {
        return selectStaffs;

    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(int viewType) {
        return new ContactViewHolder(context);
    }

    public class ContactViewHolder extends BaseRecyclerViewHolder<ContactEntity> {
        @BindByTag("userName")
        TextView userName;
        @BindByTag("userCode")
        TextView userCode;
        @BindByTag("userJob")
        TextView userJob;
        @BindByTag("company")
        TextView company;
        @BindByTag("department")
        TextView department;
        //    @BindByTag("userIcon")
        CustomCircleTextImageView userIcon;
        ImageView checkBox;

        public ContactViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected void initView() {
            super.initView();
            userIcon = itemView.findViewById(R.id.userIcon);
            checkBox = itemView.findViewById(R.id.checkBox);
        }

        @Override
        protected void initListener() {
            super.initListener();
            itemView.setOnClickListener(v -> {

                ContactEntity contactEntity = getItem(getAdapterPosition());
                if (!isSelectStaff) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.IntentKey.CONTACT_ENTITY, contactEntity);
                    IntentRouter.go(context, Constant.Router.CONTACT_VIEW, bundle);
                } else if (!isMulti) {
                    CommonSearchEvent commonSearchEvent = new CommonSearchEvent();
                    CommonSearchStaff commonSearchStaff = new CommonSearchStaff();
                    commonSearchStaff.id = contactEntity.getSTAFFID();
                    commonSearchStaff.code = contactEntity.getCODE();
                    commonSearchStaff.name = contactEntity.getNAME();
                    commonSearchStaff.pinyin = contactEntity.getSearchPinyin();
                    commonSearchStaff.department = contactEntity.getDEPARTMENTNAME();
                    commonSearchStaff.mainPosition = contactEntity.getPOSITIONNAME();
                    commonSearchEvent.commonSearchEntity = commonSearchStaff;
                    commonSearchEvent.flag = searchTag;
                    EventBus.getDefault().post(commonSearchEvent);
                    onItemChildViewClick(itemView, 0, contactEntity);
                } else if (selectStaffs.containsKey(contactEntity.getCODE())) {
                    selectStaffs.remove(contactEntity.getCODE());
                    checkBox.setImageResource(R.drawable.ic_check_no);
                } else {
                    selectStaffs.put(contactEntity.getCODE(), contactEntity);
                    checkBox.setImageResource(R.drawable.ic_check_yes);
                }

                contactEntity.updateTime = System.currentTimeMillis();
                EamApplication.dao().getContactEntityDao().insertOrReplace(contactEntity);


            });

        }

        @Override
        protected int layoutId() {
            return R.layout.item_contact;
        }

        @Override
        protected void update(ContactEntity data) {

            final String searchName = data.getNAME();
            userIcon.setText(searchName.length() > 0 ? "" + searchName.charAt(0) : "");
            userIcon.setImageResource(colors[getAdapterPosition() % 3]);

//            if(!TextUtils.isEmpty(data.picturePath)){
//                new ContactPicController(itemView).getStaffPic(data.staffId);
//            }
            userName.setText(data.getNAME());
            userCode.setText(data.getCODE());
            userJob.setText(data.getPOSITIONNAME());

            if (!TextUtils.isEmpty(data.getDEPARTMENTNAME())) {
                department.setText(data.getDEPARTMENTNAME());
            } else {
                department.setText(data.getFULLPATHNAME());
            }

            if (!TextUtils.isEmpty(data.getCOMPANYNAME())) {
                company.setText(data.getCOMPANYNAME());
            } else {
                company.setText("");
            }


            if (isMulti) {
                checkBox.setVisibility(View.VISIBLE);
                if (selectStaffs.containsKey(data.getCODE())) {
                    checkBox.setImageResource(R.drawable.ic_check_yes);
                } else {
                    checkBox.setImageResource(R.drawable.ic_check_no);
                }
            }
        }
    }
}
