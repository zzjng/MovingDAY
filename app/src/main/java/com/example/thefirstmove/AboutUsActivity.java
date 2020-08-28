package com.example.thefirstmove;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AboutUsActivity extends AppCompatActivity {
    private AU_AFragment aFragment;
    private AU_BFragment bFragment;
    private AU_CFragment cFragment;
    private AU_Fragment Fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        SharedPreferences sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
        String phone=sharedPreferences.getString("userID","");

        if (phone==null){
            Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(this,"欢迎",Toast.LENGTH_SHORT).show();
        }
        Fragment =new AU_Fragment();
        //把Fragment添加到Activity中，记得调用commit
        getSupportFragmentManager().beginTransaction().add(R.id.container,Fragment).commitNowAllowingStateLoss();

        Button First=(Button)findViewById(R.id.first);
        First.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aFragment==null){
                    aFragment=new AU_AFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container,aFragment).commitAllowingStateLoss();
            }
        });
        Button Second=(Button)findViewById(R.id.second);
        Second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bFragment==null){
                    bFragment=new AU_BFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container,bFragment).commitAllowingStateLoss();
            }
        });
        Button Third=(Button)findViewById(R.id.third);
        Third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cFragment==null){
                    cFragment=new AU_CFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container,cFragment).commitAllowingStateLoss();
            }
        });
    }
}
