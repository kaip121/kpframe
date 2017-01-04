package com.kplike.library.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.kplike.library.base.BaseApplication;

/**
 * preference工具类
 * 使用utils路径，存放在项目所在目录底下
 * @author nowy
 *
 */
public class PreferenceUtils extends SingletonUtils<PreferenceUtils>{

	private Context mContext;
	private SharedPreferences sharedpreferences;
	private Editor editor;

	@Override
	public PreferenceUtils newInstance() {
		return new PreferenceUtils(BaseApplication.context());
	}

	private PreferenceUtils(Context context){
		this.mContext = context;
		sharedpreferences = mContext.getSharedPreferences("utils",
				Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();
	}

	public void remove(String key){
		editor.remove(key);
		editor.commit();
	}

	public void clear(){
		editor.clear();
		editor.commit();
	}

	public <T> T getObject(String key,Class<T> cls){
		String value = getString(key);
		if (value == null) {
			return null;
		}
		Gson gson = new Gson();
		try {
			return gson.fromJson(value, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void putObject(String key,Object obj){
		if (obj == null) {
			putString(key, "");
			return;
		}
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		putString(key, json);
	}

	public void putString(String key,String value){
		editor.putString(key, value);
		editor.commit();
	}

	public String getString(String key){
		return sharedpreferences.getString(key, null);
	}

	public void putInt(String key,int value){
		editor.putInt(key, value);
		editor.commit();
	}

	public int getInt(String key){
		return sharedpreferences.getInt(key, 0);
	}

	public int getInt(String key,int defaultValue){
		return sharedpreferences.getInt(key, defaultValue);
	}

	public void putBoolean(String key,boolean value){
		editor.putBoolean(key, value);
		editor.commit();
	}

	public boolean getBoolean(String key){
		return sharedpreferences.getBoolean(key, false);
	}

	public boolean getBoolean(String key,boolean defaulValue){
		return sharedpreferences.getBoolean(key, defaulValue);
	}

	public void putLong(String key,long value){
		editor.putLong(key, value);
		editor.commit();
	}

	public long getLong(String key,long defaultValue){
		return sharedpreferences.getLong(key, defaultValue);
	}
}
