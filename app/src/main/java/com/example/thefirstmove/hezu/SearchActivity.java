package com.example.thefirstmove.hezu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.thefirstmove.LodgerActivity;
import com.example.thefirstmove.MainActivity;
import com.example.thefirstmove.R;
import com.example.thefirstmove.dao.DBOpenHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private static Cursor c;
    private EditText monmin;
    private EditText monmax;
    private EditText agmin;
    private EditText agmax;
    private Spinner provinceSpinner = null;  //省级（省、直辖市）
    private Spinner citySpinner = null;     //地级市
    private Spinner countySpinner = null;    //县级（区、县、县级市）
    ArrayAdapter<String> provinceAdapter = null;  //省级适配器
    ArrayAdapter<String> cityAdapter = null;    //地级适配器
    ArrayAdapter<String> countyAdapter = null;    //县级适配器
    static int provincePosition = 3;


    private ListView lvTips;//弹出列表
    private ArrayAdapter<String> mHintAdapter;//提示adapter （推荐adapter）
    private ArrayAdapter<String> mAutoCompleteAdapter;//自动补全adapter 只显示名字

    private LinearLayout zhiye;
    private RadioGroup shiyousex;
    private Button quxiao;
    private Button sure;
    private String gend;
    public String job;
    private SimpleAdapter adapter = null;
    /**
     * 设置搜索回调接口
     *
     * @param监听者
     */



    static public  Cursor getcursor() {
        return c;
    }

    public void setcursor(Cursor cursor) {
        this.c = (Cursor) cursor;
    }



    //省级选项值
    private String[] province = new String[]{"北京", "上海", "天津", "广东"};//,"重庆","黑龙江","江苏","山东","浙江","香港","澳门"};
    //地级选项值
    private String[][] city = new String[][]
            {
                    {"东城区", "西城区", "崇文区", "宣武区", "朝阳区", "海淀区", "丰台区", "石景山区", "门头沟区",
                            "房山区", "通州区", "顺义区", "大兴区", "昌平区", "平谷区", "怀柔区", "密云县",
                            "延庆县"},
                    {"长宁区", "静安区", "普陀区", "闸北区", "虹口区"},
                    {"和平区", "河东区", "河西区", "南开区", "河北区", "红桥区", "塘沽区", "汉沽区", "大港区",
                            "东丽区"},
                    {"广州", "深圳", "韶关"  //,"珠海","汕头","佛山","湛江","肇庆","江门","茂名","惠州","梅州",
                            // "汕尾","河源","阳江","清远","东莞","中山","潮州","揭阳","云浮"
                    }
            };

    //县级选项值
    private String[][][] county = new String[][][]
            {
                    {   //北京
                            {"无"}, {"无"}, {"无"}, {"无"}, {"无"}, {"无"}, {"无"}, {"无"}, {"无"}, {"无"},
                            {"无"}, {"无"}, {"无"}, {"无"}, {"无"}, {"无"}, {"无"}, {"无"}
                    },
                    {    //上海
                            {"无"}, {"无"}, {"无"}, {"无"}, {"无"}
                    },
                    {    //天津
                            {"无"}, {"无"}, {"无"}, {"无"}, {"无"}, {"无"}, {"无"}, {"无"}, {"无"}, {"无"}
                    },
                    {    //广东
                            {"海珠", "荔湾", "越秀", "白云", "萝岗", "天河", "黄埔", "花都", "从化市", "增城市", "番禺", "南沙"}, //广州
                            {"宝安", "福田", "龙岗", "罗湖", "南山", "盐田"}, //深圳
                            {"武江", "浈江", "曲江", "乐昌", "南雄市", "始兴县", "仁化县", "翁源县", "新丰县", "乳源县"}  //韶关
                    }
            };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        provinceSpinner = (Spinner) findViewById(R.id.spin_province);
        countySpinner = (Spinner) findViewById(R.id.spin_county);
        citySpinner = (Spinner) findViewById(R.id.spin_city);
        monmin = (EditText) findViewById(R.id.moneymin);
        monmax = (EditText) findViewById(R.id.moneymax);
        agmin = (EditText) findViewById(R.id.agemin);
        agmax = (EditText) findViewById(R.id.agemax);
        zhiye = (LinearLayout) findViewById(R.id.zhiyeleixing);
        quxiao = (Button) findViewById(R.id.quxiao);
        sure = (Button) findViewById(R.id.queren);
        // lvTips= (ListView) View.inflate(this,R.layout.activity_lodger,null).findViewById(R.id.show1);

//        LayoutInflater factory = LayoutInflater.from(SearchActivity.this);
//        View layout = factory.inflate(R.layout.activity_lodger, null);
//        ListView lvTips = (ListView) layout.findViewById(R.id.main_lv_search_results);


//        lvTips = (ListView) findViewById(R.id.results);

//        lvTips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //hint list view gone and result list view show
//                lvTips.setVisibility(View.GONE);
//            }
//        });

//性别监听
        shiyousex = (RadioGroup) findViewById(R.id.shiyouxingbie);
        int gender_id = shiyousex.getCheckedRadioButtonId();    //获取这个id
        RadioButton checked_btn = (RadioButton) findViewById(gender_id);//获取这个按钮
        gend = checked_btn.getText().toString();  //得到值

        setSpinner();

    }


    /*
     * 设置下拉框
     */
    private void setSpinner() {

        provinceSpinner = (Spinner) findViewById(R.id.spin_province);
        citySpinner = (Spinner) findViewById(R.id.spin_city);
        countySpinner = (Spinner) findViewById(R.id.spin_county);

        //绑定适配器和值
        provinceAdapter = new ArrayAdapter<String>(SearchActivity.this,
                android.R.layout.simple_spinner_item, province);
        provinceSpinner.setAdapter(provinceAdapter);
        provinceSpinner.setSelection(3, true);  //设置默认选中项，此处为默认选中第4个值

        cityAdapter = new ArrayAdapter<String>(SearchActivity.this,
                android.R.layout.simple_spinner_item, city[3]);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setSelection(0, true);  //默认选中第0个

        countyAdapter = new ArrayAdapter<String>(SearchActivity.this,
                android.R.layout.simple_spinner_item, county[3][0]);
        countySpinner.setAdapter(countyAdapter);
        countySpinner.setSelection(0, true);


        //省级下拉框监听
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //position为当前省级选中的值的序号

                //将地级适配器的值改变为city[position]中的值
                cityAdapter = new ArrayAdapter<String>(
                        SearchActivity.this, android.R.layout.simple_spinner_item, city[position]);
                // 设置二级下拉列表的选项内容适配器
                citySpinner.setAdapter(cityAdapter);
                provincePosition = position;    //记录当前省级序号，留给下面修改县级适配器时用
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


        //地级下拉监听
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                countyAdapter = new ArrayAdapter<String>(SearchActivity.this,
                        android.R.layout.simple_spinner_item, county[provincePosition][position]);
                countySpinner.setAdapter(countyAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    /**
     * 城市区域监听结束
     */


    public void OnfanhuiClick(View v) {
        finish();
    }


    public void OnMysearchClick(View v) {
        StringBuffer sb = new StringBuffer();
        //拿到所有的子类长度
        int cNum = zhiye.getChildCount();
        for (int i = 0; i < cNum; i++) {
            //根据i 拿到每一个CheckBox
            CheckBox cb = (CheckBox) zhiye.getChildAt(i);
            //判断CheckBox是否被选中
            if (cb.isChecked()) {
                //把被选中的文字添加到StringBuffer中
                sb.append(cb.getText().toString());
            }
        }
        job = sb.toString();//sb局部变量，所有选中的职业


//        /**
//         * 获取城市下拉框选中的值
//         */
//        provinceSpinner.getSelectedItem().toString();
//        citySpinner.getSelectedItem().toString();
//        countySpinner.getSelectedItem().toString();
//        gend;//性别
//        monmax.getText().toString();
//        monmin.getText().toString();
//        agmax.getText().toString();
//        agmin.getText().toString();
//        quxiao;
//        sure;
//        btnBack;
//        lvTips;


        //数据库
        //call DBOpenHelper
        DBOpenHelper helper = new DBOpenHelper(this, "movingday.db", null, 3);
        SQLiteDatabase db = helper.getReadableDatabase();
        c= db.query("hezu_tb", null, " province=? and country=? and city=? and xingbie=? and zhiye=? and zujin>? and zujin<? and nianling>? and nianling<? ", new String[]{provinceSpinner.getSelectedItem().toString(), countySpinner.getSelectedItem().toString(), citySpinner.getSelectedItem().toString(), gend, job, monmin.getText().toString(), monmax.getText().toString(), agmin.getText().toString(), agmax.getText().toString()}, null, null, null);


                if (c != null && c.getCount() >= 1) {
//
//
//
                    Intent i = new Intent(SearchActivity.this, LodgerActivity.class);
                    startActivity(i);


////显示数据listview
////            adapter = new SimpleCursorAdapter(this,
////                    R.layout.list,//R.layout.activity_lodger
////                    c,
////                    new String[]{"mingzi","province", "country", "city", "zujin", "nianling", "zhiye", "xingbie"},//游标数据的名称，实际是Table列名字
////                    new int[]{R.id.name,R.id.sheng,R.id.shi,R.id.qu,R.id.jin,R.id.year,R.id.ye,R.id.xing}, 0);//对应的UI微件的id  R.id.main_lv_search_results
////
//            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
//            Map<String,Object> map;
////遍历数据
//            while (c.moveToNext())
//            {
//                map = new HashMap<String, Object>();
//                map.put("mingzi",c.getString(c.getColumnIndex("mingzi")));
//                map.put("province",c.getString(c.getColumnIndex("province")));
//                map.put("country",c.getString(c.getColumnIndex("country")));
//                map.put("city",c.getString(c.getColumnIndex("city")));
//                map.put("zujin",c.getString(c.getColumnIndex("zujin")));
//                map.put("nianling",c.getString(c.getColumnIndex("nianling")));
//                map.put("zhiye",c.getString(c.getColumnIndex("zhiye")));
//                map.put("xingbie",c.getString(c.getColumnIndex("xingbie")));
//                list.add(map);
//            }
//            SimpleAdapter    a = new SimpleAdapter(this,list,R.layout.list,new String[]{"mingzi","province", "country", "city", "zujin", "nianling", "zhiye", "xingbie"},
//                    new int[]{R.id.name,R.id.sheng,R.id.shi,R.id.qu,R.id.jin,R.id.year,R.id.ye,R.id.xing});
//
//
//            lvTips.setAdapter(a);
//
        } else {
            Toast.makeText(this, "未搜索到合适室友！", Toast.LENGTH_SHORT).show();
        }
//
//    }




}

    }





