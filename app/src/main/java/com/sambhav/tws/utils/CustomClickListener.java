package com.sambhav.tws.utils;

import android.os.SystemClock;
import android.view.View;

public class CustomClickListener implements View.OnClickListener {

    private static final long DEBOUNCE_INTERVAL_DEFAULT = 500;
    private final long debounceInterval;
    private long lastClickTime;
    private final View.OnClickListener clickListener;

    public CustomClickListener(final View.OnClickListener clickListener) {
        this(clickListener, DEBOUNCE_INTERVAL_DEFAULT);
    }

    private CustomClickListener(final View.OnClickListener clickListener, final long debounceInterval) {
        this.clickListener = clickListener;
        this.debounceInterval = debounceInterval;
    }

    @Override
    public void onClick(final View v) {
        if ((SystemClock.elapsedRealtime() - lastClickTime) < debounceInterval) {
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();
        clickListener.onClick(v);
    }
}