package com.kplike.library.util;

import android.content.Context;
import android.widget.Toast;

import com.kplike.library.base.KpFrame;

public class ToastUtil {
	public static void showLongMsg(Context context, String msg){
		if (null == context) return;
		showMsg(context, msg, Toast.LENGTH_LONG);
	}
	
	public static void showMsg(Context context, String msg, int duration){
		if (null == context) return;
		Toast.makeText(context, msg, duration).show();
	}
	
	public static void showShortMsg(Context context, String msg){
		if (null == context) return;
		showMsg(context, msg, Toast.LENGTH_SHORT);
	}

	public static void show(Context context, String msg){
		if (null == context) return;
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void show(String msg){
		Toast.makeText(KpFrame.context(), msg, Toast.LENGTH_SHORT).show();
	}
}
