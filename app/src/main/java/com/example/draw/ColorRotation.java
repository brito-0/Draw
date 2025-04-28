package com.example.draw;

import android.content.Context;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class ColorRotation
{
    private final List<CRColor> cRot;

    final int N;
    int count;

    ColorRotation(Context c)
    {
        cRot = new ArrayList<>();
        cRot.add(new CRColor("black", ContextCompat.getColor(c,R.color.paint_black)));
        cRot.add(new CRColor("blue",ContextCompat.getColor(c,R.color.paint_blue)));
        cRot.add(new CRColor("red",ContextCompat.getColor(c,R.color.paint_red)));
        cRot.add(new CRColor("yellow",ContextCompat.getColor(c,R.color.paint_yellow)));
        cRot.add(new CRColor("orange",ContextCompat.getColor(c,R.color.paint_orange)));
        cRot.add(new CRColor("green",ContextCompat.getColor(c,R.color.paint_green)));

        N = cRot.size();
        count = 0;
    }


    // single click moves to the next colour
    // double click goes to black

    public CRColor getCurrent() { return cRot.get(count); }

    public CRColor incrementColor()
    {
        if (++count == N) count = 0;
        return cRot.get(count);
    }

    public CRColor resetColor()
    {
        count = 0;
        return cRot.get(count);
    }


    public static class CRColor
    {
        private final String name;
        private final int colourNum;

        CRColor(final String _name, final int _colourNum)
        {
            this.name = _name;
            this.colourNum = _colourNum;
        }

        public String getName() { return name; }

        public int getColourNum() { return colourNum; }
    }
}
