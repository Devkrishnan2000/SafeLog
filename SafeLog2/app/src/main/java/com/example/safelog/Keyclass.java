package com.example.safelog;

import android.util.Log;

public class Keyclass {

    private static final String TAG = "dfsdf";
    static  Keyclass key = null;
    public String pascode;

    private  Keyclass(String pascode)
    {
      this.pascode = pascode;
    }


    public static  void setKey(String pascode)
    {
            key = new Keyclass(pascode);
        Log.d(TAG, "setKey: invoked");
        Log.d(TAG, "setKey: "+key.pascode);
    }

    public static Keyclass getKey()
    {
        Log.d(TAG, "getKey: "+key.pascode);
        return  key;

    }

    public  static void reset()
    {
        key.pascode = null;
    }
}
