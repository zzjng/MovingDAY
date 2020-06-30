package com.example.thefirstmove.hezu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.CursorAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thefirstmove.LodgerActivity;
import com.example.thefirstmove.MainActivity;
import com.example.thefirstmove.R;
import com.example.thefirstmove.dao.DBOpenHelper;

import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

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
    private Spinner spsheng;

    private Button btnBack;//返回按钮
    private Context mContext;// 上下文对象
    private ListView lvTips;//弹出列表
    private ArrayAdapter<String> mHintAdapter;//提示adapter （推荐adapter）
    private ArrayAdapter<String> mAutoCompleteAdapter;//自动补全adapter 只显示名字

    private LinearLayout zhiye;
    private RadioGroup shiyousex;
    private Button quxiao;
    private Button sure;
    private String gend;
    public String job;
    private SimpleCursorAdapter adapter = null;
    /**
     * 设置搜索回调接口
     *
     * @param监听者
     */

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
                            {"海珠区", "荔湾区", "越秀区", "白云区", "萝岗区", "天河区", "黄埔区", "花都区", "从化市", "增城市", "番禺区", "南沙区"}, //广州
                            {"宝安区", "福田区", "龙岗区", "罗湖区", "南山区", "盐田区"}, //深圳
                            {"武江区", "浈江区", "曲江区", "乐昌市", "南雄市", "始兴县", "仁化县", "翁源县", "新丰县", "乳源县"}  //韶关
                    }
            };

private Cursor c;
private ListView lvtips;
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

        btnBack = (Button) findViewById(R.id.search_btn_back);
//        LayoutInflater factory = LayoutInflater.from(SearchActivity.this);
//        View layout = factory.inflate(R.layout.activity_lodger, null);
//        ListView lvTips = (ListView) layout.findViewById(R.id.main_lv_search_results);
        lvTips = (ListView) findViewById(R.id.results);

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
        DBOpenHelper helper = new DBOpenHelper(this, "movingday.db", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        c= db.query("hezu_tb", null, " province=? and country=? and city=? and xingbie=? and zhiye=? and zujin>? and zujin<? and nianling>? and nianling<? ", new String[]{provinceSpinner.getSelectedItem().toString(), countySpinner.getSelectedItem().toString(), citySpinner.getSelectedItem().toString(), gend, job, monmin.getText().toString(), monmax.getText().toString(), agmin.getText().toString(), agmax.getText().toString()}, null, null, null);


        if (c != null && c.getCount() >= 1) {

//            LayoutInflater factory = LayoutInflater.from(SearchActivity.this);
//            View layout = factory.inflate(R.layout.activity_lodger, null);
//            ListView LR = (ListView) layout.findViewById(R.id.main_lv_search_results);

//            int resId = getResources().getIdentifier("activity_lodger", "layout",getPackageName());
//            Log.i("res",Integer.toString(resId));
//            int Id = getResources().getIdentifier("main_lv_search_results", "id",getPackageName());
//            Log.i("res",Integer.toString(Id));

//            Intent i = new Intent(SearchActivity.this , LodgerActivity.class);
//              startActivity(i);
//显示数据listview
            adapter = new SimpleCursorAdapter(this,
                    R.layout.activity_search,//R.layout.activity_lodger
                    c,
                    new String[]{"province", "country", "city", "zujin", "nianling", "zhiye", "xingbie"},//游标数据的名称，实际是Table列名字
                    new int[]{R.id.results}, 0);//对应的UI微件的id  R.id.main_lv_search_results
            lvTips.setAdapter(adapter);
        } else {
            Toast.makeText(this, "未搜索到合适室友！", Toast.LENGTH_SHORT).show();
        }

    }

    public  Cursor getcursor(){
        return c;
    }


}





