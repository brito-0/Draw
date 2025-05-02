package com.example.draw;

import android.content.Context;

import androidx.core.content.ContextCompat;

public class ColorRotation
{
    /**
     * single click > moves to a different colour
     * double click > changes the direction of movement
     * triple click > moves to the first colour (black)
     * hold         > show the colours in their original order
     *
     * add more colours and change the order
     * */


    private static CRColor[] cRot;

    private static String allColors;

    private boolean isReversed;

    private int i;

    ColorRotation(Context c)
    {
        cRot = new CRColor[]
                {
                        new CRColor("Black",ContextCompat.getColor(c,R.color.paint_black)),
                        new CRColor("Blue",ContextCompat.getColor(c,R.color.paint_blue)),
                        new CRColor("Red",ContextCompat.getColor(c,R.color.paint_red)),
                        new CRColor("Yellow",ContextCompat.getColor(c,R.color.paint_yellow)),
                        new CRColor("Orange",ContextCompat.getColor(c,R.color.paint_orange)),
                        new CRColor("Green",ContextCompat.getColor(c,R.color.paint_green))
                };

        setAllColors();

        isReversed = false;
        i = 0;
    }

    public CRColor getCurrent() { return cRot[i]; }

    public final int getPosition() { return i; }

    public CRColor incrementColor()
    {
//        if (++i == cRot.length) i = 0;

        i += isReversed ? -1 : 1;
        if (i == cRot.length) i = 0;
        else if (i == -1) i = cRot.length-1;

        return cRot[i];
    }

    public CRColor resetColor()
    {
        i = 0;
        return cRot[i];
    }

    public void reverseOrder() { isReversed = !isReversed; }

    public String getAllColors() { return allColors; }

    private void setAllColors()
    {
        final StringBuilder sb = new StringBuilder("|");
        for (final CRColor c : cRot)
            sb.append(' ').append(c.getName()).append(" >");
        sb.append("> |");
        allColors = sb.toString();
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
