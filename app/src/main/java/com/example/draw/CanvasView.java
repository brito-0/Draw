package com.example.draw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;


public class CanvasView extends View implements View.OnTouchListener
{
    private final Paint paintOld = new Paint(), paint = new Paint();

    private final List<Drawing> drawings = new ArrayList<>();
    private Drawing current;

    // 15.f
    private static final float paintSize = 16.f;

    @SuppressLint("ClickableViewAccessibility")
    public CanvasView(Context context)
    {
        super(context);

        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        paintOld.setColor(Color.RED);
        paintOld.setStrokeWidth(paintSize);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(paintSize);

        current = new Drawing();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        current.draw(canvas,paintOld);
        for (Drawing d : drawings) d.draw(canvas,paint);

        Log.d("Draws","0");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
//            if (current.size() > 1) drawings.add(current);
            drawings.add(current);
            current = new Drawing();

            invalidate();

            Log.d("Drawings",String.valueOf(drawings.size()));

            return true;
        }

        CPoint p = new CPoint();
        p.x = event.getX();
        p.y = event.getY();
        current.addPoint(p);
        invalidate();
        return true;
    }

    public void clearDrawings()
    {
        current = new Drawing();
        drawings.clear();
        invalidate();
    }


    private static class CPoint
    {
        protected float x,y;
    }

    private static class Drawing
    {
        private final List<CPoint> points = new ArrayList<>();

        protected void addPoint(final CPoint point) { points.add(point); }

        protected void draw(@NonNull Canvas canvas, Paint paint)
        {
            final int n = points.size();
            if (n == 1)
            {
                canvas.drawCircle(points.get(0).x,points.get(0).y,10.f,paint);
                return;
            }

            for (int i = 1; i < n; i++)
                canvas.drawLine(points.get(i-1).x,points.get(i-1).y,points.get(i).x,points.get(i).y,paint);
        }

        protected int size() { return points.size(); }
    }
}
