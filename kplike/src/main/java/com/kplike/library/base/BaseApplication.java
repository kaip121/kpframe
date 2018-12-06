package com.kplike.library.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * @author kaiping
 * @date 2016/5/20 11:46
 */
public class BaseApplication extends Application {
    static Context _context;
    static Resources _resource;

    @Override
    public void onCreate() {
        super.onCreate();
        _context = getApplicationContext();
        _resource = _context.getResources();
        KpFrame.init(this);
    }

    public static synchronized BaseApplication context() {
        return (BaseApplication) _context;
    }

    public static Resources resources() {
        return _resource;
    }
}
