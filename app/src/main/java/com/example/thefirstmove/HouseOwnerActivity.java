package com.example.thefirstmove;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HouseOwnerActivity extends AppCompatActivity {

    private Uri imageUri;
    private ImageView cameraPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_owner);
        cameraPic=findViewById(R.id.CameraPic);
        SharedPreferences sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
        String phone=sharedPreferences.getString("userID","");

        if (phone==null){
            Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(this,"欢迎",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 检查权限
     */

    public void click(View view)  {
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

        if (requestCode == 10 && resultCode == RESULT_OK) {
            if (ContextCompat.checkSelfPermission(HouseOwnerActivity.this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(HouseOwnerActivity.this,"非常感谢您的同意",Toast.LENGTH_SHORT).show();
            }


            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            cameraPic.setImageBitmap(bitmap);
        }

}
//打开相机
public void startCamera(){
    cameraPic = findViewById(R.id.CameraPic);
    System.out.println(getExternalCacheDir());
    File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
    try {
        if (outputImage.exists()) {
            outputImage.delete();
        }
        outputImage.createNewFile();
    } catch (IOException e) {
        e.printStackTrace();
    }
    ActivityCompat.requestPermissions(HouseOwnerActivity.this, new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
    //判断版本号
    if (Build.VERSION.SDK_INT < 24) {
        imageUri = Uri.fromFile(outputImage);
    } else {
        imageUri = FileProvider.getUriForFile(HouseOwnerActivity.this,
                "com.example.thefirstmove.provider", outputImage);
    }
    /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    startActivityForResult(intent, 0);
*/
    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
    startActivityForResult(intent, 1);
}

}
