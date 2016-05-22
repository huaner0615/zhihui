package com.example.zhihui.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.zhihui.base.BasePager;

public class SettingPager extends BasePager {

	public SettingPager(Activity a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initDate() {
		// TODO Auto-generated method stub
		tvTitle.setText("…Ë÷√");
		btnMenu.setVisibility(View.GONE);

		TextView view = new TextView(mActivity); 
		view.setText("…Ë÷√");
		view.setTextColor(Color.RED);
		view.setTextSize(20);
		view.setGravity(Gravity.CENTER);
		
		flContent.addView(view);
	}

}
