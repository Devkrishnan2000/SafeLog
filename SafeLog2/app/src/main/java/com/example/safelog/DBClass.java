package com.example.safelog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import  net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBClass extends SQLiteOpenHelper {
    public static final String GID_COLUMN = "COLUMN_GID";
    public static final String GNAME_COLUMN = "COLUMN_GNAME";
    public static final String GROUP_TABLE = "GROUP_TABLE";
    public String pascode =Keyclass.getKey().pascode;// for test purposes dev@123
    public static final String PASSWORD_TABLE = "PASSWORD_TABLE";
    public static final String PID_COLUMN = "PID_COLUMN";
    public static final String PNAME_COLUMN = "PNAME_COLUMN";
    public static final String GID_COLUMNP = "GID_COLUMNP";
    public static final String COLOUR_COLUMN = "COLOUR_COLUMN";
    public static final String PASTYPE_COLUMN = "PASTYPE_COLUMN";
    public static final String USERNAME_COLUMN = "USERNAME_COLUMN";
    public static final String PASSWORD_COLUMN = "PASSWORD_COLUMN";
    private static final String TAG ="sf" ;

    public DBClass(@Nullable Context context) {
        super(context, "SafeLog.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String  creatgrp = "CREATE TABLE " + GROUP_TABLE + " ( " + GID_COLUMN + " INTEGER PRIMARY KEY, AUTO_INCREMENT," + GNAME_COLUMN + " TEXT );";
        sqLiteDatabase.execSQL(creatgrp);
        String paswrd = "CREATE TABLE " + PASSWORD_TABLE + " ( " + PID_COLUMN + " INTEGER PRIMARY KEY, AUTO_INCREMENT, " + PNAME_COLUMN + " TEXT, " + GID_COLUMNP + " INTEGER," + COLOUR_COLUMN + " INTEGER," + PASTYPE_COLUMN + " TEXT," + USERNAME_COLUMN + " TEXT," + PASSWORD_COLUMN + " TEXT) ";
        sqLiteDatabase.execSQL(paswrd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void setpasword()
    {
        SQLiteDatabase db = this.getWritableDatabase(pascode);
        db.close();
    }

    public boolean checkpasword()
    {
        boolean correct=true;
        SQLiteDatabase db;
        try{
            db = this.getReadableDatabase(pascode);
        }
        catch (Exception e)
        {
            correct=false;
        }

        return correct;
    }

    public void insertGroup(GroupModelClass groupModelClass)
    {
        SQLiteDatabase db = this.getWritableDatabase(pascode);
        ContentValues values = new ContentValues();
        values.put(GNAME_COLUMN,groupModelClass.grpname);
        db.insert(GROUP_TABLE,null,values);
        db.close();

    }

    public Allinfo getallinfo()
    {
        Allinfo allinfo = new Allinfo();
        List<GroupModelClass> groupModelClassList = new ArrayList<>();
        List<PaslistClass> paslistClassList = new ArrayList<>();

        try{

            SQLiteDatabase db = this.getReadableDatabase(pascode);
            String group_query = "Select * from "+GROUP_TABLE;
            Cursor group_cursor= db.rawQuery(group_query,null);
            if(group_cursor.moveToFirst())
            {
                do{
                    int id = group_cursor.getInt(0);
                    String grpname = group_cursor.getString(2);
                    GroupModelClass groupModelClass = new GroupModelClass(id,grpname);
                    groupModelClassList.add(groupModelClass);
                }while (group_cursor.moveToNext());
            }
            group_cursor.close();

            String pass_query = "select * from "+PASSWORD_TABLE;
            Cursor pass_cursor = db.rawQuery(pass_query,null);

            if(pass_cursor.moveToFirst())
            {
                do{
                    int pasid = pass_cursor.getInt(0);
                    String pasname = pass_cursor.getString(2);
                    int grpid = pass_cursor.getInt(3);
                    int color = pass_cursor.getInt(4);
                    String pastype = pass_cursor.getString(5);
                    String usrname = pass_cursor.getString(6);
                    String paswrd = pass_cursor.getString(7);
                    PaslistClass paslistClass = new PaslistClass(pasid,pasname,grpid,color,pastype,usrname,paswrd);
                    paslistClassList.add(paslistClass);

                }while(pass_cursor.moveToNext());
            }

            pass_cursor.close();
            db.close();

            allinfo.grouplist = groupModelClassList;
            allinfo.paslist = paslistClassList;
            allinfo.checkfail =false;
        }
        catch (Exception ex)
        {
            allinfo.checkfail =true;
        }


      return allinfo;
    }





    public void delGroup(int groupid)
    {
        SQLiteDatabase db = this.getWritableDatabase(pascode);
        String query = "DELETE FROM "+GROUP_TABLE+" WHERE "+GID_COLUMN+" = "+groupid+"";
        Cursor cursor=  db.rawQuery(query,null);
        if(cursor.moveToFirst())
        { }
        else
        { }
        cursor.close();
        db.close();
    }

    public void renameGroup(String newgrpname,ModelClass curentgrp)
    {
        SQLiteDatabase db = this.getWritableDatabase(pascode);
        String query ="UPDATE "+GROUP_TABLE+" SET "+GNAME_COLUMN+" = '"+newgrpname+"' WHERE "+GID_COLUMN+" ="+curentgrp.pos+"";
       Cursor cursor= db.rawQuery(query,null);
        if(cursor.moveToFirst())
        {

        }
        else {}
        cursor.close();
        db.close();
    }

    public void insertPass(PassModelClass passModelClass)
    {
        SQLiteDatabase db = getWritableDatabase(pascode);
        ContentValues values = new ContentValues();
        values.put(PNAME_COLUMN,passModelClass.pastitle);
        values.put(GID_COLUMNP,passModelClass.grpidp);
        values.put(COLOUR_COLUMN,passModelClass.col);
        values.put(PASTYPE_COLUMN,passModelClass.pastype);
        values.put(USERNAME_COLUMN,passModelClass.usrname);
        values.put(PASSWORD_COLUMN,passModelClass.paswrd);
        db.insert(PASSWORD_TABLE,null,values);
        db.close();
    }



    public void deletegrppass(int grpid)
    {
        SQLiteDatabase db = getWritableDatabase(pascode);
        String query = "DELETE FROM "+PASSWORD_TABLE+" WHERE "+GID_COLUMNP+" = "+grpid+"";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst())
        { }
        else
        {}
        cursor.close();
        db.close();
    }
/*
   public PaslistClass getpass(int pasid)
    {
        PaslistClass paswrddata;
        SQLiteDatabase db = getReadableDatabase(pascode);
        String query = "SELECT "+PASTYPE_COLUMN+","+PASSWORD_COLUMN+","+USERNAME_COLUMN+" FROM "+PASSWORD_TABLE+" WHERE "+PID_COLUMN+" = "+pasid+"";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            paswrddata = new PaslistClass()

            //    paswrddata.username =cursor.getString(2);
             //   paswrddata.paswrd =cursor.getString(1);
              //  paswrddata.paswtype =cursor.getString(0);

        }
        else
        {}
        cursor.close();
        db.close();
        return  paswrddata;
    }
    */



    public void delpass(int pasid)
    {
        SQLiteDatabase db = getReadableDatabase(pascode);
        String query = "DELETE FROM "+PASSWORD_TABLE+" WHERE "+PID_COLUMN+" = "+pasid+"";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst())
        {

        }
        else
        {}
        cursor.close();
        db.close();
    }

    public void editpas(int pasid, PaslistClass paswrd)
    {
        SQLiteDatabase db = getWritableDatabase(pascode);
        String query = "UPDATE "+PASSWORD_TABLE+" SET "+USERNAME_COLUMN+"='"+paswrd.username+"',"+PASSWORD_COLUMN+"= '"+paswrd.passwrd+"' WHERE "+PID_COLUMN+"= "+pasid+"";
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        cursor.close();
        db.close();
    }



}
