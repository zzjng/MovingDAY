package com.example.thefirstmove;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.thefirstmove.util.JsonParser;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ChatActivity extends Activity {
    private List<ChatMessage> list;
    private ListView listview;
    private EditText input;
    private Button send;
    private ChatMessageAdapter chatAdapter;
    private ChatMessage chatMessage = null;
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private static final String APPID = "appid=5f0abcf0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chat);
        initView();
        initListener();
        initData();
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5f0abcf0");
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

    public void startSpeechClick(View view) {
        //初始化识别无UI识别对象
        //使用SpeechRecognizer对象，可根据回调消息自定义界面；
        checkRecordPermission();
        SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(this, null);
        //设置参数
        mIat.setParameter(SpeechConstant.PARAMS, "iat");      //应用领域
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn"); //语音
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin"); //普通话
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);//引擎
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");//返回结果格式
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS,"1000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        //mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");

        //开始听写
        mIat.startListening(mRecoListener);
    }

    private RecognizerListener mRecoListener= new RecognizerListener() {

        //音量0-30
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        //开始录音
        @Override
        public void onBeginOfSpeech() {
            Toast.makeText(ChatActivity.this, "请说话",Toast.LENGTH_SHORT).show();
        }

        //结束录音
        @Override
        public void onEndOfSpeech() {
            Toast.makeText(ChatActivity.this, "识别成功",Toast.LENGTH_SHORT).show();
        }

        //返回结果
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            System.out.println(recognizerResult.getResultString());
            printResult(recognizerResult);
        }

        @Override
        public void onError(SpeechError speechError) {

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    //输出结果
    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        input.setText(resultBuffer.toString());
    }
    private void checkRecordPermission() {
        // 检查是否有定位权限
        // 检查权限的方法: ContextCompat.checkSelfPermission()两个参数分别是Context和权限名.
        // 返回PERMISSION_GRANTED是有权限，PERMISSION_DENIED没有权限
        if (ContextCompat.checkSelfPermission(ChatActivity.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            //没有权限，向系统申请该权限。
            Log.i("MY","没有权限");
            ActivityCompat.requestPermissions(ChatActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},100);
        } else {
            //已经获得权限，则执行定位请求。
           // Toast.makeText(ChatActivity.this, "已获取录音权限",Toast.LENGTH_SHORT).show();
        }
    }
}
