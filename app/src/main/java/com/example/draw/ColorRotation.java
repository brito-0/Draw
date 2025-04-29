package com.example.draw;

import android.content.Context;

import androidx.core.content.ContextCompat;

public class ColorRotation
{
    private static CRColor[] cRot;

    int i;

    ColorRotation(Context c)
    {
        cRot = new CRColor[]
                {
                        new CRColor("black", ContextCompat.getColor(c,R.color.paint_black)),
                        new CRColor("blue",ContextCompat.getColor(c,R.color.paint_blue)),
                        new CRColor("red",ContextCompat.getColor(c,R.color.paint_red)),
                        new CRColor("yellow",ContextCompat.getColor(c,R.color.paint_yellow)),
                        new CRColor("orange",ContextCompat.getColor(c,R.color.paint_orange)),
                        new CRColor("green",ContextCompat.getColor(c,R.color.paint_green))
                };

        i = 0;
    }

    public CRColor getCurrent() { return cRot[i]; }

    public CRColor incrementColor()
    {
        if (++i == cRot.length) i = 0;
        return cRot[i];
    }

    public CRColor resetColor()
    {
        i = 0;
        return cRot[i];
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
