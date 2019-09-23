package com.supcon.mes.middleware.util;

import android.content.Context;
import android.text.TextUtils;

import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.mes.mbap.adapter.ListEmptyAdapter;
import com.supcon.mes.mbap.adapter.RecyclerEmptyAdapter;
import com.supcon.mes.mbap.beans.EmptyViewEntity;

/**
 * Created by wangshizhan on 2018/7/9
 * Email:wangshizhan@supcom.com
 */
public class EmptyAdapterHelper {

    public static IListAdapter<EmptyViewEntity> getRecyclerEmptyAdapter(Context context, String msg){

        RecyclerEmptyAdapter emptyAdapter = new RecyclerEmptyAdapter(context);
        emptyAdapter.addData(EmptyViewHelper.createEmptyEntity(msg));

        return emptyAdapter;

    }

    public static IListAdapter<EmptyViewEntity> getRecyclerEmptyAdapter(Context context, String msg, String btnText){

        if(TextUtils.isEmpty(btnText)){
            return getRecyclerEmptyAdapter(context, msg);
        }

        RecyclerEmptyAdapter emptyAdapter = new RecyclerEmptyAdapter(context);
        emptyAdapter.addData(EmptyViewHelper.createEmptyEntity(msg, btnText));

        return emptyAdapter;

    }

    public static IListAdapter<EmptyViewEntity> getListEmptyAdapter(Context context, String msg){
        ListEmptyAdapter emptyAdapter = new ListEmptyAdapter(context);
        emptyAdapter.addData(EmptyViewHelper.createEmptyEntity(msg));

        return emptyAdapter;

    }

    public static IListAdapter<EmptyViewEntity> getListEmptyAdapter(Context context, String msg, String btnText){

        if(TextUtils.isEmpty(btnText)){
            return getListEmptyAdapter(context, msg);
        }
        ListEmptyAdapter emptyAdapter = new ListEmptyAdapter(context);
        emptyAdapter.addData(EmptyViewHelper.createEmptyEntity(msg, btnText));

        return emptyAdapter;

    }
}
