/*
 * @(#)CrashHandler.java		       Project: crash
 * Date:2014-5-26
 *
 * Copyright (c) 2014 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kplike.library.common.crash.log;

import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;



/**
 * 崩溃处理者。
 *
 */
public class CrashCatcher implements UncaughtExceptionHandler {
    private static final String LOG_TAG = CrashCatcher.class.getSimpleName();

    private static final CrashCatcher sHandler = new CrashCatcher();

    private CrashListener mListener;
    private File mLogFile;

    public static CrashCatcher getInstance() {
        return sHandler;
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        ex.printStackTrace();
        Log.e(LOG_TAG, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        Log.e(LOG_TAG, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    mListener.closeApp(thread, ex);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            LogWriter.writeLog(mLogFile, "CrashHandler", ex.getMessage(), ex);
        }catch (Exception e) {
            Log.w(LOG_TAG, e);
        }
        // 杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 初始化日志文件及CrashListener对象
     * 
     * @param logFile
     *            保存日志的文件
     * @param listener
     *            回调接口
     */
    public void init(File logFile, CrashListener listener) {
        mLogFile = logFile;
        mListener = listener;
    }
}
