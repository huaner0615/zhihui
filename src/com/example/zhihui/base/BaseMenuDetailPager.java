package com.example.zhihui.base;

import android.app.Activity;
import android.view.View;

public abstract class BaseMenuDetailPager {
	
	public Activity mActivity ;
	public View myRootView;
	public BaseMenuDetailPager(Activity activity){
		mActivity = activity;
		myRootView=initView();
	}
	
	public abstract View initView() ;
	public void initData() {
		// TODO Auto-generated method stub

	}
}
