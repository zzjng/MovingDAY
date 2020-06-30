package com.example.thefirstmove.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

import com.example.thefirstmove.R;
import com.example.thefirstmove.hezu.ViewHolder;

/**
 * @programName: DBOpenHelper.java
 * @programFunction: database helper class
 * @createDate: 2018/09/19
 * @author:zzj
 * @version:
 * xx.   yyyy/mm/dd   ver    author    comments
 * 01.   2018/09/19   1.00   AnneHan   New Create
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(Context context,String name, CursorFactory factory,
                        int version){
        super(context, name, factory, version);
    }

    @Override
    //首次创建数据库的时候调用，一般可以执行建库，建表的操作
    //Sqlite没有单独的布尔存储类型，它使用INTEGER作为存储类型，0为false，1为true
    public void onCreate(SQLiteDatabase db) {
        //user table
        db.execSQL("create table if not exists user_tb(_id integer primary key autoincrement," +
                "userID text not null," +
                "pwd text not null)");
        db.execSQL("create table if not exists hezu_tb(_id integer primary key autoincrement," +
                "province text not null," +
                "country text not null,"+"city text not null,"+"zujin text not null,"+"nianling text not null,"+"zhiye text not null,"+"xingbie text not null)" );

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //改动3
        switch (oldVersion) {

            case 1:
                db.execSQL("create table if not exists hezu_tb(_id integer primary key autoincrement," +
                        "province text not null," +
                        "country text not null,"+"city text not null,"+"zujin text not null,"+"nianling text not null,"+"zhiye text not null,"+"xingbie text not null)" );

            default:
        }


    }



}



