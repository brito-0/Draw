package com.example.draw;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import android.os.Handler;

public abstract class MultipleClickListener implements View.OnClickListener
{
    private static final long MULTIPLE_CLICK_INTERVAL = 200L;

    private boolean isSingle, isDouble;
    private long lastClick;
    private final Handler handler;
    private final Runnable runnable;

    private int counter = 0;

    public MultipleClickListener()
    {
        lastClick = 0L;
        handler = new Handler();
        runnable = () ->
        {
            counter = 0;
            if (isSingle) onSingleClick();
            else if (isDouble) onDoubleClick();
        };
    }

    @Override
    public void onClick(View v)
    {
        if (counter == 1 && (SystemClock.elapsedRealtime()-lastClick) < MULTIPLE_CLICK_INTERVAL)
        {
            Log.d("CLICK_TEST","DOUBLE CLICK\t"+counter);

            ++counter;
            isSingle = false;
            isDouble = true;
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable,MULTIPLE_CLICK_INTERVAL);
            lastClick = SystemClock.elapsedRealtime();
            return;
        }
        else if (counter == 2 && (SystemClock.elapsedRealtime()-lastClick) < MULTIPLE_CLICK_INTERVAL)
        {
            Log.d("CLICK_TEST","TRIPLE CLICK\t"+counter);

            counter = 0;
            isSingle = false;
            isDouble = false;
            handler.removeCallbacks(runnable);
            onTripleClick();
            return;
        }

        Log.d("CLICK_TEST","SINGLE CLICK\t"+counter);

        ++counter;
        isSingle = true;
        isDouble = false;
        handler.postDelayed(runnable,MULTIPLE_CLICK_INTERVAL);
        lastClick = SystemClock.elapsedRealtime();
    }


    public abstract void onSingleClick();
    public abstract void onDoubleClick();
    public abstract void onTripleClick();
}
