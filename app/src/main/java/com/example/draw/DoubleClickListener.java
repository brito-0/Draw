package com.example.draw;

import android.os.SystemClock;
import android.view.View;

import android.os.Handler;

public abstract class DoubleClickListener implements View.OnClickListener
{
    private static final long DOUBLE_CLICK_INTERVAL = 200L;

    private boolean isSingle;
    private long lastClick;
    private final Handler handler;
    private final Runnable runnable;

    public DoubleClickListener()
    {
        lastClick = 0L;
        handler = new Handler();
        runnable = () -> { if (isSingle) onSingleClick(); };
    }

    @Override
    public void onClick(View v)
    {
        if ((SystemClock.elapsedRealtime()-lastClick) < DOUBLE_CLICK_INTERVAL)
        {
            isSingle = false;
            handler.removeCallbacks(runnable);
            onDoubleClick();
            return;
        }

        isSingle = true;
        handler.postDelayed(runnable,DOUBLE_CLICK_INTERVAL);
        lastClick = SystemClock.elapsedRealtime();
    }


    public abstract void onSingleClick();
    public abstract void onDoubleClick();
}
