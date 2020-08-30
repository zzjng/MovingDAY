package com.example.thefirstmove.bean;

public class AaaBean { //动态的aaatable实体类
    String nick;
    String address;
    String description;
    String id;
    byte[] img;
    public AaaBean(){
    }
    /*public AaaBean(String nick,String address,String description,String  id,byte[] img){
        this.nick=nick;
        this.address=address;
        this.description=description;
        this.id=id;
        this.img=img;
    }*/
    public AaaBean(String nick,String address,String description,String  id){
        this.nick=nick;
        this.address=address;
        this.description=description;
        this.id=id;
    }

    public String getNick() {
        return nick;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getId(){
        return id;
    }

    public byte[] getImg(){
        return img;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImg(byte[] img){
        this.img = img;
    }
}