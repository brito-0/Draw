package com.example.draw;

import android.content.Context;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class ColorRotation
{
    private final List<CRColor> cRot;

    final int N;
    int i;

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
        i = 0;
    }

    public CRColor getCurrent() { return cRot.get(i); }

    public CRColor incrementColor()
    {
        if (++i == N) i = 0;
        return cRot.get(i);
    }

    public CRColor resetColor()
    {
        i = 0;
        return cRot.get(i);
    }


    public static class CRColor
    {
        private final String name;
        private final int colorNum;

        CRColor(final String _name, final int _colorNum)
        {
            this.name = _name;
            this.colorNum = _colorNum;
        }

        public String getName() { return name; }

        public int getColorNum() { return colorNum; }
    }
}
