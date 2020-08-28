package com.example.thefirstmove;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thefirstmove.bean.Casts;
import com.example.thefirstmove.bean.Weather;
import com.example.thefirstmove.util.WeatherUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlanSearchWeather extends AppCompatActivity {
    private EditText edit_city;
    private Button search_btn;
    private ListView listView;

    //请求的API，详细参考https://lbs.amap.com/api/webservice/guide/api/weatherinfo/
    private String url = "https://restapi.amap.com/v3/weather/weatherInfo?key=e1ba0e4066321825ff162ed46062f9dc";
    //"https://restapi.amap.com/v3/weather/weatherInfo?key=e1ba0e4066321825ff162ed46062f9dc&extensions=all&output=json";
    //使用OkHttpClient进行网络请求
    private OkHttpClient httpClient = new OkHttpClient();
    //使用Gson解析json字符串
    private Gson gson = new Gson();

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //Toast.makeText(PlanSearchWeather.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    //Toast.makeText(PlanSearchWeather.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    //Toast.makeText(PlanSearchWeather.this, "该城市adcode为" + (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    List<Map<String, String>> list = (List<Map<String, String>>) msg.obj;
                    //创建Adapter
                    final SimpleAdapter simpleAdapter = new SimpleAdapter(PlanSearchWeather.this
                            , list, R.layout.weather_listview_item
                            , new String[]{"date", "day_weather", "day_temp", "day_wind", "day_power"
                            , "night_weather", "night_temp", "night_wind", "night_power"}
                            , new int[]{R.id.date, R.id.day_weather, R.id.day_temp, R.id.day_wind, R.id.day_power
                            , R.id.night_weather, R.id.night_temp, R.id.night_wind, R.id.night_power});
                    //绑定Adapter
                    listView.setAdapter(simpleAdapter);
                    Toast.makeText(PlanSearchWeather.this, "查询成功", Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_plansearchweather);
        //初始化
        init();
    }

    private void init() {
        edit_city = findViewById(R.id.edit_city);
        search_btn = findViewById(R.id.search_w_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用方法获取该城市的城市编码
                getAdcode();
            }
        });
        listView = findViewById(R.id.search_weather);
    }

    /**
     * 高德开发平台请求天气查询API中区域编码adcode是必须项，使用高德地理编码服务获取区域编码
     * address:结构化地址信息，规则遵循：国家、省份、城市、区县、城镇、乡村、街道、门牌号码、屋邨、大厦
     */
    private void getAdcode() {
        String address = edit_city.getText().toString();
        String url = "https://restapi.amap.com/v3/geocode/geo?key=e1ba0e4066321825ff162ed46062f9dc&address=" + address;

        final Request request = new Request.Builder().url(url).get().build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = httpClient.newCall(request).execute();
                    //请求成功
                    if (response.isSuccessful()) {
                        String result = response.body().string();

                        Log.i("result", result);

                        //转JsonObject
                        JsonObject object = new JsonParser().parse(result).getAsJsonObject();
                        //转JsonArray
                        JsonArray array = object.get("geocodes").getAsJsonArray();
                        JsonObject info = array.get(0).getAsJsonObject();

                        //获取adcode
                        String adcode = info.get("adcode").getAsString();
                        Log.i("测试获取adcode", adcode);

                        //请求天气查询
                        getWeather(adcode);

                        Message message = Message.obtain();
                        message.what = 2;
                        message.obj = adcode;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    Log.i("SearchMainActivity.java", "服务器异常:" + e.toString());

                    Message message = Message.obtain();
                    message.what = 0;
                    message.obj = "服务器异常";
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 查询天气
     */
    private void getWeather(String adcode) {
        String newUrl = url + "&city=" + adcode + "&extensions=all&output=json";
        final Request request = new Request.Builder().url(newUrl).get().build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = httpClient.newCall(request).execute();
                    //请求成功
                    if (response.isSuccessful()) {
                        String result = response.body().string();
                        List<Map<String, String>> weatherList = new ArrayList<>();
                        Log.i("服务器返回的结果:", result);
                        //存储解析json字符串得到的天气信息
                        //List<Map<String, String>> weatherList = new ArrayList<>();

                        //使用Gson解析
                        Weather weather = gson.fromJson(result, Weather.class);
                        //获取今天天气信息
                        Casts today = weather.getForecasts().get(0).getCasts().get(0);
                        //添加Map数据到List
                        Map<String, String> map1 = new HashMap<>();
                        map1.put("date", "今天");
                        map1.put("day_weather", today.getDayweather());
                        map1.put("day_temp", today.getDaytemp() + "℃");
                        if (WeatherUtil.noWindDirection(today.getDaywind())) {
                            map1.put("day_wind", today.getDaywind());
                        } else {
                            map1.put("day_wind", today.getDaywind() + "风");
                        }
                        map1.put("day_power", today.getDaypower() + "级");
                        map1.put("night_weather", today.getNightweather());
                        map1.put("night_temp", today.getNighttemp() + "℃");
                        if (WeatherUtil.noWindDirection(today.getNightwind())) {
                            map1.put("night_wind", today.getNightwind());
                        } else {
                            map1.put("night_wind", today.getNightwind() + "风");
                        }
                        map1.put("night_power", today.getNightpower() + "级");
                        weatherList.add(map1);

                        //获取明天天气信息
                        Casts tomorrow = weather.getForecasts().get(0).getCasts().get(1);
                        //添加Map数据到List
                        Map<String, String> map2 = new HashMap<>();
                        map2.put("date", "明天");
                        map2.put("day_weather", tomorrow.getDayweather());
                        map2.put("day_temp", tomorrow.getDaytemp() + "℃");
                        if (WeatherUtil.noWindDirection(tomorrow.getDaywind())) {
                            map2.put("day_wind", tomorrow.getDaywind());
                        } else {
                            map2.put("day_wind", tomorrow.getDaywind() + "风");
                        }
                        map2.put("day_power", tomorrow.getDaypower() + "级");
                        map2.put("night_weather", tomorrow.getNightweather());
                        map2.put("night_temp", tomorrow.getNighttemp() + "℃");
                        if (WeatherUtil.noWindDirection(tomorrow.getNightwind())) {
                            map2.put("night_wind", tomorrow.getNightwind());
                        } else {
                            map2.put("night_wind", tomorrow.getNightwind() + "风");
                        }
                        map2.put("night_power", tomorrow.getNightpower() + "级");
                        weatherList.add(map2);

                        //获取后天天气信息
                        Casts afterTomorrow = weather.getForecasts().get(0).getCasts().get(2);
                        //添加Map数据到List
                        Map<String, String> map3 = new HashMap<>();
                        map3.put("date", "后天");
                        map3.put("day_weather", afterTomorrow.getDayweather());
                        map3.put("day_temp", afterTomorrow.getDaytemp() + "℃");
                        if (WeatherUtil.noWindDirection(afterTomorrow.getDaywind())) {
                            map3.put("day_wind", afterTomorrow.getDaywind());
                        } else {
                            map3.put("day_wind", afterTomorrow.getDaywind() + "风");
                        }
                        map3.put("day_power", afterTomorrow.getDaypower() + "级");
                        map3.put("night_weather", afterTomorrow.getNightweather());
                        map3.put("night_temp", afterTomorrow.getNighttemp() + "℃");
                        if (WeatherUtil.noWindDirection(afterTomorrow.getNightwind())) {
                            map3.put("night_wind", afterTomorrow.getNightwind());
                        } else {
                            map3.put("night_wind", afterTomorrow.getNightwind() + "风");
                        }
                        map3.put("night_power", afterTomorrow.getNightpower() + "级");
                        weatherList.add(map3);

                        //将服务器返回数据写入Handler
                        Message message = Message.obtain();
                        message.what = 3;
                        message.obj = weatherList;
                        handler.sendMessage(message);
                        //handler.obtainMessage(1, response).sendToTarget();
                    }
                } catch (Exception e) {
                    Log.i("SearchWeather.java", "服务器异常:" + e.toString());

                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = e.toString();
                    e.printStackTrace();
                }
            }
        }).start();
    }

}