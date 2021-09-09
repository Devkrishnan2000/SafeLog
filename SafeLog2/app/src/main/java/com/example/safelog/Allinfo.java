package com.example.safelog;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Allinfo  implements Parcelable {

    List<GroupModelClass> grouplist;
    List<PaslistClass> paslist;

    public Allinfo()
    {

    }

    protected Allinfo(Parcel in) {

          grouplist = new ArrayList<>();
          paslist = new ArrayList<>();
            in.readList(grouplist,GroupModelClass.class.getClassLoader());
            in.readList(paslist,PaslistClass.class.getClassLoader());

    }

    public static final Creator<Allinfo> CREATOR = new Creator<Allinfo>() {
        @Override
        public Allinfo createFromParcel(Parcel in) {
            return new Allinfo(in);
        }

        @Override
        public Allinfo[] newArray(int size) {
            return new Allinfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {


          parcel.writeList(grouplist);
          parcel.writeList(paslist);
    }
}
