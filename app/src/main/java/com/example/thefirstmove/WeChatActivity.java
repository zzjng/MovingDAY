package com.example.thefirstmove;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeChatActivity extends AppCompatActivity implements View.OnClickListener {

    private Button BtnSend;
    private EditText InputBox;
    private  TextView textname;
    private List<WeChatMessage> mData = new ArrayList<>();
    private WeChatAdapter mAdapter;
     boolean isRunning=false;

    private Socket socketSend;
    private String ip="172.26.203.250";
    private String port="8086";
    DataInputStream dis;
    DataOutputStream dos;

    private String timMsg;//时间消息类型
    private String recMsg;//接收消息类型
    private boolean isSend=false;//发送检测
    private ListView mListView;

    /**
     * 由于Handler运行在主线程中(UI线程中)，它与子线程可以通过Message对象来传递数据，这个时候，
     * Handler就承担着接受子线程传过来的(子线程用sedMessage()方法传弟)Message对象，(里面包含数据)  , 把这些消息放入主线程队列中，配合主线程进行更新UI。
     */
    private Handler handler = new Handler(Looper.myLooper()){//获取当前进程的Looper对象传给handler
        @Override
        public void handleMessage(@NonNull Message msg){//?
            //获取到命令，进行命令分支
            int handi=msg.arg1;
            switch (handi){
                case 0:
                    String ormsg=(String)msg.obj;
                    disSocket();//断开网络
                    Toast.makeText(WeChatActivity.this,"发生错误=>："+ormsg,Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(WeChatActivity.this,"连接成功",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    //收到数据

                    WeChatMessage message = new WeChatMessage(WeChatMessage.MessageType_From,recMsg);//添加新数据接受者的信息类型
                    mData.add(message);
                    break;
                case  3:
                    //收到时间
                    message = new WeChatMessage(WeChatMessage.MessageType_Time,timMsg);//添加新时间的信息类型
                    mData.add(message);
                    break;
                default:break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat1);

        InputBox=(EditText)findViewById(R.id.InputBox);
        textname=(TextView)findViewById(R.id.textname);
        BtnSend=(Button)findViewById(R.id.BtnSend);
        Intent intent =getIntent();
        //getXxxExtra方法获取Intent传递过来的数据用户名
        String msg=intent.getStringExtra("name");
        textname.setText(msg);

        BtnSend.setOnClickListener(this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {//执行UI更新操作

                LinearLayoutManager layoutManager = new LinearLayoutManager(WeChatActivity.this);
                mListView = (ListView) findViewById(R.id.MainList);

                mAdapter = new WeChatAdapter(WeChatActivity.this, mData);
                mListView.setAdapter(mAdapter);
                mListView.smoothScrollToPositionFromTop(mData.size(), 0);
            }
        });

        /**
         * Thread线程对象，通常写成一个类形式（如class ThreadTest implements Runnable），在run()方法中操作数据，并把数据handler.sendMessage（）方法传输到handler对象中，并开启线程。
         */
        new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    if((socketSend = new Socket(ip,Integer.parseInt(port)))==null){
                        Log.d("ttw","发送了一条消息1");
                    }
                    else{
                        isRunning = true;
                        Log.d("ttw","发送了一条消息2");
                        dis = new DataInputStream(socketSend.getInputStream());
                        dos = new DataOutputStream(socketSend.getOutputStream());
                        new Thread(new Time(),"时间线程").start();
                        new Thread(new receive(),"接收线程").start();
                        new Thread(new Send(),"发送线程").start();
                        Message message=new Message();
                        message.arg1=1;
                        handler.sendMessage(message);
                    }
//                    socketSend = new Socket(ip, Integer.parseInt(port));
//                        //阻塞停止，表示连接成功，发送连接成功消息
//                        Message message=new Message();
//                        message.arg1=1;
//                        handler.sendMessage(message);
//                        isRunning=true;

                }catch(Exception e) {
                    isRunning = false;
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(WeChatActivity.this, "连接服务器失败！！！", Toast.LENGTH_SHORT).show();
                    Looper.loop();

                    Message message=new Message();
                    message.arg1=0;
                    message.obj="连接服务器时异常";
                    handler.sendMessage(message);
                    try{
                        socketSend.close();
                    }catch(IOException e1){
                        e1.printStackTrace();
                    }
                    finish();
                }
//                try {
//
//                    dis = new DataInputStream(socketSend.getInputStream());
//                    dos = new DataOutputStream(socketSend.getOutputStream());
//                 } catch (IOException e) {
//                    isRunning=false;
//                    Message message=new Message();
//                    message.arg1=0;
//                    message.obj="获取输入输出流异常";
//                    handler.sendMessage(message);
//                    e.printStackTrace();
//                    return;
//                }
//
//                new Thread(new receive(), "接收线程").start();
//                new Thread(new Send(), "发送线程").start();
//
//                try{
//                        socketSend.close();
//                    }catch(IOException e1){
//                        e1.printStackTrace();
//                    }
//                    finish();
                }
        }).start();
    }


    private void disSocket(){
        //如果不为空，则断开socket
        if(socketSend !=null){
            try {
                dis.close();
                dos.close();
                socketSend.close();//关闭
                socketSend = null;
            }catch (Exception e){
                //发送连接失败异常
                isRunning=false;
                Message message=new Message();
                message.arg1=0;
                message.obj="断开连接时发生错误";
                handler.sendMessage(message);

            }
        }

    }
    @Override
    public void onClick(View v) {

        String content = InputBox.getText().toString();
        InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        //获取时间
        Calendar c=Calendar.getInstance();
        StringBuilder mBuilder=new StringBuilder();
        mBuilder.append(Integer.toString(c.get(Calendar.YEAR))+"年");
        mBuilder.append(Integer.toString(c.get(Calendar.MONTH)+1)+"月");
        mBuilder.append(Integer.toString(c.get(Calendar.DATE))+"日");
        mBuilder.append(Integer.toString(c.get(Calendar.HOUR_OF_DAY)+8)+":");
        mBuilder.append(Integer.toString(c.get(Calendar.MINUTE)));
        timMsg=mBuilder.toString();

        if(content!="")
        {

            //构造时间消息
            WeChatMessage Message=new WeChatMessage(WeChatMessage.MessageType_Time,timMsg);
            mData.add(Message);
            //构造输入消息
            WeChatMessage Message1=new WeChatMessage(WeChatMessage.MessageType_To,InputBox.getText().toString());
            mData.add(Message1);
            //构造返回消息，如果这里加入网络的功能，那么这里将变成一个网络机器人
//            Message=new WeChatMessage(WeChatMessage.MessageType_From,"暂时不在，一会回来！");
//            mData.add(Message);
            //更新数据
            mAdapter.Refresh();
        }
        mBuilder.delete(0,mBuilder.length());
        //清空输入框
        InputBox.setText("");
        //关闭输入法
        imm.hideSoftInputFromWindow(null, InputMethodManager.HIDE_IMPLICIT_ONLY);
        //滚动列表到当前消息
            isSend=true;
        mListView.smoothScrollToPositionFromTop(mData.size(), 0);

    }

    class Time implements Runnable{
        public void run(){
            timMsg="";
            recMsg="";
            try {
                recMsg = dis.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
                Message   message=new Message();
                message.arg1=0;
                message.obj="数据发送异常";
                handler.sendMessage(message);
            }
            Calendar c = Calendar.getInstance();
            StringBuilder mBuilder = new StringBuilder();
            mBuilder.append(Integer.toString(c.get(Calendar.YEAR)) + "年");
            mBuilder.append(Integer.toString(c.get(Calendar.MONTH) + 1) + "月");
            mBuilder.append(Integer.toString(c.get(Calendar.DATE)) + "日");
            mBuilder.append(Integer.toString(c.get(Calendar.HOUR_OF_DAY)) + ":");
            mBuilder.append(Integer.toString(c.get(Calendar.MINUTE)));
            timMsg = mBuilder.toString();

            while(isRunning) {
                String content = InputBox.getText().toString();
                if(recMsg!=""||content!=""){
                Message message = new Message();
                message.arg1 = 3;
                message.obj = timMsg;
                handler.sendMessage(message);
               }

            }
        }
    }


    class receive implements Runnable{
        public void run(){
            recMsg = "";
            while(isRunning){
                try {

                    recMsg = dis.readUTF();

                } catch (IOException e) {
                    System.out.println(e);
                    Message  message=new Message();
                    message.arg1=0;
                    message.obj="数据发送异常";
                    handler.sendMessage(message);

                }

                if(!TextUtils.isEmpty(recMsg)){
//发送出收到的数据
                    Message  message=new Message();
                    message.arg1=2;
                    message.obj=recMsg;
                    handler.sendMessage(message);
                }
                }
            }
        }


    class Send implements Runnable {
        @Override
        public void run() {
            while (isRunning) {
                String content = InputBox.getText().toString();

                if (!"".equals(content) && isSend) {
//                        //获取时间
//                        Calendar c=Calendar.getInstance();
//                        StringBuilder mBuilder=new StringBuilder();
//                        mBuilder.append(Integer.toString(c.get(Calendar.YEAR))+"年");
//                        mBuilder.append(Integer.toString(c.get(Calendar.MONTH))+"月");
//                        mBuilder.append(Integer.toString(c.get(Calendar.DATE))+"日");
//                        mBuilder.append(Integer.toString(c.get(Calendar.HOUR_OF_DAY))+":");
//                        mBuilder.append(Integer.toString(c.get(Calendar.MINUTE)));
//                        //构造时间消息
////                      WeChatMessage Message=new WeChatMessage(WeChatMessage.MessageType_Time,mBuilder.toString());
////                        mData.add(Message);
//
//                    timMsg=mBuilder.toString();

                    try {
//                        dos.writeUTF(mBuilder.toString());
                        dos.writeUTF(content);
//                        mBuilder.delete(0, mBuilder.length());
                        Log.d("ttw", "发送了一条消息");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isSend = false;
                    InputBox.setText("");
                }
            }
        }

    }



}



