package com.example.thefirstmove;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class TenantActivity extends AppCompatActivity {
    private TT_AFragment aFragment;
    private TT_BFragment bFragment;
    private TT_CFragment cFragment;
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant);
        //
        TextView tv=(TextView)findViewById(R.id.user);
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
         phone = sharedPreferences.getString("userID", "");

        if (phone==null){
            Toast.makeText(this,"对不起，您未登录",Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            tv.setText(phone);
            Button First = (Button) findViewById(R.id.findmap);
            First.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TenantActivity.this, TTMapActivity.class);
                    if(aFragment==null){
                        aFragment=new TT_AFragment(); }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,aFragment).commitAllowingStateLoss();
                    startActivity(intent);
                }
            });
            Button Second = (Button) findViewById(R.id.weather);
            Second.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TenantActivity.this, TTWeatherActivity.class);
                    if(bFragment==null){
                        bFragment=new TT_BFragment(); }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,bFragment).commitAllowingStateLoss();
                    startActivity(intent);
                }
            });
            Button Third = (Button) findViewById(R.id.chattt);
            Third.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TenantActivity.this, ChatActivity.class);
                    if(cFragment==null){
                        cFragment=new TT_CFragment(); }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,cFragment).commitAllowingStateLoss();
                    startActivity(intent);
                }
            });
        }
    }
}