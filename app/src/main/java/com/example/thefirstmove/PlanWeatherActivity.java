package com.example.thefirstmove;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.thefirstmove.bean.Casts;
import com.example.thefirstmove.bean.Weather;
import com.example.thefirstmove.util.WeatherUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlanWeatherActivity extends AppCompatActivity {

    //控件
    private ListView listView;
    private TextView showCity;
    private Button searchBtn;

    //请求的API，详细参考https://lbs.amap.com/api/webservice/guide/api/weatherinfo/
    private String url = "https://restapi.amap.com/v3/weather/weatherInfo?key=e1ba0e4066321825ff162ed46062f9dc";
    //"https://restapi.amap.com/v3/weather/weatherInfo?key=e1ba0e4066321825ff162ed46062f9dc&city=340104&extensions=all&output=json";
    //使用OkHttpClient进行网络请求
    private OkHttpClient httpClient = new OkHttpClient();
    //使用Gson解析json字符串
    private Gson gson = new Gson();
    //存储解析json字符串得到的天气信息
    private List<Map<String, String>> weatherList = new ArrayList<>();
    //保存经纬度
    private String longitude;
    private String latitude;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                //错误码为0表示定位成功
                if (aMapLocation.getErrorCode() == 0) {
                    //获取城市信息
                    String city = aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict();
                    //设置显示
                    showCity.setText(city);
                    //获取经纬度,截取经纬度（不超过6位）
                    longitude = String.valueOf(aMapLocation.getLongitude()).substring(0, 5);
                    latitude = String.valueOf(aMapLocation.getLatitude()).substring(0, 5);
                    //Toast.makeText(PlanWeatherActivity.this, "经纬度为：" + longitude + "," + latitude, Toast.LENGTH_SHORT).show();
                    //根据经纬度获取adcode
                    getAdcode(longitude + "," + latitude);
                } else {
                    Toast.makeText(PlanWeatherActivity.this, "定位失败，ErrCode=" + aMapLocation.getErrorCode(), Toast.LENGTH_SHORT).show();

                    //定位失败时,可通过ErrCode(错误码)信息来确定失败的原因,errInfo是错误信息,详见错误码表。
                    Log.i("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //Toast.makeText(PlanWeatherActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    //Toast.makeText(PlanWeatherActivity.this, "该城市adcode为" + (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    //Toast.makeText(PlanWeatherActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    //创建Adapter
                    final SimpleAdapter simpleAdapter = new SimpleAdapter(PlanWeatherActivity.this
                            , weatherList, R.layout.weather_listview_item
                            , new String[]{"date", "day_weather", "day_temp", "day_wind", "day_power"
                            , "night_weather", "night_temp", "night_wind", "night_power"}
                            , new int[]{R.id.date, R.id.day_weather, R.id.day_temp, R.id.day_wind, R.id.day_power
                            , R.id.night_weather, R.id.night_temp, R.id.night_wind, R.id.night_power});
                    //绑定Adapter
                    listView.setAdapter(simpleAdapter);
                    Toast.makeText(PlanWeatherActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_planweather);
        Toast.makeText(PlanWeatherActivity.this,"请稍候，正在查询",Toast.LENGTH_SHORT).show();
        //初始化
        init();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //如果权限不足提示请求权限
            ActivityCompat.requestPermissions(PlanWeatherActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            //如果权限已允许，准备定位
            prepareLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止定位
        mLocationClient.stopLocation();
    }

    /**
     * Android6.0申请权限的回调方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //如果允许权限则准备定位
            prepareLocation();
        } else {
            Toast.makeText(PlanWeatherActivity.this, "请允许应用获取相应的权限", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 定位准备工作
     */
    private void prepareLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化
        mLocationOption = new AMapLocationClientOption();
        //低功耗定位模式：不会使用GPS和其他传感器，只会使用网络定位（Wi-Fi和基站定位）；
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //使用单次定位，默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 初始化控件
     */
    private void init() {
        //获取控件
        showCity = findViewById(R.id.show_city);
        listView = findViewById(R.id.home_weather);
        searchBtn = findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanWeatherActivity.this, PlanSearchWeather.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取经纬度后，通过逆地理编码API获得该区域的adcode，将其作为天气查询API的参数获得当前位置的天气信息
     *
     * @param location 经纬度信息：经度在前，纬度在后，经纬度间以“,”分割，经纬度小数点后不要超过 6 位
     */
    private void getAdcode(String location) {
        String url = "https://restapi.amap.com/v3/geocode/regeo?key=6d29a580912e6f4b7ffb3b057d1f9ab2&location=" + location;

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

                        String adcode = object.get("regeocode").getAsJsonObject()
                                .get("addressComponent").getAsJsonObject().get("adcode").getAsString();

                        Log.i("测试获取adcode", adcode);

                        //请求天气查询
                        getWeather(adcode);

                        Message message = Message.obtain();
                        message.what = 1;
                        message.obj = adcode;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    Log.i("ShowWeather.java", "服务器异常:" + e.toString());

                    Message message = Message.obtain();
                    message.what = 0;
                    message.obj = "服务器异常";
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 通过OkHttp发送网络请求查询天气
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

                        Log.i("服务器返回的结果", result);

                        //使用Gson解析
                        Weather weather = gson.fromJson(result, Weather.class);
                        //获取今天天气信息
                        Casts today = weather.getForecasts().get(0).getCasts().get(0);
                        //添加Map数据到List
                        Map<String, String> map1 = new HashMap<>();
                        map1.put("date", "今天");
                        map1.put("day_weather", today.getDayweather());
                        map1.put("day_temp", today.getDaytemp() + "℃");
                        //如果是“无风向”，直接显示，否则显示“xx风”，提升显示效果
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

                        Log.i("今天", today.toString());

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

                        Log.i("明天", tomorrow.toString());

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

                        Log.i("后天", afterTomorrow.toString());

                        Message message = Message.obtain();
                        message.what = 3;
                        message.obj = "查询成功";
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    Log.i("ShowWeather.java", "服务器异常:" + e.toString());

                    Message message = Message.obtain();
                    message.what = 2;
                    message.obj = "服务器异常";
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
