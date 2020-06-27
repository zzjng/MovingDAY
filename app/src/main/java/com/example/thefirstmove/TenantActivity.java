package com.example.thefirstmove;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class TenantActivity extends AppCompatActivity {
    private TT_AFragment aFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant);
        /*aFragment =new TT_AFragment();
        //把Fragment添加到Activity中，记得调用commit
        getSupportFragmentManager().beginTransaction().add(R.id.container,aFragment).commitNowAllowingStateLoss();*/
        Button First=(Button)findViewById(R.id.findmap);
        First.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TenantActivity.this,TTMapActivity.class);
                startActivity(intent);
            }
        });
       /* Button First=(Button)findViewById(R.id.map);
        First.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aFragment==null){
                    aFragment=new TT_AFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container,aFragment).commitAllowingStateLoss();
            }
        });*/
    }
}