package com.example.socialcode;

public class friendInfo {

    private String uid,name,college;

    public friendInfo(){
        this.uid = "";
        this.name = "";
        this.college = "";
    }


    public friendInfo(String uid, String name, String college) {
        this.uid = uid;
        this.name = name;
        this.college = college;
    }

    public String getUid(){return uid;}

    public void setUid(String uid){this.uid = uid;}

    public String getName(){return name;}

    public void setName(String name){this.name = name;}

    public String getCollege(){return college;}

    public void setCollege(String college) {this.college = college;}


}
