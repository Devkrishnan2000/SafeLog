package com.example.safelog;

public class BlockModelClass {
    int imageid;
    int colorid;

    public BlockModelClass(int imageid,int colorid) {
        this.imageid = imageid;
        this.colorid =colorid;
    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public int getColorid() {
        return colorid;
    }

    public void setColorid(int colorid) {
        this.colorid = colorid;
    }

}
