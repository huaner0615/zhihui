package com.example.zhihui.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.zhihui.base.BasePager;

public class SmartPager extends BasePager {

	public SmartPager(Activity a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initDate() {
		// TODO Auto-generated method stub
		tvTitle.setText("�ǻ۷���");
		TextView view = new TextView(mActivity); 
		view.setText("�ǻ۷���");
		view.setTextColor(Color.RED);
		view.setTextSize(20);
		view.setGravity(Gravity.CENTER);
		
		flContent.addView(view);
	}

}
