package com.example.draw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class CanvasView extends View implements View.OnTouchListener
{
    private static final Paint paint = new Paint();

    private final List<Drawing> drawings = new ArrayList<>();
    private Drawing current;// , redo = null;
    private final Stack<Drawing> redoSt = new Stack<>();
    private int N = 0;

    private static final float paintSize = 16.f;

    private Button buttonUndo, buttonRedo;

    private boolean eraseMode = false;


    @SuppressLint("ClickableViewAccessibility")
    private void initCanvas()
    {
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
//        paintOld.setColor(Color.RED);
//        paintOld.setStrokeWidth(paintSize);
//        paint.setColor(Color.BLACK);
//        paint.setStrokeWidth(paintSize);

        current = new Drawing();
    }

    public CanvasView(Context context)
    {
        super(context);

        initCanvas();
    }

    public CanvasView(Context context, Button bu, Button br)
    {
        super(context);

        initCanvas();

        buttonUndo = bu;
        buttonRedo = br;

        buttonUndo.setEnabled(false);
        buttonRedo.setEnabled(false);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        for (Drawing d : drawings) d.draw(canvas);
        current.draw(canvas);

//        for (Drawing d : drawings) d.drawTest(canvas,paint);
//        current.drawTest(canvas,paintOld);

        Log.d("Draws","0");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (!eraseMode) current.pType = Drawing.PaintType.NORMAL;

            drawingsAdd(current);
//            current = new Drawing();
            resetCurrent();

            invalidate();
            return true;
        }

//        if (redo != null) redo = null;
        if (!redoSt.isEmpty()) redoStClear();

        CPoint p = new CPoint();
        p.x = event.getX();
        p.y = event.getY();
        current.addPoint(p);
        invalidate();
        return true;
    }

    public boolean clearDrawings()
    {
        if (N == 0 && redoSt.isEmpty()) return false;

//        current = new Drawing();
        resetCurrent();

        drawingsClear();
        redoStClear();

        invalidate();
        return true;
    }

    public boolean undoDrawings()
    {
        if (N == 0) return false;

//        current = new Drawing();
        resetCurrent();

        redoStAdd(drawingsPop());

        invalidate();
        return true;
    }

//    public boolean redoDrawings()
//    {
//        if (redo == null) return false;
//
//        addDrawing(redo);
//        redo = null;
//        return true;
//    }

    public boolean redoStDrawings()
    {
        if (redoSt.isEmpty()) return false;

        drawingsAdd(redoStPop());

        invalidate();
        return true;
    }

    public void setEraseModeValue(boolean value)
    {
        eraseMode = value;
        resetCurrent();
    }

    private void drawingsAdd(final Drawing d)
    {
        drawings.add(d);
        ++N;

        buttonUndo.setEnabled(true);

        Log.d("Drawings",String.valueOf(N));
    }

    private Drawing drawingsPop()
    {
        if (N == 0) return null;

        final Drawing d = drawings.get(--N);
        drawings.remove(N);

        if (N == 0) buttonUndo.setEnabled(false);

        Log.d("Drawings",String.valueOf(N));

        return d;
    }

    private void drawingsClear()
    {
        drawings.clear();

        buttonUndo.setEnabled(false);

        Log.d("Drawings",String.valueOf(N = 0));
    }

    private void redoStAdd(final Drawing d)
    {
        redoSt.push(d);

        buttonRedo.setEnabled(true);

        Log.d("Redo",String.valueOf(redoSt.size()));
    }

    private Drawing redoStPop()
    {
        if (redoSt.isEmpty()) return null;

        final Drawing d = redoSt.pop();

        if (redoSt.isEmpty()) buttonRedo.setEnabled(false);

        Log.d("Redo",String.valueOf(redoSt.size()));

        return d;
    }

    private void redoStClear()
    {
        redoSt.clear();

        buttonRedo.setEnabled(false);

        Log.d("Redo",String.valueOf(0));
    }

    private void resetCurrent()
    {
        current = new Drawing();
        if (eraseMode) current.pType = Drawing.PaintType.ERASE;
    }

    private static void setPaintNew()
    {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(paintSize);
    }
    private static void setPaintNormal()
    {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(paintSize);
    }
    private static void setPaintErase()
    {
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(paintSize*2);
    }



    private static class CPoint
    {
        protected float x,y;
    }

    private static class Drawing
    {
        enum PaintType
        {
            NEW,
            NORMAL,
            ERASE
        }
        private PaintType pType = PaintType.NEW;

        private final List<CPoint> points = new ArrayList<>();

        protected void addPoint(final CPoint point) { points.add(point); }

        protected void draw(@NonNull Canvas canvas)
        {
            changePaintColour();

            final int n = points.size();
            if (n == 1)
            {
                canvas.drawCircle(points.get(0).x,points.get(0).y,10.f,paint);
                return;
            }

            for (int i = 1; i < n; i++)
                canvas.drawLine(points.get(i-1).x,points.get(i-1).y,points.get(i).x,points.get(i).y,paint);
        }

//        protected void drawTest(@NonNull Canvas canvas, Paint paint)
//        {
//            final int n = points.size();
//            if (n == 1)
//            {
//                canvas.drawCircle(points.get(0).x,points.get(0).y,10.f,paint);
//                return;
//            }
//
//            for (int i = 1; i < n; i++)
//            {
//                final CPoint middle = new CPoint();
//                middle.x = points.get(i-1).x+(points.get(i).x-points.get(i-1).x)/2;
//                middle.y = points.get(i-1).y+(points.get(i).y-points.get(i-1).y)/2;
//                canvas.drawLine(points.get(i-1).x,points.get(i-1).y,middle.x,middle.y,paint);
//                canvas.drawLine(middle.x,middle.y,points.get(i).x,points.get(i).y,paint);
//
//
////                canvas.draw
//            }
//        }

        protected int size() { return points.size(); }

        private void changePaintColour()
        {
            switch (pType)
            {
                case NEW:
                    setPaintNew();
                    break;
                case NORMAL:
                    setPaintNormal();
                    break;
                case ERASE:
                    setPaintErase();
                    break;
            }
        }
    }
}
