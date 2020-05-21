package com.example.thefirstmove;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thefirstmove.dao.DBOpenHelper;
import com.example.thefirstmove.util.pubFun;

public class ResPwdActivity extends AppCompatActivity {
    private EditText editPhone;
    private EditText editPwd;
    private EditText editResPwd;
    private Button btnConfirm;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.res_password);
        editPhone = findViewById(R.id.editPhone);
        editPwd = findViewById(R.id.editPwd);
        editResPwd = findViewById(R.id.editResPwd);
        btnConfirm = findViewById(R.id.btnConfirm);
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

        //调用DBOpenHelper
        DBOpenHelper helper = new DBOpenHelper(this,"qianbao.db",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.query("user_tb",null,"userID=?",new String[]{editPhone.getText().toString()},null,null,null);
        if(c!=null && c.getCount() >= 1){
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
}