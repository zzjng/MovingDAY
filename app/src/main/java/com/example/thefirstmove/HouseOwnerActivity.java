package com.example.thefirstmove;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

class HouseOwnerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_owner);
    }

    /**
     * 检查权限
     */

    public void click(View view)  {
        /**
         * 检查App是否有相机权限，没有就申请权限
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){

            //Toast.makeText(MainActivity.this,"您申请了动态权限",Toast.LENGTH_SHORT).show();
            //如果有了相机的权限有调用相机
            startCamera();

        }else{
            //否则去请求相机权限
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},100);

        }

    }

//    请求回来的结果会在这个方法中 返回
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            //如果requestCode为100 就走这里
            case 100:

                //permissions[0].equals(Manifest.permission.CAMERA)
                //grantResults[0] == PackageManager.PERMISSION_GRANTED
                //上面的俩个判断可有可无
                if (permissions[0].equals(Manifest.permission.CAMERA)){

                    if (grantResults[0]  == PackageManager.PERMISSION_GRANTED){
                        //如果用户同意了再去打开相机
                        Toast.makeText(HouseOwnerActivity.this,"非常感谢您的同意,再会",Toast.LENGTH_SHORT).show();
                        startCamera();

                    }else{
                        //因为第一次的对话框是系统提供的 从这以后系统不会自动弹出对话框 我们需要自己弹出一个对话框
                        //进行询问的工作
                        startAlertDiaLog();

                    }

                }
                break;
        }

    }
//    创建一个AlertDiaLog 引导用户打开相机权限
public void startAlertDiaLog(){

    AlertDialog.Builder alert = new AlertDialog.Builder(HouseOwnerActivity.this);

    alert.setTitle("说明");
    alert.setMessage("需要相机权限 去拍照");
    alert.setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            //这里的立即开启 是打开手机的设置页面(打开相机权限)
            startSetting();

        }
    });
    alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //如果用户还不打开 只能等用户下次点击时再次询问
            Toast.makeText(HouseOwnerActivity.this,"当您点击我们会再次询问",Toast.LENGTH_SHORT).show();

        }
    });
    alert.create();
    alert.show();

}
//打开手机的设置页面 让用户自己打开相机权限
public void startSetting(){

    Intent intent = new Intent();
    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    Uri uri = Uri.fromParts("package",getPackageName(),null);
    intent.setData(uri);
    startActivityForResult(intent,10);

}
//当用户从设置页面回来的时候 还需要去 查询用户是否已经开启了 相机权限
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == 10  && resultCode == RESULT_OK){
        if (ContextCompat.checkSelfPermission(HouseOwnerActivity.this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(HouseOwnerActivity.this,"非常感谢您的同意",Toast.LENGTH_SHORT).show();
        }else{

        }
    }
}
//打开相机
public void startCamera(){
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    startActivityForResult(intent, 0);

}




}
