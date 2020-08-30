package com.example.thefirstmove;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SharepiActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharepi);
        SharedPreferences sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
        String phone=sharedPreferences.getString("userID","");

        if (phone==null){
            Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(this,"欢迎",Toast.LENGTH_SHORT).show();
        }
        Button ckdt=(Button)findViewById(R.id.ckdt);
        ckdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SharepiActivity.this, SharePhotoActivity.class);
                startActivity(intent);
            }
        });
        Button fbdt=(Button)findViewById(R.id.fbdt);
        fbdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SharepiActivity.this, ShareInfoActivity.class);
                startActivity(intent);
            }
        });
        Button cktp=(Button)findViewById(R.id.cktp);
        cktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SharepiActivity.this, ShareShakeActivity.class);
                startActivity(intent);
            }
        });
    }
}