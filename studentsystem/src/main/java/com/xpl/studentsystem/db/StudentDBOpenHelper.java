package com.xpl.studentsystem.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库操作者
  */
public class StudentDBOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "StudentDBOpenHelper" ;

    public StudentDBOpenHelper( Context context) {
        super(context, "student.db", null, 1);
        Log.d(TAG, "StudentDBOpenHelper: ");
    }

    /**
     * 数据库第一次创建的时候调用
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table info (_id integer primary key autoincrement,studentid varchar(20),name varchar(20),phone varchar(20));");
//        "create table info (_id integer primary key autoincrement, studentid varchar(20), name varchar(20), phone varchar(20));"
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: ");

    }
}
