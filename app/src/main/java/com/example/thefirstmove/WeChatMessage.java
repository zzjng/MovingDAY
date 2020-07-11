package com.example.thefirstmove;

public class WeChatMessage
{
    //定义3种布局类型
    public static final int MessageType_Time=0;
    public static final int MessageType_From=1;
    public static final int MessageType_To=2;

    public WeChatMessage(int Type,String Content)
    {
        this.mType=Type;
        this.mContent=Content;
    }


    //消息类型
    private int mType;
    //消息内容
    private String mContent;
    //获取类型
    public int getType() {
        return mType;
    }
    //设置类型
    public void setType(int mType) {
        this.mType = mType;
    }
    //获取内容
    public String getContent() {
        return mContent;
    }
    //设置内容
    public void setContent(String mContent) {
        this.mContent = mContent;
    }
}
