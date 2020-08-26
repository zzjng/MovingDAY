package com.example.thefirstmove;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thefirstmove.dao.DBOpenHelper;
import com.example.thefirstmove.util.pubFun;
import com.mob.MobSDK;

import java.util.ArrayList;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ResPwdActivity extends AppCompatActivity {
    private EditText editPhone;
    private EditText editPwd;
    private EditText etcode;
    private EditText editResPwd;
    private Button btnConfirm;
    private Button btgetcode;

    private final String TAG="ResPwdActivity";
    //控制按钮样式是否改变
    private boolean tag = true;
    //每次验证请求需要间隔60S
    private int i=60;
//短信验证

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.res_password);
        editPhone = findViewById(R.id.editPhone);
        editPwd = findViewById(R.id.editPwd);
        editResPwd = findViewById(R.id.editResPwd);
        btnConfirm = findViewById(R.id.btnConfirm);
        etcode=findViewById(R.id.etcode);
        btgetcode=findViewById(R.id.bt_getCode);

//              用户隐私授权一致true
        MobSDK.submitPolicyGrantResult(true, null);
        //SDK操作回调


        if (Build.VERSION.SDK_INT >= 23) {
            int readPhone = checkSelfPermission("android.permission.READ_PHONE_STATE");
            int receiveSms = checkSelfPermission("android.permission.RECEIVE_SMS");
            int readContacts = checkSelfPermission("android.permission.READ_CONTACTS");
            int readSdcard = checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE");

            int requestCode = 0;
            final ArrayList<String> permissions = new ArrayList<String>();
            if (readPhone != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 0;
                permissions.add("android.permission.READ_PHONE_STATE");
            }
            if (receiveSms != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 1;
                permissions.add("android.permission.RECEIVE_SMS");
            }
            if (readContacts != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 2;
                permissions.add("android.permission.READ_CONTACTS");
            }
            if (readSdcard != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 3;
                permissions.add("android.permission.READ_EXTERNAL_STORAGE");
            }
            if (requestCode > 0) {
                String[] permission = new String[permissions.size()];
                this.requestPermissions(permissions.toArray(permission), requestCode);
                return;
            }
        }

//注册一个事件回调监听，用于处理SMSSDK接口请求的结果
        SMSSDK.registerEventHandler(eh);
        //使用完EventHandler需注销，否则可能出现内存泄漏
        SMSSDK.unregisterEventHandler(eh);
    }

    /**
     * code event
     */
    public void getCodeClick(View v) {
        //获取验证码操作
        String phone=((EditText)findViewById(R.id.editPhone)).getText().toString().trim();
        if(phone.equals("")){
            Toast.makeText(ResPwdActivity.this,"手机号不能为空",Toast.LENGTH_SHORT).show();
            return;
        }else{
            //填写了手机号码,isMobileNO(phone)判断号码格式
            if(isMobileNO(phone)){
                //如果手机号码无误，则发送验证请求
                SMSSDK.getVerificationCode("86", phone);
//                btnCode.setClickable(true);
                //让按钮的样式变成60S倒计时
                changeBtnGetCode();
            }else{
                //手机号格式有误
                Toast.makeText(ResPwdActivity.this,"手机号格式错误，请检查",Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    /**
     * confirm event
     * @param v
     */
    public void OnMyConfirmClick(View v) {
        confirmInfo();
    }

    /**
     * confirm event
     */
    private void confirmInfo() {
        if(pubFun.isEmpty(editPhone.getText().toString()) || pubFun.isEmpty(editPwd.getText().toString()) || pubFun.isEmpty(editResPwd.getText().toString())){
            Toast.makeText(this, "手机号或密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!editPwd.getText().toString().equals(editResPwd.getText().toString())){
            Toast.makeText(this, "输入密码不正确！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etcode.getText() == null || etcode.getText().toString().trim().equals("")){
            Toast.makeText(ResPwdActivity.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        //调用DBOpenHelper
        DBOpenHelper helper = new DBOpenHelper(this,"movingday.db",null,3);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.query("user_tb",null,"userID=?",new String[]{editPhone.getText().toString()},null,null,null);
        if(c!=null && c.getCount() >= 1){
            //填写了验证码，进行验证,SMSSDK自带的方法
            SMSSDK.submitVerificationCode("86", editPhone.getText().toString(), etcode.getText().toString());
            ContentValues cv = new ContentValues();
            cv.put("pwd", editPwd.getText().toString());
            String[] args = {String.valueOf(editPhone.getText().toString())};
            long rowid = db.update("user_tb", cv, "userID=?",args);
            c.close();
            db.close();
            Toast.makeText(this, "密码重置成功！", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        else{
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("该用户不存在，请到注册界面进行注册！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            setResult(RESULT_OK);
                            Intent intent=new Intent(ResPwdActivity.this,RegistActivity.class);
                            ResPwdActivity.this.startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            return;
                        }
                    })
                    .show();
        }
    }


    /**
     * 改变按钮样式
     */
    private void changeBtnGetCode() {


        Thread thread = new Thread() {
            @Override
            public void run() {

                if (tag) {
                    while (i > 0) {
                        i--;
                        //如果活动为空
                        if (ResPwdActivity.this == null) {
                            break;
                        }

                        String ti= Integer.toString(i);
                        ResPwdActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btgetcode.setText(ti);
                                btgetcode.setClickable(false);
                            }
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    tag = false;
                }
                i = 60;
                tag = true;

                if (ResPwdActivity.this != null) {
                    ResPwdActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btgetcode.setText("获取验证码");
                            btgetcode.setClickable(true);
                        }
                    });
                }
            }
        };
        thread.start();
    }

    private boolean isMobileNO(String phone) {
       /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(phone)) return false;
        else return phone.matches(telRegex);
    }


    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg == null || msg.obj == null){
                return;
            }
            Toast.makeText(ResPwdActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "handleMessage: "+msg.obj.toString() );

        }
    };
    EventHandler eh=new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object data) {
            // TODO 此处不可直接处理UI线程，处理后续操作需传到主线程中操作
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                if(result == SMSSDK.RESULT_COMPLETE) {
                    boolean smart = (Boolean)data;
                    Log.e("TAG", "afterEvent:22 "+  SMSSDK.getGroupedCountryList().toString());
                    if(smart) {
                        //通过Mob云验证
                        Log.e(TAG, "afterEvent: "+"通过Mob云验证" );
                    } else {
                        //依然走短信验证
                        Log.e(TAG, "afterEvent: "+"依然走短信验证" );

                    }
                }
            }

            mHandler.sendMessage(msg);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }
}