package com.example.thefirstmove;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.thefirstmove.hezu.Bean;

import com.example.thefirstmove.hezu.CommonAdapter;
import com.example.thefirstmove.hezu.HezuxinxiActivity;
import com.example.thefirstmove.hezu.SearchActivity;
import com.example.thefirstmove.hezu.SearchAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LodgerActivity extends AppCompatActivity {




    /**
     * 搜索结果列表view
     */
    private ListView lvResults;
    private Button tijiao;
    private SearchActivity s;
    private SimpleCursorAdapter adapter = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lodger);
        s = new SearchActivity();

        lvResults = (ListView) findViewById(R.id.main_lv_search_results);
//        Intent intent = getIntent();
//        String id = intent.getStringExtra("id");
        tijiao=findViewById(R.id.tijiaoxinxi);

//        Log.v("debug", "s.getcursor().getColumnNames()");
//        lvResults.setOnItemClickListener(new ListView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
////                Intent intent = new Intent(LodgerActivity.this,LodgerActivity.class);//OrderActivity.this,当前页面，DaiActivity.class转至du的页面
//////item内的数据保存在zhimap中
////                Map<String, String> map = (Map<String, String>)lvResults.getItemAtPosition(arg2);
//////需要的数据在intent中
////                intent.putExtra("id", map.get("Id"));
//////跳转页面
////                LodgerActivity.this.startActivity(intent);
//            }
//        });
//显示数据
//        adapter = new SimpleCursorAdapter(this,
//                R.layout.activity_lodger,//R.layout.activity_lodger
//                s.getcursor(),
//                new String[]{"province", "country", "city", "zujin", "nianling", "zhiye", "xingbie"},//游标数据的名称，实际是Table列名字
//                new int[]{R.id.main_lv_search_results}, 0);//对应的UI微件的id  R.id.main_lv_search_results
//        lvResults.setAdapter(adapter);

 //       s.getcursor().close();
//
//        this.finish();
    }

public void OnMyqueClick(View v) {
    Intent intent = new Intent(LodgerActivity.this, HezuxinxiActivity.class);
    LodgerActivity.this.startActivity(intent);
}

    public void OnMysearchClick(View v) {
        Intent intent = new Intent(LodgerActivity.this, SearchActivity.class);
        LodgerActivity.this.startActivity(intent);
    }
}
