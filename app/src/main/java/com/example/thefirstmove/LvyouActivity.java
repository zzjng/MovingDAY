package com.example.thefirstmove;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.ListView;

import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.thefirstmove.hezu.HezuxinxiActivity;
import com.example.thefirstmove.hezu.SearchActivity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class LvyouActivity extends AppCompatActivity {


    /**
     * 搜索结果列表view
     */



    private Button tijiao;

    private SimpleCursorAdapter adapter = null;
private Cursor c;
private ListView lvTips;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lvyou);

        tijiao=findViewById(R.id.tijiaoxinxi);
        lvTips=findViewById(R.id.show1);

        SharedPreferences sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
        String phone=sharedPreferences.getString("userID","");

        if (phone==null){
            Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(this,"欢迎",Toast.LENGTH_SHORT).show();
        }

        c=SearchActivity.getcursor();

        if (c != null && c.getCount() >= 1){
            show();
        }else{
            Toast.makeText(this, "未传递数据！", Toast.LENGTH_SHORT).show();
        }

    }

    public void show() {



        if (c != null && c.getCount() >= 1) {

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            Map<String, Object> map;
//遍历数据
            while (c.moveToNext()) {
                map = new HashMap<String, Object>();
                map.put("mingzi", c.getString(c.getColumnIndex("mingzi")));
                map.put("province", c.getString(c.getColumnIndex("province")));
                map.put("country", c.getString(c.getColumnIndex("country")));
                map.put("city", c.getString(c.getColumnIndex("city")));
                map.put("zujin", c.getString(c.getColumnIndex("zujin")));
                map.put("nianling", c.getString(c.getColumnIndex("nianling")));
                map.put("zhiye", c.getString(c.getColumnIndex("zhiye")));
                map.put("xingbie", c.getString(c.getColumnIndex("xingbie")));
                list.add(map);
            }
            SimpleAdapter a = new SimpleAdapter(this, list, R.layout.list, new String[]{"mingzi", "province", "country", "city", "zujin", "nianling", "zhiye", "xingbie"},
                    new int[]{R.id.name, R.id.sheng, R.id.shi, R.id.qu, R.id.jin, R.id.year, R.id.ye, R.id.xing});
            lvTips.setAdapter(a);
               lvTips.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {

//item内的数据保存在zhimap中
                Map<String, String> map = (Map<String, String>)lvTips.getItemAtPosition(arg2);
//需要的数据在intent中

//跳转页面

                Intent intent = new Intent(LvyouActivity.this,WeChatActivity.class);//OrderActivity.this,当前页面，DaiActivity.class转至du的页面
                LvyouActivity.this.startActivity(intent);
            }
        });

        } else {
            Toast.makeText(this, "未搜索到符合心意驴友！", Toast.LENGTH_SHORT).show();
        }

    }

public void OnMyqueClick(View v) {
    Intent intent = new Intent(LvyouActivity.this, HezuxinxiActivity.class);
    LvyouActivity.this.startActivity(intent);
}

    public void OnMysearchClick(View v) {
        Intent intent = new Intent(LvyouActivity.this, SearchActivity.class);
        LvyouActivity.this.startActivity(intent);
    }


    public void method() {
    }
}
