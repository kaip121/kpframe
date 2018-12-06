package com.kplike.library.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by admin on 2018/12/6.
 */

public class KpFrame {

    static Context _context;

    public static synchronized void init(Context context){
        _context = context;
    }

    public static Context context(){
        return _context;
    }
}
