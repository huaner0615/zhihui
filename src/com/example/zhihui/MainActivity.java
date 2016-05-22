package com.example.zhihui;

import com.example.zhihui.base.impl.ContentMenuFragment;
import com.example.zhihui.base.impl.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.R.integer;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends SlidingFragmentActivity {

	private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
	private static final String TAG_CONTENT = "TAG_CONTENT";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去掉标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		//设置左侧侧边栏
		setBehindContentView(R.layout.activity_left);
		SlidingMenu slidingMenu = getSlidingMenu();
		//全屏触摸
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		WindowManager wManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		int height = wManager.getDefaultDisplay().getWidth();
		
		//像素
		slidingMenu.setBehindOffset((int) (height*0.625));
		initFragment();
	}
	/**
	初始化Fragment
	*/
	private void initFragment() {
		// TODO Auto-generated method stub
		FragmentManager frag = getSupportFragmentManager();
		FragmentTransaction ft = frag.beginTransaction();
		ft.replace(R.id.fl_left, new LeftMenuFragment(),TAG_LEFT_MENU);
		ft.replace(R.id.fl_main, new ContentMenuFragment(), TAG_CONTENT);
		ft.commit();
	}
	/*
	 * 获取侧边栏
	 * **/
	public LeftMenuFragment getLeftMenuFragment(){
		FragmentManager frag = getSupportFragmentManager();
		LeftMenuFragment  fragment = (LeftMenuFragment) frag.findFragmentByTag(TAG_LEFT_MENU);
		return fragment;
		
	}
	/***
	 * 获取主页对象
	 * */
	public ContentMenuFragment getContentMenuFragment(){
		FragmentManager frag = getSupportFragmentManager();
		ContentMenuFragment  fragment = (ContentMenuFragment) frag.findFragmentByTag(TAG_CONTENT);
		return fragment;
		
	}


}
