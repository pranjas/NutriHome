/*
 * Copyright (c) 2016.
 * Original Author[Case insensitive]: Pranay Kumar Srivastava <pranjas (at) gmail.com>
 *
 *  You are free to re-distribute this code, modify it and make derivatives of this work
 *  however under all such cases the Original Author and Copyright holder must be
 *  accredited. The original author reserves the right to modify this software at anytime however the above clauses would still be applicable.
 *
 *  This software may not be used commercially without prior written agreement with the Original  Author and the Original Author above CANNOT be changed under any circumstances.
 *
 *  There's absolutely NO WARRANTY WHATSOEVER for using this software.
 *
 */

package com.example.pranay.nutrihome;

import android.os.SystemClock;
import android.util.Log;

/**
 * Created by pranay on 3/9/16.
 */
public class AppLogger {
    private static AppLogger instance = new AppLogger();
    private static boolean enabled = false;
    private AppLogger()
    {
    }

    public static AppLogger getInstance()
    {
        return instance;
    }

    public void debug(String msg)
    {
        if (enabled)
        Log.d(Logger.TAG_DEBUG +
                "[" + SystemClock.currentThreadTimeMillis() +"]", msg);
    }

    public void error(String msg)
    {
        if(enabled)
        Log.e(Logger.TAG_ERROR +
                "[" + SystemClock.currentThreadTimeMillis() + "]", msg);
    }

    public boolean setEnabled(boolean enable)
    {
        synchronized (instance) {
            boolean curEnabled = instance.enabled;
            instance.enabled = enable;
            return curEnabled;
        }
    }
    public boolean isEnabled()
    {
        return  instance.enabled;
    }
}
