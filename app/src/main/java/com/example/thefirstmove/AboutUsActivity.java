package com.example.thefirstmove;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AboutUsActivity extends AppCompatActivity {
    private AU_AFragment aFragment;
    private AU_BFragment bFragment;
    private AU_Fragment Fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
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
    }
}
