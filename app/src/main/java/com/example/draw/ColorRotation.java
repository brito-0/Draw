package com.example.draw;

import android.content.Context;

import androidx.core.content.ContextCompat;

public class ColorRotation
{

//    single click > moves to a different colour
//    double click > changes the direction of movement
//    triple click > moves to the first colour (black)
//    hold         > show the colours in their original order


    private static CRColor[] cRot;

    private static String allColors, allColorsReversed;

    private static boolean isReversed;

    private static int i;

    ColorRotation(Context c)
    {
        cRot = new CRColor[]
                {
                        new CRColor("Black",ContextCompat.getColor(c,R.color.paint_black)),
                        new CRColor("Red",ContextCompat.getColor(c,R.color.paint_red)),
                        new CRColor("Orange",ContextCompat.getColor(c,R.color.paint_orange)),
                        new CRColor("Yellow",ContextCompat.getColor(c,R.color.paint_yellow)),
                        new CRColor("Green",ContextCompat.getColor(c,R.color.paint_green)),
                        new CRColor("Blue",ContextCompat.getColor(c,R.color.paint_blue)),
                        new CRColor("Pink",ContextCompat.getColor(c,R.color.paint_pink))
                };

        setAllColors();

        isReversed = false;
        i = 0;
    }

    public CRColor getCurrent() { return cRot[i]; }

    public final int getPosition() { return i; }

    public CRColor incrementColor()
    {
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

    public String getAllColors() { return isReversed ? allColorsReversed : allColors; }

    private void setAllColors()
    {
        final StringBuilder ac = new StringBuilder("|"), acr = new StringBuilder("| <");
        for (final CRColor c : cRot)
        {
            ac.append(' ').append(c.getName()).append(" >");
            acr.append("< ").append(c.getName()).append(' ');
        }

        allColors = ac.append("> |").toString();
        allColorsReversed = acr.append("|").toString();
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
