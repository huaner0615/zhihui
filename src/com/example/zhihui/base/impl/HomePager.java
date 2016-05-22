package com.example.zhihui.base.impl;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import com.example.zhihui.base.BasePager;

public class HomePager extends BasePager {

	public HomePager(Activity a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initDate() {
		// TODO Auto-generated method stub
		tvTitle.setText("ÖÇ»ÛÉÏº£");
		btnMenu.setVisibility(View.GONE);

		TextView view = new TextView(mActivity); 
		view.setText("Ê×Ò³");
		view.setTextColor(Color.RED);
		view.setTextSize(20);
		view.setGravity(Gravity.CENTER);
		
		flContent.addView(view);
	}

}
