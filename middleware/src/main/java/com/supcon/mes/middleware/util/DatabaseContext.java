package com.supcon.mes.middleware.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/7/13
 * Email zhangwenshuai1@supcon.com
 * Desc 自定义数据库设计路径
 */
public class DatabaseContext extends ContextWrapper {
    public static String dbPath = "";

    public DatabaseContext(Context base) {
        super(base);
    }

    public DatabaseContext(Context base,String databasePath){
        super(base);
        if (!TextUtils.isEmpty(databasePath)){
            dbPath = databasePath;
        }
    }

    @Override
    public File getDatabasePath(String name) {
//        return super.getDatabasePath(name);
        File dbDir = new File(dbPath);
        if(!dbDir.exists()){
            dbDir.mkdir();
        }

        File dbFile = new File(dbPath, name);
        if(!dbFile.exists()){
            try {
                dbFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dbFile;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
//        return super.openOrCreateDatabase(name, mode, factory);
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name),factory);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return super.openOrCreateDatabase(name, mode, factory, errorHandler);
    }
}
