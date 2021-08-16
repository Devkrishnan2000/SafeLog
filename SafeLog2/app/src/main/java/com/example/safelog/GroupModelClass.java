package com.example.safelog;

import androidx.annotation.NonNull;

public class GroupModelClass {
    int id;
    String grpname;

    public GroupModelClass(int id, String grpname) {
        this.id = id;
        this.grpname = grpname;
    }
    public GroupModelClass()
    {

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGrpname(String grpname) {
        this.grpname = grpname;
    }

    public int getId() {
        return id;
    }

    public String getGrpname() {
        return grpname;
    }

    @NonNull
   @Override
    public String toString()
   {
       return grpname;
   }
}
