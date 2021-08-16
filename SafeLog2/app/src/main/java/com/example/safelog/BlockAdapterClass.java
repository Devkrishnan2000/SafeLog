package com.example.safelog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BlockAdapterClass extends ArrayAdapter<BlockModelClass> {

    public BlockAdapterClass(Context context, ArrayList<BlockModelClass> colourlist)
    {
        super(context,0,colourlist);
    }

    public BlockAdapterClass(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initview(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initview(position,convertView,parent);
    }

    private View initview(int position , View convertView, ViewGroup parent)
    {
        if(convertView==null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.blockspinnerdropdown,parent,false);
        }

        ImageView colouricon = convertView.findViewById(R.id.color_icon);
        BlockModelClass  currentblock = getItem(position);
        if(currentblock!=null)
        colouricon.setImageResource(currentblock.imageid);

        return  convertView;
    }
}
