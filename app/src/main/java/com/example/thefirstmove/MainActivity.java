package com.example.thefirstmove;
/**
 * @programName: MovingDAY
 * @programFunction: help people to live a suitable house
 * @createDate: 2020/05/19
 * @author: 张召静 周梦焓
 * totally six parts:登录注册，我是房主，我是房客，我要合租，个人资料，关于我们
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //第一次点击事件发生的时间
    private long mExitTime;
    private String phoneno;
    Bundle bundle=new Bundle();
    private CircleMenuLayout mCircleMenuLayout;

    private String[] mItemTexts = new String[] { "登录&注册", "关于我们", "个人空间",
            "我是房主", "我是房客", "合租论坛" };
    private int[] mItemImgs = new int[] { R.mipmap.home_mbank_1_normal,
            R.mipmap.home_mbank_2_normal, R.mipmap.home_mbank_3_normal,
            R.mipmap.home_mbank_4_normal, R.mipmap.home_mbank_5_normal,
            R.mipmap.home_mbank_6_normal };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CircleMenuLayout mCircleMenuLayout = findViewById(R.id.id_menulayout);
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);

        mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
            public void itemClick(View view, int pos) {
                if (mItemTexts[pos] == "我是房主") {
                    openHouseOwner(view);
                } else if (mItemTexts[pos] == "我是房客") {
                    openTenant(view);
                } else if (mItemTexts[pos] == "登录&注册") {
                    openLogin(view);
                } else if (mItemTexts[pos] == "合租论坛") {
                    openShare(view);
                } else if (mItemTexts[pos] == "关于我们") {
                    openAboutUs(view);
                } else if (mItemTexts[pos] == "个人空间") {
                    openIndividual(view);
                }
                ;
            }

            public void itemCenterClick(View view) {
                Toast.makeText(MainActivity.this, "you can do something just like login or register", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 跳转至登录&注册界面
     * @param v
     */
    private void openLogin(View v){

        Intent intent = new Intent();
        intent.setClass(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent, 100);
    }

    /**
     * 跳转至我是房客界面
     * @param v
     */
    private void openTenant(View v){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, TenantActivity.class);
        this.startActivity(intent);
    }

    /**
     * 跳转至我要合租界面，里面包含合租信息以及租客交流
     * @param v
     */
    private void openShare(View v){
        Intent intent=new Intent();
        intent.setClass(MainActivity.this, ShareActivity.class);
        this.startActivity(intent);
    }

    /**
     * 跳转至我是房主界面，个人资料上传，房源信息上传
     * @param v
     */
    private void openHouseOwner(View v){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, HouseOwnerActivity.class);
        this.startActivity(intent);
    }

    /**
     * 跳转至个人资料界面
     * @param v
     */
    private void openIndividual(View v){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, UserInfoActivity.class);
        Bundle bundle2=new Bundle();
        bundle2.putString("phone",phoneno);
        //System.out.println("看这儿看着！"+phoneno);
        intent.putExtras(bundle2);
        this.startActivity(intent);
    }

    /**
     * 跳转至关于我们界面
     * @param v
     */
    private void openAboutUs(View v){
        Intent intent=new Intent();
        intent.setClass(MainActivity.this, AboutUsActivity.class);
        this.startActivity(intent);
    }

    /**
     * 点击两次返回退出app
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出搬家日", Toast.LENGTH_SHORT).show();
                //System.currentTimeMillis()系统当前时间
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==101) {
            bundle=data.getExtras();
            phoneno=bundle.getString("phoneno");
            //phoneno = data.getStringExtra("phoneno");
        }
    }
}
