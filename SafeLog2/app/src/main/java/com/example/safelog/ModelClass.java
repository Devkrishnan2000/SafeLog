package com.example.safelog;

public class ModelClass {
    String heading;
    int pos;

    public ModelClass(String heading,int pos)
    {
        this.heading = heading;
        this.pos = pos;
    }

    public String Heading()
    {
        return heading;
    }

    @Override
    public String toString()
    {
        return heading;
    }
}
