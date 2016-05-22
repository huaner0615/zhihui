package com.example.zhihui.base;

import com.example.zhihui.MainActivity;
import com.example.zhihui.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public abstract class BasePager {
	public Activity mActivity;
	public View mRootView;
	public ImageButton btnMenu;
	public FrameLayout flContent;
	public TextView tvTitle;
	public ImageButton btnDisplay;
	public BasePager(Activity a){
		mActivity=a;
		initView();
	}
	
	public void initView() {
		mRootView = View.inflate(mActivity, R.layout.base_pager, null);
		tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
		btnMenu = (ImageButton) mRootView.findViewById(R.id.btn_menu);
		flContent = (FrameLayout) mRootView.findViewById(R.id.fl_content);
		btnDisplay = (ImageButton)mRootView.findViewById(R.id.btn_display);
		
		btnMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				toggle();
			}
		});
	}
	protected void toggle() {
		// TODO Auto-generated method stub
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		slidingMenu.toggle();
	}

	public abstract void initDate();
}
