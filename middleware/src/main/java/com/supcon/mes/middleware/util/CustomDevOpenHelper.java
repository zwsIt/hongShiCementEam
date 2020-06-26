package com.supcon.mes.middleware.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.supcon.mes.middleware.model.bean.AccountInfoDao;
import com.supcon.mes.middleware.model.bean.DaoMaster;
import com.supcon.mes.middleware.model.bean.WorkInfoDao;

import org.greenrobot.greendao.database.Database;

/**
 * @Description: 自定义数据库升级帮助类
 * @Author: zhangwenshuai
 * @CreateDate: 2020/6/15 10:26
 */
public class CustomDevOpenHelper extends DaoMaster.DevOpenHelper {
    public CustomDevOpenHelper(Context context, String name) {
        super(context, name);
    }

    public CustomDevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
//        super.onUpgrade(db, oldVersion, newVersion);
        // 重写数据库升级方法处理
        updateDB(db,oldVersion,newVersion);
    }

    private void updateDB(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db,ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db,ifExists);
            }
        }, AccountInfoDao.class, WorkInfoDao.class);
    }
}
