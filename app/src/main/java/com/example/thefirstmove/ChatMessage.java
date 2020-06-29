package com.example.thefirstmove;

import java.util.Date;
public class ChatMessage {
    private String name;// 名字
    private String message;//聊天信息
    private Type type;// 类型
    private Date date;// 时间

    public ChatMessage() {

    }
    public ChatMessage(String message, Type type, Date date) {
        super();
        this.message = message;
        this.type = type;
        this.date = date;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public enum Type {
        INCOUNT, OUTCOUNT
    }
}