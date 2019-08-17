package com.example.socialcode;

public class userInfo {

    public static String name,college,email,codeforces,hackerrank;

    public userInfo(){}

    public userInfo(String name, String college, String email,String codeforces, String hackerrank) {
        this.name = name;
        this.college = college;
        this.email = email;
//        this.password = password;
        this.codeforces = codeforces;
        this.hackerrank = hackerrank;
    }

    public  String getName(){ return name; }

    public  void setName(String name){this.name = name; }

    public  String getCollege(){return college;}

    public  void setCollege(String college){this.college = college;}

    public  String getEmail(){return email;}

    public  void setEmail(String email){this.email = email;}

//    public String getPassword(){return password;}
//
//    public void setPassword(String password){this.password = password;}

    public  String getCodeforces(){return codeforces;}

    public  void setCodeforces(String codeforces){this.codeforces = codeforces;}

    public  String getHackerrank(){return hackerrank;}

    public  void setHackerrank(String hackerrank){this.hackerrank = hackerrank;}
}


