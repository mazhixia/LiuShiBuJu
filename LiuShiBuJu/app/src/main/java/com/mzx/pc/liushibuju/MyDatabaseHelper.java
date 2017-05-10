package com.mzx.pc.liushibuju;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/5/9.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    /*
    补充一下建表的一些类型
    integer ---整型
    real-----浮点类型
    text---文本类型
    blob---二进制类型

    */
    public static final String CREATE_BUJU= "create table buju(id integer primary key autoincrement,name text)";
    private Context mContext ;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super( context, name, factory, version);
        mContext= context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //执行建表语句
        db.execSQL(CREATE_BUJU);
//        Toast.makeText(mContext , "数据库创建成功" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
