package com.example.thefirstmove;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WeChatActivity extends AppCompatActivity {

    private Button BtnSend;
    private EditText InputBox;
    private List<WeChatMessage> mData;
    private WeChatAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat1);
        final ListView mListView=(ListView)findViewById(R.id.MainList);
        mData=LoadData();
        mAdapter=new WeChatAdapter(this, mData);
        mListView.setAdapter(mAdapter);
        mListView.smoothScrollToPositionFromTop(mData.size(), 0);
        InputBox=(EditText)findViewById(R.id.InputBox);
        BtnSend=(Button)findViewById(R.id.BtnSend);
        BtnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                if(InputBox.getText().toString()!="")
                {
                    //获取时间
                    Calendar c=Calendar.getInstance();
                    StringBuilder mBuilder=new StringBuilder();
                    mBuilder.append(Integer.toString(c.get(Calendar.YEAR))+"年");
                    mBuilder.append(Integer.toString(c.get(Calendar.MONTH))+"月");
                    mBuilder.append(Integer.toString(c.get(Calendar.DATE))+"日");
                    mBuilder.append(Integer.toString(c.get(Calendar.HOUR_OF_DAY))+":");
                    mBuilder.append(Integer.toString(c.get(Calendar.MINUTE)));
                    //构造时间消息
                    WeChatMessage Message=new WeChatMessage(WeChatMessage.MessageType_Time,mBuilder.toString());
                    mData.add(Message);
                    //构造输入消息
                    Message=new WeChatMessage(WeChatMessage.MessageType_To,InputBox.getText().toString());
                    mData.add(Message);
                    //构造返回消息，如果这里加入网络的功能，那么这里将变成一个网络机器人
                    Message=new WeChatMessage(WeChatMessage.MessageType_From,"暂时不在，一会回来！");
                    mData.add(Message);
                    //更新数据
                    mAdapter.Refresh();
                }
                //清空输入框
                InputBox.setText("");
                //关闭输入法
                imm.hideSoftInputFromWindow(null, InputMethodManager.HIDE_IMPLICIT_ONLY);
                //滚动列表到当前消息
                mListView.smoothScrollToPositionFromTop(mData.size(), 0);
            }
        });
    }

    private List<WeChatMessage> LoadData()
    {
        List<WeChatMessage> Messages=new ArrayList<WeChatMessage>();

        WeChatMessage Message=new WeChatMessage(WeChatMessage.MessageType_Time,"2020年7月10日");
        Messages.add(Message);

        Message=new WeChatMessage(WeChatMessage.MessageType_Time,"19：25");
        Messages.add(Message);

        Message=new WeChatMessage(WeChatMessage.MessageType_From,"这是你做的Android程序吗？");
        Messages.add(Message);

        Message=new WeChatMessage(WeChatMessage.MessageType_To,"是的，这是一个仿微信的聊天界面");
        Messages.add(Message);

        Message=new WeChatMessage(WeChatMessage.MessageType_From,"好吧，可是怎么发图片啊");
        Messages.add(Message);

        Message=new WeChatMessage(WeChatMessage.MessageType_To,"很简单啊，你继续定义一种布局类型，然后再写一个布局就可以了");
        Messages.add(Message);
        return Messages;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

}


