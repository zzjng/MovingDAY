package com.example.thefirstmove.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
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

    private static final int oldVersion = 1;
    private static final int currentVersion = 3;

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

        Log.e("DBOpenHelper", "onCreate: ");
        //首次安装检查是否有更新
        onUpgrade(db,oldVersion,currentVersion);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        Log.e("DBOpenHelper", "oldVersion: "+oldVersion);
        Log.e("DBOpenHelper", "newVersion: "+newVersion);


        if (oldVersion < newVersion){
            switch (oldVersion){
                case 1:
                    db.execSQL("create table if not exists hezu_tb(_id integer primary key autoincrement," +"mingzi text not null,"+
                            "province text not null," +
                            "country text not null,"+"city text not null,"+"zujin text not null,"+"nianling text not null,"+"zhiye text not null,"+"xingbie text not null)" );
                    onUpgrade(db,2,newVersion);
                    break;
                case 2:
                    //查询
                    Cursor cursor = db.query("hezu_tb",null,null,null,null,null,null);
                     if(cursor.getColumnIndex("mignzi") != -1){
                    db.execSQL("alter table hezu_tb add column mingzi varchar(20)");
                    onUpgrade(db,3,newVersion);}
                    break;
                default:
                    break;
            }
        }



    }



}



