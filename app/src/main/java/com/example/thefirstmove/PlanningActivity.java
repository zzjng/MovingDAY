package com.example.thefirstmove;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class PlanningActivity extends AppCompatActivity {
    private Plan_AFragment aFragment;
    private Plan_BFragment bFragment;
    private Plan_CFragment cFragment;
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);
        //
        /*TextView tv=(TextView)findViewById(R.id.user);
        Intent intent=getIntent();
        Bundle bundle=getIntent().getExtras();
        phone=bundle.getString("phone");
        if (phone==null){
            Toast.makeText(this,"对不起，您未登录",Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            tv.setText(phone);*/
        SharedPreferences sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
        String phone=sharedPreferences.getString("userID","");

        if (phone==null){
            Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(this,"欢迎"+phone,Toast.LENGTH_SHORT).show();
            Button First = (Button) findViewById(R.id.findmap);
            First.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PlanningActivity.this, PlanMapActivity.class);
                    if(aFragment==null){
                        aFragment=new Plan_AFragment(); }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,aFragment).commitAllowingStateLoss();
                    startActivity(intent);
                }
            });
            Button Second = (Button) findViewById(R.id.weather);
            Second.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PlanningActivity.this, PlanWeatherActivity.class);
                    if(bFragment==null){
                        bFragment=new Plan_BFragment(); }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,bFragment).commitAllowingStateLoss();
                    startActivity(intent);
                }
            });
            Button Third = (Button) findViewById(R.id.chattt);
            Third.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PlanningActivity.this, ChatActivity.class);
                    if(cFragment==null){
                        cFragment=new Plan_CFragment(); }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,cFragment).commitAllowingStateLoss();
                    startActivity(intent);
                }
            });
        }
    }
}