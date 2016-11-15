package com.kplike.library.util;

import android.app.Activity;
import android.content.Intent;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.util.Map;

/**
 * 监听器帮助类
 */
public class ListenerHelper {
	public static void bindBackListener(final Activity activity,int... resourceIds){
		
		OnClickListener backClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.finish();
			}
		};
		bindOnCLickListener(activity, backClick, resourceIds);
	}
	
	/**
	 * 绑定Intent监听
	 * @param activity
	 * @param resourceId
	 * @param intent 标题
	 */
	public static void bindIntentListener(final Activity activity,int resourceId,final Intent intent){
		View view = activity.findViewById(resourceId);
		if (view !=null){
			view.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					activity.startActivity(intent);
				}
			});
		}
	}
	
	/**
	 * 绑定跳转监听
	 * @param activity
	 * @param resourceId 被监听的资源id
	 * @param toActivityClass 要跳转的activity目标
	 */
	public static void bindJumpListener(final Activity activity,int resourceId,final Class<? extends Activity> toActivityClass){
		Intent intent = new Intent(activity,toActivityClass);
		bindJumpListener(activity, resourceId, intent);
	}
	
	public static void bindJumpListenerWithData(final Activity activity,int resourceId,final Class<? extends Activity> toActivityClass, Map<String, String> bindData){
		Intent intent = new Intent(activity,toActivityClass);
		for(String key : bindData.keySet()) {
			intent.putExtra(key, bindData.get(key));
		}
		bindJumpListener(activity, resourceId, intent);
	}
	
	/**
	 * 绑定跳转监听
	 * @param activity
	 * @param resourceId 被监听的资源id
	 * @param intent 要跳转的activity目标
	 */
	public static void bindJumpListener(final Activity activity,int resourceId,final Intent intent){
		activity.findViewById(resourceId).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				activity.startActivity(intent);
			}
		});
	}
	
	
	/**
	 * 绑定单击事件
	 * @param activity
	 * @param clickListener
	 * @param resourceIds
	 */
	public static void bindOnCLickListener(final Activity activity,final OnClickListener clickListener,int... resourceIds){
		for (int resourceId:resourceIds) {
			activity.findViewById(resourceId).setOnClickListener(clickListener);
		}
	}
	
	/**
	 * 绑定单击事件
	 * @param parent
	 * @param clickListener
	 * @param resourceIds
	 */
	public static void bindOnCLickListener(final View parent,final OnClickListener clickListener,int... resourceIds){
		for (int resourceId:resourceIds) {
			parent.findViewById(resourceId).setOnClickListener(clickListener);
		}
	}
	
	/**
	 * 绑定单击事件
	 * @param activity
	 * @param resourceIds
	 */
	public static void bindOnCLickListener(final Activity activity, int... resourceIds){
		for (int resourceId:resourceIds) {
			activity.findViewById(resourceId).setOnClickListener((OnClickListener)activity);
		}
	}
	
	/**
	 * 绑定EditText文本输入事件
	 * @param activity
	 * @param resourceIds
	 */
	public static void bindOnTextChangedListener(final Activity activity, int... resourceIds){
		for (int resourceId:resourceIds) {
			((TextView)activity.findViewById(resourceId)).addTextChangedListener((TextWatcher)activity);
		}
	}
	
	/**
	 * 绑定EditText文本输入事件
	 * @param activity
	 * @param resourceIds
	 */
	public static void bindOnTextChangedListener(final Activity activity, final TextWatcher textWatcher, int... resourceIds){
		for (int resourceId:resourceIds) {
			((TextView)activity.findViewById(resourceId)).addTextChangedListener(textWatcher);
		}
	}
}
