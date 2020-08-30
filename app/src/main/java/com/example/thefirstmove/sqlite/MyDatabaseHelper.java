package com.example.thefirstmove.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {//创建动态分享数据库
    public static final String CREATE_aaatable="create table aaatable("+"nick text primary key,"
            +"address text,"
            +"description text,"
            +"id text)";
            //+"img BLOB)";
    private Context mContext;
    public MyDatabaseHelper(Context context , String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext=context;
        //Toast.makeText(mContext,"create aaaaaaa", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建sql语句，新建表
        String sqlCommand1 = CREATE_aaatable;
        //执行sql语句
        db.execSQL(sqlCommand1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }
}
