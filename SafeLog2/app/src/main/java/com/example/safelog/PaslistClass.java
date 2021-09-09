package com.example.safelog;

import java.io.Serializable;

public class PaslistClass implements Serializable {

    int pasid;
    String title;
    int grpid;
    int color;
    String pastype;
    String username;
    String passwrd;

    public PaslistClass(int pasid, String title, int grpid, int color,String pastype,String username,String passwrd) {
        this.pasid = pasid;
        this.title = title;
        this.grpid = grpid;
        this.color = color;
        this.pastype =pastype;
        this.username = username;
        this.passwrd = passwrd;
    }

}
