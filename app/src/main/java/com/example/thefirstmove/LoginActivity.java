package com.example.thefirstmove;
/**
 *登录界面
 * 需要用户名、密码
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thefirstmove.dao.DBOpenHelper;
import com.example.thefirstmove.util.pubFun;

public class LoginActivity extends AppCompatActivity {

    private EditText editPhone;
    private EditText editPwd;
    private Button btnLogin;
//数据连接
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        editPhone = findViewById(R.id.editPhone);
        editPwd = findViewById(R.id.editPwd);
        btnLogin = findViewById(R.id.btnLogin);

    }

    /**
     * login event
     *
     * @param v
     */

        public void OnMyLoginClick(View v) {
        if (pubFun.isEmpty(editPhone.getText().toString()) || pubFun.isEmpty(editPwd.getText().toString())) {
            Toast.makeText(this, "手机号或密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }


//数据库
        //call DBOpenHelper
        DBOpenHelper helper = new DBOpenHelper(this, "movingday.db", null, 3);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.query("user_tb", null, "userID=? and pwd=?", new String[]{editPhone.getText().toString(), editPwd.getText().toString()}, null, null, null);
        if (c != null && c.getCount() >= 1) {
            String[] cols = c.getColumnNames();
            while (c.moveToNext()) {
                for (String ColumnName : cols) {
                    Log.i("info", ColumnName + ":" + c.getString(c.getColumnIndex(ColumnName)));
                }
            }
            c.close();
            db.close();

            //将登陆用户信息存储到SharedPreferences中
            SharedPreferences mySharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE); //实例化SharedPreferences对象
            SharedPreferences.Editor editor = mySharedPreferences.edit();//实例化SharedPreferences.Editor对象
            editor.putString("userID", editPhone.getText().toString()); //用putString的方法保存数据
            editor.commit(); //提交当前数据
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();

            //传用户手机号到个人空间
            String phoneno=editPhone.getText().toString();
            Intent intent=new Intent();
            Bundle bundle=new Bundle();
            bundle.putString("phoneno",phoneno);
            intent.putExtras(bundle);
            this.setResult(101,intent);

            this.finish();
        } else {
            Toast.makeText(this, "手机号或密码输入错误！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Jump to the registration interface
     *
     * @param v
     */
    public void OnMyRegistClick(View v) {
        Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
        LoginActivity.this.startActivity(intent);
    }

    /**
     * Jump to reset password interface
     *
     * @param v
     */
    public void OnMyResPwdClick(View v) {
        Intent intent = new Intent(LoginActivity.this, ResPwdActivity.class);
        LoginActivity.this.startActivity(intent);
    }
}
