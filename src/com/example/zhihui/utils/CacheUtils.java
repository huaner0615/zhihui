package com.example.zhihui.utils;

import android.content.Context;

public class CacheUtils {
	
	
	public static String getCache(String url,Context ctx){
		return PrefUtils.getString(url, null, ctx);
	}
	public static void setCache(String url,String json,Context ctx){
		PrefUtils.putString(url, json, ctx);
	}
}
