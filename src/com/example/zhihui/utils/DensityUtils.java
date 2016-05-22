package com.example.zhihui.utils;

import android.content.Context;

public class DensityUtils {
	public static int dp2dx(float dp,Context ctx){
		float density = ctx.getResources().getDisplayMetrics().density;
		int px = (int) (dp*density+0.5f);
		return px;
	}
	public static float dx2dp(int px,Context ctx){
		float density = ctx.getResources().getDisplayMetrics().density;
		float dp = px/density;
		return dp;
	}
}
