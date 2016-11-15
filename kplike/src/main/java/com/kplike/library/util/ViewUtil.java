package com.kplike.library.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class ViewUtil {
	/**
	 * 隐藏控件
	 * 
	 * @param activity
	 * @param resourdIds
	 */
	public static void hide(Activity activity, int... resourdIds) {
		for (int id : resourdIds) {
			View view = activity.findViewById(id);
			if (view != null) {
				view.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 隐藏控件
	 * @param parent
	 * @param resourdIds
     */
	public static void hide(View parent, int... resourdIds) {
		for (int id : resourdIds) {
			View view = parent.findViewById(id);
			if (view != null) {
				view.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 显示控件
	 * @param activity
	 * @param resourdIds
     */
	public static void show(Activity activity, int... resourdIds) {
		for (int id : resourdIds) {
			View view = activity.findViewById(id);
			if (view != null) {
				view.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 显示控件
	 * @param parent
	 * @param resourdIds
     */
	public static void show(View parent, int... resourdIds) {
		for (int id : resourdIds) {
			View view = parent.findViewById(id);
			if (view != null) {
				view.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 设置TextView控件内容
	 * @param activity
	 * @param textViewId
	 * @param text
     */
	public static void setText(Activity activity,int textViewId,String text){
		TextView textView = (TextView)activity.findViewById(textViewId);
		if (textView!=null){
			setText(textView,text);
		}
	}

	/**
	 * 设置TextView控件内容
	 * @param activity
	 * @param textViewId
	 * @param text
     */
	public static void setText(Activity activity,int textViewId,Spanned text){
		TextView textView = (TextView)activity.findViewById(textViewId);
		if (textView!=null){
			textView.setText(text);
		}
	}

	/**
	 * 设置TextView控件内容
	 * @param v
	 * @param textViewId
	 * @param text
     */
	public static void setText(View v,int textViewId,String text){
		TextView textView = (TextView)v.findViewById(textViewId);
		if (textView!=null){
			setText(textView,text);
		}
	}
	
	public static void setText(View v,int textViewId,Spanned text){
		TextView textView = (TextView)v.findViewById(textViewId);
		if (textView!=null){
			textView.setText(text);
		}
	}

	/**
	 * 设置TextView控件内容
	 * @param textView
	 * @param text
     */
	public static void setText(TextView textView,String text){
		textView.setText(text);
	}
	
	/**
	 * 清空TextView
	 * @param activity
	 * @param resourdIds
	 */
	public static void clearText(Activity activity, int... resourdIds) {
		for (int id : resourdIds) {
			setText(activity, id, "");
		}
	}

	/**
	 * 获取TextView控件内容
	 * @param view
	 * @return
     */
	public static String getText(TextView view) {
		return view.getText().toString();
	}

	/**
	 * 获取TextView控件内容
	 * @param activity
	 * @param viewId
     * @return
     */
	public static String getText(Activity activity, int viewId) {
		return getText((TextView) activity.findViewById(viewId));
	}

	/**
	 * 获取TextView控件内容
	 * @param view
	 * @param viewId
     * @return
     */
	public static String getText(View view, int viewId) {
		return getText((TextView) view.findViewById(viewId));
	}

	/**
	 * 设置图片
	 * @param activity
	 * @param resourdId
	 * @param imgId
     */
	public static void setImage(Activity activity, int resourdId, int imgId) {
		View view = activity.findViewById(resourdId);
		setImage(activity, view, imgId);
	}

	/**
	 * 设置图片
	 * @param activity
	 * @param view
	 * @param imgId
     */
	public static void setImage(Activity activity, View view, int imgId) {
		if (view != null) {
			view.setBackgroundResource(imgId);
		}
	}

	/**
	 * 向TextView控件中追加内容
	 * @param activity
	 * @param textViewId
	 * @param value
     */
	public static void append(Activity activity, int textViewId, String value) {
		TextView view = (TextView) activity.findViewById(textViewId);
		view.append(value);
	}

	/**
	 * 向TextView控件中替换内容
	 * @param activity
	 * @param textViewId
	 * @param values
     */
	public static void formatText(Activity activity, int textViewId, Object... values){
		TextView view = (TextView) activity.findViewById(textViewId);
		String text = view.getText().toString();
		view.setText(String.format(text, values));
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param activity
	 */
	public static void hideKeyboard(final Activity activity) {
		((InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(activity.getWindow().getDecorView()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/**
	 * 关闭输入法
	 * @param activity
	 */
	public static void hideInputMethod(final Activity activity){
		((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/**
	 * 从Assets中读取图片
	 */
	private static Bitmap getImageFromAssetsFile(Context context,String fileName) {
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	/**
	 * 切换控件的可见状态。
	 * @param activity
	 * @param resoureId
	 */
	public static void toggle(Activity activity, int resoureId){
		View v = activity.findViewById(resoureId);
		if (v.getVisibility() == View.VISIBLE){
			v.setVisibility(View.GONE);
		} else {
			v.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 切换控件的可见状态。
	 * @param parent
	 * @param resoureId
	 */
	public static void toggle(View parent, int resoureId){
		View v = parent.findViewById(resoureId);
		if (v.getVisibility() == View.VISIBLE){
			v.setVisibility(View.GONE);
		} else {
			v.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 隐藏控件
	 * @param views
	 */
	public static void hide(View... views) {
		for (View v : views) {
			v.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 显示控件
	 * @param views
	 */
	public static void show(View... views) {
		for (View v : views) {
			v.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 切换控件的可见状态。
	 * @param views
	 */
	public static void toggle(View... views){
		for (View v : views) {
			if (v.getVisibility() == View.VISIBLE){
				v.setVisibility(View.GONE);
			} else {
				v.setVisibility(View.VISIBLE);
			}
		}
	}
	
	
}
