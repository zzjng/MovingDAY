package com.example.thefirstmove;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends Activity {
    private List<ChatMessage> list;
    private ListView listview;
    private EditText input;
    private Button send;
    private ChatMessageAdapter chatAdapter;
    private ChatMessage chatMessage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chat);
        initView();
        initListener();
        initData();
    }

    // 初始视图
    private void initView() {
        listview = (ListView) findViewById(R.id.listview);
        input = (EditText) findViewById(R.id.chat_input_message);
        send = (Button) findViewById(R.id.send);
    }

    // 设置监听事件
    private void initListener() {
        send.setOnClickListener(onClickListener);
    }

    // 初始化数据
    private void initData() {
        list = new ArrayList<ChatMessage>();
        list.add(new ChatMessage("您好,莲音为您服务!", ChatMessage.Type.INCOUNT, new Date()));
        chatAdapter = new ChatMessageAdapter(list);
        listview.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
    }

    // 发送消息聊天
    private void chat() {
        //判断是否输入内容
        final String send_message = input.getText().toString().trim();
        if (TextUtils.isEmpty(send_message)) {
            Toast.makeText(ChatActivity.this, "对不起，您还未发送任何消息",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // 记录刷新
        ChatMessage sendChatMessage = new ChatMessage();
        sendChatMessage.setMessage(send_message);
        sendChatMessage.setDate(new Date());
        sendChatMessage.setType(ChatMessage.Type.OUTCOUNT);
        list.add(sendChatMessage);
        chatAdapter.notifyDataSetChanged();
        input.setText("");
        // 发送消息去服务器端，返回数据
        new Thread() {
            public void run() {
                ChatMessage chat = HttpRequest.sendMessage(send_message);
                Message message = new Message();
                message.what = 0x1;
                message.obj = chat;
                handler.sendMessage(message);
            };
        }.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0x1) {
                if (msg.obj != null) {
                    chatMessage = (ChatMessage) msg.obj;
                }
                //更新数据
                list.add(chatMessage);
                chatAdapter.notifyDataSetChanged();
            }
        };
    };

    // 点击事件监听
    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.send:
                    chat();
                    break;
            }
        }
    };
}
