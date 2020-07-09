package com.example.thefirstmove.hezu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.thefirstmove.LodgerActivity;
import com.example.thefirstmove.R;
import com.example.thefirstmove.dao.DBOpenHelper;
import com.example.thefirstmove.util.pubFun;

public class HezuxinxiActivity extends AppCompatActivity {

    private RadioGroup xingbie;
    private EditText money;
    private EditText age;
    private EditText mingzi;
    private LinearLayout zhiye;
    private Button que;
    private Spinner provinceSpinner = null;  //省级（省、直辖市）
    private Spinner citySpinner = null;     //地级市
    private Spinner countySpinner = null;    //县级（区、县、县级市）
    ArrayAdapter<String> provinceAdapter = null;  //省级适配器
    ArrayAdapter<String> cityAdapter = null;    //地级适配器
    ArrayAdapter<String> countyAdapter = null;    //县级适配器
    static int provincePosition = 3;
    private Spinner spsheng;
private SearchActivity s;
private String gender;

    //省级选项值
    private String[] province = new String[] {"北京","上海","天津","广东"};//,"重庆","黑龙江","江苏","山东","浙江","香港","澳门"};
    //地级选项值
    private String[][] city = new String[][]
            {
                    { "东城区", "西城区", "崇文区", "宣武区", "朝阳区", "海淀区", "丰台区", "石景山区", "门头沟区",
                            "房山区", "通州区", "顺义区", "大兴区", "昌平区", "平谷区", "怀柔区", "密云县",
                            "延庆县" },
                    { "长宁区", "静安区", "普陀区", "闸北区", "虹口区" },
                    { "和平区", "河东区", "河西区", "南开区", "河北区", "红桥区", "塘沽区", "汉沽区", "大港区",
                            "东丽区" },
                    { "广州", "深圳", "韶关"  //,"珠海","汕头","佛山","湛江","肇庆","江门","茂名","惠州","梅州",
                            // "汕尾","河源","阳江","清远","东莞","中山","潮州","揭阳","云浮"
                    }
            };

    //县级选项值
    private String[][][] county = new String[][][]
            {
                    {   //北京
                            {"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},
                            {"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"}
                    },
                    {    //上海
                            {"无"},{"无"},{"无"},{"无"},{"无"}
                    },
                    {    //天津
                            {"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"}
                    },
                    {    //广东
                            {"海珠区","荔湾区","越秀区","白云区","萝岗区","天河区","黄埔区","花都区","从化市","增城市","番禺区","南沙区"}, //广州
                            {"宝安区","福田区","龙岗区","罗湖区","南山区","盐田区"}, //深圳
                            {"武江区","浈江区","曲江区","乐昌市","南雄市","始兴县","仁化县","翁源县","新丰县","乳源县"}  //韶关
                    }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hezuxinxi);

        provinceSpinner = (Spinner) findViewById(R.id.province);
        countySpinner = (Spinner) findViewById(R.id.county);
        citySpinner = (Spinner) findViewById(R.id.city);

        money = (EditText) findViewById(R.id.money);
        age = (EditText) findViewById(R.id.age);
        mingzi = (EditText) findViewById(R.id.mingzi);
        zhiye = (LinearLayout) findViewById(R.id.zhiye);
        xingbie = (RadioGroup) findViewById(R.id.xingbie);

        int gender_id=xingbie.getCheckedRadioButtonId();    //获取这个id
        RadioButton checked_btn=(RadioButton)findViewById(gender_id);//获取这个按钮
        gender = checked_btn.getText().toString();  //得到值

        que = (Button) findViewById(R.id.que);
        setSpinner();

    }
    private void setSpinner()
    {

        provinceSpinner = (Spinner)findViewById(R.id.province);
        citySpinner = (Spinner)findViewById(R.id.city);
        countySpinner = (Spinner)findViewById(R.id.county);

        //绑定适配器和值
        provinceAdapter = new ArrayAdapter<String>(HezuxinxiActivity.this,
                android.R.layout.simple_spinner_item, province);
        provinceSpinner.setAdapter(provinceAdapter);
        provinceSpinner.setSelection(3,true);  //设置默认选中项，此处为默认选中第4个值

        cityAdapter = new ArrayAdapter<String>(HezuxinxiActivity.this,
                android.R.layout.simple_spinner_item, city[3]);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setSelection(0,true);  //默认选中第0个

        countyAdapter = new ArrayAdapter<String>(HezuxinxiActivity.this,
                android.R.layout.simple_spinner_item, county[3][0]);
        countySpinner.setAdapter(countyAdapter);
        countySpinner.setSelection(0, true);


        //省级下拉框监听
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                //position为当前省级选中的值的序号

                //将地级适配器的值改变为city[position]中的值
                cityAdapter = new ArrayAdapter<String>(
                        HezuxinxiActivity.this, android.R.layout.simple_spinner_item, city[position]);
                // 设置二级下拉列表的选项内容适配器
                citySpinner.setAdapter(cityAdapter);
                provincePosition = position;    //记录当前省级序号，留给下面修改县级适配器时用
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }

        });


        //地级下拉监听
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3)
            {
                countyAdapter = new ArrayAdapter<String>(HezuxinxiActivity.this,
                        android.R.layout.simple_spinner_item, county[provincePosition][position]);
                countySpinner.setAdapter(countyAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });
    }

    public void OnMyqueClick(View v) {

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

        //数据库
        //call DBOpenHelper
        DBOpenHelper helper = new DBOpenHelper(this, "movingday.db", null,3);
        SQLiteDatabase db = helper.getWritableDatabase();
        boolean isTrue = true;
        if (pubFun.isEmpty(countySpinner.getSelectedItem().toString()) || pubFun.isEmpty(citySpinner.getSelectedItem().toString())||pubFun.isEmpty(provinceSpinner.getSelectedItem().toString())) {
            Toast.makeText(this, "入住区域不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(pubFun.isEmpty(money.getText().toString())){
            isTrue = false;
            Toast.makeText(this, "租金不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(pubFun.isEmpty(age.getText().toString())){
            isTrue = false;
            Toast.makeText(this, "年龄不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(pubFun.isEmpty(sb.toString())){
            isTrue = false;
            Toast.makeText(this, "职业不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(pubFun.isEmpty(gender)){
            isTrue = false;
            Toast.makeText(this, "性别不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        if(isTrue = true){
            //call DBOpenHelper
             helper = new DBOpenHelper(this,"movingday.db",null,3);
            db = helper.getWritableDatabase();
            Cursor c = db.query("hezu_tb", null, "province=? and country=? and city=? and zujin=? and nianling=? and zhiye=? and xingbie=?", new String[]{provinceSpinner.getSelectedItem().toString(),countySpinner.getSelectedItem().toString(),citySpinner.getSelectedItem().toString(), money.getText().toString(), age.getText().toString(),sb.toString(), gender}, null, null, null);
               //insert data
                ContentValues values= new ContentValues();
                values.put("mingzi", mingzi.getText().toString());
                values.put("province",provinceSpinner.getSelectedItem().toString());
                values.put("country",countySpinner.getSelectedItem().toString());
                values.put("city",citySpinner.getSelectedItem().toString());
                values.put("zujin",money.getText().toString());
                values.put("nianling",age.getText().toString());
                values.put("zhiye",sb.toString());
                values.put("xingbie", gender);

                long rowid = db.insert("hezu_tb",null,values);

                Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
                this.finish();
            }
            db.close();
        }

    }

