package com.kplike.library.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MobileUtil {
	//获取接入点类型  
	public static Uri PREFERRED_APN_URI = Uri  
	            .parse("content://telephony/carriers/preferapn");// 获取当前设置的APN 

	/**
	 * 获取IP（ipv4）地址
	 * @return
	 */
	public static String getLocalIpAddress() {    
        try {    
            for (Enumeration<NetworkInterface> en = NetworkInterface    
                    .getNetworkInterfaces(); en.hasMoreElements();) {    
                NetworkInterface intf = en.nextElement();    
                for (Enumeration<InetAddress> enumIpAddr = intf    
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {    
                    InetAddress inetAddress = enumIpAddr.nextElement();    
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {    //!inetAddress.isLinkLocalAddress() Android4.0以上排除IPv6地址
                        return inetAddress.getHostAddress().toString();    
                    }    
                }    
            }    
        } catch (SocketException ex) {    
            Log.e("WifiPreference IpAddress", ex.toString());    
        }    
        return null;    
    }
	
	/**
	 * 获取MAC地址
	 * @return
	 */
	public static String getLocalMacAddress(Context context) {    
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);    
        WifiInfo info = wifi.getConnectionInfo();    
        return info.getMacAddress();    
	}
	
	/**
	 * 获取手机移动运营商
	 * 
	 * 需要加入权限<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
	 * @return
	 */
	public static String getProvidersName(TelephonyManager telephonyManager) {
	    String ProvidersName = null;
	    String IMSI = telephonyManager.getSubscriberId();
	    // IMSI号前面3位460是国家，紧接着后面2位00 02 07是中国移动，01是中国联通，03是中国电信。
	    if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
	        ProvidersName = "中国移动";
	    } else if (IMSI.startsWith("46001")) {
	        ProvidersName = "中国联通";
	    } else if (IMSI.startsWith("46003")) {
	        ProvidersName = "中国电信";
	    }
	    return ProvidersName;
	}
	
	/**
	 * 获取手机接入点名称
	 * @param context
	 * @return
	 */
	public static String getApnType(Context context) {
        String apntype = "nomatch";
        try {
            Cursor c = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
            c.moveToFirst();
            return c.getString(c.getColumnIndex("apn"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apntype;
    }
	
	/**
	 * Android获得UA信息
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getUserAgentString(Context ctx) {
	    WebView webview;
	    webview = new WebView(ctx);
	    webview.layout(0, 0, 0, 0);
	    WebSettings settings = webview.getSettings();
	    String ua = settings.getUserAgentString();
	    Log.i("UA", ua);
	    return ua;
	}
	
	/**
     * Wifi状态切换
     */
    public static void toggleWiFi(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wm.setWifiEnabled(!wm.isWifiEnabled()); //设置为false时，点击关闭
    }
    
    /**
     * 通过反射 切换移动数据
     * 注意相关权限添加
     */
    private void switchMobileData(Context context) {
        Object[] arg = null;
        try {
            boolean isMobileDataEnable = invokeMethod(context, "getMobileDataEnabled", arg); //获取移动数据状态
            if (!isMobileDataEnable) {
                invokeBooleanArgMethod(context, "setMobileDataEnabled", true); //开启移动数据
            } else {
                invokeBooleanArgMethod(context, "setMobileDataEnabled", false); //关闭移动数据
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public boolean invokeMethod(Context context, String methodName, Object[] arg) throws Exception {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class ownerClass = mConnectivityManager.getClass();
        Class[] argsClass = null;
        if (arg != null) {
            argsClass = new Class[1];
            argsClass[0] = arg.getClass();
        }
        Method method = ownerClass.getMethod(methodName, argsClass);
        Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);
        return isOpen;
    }
 
    public Object invokeBooleanArgMethod(Context context, String methodName, boolean value) throws Exception {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class ownerClass = mConnectivityManager.getClass();
        Class[] argsClass = new Class[1];
        argsClass[0] = boolean.class;
        Method method = ownerClass.getMethod(methodName, argsClass);
        return method.invoke(mConnectivityManager, value);
    }
    
    /**
     * 判断手机是否飞行模式
     * @param context
     * @return
     */
    public static boolean getAirplaneMode(Context context){
        int isAirplaneMode = Settings.System.getInt(context.getContentResolver(),
		Settings.System.AIRPLANE_MODE_ON, 0) ;
		return (isAirplaneMode == 1)?true:false;
    }
    
    /**
     * 设置手机飞行模式
     * @param context
     * @param enabling true:设置为飞行模式 false:取消飞行模式
     */
     public static void setAirplaneModeOn(Context context,boolean enabling) {
	     Settings.System.putInt(context.getContentResolver(),
	     Settings.System.AIRPLANE_MODE_ON,enabling ? 1 : 0);
	     Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
	     intent.putExtra("state", enabling);
	     context.sendBroadcast(intent);
     }
}
