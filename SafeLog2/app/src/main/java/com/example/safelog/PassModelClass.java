package com.example.safelog;

public class PassModelClass {
    String pastitle;
    String usrname;
    String paswrd;
    String pastype;
    int grpidp;
    int pasid;
    int col;

    public String getPastitle() {
        return pastitle;
    }

    public void setPastitle(String pastitle) {
        this.pastitle = pastitle;
    }

    public String getUsrname() {
        return usrname;
    }

    public void setUsrname(String usrname) {
        this.usrname = usrname;
    }

    public String getPaswrd() {
        return paswrd;
    }

    public void setPaswrd(String paswrd) {
        this.paswrd = paswrd;
    }

    public String getPastype() {
        return pastype;
    }

    public void setPastype(String pastype) {
        this.pastype = pastype;
    }

    public int getGrpidp() {
        return grpidp;
    }

    public void setGrpidp(int grpidp) {
        this.grpidp = grpidp;
    }

    public int getPasid() {
        return pasid;
    }

    public void setPasid(int pasid) {
        this.pasid = pasid;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }



    public PassModelClass(String pastitle, String usrname, String paswrd, String pastype, int grpidp, int pasid, int col) {
        this.pastitle = pastitle;
        this.usrname = usrname;
        this.paswrd = paswrd;
        this.pastype = pastype;
        this.grpidp = grpidp;
        this.pasid = pasid;
        this.col = col;
    }
}

